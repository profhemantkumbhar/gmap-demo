package com.test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.test.model.LocationForm;
import com.test.service.GmapPagesService;

@RestController
public class GmapPagesController {
	@Autowired
	GmapPagesService  gmapPagesService;
	
	@GetMapping("/location")
	public String hello() {
		return "Welcome to Gmap Application";
	}

	@RequestMapping(value = { "/gmap/save" }, method = RequestMethod.POST)
	public ResponseEntity<Integer> saveStud(@RequestBody LocationForm location) {
		return ResponseEntity.ok(gmapPagesService.saveLocation(location));
	}

	@RequestMapping(value = { "/gmap/list" }, method = RequestMethod.GET)
	public ResponseEntity<List<LocationForm>> locatioList() {
		return ResponseEntity.ok(gmapPagesService.getLocationList());
	}
}
