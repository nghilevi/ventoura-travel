package com.Mindelo.Ventoura.Ghost.DBManager;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.Mindelo.Ventoura.Constant.DBConstant;
import com.Mindelo.Ventoura.Enum.Gender;
import com.Mindelo.Ventoura.Enum.UserRole;
import com.Mindelo.Ventoura.Ghost.DBHelper.DAO;
import com.Mindelo.Ventoura.JSONEntity.JSONGuideVFunctionSettings;
import com.Mindelo.Ventoura.JSONEntity.JSONTravellerVFunctionSettings;

public class TravellerVFunctionSettingsDBManager extends
		DAO<JSONTravellerVFunctionSettings> {

	private SQLiteDatabase db;

	public TravellerVFunctionSettingsDBManager(SQLiteDatabase db) {
		super(db);
		this.db = db;
	}

	@Override
	public void save(JSONTravellerVFunctionSettings entity) {
		// the setting is first save in traveller profile db manager
	}

	@Override
	public List<JSONTravellerVFunctionSettings> findAll() {
		Cursor c = getQueryCursor(null,
				DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS);
		List<JSONTravellerVFunctionSettings> settings = loadListEntityFromCursor(c);
		c.close();
		return settings;
	}

	@Override
	public JSONTravellerVFunctionSettings findOne(String condition) {
		Cursor c = getQueryCursor(condition,
				DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS);
		if (c.getCount() == 0)
			return null;
		c.moveToFirst();
		JSONTravellerVFunctionSettings setting = loadSingleEntityFromCursor(c);
		c.close();
		return setting;
	}

	@Override
	public List<JSONTravellerVFunctionSettings> findMany(String condition) {
		Cursor c = getQueryCursor(condition,
				DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS);
		List<JSONTravellerVFunctionSettings> settings = loadListEntityFromCursor(c);
		c.close();
		return settings;
	}

	@Override
	public JSONTravellerVFunctionSettings loadSingleEntityFromCursor(
			Cursor cursor) {
		JSONTravellerVFunctionSettings setting = new JSONTravellerVFunctionSettings();

		setting.setTravellerId(cursor.getLong(cursor
				.getColumnIndex(DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_TRAVELLER_ID)));

		Gender preferedGender = Gender.values()[cursor
				.getInt(cursor
						.getColumnIndex(DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_PREFERED_GENDER))];
		setting.setPreferedGender(preferedGender);

		UserRole userRole = UserRole.values()[cursor
				.getInt(cursor
						.getColumnIndex(DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_PREFERED_USER_ROLE))];
		setting.setPreferedUserRole(userRole);

		setting.setMaxAge(cursor.getInt(cursor
				.getColumnIndex(DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_MAX_AGE)));
		setting.setMiniAge(cursor.getInt(cursor
				.getColumnIndex(DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_MIN_AGE)));

		setting.setMaxPrice(cursor.getFloat(cursor
				.getColumnIndex(DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_MAX_PRICE)));
		setting.setMiniPrice(cursor.getFloat(cursor
				.getColumnIndex(DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_MIN_PRICE)));

		setting.setSpecifyCityToggle(cursor.getInt(cursor
				.getColumnIndex(DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_SPECIFY_CITY_TOGGLE)) == 1 ? true
				: false);

		setting.setCityIds(cursor.getString(cursor
				.getColumnIndex(DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_SPECIFY_CITY_IDS)));

		return setting;
	}

	@Override
	public void deleteMany(String condition) {
		delete(condition, DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS);
	}

	public void updateVFunctionSettings(JSONTravellerVFunctionSettings setting) {
		String query = "update " + DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS
				+ " set " 
				+ DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_MAX_AGE + "=" + setting.getMaxAge() + "," 
				+ DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_MIN_AGE + "=" + setting.getMiniAge() + "," 
				+ DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_MAX_PRICE + "=" + setting.getMaxPrice() + "," 
				+ DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_MIN_PRICE + "=" + setting.getMiniPrice() + "," 
				+ DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_PREFERED_GENDER + "=" + setting.getPreferedGender().getNumVal() + "," 
				+ DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_PREFERED_USER_ROLE + "=" + setting.getPreferedUserRole().getNumVal() + "," 
				+ DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_SPECIFY_CITY_TOGGLE + "=" + (setting.isSpecifyCityToggle()==true?1:0) + "," 
				+ DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_SPECIFY_CITY_IDS + "='" + setting.getCityIds() + "'" 
				+ " where "
				+ DBConstant.TABLE_TRAVELLER_VFUNCTION_SETTINGS_FIELD_TRAVELLER_ID
				+ "=" + setting.getTravellerId() + " ";
		
		db.execSQL(query);
	}
	
}
