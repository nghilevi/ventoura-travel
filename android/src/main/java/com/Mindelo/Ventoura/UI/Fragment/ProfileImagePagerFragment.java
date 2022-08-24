package com.Mindelo.Ventoura.UI.Fragment;

import lombok.Setter;

import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Entity.ImageProfile;
import com.Mindelo.Ventoura.UI.Activity.R;
import com.Mindelo.Ventoura.Util.BitmapUtil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ProfileImagePagerFragment extends Fragment {

	public static final String TAG = "ProfileImagePagerFragment";

	@Setter
	private ImageProfile imageEntity;

	public static ProfileImagePagerFragment newInstance(ImageProfile imageEntity) {

		ProfileImagePagerFragment f = new ProfileImagePagerFragment();
		f.setImageEntity(imageEntity);
		return f;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = null;
		ImageView image;
		view = inflater.inflate(
				R.layout.adapter_fragment_traveller_viewpager_item, container,
				false);
		image = (ImageView) view.findViewById(R.id.iv_traveller_image);

		image.setImageBitmap(BitmapUtil.byteArrayToBitMap(
				imageEntity.getImageContent(),
				ConfigurationConstant.NORMAL_USER_PORTAL_IMAGE_WIDTH,
				ConfigurationConstant.NORMAL_USER_PORTAL_IMAGE_HEIGHT));

		return view;
	}

}
