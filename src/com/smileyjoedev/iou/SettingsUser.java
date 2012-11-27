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

public class SettingsUser extends SherlockPreferenceActivity implements OnPreferenceClickListener {
	
	private SharedPreferences prefs;
	private Preference defaultEmailRemindBody;
	private Preference defaultEmailRemindSubject;
	private Preference emailReminders;
	private Preference smsReminders;
	private Preference defaultSmsRemindBody;
	private Preference notificationReminders;
	private Preference notificationReminderPersistent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.settings_user);
		
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
		
		this.notificationReminders = (Preference) findPreference("allow_notification_reminders");
		this.notificationReminders.setOnPreferenceClickListener(this);
		
		this.notificationReminderPersistent = (Preference) findPreference("notification_reminder_persistent");
		this.notificationReminderPersistent.setEnabled(this.prefs.getBoolean("allow_notification_reminders", true));
	}
	
	public void populateView(){
		
	}

	@Override
	public boolean onPreferenceClick(Preference pref) {
		
		if(pref.getKey().equals("allow_email_reminders")){
			this.defaultEmailRemindBody.setEnabled(!this.defaultEmailRemindBody.isEnabled());
			this.defaultEmailRemindSubject.setEnabled(!this.defaultEmailRemindSubject.isEnabled());
		}
		
		if(pref.getKey().equals("allow_sms_reminders")){
			this.defaultSmsRemindBody.setEnabled(!this.defaultSmsRemindBody.isEnabled());
		}
		
		if(pref.getKey().equals("allow_notification_reminders")){
			this.notificationReminderPersistent.setEnabled(!this.notificationReminderPersistent.isEnabled());
		}
		
		return true;
	}
	
}
