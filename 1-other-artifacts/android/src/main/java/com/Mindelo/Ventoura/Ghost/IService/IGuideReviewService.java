package com.Mindelo.Ventoura.Ghost.IService;

import java.util.List;

import com.Mindelo.Ventoura.Entity.GuideReview;

public interface IGuideReviewService {
	public List<GuideReview> getGuideReviews(long guideId);
	public long createGuideReview(GuideReview guideReview);
}
