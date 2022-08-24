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
@Table(name = "TravellerSchedule")
public class TravellerSchedule  implements Serializable{

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
	
	@Column(name = "startTime", nullable = false)
	private Date startTime;
	
	@Column(name = "endTime", nullable = false)
	private Date endTime;
	
	@Column(name = "city", nullable = false)
	private int city;
	
	@Column(name = "country")
	private int country;
	
}
