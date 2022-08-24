package com.Mindelo.Ventoura.UI.Adapter;

import java.util.ArrayList;
import java.util.List;

import com.Mindelo.Ventoura.UI.Activity.R;
import com.Mindelo.Ventoura.UI.Fragment.TravellerPayBookingImageSlidingFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TravellerPayBookingCreditCardAdapter extends FragmentPagerAdapter{

	private List<Integer> imgResid;
	
	public TravellerPayBookingCreditCardAdapter(FragmentManager fm) {
		super(fm);
		imgResid=new ArrayList<>();
		imgResid.add(R.drawable.traveller_pay_booking_lock);
		imgResid.add(R.drawable.traveller_pay_booking_card_number);
		imgResid.add(R.drawable.traveller_pay_booking_expiry_date);
		imgResid.add(R.drawable.traveller_pay_booking_ccv);
	}

	@Override
	public Fragment getItem(int location) {
		return TravellerPayBookingImageSlidingFragment.newInstance(imgResid.get(location));
	}

	@Override
	public int getCount() {
		return imgResid.size();
	}

}
