package com.Mindelo.VentouraServer.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.Mindelo.VentouraServer.Entity.TravellerSchedule;
import com.Mindelo.VentouraServer.IDAO.ITravellerScheduleDAO;
import com.Mindelo.VentouraServer.IService.ITravellerScheduleService;



@Service
@Component
public class TraverllerScheduleService implements ITravellerScheduleService{
	
	@Autowired
	private ITravellerScheduleDAO travellerScheduleDao;

	@Override
	public void saveAllTravellerSchdeules(
			List<TravellerSchedule> travellerSchedules) {
		travellerScheduleDao.saveAll(travellerSchedules);
	}

	@Override
	public void deleteScheduleByTravellerId(long travellerId) {
		travellerScheduleDao.deleteScheduleByTraverllerId(travellerId);
	}

	@Override
	public void deleteScheduleByScheduleId(long scheduleId) {
		travellerScheduleDao.deleteById(TravellerSchedule.class, scheduleId);
	}

	@Override
	public void saveTravellerSchdeule(TravellerSchedule travellerSchedule) {
		travellerScheduleDao.save(travellerSchedule);
	}

	@Override
	public void updateTravellerSchedule(TravellerSchedule travellerSchedule) {
		travellerScheduleDao.update(travellerSchedule);
	}

	@Override
	public List<TravellerSchedule> getTravellerSchedulesByTravellerId(
			long travellerId) {
		return travellerScheduleDao.findScheduleByTravellerId(travellerId);
	}

	@Override
	public TravellerSchedule getTravellerScheduleById(long scheduleId) {
		return travellerScheduleDao.findByID(TravellerSchedule.class, scheduleId);
	}

}
