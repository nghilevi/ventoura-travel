package com.Mindelo.Ventoura.UI.Fragment;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import com.Mindelo.Ventoura.Constant.BroadcastConstant;
import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Constant.IMConstant;
import com.Mindelo.Ventoura.Entity.ChattingHistory;
import com.Mindelo.Ventoura.Enum.UserRole;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.Mindelo.Ventoura.Ghost.IService.IChattingHistoryService;
import com.Mindelo.Ventoura.Ghost.IService.IGuideService;
import com.Mindelo.Ventoura.Ghost.IService.IMatchesService;
import com.Mindelo.Ventoura.Ghost.IService.ITravellerService;
import com.Mindelo.Ventoura.JSONEntity.JSONMatch;
import com.Mindelo.Ventoura.JSONEntity.JSONMatchesList;
import com.Mindelo.Ventoura.UI.Activity.GuideMessageFilterActivity;
import com.Mindelo.Ventoura.UI.Activity.R;
import com.Mindelo.Ventoura.UI.Activity.TravellerConversationActivity;
import com.Mindelo.Ventoura.UI.Activity.TravellerMessageFilterActivity;
import com.Mindelo.Ventoura.UI.Activity.TravellerPortalActivity;
import com.Mindelo.Ventoura.UI.Adapter.MessageListAdapter;
import com.Mindelo.Ventoura.UI.View.PopupActionItem;
import com.Mindelo.Ventoura.UI.View.BottomPopupMenuAction;
import com.Mindelo.Ventoura.UI.View.RoundedImageView;
import com.Mindelo.Ventoura.UI.View.TopPopupMenuAction;
import com.Mindelo.Ventoura.Util.BitmapUtil;
import com.Mindelo.Ventoura.Util.DateTimeUtil;

