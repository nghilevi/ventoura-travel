package com.Mindelo.VentouraServer.DAO;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Mindelo.VentouraServer.Entity.Booking;
import com.Mindelo.VentouraServer.Enum.BookingStatus;
import com.Mindelo.VentouraServer.IDAO.IBookingDAO;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class BookingDAO extends DAO<Booking, Long> implements IBookingDAO {

	@Override
	public List<Booking> findBookingsTravellerId(long travellerId) {
		List<Booking> travellerBookings = new ArrayList<Booking>();
		String sql = "SELECT b FROM Booking b WHERE b.travellerId = :travellerId";
		TypedQuery<Booking> query = entityManager
				.createQuery(sql, Booking.class)
				.setParameter("travellerId", travellerId);
		try {
			travellerBookings = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return travellerBookings;
	}

	@Override
	public List<Booking> findBookingsGuideId(long guideId) {

		List<Booking> guideBookings = new ArrayList<Booking>();
		String sql = "SELECT b FROM Booking b WHERE b.guideId = :guideId";
		TypedQuery<Booking> query = entityManager
				.createQuery(sql, Booking.class)
				.setParameter("guideId", guideId);
		try {
			guideBookings = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return guideBookings;
	}

	@Override
	@Transactional
	public void deleteBookingsByTraverllerId(long travellerId) {

		String sql = "delete FROM Booking b WHERE b.travellerId = :travellerId";
		Query query = entityManager.createQuery(sql)
				.setParameter("travellerId", travellerId);

		query.executeUpdate();

	}

	@Override
	@Transactional
	public void deleteBookingsByGuideId(long guideId) {

		String sql = "delete FROM Booking b WHERE b.guideId = :guideId";
		Query query = entityManager.createQuery(sql)
				.setParameter("guideId", guideId);

		query.executeUpdate();

	}

	@Override
	@Transactional
	public void updateBookingStatus(long bookingId, BookingStatus status) {


		String sql = "update Booking b set b.bookingStatus=:bookingStatus WHERE b.id = :bookingId";
		Query query = entityManager.createQuery(sql)
				.setParameter("bookingStatus", status)
				.setParameter("bookingId", bookingId);

		query.executeUpdate();


	}

}
