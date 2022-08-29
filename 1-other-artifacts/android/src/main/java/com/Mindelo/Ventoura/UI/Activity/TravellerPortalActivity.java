package com.Mindelo.Ventoura.UI.Activity;

import lombok.Getter;

import com.Mindelo.Ventoura.AndroidService.IMListenerService;
import com.Mindelo.Ventoura.Constant.IMConstant;
import com.Mindelo.Ventoura.Constant.VentouraConstant;
import com.Mindelo.Ventoura.Ghost.IService.IChattingHistoryService;
import com.Mindelo.Ventoura.Ghost.IService.ICountryService;
import com.Mindelo.Ventoura.Ghost.IService.IGalleryService;
import com.Mindelo.Ventoura.Ghost.IService.IGuideService;
import com.Mindelo.Ventoura.Ghost.IService.IMatchesService;
import com.Mindelo.Ventoura.Ghost.IService.ITravellerService;
import com.Mindelo.Ventoura.Ghost.Service.ChattingHistoryService;
import com.Mindelo.Ventoura.Ghost.Service.CountryService;
import com.Mindelo.Ventoura.Ghost.Service.GalleryService;
import com.Mindelo.Ventoura.Ghost.Service.GuideService;
import com.Mindelo.Ventoura.Ghost.Service.MatchesService;
import com.Mindelo.Ventoura.Ghost.Service.TravellerService;
import com.Mindelo.Ventoura.UI.Fragment.TravellerMenuListFragment;
import com.Mindelo.Ventoura.Util.AndroidServiceUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class TravellerPortalActivity extends SlidingFragmentActivity {

	private static final String TAG = "com.Mindelo.Ventoura.UI.Activity.TravellerPortalActivity";

	public static boolean active;

	private long backPressedTime = 0; // used by onBackPressed()

	/*
	 * progress bar shared by all fragments
	 */
	@Getter
	private ProgressDialog progressDialog;

	/*
	 * Utility Instances
	 */

	
	@Getter
	private ICountryService countryService;
	@Getter
	private SharedPreferences sharedPre;
	@Getter
	private IGuideService guideService;
	@Getter
	private ITravellerService travellerService;
	@Getter
	private IGalleryService galleryService;
	@Getter
	private IMatchesService matchesService;
	@Getter
	private IChattingHistoryService chattingHistoryService;
	@Getter
	private long travellerId;
	@Getter
	private String userName;

	/*
	 * Fragment Indexes
	 */
	public static final int TRAVELLER_PROFILE_FRAGMENT = 0;
	public static final int TRAVELLER_VENTOURING_FRAGMENT = 1;
	public static final int TRAVELLER_MESSAGES_FRAGMENT = 2;
	public static final int TRAVELLER_MY_TRIP_FRAGMENT = 3;
	public static final int TRAVELLER_PROMOTIOIN_FRAGMENT = 4;
	public static final int TRAVELLER_SETTINGS_FRAGMENT = 5;

	private static final int FRAGMENT_COUNT = TRAVELLER_SETTINGS_FRAGMENT + 1;

	private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];

	// private Fragment[] actionFragments = new Fragment[FRAGMENT_COUNT];

	@Getter
	private TravellerMenuListFragment menuFragment;

	private SlidingMenu menu;

	private FragmentManager fm;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		Log.i(TAG, "onCreate()");
		super.onCreate(savedInstanceState);

		// the city service should be initialized before its fragment uses it
		countryService = new CountryService(this);
		guideService = new GuideService(this);
		travellerService = new TravellerService(this);
		galleryService = new GalleryService(this);
		matchesService = new MatchesService(this);
		chattingHistoryService = new ChattingHistoryService(this);
		/*
		 * initiate the progress bar but not show it yet
		 */
		progressDialog = new ProgressDialog(this);

		/*
		 * Initiate Utility Instances
		 */
		sharedPre = getSharedPreferences(
				VentouraConstant.SHARED_PREFERENCE_VENTOURA,
				Context.MODE_PRIVATE);
		travellerId = sharedPre.getLong(VentouraConstant.PRE_USER_ID_IN_SERVER,
				-1);
		userName = sharedPre.getString(VentouraConstant.PRE_USER_FIRST_NAME,
				"Noname");

		/*
		 * load the fragment after the utility instances has been initialized
		 */
		this.setContentView(R.layout.activity_traveller_portal);
		initSlidingMenu(savedInstanceState);

		/**
		 * start the IM Service running in the background if it is not running
		 */
		if (!AndroidServiceUtil.isMyServiceRunning(IMListenerService.class,
				this)) {
			Intent intent = new Intent(this, IMListenerService.class);
			intent.putExtra(IMConstant.USER_NAME, "t_" + travellerId);
			intent.putExtra(IMConstant.USER_PASSWORD,
					getString(R.string.user_im_token));
			startService(intent);
		}
	}

	private void initSlidingMenu(Bundle savedInstanceState) {

		menu = getSlidingMenu();
		fm = getSupportFragmentManager();

		fragments[TRAVELLER_PROFILE_FRAGMENT] = fm
				.findFragmentById(R.id.fragment_traveller_profile);
		fragments[TRAVELLER_VENTOURING_FRAGMENT] = fm
				.findFragmentById(R.id.fragment_traveller_ventouring);
		fragments[TRAVELLER_MESSAGES_FRAGMENT] = fm
				.findFragmentById(R.id.fragment_traveller_message);
		fragments[TRAVELLER_MY_TRIP_FRAGMENT] = fm
				.findFragmentById(R.id.fragment_traveller_mytrip);
		fragments[TRAVELLER_PROMOTIOIN_FRAGMENT] = fm
				.findFragmentById(R.id.fragment_traveller_promotion);
		fragments[TRAVELLER_SETTINGS_FRAGMENT] = fm
				.findFragmentById(R.id.fragment_traveller_settings);

		setBehindContentView(R.layout.fragment_menu);

		menuFragment = new TravellerMenuListFragment();

		fm.beginTransaction().replace(R.id.menu_frame, menuFragment).commit();

		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.effects_shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);

		if (IMListenerService.isImserviceStartPortalActivity() == true) {
			IMListenerService.setImserviceStartPortalActivity(false);
			switchContent(TRAVELLER_MESSAGES_FRAGMENT);
		} else {
			switchContent(TRAVELLER_VENTOURING_FRAGMENT);
		}

	}

	public void switchContent(int fragmentIndex) {
		showFragment(fragmentIndex, false);
		menu.showContent(); // show the main content when clicked
		Log.i(TAG, "fragments" + fragmentIndex);
	}

	/*
	 * The menu button click to switch the menu and content
	 */
	public void btnMenuOnClick(View view) {
		if (menu.isMenuShowing()) {
			menu.showContent();
		} else {
			menu.showMenu();
		}
	}

	@Override
	public void onBackPressed() {

		if (menu.isMenuShowing()) {
			menu.showContent();
		} else {
			long t = System.currentTimeMillis();
			if (t - backPressedTime > 2000) { // 2 secs
				backPressedTime = t;
				Toast.makeText(this, "Press back again to go home",
						Toast.LENGTH_SHORT).show();
			} else { // this guy is serious
				// clean up
				super.onBackPressed(); // bye
			}
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		active = true;
	}

	@Override
	public void onStop() {
		super.onStop();
		active = false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		active = false;
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
	 * show a fragment according the the fragment index
	 */
	public void showFragment(int fragmentIndex, boolean addToBackStack) {

		FragmentManager fm = getSupportFragmentManager();

		FragmentTransaction transaction = fm.beginTransaction();
		for (int i = 0; i < fragments.length; i++) {
			if (i == fragmentIndex) {
				transaction.show(fragments[i]);

			} else {
				transaction.hide(fragments[i]);
			}
		}

		if (addToBackStack) {
			transaction.addToBackStack(null);
		}
		transaction.commit();
	}

}
