package com.Mindelo.Ventoura.UI.Fragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Mindelo.Ventoura.Constant.BroadcastConstant;
import com.Mindelo.Ventoura.Constant.IMConstant;
import com.Mindelo.Ventoura.Constant.VentouraConstant;
import com.Mindelo.Ventoura.Entity.Guide;
import com.Mindelo.Ventoura.Entity.ImageProfile;
import com.Mindelo.Ventoura.Enum.Gender;
import com.Mindelo.Ventoura.Enum.PaymentMethod;
import com.Mindelo.Ventoura.Enum.UserRole;
import com.Mindelo.Ventoura.Ghost.IService.IGuideService;
import com.Mindelo.Ventoura.Ghost.IService.IIMService;
import com.Mindelo.Ventoura.Ghost.Service.GuideService;
import com.Mindelo.Ventoura.Ghost.Service.IMService;
import com.Mindelo.Ventoura.Model.PaymentMethodSlidingFragmentAdapterItem;
import com.Mindelo.Ventoura.UI.Activity.GuidePortalActivity;
import com.Mindelo.Ventoura.UI.Activity.GuideProfileInitializeActivity;
import com.Mindelo.Ventoura.UI.Activity.R;
import com.Mindelo.Ventoura.Util.HttpUtility;

public class GuidePaymentMethodSlidingFragment extends Fragment {

	private static final String TAG = "GuideVIniFragment";

	/*
	 * progress dialog
	 */
	private ProgressDialog progressDialog;

	/*
	 * Utility instances
	 */
	private GuideProfileInitializeActivity activity;
	private IGuideService guideService;
	private SharedPreferences sharedPre;
	private IIMService imService;

	/*
	 * global variables
	 */
	private String facebookUserId, userFirstname, userLastname, gender, city,
			country, birthday;
	private PaymentMethod paymentMethod = PaymentMethod.CARD;
	private PaymentMethodSlidingFragmentAdapterItem item;
	private CreateNewGuideProbeTask createNewGuideProbeTask;

