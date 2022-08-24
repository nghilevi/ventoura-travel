package com.Mindelo.Ventoura.UI.Activity;

import java.io.InputStream;
import java.util.List;

import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Entity.City;
import com.Mindelo.Ventoura.Entity.ImageProfile;
import com.Mindelo.Ventoura.Ghost.IService.ICityService;
import com.Mindelo.Ventoura.Ghost.IService.IGuideService;
import com.Mindelo.Ventoura.Ghost.Service.CityService;
import com.Mindelo.Ventoura.Ghost.Service.GuideService;
import com.Mindelo.Ventoura.JSONEntity.JSONGuideAttraction;
import com.Mindelo.Ventoura.JSONEntity.JSONGuideProfile;
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

public class GuideDetailFriendActivity extends FragmentActivity {

	private static final String TAG = "GuideDetailFriendActivity";
	
	/*
	 * progress bar
	 */
	private ProgressBar loadingImageProgressbar;

	/*
	 * instances need to be initiazed then to be used to set views
	 */
	private long guideId;
	private JSONGuideProfile guideProfile = null;
	private boolean profileUpdatedFlag = false;
	private boolean galleryUpdatedFlag = false;

	/*
	 * utility instances
	 */
	private IGuideService guideService;
	private ICityService cityService;

	/*
	 * views
	 */
	private TextView tv_about, tv_tour_price, tv_tour_length, tv_attractions,
			tv_tour_type, tv_name_age, tv_city;
	private ImageView iv_countryFlag;
	private Button btn_action;

	private CirclePageIndicator vpIndicator;
	private ViewPager vpImageGallery;

	/**
	 * adapter
	 * */
	private ProfileSlideShowViewAdapter pvAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_detail_friend);

		guideService = new GuideService(this);
		cityService = new CityService(this);
		guideId = getIntent().getLongExtra("guideId", -1);

		/*
		 * slide image view
		 */
		vpIndicator = (CirclePageIndicator) findViewById(R.id.vp_indicator);
		vpImageGallery = (ViewPager) findViewById(R.id.vp_image_gallery);
		
		
		loadingImageProgressbar = (ProgressBar) findViewById(R.id.guide_detail_loading_image_progress_bar);
		loadingImageProgressbar.setVisibility(View.GONE);
		
		btn_action = (Button) findViewById(R.id.btn_action);
		btn_action.setVisibility(View.GONE);

		/*
		 * guide basic information
		 */
		tv_about = (TextView) findViewById(R.id.guide_detail_friend_description_tv);
		tv_attractions = (TextView) findViewById(R.id.guide_detail_friend_attractions_tv);
		tv_tour_price = (TextView) findViewById(R.id.guide_detail_friend_tour_price_tv);
		tv_tour_length = (TextView) findViewById(R.id.guide_detail_friend_tour_length_tv);
		tv_tour_type = (TextView) findViewById(R.id.guide_detail_friend_tour_type_tv);
		tv_name_age = (TextView) findViewById(R.id.guide_detail_friend_name_age_tv);
		tv_city = (TextView) findViewById(R.id.guide_detail_friend_city_tv);
		iv_countryFlag = (ImageView) findViewById(R.id.guide_detail_friend_country_img);

		/*
		 * load the guide profile data
		 */
		setProfileFragmentViews();
		LoadGuideProfileTask loadGuideProfileTask = new LoadGuideProfileTask();
		loadGuideProfileTask.execute();

	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
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

	private void loadGuideProfileFromDB() {

		guideProfile = guideService.getGuideProfileByIdFromDB(guideId);

		if (guideProfile != null) {

			tv_tour_length.setText(guideProfile.getTourLength() + "");
			tv_tour_price.setText(guideProfile.getTourPrice() + "");
			tv_tour_type.setText(guideProfile.getTourType() + "");

			if (guideProfile.getTextBiography() != null
					&& guideProfile.getTextBiography().length() > 0) {
				tv_about.setText(guideProfile.getTextBiography() + "");
			}

			City city = cityService.getCityById(guideProfile.getCity());
			if (city != null) {
				tv_city.setText(city.getCityName());
			}

			tv_name_age.setText(guideProfile.getGuideFirstname() + ", "
					+ guideProfile.getAge());

			// set the country flag
			try {
				InputStream ims;
				ims = getAssets().open(
						ConfigurationConstant.VENTOURA_ASSET_COUNTRY_FLAG + "/"
								+ guideProfile.getCountry() + ".png");
				Drawable countryFlag = Drawable.createFromStream(ims, null);
				iv_countryFlag.setImageDrawable(countryFlag);
			} catch (Exception e) {
				Log.e(TAG, "guide profile load country flag error ");
				e.printStackTrace();
			}

			String attractions = "";
			List<JSONGuideAttraction> jsonAttractions = guideProfile
					.getAttractions();
			if (jsonAttractions != null) {
				for (JSONGuideAttraction attraction : jsonAttractions) {
					attractions += attraction.getAttractionName() + "\n";
				}
				tv_attractions.setText(attractions);
			}
		}
	}

	/**
	 * Network tasks for load the guide's profile
	 */
	private class LoadGuideProfileTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			if(guideProfile == null){
				loadingImageProgressbar.setVisibility(View.VISIBLE);	
			}
		}

		@Override
		protected Void doInBackground(Void... params) {

			guideProfile = guideService.getGuideProfileByIdFromServer(guideId);
			if(guideProfile != null){
				profileUpdatedFlag = true;
			}
			if(guideService.loadGuideAllGalleryImagesFromServer(guideId)){
				galleryUpdatedFlag = true;
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			loadingImageProgressbar.setVisibility(View.GONE);
			
			if (profileUpdatedFlag) {
				loadGuideProfileFromDB();
			}
			if (galleryUpdatedFlag) {
				loadSlideProfileImagesFromDB();
			}
		}
	}

	private void setProfileFragmentViews() {
		loadGuideProfileFromDB();
		loadSlideProfileImagesFromDB();
	}

	private void loadSlideProfileImagesFromDB() {

		List<ImageProfile> imageProfiles = guideService
				.getGuideGalleryImagesFromDB(guideId);

		pvAdapter = new ProfileSlideShowViewAdapter(
				getSupportFragmentManager(), imageProfiles);
		vpImageGallery.setAdapter(pvAdapter);
		vpIndicator.setViewPager(vpImageGallery);

	}
	
	public void btnBackOnClick(View view) {
		onBackPressed();
	}

}
