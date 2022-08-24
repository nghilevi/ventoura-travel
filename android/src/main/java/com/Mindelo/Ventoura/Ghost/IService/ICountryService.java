package com.Mindelo.Ventoura.Ghost.IService;

import java.util.List;

import com.Mindelo.Ventoura.Entity.Country;

public interface ICountryService {

	/**
	 * 
	 * @param pattern
	 * @return a list of suggested countries
	 */
	public List<Country> getSuggestCountry(String pattern);
	
	/**
	 * 
	 * @param city name
	 */
	public Country getCountryByName(String countryName);
	
	public Country getCountryById(int id);
	
	public List<Country> getAllCountryAlphabetically();
	
	public Country getCountryByISO2(String iso2);
	
}
