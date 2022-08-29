package com.Mindelo.Ventoura.AndroidService;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import com.Mindelo.Ventoura.Constant.PushConstant;
import com.Mindelo.Ventoura.Enum.UserRole;
import com.Mindelo.Ventoura.UI.Activity.GuidePortalActivity;
import com.Mindelo.Ventoura.UI.Activity.R;
import com.Mindelo.Ventoura.UI.Activity.TravellerPortalActivity;

/**
 * this is a helper class to the IMListenerService to handle the push
 * information from the ventoura server
 */
public class ServerPushService {
 
	public static void handlePushMessage(Context context, String message, int userRole) {
		String[] infors = message.split("#");
		if (infors.length >= 2) {
			switch (infors[0]) {
			case PushConstant.PUSH_IS_MATCH_WITH_TRAVELLER:
				createNotificationBar(context, "SET THE NAME", " you got a new match", userRole);
				break;
			case PushConstant.PUSH_IS_MATCH_WITH_GUIDE:
				createNotificationBar(context, "SET THE NAME", "you got a new interests", userRole);
				break;
			case PushConstant.PUSH_TRAVELLER_CREATE_BOOKING:
				createNotificationBar(context, "SET THE NAME", "create a booking", userRole);
				break;
			case PushConstant.PUSH_TRAVELLER_PAID_BOOKING:
				createNotificationBar(context, "SET THE NAME", "paid a booking", userRole);
				break;
			case PushConstant.PUSH_GUIDE_ACCEPT_BOOKING:
				createNotificationBar(context, "SET THE NAME", "accepted your booking", userRole);
				break;
			case PushConstant.PUSH_GUIDE_REFUSE_BOOKING:
				createNotificationBar(context, "SET THE NAME", "refused your booking", userRole);
				break;
			}
		}

	}
	
	
	private static void createNotificationBar(Context context, String title, String content,
			int userRole) {
		Intent intent;
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(R.drawable.ic_launcher).setAutoCancel(true)
				.setContentTitle(title).setContentText(content);

		if (userRole == UserRole.TRAVELLER.getNumVal()) {
			intent = new Intent(context, TravellerPortalActivity.class);
			stackBuilder.addParentStack(TravellerPortalActivity.class);
		} else {
			intent = new Intent(context, GuidePortalActivity.class);
			stackBuilder.addParentStack(GuidePortalActivity.class);
		}

		stackBuilder.addNextIntent(intent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);

		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		mNotificationManager.notify(0, mBuilder.build());

	}

}
