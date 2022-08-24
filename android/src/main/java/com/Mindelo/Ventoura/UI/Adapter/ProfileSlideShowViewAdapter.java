package com.Mindelo.Ventoura.UI.Adapter;

import java.util.List;

import com.Mindelo.Ventoura.Entity.ImageProfile;
import com.Mindelo.Ventoura.UI.Fragment.ProfileImagePagerFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ProfileSlideShowViewAdapter extends FragmentPagerAdapter {

	private List<ImageProfile> entities;

	public ProfileSlideShowViewAdapter(FragmentManager fm,
			List<ImageProfile> entities) {
		super(fm);
		this.entities = entities;
	}

	@Override
	public Fragment getItem(int pos) {
		return ProfileImagePagerFragment.newInstance(entities.get(pos));
	}

	@Override
	public int getCount() {
		return entities.size();
	}

}
