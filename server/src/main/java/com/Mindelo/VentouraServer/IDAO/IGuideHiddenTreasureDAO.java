package com.Mindelo.VentouraServer.IDAO;

import java.util.List;

import com.Mindelo.VentouraServer.Entity.GuideHiddenTreasure;


public interface IGuideHiddenTreasureDAO extends IDAO<GuideHiddenTreasure, Long>{
	List<GuideHiddenTreasure> findGuideHiddenTreasuresGuideId(long guideId);
}
