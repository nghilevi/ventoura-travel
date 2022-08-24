package com.Mindelo.VentouraServer.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.Mindelo.VentouraServer.Entity.Booking;
import com.Mindelo.VentouraServer.Enum.BookingStatus;
import com.Mindelo.VentouraServer.IDAO.IBookingDAO;
import com.Mindelo.VentouraServer.IService.IBookingService;

@Service
@Component
public class BookingService implements IBookingService{
	
	@Autowired
	private IBookingDAO bookingsDao;

	@Override
	public List<Booking> getAllBookingsByGuideId(long guideId) {
		return bookingsDao.findBookingsGuideId(guideId);
	}

	@Override
	public void updateBookingStatus(long bookingId, BookingStatus status) {
		bookingsDao.updateBookingStatus(bookingId, status);
	}

	@Override
	public void saveNewBooking(Booking booking) {
		bookingsDao.save(booking);
	}

	@Override
	public void cancelABooking(long bookingId) {
		// TODO need to do something more ?
		bookingsDao.deleteById(Booking.class,bookingId);
	}

	@Override
	public List<Booking> getAllBookingsByTravellerId(long travellerId) {
		return bookingsDao.findBookingsTravellerId(travellerId);
	}

	@Override
	public void updateBooking(Booking booking) {
		bookingsDao.update(booking);
	}

	@Override
	public Booking getBookingById(long bookingId) {
		return bookingsDao.findByID(Booking.class, bookingId);
	}
}
