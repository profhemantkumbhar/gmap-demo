package com.test.service;

import com.test.dao.*;
import com.test.model.LocationForm;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GmapPagesServiceImpl implements GmapPagesService{

	@Autowired
	LocationPagsDao  locationPagsDao;
	
	@Override
	public List<LocationForm>  getLocationList() 
	{
		return locationPagsDao.getLocationsList();
	}
	
	@Override
	public Integer saveLocation(LocationForm location)
	{
		return locationPagsDao.saveLocation(location);
	}
	
}
