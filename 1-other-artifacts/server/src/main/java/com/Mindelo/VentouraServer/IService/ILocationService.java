package com.Mindelo.VentouraServer.IService;

import java.util.List;

import com.Mindelo.VentouraServer.Entity.City;
import com.Mindelo.VentouraServer.Entity.Country;

public interface ILocationService {
	
	public Country getCountryByName(String name);
	
	public City getCityByName(String name);
	
	public City getCityById(long id);
	
	public Country getCountryById(long id);
	
	public List<City> getAllCities();
	
	public List<Country> getAllCountries();
}
