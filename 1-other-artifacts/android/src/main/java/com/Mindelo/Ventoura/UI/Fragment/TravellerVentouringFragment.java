package com.Mindelo.Ventoura.UI.Fragment;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.Mindelo.Ventoura.Enum.UserRole;
import com.Mindelo.Ventoura.Ghost.IService.IGuideService;
import com.Mindelo.Ventoura.Ghost.IService.ITravellerService;
import com.Mindelo.Ventoura.Ghost.IService.IVentouraService;
import com.Mindelo.Ventoura.Ghost.Service.CityService;
import com.Mindelo.Ventoura.Ghost.Service.CountryService;
import com.Mindelo.Ventoura.Ghost.Service.VentouraService;
import com.Mindelo.Ventoura.JSONEntity.JSONVentoura;
import com.Mindelo.Ventoura.Model.CardModel;
import com.Mindelo.Ventoura.UI.Activity.GuideDetailVentouraActivity;
import com.Mindelo.Ventoura.UI.Activity.R;
import com.Mindelo.Ventoura.UI.Activity.TravellerDetailVentouraActivity;
import com.Mindelo.Ventoura.UI.Activity.TravellerPortalActivity;
import com.Mindelo.Ventoura.UI.Activity.TravellerVentouringFilterActivity;
import com.Mindelo.Ventoura.UI.Adapter.VentouraCardStackAdapter;
import com.Mindelo.Ventoura.UI.View.CardContainerView;

public class TravellerVentouringFragment extends Fragment {

	private static final String TAG = "TravellerVentouringFragment";

	/*
	 * progress bar
	 */
	private ProgressBar loadingVentouraProgressbar;

	/*
	 * utility instances
	 */
	private TravellerPortalActivity activity;
	private IVentouraService ventouraService;
	private ITravellerService travellerService;
	private IGuideService guideService;
	private long travellerId;

	/*
	 * views
	 */
	private TextView ventouraNameTextView;
	private TextView ventouraAgeTextView;
	private TextView ventouraCityTextView;
	private ImageButton likeButton, notLikeButton;


	/*
	 * global variables
	 */
	private Map<String, Bitmap> ventouraPortalImagesMap;
	private List<JSONVentoura> ventouraList;
	private CardContainerView vCardContainer;
	private VentouraCardStackAdapter adapter;
	
