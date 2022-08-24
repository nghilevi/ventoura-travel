package com.Mindelo.Ventoura.JSONEntity;


import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.Mindelo.Ventoura.Enum.Gender;
import com.Mindelo.Ventoura.Enum.PaymentMethod;
import com.Mindelo.Ventoura.Enum.UserRole;

import lombok.Data;

@Data
public class JSONVentoura implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	
	private UserRole userRole;
	
	private String firstname;
	private Gender gender;
	
	private int city;
	
	private int country;
	
	private String tourType; // this field is used only when he is guide
	private String tourLength;
	private float tourPrice;
	private Date lastTimeActive;
	private String textBiography;
	
	private PaymentMethod paymentMethod;  // 0 represents pay by cash, 1 represents card, 2 represent both
	
	private float avgReviewScore;
	private int numberOfReviewers;
	
	private int age;
	private int numberOfGallery;
	
	private List<JSONGuideAttraction> attractions;
	private List<JSONGuideHiddenTreasure> hiddenTreasures;
	
}

