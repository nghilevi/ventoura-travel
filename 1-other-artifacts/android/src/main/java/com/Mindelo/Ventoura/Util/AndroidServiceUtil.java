package com.Mindelo.Ventoura.Util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class AndroidServiceUtil {
	
	/**
	 * check whether an Android service is running 
	 * @param serviceClass the class object of the android service
	 */
	public static boolean isMyServiceRunning(Class<?> serviceClass, Activity activity) {
		ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
}
