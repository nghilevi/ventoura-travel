package com.Mindelo.VentouraServer.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.Mindelo.VentouraServer.Entity.City;
import com.Mindelo.VentouraServer.Entity.Country;
import com.Mindelo.VentouraServer.IDAO.ICityDAO;
import com.Mindelo.VentouraServer.IDAO.ICountryDAO;
import com.Mindelo.VentouraServer.IService.ILocationService;



@Service
@Component
public class LocationService implements ILocationService{
	
	@Autowired
	private ICityDAO cityDao;
	
	@Autowired
	private ICountryDAO countryDao;
	
	@Override
	public Country getCountryByName(String name) {
		return countryDao.getCountryByname(name);
	}

	@Override
	public City getCityByName(String name) {
		return cityDao.getCityByname(name);
	}

	@Override
	public List<City> getAllCities() {
		return cityDao.findAll(City.class);
	}

	@Override
	public List<Country> getAllCountries() {
		return countryDao.findAll(Country.class);
	}

	@Override
	public City getCityById(long id) {
		return cityDao.findByID(City.class, id);
	}

	@Override
	public Country getCountryById(long id) {
		return countryDao.findByID(Country.class, id);
	}
}
