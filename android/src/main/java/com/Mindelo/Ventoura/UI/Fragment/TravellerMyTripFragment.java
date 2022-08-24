package com.Mindelo.Ventoura.UI.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.Mindelo.Ventoura.Constant.BroadcastConstant;
import com.Mindelo.Ventoura.Enum.TravellerTripListViewItemType;
import com.Mindelo.Ventoura.Ghost.IService.IBookingScheduleService;
import com.Mindelo.Ventoura.Ghost.Service.BookingScheduleService;
import com.Mindelo.Ventoura.JSONEntity.JSONBooking;
import com.Mindelo.Ventoura.JSONEntity.JSONBookingList;
import com.Mindelo.Ventoura.JSONEntity.JSONTravellerSchedule;
import com.Mindelo.Ventoura.JSONEntity.JSONTravellerScheduleList;
import com.Mindelo.Ventoura.UI.Activity.R;
import com.Mindelo.Ventoura.UI.Activity.TravellerAddTripActivity;
import com.Mindelo.Ventoura.UI.Activity.TravellerBookingStatusActivity;
import com.Mindelo.Ventoura.UI.Activity.TravellerPortalActivity;
import com.Mindelo.Ventoura.UI.Adapter.TravellerTripListViewAdapter;
import com.Mindelo.Ventoura.UI.View.BottomPopupMenuAction;
import com.Mindelo.Ventoura.UI.View.PopupActionItem;

public class TravellerMyTripFragment extends Fragment {

	private static final String TAG = "TravellerMyTripFragment";

	/*
	 * utility instances
	 */
	private TravellerPortalActivity activity;
	private IBookingScheduleService bookingScheduleService;
	private long travellerId;
	private JSONTravellerSchedule selectedSchedule;
	private int selectedIndex;

	/*
	 * views
	 */
	public static final String ITEMTYPE = "ITEMTYPE";
	public static final String BOOKING = "BOOKING";
	public static final String TRAVELLER_SCHEDULE = "TRAVELLER_SCHEDULE";

	/*
	 * global variables
	 */
	private ListView lvOfToursSchdeluer;
	private TravellerTripListViewAdapter travellerTripListViewAdapter;
	private List<Map<String, Object>> travellerTripListViewItems;
	private Button optionButton;

	/*
	 * bottom menu popup windows
	 */
	private static final int ID_BOTTOM_MENU_DELTEE_TOUR = 1;
	private BottomPopupMenuAction bottomMenuAction;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity = (TravellerPortalActivity) getActivity();

