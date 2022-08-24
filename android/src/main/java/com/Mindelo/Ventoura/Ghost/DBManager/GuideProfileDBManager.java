package com.Mindelo.Ventoura.Ghost.DBManager;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Constant.DBConstant;
import com.Mindelo.Ventoura.Entity.GuideUpdatedLog;
import com.Mindelo.Ventoura.Enum.Gender;
import com.Mindelo.Ventoura.Enum.PaymentMethod;
import com.Mindelo.Ventoura.Ghost.DBHelper.DAO;
import com.Mindelo.Ventoura.JSONEntity.JSONGuideAttraction;
import com.Mindelo.Ventoura.JSONEntity.JSONGuideProfile;

public class GuideProfileDBManager extends DAO<JSONGuideProfile> {

	private SQLiteDatabase db;

	public GuideProfileDBManager(SQLiteDatabase db) {
		super(db);
		this.db = db;
	}

	public void save(JSONGuideProfile guideProfile) {

		db.execSQL(
				"INSERT INTO "
						+ DBConstant.TABLE_GUIDE_PROFILE
						+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				new Object[] {
						guideProfile.getId(),
						guideProfile.getGuideFirstname(),
						guideProfile.getGuideLastname(),
						guideProfile.getGender().getNumVal(),
						guideProfile.getAge(),
						guideProfile.getFacebookAccountName(),
						guideProfile.getCountry(),
						guideProfile.getCity(),
						guideProfile.getTextBiography(),
						guideProfile.getNumberOfGallery(),
						guideProfile.getNumberOfReviews(),
						guideProfile.getAvgReviewScore(),
						guideProfile.isPremium(),
						guideProfile.getPaymentMethod() == null ? 1
								: guideProfile.getPaymentMethod().getNumVal(),
						guideProfile.getMoneyType(),
						guideProfile.getTourLength(),
						guideProfile.getTourPrice(), guideProfile.getTourType() });

		for (JSONGuideAttraction attraction : guideProfile.getAttractions()) {
			db.execSQL("INSERT INTO " + DBConstant.TABLE_GUIDE_ATTRACTION
					+ " VALUES(?, ?, ?)", new Object[] { attraction.getId(),
					guideProfile.getId(), attraction.getAttractionName() });
		}

		/*
		 * if other singleton things exist, don't create them again
		 */
		Cursor updateLogCursor = getQueryCursor(" where "
				+ DBConstant.TABLE_GUIDE_UPDATED_LOG_FIELD_GUIDE_ID + "="
				+ guideProfile.getId(), DBConstant.TABLE_GUIDE_UPDATED_LOG);
		if (updateLogCursor.getCount() == 0) {
			GuideUpdatedLog log = new GuideUpdatedLog(guideProfile.getId(), 0);
			db.execSQL(
					"INSERT INTO " + DBConstant.TABLE_GUIDE_UPDATED_LOG
							+ " VALUES(null, ?, ?, ?, ?, ?, ?, ?)",
					new Object[] { log.getGuideId(),
							log.getProfileLastUpdated(),
							log.getGalleryLastUpdated(),
							log.getBookingsLastUpdated(),
							log.getReviewLastUpdated(),
							log.getMatchesLastUpdated(),
							log.getVFunctionSettingsLastUpdated() });
		}
		updateLogCursor.close();

		Cursor vFunctionSettingCursor = getQueryCursor(" where "
				+ DBConstant.TABLE_GUIDE_VFUNCTION_SETTINGS_FIELD_GUIDE_ID
				+ "=" + guideProfile.getId(),
				DBConstant.TABLE_GUIDE_VFUNCTION_SETTINGS);
		if (vFunctionSettingCursor.getCount() == 0) {
			db.execSQL(
					"INSERT INTO " + DBConstant.TABLE_GUIDE_VFUNCTION_SETTINGS
							+ " VALUES(?, ?, ?, ?, ?)",
					new Object[] { guideProfile.getId(),
							Gender.BOTH.getNumVal(),
							ConfigurationConstant.VENTOURA_MAX_AGE,
							ConfigurationConstant.VENTOURA_MIN_AGE,
							ConfigurationConstant.VENTOURA_MAX_ACTIVE_DAYS });
		}
		vFunctionSettingCursor.close();

	}

	public void saveAll(List<JSONGuideProfile> guideProfiles) {
		for (JSONGuideProfile guideProfile : guideProfiles) {
			save(guideProfile);
		}
	}

	@Override
	public List<JSONGuideProfile> findAll() {
		Cursor c = getQueryCursor(null, DBConstant.TABLE_GUIDE_PROFILE);
		List<JSONGuideProfile> guideProfiles = loadListEntityFromCursor(c);
		c.close();
		return guideProfiles;
	}

	@Override
	public JSONGuideProfile findOne(String condition) {
		Cursor c = getQueryCursor(condition, DBConstant.TABLE_GUIDE_PROFILE);
		if (c.getCount() == 0)
			return null;
		c.moveToFirst();
		JSONGuideProfile guideProfile = loadSingleEntityFromCursor(c);
		c.close();

		// load attractions
		List<JSONGuideAttraction> attractions = new ArrayList<JSONGuideAttraction>();
		String conditionAttraction = " where "
				+ DBConstant.TABLE_GUIDE_ATTRACTION_FIELD_GUIDE_ID + " = "
				+ guideProfile.getId();
		Cursor c2 = getQueryCursor(conditionAttraction,
				DBConstant.TABLE_GUIDE_ATTRACTION);
		if (c2.getCount() == 0)
			return guideProfile;
		while (c2.moveToNext()) {
			attractions.add(loadSingleAttractionFromCursor(c2));
		}
		guideProfile.setAttractions(attractions);

		return guideProfile;
	}

