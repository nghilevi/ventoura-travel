package com.Mindelo.VentouraServer.Entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.Mindelo.VentouraServer.Enum.BookingStatus;

import lombok.Data;

@Data
@Entity
@Table(name = "Booking")
public class Booking implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;
	
	@Column(name = "guideId", nullable = false)
	private long guideId;  
	
	@Column(name = "guideFirstname")
	private String guideFirstname;  
	
	@Column(name = "travellerId", nullable = false)
	private long travellerId;  
	
	@Column(name = "travellerFirstname")
	private String travellerFirstname;  
	
	@Column(name = "tourPrice")
	private float tourPrice;  
	
	@Column(name = "tourDate")
	private Date tourDate;
	
	@Column(name = "paidDate")
	private Date paidDate;
	
	@Column(name = "bookedDate")
	private Date bookedDate;
	
	@Column(name = "bookingStatus")
	private BookingStatus bookingStatus;
	
	
	
}
