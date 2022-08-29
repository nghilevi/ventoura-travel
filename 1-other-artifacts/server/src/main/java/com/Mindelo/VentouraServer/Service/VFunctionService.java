package com.Mindelo.VentouraServer.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.Mindelo.VentouraServer.Entity.GuideVFunctionSettings;
import com.Mindelo.VentouraServer.Entity.TravellerVFunctionGuideSettings;
import com.Mindelo.VentouraServer.Entity.TravellerVFunctionTravellerSettings;
import com.Mindelo.VentouraServer.IDAO.IGuideVFunctionSettingsDAO;
import com.Mindelo.VentouraServer.IDAO.ITravellerVFunctionGuideSettingsDAO;
import com.Mindelo.VentouraServer.IDAO.ITravellerVFunctionTravellerSettingsDAO;
import com.Mindelo.VentouraServer.IService.IVFunctionService;

@Service
@Component
public class VFunctionService implements IVFunctionService{
	
	@Autowired
	ITravellerVFunctionTravellerSettingsDAO travellerVFunctionTravellerSettingsDao;
	@Autowired
	ITravellerVFunctionGuideSettingsDAO travellerVFunctionGuideSettingsDao;
	@Autowired
	IGuideVFunctionSettingsDAO guideVFunctionSettingsDao;

	@Override
	public void saveTravellerVFunctionTravellerSettings(
			TravellerVFunctionTravellerSettings settings) {
		travellerVFunctionTravellerSettingsDao.save(settings);
	}

	@Override
	public void saveTravellerVFunctionGuideSettings(
			TravellerVFunctionGuideSettings settings) {
		travellerVFunctionGuideSettingsDao.save(settings);
	}

	@Override
	public void saveGuideVFunctionSettings(GuideVFunctionSettings settings) {
		guideVFunctionSettingsDao.save(settings);
	}

	@Override
	public void updateTVFunctionTravellerSettings(
			TravellerVFunctionTravellerSettings VSettings) {
		travellerVFunctionTravellerSettingsDao.update(VSettings);
	}

	@Override
	public void updateTVFunctionGuideSettings(
			TravellerVFunctionGuideSettings VSettings) {
		travellerVFunctionGuideSettingsDao.update(VSettings);
	}

	@Override
	public void updateGVFunctionSettings(GuideVFunctionSettings VSettings) {
		guideVFunctionSettingsDao.update(VSettings);
	}

	@Override
	public void deleteTVFunctionTravellerSettings(long VSettingsId) {
		travellerVFunctionTravellerSettingsDao.deleteById(TravellerVFunctionTravellerSettings.class, VSettingsId);
	}

	@Override
	public void deleteTVFunctionGuideSettings(long VSettingsId) {
		travellerVFunctionGuideSettingsDao.deleteById(TravellerVFunctionGuideSettings.class, VSettingsId);
	}

	@Override
	public void deleteGVFunctionSettings(long VSettingsId) {
		guideVFunctionSettingsDao.deleteById(GuideVFunctionSettings.class, VSettingsId);
	}

}
