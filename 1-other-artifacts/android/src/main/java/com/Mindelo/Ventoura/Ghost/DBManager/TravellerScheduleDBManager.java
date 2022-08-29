package com.Mindelo.Ventoura.Ghost.DBManager;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.Mindelo.Ventoura.Constant.DBConstant;
import com.Mindelo.Ventoura.Ghost.DBHelper.DAO;
import com.Mindelo.Ventoura.JSONEntity.JSONTravellerSchedule;

public class TravellerScheduleDBManager extends DAO<JSONTravellerSchedule> {

	private SQLiteDatabase db;

	public TravellerScheduleDBManager(SQLiteDatabase db) {
		super(db);
		this.db = db;
	}

	@Override
	public void save(JSONTravellerSchedule schedule) {
		db.execSQL("INSERT INTO " + DBConstant.TABLE_TRAVELLER_SCHEDULE
				+ " VALUES(?, ?, ?, ?, ?, ?)",
				new Object[] { 
							schedule.getId(), 
							schedule.getTravellerId(),
							schedule.getStartTime(), 
							schedule.getEndTime(),
							schedule.getCity(), 
							schedule.getCountry()
					});
	}

	@Override
	public List<JSONTravellerSchedule> findAll() {
		Cursor c = getQueryCursor(null, DBConstant.TABLE_TRAVELLER_SCHEDULE);
		List<JSONTravellerSchedule> schedules = loadListEntityFromCursor(c);
		c.close();
		return schedules;
	}

	@Override
	public JSONTravellerSchedule findOne(String condition) {
		Cursor c = getQueryCursor(condition, DBConstant.TABLE_TRAVELLER_SCHEDULE);
		if (c.getCount() == 0)
			return null;
		c.moveToFirst();
		JSONTravellerSchedule  schedule = loadSingleEntityFromCursor(c);
		c.close();
		return schedule;
	}

	@Override
	public List<JSONTravellerSchedule> findMany(String condition) {
		Cursor c = getQueryCursor(condition, DBConstant.TABLE_TRAVELLER_SCHEDULE);
		List<JSONTravellerSchedule> schedules = loadListEntityFromCursor(c);
		c.close();
		return schedules;
	}

	@Override
	public JSONTravellerSchedule loadSingleEntityFromCursor(Cursor cursor) {    
		JSONTravellerSchedule schedule = new JSONTravellerSchedule();
		
		schedule.setId(cursor.getLong(cursor
				.getColumnIndex(DBConstant.TABLE_TRAVELLER_SCHEDULE_FIELD_ID)));
		schedule.setTravellerId(cursor.getLong(cursor
				.getColumnIndex(DBConstant.TABLE_TRAVELLER_SCHEDULE_FIELD_TRAVELLER_ID)));
		schedule.setCity(cursor.getInt(cursor
				.getColumnIndex(DBConstant.TABLE_TRAVELLER_SCHEDULE_FIELD_CITY)));
		schedule.setCountry(cursor.getInt(cursor
				.getColumnIndex(DBConstant.TABLE_TRAVELLER_SCHEDULE_FIELD_COUNTRY)));
		schedule.setEndTime(cursor.getString(cursor
				.getColumnIndex(DBConstant.TABLE_TRAVELLER_SCHEDULE_FIELD_END_TIME)));
		schedule.setStartTime(cursor.getString(cursor
				.getColumnIndex(DBConstant.TABLE_TRAVELLER_SCHEDULE_FIELD_START_TIME)));
	
		return schedule;
	}

	@Override
	public void deleteMany(String condition) {
		delete(condition, DBConstant.TABLE_TRAVELLER_SCHEDULE);
	}

}
