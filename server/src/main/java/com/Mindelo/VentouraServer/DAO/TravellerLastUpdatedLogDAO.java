package com.Mindelo.VentouraServer.DAO;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Mindelo.VentouraServer.Entity.TravellerLastUpdatedLog;
import com.Mindelo.VentouraServer.IDAO.ITravellerLastUpdatedLogDAO;


@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class TravellerLastUpdatedLogDAO extends
		DAO<TravellerLastUpdatedLog, Long> implements
		ITravellerLastUpdatedLogDAO {

	@Override
	public TravellerLastUpdatedLog getTravellerLastUpdatedByTravellerId(
			long travellerId) {
		
		TravellerLastUpdatedLog tul = null;
		String sql = "SELECT tl FROM TravellerLastUpdatedLog tl WHERE tl.travellerId = :travellerId";
		TypedQuery<TravellerLastUpdatedLog> query = entityManager.createQuery(sql,
				TravellerLastUpdatedLog.class).setParameter("travellerId",
				travellerId);
		try {
			tul = query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return tul;
	}

	@Override
	@Transactional
	public void setTravellerProfileLastUpdated(long lastUpdated,
			long travellerId) {
		
		

		String sql = "update TravellerLastUpdatedLog tul set tul.profileLastUpdated=:lastUpdated WHERE tul.travellerId=:travellerId";
		Query query = entityManager.createQuery(sql)
				.setParameter("travellerId", travellerId)
				.setParameter("lastUpdated", lastUpdated);
		query.executeUpdate();

		

	}

	@Override
	@Transactional
	public void setTravellerBookingsLastUpdated(long lastUpdated,
			long travellerId) {
		
		

		String sql = "update TravellerLastUpdatedLog tul set tul.bookingsLastUpdated=:lastUpdated WHERE tul.travellerId=:travellerId";
		Query query = entityManager.createQuery(sql)
				.setParameter("travellerId", travellerId)
				.setParameter("lastUpdated", lastUpdated);
		query.executeUpdate();

		

	}

	@Override
	@Transactional
	public void setTravellerToursLastUpdated(long lastUpdated, long travellerId) {
		
		

		String sql = "update TravellerLastUpdatedLog tul set tul.toursLastUpdated=:lastUpdated WHERE tul.travellerId=:travellerId";
		Query query = entityManager.createQuery(sql)
				.setParameter("travellerId", travellerId)
				.setParameter("lastUpdated", lastUpdated);
		query.executeUpdate();

		

	}

	@Override
	@Transactional
	public void setTravellerMatchesLastUpdated(long lastUpdated,
			long travellerId) {
		
		

		String sql = "update TravellerLastUpdatedLog tul set tul.matchesLastUpdated=:lastUpdated WHERE tul.travellerId=:travellerId";
		Query query = entityManager.createQuery(sql)
				.setParameter("travellerId", travellerId)
				.setParameter("lastUpdated", lastUpdated);
		query.executeUpdate();

		

	}

	@Override
	@Transactional
	public void setTravellerGalleryLastUpdated(long lastUpdated,
			long travellerId) {
		
		

		String sql = "update TravellerLastUpdatedLog tul set tul.galleryLastUpdated=:lastUpdated WHERE tul.travellerId=:travellerId";
		Query query = entityManager.createQuery(sql)
				.setParameter("travellerId", travellerId)
				.setParameter("lastUpdated", lastUpdated);
		query.executeUpdate();

		

	}

}
