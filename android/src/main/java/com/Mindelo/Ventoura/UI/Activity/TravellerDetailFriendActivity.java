package com.Mindelo.Ventoura.UI.Activity;

import java.io.InputStream;
import java.util.List;

import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Entity.Country;
import com.Mindelo.Ventoura.Entity.ImageProfile;
import com.Mindelo.Ventoura.Enum.Gender;
import com.Mindelo.Ventoura.Ghost.IService.ICountryService;
import com.Mindelo.Ventoura.Ghost.IService.ITravellerService;
import com.Mindelo.Ventoura.Ghost.Service.CountryService;
import com.Mindelo.Ventoura.Ghost.Service.TravellerService;
import com.Mindelo.Ventoura.JSONEntity.JSONTravellerProfile;
import com.Mindelo.Ventoura.UI.Adapter.ProfileSlideShowViewAdapter;
import com.viewpagerindicator.CirclePageIndicator;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TravellerDetailFriendActivity extends FragmentActivity {

	private static final String TAG = "TravellerDetailFriendActivity";

	public static final int REQUESTCODE = 19110;
	/*
	 * progress bar
	 */
	private ProgressBar loadingImageProgressbar;

	/*
	 * instances need to be initiazed then to be used to set views
	 */
	private long travellerId;
	private JSONTravellerProfile travellerProfile;
	private boolean profileUpdatedFlag = false;

	/*
	 * represent the resource of the image
	 */
	private List<ImageProfile> imageProfiles;
	private boolean galleryUpdatedFlag = false;

	/*
	 * utility instances
	 */
	private ITravellerService travellerService;
	private ICountryService countryService;

	/*
	 * views
	 */
	private TextView tv_about, tv_name_age, tv_country;
	private ImageView iv_countryFlag;
	private Button btn_action;

	private CirclePageIndicator vpIndicator;
	private ViewPager vpImageGallery;

	/**
	 * adapter
	 * */
	private ProfileSlideShowViewAdapter pvAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_traveller_detail_friend);

		countryService = new CountryService(this);
		travellerService = new TravellerService(this);

		/*
		 * slide image view
		 */
		vpIndicator = (CirclePageIndicator) findViewById(R.id.vp_indicator);
		vpImageGallery = (ViewPager) findViewById(R.id.vp_image_gallery);

		/*
		 * guide basic information
		 */
		tv_country = (TextView) findViewById(R.id.traveller_detail_friend_country_tv);
		tv_about = (TextView) findViewById(R.id.traveller_detail_friend_description_tv);
		tv_name_age = (TextView) findViewById(R.id.traveller_detail_friend_name_age_tv);
		iv_countryFlag = (ImageView) findViewById(R.id.traveller_detail_friend_country_img);

		btn_action = (Button) findViewById(R.id.btn_action);
		btn_action.setVisibility(View.GONE);

		loadingImageProgressbar = (ProgressBar) findViewById(R.id.traveller_detail_loading_image_progress_bar);
		loadingImageProgressbar.setVisibility(View.GONE);

		/*
		 * set the views
		 */
		travellerId = getIntent().getLongExtra("travellerId", -1);

		setProfileFragmentViews();
		LoadTravellerProfileTask loadTravellerProfileTask = new LoadTravellerProfileTask();
		loadTravellerProfileTask.execute();

	}
	
	private void setProfileFragmentViews() {
		loadTravellerProfileFromDB();
		loadSlideProfileImagesFromDB();
	}

	private void loadSlideProfileImagesFromDB() {

		imageProfiles = travellerService
				.getTravellerGalleryImagesFromDB(travellerId);
		// TODO if only set as portal, seems this page cannot be updated. The
		// order of imageProfiles are all correct.
		pvAdapter = new ProfileSlideShowViewAdapter(
				getSupportFragmentManager(), imageProfiles);
		vpImageGallery.setAdapter(pvAdapter);
		vpIndicator.setViewPager(vpImageGallery);

	}

	private void loadTravellerProfileFromDB() {

		travellerProfile = travellerService
				.getTravellerProfileByIdFromDB(travellerId);

		if (travellerProfile != null) {
			if (travellerProfile.getTextBiography() != null
					&& travellerProfile.getTextBiography().length() > 0) {
				tv_about.setText(travellerProfile.getTextBiography() + "");
			} else {
				if (travellerProfile.getGender() == Gender.MALE) {
					tv_about.setText("He didn't say anything about himself.");
				} else {
					tv_about.setText("She didn't say anything about herself.");
				}
			}

			tv_name_age.setText(travellerProfile.getTravellerFirstname() + ", "
					+ travellerProfile.getAge());

			Country country = countryService.getCountryById(travellerProfile
					.getCountry());
			if (country != null) {
				tv_country.setText(country.getCountryName());
			} else {
				tv_country.setText("Alien ^.^"); // TODO
			}

			// set the country flag
			try {
				InputStream ims;
				ims = getAssets().open(
						ConfigurationConstant.VENTOURA_ASSET_COUNTRY_FLAG + "/"
								+ travellerProfile.getCountry() + ".png");
				Drawable countryFlag = Drawable.createFromStream(ims, null);
				iv_countryFlag.setImageDrawable(countryFlag);
			} catch (Exception e) {
				Log.e(TAG, "traveller profile load country flag error ");
				e.printStackTrace();
			}

		}

	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private class LoadTravellerProfileTask extends
			AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if(travellerProfile == null){
				loadingImageProgressbar.setVisibility(View.VISIBLE);	
			}
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {

			if (travellerService.getTravellerProfileByIdFromServer(travellerId) != null) {
				profileUpdatedFlag = true;
			}

			if (travellerService
					.loadTravellerAllGalleryImagesFromServer(travellerId)) {
				galleryUpdatedFlag = true;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			loadingImageProgressbar.setVisibility(View.GONE);
			if (profileUpdatedFlag) {
				loadTravellerProfileFromDB();
			}
			if (galleryUpdatedFlag) {
				loadSlideProfileImagesFromDB();
			}

		}
	}

	public void btnBackOnClick(View view) {
		onBackPressed();
	}

}
