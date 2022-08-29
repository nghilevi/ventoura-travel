package com.Mindelo.Ventoura.UI.Fragment;

import android.support.v4.app.Fragment;
import android.widget.AbsListView;

import com.Mindelo.Ventoura.UI.ViewHolder.ProfileEditTabHolder;

public abstract class ProfileEditTabHolderFragment extends Fragment implements
		ProfileEditTabHolder {

	protected ProfileEditTabHolder mScrollTabHolder;

	public void setScrollTabHolder(ProfileEditTabHolder scrollTabHolder) {
		mScrollTabHolder = scrollTabHolder;
	}

	@Override
	public void adjustScroll(int scrollHeight) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount, int pagePosition) {
		// TODO Auto-generated method stub

	}

}
