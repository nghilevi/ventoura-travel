package com.Mindelo.Ventoura.UI.Activity;

import com.Mindelo.Ventoura.Constant.BroadcastConstant;
import com.Mindelo.Ventoura.Constant.VentouraConstant;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class GuideBookingFilterActivity extends Activity {

	/*
	 * utility instances
	 */
	private SharedPreferences sharedPre;

	/*
	 * global variables
	 */
	private boolean userPreBookRequest = true, userPreBookNotPaid = true,
			userPreBookPaid = true, userPreBookCancelled = true,
			userPreBookComplete = true, userPreBookError = true, userPreFemale = true,
			userPreMale = true;

	/*
	 * views
	 */
	private CheckBox preBookRequestCB, preBookNotPaidCB, preBookPaidCB,
			preBookCancelledCB, preBookCompleteCB, preBookErrorCB, preFemaleCB,
			preMaleCB;
	private Button backBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_booking_filter);

		sharedPre = getSharedPreferences(
				VentouraConstant.SHARED_PREFERENCE_VENTOURA,
				Context.MODE_PRIVATE);

		userPreBookRequest = sharedPre.getBoolean(
				VentouraConstant.PRE_BOOKING_FILTER_BOOK_REQUEST, true);
		userPreBookNotPaid = sharedPre.getBoolean(
				VentouraConstant.PRE_BOOKING_FILTER_BOOK_NOT_PAID, true);
		userPreBookPaid = sharedPre.getBoolean(
				VentouraConstant.PRE_BOOKING_FILTER_BOOK_PAID, true);
		userPreBookCancelled = sharedPre.getBoolean(
				VentouraConstant.PRE_BOOKING_FILTER_BOOK_CANCELLED, true);
		userPreBookComplete = sharedPre.getBoolean(
				VentouraConstant.PRE_BOOKING_FILTER_BOOK_COMPLETE, true);
		userPreBookError = sharedPre.getBoolean(
				VentouraConstant.PRE_BOOKING_FILTER_BOOK_ERROR, true);
		
		userPreFemale = sharedPre.getBoolean(
				VentouraConstant.PRE_BOOKING_FILTER_GENDER_FEMALE, true);
		userPreMale = sharedPre.getBoolean(
				VentouraConstant.PRE_BOOKING_FILTER_GENDER_MALE, true);

		/*
		 * initialize views
		 */
		preBookRequestCB = (CheckBox) findViewById(R.id.bfilter_book_request_switch);
		preBookNotPaidCB = (CheckBox) findViewById(R.id.bfilter_book_not_paid_switch);
		preBookCancelledCB = (CheckBox) findViewById(R.id.bfilter_book_cancelled_switch);
		preBookCompleteCB = (CheckBox) findViewById(R.id.bfilter_book_complete_switch);
		preBookErrorCB = (CheckBox) findViewById(R.id.bfilter_book_error_switch);
		preBookPaidCB = (CheckBox) findViewById(R.id.bfilter_book_paid_switch);

		preFemaleCB = (CheckBox) findViewById(R.id.bfilter_female_switch);
		preMaleCB = (CheckBox) findViewById(R.id.bfilter_male_switch);

		
		preBookRequestCB.setChecked(userPreBookRequest);
		preBookPaidCB.setChecked(userPreBookPaid);
		preBookNotPaidCB.setChecked(userPreBookNotPaid);
		preBookCancelledCB.setChecked(userPreBookCancelled);
		preBookCompleteCB.setChecked(userPreBookComplete);
		preBookErrorCB.setChecked(userPreBookError);
		
		preFemaleCB.setChecked(userPreFemale);
		preMaleCB.setChecked(userPreMale);
		
		backBtn = (Button) findViewById(R.id.btn_back);
		backBtn.setText("Done");

	}

	@Override
	protected void onPause() {
		super.onPause();
		Editor editor = sharedPre.edit();

		editor.putBoolean(VentouraConstant.PRE_BOOKING_FILTER_BOOK_PAID,
				preBookPaidCB.isChecked());
		editor.putBoolean(VentouraConstant.PRE_BOOKING_FILTER_BOOK_REQUEST,
				preBookRequestCB.isChecked());
		editor.putBoolean(VentouraConstant.PRE_BOOKING_FILTER_BOOK_NOT_PAID,
				preBookNotPaidCB.isChecked());
		editor.putBoolean(VentouraConstant.PRE_BOOKING_FILTER_BOOK_CANCELLED,
				preBookCancelledCB.isChecked());
		editor.putBoolean(VentouraConstant.PRE_BOOKING_FILTER_BOOK_COMPLETE,
				preBookCompleteCB.isChecked());
		editor.putBoolean(VentouraConstant.PRE_BOOKING_FILTER_BOOK_ERROR,
				preBookErrorCB.isChecked());

		
		editor.putBoolean(VentouraConstant.PRE_BOOKING_FILTER_GENDER_MALE,
				preMaleCB.isChecked());
		editor.putBoolean(VentouraConstant.PRE_BOOKING_FILTER_GENDER_FEMALE,
				preFemaleCB.isChecked());

		editor.commit();

		// send the broad cast that the message filter was changed
		Intent intent = new Intent(
				BroadcastConstant.USER_BOOKING_FILTER_UPDATED_ACTION);
		sendBroadcast(intent);

	}

	/*
	 * go back button
	 */
	public void btnBackOnClick(View view) {
		onBackPressed();
	}
}
