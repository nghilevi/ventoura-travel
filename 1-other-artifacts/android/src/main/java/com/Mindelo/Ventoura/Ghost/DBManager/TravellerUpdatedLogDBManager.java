package com.Mindelo.Ventoura.Ghost.DBManager;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.Mindelo.Ventoura.Ghost.DBHelper.DAO;
import com.Mindelo.Ventoura.Constant.DBConstant;
import com.Mindelo.Ventoura.Entity.TravellerUpdatedLog;

public class TravellerUpdatedLogDBManager extends DAO<TravellerUpdatedLog> {

	private SQLiteDatabase db;

	public TravellerUpdatedLogDBManager(SQLiteDatabase db) {
		super(db);
		this.db = db;
	}

	@Override
	public void save(TravellerUpdatedLog log) {
		// See also in the guidepfile save. 
		// you don't need to call this function
	}

	@Override
	public List<TravellerUpdatedLog> findAll() {
		Cursor c = getQueryCursor(null, DBConstant.TABLE_TRAVELLER_UPDATED_LOG);
		List<TravellerUpdatedLog> logs = loadListEntityFromCursor(c);
		c.close();
		return logs;
	}

	@Override
	public TravellerUpdatedLog findOne(String condition) {
		Cursor c = getQueryCursor(condition, DBConstant.TABLE_TRAVELLER_UPDATED_LOG);
		if (c.getCount() == 0)
			return null;
		c.moveToFirst();
		TravellerUpdatedLog  log = loadSingleEntityFromCursor(c);
		c.close();
		return log;
	}

	@Override
	public List<TravellerUpdatedLog> findMany(String condition) {
		Cursor c = getQueryCursor(condition, DBConstant.TABLE_MATCH);
		List<TravellerUpdatedLog> logs = loadListEntityFromCursor(c);
		c.close();
		return logs;
	}

	@Override
	public TravellerUpdatedLog loadSingleEntityFromCursor(Cursor cursor) {
		TravellerUpdatedLog log = new TravellerUpdatedLog();

		log.setTravellerId(cursor.getLong(cursor
				.getColumnIndex(DBConstant.TABLE_TRAVELLER_UPDATED_LOG_FIELD_TRAVELLER_ID)));
		log.setProfileLastUpdated(cursor.getLong(cursor
				.getColumnIndex(DBConstant.TABLE_TRAVELLER_UPDATED_LOG_FIELD_PROFILE_UPDATED)));
		log.setGalleryLastUpdated(cursor.getLong(cursor
				.getColumnIndex(DBConstant.TABLE_TRAVELLER_UPDATED_LOG_FIELD_GALLERY_UPDATED)));
		log.setToursLastUpdated(cursor.getLong(cursor
				.getColumnIndex(DBConstant.TABLE_TRAVELLER_UPDATED_LOG_FIELD_TOURS_UPDATED)));
		log.setBookingsLastUpdated(cursor.getLong(cursor
				.getColumnIndex(DBConstant.TABLE_TRAVELLER_UPDATED_LOG_FIELD_BOOKINGS_UPDATED)));
		log.setMatchesLastUpdated(cursor.getLong(cursor
				.getColumnIndex(DBConstant.TABLE_TRAVELLER_UPDATED_LOG_FIELD_MATCHES_UPDATED)));
		
		return log;
	}

	@Override
	public void deleteMany(String condition) {
		delete(condition, DBConstant.TABLE_TRAVELLER_UPDATED_LOG);
	}

	public void updateUpdatedLog(String column, long lastupdatedTime,
			long travellerId) {
		String query = "update " + DBConstant.TABLE_TRAVELLER_UPDATED_LOG
				+ " set " + column + "=" + lastupdatedTime + " where "
				+ DBConstant.TABLE_TRAVELLER_UPDATED_LOG_FIELD_TRAVELLER_ID
				+ "=" + travellerId + " AND " + column + " < " + lastupdatedTime;
		
		db.execSQL(query);
	}

}
