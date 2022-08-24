package com.Mindelo.Ventoura.AndroidService;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;

import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Constant.IMConstant;
import com.Mindelo.Ventoura.Constant.VentouraConstant;
import com.Mindelo.Ventoura.Entity.ChattingHistory;
import com.Mindelo.Ventoura.Entity.ImageMatch;
import com.Mindelo.Ventoura.Enum.UserRole;
import com.Mindelo.Ventoura.Ghost.IService.IChattingHistoryService;
import com.Mindelo.Ventoura.Ghost.IService.IIMService;
import com.Mindelo.Ventoura.Ghost.IService.IMatchesService;
import com.Mindelo.Ventoura.Ghost.Service.ChattingHistoryService;
import com.Mindelo.Ventoura.Ghost.Service.IMService;
import com.Mindelo.Ventoura.Ghost.Service.MatchesService;
import com.Mindelo.Ventoura.JSONEntity.JSONMatch;
import com.Mindelo.Ventoura.UI.Activity.GuideConversationActivity;
import com.Mindelo.Ventoura.UI.Activity.R;
import com.Mindelo.Ventoura.UI.Activity.TravellerConversationActivity;
import com.Mindelo.Ventoura.UI.Fragment.GuideMessageFragment;
import com.Mindelo.Ventoura.UI.Fragment.TravellerMessageFragment;
import com.Mindelo.Ventoura.Util.BitmapUtil;
import com.Mindelo.Ventoura.Util.ImageUtil;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

public class IMListenerService extends Service {

	private static final String TAG = "IMListenerService";

	@Getter
	@Setter
	private static boolean imserviceStartPortalActivity;

	/*
	 * utility instances
	 */
	@Getter
	public static XMPPConnection imConnection;
	@Getter
	private static IIMService imService;

	private IMatchesService matchesService;
	@Getter
	private IChattingHistoryService chattingHistoryService;

	/*
	 * global variables
	 */
	private MediaPlayer player;
	private SharedPreferences preferences;

	String messageContent;

