package com.Mindelo.Ventoura.JSONEntity;


import java.io.Serializable;

import lombok.Data;



@Data
public class JSONBooking implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;

	private long guideId;
	private String guideFirstname;  
	private int guideAge;
	private int guideGender;

	private long travellerId;  
	private String travellerFirstname;
	private int travellerAge;
	private int travellerGender;
	
	private String statusLastUpdatedTime;
	private float tourPrice;
	private String tourDate;
	private int bookingStatus;

}
