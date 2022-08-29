package com.Mindelo.Ventoura.Ghost.IService;

import java.io.IOException;
import java.util.List;

import com.Mindelo.Ventoura.Entity.City;
import com.Mindelo.Ventoura.Entity.Image;

public interface ICityService {

	/**
	 * 
	 * @param pattern
	 * @return a list of suggested cities
	 */
	public List<City> getSuggestCity(String pattern);
	
	/**
	 * 
	 * @param city name
	 */
	public City getCityByName(String pattern);
	
	public City getCityById(int id);
	
	public List<City> getAllCityAlphabetically();
}
