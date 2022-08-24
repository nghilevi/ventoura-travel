package com.Mindelo.Ventoura.Ghost.DBManager;

import java.util.ArrayList;
import java.util.List;

import com.Mindelo.Ventoura.Constant.DBConstant;
import com.Mindelo.Ventoura.Entity.Country;
import com.Mindelo.Ventoura.Ghost.DBHelper.DAO;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CountryDBManager extends DAO<Country> {


	public CountryDBManager(SQLiteDatabase db) {

		super(db);
	}


	public void save(Country entity) {
		// TODO Auto-generated method stub

	}

	
	public List<Country> findAll() {
		Cursor c = getQueryCursor(null, DBConstant.TABLE_COUNTRY);
		List<Country> countries = loadListEntityFromCursor(c);
		c.close();
		return countries;
	}

	
	public Country findOne(String condition) {
		Cursor c = getQueryCursor(condition, DBConstant.TABLE_COUNTRY);
		if (c.getCount() == 0)
			return null;
		c.moveToFirst();
		Country country = loadSingleEntityFromCursor(c);
		c.close();
		return country;
	}

	
	public List<Country> findMany(String condition) {
		Cursor c = getQueryCursor(condition, DBConstant.TABLE_COUNTRY);
		List<Country> buildings = loadListEntityFromCursor(c);
		c.close();
		return buildings;
	}

	
	public Country loadSingleEntityFromCursor(Cursor cursor) {

		Country country = new Country();

		country.setId(cursor.getInt(cursor
				.getColumnIndex(DBConstant.TABLE_COUNTRY_FIELD_ID)));

		country.setCountryName(cursor.getString(cursor
				.getColumnIndex(DBConstant.TABLE_COUNTRY_FIELD_COUNTRY_NAME)));

		return country;
	
	}

	
	public List<Country> loadListEntityFromCursor(Cursor cursor) {
		ArrayList<Country> country = new ArrayList<Country>();
		while (cursor.moveToNext()) {
			country.add(loadSingleEntityFromCursor(cursor));
		}
		return country;
	}


	@Override
	public void deleteMany(String condition) {
		// TODO Auto-generated method stub
		
	}
}
