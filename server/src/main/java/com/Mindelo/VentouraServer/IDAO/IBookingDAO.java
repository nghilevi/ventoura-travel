package com.Mindelo.VentouraServer.IDAO;

import java.util.List;

import com.Mindelo.VentouraServer.Entity.Booking;
import com.Mindelo.VentouraServer.Enum.BookingStatus;

public interface IBookingDAO extends IDAO<Booking, Long>{

	List<Booking> findBookingsTravellerId(long travellerId);

	List<Booking> findBookingsGuideId(long guideId);

	void deleteBookingsByTraverllerId(long travellerId);

	void deleteBookingsByGuideId(long guideId);
	
	void updateBookingStatus(long bookingId, BookingStatus status);

}
