package com.Mindelo.Ventoura.Entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class TravellerUpdatedLog implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long travellerId;
	
	public TravellerUpdatedLog(){
		
	}
	
	
	public TravellerUpdatedLog(long userId, long createdTime){
		this.travellerId = userId;
		profileLastUpdated = createdTime;
		galleryLastUpdated = createdTime;
		toursLastUpdated = createdTime;
		bookingsLastUpdated = createdTime;
		matchesLastUpdated = createdTime;
		vFunctionSettingsLastUpdated = createdTime;
	}
	
	
	/*
	 * all the time are saved in milliseconds
	 */
	private long profileLastUpdated;
	
	private long galleryLastUpdated;
	
	private long toursLastUpdated;
	
	private long bookingsLastUpdated;
	
	private long matchesLastUpdated;
	
	private long vFunctionSettingsLastUpdated;

}
