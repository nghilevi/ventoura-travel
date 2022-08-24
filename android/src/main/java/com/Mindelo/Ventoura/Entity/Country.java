package com.Mindelo.Ventoura.Entity;

import java.io.Serializable;
import lombok.Data;

@Data
public class Country implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private String countryName;
}
