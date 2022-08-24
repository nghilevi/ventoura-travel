package com.Mindelo.VentouraServer.IDAO;

import java.util.List;

import com.Mindelo.VentouraServer.Entity.TravellerSchedule;

public interface ITravellerScheduleDAO extends IDAO<TravellerSchedule, Long>{
	List<TravellerSchedule> findScheduleByTravellerId(long travellerId);
	
	void deleteScheduleByTraverllerId(long travellerId);
}
