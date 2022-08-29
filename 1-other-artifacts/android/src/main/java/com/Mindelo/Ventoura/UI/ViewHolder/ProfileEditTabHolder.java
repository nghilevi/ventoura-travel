package com.Mindelo.Ventoura.UI.ViewHolder;

import android.widget.AbsListView;


/**
 * seems like a customized scrool fragment designed for Guide Edit Profile
 */
public interface ProfileEditTabHolder {
	void adjustScroll(int scrollHeight);

	void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,int totalItemCount, int pagePosition);
}