	private long userId;
	private int userRole;
	private JSONMatch jsonMatch = null;
	private long partnerId;
	private String partnerAccountname;
	private UserRole chattingPartnerRole;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);

		final String userName = intent.getStringExtra(IMConstant.USER_NAME);
		final String userPassword = intent
				.getStringExtra(IMConstant.USER_PASSWORD);

		new Thread(new Runnable() {
			public void run() {
				try {
					imConnection = imService.getConnection(
							IMConstant.URL_IM_SERVER,
							IMConstant.IM_SERVER_PORT, IMConstant.SERVICE);
					imConnection.login(userName, userPassword);
					Log.i(TAG, "Login Success");
					setPacketListener();

				} catch (Exception e) {
					e.printStackTrace();
					Log.i(TAG, "Login Fail");
				}
			}
		}).start();

		return START_REDELIVER_INTENT;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		preferences = getSharedPreferences(
				VentouraConstant.SHARED_PREFERENCE_VENTOURA,
				Context.MODE_PRIVATE);

		chattingHistoryService = new ChattingHistoryService(this);
		imService = new IMService();
		matchesService = new MatchesService(this);

		userId = preferences
				.getLong(VentouraConstant.PRE_USER_ID_IN_SERVER, -1);
		userRole = preferences.getInt(VentouraConstant.PRE_USER_ROLE, -1);

	}

	private void setPacketListener() {
		if (imConnection != null) {
			// Add a packet listener to get messages sent to us
			PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
			imConnection.addPacketListener(new PacketListener() {
				public void processPacket(Packet packet) {
					final Message message = (Message) packet;
					actionsAfterReceiveMessage(message);
				}
			}, filter);
		}
	}

	private void actionsAfterReceiveMessage(final Message message) {

		messageContent = message.getBody();

		if (messageContent != null && messageContent != "") {

			notificationDeviceSound();

			/*
			 * retrive information from the message
			 */
			partnerAccountname = StringUtils
					.parseBareAddress(message.getFrom());
			String[] fromInfos = partnerAccountname.split("@");

			if (fromInfos[0]
					.equalsIgnoreCase(IMConstant.VENTOURA_SERVER_IM_ACCOUNT)) {
				/*
				 * the message is sent from the server
				 */
				ServerPushService.handlePushMessage(this, messageContent,
						userRole);
				return;
			}

			/*
			 * get partner information
			 */

			partnerId = Long.valueOf(fromInfos[0].substring(2,
					fromInfos[0].length()));
			if (fromInfos[0].startsWith("g")) {
				chattingPartnerRole = UserRole.GUIDE;
				jsonMatch = matchesService.getSingleMatchFromDB(userId,
						userRole, partnerId, UserRole.GUIDE.getNumVal());

			} else {
				chattingPartnerRole = UserRole.TRAVELLER;
				jsonMatch = matchesService.getSingleMatchFromDB(userId,
						userRole, partnerId, UserRole.TRAVELLER.getNumVal());
			}

			/*
			 * create a chattingHistory Item
			 */
			ChattingHistory chattingHistory = setChattingHistory();
			// save message into chatting history
			chattingHistoryService.saveChattingHistory(chattingHistory);

			if (partnerAccountname.equalsIgnoreCase(preferences.getString(
					VentouraConstant.PRE_CHATTING_PARTNER_IM_ACCOUNT_NAME, ""))) {

				chattingHistory.setRead(true);

				/*
				 * broadcast that received a message when the user is chating
				 * with the sender, the user's message view will be updated
				 */
				Intent intent = new Intent(
						IMConstant.INCOMING_MESSAGE_NOTICE_CONVERSATION);
				intent.putExtra(
						IMConstant.INCOMING_MESSAGE_NOTICE_MESSAGE_PAYLOAD,
						chattingHistory);

				sendBroadcast(intent);
				Log.i("XMPPChatDemoActivity", "Text Recieved " + messageContent
						+ "from" + partnerAccountname);
			} else if (TravellerMessageFragment.isActive == true
					|| GuideMessageFragment.isActive == true) {
				/*
				 * if the user is in the message page
				 */
				Intent intent = new Intent(
						IMConstant.INCOMING_MESSAGE_NOTICE_MESSAGE_LIST);
				intent.putExtra(
						IMConstant.INCOMING_MESSAGE_NOTICE_MESSAGE_PAYLOAD,
						chattingHistory);

				sendBroadcast(intent);

			} else {
				/*
				 * if the user is not chatting with the sender and not in the
				 * message page
				 */
				chattingHistory.setRead(false);
				if (jsonMatch != null) {
					createNotificationBar(jsonMatch.getUserFirstname()
							+ " sent you a message", messageContent);
				}

			}
		}

	}

	private ChattingHistory setChattingHistory() {
		ChattingHistory chattingHistory = new ChattingHistory();

		chattingHistory.setUserId(userId);
		chattingHistory.setUserRole(userRole);
		chattingHistory.setPartnerId(partnerId);
		chattingHistory.setPartnerRole(chattingPartnerRole.getNumVal());
		chattingHistory.setDateTime(new Date());
		chattingHistory.setMine(false);
		chattingHistory.setStatusMessage(false);
		chattingHistory.setMessageContent(messageContent);
		return chattingHistory;
	}

	private void createNotificationBar(String title, String content) {

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

		ImageMatch headImage = matchesService.getSingleMatchImageFromDB(
				jsonMatch.getUserId(), jsonMatch.getUserRole().getNumVal());
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				this);
		if (headImage != null) {
			mBuilder.setLargeIcon(BitmapUtil.byteArrayToBitMap(
					headImage.getImageContent(),
					ConfigurationConstant.SMALL_USER_PORTAL_IMAGE_WIDTH,
					ConfigurationConstant.SMALL_USER_PORTAL_IMAGE_HEIGHT));
		}

		mBuilder.setSmallIcon(R.drawable.ic_stat_icon).setAutoCancel(true);
		mBuilder.setContentTitle(title).setContentText(content);

		/*
		 * load intent data
		 */
		Intent intent;
		if (userRole == UserRole.TRAVELLER.getNumVal()) {
			intent = new Intent(this, TravellerConversationActivity.class);
			stackBuilder.addParentStack(TravellerConversationActivity.class);
		} else {
			intent = new Intent(this, GuideConversationActivity.class);
			stackBuilder.addParentStack(GuideConversationActivity.class);
		}

		intent.putExtra(IMConstant.IM_CURRENT_CHATTING_PARTNER_ID, partnerId);
		intent.putExtra(IMConstant.IM_CURRENT_CHATTING_PARTNER_IM_ACCOUNT_NAME,
				partnerAccountname);
		if (jsonMatch != null) {
			intent.putExtra(IMConstant.IM_CURRENT_CHATTING_PARTNER_NAME,
					jsonMatch.getUserFirstname());
		}
		intent.putExtra(IMConstant.IM_CURRENT_CHATTING_PARTNER_ROLE,
				chattingPartnerRole.getNumVal());

		/*
		 * put intent into builder task
		 */
		stackBuilder.addNextIntent(intent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);

		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		// mId allows you to update the notification later on.
		imserviceStartPortalActivity = true;
		mNotificationManager.notify((int) partnerId, mBuilder.build());

		Log.i(TAG, "Incoming message notification");
	}

	private void notificationDeviceSound() {
		/* notice the user that he received a new message */
		AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		switch (audio.getRingerMode()) {
		case AudioManager.RINGER_MODE_NORMAL: // Use the default rintone of
												// the device
			player = MediaPlayer.create(this, R.raw.ventoura_incoming_message);
			player.start();
			break;
		case AudioManager.RINGER_MODE_VIBRATE:
			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			// Vibrate for 250 milliseconds
			v.vibrate(250);
			break;
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onDestroy() {
		// Toast.makeText(this, "IM service done", Toast.LENGTH_SHORT).show();
	}
}
