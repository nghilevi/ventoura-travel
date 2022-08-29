package com.Mindelo.Ventoura.UI.Activity;

import java.lang.reflect.Field;

import com.Mindelo.Ventoura.Constant.BroadcastConstant;
import com.Mindelo.Ventoura.UI.Adapter.ChooseRoleSlidingFragmentAdapter;
import com.Mindelo.Ventoura.UI.View.ViewPageScrollSpeedScroller;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.animation.AccelerateInterpolator;

public class LoginChooseRoleActivity extends FragmentActivity {

	private final String TAG = "LoginChooseRoleActivity";

	/*
	 * views
	 */
	private ChooseRoleSlidingFragmentAdapter doubleLayerAdapter;
	private ViewPageScrollSpeedScroller mScroller;
	private int viewPagerSlideSpeed = 1000;
	ViewPager viewPager;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_login_choose_role);
		
		/*
		 * sliding views to choose role
		 */
		doubleLayerAdapter = new ChooseRoleSlidingFragmentAdapter(getSupportFragmentManager());
		viewPager = (ViewPager) this.findViewById(R.id.viewpage);
		viewPager.setAdapter(doubleLayerAdapter);
		initViewPageScroll();
	}

	
	@Override
	protected void onStart() {
		super.onStart();
		//register the broadcast for viewpager refresh
		IntentFilter filter=new IntentFilter();
		filter.addAction(BroadcastConstant.CHOOSE_ROLE_REFRESH_VIEWPAGE_ACTION);
		this.registerReceiver(refreshViewPagerReceiver, filter);
	}

	@Override
	protected void onStop() {
		super.onStop();
		//unregister the broadcast for viewpager refresh when stop the activity
		this.unregisterReceiver(refreshViewPagerReceiver);
	}

	private void initViewPageScroll() {
		try {
			Field mField  = null;
			mField  = ViewPager.class.getDeclaredField("mScroller");
			mField .setAccessible(true);
			mScroller = new ViewPageScrollSpeedScroller(viewPager.getContext(), new AccelerateInterpolator());
			mScroller.setmDuration(viewPagerSlideSpeed);
			mField.set(viewPager, mScroller);
		} catch (Exception e) {
			e.printStackTrace();
		} 

	}
	
	private BroadcastReceiver refreshViewPagerReceiver =new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			//get the newer current page of the page and refresh the view page
			if(intent.getAction().equals(BroadcastConstant.CHOOSE_ROLE_REFRESH_VIEWPAGE_ACTION)){
				int pagenum=intent.getIntExtra("pageNum", 0);
				viewPager.setCurrentItem(pagenum);
			}
		}};
}
