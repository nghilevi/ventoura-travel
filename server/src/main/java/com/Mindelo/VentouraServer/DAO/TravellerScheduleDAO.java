package com.Mindelo.VentouraServer.DAO;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Mindelo.VentouraServer.Entity.TravellerSchedule;
import com.Mindelo.VentouraServer.IDAO.ITravellerScheduleDAO;


@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class TravellerScheduleDAO extends DAO<TravellerSchedule, Long> implements ITravellerScheduleDAO{

	@Override
	public List<TravellerSchedule> findScheduleByTravellerId(long travellerId) {
		
		
		List<TravellerSchedule> travellerSchedules = null;
		String sql = "SELECT t FROM TravellerSchedule t WHERE t.travellerId = :travellerId";
		TypedQuery<TravellerSchedule> query = entityManager.createQuery(sql, TravellerSchedule.class)
				.setParameter("travellerId", travellerId);
		travellerSchedules = query.getResultList();
		
		

		return travellerSchedules;
	}

	@Override
	@Transactional
	public void deleteScheduleByTraverllerId(long travellerId) {
		
		
		
		String sql = "delete FROM TravellerSchedule t WHERE t.travellerId = :travellerId";
		Query query = entityManager.createQuery(sql)
				.setParameter("travellerId", travellerId);
		
		query.executeUpdate();
		
		
		
	}

}
