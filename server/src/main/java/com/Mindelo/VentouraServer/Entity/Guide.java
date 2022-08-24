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
import com.Mindelo.VentouraServer.Enum.PaymentMethod;

import lombok.Data;


@Data
@Entity
@Table(name = "Guide")
public class Guide implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;
	
	@Column(name = "isActive", nullable = false)
	private boolean isActive;
	
	@Column(name = "textBiography")
	private String textBiography;
	
//	@Column(name = "email")
//	private String email;
	
	@Column(name = "country")
	private int country;  // foreign key to country
	
	@Column(name = "language")
	private int language;  // foreign key to language
	
	@Column(name = "city")
	private int city;  // foreign key to country
	
	@Column(name = "guideFirstname")
	private String guideFirstname;
	
	@Column(name = "guideLastname")
	private String guideLastname;
	
	@Column(name = "gender")
	private Gender gender;
	
	@Column(name = "dateOfBirth")
	private Date dateOfBirth;
	
	@Column(name = "facebookAccountName", nullable = false)
	private String facebookAccountName;
	
	@Column(name = "avgReviewScore")
	private float avgReviewScore;
	
	@Column(name = "lastTimeActive")
	private Date lastTimeActive;
	
	@Column(name = "isPremium")
	private boolean isPremium;
	
	/* Tour information */
	@Column(name = "paymentMethod")
	private PaymentMethod paymentMethod; // 0 represents pay by case, 1 represents prepay, 2 represents both
	
	@Column(name = "moneyType")
	private int moneyType;  // foreign key for money type

	@Column(name = "tourLength")
	private String tourLength;
	
	@Column(name = "tourPrice")
	private float tourPrice;
	
	@Column(name = "tourType")
	private String tourType;
}
