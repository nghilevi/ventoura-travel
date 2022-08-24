package com.Mindelo.Ventoura.Ghost.Service;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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
import com.Mindelo.Ventoura.Entity.ImageProfile;
import com.Mindelo.Ventoura.Ghost.DBHelper.VentouraDBHelper;
import com.Mindelo.Ventoura.Ghost.DBManager.GuideGalleryImageDBManager;
import com.Mindelo.Ventoura.Ghost.DBManager.GuideUpdatedLogDBManager;
import com.Mindelo.Ventoura.Ghost.DBManager.TravellerGalleryImageDBManager;
import com.Mindelo.Ventoura.Ghost.DBManager.TravellerUpdatedLogDBManager;
import com.Mindelo.Ventoura.Ghost.IService.IGalleryService;
import com.Mindelo.Ventoura.Util.CommonUtil;
import com.Mindelo.Ventoura.Util.DateTimeUtil;
import com.Mindelo.Ventoura.Util.HttpUtility;

public class GalleryService implements IGalleryService {

	private SQLiteDatabase db;
	private VentouraDBHelper ventouraDbHelper;

	private GuideGalleryImageDBManager guideGalleryImageDBManager;
	private GuideUpdatedLogDBManager gulDbManager;

	private TravellerGalleryImageDBManager travellerGalleryImageDBManager;
	private TravellerUpdatedLogDBManager tulDbManager;

	public GalleryService(Context context) {
		ventouraDbHelper = new VentouraDBHelper(context);
		db = ventouraDbHelper.getWritableDatabase();
		gulDbManager = new GuideUpdatedLogDBManager(this.db);
		guideGalleryImageDBManager = new GuideGalleryImageDBManager(this.db);

		tulDbManager = new TravellerUpdatedLogDBManager(this.db);
		travellerGalleryImageDBManager = new TravellerGalleryImageDBManager(
				this.db);
	}

