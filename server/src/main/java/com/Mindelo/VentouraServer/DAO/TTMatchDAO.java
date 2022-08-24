package com.Mindelo.VentouraServer.DAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Mindelo.VentouraServer.Entity.TTMatch;
import com.Mindelo.VentouraServer.IDAO.ITTMatchDAO;


@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class TTMatchDAO extends DAO<TTMatch, Long> implements ITTMatchDAO {
	@Override
	public List<TTMatch> getTravellerMatches(long travellerId) {
		
		
		List<TTMatch> travellerMatchs = new ArrayList<TTMatch>();
		String sql = "SELECT tt FROM TTMatch tt WHERE (tt.redTravellerId = :travellerId OR tt.blueTravellerId = :travellerId) AND redAttitude = 1 AND blueAttitude = 1";
		TypedQuery<TTMatch> query = entityManager.createQuery(sql, TTMatch.class)
				.setParameter("travellerId", travellerId);
		try {
			travellerMatchs = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}

		return travellerMatchs;
	}

	@Override
	public void travellerUnlikeTraveller(long travellerId,
			long unlikedTravellerId) {
		
		

		TTMatch ttMatch = new TTMatch();
		TypedQuery<TTMatch> query;

		String sql = "SELECT tt FROM TTMatch tt where tt.redTravellerId = :redTravellerId and tt.blueTravellerId= :blueTravellerId";
		boolean flag;
		if (travellerId < unlikedTravellerId) {
			query = entityManager.createQuery(sql, TTMatch.class)
					.setParameter("redTravellerId", travellerId)
					.setParameter("blueTravellerId", unlikedTravellerId);

			ttMatch.setRedTravellerId(travellerId);
			ttMatch.setBlueTravellerId(unlikedTravellerId);

			ttMatch.setRedAttitude(-1);
			ttMatch.setBlueAttitude(0);
			flag = true;

		} else {

			query = entityManager.createQuery(sql, TTMatch.class)
					.setParameter("redTravellerId", unlikedTravellerId)
					.setParameter("blueTravellerId", travellerId);

			ttMatch.setBlueTravellerId(travellerId);
			ttMatch.setRedTravellerId(unlikedTravellerId);

			ttMatch.setRedAttitude(0);
			ttMatch.setBlueAttitude(-1);

			flag = false;
		}

		try {
			ttMatch = query.getSingleResult();
			// this pair has already been created
			if (flag) {
				ttMatch.setRedAttitude(-1);
			} else {
				ttMatch.setBlueAttitude(-1);
			}

			entityManager.merge(ttMatch);
		} catch (Exception e) {
			entityManager.persist(ttMatch);
		} finally {
			
			
		}

	}

	@Override
	public boolean travellerLikeTraveller(long travellerId, long likedTravellerId) {

		
		

		TTMatch ttMatch = new TTMatch();
		TypedQuery<TTMatch> query;

		String sql = "SELECT tt FROM TTMatch tt where tt.redTravellerId = :redTravellerId and tt.blueTravellerId= :blueTravellerId";
		boolean flag, matchedFlag = false;
		if (travellerId < likedTravellerId) {
			query = entityManager.createQuery(sql, TTMatch.class)
					.setParameter("redTravellerId", travellerId)
					.setParameter("blueTravellerId", likedTravellerId);

			ttMatch.setRedTravellerId(travellerId);
			ttMatch.setBlueTravellerId(likedTravellerId);

			ttMatch.setRedAttitude(1);
			ttMatch.setBlueAttitude(0);
			flag = true;

		} else {

			query = entityManager.createQuery(sql, TTMatch.class)
					.setParameter("redTravellerId", likedTravellerId)
					.setParameter("blueTravellerId", travellerId);

			ttMatch.setBlueTravellerId(travellerId);
			ttMatch.setRedTravellerId(likedTravellerId);

			ttMatch.setRedAttitude(0);
			ttMatch.setBlueAttitude(1);

			flag = false;
		}

		try {
			ttMatch = query.getSingleResult();
			// this pair has already been created
			if (flag) {
				ttMatch.setRedAttitude(1);
				if (ttMatch.getBlueAttitude() == 1) {
					ttMatch.setMatchedDate(new Date());
					matchedFlag = true;
				}
			} else {
				ttMatch.setBlueAttitude(1);
				if (ttMatch.getRedAttitude() == 1) {
					ttMatch.setMatchedDate(new Date());
					matchedFlag = true;
				}
			}
			entityManager.merge(ttMatch);

		} catch (Exception e) {
			entityManager.persist(ttMatch);
		} finally {
			
		}
		
		return matchedFlag;
	}

}
