package com.Mindelo.Ventoura.UI.Activity;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Entity.City;
import com.Mindelo.Ventoura.Entity.ImageProfile;
import com.Mindelo.Ventoura.Enum.Gender;
import com.Mindelo.Ventoura.Ghost.IService.ICityService;
import com.Mindelo.Ventoura.Ghost.IService.IGalleryService;
import com.Mindelo.Ventoura.Ghost.IService.IGuideService;
import com.Mindelo.Ventoura.Ghost.Service.CityService;
import com.Mindelo.Ventoura.Ghost.Service.GalleryService;
import com.Mindelo.Ventoura.Ghost.Service.GuideService;
import com.Mindelo.Ventoura.JSONEntity.JSONGuideAttraction;
import com.Mindelo.Ventoura.JSONEntity.JSONVentoura;
import com.Mindelo.Ventoura.UI.Adapter.ProfileSlideShowViewAdapter;
import com.viewpagerindicator.CirclePageIndicator;

import android.graphics.Bitmap;
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

public class GuideDetailVentouraActivity extends FragmentActivity {
	
	private static final String TAG = "GuideDetailVentouraActivity";

	public static final int REQUESTCODE = 19109;

	/*
	 * progress bar
	 */
	private ProgressBar loadingImageProgressbar;

	/*
	 * instances need to be initiazed then to be used to set views
	 */
	private JSONVentoura ventoura;

	/*
	 * utility instances
	 */
	private IGalleryService galleryService;
	private ICityService cityService;

	/*
	 * represent the resource of the image
	 */
	private List<ImageProfile> entities;

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
		setContentView(R.layout.activity_guide_detail_ventoura);
		
		galleryService = new GalleryService(this);
		cityService = new CityService(this);
		
		/*
		 * slide image view
		 */
		vpIndicator = (CirclePageIndicator) findViewById(R.id.vp_indicator);
		vpImageGallery = (ViewPager) findViewById(R.id.vp_image_gallery);

		/*
		 * guide basic information
		 */
		tv_about = (TextView) findViewById(R.id.guide_detail_ventoura_description_tv);
		tv_attractions = (TextView) findViewById(R.id.guide_detail_ventoura_attractions_tv);
		tv_tour_price = (TextView) findViewById(R.id.guide_detail_ventoura_tour_price_tv);
		tv_tour_length = (TextView) findViewById(R.id.guide_detail_ventoura_tour_length_tv);
		tv_tour_type = (TextView) findViewById(R.id.guide_detail_ventoura_tour_type_tv);
		tv_name_age = (TextView) findViewById(R.id.guide_detail_ventoura_name_age_tv);
		tv_city = (TextView) findViewById(R.id.guide_detail_ventoura_city_tv);
		iv_countryFlag = (ImageView) findViewById(R.id.guide_detail_ventoura_country_img);
		
		btn_action = (Button) findViewById(R.id.btn_action);
		btn_action.setVisibility(View.GONE);
		
		loadingImageProgressbar = (ProgressBar) findViewById(R.id.guide_detail_loading_image_progress_bar);
		loadingImageProgressbar.setVisibility(View.GONE);
		
		/*
		 * set the views
		 */
		ventoura = (JSONVentoura) getIntent().getSerializableExtra("ventoura");
		setCommonFieldViews();
		LoadGalleyImageTask loadGalleyImageTask = new LoadGalleyImageTask();
		loadGalleyImageTask.execute();
		
	}
	
	private void setCommonFieldViews(){	
		if(ventoura != null){
			if (ventoura.getTextBiography() != null
					&& ventoura.getTextBiography().length() > 0) {
				tv_about.setText(ventoura.getTextBiography() + "");
			}else{
				if(ventoura.getGender() == Gender.MALE){
					tv_about.setText("He didn't say anything about himself.");	
				}else{
					tv_about.setText("She didn't say anything about herself.");	
				}
			}

			tv_name_age.setText(ventoura.getFirstname() + ", "
					+ ventoura.getAge());
			
			City city = cityService.getCityById(ventoura.getCity());
			if(city != null){
				tv_city.setText(city.getCityName());
			}else{
				tv_city.setText("Alien ^.^"); // TODO
			}
			
			// set the tours
			tv_tour_length.setText(ventoura.getTourLength() + "");
			tv_tour_price.setText(ventoura.getTourPrice() + "");
			tv_tour_type.setText(ventoura.getTourType() + "");
			
			// set the country flag
			try {
				InputStream ims;
				ims = getAssets().open(
						ConfigurationConstant.VENTOURA_ASSET_COUNTRY_FLAG + "/"
								+ ventoura.getCountry() + ".png");
				Drawable countryFlag = Drawable.createFromStream(ims, null);
				iv_countryFlag.setImageDrawable(countryFlag);
			} catch (Exception e) {
				Log.e(TAG, "guide profile load country flag error ");
				e.printStackTrace();
			}
			
			String attractions = "";
			List<JSONGuideAttraction> jsonAttractions = ventoura.getAttractions();
			if(jsonAttractions != null && jsonAttractions.size() > 0){
				for (JSONGuideAttraction attraction : jsonAttractions) {
					attractions += attraction.getAttractionName() + "\n";
				}	
				tv_attractions.setText(attractions);
			}else{
				tv_attractions.setText("No special attractions provided !");
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
	
	private class LoadGalleyImageTask extends AsyncTask<Void, Void, Void>{
		

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loadingImageProgressbar.setVisibility(View.VISIBLE);
		}
		
		@Override
		protected Void doInBackground(Void... arg0) {
			entities = galleryService.loadTravellerAllGalleryImagesIntoList(ventoura.getId());
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			
			pvAdapter = new ProfileSlideShowViewAdapter(getSupportFragmentManager(), entities);
			vpImageGallery.setAdapter(pvAdapter);
			vpIndicator.setViewPager(vpImageGallery);
			
			loadingImageProgressbar.setVisibility(View.GONE);
		}
	}
	

	public void btnBackOnClick(View view) {
		onBackPressed();
	}
	
}
