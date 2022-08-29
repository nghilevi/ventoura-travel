package com.Mindelo.Ventoura.UI.Activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;

import org.jivesoftware.smack.XMPPConnection;

import com.Mindelo.Ventoura.AndroidService.IMListenerService;
import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Constant.IMConstant;
import com.Mindelo.Ventoura.Constant.VentouraConstant;
import com.Mindelo.Ventoura.Entity.ChattingHistory;
import com.Mindelo.Ventoura.Entity.ImageMatch;
import com.Mindelo.Ventoura.Enum.UserRole;
import com.Mindelo.Ventoura.Ghost.IService.IChattingHistoryService;
import com.Mindelo.Ventoura.Ghost.IService.IGuideService;
import com.Mindelo.Ventoura.Ghost.IService.IIMService;
import com.Mindelo.Ventoura.Ghost.IService.IMatchesService;
import com.Mindelo.Ventoura.Ghost.Service.ChattingHistoryService;
import com.Mindelo.Ventoura.Ghost.Service.GuideService;
import com.Mindelo.Ventoura.Ghost.Service.MatchesService;
import com.Mindelo.Ventoura.UI.Activity.R;
import com.Mindelo.Ventoura.UI.Adapter.MessageChattingAdapter;
import com.Mindelo.Ventoura.UI.View.PopupActionItem;
import com.Mindelo.Ventoura.UI.View.TopPopupMenuAction;
import com.Mindelo.Ventoura.Util.BitmapUtil;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class GuideConversationActivity extends ListActivity implements
		OnClickListener {
	private static final String TAG = "GuideConversationActivity";

	@Getter
	private static boolean active;
	/*
	 * Utility instances
	 */
	private long guideId;
	private long chattingPartnerId;
	private String chattingPartnerIMAccountname;
	private String chattingPartnerUsername;

	private String guideIMAccountname;
	private String guideUsername;

	private IIMService imService;
	private IChattingHistoryService chattingHistoryService;
	private IMatchesService matchesService;
	private IGuideService guideService;
	private XMPPConnection imConnection;
	private UserRole chattingPartnerRole;
	private SharedPreferences sharedPre;

	/*
	 * views
	 */
	private Button sendMessageButton, actionBtn;
	private EditText textMessage;
	private TopPopupMenuAction popupAction;

	private ListView lsView;
	private SwipeRefreshLayout swipeView;
	private MessageChattingAdapter adapter;

	/*
	 * global variables
	 */
	private ArrayList<ChattingHistory> messages = new ArrayList<ChattingHistory>();
	private static final int ID_PROFILE = 1;
	private static final int ID_DELETE = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_guide_conversation);

		sharedPre = getSharedPreferences(
				VentouraConstant.SHARED_PREFERENCE_VENTOURA,
				Context.MODE_PRIVATE);

		/*
		 * utility instances initialized
		 */
		imService = IMListenerService.getImService();
		imConnection = IMListenerService.getImConnection();
		chattingHistoryService = new ChattingHistoryService(this);
		matchesService = new MatchesService(this);
		guideService = new GuideService(this);

		/*
		 * get guide information from sharedpreferences
		 */
		guideId = sharedPre.getLong(VentouraConstant.PRE_USER_ID_IN_SERVER, -1);
		guideUsername = sharedPre.getString(
				VentouraConstant.PRE_USER_FIRST_NAME, "Noname");

		guideIMAccountname = "g_" + guideId + "@"
				+ IMConstant.SERVER_DOMAIN_NAME;

		/*
		 * get chatting partner information from the Intent
		 */
		Intent intent = getIntent();
		chattingPartnerId = intent.getLongExtra(
				IMConstant.IM_CURRENT_CHATTING_PARTNER_ID, -1);
		int roleValue = intent.getIntExtra(
				IMConstant.IM_CURRENT_CHATTING_PARTNER_ROLE, -1);
		if (roleValue == UserRole.GUIDE.getNumVal()) {
			chattingPartnerRole = UserRole.GUIDE;
		} else if (roleValue == UserRole.TRAVELLER.getNumVal()) {
			chattingPartnerRole = UserRole.TRAVELLER;
		}
		chattingPartnerUsername = intent
				.getStringExtra(IMConstant.IM_CURRENT_CHATTING_PARTNER_NAME);
		chattingPartnerIMAccountname = intent
				.getStringExtra(IMConstant.IM_CURRENT_CHATTING_PARTNER_IM_ACCOUNT_NAME);

		/*
		 * update the current partner accountname which is used in
		 * IMLisnerSrvervice
		 */
		Editor editor = sharedPre.edit();
		editor.putString(VentouraConstant.PRE_CHATTING_PARTNER_IM_ACCOUNT_NAME,
				chattingPartnerIMAccountname);
		editor.commit();

		/*
		 * initiate the views
		 */
		textMessage = (EditText) findViewById(R.id.conversation_edittext_message_content_field);

		sendMessageButton = (Button) findViewById(R.id.button_conversatioon_send_message);
		sendMessageButton.setOnClickListener(this);
		actionBtn = (Button) findViewById(R.id.btn_action);
		actionBtn.setText("More");
		
		/*
		 * set the initial messages list view
		 */
		Bitmap headImage = BitmapUtil.byteArrayToBitMap(guideService
				.getGuidePortalImageFromDB(guideId).getImageContent(),
				ConfigurationConstant.SMALL_USER_PORTAL_IMAGE_WIDTH,
				ConfigurationConstant.SMALL_USER_PORTAL_IMAGE_HEIGHT);

		ImageMatch partnerHeadImage = matchesService.getSingleMatchImageFromDB(
				chattingPartnerId, chattingPartnerRole.getNumVal());

		Bitmap partnerHeadImageBitmap = null;
		if (partnerHeadImage != null) {
			partnerHeadImageBitmap = BitmapUtil.byteArrayToBitMap(
					partnerHeadImage.getImageContent(),
					ConfigurationConstant.SMALL_USER_PORTAL_IMAGE_WIDTH,
					ConfigurationConstant.SMALL_USER_PORTAL_IMAGE_HEIGHT);
		}

		adapter = new MessageChattingAdapter(this, messages, headImage,
				partnerHeadImageBitmap);
		this.setListAdapter(adapter);
		lsView = this.getListView();

		/*
		 * set swipe refresh
		 */
		swipeView = (SwipeRefreshLayout) findViewById(R.id.traveller_conversation_swipt_container);
		swipeView
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					public void onRefresh() {
						new Handler().postDelayed(new Runnable() {
							public void run() {
								swipeView.setRefreshing(false);

								List<ChattingHistory> moreChattingHistories = chattingHistoryService
										.getChattingHistory(
												guideId,
												chattingPartnerId,
												UserRole.GUIDE.getNumVal(),
												chattingPartnerRole.getNumVal(),
												messages.get(0).getId(),
												ConfigurationConstant.NUMBER_OF_CHATTING_HISTORY_PER_LOAD);
								for (ChattingHistory chattingHistory : moreChattingHistories) {
									messages.add(0, chattingHistory);
								}
								adapter.notifyDataSetChanged();

								// set the new position
								if (moreChattingHistories.size() > 0) {
									lsView.setSelection(moreChattingHistories
											.size() - 1);
								} else {
									lsView.setSelection(0);
								}
							}
						}, 100);
					}
				});

		// sets the colors used in the refresh animation
		swipeView.setColorSchemeResources(R.color.ventoura_color,
				R.color.green, R.color.cyan, R.color.ventoura_color);

		lsView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				boolean enable = false;
				if (lsView != null && lsView.getChildCount() > 0) {
					// check if the first item of the list is visible
					boolean firstItemVisible = lsView.getFirstVisiblePosition() == 0;
					// check if the top of the first item is visible
					boolean topOfFirstItemVisible = lsView.getChildAt(0)
							.getTop() == 0;
					// enabling or disabling the refresh layout
					enable = firstItemVisible && topOfFirstItemVisible;
				}
				swipeView.setEnabled(enable);
			}
		});

		/*
		 * the option button
		 */
		initOptionPopupMenu();

		/*
		 * initialize history messages
		 */
		setHistoryMessages();

	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				addNewMessage((ChattingHistory) bundle
						.getSerializable(IMConstant.INCOMING_MESSAGE_NOTICE_MESSAGE_PAYLOAD));
				Log.i(TAG, "a message from IM's broad cast");
			}
		}
	};

	private void addNewMessage(ChattingHistory m) {
		if (m != null) {
			messages.add(m);
			adapter.notifyDataSetChanged();

			Log.i(TAG, "add new message to guide chatting screen ");
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		/*
		 * stop listen to the message broadcast from the IM Service
		 */
		unregisterReceiver(receiver);

		/*
		 * clean chatting this partner information
		 */
		Editor editor = sharedPre.edit();
		editor.putString(VentouraConstant.PRE_CHATTING_PARTNER_IM_ACCOUNT_NAME,
				"");
		editor.commit();
		Log.i(TAG, "shared preference about chatting partner is cleaned");
	}

	@Override
	public void onResume() {
		super.onResume();
		registerReceiver(receiver, new IntentFilter(
				IMConstant.INCOMING_MESSAGE_NOTICE_CONVERSATION));
	}

	@Override
	public void onStart() {
		super.onStart();
		active = true;
	}

	@Override
	public void onStop() {
		super.onStop();
		active = false;
	}

	private void setHistoryMessages() {
		messages.clear();
		/*
		 * get unread chatting messages of this traveller from the local DB
		 */
		List<ChattingHistory> unreadChattingHistories = chattingHistoryService
				.getChattingHistoryForInitialState(guideId, chattingPartnerId,
						UserRole.GUIDE.getNumVal(),
						chattingPartnerRole.getNumVal());
		/*
		 * the order is based on time, it is important
		 */
		for (int i = unreadChattingHistories.size() - 1; i >= 0; i--) {
			messages.add(unreadChattingHistories.get(i));
		}

		Log.i(TAG, "load guide chatting histories");
		adapter.notifyDataSetChanged();
	}

	/**
	 * when the send button is clicked, send the message out
	 */
	public void onClick(View v) {
		if (v.getId() == R.id.button_conversatioon_send_message) {
			String newMessageContent = textMessage.getText().toString();

			/*
			 * not allow empty message
			 */
			if (newMessageContent == null || newMessageContent.equals("")) {
				// TODO he is trying to send empory messages
				Toast.makeText(getApplicationContext(),
						"the message can not be null", Toast.LENGTH_SHORT)
						.show();
				return;
			}

			imService.sendMessage(imConnection, chattingPartnerIMAccountname,
					newMessageContent);

			Log.i(TAG, "Send message to " + chattingPartnerIMAccountname);

			// hide the keyboard and clean the input field
			textMessage.setText("");
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(textMessage.getWindowToken(), 0);

			// output the message which has been sent
			ChattingHistory chattingHistory = new ChattingHistory();
			chattingHistory.setUserId(guideId);
			chattingHistory.setUserRole(UserRole.GUIDE.getNumVal());
			chattingHistory.setPartnerId(chattingPartnerId);
			chattingHistory.setPartnerRole(chattingPartnerRole.getNumVal());

			chattingHistory.setDateTime(new Date());
			chattingHistory.setMine(true);
			chattingHistory.setRead(true);
			chattingHistory.setStatusMessage(false);
			chattingHistory.setMessageContent(newMessageContent);
			addNewMessage(chattingHistory);

			// save the new sent message into database
			chattingHistoryService.saveChattingHistory(chattingHistory);

		}
	}

	private void initOptionPopupMenu() {

		PopupActionItem profileItem = new PopupActionItem(ID_PROFILE, "Profile");
		PopupActionItem bookTourItem = new PopupActionItem(ID_DELETE,
				"Delete Contact");

		popupAction = new TopPopupMenuAction(this);

		popupAction.addActionItem(profileItem);
		popupAction.addActionItem(bookTourItem);

		popupAction
				.setOnActionItemClickListener(new TopPopupMenuAction.OnActionItemClickListener() {

					@Override
					public void onItemClick(TopPopupMenuAction source, int pos,
							int actionId) {
						if (actionId == ID_PROFILE) {
							Intent profileIntent = new Intent(
									GuideConversationActivity.this,
									TravellerDetailFriendActivity.class);
							profileIntent.putExtra("travellerId",
									chattingPartnerId);
							startActivity(profileIntent);
						} else if (actionId == ID_DELETE) {
							Toast.makeText(getApplicationContext(), "Delete",
									Toast.LENGTH_SHORT).show();
						}
					}
				});

		popupAction
				.setOnDismissListener(new TopPopupMenuAction.OnDismissListener() {
					@Override
					public void onDismiss() {

					}
				});
	}

	/*
	 * this method will be used when the option is a drop down menu
	 */
	public void btnActionOnClick(View view) {
		popupAction.show(view);
	}

	/*
	 * go back button
	 */
	public void btnBackOnClick(View view) {
		onBackPressed();
	}
}
