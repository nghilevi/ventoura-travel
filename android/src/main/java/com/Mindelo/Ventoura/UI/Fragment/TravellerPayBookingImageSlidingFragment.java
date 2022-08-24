package com.Mindelo.Ventoura.UI.Fragment;

import com.Mindelo.Ventoura.UI.Activity.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class TravellerPayBookingImageSlidingFragment extends Fragment {

	private Integer resId;
	
	public static TravellerPayBookingImageSlidingFragment newInstance(Integer resId){
		TravellerPayBookingImageSlidingFragment fragment=new TravellerPayBookingImageSlidingFragment();
		Bundle args = new Bundle();
		args.putInt("resId", resId);
		fragment.setArguments(args);
		return fragment;	
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		resId=args.getInt("resId");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=null;
		view=inflater.inflate(R.layout.adapter_traveller_pay_booking_credict_card_image_sliding_vp_item, container,false);
		ImageView imageView=(ImageView) view.findViewById(R.id.iv_pay_booking_slide_image);
		imageView.setBackgroundResource(resId);
		return view;
	}


}
