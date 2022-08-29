package com.Mindelo.Ventoura.UI.Adapter;

import java.util.ArrayList;
import java.util.List;

import com.Mindelo.Ventoura.Enum.PaymentMethod;
import com.Mindelo.Ventoura.Model.PaymentMethodSlidingFragmentAdapterItem;
import com.Mindelo.Ventoura.UI.Activity.R;
import com.Mindelo.Ventoura.UI.Fragment.GuidePaymentMethodSlidingFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class PaymentMethodSlidingFragmentAdapter extends FragmentPagerAdapter {

	protected static List<PaymentMethodSlidingFragmentAdapterItem> items = new ArrayList<PaymentMethodSlidingFragmentAdapterItem>();

	static {
		// first item
		PaymentMethodSlidingFragmentAdapterItem item = new PaymentMethodSlidingFragmentAdapterItem();
		item.setBgColorResId(R.color.ventoura_color);
		item.setTopicTitle("Get paid by cash ?");
		List<String> descriptionContents = new ArrayList<String>();
		descriptionContents
				.add("- Chat and connect with travellers at your destination.");
		descriptionContents.add("- Take a tour and live like a local");
		item.setDescriptionContents(descriptionContents);
		item.setPaymentMethodButtonTitle("Cash");
		item.setPaymentMethod(PaymentMethod.CASH);
		item.setNavigationIVBgResId(R.drawable.arrow_to_right_black);
		item.setPaymentMethodButtonSelectorResid(R.drawable.selector_btn_login_traveller);
		items.add(item);

		// second item
		item = new PaymentMethodSlidingFragmentAdapterItem();
		item.setBgColorResId(R.color.ventoura_blue);
		item.setTopicTitle("Get paid by card ?");
		descriptionContents = new ArrayList<String>();
		descriptionContents.add("- Craft your own tour expericnce,earn cash.");
		descriptionContents
				.add("- Make international friends,in a setting you create");
		descriptionContents.add("- No schedule,no boss,do it your way");
		item.setDescriptionContents(descriptionContents);
		item.setPaymentMethodButtonTitle("Card");
		item.setPaymentMethod(PaymentMethod.CARD);
		item.setNavigationIVBgResId(R.drawable.arrow_to_left_black);
		item.setPaymentMethodButtonSelectorResid(R.drawable.selector_btn_login_guide);
		items.add(item);

	}

	public PaymentMethodSlidingFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int location) {
		return GuidePaymentMethodSlidingFragment.newInstance(items.get(location));
	}

	@Override
	public int getCount() {
		return items.size();
	}

}
