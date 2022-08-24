package com.Mindelo.Ventoura.UI.Fragment;

import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Enum.UserRole;
import com.Mindelo.Ventoura.Ghost.IService.IGuideService;
import com.Mindelo.Ventoura.Ghost.IService.ITravellerService;
import com.Mindelo.Ventoura.Ghost.IService.IVentouraService;
import com.Mindelo.Ventoura.Ghost.Service.CityService;
import com.Mindelo.Ventoura.Ghost.Service.CountryService;
import com.Mindelo.Ventoura.Ghost.Service.VentouraService;
import com.Mindelo.Ventoura.JSONEntity.JSONVentoura;
import com.Mindelo.Ventoura.JSONEntity.JSONVentouraList;
import com.Mindelo.Ventoura.Model.CardModel;
import com.Mindelo.Ventoura.UI.Activity.GuidePortalActivity;
import com.Mindelo.Ventoura.UI.Activity.GuideVentouringFilterActivity;
import com.Mindelo.Ventoura.UI.Activity.R;
import com.Mindelo.Ventoura.UI.Activity.TravellerDetailVentouraActivity;
import com.Mindelo.Ventoura.UI.Activity.TravellerPortalActivity;
import com.Mindelo.Ventoura.UI.Activity.TravellerVentouringFilterActivity;
import com.Mindelo.Ventoura.UI.Adapter.VentouraCardStackAdapter;
import com.Mindelo.Ventoura.UI.View.PopupActionItem;
import com.Mindelo.Ventoura.UI.View.CardContainerView;
import com.Mindelo.Ventoura.UI.View.TopPopupMenuAction;
import com.Mindelo.Ventoura.UI.View.RoundedImageView;
import com.Mindelo.Ventoura.Util.ImageUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class GuideVentouringFragment extends Fragment {

	private static final String TAG = "GuideVentouringFragment";

	/*
	 * progress bar
	 */
	private ProgressBar loadingVentouraProgressbar;

	/*
	 * utility instances
	 */
	private GuidePortalActivity activity;
	private IVentouraService ventouraService;
	private ITravellerService travellerService;
	private IGuideService guideService;
	private long guideId;

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity = (GuidePortalActivity) getActivity();
		ventouraService = new VentouraService();
		guideService = activity.getGuideService();
		guideId = activity.getGuideId();
		travellerService = activity.getTravellerService();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);

		View view = inflater.inflate(R.layout.fragment_guide_ventouring,
				container, false);
		
		
		loadingVentouraProgressbar = (ProgressBar) view.findViewById(R.id.ventoura_loading_progress_bar);
		loadingVentouraProgressbar.setVisibility(View.GONE);

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
		Button optionButton = (Button) view.findViewById(R.id.btn_action);
		optionButton.setText("Filter");
		optionButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(),
						GuideVentouringFilterActivity.class));
			}
		});

		// fetch first ventoura package
		LoadVentouraTask loadVentouraTask = new LoadVentouraTask();
		loadVentouraTask.execute();
		
		return view;
	}

	/**
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
				ventouraList = ventouraService.getGuideVentouraList(guideId)
						.getVentouraList();
				ventouraPortalImagesMap = ventouraService
						.loadVentouraImages(ventouraList);
				return null;

			} catch (Exception e) {
				e.printStackTrace();
				Log.e(TAG, "Guide Ventoura Download Error");
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

					final JSONVentoura ventouraTemp = ventouraList.get(index);
					String ventouraPortalImageFile = "t_"
							+ ventouraTemp.getId() + "_.png";

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
										handleSwipedAction(true,
												ventouraTemp.getId());
									}

									@Override
									public void onDislike() {
										handleSwipedAction(false,
												ventouraTemp.getId());
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
	private void handleSwipedAction(boolean likeOrNot, long travellerId) {

		long likeOrNotLong = 1;
		if (likeOrNot) {
			likeOrNotLong = 0;
		}
		LikeOrNotPeopleTask likeOrNotTask = new LikeOrNotPeopleTask();
		likeOrNotTask.execute(likeOrNotLong, travellerId);
	}

	/**
	 * Network tasks for like or dislike a person
	 */
	private class LikeOrNotPeopleTask extends AsyncTask<Long, Void, Void> {

		@Override
		protected Void doInBackground(Long... params) {

			// first param is 0 means like
			boolean likeOrNot = params[0] == 0 ? true : false;

			// second param is the travellerIt which is judged by this guide
			// TODO if it is a match.. notice the user
			ventouraService.guideJudgeTraveller(guideId, likeOrNot, params[1]);
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
		Intent intent = new Intent(activity,
				TravellerDetailVentouraActivity.class);
		intent.putExtra("ventoura", ventoura);
		startActivity(intent);
	}

}
