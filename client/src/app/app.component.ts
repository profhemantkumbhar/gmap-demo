import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { } from 'googlemaps';
import { GoogleMapService } from './google-map/google-map.service';
import { Circle } from './models';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, AfterViewInit {
  title = 'map-demo';
  @ViewChild('gmap') gmapElement: any;
  map: google.maps.Map | undefined;
  drawingManager!: google.maps.drawing.DrawingManager;

  allCirclesOnMap: Circle[] = [];
  currentPosition: google.maps.LatLngLiteral = { lat: 35.2271, lng: -80.8431 };

  constructor(public googleMapService: GoogleMapService) {

    // get current position to callback
    this.googleMapService.currentPositionAction$.subscribe(
      (currentPosition) => {
        this.currentPosition = currentPosition;
        this.map = this.googleMapService.initMap(this.currentPosition, this.gmapElement.nativeElement);
        
        this.drawingManager = this.googleMapService.setUpDrawingManager(this.map);
      }
    );
  }

  ngOnInit(): void {
  }

  ngAfterViewInit() {
    this.googleMapService.getCurrentPosition();

    this.googleMapService.circleDrawnAction$.subscribe((circle:google.maps.Circle)=>{
        this.googleMapService.selectedCircle =<Circle> {id:0, 
          name: '',
          latitude: circle.getCenter().lat(),
          longitude: circle.getCenter().lng(),
          radius: circle.getRadius()}
    });
  }


  
  onCircleSelected(circle: Circle) {
    this.googleMapService.clearAllCircles();
    let position: google.maps.LatLngLiteral = { lat: circle.latitude, lng: circle.longitude };
    if (this.map != undefined)
      this.googleMapService.addCircle(this.map, position, circle.radius, circle.name);

    this.googleMapService.selectedCircle=undefined;  
  }

  onClose(event:string)
  {
     this.googleMapService.selectedCircle=undefined;
     this.googleMapService.clearAllCircles();
  }
}
