package com.Mindelo.Ventoura.UI.Activity;

import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Constant.VentouraConstant;
import com.Mindelo.Ventoura.Entity.ImageMatch;
import com.Mindelo.Ventoura.Enum.BookingStatus;
import com.Mindelo.Ventoura.Enum.UserRole;
import com.Mindelo.Ventoura.Ghost.IService.IBookingScheduleService;
import com.Mindelo.Ventoura.Ghost.IService.IMatchesService;
import com.Mindelo.Ventoura.Ghost.Service.BookingScheduleService;
import com.Mindelo.Ventoura.Ghost.Service.MatchesService;
import com.Mindelo.Ventoura.JSONEntity.JSONBooking;
import com.Mindelo.Ventoura.UI.View.RoundedImageView;
import com.Mindelo.Ventoura.Util.BitmapUtil;
import com.Mindelo.Ventoura.Util.ImageUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GuideBookingResponseRequestActivity extends FragmentActivity {
	
	
	/*
	 * progress progressDialog to show user that the backup is processing.
	 */
	private ProgressDialog progressDialog;
	
	private SharedPreferences sharedPre;
	private IBookingScheduleService bookingScheduleService;
	private long guideId;
	private long bookingId;
	private JSONBooking jsonBooking;
	private IMatchesService matchService;

	/*
	 * views
	 */
	private RoundedImageView bookTourTravellerImageView;
	private TextView travellerFistnameTv;
	private Button btn_refuse, btn_accept;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_booking_response_request);

		/*
		 * initialize utilities
		 */
		sharedPre = getSharedPreferences(
				VentouraConstant.SHARED_PREFERENCE_VENTOURA,
				Context.MODE_PRIVATE);
		bookingScheduleService = new BookingScheduleService(this);
		matchService = new MatchesService(this);
		guideId = sharedPre.getLong(VentouraConstant.PRE_USER_ID_IN_SERVER, -1);

		/*
		 * initialize global variables
		 */
		Intent intent = getIntent();
		jsonBooking = (JSONBooking) intent.getSerializableExtra("jsonBooking");

		/*
		 * get the views
		 */
		bookTourTravellerImageView = (RoundedImageView) findViewById(R.id.guide_booking_status_traveller_head_img);
		btn_refuse = (Button) findViewById(R.id.guide_refuse_btn);
		btn_accept = (Button) findViewById(R.id.guide_accept_btn);
		btn_refuse.setOnClickListener(new GuideDecisionListener());
		btn_accept.setOnClickListener(new GuideDecisionListener());
		
		travellerFistnameTv = (TextView) this
				.findViewById(R.id.guide_booking_status_traveller_name);

		/*
		 * set the views
		 */
		if (jsonBooking != null) {
			travellerFistnameTv.setText(jsonBooking.getTravellerFirstname());
			try {
				
				ImageMatch headImage = matchService.getSingleMatchImageFromDB(jsonBooking.getTravellerId(), UserRole.TRAVELLER.getNumVal());
				if(headImage != null){
					Bitmap bitMap = BitmapUtil.byteArrayToBitMap(
							headImage.getImageContent(),
							ConfigurationConstant.NORMAL_USER_PORTAL_IMAGE_WIDTH,
							ConfigurationConstant.NORMAL_USER_PORTAL_IMAGE_HEIGHT);
					bookTourTravellerImageView.setImageBitmap(bitMap);	
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private class GuideDecisionListener implements OnClickListener {

		@Override
		public void onClick(View view) {
			GuideResponseBookingTask guideResponseBookingTask = new GuideResponseBookingTask();
			switch(view.getId()){
			case R.id.guide_refuse_btn:
				guideResponseBookingTask.execute(BookingStatus.REQUEST_DECLINED_BY_LOCAL.getNumVal());
				break;
			case R.id.guide_accept_btn:
				guideResponseBookingTask.execute(BookingStatus.REQUEST_ACCEPTED_BY_LOCAL.getNumVal());
				break;
			}
		}
		
	}
	
	/*
	 * The back button click to switch the menu and content
	 */
	public void btnBackOnClick(View view) {
		onBackPressed();
	}
	
	private class GuideResponseBookingTask extends AsyncTask<Integer, Void, Boolean>{

		@Override
		protected void onPreExecute() {
			progressDialog = new ProgressDialog(GuideBookingResponseRequestActivity.this);
			progressDialog.setMessage("Notifying traveller...");
			progressDialog.show();
		}
		
		@Override
		protected Boolean doInBackground(Integer... arg0) {
			return bookingScheduleService.guideUpdateBookingStatus(jsonBooking.getId(), arg0[0]);
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			dismissProgressDialog();
			if(result == true){
				Toast.makeText(GuideBookingResponseRequestActivity.this, "Response the reques succeeded !", Toast.LENGTH_SHORT);
				//TODO
			}else{
				Toast.makeText(GuideBookingResponseRequestActivity.this, "Response the reques error !", Toast.LENGTH_SHORT);
			}
		}
	}
	
	/*
	 * helper to handle the dismiss progress progressDialog
	 */
	private void dismissProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		progressDialog = null;
	}

}
