package com.Mindelo.Ventoura.Ghost.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Constant.DBConstant;
import com.Mindelo.Ventoura.Constant.Http_API_URL;
import com.Mindelo.Ventoura.Entity.GuideUpdatedLog;
import com.Mindelo.Ventoura.Entity.ImageMatch;
import com.Mindelo.Ventoura.Entity.ImageProfile;
import com.Mindelo.Ventoura.Entity.TravellerUpdatedLog;
import com.Mindelo.Ventoura.Enum.UserRole;
import com.Mindelo.Ventoura.Ghost.DBHelper.VentouraDBHelper;
import com.Mindelo.Ventoura.Ghost.DBManager.GuideUpdatedLogDBManager;
import com.Mindelo.Ventoura.Ghost.DBManager.MatchDBManager;
import com.Mindelo.Ventoura.Ghost.DBManager.MatchImageDBManager;
import com.Mindelo.Ventoura.Ghost.DBManager.TravellerUpdatedLogDBManager;
import com.Mindelo.Ventoura.Ghost.IService.IMatchesService;
import com.Mindelo.Ventoura.JSONEntity.JSONMatch;
import com.Mindelo.Ventoura.JSONEntity.JSONMatchesList;
import com.Mindelo.Ventoura.Util.CommonUtil;
import com.Mindelo.Ventoura.Util.DateTimeUtil;
import com.Mindelo.Ventoura.Util.HttpUtility;
import com.google.gson.Gson;

public class MatchesService implements IMatchesService {

	private SQLiteDatabase db;
	private VentouraDBHelper ventouraDbHelper;
	private MatchDBManager matchDBManager;
	private MatchImageDBManager matchImageDBManager;
	private TravellerUpdatedLogDBManager tulDbManager;
	private GuideUpdatedLogDBManager gulDbManager;

	public MatchesService(Context context) {
		ventouraDbHelper = new VentouraDBHelper(context);
		db = ventouraDbHelper.getWritableDatabase();
		matchDBManager = new MatchDBManager(this.db);
		tulDbManager = new TravellerUpdatedLogDBManager(this.db);
		gulDbManager = new GuideUpdatedLogDBManager(this.db);
		matchImageDBManager = new MatchImageDBManager(this.db);
	}

	public JSONMatchesList getTravellerMatchListFromDB(long travellerId) {
		JSONMatchesList travellerMatches = new JSONMatchesList();
		/*
		 * no data changed, use the local cached data
		 */
		List<JSONMatch> travellerMatchList = matchDBManager.findMany(" where "
				+ DBConstant.TABLE_MATCH_FIELD_OWNER_ID + "=" + travellerId
				+ " and " + DBConstant.TABLE_MATCH_FIELD_OWNER_ROLE + "="
				+ UserRole.TRAVELLER.getNumVal());
		travellerMatches.setMatches(travellerMatchList);
		return travellerMatches;
	}

