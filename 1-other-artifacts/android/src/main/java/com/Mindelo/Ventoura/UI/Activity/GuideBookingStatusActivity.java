package com.Mindelo.Ventoura.UI.Activity;

import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Constant.VentouraConstant;
import com.Mindelo.Ventoura.Entity.ImageMatch;
import com.Mindelo.Ventoura.Enum.BookingStatus;
import com.Mindelo.Ventoura.Enum.UserRole;
import com.Mindelo.Ventoura.Ghost.IService.IBookingScheduleService;
import com.Mindelo.Ventoura.Ghost.IService.IMatchesService;
import com.Mindelo.Ventoura.Ghost.Service.BookingScheduleService;
import com.Mindelo.Ventoura.Ghost.Service.MatchesService;
import com.Mindelo.Ventoura.JSONEntity.JSONBooking;
import com.Mindelo.Ventoura.UI.View.RoundedImageView;
import com.Mindelo.Ventoura.Util.BitmapUtil;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GuideBookingStatusActivity extends FragmentActivity {

	/*
	 * progress dialog
	 */
	private ProgressDialog progressDialog;

	/*
	 * utility instantces
	 */
	private SharedPreferences sharedPre;
	private IBookingScheduleService bookingScheduleService;
	private IMatchesService matchService;
	private long guideId;
	private long bookingId;
	private JSONBooking jsonBooking;

	/*
	 * views
	 */
	private RoundedImageView bookTourTravellerImageView;
	private TextView travellerFistnameTv;
	private Button action_btn_1, action_btn_2;
	private TextView notificationTv;
	private ImageView statusBarImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_booking_status);

		/*
		 * initialize utilities
		 */
		sharedPre = getSharedPreferences(
				VentouraConstant.SHARED_PREFERENCE_VENTOURA,
				Context.MODE_PRIVATE);
		bookingScheduleService = new BookingScheduleService(this);
		matchService = new MatchesService(this);
		guideId = sharedPre.getLong(VentouraConstant.PRE_USER_ID_IN_SERVER, -1);

		/*
		 * initialize global variables
		 */
		Intent intent = getIntent();
		jsonBooking = (JSONBooking) intent.getSerializableExtra("jsonBooking");

		/*
		 * get the views
		 */
		bookTourTravellerImageView = (RoundedImageView) findViewById(R.id.guide_booking_status_traveller_head_img);
		travellerFistnameTv = (TextView) this
				.findViewById(R.id.guide_booking_status_traveller_name);

		statusBarImageView = (ImageView) findViewById(R.id.traveller_booking_status_status_bar);
		notificationTv = (TextView) this
				.findViewById(R.id.traveller_booking_status_notification);
		action_btn_1 = (Button) this
				.findViewById(R.id.traveller_booking_status_btn_1);
		action_btn_2 = (Button) this
				.findViewById(R.id.traveller_booking_status_btn_2);

		/*
		 * set the views
		 */
		if (jsonBooking != null) {
			travellerFistnameTv.setText(jsonBooking.getTravellerFirstname());
			try {
				ImageMatch headImage = matchService.getSingleMatchImageFromDB(
						jsonBooking.getTravellerId(),
						UserRole.TRAVELLER.getNumVal());
				if (headImage != null) {
					Bitmap bitMap = BitmapUtil
							.byteArrayToBitMap(
									headImage.getImageContent(),
									ConfigurationConstant.NORMAL_USER_PORTAL_IMAGE_WIDTH,
									ConfigurationConstant.NORMAL_USER_PORTAL_IMAGE_HEIGHT);
					bookTourTravellerImageView.setImageBitmap(bitMap);
				}

				setStatusBarAndNotification();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void setStatusBarAndNotification() {

		BookingStatus bookingStatus = BookingStatus.values()[jsonBooking
				.getBookingStatus()];
		switch (bookingStatus) {
		case REQUEST_BY_TRAVELLER:
			break;
		case REQUEST_CANCELLED_BY_TRAVELLER:
			break;
		case REQUEST_DECLINED_BY_LOCAL:
			break;
		case REQUEST_ACCEPTED_BY_LOCAL:
			notificationTv.setText("Wait for "
					+ jsonBooking.getTravellerFirstname() + " to book.");
			statusBarImageView
					.setImageResource(R.drawable.booking_status_bar_declined);
			action_btn_1.setVisibility(View.GONE);
			action_btn_2.setText("Cancel Request");
			action_btn_2
					.setOnClickListener(new GuideCancelTourBeforeBookingOnClickListener());
			break;
		case TOUR_CANCELLED_BY_TRAVELLER_BFRORE_BOOKING:
			notificationTv
					.setText(jsonBooking.getTravellerFirstname()
							+ " has cancelled this tour. You may delete it from your record.");
			statusBarImageView
					.setImageResource(R.drawable.booking_status_bar_cancelled_before_booked);
			action_btn_1.setVisibility(View.GONE);
			action_btn_2.setText("Delete Tour");
			action_btn_2
					.setOnClickListener(new GuideDeleteTourOnClickListener());
			break;
		case TOUR_CANCELLED_BY_GUIDE_BFRORE_BOOKING:
			break;
		case TOUR_TIME_OUT_BEFORE_BOOKING:
			notificationTv
					.setText(jsonBooking.getTravellerFirstname()
							+ "'s tour booking has timed out. \n See if they want to renew a request. You may delete this request from your record.");
			statusBarImageView
					.setImageResource(R.drawable.booking_status_bar_book_failed);
			action_btn_1.setVisibility(View.GONE);
			action_btn_2.setText("Delete Tour");
			action_btn_2
					.setOnClickListener(new GuideDeleteTourOnClickListener());
			break;
		case TOUR_BOOKED_BY_TRAVELLER:
			notificationTv.setText("This tour booking is confirmed. ");
			statusBarImageView
					.setImageResource(R.drawable.booking_status_bar_booked_succeeded);
			action_btn_1.setVisibility(View.GONE);
			action_btn_2.setText("Delete Tour");
			action_btn_2
					.setOnClickListener(new GuideCancelTourAfterBookingBeforeChargedOnClickListener());
			break;
		case TOUR_CHARGED_SUCCESSFULLY:
			notificationTv.setText("This tour booking is confirmed. ");
			statusBarImageView
					.setImageResource(R.drawable.booking_status_bar_booked_succeeded);
			action_btn_1.setVisibility(View.GONE);
			action_btn_2.setText("Delete Tour");
			action_btn_2
					.setOnClickListener(new GuideCancelTourAfterBookingAfterChargedOnClickListener());
			break;
		case BOOKING_CANCELLED_BY_TRAVELLER_BEFORE_CHARGED:
			notificationTv
					.setText(jsonBooking.getTravellerFirstname()
							+ " has cancelled this tour. Your may delete it from your record. ");
			statusBarImageView
					.setImageResource(R.drawable.booking_status_bar_cancelled_after_booked);
			action_btn_1.setVisibility(View.GONE);
			action_btn_2.setText("Delete Tour");
			action_btn_2
					.setOnClickListener(new GuideDeleteTourOnClickListener());
			break;
		case BOOKING_CANCELLED_BY_TRAVELLER_AFTER_CHARGED:
			notificationTv
					.setText(jsonBooking.getTravellerFirstname()
							+ " has cancelled this tour. but you will still receive your tour fee thanks to the Ventoura 7 day cancellation policy! You may delete this tour from your record.");
			statusBarImageView
					.setImageResource(R.drawable.booking_status_bar_cancelled_after_booked);
			action_btn_1.setVisibility(View.GONE);
			action_btn_2.setText("Delete Tour");
			action_btn_2
					.setOnClickListener(new GuideDeleteTourOnClickListener());
			break;
		case BOOKING_AUTHORIZATION_FAILED:
			notificationTv
					.setText("Unfortunately, "
							+ jsonBooking.getTravellerFirstname()
							+ "'s payment for your tour failed and their booking has been cancelled. They can rebook the tour if they make a new tour request.");
			statusBarImageView
					.setImageResource(R.drawable.booking_status_bar_book_failed);
			action_btn_1.setVisibility(View.GONE);
			action_btn_2.setText("Delete Tour");
			action_btn_2
					.setOnClickListener(new GuideCancelTourAfterBookingBeforeChargedOnClickListener());
			break;
		case BOOKING_CAPTURED_FAILED:
			notificationTv
					.setText("Unfortunately, "
							+ jsonBooking.getTravellerFirstname()
							+ "'s payment for your tour failed and their booking has been cancelled. They can rebook the tour if they make a new tour request.");
			statusBarImageView
					.setImageResource(R.drawable.booking_status_bar_book_failed);
			action_btn_1.setVisibility(View.GONE);
			action_btn_2.setText("Delete Tour");
			action_btn_2
					.setOnClickListener(new GuideCancelTourAfterBookingBeforeChargedOnClickListener());
			break;
		case BOOKING_LAPSED_DUE_TO_PAYMENT_FAILURE:
			notificationTv
					.setText("Unfortunately, "
							+ jsonBooking.getTravellerFirstname()
							+ "'s payment for your tour failed and their booking has been cancelled. They can rebook the tour if they make a new tour request.");
			statusBarImageView
					.setImageResource(R.drawable.booking_status_bar_book_failed);
			action_btn_1.setVisibility(View.GONE);
			action_btn_2.setText("Delete Tour");
			action_btn_2
					.setOnClickListener(new GuideCancelTourAfterBookingBeforeChargedOnClickListener());
			break;
		case BOOKING_CANCELLED_BY_LOCAL_BEFORE_CHARGED:
			break;
		case BOOKING_CANCELLED_BY_LOCAL_AFTER_CHARGED:
			break;
		case TOUR_REGISTERED:
			notificationTv.setText("Happy travelling with "
					+ jsonBooking.getTravellerFirstname());
			statusBarImageView
					.setImageResource(R.drawable.booking_status_bar_tour_registered);
			action_btn_1.setVisibility(View.GONE);
			action_btn_2.setText("Delete Tour");
			action_btn_2
					.setOnClickListener(new GuideDeleteTourOnClickListener());
			break;
		case TOUR_REVIEWED:
			notificationTv.setText("The tour has been reviewed by "
					+ jsonBooking.getTravellerFirstname());
			statusBarImageView
					.setImageResource(R.drawable.booking_status_bar_tour_reviewed);
			action_btn_1.setVisibility(View.GONE);
			action_btn_2.setText("Delete Tour");
			action_btn_2
					.setOnClickListener(new GuideDeleteTourOnClickListener());
			break;
		case TOUR_DISPUTED:
			notificationTv.setText("The tour has been disputed by "
					+ jsonBooking.getTravellerFirstname());
			statusBarImageView
					.setImageResource(R.drawable.booking_status_bar_tour_disputed);
			action_btn_1.setVisibility(View.GONE);
			action_btn_2.setText("Delete Tour");
			action_btn_2
					.setOnClickListener(new GuideDeleteTourOnClickListener());
			break;
		}

	}

	private class UpdateBookingStatusTask extends
			AsyncTask<Integer, Void, Void> {
		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(GuideBookingStatusActivity.this);
			progressDialog.setTitle("Processing...");
			progressDialog.show();
		}

		@Override
		protected Void doInBackground(Integer... arg0) {
			bookingScheduleService.travellerUpdateBookingStatus(
					jsonBooking.getId(), arg0[0]);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			dismissProgressDialog();
		}
	}

	private class GuideDeleteTourOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			UpdateBookingStatusTask updateBookingStatusTask = new UpdateBookingStatusTask();
			updateBookingStatusTask.execute(BookingStatus.TOUR_DELETED_BY_GUIDE
					.getNumVal());
		}
	}

	private class GuideCancelTourBeforeBookingOnClickListener implements
			OnClickListener {
		@Override
		public void onClick(View v) {
			UpdateBookingStatusTask updateBookingStatusTask = new UpdateBookingStatusTask();
			updateBookingStatusTask
					.execute(BookingStatus.TOUR_CANCELLED_BY_GUIDE_BFRORE_BOOKING
							.getNumVal());
		}
	}

	private class GuideCancelTourAfterBookingBeforeChargedOnClickListener
			implements OnClickListener {
		@Override
		public void onClick(View v) {
			UpdateBookingStatusTask updateBookingStatusTask = new UpdateBookingStatusTask();
			updateBookingStatusTask
					.execute(BookingStatus.BOOKING_CANCELLED_BY_LOCAL_BEFORE_CHARGED
							.getNumVal());
		}
	}

	private class GuideCancelTourAfterBookingAfterChargedOnClickListener
			implements OnClickListener {
		@Override
		public void onClick(View v) {
			UpdateBookingStatusTask updateBookingStatusTask = new UpdateBookingStatusTask();
			updateBookingStatusTask
					.execute(BookingStatus.BOOKING_CANCELLED_BY_LOCAL_AFTER_CHARGED
							.getNumVal());
		}
	}

	/*
	 * The back button click to switch the menu and content
	 */
	public void btnBackOnClick(View view) {
		onBackPressed();
	}

	/*
	 * helper to handle the dismiss progress dialog
	 */
	private void dismissProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		progressDialog = null;
	}

}
