package com.Mindelo.VentouraServer.DAO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Mindelo.VentouraServer.IDAO.IDAO;


@Transactional(propagation = Propagation.REQUIRED)
public abstract class DAO<T, ID extends Serializable> implements IDAO<T, ID> {

	@PersistenceContext(unitName = "entityManager")
	protected EntityManager entityManager;

	@Override
	@Transactional
	public void save(T entity) {
		entityManager.persist(entity);
	}

	@Override
	@Transactional
	public void update(T entity) {

		entityManager.merge(entity);

	}

	@Override
	@Transactional
	public void delete(T entity) {

		entityManager.remove(entity);

	}

	@Override
	@Transactional
	public void deleteById(Class<T> clazz, Long id) {

		Query query = entityManager.createQuery("delete FROM "
				+ clazz.getSimpleName() + " where id=" + id);
		query.executeUpdate();

	}

	@Override
	public T findByID(Class<T> clazz, Long id) {

		T t = null;
		t = (T) entityManager.find(clazz, id);

		return t;
	}

	@Override
	public List<T> findAll(Class<T> clazz) {

		TypedQuery<T> query = entityManager.createQuery("SELECT e FROM "
				+ clazz.getSimpleName() + " e ", clazz);
		List<T> results = query.getResultList();

		return results;
	}

	@Override
	@Transactional
	public void saveAll(List<T> entities) {

		for (T entity : entities) {
			entityManager.merge(entity);
		}

	}

	@Override
	@Transactional
	public void updateAll(List<T> entities) {

		for (T entity : entities) {
			entityManager.merge(entity);
		}

	}

	@Override
	public List<T> findByAllID(Class<T> clazz, List<Long> ids) {
		List<T> tList = new ArrayList<T>();

		for (Long id : ids) {
			T t = null;
			t = (T) entityManager.find(clazz, id);
			tList.add(t);
		}

		return tList;

	}

}
