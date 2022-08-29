package com.Mindelo.VentouraServer.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.Mindelo.VentouraServer.Entity.Guide;
import com.Mindelo.VentouraServer.Entity.GuideAttraction;
import com.Mindelo.VentouraServer.Entity.GuideGallery;
import com.Mindelo.VentouraServer.Entity.GuideHiddenTreasure;
import com.Mindelo.VentouraServer.Entity.GuideReview;
import com.Mindelo.VentouraServer.IDAO.IGuideAttractionDAO;
import com.Mindelo.VentouraServer.IDAO.IGuideDAO;
import com.Mindelo.VentouraServer.IDAO.IGuideGalleryDAO;
import com.Mindelo.VentouraServer.IDAO.IGuideHiddenTreasureDAO;
import com.Mindelo.VentouraServer.IDAO.IGuideReviewDAO;
import com.Mindelo.VentouraServer.IService.IGuideService;

@Service
@Component
public class GuideService implements IGuideService {

	@Autowired
	private IGuideDAO guideDao;
	@Autowired
	private IGuideAttractionDAO guideAttractionDao;
	@Autowired
	private IGuideHiddenTreasureDAO guideHiddenTreasureDao;
	@Autowired
	private IGuideReviewDAO guideReviewDao;

	@Autowired
	private IGuideGalleryDAO guideGalleryDao;

	@Override
	public void saveGuide(Guide guide) {
		guideDao.save(guide);

	}

	@Override
	public Guide getGuideByFacebookAccount(String facebookAccountName) {
		return guideDao.getGuideByFacebookAccountName(facebookAccountName);
	}

	@Override
	public void saveGuideGallery(List<GuideGallery> guideGalleries) {
		guideGalleryDao.saveAll(guideGalleries);
	}

	@Override
	public void saveGuideGallery(GuideGallery guideGallery) {
		guideGalleryDao.save(guideGallery);
	}

	@Override
	public void updateGuideProfile(Guide guide) {
		guideDao.update(guide);
	}

	@Override
	public void deactivateGuide(long guideId) {
		Guide guide = guideDao.findByID(Guide.class, guideId);
		guide.setActive(false);
		guideDao.update(guide);
	}

	@Override
	public void deleteGuideGallery(long galleryId) {
		guideGalleryDao.deleteById(GuideGallery.class, galleryId);
	}

	@Override
	public Guide getGuideById(long guideId) {
		return guideDao.findByID(Guide.class, guideId);
	}

	@Override
	public GuideGallery getGuideGalleryById(long galleryId) {
		return guideGalleryDao.findByID(GuideGallery.class, galleryId);
	}

	@Override
	public List<GuideGallery> getAllGalleryByGuideId(long guideId) {
		return guideGalleryDao.getGuideGalleryByGuideId(guideId);
	}

	@Override
	public GuideGallery getGuidePortalGalleryByGuideId(long guideId) {

		return guideGalleryDao.getGuidePortalGallery(guideId);

	}

	@Override
	public void setGuidePortalGallery(long guideId, long galleryId) {
		guideGalleryDao.setGuidePortalByGuideId(guideId, galleryId);
	}

	@Override
	public List<GuideAttraction> getGuideAttractions(long guideId) {
		return guideAttractionDao.findGuideAttractionsGuideId(guideId);
	}

	@Override
	public List<GuideHiddenTreasure> getGuideHiddenTreasures(long guideId) {
		return guideHiddenTreasureDao.findGuideHiddenTreasuresGuideId(guideId);
	}

	@Override
	public void saveGuideAttraction(GuideAttraction guideAttraction) {
		guideAttractionDao.save(guideAttraction);
	}

	@Override
	public void saveGuideHiddenTreasure(GuideHiddenTreasure guideHiddenTreasure) {
		guideHiddenTreasureDao.save(guideHiddenTreasure);
	}

	@Override
	public void deleteGuideAttraction(long guideAttractionId) {
		guideAttractionDao.deleteById(GuideAttraction.class, guideAttractionId);
	}

	@Override
	public void deleteGuideHiddenTreasure(long guideHiddenTreasureId) {
		guideHiddenTreasureDao.deleteById(GuideHiddenTreasure.class,
				guideHiddenTreasureId);
	}

	@Override
	public List<GuideReview> getGuideReviewByGuideId(long guideId) {
		return guideReviewDao.getGuideReviewByGuideId(guideId);
	}

	@Override
	public void saveGuideReview(GuideReview guideReview) {
		guideReviewDao.save(guideReview);
	}

	@Override
	public void deleteGuideReview(long guideReviewId) {
		guideReviewDao.deleteById(GuideReview.class, guideReviewId);
	}

	@Override
	public GuideAttraction getGuideAttractionById(long attractionId) {
		return guideAttractionDao.findByID(GuideAttraction.class, attractionId);
	}

	@Override
	public GuideHiddenTreasure getGuideHiddenTreasureById(long hiddenTreasureId) {
		return guideHiddenTreasureDao.findByID(GuideHiddenTreasure.class,
				hiddenTreasureId);
	}

	@Override
	public void deleteBatchGallery(List<Long> galleryIds) {
		for (long galleryId : galleryIds) {
			guideGalleryDao.deleteById(GuideGallery.class, galleryId);
		}
	}

	@Override
	public void deleteGuideById(long guideId) {
		guideDao.deleteById(Guide.class, guideId);
	}

	@Override
	public void deleteBatchGuideAttraction(List<Long> guideAttractionIds) {
		for (long attractionId : guideAttractionIds) {
			guideAttractionDao.deleteById(GuideAttraction.class, attractionId);
		}
	}

	@Override
	public void deleteBatchGuideHiddenTreasure(List<Long> guideHiddenTreasureids) {
		for (long treasureId : guideHiddenTreasureids) {
			guideHiddenTreasureDao.deleteById(GuideHiddenTreasure.class,
					treasureId);
		}

	}

}
