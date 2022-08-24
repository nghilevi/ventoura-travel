package com.Mindelo.Ventoura.UI.Adapter;

import java.util.List;

import com.Mindelo.Ventoura.Entity.City;
import com.Mindelo.Ventoura.UI.Activity.R;
import com.Mindelo.Ventoura.UI.View.QuickScrollScrollable;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FilterCitySelectorAdapter extends BaseAdapter implements QuickScrollScrollable{

	private Context mContext;

	public List<City> citys;

	private int []index = new int[26];

	public FilterCitySelectorAdapter(Context context, List<City> citylist) {

		super();
		this.mContext = context;
		this.citys = citylist;
		/*
		 * init the alphabet index
		 */
		for(int i=0; i< index.length; i++){
			index[i] = -1;
		}

	}

	public int getCount() {
		return citys.size();
	}

	public Object getItem(int position) {
		return citys.get(position);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.adapter_filter_city_selector_item, null);
			holder = new ViewHolder();

			holder.tv_cityname = (TextView) convertView
					.findViewById(R.id.filter_city_selector_item_city);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		City tmpCity = citys.get(position);
		
		String cityName = tmpCity.getCityName();
		holder.tv_cityname.setText(cityName);
		/*
		 * TO DO: iv country's flag
		 */

		return convertView;
	}

	private static class ViewHolder {
		TextView tv_cityname;
	}

	public long getItemId(int position) {
		// Unimplemented, because we aren't using Sqlite.
		return position;
	}

	private class HeadImageOnclickListener implements OnClickListener {

		private int meOrChattingPartnerFlag;

		public HeadImageOnclickListener(int meOrChattingPartnerFlag) {
			this.meOrChattingPartnerFlag = meOrChattingPartnerFlag;
		}

		public void onClick(View v) {
			if (meOrChattingPartnerFlag == 0) {
				// TODO fix
			} else {
				// TODO fix
			}

		}

	}

	@Override
	public String getIndicatorForPosition(int childposition, int groupposition) {
//		return Character.toString(citys.get(childposition).getCityName().charAt(0)) + Character.toString(Character.toLowerCase(citys.get(childposition).getCityName().charAt(0)));
		/*
		 * get the alphabet index indicator
		 */
		Character ch = Character.valueOf((char) ((childposition* 26)/citys.size() + 'A'));
		return ch + "";
	}

	@Override
	public int getScrollPosition(int childposition, int groupposition) {
		/*
		 * get first initial city
		 */
		int k = (childposition* 26)/citys.size();
		if(index[k] == -1){
			char ch = (char)(k + 'A');
			for(int i = 0; i < citys.size(); i++){
				if(citys.get(i).getCityName().charAt(0) == ch){
					index[k] = i;
					break;
				}else if(citys.get(i).getCityName().charAt(0) > ch){
					index[k] = i!=0?i - 1:i;
					break;
				}
			}
			/*
			 * the last 
			 */
			if(index[k] == -1){
				index[k] = citys.size() != 0?citys.size() - 1: citys.size();
			}
		}
	//	Log.i("index",k+ ":" + index[k]);
		return index[k];
	}

}
