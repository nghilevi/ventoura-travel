package com.Mindelo.Ventoura.UI.Activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PureInputActivity extends Activity {
	
	
	public static final String TOTAL_ALLOWED_NUMBER_CHARACTERS = "inputTotalAllowedNumberCharacters";
	public static final String ORIGINAL_CONTENT = "originalContent";
	
	/*
	 * views
	 */
	private EditText inputContent;
	private TextView leftNumberOfCharacters;
	private Button btn_action;
	
	/*
	 * global variables
	 */
	private int inputTotalAllowedNumberCharacters;
	private String originalContent;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pure_input);
		
		inputTotalAllowedNumberCharacters = getIntent().getIntExtra(TOTAL_ALLOWED_NUMBER_CHARACTERS, 0);
		originalContent = getIntent().getStringExtra(ORIGINAL_CONTENT);
		
		inputContent = (EditText) findViewById(R.id.activity_input_et_input_content);
		leftNumberOfCharacters = (TextView)findViewById(R.id.activity_et_left_number_input_characters);
		
		inputContent.setText(originalContent);
		inputContent.setSelection(originalContent.length()); // put the cursor to the end
		
		leftNumberOfCharacters.setText(inputTotalAllowedNumberCharacters - originalContent.length() + "");
		
		
		inputContent.setFilters( new InputFilter[] { new InputFilter.LengthFilter(inputTotalAllowedNumberCharacters) } );
		btn_action = (Button) findViewById(R.id.btn_action);
		btn_action.setText("Done");
		
		inputContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				leftNumberOfCharacters.setText(inputTotalAllowedNumberCharacters - s.toString().length() + "");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
	}
	
	/*
	 * The back button click to switch the menu and content
	 */
	public void btnBackOnClick(View view) {
		onBackPressed();
	}
	
	public void btnActionOnClick(View view){
		Intent it = new Intent();
		it.putExtra("inputContent", inputContent.getText().toString());
		setResult(RESULT_OK, it);
		finish();
	}
	
	
}
