package com.Mindelo.VentouraServer.Controller;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jivesoftware.smack.XMPPConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Mindelo.VentouraServer.Constant.ConfigurationConstant;
import com.Mindelo.VentouraServer.Constant.HttpFieldConstant;
import com.Mindelo.VentouraServer.Constant.PushConstant;
import com.Mindelo.VentouraServer.Enum.UserRole;
import com.Mindelo.VentouraServer.IService.IGuideService;
import com.Mindelo.VentouraServer.IService.IIMService;
import com.Mindelo.VentouraServer.IService.ILastUpdatedService;
import com.Mindelo.VentouraServer.IService.ITravellerService;
import com.Mindelo.VentouraServer.IService.IVentouraService;
import com.Mindelo.VentouraServer.JSONEntity.JSONKeyValueMessage;
import com.Mindelo.VentouraServer.JSONEntity.JSONVentouraList;
import com.Mindelo.VentouraServer.Main.IMManager;
import com.Mindelo.VentouraServer.Util.DateTimeUtil;

@Controller
@RequestMapping(value = "/ventoura")
public class VentouraController {

	@Autowired
	private IVentouraService ventouraService;
	@Autowired
	private ITravellerService travellerService;
	@Autowired
	private IGuideService guideService;
	@Autowired
	private IIMService imService;
	@Autowired
	private ILastUpdatedService luService;

	private XMPPConnection connection;

	@RequestMapping(value = "/traveller/getVentouraPackage/{travellerId}", method = RequestMethod.GET)
	public @ResponseBody JSONVentouraList getTravellerVentoura(
			HttpServletResponse response,
			@PathVariable("travellerId") final long travellerId) {

		return ventouraService.getTravellerVentoura(travellerId);

	}

