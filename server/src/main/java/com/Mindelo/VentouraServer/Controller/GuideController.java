package com.Mindelo.VentouraServer.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.jivesoftware.smack.XMPPConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Mindelo.VentouraServer.Constant.ConfigurationConstant;
import com.Mindelo.VentouraServer.Constant.HttpFieldConstant;
import com.Mindelo.VentouraServer.Constant.IMConstant;
import com.Mindelo.VentouraServer.Entity.Guide;
import com.Mindelo.VentouraServer.Entity.GuideAttraction;
import com.Mindelo.VentouraServer.Entity.GuideGallery;
import com.Mindelo.VentouraServer.Entity.GuideHiddenTreasure;
import com.Mindelo.VentouraServer.Entity.GuideLastUpdatedLog;
import com.Mindelo.VentouraServer.Enum.Gender;
import com.Mindelo.VentouraServer.Enum.PaymentMethod;
import com.Mindelo.VentouraServer.IService.IGuideService;
import com.Mindelo.VentouraServer.IService.IIMService;
import com.Mindelo.VentouraServer.IService.ILastUpdatedService;
import com.Mindelo.VentouraServer.IService.ILocationService;
import com.Mindelo.VentouraServer.JSONEntity.JSONGuideAttraction;
import com.Mindelo.VentouraServer.JSONEntity.JSONGuideHiddenTreasure;
import com.Mindelo.VentouraServer.JSONEntity.JSONGuideProfile;
import com.Mindelo.VentouraServer.JSONEntity.JSONKeyValueMessage;
import com.Mindelo.VentouraServer.Main.IMManager;
import com.Mindelo.VentouraServer.Util.DateTimeUtil;

@Controller
@RequestMapping(value = "/guide")
public class GuideController {

	@Autowired
	private IGuideService guideService;
	@Autowired
	private ILocationService locationService;
	@Autowired
	private ILastUpdatedService luService;
	@Autowired
	private IIMService imService;

	private XMPPConnection connection;

	@RequestMapping(value = "/getGuideProfile/{guideId}", method = RequestMethod.GET)
	public @ResponseBody JSONGuideProfile getGuideProfile(
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable("guideId") final long guideId) {

		JSONGuideProfile jsonGuideProfile = new JSONGuideProfile();

		/*
		 * no changes in the server
		 */
		GuideLastUpdatedLog guideLastUpdatedLog = luService
				.getGuideLastUpdatedLogByGuideId(guideId);
		try {
			String modifiedSince = request
					.getHeader(ConfigurationConstant.HTTP_HEADER_MODIFIED_SINCE);
			if (modifiedSince != null) {

				long lastUpdatedTime = DateTimeUtil.fromStringToDate_GMT(
						modifiedSince).getTime();
				if (guideLastUpdatedLog != null
						&& lastUpdatedTime == guideLastUpdatedLog
								.getProfileLastUpdated()) {
					response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
					return null;
				}
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}

		/*
		 * some changes happened in the server
		 */
		response.setStatus(HttpServletResponse.SC_ACCEPTED);
		if (guideLastUpdatedLog != null) {
			Date date_modified = new Date();
			date_modified.setTime(guideLastUpdatedLog.getProfileLastUpdated());
			response.setHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED,
					DateTimeUtil.fromDateToString_GMT(date_modified));
		}

		/*
		 * return the databody
		 */
		Guide guide = guideService.getGuideById(guideId);
		List<GuideAttraction> guideAttractions = guideService
				.getGuideAttractions(guideId);
		List<GuideHiddenTreasure> guideHiddenTreasures = guideService
				.getGuideHiddenTreasures(guideId);

		if (guide != null) {
			jsonGuideProfile.setId(guideId);
			jsonGuideProfile.setCity(guide.getCity());
			jsonGuideProfile.setCountry(guide.getCountry());
			jsonGuideProfile.setDateOfBirth(DateTimeUtil.fromDateToString(guide
					.getDateOfBirth()));

			jsonGuideProfile.setGender(guide.getGender());
			jsonGuideProfile.setTextBiography(guide.getTextBiography());
			jsonGuideProfile.setGuideFirstname(guide.getGuideFirstname());
			jsonGuideProfile.setGuideLastname(guide.getGuideLastname());
			jsonGuideProfile.setFacebookAccountName(guide
					.getFacebookAccountName());

			jsonGuideProfile.setPremium(guide.isPremium());
			jsonGuideProfile.setMoneyType(0);
			jsonGuideProfile.setAvgReviewScore(guide.getAvgReviewScore());
			jsonGuideProfile.setTourLength(guide.getTourLength());
			jsonGuideProfile.setTourPrice(guide.getTourPrice());
			jsonGuideProfile.setTourType(guide.getTourType());

			List<JSONGuideAttraction> jsonGuideAttractions = new ArrayList<JSONGuideAttraction>();
			List<JSONGuideHiddenTreasure> jsonGuideHiddenTreasures = new ArrayList<JSONGuideHiddenTreasure>();

			for (GuideAttraction guideAttraction : guideAttractions) {
				JSONGuideAttraction jsonGuideAttraction = new JSONGuideAttraction();
				jsonGuideAttraction.setAttractionName(guideAttraction
						.getAttractionName());
				jsonGuideAttraction.setId(guideAttraction.getId());
				jsonGuideAttractions.add(jsonGuideAttraction);
			}

			for (GuideHiddenTreasure guidHiddenTreasure : guideHiddenTreasures) {
				JSONGuideHiddenTreasure jsonGuideHiddenTreasure = new JSONGuideHiddenTreasure();
				jsonGuideHiddenTreasure
						.setHiddenTreasureName(guidHiddenTreasure
								.getHiddenTreasureName());
				jsonGuideHiddenTreasure.setId(guidHiddenTreasure.getId());

				jsonGuideHiddenTreasures.add(jsonGuideHiddenTreasure);
			}
			jsonGuideProfile.setAttractions(jsonGuideAttractions);
			jsonGuideProfile.setHiddenTreasures(jsonGuideHiddenTreasures);
		}

		return jsonGuideProfile;
	}


