package com.Mindelo.VentouraServer.IDAO;


import java.util.List;

import com.Mindelo.VentouraServer.Entity.TravellerGallery;


public interface ITravellerGalleryDAO  extends IDAO<TravellerGallery, Long>{
	
	public List<TravellerGallery> getTravellerGalleryByTravellerId(long travellerId);
	
	public void deleteTravellerGalleryByTravellerId(long travellerId);
	
	public TravellerGallery getTravellerPortalGallery(long travellerId);

	/**
	 * set the portal picture for a traveller
	 * @paremeter travellerId the gallery which will be set as portal
	 * @paremeter galleryId the id of the traveller
	 * 
	 */
	void setTravellerPortalByTravellerId(long travellerId, long galleryId);
}
