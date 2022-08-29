package com.Mindelo.Ventoura.UI.Adapter;

import com.Mindelo.Ventoura.Enum.UserRole;
import com.Mindelo.Ventoura.UI.Fragment.ProfileEditContentFragment;
import com.Mindelo.Ventoura.UI.Fragment.ProfileEditTabHolderFragment;
import com.Mindelo.Ventoura.UI.ViewHolder.ProfileEditTabHolder;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;

public class ProfileEditPagerAdapter extends FragmentPagerAdapter{
	private SparseArrayCompat<ProfileEditTabHolder> mScrollTabHolders;
	private final String[] TITLES = { "Page 1"};
	private ProfileEditTabHolder mListener;
	private UserRole userRole;
	
	public void setTabHolderScrollingContent(ProfileEditTabHolder listener) {
		mListener = listener;
	}

	public ProfileEditPagerAdapter(FragmentManager fm, UserRole userRole) {
		super(fm);
		this.userRole = userRole;
		mScrollTabHolders = new SparseArrayCompat<ProfileEditTabHolder>();
	}

	@Override
	public Fragment getItem(int position) {
		ProfileEditTabHolderFragment fragment = (ProfileEditTabHolderFragment) ProfileEditContentFragment.newInstance(position, userRole);
		mScrollTabHolders.put(position, fragment);
		if (mListener != null) {
			fragment.setScrollTabHolder(mListener);
		}
		return fragment;
	}

	public SparseArrayCompat<ProfileEditTabHolder> getScrollTabHolders() {
		return mScrollTabHolders;
	}

	@Override
	public int getCount() {
		return TITLES.length;
	}

}
