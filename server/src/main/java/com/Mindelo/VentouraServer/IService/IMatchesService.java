package com.Mindelo.VentouraServer.IService;

import java.util.List;

import com.Mindelo.VentouraServer.Entity.TGMatch;
import com.Mindelo.VentouraServer.Entity.TTMatch;

public interface IMatchesService {
	
	List<TTMatch> getTravellerTTMatch(long travellerId);
	
	List<TGMatch> getGuideTGMatch(long guideId);

	List<TGMatch> getTravellerTGMatch(long travellerId);

}
