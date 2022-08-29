package com.Mindelo.VentouraServer.IDAO;

import com.Mindelo.VentouraServer.Entity.Guide;



public interface IGuideDAO extends IDAO<Guide, Long> {
	public Guide getGuideByFacebookAccountName(String facebookAccountName);
}
