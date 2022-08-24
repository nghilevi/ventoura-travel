package com.Mindelo.Ventoura.UI.Activity;

import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
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
import com.Mindelo.Ventoura.Util.ImageUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TravellerBookingStatusActivity extends FragmentActivity {

	private IBookingScheduleService bookingScheduleService;
	private IMatchesService matchService;
	private JSONBooking jsonBooking;

	/*
	 * progress dialog
	 */
	private ProgressDialog progressDialog;

	/*
	 * views
	 */
	private RoundedImageView bookTourGuideImageView;
	private TextView travellerFistnameTv;
	private Button action_btn_1, action_btn_2;
	private TextView notificationTv;
	private ImageView statusBarImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_traveller_booking_status);

		/*
		 * initialize utilities
		 */
		bookingScheduleService = new BookingScheduleService(this);
		matchService = new MatchesService(this);

		/*
		 * initialize global variables
		 */
		Intent intent = getIntent();
		jsonBooking = (JSONBooking) intent.getSerializableExtra("jsonBooking");

		/*
		 * get the views
		 */

		bookTourGuideImageView = (RoundedImageView) findViewById(R.id.traveller_booking_status_traveller_head_img);
		statusBarImageView = (ImageView) findViewById(R.id.traveller_booking_status_status_bar);
		travellerFistnameTv = (TextView) this
				.findViewById(R.id.traveller_booking_status_traveller_name);
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
			travellerFistnameTv.setText(jsonBooking.getGuideFirstname());
			try {

				ImageMatch headImage = matchService.getSingleMatchImageFromDB(
						jsonBooking.getGuideId(), UserRole.GUIDE.getNumVal());
				if (headImage != null) {
					Bitmap bitMap = BitmapUtil
							.byteArrayToBitMap(
									headImage.getImageContent(),
									ConfigurationConstant.NORMAL_USER_PORTAL_IMAGE_WIDTH,
									ConfigurationConstant.NORMAL_USER_PORTAL_IMAGE_HEIGHT);
					bookTourGuideImageView.setImageBitmap(bitMap);
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
			notificationTv.setText("Waiting for response");
			statusBarImageView
					.setImageResource(R.drawable.booking_status_bar_wait_response);
			action_btn_1.setVisibility(View.GONE);
			action_btn_2.setText("Cancel Request");
			action_btn_2
					.setOnClickListener(new TravellerDeleteTourOnClickListener());
			break;
		case REQUEST_CANCELLED_BY_TRAVELLER:
			break;
		case REQUEST_DECLINED_BY_LOCAL:
			notificationTv.setText(jsonBooking.getGuideFirstname()
					+ " has declined your tour request.");
			statusBarImageView
					.setImageResource(R.drawable.booking_status_bar_declined);
			action_btn_1.setVisibility(View.GONE);
			action_btn_2.setText("Delete");
			action_btn_2
					.setOnClickListener(new TravellerDeleteTourOnClickListener());
			break;
		case REQUEST_ACCEPTED_BY_LOCAL:
			notificationTv.setText(jsonBooking.getGuideFirstname()
					+ " has accepted your tour request. You can book it now.");
			statusBarImageView
					.setImageResource(R.drawable.booking_status_bar_declined);

			action_btn_1.setText("Book Now");
			action_btn_1
					.setOnClickListener(new TravellerBookTourOnClickListener());
			action_btn_2.setText("Cancel Tour");
			action_btn_2
					.setOnClickListener(new TravellerCancelTourBeforeBookingOnClickListener());
			break;
		case TOUR_CANCELLED_BY_TRAVELLER_BFRORE_BOOKING:
			break;
		case TOUR_CANCELLED_BY_GUIDE_BFRORE_BOOKING:
			notificationTv.setText(jsonBooking.getGuideFirstname()
					+ " has cancelled this tour.");
			statusBarImageView
					.setImageResource(R.drawable.booking_status_bar_cancelled_before_booked);
			action_btn_1.setVisibility(View.GONE);
			action_btn_2.setText("Delete");
			action_btn_2
					.setOnClickListener(new TravellerDeleteTourOnClickListener());
			break;
		case TOUR_TIME_OUT_BEFORE_BOOKING:
			notificationTv
					.setText("Your booking window has timed out, you can send another request to "
							+ jsonBooking.getGuideFirstname()
							+ "if you still want to book");
			statusBarImageView
					.setImageResource(R.drawable.booking_status_bar_book_failed);
			action_btn_1.setVisibility(View.GONE);
			action_btn_2.setText("Delete");
			action_btn_2
					.setOnClickListener(new TravellerDeleteTourOnClickListener());
			break;
		case TOUR_BOOKED_BY_TRAVELLER:
			notificationTv.setText("You have booked this tour.");
			statusBarImageView
					.setImageResource(R.drawable.booking_status_bar_booked_succeeded);

			action_btn_1.setVisibility(View.GONE);
			action_btn_2.setText("Cancel Tour");
			action_btn_2
					.setOnClickListener(new TravellerCancelTourAfterBookingBeforeChargedOnClickListener());
			break;
		case TOUR_CHARGED_SUCCESSFULLY:
			notificationTv.setText("You have booked this tour.");
			statusBarImageView
					.setImageResource(R.drawable.booking_status_bar_booked_succeeded);
			action_btn_1.setVisibility(View.GONE);
			action_btn_2.setText("Cancel Tour");
			action_btn_2
					.setOnClickListener(new TravellerCancelTourAfterBookingAfterChargedOnClickListener());
			break;
		case BOOKING_CANCELLED_BY_TRAVELLER_BEFORE_CHARGED:
			break;
		case BOOKING_CANCELLED_BY_TRAVELLER_AFTER_CHARGED:
			break;
		case BOOKING_AUTHORIZATION_FAILED:
			notificationTv
					.setText("Your payment has failed. You can try to pay it again.");
			statusBarImageView
					.setImageResource(R.drawable.booking_status_bar_book_failed);
			action_btn_1.setText("Enter Correct Payment");
			action_btn_1
					.setOnClickListener(new TravellerBookTourOnClickListener());
			action_btn_2.setText("Cancel Tour");
			action_btn_2
					.setOnClickListener(new TravellerCancelTourAfterBookingBeforeChargedOnClickListener());
			break;
		case BOOKING_CAPTURED_FAILED:
			notificationTv
					.setText("Your payment has failed. You can try to pay it again.");
			statusBarImageView
					.setImageResource(R.drawable.booking_status_bar_book_failed);
			action_btn_1.setText("Enter Correct Payment");
			action_btn_1
					.setOnClickListener(new TravellerBookTourOnClickListener());
			action_btn_2.setText("Cancel Tour");
			action_btn_2
					.setOnClickListener(new TravellerCancelTourAfterBookingBeforeChargedOnClickListener());
			break;
		case BOOKING_LAPSED_DUE_TO_PAYMENT_FAILURE:
			notificationTv
					.setText("Unfortunately your payment for your tour with "
							+ jsonBooking.getGuideFirstname()
							+ " failed and your booking has lapsed. You can still request another tour if you wish. ");
			statusBarImageView
					.setImageResource(R.drawable.booking_status_bar_book_failed);

			action_btn_1.setVisibility(View.GONE);
			action_btn_2.setText("Delete");
			action_btn_2
					.setOnClickListener(new TravellerDeleteTourOnClickListener());
			break;
		case BOOKING_CANCELLED_BY_LOCAL_BEFORE_CHARGED:
			notificationTv
					.setText(jsonBooking.getGuideFirstname()
							+ " has cancelled your tour. \n You have not been charged.");
			statusBarImageView
					.setImageResource(R.drawable.booking_status_bar_cancelled_after_booked);

			action_btn_1.setVisibility(View.GONE);
			action_btn_2.setText("Delete");
			action_btn_2
					.setOnClickListener(new TravellerDeleteTourOnClickListener());
			break;
		case BOOKING_CANCELLED_BY_LOCAL_AFTER_CHARGED:
			notificationTv
					.setText(jsonBooking.getGuideFirstname()
							+ " has cancelled your tour. You will receive a full refund to your credit card/paypal within the next 7 days.");
			statusBarImageView
					.setImageResource(R.drawable.booking_status_bar_cancelled_after_booked);

			action_btn_1.setVisibility(View.GONE);
			action_btn_2.setText("Delete");
			action_btn_2
					.setOnClickListener(new TravellerDeleteTourOnClickListener());
			break;
		case TOUR_REGISTERED:
			notificationTv.setText("Happy travelling with "
					+ jsonBooking.getGuideFirstname());
			statusBarImageView
					.setImageResource(R.drawable.booking_status_bar_tour_registered);

			action_btn_1.setVisibility(View.GONE);
			action_btn_2.setText("Review This Tour");
			action_btn_2
					.setOnClickListener(new TravellerReviewTourOnClickListener());
			break;
		case TOUR_REVIEWED:
			notificationTv.setText("You have already reviewed this tour.");
			statusBarImageView
					.setImageResource(R.drawable.booking_status_bar_tour_reviewed);

			action_btn_1.setVisibility(View.GONE);
			action_btn_2.setText("Delete");
			action_btn_2
					.setOnClickListener(new TravellerDeleteTourOnClickListener());
			break;
		case TOUR_DISPUTED:
			notificationTv.setText("You have already disputed this tour.");
			statusBarImageView
					.setImageResource(R.drawable.booking_status_bar_tour_disputed);

			action_btn_1.setVisibility(View.GONE);
			action_btn_2.setText("Delete");
			action_btn_2
					.setOnClickListener(new TravellerDeleteTourOnClickListener());
			break;
		case TOUR_DELETED_BY_TRAVELLER:
			break;
		case TOUR_DELETED_BY_GUIDE:
			break;
		}

	}

	private class UpdateBookingStatusTask extends
			AsyncTask<Integer, Void, Void> {
		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(
					TravellerBookingStatusActivity.this);
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

	private class TravellerDeleteTourOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			UpdateBookingStatusTask updateBookingStatusTask = new UpdateBookingStatusTask();
			updateBookingStatusTask
					.execute(BookingStatus.TOUR_DELETED_BY_TRAVELLER
							.getNumVal());
		}
	}

	private class TravellerBookTourOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent bookNowIntent = new Intent(
					TravellerBookingStatusActivity.this,
					TravellerPayBookingSummaryActivity.class);
			bookNowIntent.putExtra("jsonBooking", jsonBooking);
			startActivity(bookNowIntent);
		}
	}

	private class TravellerCancelTourBeforeBookingOnClickListener implements
			OnClickListener {
		@Override
		public void onClick(View v) {
			UpdateBookingStatusTask updateBookingStatusTask = new UpdateBookingStatusTask();
			updateBookingStatusTask
					.execute(BookingStatus.TOUR_CANCELLED_BY_TRAVELLER_BFRORE_BOOKING
							.getNumVal());
		}
	}

	private class TravellerCancelTourAfterBookingBeforeChargedOnClickListener
			implements OnClickListener {
		@Override
		public void onClick(View v) {
			UpdateBookingStatusTask updateBookingStatusTask = new UpdateBookingStatusTask();
			updateBookingStatusTask
					.execute(BookingStatus.BOOKING_CANCELLED_BY_TRAVELLER_BEFORE_CHARGED
							.getNumVal());
		}
	}

	private class TravellerCancelTourAfterBookingAfterChargedOnClickListener
			implements OnClickListener {
		@Override
		public void onClick(View v) {
			UpdateBookingStatusTask updateBookingStatusTask = new UpdateBookingStatusTask();
			updateBookingStatusTask
					.execute(BookingStatus.BOOKING_CANCELLED_BY_TRAVELLER_AFTER_CHARGED
							.getNumVal());
		}
	}

	private class TravellerReviewTourOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			// TODO delete the request
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