	@Override
	public boolean deleteGuideGallery(long guideId, long galleryId) {
		try {
			HttpGet get = new HttpGet(
					Http_API_URL.URL_SERVER_DELETE_GUIDE_GALLERY + "/"
							+ galleryId);

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {

				guideGalleryImageDBManager.deleteMany(" where "
						+ DBConstant.TABLE_GUIDE_PROFILE_GALLERY_FIELD_ID + "="
						+ galleryId);

				updateGuideCacheGalleryUpdatedLog(response, guideId);
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
	public boolean deleteTravellerGallery(long travellerId, long galleryId) {
		try {
			HttpGet get = new HttpGet(
					Http_API_URL.URL_SERVER_DELETE_TRAVELLER_GALLERY + "/"
							+ galleryId);

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {

				travellerGalleryImageDBManager.deleteMany(" where "
						+ DBConstant.TABLE_TRAVELLER_PROFILE_GALLERY_FIELD_ID
						+ "=" + galleryId);
				updateTravellerCacheGalleryUpdatedLog(response, travellerId);

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
	public long uploadSingleTravellerGallery(long travellerId,
			byte[] imageContent) {
		try {
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

			builder.addTextBody(HttpFieldConstant.SERVER_TRAVELLER_ID, ""
					+ travellerId);

			builder.addBinaryBody(HttpFieldConstant.TRAVELLER_NEW_PHOTO,
					imageContent, ContentType.MULTIPART_FORM_DATA,
					HttpFieldConstant.TRAVELLER_NEW_PHOTO);

			HttpPost post = new HttpPost(
					Http_API_URL.URL_SERVER_UPLOAD_SINGLE_TRAVELLER_GALLERY);

			post.setEntity(builder.build());

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(post);

			ImageProfile imageProfile = new ImageProfile();
			imageProfile.setId(HttpUtility
					.parseResponseMessageLongValue(response));
			imageProfile.setUserId(travellerId);
			imageProfile.setPortal(false);
			imageProfile.setImageContent(imageContent);
			travellerGalleryImageDBManager.save(imageProfile);
			updateTravellerCacheGalleryUpdatedLog(response, travellerId);
			
			return imageProfile.getId();

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public List<ImageProfile> loadTravellerAllGalleryImagesIntoList(
			long travellerId) {

		Map<String, byte[]> images = downloadGalleryImagesFromServerIntoBytes(Http_API_URL.URL_SERVER_GET_TRAVELLER_GALLERY_IMAGES
				+ "/" + travellerId);

		List<ImageProfile> imageLists = new ArrayList<ImageProfile>();

		// ensure the first image is the portal image
		for (Map.Entry<String, byte[]> entry : images.entrySet()) {

			ImageProfile image = new ImageProfile();
			image.setId(Long.valueOf(entry.getKey().split("_")[1]));
			image.setUserId(travellerId);
			image.setImageContent(entry.getValue());

			if (entry.getKey().contains("tp")) {
				image.setPortal(true);
				imageLists.add(0, image);
			} else {
				image.setPortal(false);
				imageLists.add(image);
			}

		}
		return imageLists;
	}

	public List<ImageProfile> loadGuideAllGalleryImagesIntoList(long guideId) {

		Map<String, byte[]> images = downloadGalleryImagesFromServerIntoBytes(Http_API_URL.URL_SERVER_GET_GUIDE_GALLERY_IMAGES
				+ "/" + guideId);

		List<ImageProfile> imageLists = new ArrayList<ImageProfile>();

		// ensure the first image is the portal image
		for (Map.Entry<String, byte[]> entry : images.entrySet()) {

			ImageProfile image = new ImageProfile();
			image.setId(Long.valueOf(entry.getKey().split("_")[1]));
			image.setUserId(guideId);
			image.setImageContent(entry.getValue());

			if (entry.getKey().contains("gp")) {
				image.setPortal(true);
				imageLists.add(0, image);
			} else {
				image.setPortal(false);
				imageLists.add(image);
			}

		}
		return imageLists;
	}

	private Map<String, byte[]> downloadGalleryImagesFromServerIntoBytes(
			String url) {
		try {
			HttpGet get = new HttpGet(url);

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {
				/*
				 * this method is used to download temp galleris from server, no need cache
				 */
				InputStream is = response.getEntity().getContent();
				return CommonUtil.unzipImageFilesIntoBytes(is);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public long uploadSingleGuideGallery(long guideId, byte[] imageContent) {
		try {
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

			builder.addTextBody(HttpFieldConstant.SERVER_GUIDE_ID, "" + guideId);

			builder.addBinaryBody(HttpFieldConstant.GUIDE_NEW_PHOTO,
					imageContent, ContentType.MULTIPART_FORM_DATA,
					HttpFieldConstant.GUIDE_NEW_PHOTO);

			HttpPost post = new HttpPost(
					Http_API_URL.URL_SERVER_UPLOAD_SINGLE_GUIDE_GALLERY);

			post.setEntity(builder.build());

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(post);

			ImageProfile imageProfile = new ImageProfile();
			imageProfile.setId(HttpUtility
					.parseResponseMessageLongValue(response));
			imageProfile.setUserId(guideId);
			imageProfile.setPortal(false);
			imageProfile.setImageContent(imageContent);

			guideGalleryImageDBManager.save(imageProfile);
			updateGuideCacheGalleryUpdatedLog(response, guideId);
			
			return imageProfile.getId();

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public boolean setTravellerPortalImage(long travellerId, long galleryId) {
		try {
			HttpGet get = new HttpGet(
					Http_API_URL.URL_SERVER_SET_TRAVELLER_PORTAL_GALLERY + "/"
							+ travellerId + "/" + galleryId);

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {

				travellerGalleryImageDBManager.setImageAsPortal(travellerId,
						galleryId);
				updateTravellerCacheGalleryUpdatedLog(response, travellerId);

				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean setGuidePortalImage(long guideId, long galleryId) {
		try {
			HttpGet get = new HttpGet(
					Http_API_URL.URL_SERVER_SET_GUIDE_PORTAL_GALLERY + "/"
							+ guideId + "/" + galleryId);

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {

				guideGalleryImageDBManager.setImageAsPortal(guideId, galleryId);
				updateGuideCacheGalleryUpdatedLog(response, guideId);

				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean deleteBatchTravellerGallery(long travellerId, List<Long> galleryIds) {

		if (galleryIds.size() == 0) {
			return true;
		}

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		try {
			for (Long galleryId : galleryIds) {
				nameValuePairs.add(new BasicNameValuePair(galleryId + "", ""));
			}
			HttpPost post = new HttpPost(
					Http_API_URL.URL_SERVER_TRAVELLER_DELETE_BATCH_GALLERY);
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(post);

			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {
				
				for(long galleryId : galleryIds){
					travellerGalleryImageDBManager.deleteMany(" where " + DBConstant.TABLE_TRAVELLER_PROFILE_GALLERY_FIELD_ID + "=" + galleryId);	
				}
				updateTravellerCacheGalleryUpdatedLog(response, travellerId);
				
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	@Override
	public boolean deleteBatchGuideGallery(long guideId, List<Long> galleryIds) {

		if (galleryIds.size() == 0) {
			return true;
		}

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		try {
			for (Long galleryId : galleryIds) {
				nameValuePairs.add(new BasicNameValuePair(galleryId + "", ""));
			}
			HttpPost post = new HttpPost(
					Http_API_URL.URL_SERVER_GUIDE_DELETE_BATCH_GALLERY);
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(post);

			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {
				
				for(long galleryId : galleryIds){
					guideGalleryImageDBManager.deleteMany(" where " + DBConstant.TABLE_GUIDE_PROFILE_GALLERY_FIELD_ID + "=" + galleryId);	
				}
				updateGuideCacheGalleryUpdatedLog(response, guideId);
				
				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	@Override
	public ImageProfile getTravellerPortalImage(long travellerId) {
		try {
			ImageProfile portalImage = new ImageProfile();
			portalImage
					.setImageContent(HttpUtility
							.downloadImageFromUrl(Http_API_URL.URL_SERVER_GET_TRAVELLER_PORTALIMG
									+ "/" + travellerId));
			portalImage.setPortal(true);
			return portalImage;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ImageProfile getGuidePortalImage(long guideId) {
		try {
			ImageProfile portalImage = new ImageProfile();
			portalImage
					.setImageContent(HttpUtility
							.downloadImageFromUrl(Http_API_URL.URL_SERVER_GET_GUIDE_PORTALIMG
									+ "/" + guideId));
			portalImage.setPortal(true);
			return portalImage;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/****************************************************/
	/**** Cache functions ****/
	/****************************************************/
	private void updateTravellerCacheGalleryUpdatedLog(HttpResponse response,
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

	private void updateGuideCacheGalleryUpdatedLog(HttpResponse response,
			long guideId) throws ParseException {
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
}
