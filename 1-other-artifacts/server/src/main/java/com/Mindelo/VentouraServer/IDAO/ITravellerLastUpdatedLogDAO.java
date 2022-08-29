package com.Mindelo.VentouraServer.IDAO;

import com.Mindelo.VentouraServer.Entity.TravellerLastUpdatedLog;

public interface ITravellerLastUpdatedLogDAO extends
		IDAO<TravellerLastUpdatedLog, Long> {

	/**
	 * given the travellerid, return its last updated log
	 * @param travellerId  
	 */
	public TravellerLastUpdatedLog getTravellerLastUpdatedByTravellerId(long travellerId);
	
	public void setTravellerProfileLastUpdated(long lastUpdated, long travellerId);
	public void setTravellerBookingsLastUpdated(long lastUpdated, long travellerId);
	public void setTravellerToursLastUpdated(long lastUpdated, long travellerId);
	public void setTravellerMatchesLastUpdated(long lastUpdated, long travellerId);
	public void setTravellerGalleryLastUpdated(long lastUpdated, long travellerId);
	
}