	@RequestMapping(value = "/createNewGuide", method = RequestMethod.POST)
	public @ResponseBody JSONKeyValueMessage<String, String> createNewGuide(
			HttpServletRequest request, HttpServletResponse response,
			Model model) {

		Guide guide = new Guide();
		List<GuideGallery> guideGalleries = new ArrayList<GuideGallery>();

		try {
			guide = loadGuideFormField(guideGalleries, request);

			if (guideService.getGuideByFacebookAccount(guide
					.getFacebookAccountName()) != null) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
				return null;
			}

			// save the new guide
			guideService.saveGuide(guide);

			for (GuideGallery gallery : guideGalleries) {
				gallery.setGuideId(guide.getId());
			}

			// save the gallery
			guideService.saveGuideGallery(guideGalleries);
			response.setStatus(HttpServletResponse.SC_ACCEPTED);

			/*
			 * create the IM account for the traveller
			 */
			connection = IMManager.getXMPPConnection();
			if (!imService.createAccount(connection, "g_" + guide.getId(),
					IMConstant.VENTOURA_SERVER_IM_USER_PASSWORD)) {
				guideService.deleteGuideById(guide.getId());
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return null;
			}

			/*
			 * return value to the client
			 */
			JSONKeyValueMessage<String, String> message = new JSONKeyValueMessage<String, String>();
			message.setKey(HttpFieldConstant.SERVER_GUIDE_ID);
			message.setValue(guide.getId() + "");

			/*
			 * create the update log for this guide
			 */
			Date time_modified = new Date();
			response.setHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED,
					DateTimeUtil.fromDateToString_GMT(time_modified));
			GuideLastUpdatedLog log = new GuideLastUpdatedLog(
					time_modified.getTime());
			log.setGuideId(guide.getId());
			luService.saveGuideLastUpdatedLog(log);

