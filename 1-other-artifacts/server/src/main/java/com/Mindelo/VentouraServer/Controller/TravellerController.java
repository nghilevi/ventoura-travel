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
import com.Mindelo.VentouraServer.Entity.Traveller;
import com.Mindelo.VentouraServer.Entity.TravellerGallery;
import com.Mindelo.VentouraServer.Entity.TravellerLastUpdatedLog;
import com.Mindelo.VentouraServer.Enum.Gender;
import com.Mindelo.VentouraServer.IService.IIMService;
import com.Mindelo.VentouraServer.IService.ILastUpdatedService;
import com.Mindelo.VentouraServer.IService.ILocationService;
import com.Mindelo.VentouraServer.IService.ITravellerService;
import com.Mindelo.VentouraServer.JSONEntity.JSONKeyValueMessage;
import com.Mindelo.VentouraServer.JSONEntity.JSONTravellerProfile;
import com.Mindelo.VentouraServer.Main.IMManager;
import com.Mindelo.VentouraServer.Util.DateTimeUtil;

@Controller
@RequestMapping(value = "/traveller")
public class TravellerController {

	@Autowired
	private ITravellerService travellerService;
	@Autowired
	private ILocationService locationService;
	@Autowired
	private ILastUpdatedService luService;
	@Autowired
	private IIMService imService;

	private XMPPConnection connection;

