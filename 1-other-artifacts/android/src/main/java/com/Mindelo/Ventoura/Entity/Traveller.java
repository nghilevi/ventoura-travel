package com.Mindelo.Ventoura.Entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.Mindelo.Ventoura.Enum.Gender;

import lombok.Data;

@Data
public class Traveller implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;

	private String facebookAccountName;

	private Gender gender;
	
	private int city;
	
	private int country;

	private String travellerFirstname;
	
	private String travellerLastname;

	private Date dateOfBirth;
	
	ImageProfile portalImage;
	
	private String textBiography;

}
