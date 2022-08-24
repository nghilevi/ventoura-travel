package com.Mindelo.Ventoura.Util;

import android.app.Activity;

import com.facebook.Session;

public class FacebookUtil {
	public static void logoutFacebook(Activity activity){
		Session session = Session.getActiveSession();
	    if (session != null) {

	        if (!session.isClosed()) {
	            session.closeAndClearTokenInformation();
	            //clear your preferences if saved
	        }
	    } else {

	        session = new Session(activity);
	        Session.setActiveSession(session);

	        session.closeAndClearTokenInformation();
	            //clear your preferences if saved

	    }
		
	}
}
