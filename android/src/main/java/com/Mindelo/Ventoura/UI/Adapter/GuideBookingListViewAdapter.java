package com.Mindelo.Ventoura.UI.Adapter;

import java.util.List;

import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Entity.ImageMatch;
import com.Mindelo.Ventoura.Enum.BookingStatus;
import com.Mindelo.Ventoura.Enum.UserRole;
import com.Mindelo.Ventoura.Ghost.IService.IMatchesService;
import com.Mindelo.Ventoura.Ghost.Service.MatchesService;
import com.Mindelo.Ventoura.JSONEntity.JSONBooking;
import com.Mindelo.Ventoura.UI.Activity.R;
import com.Mindelo.Ventoura.UI.View.RoundedImageView;
import com.Mindelo.Ventoura.Util.BitmapUtil;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GuideBookingListViewAdapter extends BaseAdapter {

	private final static String TAG = "GuideBookingListViewAdapter";

	private Context context;

	private List<JSONBooking> guideBookings;
	private IMatchesService matchesService;

	public void setToursListViewItems(List<JSONBooking> guideBookings) {
		this.guideBookings = guideBookings;
	}

	public GuideBookingListViewAdapter(Context context,
			List<JSONBooking> guideBookings) {
		super();
		this.context = context;
		this.guideBookings = guideBookings;
		matchesService = new MatchesService(context);
	}

	public int getCount() {
		return guideBookings.size();
	}

	public Object getItem(int position) {
		return guideBookings.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		// first get the singleBooking from ToursListViewItem and judge the
		// selected
		// status
		ViewHolder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
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

		JSONBooking tmpBooking = guideBookings.get(position);

		String name = tmpBooking.getTravellerFirstname();
		String date = tmpBooking.getTourDate();
		String price = "$" + tmpBooking.getTourPrice();
		holder.tvNameOrCity.setText(name);
		holder.tvDate.setText(date);
		// holder.tvPrice.setText(price);
		// head image
		ImageMatch headImage = matchesService.getSingleMatchImageFromDB(
				tmpBooking.getTravellerId(), UserRole.TRAVELLER.getNumVal());
		if (headImage != null) {
			Bitmap bitMap = BitmapUtil.byteArrayToBitMap(
					headImage.getImageContent(),
					ConfigurationConstant.SMALL_USER_PORTAL_IMAGE_WIDTH,
					ConfigurationConstant.SMALL_USER_PORTAL_IMAGE_HEIGHT);
			holder.headImageView.setImageBitmap(bitMap);
		}

		Log.i(TAG, tmpBooking.getBookingStatus() + "");

		/*
		 * set the icons according to the booking status
		 */
		BookingStatus bookingStatus = BookingStatus.values()[tmpBooking
				.getBookingStatus()];
		switch (bookingStatus) {
		case REQUEST_BY_TRAVELLER:
			holder.bookingStatusImageView
					.setImageResource(R.drawable.icon_booking_request);
			break;
		case REQUEST_CANCELLED_BY_TRAVELLER:
			holder.bookingStatusImageView
					.setImageResource(R.drawable.icon_booking_cancelled);
			break;
		case REQUEST_DECLINED_BY_LOCAL:
			break;
		case REQUEST_ACCEPTED_BY_LOCAL:
			holder.bookingStatusImageView
					.setImageResource(R.drawable.icon_booking_notpaid);
			break;
		case TOUR_CANCELLED_BY_TRAVELLER_BFRORE_BOOKING:
			holder.bookingStatusImageView
					.setImageResource(R.drawable.icon_booking_cancelled);
			break;
		case TOUR_CANCELLED_BY_GUIDE_BFRORE_BOOKING:
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
			holder.bookingStatusImageView
					.setImageResource(R.drawable.icon_booking_cancelled);
			break;
		case BOOKING_CANCELLED_BY_TRAVELLER_AFTER_CHARGED:
			holder.bookingStatusImageView
					.setImageResource(R.drawable.icon_booking_cancelled);
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
			break;
		case BOOKING_CANCELLED_BY_LOCAL_AFTER_CHARGED:
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

		// use the following codes to set the more text. seems not needed
		/*
		 * if (bookingStatus == BookingStatus.NEEDACCEPT) {
		 * holder.tvIsNew.setVisibility(View.VISIBLE); }else{
		 * holder.tvIsNew.setVisibility(View.INVISIBLE); }
		 */

		return convertView;
	}

	static class ViewHolder {
		TextView tvNameOrCity;
		TextView tvDate;
		TextView tvIsNew; // this is the textview placeholder, set it as visible
							// as you need
		ImageView bookingStatusImageView;
		RoundedImageView headImageView;
	}
}
