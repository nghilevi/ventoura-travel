package com.Mindelo.VentouraServer.IService;

import com.Mindelo.VentouraServer.Entity.GuideLastUpdatedLog;
import com.Mindelo.VentouraServer.Entity.TravellerLastUpdatedLog;

public interface ILastUpdatedService {
	
	public void saveTravellerLastUpdatedLog(TravellerLastUpdatedLog log);
	
	public void saveGuideLastUpdatedLog(GuideLastUpdatedLog log);

	public TravellerLastUpdatedLog getTravellerLastUpdatedLogByTravellerId(
			long travellerId);

	public void setTravellerProfileLastUpdated(long lastUpdated,
			long travellerId);

	public void setTravellerBookingsLastUpdated(long lastUpdated,
			long travellerId);

	public void setTravellerToursLastUpdated(long lastUpdated, long travellerId);

	public void setTravellerMatchesLastUpdated(long lastUpdated,
			long travellerId);

	public void setTravellerGalleryLastUpdated(long lastUpdated,
			long travellerId);

	public GuideLastUpdatedLog getGuideLastUpdatedLogByGuideId(long guideId);

	public void setGuideProfileLastUpdated(long lastUpdated, long guideId);

	public void setGuideBookingsLastUpdated(long lastUpdated, long guideId);

	public void setGuideMatchesLastUpdated(long lastUpdated, long guideId);

	public void setGuideGalleryLastUpdated(long lastUpdated, long guideId);
	
	public void setGuideReviewLastUpdated(long lastUpdated, long guideId);

}
