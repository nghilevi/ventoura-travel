package com.Mindelo.VentouraServer.Entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;


@Data
@Entity
@Table(name = "TTMatch")
public class TTMatch  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;
	
	@Column(name = "redTravellerId", nullable = false)
	private long redTravellerId;   // redtravellerId is smaller than the bluetravellerId
	
	@Column(name = "blueTravellerId", nullable = false)
	private long blueTravellerId;  

	
	@Column(name = "matchedDate") // the date two sites give attitude
	private Date matchedDate;
	
	@Column(name = "redAttitude")
	private int redAttitude; 
	
	@Column(name = "blueAttitude")
	private int blueAttitude;
}
