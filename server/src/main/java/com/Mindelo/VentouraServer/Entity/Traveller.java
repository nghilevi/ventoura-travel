package com.Mindelo.VentouraServer.Entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.Mindelo.VentouraServer.Enum.Gender;

import lombok.Data;



@Data
@Entity
@Table(name = "Traveller")
public class Traveller implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;
	
	
	@Column(name = "facebookAccountName", nullable = false)
	private String facebookAccountName;
	
	@Column(name = "gender")
	private Gender gender;
	
	@Column(name = "dateOfBirth")
	private Date dateOfBirth;
	
	@Column(name = "isActive", nullable = false)
	private boolean isActive;
	
	
	@Column(name = "travellerFirstname")
	private String travellerFirstname;
	
	@Column(name = "travellerLastname")
	private String travellerLastname;
	
	@Column(name = "textBiography")
	private String textBiography;
	
	@Column(name = "country")
	private int country;  // foreign key to country
	
	@Column(name = "city")
	private int city;  // foreign key to country
	
	@Column(name = "lastTimeActive")
	private Date lastTimeActive;
	
	
}
