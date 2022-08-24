package com.Mindelo.VentouraServer.DAO;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Mindelo.VentouraServer.Entity.TravellerVFunctionTravellerSettings;
import com.Mindelo.VentouraServer.IDAO.ITravellerVFunctionTravellerSettingsDAO;


@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class TravellerVFunctionTravellerSettingsDAO extends DAO<TravellerVFunctionTravellerSettings, Long> implements ITravellerVFunctionTravellerSettingsDAO{

}
