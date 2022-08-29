package com.Mindelo.Ventoura.UI.ViewHolder;

import android.view.View;
import android.widget.TextView;

import com.Mindelo.Ventoura.UI.Activity.R;
import com.Mindelo.Ventoura.UI.View.RoundedImageView;

public class TravellerTripBookingViewHolder {

	public RoundedImageView roundedImage;

	public TextView guideName, tourDate, tourPrice, bookingStatus;
	
	public TravellerTripBookingViewHolder(View view){
		roundedImage=(RoundedImageView) view.findViewById(R.id.tours_scheduler_character_rounded_image);		
		guideName=(TextView) view.findViewById(R.id.tv_tours_scheduler_character_name);
		tourDate=(TextView) view.findViewById(R.id.tv_tours_character_time_interval);	
		tourPrice=(TextView) view.findViewById(R.id.tv_tours_character_money);	
		bookingStatus=(TextView) view.findViewById(R.id.tv_tours_character_booking_status);	
	}
}
