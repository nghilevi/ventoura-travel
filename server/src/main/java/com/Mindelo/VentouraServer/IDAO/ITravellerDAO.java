package com.Mindelo.VentouraServer.IDAO;

import com.Mindelo.VentouraServer.Entity.Traveller;



public interface ITravellerDAO extends IDAO<Traveller, Long>{
	
	public Traveller getTravellerByFacebookAccountName(String facebookAccountName);

}
