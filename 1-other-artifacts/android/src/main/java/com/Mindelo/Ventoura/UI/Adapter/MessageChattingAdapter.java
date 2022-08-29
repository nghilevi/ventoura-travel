package com.Mindelo.Ventoura.UI.Adapter;

import java.io.File;
import java.util.ArrayList;

import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Entity.ChattingHistory;
import com.Mindelo.Ventoura.UI.Activity.R;
import com.Mindelo.Ventoura.Util.ConversationEmoticonUtil;
import com.Mindelo.Ventoura.Util.ImageUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageChattingAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<ChattingHistory> chattingHistories;
	private Bitmap userHeadImage;
	private Bitmap chattingPartnerImage;

	public MessageChattingAdapter(Context context,
			ArrayList<ChattingHistory> chattingHistories, Bitmap userHeadImage,
			Bitmap chattingPartnerImage) {

		super();
		this.mContext = context;
		this.chattingHistories = chattingHistories;
		this.userHeadImage = userHeadImage;
		this.chattingPartnerImage = chattingPartnerImage;
	}

	public int getCount() {
		return chattingHistories.size();
	}

	public Object getItem(int position) {
		return chattingHistories.get(position);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ChattingHistory chattingHistory = (ChattingHistory) this
				.getItem(position);
		ViewHolder holder = new ViewHolder();

		/*
		 * arrange the speech bubbles and head images
		 */
		if (chattingHistory.isStatusMessage()) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.adapter_chatting_single_right_message, parent,
					false);
		} else if (chattingHistory.isMine()) {
			/*
			 * message from me
			 */
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.adapter_chatting_single_right_message, parent,
					false);

			holder.chattingHistory = (TextView) convertView
					.findViewById(R.id.message_text);
			holder.chattingHistory.setTextColor(mContext.getResources()
					.getColor(R.color.text_field_color));

			int chatFaceheight = (int) (holder.chattingHistory.getTextSize() * 5 / 4);
			SpannableString textContent = getConversationContent(
					chattingHistory.getMessageContent(), chatFaceheight);
			holder.chattingHistory.setText(textContent);

			holder.sender_head_image = (ImageView) convertView
					.findViewById(R.id.message_sender_head_img);

			holder.sender_head_image.setImageBitmap(userHeadImage);
			holder.sender_head_image
					.setOnClickListener(new HeadImageOnclickListener(0));

			convertView.setTag(holder);

		} else {
			/*
			 * message to me
			 */
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.adapter_chatting_single_left_message, parent,
					false);

			holder.chattingHistory = (TextView) convertView
					.findViewById(R.id.message_text);
			holder.chattingHistory.setTextColor(mContext.getResources()
					.getColor(R.color.text_field_color));

			int chatFaceheight = (int) (holder.chattingHistory.getTextSize() * 5 / 4);
			SpannableString textContent = getConversationContent(
					chattingHistory.getMessageContent(), chatFaceheight);
			holder.chattingHistory.setText(textContent);

			holder.sender_head_image = (ImageView) convertView
					.findViewById(R.id.message_sender_head_img);


			if (chattingPartnerImage != null) {
				holder.sender_head_image
						.setImageBitmap(chattingPartnerImage);
			}
			holder.sender_head_image
					.setOnClickListener(new HeadImageOnclickListener(1));

			convertView.setTag(holder);

		}

		return convertView;
	}

	private static class ViewHolder {
		TextView chattingHistory;
		ImageView sender_head_image;
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

	private SpannableString getConversationContent(String str, int height) {
		return ConversationEmoticonUtil.getInstance().getExpressionString(
				this.mContext, str, height);
	}

}
