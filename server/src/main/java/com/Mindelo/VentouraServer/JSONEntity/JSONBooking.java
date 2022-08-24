package com.Mindelo.VentouraServer.JSONEntity;

import java.util.Date;





import lombok.Data;

import com.Mindelo.VentouraServer.Entity.Booking;
import com.Mindelo.VentouraServer.Enum.BookingStatus;
import com.Mindelo.VentouraServer.Util.DateTimeUtil;

@Data
public class JSONBooking {
	
	private long id;

	private long guideId;
	private String guideFirstname;  

	private long travellerId;  
	private String travellerFirstname; 
	
	private float tourPrice;
	
	private String tourDate;

	private BookingStatus bookingStatus;
	
	private String imagePath; // Only used by the client.
	
	public JSONBooking(){
		
	}
	
	public JSONBooking(Booking booking){
		this.id = booking.getId();
		this.guideId = booking.getGuideId();
		this.guideFirstname = booking.getGuideFirstname();
		
		this.travellerId = booking.getTravellerId();
		this.travellerFirstname = booking.getTravellerFirstname();
		
		this.tourPrice = booking.getTourPrice();	
		this.tourDate = DateTimeUtil.fromDateToString(booking.getTourDate());
		this.bookingStatus = booking.getBookingStatus();
	}

}
