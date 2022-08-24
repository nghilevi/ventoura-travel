package com.Mindelo.Ventoura.Ghost.Service;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Constant.DBConstant;
import com.Mindelo.Ventoura.Entity.ChattingHistory;
import com.Mindelo.Ventoura.Ghost.DBHelper.VentouraDBHelper;
import com.Mindelo.Ventoura.Ghost.DBManager.ChattingHistoryDBManager;
import com.Mindelo.Ventoura.Ghost.IService.IChattingHistoryService;

public class ChattingHistoryService implements IChattingHistoryService {

	private SQLiteDatabase db;
	private VentouraDBHelper ventouraDbHelper;
	private ChattingHistoryDBManager chattingHistoryDbManager;

	public ChattingHistoryService(Context context) {
		ventouraDbHelper = new VentouraDBHelper(context);
		db = ventouraDbHelper.getWritableDatabase();
		chattingHistoryDbManager = new ChattingHistoryDBManager(this.db);
	}

	public List<ChattingHistory> getChattingHistory(long userId,
			long partnerId, int userRole, int partnerRole, long earlierThanId,
			int limit) {
		String condition = "";

		condition += " where "
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_USER_ID + "="
				+ userId + " and "
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_PARTNER_ID + "="
				+ partnerId + " and "
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_USER_ROLE + "="
				+ userRole + " and "
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_PARTNER_ROLE + "="
				+ partnerRole + " and "
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_ID + "<"
				+ earlierThanId;

		condition += " order by dateTime desc limit " + limit;

		return chattingHistoryDbManager.findMany(condition);
	}

	public ChattingHistory getChattingHistoryByid(long id) {
		String condition = DBConstant.TABLE_CHATTING_HISTORY_FIELD_ID + "="
				+ id;
		return chattingHistoryDbManager.findOne(condition);
	}

	public void saveChattingHistory(ChattingHistory chattingHistory) {
		chattingHistoryDbManager.save(chattingHistory);
	}

	public List<ChattingHistory> getAllUnreadChattingHistory(long userId,
			long partnerId, int userRole, int partnerRole) {
		String condition = "";

		condition += " where "
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_USER_ID + "="
				+ userId + " and "
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_PARTNER_ID + "="
				+ partnerId + " and "
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_USER_ROLE + "="
				+ userRole + " and "
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_PARTNER_ROLE + "="
				+ partnerRole + " and "
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_ISREAD + "=0";

		condition += " order by dateTime desc";

		return chattingHistoryDbManager.findMany(condition);
	}

	public void setChattingHistoryAsRead(List<ChattingHistory> chattingHistories) {
		for (ChattingHistory chattingHistory : chattingHistories) {
			chattingHistoryDbManager.setChattingHistoryAsRead(
					chattingHistory.getUserId(), chattingHistory.getUserRole(),
					chattingHistory.getPartnerId(),
					chattingHistory.getPartnerRole());
		}

	}

	// @Override
	public int getNumberOfChatterWithUnreadMessage(long userId, int userRole) {
		String condition = "SELECT count(*) from "
				+ DBConstant.TABLE_CHATTING_HISTORY;

		condition += " where "
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_USER_ID + "="
				+ userId + " and "
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_USER_ROLE + "="
				+ userRole + " and "
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_ISREAD + "=0";

		condition += " group by "
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_PARTNER_ID;

		Cursor cursor = db.rawQuery(condition, null);
		if (cursor.getCount() == 0)
			return 0;
		cursor.moveToFirst();

		return cursor.getInt(0);
	}

	@Override
	public ChattingHistory getLastMessageWithASpecificChatter(long userId,
			long partnerId, int userRole, int partnerRole) {

		String condition = "";

		condition += " where "
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_USER_ID + "="
				+ userId + " and "
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_PARTNER_ID + "="
				+ partnerId + " and "
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_USER_ROLE + "="
				+ userRole + " and "
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_PARTNER_ROLE + "="
				+ partnerRole;

		condition += " order by dateTime desc limit 1 ";

		ChattingHistory chattingHistory = chattingHistoryDbManager
				.findOne(condition);
		if (chattingHistory != null) {
			return chattingHistory;
		} else {
			return null;
		}
	}

	@Override
	public int getNumberOfUnreadChattingHistoryParticularPartner(long userId,
			long partnerId, int userRole, int partnerRole) {
		String condition = "SELECT count(*) from "
				+ DBConstant.TABLE_CHATTING_HISTORY;

		condition += " where "
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_USER_ID + "="
				+ userId + " and "
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_PARTNER_ID + "="
				+ partnerId + " and "
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_USER_ROLE + "="
				+ userRole + " and "
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_PARTNER_ROLE + "="
				+ partnerRole + " and "
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_ISREAD + "=0";

		Cursor cursor = db.rawQuery(condition, null);
		if (cursor.getCount() == 0)
			return 0;
		cursor.moveToFirst();

		return cursor.getInt(0);
	}

	@Override
	public List<ChattingHistory> getChattingHistoryForInitialState(long userId,
			long partnerId, int userRole, int partnerRole) {

		List<ChattingHistory> chattingHistories = getAllUnreadChattingHistory(
				userId, partnerId, userRole, partnerRole);

		// the unread messages will be load, they can be set as read
		setChattingHistoryAsRead(chattingHistories);

		long minnimumUnreadMessageId = Long.MAX_VALUE;
		if (chattingHistories.size() > 0) {
			minnimumUnreadMessageId = chattingHistories.get(
					chattingHistories.size() - 1).getId();
		}

		if (chattingHistories.size() < ConfigurationConstant.NUMBER_OF_CHATTING_HISTORY_PER_LOAD) {
			chattingHistories.addAll(getChattingHistory(userId, partnerId,
					userRole, partnerRole, minnimumUnreadMessageId,
					ConfigurationConstant.NUMBER_OF_CHATTING_HISTORY_PER_LOAD
							- chattingHistories.size()));
		}

		return chattingHistories;
	}

}
