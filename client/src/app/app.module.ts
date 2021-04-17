import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { FormBuilder, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { SaveLocationComponent } from './save-location/save-location.component';
import { ListLocationsComponent } from './list-locations/list-locations.component';
import { HttpClientModule } from '@angular/common/http';
import { GoogleMapService } from './google-map/google-map.service';

@NgModule({
  declarations: [
    AppComponent,
    SaveLocationComponent,
    ListLocationsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule, HttpClientModule, ReactiveFormsModule
  ],
  providers: [GoogleMapService],
  bootstrap: [AppComponent]
})
export class AppModule { }
