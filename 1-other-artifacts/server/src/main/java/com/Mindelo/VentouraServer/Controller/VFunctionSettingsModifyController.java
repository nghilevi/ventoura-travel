package com.Mindelo.VentouraServer.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Mindelo.VentouraServer.Constant.HttpFieldConstant;
import com.Mindelo.VentouraServer.Entity.GuideVFunctionSettings;
import com.Mindelo.VentouraServer.Entity.TravellerVFunctionGuideSettings;
import com.Mindelo.VentouraServer.Entity.TravellerVFunctionTravellerSettings;
import com.Mindelo.VentouraServer.Enum.Gender;
import com.Mindelo.VentouraServer.IService.IVFunctionService;
import com.Mindelo.VentouraServer.JSONEntity.JSONKeyValueMessage;

@Controller
@RequestMapping(value = "/VFunctionSettings")
public class VFunctionSettingsModifyController {

	@Autowired
	IVFunctionService vFunctionService;

	@RequestMapping(value = "/createNewTVTSettings", method = RequestMethod.POST)
	public @ResponseBody
	JSONKeyValueMessage<String, String> uploadTVTSettingPost(
			HttpServletRequest request, HttpServletResponse response,
			Model model) {

		TravellerVFunctionTravellerSettings tvtSettings = loadTVTFormField(request);

		vFunctionService.saveTravellerVFunctionTravellerSettings(tvtSettings);

		JSONKeyValueMessage<String, String> msg = new JSONKeyValueMessage<String, String>();
		msg.setKey("settingId");
		msg.setValue("" + tvtSettings.getId());

		return msg;

	}

	@RequestMapping(value = "/updateTVTSettings", method = RequestMethod.POST)
	public void updateTVTSettingPost(
			HttpServletRequest request, HttpServletResponse response,
			Model model) {

		TravellerVFunctionTravellerSettings tvtSettings = loadTVTFormField(request);

		tvtSettings.setId(Long.valueOf(request
				.getParameter(HttpFieldConstant.SERVER_TVT_ID)));

		vFunctionService.updateTVFunctionTravellerSettings(tvtSettings);
	}

	@RequestMapping(value = "/deleteTVTSettings/{VSettingsId}", method = RequestMethod.GET)
	public void deleteTVTSettingsController(HttpServletResponse response,
			@PathVariable("VSettingsId") final long VSettingsId) {
		vFunctionService.deleteTVFunctionTravellerSettings(VSettingsId);
	}

	private TravellerVFunctionTravellerSettings loadTVTFormField(
			HttpServletRequest request) {

		TravellerVFunctionTravellerSettings tvtSettings = new TravellerVFunctionTravellerSettings();

		tvtSettings.setMaxAge(Integer.valueOf(request
				.getParameter(HttpFieldConstant.TVT_MAX_AGE)));
		tvtSettings.setMiniAge(Integer.valueOf(request
				.getParameter(HttpFieldConstant.TVT_MIN_AGE)));

		
		tvtSettings.setFemalePercent(Float.valueOf(request.getParameter(HttpFieldConstant.TVT_FEMALE_PERCENT)));
		
		
		tvtSettings.setTravellerId(Long.valueOf(request
				.getParameter(HttpFieldConstant.TVT_TRAVELLER_ID)));

		return tvtSettings;
	}

	@RequestMapping(value = "/updateTVGSettings", method = RequestMethod.POST)
	public void updateTVGSettingPost(
			HttpServletRequest request, HttpServletResponse response,
			Model model) {

		TravellerVFunctionGuideSettings tvgSettings = loadTVGFormField(request);

		tvgSettings.setId(Long.valueOf(request
				.getParameter(HttpFieldConstant.SERVER_TVG_ID)));

		vFunctionService.updateTVFunctionGuideSettings(tvgSettings);
	}

