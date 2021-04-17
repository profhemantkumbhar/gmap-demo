import { Injectable } from "@angular/core";
import { Observable, Subject } from "rxjs";
import { } from 'googlemaps';
import { Circle } from "../models";

@Injectable()
export class GoogleMapService {

    private circleDrawnSubject=new Subject<google.maps.Circle>();
    circleDrawnAction$ = this.circleDrawnSubject.asObservable();

    private currentPositionSubject = new Subject<google.maps.LatLngLiteral>();
    currentPositionAction$ = this.currentPositionSubject.asObservable();

    
    map: google.maps.Map | undefined;
    drawingManager!: google.maps.drawing.DrawingManager;
    selectedCircle: Circle | undefined;
    allCirclesOnMap: google.maps.Circle[] = [];
    currentPosition: google.maps.LatLngLiteral = { lat: 35.2271, lng: -80.8431 };

    constructor() {

    }

    /** initializes google map */
    initMap(position: google.maps.LatLngLiteral, nativeElement: Element): google.maps.Map {
        const mapProperties = {
            center: new google.maps.LatLng(position.lat, position.lng),
            zoom: 15,
            mapTypeId: google.maps.MapTypeId.ROADMAP
        };
        this.map = new google.maps.Map(nativeElement, mapProperties);
        return this.map;
    }

    /** sets up google drawing manager */
    setUpDrawingManager(map: google.maps.Map): google.maps.drawing.DrawingManager {
        this.drawingManager = new google.maps.drawing.DrawingManager({
            drawingMode: google.maps.drawing.OverlayType.MARKER,
            drawingControl: true,
            drawingControlOptions: {
                position: google.maps.ControlPosition.TOP_CENTER,
                drawingModes: [
                    google.maps.drawing.OverlayType.CIRCLE              
                ],
            },
            markerOptions: {
                icon:
                    "https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png",
            },
            circleOptions: {
                fillColor: "#ffff00",
                fillOpacity: 0.5,
                strokeWeight: 5,
                clickable: true,
                editable: true,
                zIndex: 1,
            },
        });
        this.drawingManager.setMap(map);
        google.maps.event.addListener(this.drawingManager, 'circlecomplete', (circle: google.maps.Circle) => {
            var center = circle.getCenter();
            var radius = circle.getRadius();
            console.log(`${center.lat()} ${center.lng()} ${radius}`);

            this.allCirclesOnMap.push(circle);

            console.log(this.allCirclesOnMap);
            // gets circle's center on center_changed event
            circle.addListener('center_changed', () => {
                console.log(`circle center-changed ${circle.getCenter()}`)
                if(this.selectedCircle!=undefined){
                    this.selectedCircle.latitude=circle.getCenter().lat();
                    this.selectedCircle.longitude=circle.getCenter().lng();
                }            
            });

            // gets circle's radius on radius_changed event
            circle.addListener('radius_changed', () => {
                console.log(`circle radius_changed ${circle.getRadius()}`)
                if(this.selectedCircle!=undefined){
                    this.selectedCircle.radius=circle.getRadius();
                }  
            });

            // removes the circle from map on dblclick
            circle.addListener('dblclick', () => {
                console.log(`circle dblclick`);
                circle.setVisible(false);
            });

            //call subject event
            this.circleDrawnSubject.next(circle);
        });

        google.maps.event.addListener(this.drawingManager, 'overlaycomplete', (event: google.maps.drawing.OverlayCompleteEvent) => {
            console.log(event.type);

            this.setDrawingMode(null);
        });

        // Configure the click listener.
        map.addListener("click", (mapsMouseEvent) => {
            console.log(JSON.stringify(mapsMouseEvent.latLng.toJSON(), null, 2));
        });


        return this.drawingManager;
    }

    setDrawingMode(overlayType: google.maps.drawing.OverlayType | null) {
        this.drawingManager.setDrawingMode(overlayType);
    }

    /** get current position from google navigator */
    getCurrentPosition(): void {
        navigator.geolocation.getCurrentPosition(
            (position: GeolocationPosition) => {

                const pos: google.maps.LatLngLiteral = {
                    lat: position.coords.latitude,
                    lng: position.coords.longitude,
                };

                this.currentPositionSubject.next(pos);
            },
            () => {
                console.log(`Error while getting current position`);
            }
        );
    }

    /** adds circle to map */
    addCircle(map: google.maps.Map, center: google.maps.LatLngLiteral, radius: number, displayText: string): google.maps.Circle {
        const circle = new google.maps.Circle({
            strokeColor: "#FF0000",
            strokeOpacity: 0.8,
            strokeWeight: 2,
            fillColor: "#FF0000",
            fillOpacity: 0.35,
            map,
            center: center,
            radius: radius
        });

        const infowindow = new google.maps.InfoWindow({
            content: displayText,
            position: center
        });
        infowindow.open(map, circle);
        circle.addListener("click", () => {
            infowindow.open(map, circle);
        });

        map.setCenter(circle.getCenter());
        this.allCirclesOnMap.push(circle);
        return circle;
    }

    /** adds marker to map */
    addMarker(location: google.maps.LatLngLiteral, map: google.maps.Map, displayText: string): google.maps.Marker {
        const marker = new google.maps.Marker({
            position: location,
            label: displayText,
            map: map,
            shape: { coords: [0, 0, 0], type: "circle" }
        });

        return marker;
    }

    /** clear all circles */
    clearAllCircles() {
        console.log(`Removing all ${this.allCirclesOnMap.length} shapes `)
        this.allCirclesOnMap.forEach((shape) => {
            shape.setMap(null);
        });
        this.allCirclesOnMap.splice(0, this.allCirclesOnMap.length);
      }
    
}