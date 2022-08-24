package com.Mindelo.Ventoura.Ghost.IService;

import java.util.Map;

import com.Mindelo.Ventoura.Entity.ImageMatch;
import com.Mindelo.Ventoura.JSONEntity.JSONMatch;
import com.Mindelo.Ventoura.JSONEntity.JSONMatchesList;

public interface IMatchesService {

	/**
	 * only get the jsonmatches of the traveller from server, not include the match images
	 */
	public boolean getTravellerMatchListFromServer(long travellerId);
	
	/**
	 * get the head images for the traveller's matches
	 * @return String is the name of the head image which contains image id information
	 */
	public Map<String, byte[]> getTravellerMatchHeadImagesFromServer(long travellerId);
	public JSONMatchesList getTravellerMatchListFromDB(long travellerId);

	/**
	 * only get the jsonmatches of the guide from server, not include the match images
	 */
	public boolean getGuideMatchListFromServer(long guideId);
	
	/**
	 * get the head images for the guide's matches
	 * @return String is the name of the head image which contains image id information
	 */
	public Map<String, byte[]> getGuideMatchHeadImagesFromServer(long guideId);
	public JSONMatchesList getGuideMatchListFromDB(long guideId);
	
	
	public ImageMatch getSingleMatchImageFromDB(long userId, int userRole);
	public JSONMatch getSingleMatchFromDB(long ownerId, int ownerRole, long userId,
			int userRole);

	public void deleteTTMatchMatch(long redTravellerId, long blueTravellerId);

	public void deleteTGMatch(long guideId, long travellerId);

}
