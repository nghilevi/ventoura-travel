package com.Mindelo.Ventoura.Ghost.IService;

import java.io.IOException;
import java.util.List;

import com.Mindelo.Ventoura.Entity.Guide;
import com.Mindelo.Ventoura.Entity.ImageProfile;
import com.Mindelo.Ventoura.JSONEntity.JSONGuideProfile;



public interface IGuideService {
	/**
	 * @return traveller's id saved in the server
	 * @param traveller an instance of guide
	 */
	public long uploadGuideProfile(Guide guide);
	
	public boolean updateGuideProfile(Guide guide);
	
	public boolean deactivateGuide(long guideId);

	/**
	 * try to get the guide profile from the server
	 * @return null if there is not changes on the server side. or error happened 
	 */
	public JSONGuideProfile getGuideProfileByIdFromServer(long guideId);

	/**
	 * try to get the guide profile from the local DB cache 
	 * @return null if there is no this profile cached
	 */
	public JSONGuideProfile getGuideProfileByIdFromDB(long guideId);
	

	/**
	 * create a single guide attraction
	 */
	public long uploadGuideAttraction(long guideId, String attractionName);
	/**
	 * create multiple attractions at the same time
	 */
	public boolean batchUploadGuideAttraction(long guideId, List<String> attractions);
	/**
	 * delete multiple attractions at the same time
	 */
	public boolean batchDeleteGuideAttraction(List<Long> attractions);

	public boolean deleteGuideAttraction(long guideAttractionId);
	
	public long uploadGuideHiddenTreasure(long guideId, String guideHiddenTreasureName);
	public boolean deleteGuideHiddenTreasure(long guideHiddenTreasureId);

	
	/*******************************************************/
	/***********Profile Gallery Management******************/
	/*******************************************************/
	
	/**
	 * download guide galley images from server and save all the images to DB
	 */
	public boolean loadGuideAllGalleryImagesFromServer(long guideId);
	/**
	 * get the portal image from the DB not from server
	 */
	public ImageProfile getGuidePortalImageFromDB(long guideId);
	
	/**
	 * get the galley images from the DB not from server
	 */
	public List<ImageProfile> getGuideGalleryImagesFromDB(long guideId);
	
}
