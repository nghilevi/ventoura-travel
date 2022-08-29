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
@Table(name = "TravellerLastUpdatedLog")
public class TravellerLastUpdatedLog implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;
	
	@Column(name = "travellerId", nullable = false)
	private long travellerId;
	
	public TravellerLastUpdatedLog(){
		
	}
	
	public TravellerLastUpdatedLog(long createdTime){
		profileLastUpdated = createdTime;
		galleryLastUpdated = createdTime;
		toursLastUpdated = createdTime;
		bookingsLastUpdated = createdTime;
		matchesLastUpdated = createdTime;
	}
	
	
	/*
	 * all the time are saved in milliseconds
	 */
	
	@Column(name = "profileLastUpdated", nullable = false)
	private long profileLastUpdated;
	
	@Column(name = "galleryLastUpdated", nullable = false)
	private long galleryLastUpdated;
	
	@Column(name = "toursLastUpdated", nullable = false)
	private long toursLastUpdated;
	
	@Column(name = "bookingsLastUpdated", nullable = false)
	private long bookingsLastUpdated;
	
	@Column(name = "matchesLastUpdated", nullable = false)
	private long matchesLastUpdated;

}