	@RequestMapping(value = "/createNewTVGSettings", method = RequestMethod.POST)
	public @ResponseBody
	JSONKeyValueMessage<String, String> uploadTVGSettingPost(
			HttpServletRequest request, HttpServletResponse response,
			Model model) {

		TravellerVFunctionGuideSettings tvgSettings = loadTVGFormField(request);

		vFunctionService.saveTravellerVFunctionGuideSettings(tvgSettings);

		JSONKeyValueMessage<String, String> msg = new JSONKeyValueMessage<String, String>();
		msg.setKey("settingId");
		msg.setValue("" + tvgSettings.getId());
		return msg;
	}

	@RequestMapping(value = "/deleteTVGSettings/{VSettingsId}", method = RequestMethod.GET)
	public void deleteTVGSettingsController(HttpServletResponse response,
			@PathVariable("VSettingsId") final long VSettingsId) {
		vFunctionService.deleteTVFunctionGuideSettings(VSettingsId);
		response.setStatus(202);
	}

	private TravellerVFunctionGuideSettings loadTVGFormField(
			HttpServletRequest request) {
		TravellerVFunctionGuideSettings tvgSettings = new TravellerVFunctionGuideSettings();

	
		tvgSettings.setMaxAge(Integer.valueOf(request
				.getParameter(HttpFieldConstant.TVG_MAX_AGE)));
		tvgSettings.setMiniAge(Integer.valueOf(request
				.getParameter(HttpFieldConstant.TVG_MIN_AGE)));

		tvgSettings.setFemalePercent(Float.valueOf(request.getParameter(HttpFieldConstant.TVT_FEMALE_PERCENT)));
		
		tvgSettings.setTravellerId(Long.valueOf(request
				.getParameter(HttpFieldConstant.TVG_TRAVELLER_ID)));
		return tvgSettings;
	}

	@RequestMapping(value = "/createNewGVSettings", method = RequestMethod.POST)
	public @ResponseBody
	JSONKeyValueMessage<String, String> uploadGVSettingPost(
			HttpServletRequest request, HttpServletResponse response,
			Model model) {

		GuideVFunctionSettings gvSettings = loadGVFormField(request);
		
		vFunctionService.saveGuideVFunctionSettings(gvSettings);

		JSONKeyValueMessage<String, String> msg = new JSONKeyValueMessage<String, String>();
		msg.setKey("settingId");
		msg.setValue("" + gvSettings.getId());
		return msg;

	}
	@RequestMapping(value = "/updateGVSettings", method = RequestMethod.POST)
	public void updateGVSettingPost(
			HttpServletRequest request, HttpServletResponse response,
			Model model) {

		GuideVFunctionSettings gvSettings = loadGVFormField(request);
		
		gvSettings.setId(Long.valueOf(request
				.getParameter(HttpFieldConstant.SERVER_GV_ID)));
		
		vFunctionService.updateGVFunctionSettings(gvSettings);

	}
	
	@RequestMapping(value = "/deleteGVSettings/{VSettingsId}", method = RequestMethod.GET)
	public void deleteGVSettingsController(HttpServletResponse response,
			@PathVariable("VSettingsId") final long VSettingsId) {
		vFunctionService.deleteGVFunctionSettings(VSettingsId);
		response.setStatus(202);
	}

	private GuideVFunctionSettings loadGVFormField(HttpServletRequest request) {
		
		GuideVFunctionSettings gvSettings = new GuideVFunctionSettings();

	

		gvSettings.setMaxAge(Integer.valueOf(request
				.getParameter(HttpFieldConstant.GV_MAX_AGE)));
		gvSettings.setMiniAge(Integer.valueOf(request
				.getParameter(HttpFieldConstant.GV_MIN_AGE)));

		gvSettings.setFemalePercent(Float.valueOf(request.getParameter(HttpFieldConstant.TVT_FEMALE_PERCENT)));
		
		gvSettings.setGuideId(Long.valueOf(request
				.getParameter(HttpFieldConstant.GV_GUIDE_ID)));
		return gvSettings;
	}

}
