package com.Mindelo.Ventoura.UI.Fragment;

import java.util.ArrayList;
import java.util.List;

import com.Mindelo.Ventoura.Enum.UserRole;
import com.Mindelo.Ventoura.UI.Activity.R;
import com.Mindelo.Ventoura.UI.Adapter.ProfileEditFragmentAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

/**
 * seems like a customized scrool fragment designed for Edit Profile
 */
public class ProfileEditContentFragment extends ProfileEditTabHolderFragment implements
		OnScrollListener {

	private ListView mListView;

	private int mPosition;
	private static UserRole userRole;
	
	private ProfileEditFragmentAdapter sampleListFragmentAdapter;

	public static Fragment newInstance(int position, UserRole userRole) {
		ProfileEditContentFragment f = new ProfileEditContentFragment();
		ProfileEditContentFragment.userRole = userRole;
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_profile_edit_content_listview, null);
		mListView = (ListView) v.findViewById(R.id.listView);
		View placeHolderView = inflater.inflate(R.layout.fragment_profile_edit_content_listview_header_placeholder, mListView, false);
		mListView.addHeaderView(placeHolderView);
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mListView.setOnScrollListener(this);
		List<String> content = new ArrayList<>();
		content.add("one");
		sampleListFragmentAdapter =new ProfileEditFragmentAdapter(content, ProfileEditContentFragment.userRole);
		mListView.setAdapter(sampleListFragmentAdapter);
	}

	@Override
	public void adjustScroll(int scrollHeight) {
		if (scrollHeight == 0 && mListView.getFirstVisiblePosition() >= 1) {
			return;
		}
		mListView.setSelectionFromTop(1, scrollHeight);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (mScrollTabHolder != null)
			mScrollTabHolder.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount, mPosition);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// nothing
	}	

}
