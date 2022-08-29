package com.Mindelo.VentouraServer.DAO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Mindelo.VentouraServer.Entity.TGMatch;
import com.Mindelo.VentouraServer.IDAO.ITGMatchDAO;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class TGMatchDAO extends DAO<TGMatch, Long> implements ITGMatchDAO {

	@Override
	public List<TGMatch> getTravellerMatches(long travellerId) {
		
		List<TGMatch> travellerMatchs = new ArrayList<TGMatch>();
		String sql = "SELECT tg FROM TGMatch tg WHERE tg.travellerId = :travellerId AND tg.travellerAttitude = 1 AND guideAttitude = 1";
		TypedQuery<TGMatch> query = entityManager.createQuery(sql, TGMatch.class)
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
	public List<TGMatch> getGuideMatches(long guideId) {
		
		
		List<TGMatch> guideMatches = new ArrayList<TGMatch>();
		String sql = "SELECT tg FROM TGMatch tg WHERE tg.guideId = :guideId AND tg.travellerAttitude = 1 AND guideAttitude = 1";
		TypedQuery<TGMatch> query = entityManager.createQuery(sql, TGMatch.class)
				.setParameter("guideId", guideId);
		try {
			guideMatches = query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
		}
		return guideMatches;
	}

	@Override
	public void travellerUnlikeGuide(long travellerId, long unlikedGuideId) {
		
		

		TGMatch tgMatch = new TGMatch();
		tgMatch.setTravellerId(travellerId);
		tgMatch.setGuideId(unlikedGuideId);
		tgMatch.setTravellerAttitude(-1);
		tgMatch.setGuideAttitude(0);

		TypedQuery<TGMatch> query;
		String sql = "SELECT tg FROM TGMatch tg where tg.travellerId = :travellerId and tg.guideId= :unlikedGuideId";
		query = entityManager.createQuery(sql, TGMatch.class)
				.setParameter("travellerId", travellerId)
				.setParameter("unlikedGuideId", unlikedGuideId);

		try {
			tgMatch = query.getSingleResult();
			// this pair has already been created
			tgMatch.setTravellerAttitude(-1);
			entityManager.merge(tgMatch);

		} catch (Exception e) {
			entityManager.persist(tgMatch);
		} finally {
				
		}

	}

	@Override
	public boolean travellerLikeGuide(long travellerId, long likedGuideId) {
		
		

		TGMatch tgMatch = new TGMatch();
		boolean matchedFlag = false;
		tgMatch.setTravellerId(travellerId);
		tgMatch.setGuideId(likedGuideId);
		tgMatch.setTravellerAttitude(1);
		tgMatch.setGuideAttitude(0);

		TypedQuery<TGMatch> query;
		String sql = "SELECT tg FROM TGMatch tg where tg.travellerId = :travellerId and tg.guideId= :likedGuideId";
		query = entityManager.createQuery(sql, TGMatch.class)
				.setParameter("travellerId", travellerId)
				.setParameter("likedGuideId", likedGuideId);

		try {
			tgMatch = query.getSingleResult();
			// this pair has already been created
			tgMatch.setTravellerAttitude(1);
			if(tgMatch.getGuideAttitude() == 1){
				tgMatch.setMatchedDate(new Date());
				matchedFlag = true;
			}
			entityManager.merge(tgMatch);

		} catch (Exception e) {
			entityManager.persist(tgMatch);
		} finally {
			
			
		}
		return matchedFlag;
	}

	@Override
	public void guideUnlikeTraveller(long guideId, long unlikedTravellerId) {
		
		
		

		TGMatch tgMatch = new TGMatch();
		tgMatch.setTravellerId(unlikedTravellerId);
		tgMatch.setGuideId(guideId);
		tgMatch.setTravellerAttitude(0);
		tgMatch.setGuideAttitude(-1);

		TypedQuery<TGMatch> query;
		String sql = "SELECT tg FROM TGMatch tg where tg.travellerId = :unlikedTravellerId and tg.guideId= :guideId";
		query = entityManager.createQuery(sql, TGMatch.class)
				.setParameter("unlikedTravellerId", unlikedTravellerId)
				.setParameter("guideId", guideId);

		try {
			tgMatch = query.getSingleResult();
			// this pair has already been created
			tgMatch.setGuideAttitude(-1);
			
			entityManager.merge(tgMatch);

		} catch (Exception e) {
			entityManager.persist(tgMatch);
		} finally {
			
			
		}

	}

	@Override
	public boolean guideLikeTraveller(long guideId, long likedTravellerId) {
		
	
		boolean matchedFlag = false;
		TGMatch tgMatch = new TGMatch();
		tgMatch.setTravellerId(likedTravellerId);
		tgMatch.setGuideId(guideId);
		tgMatch.setTravellerAttitude(0);
		tgMatch.setGuideAttitude(1);

		TypedQuery<TGMatch> query;
		String sql = "SELECT tg FROM TGMatch tg where tg.travellerId = :likedTravellerId and tg.guideId= :guideId";
		query = entityManager.createQuery(sql, TGMatch.class)
				.setParameter("likedTravellerId", likedTravellerId)
				.setParameter("guideId", guideId);

		try {
			tgMatch = query.getSingleResult();
			// this pair has already been created
			tgMatch.setGuideAttitude(1);
			if(tgMatch.getTravellerAttitude() == 1){
				tgMatch.setMatchedDate(new Date());
				matchedFlag = true;
			}
			entityManager.merge(tgMatch);

		} catch (Exception e) {
			entityManager.persist(tgMatch);
		} finally {
			
			
		}
		
		return matchedFlag;
	}

}
