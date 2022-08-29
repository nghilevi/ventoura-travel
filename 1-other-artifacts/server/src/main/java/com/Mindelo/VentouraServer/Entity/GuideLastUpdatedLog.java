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
@Table(name = "GuideLastUpdatedLog")
public class GuideLastUpdatedLog implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;
	
	@Column(name = "guideId", nullable = false)
	private long guideId;
	
	public GuideLastUpdatedLog(){
		
	}
	
	public GuideLastUpdatedLog(long createdTime){
		profileLastUpdated = createdTime;
		galleryLastUpdated = createdTime;
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
	
	@Column(name = "bookingsLastUpdated", nullable = false)
	private long bookingsLastUpdated;
	
	@Column(name = "matchesLastUpdated", nullable = false)
	private long matchesLastUpdated;
	
	@Column(name = "reviewLastUpdated", nullable = false)
	private long reviewLastUpdated;

}
