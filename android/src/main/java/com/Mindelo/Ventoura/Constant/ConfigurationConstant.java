package com.Mindelo.Ventoura.Constant;

import android.os.Environment;

public class ConfigurationConstant {
	
	// payment system
	public static final double VENTOURA_BOOKING_PERCENTAGE = 0.1;
	
	// Ventoura data storage root path
	public static final String VENTOURA_STORAGE_ROOT = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/Ventoura";
	
	
	public static final String VENTOURA_PROMOTION_CITY_IMAGES_PATH = VENTOURA_STORAGE_ROOT
			+ "/promotionCityImages";
	public static final String VENTOURA_PROMOTION_CITY_IMAGES_FILENAME = "promotion.png";


	public static final String VENTOURA_DB_PATH = VENTOURA_STORAGE_ROOT
			+ "/ventouraDB";
	
	public static final String VENTOURA_ASSET_COUNTRY_FLAG = "countryFlag";
	
	public static final String VENTOURA_ASSET_DB = "Ventoura_ExternalDB.db";

	public static final int SHRINK_USER_IMAGE_WIDTH = 320;
	public static final int SHRINK_USER_IMAGE_HEIGHT = 320;

	public static final int NORMAL_USER_PORTAL_IMAGE_WIDTH = 320;
	public static final int NORMAL_USER_PORTAL_IMAGE_HEIGHT = 320;
	
	public static final int LARGE_USER_PORTAL_IMAGE_WIDTH = 500;
	public static final int LARGE_USER_PORTAL_IMAGE_HEIGHT = 500;

	public static final int SMALL_USER_PORTAL_IMAGE_WIDTH = 100;
	public static final int SMALL_USER_PORTAL_IMAGE_HEIGHT = 100;

	public static final int GALLEY_USER_IMAGE_SLIDE_DISPLAY_WIDTH = 100;
	public static final int GALLEY_USER_IMAGE_SLIDE_DISPLAY_HEIGHT = 100;

	public static final int AUTO_COMPLETE_CITY_SUGGRSTION_NUMBER = 5;
	public static final int AUTO_COMPLETE_COUNTRY_SUGGRSTION_NUMBER = 5;

	public static final int GUIDE_PROFILE_ATTRACTIONS_MAX_NUMBER = 5;
	
	public static final String HTTP_HEADER_MODIFIED_SINCE = "If-Modified-Since";
	public static final String HTTP_HEADER_LASR_MODIFIED = "Last-Modified";

	public static final int MINCROPIMAGESIZE= 1024; // the minimum size of an image allowed to upload
	
	public static final int NUMBER_OF_CHATTING_HISTORY_PER_LOAD = 20;
	
	public static final int VENTOURA_MAX_AGE = 120;
	public static final int VENTOURA_MIN_AGE = 0;
	public static final int VENTOURA_MAX_PRICE = 1000;
	public static final int VENTOURA_MIN_PRICE = 0;
	public static final int VENTOURA_MAX_ACTIVE_DAYS = 90;
}
