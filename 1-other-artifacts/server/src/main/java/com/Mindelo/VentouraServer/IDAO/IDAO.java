package com.Mindelo.VentouraServer.IDAO;

import java.io.Serializable;
import java.util.List;

public interface IDAO<T, ID extends Serializable> {
	
	public void save(T entity);

	public void update(T entity);

	public void delete(T entity);
	
	public void deleteById(Class<T> clazz, Long id);

	public List<T> findAll(Class<T> clazz);

	public T findByID(Class<T> clazz, Long id);
	
	public List<T> findByAllID(Class<T> clazz, List<Long> id);

	public void saveAll(List<T> entities);
	
	public void updateAll(List<T> entities);
}
