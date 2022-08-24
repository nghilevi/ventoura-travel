package com.Mindelo.VentouraServer.IDAO;

import java.util.List;

import com.Mindelo.VentouraServer.Entity.GuideReview;



public interface IGuideReviewDAO extends IDAO<GuideReview, Long>{
	
	public List<GuideReview> getGuideReviewByGuideId(long guideId);
	public int getGuideReviewNumberByGuideId(long guideId);

}
