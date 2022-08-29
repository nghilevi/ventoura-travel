package com.Mindelo.Ventoura.UI.Activity;

import java.util.Arrays;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.Mindelo.Ventoura.Constant.VentouraConstant;
import com.Mindelo.Ventoura.Ghost.IService.IGeoLocationService;
import com.Mindelo.Ventoura.Ghost.Service.GeoLocationService;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

public class LoginActivity extends FragmentActivity {

	private final String TAG = "com.Mindelo.Ventoura.UI.Activity.LoginActivity";

	/*
	 * facebook login variables
	 */
	private UiLifecycleHelper uiHelper;
	private Session session;
	private Activity activity;
	private IGeoLocationService locationService;

	/*
	 * global variables
	 */
	private SharedPreferences sharedPre;
	private String facebookId = null, userFirstname, userLastname, userGender,
			userCity, userCountry, userBirthday;

	/*
	 * views
	 */
	private LoginButton authButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);

		/*
		 * facebook login button
		 */
		this.setContentView(R.layout.activity_login);
		authButton = (LoginButton) findViewById(R.id.facebook_login_button);

		/*
		 * Ask facebook api permissions
		 */
		authButton.setReadPermissions(Arrays.asList("public_profile",
				"user_birthday"));

		sharedPre = getSharedPreferences(
				VentouraConstant.SHARED_PREFERENCE_VENTOURA,
				Context.MODE_PRIVATE);

		locationService = new GeoLocationService(this);

	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	// The status of the login session will be shown in this method
	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
	}

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.i(TAG, "Return from facebook login page");
		uiHelper.onActivityResult(requestCode, resultCode, data);
		/*
		 * get user's infor from facebook
		 */
		retrieveUserInfoFromFacebook();
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	/*
	 * fetch user information from the facebook
	 */
	private void retrieveUserInfoFromFacebook() {
		session = Session.getActiveSession();
		// If the session is open, make an API call to get user data
		// and define a new callback to handle the response
		Request request = Request.newMeRequest(session,
				new Request.GraphUserCallback() {
					public void onCompleted(GraphUser user, Response response) {
						if (user != null) {
							facebookId = user.getId(); // user id
							userFirstname = user.getFirstName();
							userLastname = user.getLastName();

							userBirthday = user.getBirthday();
							userGender = user.asMap().get("gender").toString();

							// search by country code, cause the country name
							// retrieved from Geocoder maybe not consistent with
							// our own database
							userCountry = locationService
									.getUserCurrentCountryCode();
							userCity = "TODO";
							/*
							 * save basic infor
							 */
							saveUserSessionData();

							/*
							 * to choose role activity
							 */
							activity.finish();
							Intent intent = new Intent(activity,
									LoginChooseRoleActivity.class);
							startActivity(intent);

						}
					}
				});

		Request.executeBatchAsync(request);
	}

	/*
	 * After the user login to ventoura server, save the basic information to
	 * the shared preference
	 */
	private void saveUserSessionData() {
		Editor editor = sharedPre.edit();

		editor.putString(VentouraConstant.PRE_USER_FIRST_NAME, userFirstname);
		editor.putString(VentouraConstant.PRE_USER_LAST_NAME, userLastname);
		editor.putString(VentouraConstant.PRE_USER_BIRTHDAY, userBirthday);
		editor.putString(VentouraConstant.PRE_USER_GENDER, userGender);
		editor.putString(VentouraConstant.PRE_USER_CITY, userCity);
		editor.putString(VentouraConstant.PRE_USER_COUNTRY, userCountry);
		editor.putString(VentouraConstant.PRE_USER_FACEBOOK_ID, facebookId);

		editor.commit();
	}

	@Override
	protected void onStart() {
		Log.i(TAG, "onStart start");
		super.onStart();
	}

	@Override
	protected void onRestart() {
		Log.i(TAG, "onRestart start");
		super.onRestart();
	}

	@Override
	protected void onStop() {
		Log.i(TAG, "onStop start");
		super.onStop();
	}
}
