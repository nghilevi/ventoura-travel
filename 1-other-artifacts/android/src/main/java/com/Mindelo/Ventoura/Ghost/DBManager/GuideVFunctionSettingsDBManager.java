package com.Mindelo.Ventoura.Ghost.DBManager;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.Mindelo.Ventoura.Constant.DBConstant;
import com.Mindelo.Ventoura.Enum.Gender;
import com.Mindelo.Ventoura.Ghost.DBHelper.DAO;
import com.Mindelo.Ventoura.JSONEntity.JSONGuideVFunctionSettings;

public class GuideVFunctionSettingsDBManager extends
		DAO<JSONGuideVFunctionSettings> {

	private SQLiteDatabase db;

	public GuideVFunctionSettingsDBManager(SQLiteDatabase db) {
		super(db);
		this.db = db;
	}

	@Override
	public void save(JSONGuideVFunctionSettings entity) {
		// the setting is first saved in the guideProfile manager
	}

	@Override
	public List<JSONGuideVFunctionSettings> findAll() {
		Cursor c = getQueryCursor(null,
				DBConstant.TABLE_GUIDE_VFUNCTION_SETTINGS);
		List<JSONGuideVFunctionSettings> settings = loadListEntityFromCursor(c);
		c.close();
		return settings;
	}

	@Override
	public JSONGuideVFunctionSettings findOne(String condition) {
		Cursor c = getQueryCursor(condition,
				DBConstant.TABLE_GUIDE_VFUNCTION_SETTINGS);
		if (c.getCount() == 0)
			return null;
		c.moveToFirst();
		JSONGuideVFunctionSettings setting = loadSingleEntityFromCursor(c);
		c.close();
		return setting;
	}

	@Override
	public List<JSONGuideVFunctionSettings> findMany(String condition) {
		Cursor c = getQueryCursor(condition,
				DBConstant.TABLE_GUIDE_VFUNCTION_SETTINGS);
		List<JSONGuideVFunctionSettings> settings = loadListEntityFromCursor(c);
		c.close();
		return settings;
	}

	@Override
	public JSONGuideVFunctionSettings loadSingleEntityFromCursor(Cursor cursor) {
		JSONGuideVFunctionSettings setting = new JSONGuideVFunctionSettings();

		setting.setGuideId(cursor.getLong(cursor
				.getColumnIndex(DBConstant.TABLE_GUIDE_VFUNCTION_SETTINGS_FIELD_GUIDE_ID)));

		Gender preferedGender = Gender.values()[cursor
				.getInt(cursor
						.getColumnIndex(DBConstant.TABLE_GUIDE_VFUNCTION_SETTINGS_FIELD_PREFERED_GENDER))];
		setting.setPreferedGender(preferedGender);

		setting.setMaxAge(cursor.getInt(cursor
				.getColumnIndex(DBConstant.TABLE_GUIDE_VFUNCTION_SETTINGS_FIELD_MAX_AGE)));
		setting.setMiniAge(cursor.getInt(cursor
				.getColumnIndex(DBConstant.TABLE_GUIDE_VFUNCTION_SETTINGS_FIELD_MIN_AGE)));

		setting.setLastActivateDays(cursor.getInt(cursor
				.getColumnIndex(DBConstant.TABLE_GUIDE_VFUNCTION_SETTINGS_FIELD_LAST_ACTIVE_DAYS)));

		return setting;
	}

	@Override
	public void deleteMany(String condition) {
		delete(condition, DBConstant.TABLE_GUIDE_VFUNCTION_SETTINGS);
	}
	
	public void updateVFunctionSettings(JSONGuideVFunctionSettings setting) {
		String query = "update " + DBConstant.TABLE_GUIDE_VFUNCTION_SETTINGS
				+ " set " 
				+ DBConstant.TABLE_GUIDE_VFUNCTION_SETTINGS_FIELD_MAX_AGE + "=" + setting.getMaxAge() + "," 
				+ DBConstant.TABLE_GUIDE_VFUNCTION_SETTINGS_FIELD_MIN_AGE + "=" + setting.getMiniAge() + "," 
				+ DBConstant.TABLE_GUIDE_VFUNCTION_SETTINGS_FIELD_PREFERED_GENDER + "=" + setting.getPreferedGender().getNumVal() + "," 
				+ DBConstant.TABLE_GUIDE_VFUNCTION_SETTINGS_FIELD_LAST_ACTIVE_DAYS + "=" + setting.getLastActivateDays()
				+ " where "
				+ DBConstant.TABLE_GUIDE_VFUNCTION_SETTINGS_FIELD_GUIDE_ID
				+ "=" + setting.getGuideId() + " ";
		
		db.execSQL(query);
	}
}