	@RequestMapping(value = "/loadVentouraImages", method = RequestMethod.POST)
	public void doDownload(HttpServletRequest request,
			HttpServletResponse response) {

		// set headers for the response
		response.setStatus(HttpServletResponse.SC_ACCEPTED);
		response.setContentType("application/zip");
		response.setHeader("Content-Disposition",
				"attachment;filename=temp_ventoura_images.zip");
		try {
			int size = 0;
			ZipOutputStream zos = new ZipOutputStream(
					response.getOutputStream());
			Enumeration<String> parameters = request.getParameterNames();

			while (parameters.hasMoreElements()) {
				String parameter = (String) parameters.nextElement();
				String[] ventoura_info = parameter.split("_");
				byte[] image;
				ZipEntry entry;

				if (ventoura_info[0].equalsIgnoreCase("g")) {
					entry = new ZipEntry("g_" + ventoura_info[1] + "_.png");
					image = guideService.getGuidePortalGalleryByGuideId(
							Long.valueOf(ventoura_info[1])).getImage();
					size += image.length;
				} else {
					entry = new ZipEntry("t_" + ventoura_info[1] + "_.png");
					image = travellerService
							.getTravellerPortalGalleryByTravellerId(
									Long.valueOf(ventoura_info[1])).getImage();
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

	@RequestMapping(value = "/guide/getVentouraPackage/{guideId}", method = RequestMethod.GET)
	public @ResponseBody JSONVentouraList getGuideVentoura(
			HttpServletResponse response,
			@PathVariable("guideId") final long guideId) {

		return ventouraService.getGuideVentoura(guideId);

	}

	@RequestMapping(value = "/traveller/judgeTraveller/{travellerId}/{likeOrnot}/{viewedTravellerId}", method = RequestMethod.GET)
	public @ResponseBody JSONKeyValueMessage<String, String> travellerJudgeTraveller(
			HttpServletResponse response,
			@PathVariable("travellerId") final long travellerId,
			@PathVariable("likeOrnot") final boolean likeOrnot,
			@PathVariable("viewedTravellerId") final long viewedTravellerId) {

		JSONKeyValueMessage<String, String> message = new JSONKeyValueMessage<String, String>();

		if (ventouraService.travellerJudgeTraveller(travellerId, likeOrnot,
				viewedTravellerId)) {
			message.setKey(HttpFieldConstant.IS_A_MATCH);
			message.setValue("1");

			/*
			 * set update log
			 */
			Date time_modified = new Date();
			response.setHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED,
					DateTimeUtil.fromDateToString_GMT(time_modified));
			luService.setTravellerMatchesLastUpdated(time_modified.getTime(),
					travellerId);
			luService.setTravellerMatchesLastUpdated(time_modified.getTime(),
					viewedTravellerId);
			/*
			 * notice the traveller that, it is a match
			 */
			connection = IMManager.getXMPPConnection();
			imService.sendMessage(connection, viewedTravellerId,
					UserRole.TRAVELLER,
					PushConstant.PUSH_IS_MATCH_WITH_TRAVELLER + travellerId);

		} else {
			message.setKey(HttpFieldConstant.IS_A_MATCH);
			message.setValue("0");
		}
		return message;
	}

	@RequestMapping(value = "/traveller/judgeGuide/{travellerId}/{likeOrnot}/{guideId}", method = RequestMethod.GET)
	public @ResponseBody JSONKeyValueMessage<String, String> travellerJudgeGuide(
			HttpServletResponse response,
			@PathVariable("travellerId") final long travellerId,
			@PathVariable("likeOrnot") final boolean likeOrnot,
			@PathVariable("guideId") final long guideId) {

		JSONKeyValueMessage<String, String> message = new JSONKeyValueMessage<String, String>();

		if (ventouraService
				.travellerJudgeGuide(travellerId, likeOrnot, guideId)) {
			message.setKey(HttpFieldConstant.IS_A_MATCH);
			message.setValue("1");

			/*
			 * set update log
			 */
			Date time_modified = new Date();
			response.setHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED,
					DateTimeUtil.fromDateToString_GMT(time_modified));
			luService.setTravellerMatchesLastUpdated(time_modified.getTime(),
					travellerId);
			luService.setGuideMatchesLastUpdated(time_modified.getTime(),
					guideId);

			/*
			 * notice the guide that, it is a match
			 */
			connection = IMManager.getXMPPConnection();
			imService.sendMessage(connection, guideId, UserRole.GUIDE,
					PushConstant.PUSH_IS_MATCH_WITH_TRAVELLER + travellerId);

		} else {
			message.setKey(HttpFieldConstant.IS_A_MATCH);
			message.setValue("0");
		}
		return message;
	}

	@RequestMapping(value = "/guide/judgeTraveller/{guideId}/{likeOrnot}/{travellerId}", method = RequestMethod.GET)
	public @ResponseBody JSONKeyValueMessage<String, String> guideJudgeTraveller(
			HttpServletResponse response,
			@PathVariable("guideId") final long guideId,
			@PathVariable("likeOrnot") final boolean likeOrnot,
			@PathVariable("travellerId") final long travellerId) {

		JSONKeyValueMessage<String, String> message = new JSONKeyValueMessage<String, String>();

		if (ventouraService
				.guideJudgeTraveller(guideId, likeOrnot, travellerId)) {
			message.setKey(HttpFieldConstant.IS_A_MATCH);
			message.setValue("1");

			/*
			 * set update log
			 */
			Date time_modified = new Date();
			response.setHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED,
					DateTimeUtil.fromDateToString_GMT(time_modified));
			luService.setTravellerMatchesLastUpdated(time_modified.getTime(),
					travellerId);
			luService.setGuideMatchesLastUpdated(time_modified.getTime(),
					guideId);

			/*
			 * notice the traveller that, it is a match
			 */
			connection = IMManager.getXMPPConnection();
			imService.sendMessage(connection, travellerId, UserRole.TRAVELLER,
					PushConstant.PUSH_IS_MATCH_WITH_GUIDE + guideId);

		} else {
			message.setKey(HttpFieldConstant.IS_A_MATCH);
			message.setValue("0");
		}

		return message;

	}

}
