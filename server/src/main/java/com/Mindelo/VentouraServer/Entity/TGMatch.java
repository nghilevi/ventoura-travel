package com.Mindelo.VentouraServer.Entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.Mindelo.VentouraServer.Enum.UserRole;

import lombok.Data;



@Data
@Entity
@Table(name = "TGMatch")
public class TGMatch  implements Serializable {
	
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
	
	
	
	@Column(name = "matchedDate") // the date two sites give attitude
	private Date matchedDate;
	
	
	@Column(name = "guideAttitude") // 0 no viewed, -1 means not like, 1 means like
	private int guideAttitude;
	
	@Column(name = "travellerAttitude")
	private int travellerAttitude; // 0 no viewed, -1 means not like, 1 means like
	

}