	public boolean getTravellerMatchListFromServer(long travellerId) {
		Gson gson = new Gson();
		JSONMatchesList travellerMatches = new JSONMatchesList();

		try {
			HttpGet get = new HttpGet(
					Http_API_URL.URL_SERVER_GET_TRAVELLER_MATCHES_LIST + "/"
							+ travellerId);

			HttpClient client = new DefaultHttpClient();

			// set the header
			setTravellerHttpModifiedHeader(get, travellerId);

			HttpResponse response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {

				BufferedReader reader = HttpUtility
						.fromHttpJsonResponseToReader(response);
				travellerMatches = gson.fromJson(reader, JSONMatchesList.class);

				/*
				 * cache task
				 */
				matchDBManager.deleteMany(" where "
						+ DBConstant.TABLE_MATCH_FIELD_OWNER_ID + "="
						+ travellerId + " and "
						+ DBConstant.TABLE_MATCH_FIELD_OWNER_ROLE + "="
						+ UserRole.TRAVELLER.getNumVal()); // delete all the
															// matches
				matchDBManager.saveAll(travellerMatches.getMatches(),
						travellerId, UserRole.TRAVELLER.getNumVal());

				updateTravellerCacheMatchUpdateLog(response, travellerId);

				return true;

			} else {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public JSONMatchesList getGuideMatchListFromDB(long guideId) {
		JSONMatchesList guideMatches = new JSONMatchesList();
		/*
		 * no data changed, use the local cached data
		 */
		List<JSONMatch> travellerMatchList = matchDBManager.findMany(" where "
				+ DBConstant.TABLE_MATCH_FIELD_OWNER_ID + "=" + guideId
				+ " and " + DBConstant.TABLE_MATCH_FIELD_OWNER_ROLE + "="
				+ UserRole.GUIDE.getNumVal());
		guideMatches.setMatches(travellerMatchList);
		return guideMatches;
	}

	public boolean getGuideMatchListFromServer(long guideId) {

		Gson gson = new Gson();
		JSONMatchesList guideMatches = new JSONMatchesList();

		try {
			HttpGet get = new HttpGet(
					Http_API_URL.URL_SERVER_GET_GUIDE_MATCHES_LIST + "/"
							+ guideId);

			// set the modified header
			setGuideHttpModifiedHeader(get, guideId);

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {

				BufferedReader reader = HttpUtility
						.fromHttpJsonResponseToReader(response);
				guideMatches = gson.fromJson(reader, JSONMatchesList.class);
				/*
				 * cache task, save the new coming data to DB
				 */
				matchDBManager.deleteMany(" where "
						+ DBConstant.TABLE_MATCH_FIELD_OWNER_ID + "=" + guideId
						+ " and " + DBConstant.TABLE_MATCH_FIELD_OWNER_ROLE
						+ "=" + UserRole.GUIDE.getNumVal());
				matchDBManager.saveAll(guideMatches.getMatches(), guideId,
						UserRole.GUIDE.getNumVal());

				updateGuideCacheMatchUpdatedLog(response, guideId);

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
	public void deleteTTMatchMatch(long redTravellerId, long blueTravellerId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteTGMatch(long guideId, long travellerId) {
		// TODO Auto-generated method stub

	}

	@Override
	public JSONMatch getSingleMatchFromDB(long ownerId, int ownerRole,
			long userId, int userRole) {

		String condition = " where " + DBConstant.TABLE_MATCH_FIELD_OWNER_ID
				+ "=" + ownerId + " and "
				+ DBConstant.TABLE_MATCH_FIELD_OWNER_ROLE + "=" + ownerRole
				+ " and " + DBConstant.TABLE_MATCH_FIELD_PARTNER_ID + "="
				+ userId + " and " + DBConstant.TABLE_MATCH_FIELD_PARTNER_ROLE
				+ "=" + userRole;

		return matchDBManager.findOne(condition);
	}

	@Override
	public Map<String, byte[]> getTravellerMatchHeadImagesFromServer(
			long travellerId) {

		Map<String, byte[]> newComesImages = new HashMap<String, byte[]>();
		List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		try {
			JSONMatchesList jsonMatchesList = getTravellerMatchListFromDB(travellerId);
			if (jsonMatchesList != null && jsonMatchesList.getMatches() != null) {
				/* load the images */
				postParameters = new ArrayList<NameValuePair>();
				for (JSONMatch match : jsonMatchesList.getMatches()) {

					// get the image id of the headimage saved in DB of this
					// match
					long headImageId = -1;
					String condition = " where "
							+ DBConstant.TABLE_MATCH_HEAD_IMAGE_FIELD_USER_ID
							+ "=" + match.getUserId() + " AND "
							+ DBConstant.TABLE_MATCH_HEAD_IMAGE_FIELD_USER_ROLE
							+ "=" + match.getUserRole().getNumVal() + " ";

					ImageMatch headImage = matchImageDBManager
							.findOne(condition);
					if (headImage != null) {
						headImageId = headImage.getId();
					}

					if (match.getUserRole() == UserRole.GUIDE) {
						postParameters.add(new BasicNameValuePair("g_"
								+ match.getUserId(), "" + headImageId));
					} else {
						postParameters.add(new BasicNameValuePair("t_"
								+ match.getUserId(), "" + headImageId));
					}
				}
			}
			newComesImages = loadMatchesImages(postParameters,
					Http_API_URL.URL_SERVER_LOAD_TRAVELLER_MATCHES_IMAGES);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newComesImages;
	}

	@Override
	public Map<String, byte[]> getGuideMatchHeadImagesFromServer(long guideId) {

		Map<String, byte[]> newComesImages = new HashMap<String, byte[]>();
		List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		try {
			JSONMatchesList jsonMatchesList = getGuideMatchListFromDB(guideId);
			if (jsonMatchesList != null && jsonMatchesList.getMatches() != null) {
				/* load the images */
				postParameters = new ArrayList<NameValuePair>();
				for (JSONMatch match : jsonMatchesList.getMatches()) {

					// get the image id of the headimage saved in DB of this
					// match
					long headImageId = -1;
					String condition = " where "
							+ DBConstant.TABLE_MATCH_HEAD_IMAGE_FIELD_USER_ID
							+ "=" + match.getUserId() + " AND "
							+ DBConstant.TABLE_MATCH_HEAD_IMAGE_FIELD_USER_ROLE
							+ "=" + match.getUserRole().getNumVal() + " ";

					ImageMatch headImage = matchImageDBManager
							.findOne(condition);
					if (headImage != null) {
						headImageId = headImage.getId();
					}
					postParameters.add(new BasicNameValuePair("t_"
							+ match.getUserId(), "" + headImageId));
				}
			}
			newComesImages = loadMatchesImages(postParameters,
					Http_API_URL.URL_SERVER_LOAD_GUIDE_MATCHES_IMAGES);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newComesImages;
	}

	private Map<String, byte[]> loadMatchesImages(
			List<NameValuePair> postParameters, String url) throws Exception {

		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		post.setEntity(new UrlEncodedFormEntity(postParameters));

		HttpResponse response = client.execute(post);

		Map<String, byte[]> headImageMaps = new HashMap<String, byte[]>();

		if (response.getStatusLine().getStatusCode() == 202
				|| response.getStatusLine().getStatusCode() == 200) {

			InputStream is = response.getEntity().getContent();
			headImageMaps = CommonUtil.unzipImageFilesIntoBytes(is);

			// save the new coming head images into database
			for (Map.Entry<String, byte[]> entry : headImageMaps.entrySet()) {

				String[] headImageInfo = entry.getKey().split("_");
				long userId = Long.valueOf(entry.getKey().split("_")[1]);
				long imageId = Long.valueOf(entry.getKey().split("_")[2]);

				ImageMatch newHeadImage = new ImageMatch();
				newHeadImage.setId(imageId);
				newHeadImage.setUserId(userId);
				newHeadImage.setImageContent(entry.getValue());

				if (headImageInfo[0].startsWith("t")) {
					newHeadImage.setUserRole(UserRole.TRAVELLER);
				} else {
					newHeadImage.setUserRole(UserRole.GUIDE);
				}

				ImageMatch oldHeadImage = getSingleMatchImageFromDB(userId,
						newHeadImage.getUserRole().getNumVal());

				if (oldHeadImage == null) {
					matchImageDBManager.save(newHeadImage);
				} else {
					matchImageDBManager.update(newHeadImage);
				}
			}
		}

		return headImageMaps;
	}

	@Override
	public ImageMatch getSingleMatchImageFromDB(long userId, int userRole) {
		String condition = " where "
				+ DBConstant.TABLE_MATCH_HEAD_IMAGE_FIELD_USER_ID + "="
				+ userId + " AND "
				+ DBConstant.TABLE_MATCH_HEAD_IMAGE_FIELD_USER_ROLE + "="
				+ userRole + " ";
		
		return matchImageDBManager.findOne(condition);
	}

	/****************************************************/
	/**** Cache functions ****/
	/****************************************************/

	private void setTravellerHttpModifiedHeader(HttpRequestBase request,
			long travellerId) {
		/*
		 * set modified header
		 */
		TravellerUpdatedLog log = tulDbManager.findOne(" where "
				+ DBConstant.TABLE_TRAVELLER_UPDATED_LOG_FIELD_TRAVELLER_ID
				+ "=" + travellerId);
		if (log != null) {
			Date date = new Date(log.getMatchesLastUpdated());
			date.setTime(log.getMatchesLastUpdated());
			String time_GMT = DateTimeUtil.fromDateToString_GMT(date);
			request.setHeader(ConfigurationConstant.HTTP_HEADER_MODIFIED_SINCE,
					time_GMT);
		}
	}

	private void setGuideHttpModifiedHeader(HttpRequestBase request,
			long guideId) {
		/*
		 * set modified header
		 */
		GuideUpdatedLog log = gulDbManager.findOne(" where "
				+ DBConstant.TABLE_GUIDE_UPDATED_LOG_FIELD_GUIDE_ID + "="
				+ guideId);
		if (log != null) {
			Date date = new Date(log.getMatchesLastUpdated());
			String time_GMT = DateTimeUtil.fromDateToString_GMT(date);
			request.setHeader(ConfigurationConstant.HTTP_HEADER_MODIFIED_SINCE,
					time_GMT);
		}
	}

	private void updateTravellerCacheMatchUpdateLog(HttpResponse response,
			long travellerId) throws ParseException {
		Header header = response
				.getFirstHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED);
		tulDbManager.updateUpdatedLog(
				DBConstant.TABLE_TRAVELLER_UPDATED_LOG_FIELD_MATCHES_UPDATED,
				DateTimeUtil.fromStringToDate_GMT(header.getValue()).getTime(),
				travellerId);
	}

	private void updateGuideCacheMatchUpdatedLog(HttpResponse response,
			long guideId) throws ParseException {
		Header header = response
				.getFirstHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED);
		gulDbManager.updateUpdatedLog(
				DBConstant.TABLE_GUIDE_UPDATED_LOG_FIELD_MATCHES_UPDATED,
				DateTimeUtil.fromStringToDate_GMT(header.getValue()).getTime(),
				guideId);
	}

}
