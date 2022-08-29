package com.Mindelo.Ventoura.UI.ViewHolder;

import android.view.View;
import android.widget.TextView;

import com.Mindelo.Ventoura.UI.Activity.R;
import com.Mindelo.Ventoura.UI.View.RoundedImageView;

public class TravellerTripTravellerScheduleViewHolder {

	public RoundedImageView roundedImage;
	
	public TextView cityName,numberOfDays,tourDate,tourUnit;
	
	public TravellerTripTravellerScheduleViewHolder(View view){
		roundedImage=(RoundedImageView) view.findViewById(R.id.tours_scheduler_sightpoint_rounded_image);
	
		cityName=(TextView) view.findViewById(R.id.tv_tours_scheduler_sight_point_name);
		
		numberOfDays=(TextView) view.findViewById(R.id.tv_tours_scheduler_time_value);
		tourUnit=(TextView) view.findViewById(R.id.tv_tours_scheduler_time_unit);
		
		tourDate=(TextView) view.findViewById(R.id.tv_tours_scheduler_time_interval);
	}
	
}
