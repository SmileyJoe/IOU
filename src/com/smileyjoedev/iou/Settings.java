package com.smileyjoedev.iou;

import java.io.IOException;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.smileyjoedev.genLibrary.Debug;
import com.smileyjoedev.genLibrary.Notify;
import com.smileyjoedev.genLibrary.Send;
import com.smileyjoedev.iou.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.view.View;

public class Settings extends SherlockPreferenceActivity implements OnPreferenceClickListener {
	
	public static final int START_HOME_DASH = 1;
	public static final int START_INDIVIDUAL_VIEW_ALL = 2;
	public static final int START_GROUP_VIEW_ALL = 3;
	
	private SharedPreferences prefs;
//	private String[] themeTypes = {"Dark", "Light"};
//	private String[] themeTypesIds = {"1","2"};
//	private ListPreference prefTheme;
	private Preference contact;
	private Preference minimalisticTextDownload;
	private Preference allowCustomCurrSym;
	private Preference customCurrSym;
	private Preference dbBackup;
	private Preference dbRestore;
	private ListPreference prefStartPage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.settings);
		
		this.initialize();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		this.populateView();
	}
	
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
	        default:
	            return super.onOptionsItemSelected(item);
        }

    }
	
	public void initialize(){
		this.prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//		this.prefTheme = (ListPreference) findPreference("theme");
		
//		this.prefTheme.setEntries(this.themeTypes);
//		this.prefTheme.setEntryValues(this.themeTypesIds);
		
		String[] startPages = {
			this.getString(R.string.pref_start_home_dash_title), 
			this.getString(R.string.pref_start_individual_view_all_title),
			this.getString(R.string.pref_start_group_view_all_title)
		};
		
		String[] startPageIds = {
			Integer.toString(this.START_HOME_DASH),
			Integer.toString(this.START_INDIVIDUAL_VIEW_ALL),
			Integer.toString(this.START_GROUP_VIEW_ALL)
		};
		
		this.contact = (Preference) findPreference("contact");
		this.contact.setOnPreferenceClickListener(this);
		
		this.minimalisticTextDownload = (Preference) findPreference("minimalistic_text_download");
		this.minimalisticTextDownload.setOnPreferenceClickListener(this);
		
		this.prefStartPage = (ListPreference) findPreference("default_start_page");
		this.prefStartPage.setEntries(startPages);
		this.prefStartPage.setEntryValues(startPageIds);
		
		this.allowCustomCurrSym = (Preference) findPreference("allow_custom_currency_symbol");
		this.allowCustomCurrSym.setOnPreferenceClickListener(this);
		
		this.customCurrSym = (Preference) findPreference("custom_currency_symbol");
		this.customCurrSym.setEnabled(this.prefs.getBoolean("allow_custom_currency_symbol", false));
		
		this.dbBackup = (Preference) findPreference("db_backup");
		this.dbBackup.setOnPreferenceClickListener(this);
		
		this.dbRestore = (Preference) findPreference("db_restore");
		this.dbRestore.setOnPreferenceClickListener(this);
	}
	
	public void populateView(){
		
	}

	@Override
	public boolean onPreferenceClick(Preference pref) {
		if(pref.getKey().equals("contact")){
			Send.emailDialog(this, "smileyjoedev@gmail.com", "IOU - Feedback", "");
		}
		
		if(pref.getKey().equals("minimalistic_text_download")){
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse("market://details?id=de.devmil.minimaltext&hl=en"));
			startActivity(intent);
		}
		
		if(pref.getKey().equals("allow_custom_currency_symbol")){
			this.customCurrSym.setEnabled(!this.customCurrSym.isEnabled());
		}
		
		if(pref.getKey().equals("db_backup")){
			DbHelper helper = new DbHelper(this);
			boolean success = false;
			try {
				success = helper.exportDatabase();
			} catch (IOException e) {
				success = false;
			}
			
			if(success){
				Notify.toast(this, R.string.toast_db_backup_successful);
			} else {
				Notify.toast(this, R.string.toast_db_backup_failed);
			}
		}
		
		if(pref.getKey().equals("db_restore")){
			DbHelper helper = new DbHelper(this);
			boolean success = false;
			try {
				success = helper.importDatabase();
			} catch (IOException e) {
				success = false;
			}
			
			if(success){
				Notify.toast(this, R.string.toast_db_restore_successful);
			} else {
				Notify.toast(this, R.string.toast_db_restore_failed);
			}
		}
		
		return true;
	}
	
}
