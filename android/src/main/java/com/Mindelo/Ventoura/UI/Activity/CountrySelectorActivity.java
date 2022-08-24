package com.Mindelo.Ventoura.UI.Activity;



import java.util.List;

import com.Mindelo.Ventoura.Entity.Country;
import com.Mindelo.Ventoura.Ghost.IService.ICountryService;
import com.Mindelo.Ventoura.Ghost.Service.CountryService;
import com.Mindelo.Ventoura.UI.Adapter.CountrySelectorAdapter;
import com.Mindelo.Ventoura.UI.View.QuickScroll;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;

public class CountrySelectorActivity  extends FragmentActivity implements
OnItemClickListener{

	private ListView lvCountry;
	private List<Country> countrylist;
	private ICountryService countryService;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_country_selector);
		
		final ViewGroup root = (ViewGroup)findViewById(R.id.country_selector_layout);
		
		lvCountry = (ListView)findViewById(R.id.lv_country_selector);
		lvCountry.setOnItemClickListener(this);
		
		

		countryService = new CountryService(this);		
		countrylist = countryService.getAllCountryAlphabetically();
		
		
		CountrySelectorAdapter countryAdapter = new CountrySelectorAdapter(this, countrylist);
		lvCountry.setAdapter(countryAdapter);
		
		final QuickScroll fastTrack = (QuickScroll)findViewById(R.id.quickscroll);
		fastTrack.init(QuickScroll.TYPE_POPUP, lvCountry, countryAdapter, QuickScroll.STYLE_NONE);
		fastTrack.setFixedSize(2);
		fastTrack.setPopupColor(QuickScroll.BLUE_LIGHT, QuickScroll.BLUE_LIGHT_SEMITRANSPARENT, 1, Color.WHITE, 1);
		
		root.addView(createAlphabetTrack());
		setContentView(root);
	}
	
	private View createAlphabetTrack() {
		final LinearLayout layout = new LinearLayout(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) (30 * getResources().getDisplayMetrics().density), LayoutParams.MATCH_PARENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params.addRule(RelativeLayout.BELOW, R.id.country_selector_titlebar);
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent it = new Intent();
		it.putExtra("CountrySelected", countrylist.get(position));
		setResult(RESULT_OK, it);
		finish();
	}
	
	/*
	 * The back button click to switch the menu and content
	 */
	public void btnBackOnClick(View view) {
		onBackPressed();
	}
}
