package com.Mindelo.Ventoura.UI.Fragment;

import java.util.List;

import com.Mindelo.Ventoura.Constant.BroadcastConstant;
import com.Mindelo.Ventoura.Constant.VentouraConstant;
import com.Mindelo.Ventoura.Enum.UserRole;
import com.Mindelo.Ventoura.Ghost.IService.ISystemService;
import com.Mindelo.Ventoura.Ghost.Service.SystemService;
import com.Mindelo.Ventoura.Model.ChooseRoleSlidingFragmentAdapterItem;
import com.Mindelo.Ventoura.UI.Activity.GuidePortalActivity;
import com.Mindelo.Ventoura.UI.Activity.GuideProfileInitializeActivity;
import com.Mindelo.Ventoura.UI.Activity.R;
import com.Mindelo.Ventoura.UI.Activity.TravellerPortalActivity;
import com.Mindelo.Ventoura.UI.Activity.TravellerProfileInitializeActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ChooseRoleSlidingFragment extends Fragment {

	private static final String TAG = "ChooseRoleSlidingFragment";

	/*
	 * progress dialog
	 */
	private ProgressDialog progressDialog;

	/*
	 * utilities
	 */
	private Activity activity;
	private SharedPreferences sharedPre;
	private ISystemService systemService;

	/*
	 * global variables
	 */
	private long userIdInServer = -1;
	private UserRole roleChoosed = UserRole.TRAVELLER;
	private LoginProbeTask loginProbeTask;
	private String facebookId = null;

	private ChooseRoleSlidingFragmentAdapterItem item;

	public static ChooseRoleSlidingFragment newInstance(
			ChooseRoleSlidingFragmentAdapterItem item) {
		ChooseRoleSlidingFragment doubleLayerSlidingFragment = new ChooseRoleSlidingFragment();
		Bundle args = new Bundle();
		args.putSerializable("item", item);
		doubleLayerSlidingFragment.setArguments(args);
		return doubleLayerSlidingFragment;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		item = (ChooseRoleSlidingFragmentAdapterItem) args
				.getSerializable("item");
		/*
		 * utilities initialized
		 */
		activity = getActivity();
		loginProbeTask = new LoginProbeTask(activity);
		sharedPre = activity.getSharedPreferences(
				VentouraConstant.SHARED_PREFERENCE_VENTOURA,
				Context.MODE_PRIVATE);
		facebookId = sharedPre.getString(VentouraConstant.PRE_USER_FACEBOOK_ID,
				"");
		systemService = new SystemService();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		/*
		 * views
		 */
		View view = null;
		TextView topicTitle;
		TextView descriptionContents;
		Button loginButtonTitle;
		ImageView nagivationImageView;

		/*
		 * sliding views
		 */
		final Intent intent = new Intent();
		intent.setAction(BroadcastConstant.CHOOSE_ROLE_REFRESH_VIEWPAGE_ACTION);
		if (item.getUserRole().equals(UserRole.GUIDE)) {
			view = inflater
					.inflate(
							R.layout.doublelayer_chooserole_sliding_viewpage_guide_item,
							container, false);
			topicTitle = (TextView) view.findViewById(R.id.topicTitle);
			descriptionContents = (TextView) view
					.findViewById(R.id.descriptionContents);
			loginButtonTitle = (Button) view
					.findViewById(R.id.loginButtonTitle);
			nagivationImageView = (ImageView) view
					.findViewById(R.id.nagivation_image_view);
			nagivationImageView.setImageResource(item.getNavigationIVBgResId());
			topicTitle.setText(item.getTopicTitle());
			loginButtonTitle.setText(item.getLoginButtonTitle());
			loginButtonTitle.setBackgroundResource(item
					.getLoginButtonSelectorResid());
			List<String> contents = item.getDescriptionContents();
			String desContent = "";
			for (String str : contents) {
				desContent += str + "\r\n" + "\r\n";
			}
			descriptionContents.setText(desContent);
			view.setBackgroundResource(item.getBgColorResId());
			loginButtonTitle.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (item.getUserRole().equals(UserRole.GUIDE)) {
						/*
						 * Login as GUIDE button clicked
						 */
						roleChoosed = UserRole.GUIDE;
						loginProbeTask.execute();
					}
				}
			});

			nagivationImageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					/*
					 * the arrow is clicked, switch to TRAVELLER
					 */
					intent.putExtra("pageNum", 0);
					ChooseRoleSlidingFragment.this.getActivity()
							.getApplicationContext().sendBroadcast(intent);
				}
			});
		} else if (item.getUserRole().equals(UserRole.TRAVELLER)) {
			view = inflater
					.inflate(
							R.layout.doublelayer_chooserole_sliding_viewpage_traveller_item,
							container, false);
			topicTitle = (TextView) view.findViewById(R.id.topicTitle);
			descriptionContents = (TextView) view
					.findViewById(R.id.descriptionContents);
			loginButtonTitle = (Button) view
					.findViewById(R.id.loginButtonTitle);
			topicTitle.setText(item.getTopicTitle());
			loginButtonTitle.setText(item.getLoginButtonTitle());
			List<String> contents = item.getDescriptionContents();
			nagivationImageView = (ImageView) view
					.findViewById(R.id.nagivation_image_view);
			nagivationImageView.setImageResource(item.getNavigationIVBgResId());
			loginButtonTitle.setBackgroundResource(item
					.getLoginButtonSelectorResid());
			String desContent = "";
			for (String str : contents) {
				desContent += str + "\r\n" + "\r\n";
			}
			descriptionContents.setText(desContent);
			view.setBackgroundResource(item.getBgColorResId());
			loginButtonTitle.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					/*
					 * Login as traveller button clicked
					 */
					if ((item.getUserRole().equals(UserRole.TRAVELLER))) {
						roleChoosed = UserRole.TRAVELLER;
						loginProbeTask.execute();
					}
				}
			});

			nagivationImageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					/*
					 * the arrow is clicked, switch to GUIDE
					 */
					intent.putExtra("pageNum", 1);
					ChooseRoleSlidingFragment.this.getActivity()
							.getApplicationContext().sendBroadcast(intent);
				}
			});
		}
		return view;
	}

	/**
	 * Network tasks for login probing
	 */
	private class LoginProbeTask extends AsyncTask<Void, Void, Void> {

		public LoginProbeTask(Activity activity) {
			progressDialog = new ProgressDialog(activity);
		}

		@Override
		protected void onPreExecute() {
			progressDialog.setMessage("Logging in...");
			progressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg) {

			if (roleChoosed == UserRole.GUIDE) {
				userIdInServer = systemService.guideLoginProbe(facebookId);
			} else {
				userIdInServer = systemService.traverllerLoginProbe(facebookId);
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			progressDialog.dismiss();
			progressDialog=null;
			redirectToNewActivity();
		}
	}

	private void redirectToNewActivity() {

		switch ((int) userIdInServer) {
		case -1:
			// guide
			if (roleChoosed == UserRole.GUIDE) {
				Intent intent1 = new Intent(activity,
						GuideProfileInitializeActivity.class);
				startActivity(intent1);
				activity.finish();
			} else {
				Intent intent2 = new Intent(activity,
						TravellerProfileInitializeActivity.class);
				startActivity(intent2);
				activity.finish();
			}
			break;
		default:
			// make sure the user's id is always saved in the
			saveUserRoleData();

			if (roleChoosed == UserRole.GUIDE) {
				startActivity(new Intent(activity, GuidePortalActivity.class));
			} else {
				startActivity(new Intent(activity,
						TravellerPortalActivity.class));
			}
			activity.finish();
			break;
		}
	}

	private void saveUserRoleData() {
		Editor editor = sharedPre.edit();

		editor.putLong(VentouraConstant.PRE_USER_ID_IN_SERVER, userIdInServer);
		if (roleChoosed == UserRole.GUIDE) {
			editor.putInt(VentouraConstant.PRE_USER_ROLE,
					UserRole.GUIDE.getNumVal());
		} else {
			editor.putInt(VentouraConstant.PRE_USER_ROLE,
					UserRole.TRAVELLER.getNumVal());
		}

		editor.commit();
	}

}
