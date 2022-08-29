package com.Mindelo.Ventoura.JSONEntity;



import java.io.Serializable;

import lombok.Data;


@Data
public class JSONGuideAttraction implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String attractionName;
}
