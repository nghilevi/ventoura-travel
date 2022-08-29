package com.Mindelo.VentouraServer.DAO;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Mindelo.VentouraServer.Entity.TravellerGallery;
import com.Mindelo.VentouraServer.IDAO.ITravellerGalleryDAO;


@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class TravellerGalleryDAO extends DAO<TravellerGallery, Long> implements
		ITravellerGalleryDAO {

	@Override
	public List<TravellerGallery> getTravellerGalleryByTravellerId(
			long travellerId) {

		String sql = "select tg FROM TravellerGallery tg WHERE travellerId = :travellerId";
		TypedQuery<TravellerGallery> query = entityManager
				.createQuery(sql, TravellerGallery.class)
				.setParameter("travellerId", travellerId);

		List<TravellerGallery> galleries = query.getResultList();

		return galleries;
	}

	@Override
	@Transactional
	public void setTravellerPortalByTravellerId(long travellerId, long galleryId) {

		String sql = "select tg FROM TravellerGallery tg WHERE tg.travellerId = :travellerId and tg.portalPhoto=1";
		TypedQuery<TravellerGallery> query = entityManager
				.createQuery(sql, TravellerGallery.class)
				.setParameter("travellerId", travellerId);

		try {
			TravellerGallery gallery = query.getSingleResult();

			sql = "update TravellerGallery tg set tg.portalPhoto=1 WHERE tg.id = :galleryId";
			Query update_query_1 = entityManager.createQuery(sql)
					.setParameter("galleryId", galleryId);
			update_query_1.executeUpdate();

			sql = "update TravellerGallery tg set tg.portalPhoto=0 WHERE tg.id = :galleryId";
			Query update_query_2 = entityManager.createQuery(sql)
					.setParameter("galleryId", gallery.getId());
			update_query_2.executeUpdate();

		} catch (NoResultException e) {
			sql = "update TravellerGallery tg set tg.portalPhoto=1 WHERE tg.id = :galleryId";
			Query update_query_1 = entityManager.createQuery(sql)
					.setParameter("galleryId", galleryId);
			update_query_1.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	@Transactional
	public void deleteTravellerGalleryByTravellerId(long travellerId) {

		String sql = "delete FROM TravellerGallery tg WHERE tg.travellerId = :travellerId";
		Query query = entityManager.createQuery(sql)
				.setParameter("travellerId", travellerId);

		query.executeUpdate();

	}

	@Override
	public TravellerGallery getTravellerPortalGallery(long travellerId) {

		String sql = "select tg FROM TravellerGallery tg WHERE tg.travellerId = :travellerId and tg.portalPhoto=true";
		TypedQuery<TravellerGallery> query = entityManager
				.createQuery(sql, TravellerGallery.class)
				.setParameter("travellerId", travellerId);

		TravellerGallery gallery = null;
		try {
			gallery = query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		return gallery;

	}

}
