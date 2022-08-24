package com.Mindelo.VentouraServer.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.Mindelo.VentouraServer.Entity.GuideLastUpdatedLog;
import com.Mindelo.VentouraServer.Entity.TravellerLastUpdatedLog;
import com.Mindelo.VentouraServer.IDAO.IGuideLastUpdatedLogDAO;
import com.Mindelo.VentouraServer.IDAO.ITravellerLastUpdatedLogDAO;
import com.Mindelo.VentouraServer.IService.ILastUpdatedService;

@Service
@Component
public class LastUpdatedService implements ILastUpdatedService {

	@Autowired
	private IGuideLastUpdatedLogDAO gulDao;

	@Autowired
	private ITravellerLastUpdatedLogDAO tulDao;

	@Override
	public TravellerLastUpdatedLog getTravellerLastUpdatedLogByTravellerId(
			long travellerId) {
		return tulDao.getTravellerLastUpdatedByTravellerId(travellerId);
	}

	@Override
	public GuideLastUpdatedLog getGuideLastUpdatedLogByGuideId(long guideId) {
		return gulDao.getGuideLastUpdatedByGuideId(guideId);
	}

	@Override
	public void setTravellerProfileLastUpdated(long lastUpdated, long travellerId) {
		tulDao.setTravellerProfileLastUpdated(lastUpdated, travellerId);
	}

	@Override
	public void setTravellerBookingsLastUpdated(long lastUpdated, long travellerId) {
		tulDao.setTravellerBookingsLastUpdated(lastUpdated, travellerId);
	}

	@Override
	public void setTravellerToursLastUpdated(long lastUpdated, long travellerId) {
		tulDao.setTravellerToursLastUpdated(lastUpdated, travellerId);
	}

	@Override
	public void setTravellerMatchesLastUpdated(long lastUpdated, long travellerId) {
		tulDao.setTravellerMatchesLastUpdated(lastUpdated, travellerId);
	}
	
	@Override
	public void setTravellerGalleryLastUpdated(long lastUpdated,
			long travellerId) {
		tulDao.setTravellerGalleryLastUpdated(lastUpdated, travellerId);
	}

	@Override
	public void setGuideProfileLastUpdated(long lastUpdated, long guideId) {
		gulDao.setGuideProfileLastUpdated(lastUpdated, guideId);
	}

	@Override
	public void setGuideBookingsLastUpdated(long lastUpdated, long guideId) {
		gulDao.setGuideBookingsLastUpdated(lastUpdated, guideId);
	}

	@Override
	public void setGuideMatchesLastUpdated(long lastUpdated, long guideId) {
		gulDao.setGuideMatchesLastUpdated(lastUpdated, guideId);
	}

	@Override
	public void setGuideGalleryLastUpdated(long lastUpdated, long guideId) {
		gulDao.setGuideGalleryLastUpdated(lastUpdated, guideId);
	}

	@Override
	public void setGuideReviewLastUpdated(long lastUpdated, long guideId) {
		gulDao.setGuideReviewLastUpdated(lastUpdated, guideId);
	}

	@Override
	public void saveTravellerLastUpdatedLog(TravellerLastUpdatedLog log) {
		tulDao.save(log);
	}

	@Override
	public void saveGuideLastUpdatedLog(GuideLastUpdatedLog log) {
		gulDao.save(log);
	}

}
