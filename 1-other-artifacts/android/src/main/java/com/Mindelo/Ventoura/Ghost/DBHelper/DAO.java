package com.Mindelo.Ventoura.Ghost.DBHelper;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class DAO<T> implements IDAO<T> {

	private SQLiteDatabase db;

	public DAO(SQLiteDatabase db) {
		this.db = db;
	}
	
	
	@Override
	public void executeRawQuery(String query){
		db.execSQL(query);
	}

	@Override
	public void saveAll(List<T> entityList) {
		for (T entity : entityList) {
			save(entity);
		}
	}

	@Override
	public List<T> loadListEntityFromCursor(Cursor cursor) {
		ArrayList<T> entities = new ArrayList<T>();
		while (cursor.moveToNext()) {
			entities.add(loadSingleEntityFromCursor(cursor));
		}
		return entities;
	}
	
	public void close(){
		db.close();
	}

	/**
	 * get the select query cursor
	 * 
	 * @param condition
	 *            the condition in the SQL where clause
	 * @param tableName
	 *            the table which will be queried
	 */
	protected Cursor getQueryCursor(String condition, String tableName) {
		Cursor c = null;
		if (condition != null) {
			c = db.rawQuery("SELECT * FROM " + tableName + condition + ";",
					null);
		} else {
			c = db.rawQuery("SELECT * FROM " + tableName + ";", null);
		}
		return c;
	}

	/**
	 * delete selected entities
	 * 
	 * @param condition
	 *            the condition in the SQL where clause
	 * @param tableName
	 *            the table which will be queried
	 */
	protected void delete(String condition, String tableName) {
		if (condition != null) {
			db.execSQL(" delete from " + tableName + " " + condition + ";");
		} else {
			db.execSQL(" delete from " + tableName + ";");
		}

	}
	
}
