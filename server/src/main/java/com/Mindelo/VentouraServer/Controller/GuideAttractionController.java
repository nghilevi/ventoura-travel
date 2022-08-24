package com.Mindelo.VentouraServer.Controller; 

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Mindelo.VentouraServer.Constant.ConfigurationConstant;
import com.Mindelo.VentouraServer.Constant.HttpFieldConstant;
import com.Mindelo.VentouraServer.Entity.GuideAttraction;
import com.Mindelo.VentouraServer.IService.IGuideService;
import com.Mindelo.VentouraServer.IService.ILastUpdatedService;
import com.Mindelo.VentouraServer.JSONEntity.JSONKeyValueMessage;
import com.Mindelo.VentouraServer.Util.DateTimeUtil;

@Controller
@RequestMapping(value = "/guide")
public class GuideAttractionController { 

	@Autowired
	private IGuideService guideService;
	@Autowired
	private ILastUpdatedService luService;

	@RequestMapping(value = "/deleteGuideAttraction/{guideAttractionId}", method = RequestMethod.GET)
	public void deleteGuideAttraction(HttpServletResponse response,
			@PathVariable("guideAttractionId") final long guideAttractionId) {

		GuideAttraction attraction = guideService
				.getGuideAttractionById(guideAttractionId);

		if (attraction != null) {
			// set updated log
			setUpdatedLog(response, attraction.getGuideId());
			guideService.deleteGuideAttraction(guideAttractionId);
		}
	}

	@RequestMapping(value = "/deleteBatchGuideAttraction", method = RequestMethod.POST)
	public void deleteGuideAttraction(HttpServletRequest request,
			HttpServletResponse response) {

		Enumeration<String> parameters = request.getParameterNames();

		List<Long> attractionIds = new ArrayList<Long>();
		while (parameters.hasMoreElements()) {
			String parameter = (String) parameters.nextElement();
			attractionIds.add(Long.valueOf(parameter));
		}

		if (attractionIds.size() >= 1) {
			GuideAttraction treasure = guideService
					.getGuideAttractionById(attractionIds.get(0));

			guideService.deleteBatchGuideAttraction(attractionIds);
			// set updated log
			setUpdatedLog(response, treasure.getGuideId());
		}
	}

	@RequestMapping(value = "/batchCreateGuideAttraction/{guideId}", method = RequestMethod.POST)
	public void batchCreateGuideAttraction(HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable("guideId") final long guideId) {

		Enumeration<String> parameters = request.getParameterNames();
		
		while (parameters.hasMoreElements()) {
			GuideAttraction GuideAttraction = new GuideAttraction();
			GuideAttraction.setGuideId(guideId);
			GuideAttraction.setAttractionName((String) parameters
					.nextElement());
			guideService.saveGuideAttraction(GuideAttraction);
		}
		
		// set updated log
		setUpdatedLog(response, guideId);	
	}

	
	@RequestMapping(value = "/createGuideAttraction", method = RequestMethod.POST)
	public @ResponseBody JSONKeyValueMessage<String, String> uploadGuideAttractionPost(
			HttpServletRequest request, HttpServletResponse response,
			Model model) {

		GuideAttraction guideAttraction = new GuideAttraction();

		try {

			guideAttraction.setGuideId(Long.valueOf(request
					.getParameter(HttpFieldConstant.SERVER_GUIDE_ID)));
			guideAttraction.setAttractionName(request
					.getParameter(HttpFieldConstant.GUIDE_ATTRACTION_NAME));

			guideService.saveGuideAttraction(guideAttraction);

			// set updated log
			setUpdatedLog(response, guideAttraction.getId());

			/*
			 * return values
			 */
			JSONKeyValueMessage<String, String> msg = new JSONKeyValueMessage<String, String>();
			msg.setKey("guideAttractionId");
			msg.setValue("" + guideAttraction.getId());

			return msg;
		} catch (Exception e) {
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return null;
			} catch (IOException e1) {
				e1.printStackTrace();
				return null;
			}
		}

	}

	private void setUpdatedLog(HttpServletResponse response, long guideId) {
		/*
		 * set update log
		 */
		Date time_modified = new Date();
		response.setHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED,
				DateTimeUtil.fromDateToString_GMT(time_modified));
		luService.setGuideProfileLastUpdated(time_modified.getTime(), guideId);
	}

}
