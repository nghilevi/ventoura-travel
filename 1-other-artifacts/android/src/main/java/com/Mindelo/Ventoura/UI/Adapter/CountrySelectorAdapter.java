package com.Mindelo.Ventoura.UI.Adapter;

import java.util.List;

import com.Mindelo.Ventoura.Entity.City;
import com.Mindelo.Ventoura.Entity.Country;
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

public class CountrySelectorAdapter extends BaseAdapter implements QuickScrollScrollable{

	private Context mContext;

	public List<Country> countries;

	private int []index = new int[26];

	public CountrySelectorAdapter(Context context, List<Country> countrylist) {

		super();
		this.mContext = context;
		this.countries = countrylist;
		/*
		 * init the alphabet index
		 */
		for(int i=0; i< index.length; i++){
			index[i] = -1;
		}

	}

	public int getCount() {
		return countries.size();
	}

	public Object getItem(int position) {
		return countries.get(position);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.adapter_country_selector_item, null);
			holder = new ViewHolder();

			holder.tv_countryname = (TextView) convertView
					.findViewById(R.id.tv_country_selector_item_country);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Country tmpCountry = countries.get(position);
		
		String countryName = tmpCountry.getCountryName();
		holder.tv_countryname.setText(countryName);
		/*
		 * TO DO: iv country's flag
		 */

		return convertView;
	}

	private static class ViewHolder {
		TextView tv_countryname;
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
//		return Character.toString(countries.get(childposition).getcountryName().charAt(0)) + Character.toString(Character.toLowerCase(countries.get(childposition).getcountryName().charAt(0)));
		/*
		 * get the alphabet index indicator
		 */
		Character ch = Character.valueOf((char) ((childposition* 26)/countries.size() + 'A'));
		return ch + "";
	}

	@Override
	public int getScrollPosition(int childposition, int groupposition) {
		/*
		 * get first initial country
		 */
		int k = (childposition* 26)/countries.size();
		if(index[k] == -1){
			char ch = (char)(k + 'A');
			for(int i = 0; i < countries.size(); i++){
				if(countries.get(i).getCountryName().charAt(0) == ch){
					index[k] = i;
					break;
				}else if(countries.get(i).getCountryName().charAt(0) > ch){
					index[k] = i!=0?i - 1:i;
					break;
				}
			}
			/*
			 * the last 
			 */
			if(index[k] == -1){
				index[k] = countries.size() != 0?countries.size() - 1: countries.size();
			}
		}
	//	Log.i("index",k+ ":" + index[k]);
		return index[k];
	}

}
