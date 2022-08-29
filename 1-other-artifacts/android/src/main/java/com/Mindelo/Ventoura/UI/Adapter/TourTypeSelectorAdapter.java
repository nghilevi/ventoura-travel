package com.Mindelo.Ventoura.UI.Adapter;

import java.util.List;

import com.Mindelo.Ventoura.UI.Activity.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TourTypeSelectorAdapter extends BaseAdapter{

	private Context mContext;

	public List<String> tourTypes;


	public TourTypeSelectorAdapter(Context context, List<String> tourTypeList) {

		super();
		this.mContext = context;
		this.tourTypes = tourTypeList;
	}

	public int getCount() {
		return tourTypes.size();
	}

	public Object getItem(int position) {
		return tourTypes.get(position);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.adapter_guide_profile_edit_tour_type_selector_item, null);
			holder = new ViewHolder();

			holder.tv_tourType = (TextView) convertView
					.findViewById(R.id.tv_tour_type_selector_item);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		String temTourType = tourTypes.get(position);
		
		holder.tv_tourType.setText(temTourType);

		return convertView;
	}

	private static class ViewHolder {
		TextView tv_tourType;
	}

	public long getItemId(int position) {
		// Unimplemented, because we aren't using Sqlite.
		return position;
	}

}
