package com.Mindelo.Ventoura.Ghost.Service;

import java.io.BufferedReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Constant.DBConstant;
import com.Mindelo.Ventoura.Constant.HttpFieldConstant;
import com.Mindelo.Ventoura.Constant.Http_API_URL;
import com.Mindelo.Ventoura.Entity.GuideUpdatedLog;
import com.Mindelo.Ventoura.Entity.TravellerUpdatedLog;
import com.Mindelo.Ventoura.Ghost.DBHelper.VentouraDBHelper;
import com.Mindelo.Ventoura.Ghost.DBManager.GuideUpdatedLogDBManager;
import com.Mindelo.Ventoura.Ghost.DBManager.GuideVFunctionSettingsDBManager;
import com.Mindelo.Ventoura.Ghost.DBManager.TravellerUpdatedLogDBManager;
import com.Mindelo.Ventoura.Ghost.DBManager.TravellerVFunctionSettingsDBManager;
import com.Mindelo.Ventoura.Ghost.IService.IVFunctionSettingsService;
import com.Mindelo.Ventoura.JSONEntity.JSONGuideVFunctionSettings;
import com.Mindelo.Ventoura.JSONEntity.JSONTravellerVFunctionSettings;
import com.Mindelo.Ventoura.Util.DateTimeUtil;
import com.Mindelo.Ventoura.Util.HttpUtility;
import com.google.gson.Gson;

public class VFunctionSettingsService implements IVFunctionSettingsService {

	private SQLiteDatabase db;
	private VentouraDBHelper ventouraDbHelper;
	private TravellerUpdatedLogDBManager tulDbManager;
	private GuideUpdatedLogDBManager gulDbManager;

	private TravellerVFunctionSettingsDBManager tvDbManager;
	private GuideVFunctionSettingsDBManager gvDbManager;

	public VFunctionSettingsService(Context context) {
		ventouraDbHelper = new VentouraDBHelper(context);
		db = ventouraDbHelper.getWritableDatabase();

		tulDbManager = new TravellerUpdatedLogDBManager(this.db);
		tvDbManager = new TravellerVFunctionSettingsDBManager(this.db);
		
		gulDbManager = new GuideUpdatedLogDBManager(this.db);
		gvDbManager = new GuideVFunctionSettingsDBManager(this.db);
	}

