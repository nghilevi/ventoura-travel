package com.Mindelo.VentouraServer.IDAO;

import com.Mindelo.VentouraServer.Entity.GuideLastUpdatedLog;

public interface IGuideLastUpdatedLogDAO extends
		IDAO<GuideLastUpdatedLog, Long> {
	/**
	 * given the guideid, return its last updated log
	 * 
	 * @param guideId
	 */
	public GuideLastUpdatedLog getGuideLastUpdatedByGuideId(long guideId);
	
	public void setGuideProfileLastUpdated(long lastUpdated, long guideId);
	public void setGuideBookingsLastUpdated(long lastUpdated, long guideId);
	public void setGuideMatchesLastUpdated(long lastUpdated, long guideId);
	public void setGuideGalleryLastUpdated(long lastUpdated, long guideId);
	public void setGuideReviewLastUpdated(long lastUpdated, long guideId);

}
