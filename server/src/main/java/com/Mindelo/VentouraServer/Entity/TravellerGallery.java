package com.Mindelo.VentouraServer.Entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.Data;

import org.hibernate.annotations.GenericGenerator;

@Data
@Entity
@Table(name = "TravellerGallery")
public class TravellerGallery implements Serializable{
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

	
	@Column(name = "portalPhoto", nullable = true)
	private boolean portalPhoto; 
	
	@Lob @Basic(fetch = FetchType.LAZY)
	@Column(name = "image", length=65535)
	private byte[] image;
	
}