package com.Mindelo.Ventoura.UI.Activity;

import com.Mindelo.Ventoura.Constant.VentouraConstant;
import com.Mindelo.Ventoura.Enum.UserRole;
import com.Mindelo.Ventoura.Util.ConversationEmoticonUtil;
import com.Mindelo.Ventoura.Util.DeviceStatusChecker;
import com.Mindelo.Ventoura.Util.DeviceStatusChecker.Resource;
import com.facebook.Session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;

public class LoadingActivity extends FragmentActivity {

	private boolean is_touched = false;
	private int delayedTime = 1000;

	private SharedPreferences sharedPre;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_loading);

		Handler handler = new Handler();
		handler.postDelayed(new splashHandler(), delayedTime);

		sharedPre = getSharedPreferences(
				VentouraConstant.SHARED_PREFERENCE_VENTOURA,
				Context.MODE_PRIVATE);
	}

	class splashHandler implements Runnable {

		public void run() {
			if (false == is_touched) {
				startFirstActivity();
			}
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			is_touched = true;
			startFirstActivity();
		}
		return super.onTouchEvent(event);
	}

	private void startFirstActivity() {
		DeviceStatusChecker checker = new DeviceStatusChecker(this);
		/*
		 * check network status
		 */
		if (checker.isNetworkActivated(this)) {

			//init the chat face 
			new Thread(new Runnable() {
	            @Override
	            public void run() {
	            	ConversationEmoticonUtil.getInstance().getFileText(getApplication());
	            }
	        }).start();
			
			finish();
			// Decide whether the user has already been logged in
			Session facebookSession = Session.getActiveSession();
			if (facebookSession != null && facebookSession.isClosed()) {
				startActivity(new Intent(getApplication(), LoginActivity.class));
				
			} else if (sharedPre.getLong(
					VentouraConstant.PRE_USER_ID_IN_SERVER, -1) == -1
					|| sharedPre.getInt(VentouraConstant.PRE_USER_ROLE, -1) == -1) {

				if(sharedPre.getString(VentouraConstant.PRE_USER_FACEBOOK_ID, "").equalsIgnoreCase("")){
					startActivity(new Intent(getApplication(),	LoginActivity.class));
				}
				else{
					startActivity(new Intent(getApplication(),	LoginChooseRoleActivity.class));
				}

			}else if (sharedPre.getInt(VentouraConstant.PRE_USER_ROLE, -1) == UserRole.GUIDE
					.getNumVal()) {

				startActivity(new Intent(this, GuidePortalActivity.class));

			} else {

				startActivity(new Intent(this, TravellerPortalActivity.class));
			}
		} else {
			checker.check(Resource.NETWORK);
		}
	}
}
