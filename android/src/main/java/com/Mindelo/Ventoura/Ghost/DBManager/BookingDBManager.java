package com.Mindelo.Ventoura.Ghost.DBManager;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.Mindelo.Ventoura.Constant.DBConstant;
import com.Mindelo.Ventoura.Ghost.DBHelper.DAO;
import com.Mindelo.Ventoura.JSONEntity.JSONBooking;

public class BookingDBManager extends DAO<JSONBooking> {

	private SQLiteDatabase db;

	public BookingDBManager(SQLiteDatabase db) {
		super(db);
		this.db = db;
	}

	@Override
	public void save(JSONBooking booking) {
		db.execSQL(
				"INSERT INTO " + DBConstant.TABLE_BOOKINGS
						+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				new Object[] { booking.getId(), booking.getTravellerId(),
						booking.getTravellerFirstname(), booking.getGuideAge(),
						booking.getGuideGender(), booking.getGuideId(),
						booking.getGuideFirstname(), booking.getGuideAge(),
						booking.getGuideGender(),
						booking.getStatusLastUpdatedTime(),
						booking.getBookingStatus(), booking.getTourPrice(),
						booking.getTourDate() });
	}

	@Override
	public List<JSONBooking> findAll() {
		Cursor c = getQueryCursor(null, DBConstant.TABLE_BOOKINGS);
		List<JSONBooking> bookings = loadListEntityFromCursor(c);
		c.close();
		return bookings;
	}

	@Override
	public JSONBooking findOne(String condition) {
		Cursor c = getQueryCursor(condition, DBConstant.TABLE_BOOKINGS);
		if (c.getCount() == 0)
			return null;
		c.moveToFirst();
		JSONBooking schedule = loadSingleEntityFromCursor(c);
		c.close();
		return schedule;
	}

	@Override
	public List<JSONBooking> findMany(String condition) {
		Cursor c = getQueryCursor(condition, DBConstant.TABLE_BOOKINGS);
		List<JSONBooking> bookings = loadListEntityFromCursor(c);
		c.close();
		return bookings;
	}

	@Override
	public JSONBooking loadSingleEntityFromCursor(Cursor cursor) {
		JSONBooking jsonBooking = new JSONBooking();

		jsonBooking.setId(cursor.getLong(cursor
				.getColumnIndex(DBConstant.TABLE_BOOKINGS_FIELD_ID)));
		jsonBooking.setTravellerId(cursor.getLong(cursor
				.getColumnIndex(DBConstant.TABLE_BOOKINGS_FIELD_TRAVELLER_ID)));
		jsonBooking
				.setTravellerFirstname(cursor.getString(cursor
						.getColumnIndex(DBConstant.TABLE_BOOKINGS_FIELD_TRAVELLER_FIRSTNAME)));
		jsonBooking
				.setTravellerGender(cursor.getInt(cursor
						.getColumnIndex(DBConstant.TABLE_BOOKINGS_FIELD_TRAVELLER_GENDER)));
		jsonBooking
				.setTravellerAge(cursor.getInt(cursor
						.getColumnIndex(DBConstant.TABLE_BOOKINGS_FIELD_TRAVELLER_AGE)));

		jsonBooking.setGuideId(cursor.getLong(cursor
				.getColumnIndex(DBConstant.TABLE_BOOKINGS_FIELD_GUIDE_ID)));
		jsonBooking
				.setGuideFirstname(cursor.getString(cursor
						.getColumnIndex(DBConstant.TABLE_BOOKINGS_FIELD_GUIDE_FIRSTNAME)));
		jsonBooking.setGuideAge(cursor.getInt(cursor
				.getColumnIndex(DBConstant.TABLE_BOOKINGS_FIELD_GUIDE_AGE)));
		jsonBooking.setGuideGender(cursor.getInt(cursor
				.getColumnIndex(DBConstant.TABLE_BOOKINGS_FIELD_GUIDE_GENDER)));

		jsonBooking
				.setBookingStatus(cursor.getInt(cursor
						.getColumnIndex(DBConstant.TABLE_BOOKINGS_FIELD_BOOKING_STATUS)));
		jsonBooking.setTourPrice(cursor.getFloat(cursor
				.getColumnIndex(DBConstant.TABLE_BOOKINGS_FIELD_TOUR_PRICE)));
		jsonBooking.setTourDate(cursor.getString(cursor
				.getColumnIndex(DBConstant.TABLE_BOOKINGS_FIELD_TOUR_DATE)));
		jsonBooking
				.setStatusLastUpdatedTime(cursor.getString(cursor
						.getColumnIndex(DBConstant.TABLE_BOOKINGS_FIELD_STATUS_LAST_UPDATED_DATE)));

		return jsonBooking;
	}

	@Override
	public void deleteMany(String condition) {
		delete(condition, DBConstant.TABLE_BOOKINGS);
	}

}
