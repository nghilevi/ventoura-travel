package com.Mindelo.VentouraServer.Entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;


@Data
@Entity
@Table(name = "TravellerVFunctionGuideSettings")
public class TravellerVFunctionGuideSettings implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;
	
	@Column(name = "travellerId", nullable=false)
	private long travellerId;
	
	
	@Column(name = "femalePercent")
	private float femalePercent = 0;
	
	@Column(name = "miniAge")
	private int miniAge = 0;
	
	@Column(name = "maxAge")
	private int maxAge = 100;

}
