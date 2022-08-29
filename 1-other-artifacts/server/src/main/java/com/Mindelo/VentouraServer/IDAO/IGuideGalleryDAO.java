package com.Mindelo.VentouraServer.IDAO;


import java.util.List;

import com.Mindelo.VentouraServer.Entity.GuideGallery;

public interface IGuideGalleryDAO  extends IDAO<GuideGallery, Long>{
	
	public void deleteGuideGalleryByGuideId(long guideId);
	
	public List<GuideGallery> getGuideGalleryByGuideId(long guideId);
	
	public GuideGallery getGuidePortalGallery(long guideId);

	/**
	 * set the portal picture for a guide
	 * @paremeter galleryId the gallery which will be set as portal
	 * @paremeter guideId the id of the guide
	 * 
	 */
	void setGuidePortalByGuideId(long guideId, long galleryId);
}
