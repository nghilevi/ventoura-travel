package com.Mindelo.Ventoura.Ghost.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;

import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Entity.City;
import com.Mindelo.Ventoura.Ghost.DBManager.CityDBManager;
import com.Mindelo.Ventoura.Ghost.IService.ICityService;

public class CityService implements ICityService {

	private SQLiteDatabase db;
	private CityDBManager cityDbManager;

	public CityService(Activity activity) {

		File dbFileFolder = new File(ConfigurationConstant.VENTOURA_DB_PATH);
		File dbFile = new File(ConfigurationConstant.VENTOURA_DB_PATH + "/"
				+ ConfigurationConstant.VENTOURA_ASSET_DB);

		if (!dbFileFolder.exists()) {
			dbFileFolder.mkdirs();
		}
		if (!dbFile.exists()) {
			try {
				dbFile.createNewFile();
				InputStream in_db = activity.getAssets().open(
						ConfigurationConstant.VENTOURA_ASSET_DB);
				OutputStream out_db = new FileOutputStream(dbFile);
				IOUtils.copy(in_db, out_db);
				out_db.close();
				in_db.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (dbFile.exists()) {
			this.db = SQLiteDatabase.openDatabase(
					ConfigurationConstant.VENTOURA_DB_PATH + "/"
							+ ConfigurationConstant.VENTOURA_ASSET_DB, null,
					SQLiteDatabase.OPEN_READONLY);
			cityDbManager = new CityDBManager(this.db);
		}

	}

	public List<City> getSuggestCity(String pattern) {

		String condition = " where ";

		condition += " cityName like \'%" + pattern + "%\'";

		condition += " limit "
				+ ConfigurationConstant.AUTO_COMPLETE_CITY_SUGGRSTION_NUMBER;

		return cityDbManager.findMany(condition);
	}

	public City getCityByName(String cityName) {

		String condition = " where ";

		condition += " cityName='" + cityName +"'";

		return cityDbManager.findOne(condition);
	}

	@Override
	public City getCityById(int id) {
		String condition = " where ";

		condition += " id=" + id;
		return cityDbManager.findOne(condition);
	}

	@Override
	public List<City> getAllCityAlphabetically() {
		
		String condition = " order by cityName ";

		return cityDbManager.findMany(condition);
	}

}
