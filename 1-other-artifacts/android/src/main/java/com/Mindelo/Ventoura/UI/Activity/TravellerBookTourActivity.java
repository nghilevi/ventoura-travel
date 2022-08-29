package com.Mindelo.Ventoura.UI.Activity;

import java.util.Calendar;

import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Constant.VentouraConstant;
import com.Mindelo.Ventoura.Entity.ImageMatch;
import com.Mindelo.Ventoura.Enum.UserRole;
import com.Mindelo.Ventoura.Ghost.IService.IBookingScheduleService;
import com.Mindelo.Ventoura.Ghost.IService.IMatchesService;
import com.Mindelo.Ventoura.Ghost.Service.BookingScheduleService;
import com.Mindelo.Ventoura.Ghost.Service.MatchesService;
import com.Mindelo.Ventoura.JSONEntity.JSONBooking;
import com.Mindelo.Ventoura.UI.View.RoundedImageView;
import com.Mindelo.Ventoura.Util.BitmapUtil;
import com.Mindelo.Ventoura.Util.DateTimeUtil;
import com.Mindelo.Ventoura.Util.ImageUtil;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class TravellerBookTourActivity extends FragmentActivity implements
		OnClickListener {

	/*
	 * progress progressDialog to show user that the backup is processing.
	 */
	private ProgressDialog progressDialog;

	/*
	 * views
	 */
	private Button bookTourConfirmButton;
	private View bookTourDateButton, bookTourTimeButton;
	private RoundedImageView bookTourGuideImageView;
	private TextView guideFistnameTv, guideTourCityTv, guideTourAmountTv,
			guideTourPaybyTv, guideTourDateTv, guideTourTimeTv;

	/*
	 * utility instances
	 */
	private SharedPreferences sharedPre;
	private IBookingScheduleService bookingService;
	private IMatchesService matchService;

	/*
	 * global variables
	 */
	private String guideFirstname;
	private long guideId;
	private String travellerFirstname;
	private long travellerId;

	private Calendar bookTourDate = Calendar.getInstance();

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(R.layout.activity_traveller_book_tour);

		/*
		 * ini views
		 */
		guideFistnameTv = (TextView) this
				.findViewById(R.id.traveller_book_tour_guide_name);
		bookTourConfirmButton = (Button) findViewById(R.id.btn_traveller_book_tour_confirm);
		bookTourConfirmButton.setOnClickListener(this);
		bookTourGuideImageView = (RoundedImageView) findViewById(R.id.traveller_book_tour_guide_head_img);
		guideTourCityTv = (TextView) this
				.findViewById(R.id.traveller_book_tour_city_content_tv);
		guideTourAmountTv = (TextView) this
				.findViewById(R.id.traveller_book_tour_price_content_tv);

		guideTourTimeTv = (TextView) findViewById(R.id.traveller_book_tour_time_tv);
		guideTourDateTv = (TextView) findViewById(R.id.traveller_book_tour_date_tv);

		bookTourDateButton = findViewById(R.id.traveller_book_tour_date_btn);
		bookTourDateButton.setOnClickListener(this);
		bookTourTimeButton = findViewById(R.id.traveller_book_tour_time_btn);
		bookTourTimeButton.setOnClickListener(this);

		/*
		 * ini utility instances
		 */
		sharedPre = getSharedPreferences(
				VentouraConstant.SHARED_PREFERENCE_VENTOURA,
				Context.MODE_PRIVATE);
		bookingService = new BookingScheduleService(this);
		matchService = new MatchesService(this);

		/*
		 * ini values from shared preference
		 */
		sharedPre = getSharedPreferences(
				VentouraConstant.SHARED_PREFERENCE_VENTOURA,
				Context.MODE_PRIVATE);

		travellerId = sharedPre.getLong(VentouraConstant.PRE_USER_ID_IN_SERVER,
				-1);
		travellerFirstname = sharedPre.getString(
				VentouraConstant.PRE_USER_FIRST_NAME, "Noname");

		guideId = getIntent().getLongExtra("guideId", -1);
		guideFirstname = getIntent().getStringExtra("guideFirstname");

		/*
		 * set view values
		 */
		guideTourCityTv.setText("fixc city");
		guideTourAmountTv.setText("fix amount"); // TODO

		guideFistnameTv.setText(guideFirstname);

		try {
			ImageMatch headImage = matchService.getSingleMatchImageFromDB(guideId, UserRole.GUIDE.getNumVal());
			if(headImage != null){
				Bitmap bitMap = BitmapUtil.byteArrayToBitMap(
						headImage.getImageContent(),
						ConfigurationConstant.NORMAL_USER_PORTAL_IMAGE_WIDTH,
						ConfigurationConstant.NORMAL_USER_PORTAL_IMAGE_HEIGHT);
				bookTourGuideImageView.setImageBitmap(bitMap);	
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * when the send button is clicked, send the message out
	 */
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btn_traveller_book_tour_confirm:
			CreateBookingTask createBookingTask = new CreateBookingTask();
			createBookingTask.execute();
			break;
		case R.id.traveller_book_tour_date_btn:
			DatePickerDialog dataPicker = new DatePickerDialog(this,
					new OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int year,
								int monthOfYear, int dayOfMonth) {
							bookTourDate.set(year, monthOfYear, dayOfMonth);
							guideTourDateTv.setText(dayOfMonth + "-"
									+ (monthOfYear + 1) + "-" + year);
						}
					}, bookTourDate.get(Calendar.YEAR),
					bookTourDate.get(Calendar.MONTH),
					bookTourDate.get(Calendar.DAY_OF_MONTH));
			dataPicker.show();
			break;
		case R.id.traveller_book_tour_time_btn:
			TimePickerDialog timePicker = new TimePickerDialog(this,
					new OnTimeSetListener() {

						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							bookTourDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
							bookTourDate.set(Calendar.MINUTE, minute);
							guideTourTimeTv.setText(hourOfDay + ":" + minute);
						}
					}, bookTourDate.get(Calendar.HOUR_OF_DAY),
					bookTourDate.get(Calendar.MINUTE), true);
			timePicker.show();
			break;
		}
	}

	/**
	 * Network tasks for creating load bookings and shedules
	 */
	private class CreateBookingTask extends AsyncTask<Void, Void, Long> {

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(TravellerBookTourActivity.this);
			progressDialog.setMessage("Creating booking...");
			progressDialog.show();
		}

		@Override
		protected Long doInBackground(Void... params) {
			String tourDate = DateTimeUtil.fromDateToString(bookTourDate
					.getTime());

			JSONBooking booking = new JSONBooking();
			booking.setGuideId(guideId);
			booking.setGuideFirstname(guideFirstname);
			booking.setTravellerId(travellerId);
			booking.setTravellerFirstname(travellerFirstname);
			booking.setTourDate(tourDate);

			return bookingService.createBooking(booking);
		}

		@Override
		protected void onPostExecute(Long result) {

			dismissProgressDialog();

			if (result == -1) {
				// Something is wrong when creating a booking
				Toast.makeText(TravellerBookTourActivity.this,
						"Error happened when creating the booking",
						Toast.LENGTH_LONG).show();
			} else {
				// More after successfully create the tour
				Toast.makeText(TravellerBookTourActivity.this, "The booking has been created",
						Toast.LENGTH_SHORT).show();
				onBackPressed();
			}
		}
	}

	/*
	 * go back button
	 */
	public void btnBackOnClick(View view) {
		onBackPressed();
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
