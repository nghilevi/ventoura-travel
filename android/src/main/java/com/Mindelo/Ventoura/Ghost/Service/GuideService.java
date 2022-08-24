package com.Mindelo.Ventoura.Ghost.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Constant.DBConstant;
import com.Mindelo.Ventoura.Constant.HttpFieldConstant;
import com.Mindelo.Ventoura.Constant.Http_API_URL;
import com.Mindelo.Ventoura.Entity.Guide;
import com.Mindelo.Ventoura.Entity.GuideUpdatedLog;
import com.Mindelo.Ventoura.Entity.ImageProfile;
import com.Mindelo.Ventoura.Enum.Gender;
import com.Mindelo.Ventoura.Ghost.DBHelper.VentouraDBHelper;
import com.Mindelo.Ventoura.Ghost.DBManager.GuideGalleryImageDBManager;
import com.Mindelo.Ventoura.Ghost.DBManager.GuideUpdatedLogDBManager;
import com.Mindelo.Ventoura.Ghost.DBManager.GuideProfileDBManager;
import com.Mindelo.Ventoura.Ghost.IService.IGuideService;
import com.Mindelo.Ventoura.JSONEntity.JSONGuideProfile;
import com.Mindelo.Ventoura.Util.CommonUtil;
import com.Mindelo.Ventoura.Util.DateTimeUtil;
import com.Mindelo.Ventoura.Util.HttpUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GuideService implements IGuideService {

	private SQLiteDatabase db;
	private VentouraDBHelper ventouraDbHelper;
	private GuideProfileDBManager guideProfileDBManager;
	private GuideGalleryImageDBManager guideGalleryImageDBManager;
	private GuideUpdatedLogDBManager gulDbManager;

	public GuideService(Context context) {
		ventouraDbHelper = new VentouraDBHelper(context);
		db = ventouraDbHelper.getWritableDatabase();
		gulDbManager = new GuideUpdatedLogDBManager(this.db);
		guideProfileDBManager = new GuideProfileDBManager(this.db);
		guideGalleryImageDBManager = new GuideGalleryImageDBManager(this.db);
	}

	@Override
	public long uploadGuideProfile(Guide guide) {
		try {
			MultipartEntityBuilder builder = loadNewGuideFormFieldData(guide);
			HttpPost post = new HttpPost(Http_API_URL.URL_SERVER_UPLOAD_GUIDE);

			post.setEntity(builder.build());

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(post);

			long guideId = -1;

			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {

				guideId = HttpUtility.parseResponseMessageLongValue(response);
				// create an entry for this guide
				saveCacheProfileUpdatedLog(guideId);
			}

			return guideId;

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public boolean updateGuideProfile(Guide guide) {
		try {
			MultipartEntityBuilder builder = loadUpdateGuideFormFieldData(guide);

			HttpPost post = new HttpPost(Http_API_URL.URL_SERVER_UPDATE_GUIDE);

			post.setEntity(builder.build());

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(post);

			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {
				return true;
			} else
				return false;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private MultipartEntityBuilder loadUpdateGuideFormFieldData(Guide guide) {
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		builder.addTextBody(HttpFieldConstant.GUIDE_TEXT_BIOGRAPHY,
				guide.getTextBiography());

		builder.addTextBody(HttpFieldConstant.SERVER_GUIDE_ID,
				"" + guide.getId());

		builder.addTextBody(HttpFieldConstant.GUIDE_TOUR_LENGTH,
				"" + guide.getTourLength());
		builder.addTextBody(HttpFieldConstant.GUIDE_TOUR_TYPE,
				"" + guide.getTourType());
		builder.addTextBody(HttpFieldConstant.GUIDE_TOUR_PRICE,
				"" + guide.getTourPrice());

		builder.addTextBody(HttpFieldConstant.GUIDE_PAYMENT_MONEY_TYPE, ""
				+ guide.getMoneyType());

		builder.addTextBody(HttpFieldConstant.GUIDE_COUNTRY,
				"" + guide.getCountry());
		builder.addTextBody(HttpFieldConstant.GUIDE_CITY, "" + guide.getCity());

		return builder;
	}

	private MultipartEntityBuilder loadNewGuideFormFieldData(Guide guide)
			throws UnsupportedEncodingException {

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		String dob = DateTimeUtil.fromDateToString(guide.getDateOfBirth());

		if (guide.getGender() == Gender.MALE) {
			builder.addTextBody(HttpFieldConstant.GUIDE_GENDER, "male");
		} else {
			builder.addTextBody(HttpFieldConstant.GUIDE_GENDER, "female");
		}

		builder.addTextBody(HttpFieldConstant.GUIDE_DATE_OF_BIRTH, dob);
		builder.addTextBody(HttpFieldConstant.GUIDE_FACEBOOK_ACCOUNT_NAME,
				guide.getFacebookAccountName());

		builder.addTextBody(HttpFieldConstant.GUIDE_GUIDE_FIRST_NAME,
				guide.getGuideFirstname());
		builder.addTextBody(HttpFieldConstant.GUIDE_GUIDE_LAST_NAME,
				guide.getGuideLastname());

		builder.addTextBody(HttpFieldConstant.GUIDE_TEXT_BIOGRAPHY,
				guide.getTextBiography());

		builder.addTextBody(HttpFieldConstant.GUIDE_PAYMENT_METHOD, ""
				+ guide.getPaymentMethod().getNumVal());
		builder.addTextBody(HttpFieldConstant.GUIDE_TOUR_LENGTH,
				"" + guide.getTourLength());
		builder.addTextBody(HttpFieldConstant.GUIDE_TOUR_PRICE,
				"" + guide.getTourPrice());
		builder.addTextBody(HttpFieldConstant.GUIDE_TOUR_TYPE,
				"" + guide.getTourType());

		builder.addTextBody(HttpFieldConstant.GUIDE_CITY, "" + guide.getCity());
		builder.addTextBody(HttpFieldConstant.GUIDE_PAYMENT_MONEY_TYPE, ""
				+ guide.getMoneyType());

		builder.addTextBody(HttpFieldConstant.GUIDE_COUNTRY,
				"" + guide.getCountry());

		// Device Account
		builder.addTextBody(HttpFieldConstant.LOGIN_USER_DEVICE_OSTYPE, "1");
		builder.addTextBody(HttpFieldConstant.LOGIN_USER_DEVICE_TOKEN, "");

		ImageProfile portalImage = guide.getPortalImage();

		builder.addBinaryBody(HttpFieldConstant.GUIDE_PORTAL_PHOTO,
				portalImage.getImageContent(), ContentType.MULTIPART_FORM_DATA,
				HttpFieldConstant.GUIDE_PORTAL_PHOTO);

		return builder;
	}

	@Override
	public boolean deactivateGuide(long guideId) {
		try {
			HttpGet get = new HttpGet(Http_API_URL.URL_SERVER_DEACTIVATE_GUIDE
					+ "/" + guideId);

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public JSONGuideProfile getGuideProfileByIdFromServer(long guideId) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm")
				.create();

		try {
			HttpGet get = new HttpGet(Http_API_URL.URL_SERVER_GET_GUIDE_PROFILE
					+ "/" + guideId);

			setProfileHttpModifiedHeader(get, guideId);

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {

				BufferedReader reader = HttpUtility
						.fromHttpJsonResponseToReader(response);
				JSONGuideProfile guideProfile = gson.fromJson(reader,
						JSONGuideProfile.class);

				/*
				 * cache task, save the new coming data to DB
				 */
				guideProfileDBManager.deleteMany(" where "
						+ DBConstant.TABLE_GUIDE_PROFILE_FIELD_ID + "="
						+ guideId);
				guideProfileDBManager.save(guideProfile);

				updateCacheProfileUpdatedLog(response, guideId);

				return guideProfile;

			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public JSONGuideProfile getGuideProfileByIdFromDB(long guideId) {

		JSONGuideProfile jsonGuideProfile = guideProfileDBManager
				.findOne(" where " + DBConstant.TABLE_GUIDE_PROFILE_FIELD_ID
						+ "=" + guideId);

		return jsonGuideProfile;
	}

	@Override
	public ImageProfile getGuidePortalImageFromDB(long guideId) {
		String condition = " where "
				+ DBConstant.TABLE_GUIDE_PROFILE_GALLERY_FIELD_GUIDE_ID + "="
				+ guideId + " AND "
				+ DBConstant.TABLE_GUIDE_PROFILE_GALLERY_FIELD_IS_PORTAL + "="
				+ "1 ";
		return guideGalleryImageDBManager.findOne(condition);
	}

	@Override
	public List<ImageProfile> getGuideGalleryImagesFromDB(long guideId) {
		String condition = " where "
				+ DBConstant.TABLE_GUIDE_PROFILE_GALLERY_FIELD_GUIDE_ID + "="
				+ guideId + " order by " + DBConstant.TABLE_GUIDE_PROFILE_GALLERY_FIELD_IS_PORTAL + " desc ";
		return guideGalleryImageDBManager.findMany(condition);
	}

	@Override
	public boolean loadGuideAllGalleryImagesFromServer(long guideId) {

		Map<String, byte[]> imageMaps = downloadGalleryImagesFromServerIntoBytes(Http_API_URL.URL_SERVER_GET_GUIDE_GALLERY_IMAGES
				+ "/" + guideId, guideId);

		if (imageMaps == null) {
			// no new data comes
			return false;
		}

		// clean old data
		String condition = " where "
				+ DBConstant.TABLE_GUIDE_PROFILE_GALLERY_FIELD_GUIDE_ID + "="
				+ guideId + " ";
		guideGalleryImageDBManager.deleteMany(condition);
		
		// save new data
		List<ImageProfile> images = new ArrayList<ImageProfile>();
		for (Map.Entry<String, byte[]> entry : imageMaps.entrySet()) {

			ImageProfile image = new ImageProfile();
			image.setId(Long.valueOf(entry.getKey().split("_")[1]));
			image.setUserId(guideId);
			image.setImageContent(entry.getValue());

			if (entry.getKey().contains("gp")) {
				image.setPortal(true);
			} else {
				image.setPortal(false);
			}
			images.add(image);
		}
		guideGalleryImageDBManager.saveAll(images);
		return true;
	}

	private Map<String, byte[]> downloadGalleryImagesFromServerIntoBytes(
			String url, long guideId) {
		try {
			HttpGet get = new HttpGet(url);
			// set cache header
			setGalleryHttpModifiedHeader(get, guideId);
			
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(get);


			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {
				
				updateCacheGalleryUpdatedLog(response, guideId);
				
				InputStream is = response.getEntity().getContent();
				return CommonUtil.unzipImageFilesIntoBytes(is);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public long uploadGuideAttraction(long guideId, String attractionName) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		try {
			nameValuePairs.add(new BasicNameValuePair(
					HttpFieldConstant.SERVER_GUIDE_ID, "" + guideId));
			nameValuePairs.add(new BasicNameValuePair(
					HttpFieldConstant.GUIDE_ATTRACTION_NAME, "" + guideId));

			HttpPost post = new HttpPost(
					Http_API_URL.URL_SERVER_UPLOAD_GUIDE_ATTRACTION);
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(post);

			return HttpUtility.parseResponseMessageLongValue(response);

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public long uploadGuideHiddenTreasure(long guideId,
			String guideHiddenTreasureName) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		try {
			nameValuePairs.add(new BasicNameValuePair(
					HttpFieldConstant.SERVER_GUIDE_ID, "" + guideId));
			nameValuePairs
					.add(new BasicNameValuePair(
							HttpFieldConstant.GUIDE_HIDDEN_TREASURE_NAME, ""
									+ guideId));

			HttpPost post = new HttpPost(
					Http_API_URL.URL_SERVER_UPLOAD_GUIDE_HIDDEN_TREASURE);
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(post);

			return HttpUtility.parseResponseMessageLongValue(response);

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public boolean batchUploadGuideAttraction(long guideId,
			List<String> attractions) {

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		try {
			for (String attraction : attractions) {
				nameValuePairs.add(new BasicNameValuePair(attraction, ""));
			}

			HttpPost post = new HttpPost(
					Http_API_URL.URL_SERVER_BATCH_UPLOAD_GUIDE_ATTRACTION + "/"
							+ guideId);
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(post);

			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean batchDeleteGuideAttraction(List<Long> attractions) {

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		try {
			for (long attraction : attractions) {
				nameValuePairs.add(new BasicNameValuePair(attraction + "", ""));
			}

			HttpPost post = new HttpPost(
					Http_API_URL.URL_SERVER_BATCH_DELETE_GUIDE_ATTRACTION);
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(post);

			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	@Override
	public boolean deleteGuideAttraction(long guideAttractionId) {
		try {
			HttpGet get = new HttpGet(
					Http_API_URL.URL_SERVER_DELETE_GUIDE_ATTRACTION + "/"
							+ guideAttractionId);

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteGuideHiddenTreasure(long guideHiddenTreasureId) {
		try {
			HttpGet get = new HttpGet(
					Http_API_URL.URL_SERVER_DELETE_GUIDE_HIDDEN_TREASURE + "/"
							+ guideHiddenTreasureId);

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/****************************************************/
	/**** Cache functions ****/
	/****************************************************/

	private void updateCacheProfileUpdatedLog(HttpResponse response, long guideId)
			throws ParseException {
		Header header = response
				.getFirstHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED);
		if (header != null) {
			// update the updated log
			gulDbManager.updateUpdatedLog(
					DBConstant.TABLE_GUIDE_UPDATED_LOG_FIELD_PROFILE_UPDATED,
					DateTimeUtil.fromStringToDate_GMT(header.getValue())
							.getTime(), guideId);
		}
	}

	private void saveCacheProfileUpdatedLog(long guideId) throws ParseException {
		gulDbManager.save(new GuideUpdatedLog(guideId, 0));
	}
	
	private void updateCacheGalleryUpdatedLog(HttpResponse response, long guideId)
			throws ParseException {
		Header header = response
				.getFirstHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED);
		if (header != null) {
			// update the updated log
			gulDbManager.updateUpdatedLog(
					DBConstant.TABLE_GUIDE_UPDATED_LOG_FIELD_GALLERY_UPDATED,
					DateTimeUtil.fromStringToDate_GMT(header.getValue())
							.getTime(), guideId);
		}
	}

	private void setProfileHttpModifiedHeader(HttpRequestBase request, long guideId) {
		/*
		 * set modified header
		 */
		GuideUpdatedLog log = gulDbManager.findOne(" where "
				+ DBConstant.TABLE_GUIDE_UPDATED_LOG_FIELD_GUIDE_ID + "="
				+ guideId);
		if (log != null) {
			Date date = new Date(log.getProfileLastUpdated());
			String time_GMT = DateTimeUtil.fromDateToString_GMT(date);
			request.setHeader(ConfigurationConstant.HTTP_HEADER_MODIFIED_SINCE,
					time_GMT);
		}
	}
	
	private void setGalleryHttpModifiedHeader(HttpRequestBase request, long guideId){
		/*
		 * set modified header
		 */
		GuideUpdatedLog log = gulDbManager.findOne(" where "
				+ DBConstant.TABLE_GUIDE_UPDATED_LOG_FIELD_GUIDE_ID + "="
				+ guideId);
		if (log != null) {
			Date date = new Date(log.getGalleryLastUpdated());
			String time_GMT = DateTimeUtil.fromDateToString_GMT(date);
			request.setHeader(ConfigurationConstant.HTTP_HEADER_MODIFIED_SINCE,
					time_GMT);
		}
	}

}
