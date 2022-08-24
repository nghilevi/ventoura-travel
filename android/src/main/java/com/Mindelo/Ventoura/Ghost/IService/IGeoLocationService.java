package com.Mindelo.Ventoura.Ghost.IService;

public interface IGeoLocationService {

	/**
	 * get the user's current country
	 */
	public String getUserCurrentCountry();
	
	/**
	 * get the user's country code
	 */
	public String getUserCurrentCountryCode();

}
