package com.Mindelo.VentouraServer.IService;

import java.util.List;

import com.Mindelo.VentouraServer.Entity.Traveller;
import com.Mindelo.VentouraServer.Entity.TravellerGallery;

public interface ITravellerService {
	
	/*
	 * save actions
	 */
	public void saveTraveller(Traveller traveller);
	public void saveTravellerGallery(List<TravellerGallery> travellerGalleries);
	public void saveTravellerGallery(TravellerGallery travellerGallery);

	
	/*
	 * update actions
	 */
	public void updateTraverllerProfile(Traveller traveller);
	public void setTravellerPortalGallery(long travellerId, long galleryId);
	public void deactivateTraverller(long travellerId);

	
	/*
	 * retrieve methods
	 */
	
	public Traveller getTravellerById(long travellerId);
	public TravellerGallery getTravellerGalleryById(long galleryId);
	public TravellerGallery getTravellerPortalGalleryByTravellerId(
			long travellerid);
	public List<TravellerGallery> getAllGalleryByTravellerId(long travellerId);
	public Traveller getTravallerByFacebookAccount(String facebookAccountName);
	
	
	/*
	 * delete actions
	 */
	public void deleteTravellerById(long travellerId);
	public void deleteTravellerGallery(long galleryId);
	public void deleteBatchGallery(List<Long> galleryIds);
	
	

}
