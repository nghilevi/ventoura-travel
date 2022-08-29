package com.Mindelo.VentouraServer.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.Mindelo.VentouraServer.Entity.TGMatch;
import com.Mindelo.VentouraServer.Entity.TTMatch;
import com.Mindelo.VentouraServer.IDAO.ITGMatchDAO;
import com.Mindelo.VentouraServer.IDAO.ITTMatchDAO;
import com.Mindelo.VentouraServer.IService.IMatchesService;

@Service
@Component
public class MatchesService implements IMatchesService{
	
	@Autowired
	private ITGMatchDAO tgMatchDao;

	@Autowired
	private ITTMatchDAO ttMatchDao;
	
	@Override
	public List<TGMatch> getTravellerTGMatch(long travellerId) {
		return tgMatchDao.getTravellerMatches(travellerId);
	}
	
	@Override
	public List<TTMatch> getTravellerTTMatch(long travellerId) {
		return ttMatchDao.getTravellerMatches(travellerId);
	}

	@Override
	public List<TGMatch> getGuideTGMatch(long guideId) {
		return tgMatchDao.getGuideMatches(guideId);
	}

}
