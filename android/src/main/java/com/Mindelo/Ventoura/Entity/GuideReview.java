package com.Mindelo.Ventoura.Entity;


import lombok.Data;


@Data
public class GuideReview {

	private long id;
	
	private long guideId;  
	
	private long travellerId;  
	
	private String travellerFirstname;  

	private float reviewScore;  
	
	private String reviewMessage;  
	
}
