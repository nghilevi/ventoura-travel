package com.Mindelo.VentouraServer.DAO;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Mindelo.VentouraServer.Entity.GuideReview;
import com.Mindelo.VentouraServer.IDAO.IGuideReviewDAO;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class GuideReviewDAO extends DAO<GuideReview, Long> implements
		IGuideReviewDAO {

	@Override
	public List<GuideReview> getGuideReviewByGuideId(long guideId) {
		
		

		List<GuideReview> guideReviews = new ArrayList<GuideReview>();
		String sql = "SELECT gr FROM GuideReview gr WHERE gr.guideId = :guideId ";
		TypedQuery<GuideReview> query = entityManager.createQuery(sql, GuideReview.class)
				.setParameter("guideId", guideId);
		try {
			guideReviews = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}

		return guideReviews;
	}

	@Override
	public int getGuideReviewNumberByGuideId(long guideId) {
		
		

		String sql = "SELECT count(distinct gr.id) as ReviewAccount FROM GuideReview gr WHERE gr.guideId = :guideId ";
		long numberOfReviews = 0;
		
		Query query = entityManager.createQuery(sql);
		query.setParameter("guideId", guideId);
		try {
			numberOfReviews = (Long) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}

		return (int)numberOfReviews;
	}

}
