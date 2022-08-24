package com.Mindelo.VentouraServer.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.Mindelo.VentouraServer.Entity.Guide;
import com.Mindelo.VentouraServer.Entity.GuideAttraction;
import com.Mindelo.VentouraServer.Entity.GuideHiddenTreasure;
import com.Mindelo.VentouraServer.Entity.Traveller;
import com.Mindelo.VentouraServer.Enum.UserRole;
import com.Mindelo.VentouraServer.IDAO.IGuideAttractionDAO;
import com.Mindelo.VentouraServer.IDAO.IGuideDAO;
import com.Mindelo.VentouraServer.IDAO.IGuideHiddenTreasureDAO;
import com.Mindelo.VentouraServer.IDAO.IGuideReviewDAO;
import com.Mindelo.VentouraServer.IDAO.ITGMatchDAO;
import com.Mindelo.VentouraServer.IDAO.ITTMatchDAO;
import com.Mindelo.VentouraServer.IDAO.ITravellerDAO;
import com.Mindelo.VentouraServer.IService.IVentouraService;
import com.Mindelo.VentouraServer.JSONEntity.JSONGuideAttraction;
import com.Mindelo.VentouraServer.JSONEntity.JSONGuideHiddenTreasure;
import com.Mindelo.VentouraServer.JSONEntity.JSONVentoura;
import com.Mindelo.VentouraServer.JSONEntity.JSONVentouraList;
import com.Mindelo.VentouraServer.Util.DateTimeUtil;

@Service
@Component
public class VentouraService implements IVentouraService {

	@Autowired
	ITravellerDAO travellerDao;
	@Autowired
	IGuideDAO guideDao;
	@Autowired
	IGuideReviewDAO guideReviewDao;
	@Autowired
	IGuideAttractionDAO attractionDao;
	@Autowired
	IGuideHiddenTreasureDAO hiddenTreasureDao;
	@Autowired
	ITTMatchDAO ttMatchDao;
	@Autowired
	ITGMatchDAO tgMatchDao;

	@Override
	public JSONVentouraList getTravellerVentoura(long travellerId) {

		// TODO.. Ventoura function is complicated
		JSONVentouraList jsonVentouraList = new JSONVentouraList();
		jsonVentouraList.setVentouraList(new ArrayList<JSONVentoura>());

		List<Traveller> travellers = travellerDao.findAll(Traveller.class);
		for (Traveller traveller : travellers) {
			if (traveller.getId() == travellerId) {
				continue;
			}
			JSONVentoura jsonVentoura = new JSONVentoura();
			jsonVentoura.setId(traveller.getId());
			if (traveller.getDateOfBirth() != null) {

			}
			jsonVentoura.setAge(DateTimeUtil.computeAge(traveller
					.getDateOfBirth()));
			jsonVentoura.setCountry(traveller.getCountry());
			jsonVentoura.setCity(traveller.getCity());
			jsonVentoura.setFirstname(traveller.getTravellerFirstname());
			jsonVentoura.setUserRole(UserRole.TRAVELLER);

			jsonVentouraList.getVentouraList().add(jsonVentoura);
		}
		List<Guide> guides = guideDao.findAll(Guide.class);
		for (Guide guide : guides) {

			JSONVentoura jsonVentoura = new JSONVentoura();
			jsonVentoura.setId(guide.getId());
			if (guide.getDateOfBirth() != null) {

			}
			jsonVentoura.setAvgReviewScore(guide.getAvgReviewScore());
			jsonVentoura.setNumberOfReviewers(guideReviewDao
					.getGuideReviewNumberByGuideId(guide.getId()));

			jsonVentoura.setTourType(guide.getTourType());
			jsonVentoura.setPaymentMethod(guide.getPaymentMethod());
			jsonVentoura.setTourPrice(guide.getTourPrice());
			jsonVentoura.setTourLength(guide.getTourLength());

			jsonVentoura
					.setAge(DateTimeUtil.computeAge(guide.getDateOfBirth()));
			jsonVentoura.setCountry(guide.getCountry());
			jsonVentoura.setCity(guide.getCity());
			jsonVentoura.setFirstname(guide.getGuideFirstname());
			jsonVentoura.setTextBiography(guide.getTextBiography());
			jsonVentoura.setUserRole(UserRole.GUIDE);

			/*
			 * hidden treasure and attractions
			 */
			List<JSONGuideAttraction> jsonGuideAttractions = new ArrayList<JSONGuideAttraction>();
			List<JSONGuideHiddenTreasure> jsonGuideHiddenTreasures = new ArrayList<JSONGuideHiddenTreasure>();

			for (GuideAttraction guideAttraction : attractionDao
					.findGuideAttractionsGuideId(guide.getId())) {
				JSONGuideAttraction jsonGuideAttraction = new JSONGuideAttraction();
				jsonGuideAttraction.setAttractionName(guideAttraction
						.getAttractionName());
				jsonGuideAttraction.setId(guideAttraction.getId());
				jsonGuideAttractions.add(jsonGuideAttraction);
			}

			for (GuideHiddenTreasure guidHiddenTreasure : hiddenTreasureDao
					.findGuideHiddenTreasuresGuideId(guide.getId())) {
				JSONGuideHiddenTreasure jsonGuideHiddenTreasure = new JSONGuideHiddenTreasure();
				jsonGuideHiddenTreasure
						.setHiddenTreasureName(guidHiddenTreasure
								.getHiddenTreasureName());
				jsonGuideHiddenTreasure.setId(guidHiddenTreasure.getId());

				jsonGuideHiddenTreasures.add(jsonGuideHiddenTreasure);
			}

			jsonVentoura.setAttractions(jsonGuideAttractions);
			jsonVentoura.setHiddenTreasures(jsonGuideHiddenTreasures);

			jsonVentouraList.getVentouraList().add(jsonVentoura);
		}

		return jsonVentouraList;
	}

