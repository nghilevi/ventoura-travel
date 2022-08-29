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
import com.Mindelo.VentouraServer.Entity.GuideGallery;
import com.Mindelo.VentouraServer.Entity.GuideHiddenTreasure;
import com.Mindelo.VentouraServer.IService.IGuideService;
import com.Mindelo.VentouraServer.IService.ILastUpdatedService;
import com.Mindelo.VentouraServer.JSONEntity.JSONKeyValueMessage;
import com.Mindelo.VentouraServer.Util.DateTimeUtil;

@Controller
@RequestMapping(value = "/guide")
public class GuideHiddenTreasureController {

	@Autowired
	private IGuideService guideService;
	@Autowired
	private ILastUpdatedService luService;

	@RequestMapping(value = "/deleteBatchGuideHiddenTreasure", method = RequestMethod.POST)
	public void deleteGuideHiddenTreasure(HttpServletRequest request,
			HttpServletResponse response) {

		Enumeration<String> parameters = request.getParameterNames();

		List<Long> hiddenTreasureIds = new ArrayList<Long>();
		while (parameters.hasMoreElements()) {
			String parameter = (String) parameters.nextElement();
			hiddenTreasureIds.add(Long.valueOf(parameter));
		}

		if (hiddenTreasureIds.size() >= 1) {
			GuideHiddenTreasure treasure = guideService
					.getGuideHiddenTreasureById(hiddenTreasureIds.get(0));

			guideService.deleteBatchGuideHiddenTreasure(hiddenTreasureIds);
			// set updated log
			setUpdatedLog(response, treasure.getGuideId());
		}
	}

	@RequestMapping(value = "/deleteGuideHiddenTreasure/{guideHiddenTreasureId}", method = RequestMethod.GET)
	public void deleteGuideHiddenTreasure(
			HttpServletResponse response,
			@PathVariable("guideHiddenTreasureId") final long guideHiddenTreasureId) {

		GuideHiddenTreasure treasure = guideService
				.getGuideHiddenTreasureById(guideHiddenTreasureId);

		if (treasure != null) {
			// set updated log
			setUpdatedLog(response, treasure.getGuideId());
			guideService.deleteGuideHiddenTreasure(guideHiddenTreasureId);
		}
	}

	@RequestMapping(value = "/batchCreateGuideHiddenTreasure/{guideId}", method = RequestMethod.POST)
	public void batchCreateGuideHiddenTreasure(HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable("guideId") final long guideId) {

		Enumeration<String> parameters = request.getParameterNames();
		
		while (parameters.hasMoreElements()) {
			GuideHiddenTreasure guideHiddenTreasure = new GuideHiddenTreasure();
			guideHiddenTreasure.setGuideId(guideId);
			guideHiddenTreasure.setHiddenTreasureName((String) parameters
					.nextElement());
			guideService.saveGuideHiddenTreasure(guideHiddenTreasure);
		}
		
		// set updated log
		setUpdatedLog(response, guideId);	
	}

	@RequestMapping(value = "/createGuideHiddenTreasure", method = RequestMethod.POST)
	public @ResponseBody JSONKeyValueMessage<String, String> uploadGuideHiddenTreasurePost(
			HttpServletRequest request, HttpServletResponse response,
			Model model) {

		GuideHiddenTreasure guideHiddenTreasure = new GuideHiddenTreasure();

		try {

			guideHiddenTreasure.setGuideId(Long.valueOf(request
					.getParameter(HttpFieldConstant.SERVER_GUIDE_ID)));
			guideHiddenTreasure
					.setHiddenTreasureName(request
							.getParameter(HttpFieldConstant.GUIDE_HIDDEN_TREASURE_NAME));

			guideService.saveGuideHiddenTreasure(guideHiddenTreasure);

			// set updated log
			setUpdatedLog(response, guideHiddenTreasure.getGuideId());

			/*
			 * return values
			 */
			JSONKeyValueMessage<String, String> msg = new JSONKeyValueMessage<String, String>();
			msg.setKey("guideHiddenTreasureId");
			msg.setValue("" + guideHiddenTreasure.getId());

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
