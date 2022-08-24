package com.Mindelo.Ventoura.UI.Activity;

import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Entity.ImageMatch;
import com.Mindelo.Ventoura.Enum.UserRole;
import com.Mindelo.Ventoura.Ghost.IService.IMatchesService;
import com.Mindelo.Ventoura.Ghost.IService.IPaymentService;
import com.Mindelo.Ventoura.Ghost.Service.MatchesService;
import com.Mindelo.Ventoura.Ghost.Service.PaymentService;
import com.Mindelo.Ventoura.JSONEntity.JSONBooking;
import com.Mindelo.Ventoura.UI.View.RoundedImageView;
import com.Mindelo.Ventoura.Util.BitmapUtil;
import com.Mindelo.Ventoura.Util.ImageUtil;
import com.braintreepayments.api.Braintree;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TravellerPayBookingSummaryActivity extends Activity {

	private final String TAG = "com.Mindelo.Ventoura.UI.Activity.PaymentActivity";

	/*
	 * utilities
	 */
	private IPaymentService paymentService;
	private Braintree braintree;
	private IMatchesService matchService;
	Braintree.PaymentMethodNonceListener paymentMethodNonceListener;
	/*
	 * global variables
	 */
	private JSONBooking jsonBooking;
	private String clientTokenFromServer;
	private String clientPaymentNonce;
	
	private static final int REQUEST_CODE_CREDIT_CARD = 0;
	private static final int REQUEST_CODE_PAYPAL = 1;
	private int selectedMethod;

	/*
	 * views
	 */
	private RelativeLayout layout;
	private View left, right;
	private RelativeLayout payPalMethod, creditCardMethod;
	private TextView tvTouristName, tvTourCity, tvTourTime, tvLocalFee,
			tvVentouraFee, tvTotalFee;
	private RoundedImageView headImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate start");
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_traveller_pay_booking_summary);
		layout = (RelativeLayout) this.findViewById(R.id.layout_ventoura_fee);

		/*
		 * initialize utilities
		 */
		paymentService = new PaymentService();
		matchService = new MatchesService(this);

		/*
		 * initialize global variables
		 */
		jsonBooking = (JSONBooking) getIntent().getSerializableExtra(
				"jsonBooking");

		/*
		 * initialize views
		 */
		left = this.findViewById(R.id.layout_left_vertical_line);
		right = this.findViewById(R.id.layout_right_vertical_line);

		payPalMethod = (RelativeLayout) this
				.findViewById(R.id.layout_payment_paypal_method);
		creditCardMethod = (RelativeLayout) this
				.findViewById(R.id.layout_payment_credit_card_method);
		payPalMethod.setOnClickListener(new PaymentMethodOnclickListener());
		creditCardMethod.setOnClickListener(new PaymentMethodOnclickListener());

		headImage = (RoundedImageView) this
				.findViewById(R.id.traveller_pay_booking_summary_head_image);
		tvLocalFee = (TextView) this.findViewById(R.id.tv_local_fee);
		tvTotalFee = (TextView) this.findViewById(R.id.tv_total_fee);
		tvVentouraFee = (TextView) this.findViewById(R.id.tv_ventoura_fee);
		tvTouristName = (TextView) this.findViewById(R.id.tv_tourist_name);
		tvTourCity = (TextView) findViewById(R.id.tv_tourist_city);
		tvTourTime = (TextView) findViewById(R.id.tv_tourist_time_info);
		/*
		 * set the views
		 */
		if (jsonBooking != null) {
			try {
				
				ImageMatch headMatchImage = matchService.getSingleMatchImageFromDB(jsonBooking.getGuideId(), UserRole.GUIDE.getNumVal());
				if(headMatchImage != null){
					Bitmap bitMap = BitmapUtil.byteArrayToBitMap(
							headMatchImage.getImageContent(),
							ConfigurationConstant.NORMAL_USER_PORTAL_IMAGE_WIDTH,
							ConfigurationConstant.NORMAL_USER_PORTAL_IMAGE_HEIGHT);
					headImage.setImageBitmap(bitMap);	
				}

				tvVentouraFee
						.setText(jsonBooking.getTourPrice()
								* (1 - ConfigurationConstant.VENTOURA_BOOKING_PERCENTAGE)
								+ "");
				tvLocalFee.setText(jsonBooking.getTourPrice()
						* ConfigurationConstant.VENTOURA_BOOKING_PERCENTAGE
						+ "");
				tvTotalFee.setText(jsonBooking.getTourPrice() + "");
				tvTouristName.setText(jsonBooking.getTravellerFirstname());
				tvTourCity.setText("London-Fix");
				tvTourTime.setText(jsonBooking.getTourDate());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(paymentMethodNonceListener != null){
			braintree.removeListener(paymentMethodNonceListener);	
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		RelativeLayout.LayoutParams paramleft = new RelativeLayout.LayoutParams(
				left.getWidth(), layout.getHeight());
		RelativeLayout.LayoutParams paramright = new RelativeLayout.LayoutParams(
				right.getWidth(), layout.getHeight());
		paramright.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		left.setLayoutParams(paramleft);
		right.setLayoutParams(paramright);
	}

	/*
	 * The back button click to switch the menu and content
	 */
	public void btnBackOnClick(View view) {
		onBackPressed();
	}
	
	
	private class PaymentMethodOnclickListener implements OnClickListener {
		@Override
		public void onClick(View view) {
			
			switch (view.getId()) {
			case R.id.layout_payment_paypal_method:
				selectedMethod = REQUEST_CODE_PAYPAL;
				break;
			case R.id.layout_payment_credit_card_method:
				selectedMethod = REQUEST_CODE_CREDIT_CARD;
				break;
			}
			
			GetClientTokenFromServerTask getClientTokenFromServerTask = new GetClientTokenFromServerTask();
			getClientTokenFromServerTask.execute();
			
		}
	}
	

	/**
	 * Handle payment network task: retrieve a token from server and sent the
	 * nonce to the server
	 */
	private class GetClientTokenFromServerTask extends
			AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			clientTokenFromServer = paymentService.getBraintreeClientToken();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			/*
			 * Collect the generated the Braintree nonce which can be from credit card or paypal
			 */
			braintree = Braintree.getInstance(TravellerPayBookingSummaryActivity.this, clientTokenFromServer);
			paymentMethodNonceListener = new Braintree.PaymentMethodNonceListener() {
				public void onPaymentMethodNonce(String paymentMethodNonce) {
					clientPaymentNonce = paymentMethodNonce;
					PurchasePostTokenTask purchasePostTokenTask = new PurchasePostTokenTask();
					purchasePostTokenTask.execute();
				}
			};
			braintree.addListener(paymentMethodNonceListener);
			switch(selectedMethod){
			case REQUEST_CODE_CREDIT_CARD:
				Intent intent;
				intent = new Intent(TravellerPayBookingSummaryActivity.this,
						TravellerPayBookingCreditCardActicity.class);
				intent.putExtra("clientTokenFromServer", clientTokenFromServer);
				startActivityForResult(intent, REQUEST_CODE_CREDIT_CARD);
				break;
			case REQUEST_CODE_PAYPAL:
				braintree.startPayWithPayPal(TravellerPayBookingSummaryActivity.this, REQUEST_CODE_PAYPAL);
				break;
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent data) {
		if (requestCode == REQUEST_CODE_CREDIT_CARD) {
			if (resultCode == RESULT_OK) {
				//TODO, returned from credit card
			}
		} else if (requestCode == REQUEST_CODE_PAYPAL) {
			if (resultCode == RESULT_OK) {
				braintree.finishPayWithPayPal(TravellerPayBookingSummaryActivity.this, resultCode, data);
			}
		}
	}

	/**
	 * network task for posting the token to the server
	 */
	public class PurchasePostTokenTask extends
			AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {
			return paymentService
					.postBraintreeNonceTokenToServer(clientPaymentNonce);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result.booleanValue()) {
				Toast.makeText(TravellerPayBookingSummaryActivity.this,
						"You have done payments for this booking.",
						Toast.LENGTH_SHORT).show();
				// TODO more
			} else {
				Toast.makeText(TravellerPayBookingSummaryActivity.this, "You payment is not successful",
						Toast.LENGTH_SHORT).show();
				//TODO more
			}
		}
	}

}
