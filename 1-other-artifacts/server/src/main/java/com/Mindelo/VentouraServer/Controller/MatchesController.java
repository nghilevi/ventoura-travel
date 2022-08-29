package com.Mindelo.VentouraServer.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Mindelo.VentouraServer.Constant.ConfigurationConstant;
import com.Mindelo.VentouraServer.Entity.Guide;
import com.Mindelo.VentouraServer.Entity.GuideLastUpdatedLog;
import com.Mindelo.VentouraServer.Entity.TGMatch;
import com.Mindelo.VentouraServer.Entity.TTMatch;
import com.Mindelo.VentouraServer.Entity.Traveller;
import com.Mindelo.VentouraServer.Entity.TravellerLastUpdatedLog;
import com.Mindelo.VentouraServer.Enum.UserRole;
import com.Mindelo.VentouraServer.IService.IGuideService;
import com.Mindelo.VentouraServer.IService.ILastUpdatedService;
import com.Mindelo.VentouraServer.IService.IMatchesService;
import com.Mindelo.VentouraServer.IService.ITravellerService;
import com.Mindelo.VentouraServer.JSONEntity.JSONMatch;
import com.Mindelo.VentouraServer.JSONEntity.JSONMatchesList;
import com.Mindelo.VentouraServer.Util.DateTimeUtil;

@Controller
public class MatchesController {

	@Autowired
	private IMatchesService matchesService;
	@Autowired
	private ITravellerService travellerService;
	@Autowired
	private IGuideService guideService;
	@Autowired
	private ILastUpdatedService luService;

	@RequestMapping(value = "/guide/getAllMatches/{guideId}", method = RequestMethod.GET)
	public @ResponseBody JSONMatchesList getGuideMatches(
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable("guideId") final long guideId) {

		JSONMatchesList matchList = new JSONMatchesList();

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
								.getMatchesLastUpdated()) {
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
		if(guideLastUpdatedLog != null){
			Date date_modified = new Date();
			date_modified.setTime(guideLastUpdatedLog.getMatchesLastUpdated());
			response.setHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED, DateTimeUtil.fromDateToString_GMT(date_modified));	
		}
		
