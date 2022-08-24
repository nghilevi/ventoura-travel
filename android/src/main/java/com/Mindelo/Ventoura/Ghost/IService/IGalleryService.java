package com.Mindelo.Ventoura.Ghost.IService;

import java.util.List;

import com.Mindelo.Ventoura.Entity.ImageProfile;

import android.graphics.Bitmap;


/**
 * @author CTO
 * the gallery service is normally used by not-profile activities, like when to load a ventoura's gallery image
 */
public interface IGalleryService {
	
	/**
	 * @return the portal filename of the traveller, all the files are saved in memory 
	 */
	public List<ImageProfile> loadTravellerAllGalleryImagesIntoList(long travellerId);
	public ImageProfile getTravellerPortalImage(long travellerId);
	
	public long uploadSingleTravellerGallery(long travellerId, byte[] imageContent);
	public boolean deleteTravellerGallery(long travellerId, long galleryId);
	public boolean deleteBatchTravellerGallery(long travellerId, List<Long> galleryIds);
	
	public boolean setTravellerPortalImage(long travellerId, long galleryId);
	
	/**
	 * @return the portal filename of the guide, all the files are saved in memory 
	 */
	public List<ImageProfile> loadGuideAllGalleryImagesIntoList(long guideId);
	
	/**
	 * get a larger portal image for this guide
	 */
	public ImageProfile getGuidePortalImage(long guideId);
	
	public long uploadSingleGuideGallery(long guideId,  byte[] imageContent);
	public boolean deleteGuideGallery(long guideId, long galleryId);
	public boolean deleteBatchGuideGallery(long guideId, List<Long> galleryIds);
	public boolean setGuidePortalImage(long travellerId, long galleryId);
}
