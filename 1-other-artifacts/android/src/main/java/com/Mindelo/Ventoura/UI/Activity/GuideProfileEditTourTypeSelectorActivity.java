package com.Mindelo.Ventoura.UI.Activity;



import java.util.Arrays;
import java.util.List;

import com.Mindelo.Ventoura.UI.Adapter.TourTypeSelectorAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class GuideProfileEditTourTypeSelectorActivity  extends FragmentActivity implements
OnItemClickListener{

	private ListView lvTourType;
	private List<String> tourTypelist;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_guide_profile_edit_tour_type_selector);

		String[] tourTypes = getResources().getStringArray(R.array.tour_type);
		tourTypelist = Arrays.asList(tourTypes);
		
		lvTourType = (ListView)findViewById(R.id.lv_tour_type_selector);
		lvTourType.setOnItemClickListener(this);
		
		TourTypeSelectorAdapter tourTypeSelector = new TourTypeSelectorAdapter(this, tourTypelist);
		lvTourType.setAdapter(tourTypeSelector);

	}
	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent it = new Intent();
		it.putExtra("tourTypeSelected", tourTypelist.get(position));
		setResult(RESULT_OK, it);
		finish();
	}
	
	/*
	 * The back button click to switch the menu and content
	 */
	public void btnBackOnClick(View view) {
		onBackPressed();
	}
}
