package com.Mindelo.Ventoura.UI.Activity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.Mindelo.Ventoura.Constant.VentouraConstant;
import com.Mindelo.Ventoura.Entity.Country;
import com.Mindelo.Ventoura.Entity.ImageProfile;
import com.Mindelo.Ventoura.Entity.Traveller;
import com.Mindelo.Ventoura.Enum.Gender;
import com.Mindelo.Ventoura.Enum.UserRole;
import com.Mindelo.Ventoura.Ghost.IService.ICountryService;
import com.Mindelo.Ventoura.Ghost.IService.ITravellerService;
import com.Mindelo.Ventoura.Ghost.Service.CountryService;
import com.Mindelo.Ventoura.Ghost.Service.TravellerService;
import com.Mindelo.Ventoura.UI.Adapter.TravellerWalkThroughViewPagerAdapter;
import com.Mindelo.Ventoura.Util.FacebookUtil;
import com.Mindelo.Ventoura.Util.HttpUtility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TravellerProfileInitializeActivity extends FragmentActivity {

	private static final String TAG = "TravellerProfileInitializeActivity";

	ITravellerService travellerService;
	ICountryService countryService;
	Activity activity;
	SharedPreferences sharedPre;
	
	/*
	 * views
	 */
	private ViewPager viewPager;
	private Button getStartBtn;

	/*
	 * global variables
	 */
	private String facebookUserId, userFirstname, userLastname, gender, city,
			country, birthday;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		Log.i(TAG, "onCreate()");
		super.onCreate(savedInstanceState);
		sharedPre = getSharedPreferences(
				VentouraConstant.SHARED_PREFERENCE_VENTOURA,
				Context.MODE_PRIVATE);
		
		this.setContentView(R.layout.activity_traveller_profile_initialize);
		
		/*
		 * initialize slide image views
		 */
		viewPager = (ViewPager) findViewById(R.id.traveller_walk_through_viewpager); 
		getStartBtn = (Button) findViewById(R.id.traveller_walk_through_get_start_btn);
		
		LayoutInflater mLi = LayoutInflater.from(this);
		View view1 = mLi.inflate(R.layout.traveller_walk_through_flip01, null);
		View view2 = mLi.inflate(R.layout.traveller_walk_through_flip02, null);
		View view3 = mLi.inflate(R.layout.traveller_walk_through_flip03, null);
		

		final List<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		views.add(view3);

		TravellerWalkThroughViewPagerAdapter mPagerAdapter = new TravellerWalkThroughViewPagerAdapter(views);
		viewPager.setAdapter(mPagerAdapter);
		
		/*
		 * initialize utilities
		 */
		activity = this;
		travellerService = new TravellerService(this);
		countryService = new CountryService(this);

		facebookUserId = sharedPre.getString(
				VentouraConstant.PRE_USER_FACEBOOK_ID, "");
		userFirstname = sharedPre.getString(
				VentouraConstant.PRE_USER_FIRST_NAME, "");
		userLastname = sharedPre.getString(VentouraConstant.PRE_USER_LAST_NAME,
				"");
		gender = sharedPre.getString(VentouraConstant.PRE_USER_GENDER, "");
		city = sharedPre.getString(VentouraConstant.PRE_USER_CITY, "");
		country = sharedPre.getString(VentouraConstant.PRE_USER_COUNTRY, "");
		birthday = sharedPre.getString(VentouraConstant.PRE_USER_BIRTHDAY, "");
		
		
		// create the user
		CreateNewTravellerProbeTask createNewTravellerProbeTask = new CreateNewTravellerProbeTask();
		createNewTravellerProbeTask.execute();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	/**
	 * Network tasks for creating a new user
	 */
	private class CreateNewTravellerProbeTask extends
			AsyncTask<Void, Void, Boolean> {

		public CreateNewTravellerProbeTask() {
			//dialog = new ProgressDialog(activity);
		}

		@Override
		protected void onPreExecute() {
			//dialog.setMessage("Progress start");
			//dialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			long result;

			Traveller traveller = new Traveller();

			traveller.setTravellerFirstname(userFirstname);
			traveller.setTravellerLastname(userLastname);
			traveller.setFacebookAccountName(facebookUserId);
			
			if(country != null && !country.equals("")){
				Country userCountry = countryService.getCountryByISO2(country);
				if(userCountry != null){
					traveller.setCountry(userCountry.getId());	
				}
			}
			
			if (gender.equalsIgnoreCase("male")
					|| gender.equalsIgnoreCase("boy")) {
				traveller.setGender(Gender.MALE);
			} else {
				traveller.setGender(Gender.FEMALE);
			}
			
			traveller.setTextBiography("");

			DateFormat format = new SimpleDateFormat("MM/dd/yyyy",
					Locale.getDefault());
			try {
				traveller.setDateOfBirth(format.parse(birthday));
			} catch (Exception e) {
				// TODO Nothing.
			}
			try {
				ImageProfile portalImage = new ImageProfile();
				portalImage.setImageContent(HttpUtility
						.downloadImageFromUrl("https://graph.facebook.com/"
								+ facebookUserId + "/picture?type=large"));
				portalImage.setPortal(true);
				traveller.setPortalImage(portalImage);

				result = travellerService.uploadTraverllerProfile(traveller);
				if (result == -1) {
					return false;
				}

			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			saveUserSessionData(result);
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {

			/*if (dialog.isShowing()) {
				dialog.dismiss();	
			}*/
			finish();
			if (result) {
				// TODO the user was created
			} else {
				FacebookUtil.logoutFacebook(activity);
				startActivity(new Intent(activity, LoginActivity.class));

				Toast t = Toast.makeText(activity,
						"Login error, please try again.", Toast.LENGTH_SHORT);
				t.setGravity(Gravity.FILL_HORIZONTAL, 0, 0);
			}
		}
	}
	
	/**
	 * the function when the "get start" button clicked
	 */
	public void letsGoOnClick(View v) {
		startActivity(new Intent(activity,
				TravellerPortalActivity.class));
		finish();
	}

	/*
	 * After the user login, save all the session data into the shared reference
	 * here
	 */
	private void saveUserSessionData(long userIdInServer) {
		Editor editor = sharedPre.edit();

		editor.putInt(VentouraConstant.PRE_USER_ROLE,
				UserRole.TRAVELLER.getNumVal());
		editor.putLong(VentouraConstant.PRE_USER_ID_IN_SERVER, userIdInServer);
		editor.commit();
	}

}
