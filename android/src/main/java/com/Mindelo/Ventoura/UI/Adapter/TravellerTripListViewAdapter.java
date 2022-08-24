package com.Mindelo.Ventoura.UI.Adapter;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Entity.City;
import com.Mindelo.Ventoura.Entity.ImageMatch;
import com.Mindelo.Ventoura.Enum.BookingStatus;
import com.Mindelo.Ventoura.Enum.TravellerTripListViewItemType;
import com.Mindelo.Ventoura.Enum.UserRole;
import com.Mindelo.Ventoura.Ghost.IService.ICityService;
import com.Mindelo.Ventoura.Ghost.IService.IMatchesService;
import com.Mindelo.Ventoura.Ghost.Service.CityService;
import com.Mindelo.Ventoura.Ghost.Service.MatchesService;
import com.Mindelo.Ventoura.JSONEntity.JSONBooking;
import com.Mindelo.Ventoura.JSONEntity.JSONTravellerSchedule;
import com.Mindelo.Ventoura.UI.Activity.R;
import com.Mindelo.Ventoura.UI.Activity.TravellerPortalActivity;
import com.Mindelo.Ventoura.UI.Fragment.TravellerMyTripFragment;
import com.Mindelo.Ventoura.UI.View.RoundedImageView;
import com.Mindelo.Ventoura.Util.BitmapUtil;
import com.Mindelo.Ventoura.Util.DateTimeUtil;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TravellerTripListViewAdapter extends BaseAdapter {

	private final static String TAG = "com.Mindelo.Ventoura.UI.Adapter.ToursSchedulerListViewAdapter";

	private TravellerPortalActivity activity;
	private LayoutInflater inflater;
	private List<Map<String, Object>> travellerTripListViewItems;
	private ICityService cityService;
	private IMatchesService matchesService;

	/*
	 * Global variables
	 */
	private City city;
	String tour_start_time;
	String tour_end_time;

	public TravellerTripListViewAdapter(TravellerPortalActivity activity,
			List<Map<String, Object>> travellerTripListViewItems) {
		super();
		this.activity = activity;
		this.travellerTripListViewItems = travellerTripListViewItems;
		this.inflater = (LayoutInflater) this.activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		cityService = new CityService(activity);
		matchesService = new MatchesService(activity);
	}

	public void setToursSchedulerListViewItems(
			List<Map<String, Object>> travellerTripListViewItems) {
		this.travellerTripListViewItems = travellerTripListViewItems;

	}

	public int getCount() {
		return travellerTripListViewItems.size();
	}

	public Object getItem(int position) {
		return travellerTripListViewItems.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from(activity).inflate(
					R.layout.adapter_row_trip_or_booking_item, null);

			holder = new ViewHolder();

			holder.headImageView = (RoundedImageView) convertView
					.findViewById(R.id.iv_trip_booking_list_head_image);
			holder.tvNameOrCity = (TextView) convertView
					.findViewById(R.id.tv_trip_booking_list_name);
			holder.tvDate = (TextView) convertView
					.findViewById(R.id.tv_trip_booking_list_date);
			holder.tvIsNew = (TextView) convertView
					.findViewById(R.id.tv_trip_booking_item_new);
			holder.bookingStatusImageView = (ImageView) convertView
					.findViewById(R.id.iv_trip_booking_list_item_status);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Map<String, Object> lvItem = travellerTripListViewItems.get(position);
		TravellerTripListViewItemType itemType = (TravellerTripListViewItemType) lvItem
				.get(TravellerMyTripFragment.ITEMTYPE);
		if (itemType == TravellerTripListViewItemType.BOOKING) {

			/*
			 * display a user's booking
			 */

			JSONBooking booking = (JSONBooking) lvItem
					.get(TravellerMyTripFragment.BOOKING);

			ImageMatch headImage = matchesService.getSingleMatchImageFromDB(
					booking.getGuideId(), UserRole.GUIDE.getNumVal());
			if (headImage != null) {
				Bitmap bitMap = BitmapUtil.byteArrayToBitMap(
						headImage.getImageContent(),
						ConfigurationConstant.SMALL_USER_PORTAL_IMAGE_WIDTH,
						ConfigurationConstant.SMALL_USER_PORTAL_IMAGE_HEIGHT);
				holder.headImageView.setImageBitmap(bitMap);
			}

			holder.tvNameOrCity.setText(booking.getGuideFirstname());
			holder.tvDate.setText(DateTimeUtil
					.fromDateToString_DD_MMM_YYYY(DateTimeUtil
							.fromStringToDate(booking.getTourDate())));

			Log.i(TAG, booking.getBookingStatus() + "");
			/*
			 * set the icons according to the booking status
			 */
			BookingStatus bookingStatus = BookingStatus.values()[booking
					.getBookingStatus()];
			switch (bookingStatus) {
			case REQUEST_BY_TRAVELLER:
				holder.bookingStatusImageView
						.setImageResource(R.drawable.icon_booking_waiting);
				break;
			case REQUEST_CANCELLED_BY_TRAVELLER:
				break;
			case REQUEST_DECLINED_BY_LOCAL:
				holder.bookingStatusImageView
						.setImageResource(R.drawable.icon_booking_refused);
				break;
			case REQUEST_ACCEPTED_BY_LOCAL:
				holder.bookingStatusImageView
						.setImageResource(R.drawable.icon_booking_notpaid);
				break;
			case TOUR_CANCELLED_BY_TRAVELLER_BFRORE_BOOKING:
				break;
			case TOUR_CANCELLED_BY_GUIDE_BFRORE_BOOKING:
				holder.bookingStatusImageView
						.setImageResource(R.drawable.icon_booking_cancelled);
				break;
			case TOUR_TIME_OUT_BEFORE_BOOKING:
				holder.bookingStatusImageView
						.setImageResource(R.drawable.icon_booking_cancelled);
				break;
			case TOUR_BOOKED_BY_TRAVELLER:
				holder.bookingStatusImageView
						.setImageResource(R.drawable.icon_booking_paid);
				break;
			case TOUR_CHARGED_SUCCESSFULLY:
				holder.bookingStatusImageView
						.setImageResource(R.drawable.icon_booking_paid);
				break;
			case BOOKING_CANCELLED_BY_TRAVELLER_BEFORE_CHARGED:
				break;
			case BOOKING_CANCELLED_BY_TRAVELLER_AFTER_CHARGED:
				break;
			case BOOKING_AUTHORIZATION_FAILED:
				holder.bookingStatusImageView
						.setImageResource(R.drawable.icon_booking_error);
				break;
			case BOOKING_CAPTURED_FAILED:
				holder.bookingStatusImageView
						.setImageResource(R.drawable.icon_booking_error);
				break;
			case BOOKING_LAPSED_DUE_TO_PAYMENT_FAILURE:
				holder.bookingStatusImageView
						.setImageResource(R.drawable.icon_booking_cancelled);
				break;
			case BOOKING_CANCELLED_BY_LOCAL_BEFORE_CHARGED:
				holder.bookingStatusImageView
						.setImageResource(R.drawable.icon_booking_cancelled);
				break;
			case BOOKING_CANCELLED_BY_LOCAL_AFTER_CHARGED:
				holder.bookingStatusImageView
						.setImageResource(R.drawable.icon_booking_cancelled);
				break;
			case TOUR_REGISTERED:
				holder.bookingStatusImageView
						.setImageResource(R.drawable.icon_booking_complete);
				break;
			case TOUR_REVIEWED:
				// TODO we need reviewed
				holder.bookingStatusImageView
						.setImageResource(R.drawable.icon_booking_complete);
				break;
			case TOUR_DISPUTED:
				// TODO We need dispute
				holder.bookingStatusImageView
						.setImageResource(R.drawable.icon_booking_complete);
				break;
			}
		} else if (itemType == TravellerTripListViewItemType.TRAVELLER_SCHEDULE) {

			/*
			 * display a user's traveller schedule
			 */

			JSONTravellerSchedule travellerScheduleItem = (JSONTravellerSchedule) lvItem
					.get(TravellerMyTripFragment.TRAVELLER_SCHEDULE);

			/*
			 * set the country flag
			 */
			try {
				InputStream ims;
				ims = activity.getAssets().open(
						ConfigurationConstant.VENTOURA_ASSET_COUNTRY_FLAG + "/"
								+ travellerScheduleItem.getCountry() + ".png");
				Drawable countryFlag = Drawable.createFromStream(ims, null);
				holder.bookingStatusImageView.setImageDrawable(countryFlag);
				holder.headImageView.setImageDrawable(countryFlag);

			} catch (IOException e) {
				Log.e(TAG, "trip loaded country flag error ");
				e.printStackTrace();
			}

			city = cityService.getCityById(travellerScheduleItem.getCity());
			holder.tvNameOrCity.setText(city.getCityName());

			try {
				String tourDateStr;
				tourDateStr = DateTimeUtil
						.fromDateToString_DD_MMM_YYYY(DateTimeUtil
								.fromStringToDATE_DDMMYYYY((travellerScheduleItem
										.getStartTime())))
						+ " - "
						+ DateTimeUtil
								.fromDateToString_DD_MMM_YYYY(DateTimeUtil
										.fromStringToDATE_DDMMYYYY((travellerScheduleItem
												.getEndTime())));
				holder.tvDate.setText(tourDateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}

		return convertView;
	}

	static class ViewHolder {
		TextView tvNameOrCity;
		TextView tvDate;
		TextView tvIsNew; // this is the text view place holder
		ImageView bookingStatusImageView;
		RoundedImageView headImageView;
	}

}
