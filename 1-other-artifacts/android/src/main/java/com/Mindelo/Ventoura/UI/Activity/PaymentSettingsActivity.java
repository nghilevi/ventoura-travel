package com.Mindelo.Ventoura.UI.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class PaymentSettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment_settings);
	}
	
	/*
	 * The back button click to switch the menu and content
	 */
	public void btnBackOnClick(View view) {
		onBackPressed();
	}
}
