package com.Mindelo.VentouraServer.IService;

import java.util.List;

import com.Mindelo.VentouraServer.Entity.TravellerSchedule;

public interface ITravellerScheduleService {
	
	public void saveAllTravellerSchdeules(List<TravellerSchedule> travellerSchedules);
	public void saveTravellerSchdeule(TravellerSchedule travellerSchedule);
	
	public void updateTravellerSchedule(TravellerSchedule travellerSchedule);
	
	public void deleteScheduleByTravellerId(long travellerId);
	public void deleteScheduleByScheduleId(long scheduleId);
	
	List<TravellerSchedule> getTravellerSchedulesByTravellerId(long travellerId);

	TravellerSchedule getTravellerScheduleById(long scheduleId);
}
