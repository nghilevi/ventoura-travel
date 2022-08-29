package com.Mindelo.VentouraServer.IService;

import java.util.List;

import com.Mindelo.VentouraServer.Entity.City;
import com.Mindelo.VentouraServer.Entity.Country;
import com.Mindelo.VentouraServer.Entity.Landmark;



public interface IMaintenanceService {
	
	public void saveLandMark(Landmark landmark);
	
	public List<Landmark> getAllLandmarks();
	
	public Landmark getLandmarkById(long id);
}
