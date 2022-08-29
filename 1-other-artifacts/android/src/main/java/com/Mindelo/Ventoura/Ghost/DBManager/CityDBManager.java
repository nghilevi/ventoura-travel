package com.Mindelo.Ventoura.Ghost.DBManager;

import java.util.List;

import com.Mindelo.Ventoura.Constant.DBConstant;
import com.Mindelo.Ventoura.Entity.City;
import com.Mindelo.Ventoura.Ghost.DBHelper.DAO;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CityDBManager extends DAO<City> {


	public CityDBManager(SQLiteDatabase db) {

		super(db);
	}


	public void save(City entity) {
		// TODO Auto-generated method stub

	}

	
	public List<City> findAll() {
		Cursor c = getQueryCursor(null, DBConstant.TABLE_CITY);
		List<City> cities = loadListEntityFromCursor(c);
		c.close();
		return cities;
	}

	
	public City findOne(String condition) {
		Cursor c = getQueryCursor(condition, DBConstant.TABLE_CITY);
		if (c.getCount() == 0)
			return null;
		c.moveToFirst();
		City city = loadSingleEntityFromCursor(c);
		c.close();
		return city;
	}

	
	public List<City> findMany(String condition) {
		Cursor c = getQueryCursor(condition, DBConstant.TABLE_CITY);
		List<City> cities = loadListEntityFromCursor(c);
		c.close();
		return cities;
	}

	
	public City loadSingleEntityFromCursor(Cursor cursor) {

		City city = new City();

		city.setId(cursor.getInt(cursor
				.getColumnIndex(DBConstant.TABLE_CITY_FIELD_ID)));

		city.setCityName(cursor.getString(cursor
				.getColumnIndex(DBConstant.TABLE_CITY_FIELD_CITY_NAME)));

		city.setCountryId(cursor.getInt(cursor
				.getColumnIndex(DBConstant.TABLE_CITY_FIELD_COUNTRY_ID)));

		city.setCountryName(cursor.getString(cursor
				.getColumnIndex(DBConstant.TABLE_CITY_FIELD_COUNTRY_NAME)));

		return city;
	
	}


	@Override
	public void deleteMany(String condition) {
		// TODO Auto-generated method stub
		
	}
}
