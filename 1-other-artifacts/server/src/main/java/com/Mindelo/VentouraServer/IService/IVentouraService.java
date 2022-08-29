package com.Mindelo.VentouraServer.IService;

import com.Mindelo.VentouraServer.JSONEntity.JSONVentouraList;


public interface IVentouraService {
	
	public JSONVentouraList getTravellerVentoura(long travellerID);
	
	public JSONVentouraList getGuideVentoura(long guideId);
	
	public boolean travellerJudgeTraveller(long travellerId, boolean likeOrNot, long viewedTravellerId);
	
	public boolean travellerJudgeGuide(long travellerId, boolean likeOrNot, long guideId);
	
	public boolean guideJudgeTraveller(long guideId, boolean likeOrNot, long travellerId);

}
