package com.Mindelo.Ventoura.Ghost.DBManager;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.Mindelo.Ventoura.Constant.DBConstant;
import com.Mindelo.Ventoura.Entity.ImageMatch;
import com.Mindelo.Ventoura.Enum.UserRole;
import com.Mindelo.Ventoura.Ghost.DBHelper.DAO;

public class MatchImageDBManager extends DAO<ImageMatch> {

	private SQLiteDatabase db;

	public MatchImageDBManager(SQLiteDatabase db) {
		super(db);
		this.db = db;
	}

	@Override
	public void save(ImageMatch entity) {
		db.execSQL(
				"INSERT INTO " + DBConstant.TABLE_MATCH_HEAD_IMAGE
						+ " VALUES(?, ?, ?, ?)",
				new Object[] { entity.getId(), entity.getUserId(),
						entity.getUserRole().getNumVal(),
						entity.getImageContent() });
	}

	@Override
	public List<ImageMatch> findAll() {
		Cursor c = getQueryCursor(null, DBConstant.TABLE_MATCH_HEAD_IMAGE);
		List<ImageMatch> images = loadListEntityFromCursor(c);
		c.close();
		return images;
	}

	@Override
	public ImageMatch findOne(String condition) {
		Cursor c = getQueryCursor(condition, DBConstant.TABLE_MATCH_HEAD_IMAGE);
		if (c.getCount() == 0)
			return null;
		c.moveToFirst();
		ImageMatch image = loadSingleEntityFromCursor(c);
		c.close();
		return image;
	}

	@Override
	public List<ImageMatch> findMany(String condition) {
		Cursor c = getQueryCursor(condition, DBConstant.TABLE_MATCH_HEAD_IMAGE);
		List<ImageMatch> images = loadListEntityFromCursor(c);
		c.close();
		return images;
	}

	@Override
	public ImageMatch loadSingleEntityFromCursor(Cursor cursor) {

		ImageMatch imageMatch = new ImageMatch();

		imageMatch.setId(cursor.getLong(cursor
				.getColumnIndex(DBConstant.TABLE_MATCH_HEAD_IMAGE_FIELD_ID)));
		imageMatch
				.setUserId(cursor.getLong(cursor
						.getColumnIndex(DBConstant.TABLE_MATCH_HEAD_IMAGE_FIELD_USER_ID)));

		UserRole userRole = UserRole.values()[cursor
				.getInt(cursor
						.getColumnIndex(DBConstant.TABLE_MATCH_HEAD_IMAGE_FIELD_USER_ROLE))];

		imageMatch.setUserRole(userRole);

		imageMatch
				.setImageContent(cursor.getBlob(cursor
						.getColumnIndex(DBConstant.TABLE_MATCH_HEAD_IMAGE_FIELD_CONTENT)));

		return imageMatch;
	}

	@Override
	public void deleteMany(String condition) {
		delete(condition, DBConstant.TABLE_MATCH_HEAD_IMAGE);
	}

	public void update(ImageMatch headImage) {

		ContentValues args = new ContentValues();
		args.put(DBConstant.TABLE_MATCH_HEAD_IMAGE_FIELD_CONTENT,
				headImage.getImageContent());
		args.put(DBConstant.TABLE_MATCH_HEAD_IMAGE_FIELD_USER_ID,
				headImage.getId());
		String condition = DBConstant.TABLE_MATCH_HEAD_IMAGE_FIELD_USER_ID
				+ "=" + headImage.getUserId() + " AND "
				+ DBConstant.TABLE_MATCH_HEAD_IMAGE_FIELD_USER_ROLE + "="
				+ headImage.getUserRole().getNumVal();

		this.db.update(DBConstant.TABLE_MATCH_HEAD_IMAGE, args, condition, null);
	}

}
