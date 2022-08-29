package com.Mindelo.VentouraServer.IService;

import java.util.List;

import com.Mindelo.VentouraServer.Entity.Guide;
import com.Mindelo.VentouraServer.Entity.GuideAttraction;
import com.Mindelo.VentouraServer.Entity.GuideHiddenTreasure;
import com.Mindelo.VentouraServer.Entity.GuideGallery;
import com.Mindelo.VentouraServer.Entity.GuideReview;

public interface IGuideService {
	
	/*
	 * save actions
	 */

	public void saveGuide(Guide guide);
	public void saveGuideGallery(List<GuideGallery> guideGalleries);
	public void saveGuideGallery(GuideGallery guideGallery);
	public void saveGuideAttraction(GuideAttraction guideAttraction);
	public void saveGuideHiddenTreasure(GuideHiddenTreasure guideHiddenTreasure);
	public void saveGuideReview(GuideReview guideReview);

	/*
	 * delete actions
	 */
	public void deactivateGuide(long guideId);
	public void deleteGuideById(long guideId);
	public void deleteGuideGallery(long galleryId);
	public void deleteBatchGallery(List<Long> galleryIds);
	public void deleteGuideAttraction(long guideAttractionId);
	public void deleteBatchGuideAttraction(List<Long> guideAttractionIds);
	public void deleteGuideHiddenTreasure(long guideHiddenTreasureid);
	public void deleteBatchGuideHiddenTreasure(List<Long> guideHiddenTreasureids);
	public void deleteGuideReview(long guideReviewId);
	

	/*
	 * retrieve  actions
	 */
	public Guide getGuideByFacebookAccount(String facebookAccountName);
	public Guide getGuideById(long guideId);
	public GuideGallery getGuideGalleryById(long galleryId);
	public GuideGallery getGuidePortalGalleryByGuideId(long guideId);
	public List<GuideGallery> getAllGalleryByGuideId(long guideId);
	public List<GuideAttraction> getGuideAttractions(long guideId);
	public List<GuideHiddenTreasure> getGuideHiddenTreasures(long guideId);
	public GuideAttraction getGuideAttractionById(long attractionId);
	public GuideHiddenTreasure getGuideHiddenTreasureById(long hiddenTreasureId);
	public List<GuideReview> getGuideReviewByGuideId(long guideId);

	
	/*
	 * update  actions
	 */
	public void updateGuideProfile(Guide guide);
	public void setGuidePortalGallery(long guideId, long galleryId);



}
