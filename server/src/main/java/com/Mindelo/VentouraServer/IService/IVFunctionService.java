package com.Mindelo.VentouraServer.IService;

import com.Mindelo.VentouraServer.Entity.GuideVFunctionSettings;
import com.Mindelo.VentouraServer.Entity.TravellerVFunctionGuideSettings;
import com.Mindelo.VentouraServer.Entity.TravellerVFunctionTravellerSettings;

public interface IVFunctionService {

	public void saveTravellerVFunctionTravellerSettings(
			TravellerVFunctionTravellerSettings settings);

	public void saveTravellerVFunctionGuideSettings(
			TravellerVFunctionGuideSettings settings);

	public void saveGuideVFunctionSettings(GuideVFunctionSettings settings);

	// update settings
	public void updateTVFunctionTravellerSettings(
			TravellerVFunctionTravellerSettings VSettings);

	public void updateTVFunctionGuideSettings(
			TravellerVFunctionGuideSettings VSettings);

	public void updateGVFunctionSettings(GuideVFunctionSettings VSettings);
	
	
	// delete settings
	public void deleteTVFunctionTravellerSettings(long VSettingsId);

	public void deleteTVFunctionGuideSettings(
			long VSettingsId);

	public void deleteGVFunctionSettings(long VSettingsId);
	

}
