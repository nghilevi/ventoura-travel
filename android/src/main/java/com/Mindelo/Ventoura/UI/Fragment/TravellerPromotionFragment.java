package com.Mindelo.Ventoura.UI.Fragment;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.Mindelo.Ventoura.Constant.VentouraConstant;
import com.Mindelo.Ventoura.Entity.City;
import com.Mindelo.Ventoura.Ghost.IService.IPromotionService;
import com.Mindelo.Ventoura.Ghost.Service.PromotionService;
import com.Mindelo.Ventoura.UI.Activity.CitySelectorActivity;
import com.Mindelo.Ventoura.UI.Activity.R;
import com.Mindelo.Ventoura.UI.Activity.TravellerPortalActivity;
import com.Mindelo.Ventoura.UI.Activity.TravellerPromotionPreviewActivity;
import com.Mindelo.Ventoura.Util.ImageUtil;

public class TravellerPromotionFragment extends Fragment implements
		OnClickListener {

	private static final String TAG = "TravellerPromotionFragment";
	

	/*
	 * progress dialog
	 */
	private ProgressDialog progressDialog;
	

	private TravellerPortalActivity portalActivity;
	private SharedPreferences sharedPre;
	private IPromotionService promotionService;

	private static final int REQUESTCODECITY0 = 0;
	private static final int REQUESTCODECITY1 = 1;
	private static final int REQUESTCODECITY2 = 2;
	private static final int REQUESTCODECITY3 = 3;

	/*
	 * views
	 */
	private FrameLayout fragmentSelectCity, fragmentAttended;
	private Button btnCity00, btnCity01, btnCity02, btnCity03;
	private Button previewPromotionBtn;
	private ImageView promotion_image_view;

	/*
	 * blobal variables
	 */
	private boolean promotionIsDone = false;
	private City cities[];
	private ArrayList<Integer> cityIds = new ArrayList<Integer>();
	private long travellerId;
	private boolean alreadyAttendedPromotion = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		portalActivity = (TravellerPortalActivity) getActivity();
		sharedPre = portalActivity.getSharedPre();
		promotionService = new PromotionService();
		travellerId = portalActivity.getTravellerId();

		PromotionProbeTask promotionProbeTask = new PromotionProbeTask();
		promotionProbeTask.execute();
		
	}

	@Override
	public void onResume() {
		super.onResume();
		alreadyAttendedPromotion = sharedPre.getBoolean(
				VentouraConstant.PRE_USER_ATTENDED_PROMOTION, false);
		setViewsIfAttended();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);

		View view = inflater
				.inflate(R.layout.fragment_traveller_promotion_content,
						container, false);

		fragmentSelectCity = (FrameLayout) view
				.findViewById(R.id.fragment_traveller_promotion_select_city);
		fragmentAttended = (FrameLayout) view
				.findViewById(R.id.fragment_traveller_promotion_attended);

		cities = new City[4];

		btnCity00 = (Button) view
				.findViewById(R.id.traveller_promotion_dream_trip_city00);
		btnCity00.setOnClickListener(this);
		btnCity01 = (Button) view
				.findViewById(R.id.traveller_promotion_dream_trip_city01);
		btnCity01.setOnClickListener(this);
		btnCity02 = (Button) view
				.findViewById(R.id.traveller_promotion_dream_trip_city02);
		btnCity02.setOnClickListener(this);
		btnCity03 = (Button) view
				.findViewById(R.id.traveller_promotion_dream_trip_city03);
		btnCity03.setOnClickListener(this);

		previewPromotionBtn = (Button) view
				.findViewById(R.id.promotion_next_preview_btn);
		previewPromotionBtn.setOnClickListener(this);

		promotion_image_view = (ImageView) view
				.findViewById(R.id.activity_traveller_promotion_preview_city_image);

		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.traveller_promotion_dream_trip_city00:
			startActivityForResult(new Intent(portalActivity,
					CitySelectorActivity.class), REQUESTCODECITY0);
			break;
		case R.id.traveller_promotion_dream_trip_city01:
			startActivityForResult(new Intent(portalActivity,
					CitySelectorActivity.class), REQUESTCODECITY1);
			break;
		case R.id.traveller_promotion_dream_trip_city02:
			startActivityForResult(new Intent(portalActivity,
					CitySelectorActivity.class), REQUESTCODECITY2);
			break;
		case R.id.traveller_promotion_dream_trip_city03:
			startActivityForResult(new Intent(portalActivity,
					CitySelectorActivity.class), REQUESTCODECITY3);
			break;
		case R.id.promotion_next_preview_btn:
			if (promotionIsDone == true) {
				break;
			}
			if (btnCity00.getText().equals("Add City")
					|| btnCity01.getText().equals("Add City")
					|| btnCity02.getText().equals("Add City")
					|| btnCity03.getText().equals("Add City")) {
				Toast.makeText(portalActivity, "pelease select four cities",
						Toast.LENGTH_SHORT).show();
				break;
			}
			// first time done
			promotionIsDone = true;

			// @share to facebook
			LoadPromotionImageTask loadPromotionImageTask = new LoadPromotionImageTask();
			loadPromotionImageTask.execute();

			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		City citySelected = null;
		if (data != null) {
			citySelected = (City) data.getSerializableExtra("CitySelected");
		}
		if (citySelected == null) {
			return;
		}
		switch (requestCode) {
		case REQUESTCODECITY0:
			cities[0] = citySelected;
			btnCity00.setTextColor(R.color.white);
			btnCity00
					.setBackgroundResource(R.drawable.promotion_add_city_done_bg);
			btnCity00.setText(citySelected.getCityName());
			break;
		case REQUESTCODECITY1:
			cities[1] = citySelected;
			btnCity01.setTextColor(R.color.white);
			btnCity01
					.setBackgroundResource(R.drawable.promotion_add_city_done_bg);
			btnCity01.setText(citySelected.getCityName());
			break;
		case REQUESTCODECITY2:
			cities[2] = citySelected;
			btnCity02
					.setBackgroundResource(R.drawable.promotion_add_city_done_bg);
			btnCity02.setTextColor(R.color.white);
			btnCity02.setText(citySelected.getCityName());
			break;
		case REQUESTCODECITY3:
			cities[3] = citySelected;
			btnCity03.setTextColor(R.color.white);
			btnCity03
					.setBackgroundResource(R.drawable.promotion_add_city_done_bg);
			btnCity03.setText(citySelected.getCityName());
			break;
		default:
			break;
		}
	}

	private void setViewsIfAttended() {
		
		if (alreadyAttendedPromotion) {
			fragmentSelectCity.setVisibility(View.GONE);
			fragmentAttended.setVisibility(View.VISIBLE);
			previewPromotionBtn
					.setBackgroundResource(R.drawable.selector_like_us_on_facebook_btn);
			previewPromotionBtn.setText("");
			previewPromotionBtn
					.setOnClickListener(new LikeUsOnFacebookOnclickListener());
			promotion_image_view.setImageBitmap(ImageUtil
					.decodeScaledBitmapFromSdCard(promotionService
							.getCityPromotionImageFilePath(travellerId)));

		} else {
			fragmentSelectCity.setVisibility(View.VISIBLE);
			fragmentAttended.setVisibility(View.GONE);
		}
	}
	

	private class LikeUsOnFacebookOnclickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {
			// Open the browser to like us
			Uri webpage = Uri.parse(VentouraConstant.URL_VENTOURA_FACEBOOK);
			Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
			startActivity(intent);
		}
	}

	/**
	 * Network tasks for creating loading the city image from file server
	 */
	private class LoadPromotionImageTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			showProgressDialog();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				for (City city : cities) {
					cityIds.add(city.getId());
				}
				return promotionService.getCityPromotionImageFromServer(travellerId,
						cities);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			dismissProgressDialog();
			if(result.booleanValue()){
				Intent intent = new Intent(portalActivity,
						TravellerPromotionPreviewActivity.class);
				intent.putIntegerArrayListExtra("cityIds", cityIds);
				intent.putExtra("travellerId", travellerId);
				startActivity(intent);	
			}else{
				Toast.makeText(portalActivity, "Error happened. Please try it again." , Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	/**
	 * Network tasks for testing whether this guy already attended promotion
	 */
	private class PromotionProbeTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... arg0) {
			return promotionService.travellerPromotionProbe(travellerId);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result.booleanValue()) {
				if (alreadyAttendedPromotion == false) {
					alreadyAttendedPromotion = true;
					setViewsIfAttended();
					Editor edit = sharedPre.edit();
					edit.putBoolean(VentouraConstant.PRE_USER_ATTENDED_PROMOTION, true);
					edit.commit();
				}
			}
		}
	}
	
	private void showProgressDialog() {
		progressDialog = new ProgressDialog(portalActivity);
		progressDialog.setTitle("Varifying...");
		progressDialog.show();
	}

	private void dismissProgressDialog() {
		// Dismiss the progress dialog
		if (progressDialog != null) {
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			progressDialog = null;
		}
	}

}