	@Override
	public List<JSONGuideProfile> findMany(String condition) {
		Cursor c = getQueryCursor(condition, DBConstant.TABLE_GUIDE_PROFILE);
		List<JSONGuideProfile> guideProfiles = loadListEntityFromCursor(c);
		c.close();
		return guideProfiles;
	}

	/**
	 * this method is not a inherated from parents. it is a helping method to
	 * load attrations from DB
	 */
	private JSONGuideAttraction loadSingleAttractionFromCursor(Cursor cursor) {
		JSONGuideAttraction attraction = new JSONGuideAttraction();
		attraction.setId(cursor.getInt(cursor
				.getColumnIndex(DBConstant.TABLE_GUIDE_ATTRACTION_FIELD_ID)));
		attraction.setAttractionName(cursor.getString(cursor
				.getColumnIndex(DBConstant.TABLE_GUIDE_ATTRACTION_FIELD_NAME)));
		return attraction;
	}

	@Override
	public JSONGuideProfile loadSingleEntityFromCursor(Cursor cursor) {
		JSONGuideProfile guideProfile = new JSONGuideProfile();

		guideProfile.setId(cursor.getLong(cursor
				.getColumnIndex(DBConstant.TABLE_GUIDE_PROFILE_FIELD_ID)));
		guideProfile
				.setGuideFirstname(cursor.getString(cursor
						.getColumnIndex(DBConstant.TABLE_GUIDE_PROFILE_FIELD_FIRSTNAME)));
		guideProfile
				.setGuideFirstname(cursor.getString(cursor
						.getColumnIndex(DBConstant.TABLE_GUIDE_PROFILE_FIELD_FIRSTNAME)));
		if (cursor.getInt(cursor
				.getColumnIndex(DBConstant.TABLE_GUIDE_PROFILE_FIELD_GENDER)) == 0) {
			guideProfile.setGender(Gender.MALE);
		} else {
			guideProfile.setGender(Gender.FEMALE);
		}

		guideProfile.setAge(cursor.getInt(cursor
				.getColumnIndex(DBConstant.TABLE_GUIDE_PROFILE_FIELD_AGE)));

		guideProfile
				.setFacebookAccountName(cursor.getString(cursor
						.getColumnIndex(DBConstant.TABLE_GUIDE_PROFILE_FIELD_FACEBOOK)));

		guideProfile.setCountry(cursor.getInt(cursor
				.getColumnIndex(DBConstant.TABLE_GUIDE_PROFILE_FIELD_COUNTRY)));
		guideProfile.setCity(cursor.getInt(cursor
				.getColumnIndex(DBConstant.TABLE_GUIDE_PROFILE_FIELD_CITY)));

		guideProfile
				.setTextBiography(cursor.getString(cursor
						.getColumnIndex(DBConstant.TABLE_GUIDE_PROFILE_FIELD_BIOGRAPHY)));

		guideProfile
				.setNumberOfGallery(cursor.getInt(cursor
						.getColumnIndex(DBConstant.TABLE_GUIDE_PROFILE_FIELD_NUMBER_GALLERY)));

		guideProfile
				.setNumberOfReviews(cursor.getInt(cursor
						.getColumnIndex(DBConstant.TABLE_GUIDE_PROFILE_FIELD_NUMBER_REVIEWS)));

		guideProfile
				.setAvgReviewScore(cursor.getFloat(cursor
						.getColumnIndex(DBConstant.TABLE_GUIDE_PROFILE_FIELD_AVG_REVIEW_SCORE)));

		if (cursor
				.getInt(cursor
						.getColumnIndex(DBConstant.TABLE_GUIDE_PROFILE_FIELD_IS_PREMIUM)) == 0) {
			guideProfile.setPremium(false);
		} else {
			guideProfile.setPremium(true);
		}

		PaymentMethod paymentMethod = PaymentMethod.values()[cursor
				.getInt(cursor
						.getColumnIndex(DBConstant.TABLE_GUIDE_PROFILE_FIELD_PAYMENT_METHOD))];
		guideProfile.setPaymentMethod(paymentMethod);

		guideProfile
				.setTourType(cursor.getString(cursor
						.getColumnIndex(DBConstant.TABLE_GUIDE_PROFILE_FIELD_TOUR_TYPE)));
		guideProfile
				.setTourLength(cursor.getString(cursor
						.getColumnIndex(DBConstant.TABLE_GUIDE_PROFILE_FIELD_TOUR_LENGTH)));
		guideProfile
				.setTourPrice(cursor.getFloat(cursor
						.getColumnIndex(DBConstant.TABLE_GUIDE_PROFILE_FIELD_TOUR_PRICE)));

		return guideProfile;
	}

	@Override
	public void deleteMany(String condition) {
		JSONGuideProfile profile = findOne(condition);
		if (profile != null) {
			delete(" where " + DBConstant.TABLE_GUIDE_ATTRACTION_FIELD_GUIDE_ID
					+ "=" + profile.getId(), DBConstant.TABLE_GUIDE_ATTRACTION); // delete
																					// all
																					// attractions
		}
		delete(condition, DBConstant.TABLE_GUIDE_PROFILE);
	}

}
