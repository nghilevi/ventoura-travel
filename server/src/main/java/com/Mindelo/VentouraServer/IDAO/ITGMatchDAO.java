package com.Mindelo.VentouraServer.IDAO;

import java.util.List;

import com.Mindelo.VentouraServer.Entity.TGMatch;



public interface ITGMatchDAO extends IDAO<TGMatch, Long>{
	
	List<TGMatch> getTravellerMatches(long travellerId);
	List<TGMatch> getGuideMatches(long guideId);
	
	void guideUnlikeTraveller(long guideId, long unlikedTravellerId); 
	boolean guideLikeTraveller(long guideId, long likedTravellerId); 
	
	void travellerUnlikeGuide(long travellerId, long unlikedGuideId); 
	boolean travellerLikeGuide(long travellerId, long likedGuideId); 

}
