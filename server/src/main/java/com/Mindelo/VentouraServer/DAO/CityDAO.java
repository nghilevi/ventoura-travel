package com.Mindelo.VentouraServer.DAO;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Mindelo.VentouraServer.Entity.City;
import com.Mindelo.VentouraServer.IDAO.ICityDAO;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class CityDAO extends DAO<City, Long> implements ICityDAO {
	@Override
	public City getCityByname(String name) {

		City city = null;
		String sql = "SELECT c FROM City c WHERE c.cityName = :name";
		TypedQuery<City> query = entityManager
				.createQuery(sql, City.class).setParameter("name", name);
		try {
			city = query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return city;
	}
}
