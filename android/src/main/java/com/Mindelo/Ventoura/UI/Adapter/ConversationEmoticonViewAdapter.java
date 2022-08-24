package com.Mindelo.Ventoura.UI.Adapter;

import java.util.List;

import com.Mindelo.Ventoura.Entity.ConversationEmoticon;
import com.Mindelo.Ventoura.UI.Activity.R;
import com.Mindelo.Ventoura.UI.ViewHolder.ConversationEmoticonViewHolder;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ConversationEmoticonViewAdapter extends BaseAdapter {

	private List<ConversationEmoticon> chatFaces;

	private LayoutInflater inflater;

	public ConversationEmoticonViewAdapter(Context context, List<ConversationEmoticon> chatFaces) {
		this.chatFaces = chatFaces;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return this.chatFaces.size();
	}

	@Override
	public Object getItem(int position) {
		return chatFaces.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ConversationEmoticon chatFace = this.chatFaces.get(position);
		ConversationEmoticonViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.conversation_emoticon_face_item, null);
			viewHolder = new ConversationEmoticonViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ConversationEmoticonViewHolder) convertView.getTag();
		}
		if (chatFace.getFaceImageResId() == R.drawable.conversation_emoticon_delete_icon) {
			convertView.setBackgroundDrawable(null);
			viewHolder.ivChatFace
					.setImageResource(chatFace.getFaceImageResId());
		} else if (TextUtils.isEmpty(chatFace.getFaceImageDescription())) {
			convertView.setBackgroundDrawable(null);
			viewHolder.ivChatFace.setImageDrawable(null);
		} else {
			viewHolder.ivChatFace.setTag(chatFace);
			viewHolder.ivChatFace
					.setImageResource(chatFace.getFaceImageResId());
		}
		return convertView;
	}

}
