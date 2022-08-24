package com.Mindelo.VentouraServer.DAO;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Mindelo.VentouraServer.Entity.GuideAttraction;
import com.Mindelo.VentouraServer.IDAO.IGuideAttractionDAO;


@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class GuideAttractionDAO extends DAO<GuideAttraction, Long> implements IGuideAttractionDAO{

	@Override
	public List<GuideAttraction> findGuideAttractionsGuideId(long guideId) {

		List<GuideAttraction> guideAttractions = new ArrayList<GuideAttraction>();
		String sql = "SELECT ga FROM GuideAttraction ga WHERE ga.guideId = :guideId";
		TypedQuery<GuideAttraction> query = entityManager.createQuery(sql, GuideAttraction.class)
				.setParameter("guideId", guideId);
		try {
			guideAttractions = query.getResultList();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return guideAttractions;
	}

}
