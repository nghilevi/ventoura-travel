package com.Mindelo.Ventoura.UI.ViewHolder;

import com.Mindelo.Ventoura.UI.Activity.R;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class OneTagViewHolder {

	public TextView tvTag;

	public ImageView ivCancel;

	public OneTagViewHolder(View view) {
		tvTag = (TextView) view.findViewById(R.id.tv_tags);
		ivCancel = (ImageView) view.findViewById(R.id.iv_cancel_tags);
	}

}
