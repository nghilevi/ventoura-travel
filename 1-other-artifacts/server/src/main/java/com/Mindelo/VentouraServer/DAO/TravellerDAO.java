package com.Mindelo.VentouraServer.DAO;



import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Mindelo.VentouraServer.Entity.Traveller;
import com.Mindelo.VentouraServer.IDAO.ITravellerDAO;


@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class TravellerDAO extends DAO<Traveller, Long> implements ITravellerDAO{

	@Override
	public Traveller getTravellerByFacebookAccountName(
			String facebookAccountName) {
		
		
		Traveller traveller = null;
		String sql = "SELECT tr FROM Traveller tr WHERE tr.facebookAccountName = :facebookAccountName";
		TypedQuery<Traveller> query = entityManager.createQuery(sql, Traveller.class)
				.setParameter("facebookAccountName", facebookAccountName);
		try{
			traveller = query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}

		return traveller;
	}

}
