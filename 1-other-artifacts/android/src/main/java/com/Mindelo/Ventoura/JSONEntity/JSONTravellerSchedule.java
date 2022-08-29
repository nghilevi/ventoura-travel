package com.Mindelo.Ventoura.JSONEntity;


import lombok.Data;

@Data
public class JSONTravellerSchedule {

	private long id;

	private long travellerId;

	private String startTime;

	private String endTime;

	private int city;

	private int country;

}
