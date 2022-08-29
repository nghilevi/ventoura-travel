package com.Mindelo.VentouraServer.IDAO;

import com.Mindelo.VentouraServer.Entity.Country;

public interface ICountryDAO extends IDAO<Country, Long>{
	public Country getCountryByname(String name);
}
