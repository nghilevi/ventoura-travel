package com.Mindelo.VentouraServer.IDAO;

import java.util.List;

import com.Mindelo.VentouraServer.Entity.GuideAttraction;

public interface IGuideAttractionDAO extends IDAO<GuideAttraction, Long>{
	List<GuideAttraction> findGuideAttractionsGuideId(long guideId);
}
