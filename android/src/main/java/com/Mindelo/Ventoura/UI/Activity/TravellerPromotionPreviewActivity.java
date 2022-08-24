package com.Mindelo.Ventoura.UI.Activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.Mindelo.Ventoura.Constant.VentouraConstant;
import com.Mindelo.Ventoura.Ghost.IService.IPromotionService;
import com.Mindelo.Ventoura.Ghost.Service.PromotionService;
import com.Mindelo.Ventoura.Util.ImageUtil;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.RequestBatch;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphObjectList;
import com.facebook.model.OpenGraphAction;
import com.facebook.model.OpenGraphObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class TravellerPromotionPreviewActivity extends FragmentActivity
		implements OnClickListener {

	private static final String TAG = "TravellerPromotionPreviewActivity";

	/*
	 * progress dialog
	 */
	private ProgressDialog progressDialog;

	/*
	 * views
	 */
	private Button shareOnFacebookBtn;
	private ImageView promotion_image_view;

	private long travellerId;
	private ArrayList<Integer> cityIds = null;
	private IPromotionService promotionService;

	/*
	 * used by sharing promotion to facebook
	 */
	private static final List<String> PERMISSIONS = Arrays
			.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	static final boolean UPLOAD_IMAGE = true;
	private boolean pendingPublishReauthorization = false;
	private UiLifecycleHelper uiHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_traveller_promotion_preview);

		travellerId = getIntent().getLongExtra("travellerId", -1);
		cityIds = getIntent().getIntegerArrayListExtra("cityIds");
		promotionService = new PromotionService();

		/*
		 * initialize views
		 */
		shareOnFacebookBtn = (Button) findViewById(R.id.promotion_share_trip_on_facebook_btn);
		shareOnFacebookBtn.setOnClickListener(this);
		promotion_image_view = (ImageView) findViewById(R.id.activity_traveller_promotion_preview_city_image);

		/*
		 * set the promotion image
		 */
		File promotionImage = promotionService
				.getCityPromotionImageFileFromSDCard(travellerId);
		if (promotionImage.exists()) {
			promotion_image_view.setImageBitmap(ImageUtil
					.decodeScaledBitmapFromSdCard(promotionService
							.getCityPromotionImageFilePath(travellerId)));
		}

		/*
		 * @share to facebook
		 */
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			pendingPublishReauthorization = savedInstanceState.getBoolean(
					PENDING_PUBLISH_KEY, false);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.promotion_share_trip_on_facebook_btn:
			publishStory();
			break;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		// @share to facebook
		uiHelper.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		// @share to facebook
		uiHelper.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// @share to facebook
		uiHelper.onDestroy();
	}

	/*
	 * The back button click to switch the menu and content
	 */
	public void btnBackOnClick(View view) {
		onBackPressed();
	}

	/*
	 * The add button click to switch the menu and content
	 */
	public void btnActionOnClick(View view) {
		onBackPressed();
	}

	// @share to facebook
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(final Session session, final SessionState state,
				final Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	// @share to facebook
	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		if (state.isOpened()) {
			if (pendingPublishReauthorization
					&& state.equals(SessionState.OPENED_TOKEN_UPDATED)) {
				pendingPublishReauthorization = false;
				publishStory();
			}
		} else if (state.isClosed()) {
			//
		}
	}

	/**
	 * Publish a story with image to facebook @share to facebook
	 */
	private void publishStory() {

		Session session = Session.getActiveSession();

		if (session != null) {

			showProgressDialog();

			pendingPublishReauthorization = true;
			List<String> permissions = session.getPermissions();
			if (!isSubsetOf(PERMISSIONS, permissions)) {
				Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(
						this, PERMISSIONS);
				session.requestNewPublishPermissions(newPermissionsRequest);
				return;
			}

			RequestBatch requestBatch = new RequestBatch();
			// If uploading an image, set up the first batch request
			// to do this.
			if (UPLOAD_IMAGE) {

				Bitmap image = ImageUtil
						.decodeScaledBitmapFromSdCard(promotionService
								.getCityPromotionImageFilePath(travellerId)); // get
																				// the
																				// image
				// path

				Request.Callback imageCallback = new Request.Callback() {

					@Override
					public void onCompleted(Response response) {
						FacebookRequestError error = response.getError();
						if (error != null) {
							dismissProgressDialog();
							Log.i(TAG, error.getErrorMessage());
						}
					}
				};
				Request imageRequest = Request
						.newUploadStagingResourceWithImageRequest(
								Session.getActiveSession(), image,
								imageCallback);
				imageRequest.setBatchEntryName("imageUpload");
				requestBatch.add(imageRequest);
			}

			// Request: Object request
			// Set up the JSON representing the book
			OpenGraphObject book = OpenGraphObject.Factory
					.createForPost("ventoura:promotion");

			GraphObject imageObject = GraphObject.Factory.create();
			imageObject.setProperty("url", "{result=imageUpload:$.uri}");
			imageObject.setProperty("user_generated", "true");
			GraphObjectList<GraphObject> images = GraphObject.Factory
					.createList(GraphObject.class);
			images.add(imageObject);
			book.setImage(images);

			book.setTitle("Ventoura Promotion");
			book.setUrl("http://www.ventoura.net");
			book.setDescription("Win a free flight to Enrope by downloading Ventoura.");
			// books.book-specific properties go under "data"
			// book.getData().setProperty("isbn", "0-553-57340-3");

			// Set up the object request callback
			Request.Callback objectCallback = new Request.Callback() {
				@Override
				public void onCompleted(Response response) {
					FacebookRequestError error = response.getError();
					if (error != null) {
						dismissProgressDialog();
						Log.i(TAG, error.getErrorMessage());
					}
				}
			};

			// Create the request for object creation
			Request objectRequest = Request.newPostOpenGraphObjectRequest(
					Session.getActiveSession(), book, objectCallback);
			objectRequest.setBatchEntryName("objectCreate");
			requestBatch.add(objectRequest);

			// Request: Publish action request
			OpenGraphAction readAction = OpenGraphAction.Factory
					.createForPost("ventoura:participate");
			readAction.setProperty("promotion", "{result=objectCreate:$.id}");
			readAction.setProperty("fb:explicitly_shared", "true");

			Request.Callback actionCallback = new Request.Callback() {
				@Override
				public void onCompleted(Response response) {
					FacebookRequestError error = response.getError();
					if (error != null) {
						Toast.makeText(getApplicationContext(),
								error.getErrorMessage(), Toast.LENGTH_SHORT)
								.show();
					} else {
						String actionId = null;
						try {
							JSONObject graphResponse = response
									.getGraphObject().getInnerJSONObject();
							actionId = graphResponse.getString("id");
						} catch (JSONException e) {
							Log.i(TAG, "JSON error " + e.getMessage());
						}
						dismissProgressDialog();
						afterSharingToFacebook();
						// Toast.makeText(getApplicationContext(), actionId,
						// Toast.LENGTH_LONG).show();
					}
				}
			};

			// Create the publish action request
			Request actionRequest = Request.newPostOpenGraphActionRequest(
					Session.getActiveSession(), readAction, actionCallback);

			// Add the request to the batch
			requestBatch.add(actionRequest);

			// Execute the batch request
			requestBatch.executeAsync();

		}
	}

	/*
	 * Helper method to check a collection for a string.
	 */
	private boolean isSubsetOf(Collection<String> subset,
			Collection<String> superset) {
		for (String string : subset) {
			if (!superset.contains(string)) {
				return false;
			}
		}
		return true;
	}

	/*
	 * Helper method to handle after sharing facebook
	 */
	private void afterSharingToFacebook() {
		Editor edit =  getSharedPreferences(
				VentouraConstant.SHARED_PREFERENCE_VENTOURA,
				Context.MODE_PRIVATE).edit();
		edit.putBoolean(VentouraConstant.PRE_USER_ATTENDED_PROMOTION, true);
		edit.commit();
		
		AddNewCandidateTask AddNewCandidateTask = new AddNewCandidateTask();
		AddNewCandidateTask.execute();
		
		finish();
	}

	/**
	 * Network tasks for adding the candidate
	 */
	public class AddNewCandidateTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			if (cityIds != null) {
				if (promotionService.addNewPromotionCandidate(travellerId,
						cityIds) != -1) {
					//
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
		}
	}

	private void showProgressDialog() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("Publishing...");
		progressDialog.show();
	}

	private void dismissProgressDialog() {
		// Dismiss the progress dialog
		if (progressDialog != null) {
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			progressDialog = null;
		}
	}

}
