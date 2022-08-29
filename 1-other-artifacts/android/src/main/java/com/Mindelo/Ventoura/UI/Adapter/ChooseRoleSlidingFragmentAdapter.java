package com.Mindelo.Ventoura.UI.Adapter;

import java.util.ArrayList;
import java.util.List;

import com.Mindelo.Ventoura.Enum.UserRole;
import com.Mindelo.Ventoura.Model.ChooseRoleSlidingFragmentAdapterItem;
import com.Mindelo.Ventoura.UI.Activity.R;
import com.Mindelo.Ventoura.UI.Fragment.ChooseRoleSlidingFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ChooseRoleSlidingFragmentAdapter extends FragmentPagerAdapter {

	protected static List<ChooseRoleSlidingFragmentAdapterItem> items = new ArrayList<ChooseRoleSlidingFragmentAdapterItem>();

	static {
		// first item
		ChooseRoleSlidingFragmentAdapterItem item = new ChooseRoleSlidingFragmentAdapterItem();
		item.setBgColorResId(R.color.ventoura_color);
		item.setTopicTitle("Travelling the globe ?");
		List<String> descriptionContents = new ArrayList<String>();
		descriptionContents
				.add("- Chat and connect with travellers at your destination.");
		descriptionContents.add("- Take a tour and live like a local");
		item.setDescriptionContents(descriptionContents);
		item.setLoginButtonTitle("Traveller");
		item.setUserRole(UserRole.TRAVELLER);
		item.setNavigationIVBgResId(R.drawable.arrow_to_right_black);
		item.setLoginButtonSelectorResid(R.drawable.selector_btn_login_traveller);
		items.add(item);

		// second item
		item = new ChooseRoleSlidingFragmentAdapterItem();
		item.setBgColorResId(R.color.ventoura_blue);
		item.setTopicTitle("Know your city?");
		descriptionContents = new ArrayList<String>();
		descriptionContents.add("- Craft your own tour expericnce,earn cash.");
		descriptionContents
				.add("- Make international friends,in a setting you create");
		descriptionContents.add("- No schedule,no boss,do it your way");
		item.setDescriptionContents(descriptionContents);
		item.setLoginButtonTitle("Local");
		item.setUserRole(UserRole.GUIDE);
		item.setNavigationIVBgResId(R.drawable.arrow_to_left_black);
		item.setLoginButtonSelectorResid(R.drawable.selector_btn_login_guide);
		items.add(item);

	}

	public ChooseRoleSlidingFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int location) {
		return ChooseRoleSlidingFragment.newInstance(items.get(location));
	}

	@Override
	public int getCount() {
		return items.size();
	}

}
