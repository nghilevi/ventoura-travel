package com.Mindelo.Ventoura.Ghost.DBManager;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.Mindelo.Ventoura.Ghost.DBHelper.DAO;
import com.Mindelo.Ventoura.Constant.DBConstant;
import com.Mindelo.Ventoura.Entity.GuideUpdatedLog;

public class GuideUpdatedLogDBManager extends DAO<GuideUpdatedLog> {

	private SQLiteDatabase db;

	public GuideUpdatedLogDBManager(SQLiteDatabase db) {
		super(db);
		this.db = db;
	}

	@Override
	public void save(GuideUpdatedLog log) {
		// See also in the guidepfile save. 
		// you don't need to call this function
	}

	@Override
	public List<GuideUpdatedLog> findAll() {
		Cursor c = getQueryCursor(null, DBConstant.TABLE_GUIDE_UPDATED_LOG);
		List<GuideUpdatedLog> logs = loadListEntityFromCursor(c);
		c.close();
		return logs;
	}

	@Override
	public GuideUpdatedLog findOne(String condition) {
		Cursor c = getQueryCursor(condition, DBConstant.TABLE_GUIDE_UPDATED_LOG);
		if (c.getCount() == 0)
			return null;
		c.moveToFirst();
		GuideUpdatedLog log = loadSingleEntityFromCursor(c);
		c.close();
		return log;
	}

	@Override
	public List<GuideUpdatedLog> findMany(String condition) {
		Cursor c = getQueryCursor(condition, DBConstant.TABLE_GUIDE_UPDATED_LOG);
		List<GuideUpdatedLog> logs = loadListEntityFromCursor(c);
		c.close();
		return logs;
	}

	@Override
	public GuideUpdatedLog loadSingleEntityFromCursor(Cursor cursor) {
		GuideUpdatedLog log = new GuideUpdatedLog();

		log.setGuideId(cursor.getLong(cursor
				.getColumnIndex(DBConstant.TABLE_GUIDE_UPDATED_LOG_FIELD_GUIDE_ID)));
		log.setProfileLastUpdated(cursor.getLong(cursor
				.getColumnIndex(DBConstant.TABLE_GUIDE_UPDATED_LOG_FIELD_PROFILE_UPDATED)));
		log.setGalleryLastUpdated(cursor.getLong(cursor
				.getColumnIndex(DBConstant.TABLE_GUIDE_UPDATED_LOG_FIELD_GALLERY_UPDATED)));
		log.setReviewLastUpdated(cursor.getLong(cursor
				.getColumnIndex(DBConstant.TABLE_GUIDE_UPDATED_LOG_FIELD_REVIEWS_UPDATED)));
		log.setBookingsLastUpdated(cursor.getLong(cursor
				.getColumnIndex(DBConstant.TABLE_GUIDE_UPDATED_LOG_FIELD_BOOKINGS_UPDATED)));
		log.setMatchesLastUpdated(cursor.getLong(cursor
				.getColumnIndex(DBConstant.TABLE_GUIDE_UPDATED_LOG_FIELD_MATCHES_UPDATED)));

		return log;
	}

	@Override
	public void deleteMany(String condition) {
		delete(condition, DBConstant.TABLE_GUIDE_UPDATED_LOG);
	}

	public void updateUpdatedLog(String column, long lastupdatedTime,
			long guideId) {
		String query = "update " + DBConstant.TABLE_GUIDE_UPDATED_LOG + " set "
				+ column + "=" + lastupdatedTime + " where "
				+ DBConstant.TABLE_GUIDE_UPDATED_LOG_FIELD_GUIDE_ID + "="
				+ guideId + " AND " + column + " < " + lastupdatedTime;
		executeRawQuery(query);
	}

}
