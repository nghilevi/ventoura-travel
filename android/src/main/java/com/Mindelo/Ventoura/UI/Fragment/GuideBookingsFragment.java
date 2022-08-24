package com.Mindelo.Ventoura.UI.Fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.Mindelo.Ventoura.Constant.BroadcastConstant;
import com.Mindelo.Ventoura.Constant.VentouraConstant;
import com.Mindelo.Ventoura.Enum.BookingStatus;
import com.Mindelo.Ventoura.Ghost.IService.IBookingScheduleService;
import com.Mindelo.Ventoura.Ghost.Service.BookingScheduleService;
import com.Mindelo.Ventoura.JSONEntity.JSONBooking;
import com.Mindelo.Ventoura.JSONEntity.JSONBookingList;
import com.Mindelo.Ventoura.UI.Activity.GuideBookingFilterActivity;
import com.Mindelo.Ventoura.UI.Activity.GuideBookingResponseRequestActivity;
import com.Mindelo.Ventoura.UI.Activity.GuideBookingStatusActivity;
import com.Mindelo.Ventoura.UI.Activity.GuideMessageFilterActivity;
import com.Mindelo.Ventoura.UI.Activity.GuidePortalActivity;
import com.Mindelo.Ventoura.UI.Activity.R;
import com.Mindelo.Ventoura.UI.Adapter.GuideBookingListViewAdapter;
import com.google.android.gms.plus.model.people.Person.Gender;

