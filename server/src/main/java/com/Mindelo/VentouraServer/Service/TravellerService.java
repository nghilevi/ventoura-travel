package com.Mindelo.VentouraServer.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.Mindelo.VentouraServer.Entity.GuideGallery;
import com.Mindelo.VentouraServer.Entity.Traveller;
import com.Mindelo.VentouraServer.Entity.TravellerGallery;
import com.Mindelo.VentouraServer.IDAO.ITravellerDAO;
import com.Mindelo.VentouraServer.IDAO.ITravellerGalleryDAO;
import com.Mindelo.VentouraServer.IService.ITravellerService;

@Service
@Component
public class TravellerService implements ITravellerService {

	@Autowired
	private ITravellerDAO travellerDao;
	@Autowired
	private ITravellerGalleryDAO travellerGalleryDao;

	// save methods

	@Override
	public void saveTraveller(Traveller traveller) {

		travellerDao.save(traveller);
	}

	@Override
	public void saveTravellerGallery(List<TravellerGallery> travellerGalleries) {
		travellerGalleryDao.saveAll(travellerGalleries);
	}

	@Override
	public void saveTravellerGallery(TravellerGallery travellerGallery) {
		travellerGalleryDao.save(travellerGallery);
	}

	// update methods

	@Override
	public void updateTraverllerProfile(Traveller traveller) {
		travellerDao.update(traveller);
	}

	@Override
	public void deactivateTraverller(long travellerId) {
		Traveller traveller = travellerDao.findByID(Traveller.class,
				travellerId);
		traveller.setActive(false);
		travellerDao.update(traveller);
	}

	@Override
	public void deleteTravellerGallery(long galleryId) {
		travellerGalleryDao.deleteById(TravellerGallery.class, galleryId);
	}

	// retrieve methods
	@Override
	public Traveller getTravallerByFacebookAccount(String facebookAccountName) {
		return travellerDao
				.getTravellerByFacebookAccountName(facebookAccountName);
	}

	@Override
	public Traveller getTravellerById(long travellerId) {
		return travellerDao.findByID(Traveller.class, travellerId);
	}

	@Override
	public TravellerGallery getTravellerGalleryById(long galleryId) {
		return travellerGalleryDao.findByID(TravellerGallery.class, galleryId);
	}

	@Override
	public List<TravellerGallery> getAllGalleryByTravellerId(long travellerId) {
		return travellerGalleryDao
				.getTravellerGalleryByTravellerId(travellerId);
	}

	@Override
	public TravellerGallery getTravellerPortalGalleryByTravellerId(
			long travellerId) {
		return travellerGalleryDao.getTravellerPortalGallery(travellerId);
	}

	@Override
	public void setTravellerPortalGallery(long travellerId, long galleryId) {
		travellerGalleryDao.setTravellerPortalByTravellerId(travellerId,
				galleryId);
	}

	@Override
	public void deleteBatchGallery(List<Long> galleryIds) {
		for (long galleryId : galleryIds) {
			travellerGalleryDao.deleteById(TravellerGallery.class, galleryId);
		}
	}

	@Override
	public void deleteTravellerById(long travellerId) {
		travellerDao.deleteById(Traveller.class, travellerId);
	}

}
