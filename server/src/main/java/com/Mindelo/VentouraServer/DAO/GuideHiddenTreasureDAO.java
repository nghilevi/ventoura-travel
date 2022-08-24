package com.Mindelo.VentouraServer.DAO;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Mindelo.VentouraServer.Entity.GuideHiddenTreasure;
import com.Mindelo.VentouraServer.IDAO.IGuideHiddenTreasureDAO;


@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class GuideHiddenTreasureDAO extends DAO<GuideHiddenTreasure, Long> implements IGuideHiddenTreasureDAO{

	@Override
	public List<GuideHiddenTreasure> findGuideHiddenTreasuresGuideId(
			long guideId) {
		

		List<GuideHiddenTreasure> guideHiddenTreasures = new ArrayList<GuideHiddenTreasure>();
		String sql = "SELECT ga FROM GuideHiddenTreasure ga WHERE ga.guideId = :guideId";
		TypedQuery<GuideHiddenTreasure> query = entityManager.createQuery(sql, GuideHiddenTreasure.class)
				.setParameter("guideId", guideId);
		try {
			guideHiddenTreasures = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return guideHiddenTreasures;
	}

}
