package com.Mindelo.VentouraServer.Controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.*;

import com.Mindelo.VentouraServer.Entity.Landmark;

@Controller
public class IndexController {

	
	@RequestMapping("/")
	public ModelAndView home(HttpServletRequest request) {
		return new ModelAndView("index");
	}
}