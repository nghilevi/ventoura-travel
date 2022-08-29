package com.Mindelo.VentouraServer.JSONEntity;

import java.util.List;

import lombok.Data;


@Data
public class JSONTravellerTripList {
	List<JSONTravellerSchedule> travellerScheduleList;
	List<JSONBooking> travellerBookingList;
}
