package com.Mindelo.VentouraServer.DAO;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Mindelo.VentouraServer.Entity.GuideVFunctionSettings;
import com.Mindelo.VentouraServer.IDAO.IGuideVFunctionSettingsDAO;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class GuideVFunctionSettingsDAO extends DAO<GuideVFunctionSettings, Long> implements IGuideVFunctionSettingsDAO{

}
