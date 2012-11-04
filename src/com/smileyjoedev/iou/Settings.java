package com.smileyjoedev.iou;

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

public class Settings extends PreferenceActivity implements OnPreferenceClickListener {
	
	private SharedPreferences prefs;
//	private String[] themeTypes = {"Dark", "Light"};
//	private String[] themeTypesIds = {"1","2"};
//	private ListPreference prefTheme;
	private Preference contact;
	private Preference minimalisticTextDownload;
	private Preference defaultEmailRemindBody;
	private Preference defaultEmailRemindSubject;
	private Preference emailReminders;
	private Preference smsReminders;
	private Preference defaultSmsRemindBody;
	private String[] startPages = {"Individual", "Group"};
	private String[] startPageIds = {"1","2"};
	private ListPreference prefStartPage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.settings);
		
		this.initialize();
		this.populate_view();
	}
	
	public void initialize(){
		this.prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//		this.prefTheme = (ListPreference) findPreference("theme");
		
//		this.prefTheme.setEntries(this.themeTypes);
//		this.prefTheme.setEntryValues(this.themeTypesIds);
		
		this.contact = (Preference) findPreference("contact");
		this.contact.setOnPreferenceClickListener(this);
		
		this.minimalisticTextDownload = (Preference) findPreference("minimalistic_text_download");
		this.minimalisticTextDownload.setOnPreferenceClickListener(this);
		
		this.emailReminders = (Preference) findPreference("allow_email_reminders");
		this.emailReminders.setOnPreferenceClickListener(this);
		this.defaultEmailRemindBody = (Preference) findPreference("default_email_reminder_body");
		this.defaultEmailRemindBody.setEnabled(this.prefs.getBoolean("allow_email_reminders", true));
		this.defaultEmailRemindSubject = (Preference) findPreference("default_email_reminder_subject");
		this.defaultEmailRemindSubject.setEnabled(this.prefs.getBoolean("allow_email_reminders", true));
		
		this.smsReminders = (Preference) findPreference("allow_sms_reminders");
		this.smsReminders.setOnPreferenceClickListener(this);
		this.defaultSmsRemindBody = (Preference) findPreference("default_sms_reminder_body");
		this.defaultSmsRemindBody.setEnabled(this.prefs.getBoolean("allow_sms_reminders", true));
		
		this.prefStartPage = (ListPreference) findPreference("default_start_page");
		this.prefStartPage.setEntries(this.startPages);
		this.prefStartPage.setEntryValues(this.startPageIds);
	}
	
	public void populate_view(){
		
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
		
		if(pref.getKey().equals("allow_email_reminders")){
			this.defaultEmailRemindBody.setEnabled(!this.defaultEmailRemindBody.isEnabled());
			this.defaultEmailRemindSubject.setEnabled(!this.defaultEmailRemindSubject.isEnabled());
		}
		
		if(pref.getKey().equals("allow_sms_reminders")){
			this.defaultSmsRemindBody.setEnabled(!this.defaultSmsRemindBody.isEnabled());
		}
		return true;
	}
	
}
