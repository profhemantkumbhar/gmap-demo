package com.test.service;

import java.util.List;

import com.test.model.LocationForm;

public interface GmapPagesService {
	public List<LocationForm>  getLocationList() ;
	
	public Integer saveLocation(LocationForm location);
}
