package com.Mindelo.Ventoura.UI.Adapter;

import java.io.InputStream;

import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Enum.UserRole;
import com.Mindelo.Ventoura.JSONEntity.JSONVentoura;
import com.Mindelo.Ventoura.Model.CardModel;
import com.Mindelo.Ventoura.UI.Activity.R;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public final class VentouraCardStackAdapter extends CardStackAdapter {

	private Context context;

	private static final int[] VENTOURING_STARS = { R.drawable.star_black_1,
			R.drawable.star_black_2, R.drawable.star_black_3,
			R.drawable.star_black_4, R.drawable.star_black_5,
			R.drawable.star_black_6, R.drawable.star_black_7,
			R.drawable.star_black_8, R.drawable.star_black_9,
			R.drawable.star_black_10 };

	public VentouraCardStackAdapter(Context mContext) {
		super(mContext);
		this.context = mContext;
	}

	@Override
	public View getCardView(int position, CardModel model, View convertView,
			ViewGroup parent) {
		
		JSONVentoura ventoura = model.getVentoura();
		
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.ventoura_card_inner,
					parent, false);
			assert convertView != null;
		}

		// set ventoura image
		((ImageView) convertView.findViewById(R.id.ventouring_rounded_image))
				.setImageDrawable(model.getCardImageDrawable());
		
		TextView userType = (TextView) convertView
				.findViewById(R.id.ventoura_type_tv);
		ImageView stars = ((ImageView) convertView.findViewById(R.id.ventoura_stars_iv));
		if (ventoura.getUserRole() == UserRole.GUIDE) {
			if(ventoura.getTourType() != null){
				userType.setText(ventoura.getTourType()); 	
			}
			stars.setImageResource(VENTOURING_STARS[(int)Math.round(ventoura.getAvgReviewScore())]);
		} else {
			userType.setText("Traveller");
			stars.setVisibility(View.GONE);
		}
		
		/*
		 * set the country flag
		 */
		try {
			InputStream ims;
			ims = context.getAssets().open(
					ConfigurationConstant.VENTOURA_ASSET_COUNTRY_FLAG + "/"
							+ ventoura.getCountry() + ".png");
			Drawable countryFlag = Drawable.createFromStream(ims, null);
			((ImageView)convertView.findViewById(R.id.ventoura_nationality)).setImageDrawable(countryFlag);
			
		} catch (Exception e) {
			e.printStackTrace();	
		}


		return convertView;
	}
}
