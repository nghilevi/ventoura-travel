package com.Mindelo.VentouraServer.JSONEntity;

import com.Mindelo.VentouraServer.Enum.Gender;
import com.Mindelo.VentouraServer.Enum.PaymentMethod;
import com.Mindelo.VentouraServer.Enum.UserRole;

import lombok.Data;

/*
 * This class is exactly the same as that of Match class in ventoura client
 */

@Data
public class JSONMatch {
	
	long userId;
	
	int city;
	
	int country;
	
	String userFirstname;
	
	UserRole userRole;
	
	String timeMatched;
	
	Gender gender;
	
	int age;
	
	float tourPrice;
	
	PaymentMethod paymentMethod;

}
