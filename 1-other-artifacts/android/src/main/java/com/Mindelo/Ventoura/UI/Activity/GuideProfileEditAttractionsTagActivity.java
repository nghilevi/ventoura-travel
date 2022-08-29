package com.Mindelo.Ventoura.UI.Activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.Mindelo.Ventoura.UI.Adapter.GuideProfileAttractionsTagAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

public class GuideProfileEditAttractionsTagActivity extends FragmentActivity {

	private static final String TAG = "GuideProfileEditAttractionsTagActivity";

	public static final int MAX_NUMBER_ATTRACTIONS = 4;
	public static final int MAX_NUMBER_ATTRACTION_CHARACTOERS = 20;
	
	public List<Integer> deletedAttractionIndex = new ArrayList<Integer>();
	public Set<Integer> newAddedAttractionIndex = new HashSet<Integer>();

	/*
	 * views
	 */
	//private GridView gdTags;
	private GuideProfileAttractionsTagAdapter tagSelectionAdapter;

	private ListView lvTags;
	
	/*
	 * global variables
	 */
	private Button bt_action;
	private ArrayList<String> attractionsArray;
	private EditText et_add_attractions;
	private TextView tv_left_characters;

	public static final int REQUESTCODE = 0x1112;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_guide_profile_edit_attractions_tags);

		Intent intent = this.getIntent();
		attractionsArray = intent.getStringArrayListExtra("attractionsArray");
		if(attractionsArray == null){
			attractionsArray = new ArrayList<String>();
		}

		/*
		 * initialize views
		 */
		tagSelectionAdapter = new GuideProfileAttractionsTagAdapter(getApplicationContext(), attractionsArray);
		lvTags=(ListView) this.findViewById(R.id.lv_tags_display);
		lvTags.setAdapter(tagSelectionAdapter);

		et_add_attractions = (EditText) this
				.findViewById(R.id.et_guide_profile_add_tags);
		tv_left_characters = (TextView) this
				.findViewById(R.id.tv_left_number_input_characters);
		bt_action = (Button) this.findViewById(R.id.btn_action);

		tv_left_characters.setText(MAX_NUMBER_ATTRACTION_CHARACTOERS + "");
		bt_action.setText("Done");
		
		et_add_attractions.setFilters( new InputFilter[] { new InputFilter.LengthFilter(MAX_NUMBER_ATTRACTION_CHARACTOERS) } ); // max number of characters
		et_add_attractions.setImeActionLabel("Add", KeyEvent.KEYCODE_ENTER);
		et_add_attractions
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView exampleView, int actionId, KeyEvent event) {
						if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
							String newTag=et_add_attractions.getText().toString();
							if(newTag.length()==0){}else{
								attractionsArray.add(et_add_attractions.getText().toString());
								tagSelectionAdapter.notifyDataSetChanged();
							}
						}		
						return true;
					}
				});

		et_add_attractions.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				tv_left_characters.setText(MAX_NUMBER_ATTRACTION_CHARACTOERS
						- s.toString().length() + "");
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	/*
	 * The back button click to switch the menu and content
	 */
	public void btnBackOnClick(View view) {
		onBackPressed();
	}

	public void btnActionOnClick(View view) {
		Intent it = new Intent();
		it.putStringArrayListExtra("newAttractions", attractionsArray);
		setResult(RESULT_OK, it);
		finish();
	}

}
