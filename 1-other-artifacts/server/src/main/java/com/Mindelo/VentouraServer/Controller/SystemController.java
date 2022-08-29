package com.Mindelo.VentouraServer.Controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Mindelo.VentouraServer.Constant.HttpFieldConstant;
import com.Mindelo.VentouraServer.Entity.Guide;
import com.Mindelo.VentouraServer.Entity.Traveller;
import com.Mindelo.VentouraServer.IService.IGuideService;
import com.Mindelo.VentouraServer.IService.ITravellerService;
import com.Mindelo.VentouraServer.JSONEntity.JSONKeyValueMessage;


@Controller
@RequestMapping(value = "/system")
public class SystemController {
	
	
	@Autowired
	private ITravellerService travellerService;
	@Autowired
	private IGuideService guideService;
	
	
	@RequestMapping(value = "/login/traveller/{facebookAccountName}", method = RequestMethod.GET)
	public @ResponseBody
	JSONKeyValueMessage<String, String> travellerLoginProbe(HttpServletResponse response, 
			@PathVariable("facebookAccountName") final String facebookAccountName) {
		JSONKeyValueMessage<String, String> message = new JSONKeyValueMessage<String, String>();
		
		try {
			
			Traveller traveller = travellerService.getTravallerByFacebookAccount(facebookAccountName);
			if(traveller != null){
				message.setKey(HttpFieldConstant.SERVER_TRAVELLER_ID);
				message.setValue(traveller.getId() + "");
			}else{
				message.setKey(HttpFieldConstant.SERVER_TRAVELLER_ID);
				message.setValue("-1");
			}
		
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
	
	@RequestMapping(value = "/login/guide/{facebookAccountName}", method = RequestMethod.GET)
	public @ResponseBody
	JSONKeyValueMessage<String, String> guideoginProbe(HttpServletResponse response, 
			@PathVariable("facebookAccountName") final String facebookAccountName) {
		JSONKeyValueMessage<String, String> message = new JSONKeyValueMessage<String, String>();
		
		try {
			
			Guide guide = guideService.getGuideByFacebookAccount(facebookAccountName);
			if(guide != null){
				message.setKey(HttpFieldConstant.SERVER_TRAVELLER_ID);
				message.setValue(guide.getId() + "");
			}else{
				message.setKey(HttpFieldConstant.SERVER_GUIDE_ID);
				message.setValue("-1");
			}
		
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

}
