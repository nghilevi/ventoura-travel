package com.Mindelo.Ventoura.UI.Activity;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.Mindelo.Ventoura.Constant.BroadcastConstant;
import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Constant.VentouraConstant;
import com.Mindelo.Ventoura.Entity.City;
import com.Mindelo.Ventoura.Ghost.IService.IBookingScheduleService;
import com.Mindelo.Ventoura.Ghost.Service.BookingScheduleService;
import com.Mindelo.Ventoura.JSONEntity.JSONTravellerSchedule;
import com.Mindelo.Ventoura.UI.View.BottomPopupMenuAction;
import com.Mindelo.Ventoura.UI.View.PopupActionItem;
import com.Mindelo.Ventoura.Util.DateTimeUtil;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class TravellerAddTripActivity extends FragmentActivity implements
		OnClickListener {

	private ProgressDialog progressDialog;

	private static final int REQUESTCODECITY = 0;
	private Calendar arriveDate = Calendar.getInstance();
	private Calendar departDate = Calendar.getInstance();

	/*
	 * utility instances
	 */
	private IBookingScheduleService bookingService;
	private City city = null;
	private long travellerId;

	/*
	 * views
	 */
	private TextView tvCity;
	private TextView tvArrivalDate, tvDepartDate;
	private ImageView countryFlagImageView;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_traveller_add_trip);

		Button btnAdd = (Button) findViewById(R.id.btn_action);
		btnAdd.setText("Done");

		travellerId = getSharedPreferences(
				VentouraConstant.SHARED_PREFERENCE_VENTOURA,
				Context.MODE_PRIVATE).getLong(
				VentouraConstant.PRE_USER_ID_IN_SERVER, -1);

		/*
		 * initiate views
		 */
		View btnCity = findViewById(R.id.btn_add_trip_city);
		btnCity.setOnClickListener(this);
		View btnArrival = findViewById(R.id.btn_add_trip_arrival);
		btnArrival.setOnClickListener(this);
		View btnDep = findViewById(R.id.btn_add_trip_departure);
		btnDep.setOnClickListener(this);

		tvCity = (TextView) findViewById(R.id.tv_add_trip_city);
		tvArrivalDate = (TextView) findViewById(R.id.tv_add_trip_arrival);
		tvDepartDate = (TextView) findViewById(R.id.tv_add_trip_departure);
		countryFlagImageView = (ImageView) findViewById(R.id.imageview_add_trip_country_flag);

		/*
		 * initiate utility instances
		 */
		bookingService = new BookingScheduleService(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add_trip_city:
			startActivityForResult(
					new Intent(this, CitySelectorActivity.class),
					REQUESTCODECITY);
			break;
		case R.id.btn_add_trip_arrival:
			DatePickerDialog arrivalDataPicker = new DatePickerDialog(this,
					new OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							arriveDate.set(year, monthOfYear, dayOfMonth);
							tvArrivalDate.setText(dayOfMonth
									+ "-"
									+ (new SimpleDateFormat("MMM")
											.format(arriveDate.getTime()))
									+ "-" + year);
						}
					}, arriveDate.get(Calendar.YEAR),
					arriveDate.get(Calendar.MONTH),
					arriveDate.get(Calendar.DAY_OF_MONTH));
			arrivalDataPicker.show();
			break;
		case R.id.btn_add_trip_departure:
			DatePickerDialog departDataPicker = new DatePickerDialog(this,
					new OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							departDate.set(year, monthOfYear, dayOfMonth);
							tvDepartDate.setText(dayOfMonth
									+ "-"
									+ (new SimpleDateFormat("MMM")
											.format(departDate.getTime()))
									+ "-" + year);
						}
					}, departDate.get(Calendar.YEAR),
					departDate.get(Calendar.MONTH),
					departDate.get(Calendar.DAY_OF_MONTH));
			departDataPicker.show();
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUESTCODECITY:
			if (data != null) {
				city = (City) data.getSerializableExtra("CitySelected");
				tvCity.setText(city.getCityName());
				try {
					InputStream ims;
					ims = getAssets().open(
							ConfigurationConstant.VENTOURA_ASSET_COUNTRY_FLAG
									+ "/" + city.getCountryId() + ".png");
					Drawable countryFlag = Drawable.createFromStream(ims, null);
					countryFlagImageView.setImageDrawable(countryFlag);

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			break;
		default:
			break;
		}
	}

	/*
	 * The back button click to switch the menu and content
	 */
	public void btnBackOnClick(View view) {
		onBackPressed();
	}

	/*
	 * The add button click to switch the menu and content
	 */
	public void btnActionOnClick(View view) {
		if (city == null) {
			Toast.makeText(TravellerAddTripActivity.this,
					"Please choose a city", Toast.LENGTH_SHORT).show();
			return;
		}
		if (arriveDate.before(Calendar.getInstance())) {
			Toast.makeText(TravellerAddTripActivity.this,
					"The arrival date should be later than today.",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (arriveDate.after(departDate)) {
			Toast.makeText(
					TravellerAddTripActivity.this,
					"The arrival date should be earlier than the departure date.",
					Toast.LENGTH_SHORT).show();
			return;
		}

		AddTripTask addTripTask = new AddTripTask();
		addTripTask.execute();
	}

	/**
	 * Network tasks for add trips
	 */

	private class AddTripTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(TravellerAddTripActivity.this);
			if (!progressDialog.isShowing()) {
				progressDialog.setMessage("Adding trip...");
				progressDialog.show();
			}
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			JSONTravellerSchedule travellerSchedule = new JSONTravellerSchedule();
			travellerSchedule.setTravellerId(travellerId);

			// in a format of "dd/mm/yyyy"
			String tour_start_time = DateTimeUtil.fromDateToString_DDMMYYYY(arriveDate.getTime());
			String tour_end_time = DateTimeUtil.fromDateToString_DDMMYYYY(departDate.getTime());

			travellerSchedule.setStartTime(tour_start_time);
			travellerSchedule.setEndTime(tour_end_time);

			travellerSchedule.setCity(city.getId());
			travellerSchedule.setCountry(city.getCountryId());

			return bookingService.createTravellerSchedule(travellerSchedule);
		}

		@Override
		protected void onPostExecute(Boolean result) {

			dismissProgressDialog();

			if (!result.booleanValue()) {
				Toast.makeText(TravellerAddTripActivity.this,
						"Error happened when creating the tour.",
						Toast.LENGTH_SHORT).show();
			} else {
				Intent intent = new Intent();
				// send a broadcast event to notice the trip list and
				intent.setAction(BroadcastConstant.USER_NEW_TRIP_ADDED_ACTION);
				sendBroadcast(intent);

				btnBackOnClick(null);
			}
		}
	}

	/*
	 * helper to handle the dismiss progress progressDialog
	 */
	private void dismissProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		progressDialog = null;
	}


}
