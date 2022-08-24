package com.Mindelo.Ventoura.UI.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.Mindelo.Ventoura.AndroidService.IMListenerService;
import com.Mindelo.Ventoura.Constant.VentouraConstant;
import com.Mindelo.Ventoura.Enum.UserRole;
import com.Mindelo.Ventoura.UI.Activity.ContactUsActivity;
import com.Mindelo.Ventoura.UI.Activity.GuidePortalActivity;
import com.Mindelo.Ventoura.UI.Activity.LoginActivity;
import com.Mindelo.Ventoura.UI.Activity.LoginChooseRoleActivity;
import com.Mindelo.Ventoura.UI.Activity.MoreInfoActivity;
import com.Mindelo.Ventoura.UI.Activity.NotificationActivity;
import com.Mindelo.Ventoura.UI.Activity.PaymentSettingsActivity;
import com.Mindelo.Ventoura.UI.Activity.R;
import com.Mindelo.Ventoura.UI.Activity.TravellerPortalActivity;
import com.Mindelo.Ventoura.Util.FacebookUtil;
import com.facebook.Session;

public class TravellerSettingsFragment extends Fragment implements
		OnClickListener {

	private static final String TAG = "TravellerSettingsFragment";

	private SharedPreferences sharedPre;
	private TravellerPortalActivity portalActivity;
	private CheckBox matchAlert, messageAlert, halfTourReminder;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		portalActivity = (TravellerPortalActivity) getActivity();

		sharedPre = portalActivity.getSharedPre();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);

		View view = inflater.inflate(R.layout.fragment_traveller_settings,
				container, false);

		
		View logoutButton = view.findViewById(R.id.settings_logout_arrow);
		logoutButton.setOnClickListener(this);
		View switchAccountBtn = view.findViewById(R.id.settings_switch_accounts_arrow);
		switchAccountBtn.setOnClickListener(this);
		View disableAccountBtn = view.findViewById(R.id.settings_disable_account_arrow);
		disableAccountBtn.setOnClickListener(this);
		View aboutVentouraBtn = view.findViewById(R.id.settings_about_ventour_arrow);
		aboutVentouraBtn.setOnClickListener(this);
		View feedbackBtn = view.findViewById(R.id.settings_feedback_arrow);
		feedbackBtn.setOnClickListener(this);
		
		View likeUsOnFacebook = view.findViewById(R.id.settings_like_us_facebook_arrow);
		likeUsOnFacebook.setOnClickListener(this);
		View followUsOnTwitter = view.findViewById(R.id.settings_follow_us_twitter_arrow);
		followUsOnTwitter.setOnClickListener(this);
		
		matchAlert = (CheckBox) view
				.findViewById(R.id.settings_match_alert_switch);
		matchAlert.setOnClickListener(this);
		messageAlert = (CheckBox) view
				.findViewById(R.id.settings_message_alert_switch);
		messageAlert.setOnClickListener(this);
		halfTourReminder = (CheckBox) view
				.findViewById(R.id.settings_half_reminder_switch);
		halfTourReminder.setOnClickListener(this);


		return view;
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		/*
		 * Switches
		 */
		case R.id.settings_match_alert_switch:
			break;
		case R.id.settings_message_alert_switch:
			break;
		case R.id.settings_half_reminder_switch:
			if (halfTourReminder.isChecked()) {
				Toast.makeText(getActivity(), "Checked", Toast.LENGTH_SHORT)
						.show();
			} else
				Toast.makeText(getActivity(), "Delete", Toast.LENGTH_SHORT)
						.show();
			break;
		/*
		 * Account Options
		 */
		case R.id.settings_switch_accounts_arrow:
			stopIMService();
			startActivity(new Intent(portalActivity, LoginChooseRoleActivity.class));
			portalActivity.finish();
			break;
		case R.id.settings_logout_arrow:
			logout();
			startActivity(new Intent(portalActivity, LoginActivity.class));
			break;
		case R.id.settings_disable_account_arrow:
			Toast.makeText(getActivity(), "settings_disable_account",
					Toast.LENGTH_LONG).show();
			break;
		/*
		 * Ventoura
		 */
		case R.id.settings_about_ventour_arrow:
			startActivity(new Intent(portalActivity, MoreInfoActivity.class));
			break;
		case R.id.settings_feedback_arrow:
			startActivity(new Intent(portalActivity, ContactUsActivity.class));
			break;
		case R.id.settings_like_us_facebook_arrow:
			Intent facebookIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(VentouraConstant.URL_VENTOURA_FACEBOOK));
			startActivity(facebookIntent);
		case R.id.settings_follow_us_twitter_arrow:
			Intent twitterIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(VentouraConstant.URL_VENTOURA_FACEBOOK));
			startActivity(twitterIntent);
		default:
			break;
		}

	}

	// logout facebook and clean up
	private void logout() {
		clearUserPrefs();
		FacebookUtil.logoutFacebook(portalActivity);
		stopIMService();
		portalActivity.finish();

	}
	
	private void stopIMService() {
		/* turn off IM service */
		IMListenerService.getImConnection().disconnect();
		Intent intent = new Intent(portalActivity, IMListenerService.class);
		portalActivity.stopService(intent);
	}

	// clean up the users information
	void clearUserPrefs() {
		Editor editor = sharedPre.edit();
		editor.clear();
		editor.commit();
	}
	
}
