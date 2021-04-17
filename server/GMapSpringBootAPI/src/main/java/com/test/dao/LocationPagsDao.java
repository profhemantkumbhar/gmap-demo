package com.test.dao;

import java.util.List;

import com.test.model.LocationForm;

public interface LocationPagsDao {
	public List<LocationForm> getLocationsList();
	
	public Integer saveLocation(LocationForm location);
}