	public static GuidePaymentMethodSlidingFragment newInstance(
			PaymentMethodSlidingFragmentAdapterItem item) {
		GuidePaymentMethodSlidingFragment doubleLayerSlidingFragment = new GuidePaymentMethodSlidingFragment();
		Bundle args = new Bundle();
		args.putSerializable("item", item);
		doubleLayerSlidingFragment.setArguments(args);
		return doubleLayerSlidingFragment;

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*
		 * sliding fragment
		 */
		Bundle args = getArguments();
		item = (PaymentMethodSlidingFragmentAdapterItem) args
				.getSerializable("item");

		activity = (GuideProfileInitializeActivity) getActivity();

		guideService = new GuideService(activity);
		imService = new IMService();
		createNewGuideProbeTask = new CreateNewGuideProbeTask();

		sharedPre = activity.getSharedPreferences(
				VentouraConstant.SHARED_PREFERENCE_VENTOURA,
				Context.MODE_PRIVATE);

		facebookUserId = sharedPre.getString(
				VentouraConstant.PRE_USER_FACEBOOK_ID, "");
		userFirstname = sharedPre.getString(
				VentouraConstant.PRE_USER_FIRST_NAME, "");
		userLastname = sharedPre.getString(VentouraConstant.PRE_USER_LAST_NAME,
				"");
		gender = sharedPre.getString(VentouraConstant.PRE_USER_GENDER, "");
		city = sharedPre.getString(VentouraConstant.PRE_USER_CITY, "");
		country = sharedPre.getString(VentouraConstant.PRE_USER_COUNTRY, "");
		birthday = sharedPre.getString(VentouraConstant.PRE_USER_BIRTHDAY, "");
		Log.i(TAG, "shit ppp : " + birthday);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
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
		intent.setAction(BroadcastConstant.PAYMENTMETHOD_REFRESH_VIEWPAGE_ACTION);

		if (item.getPaymentMethod().equals(PaymentMethod.CASH)) {
			view = inflater
					.inflate(
							R.layout.doublelayer_paymentmethod_sliding_viewpage_cash_item,
							container, false);
			topicTitle = (TextView) view.findViewById(R.id.topicTitle);
			descriptionContents = (TextView) view
					.findViewById(R.id.descriptionContents); 
			loginButtonTitle = (Button) view
					.findViewById(R.id.paymentMethodButtonTitle);
			nagivationImageView = (ImageView) view
					.findViewById(R.id.nagivation_image_view);
			nagivationImageView.setImageResource(item.getNavigationIVBgResId());
			topicTitle.setText(item.getTopicTitle());
			loginButtonTitle.setText(item.getPaymentMethodButtonTitle());
			//TODO THIS WILL MAKE THE BUTTON HAS A DA YU HAO
			//loginButtonTitle.setBackgroundResource(item.getPaymentMethodButtonSelectorResid());
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
					if (item.getPaymentMethod().equals(PaymentMethod.CASH)) {
						/*
						 * Login as CARD button clicked
						 */
						paymentMethod = PaymentMethod.CASH;
						createNewGuideProbeTask.execute();
					}
				}
			});

			nagivationImageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					/*
					 * the arrow is clicked, switch to CARD
					 */
					intent.putExtra("pageNum", 1);
					GuidePaymentMethodSlidingFragment.this.getActivity()
							.getApplicationContext().sendBroadcast(intent);
				}
			});
		} else if (item.getPaymentMethod().equals(PaymentMethod.CARD)) {
			view = inflater
					.inflate(
							R.layout.doublelayer_paymentmethod_sliding_viewpage_card_item,
							container, false);
			topicTitle = (TextView) view.findViewById(R.id.topicTitle);
			descriptionContents = (TextView) view
					.findViewById(R.id.descriptionContents);
			loginButtonTitle = (Button) view
					.findViewById(R.id.loginButtonTitle);
			topicTitle.setText(item.getTopicTitle());
			loginButtonTitle.setText(item.getPaymentMethodButtonTitle());
			List<String> contents = item.getDescriptionContents();
			nagivationImageView = (ImageView) view
					.findViewById(R.id.nagivation_image_view);
			nagivationImageView.setImageResource(item.getNavigationIVBgResId());
			loginButtonTitle.setBackgroundResource(item
					.getNavigationIVBgResId());
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
					if ((item.getPaymentMethod().equals(PaymentMethod.CARD))) {
						paymentMethod = PaymentMethod.CARD;
						createNewGuideProbeTask.execute();
					}
				}
			});

			nagivationImageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					/*
					 * the arrow is clicked, switch to CARD
					 */
					intent.putExtra("pageNum", 0);
					GuidePaymentMethodSlidingFragment.this.getActivity()
							.getApplicationContext().sendBroadcast(intent);
				}
			});
		}
		return view;
	}

	/**
	 * Network tasks for creating a new user
	 */
	private class CreateNewGuideProbeTask extends
			AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(activity);
			progressDialog.setMessage("Creating your profile...");
			progressDialog.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			long result;

			Guide guide = new Guide();

			guide.setTourLength("");
			guide.setTourPrice(0);
			guide.setMoneyType(-1);
			guide.setGuideFirstname(userFirstname);
			guide.setGuideLastname(userLastname);

			guide.setFacebookAccountName(facebookUserId);
			if (gender.equalsIgnoreCase("male")
					|| gender.equalsIgnoreCase("boy")) {
				guide.setGender(Gender.MALE);
			} else {
				guide.setGender(Gender.FEMALE);
			}
			guide.setTextBiography("");
			guide.setPaymentMethod(paymentMethod);
			DateFormat format = new SimpleDateFormat("MM/dd/yyyy",
					Locale.getDefault());
			try {
				guide.setDateOfBirth(format.parse(birthday));
			} catch (Exception e) {
				// TODO Nothing.
			}
			try {
				ImageProfile portalImage = new ImageProfile();
				portalImage.setImageContent(HttpUtility
						.downloadImageFromUrl("https://graph.facebook.com/"
								+ facebookUserId + "/picture?type=large"));
				portalImage.setPortal(true);
				guide.setPortalImage(portalImage);

				result = guideService.uploadGuideProfile(guide);
				if (result == -1) {
					return false;
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {

			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}

			activity.finish();

			if (result) {
				startActivity(new Intent(activity, GuidePortalActivity.class));
			} else {
				Toast.makeText(getActivity().getApplicationContext(),
						"Created New User Error", Toast.LENGTH_LONG).show();
			}
		}
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

	/*
	 * After the user login, save all the session data into the shared reference
	 * here
	 */
	private void saveUserSessionData(long userIdInServer) {
		Editor editor = sharedPre.edit();

		// put facebook id and name into shared reference
		editor.putInt(VentouraConstant.PRE_USER_ROLE,
				UserRole.GUIDE.getNumVal());
		editor.putLong(VentouraConstant.PRE_USER_ID_IN_SERVER, userIdInServer);
		editor.commit();
	}

}
