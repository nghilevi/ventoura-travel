package com.Mindelo.Ventoura.Ghost.DBHelper;

import com.Mindelo.Ventoura.Constant.DBConstant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class VentouraDBHelper extends SQLiteOpenHelper {
	private static final int DATABASE_VERSION = 1;

	public VentouraDBHelper(Context context) {
		// CursorFactory
		super(context, DBConstant.VENTOURA_DB_NAME, null, DATABASE_VERSION);
	}

	// first time on create the db
	@Override
	public void onCreate(SQLiteDatabase db) {
		// news table for chatting history
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DBConstant.TABLE_CHATTING_HISTORY + "("
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_ID
				+ " INTEGER primary key autoincrement, "
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_USER_ID
				+ " INTEGER, "
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_PARTNER_ID
				+ " INTEGER, "
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_USER_ROLE
				+ " INTEGER,"
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_PARTNER_ROLE
				+ " INTEGER,"
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_DATETIME + " text,"
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_ISREAD + " INTEGER,"
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_MINE + " INTEGER,"
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_STATUS_MESSAGE
				+ " INTEGER,"
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_MESSAGE_CONTENT
				+ " text)");

		// news table for matches
		db.execSQL("CREATE TABLE IF NOT EXISTS " + DBConstant.TABLE_MATCH + "("
				+ DBConstant.TABLE_MATCH_FIELD_ID
				+ " INTEGER primary key autoincrement, "
				+ DBConstant.TABLE_MATCH_FIELD_OWNER_ID + " INTEGER, "
				+ DBConstant.TABLE_MATCH_FIELD_OWNER_ROLE + " INTEGER,"
				+ DBConstant.TABLE_MATCH_FIELD_PARTNER_ID + " INTEGER, "
				+ DBConstant.TABLE_MATCH_FIELD_PARTNER_ROLE + " INTEGER,"
				+ DBConstant.TABLE_MATCH_FIELD_PARTNER_FIRSTNAME + " text,"
				+ DBConstant.TABLE_MATCH_FIELD_TIME_MATCHED + " text,"
				+ DBConstant.TABLE_MATCH_FIELD_CITY + " INTEGER,"
				+ DBConstant.TABLE_MATCH_FIELD_COUNTRY + " INTEGER,"
				+ DBConstant.TABLE_MATCH_FIELD_GENDER + " INTEGER, "
				+ DBConstant.TABLE_MATCH_FIELD_AGE + " INTEGER)");


		// news table for match images
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DBConstant.TABLE_MATCH_HEAD_IMAGE + "("
				+ DBConstant.TABLE_MATCH_HEAD_IMAGE_FIELD_ID + " INTEGER  , "
				+ DBConstant.TABLE_MATCH_HEAD_IMAGE_FIELD_USER_ID
				+ " INTEGER, "
				+ DBConstant.TABLE_MATCH_HEAD_IMAGE_FIELD_USER_ROLE
				+ " INTEGER, "
				+ DBConstant.TABLE_MATCH_HEAD_IMAGE_FIELD_CONTENT + " BLOB, "
				+ "PRIMARY KEY ("
				+ DBConstant.TABLE_MATCH_HEAD_IMAGE_FIELD_USER_ID + ","
				+ DBConstant.TABLE_MATCH_HEAD_IMAGE_FIELD_USER_ROLE + ") "
				+ ")");

		// news table for matches
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DBConstant.TABLE_TRAVELLER_SCHEDULE + "("
				+ DBConstant.TABLE_TRAVELLER_SCHEDULE_FIELD_ID
				+ " INTEGER primary key , "
				+ DBConstant.TABLE_TRAVELLER_SCHEDULE_FIELD_TRAVELLER_ID
				+ " INTEGER, "
				+ DBConstant.TABLE_TRAVELLER_SCHEDULE_FIELD_START_TIME
				+ " TEXT," + DBConstant.TABLE_TRAVELLER_SCHEDULE_FIELD_END_TIME
				+ " TEXT," + DBConstant.TABLE_TRAVELLER_SCHEDULE_FIELD_CITY
				+ " INTEGER,"
				+ DBConstant.TABLE_TRAVELLER_SCHEDULE_FIELD_COUNTRY
				+ " INTEGER)");

		// news table for bookings
		db.execSQL("CREATE TABLE IF NOT EXISTS " + DBConstant.TABLE_BOOKINGS
				+ "(" + DBConstant.TABLE_BOOKINGS_FIELD_ID
				+ " INTEGER primary key , "
				+ DBConstant.TABLE_BOOKINGS_FIELD_TRAVELLER_ID + " INTEGER, "
				+ DBConstant.TABLE_BOOKINGS_FIELD_TRAVELLER_FIRSTNAME + " TEXT," 
				+ DBConstant.TABLE_BOOKINGS_FIELD_TRAVELLER_AGE + " INTEGER, "
				+ DBConstant.TABLE_BOOKINGS_FIELD_TRAVELLER_GENDER + " INTEGER, "
				
				+ DBConstant.TABLE_BOOKINGS_FIELD_GUIDE_ID + " INTEGER, "
				+ DBConstant.TABLE_BOOKINGS_FIELD_GUIDE_FIRSTNAME + " TEXT,"
				+ DBConstant.TABLE_BOOKINGS_FIELD_GUIDE_AGE + " INTEGER, "
				+ DBConstant.TABLE_BOOKINGS_FIELD_GUIDE_GENDER + " INTEGER, "
				
				+ DBConstant.TABLE_BOOKINGS_FIELD_STATUS_LAST_UPDATED_DATE + " TEXT, "
				+ DBConstant.TABLE_BOOKINGS_FIELD_BOOKING_STATUS + " INTEGER,"
				+ DBConstant.TABLE_BOOKINGS_FIELD_TOUR_PRICE + " REAL,"
				+ DBConstant.TABLE_BOOKINGS_FIELD_TOUR_DATE + " TEXT)");

		// news table for traveller updated log
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DBConstant.TABLE_TRAVELLER_UPDATED_LOG + "("
				+ DBConstant.TABLE_TRAVELLER_UPDATED_LOG_FIELD_ID
				+ " INTEGER primary key autoincrement, "
				+ DBConstant.TABLE_TRAVELLER_UPDATED_LOG_FIELD_TRAVELLER_ID
				+ " INTEGER, "
				+ DBConstant.TABLE_TRAVELLER_UPDATED_LOG_FIELD_PROFILE_UPDATED
				+ " INTEGER, "
				+ DBConstant.TABLE_TRAVELLER_UPDATED_LOG_FIELD_GALLERY_UPDATED
				+ " INTEGER, "
				+ DBConstant.TABLE_TRAVELLER_UPDATED_LOG_FIELD_TOURS_UPDATED
				+ " INTEGER, "
				+ DBConstant.TABLE_TRAVELLER_UPDATED_LOG_FIELD_BOOKINGS_UPDATED
				+ " INTEGER, "
				+ DBConstant.TABLE_TRAVELLER_UPDATED_LOG_FIELD_MATCHES_UPDATED
				+ " INTEGER, "
				+ DBConstant.TABLE_TRAVELLER_UPDATED_LOG_FIELD_VFUNCTION_SETTINGS_UPDATED
				+ " INTEGER " + ")");

		// news table for guide updated log
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DBConstant.TABLE_GUIDE_UPDATED_LOG + "("
				+ DBConstant.TABLE_GUIDE_UPDATED_LOG_FIELD_ID
				+ " INTEGER primary key autoincrement, "
				+ DBConstant.TABLE_GUIDE_UPDATED_LOG_FIELD_GUIDE_ID
				+ " INTEGER, "
				+ DBConstant.TABLE_GUIDE_UPDATED_LOG_FIELD_PROFILE_UPDATED
				+ " INTEGER, "
				+ DBConstant.TABLE_GUIDE_UPDATED_LOG_FIELD_GALLERY_UPDATED
				+ " INTEGER, "
				+ DBConstant.TABLE_GUIDE_UPDATED_LOG_FIELD_BOOKINGS_UPDATED
				+ " INTEGER, "
				+ DBConstant.TABLE_GUIDE_UPDATED_LOG_FIELD_REVIEWS_UPDATED
				+ " INTEGER, "
				+ DBConstant.TABLE_GUIDE_UPDATED_LOG_FIELD_MATCHES_UPDATED
				+ " INTEGER, "
				+ DBConstant.TABLE_GUIDE_UPDATED_LOG_FIELD_VFUNCTION_SETTINGS_UPDATED
				+ " INTEGER " + ")");

		// news table for guide profile
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DBConstant.TABLE_GUIDE_PROFILE + "("
				+ DBConstant.TABLE_GUIDE_PROFILE_FIELD_ID
				+ " INTEGER primary key , "
				+ DBConstant.TABLE_GUIDE_PROFILE_FIELD_FIRSTNAME + " TEXT, "
				+ DBConstant.TABLE_GUIDE_PROFILE_FIELD_LASTNAME + " TEXT, "
				+ DBConstant.TABLE_GUIDE_PROFILE_FIELD_GENDER + " INTEGER, "
				+ DBConstant.TABLE_GUIDE_PROFILE_FIELD_AGE + " INTEGER, "
				+ DBConstant.TABLE_GUIDE_PROFILE_FIELD_FACEBOOK + " TEXT, "
				+ DBConstant.TABLE_GUIDE_PROFILE_FIELD_COUNTRY + " INTEGER, "
				+ DBConstant.TABLE_GUIDE_PROFILE_FIELD_CITY + " INTEGER, "
				+ DBConstant.TABLE_GUIDE_PROFILE_FIELD_BIOGRAPHY + " TEXT, "
				+ DBConstant.TABLE_GUIDE_PROFILE_FIELD_NUMBER_GALLERY
				+ " INTEGER, "
				+ DBConstant.TABLE_GUIDE_PROFILE_FIELD_NUMBER_REVIEWS
				+ " INTEGER, "
				+ DBConstant.TABLE_GUIDE_PROFILE_FIELD_AVG_REVIEW_SCORE
				+ " REAL, " + DBConstant.TABLE_GUIDE_PROFILE_FIELD_IS_PREMIUM
				+ " INTEGER, "
				+ DBConstant.TABLE_GUIDE_PROFILE_FIELD_PAYMENT_METHOD
				+ " INTEGER, "
				+ DBConstant.TABLE_GUIDE_PROFILE_FIELD_MONEY_TYPE
				+ " INTEGER, "
				+ DBConstant.TABLE_GUIDE_PROFILE_FIELD_TOUR_LENGTH + " TEXT, "
				+ DBConstant.TABLE_GUIDE_PROFILE_FIELD_TOUR_PRICE + " REAL, "
				+ DBConstant.TABLE_GUIDE_PROFILE_FIELD_TOUR_TYPE + " TEXT "
				+ ")");

		// news table for guide attraction
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DBConstant.TABLE_GUIDE_ATTRACTION + "("
				+ DBConstant.TABLE_GUIDE_ATTRACTION_FIELD_ID
				+ " INTEGER primary key , "
				+ DBConstant.TABLE_GUIDE_ATTRACTION_FIELD_GUIDE_ID
				+ " INTEGER REFERENCES " + DBConstant.TABLE_GUIDE_PROFILE + "("
				+ DBConstant.TABLE_GUIDE_PROFILE_FIELD_ID + ")"
				+ " ON DELETE CASCADE , "
				+ DBConstant.TABLE_GUIDE_ATTRACTION_FIELD_NAME + " TEXT " + ")");

		// news table for traveller profile
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DBConstant.TABLE_TRAVELLER_PROFILE + "("
				+ DBConstant.TABLE_TRAVELLER_PROFILE_FIELD_ID
				+ " INTEGER primary key , "
				+ DBConstant.TABLE_TRAVELLER_PROFILE_FIELD_FIRSTNAME
				+ " TEXT, " + DBConstant.TABLE_TRAVELLER_PROFILE_FIELD_LASTNAME
				+ " TEXT, " + DBConstant.TABLE_TRAVELLER_PROFILE_FIELD_GENDER
				+ " INTEGER, " + DBConstant.TABLE_TRAVELLER_PROFILE_FIELD_AGE
				+ " INTEGER, "
				+ DBConstant.TABLE_TRAVELLER_PROFILE_FIELD_FACEBOOK + " TEXT, "
				+ DBConstant.TABLE_TRAVELLER_PROFILE_FIELD_COUNTRY
				+ " INTEGER, " + DBConstant.TABLE_TRAVELLER_PROFILE_FIELD_CITY
				+ " INTEGER, "
				+ DBConstant.TABLE_TRAVELLER_PROFILE_FIELD_BIOGRAPHY
				+ " TEXT, "
				+ DBConstant.TABLE_TRAVELLER_PROFILE_FIELD_NUMBER_GALLERY
				+ " INTEGER " + ")");

		// news table for traveller profile gallery
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DBConstant.TABLE_TRAVELLER_PROFILE_GALLERY + "("
				+ DBConstant.TABLE_TRAVELLER_PROFILE_GALLERY_FIELD_ID
				+ " INTEGER primary key , "
				+ DBConstant.TABLE_TRAVELLER_PROFILE_GALLERY_FIELD_TRAVELLER_ID
				+ " INTEGER, "
				+ DBConstant.TABLE_TRAVELLER_PROFILE_GALLERY_FIELD_IS_PORTAL
				+ " INTEGER, "
				+ DBConstant.TABLE_TRAVELLER_PROFILE_GALLERY_FIELD_CONTENT
				+ " BLOB " + ")");

		// news table for guide profile gallery
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DBConstant.TABLE_GUIDE_PROFILE_GALLERY + "("
				+ DBConstant.TABLE_GUIDE_PROFILE_GALLERY_FIELD_ID
				+ " INTEGER primary key , "
				+ DBConstant.TABLE_GUIDE_PROFILE_GALLERY_FIELD_GUIDE_ID
				+ " INTEGER, "
				+ DBConstant.TABLE_GUIDE_PROFILE_GALLERY_FIELD_IS_PORTAL
				+ " INTEGER, "
				+ DBConstant.TABLE_GUIDE_PROFILE_GALLERY_FIELD_CONTENT
				+ " BLOB " + ")");

		// news table for traveller vfunction settings
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS
				+ "("
				+ DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_TRAVELLER_ID
				+ " INTEGER primary key , "
				+ DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_PREFERED_GENDER
				+ " INTEGER, "
				+ DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_PREFERED_USER_ROLE
				+ " INTEGER, "
				+ DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_MAX_AGE
				+ " INTEGER, "
				+ DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_MIN_AGE
				+ " INTEGER, "
				+ DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_MAX_PRICE
				+ " REAL, "
				+ DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_MIN_PRICE
				+ " REAL, "
				+ DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_SPECIFY_CITY_TOGGLE
				+ " INTEGER, " 
				+ DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_SPECIFY_CITY_IDS
				+ " TEXT " + ")");

		// news table for guide vfunction settings
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ DBConstant.TABLE_GUIDE_VFUNCTION_SETTINGS
				+ "("
				+ DBConstant.TABLE_GUIDE_VFUNCTION_SETTINGS_FIELD_GUIDE_ID
				+ " INTEGER primary key , "
				+ DBConstant.TABLE_GUIDE_VFUNCTION_SETTINGS_FIELD_PREFERED_GENDER
				+ " INTEGER, "
				+ DBConstant.TABLE_GUIDE_VFUNCTION_SETTINGS_FIELD_MAX_AGE
				+ " INTEGER, "
				+ DBConstant.TABLE_GUIDE_VFUNCTION_SETTINGS_FIELD_MIN_AGE
				+ " INTEGER, "
				+ DBConstant.TABLE_GUIDE_VFUNCTION_SETTINGS_FIELD_LAST_ACTIVE_DAYS
				+ " INTEGER " + ")");

	}

	// 如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// db.execSQL("ALTER TABLE person ADD COLUMN other STRING");

	}
}