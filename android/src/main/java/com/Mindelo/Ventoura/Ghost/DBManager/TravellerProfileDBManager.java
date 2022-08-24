package com.Mindelo.Ventoura.Ghost.DBManager;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Constant.DBConstant;
import com.Mindelo.Ventoura.Entity.TravellerUpdatedLog;
import com.Mindelo.Ventoura.Enum.Gender;
import com.Mindelo.Ventoura.Enum.UserRole;
import com.Mindelo.Ventoura.Ghost.DBHelper.DAO;
import com.Mindelo.Ventoura.JSONEntity.JSONTravellerProfile;

public class TravellerProfileDBManager extends DAO<JSONTravellerProfile> {
	private SQLiteDatabase db;

	public TravellerProfileDBManager(SQLiteDatabase db) {
		super(db);
		this.db = db;
	}

	public void save(JSONTravellerProfile travellerProfile) {
		db.execSQL(
				"INSERT INTO " + DBConstant.TABLE_TRAVELLER_PROFILE
						+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				new Object[] { travellerProfile.getId(),
						travellerProfile.getTravellerFirstname(),
						travellerProfile.getTravellerLastname(),
						travellerProfile.getGender().getNumVal(),
						travellerProfile.getAge(),
						travellerProfile.getFacebookAccountName(),
						travellerProfile.getCountry(),
						travellerProfile.getCity(),
						travellerProfile.getTextBiography(),
						travellerProfile.getNumberOfGallery() });

		/*
		 * if other things exist, don't create them again
		 */
		Cursor updateLogCursor = getQueryCursor(" where "
				+ DBConstant.TABLE_TRAVELLER_UPDATED_LOG_FIELD_TRAVELLER_ID
				+ "=" + travellerProfile.getId(),
				DBConstant.TABLE_TRAVELLER_UPDATED_LOG);
		if (updateLogCursor.getCount() == 0) {
			TravellerUpdatedLog log = new TravellerUpdatedLog(
					travellerProfile.getId(), 0);
			db.execSQL(
					"INSERT INTO " + DBConstant.TABLE_TRAVELLER_UPDATED_LOG
							+ " VALUES(null, ?, ?, ?, ?, ?, ?, ?)",
					new Object[] { log.getTravellerId(),
							log.getProfileLastUpdated(),
							log.getGalleryLastUpdated(),
							log.getToursLastUpdated(),
							log.getBookingsLastUpdated(),
							log.getMatchesLastUpdated(),
							log.getVFunctionSettingsLastUpdated() });
		}
		updateLogCursor.close();

		Cursor vFunctionSettingCursor = getQueryCursor(
				" where "
						+ DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_TRAVELLER_ID
						+ "=" + travellerProfile.getId(),
				DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS);
		if (vFunctionSettingCursor.getCount() == 0) {
			db.execSQL("INSERT INTO "
					+ DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS
					+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)", new Object[] {
					travellerProfile.getId(), Gender.BOTH.getNumVal(),
					UserRole.BOTH.getNumVal(), ConfigurationConstant.VENTOURA_MAX_AGE, ConfigurationConstant.VENTOURA_MIN_AGE, ConfigurationConstant.VENTOURA_MAX_PRICE, ConfigurationConstant.VENTOURA_MIN_PRICE, 0, "" });
		}
		vFunctionSettingCursor.close();

	}

	public void saveAll(List<JSONTravellerProfile> guideProfiles) {
		for (JSONTravellerProfile travellerProfile : guideProfiles) {
			save(travellerProfile);
		}
	}

	@Override
	public List<JSONTravellerProfile> findAll() {
		Cursor c = getQueryCursor(null, DBConstant.TABLE_TRAVELLER_PROFILE);
		List<JSONTravellerProfile> guideProfiles = loadListEntityFromCursor(c);
		c.close();
		return guideProfiles;
	}

	@Override
	public JSONTravellerProfile findOne(String condition) {
		Cursor c = getQueryCursor(condition, DBConstant.TABLE_TRAVELLER_PROFILE);
		if (c.getCount() == 0)
			return null;
		c.moveToFirst();
		JSONTravellerProfile travellerProfile = loadSingleEntityFromCursor(c);
		c.close();
		return travellerProfile;
	}

	@Override
	public List<JSONTravellerProfile> findMany(String condition) {
		Cursor c = getQueryCursor(condition, DBConstant.TABLE_TRAVELLER_PROFILE);
		List<JSONTravellerProfile> guideProfiles = loadListEntityFromCursor(c);
		c.close();
		return guideProfiles;
	}

	@Override
	public JSONTravellerProfile loadSingleEntityFromCursor(Cursor cursor) {
		JSONTravellerProfile travellerProfile = new JSONTravellerProfile();

		travellerProfile.setId(cursor.getLong(cursor
				.getColumnIndex(DBConstant.TABLE_TRAVELLER_PROFILE_FIELD_ID)));
		travellerProfile
				.setTravellerFirstname(cursor.getString(cursor
						.getColumnIndex(DBConstant.TABLE_TRAVELLER_PROFILE_FIELD_FIRSTNAME)));
		travellerProfile
				.setTravellerFirstname(cursor.getString(cursor
						.getColumnIndex(DBConstant.TABLE_TRAVELLER_PROFILE_FIELD_FIRSTNAME)));
		if (cursor
				.getInt(cursor
						.getColumnIndex(DBConstant.TABLE_TRAVELLER_PROFILE_FIELD_GENDER)) == 0) {
			travellerProfile.setGender(Gender.MALE);
		} else {
			travellerProfile.setGender(Gender.FEMALE);
		}

		travellerProfile.setAge(cursor.getInt(cursor
				.getColumnIndex(DBConstant.TABLE_TRAVELLER_PROFILE_FIELD_AGE)));

		travellerProfile
				.setFacebookAccountName(cursor.getString(cursor
						.getColumnIndex(DBConstant.TABLE_TRAVELLER_PROFILE_FIELD_FACEBOOK)));

		travellerProfile
				.setCountry(cursor.getInt(cursor
						.getColumnIndex(DBConstant.TABLE_TRAVELLER_PROFILE_FIELD_COUNTRY)));
		travellerProfile
				.setCity(cursor.getInt(cursor
						.getColumnIndex(DBConstant.TABLE_TRAVELLER_PROFILE_FIELD_CITY)));

		travellerProfile
				.setTextBiography(cursor.getString(cursor
						.getColumnIndex(DBConstant.TABLE_TRAVELLER_PROFILE_FIELD_BIOGRAPHY)));

		travellerProfile
				.setNumberOfGallery(cursor.getInt(cursor
						.getColumnIndex(DBConstant.TABLE_TRAVELLER_PROFILE_FIELD_NUMBER_GALLERY)));

		return travellerProfile;
	}

	@Override
	public void deleteMany(String condition) {
		delete(condition, DBConstant.TABLE_TRAVELLER_PROFILE);
	}
}
