package com.test.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;


import com.test.model.LocationRowMapper;

import com.test.model.LocationForm;

@Repository
public class LocationPagsDaoImpl implements LocationPagsDao{

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<LocationForm> getLocationsList() {
		String sqlQuery = "select * from  LocationInfo";
		Map<String, Object> params = new HashMap<String, Object>();

		return this.namedParameterJdbcTemplate.query(sqlQuery, params, new LocationRowMapper());
	}
	
	@Override
	public Integer saveLocation(LocationForm location) {
		if (location.getId() != null && location.getId() > 0) {
			String sqlQuery = "update locationinfo set name= :name " + ",longitude = :longitude, latitude = :latitude, "
					+ " radius= :radius where id=:id";
			SqlParameterSource params = new MapSqlParameterSource().addValue("id", location.getId())
					.addValue("name", location.getName()).addValue("longitude", location.getLongitude())
					.addValue("latitude", location.getLatitude()).addValue("radius", location.getRadius());
			return namedParameterJdbcTemplate.update(sqlQuery, params);
		} else {
			String sqlQuery = "insert into locationinfo( name,longitude,latitude,radius ) values ("
					+ ":name,:longitude,:latitude,:radius)";
			SqlParameterSource params = new MapSqlParameterSource()
					.addValue("name", location.getName()).addValue("longitude", location.getLongitude())
					.addValue("latitude", location.getLatitude()).addValue("radius", location.getRadius());
			KeyHolder keyHolder = new GeneratedKeyHolder();
			this.namedParameterJdbcTemplate.update(sqlQuery, params, keyHolder);
			return keyHolder.getKey().intValue();
		}
	}
}
