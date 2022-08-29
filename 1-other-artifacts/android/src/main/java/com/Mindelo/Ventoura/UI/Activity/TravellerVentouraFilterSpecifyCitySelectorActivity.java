package com.Mindelo.Ventoura.UI.Activity;



import java.util.List;

import com.Mindelo.Ventoura.Entity.City;
import com.Mindelo.Ventoura.Ghost.IService.ICityService;
import com.Mindelo.Ventoura.Ghost.Service.CityService;
import com.Mindelo.Ventoura.UI.Adapter.CitySelectorAdapter;
import com.Mindelo.Ventoura.UI.Adapter.FilterCitySelectorAdapter;
import com.Mindelo.Ventoura.UI.View.QuickScroll;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

public class TravellerVentouraFilterSpecifyCitySelectorActivity  extends FragmentActivity implements
OnItemClickListener{

	private ListView lvCity;
	private List<City> citylist;
	private ICityService cityService;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_traveller_ventoura_filter_specify_city_selector);
		
		final ViewGroup root = (ViewGroup)findViewById(R.id.filter_city_selector_layout);
		
		lvCity = (ListView)findViewById(R.id.lv_filter_city_selector);
		lvCity.setOnItemClickListener(this);
		
		

		cityService = new CityService(this);		
		citylist = cityService.getAllCityAlphabetically();
		
		
		FilterCitySelectorAdapter cityAdapter = new FilterCitySelectorAdapter(this, citylist);
		lvCity.setAdapter(cityAdapter);
		
		final QuickScroll fastTrack = (QuickScroll)findViewById(R.id.quickscroll);
		fastTrack.init(QuickScroll.TYPE_POPUP, lvCity, cityAdapter, QuickScroll.STYLE_NONE);
		fastTrack.setFixedSize(2);
		fastTrack.setPopupColor(QuickScroll.BLUE_LIGHT, QuickScroll.BLUE_LIGHT_SEMITRANSPARENT, 1, Color.WHITE, 1);
		
		root.addView(createAlphabetTrack());
		setContentView(root);
	}
	
	private View createAlphabetTrack() {
		final LinearLayout layout = new LinearLayout(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (30 * getResources().getDisplayMetrics().density), LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params.addRule(RelativeLayout.BELOW, R.id.city_selector_titlebar);
		layout.setLayoutParams(params);
		layout.setOrientation(LinearLayout.VERTICAL);

		final LinearLayout.LayoutParams textparams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		textparams.weight = 1;
		final int height = getResources().getDisplayMetrics().heightPixels;
		int iterate = 0;
		if (height >= 1024){
			iterate = 1; layout.setWeightSum(26);
		} else {
			iterate = 2; layout.setWeightSum(13);
		}
		for (char character = 'A'; character <= 'Z'; character+=iterate) {
			final TextView textview = new TextView(this);
			textview.setLayoutParams(textparams);
			textview.setGravity(Gravity.CENTER_HORIZONTAL);
			textview.setText(Character.toString(character));
			layout.addView(textview);
		}

		return layout;
	}
	
	/*
	 * select the item, and set the checkbox
	 * 
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		CheckBox cb = (CheckBox)view.findViewById(R.id.filter_city_selector_item_checkbox);
		cb.setChecked(!cb.isChecked());
		Toast.makeText(this, citylist.get(position) + ":" + String.valueOf(cb.isChecked()), Toast.LENGTH_SHORT).show();
	}
	
	/*
	 * The back button click to switch the menu and content
	 */
	public void btnBackOnClick(View view) {
		onBackPressed();
	}
}