		bookingScheduleService = new BookingScheduleService(activity);
		travellerId = activity.getTravellerId();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);

		View view = inflater.inflate(R.layout.fragment_traveller_mytrip,
				container, false);

		lvOfToursSchdeluer = (ListView) view
				.findViewById(R.id.lv_activity_tours_scheduler);
		travellerTripListViewItems = new ArrayList<Map<String, Object>>();

		lvOfToursSchdeluer
				.setOnItemClickListener(new TripItemOnclickListener());
		lvOfToursSchdeluer
				.setOnItemLongClickListener(new TripItemLongClickListener());

		optionButton = (Button) view.findViewById(R.id.btn_action);
		optionButton.setText("Add City");
		optionButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(activity,
						TravellerAddTripActivity.class));
			}
		});

		initPopupBottomMenu();

		/*
		 * load old trips
		 */
		loadTripsFromDB();
		LoadSchedulesTask loadSchedulesTask = new LoadSchedulesTask();
		loadSchedulesTask.execute();

		return view;
	}

	private class TripItemLongClickListener implements OnItemLongClickListener {
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			selectedIndex = position;
			Map<String, Object> trip = travellerTripListViewItems.get(position);
			TravellerTripListViewItemType itemType = (TravellerTripListViewItemType) trip
					.get(TravellerMyTripFragment.ITEMTYPE);
			if (itemType == TravellerTripListViewItemType.BOOKING) {
				// long click a booking
			} else {
				selectedSchedule = (JSONTravellerSchedule) trip
						.get(TravellerMyTripFragment.TRAVELLER_SCHEDULE);
				bottomMenuAction.show(view);
			}
			return true;
		}
	}

	private class TripItemOnclickListener implements OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Map<String, Object> trip = travellerTripListViewItems.get(position);
			TravellerTripListViewItemType itemType = (TravellerTripListViewItemType) trip
					.get(TravellerMyTripFragment.ITEMTYPE);
			if (itemType == TravellerTripListViewItemType.BOOKING) {

				JSONBooking selectedBooking = (JSONBooking) trip
						.get(TravellerMyTripFragment.BOOKING);

				Intent intent = new Intent();
				intent.putExtra("jsonBooking", selectedBooking);
				intent.setClass(activity, TravellerBookingStatusActivity.class);
				startActivity(intent);

			} else if (itemType == TravellerTripListViewItemType.TRAVELLER_SCHEDULE) {
				// TODO ? click a traveller's own shedule item
			}
		}
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		activity.unregisterReceiver(receiver);
	}

	@Override
	public void onResume() {
		super.onResume();
		activity.registerReceiver(receiver, new IntentFilter(
				BroadcastConstant.USER_MATCH_IMAGES_UPDATED_ACTION));
		activity.registerReceiver(receiver, new IntentFilter(
				BroadcastConstant.USER_NEW_TRIP_ADDED_ACTION));
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			loadTripsFromDB();
		}
	};

	private void loadTripsFromDB() {
		JSONBookingList bookingsList = bookingScheduleService
				.getTravellerBookingsListFromDB(travellerId);
		JSONTravellerScheduleList travellerScheduleList = bookingScheduleService
				.getTravellerScheduleListFromDB(travellerId);
		travellerTripListViewItems.clear();
		if (bookingsList != null) {

			for (JSONBooking booking : bookingsList.getBookings()) {
				/*
				 * assume the guide of the booking is in the traveller's match
				 */
				Map<String, Object> item = new HashMap<String, Object>();
				item.put(TravellerMyTripFragment.ITEMTYPE,
						TravellerTripListViewItemType.BOOKING);
				item.put(TravellerMyTripFragment.BOOKING, booking);
				travellerTripListViewItems.add(item);
			}
		}
		if (travellerScheduleList != null) {
			for (JSONTravellerSchedule travellerSchedule : travellerScheduleList
					.getTravellerScheduleList()) {
				Map<String, Object> item = new HashMap<String, Object>();
				item.put(TravellerMyTripFragment.ITEMTYPE,
						TravellerTripListViewItemType.TRAVELLER_SCHEDULE);
				item.put(TravellerMyTripFragment.TRAVELLER_SCHEDULE,
						travellerSchedule);
				travellerTripListViewItems.add(item);
			}
		}
		// notify data changed, update the trip list
		travellerTripListViewAdapter = new TravellerTripListViewAdapter(
				activity, travellerTripListViewItems);
		lvOfToursSchdeluer.setAdapter(travellerTripListViewAdapter);

	}

	/**
	 * Network tasks for creating load bookings and shedules
	 */
	private class LoadSchedulesTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				boolean result = true;

				if (!bookingScheduleService
						.getTravellerBookingsListFromServer(travellerId)) {
					result = false;
				}

				if (!bookingScheduleService
						.getTravellerScheduleListFromServer(travellerId)) {
					result = false;
				}
				return result;
			} catch (Exception e) {
				e.printStackTrace();
				Log.e(TAG, "Traveller Schedules Download Error");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result != null && result.booleanValue()) {
				loadTripsFromDB();
			}
		}
	}

	/*
	 * pop up menus
	 */
	private void initPopupBottomMenu() {
		PopupActionItem deleteItem = new PopupActionItem(
				ID_BOTTOM_MENU_DELTEE_TOUR, "Delete Tour");
		bottomMenuAction = new BottomPopupMenuAction(activity);
		bottomMenuAction.addActionItem(deleteItem);
		bottomMenuAction
				.setOnActionItemClickListener(new BottomPopupMenuAction.OnActionItemClickListener() {
					@Override
					public void onItemClick(BottomPopupMenuAction source,
							int pos, int actionId) {
						switch (actionId) {
						case ID_BOTTOM_MENU_DELTEE_TOUR:
							DeleteTripTask deleteTripTask = new DeleteTripTask();
							deleteTripTask.execute();
							travellerTripListViewItems.remove(selectedIndex);
							travellerTripListViewAdapter.notifyDataSetChanged();
							break;
						}
					}
				});
		bottomMenuAction
				.setOnDismissListener(new BottomPopupMenuAction.OnDismissListener() {
					@Override
					public void onDismiss() {

					}
				});
	}

	/**
	 * Network tasks for add trips
	 */

	private class DeleteTripTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			return bookingScheduleService.deleteTravellerSchdeule(travellerId,
					selectedSchedule.getId());
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (!result.booleanValue()) {
				Toast.makeText(activity,
						"Error happened when deleting the tour.",
						Toast.LENGTH_SHORT).show();
			} else {
			}
		}
	}
}
