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
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Constant.DBConstant;
import com.Mindelo.Ventoura.Constant.HttpFieldConstant;
import com.Mindelo.Ventoura.Constant.Http_API_URL;
import com.Mindelo.Ventoura.Entity.ImageProfile;
import com.Mindelo.Ventoura.Entity.Traveller;
import com.Mindelo.Ventoura.Entity.TravellerUpdatedLog;
import com.Mindelo.Ventoura.Enum.Gender;
import com.Mindelo.Ventoura.Ghost.DBHelper.VentouraDBHelper;
import com.Mindelo.Ventoura.Ghost.DBManager.TravellerGalleryImageDBManager;
import com.Mindelo.Ventoura.Ghost.DBManager.TravellerProfileDBManager;
import com.Mindelo.Ventoura.Ghost.DBManager.TravellerUpdatedLogDBManager;
import com.Mindelo.Ventoura.Ghost.IService.ITravellerService;
import com.Mindelo.Ventoura.JSONEntity.JSONTravellerProfile;
import com.Mindelo.Ventoura.Util.CommonUtil;
import com.Mindelo.Ventoura.Util.DateTimeUtil;
import com.Mindelo.Ventoura.Util.HttpUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TravellerService implements ITravellerService {

	private SQLiteDatabase db;
	private VentouraDBHelper ventouraDbHelper;
	private TravellerProfileDBManager travellerProfileDBManager;
	private TravellerGalleryImageDBManager travellerGalleryImageDBManager;
	private TravellerUpdatedLogDBManager tulDbManager;

	public TravellerService(Context context) {
		ventouraDbHelper = new VentouraDBHelper(context);
		db = ventouraDbHelper.getWritableDatabase();
		tulDbManager = new TravellerUpdatedLogDBManager(this.db);
		travellerProfileDBManager = new TravellerProfileDBManager(this.db);
		travellerGalleryImageDBManager = new TravellerGalleryImageDBManager(
				this.db);
	}

	@Override
	public long uploadTraverllerProfile(Traveller traveller) {

		try {
			MultipartEntityBuilder builder = loadNewFormFieldData(traveller);

			HttpPost post = new HttpPost(
					Http_API_URL.URL_SERVER_UPLOAD_TRAVERLLER);

			post.setEntity(builder.build());

			// execute
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(post);

			long travellerId = -1;

			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {
				// create a updated log for this traveller
				travellerId = HttpUtility
						.parseResponseMessageLongValue(response);
				saveCacheProfileUpdatedLog(travellerId);
			}

			return travellerId;

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public boolean updateTravellerProfile(Traveller traveller) {
		try {
			MultipartEntityBuilder builder = loadUpdateFormFieldData(traveller);

			HttpPost post = new HttpPost(
					Http_API_URL.URL_SERVER_UPDATE_TRAVERLLER);

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

	@Override
	public boolean deactivateTraverller(long travellerId) {
		try {
			HttpGet get = new HttpGet(
					Http_API_URL.URL_SERVER_DEACTIVATE_TRAVERLLER + "/"
							+ travellerId);

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

	private MultipartEntityBuilder loadUpdateFormFieldData(Traveller traveller)
			throws UnsupportedEncodingException {

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		builder.addTextBody(HttpFieldConstant.TRAVELLER_TEXT_BIOGRAPHY,
				traveller.getTextBiography());

		builder.addTextBody(HttpFieldConstant.SERVER_TRAVELLER_ID, ""
				+ traveller.getId());

		builder.addTextBody(HttpFieldConstant.TRAVELLER_COUNTRY,
				"" + traveller.getCountry());
		builder.addTextBody(HttpFieldConstant.TRAVELLER_CITY,
				"" + traveller.getCity());

		return builder;
	}

	private MultipartEntityBuilder loadNewFormFieldData(Traveller traveller)
			throws UnsupportedEncodingException {

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

		String dob = DateTimeUtil.fromDateToString(traveller.getDateOfBirth());

		if (traveller.getGender() == Gender.MALE) {
			builder.addTextBody(HttpFieldConstant.TRAVELLER_GENDER, "male");
		} else {
			builder.addTextBody(HttpFieldConstant.TRAVELLER_GENDER, "female");
		}

		builder.addTextBody(HttpFieldConstant.TRAVELLER_DATE_OF_BIRTH, dob);
		builder.addTextBody(HttpFieldConstant.TRAVELLER_FACEBOOK_ACCOUNT_NAME,
				traveller.getFacebookAccountName());
		builder.addTextBody(HttpFieldConstant.TRAVELLER_TRAVERLLER_FIRST_NAME,
				traveller.getTravellerFirstname());
		builder.addTextBody(HttpFieldConstant.TRAVELLER_TRAVERLLER_LAST_NAME,
				traveller.getTravellerLastname());
		builder.addTextBody(HttpFieldConstant.TRAVELLER_TEXT_BIOGRAPHY,
				traveller.getTextBiography());
		builder.addTextBody(HttpFieldConstant.TRAVELLER_CITY,
				"" + traveller.getCity());
		builder.addTextBody(HttpFieldConstant.TRAVELLER_COUNTRY,
				"" + traveller.getCountry());

		// Device Account
		builder.addTextBody(HttpFieldConstant.LOGIN_USER_DEVICE_OSTYPE, "1");
		builder.addTextBody(HttpFieldConstant.LOGIN_USER_DEVICE_TOKEN, "");

		ImageProfile portalImage = traveller.getPortalImage();

		builder.addBinaryBody(HttpFieldConstant.TRAVELLER_PORTAL_PHOTO,
				portalImage.getImageContent(), ContentType.MULTIPART_FORM_DATA,
				HttpFieldConstant.TRAVELLER_PORTAL_PHOTO);

		return builder;
	}


	
	@Override
	public JSONTravellerProfile getTravellerProfileByIdFromServer(
			long travellerId) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm")
				.create();

		try {
			HttpGet get = new HttpGet(
					Http_API_URL.URL_SERVER_GET_TRAVERLLER_PROFILE + "/"
							+ travellerId);
			// set if modified header
			setProfileHttpModifiedHeader(get, travellerId);

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {

				BufferedReader reader = HttpUtility
						.fromHttpJsonResponseToReader(response);
				JSONTravellerProfile travellerProfile = gson.fromJson(reader,
						JSONTravellerProfile.class);

				travellerProfileDBManager.deleteMany(" where "
						+ DBConstant.TABLE_TRAVELLER_PROFILE_FIELD_ID + "="
						+ travellerId);
				travellerProfileDBManager.save(travellerProfile);

				updateCacheProfileUpdatedLog(response, travellerId);

				return travellerProfile;

			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public JSONTravellerProfile getTravellerProfileByIdFromDB(long travellerId) {

		JSONTravellerProfile travellerProfile = travellerProfileDBManager
				.findOne(" where "
						+ DBConstant.TABLE_TRAVELLER_PROFILE_FIELD_ID + "="
						+ travellerId);

		return travellerProfile;
	}

	@Override
	public ImageProfile getTravellerPortalImageFromDB(long travellerId) {
		String condition = " where "
				+ DBConstant.TABLE_TRAVELLER_PROFILE_GALLERY_FIELD_TRAVELLER_ID
				+ "=" + travellerId + " AND "
				+ DBConstant.TABLE_TRAVELLER_PROFILE_GALLERY_FIELD_IS_PORTAL
				+ "=" + "1 ";
		return travellerGalleryImageDBManager.findOne(condition);
	}

	@Override
	public List<ImageProfile> getTravellerGalleryImagesFromDB(long travellerId) {
		// make sure the first element is the portal image
		// TODO if we need to consider the order, we will need to consider more
		String condition = " where "
				+ DBConstant.TABLE_TRAVELLER_PROFILE_GALLERY_FIELD_TRAVELLER_ID
				+ "=" + travellerId + " order by "
				+ DBConstant.TABLE_TRAVELLER_PROFILE_GALLERY_FIELD_IS_PORTAL
				+ " desc ";
		return travellerGalleryImageDBManager.findMany(condition);
	}

	@Override
	public boolean loadTravellerAllGalleryImagesFromServer(long travellerId) {

		Map<String, byte[]> imageMaps = downloadGalleryImagesFromServerIntoBytes(Http_API_URL.URL_SERVER_GET_TRAVELLER_GALLERY_IMAGES
				+ "/" + travellerId, travellerId);

		// TODO Cache
		if (imageMaps == null || imageMaps.size() == 0) {
			// no new data comes
			return false;
		}

		// clean old data
		String condition = " where "
				+ DBConstant.TABLE_TRAVELLER_PROFILE_GALLERY_FIELD_TRAVELLER_ID
				+ "=" + travellerId + " ";
		travellerGalleryImageDBManager.deleteMany(condition);

		// save new data
		List<ImageProfile> images = new ArrayList<ImageProfile>();
		for (Map.Entry<String, byte[]> entry : imageMaps.entrySet()) {

			ImageProfile image = new ImageProfile();
			image.setId(Long.valueOf(entry.getKey().split("_")[1]));
			image.setUserId(travellerId);
			image.setImageContent(entry.getValue());

			if (entry.getKey().contains("tp")) {
				image.setPortal(true);
			} else {
				image.setPortal(false);
			}

			images.add(image);
		}
		travellerGalleryImageDBManager.saveAll(images);
		return true;
	}

	private Map<String, byte[]> downloadGalleryImagesFromServerIntoBytes(
			String url, long travellerId) {
		try {
			HttpGet get = new HttpGet(url);
			setGalleryHttpModifiedHeader(get, travellerId);
			
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(get);
			
			
			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {
				
				updateCacheGalleryUpdatedLog(response, travellerId);
				
				InputStream is = response.getEntity().getContent();
				return CommonUtil.unzipImageFilesIntoBytes(is);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/****************************************************/
	/**** Cache functions ****/
	/**
	 * @throws ParseException
	 **************************************************/

	private void updateCacheProfileUpdatedLog(HttpResponse response,
			long travellerId) throws ParseException {
		Header header = response
				.getFirstHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED);
		if (header != null) {
			// update the updated log
			tulDbManager
					.updateUpdatedLog(
							DBConstant.TABLE_TRAVELLER_UPDATED_LOG_FIELD_PROFILE_UPDATED,
							DateTimeUtil
									.fromStringToDate_GMT(header.getValue())
									.getTime(), travellerId);
		}
	}

	private void updateCacheGalleryUpdatedLog(HttpResponse response,
			long travellerId) throws ParseException {
		Header header = response
				.getFirstHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED);
		if (header != null) {
			// update the updated log
			tulDbManager
					.updateUpdatedLog(
							DBConstant.TABLE_TRAVELLER_UPDATED_LOG_FIELD_GALLERY_UPDATED,
							DateTimeUtil
									.fromStringToDate_GMT(header.getValue())
									.getTime(), travellerId);
		}
	}

	private void saveCacheProfileUpdatedLog(long travellerId)
			throws ParseException {
		tulDbManager.save(new TravellerUpdatedLog(travellerId, 0));
	}

	private void setProfileHttpModifiedHeader(HttpRequestBase request,
			long travellerId) {
		/*
		 * set modified header
		 */
		TravellerUpdatedLog log = tulDbManager.findOne(" where "
				+ DBConstant.TABLE_TRAVELLER_UPDATED_LOG_FIELD_TRAVELLER_ID
				+ "=" + travellerId);
		if (log != null) {
			Date date = new Date(log.getProfileLastUpdated());
			String time_GMT = DateTimeUtil.fromDateToString_GMT(date);
			request.setHeader(ConfigurationConstant.HTTP_HEADER_MODIFIED_SINCE,
					time_GMT);
		}
	}

	private void setGalleryHttpModifiedHeader(HttpRequestBase request,
			long travellerId) {
		/*
		 * set modified header
		 */
		TravellerUpdatedLog log = tulDbManager.findOne(" where "
				+ DBConstant.TABLE_TRAVELLER_UPDATED_LOG_FIELD_TRAVELLER_ID
				+ "=" + travellerId);
		if (log != null) {
			Date date = new Date(log.getGalleryLastUpdated());
			String time_GMT = DateTimeUtil.fromDateToString_GMT(date);
			request.setHeader(ConfigurationConstant.HTTP_HEADER_MODIFIED_SINCE,
					time_GMT);
		}
	}
}
