package com.Mindelo.Ventoura.UI.Activity;

import com.Mindelo.Ventoura.Constant.BroadcastConstant;
import com.Mindelo.Ventoura.Constant.IMConstant;
import com.Mindelo.Ventoura.Constant.VentouraConstant;
import com.Mindelo.Ventoura.UI.View.RangeSeekBar;
import com.Mindelo.Ventoura.UI.View.RangeSeekBar.OnRangeSeekBarChangeListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class TravellerMessageFilterActivity extends Activity {

	/*
	 * utility instances
	 */
	private SharedPreferences sharedPre;

	/*
	 * global variables
	 */
	private final int MIN_AGE = 0;
	private final int MAX_AGE = 120;

	private int userPreMinAge = 0;
	private int userPreMaxAge = 120;
	private boolean userPreTraveller = true, userPreLocal = true,
			userPreFemale = true, userPreMale = true;

	/*
	 * views
	 */
	private TextView ageMinTV, ageMaxTV;
	private CheckBox preTravellerCB, preLocalCB, preFemaleCB, preMaleCB;
	
	private Button backBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_traveller_message_filter);

		sharedPre = getSharedPreferences(
				VentouraConstant.SHARED_PREFERENCE_VENTOURA,
				Context.MODE_PRIVATE);
		userPreMinAge = sharedPre.getInt(
				VentouraConstant.PRE_MESSAGE_FILTER_MIN_AGE, MIN_AGE);
		userPreMaxAge = sharedPre.getInt(
				VentouraConstant.PRE_MESSAGE_FILTER_MAX_AGE, MAX_AGE);
		userPreTraveller = sharedPre.getBoolean(
				VentouraConstant.PRE_MESSAGE_FILTER_USER_ROLE_TRAVELLER, true);
		userPreLocal = sharedPre.getBoolean(
				VentouraConstant.PRE_MESSAGE_FILTER_USER_ROLE_LOCAL, true);
		userPreFemale = sharedPre.getBoolean(
				VentouraConstant.PRE_MESSAGE_FILTER_GENDER_FEMALE, true);
		userPreMale = sharedPre.getBoolean(
				VentouraConstant.PRE_MESSAGE_FILTER_GENDER_MALE, true);

		/*
		 * initialize views
		 */
		ageMinTV = (TextView) findViewById(R.id.filter_age_min_tv);
		ageMaxTV = (TextView) findViewById(R.id.filter_age_max_tv);
		preTravellerCB = (CheckBox) findViewById(R.id.mfilter_traveller_switch);
		preLocalCB = (CheckBox) findViewById(R.id.mfilter_locals_switch);
		preFemaleCB = (CheckBox) findViewById(R.id.mfilter_female_switch);
		preMaleCB = (CheckBox) findViewById(R.id.mfilter_male_switch);
		
		ageMinTV.setText(userPreMinAge + "");
		ageMaxTV.setText(userPreMaxAge + "");
		preTravellerCB.setChecked(userPreTraveller);
		preLocalCB.setChecked(userPreLocal);
		preFemaleCB.setChecked(userPreFemale);
		preMaleCB.setChecked(userPreMale);
		

		RangeSeekBar<Integer> ageSeekBar = new RangeSeekBar<Integer>(
				MIN_AGE, MAX_AGE, this);
		ageSeekBar.setSelectedMinValue(userPreMinAge);
		ageSeekBar.setSelectedMaxValue(userPreMaxAge);
		ageSeekBar
				.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {

					@Override
					public void onRangeSeekBarValuesChanged(
							RangeSeekBar<?> bar, Integer minValue,
							Integer maxValue) {
						ageMinTV.setText(String.valueOf(minValue));
						ageMaxTV.setText(String.valueOf(maxValue));
						userPreMinAge = minValue;
						userPreMaxAge = maxValue;
					}
				});
		ViewGroup ageSeekBarLayout = (ViewGroup) findViewById(R.id.filter_age_rangeseekbar);
		ageSeekBarLayout.addView(ageSeekBar);

		backBtn = (Button) findViewById(R.id.btn_back);
		backBtn.setText("Done");
	}
	

	@Override
	protected void onPause() {
		super.onPause();
		Editor editor = sharedPre.edit();
		editor.putInt(VentouraConstant.PRE_MESSAGE_FILTER_MIN_AGE, userPreMinAge);
		editor.putInt(VentouraConstant.PRE_MESSAGE_FILTER_MAX_AGE, userPreMaxAge);
		
		editor.putBoolean(VentouraConstant.PRE_MESSAGE_FILTER_USER_ROLE_LOCAL, preLocalCB.isChecked());
		editor.putBoolean(VentouraConstant.PRE_MESSAGE_FILTER_USER_ROLE_TRAVELLER, preTravellerCB.isChecked());
		editor.putBoolean(VentouraConstant.PRE_MESSAGE_FILTER_GENDER_MALE, preMaleCB.isChecked());
		editor.putBoolean(VentouraConstant.PRE_MESSAGE_FILTER_GENDER_FEMALE, preFemaleCB.isChecked());
		
		editor.commit();
		
		// send the broad cast that the message filter was changed
		Intent intent = new Intent(
				BroadcastConstant.USER_MESSAGE_FILTER_UPDATED_ACTION);
		sendBroadcast(intent);
	}
	
	

	/*
	 * go back button
	 */
	public void btnBackOnClick(View view) {
		onBackPressed();
	}

}