public class TravellerMessageFragment extends Fragment implements
		OnItemClickListener {

	private static final String TAG = "TravellerMessageFragment";

	public static boolean isActive = false;

	/*
	 * Utility instances
	 */
	private TravellerPortalActivity activity;
	private long travellerId;
	private IMatchesService matchesService;
	private ITravellerService travellerService;
	private IChattingHistoryService chattingHistoryService;
	private IGuideService guideService;
	private SharedPreferences sharePre;

	/*
	 * Global variables
	 */

	private List<JSONMatch> chatslist = new ArrayList<JSONMatch>();
	private MessageListAdapter msgAdapter;
	private ListView lvMessages;
	private int selectedMatchIndex = -1;


	/*
	 * bottom menu popup windows
	 */
	private static final int ID_BOTTOM_MENU_STICKTOP = 1;
	private static final int ID_BOTTOM_MENU_DELETE = 2;
	private BottomPopupMenuAction bottomMenuAction;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity = (TravellerPortalActivity) getActivity();

		matchesService = activity.getMatchesService();
		travellerId = activity.getTravellerId();
		guideService = activity.getGuideService();
		travellerService = activity.getTravellerService();
		chattingHistoryService = activity.getChattingHistoryService();
		sharePre = activity.getSharedPre();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);

		View view = inflater.inflate(R.layout.fragment_traveller_message,
				container, false);

		/*
		 * set msg adapter
		 */
		lvMessages = (ListView) view.findViewById(R.id.lv_traveller_message);
		lvMessages.setOnItemClickListener(this);
		lvMessages
				.setOnItemLongClickListener(new MessageItemLongClickListener());

		/*
		 * init the right option button
		 */
		Button optionButton = (Button) view.findViewById(R.id.btn_action);
		optionButton.setText("Filter");
		optionButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getActivity(),
						TravellerMessageFilterActivity.class));
			}
		});

		/*
		 * ini pop menus
		 */
		initPopupBottomMenu();
		
		/*
		 * load original matches and then probe the server
		 */
		loadMatchesFromDB();
		LoadMatchesTask loadMatchesTask = new LoadMatchesTask();
		loadMatchesTask.execute();

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		isActive = true;
		activity.registerReceiver(imReceiver, new IntentFilter(
				IMConstant.INCOMING_MESSAGE_NOTICE_MESSAGE_LIST));
		// back from conversation, need to set the last message
		if (selectedMatchIndex != -1) {
			ChattingHistory message = chattingHistoryService
					.getLastMessageWithASpecificChatter(travellerId, chatslist
							.get(selectedMatchIndex).getUserId(),
							UserRole.TRAVELLER.getNumVal(),
							chatslist.get(selectedMatchIndex).getUserRole()
									.getNumVal());
			if (message != null) {
				updateSingleMatchItem(selectedMatchIndex, message);
			}
		}

		activity.registerReceiver(messgeFilterUpdatedReveiver,
				new IntentFilter(
						BroadcastConstant.USER_MESSAGE_FILTER_UPDATED_ACTION));

	}

	@Override
	public void onPause() {
		super.onPause();
		isActive = false;
		activity.unregisterReceiver(imReceiver);
	}
	
	@Override 
	public void onDestroy(){
		super.onDestroy();
		activity.unregisterReceiver(messgeFilterUpdatedReveiver);
	}

	/**
	 * Message filter updated receiver
	 */
	private BroadcastReceiver messgeFilterUpdatedReveiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// refresh message list
			setMessageListView();
		}
	};

	/**
	 * Incoming message receiver
	 */
	private BroadcastReceiver imReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {

				ChattingHistory message = (ChattingHistory) bundle
						.getSerializable(IMConstant.INCOMING_MESSAGE_NOTICE_MESSAGE_PAYLOAD);

				int index = -1;
				boolean foundMatch = false;
				for (JSONMatch jsonMatch : chatslist) {
					index++;
					if (jsonMatch.getUserRole().getNumVal() == message
							.getPartnerRole()
							&& jsonMatch.getUserId() == message.getPartnerId()) {
						foundMatch = true;
						break;
					}
				}
				if (foundMatch == true) {
					updateSingleMatchItem(index, message);
				}

				Log.i(TAG, "a message from IM's broad cast");
			}
		}
	};

	/**
	 * update single friend item
	 */
	private void updateSingleMatchItem(int index, ChattingHistory message) {
		View v = lvMessages.getChildAt(index
				- lvMessages.getFirstVisiblePosition());
		
		if(v == null){
			return ;
		}
		
		int textVentouraColor = activity.getResources().getColor(
				R.color.ventoura_color);
		TextView tvTime = (TextView) v.findViewById(R.id.tv_message_list_time);
		tvTime.setText(DateTimeUtil.dateToHumanBlurSenceTime(message
				.getDateTime()));

		TextView tvMsg = (TextView) v.findViewById(R.id.tv_message_list_unread);
		tvMsg.setText(message.getMessageContent());
		// if the message is not been read
		if (!message.isRead()) {

			tvTime.setTextColor(textVentouraColor);
			tvMsg.setTypeface(null, Typeface.BOLD);

			int unreadNumber = chattingHistoryService
					.getNumberOfUnreadChattingHistoryParticularPartner(
							message.getUserId(), message.getPartnerId(),
							message.getUserRole(), message.getPartnerRole());
			TextView tvUnreadNumber = (TextView) v
					.findViewById(R.id.tv_message_unread_number_tips);
			tvUnreadNumber.setText("" + unreadNumber);
			tvUnreadNumber.setVisibility(View.VISIBLE);
		}
	}

	private class MessageItemLongClickListener implements
			OnItemLongClickListener {
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			selectedMatchIndex = position;

			bottomMenuAction.show(view);
			return true;
		}
	}

	private void loadMatchesFromDB() {

		JSONMatchesList jsonMatchList = matchesService
				.getTravellerMatchListFromDB(travellerId);
		if (jsonMatchList == null) {
			return;
		}
		chatslist = jsonMatchList.getMatches();
		setMessageListView();
	}
	
	private void setMessageListView(){
		msgAdapter = new MessageListAdapter(getActivity(), chatslist,
				travellerId, UserRole.TRAVELLER);
		lvMessages.setAdapter(msgAdapter);
	}

	/**
	 * Network tasks for creating load bookings
	 */
	private class LoadMatchesTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				return matchesService.getTravellerMatchListFromServer(travellerId);
			} catch (Exception e) {
				Log.e(TAG, "Load traveller Matches fail");
				return null;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			LoadMatchHeadImagesTask loadMatchHeadImagesTask = new LoadMatchHeadImagesTask();
			loadMatchHeadImagesTask.execute();	
		}
	}

	private class LoadMatchHeadImagesTask extends
			AsyncTask<Void, Void, Map<String, byte[]>> {
		
		
		@Override
		protected Map<String, byte[]> doInBackground(Void... params) {
			try {
				return matchesService
						.getTravellerMatchHeadImagesFromServer(travellerId);
			} catch (Exception e) {
				Log.e(TAG, "Load traveller Match images from Server fail");
				return null;
			}
		}

		@Override
		protected void onPostExecute(Map<String, byte[]> result) {
			super.onPostExecute(result);
			if(result != null && result.size() > 0){
				loadMatchesFromDB();
				// BoroadCast New Match Images comes
				Intent intent = new Intent(
						BroadcastConstant.USER_MATCH_IMAGES_UPDATED_ACTION);
				activity.sendBroadcast(intent);		
			}
		}
	}

	/**
	 * click one of the matches and enter the conversation page with this
	 * partner
	 */
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		selectedMatchIndex = position;

		/*
		 * first go to its conversation activity
		 */
		Intent intent = new Intent(activity,
				TravellerConversationActivity.class);
		intent.putExtra(IMConstant.IM_CURRENT_CHATTING_PARTNER_ID, chatslist
				.get(position).getUserId());     
		intent.putExtra(IMConstant.IM_CURRENT_CHATTING_PARTNER_NAME, chatslist
				.get(position).getUserFirstname());
		intent.putExtra(IMConstant.IM_CURRENT_CHATTING_PARTNER_ROLE, chatslist
				.get(position).getUserRole().getNumVal());

		String chattingPartnerIMAccountname;
		if (chatslist.get(position).getUserRole() == UserRole.GUIDE) {
			chattingPartnerIMAccountname = "g_"
					+ chatslist.get(position).getUserId() + "@"
					+ IMConstant.SERVER_DOMAIN_NAME;
		} else {
			chattingPartnerIMAccountname = "t_"
					+ chatslist.get(position).getUserId() + "@"
					+ IMConstant.SERVER_DOMAIN_NAME;
		}

		intent.putExtra(IMConstant.IM_CURRENT_CHATTING_PARTNER_IM_ACCOUNT_NAME,
				chattingPartnerIMAccountname);

		startActivity(intent);

		/*
		 * then update the view of this item to normal state
		 */
		View v = lvMessages.getChildAt(position
				- lvMessages.getFirstVisiblePosition());

		int detaultColor = activity.getResources().getColor(R.color.gray);
		TextView tvTime = (TextView) v.findViewById(R.id.tv_message_list_time);
		tvTime.setTextColor(detaultColor);

		TextView tvMsg = (TextView) v.findViewById(R.id.tv_message_list_unread);
		tvMsg.setTypeface(null, Typeface.NORMAL);
		TextView tvUnreadNumber = (TextView) v
				.findViewById(R.id.tv_message_unread_number_tips);
		tvUnreadNumber.setVisibility(View.INVISIBLE);
	}

	/*
	 * pop up menus
	 */
	private void initPopupBottomMenu() {
		PopupActionItem stickItem = new PopupActionItem(
				ID_BOTTOM_MENU_STICKTOP, "Sticky on top");
		PopupActionItem deleteItem = new PopupActionItem(ID_BOTTOM_MENU_DELETE,
				"Delete match");

		bottomMenuAction = new BottomPopupMenuAction(activity);

		bottomMenuAction.addActionItem(stickItem);
		bottomMenuAction.addActionItem(deleteItem);

		bottomMenuAction
				.setOnActionItemClickListener(new BottomPopupMenuAction.OnActionItemClickListener() {

					@Override
					public void onItemClick(BottomPopupMenuAction source,
							int pos, int actionId) {
						switch (actionId) {
						case ID_BOTTOM_MENU_STICKTOP:
							// TODO
							break;
						case ID_BOTTOM_MENU_DELETE:
							// TODO
							break;
						}
					}
				});

		bottomMenuAction
				.setOnDismissListener(new BottomPopupMenuAction.OnDismissListener() {
					@Override
					public void onDismiss() {

					}
				});
	}
}
