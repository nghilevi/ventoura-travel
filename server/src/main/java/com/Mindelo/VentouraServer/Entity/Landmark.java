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

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;


@Data
@Entity
@Table(name = "Landmark")
public class Landmark implements Serializable{
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;
	
	
	@Column(name = "landmarkName", nullable=false)
	private String landmarkName;
	
	
	@Column(name = "idCity")
	private long idCity;
	
	@Column(name = "idCountry")
	private long idCountry;
}
