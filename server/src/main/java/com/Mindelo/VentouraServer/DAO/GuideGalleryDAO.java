package com.Mindelo.VentouraServer.DAO;

import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Mindelo.VentouraServer.Entity.GuideGallery;
import com.Mindelo.VentouraServer.IDAO.IGuideGalleryDAO;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class GuideGalleryDAO extends DAO<GuideGallery, Long> implements
		IGuideGalleryDAO {

	@Override
	@Transactional
	public void deleteGuideGalleryByGuideId(long guideId) {

		String sql = "delete FROM GuideGallery gg WHERE gg.guideId = :guideId";
		Query query = entityManager.createQuery(sql)
				.setParameter("guideId", guideId);

		query.executeUpdate();

	}

	@Override
	@Transactional
	public void setGuidePortalByGuideId(long guideId, long galleryId) {

		String sql = "select gg FROM GuideGallery gg WHERE gg.guideId = :guideId and gg.portalPhoto=1";
		TypedQuery<GuideGallery> query = entityManager
				.createQuery(sql, GuideGallery.class)
				.setParameter("guideId", guideId);

		try {
			GuideGallery gallery = query.getSingleResult();
			
			sql = "update GuideGallery gg set gg.portalPhoto=1 WHERE gg.id = :galleryId";
			Query update_query_1 = entityManager.createQuery(sql)
					.setParameter("galleryId", galleryId);
			update_query_1.executeUpdate();

			sql = "update GuideGallery gg set portalPhoto=0 WHERE gg.id = :galleryId";
			Query update_query_2 = entityManager.createQuery(sql)
					.setParameter("galleryId", gallery.getId());
			update_query_2.executeUpdate();
			
		} catch (NoResultException  e) {
			
			// if there is no portal image.
			sql = "update GuideGallery gg set gg.portalPhoto=1 WHERE gg.id = :galleryId";
			Query update_query_1 = entityManager.createQuery(sql)
					.setParameter("galleryId", galleryId);
			update_query_1.executeUpdate();
		} catch(Exception e){
			e.printStackTrace();
		}

	}

	@Override
	public List<GuideGallery> getGuideGalleryByGuideId(long guideId) {

		String sql = "select gg FROM GuideGallery gg WHERE gg.guideId = :guideId";
		TypedQuery<GuideGallery> query = entityManager
				.createQuery(sql, GuideGallery.class)
				.setParameter("guideId", guideId);

		List<GuideGallery> galleries = query.getResultList();

		return galleries;
	}

	@Override
	public GuideGallery getGuidePortalGallery(long guideId) {

		String sql = "select gg FROM GuideGallery gg WHERE gg.guideId = :guideId and gg.portalPhoto=1";
		TypedQuery<GuideGallery> query = entityManager
				.createQuery(sql, GuideGallery.class)
				.setParameter("guideId", guideId);

		GuideGallery gallery = null;
		try {
			gallery = query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

		return gallery;
	}

}
