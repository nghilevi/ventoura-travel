package com.Mindelo.Ventoura.UI.Adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Constant.VentouraConstant;
import com.Mindelo.Ventoura.Entity.ChattingHistory;
import com.Mindelo.Ventoura.Entity.City;
import com.Mindelo.Ventoura.Entity.ImageMatch;
import com.Mindelo.Ventoura.Enum.Gender;
import com.Mindelo.Ventoura.Enum.UserRole;
import com.Mindelo.Ventoura.Ghost.IService.IChattingHistoryService;
import com.Mindelo.Ventoura.Ghost.IService.ICityService;
import com.Mindelo.Ventoura.Ghost.IService.IMatchesService;
import com.Mindelo.Ventoura.Ghost.Service.ChattingHistoryService;
import com.Mindelo.Ventoura.Ghost.Service.CityService;
import com.Mindelo.Ventoura.Ghost.Service.MatchesService;
import com.Mindelo.Ventoura.JSONEntity.JSONMatch;
import com.Mindelo.Ventoura.UI.Activity.R;
import com.Mindelo.Ventoura.UI.View.RoundedImageView;
import com.Mindelo.Ventoura.Util.BitmapUtil;
import com.Mindelo.Ventoura.Util.ConversationEmoticonUtil;
import com.Mindelo.Ventoura.Util.DateTimeUtil;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MessageListAdapter extends BaseAdapter {

	private Activity activity;

	private List<JSONMatch> list;
	private IChattingHistoryService chattingHistoryService;
	private IMatchesService matchService;
	private ICityService cityService;

	private long userId;
	private UserRole userRole;

	public MessageListAdapter(Activity activity, List<JSONMatch> list,
			long userId, UserRole userRole) {

		this.activity = activity;
		
		this.list = new ArrayList<JSONMatch>();
		for(JSONMatch jsonMatch : list){
			if(applyMessageFilter(jsonMatch)){
				this.list.add(jsonMatch);
			}
		}
		this.chattingHistoryService = new ChattingHistoryService(activity);
		this.matchService = new MatchesService(activity);
		this.cityService = new CityService(activity);
		this.userId = userId;
		this.userRole = userRole;
	}

	public int getCount() {
		return list.size(); // 加入第一行的标题，list的长度应加一
	}

	public Object getItem(int position) {

		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup viewGroup) {

		ViewHolder holder;
		int textVentouraColor = activity.getResources().getColor(
				R.color.ventoura_color);
		int detaultColor = activity.getResources().getColor(R.color.gray);
		JSONMatch friend = list.get(position);

		/*
		 * 1. initiate the item views
		 */
		if (convertView == null) {
			convertView = LayoutInflater.from(activity).inflate(
					R.layout.adapter_message_item, null);
			holder = new ViewHolder();

			holder.tvName = (TextView) convertView
					.findViewById(R.id.tv_message_list_name);
			holder.tvTime = (TextView) convertView
					.findViewById(R.id.tv_message_list_time);
			holder.headImageView = (RoundedImageView) convertView
					.findViewById(R.id.iv_message_list_head_image);
			holder.tvMsg = (TextView) convertView
					.findViewById(R.id.tv_message_list_unread);
			holder.tvUnreadNumber = (TextView) convertView
					.findViewById(R.id.tv_message_unread_number_tips);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		/*
		 * 2. chatter basic information
		 */
		String name = friend.getUserFirstname();

		holder.tvName.setText(name);
		holder.tvMsg.setTextColor(detaultColor);

		ImageMatch headImage = matchService.getSingleMatchImageFromDB(friend.getUserId(),
				friend.getUserRole().getNumVal());
		if (headImage != null) {
			holder.headImageView.setImageBitmap(BitmapUtil.byteArrayToBitMap(
					headImage.getImageContent(),
					ConfigurationConstant.SMALL_USER_PORTAL_IMAGE_WIDTH,
					ConfigurationConstant.SMALL_USER_PORTAL_IMAGE_HEIGHT));
		}

		/*
		 * TO DO set the number of unread message with each friend
		 */
		int unreadNumber = chattingHistoryService
				.getNumberOfUnreadChattingHistoryParticularPartner(userId,
						friend.getUserId(), userRole.getNumVal(), friend
								.getUserRole().getNumVal());
		if (unreadNumber == 0) {
			holder.tvUnreadNumber.setText("");
			holder.tvUnreadNumber.setVisibility(View.INVISIBLE);
		} else {
			holder.tvUnreadNumber.setText("" + unreadNumber);
			holder.tvUnreadNumber.setVisibility(View.VISIBLE);
		}
		/*
		 * 3. set the information
		 */
		ChattingHistory lastMessage = chattingHistoryService
				.getLastMessageWithASpecificChatter(userId, friend.getUserId(),
						userRole.getNumVal(), friend.getUserRole().getNumVal());

		if (lastMessage != null) {
			/*
			 * has chatted with this guy
			 */
			int chatFaceheight = (int) (holder.tvMsg.getTextSize() * 5 / 4);
			SpannableString textContent = getConversationContent(
					lastMessage.getMessageContent(), chatFaceheight);
			holder.tvMsg.setHeight(chatFaceheight * 5 / 2);
			holder.tvMsg.setText(textContent);
			if (lastMessage.isRead() == false) {
				// last message is not read yet
				holder.tvMsg.setTypeface(null, Typeface.BOLD);
				holder.tvTime.setTextColor(textVentouraColor);
			} else {
				holder.tvTime.setTextColor(detaultColor);
			}

			/*
			 * set last message time
			 */
			holder.tvTime.setText(DateTimeUtil
					.dateToHumanBlurSenceTime(lastMessage.getDateTime()));

		} else {
			/*
			 * the user has not chatted with this guy
			 */
			if (DateTimeUtil.daysBetween(
					DateTimeUtil.fromStringToDate(friend.getTimeMatched()),
					new Date()) > 6) {
				holder.tvTime.setTextColor(textVentouraColor);
				holder.tvTime.setText(R.string.new_match);
				holder.tvName.setTextColor(textVentouraColor);
			} else {
				holder.tvTime.setText(R.string.earlier_match);
			}
			if (friend.getUserRole() == UserRole.GUIDE) {

				City Fridentcity = cityService.getCityById(friend.getCity());
				if (Fridentcity == null) {
					holder.tvMsg.setText(R.string.no_city);
				}
			} else {
				holder.tvMsg.setText(R.string.traveller_match);
			}

		}

		return convertView;
	}

	static class ViewHolder {
		TextView tvName;
		TextView tvAge;
		TextView tvCity;
		TextView tvMsg;
		TextView tvTime;
		TextView tvUnreadNumber;
		RoundedImageView headImageView;
	}

	private SpannableString getConversationContent(String str, int height) {
		return ConversationEmoticonUtil.getInstance().getExpressionString(
				this.activity, str, height);
	}
	
	private boolean applyMessageFilter(JSONMatch match) {
		
		boolean filterFlag = true; 
		

		SharedPreferences sharedPre = activity.getSharedPreferences(
				VentouraConstant.SHARED_PREFERENCE_VENTOURA,
				Context.MODE_PRIVATE);
		
		int minAge = sharedPre.getInt(
				VentouraConstant.PRE_MESSAGE_FILTER_MIN_AGE, 0);
		int maxAge = sharedPre.getInt(
				VentouraConstant.PRE_MESSAGE_FILTER_MAX_AGE, 120);
		if(match.getAge() > maxAge || match.getAge() < minAge){
			return false;
		}
		
		filterFlag = sharedPre.getBoolean(
				VentouraConstant.PRE_MESSAGE_FILTER_GENDER_FEMALE, true);
		if(filterFlag == false){
			if(match.getGender() == Gender.FEMALE){
				return false;	
			}
		}
		filterFlag = sharedPre.getBoolean(
				VentouraConstant.PRE_MESSAGE_FILTER_GENDER_MALE, true);
		if(filterFlag == false){
			if(match.getGender() == Gender.MALE){
				return false;	
			}
		}
		
		filterFlag = sharedPre.getBoolean(
				VentouraConstant.PRE_MESSAGE_FILTER_USER_ROLE_TRAVELLER, true);
		if(filterFlag == false){
			if(match.getUserRole() == UserRole.TRAVELLER){
				return false;	
			}
		}
		filterFlag = sharedPre.getBoolean(
				VentouraConstant.PRE_MESSAGE_FILTER_USER_ROLE_LOCAL, true);
		if(filterFlag == false){
			if(match.getUserRole() == UserRole.GUIDE){
				return false;	
			}
		}
		
		/*
		userPreMale = sharedPre.getBoolean(
				VentouraConstant.PRE_MESSAGE_FILTER_BOOK_REQUEST, true);
		userPreMale = sharedPre.getBoolean(
				VentouraConstant.PRE_MESSAGE_FILTER_BOOK_PAID, true);
		userPreMale = sharedPre.getBoolean(
				VentouraConstant.PRE_MESSAGE_FILTER_BOOK_OTHERS, true); */
		//TODO handle filter about booking
		
		return true;
	}

}