package com.Mindelo.Ventoura.UI.Activity;

import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Constant.VentouraConstant;
import com.Mindelo.Ventoura.Enum.PaymentMethod;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class GuideSetTourPriceActivity extends Activity {

	public static final String TOTAL_ALLOWED_NUMBER_CHARACTERS = "inputTotalAllowedNumberCharacters";
	public static final String ORIGINAL_CONTENT = "originalContent";

	/*
	 * views
	 */

	/*
	 * global variables
	 */
	private int priceSet = 0;
	private Button backBtn;
	private PaymentMethod paymentPethod;
	private SeekBar priceSetSeekBar;
	private TextView yourTourFeeContentTV, ventouraFeeContentTV,
			totalFeeContentTV;
	private TextView priceSetTV, yourTourFeeTV, totalTourFeeTV;
	private final int MinPrice = ConfigurationConstant.VENTOURA_MIN_PRICE;
	private final int MaxPrice = ConfigurationConstant.VENTOURA_MAX_PRICE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_set_tour_price);

		backBtn = (Button) findViewById(R.id.btn_back);
		backBtn.setText("Done");
		
		priceSetTV = (TextView)findViewById(R.id.guide_set_tour_price_tv);
		priceSetTV.setText("￡" + priceSet);
		
		yourTourFeeTV = (TextView) findViewById(R.id.guide_set_tour_price_your_tour_fee_tv);
		yourTourFeeTV.setText("￡" + priceSet);
		
		totalTourFeeTV = (TextView) findViewById(R.id.guide_set_tour_price_your_total_fee_tv);
		int totalFee = priceSet + 8;
		totalTourFeeTV.setText("￡" + totalFee);

		priceSetSeekBar = (SeekBar) findViewById(R.id.guide_set_tour_price_seekbar);
		priceSetSeekBar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onProgressChanged(SeekBar arg0, int value,
							boolean arg2) {
						priceSet = MinPrice + value * (MaxPrice - MinPrice) /100;
						priceSetTV.setText("￡" + priceSet);
						// TODO set the views to set the price

						yourTourFeeTV.setText("￡" + priceSet);
						int totalFee = priceSet + 8;
						totalTourFeeTV.setText("￡" + totalFee);
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
					}
				});

		/*
		 * set the text content based on payment method
		 */
		paymentPethod = PaymentMethod.values()[getIntent().getIntExtra(
				VentouraConstant.INTENT_LOCAL_SET_TOUR_PAYMENT_METHOD, 1)];
		if (paymentPethod == PaymentMethod.CARD) {
			//yourTourFeeContentTV.setText("shit");

		} else if (paymentPethod == PaymentMethod.CASH) {
			//yourTourFeeContentTV.setText("TODO shit");
		}

	}

	/*
	 * The back button click to switch the menu and content
	 */
	public void btnBackOnClick(View view) {

		Intent it = new Intent();
		it.putExtra(VentouraConstant.INTENT_LOCAL_SET_TOUR_PRICE, priceSet);
		setResult(RESULT_OK, it);

		finish();
	}

}
