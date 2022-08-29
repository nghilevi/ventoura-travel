package com.Mindelo.Ventoura.Ghost.DBManager;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.Mindelo.Ventoura.Constant.DBConstant;
import com.Mindelo.Ventoura.Entity.ImageProfile;
import com.Mindelo.Ventoura.Ghost.DBHelper.DAO;

public class GuideGalleryImageDBManager extends DAO<ImageProfile> {

	private SQLiteDatabase db;

	public GuideGalleryImageDBManager(SQLiteDatabase db) {
		super(db);
		this.db = db;
	}
	
	public void setImageAsPortal(long guideId, long imageId){
		
		ContentValues args1 = new ContentValues();
		args1.put(DBConstant.TABLE_GUIDE_PROFILE_GALLERY_FIELD_IS_PORTAL,
				false);
		String condition1 = DBConstant.TABLE_GUIDE_PROFILE_GALLERY_FIELD_GUIDE_ID
				+ "=" + guideId;
		this.db.update(DBConstant.TABLE_GUIDE_PROFILE_GALLERY, args1, condition1, null);
		
		
		ContentValues args2 = new ContentValues();
		args2.put(DBConstant.TABLE_GUIDE_PROFILE_GALLERY_FIELD_IS_PORTAL,
				true);
		String condition2 = DBConstant.TABLE_GUIDE_PROFILE_GALLERY_FIELD_ID
				+ "=" + imageId;
		this.db.update(DBConstant.TABLE_GUIDE_PROFILE_GALLERY, args2, condition2, null);
	}

	@Override
	public void save(ImageProfile entity) {
		db.execSQL(
				"INSERT INTO " + DBConstant.TABLE_GUIDE_PROFILE_GALLERY
						+ " VALUES(?, ?, ?, ?)",
				new Object[] { entity.getId(), entity.getUserId(),
						entity.isPortal() == false ? 0 : 1,
						entity.getImageContent() });
	}

	@Override
	public List<ImageProfile> findAll() {
		Cursor c = getQueryCursor(null,
				DBConstant.TABLE_GUIDE_PROFILE_GALLERY);
		List<ImageProfile> images = loadListEntityFromCursor(c);
		c.close();
		return images;
	}

	@Override
	public ImageProfile findOne(String condition) {
		Cursor c = getQueryCursor(condition,
				DBConstant.TABLE_GUIDE_PROFILE_GALLERY);
		if (c.getCount() == 0)
			return null;
		c.moveToFirst();
		ImageProfile image = loadSingleEntityFromCursor(c);
		c.close();
		return image;
	}

	@Override
	public List<ImageProfile> findMany(String condition) {
		Cursor c = getQueryCursor(condition,
				DBConstant.TABLE_GUIDE_PROFILE_GALLERY);
		List<ImageProfile> images = loadListEntityFromCursor(c);
		c.close();
		return images;
	}

	@Override
	public ImageProfile loadSingleEntityFromCursor(Cursor cursor) {
		
		ImageProfile imageProfile = new ImageProfile();

		imageProfile
				.setId(cursor.getLong(cursor
						.getColumnIndex(DBConstant.TABLE_GUIDE_PROFILE_GALLERY_FIELD_ID)));
		imageProfile
				.setUserId(cursor.getLong(cursor
						.getColumnIndex(DBConstant.TABLE_GUIDE_PROFILE_GALLERY_FIELD_GUIDE_ID)));
		if (cursor
				.getInt(cursor
						.getColumnIndex(DBConstant.TABLE_GUIDE_PROFILE_GALLERY_FIELD_IS_PORTAL)) == 0) {
			imageProfile.setPortal(false);
		} else {
			imageProfile.setPortal(true);
		}
		imageProfile
				.setImageContent(cursor.getBlob(cursor
						.getColumnIndex(DBConstant.TABLE_GUIDE_PROFILE_GALLERY_FIELD_CONTENT)));

		return imageProfile;
	}

	@Override
	public void deleteMany(String condition) {
		delete(condition, DBConstant.TABLE_GUIDE_PROFILE_GALLERY);
	}

}
