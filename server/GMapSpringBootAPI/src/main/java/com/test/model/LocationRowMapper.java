package com.test.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class LocationRowMapper 	implements RowMapper<LocationForm>{
	public LocationForm mapRow(ResultSet rs, int rowNum) throws SQLException {
		LocationForm location = new LocationForm();
		location.setId(rs.getInt("id"));
		location.setName(rs.getString("name"));
		location.setLongitude(rs.getDouble("longitude"));
		location.setLatitude(rs.getDouble("latitude"));
		location.setRadius(rs.getDouble("radius"));
		
 		return location;  
	}
}