	private static final int REQUEST_CHECK_VENTOURA_DETAIL = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity = (TravellerPortalActivity) getActivity();
		ventouraService = new VentouraService();
		guideService = activity.getGuideService();
		travellerId = activity.getTravellerId();
		travellerService = activity.getTravellerService();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);

		View view = inflater.inflate(R.layout.fragment_traveller_ventouring,
				container, false);

		
		loadingVentouraProgressbar = (ProgressBar) view.findViewById(R.id.ventoura_loading_progress_bar);
		loadingVentouraProgressbar.setVisibility(View.GONE);
		/*
		 * Views initialize
		 */
		ventouraNameTextView = (TextView) view
				.findViewById(R.id.ventoura_name_tv);
		ventouraAgeTextView = (TextView) view
				.findViewById(R.id.ventoura_age_tv);
		ventouraCityTextView = (TextView) view
				.findViewById(R.id.ventoura_city_tv);

		/*
		 * like or not like button
		 */
		likeButton = (ImageButton) view.findViewById(R.id.btn_ventouring_yes);
		notLikeButton = (ImageButton) view.findViewById(R.id.btn_ventouring_no);
		LikeOrNotLikeOnclickListener likeOrNotLikeOnclickListener = new LikeOrNotLikeOnclickListener();
		likeButton.setOnClickListener(likeOrNotLikeOnclickListener);
		notLikeButton.setOnClickListener(likeOrNotLikeOnclickListener);

		vCardContainer = (CardContainerView) view
				.findViewById(R.id.iv_guide_ventouring_container);
		vCardContainer.setVentouraNameTextView(ventouraNameTextView);
		vCardContainer.setVentouraAgeTextView(ventouraAgeTextView);
		vCardContainer.setVentouraLocationTextView(ventouraCityTextView);
		vCardContainer.setCountryService(new CountryService(activity));
		vCardContainer.setCityService(new CityService(activity));
		/*
		 * set the clickable view's area
		 */
		vCardContainer.setClickViewId(R.id.ventouring_rounded_image);

		/*
		 * init the right option button 
		 */
		Button optionButton = (Button)view.findViewById(R.id.btn_action);
		optionButton.setText("Filter");
		optionButton.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(), TravellerVentouringFilterActivity.class));
			}
		});
		
		// fetch first ventoura package
		LoadVentouraTask loadVentouraTask = new LoadVentouraTask();
		loadVentouraTask.execute();
		
		return view;
	}


	/*
	 * Network tasks for loading the ventouras
	 */
	private class LoadVentouraTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			loadingVentouraProgressbar.setVisibility(View.VISIBLE);
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				ventouraList = ventouraService.getTravellerVentouraList(
						travellerId).getVentouraList();
				ventouraPortalImagesMap = ventouraService
						.loadVentouraImages(ventouraList);
				return null;

			} catch (Exception e) {
				e.printStackTrace();
				Log.e(TAG, "Traveller Ventoura Download Error");
				return null;
			}

		}

		@Override
		protected void onPostExecute(Void result) {

			if (ventouraList != null && ventouraList.size() > 0) {
				/*
				 * load the head images
				 */
				adapter = new VentouraCardStackAdapter(activity);

				for (int index = ventouraList.size() - 1; index >= 0; index--) {
					String ventouraPortalImageFile;
					final JSONVentoura ventouraTemp = ventouraList.get(index);

					if (ventouraTemp.getUserRole() == UserRole.GUIDE) {
						ventouraPortalImageFile = "g_" + ventouraTemp.getId()
								+ "_.png";
					} else {
						ventouraPortalImageFile = "t_" + ventouraTemp.getId()
								+ "_.png";
					}

					if (ventouraPortalImagesMap != null && ventouraPortalImagesMap
							.containsKey(ventouraPortalImageFile)) {

						CardModel cardModel = new CardModel(ventouraTemp,
								new BitmapDrawable(getResources(),
										ventouraPortalImagesMap
												.get(ventouraPortalImageFile)));

						cardModel
								.setOnClickListener(new CardModel.OnClickListener() {
									@Override
									public void OnClickListener() {
										checkVentouraDetail(ventouraTemp);
									}
								});
						cardModel
								.setOnCardDimissedListener(new CardModel.OnCardDimissedListener() {

									@Override
									public void onLike() {
										handleSwipedAction(true, ventouraTemp);
									}

									@Override
									public void onDislike() {
										handleSwipedAction(false, ventouraTemp);
									}
								});
						adapter.add(cardModel);
					}
				}
				
				loadingVentouraProgressbar.setVisibility(View.GONE);
				vCardContainer.setAdapter(adapter);
			}

		}
	}

	/**
	 * after the user done a swipe, following actions will be applied
	 */
	private void handleSwipedAction(boolean likeOrNot, JSONVentoura ventoura) {

		long likeOrNotLong = 1;
		if (likeOrNot) {
			likeOrNotLong = 0;
		}
		LikeOrNotPeopleTask likeOrNotTask = new LikeOrNotPeopleTask();
		likeOrNotTask.execute(likeOrNotLong, (long) ventoura.getUserRole()
				.getNumVal(), ventoura.getId());
	}

	/**
	 * Network tasks for like or dislike a person
	 */
	private class LikeOrNotPeopleTask extends AsyncTask<Long, Void, Void> {

		@Override
		protected Void doInBackground(Long... params) {

			// first param is 0 means like
			boolean likeOrNot = params[0] == 0 ? true : false;

			// second param is 0 means JudgeGuide, otherwise judge traveller
			// TODO if it is a match.. notice the user
			if (params[1] == UserRole.GUIDE.getNumVal()) {
				ventouraService.travellerJudgeGuide(travellerId, likeOrNot,
						params[2]);
			} else {
				ventouraService.travellerJudgeTraveller(travellerId, likeOrNot,
						params[2]);
			}
			return null;
		}

	}

	/**
	 * like or not like onclick listener
	 */
	private class LikeOrNotLikeOnclickListener implements OnClickListener {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_ventouring_yes:
				vCardContainer.likeFromOutPut();
				break;
			case R.id.btn_ventouring_no:
				vCardContainer.dislikeFromOutPut();
				break;
			}
		}
	}

	private void checkVentouraDetail(JSONVentoura ventoura) {

		Intent intent = new Intent();
		intent.putExtra("ventoura", ventoura);
		if (ventoura.getUserRole() == UserRole.GUIDE) {
			intent.setClass(activity, GuideDetailVentouraActivity.class);
		} else {
			intent.setClass(activity, TravellerDetailVentouraActivity.class);
		}
		
		startActivityForResult(intent, REQUEST_CHECK_VENTOURA_DETAIL);
	}

}
