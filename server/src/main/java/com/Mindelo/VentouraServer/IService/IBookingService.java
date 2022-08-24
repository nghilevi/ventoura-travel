package com.Mindelo.VentouraServer.IService;

import java.util.List;

import com.Mindelo.VentouraServer.Entity.Booking;
import com.Mindelo.VentouraServer.Enum.BookingStatus;

public interface IBookingService {

	public List<Booking> getAllBookingsByGuideId(long guideId);
	
	public List<Booking> getAllBookingsByTravellerId(long traveller);
	public Booking getBookingById(long bookingId);
	
	public void updateBookingStatus(long BookingId, BookingStatus status);
	
	public void saveNewBooking(Booking booking);
	public void updateBooking(Booking booking);
	
	public void cancelABooking(long bookingId);
	
}
