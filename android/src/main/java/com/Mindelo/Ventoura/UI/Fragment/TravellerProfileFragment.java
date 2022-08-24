package com.Mindelo.Ventoura.UI.Fragment;

import java.io.InputStream;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.Mindelo.Ventoura.Constant.BroadcastConstant;
import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Constant.VentouraConstant;
import com.Mindelo.Ventoura.Entity.Country;
import com.Mindelo.Ventoura.Entity.ImageProfile;
import com.Mindelo.Ventoura.Ghost.IService.ICountryService;
import com.Mindelo.Ventoura.Ghost.IService.IGalleryService;
import com.Mindelo.Ventoura.Ghost.IService.ITravellerService;
import com.Mindelo.Ventoura.JSONEntity.JSONTravellerProfile;
import com.Mindelo.Ventoura.UI.Activity.R;
import com.Mindelo.Ventoura.UI.Activity.TravellerPortalActivity;
import com.Mindelo.Ventoura.UI.Activity.TravellerProfileEditActivity;
import com.Mindelo.Ventoura.UI.Adapter.ProfileSlideShowViewAdapter;
import com.viewpagerindicator.CirclePageIndicator;

public class TravellerProfileFragment extends Fragment {

	private static final String TAG = "TravellerProfileFragment";

	/*
	 * instances need to be initiazed then to be used to set views
	 */
	private long travellerId;
	private JSONTravellerProfile travellerProfile;
	private boolean profileUpdatedFlag = false;
	private boolean galleryUpdatedFlag = false;
	
	/*
	 * utility global variables
	 */
	private TravellerPortalActivity activity;
	private SharedPreferences sharedPre;
	private final int EDIT_PROFILE_REQUEST = 1023;

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
	private Button editBtn;

	private CirclePageIndicator vpIndicator;
	private ViewPager vpImageGallery;

	/**
	 * adapter
	 * */
	ProfileSlideShowViewAdapter pvAdapter;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		activity = (TravellerPortalActivity) getActivity();

		travellerService = activity.getTravellerService();
		countryService = activity.getCountryService();
		travellerId = activity.getTravellerId();
		sharedPre = activity.getSharedPre();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_traveller_profile,
				container, false);

		/*
		 * slide image view
		 */
		vpIndicator = (CirclePageIndicator) view
				.findViewById(R.id.vp_indicator);
		vpImageGallery = (ViewPager) view.findViewById(R.id.vp_image_gallery);

		tv_about = (TextView) view
				.findViewById(R.id.traveller_profile_description_tv);
		tv_name_age = (TextView) view
				.findViewById(R.id.traveller_profile_name_age_tv);
		iv_countryFlag = (ImageView) view
				.findViewById(R.id.traveller_profile_country_img);
		tv_country = (TextView) view
				.findViewById(R.id.traveller_profile_country_tv);

		/*
		 * menu buttons
		 */
		editBtn = (Button) view.findViewById(R.id.btn_title_bar_edit);
		editBtn.setOnClickListener(new EditButtonOnclickListener());

		
		loadTravellerProfileFromDB();
		loadSlideProfileImagesFromDB();
		LoadTravellerProfileTask loadUserProfileTask = new LoadTravellerProfileTask();
		loadUserProfileTask.execute();

		return view;
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		activity.unregisterReceiver(receiver);
	}

	@Override
	public void onResume() {
		activity.registerReceiver(receiver, new IntentFilter(BroadcastConstant.USER_PROFILE_IMAGES_UPDATED_ACTION));
		super.onResume();
	}
	
	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			setProfileFragmentViews();
		}
	};


	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}


	private void loadTravellerProfileFromDB() {

		travellerProfile = travellerService
				.getTravellerProfileByIdFromDB(travellerId);

		if (travellerProfile != null) {

			if (travellerProfile.getTextBiography() != null
					&& travellerProfile.getTextBiography().length() > 0) {
				tv_about.setText(travellerProfile.getTextBiography() + "");
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
				ims = activity.getAssets().open(
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

	/**
	 * Network tasks for load the traveller's profile
	 */
	private class LoadTravellerProfileTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Void doInBackground(Void... params) {
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
		protected void onPostExecute(Void result) {
			if (profileUpdatedFlag) {
				loadTravellerProfileFromDB();
			}
			if (galleryUpdatedFlag) {
				loadSlideProfileImagesFromDB();
				activity.getMenuFragment().refreshPortalImage();
			}
		}
	}
	
	private void setProfileFragmentViews(){
		loadTravellerProfileFromDB();
		loadSlideProfileImagesFromDB();
		activity.getMenuFragment().refreshPortalImage();
	}

	private void loadSlideProfileImagesFromDB() {

		List<ImageProfile> imageProfiles = travellerService
				.getTravellerGalleryImagesFromDB(travellerId);
		//TODO if only set as portal, seems this page cannot be updated. The order of imageProfiles are all correct. 
		pvAdapter = new ProfileSlideShowViewAdapter(getFragmentManager(),
				imageProfiles);
		vpImageGallery.setAdapter(pvAdapter);
		vpIndicator.setViewPager(vpImageGallery);

	}

	private class EditButtonOnclickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_title_bar_edit:
				Intent intent = new Intent(getActivity(),
						TravellerProfileEditActivity.class);
				intent.putExtra(VentouraConstant.INTENT_TRAVELLER_PROFILE, travellerProfile);
				startActivityForResult(intent, EDIT_PROFILE_REQUEST);
				break;
			}
		}
	}

}
