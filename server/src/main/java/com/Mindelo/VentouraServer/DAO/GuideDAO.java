package com.Mindelo.VentouraServer.DAO;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Mindelo.VentouraServer.Entity.Guide;
import com.Mindelo.VentouraServer.IDAO.IGuideDAO;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class GuideDAO extends DAO<Guide, Long> implements IGuideDAO {

	@Override
	public Guide getGuideByFacebookAccountName(String facebookAccountName) {
		
		Guide guide = null;
		String sql = "SELECT g FROM Guide g WHERE g.facebookAccountName = :facebookAccountName";
		TypedQuery<Guide> query = entityManager.createQuery(sql, Guide.class)
				.setParameter("facebookAccountName", facebookAccountName);

		try {
			guide = query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		} 

		return guide;
	}

}
