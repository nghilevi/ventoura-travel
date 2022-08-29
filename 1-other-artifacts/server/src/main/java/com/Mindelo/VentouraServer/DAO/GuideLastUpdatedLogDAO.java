package com.Mindelo.VentouraServer.DAO;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.Mindelo.VentouraServer.Entity.GuideLastUpdatedLog;
import com.Mindelo.VentouraServer.IDAO.IGuideLastUpdatedLogDAO;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public class GuideLastUpdatedLogDAO extends DAO<GuideLastUpdatedLog, Long>
		implements IGuideLastUpdatedLogDAO {

	@Override
	public GuideLastUpdatedLog getGuideLastUpdatedByGuideId(long guideId) {

		GuideLastUpdatedLog gul = null;
		String sql = "SELECT gl FROM GuideLastUpdatedLog gl WHERE gl.guideId = :guideId";
		TypedQuery<GuideLastUpdatedLog> query = entityManager.createQuery(sql,
				GuideLastUpdatedLog.class).setParameter("guideId", guideId);

		try {
			gul = query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return gul;
	}

	@Override
	@Transactional
	public void setGuideProfileLastUpdated(long lastUpdated, long guideId) {

		String sql = "update GuideLastUpdatedLog gul set gul.profileLastUpdated=:lastUpdated WHERE gul.guideId=:guideId";
		Query query = entityManager.createQuery(sql)
				.setParameter("guideId", guideId)
				.setParameter("lastUpdated", lastUpdated);
		query.executeUpdate();

	}

	@Override
	@Transactional
	public void setGuideBookingsLastUpdated(long lastUpdated, long guideId) {

		String sql = "update GuideLastUpdatedLog gul set gul.bookingsLastUpdated=:lastUpdated WHERE gul.guideId=:guideId";
		Query query = entityManager.createQuery(sql)
				.setParameter("guideId", guideId)
				.setParameter("lastUpdated", lastUpdated);
		query.executeUpdate();

	}

	@Override
	@Transactional
	public void setGuideMatchesLastUpdated(long lastUpdated, long guideId) {

		String sql = "update GuideLastUpdatedLog gul set gul.matchesLastUpdated=:lastUpdated WHERE gul.guideId=:guideId";
		Query query = entityManager.createQuery(sql)
				.setParameter("guideId", guideId)
				.setParameter("lastUpdated", lastUpdated);
		query.executeUpdate();

	}

	@Override
	@Transactional
	public void setGuideGalleryLastUpdated(long lastUpdated, long guideId) {

		String sql = "update GuideLastUpdatedLog gul set gul.galleryLastUpdated=:lastUpdated WHERE gul.guideId=:guideId";
		Query query = entityManager.createQuery(sql)
				.setParameter("guideId", guideId)
				.setParameter("lastUpdated", lastUpdated);
		query.executeUpdate();

	}

	@Override
	@Transactional
	public void setGuideReviewLastUpdated(long lastUpdated, long guideId) {

		String sql = "update GuideLastUpdatedLog gul set gul.reviewLastUpdated=:lastUpdated WHERE gul.guideId=:guideId";
		Query query = entityManager.createQuery(sql)
				.setParameter("guideId", guideId)
				.setParameter("lastUpdated", lastUpdated);
		query.executeUpdate();

	}

}
