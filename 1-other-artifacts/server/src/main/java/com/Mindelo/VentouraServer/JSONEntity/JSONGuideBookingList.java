package com.Mindelo.VentouraServer.JSONEntity;



import java.util.List;

import lombok.Data;


@Data
public class JSONGuideBookingList {
	private List<JSONBooking> bookings;
}
