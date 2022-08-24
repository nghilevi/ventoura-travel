package com.Mindelo.Ventoura.UI.Adapter;

import java.util.List;

import com.Mindelo.Ventoura.Enum.UserRole;
import com.Mindelo.Ventoura.UI.Activity.GuideProfileEditActivity;
import com.Mindelo.Ventoura.UI.Activity.TravellerProfileEditActivity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ProfileEditFragmentAdapter extends BaseAdapter {

	private List<String> content;
	private UserRole userRole;

	public ProfileEditFragmentAdapter(List<String> content, UserRole userRole) {
		super();
		this.content = content;
		this.userRole = userRole;
	}

	@Override
	public int getCount() {
		return content.size();
	}

	@Override
	public Object getItem(int position) {
		return content.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (userRole == UserRole.GUIDE) {
			view = GuideProfileEditActivity.vContent;
		} else {
			view = TravellerProfileEditActivity.vContent;
		}
		return view;
	}

}
