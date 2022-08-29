package com.Mindelo.Ventoura.UI.ViewHolder;

import com.Mindelo.Ventoura.UI.Activity.R;

import android.view.View;
import android.widget.ImageView;

public class ConversationEmoticonViewHolder {
	public ImageView ivChatFace;

	public ConversationEmoticonViewHolder(View view) {
		ivChatFace = (ImageView) view.findViewById(R.id.item_iv_face);
	}
}
