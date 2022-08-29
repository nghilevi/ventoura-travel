package com.Mindelo.VentouraServer.IDAO;

import com.Mindelo.VentouraServer.Entity.City;


public interface ICityDAO extends IDAO<City, Long>{
	public City getCityByname(String name);
}
