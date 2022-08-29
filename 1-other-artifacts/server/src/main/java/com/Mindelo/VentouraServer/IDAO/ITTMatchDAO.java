package com.Mindelo.VentouraServer.IDAO;

import java.util.List;


import com.Mindelo.VentouraServer.Entity.TTMatch;



public interface ITTMatchDAO extends IDAO<TTMatch, Long>{
	List<TTMatch> getTravellerMatches(long travellerId);
	
	void travellerUnlikeTraveller(long travellerId, long unlikedTravellerId); 
	boolean travellerLikeTraveller(long travellerId, long likedTravellerId); 

	
}
