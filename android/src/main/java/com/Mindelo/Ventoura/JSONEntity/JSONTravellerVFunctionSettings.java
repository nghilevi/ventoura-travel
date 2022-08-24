package com.Mindelo.Ventoura.JSONEntity;

import java.io.Serializable;

import com.Mindelo.Ventoura.Enum.Gender;
import com.Mindelo.Ventoura.Enum.UserRole;

import lombok.Data;

@Data
public class JSONTravellerVFunctionSettings implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long travellerId;
	
	private Gender preferedGender;
	private UserRole preferedUserRole;
	
	private int miniAge;
	private int maxAge;
	
	private float miniPrice;
	private float maxPrice;
	
	private boolean specifyCityToggle;
	
	// cityIds seperate by ","
	private String cityIds;

}
