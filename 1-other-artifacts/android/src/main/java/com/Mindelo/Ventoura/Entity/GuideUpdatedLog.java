package com.Mindelo.Ventoura.Entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class GuideUpdatedLog implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long guideId;

	public GuideUpdatedLog() {

	}

	public GuideUpdatedLog(long guideId, long createdTime) {
		this.guideId = guideId;
		profileLastUpdated = createdTime;
		galleryLastUpdated = createdTime;
		bookingsLastUpdated = createdTime;
		matchesLastUpdated = createdTime;
		vFunctionSettingsLastUpdated = createdTime;
	}

	/*
	 * all the time are saved in milliseconds
	 */

	private long profileLastUpdated;

	private long galleryLastUpdated;

	private long bookingsLastUpdated;

	private long matchesLastUpdated;

	private long reviewLastUpdated;
	
	private long vFunctionSettingsLastUpdated;

}