		/*
		 * return the databody 
		 */
		try {
			List<JSONMatch> jsonMatches = new ArrayList<JSONMatch>();
			List<TGMatch> matches = matchesService.getGuideTGMatch(guideId);
			for (TGMatch match : matches) {
				JSONMatch jsonMatch = new JSONMatch();
				Traveller traveller = travellerService.getTravellerById(match
						.getTravellerId());

				jsonMatch.setAge(DateTimeUtil.computeAge(traveller
						.getDateOfBirth()));
				jsonMatch.setCity(traveller.getCity()); 
				jsonMatch.setCountry(traveller.getCountry());
				jsonMatch.setGender(traveller.getGender());
				jsonMatch.setTimeMatched(DateTimeUtil.fromDateToString(match
						.getMatchedDate()));

				jsonMatch.setUserId(traveller.getId());

				jsonMatch.setUserRole(UserRole.TRAVELLER);
				jsonMatch.setUserFirstname(traveller.getTravellerFirstname());
				jsonMatches.add(jsonMatch);
			}

			/*
			 * return the latest matches package
			 */
			matchList.setMatches(jsonMatches);

			return matchList;

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

	@RequestMapping(value = "/traveller/loadMatchImages", method = RequestMethod.POST)
	public void loadTravellerMatchImages(HttpServletRequest request,
			HttpServletResponse response) {

		// set headers for the response
		response.setStatus(HttpServletResponse.SC_ACCEPTED);
		response.setContentType("application/zip");
		response.setHeader("Content-Disposition",
				"attachment;filename=temp_match_images.zip");
		try {
			int size = 0;
			ZipOutputStream zos = new ZipOutputStream(
					response.getOutputStream());
			Enumeration<String> parameters = request.getParameterNames();

			while (parameters.hasMoreElements()) {
				String parameter = (String) parameters.nextElement();
				String[] match_info = parameter.split("_");
				byte[] image;
				ZipEntry entry;

				if (match_info[0].equalsIgnoreCase("g")) {
					entry = new ZipEntry("g_" + match_info[1] + "_.png");
					image = guideService.getGuidePortalGalleryByGuideId(
							Long.valueOf(match_info[1])).getImage();
					size += image.length;
				} else {
					entry = new ZipEntry("t_" + match_info[1] + "_.png");
					image = travellerService
							.getTravellerPortalGalleryByTravellerId(
									Long.valueOf(match_info[1])).getImage();
					size += image.length;
				}

				entry.setSize(image.length);
				zos.putNextEntry(entry);
				zos.write(image);
				zos.closeEntry();
			}

			zos.close();

			response.setContentLength(size);

		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@RequestMapping(value = "/guide/loadMatchImages", method = RequestMethod.POST)
	public void loadGuideMatchImages(HttpServletRequest request,
			HttpServletResponse response) {

		// set headers for the response
		response.setStatus(HttpServletResponse.SC_ACCEPTED);
		response.setContentType("application/zip");
		response.setHeader("Content-Disposition",
				"attachment;filename=temp_match_images.zip");
		try {
			int size = 0;
			ZipOutputStream zos = new ZipOutputStream(
					response.getOutputStream());
			Enumeration<String> parameters = request.getParameterNames();

			while (parameters.hasMoreElements()) {
				String parameter = (String) parameters.nextElement();
				String[] match_info = parameter.split("_");
				byte[] image;
				ZipEntry entry;

				entry = new ZipEntry("t_" + match_info[1] + "_.png");
				image = travellerService
						.getTravellerPortalGalleryByTravellerId(
								Long.valueOf(match_info[1])).getImage();
				size += image.length;

				entry.setSize(image.length);
				zos.putNextEntry(entry);
				zos.write(image);
				zos.closeEntry();
			}

			zos.close();

			response.setContentLength(size);

		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@RequestMapping(value = "/traveller/getAllMatches/{travellerId}", method = RequestMethod.GET)
	public @ResponseBody JSONMatchesList getTravellerMatches(
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable("travellerId") final long travellerId) {

		JSONMatchesList matchList = new JSONMatchesList();

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
								.getMatchesLastUpdated()) {
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
		if(travellerLastUpdatedLog != null){
			Date date_modified = new Date();
			date_modified.setTime(travellerLastUpdatedLog.getMatchesLastUpdated());
			response.setHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED, DateTimeUtil.fromDateToString_GMT(date_modified));	
		}
		
		/*
		 * return the databody 
		 */
		try {
			List<JSONMatch> jsonMatches = new ArrayList<JSONMatch>();

			List<TGMatch> guideMatches = matchesService
					.getTravellerTGMatch(travellerId);
			for (TGMatch match : guideMatches) {

				JSONMatch jsonMatch = new JSONMatch();
				Guide guide = guideService.getGuideById(match.getGuideId());

				jsonMatch
						.setAge(DateTimeUtil.computeAge(guide.getDateOfBirth()));
				jsonMatch.setCity(guide.getCity()); // TODO fix city
				jsonMatch.setCountry(guide.getCountry());
				jsonMatch.setGender(guide.getGender());
				jsonMatch.setTimeMatched(DateTimeUtil.fromDateToString(match
						.getMatchedDate()));

				jsonMatch.setUserId(guide.getId());

				jsonMatch.setUserFirstname(guide.getGuideFirstname());
				
				/*
				 * payment informations
				 */
				jsonMatch.setPaymentMethod(guide.getPaymentMethod());				
				jsonMatch.setTourPrice(guide.getTourPrice());

				jsonMatch.setUserRole(UserRole.GUIDE);

				jsonMatches.add(jsonMatch);
			}

			List<TTMatch> travellerMatches = matchesService
					.getTravellerTTMatch(travellerId);
			for (TTMatch match : travellerMatches) {
				JSONMatch jsonMatch = new JSONMatch();

				Traveller traveller;
				if (match.getRedTravellerId() == travellerId) {
					traveller = travellerService.getTravellerById(match
							.getBlueTravellerId());
				} else {
					traveller = travellerService.getTravellerById(match
							.getRedTravellerId());
				}

				jsonMatch.setAge(DateTimeUtil.computeAge(traveller
						.getDateOfBirth()));
				jsonMatch.setCity(traveller.getCity());
				jsonMatch.setCountry(traveller.getCountry());
				jsonMatch.setGender(traveller.getGender());
				jsonMatch.setTimeMatched(DateTimeUtil.fromDateToString(match
						.getMatchedDate()));

				jsonMatch.setUserId(traveller.getId());

				jsonMatch.setUserRole(UserRole.TRAVELLER);
				jsonMatch.setUserFirstname(traveller.getTravellerFirstname());

				jsonMatches.add(jsonMatch);
			}

			/*
			 * return the latest matches package
			 */
			matchList.setMatches(jsonMatches);

			return matchList;

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

}
