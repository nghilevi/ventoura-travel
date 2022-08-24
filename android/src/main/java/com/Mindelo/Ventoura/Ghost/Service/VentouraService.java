package com.Mindelo.Ventoura.Ghost.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.graphics.Bitmap;

import com.Mindelo.Ventoura.Constant.Http_API_URL;
import com.Mindelo.Ventoura.Enum.UserRole;
import com.Mindelo.Ventoura.Ghost.IService.IVentouraService;
import com.Mindelo.Ventoura.JSONEntity.JSONVentoura;
import com.Mindelo.Ventoura.JSONEntity.JSONVentouraList;
import com.Mindelo.Ventoura.Util.CommonUtil;
import com.Mindelo.Ventoura.Util.HttpUtility;
import com.google.gson.Gson;

public class VentouraService implements IVentouraService {

	public JSONVentouraList getTravellerVentouraList(long id) {

		Gson gson = new Gson();

		try {
			HttpGet get = new HttpGet(
					Http_API_URL.URL_SERVER_GET_TRAVELLER_VENTOURA + "/" + id);

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {

				BufferedReader reader = HttpUtility
						.fromHttpJsonResponseToReader(response);
				JSONVentouraList travellerVentoura = gson.fromJson(reader,
						JSONVentouraList.class);
				
				return travellerVentoura;

			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Map<String, Bitmap> loadVentouraImages(List<JSONVentoura> ventouraList)  throws Exception{
		
		/* load the images */
		List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(
				Http_API_URL.URL_SERVER_GET_LOAD_VENTOURA_IMAGES);
		
		for(JSONVentoura ventoura : ventouraList){
			if(ventoura.getUserRole() == UserRole.GUIDE){
				postParameters.add(new BasicNameValuePair("g_" + ventoura.getId(), ""));
			}else{
				postParameters.add(new BasicNameValuePair("t_" + ventoura.getId(), ""));
			}
		}
		post.setEntity(new UrlEncodedFormEntity(postParameters));
		
		HttpResponse response = client.execute(post);
		
		
		if (response.getStatusLine().getStatusCode() == 202
				|| response.getStatusLine().getStatusCode() == 200) {

			InputStream is = response.getEntity().getContent();
			
			return CommonUtil.unzipImageFilesIntoBitmaps(is);
		}
		
		return null;
	}

	public JSONVentouraList getGuideVentouraList(long id) {

		Gson gson = new Gson();

		try {
			HttpGet get = new HttpGet(
					Http_API_URL.URL_SERVER_GET_GUIDE_VENTOURA + "/" + id);

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {

				BufferedReader reader = HttpUtility
						.fromHttpJsonResponseToReader(response);
				JSONVentouraList guideVentoura = gson.fromJson(reader,
						JSONVentouraList.class);

				return guideVentoura;

			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public int travellerJudgeTraveller(long travellerId,
			boolean likeOrNot, long viewedTravellerId) {
		
		HttpGet get = new HttpGet(
				Http_API_URL.URL_SERVER_TRAVELLER_JUDGE_TRAVELLER + "/" + travellerId + "/" + likeOrNot + "/" + viewedTravellerId);

		HttpClient client = new DefaultHttpClient();
		try {
			HttpResponse response = client.execute(get);
			return HttpUtility.parseResponseMessageIntValue(response);
	
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return 0;
	}

	public int travellerJudgeGuide(long travellerId, boolean likeOrNot,
			long viewedGuideId) {
		HttpGet get = new HttpGet(
				Http_API_URL.URL_SERVER_TRAVELLER_JUDGE_GUIDE + "/" + travellerId + "/" + likeOrNot + "/" + viewedGuideId);

		HttpClient client = new DefaultHttpClient();
		try {
			HttpResponse response = client.execute(get);
			return HttpUtility.parseResponseMessageIntValue(response);
	
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return 0;
	}

	public int guideJudgeTraveller(long guideId, boolean likeOrNot,
			long viewedTravellerId) {
		HttpGet get = new HttpGet(
				Http_API_URL.URL_SERVER_GUIDE_JUDGE_TRAVELLER + "/" + guideId + "/" + likeOrNot + "/" + viewedTravellerId);

		System.out.println(guideId + "/" + likeOrNot + "/" + viewedTravellerId);
		
		HttpClient client = new DefaultHttpClient();
		try {
			HttpResponse response = client.execute(get);
			return HttpUtility.parseResponseMessageIntValue(response);
	
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return 0;
	}

}
