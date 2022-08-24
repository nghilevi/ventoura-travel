package com.Mindelo.Ventoura.UI.Activity;

import lombok.Getter;
import lombok.Setter;

import com.Mindelo.Ventoura.AndroidService.IMListenerService;
import com.Mindelo.Ventoura.Constant.IMConstant;
import com.Mindelo.Ventoura.Constant.VentouraConstant;
import com.Mindelo.Ventoura.Enum.UserRole;
import com.Mindelo.Ventoura.Ghost.IService.IChattingHistoryService;
import com.Mindelo.Ventoura.Ghost.IService.ICityService;
import com.Mindelo.Ventoura.Ghost.IService.IGalleryService;
import com.Mindelo.Ventoura.Ghost.IService.IGuideService;
import com.Mindelo.Ventoura.Ghost.IService.IMatchesService;
import com.Mindelo.Ventoura.Ghost.IService.ITravellerService;
import com.Mindelo.Ventoura.Ghost.Service.ChattingHistoryService;
import com.Mindelo.Ventoura.Ghost.Service.CityService;
import com.Mindelo.Ventoura.Ghost.Service.GalleryService;
import com.Mindelo.Ventoura.Ghost.Service.GuideService;
import com.Mindelo.Ventoura.Ghost.Service.MatchesService;
import com.Mindelo.Ventoura.Ghost.Service.TravellerService;
import com.Mindelo.Ventoura.UI.Fragment.GuideMenuListFragment;
import com.Mindelo.Ventoura.Util.AndroidServiceUtil;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

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

public class GuidePortalActivity extends SlidingFragmentActivity {

	private static final String TAG = "com.Mindelo.Ventoura.UI.Activity.GuidePortalActivity";
	public static boolean active;
	private long backPressedTime = 0;    // used by onBackPressed()

	/*
	 * Utility Instances
	 */

	@Getter
	private ICityService cityService;
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
	private long guideId;
	@Getter
	private String userName;

	@Getter
	@Setter
	private long chattingPartnerId;
	@Getter
	@Setter
	private UserRole chattingPartnerRole;

	public static final int GUIDE_PROFILE_FRAGMENT = 0;
	public static final int GUIDE_VENTOURING_FRAGMENT = 1;
	public static final int GUIDE_MESSAGES_FRAGMENT = 2;
	public static final int GUIDE_BOOKINGS_FRAGMENT = 3;
	public static final int GUIDE_SETTINGS_FRAGMENT = 4;

	private static final int FRAGMENT_COUNT = GUIDE_SETTINGS_FRAGMENT + 1;

	private Fragment[] fragments = new Fragment[FRAGMENT_COUNT];


	@Getter
	private GuideMenuListFragment menuFragment;

	private SlidingMenu menu;

	private FragmentManager fm;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		Log.i(TAG, "onCreate()");
		super.onCreate(savedInstanceState);

		/*
		 * Initiate Utility Instances
		 */
		matchesService = new MatchesService(this);
		guideService = new GuideService(this);
		galleryService = new GalleryService(this);
		travellerService = new TravellerService(this);
		chattingHistoryService = new ChattingHistoryService(this);
		cityService = new CityService(this);
		
		sharedPre = getSharedPreferences(
				VentouraConstant.SHARED_PREFERENCE_VENTOURA,
				Context.MODE_PRIVATE);
		guideId = sharedPre.getLong(VentouraConstant.PRE_USER_ID_IN_SERVER, -1);
		userName = sharedPre
				.getString(VentouraConstant.PRE_USER_FIRST_NAME, "Noname");

		this.setContentView(R.layout.activity_guide_portal);
		initFragment(savedInstanceState);

		/**
		 * start the IM Service running in the background if it is not running
		 */
		if(!AndroidServiceUtil.isMyServiceRunning(IMListenerService.class, this)){
			Intent intent = new Intent(this, IMListenerService.class);
			intent.putExtra(IMConstant.USER_NAME, "g_" + guideId);
			intent.putExtra(IMConstant.USER_PASSWORD,
					getString(R.string.user_im_token));
			startService(intent);
		}
		
	}

	private void initFragment(Bundle savedInstanceState) {

		menu = getSlidingMenu();
		fm = getSupportFragmentManager();

		fragments[GUIDE_PROFILE_FRAGMENT] = fm
				.findFragmentById(R.id.fragment_guide_profile);
		fragments[GUIDE_VENTOURING_FRAGMENT] = fm
				.findFragmentById(R.id.fragment_guide_ventouring);
		fragments[GUIDE_MESSAGES_FRAGMENT] = fm
				.findFragmentById(R.id.fragment_guide_message);
		fragments[GUIDE_BOOKINGS_FRAGMENT] = fm
				.findFragmentById(R.id.fragment_guide_bookings);
		fragments[GUIDE_SETTINGS_FRAGMENT] = fm
				.findFragmentById(R.id.fragment_guide_settings);

		setBehindContentView(R.layout.fragment_menu);

		menuFragment = new GuideMenuListFragment();
		fm.beginTransaction()
				.replace(R.id.menu_frame, menuFragment).commit();

		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.effects_shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);


		if (IMListenerService.isImserviceStartPortalActivity() == true) {
			IMListenerService.setImserviceStartPortalActivity(false);
			switchContent(GUIDE_MESSAGES_FRAGMENT);
		} else {
			switchContent(GUIDE_VENTOURING_FRAGMENT);
		}

	}

	public void switchContent(int fragmentIndex) {
		showFragment(fragmentIndex, false);
		menu.showContent(); // show the main content when clicked
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
	        if (t - backPressedTime > 2000) {    // 2 secs
	            backPressedTime = t;
	            Toast.makeText(this, "Press back again to go home",
	                                Toast.LENGTH_SHORT).show();
	        } else {    // this guy is serious
	            // clean up
	            super.onBackPressed();       // bye
	        }
		}
	}

	@Override
	public void onStart() {
		Log.i(TAG, "onStart");
		super.onStart();
		active = true;
	}

	@Override
	public void onStop() {
		Log.i(TAG, "onStop");
		super.onStop();
		active = false;
	}

	@Override
	protected void onDestroy() {
		Log.i(TAG, "onDestroy");
		super.onDestroy();
		active = false;
	}

	@Override
	protected void onPause() {
		Log.i(TAG, "onPause");
		super.onPause();
	}

	@Override
	protected void onResume() {
		Log.i(TAG, "onResume");
		super.onResume();
	}


	private void showFragment(int fragmentIndex, boolean addToBackStack) {

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