			return message;

		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return null;
			} catch (IOException e1) {
				e1.printStackTrace();
				return null;
			}
		}
	}

	@RequestMapping(value = "/updateGuide", method = RequestMethod.POST)
	public void updateGuide(HttpServletRequest request,
			HttpServletResponse response, Model model) {

		List<GuideGallery> guideGalleries = new ArrayList<GuideGallery>();

		try {
			Guide guide = loadGuideFormField(guideGalleries, request);
			// save the new guide
			guideService.updateGuideProfile(guide);
			response.setStatus(HttpServletResponse.SC_ACCEPTED);

			/*
			 * set update log
			 */
			Date time_modified = new Date();
			response.setHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED,
					DateTimeUtil.fromDateToString_GMT(time_modified));
			luService.setGuideProfileLastUpdated(time_modified.getTime(),
					guide.getId());

		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	

	@RequestMapping(value = "/deactivateGuide/{guideId}", method = RequestMethod.GET)
	public void deactivateGuide(@PathVariable("guideId") final long guideId) {
		guideService.deactivateGuide(guideId);
	}

	private Guide loadGuideFormField(List<GuideGallery> guideGalleries,
			HttpServletRequest request) throws Exception {

		Guide guide = new Guide();

		@SuppressWarnings("unchecked")
		List<FileItem> items = new ServletFileUpload(new DiskFileItemFactory())
				.parseRequest(request);
		Map<String, String> commonField = new HashMap<String, String>();
		for (FileItem item : items) {
			if (item.isFormField()) {
				// Process regular form field (input
				// type="text|radio|checkbox|etc", select, etc).
				String fieldname = item.getFieldName();
				String fieldvalue = item.getString("UTF-8").trim();
				commonField.put(fieldname, fieldvalue);

			} else {
				GuideGallery gallery = new GuideGallery();
				byte[] imageFile = IOUtils.toByteArray(item.getInputStream());
				if (item.getFieldName().equalsIgnoreCase(
						HttpFieldConstant.GUIDE_PORTAL_PHOTO))
					gallery.setPortalPhoto(true);
				gallery.setImage(imageFile);
				guideGalleries.add(gallery);
			}
		}

		guide.setActive(true);// set as active
		guide.setAvgReviewScore(0);

		if (commonField.containsKey(HttpFieldConstant.SERVER_GUIDE_ID)) {
			guide = guideService.getGuideById(Long.valueOf(commonField
					.get(HttpFieldConstant.SERVER_GUIDE_ID)));
		}
		if (commonField.containsKey(HttpFieldConstant.GUIDE_DATE_OF_BIRTH)) {
			Date dob = DateTimeUtil.fromStringToDate(commonField
					.get(HttpFieldConstant.GUIDE_DATE_OF_BIRTH));
			guide.setDateOfBirth(dob);
		}

		if (commonField.containsKey(HttpFieldConstant.GUIDE_GUIDE_LAST_NAME)) {
			guide.setGuideLastname(commonField
					.get(HttpFieldConstant.GUIDE_GUIDE_LAST_NAME));
		}
		if (commonField.containsKey(HttpFieldConstant.GUIDE_GUIDE_FIRST_NAME)) {
			guide.setGuideFirstname(commonField
					.get(HttpFieldConstant.GUIDE_GUIDE_FIRST_NAME));
		}
		if (commonField.containsKey(HttpFieldConstant.GUIDE_TEXT_BIOGRAPHY)) {
			guide.setTextBiography(commonField
					.get(HttpFieldConstant.GUIDE_TEXT_BIOGRAPHY));
		}
		if (commonField
				.containsKey(HttpFieldConstant.GUIDE_FACEBOOK_ACCOUNT_NAME)) {
			guide.setFacebookAccountName(commonField
					.get(HttpFieldConstant.GUIDE_FACEBOOK_ACCOUNT_NAME));
		}

		/************************/
		/* set tour information */
		/************************/
		if (commonField.containsKey(HttpFieldConstant.GUIDE_PAYMENT_METHOD)) {
			int paymentMethod;
			paymentMethod = Integer.valueOf(commonField
					.get(HttpFieldConstant.GUIDE_PAYMENT_METHOD));
			if (paymentMethod == PaymentMethod.CASH.getNumVal()) {
				guide.setPaymentMethod(PaymentMethod.CASH);
			} else if (paymentMethod == PaymentMethod.CARD.getNumVal()) {
				guide.setPaymentMethod(PaymentMethod.CARD);
				guide.setPremium(Boolean
						.valueOf(HttpFieldConstant.GUIDE_IS_PREMIUM));
			} else {
				guide.setPaymentMethod(PaymentMethod.ALL);
			}
		}

		if (commonField.containsKey(HttpFieldConstant.GUIDE_TOUR_LENGTH)) {
			guide.setTourLength(commonField
					.get(HttpFieldConstant.GUIDE_TOUR_LENGTH));
		}
		if (commonField.containsKey(HttpFieldConstant.GUIDE_TOUR_PRICE)) {
			guide.setTourPrice(Float.valueOf(commonField
					.get(HttpFieldConstant.GUIDE_TOUR_PRICE)));
		}
		if (commonField.containsKey(HttpFieldConstant.GUIDE_TOUR_TYPE)) {
			guide.setTourType(commonField
					.get(HttpFieldConstant.GUIDE_TOUR_TYPE));
		}

		if (commonField.containsKey(HttpFieldConstant.GUIDE_PAYMENT_MONEY_TYPE)) {
			guide.setMoneyType(Integer.valueOf(commonField
					.get(HttpFieldConstant.GUIDE_PAYMENT_MONEY_TYPE)));
		}

		/************************/
		// only get the id of the city and country
		/************************/
		if (commonField.containsKey(HttpFieldConstant.GUIDE_CITY)) {
			guide.setCity(Integer.valueOf(commonField
					.get(HttpFieldConstant.GUIDE_CITY)));
		}
		if (commonField.containsKey(HttpFieldConstant.GUIDE_COUNTRY)) {
			guide.setCountry(Integer.valueOf(commonField
					.get(HttpFieldConstant.GUIDE_COUNTRY)));
		}

		if (commonField.containsKey(HttpFieldConstant.GUIDE_GENDER)) {
			// gender
			if (commonField.get(HttpFieldConstant.GUIDE_GENDER)
					.equalsIgnoreCase("male"))
				guide.setGender(Gender.MALE);
			else
				guide.setGender(Gender.FEMALE);
		}

		return guide;
	}

}