	@Override
	public JSONVentouraList getGuideVentoura(long guideId) {
		// TODO.. Ventoura function is complicated
		JSONVentouraList jsonVentouraList = new JSONVentouraList();
		jsonVentouraList.setVentouraList(new ArrayList<JSONVentoura>());

		List<Traveller> travellers = travellerDao.findAll(Traveller.class);
		for (Traveller traveller : travellers) {
			JSONVentoura jsonVentoura = new JSONVentoura();
			jsonVentoura.setId(traveller.getId());
			if (traveller.getDateOfBirth() != null) {

			}
			jsonVentoura.setAge(DateTimeUtil.computeAge(traveller
					.getDateOfBirth()));
			jsonVentoura.setCountry(traveller.getCountry());
			jsonVentoura.setCity(traveller.getCity());
			jsonVentoura.setFirstname(traveller.getTravellerFirstname());
			jsonVentoura.setUserRole(UserRole.TRAVELLER);

			jsonVentouraList.getVentouraList().add(jsonVentoura);
		}

		return jsonVentouraList;
	}

	@Override
	public boolean travellerJudgeTraveller(long travellerId, boolean likeOrNot,
			long viewedTravellerId) {
		if (likeOrNot == true) {
			return ttMatchDao.travellerLikeTraveller(travellerId,
					viewedTravellerId);
		} else {
			ttMatchDao.travellerUnlikeTraveller(travellerId, viewedTravellerId);
			return false;
		}

	}

	@Override
	public boolean travellerJudgeGuide(long travellerId, boolean likeOrNot,
			long guideId) {
		if (likeOrNot == true) {
			return tgMatchDao.travellerLikeGuide(travellerId, guideId);
		} else {
			tgMatchDao.travellerUnlikeGuide(travellerId, guideId);
			return false;
		}
	}

	@Override
	public boolean guideJudgeTraveller(long guideId, boolean likeOrNot,
			long travellerId) {
		if (likeOrNot == true) {
			return tgMatchDao.guideLikeTraveller(guideId, travellerId);
		} else {
			tgMatchDao.guideUnlikeTraveller(guideId, travellerId);
			return false;
		}

	}

}
