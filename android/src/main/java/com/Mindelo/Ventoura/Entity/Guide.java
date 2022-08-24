package com.Mindelo.Ventoura.Entity;

import java.io.Serializable;
import java.util.Date;

import com.Mindelo.Ventoura.Enum.Gender;
import com.Mindelo.Ventoura.Enum.PaymentMethod;

import lombok.Data;

@Data
public class Guide implements Serializable {

	private static final long serialVersionUID = 1L;

	private long id;

	private boolean isActive;

	private String textBiography;

	private int country; // foreign key to country

	private int city; // foreign key to country

	private String guideFirstname;

	private String guideLastname;

	private Gender gender;

	private Date dateOfBirth;

	private String facebookAccountName;

	private ImageProfile portalImage;
	private double avgReviewScore;
	private boolean isPremium;

	/* Tour information */
	private PaymentMethod paymentMethod; // 0 represents pay by case, 1
											// represents prepay, 2 represents
											// both
	private int moneyType; // foreign key for money type
	private String tourLength;
	private double tourPrice;
	private String tourType;

}
