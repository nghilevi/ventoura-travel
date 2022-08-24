package com.Mindelo.Ventoura.Ghost.IService;

public interface ISystemService {
	
	/**
	 * get the userRole according to the account
	 * @return
	 * 		if -1 means no result has been found in the server
	 */
	long traverllerLoginProbe(String facebookAccount);
	
	long guideLoginProbe(String facebookAccount);

	
}
