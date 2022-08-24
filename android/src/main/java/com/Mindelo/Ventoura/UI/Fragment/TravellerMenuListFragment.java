package com.Mindelo.Ventoura.UI.Fragment;

import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Entity.ImageProfile;
import com.Mindelo.Ventoura.Enum.UserRole;
import com.Mindelo.Ventoura.Ghost.IService.IChattingHistoryService;
import com.Mindelo.Ventoura.Ghost.IService.ITravellerService;
import com.Mindelo.Ventoura.Ghost.Service.ChattingHistoryService;
import com.Mindelo.Ventoura.UI.Activity.TravellerPortalActivity;
import com.Mindelo.Ventoura.UI.Activity.R;
import com.Mindelo.Ventoura.UI.View.RoundedImageView;
import com.Mindelo.Ventoura.Util.BitmapUtil;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class TravellerMenuListFragment extends ListFragment {

	private TravellerPortalActivity portalActivity;
	private SharedPreferences sharedPre;
	private IChattingHistoryService chattingHistoryService;
	private ITravellerService travellerService;
	private long travellerId;
	private RoundedImageView roundedHeadImg;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		portalActivity = (TravellerPortalActivity) getActivity();

		sharedPre = portalActivity.getSharedPre();
		chattingHistoryService = new ChattingHistoryService(portalActivity);
		travellerService = portalActivity.getTravellerService();
		travellerId = portalActivity.getTravellerId();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.left_menu_listview, null);

		roundedHeadImg = (RoundedImageView) view
				.findViewById(R.id.menu_profile_rounded_img);

		View profileView = view.findViewById(R.id.menu_profile_ly);
		profileView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				/*
				 * start the Profile fragment
				 */
				portalActivity.switchContent(0);
			}

		});

		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		MenuAdapter adapter = new MenuAdapter(getActivity());

		adapter.add(new MenuItem(R.drawable.selector_menu_ventouring,
				"Ventouring", 0));
		adapter.add(new MenuItem(R.drawable.selector_menu_messages, "Messages",
				chattingHistoryService.getNumberOfChatterWithUnreadMessage(
						portalActivity.getTravellerId(),
						UserRole.TRAVELLER.getNumVal())));
		adapter.add(new MenuItem(R.drawable.selector_menu_mytrip_or_bookings,
				"My Trip", 2));
		// TODO check the server whether we have any promotions
		adapter.add(new MenuItem(R.drawable.selector_menu_mytrip_or_bookings,
				"Promotion", 0));
		adapter.add(new MenuItem(R.drawable.selector_menu_settings, "Settings",
				0));

		setListAdapter(adapter);

	}

	@Override
	public void onResume() {
		super.onResume();
		refreshPortalImage();
	}

	/**
	 * set head image
	 */
	public void refreshPortalImage() {
		ImageProfile portalImage = travellerService
				.getTravellerPortalImageFromDB(travellerId);

		if (portalImage != null) {
			Bitmap bitMap = BitmapUtil.byteArrayToBitMap(
					portalImage.getImageContent(),
					ConfigurationConstant.SMALL_USER_PORTAL_IMAGE_WIDTH,
					ConfigurationConstant.SMALL_USER_PORTAL_IMAGE_HEIGHT);
			roundedHeadImg.setImageBitmap(bitMap);
		}
	}

	private class MenuItem {
		public int iconRes;
		public String text;
		public int unreadMessages;

		public MenuItem(int iconRes, String text, int unreadMessages) {
			this.iconRes = iconRes;
			this.text = text;
			this.unreadMessages = unreadMessages;
		}
	}

	public class MenuAdapter extends ArrayAdapter<MenuItem> {

		public MenuAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(
						R.layout.adapter_menu_item, null);
			}
			ImageView icon = (ImageView) convertView
					.findViewById(R.id.row_icon);

			icon.setImageResource(getItem(position).iconRes);

			TextView tv = (TextView) convertView.findViewById(R.id.row_text);
			tv.setText(getItem(position).text);

			TextView unreadTV = (TextView) convertView
					.findViewById(R.id.row_unread_tv);
			int unreadNumber = getItem(position).unreadMessages;

			if (unreadNumber > 0) {
				unreadTV.setVisibility(View.VISIBLE);
				unreadTV.setText("" + unreadNumber);
			}

			return convertView;
		}
	}

	/*
	 * click the sliding menu to switch content 0 for profile, but profile is
	 * not in listview
	 */
	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		portalActivity.switchContent(position + 1);
	}
}
