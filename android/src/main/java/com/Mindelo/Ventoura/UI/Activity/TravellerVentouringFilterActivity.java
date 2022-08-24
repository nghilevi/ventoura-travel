package com.Mindelo.Ventoura.UI.Activity;

import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Constant.VentouraConstant;
import com.Mindelo.Ventoura.Enum.Gender;
import com.Mindelo.Ventoura.Enum.UserRole;
import com.Mindelo.Ventoura.Ghost.IService.IVFunctionSettingsService;
import com.Mindelo.Ventoura.Ghost.Service.VFunctionSettingsService;
import com.Mindelo.Ventoura.JSONEntity.JSONGuideVFunctionSettings;
import com.Mindelo.Ventoura.JSONEntity.JSONTravellerVFunctionSettings;
import com.Mindelo.Ventoura.UI.View.RangeSeekBar;
import com.Mindelo.Ventoura.UI.View.RangeSeekBar.OnRangeSeekBarChangeListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class TravellerVentouringFilterActivity extends Activity {

	private final String TAG = "TravellerVentouringFilterActivity";

	/*
	 * utility instances
	 */
	private IVFunctionSettingsService vFunctionSettingService;
	private SharedPreferences sharedPre;
	private long travellerId;

	/*
	 * global variables
	 */
	private JSONTravellerVFunctionSettings vFunctionSettings;

	/*
	 * views
	 */
	private TextView ageMinTV, ageMaxTV;
	private TextView priceMinTV, priceMaxTV;
	private Button specifyButton;
	private CheckBox specifyCheckBox;
	private RadioGroup genderRG, typeRG;
	private RangeSeekBar<Integer> ageSeekBar;
	private RangeSeekBar<Integer> priceSeekBar;

	private Button backBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_traveller_ventoura_filter);

		/*
		 * initial utility instances
		 */
		vFunctionSettingService = new VFunctionSettingsService(this);

		sharedPre = getSharedPreferences(
				VentouraConstant.SHARED_PREFERENCE_VENTOURA,
				Context.MODE_PRIVATE);
		travellerId = sharedPre.getLong(VentouraConstant.PRE_USER_ID_IN_SERVER,
				-1);

		vFunctionSettings = new JSONTravellerVFunctionSettings();
		vFunctionSettings.setTravellerId(travellerId);

		/*
		 * initiate views
		 */
		genderRG = (RadioGroup) findViewById(R.id.filter_gender_radiogroup);
		genderRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// 获取变更后的选中项的ID
				int radioButtonId = arg0.getCheckedRadioButtonId();
				// 根据ID获取RadioButton的实例
				RadioButton rb = (RadioButton) findViewById(radioButtonId);

				switch (radioButtonId) {
				case R.id.filter_gender_both_checkbox:
					vFunctionSettings.setPreferedGender(Gender.BOTH);
					break;
				case R.id.filter_gender_female_checkbox:
					vFunctionSettings.setPreferedGender(Gender.FEMALE);
					break;
				case R.id.filter_gender_male_checkbox:
					vFunctionSettings.setPreferedGender(Gender.MALE);
					break;
				}
			}
		});

		typeRG = (RadioGroup) findViewById(R.id.filter_type_radiogroup);
		typeRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// 获取变更后的选中项的ID
				int radioButtonId = arg0.getCheckedRadioButtonId();
				// 根据ID获取RadioButton的实例
				RadioButton rb = (RadioButton) findViewById(radioButtonId);
				switch (radioButtonId) {
				case R.id.filter_type_both_checkbox:
					vFunctionSettings.setPreferedUserRole(UserRole.BOTH);
					break;
				case R.id.filter_type_traveller_checkbox:
					vFunctionSettings.setPreferedUserRole(UserRole.TRAVELLER);
					break;
				case R.id.filter_type_local_checkbox:
					vFunctionSettings.setPreferedUserRole(UserRole.GUIDE);
					break;
				}
			}
		});

		ageMinTV = (TextView) findViewById(R.id.filter_age_min_tv);
		ageMaxTV = (TextView) findViewById(R.id.filter_age_max_tv);
		priceMinTV = (TextView) findViewById(R.id.filter_price_min_tv);
		priceMaxTV = (TextView) findViewById(R.id.filter_price_max_tv);

		ageSeekBar = new RangeSeekBar<Integer>(ConfigurationConstant.VENTOURA_MIN_AGE,
				ConfigurationConstant.VENTOURA_MAX_AGE, this);
		ageSeekBar
				.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {

					@Override
					public void onRangeSeekBarValuesChanged(
							RangeSeekBar<?> bar, Integer minValue,
							Integer maxValue) {
						ageMinTV.setText(String.valueOf(minValue));
						ageMaxTV.setText(String.valueOf(maxValue));
						vFunctionSettings.setMaxAge(maxValue);
						vFunctionSettings.setMiniAge(minValue);
					}
				});
		ViewGroup ageSeekBarLayout = (ViewGroup) findViewById(R.id.filter_age_rangeseekbar);
		ageSeekBarLayout.addView(ageSeekBar);

		priceSeekBar = new RangeSeekBar<Integer>(
				ConfigurationConstant.VENTOURA_MIN_PRICE, ConfigurationConstant.VENTOURA_MAX_PRICE, this);
		priceSeekBar
				.setOnRangeSeekBarChangeListener(new OnRangeSeekBarChangeListener<Integer>() {

					@Override
					public void onRangeSeekBarValuesChanged(
							RangeSeekBar<?> bar, Integer minValue,
							Integer maxValue) {
						priceMinTV.setText(VentouraConstant.EURO_SYMBOL
								+ String.valueOf(minValue));
						priceMaxTV.setText(VentouraConstant.EURO_SYMBOL
								+ String.valueOf(maxValue));
						vFunctionSettings.setMiniPrice(minValue);
						vFunctionSettings.setMaxPrice(maxValue);
					}
				});
		ViewGroup priceSeekBarLayout = (ViewGroup) findViewById(R.id.filter_price_rangeseekbar);
		priceSeekBarLayout.addView(priceSeekBar);

		specifyButton = (Button) findViewById(R.id.filter_specify_city_btn);
		specifyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(
						TravellerVentouringFilterActivity.this,
						TravellerVentouraFilterSpecifyCitySelectorActivity.class));
			}
		});

		specifyCheckBox = (CheckBox) findViewById(R.id.filter_specify_city_checkbox);

		backBtn = (Button) findViewById(R.id.btn_back);
		backBtn.setText("Done");

		loadVFunctionSettingViewsFormDB();
		LoadTravellerVFunctionTask loadTravellerVFunctionTask = new LoadTravellerVFunctionTask();
		loadTravellerVFunctionTask.execute();
	}

	@Override
	protected void onPause() {
		super.onPause();
		UpdateTravellerVFunctionTask updateTravellerVFunctionTask = new UpdateTravellerVFunctionTask();
		updateTravellerVFunctionTask.execute();
	}

	/*
	 * The cancel button on the left side
	 */
	public void btnBackOnClick(View view) {
		onBackPressed();
	}

	private void loadVFunctionSettingViewsFormDB() {

		vFunctionSettings = vFunctionSettingService
				.loadJSONTravellerVFunctionSettingsFromDB(travellerId);

		if (vFunctionSettings == null) {
			vFunctionSettings = new JSONTravellerVFunctionSettings();
			Log.i(TAG,
					" Load JSONTravellerVFunctionSettings From DB return null");
		}

		if (vFunctionSettings.getPreferedGender() == Gender.BOTH) {
			genderRG.check(R.id.filter_gender_both_checkbox);
		} else if (vFunctionSettings.getPreferedGender() == Gender.MALE) {
			genderRG.check(R.id.filter_gender_male_checkbox);
		} else {
			genderRG.check(R.id.filter_gender_female_checkbox);
		}
		if (vFunctionSettings.getPreferedUserRole() == UserRole.BOTH) {
			typeRG.check(R.id.filter_type_both_checkbox);
		} else if (vFunctionSettings.getPreferedUserRole() == UserRole.GUIDE) {
			typeRG.check(R.id.filter_type_local_checkbox);
		} else {
			typeRG.check(R.id.filter_type_traveller_checkbox);
		}

		ageMinTV.setText(vFunctionSettings.getMiniAge() + "");
		ageMaxTV.setText(vFunctionSettings.getMaxAge() + "");
		priceMinTV.setText(VentouraConstant.EURO_SYMBOL
				+  vFunctionSettings.getMiniPrice());
		priceMaxTV.setText(VentouraConstant.EURO_SYMBOL
				+ vFunctionSettings.getMaxPrice());
		
		priceSeekBar.setSelectedMaxValue((int) vFunctionSettings.getMaxPrice());
		priceSeekBar.setSelectedMinValue((int) vFunctionSettings.getMiniPrice());
		ageSeekBar.setSelectedMinValue(vFunctionSettings.getMiniAge());
		ageSeekBar.setSelectedMaxValue(vFunctionSettings.getMaxAge());
		
		specifyCheckBox.setChecked(vFunctionSettings.isSpecifyCityToggle());

	}

	/**
	 * network tasks for loading traveller vfunction settings
	 */
	private class LoadTravellerVFunctionTask extends
			AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... arg0) {
			return vFunctionSettingService
					.loadJSONTravellerVFunctionSettingsFromServer(travellerId);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				loadVFunctionSettingViewsFormDB();
			}
		}
	}

	/**
	 * network tasks for updating traveller vfunction settings
	 */
	private class UpdateTravellerVFunctionTask extends
			AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			vFunctionSettingService
					.updateTravellerVFunctionSettings(vFunctionSettings);
			return null;
		}
	}

}
