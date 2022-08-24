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
@Table(name = "GuideReview")
public class GuideReview implements Serializable {
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
	
	@Column(name = "travellerId", nullable = false)
	private long travellerId;  
	
	@Column(name = "travellerFirstname", nullable = false)
	private String travellerFirstname;  
	
	@Column(name = "reviewDate")
	private Date reviewDate;  

	@Column(name = "reviewScore")
	private float reviewScore;  
	
	@Column(name = "reviewMessage")
	private String reviewMessage;  
	
}
