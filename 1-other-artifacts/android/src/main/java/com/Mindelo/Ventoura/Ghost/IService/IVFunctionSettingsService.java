package com.Mindelo.Ventoura.Ghost.IService;

import com.Mindelo.Ventoura.JSONEntity.JSONGuideVFunctionSettings;
import com.Mindelo.Ventoura.JSONEntity.JSONTravellerVFunctionSettings;

public interface IVFunctionSettingsService {

	public boolean loadJSONTravellerVFunctionSettingsFromServer(
			long travellerId);

	public boolean loadJSONGuideVFunctionSettingsFromServer(
			long travellerId);
	
	public JSONTravellerVFunctionSettings loadJSONTravellerVFunctionSettingsFromDB(long travellerId);
	
	public JSONGuideVFunctionSettings loadJSONGuideVFunctionSettingsFromDB(long guideId);

	// update settings
	public boolean updateTravellerVFunctionSettings(
			JSONTravellerVFunctionSettings vSettings);

	public boolean updateGuideVFunctionSettings(
			JSONGuideVFunctionSettings VSettings);

}
