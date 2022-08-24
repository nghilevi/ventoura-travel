package com.Mindelo.Ventoura.Entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;


@Data
public class ChattingHistory implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	
	private long userId;
	private long partnerId;
	private int userRole;
	private int partnerRole;
	private String messageContent;
	private Date dateTime;
	private boolean isRead;
	
	private boolean statusMessage;
	private boolean mine;
	
}
