package com.Mindelo.VentouraServer.JSONEntity;


import com.Mindelo.VentouraServer.Entity.TravellerSchedule;
import com.Mindelo.VentouraServer.Util.DateTimeUtil;

import lombok.Data;


@Data
public class JSONTravellerSchedule {


	private long id;

	private long travellerId;
	
	private String startTime;

	private String endTime;
	
	private int numberOfDays;
	
	private int city;

	private int country;
	
	private String imagePath; // Only used by the client.
	
	public JSONTravellerSchedule(){
		
	}
	
public JSONTravellerSchedule(TravellerSchedule travellerSchedule){
		this.id = travellerSchedule.getId();
		this.travellerId = travellerSchedule.getTravellerId();
		this.startTime = DateTimeUtil.fromDateToString_DDMMYYYY(travellerSchedule.getStartTime());
		this.endTime = DateTimeUtil.fromDateToString_DDMMYYYY(travellerSchedule.getEndTime());
		this.numberOfDays = DateTimeUtil.daysBetween(travellerSchedule.getStartTime(), travellerSchedule.getEndTime());
		this.city = travellerSchedule.getCity();
		this.country = travellerSchedule.getCountry();
	}
}
