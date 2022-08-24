package com.Mindelo.Ventoura.JSONEntity;

import java.io.Serializable;

import com.Mindelo.Ventoura.Enum.Gender;
import com.Mindelo.Ventoura.Enum.UserRole;

import lombok.Data;

@Data
public class JSONGuideVFunctionSettings implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long guideId;
	
	private Gender preferedGender;
	
	private int miniAge;
	private int maxAge;
	
	private int lastActivateDays;

}
