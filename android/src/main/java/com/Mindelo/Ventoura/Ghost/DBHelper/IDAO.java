package com.Mindelo.Ventoura.Ghost.DBHelper;

import java.util.List;

import android.database.Cursor;

public interface IDAO<T> {
	
	/**
	 * execute a single complete sql query without any return data
	 */
	public void executeRawQuery(String query);

	/**
	 * save an entity to the database
	 * 
	 * @param <T>
	 */
	public void save(T entity);

	/**
	 * save a list of entities to the database
	 */
	public void saveAll(List<T> entityList);

	/**
	 * get all the entities
	 */
	public List<T> findAll();

	/**
	 * get on specific entity according to a condition
	 */
	public T findOne(String condition);

	/**
	 * get a list of entities according to a condition
	 */
	public List<T> findMany(String condition);

	/**
	 * load an entity from cursor
	 */
	public T loadSingleEntityFromCursor(Cursor cursor);

	/**
	 * load a list of entity from cursor
	 */
	public List<T> loadListEntityFromCursor(Cursor cursor);
	
	/**
	 * delete all the entities from the table 
	 */
	public void deleteMany(String condition);
	
	/**
	 * close the connection of db in case of db leak
	 */
	public void close();

}
