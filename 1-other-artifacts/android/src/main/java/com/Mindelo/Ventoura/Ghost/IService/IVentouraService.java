package com.Mindelo.Ventoura.Ghost.IService;

import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;

import com.Mindelo.Ventoura.JSONEntity.JSONVentoura;
import com.Mindelo.Ventoura.JSONEntity.JSONVentouraList;

public interface IVentouraService {

	JSONVentouraList getTravellerVentouraList(long id);
	
	JSONVentouraList getGuideVentouraList(long id);
	
	
	/**
	 *given a list of ventouras, the method return all the poral images belonging to each ventoura 
	 * 
	 */
	Map<String, Bitmap> loadVentouraImages(List<JSONVentoura> ventouraList)  throws Exception;
	
	/**
	 * a traveller like or not another traveller
	 * @param travellerId the id of the traveller likes or not likes
	 * @param viewedTravellerId the id of the traveller liked or not liked
	 * @return 1 means it is a match, 0 means it is not a match
	 */
	int travellerJudgeTraveller(long travellerId, boolean likeOrNot, long viewedTravellerId);
	
	/**
	 * a traveller like or not another guide
	 * @param travellerId the id of the traveller likes or not likes
	 * @param viewedGuideId the id of the guide liked or not liked
	 * @return 1 means it is a match, 0 means it is not a match
	 */
	int travellerJudgeGuide(long travellerId, boolean likeOrNot, long viewedGuideId);
	
	/**
	 * a guide like or not another traveller
	 * @param guideId the id of the guide likes or not likes
	 * @param viewedTravellerId the id of the traveller liked or not liked
	 * @return 1 means it is a match, 0 means it is not a match
	 */
	int guideJudgeTraveller(long guideId, boolean likeOrNot, long viewedTravellerId);
	
	
}
