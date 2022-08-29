package com.Mindelo.Ventoura.Ghost.IService;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.Mindelo.Ventoura.Entity.City;

public interface IPromotionService {
	
	
	/**
	 * get the merged city image for the foun cities chose by traveller
	 * the city Image will be save into the SD card
	 */
	public boolean getCityPromotionImageFromServer(long travellerId, City[] cities);
	
	/**
	 * @param travellerId
	 * @return true this traveller has already attended the promotion, the city image will be returned if the city image doesn't saved in the SD card, 
	 * @return false means he hasn't attended
	 */
	public boolean travellerPromotionProbe(long travellerId);
	
	
	/**
	 * once the traveller share the cityimage to the facebook, the client should add the traveller to our server to indicate this traveller has already attended the promotion
	 * @return the candidateId saved in the server's database
	 */
	public long addNewPromotionCandidate(long travellerId, List<Integer> cities);
	
	
	public File getCityPromotionImageFileFromSDCard(long travellerId);
	
	public String getCityPromotionImageFilePath(long travellerId);
	
	
	/**
	 * delete the promotion city image from the SD card for this traveller
	 */
	public boolean deletePromotionImage(long travellerId);
}