	@RequestMapping(value = "/getTravellerProfile/{travellerId}", method = RequestMethod.GET)
	public @ResponseBody JSONTravellerProfile getTravellerProfile(
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable("travellerId") final long travellerId) {

		JSONTravellerProfile travellerProfile = new JSONTravellerProfile();

		/*
		 * no changes in the server
		 */
		TravellerLastUpdatedLog travellerLastUpdatedLog = luService
				.getTravellerLastUpdatedLogByTravellerId(travellerId);
		try {
			String modifiedSince = request
					.getHeader(ConfigurationConstant.HTTP_HEADER_MODIFIED_SINCE);
			if (modifiedSince != null) {

				long lastUpdatedTime = DateTimeUtil.fromStringToDate_GMT(
						modifiedSince).getTime();
				if (travellerLastUpdatedLog != null
						&& lastUpdatedTime == travellerLastUpdatedLog
								.getProfileLastUpdated()) {
					response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				}
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

		/*
		 * some changes happened in the server
		 */
		response.setStatus(HttpServletResponse.SC_ACCEPTED);
		if (travellerLastUpdatedLog != null) {
			Date date_modified = new Date();
			date_modified.setTime(travellerLastUpdatedLog
					.getProfileLastUpdated());
			response.setHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED,
					DateTimeUtil.fromDateToString_GMT(date_modified));
		}

		/*
		 * return the databody
		 */
		Traveller traveller = travellerService.getTravellerById(travellerId);

		if (traveller != null) {
			travellerProfile.setId(travellerId);
			travellerProfile.setCity(traveller.getCity());
			travellerProfile.setCountry(traveller.getCountry());
			travellerProfile.setDateOfBirth(DateTimeUtil
					.fromDateToString(traveller.getDateOfBirth()));
			travellerProfile.setGender(traveller.getGender());
			travellerProfile.setTextBiography(traveller.getTextBiography());
			travellerProfile.setTravellerFirstname(traveller
					.getTravellerFirstname());
			travellerProfile.setTravellerLastname(traveller
					.getTravellerLastname());
			travellerProfile.setFacebookAccountName(traveller
					.getFacebookAccountName());
		}

		return travellerProfile;
	}

	@RequestMapping(value = "/updateTraveller", method = RequestMethod.POST)
	public void updateTraveller(HttpServletRequest request,
			HttpServletResponse response, Model model) {

		Traveller traveller = new Traveller();
		List<TravellerGallery> travellerGalleries = new ArrayList<TravellerGallery>();
		try {
			traveller = loadTravellerFormField(travellerGalleries, request);
			// save the new traveller
			travellerService.updateTraverllerProfile(traveller);

			/*
			 * set update log
			 */
			Date time_modified = new Date();
			response.setHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED,
					DateTimeUtil.fromDateToString_GMT(time_modified));
			luService.setTravellerProfileLastUpdated(time_modified.getTime(),
					traveller.getId());

		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@RequestMapping(value = "/createNewTraveller", method = RequestMethod.POST)
	public @ResponseBody JSONKeyValueMessage<String, String> createNewTraveller(
			HttpServletRequest request, HttpServletResponse response,
			Model model) {

		Traveller traveller = new Traveller();
		List<TravellerGallery> travellerGalleries = new ArrayList<TravellerGallery>();
		try {
			traveller = loadTravellerFormField(travellerGalleries, request);

			if (travellerService.getTravallerByFacebookAccount(traveller
					.getFacebookAccountName()) != null) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
				return null;
			}

			// save the new traveller
			travellerService.saveTraveller(traveller);

			for (TravellerGallery travellerGallery : travellerGalleries) {
				travellerGallery.setTravellerId(traveller.getId());
			}

			// set update log
			Date time_modified = new Date();
			response.setHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED,
					DateTimeUtil.fromDateToString_GMT(time_modified));
			TravellerLastUpdatedLog log = new TravellerLastUpdatedLog(
					time_modified.getTime());
			log.setTravellerId(traveller.getId());
			luService.saveTravellerLastUpdatedLog(log);

			// save the gallery
			travellerService.saveTravellerGallery(travellerGalleries);
			response.setStatus(HttpServletResponse.SC_ACCEPTED);

			/*
			 * create the IM account for the traveller
			 */
			connection = IMManager.getXMPPConnection();
			if (!imService.createAccount(connection, "t_" + traveller.getId(),
					IMConstant.VENTOURA_SERVER_IM_USER_PASSWORD)) {
				travellerService.deleteTravellerById(traveller.getId());
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return null;
			}

			/*
			 * return value to client
			 */
			JSONKeyValueMessage<String, String> message = new JSONKeyValueMessage<String, String>();
			message.setKey(HttpFieldConstant.SERVER_TRAVELLER_ID);
			message.setValue(traveller.getId() + "");

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

	@RequestMapping(value = "/deactivateTravller/{travellerId}", method = RequestMethod.GET)
	public void deactivateTraveller(
			@PathVariable("travellerId") final long travellerId) {
		travellerService.deactivateTraverller(travellerId);
	}

	private Traveller loadTravellerFormField(
			List<TravellerGallery> travellerGalleries,
			HttpServletRequest request) throws Exception {
		Traveller traveller = new Traveller();
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
				TravellerGallery gallery = new TravellerGallery();
				byte[] imageFile = IOUtils.toByteArray(item.getInputStream());
				if (item.getFieldName().equalsIgnoreCase(
						HttpFieldConstant.TRAVELLER_PORTAL_PHOTO))
					gallery.setPortalPhoto(true);
				gallery.setImage(imageFile);
				travellerGalleries.add(gallery);
			}
		}

		traveller.setActive(true); // set as active

		if (commonField.containsKey(HttpFieldConstant.SERVER_TRAVELLER_ID)) {
			traveller = travellerService.getTravellerById(Long
					.valueOf(commonField
							.get(HttpFieldConstant.SERVER_TRAVELLER_ID)));
		}
		if (commonField.containsKey(HttpFieldConstant.TRAVELLER_DATE_OF_BIRTH)) {
			Date dob = DateTimeUtil.fromStringToDate(commonField
					.get(HttpFieldConstant.TRAVELLER_DATE_OF_BIRTH));
			traveller.setDateOfBirth(dob);
		}

		// only transfer the id of the city and country
		if (commonField.containsKey(HttpFieldConstant.TRAVELLER_COUNTRY)) {
			traveller.setCountry(Integer.valueOf(commonField
					.get(HttpFieldConstant.TRAVELLER_COUNTRY)));
		}
		if (commonField.containsKey(HttpFieldConstant.TRAVELLER_CITY)) {
			traveller.setCity(Integer.valueOf(commonField
					.get(HttpFieldConstant.TRAVELLER_CITY)));
		}
		if (commonField
				.containsKey(HttpFieldConstant.TRAVELLER_TRAVERLLER_FIRST_NAME)) {
			traveller.setTravellerFirstname(commonField
					.get(HttpFieldConstant.TRAVELLER_TRAVERLLER_FIRST_NAME));
		}
		if (commonField
				.containsKey(HttpFieldConstant.TRAVELLER_TRAVERLLER_LAST_NAME)) {
			traveller.setTravellerLastname(commonField
					.get(HttpFieldConstant.TRAVELLER_TRAVERLLER_LAST_NAME));
		}
		if (commonField.containsKey(HttpFieldConstant.TRAVELLER_TEXT_BIOGRAPHY)) {
			traveller.setTextBiography(commonField
					.get(HttpFieldConstant.TRAVELLER_TEXT_BIOGRAPHY));
		}
		if (commonField
				.containsKey(HttpFieldConstant.TRAVELLER_FACEBOOK_ACCOUNT_NAME)) {
			traveller.setFacebookAccountName(commonField
					.get(HttpFieldConstant.TRAVELLER_FACEBOOK_ACCOUNT_NAME));
		}
		if (commonField.containsKey(HttpFieldConstant.TRAVELLER_GENDER)) {
			if (commonField.get(HttpFieldConstant.TRAVELLER_GENDER)
					.equalsIgnoreCase("male"))
				traveller.setGender(Gender.MALE);
			else
				traveller.setGender(Gender.FEMALE);
		}

		return traveller;

	}

}
