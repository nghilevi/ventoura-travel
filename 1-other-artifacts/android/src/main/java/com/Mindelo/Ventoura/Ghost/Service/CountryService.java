package com.Mindelo.Ventoura.Ghost.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;

import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Entity.Country;
import com.Mindelo.Ventoura.Ghost.DBManager.CountryDBManager;
import com.Mindelo.Ventoura.Ghost.IService.ICountryService;

public class CountryService implements ICountryService {

	private SQLiteDatabase db;
	private CountryDBManager countryDbManager;

	public CountryService(Activity activity) {

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
			countryDbManager = new CountryDBManager(this.db);
		}

	}

	public List<Country> getSuggestCountry(String pattern) {

		String condition = " where ";

		condition += " countryName like \'%" + pattern + "%\'";

		condition += " limit "
				+ ConfigurationConstant.AUTO_COMPLETE_COUNTRY_SUGGRSTION_NUMBER;

		return countryDbManager.findMany(condition);
	}

	public Country getCountryByName(String countryName) {

		String condition = " where ";

		condition += " countryName='" + countryName +"'";

		return countryDbManager.findOne(condition);
	}

	@Override
	public Country getCountryById(int id) {
		String condition = " where ";

		condition += " id=" + id;
		return countryDbManager.findOne(condition);
	}

	@Override
	public List<Country> getAllCountryAlphabetically() {
		
		String condition = " order by countryName ";
		
		return countryDbManager.findMany(condition);
	}

	@Override
	public Country getCountryByISO2(String iso2) {
		String condition = " where ";

		condition += " iso=" + iso2;
		return countryDbManager.findOne(condition);
	}

}
