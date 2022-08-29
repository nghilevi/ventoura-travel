package com.Mindelo.Ventoura.JSONEntity;



import java.io.Serializable;

import com.Mindelo.Ventoura.Enum.Gender;

import lombok.Data;

@Data
public class JSONTravellerProfile implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*
	 * traveller id saved in the server
	 */
	private long id;

	/*
	 * basic informations
	 */
	private String facebookAccountName;
	private Gender gender;
	private int city;
	private int country;
	private String travellerFirstname;
	private String travellerLastname;
	private int age;
	private String textBiography;
	
	
	/*
	 * helping statistics 
	 */
	private int numberOfGallery;

}
