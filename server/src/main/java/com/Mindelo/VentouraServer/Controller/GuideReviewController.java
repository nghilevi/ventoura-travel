package com.Mindelo.VentouraServer.Controller;

import java.io.IOException;
import java.util.Date;
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
import com.Mindelo.VentouraServer.Entity.GuideLastUpdatedLog;
import com.Mindelo.VentouraServer.Entity.GuideReview;
import com.Mindelo.VentouraServer.IService.IGuideService;
import com.Mindelo.VentouraServer.IService.ILastUpdatedService;
import com.Mindelo.VentouraServer.JSONEntity.JSONGuideReviewList;
import com.Mindelo.VentouraServer.JSONEntity.JSONKeyValueMessage;
import com.Mindelo.VentouraServer.Util.DateTimeUtil;

@Controller
@RequestMapping(value = "/guide/guideReview")
public class GuideReviewController {

	@Autowired
	private IGuideService guideService;
	@Autowired
	private ILastUpdatedService luService;

	@RequestMapping(value = "/createGuideReview", method = RequestMethod.POST)
	public @ResponseBody JSONKeyValueMessage<String, String> uploadGuideReviewPost(
			HttpServletRequest request, HttpServletResponse response,
			Model model) {

		GuideReview guideReview;
		try {
			guideReview = loadGuideReviewFormField(request);
			guideService.saveGuideReview(guideReview);

			/*
			 * set update log
			 */
			Date time_modified = new Date();
			response.setHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED, DateTimeUtil.fromDateToString_GMT(time_modified));	
			luService.setGuideReviewLastUpdated(time_modified.getTime(),
					guideReview.getGuideId());

			JSONKeyValueMessage<String, String> msg = new JSONKeyValueMessage<String, String>();
			msg.setKey(HttpFieldConstant.SERVER_GUIDE_REVIEW_ID);
			msg.setValue("" + guideReview.getId());

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

	@RequestMapping(value = "/getGuideReviews/{guideId}", method = RequestMethod.GET)
	public @ResponseBody JSONGuideReviewList getGuideReviews(
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable("guideId") final long guideId) {

		JSONGuideReviewList reviewList = new JSONGuideReviewList();

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
								.getReviewLastUpdated()) {
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
			date_modified.setTime(guideLastUpdatedLog.getReviewLastUpdated());
			response.setHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED, DateTimeUtil.fromDateToString_GMT(date_modified));	
		}
		
		/*
		 * return the databody 
		 */
		List<GuideReview> reviews = guideService
				.getGuideReviewByGuideId(guideId);
		if (reviews != null) {
			reviewList.setGuideReviewList(reviews);
		}
		return reviewList;
	}

	private GuideReview loadGuideReviewFormField(HttpServletRequest request)
			throws Exception {

		GuideReview review = new GuideReview();

		review.setGuideId(Long.valueOf(request
				.getParameter(HttpFieldConstant.GUIDE_REVIEW_GUIDE_ID)));
		review.setTravellerId(Long.valueOf(request
				.getParameter(HttpFieldConstant.GUIDE_REVIEW_TRAVELLER_ID)));

		review.setTravellerFirstname(request
				.getParameter(HttpFieldConstant.GUIDE_REVIEW_TRAVELLER_FIRSTNAME));
		review.setReviewScore(Float.valueOf(request
				.getParameter(HttpFieldConstant.GUIDE_REVIEW_SCORE)));
		review.setReviewMessage(request
				.getParameter(HttpFieldConstant.GUIDE_REVIEW_MESSAGE));
		review.setReviewDate(new Date());

		return review;
	}

}
