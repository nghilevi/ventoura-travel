package com.Mindelo.VentouraServer.DAO;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Mindelo.VentouraServer.Entity.TravellerVFunctionGuideSettings;
import com.Mindelo.VentouraServer.IDAO.ITravellerVFunctionGuideSettingsDAO;


@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class TravellerVFunctionGuideSettingsDAO extends DAO<TravellerVFunctionGuideSettings, Long> implements ITravellerVFunctionGuideSettingsDAO{

}
