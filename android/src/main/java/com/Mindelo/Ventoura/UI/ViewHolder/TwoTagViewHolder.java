package com.Mindelo.Ventoura.UI.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.Mindelo.Ventoura.UI.Activity.R;

public class TwoTagViewHolder {

	public TextView tvTagLeft;

	public ImageView ivCancelLeft;

	public TextView tvTagRight;

	public ImageView ivCancelRight;

	public TwoTagViewHolder(View view) {
		tvTagLeft = (TextView) view.findViewById(R.id.tv_tags_left);
		ivCancelLeft = (ImageView) view.findViewById(R.id.iv_cancel_tags_left);
		tvTagRight = (TextView) view.findViewById(R.id.tv_tags_right);
		ivCancelRight = (ImageView) view.findViewById(R.id.iv_cancel_tags_right);
	}
}
