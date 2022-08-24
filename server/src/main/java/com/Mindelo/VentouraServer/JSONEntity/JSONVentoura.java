package com.Mindelo.VentouraServer.JSONEntity;




import java.util.Date;
import java.util.List;

import com.Mindelo.VentouraServer.Enum.PaymentMethod;
import com.Mindelo.VentouraServer.Enum.UserRole;

import lombok.Data;



@Data
public class JSONVentoura {
	
	private long id;
	
	private UserRole userRole;
	
	private String firstname;
	
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
	
	private List<JSONGuideAttraction> attractions;
	private List<JSONGuideHiddenTreasure> hiddenTreasures;
}
