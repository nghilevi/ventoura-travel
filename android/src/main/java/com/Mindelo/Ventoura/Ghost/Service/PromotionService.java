package com.Mindelo.Ventoura.Ghost.Service;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Constant.Http_API_URL;
import com.Mindelo.Ventoura.Entity.City;
import com.Mindelo.Ventoura.Ghost.IService.IPromotionService;
import com.Mindelo.Ventoura.Util.HttpUtility;

public class PromotionService implements IPromotionService {

	@Override
	public boolean getCityPromotionImageFromServer(long travellerId,
			City[] cities) {

		try {
			File promotionImage = getCityPromotionImageFileFromSDCard(travellerId);

			/* load the images */
			List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(
					Http_API_URL.URL_SERVER_GET_PROMOTION_CITY_IMAGES);

			postParameters.add(new BasicNameValuePair("city_0", cities[0]
					.getId() + ""));
			postParameters.add(new BasicNameValuePair("city_1", cities[1]
					.getId() + ""));
			postParameters.add(new BasicNameValuePair("city_2", cities[2]
					.getId() + ""));
			postParameters.add(new BasicNameValuePair("city_3", cities[3]
					.getId() + ""));

			post.setEntity(new UrlEncodedFormEntity(postParameters));

			HttpResponse response = client.execute(post);

			if (response.getStatusLine().getStatusCode() != 500
					&& response.getStatusLine().getStatusCode() != 404) {

				InputStream is = response.getEntity().getContent();
				FileUtils.copyInputStreamToFile(is, promotionImage);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	@Override
	public boolean travellerPromotionProbe(long travellerId) {

		File promotionImage = getCityPromotionImageFileFromSDCard(travellerId);

		try {
			HttpGet get = new HttpGet(
					Http_API_URL.URL_SERVER_TRAVELLER_PROMOTION_PROBE + "/"
							+ travellerId);
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() != 500
					&& response.getStatusLine().getStatusCode() != 404) {
				long contentLength = response.getEntity().getContentLength();
				if (contentLength != 0) {
					// save a copy to the the local SD card
					InputStream is = response.getEntity().getContent();
					FileUtils.copyInputStreamToFile(is, promotionImage);
					return true;
				} else {
					return false;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public File getCityPromotionImageFileFromSDCard(long travellerId) {
		File cityImageFolder = new File(
				ConfigurationConstant.VENTOURA_PROMOTION_CITY_IMAGES_PATH);
		if (!cityImageFolder.exists()) {
			cityImageFolder.mkdirs();
		}
		File promotionImage = new File(
				getCityPromotionImageFilePath(travellerId));
		return promotionImage;
	}

	@Override
	public long addNewPromotionCandidate(long travellerId, List<Integer> cities) {

		try {

			/* load the images */
			List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(
					Http_API_URL.URL_SERVER_ADD_PROMOTION_TRAVELLER_CANDIDATE
							+ "/" + travellerId);

			postParameters.add(new BasicNameValuePair("city_0", cities.get(0)
					+ ""));
			postParameters.add(new BasicNameValuePair("city_1", cities.get(1)
					+ ""));
			postParameters.add(new BasicNameValuePair("city_2", cities.get(2)
					+ ""));
			postParameters.add(new BasicNameValuePair("city_3", cities.get(3)
					+ ""));
			post.setEntity(new UrlEncodedFormEntity(postParameters));

			HttpResponse response = client.execute(post);

			if (response.getStatusLine().getStatusCode() == 200) {
				return HttpUtility.parseResponseMessageLongValue(response);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return -1;
	}

	@Override
	public boolean deletePromotionImage(long travellerId) {
		File promotionImage = getCityPromotionImageFileFromSDCard(travellerId);
		if (promotionImage.exists()) {
			return promotionImage.delete();
		} else {
			return true;
		}
	}

	@Override
	public String getCityPromotionImageFilePath(long travellerId) {
		return ConfigurationConstant.VENTOURA_PROMOTION_CITY_IMAGES_PATH + "/"
				+ travellerId + "_"
				+ ConfigurationConstant.VENTOURA_PROMOTION_CITY_IMAGES_FILENAME;
	}

}