public class GuideBookingsFragment extends Fragment implements
		OnItemClickListener {

	private static final String TAG = "GuideBookingsFragment";

	/*
	 * views
	 */
	private ListView lvOfTours;
	private List<JSONBooking> bookingListViewItems;
	private GuideBookingListViewAdapter bookingListViewAdapter;

	/*
	 * utility instances
	 */
	private GuidePortalActivity activity;
	private IBookingScheduleService bookingService;
	private long guideId;
	private JSONBooking selectedBooking;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity = (GuidePortalActivity) getActivity();

		bookingService = new BookingScheduleService(activity);
		guideId = activity.getGuideId();

		bookingListViewItems = new ArrayList<JSONBooking>();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);

		View view = inflater.inflate(R.layout.fragment_guide_bookings,
				container, false);

		lvOfTours = (ListView) view
				.findViewById(R.id.lv_activity_guide_bookings);
		lvOfTours.setOnItemClickListener(this);

		/*
		 * init the right option button
		 */
		Button optionButton = (Button) view.findViewById(R.id.btn_action);
		optionButton.setText("Filter");
		optionButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(),
						GuideBookingFilterActivity.class));
			}
		});

		/*
		 * load original bookings
		 */
		loadBookingsFromDB();
		LoadBookingsTask loadBookingsTask = new LoadBookingsTask();
		loadBookingsTask.execute();
		
		return view;
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		activity.unregisterReceiver(receiver);
		activity.unregisterReceiver(bookingFilterUpdatedReveiver);
	}

	@Override
	public void onResume() {
		super.onResume();
		activity.registerReceiver(receiver, new IntentFilter(
				BroadcastConstant.USER_BOOKING_FILTER_UPDATED_ACTION));
		activity.registerReceiver(bookingFilterUpdatedReveiver, new IntentFilter(
				BroadcastConstant.USER_MATCH_IMAGES_UPDATED_ACTION));
	}

	/**
	 * Message filter updated receiver
	 */
	private BroadcastReceiver bookingFilterUpdatedReveiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// reload the matches from DB
			loadBookingsFromDB();
		}
	};

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			loadBookingsFromDB();
		}
	};

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	/*
	 * list view click listeners
	 */
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		selectedBooking = bookingListViewItems.get(position);

		Intent intent = new Intent();
		intent.putExtra("jsonBooking", selectedBooking);

		BookingStatus bookingStatus = BookingStatus.values()[selectedBooking
				.getBookingStatus()];
		switch (bookingStatus) {
		case REQUEST_BY_TRAVELLER:
			intent.setClass(activity, GuideBookingResponseRequestActivity.class);
			startActivity(intent);
			break;
		default:
			intent.setClass(activity, GuideBookingStatusActivity.class);
			startActivity(intent);
			break;
		}
	}

	private void loadBookingsFromDB() {

		JSONBookingList bookingList = bookingService
				.getGuideBookingsListFromDB(guideId);
		bookingListViewItems.clear();
		if (bookingList != null) {
			for (JSONBooking booking : bookingList.getBookings()) {

				/*
				 * apply booking filter
				 */
				if (!applyBookingFilter(booking)) {
					continue;
				}
				/*
				 * assume the traveller of the booking is in the guide's match
				 */
				bookingListViewItems.add(booking);
			}
		}
		bookingListViewAdapter = new GuideBookingListViewAdapter(activity,
				bookingListViewItems);

		lvOfTours.setAdapter(bookingListViewAdapter);
		Log.i(TAG, "Load Guide Bookings");
	}

	/**
	 * Network tasks for creating load bookings
	 */
	private class LoadBookingsTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {

			try {
				return bookingService.getGuideBookingListFromServer(guideId);
			} catch (Exception e) {
				Log.e(TAG, "Guide bookings Download Error");
			}
			return null;

		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result != null && result.booleanValue()) {
				loadBookingsFromDB();
			}
		}
	}

	private boolean applyBookingFilter(JSONBooking booking) {

		SharedPreferences sharedPre = getActivity().getSharedPreferences(
				VentouraConstant.SHARED_PREFERENCE_VENTOURA,
				Context.MODE_PRIVATE);

		boolean filterFlag = true;

		filterFlag = sharedPre.getBoolean(
				VentouraConstant.PRE_BOOKING_FILTER_GENDER_FEMALE, true);
		if (filterFlag == false) {
			if (booking.getTravellerGender() == Gender.FEMALE) {
				return false;
			}
		}
		filterFlag = sharedPre.getBoolean(
				VentouraConstant.PRE_BOOKING_FILTER_GENDER_MALE, true);
		if (filterFlag == false) {
			if (booking.getTravellerGender() == Gender.MALE) {
				return false;
			}
		}

		filterFlag = sharedPre.getBoolean(
				VentouraConstant.PRE_BOOKING_FILTER_BOOK_REQUEST, true);
		if (filterFlag) {
			if (booking.getBookingStatus() == BookingStatus.REQUEST_BY_TRAVELLER
					.getNumVal()) {
				return true;
			}
		}

		filterFlag = sharedPre.getBoolean(
				VentouraConstant.PRE_BOOKING_FILTER_BOOK_NOT_PAID, true);
		if (filterFlag) {
			if (booking.getBookingStatus() == BookingStatus.REQUEST_ACCEPTED_BY_LOCAL
					.getNumVal()) {
				return true;
			}
		}

		filterFlag = sharedPre.getBoolean(
				VentouraConstant.PRE_BOOKING_FILTER_BOOK_PAID, true);
		if (filterFlag) {
			if (booking.getBookingStatus() == BookingStatus.TOUR_BOOKED_BY_TRAVELLER
					.getNumVal()
					|| booking.getBookingStatus() == BookingStatus.TOUR_CHARGED_SUCCESSFULLY
							.getNumVal()) {
				return true;
			}
		}

		filterFlag = sharedPre.getBoolean(
				VentouraConstant.PRE_BOOKING_FILTER_BOOK_CANCELLED, true);
		if (filterFlag) {
			if (booking.getBookingStatus() == BookingStatus.REQUEST_CANCELLED_BY_TRAVELLER
					.getNumVal()
					|| booking.getBookingStatus() == BookingStatus.TOUR_CANCELLED_BY_TRAVELLER_BFRORE_BOOKING
							.getNumVal()
					|| booking.getBookingStatus() == BookingStatus.BOOKING_CANCELLED_BY_TRAVELLER_AFTER_CHARGED
							.getNumVal()
					|| booking.getBookingStatus() == BookingStatus.BOOKING_CANCELLED_BY_TRAVELLER_BEFORE_CHARGED
							.getNumVal()
					|| booking.getBookingStatus() == BookingStatus.BOOKING_LAPSED_DUE_TO_PAYMENT_FAILURE
							.getNumVal()
					|| booking.getBookingStatus() == BookingStatus.TOUR_TIME_OUT_BEFORE_BOOKING
							.getNumVal()) {
				return true;
			}
		}

		filterFlag = sharedPre.getBoolean(
				VentouraConstant.PRE_BOOKING_FILTER_BOOK_COMPLETE, true);
		if (filterFlag) {
			if (booking.getBookingStatus() == BookingStatus.TOUR_REGISTERED
					.getNumVal()
					|| booking.getBookingStatus() == BookingStatus.TOUR_REVIEWED
							.getNumVal()
					|| booking.getBookingStatus() == BookingStatus.TOUR_DISPUTED
							.getNumVal()) {
				return true;
			}
		}

		filterFlag = sharedPre.getBoolean(
				VentouraConstant.PRE_BOOKING_FILTER_BOOK_ERROR, true);
		if (filterFlag) {
			if (booking.getBookingStatus() == BookingStatus.BOOKING_AUTHORIZATION_FAILED
					.getNumVal()
					|| booking.getBookingStatus() == BookingStatus.BOOKING_CAPTURED_FAILED
							.getNumVal()) {
				return true;
			}
		}

		return false;
	}
}
