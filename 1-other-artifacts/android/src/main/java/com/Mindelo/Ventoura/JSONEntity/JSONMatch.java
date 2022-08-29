package com.Mindelo.Ventoura.JSONEntity;

import com.Mindelo.Ventoura.Enum.Gender;
import com.Mindelo.Ventoura.Enum.PaymentMethod;
import com.Mindelo.Ventoura.Enum.UserRole;

import lombok.Data;

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
