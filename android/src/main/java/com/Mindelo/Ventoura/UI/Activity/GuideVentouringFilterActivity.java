package com.Mindelo.Ventoura.UI.Activity;

import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Constant.VentouraConstant;
import com.Mindelo.Ventoura.Enum.Gender;
import com.Mindelo.Ventoura.Ghost.IService.IVFunctionSettingsService;
import com.Mindelo.Ventoura.Ghost.Service.VFunctionSettingsService;
import com.Mindelo.Ventoura.JSONEntity.JSONGuideVFunctionSettings;
import com.Mindelo.Ventoura.UI.View.RangeSeekBar;
import com.Mindelo.Ventoura.UI.View.RangeSeekBar.OnRangeSeekBarChangeListener;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class GuideVentouringFilterActivity extends Activity {

	private final String TAG = "GuideVentouringFilterActivity";

	/*
	 * utility instances
	 */
	private IVFunctionSettingsService vFunctionSettingService;
	private SharedPreferences sharedPre;
	private long guideId;

	/*
	 * global variablesa
	 */
	private JSONGuideVFunctionSettings vFunctionSettings;

	/*
	 * views
	 */
	private TextView ageMinTV, ageMaxTV, lastActiveMinTV, lastActiveMaxTV;
	private RadioGroup genderRG;
	private SeekBar lastActiveDaySeekBar;
	private RangeSeekBar<Integer> ageSeekBar;

	private Button backBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_ventoura_filter);

		/*
		 * initial utility instances
		 */
		vFunctionSettingService = new VFunctionSettingsService(this);

		sharedPre = getSharedPreferences(
				VentouraConstant.SHARED_PREFERENCE_VENTOURA,
				Context.MODE_PRIVATE);
		guideId = sharedPre.getLong(VentouraConstant.PRE_USER_ID_IN_SERVER, -1);

		vFunctionSettings = new JSONGuideVFunctionSettings();
		vFunctionSettings.setGuideId(guideId);

		/*
		 * initiate views
		 */
		genderRG = (RadioGroup) findViewById(R.id.filter_gender_radiogroup);
		genderRG.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
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

		ageMinTV = (TextView) findViewById(R.id.filter_age_min_tv);
		ageMaxTV = (TextView) findViewById(R.id.filter_age_max_tv);
		lastActiveMinTV = (TextView) findViewById(R.id.filter_last_active_min_tv);
		lastActiveMaxTV = (TextView) findViewById(R.id.filter_last_active_max_tv);
		lastActiveDaySeekBar = (SeekBar) findViewById(R.id.guide_ventoura_filter_last_active_days_seekbar);

		lastActiveDaySeekBar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onProgressChanged(SeekBar arg0, int value,
							boolean arg2) {
						vFunctionSettings.setLastActivateDays(value);
						if (value == 0) {
							lastActiveMaxTV.setText("Today");
						} else {
							lastActiveMaxTV.setText(value + " Days");
						}

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
					}
				});

		ageSeekBar = new RangeSeekBar<Integer>(
				ConfigurationConstant.VENTOURA_MIN_AGE,
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

		backBtn = (Button) findViewById(R.id.btn_back);
		backBtn.setText("Done");

		// load the views
		loadVFunctionSettingViewsFormDB();
		LoadGuideVFunctionTask loadGuideVFunctionTask = new LoadGuideVFunctionTask();
		loadGuideVFunctionTask.execute();
	}

	@Override
	protected void onPause() {
		super.onPause();
		UpdateGuideVFunctionTask updateGuideVFunctionTask = new UpdateGuideVFunctionTask();
		updateGuideVFunctionTask.execute();
	}

	/*
	 * The cancel button on the left side
	 */
	public void btnBackOnClick(View view) {
		onBackPressed();
	}

	private void loadVFunctionSettingViewsFormDB() {

		vFunctionSettings = vFunctionSettingService
				.loadJSONGuideVFunctionSettingsFromDB(guideId);
		if (vFunctionSettings == null) {
			vFunctionSettings = new JSONGuideVFunctionSettings();
			Log.i(TAG, " Load JSONGUideVFunctionSettings From DB return null");
		}

		if (vFunctionSettings.getPreferedGender() == Gender.BOTH) {
			genderRG.check(R.id.filter_gender_both_checkbox);
		} else if (vFunctionSettings.getPreferedGender() == Gender.MALE) {
			genderRG.check(R.id.filter_gender_male_checkbox);
		} else {
			genderRG.check(R.id.filter_gender_female_checkbox);
		}

		ageMinTV.setText(vFunctionSettings.getMiniAge() + "");
		ageMaxTV.setText(vFunctionSettings.getMaxAge() + "");

		lastActiveMinTV.setText("0");
		lastActiveMaxTV.setText(vFunctionSettings.getLastActivateDays()
				+ " Days");

		lastActiveDaySeekBar.setProgress(vFunctionSettings
				.getLastActivateDays());
		ageSeekBar.setSelectedMaxValue(vFunctionSettings.getMaxAge());
		ageSeekBar.setSelectedMinValue(vFunctionSettings.getMiniAge());

	}

	/**
	 * network tasks for loading guide vfunction settings
	 */
	private class LoadGuideVFunctionTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... arg0) {
			return vFunctionSettingService
					.loadJSONGuideVFunctionSettingsFromServer(guideId);
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
	 * network tasks for updating guide vfunction settings
	 */
	private class UpdateGuideVFunctionTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			vFunctionSettingService
					.updateGuideVFunctionSettings(vFunctionSettings);
			return null;
		}

	}

}
