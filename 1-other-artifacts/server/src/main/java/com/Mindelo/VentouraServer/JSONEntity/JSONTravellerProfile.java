package com.Mindelo.VentouraServer.JSONEntity;

import com.Mindelo.VentouraServer.Enum.Gender;

import lombok.Data;

@Data
public class JSONTravellerProfile {
	private long id;

	private String facebookAccountName;

	private Gender gender;

	private int city;

	private int country;

	private String travellerFirstname;
	
	private String travellerLastname;

	private String dateOfBirth;

	private String textBiography;
}
