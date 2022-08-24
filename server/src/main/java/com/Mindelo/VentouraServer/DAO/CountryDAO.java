package com.Mindelo.VentouraServer.DAO;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Mindelo.VentouraServer.Entity.Country;
import com.Mindelo.VentouraServer.IDAO.ICountryDAO;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class CountryDAO extends DAO<Country, Long> implements ICountryDAO {

	@Override
	public Country getCountryByname(String name) {

		
		Country country = null;
		String sql = "SELECT c FROM Country c WHERE c.countryName = :name";
		TypedQuery<Country> query = entityManager
				.createQuery(sql, Country.class).setParameter("name", name);

		try {
			country = query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		} 

		return country;
	}
}
