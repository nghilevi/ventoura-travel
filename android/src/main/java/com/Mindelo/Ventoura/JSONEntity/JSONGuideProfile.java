package com.Mindelo.Ventoura.JSONEntity;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

import com.Mindelo.Ventoura.Enum.Gender;
import com.Mindelo.Ventoura.Enum.PaymentMethod;

@Data
public class JSONGuideProfile implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*
	 * guide id saved in the server
	 */
	private long id;

	/*
	 * basic information
	 */
	private String guideFirstname;
	private String guideLastname;
	private Gender gender;
	private int age;
	private String facebookAccountName;
	private int country; // foreign key to country
	private int city; 
	private String textBiography;
	
	/*
	 * helping statistics
	 */
	private int numberOfGallery;
	private int numberOfReviews;
	private float avgReviewScore;
	private boolean isPremium;


	/*
	 * Tour information
	 */
	private PaymentMethod paymentMethod; // 0 represents pay by case, 1 represents prepay, 2 represents both
	private int moneyType;  // foreign key for money type
	private String tourLength;
	private float tourPrice;
	private String tourType;
	
	private List<JSONGuideAttraction> attractions;
	private List<JSONGuideHiddenTreasure> hiddenTreasures;
	
}
