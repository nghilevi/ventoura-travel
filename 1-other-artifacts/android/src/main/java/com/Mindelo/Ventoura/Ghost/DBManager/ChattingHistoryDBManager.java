package com.Mindelo.Ventoura.Ghost.DBManager;

import java.util.List;

import com.Mindelo.Ventoura.Constant.DBConstant;
import com.Mindelo.Ventoura.Entity.ChattingHistory;
import com.Mindelo.Ventoura.Ghost.DBHelper.DAO;
import com.Mindelo.Ventoura.Util.DateTimeUtil;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ChattingHistoryDBManager extends DAO<ChattingHistory> {

	private SQLiteDatabase db;

	public ChattingHistoryDBManager(SQLiteDatabase db) {
		super(db);
		this.db = db;
	}

	public ChattingHistory loadSingleEntityFromCursor(Cursor cursor) {
		ChattingHistory chattingHistory = new ChattingHistory();

		chattingHistory.setId(cursor.getLong(cursor
				.getColumnIndex(DBConstant.TABLE_CHATTING_HISTORY_FIELD_ID)));
		chattingHistory
				.setUserId(cursor.getLong(cursor
						.getColumnIndex(DBConstant.TABLE_CHATTING_HISTORY_FIELD_USER_ID)));
		chattingHistory
				.setPartnerId(cursor.getLong(cursor
						.getColumnIndex(DBConstant.TABLE_CHATTING_HISTORY_FIELD_PARTNER_ID)));
		chattingHistory
				.setUserRole(cursor.getInt(cursor
						.getColumnIndex(DBConstant.TABLE_CHATTING_HISTORY_FIELD_USER_ROLE)));
		chattingHistory
				.setPartnerRole(cursor.getInt(cursor
						.getColumnIndex(DBConstant.TABLE_CHATTING_HISTORY_FIELD_PARTNER_ROLE)));
		chattingHistory
				.setDateTime(DateTimeUtil.fromStringToDate(cursor.getString(cursor
						.getColumnIndex(DBConstant.TABLE_CHATTING_HISTORY_FIELD_DATETIME))));

		// boolean values
		chattingHistory
				.setRead(cursor.getInt(cursor
						.getColumnIndex(DBConstant.TABLE_CHATTING_HISTORY_FIELD_ISREAD)) > 0);
		chattingHistory
				.setMine(cursor.getInt(cursor
						.getColumnIndex(DBConstant.TABLE_CHATTING_HISTORY_FIELD_MINE)) > 0);
		chattingHistory
				.setStatusMessage(cursor.getInt(cursor
						.getColumnIndex(DBConstant.TABLE_CHATTING_HISTORY_FIELD_STATUS_MESSAGE)) > 0);

		chattingHistory
				.setMessageContent(cursor.getString(cursor
						.getColumnIndex(DBConstant.TABLE_CHATTING_HISTORY_FIELD_MESSAGE_CONTENT)));

		return chattingHistory;
	}

	/**
	 * add ChattingHistory
	 * 
	 * @param ChattingHistory
	 */
	public void save(ChattingHistory chattingHistory) {

		db.execSQL(
				"INSERT INTO " + DBConstant.TABLE_CHATTING_HISTORY
						+ " VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
				new Object[] {
						chattingHistory.getUserId(),
						chattingHistory.getPartnerId(),
						chattingHistory.getUserRole(),
						chattingHistory.getPartnerRole(),
						DateTimeUtil.fromDateToString(chattingHistory
								.getDateTime()), chattingHistory.isRead(),
						chattingHistory.isMine(),
						chattingHistory.isStatusMessage(),
						chattingHistory.getMessageContent() });

	}

	public void setChattingHistoryAsRead(long userId, int userRole,
			long chattingPartnerId, int chattingPartnerRole) {
		ContentValues contentArgs = new ContentValues();
		contentArgs.put(DBConstant.TABLE_CHATTING_HISTORY_FIELD_ISREAD, true);
		String condition = DBConstant.TABLE_CHATTING_HISTORY_FIELD_USER_ID
				+ "=" + userId + " AND "
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_PARTNER_ID + "="
				+ chattingPartnerId + " AND "
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_USER_ROLE + "="
				+ userRole + " AND "
				+ DBConstant.TABLE_CHATTING_HISTORY_FIELD_PARTNER_ROLE + "="
				+ chattingPartnerRole;
		db.update(DBConstant.TABLE_CHATTING_HISTORY, contentArgs, condition,
				null);
	}

	public List<ChattingHistory> findAll() {
		Cursor c = getQueryCursor(null, DBConstant.TABLE_CHATTING_HISTORY);
		List<ChattingHistory> chattingHistory = loadListEntityFromCursor(c);
		c.close();
		return chattingHistory;
	}

	public ChattingHistory findOne(String condition) {
		Cursor c = getQueryCursor(condition, DBConstant.TABLE_CHATTING_HISTORY);
		if (c.getCount() == 0)
			return null;
		c.moveToFirst();
		ChattingHistory chattingHistory = loadSingleEntityFromCursor(c);
		c.close();
		return chattingHistory;
	}

	public List<ChattingHistory> findMany(String condition) {
		Cursor c = getQueryCursor(condition, DBConstant.TABLE_CHATTING_HISTORY);
		List<ChattingHistory> chattingHistory = loadListEntityFromCursor(c);
		c.close();
		return chattingHistory;
	}

	@Override
	public void deleteMany(String condition) {
		delete(condition, DBConstant.TABLE_CHATTING_HISTORY);
	}

}