	@Override
	public boolean loadJSONTravellerVFunctionSettingsFromServer(long travellerId) {

		Gson gson = new Gson();

		try {
			HttpGet get = new HttpGet(
					Http_API_URL.URL_SERVER_GET_TRAVELLER_VFUNCTION_SETTINGS + "/"
							+ travellerId);

			HttpClient client = new DefaultHttpClient();
			// set the header
			setTravellerVFunctionSettingsHttpModifiedHeader(get, travellerId);
			HttpResponse response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {

				BufferedReader reader = HttpUtility
						.fromHttpJsonResponseToReader(response);
				JSONTravellerVFunctionSettings setting = gson.fromJson(reader,
						JSONTravellerVFunctionSettings.class);
				/*
				 * cache task
				 */
				Header header = response
						.getFirstHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED);

				tvDbManager.updateVFunctionSettings(setting);

				tulDbManager
						.updateUpdatedLog(
								DBConstant.TABLE_TRAVELLER_UPDATED_LOG_FIELD_VFUNCTION_SETTINGS_UPDATED,
								DateTimeUtil.fromStringToDate_GMT(
										header.getValue()).getTime(),
								travellerId);

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
	public boolean loadJSONGuideVFunctionSettingsFromServer(long guideId) {
		Gson gson = new Gson();

		try {
			HttpGet get = new HttpGet(
					Http_API_URL.URL_SERVER_GET_GUIDE_VFUNCTION_SETTINGS + "/"
							+ guideId);

			HttpClient client = new DefaultHttpClient();
			// set the header
			setGuideVFunctionSettingsHttpModifiedHeader(get, guideId);
			HttpResponse response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {

				BufferedReader reader = HttpUtility
						.fromHttpJsonResponseToReader(response);
				JSONGuideVFunctionSettings setting = gson.fromJson(reader,
						JSONGuideVFunctionSettings.class);
				/*
				 * cache task
				 */
				Header header = response
						.getFirstHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED);

				gvDbManager.updateVFunctionSettings(setting);

				gulDbManager
						.updateUpdatedLog(
								DBConstant.TABLE_GUIDE_UPDATED_LOG_FIELD_VFUNCTION_SETTINGS_UPDATED,
								DateTimeUtil.fromStringToDate_GMT(
										header.getValue()).getTime(), guideId);

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
	public JSONTravellerVFunctionSettings loadJSONTravellerVFunctionSettingsFromDB(
			long travellerId) {
		String condition = " where "
				+ DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_TRAVELLER_ID
				+ "=" + travellerId;
		return tvDbManager.findOne(condition);
	}

	@Override
	public JSONGuideVFunctionSettings loadJSONGuideVFunctionSettingsFromDB(
			long guideId) {
		String condition = " where "
				+ DBConstant.TABLE_GUIDE_VFUNCTION_SETTINGS_FIELD_GUIDE_ID
				+ "=" + guideId;
		return gvDbManager.findOne(condition);
	}

	@Override
	public boolean updateTravellerVFunctionSettings(
			JSONTravellerVFunctionSettings vSettings) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		try {

			nameValuePairs = loadFormFieldTravellerVFunctionSettings(vSettings);

			HttpPost post = new HttpPost(
					Http_API_URL.URL_SERVER_UPDATE_TRAVELLER_VFUNCTION_SETTINGS);
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {
				
				tvDbManager.updateVFunctionSettings(vSettings);
				updateTravellerVFunctionSettingsLastUpdatedLog(response, vSettings.getTravellerId());
				
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return false;
	}

	@Override
	public boolean updateGuideVFunctionSettings(
			JSONGuideVFunctionSettings vSettings) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		try {

			nameValuePairs = loadFormFieldGuideVFunctionSettings(vSettings);

			HttpPost post = new HttpPost(
					Http_API_URL.URL_SERVER_UPDATE_GUIDE_VFUNCTION_SETTINGS);
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {
				
				gvDbManager.updateVFunctionSettings(vSettings);
				updateGuideVFunctionSettingsLastUpdatedLog(response, vSettings.getGuideId());
				
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return false;
	}

	private List<NameValuePair> loadFormFieldTravellerVFunctionSettings(
			JSONTravellerVFunctionSettings VSettings) {

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair(
				HttpFieldConstant.TRAVELLER_VFUNCTION_SETTINGS_TRAVELLER_ID, ""
						+ VSettings.getTravellerId()));
		nameValuePairs.add(new BasicNameValuePair(
				HttpFieldConstant.TRAVELLER_VFUNCTION_SETTINGS_PREFERED_GENDER,
				"" + VSettings.getPreferedGender().getNumVal()));
		nameValuePairs
				.add(new BasicNameValuePair(
						HttpFieldConstant.TRAVELLER_VFUNCTION_SETTINGS_PREFERED_USER_ROLE,
						"" + VSettings.getPreferedUserRole().getNumVal()));
		nameValuePairs.add(new BasicNameValuePair(
				HttpFieldConstant.TRAVELLER_VFUNCTION_SETTINGS_MINI_AGE, ""
						+ VSettings.getMiniAge()));
		nameValuePairs.add(new BasicNameValuePair(
				HttpFieldConstant.TRAVELLER_VFUNCTION_SETTINGS_MAX_AGE, ""
						+ VSettings.getMaxAge()));
		nameValuePairs.add(new BasicNameValuePair(
				HttpFieldConstant.TRAVELLER_VFUNCTION_SETTINGS_MINI_PRICE, ""
						+ VSettings.getMiniPrice()));
		nameValuePairs.add(new BasicNameValuePair(
				HttpFieldConstant.TRAVELLER_VFUNCTION_SETTINGS_MAX_PRICE, ""
						+ VSettings.getMaxPrice()));
		nameValuePairs
				.add(new BasicNameValuePair(
						HttpFieldConstant.TRAVELLER_VFUNCTION_SETTINGS_SPECIFY_CITY_TOGGLE,
						"" + (VSettings.isSpecifyCityToggle() == true ? 1 : 0)));
		nameValuePairs.add(new BasicNameValuePair(
				HttpFieldConstant.TRAVELLER_VFUNCTION_SETTINGS_CITY_IDS, ""
						+ VSettings.getCityIds()));

		return nameValuePairs;
	}

	private List<NameValuePair> loadFormFieldGuideVFunctionSettings(
			JSONGuideVFunctionSettings VSettings) {

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair(
				HttpFieldConstant.GUIDE_VFUNCTION_SETTINGS_GUIDE_ID, ""
						+ VSettings.getGuideId()));
		nameValuePairs.add(new BasicNameValuePair(
				HttpFieldConstant.GUIDE_VFUNCTION_SETTINGS_PREFERED_GENDER, ""
						+ VSettings.getPreferedGender().getNumVal()));
		nameValuePairs.add(new BasicNameValuePair(
				HttpFieldConstant.GUIDE_VFUNCTION_SETTINGS_MINI_AGE, ""
						+ VSettings.getMiniAge()));
		nameValuePairs.add(new BasicNameValuePair(
				HttpFieldConstant.GUIDE_VFUNCTION_SETTINGS_MAX_AGE, ""
						+ VSettings.getMaxAge()));
		nameValuePairs.add(new BasicNameValuePair(
				HttpFieldConstant.GUIDE_VFUNCTION_SETTINGS_LAST_ACTIVE_DAYS, ""
						+ VSettings.getLastActivateDays()));

		return nameValuePairs;
	}

	/****************************************************/
	/**** Cache functions ****/
	/****************************************************/

	private void setTravellerVFunctionSettingsHttpModifiedHeader(
			HttpRequestBase request, long travellerId) {
		/*
		 * set modified header
		 */
		TravellerUpdatedLog log = tulDbManager.findOne(" where "
				+ DBConstant.TABLE_TRAVELLER_UPDATED_LOG_FIELD_TRAVELLER_ID
				+ "=" + travellerId);
		if (log != null) {
			Date date = new Date(log.getMatchesLastUpdated());
			date.setTime(log.getVFunctionSettingsLastUpdated());
			String time_GMT = DateTimeUtil.fromDateToString_GMT(date);
			request.setHeader(ConfigurationConstant.HTTP_HEADER_MODIFIED_SINCE,
					time_GMT);
		}
	}

	private void setGuideVFunctionSettingsHttpModifiedHeader(
			HttpRequestBase request, long guideId) {
		/*
		 * set modified header
		 */
		GuideUpdatedLog log = gulDbManager.findOne(" where "
				+ DBConstant.TABLE_GUIDE_UPDATED_LOG_FIELD_GUIDE_ID + "="
				+ guideId);
		if (log != null) {
			Date date = new Date(log.getVFunctionSettingsLastUpdated());
			String time_GMT = DateTimeUtil.fromDateToString_GMT(date);
			request.setHeader(ConfigurationConstant.HTTP_HEADER_MODIFIED_SINCE,
					time_GMT);
		}
	}
	
	private void updateTravellerVFunctionSettingsLastUpdatedLog(HttpResponse response,
			long travellerId) throws ParseException{
		Header header = response
				.getFirstHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED);
		if (header != null) {
			// update the updated log
			tulDbManager
					.updateUpdatedLog(
							DBConstant.TABLE_TRAVELLER_UPDATED_LOG_FIELD_VFUNCTION_SETTINGS_UPDATED,
							DateTimeUtil
									.fromStringToDate_GMT(header.getValue())
									.getTime(), travellerId);
		}
	}
	
	private void updateGuideVFunctionSettingsLastUpdatedLog(HttpResponse response,
			long guideId) throws ParseException{
		Header header = response
				.getFirstHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED);
		if (header != null) {
			// update the updated log
			gulDbManager
					.updateUpdatedLog(
							DBConstant.TABLE_GUIDE_UPDATED_LOG_FIELD_VFUNCTION_SETTINGS_UPDATED,
							DateTimeUtil
									.fromStringToDate_GMT(header.getValue())
									.getTime(), guideId);
		}
	}

}
