package com.Mindelo.Ventoura.Ghost.IService;

import java.util.List;

import com.Mindelo.Ventoura.Entity.ImageProfile;
import com.Mindelo.Ventoura.Entity.Traveller;
import com.Mindelo.Ventoura.JSONEntity.JSONTravellerProfile;

public interface ITravellerService {
	
	/**
	 * @return traveller's id saved in the server
	 * @param traveller an instance of traveller
	 */
	public long uploadTraverllerProfile(Traveller traveller);
	
	/**
	 * update the traveller profile 
	 */
	public boolean updateTravellerProfile(Traveller traveller);


	//public long deleteTraverllerProfile(Traveller traveller);
	
	/**
	 * Mark the traveller as deactivated 
	 */
	public boolean deactivateTraverller(long travellerId);
	
	/**
	 * try to get the traveller profile from the server
	 * @return null if there is not changes on the server side. or error happened 
	 */
	public JSONTravellerProfile getTravellerProfileByIdFromServer(long travellerId);
	
	/**
	 * try to get the traveller profile from the local DB cache 
	 * @return null if there is no this profile cached
	 */
	public JSONTravellerProfile getTravellerProfileByIdFromDB(long travellerId);
	
	
	
	
	
	
	/*******************************************************/
	/***********Profile Gallery Management******************/
	/*******************************************************/
	
	/**
	 * download traveller galley images from server and save all the images to DB
	 */
	public boolean loadTravellerAllGalleryImagesFromServer(long travellerId);
	/**
	 * get the portal image from the DB not from server
	 */
	public ImageProfile getTravellerPortalImageFromDB(long travellerId);
	
	/**
	 * get the galley images from the DB not from server
	 */
	public List<ImageProfile> getTravellerGalleryImagesFromDB(long travellerId);
	
}
