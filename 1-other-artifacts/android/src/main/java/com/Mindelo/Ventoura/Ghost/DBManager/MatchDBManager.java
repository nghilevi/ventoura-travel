package com.Mindelo.Ventoura.Ghost.DBManager;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.Mindelo.Ventoura.Constant.DBConstant;
import com.Mindelo.Ventoura.Enum.Gender;
import com.Mindelo.Ventoura.Enum.UserRole;
import com.Mindelo.Ventoura.Ghost.DBHelper.DAO;
import com.Mindelo.Ventoura.JSONEntity.JSONMatch;

public class MatchDBManager extends DAO<JSONMatch> {

	private SQLiteDatabase db;

	public MatchDBManager(SQLiteDatabase db) {
		super(db);
		this.db = db;
	}
	
	
	/*
	 * customized save functions
	 */
	public void save(JSONMatch match, long ownerId, int userRole){
		db.execSQL(
				"INSERT INTO " + DBConstant.TABLE_MATCH
						+ " VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				new Object[] { ownerId,userRole,
						match.getUserId(),
						match.getUserRole().getNumVal(),
						match.getUserFirstname(),
						match.getTimeMatched(),
						match.getCity(),
						match.getCountry(),
						match.getGender(),
						match.getAge() });
		
	}
	
	public void saveAll(List<JSONMatch> matches, long ownerId, int userRole){
		for(JSONMatch match : matches){
			save(match, ownerId, userRole);
		}
	}

	@Override
	public void save(JSONMatch match) {
		// Nothing to do 
	}

	@Override
	public List<JSONMatch> findAll() {
		Cursor c = getQueryCursor(null, DBConstant.TABLE_MATCH);
		List<JSONMatch> matches = loadListEntityFromCursor(c);
		c.close();
		return matches;
	}

	@Override
	public JSONMatch findOne(String condition) {
		Cursor c = getQueryCursor(condition, DBConstant.TABLE_MATCH);
		if (c.getCount() == 0)
			return null;
		c.moveToFirst();
		JSONMatch  match = loadSingleEntityFromCursor(c);
		c.close();
		return match;
	}

	@Override
	public List<JSONMatch> findMany(String condition) {
		Cursor c = getQueryCursor(condition, DBConstant.TABLE_MATCH);
		List<JSONMatch> matches = loadListEntityFromCursor(c);
		c.close();
		return matches;
	}

	@Override
	public JSONMatch loadSingleEntityFromCursor(Cursor cursor) {
		JSONMatch match = new JSONMatch();

		match.setUserId(cursor.getLong(cursor
				.getColumnIndex(DBConstant.TABLE_MATCH_FIELD_PARTNER_ID)));
		
		match.setCity(cursor.getInt(cursor
				.getColumnIndex(DBConstant.TABLE_MATCH_FIELD_CITY))); // TODO fix the city
		match.setCountry(cursor.getInt(cursor
				.getColumnIndex(DBConstant.TABLE_MATCH_FIELD_COUNTRY)));
		
		match.setUserFirstname(cursor.getString(cursor
				.getColumnIndex(DBConstant.TABLE_MATCH_FIELD_PARTNER_FIRSTNAME)));

		if (cursor.getInt(cursor
				.getColumnIndex(DBConstant.TABLE_MATCH_FIELD_PARTNER_ROLE)) == UserRole.GUIDE
				.getNumVal()) {
			match.setUserRole(UserRole.GUIDE);
		} else {
			match.setUserRole(UserRole.TRAVELLER);
		}

		match.setTimeMatched(cursor.getString(cursor
				.getColumnIndex(DBConstant.TABLE_MATCH_FIELD_TIME_MATCHED)));
		
		if(cursor.getInt(cursor
				.getColumnIndex(DBConstant.TABLE_MATCH_FIELD_GENDER)) == 0){
			match.setGender(Gender.MALE);
		}else{
			match.setGender(Gender.FEMALE);
		}

		match.setAge(cursor.getInt(cursor
				.getColumnIndex(DBConstant.TABLE_MATCH_FIELD_AGE)));
		
		return match;
	}
	
	@Override
	public void deleteMany(String condition) {
		delete(condition, DBConstant.TABLE_MATCH);
	}

}
