package com.Mindelo.Ventoura.UI.Activity;

import com.Mindelo.Ventoura.UI.Adapter.TravellerPayBookingCreditCardAdapter;
import com.Mindelo.Ventoura.UI.View.TravellerPayBookingCreditCardViewPager;
import com.braintreepayments.api.Braintree;
import com.braintreepayments.api.models.CardBuilder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

public class TravellerPayBookingCreditCardActicity extends FragmentActivity {

	private String TAG = "TravellerPayBookingActicity";

	/*
	 * utilities
	 */
	private Activity activity;

	/*
	 * custom layouts
	 */
	private TravellerPayBookingCreditCardViewPager slideImageViewPager;
	private TravellerPayBookingCreditCardAdapter payDetailAdapter;
	private View layoutContinue;
	
	/*
	 * views
	 */
	private EditText etCardNumber, etExpiryData, etSecurityCode;

	
	/*
	 * global variables
	 */
	private String clientTokenFromServer;
	private String cardNumber;
	private String expiryData;
	private String securityCode;
	
	private final int LOCK_IMAGE = 0;
	private final int CARD_NUMBER_IMAGE = 1;
	private final int EXPIRY_DATE_IMAGE = 2;
	private final int SECURITY_CODE_IMAGE = 3;
	
	private final int CREDIT_CARD_LENGTH = 16;
	private final int EXPIRY_DATE_LENGTH = 5;
	private final int CCV_LENGTH = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");

		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.activity_traveller_pay_booking_credit_card);
		slideImageViewPager = (TravellerPayBookingCreditCardViewPager) this
				.findViewById(R.id.vp_instruction_iamge);
		
		/*
		 * initizlize global variables
		 */
		clientTokenFromServer = getIntent().getStringExtra("clientTokenFromServer");

		/*
		 * initialize layouts
		 */
		payDetailAdapter = new TravellerPayBookingCreditCardAdapter(
				getSupportFragmentManager());
		slideImageViewPager.setAdapter(payDetailAdapter);
		slideImageViewPager.setCurrentItem(LOCK_IMAGE);
		layoutContinue = this.findViewById(R.id.layout_continue);
		layoutContinue.setEnabled(false);
		layoutContinue.setOnClickListener(new PayNowOnclickListener());

		/*
		 * initialize views
		 */
		etCardNumber = (EditText) this.findViewById(R.id.et_card_number);
		etExpiryData = (EditText) this.findViewById(R.id.et_expiry_date);
		etSecurityCode = (EditText) this.findViewById(R.id.et_security_code);

		/*
		 * when editing the credit card information, do something
		 */
		etCardNumber.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					slideImageViewPager.setCurrentItem(CARD_NUMBER_IMAGE);
					etCardNumber.setBackgroundResource(R.drawable.border_pay_booking_credit_card_edit_editing_field);
				} else {
					if(etCardNumber.getText().toString().length() != CREDIT_CARD_LENGTH){
						etCardNumber.setBackgroundResource(R.drawable.border_pay_booking_credit_card_edit_error_field);
					}
				}
			}
		});
		etExpiryData.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					slideImageViewPager.setCurrentItem(EXPIRY_DATE_IMAGE);
					etExpiryData.setBackgroundResource(R.drawable.border_pay_booking_credit_card_edit_editing_field);
				} else {
					if(etExpiryData.getText().toString().length() != EXPIRY_DATE_LENGTH){
						etExpiryData.setBackgroundResource(R.drawable.border_pay_booking_credit_card_edit_error_field);
					}
				}

			}
		});
		etSecurityCode.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					slideImageViewPager.setCurrentItem(SECURITY_CODE_IMAGE);
					etSecurityCode.setBackgroundResource(R.drawable.border_pay_booking_credit_card_edit_editing_field);
				} else {
					if(etSecurityCode.getText().toString().length() != CCV_LENGTH){
						etSecurityCode.setBackgroundResource(R.drawable.border_pay_booking_credit_card_edit_error_field);
					}
				}
			}
		});

		etCardNumber.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				checkInputContent();
			}
		});

		etExpiryData.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() == 2) {
					if (start == 1) {
						s = s + "/";
						etExpiryData.setText(s);
						etExpiryData.setSelection(s.length());
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				checkInputContent();
			}
		});

		etSecurityCode.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				checkInputContent();
				
			}
		});

	}

	/**
	 * check whether the credit card information is basically valid
	 */
	private void checkInputContent() {
		String cardNumber = etCardNumber.getText().toString();
		String expiryData = etExpiryData.getText().toString();
		String securityCode = etSecurityCode.getText().toString();
		if (cardNumber.length() == CREDIT_CARD_LENGTH && expiryData.length() == EXPIRY_DATE_LENGTH
				&& securityCode.length() == CCV_LENGTH) {
			layoutContinue.setEnabled(true);
			layoutContinue.setBackgroundResource(R.color.ventoura_color);
		}
	}

	@Override
	protected void onDestroy() {
		Log.i(TAG, "onDestroy");
		super.onDestroy();
	}

	/*
	 * The back button click to switch the menu and content
	 */
	public void btnBackOnClick(View view) {
		onBackPressed();
	}

	private class PayNowOnclickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (layoutContinue.isEnabled()) {				
				CardBuilder cardBuilder = new CardBuilder()
			    .cardNumber(cardNumber)
			    .cvv(securityCode)
			    .expirationDate(expiryData);
				/*
				 * Collect the credit card information and generate the Braintree nonce
				 * this method will trigger the nonce listener in PaymentBookingSummaryActivity
				 */
				Braintree braintree = Braintree.getInstance(activity, clientTokenFromServer);
				braintree.tokenize(cardBuilder);
				
				/*
				 * return to caller
				 */
				Intent it = new Intent();
				setResult(RESULT_OK, it);
				finish();
			} 
		}
	}
	
}
