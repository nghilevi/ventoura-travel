package com.Mindelo.VentouraServer.JSONEntity;

import java.util.List;

import lombok.Data;

import com.Mindelo.VentouraServer.Entity.GuideReview;

@Data
public class JSONGuideReviewList {
	List<GuideReview> guideReviewList;
}
