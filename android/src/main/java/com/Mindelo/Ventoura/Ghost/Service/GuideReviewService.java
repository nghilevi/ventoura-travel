package com.Mindelo.Ventoura.Ghost.Service;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.Mindelo.Ventoura.Constant.HttpFieldConstant;
import com.Mindelo.Ventoura.Constant.Http_API_URL;
import com.Mindelo.Ventoura.Entity.GuideReview;
import com.Mindelo.Ventoura.Ghost.IService.IGuideReviewService;
import com.Mindelo.Ventoura.JSONEntity.JSONGuideReviewList;
import com.Mindelo.Ventoura.Util.HttpUtility;
import com.google.gson.Gson;

public class GuideReviewService implements IGuideReviewService{

	@Override
	public List<GuideReview> getGuideReviews(long guideId) {
		Gson gson = new Gson();

		try {
			HttpGet get = new HttpGet(
					Http_API_URL.URL_SERVER_GET_GUIDE_REVIEWS + "/"
							+ guideId ); //TODO cache

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {

				BufferedReader reader = HttpUtility
						.fromHttpJsonResponseToReader(response);
				JSONGuideReviewList guideReviews = gson.fromJson(reader,
						JSONGuideReviewList.class);

				return guideReviews.getGuideReviewList();

			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public long createGuideReview(GuideReview guideReview) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		try {

			nameValuePairs = loadGuideReviewField(guideReview);

			HttpPost post = new HttpPost(
					Http_API_URL.URL_SERVER_CREATE_GUIDE_REVIEWS);
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(post);

			return HttpUtility.parseResponseMessageLongValue(response);

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

	}
	
	private List<NameValuePair> loadGuideReviewField(GuideReview guideReview) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair(
				HttpFieldConstant.GUIDE_REVIEW_TRAVELLER_ID, "" + guideReview.getTravellerId()));
		nameValuePairs.add(new BasicNameValuePair(
				HttpFieldConstant.GUIDE_REVIEW_GUIDE_ID, "" + guideReview.getGuideId()));
		
		nameValuePairs.add(new BasicNameValuePair(
				HttpFieldConstant.GUIDE_REVIEW_TRAVELLER_FIRSTNAME, "" + guideReview.getTravellerFirstname()));
		nameValuePairs.add(new BasicNameValuePair(
				HttpFieldConstant.GUIDE_REVIEW_SCORE, "" + guideReview.getReviewScore()));
		nameValuePairs.add(new BasicNameValuePair(
				HttpFieldConstant.GUIDE_REVIEW_MESSAGE, "" + guideReview.getReviewMessage()));
		

		return nameValuePairs;
	}

}
