package com.Mindelo.VentouraServer.JSONEntity;

import java.util.List;

import com.Mindelo.VentouraServer.Enum.Gender;
import com.Mindelo.VentouraServer.Enum.PaymentMethod;

import lombok.Data;

@Data
public class JSONGuideProfile {

	private long id;

	private String textBiography;

	private int country; // foreign key to country

	private int city; 

	private String guideFirstname;
	private String guideLastname;

	private Gender gender;

	private String dateOfBirth;

	private String facebookAccountName;
	

	private float avgReviewScore;
	private boolean isPremium;

	/* Tour information */
	private PaymentMethod paymentMethod; // 0 represents pay by case, 1 represents prepay, 2 represents both
	private int moneyType;  // foreign key for money type
	private String tourLength;
	private float tourPrice;
	private String tourType;
	
	private List<JSONGuideAttraction> attractions;
	private List<JSONGuideHiddenTreasure> hiddenTreasures;
	
}
