package com.smileyjoedev.iou;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.smileyjoedev.genLibrary.Debug;
import com.smileyjoedev.genLibrary.Files;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
	public DbHelper(Context context) {
		super(context, Constants.DB_NAME, null, Constants.DB_VERSION);
	}
	
	// Method is called during creation of the database
	@Override
	public void onCreate(SQLiteDatabase database) {
		Db.onCreate(database);
	}
	
	// Method is called during an upgrade of the database,
	// e.g. if you increase the database version
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Db.onUpgrade(database, oldVersion, newVersion);
	}
	
	public boolean importDatabase() throws IOException {
	    close();
	    File newDb = new File(Constants.DB_BACKUP_FILEPATH);
	    File oldDb = new File(Constants.DB_FILEPATH);
	    if (newDb.exists()) {
	        Files.copyFile(new FileInputStream(newDb), new FileOutputStream(oldDb));
	        getWritableDatabase().close();
	        return true;
	    }
	    return false;
	}
	
	public boolean exportDatabase() throws IOException {
		File appPath = new File(Constants.APP_FILEPATH);
	    File newDb = new File(Constants.DB_FILEPATH);
	    File oldDb = new File(Constants.DB_BACKUP_FILEPATH);
	    appPath.mkdirs();
	    if (newDb.exists()) {
	        Files.copyFile(new FileInputStream(newDb), new FileOutputStream(oldDb));
	        return true;
	    }
	    return false;
	}
}
