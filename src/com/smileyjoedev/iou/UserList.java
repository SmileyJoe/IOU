package com.smileyjoedev.iou;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.bugsense.trace.BugSenseHandler;
import com.smileyjoedev.genLibrary.Contact;
import com.smileyjoedev.genLibrary.Contacts;
import com.smileyjoedev.genLibrary.Send;
import com.smileyjoedev.iou.R;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.TabHost.TabContentFactory;


public class UserList extends SherlockActivity implements OnItemClickListener, OnClickListener, OnItemSelectedListener {
	
	private ArrayList<User> users;
	private DbUserAdapter userAdapter;
	private ListView lvUserList;
	private Views views;
	private int selectedUser;
	private DbUserPaymentAdapter userPaymentAdapter;
	private Spinner spFilter;
	private ArrayList<User> allUsers;
	private Spinner spSort;
	private int sort;
	private Contact contact;
	private Spinner spEmailList;
	private Spinner spPhoneNumberList;
	private SharedPreferences prefs;
	private LinearLayout llFilterWrapper;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gen.setTheme(this);
        setContentView(R.layout.user_list);
//        BugSenseHandler.setup(this, "04b74a70");
        
        this.initialize();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.populate_view();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.user_list, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
			case R.id.menu_add_user:
				startActivityForResult(Intents.new_user(this), Constants.ACTIVITY_NEW_USER);
				return true;
			case R.id.menu_filter:
				if(this.llFilterWrapper.getVisibility() == View.VISIBLE){
					this.llFilterWrapper.setVisibility(View.GONE);
				} else {
					this.llFilterWrapper.setVisibility(View.VISIBLE);
				}
				return true;
			case android.R.id.home:
				finish();
				return true;
	        default:
	            return super.onOptionsItemSelected(item);
        }

    }
    
	public boolean is_table_exists(SQLiteDatabase database, String tableName) {
	    Cursor cursor = database.rawQuery("select DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = '"+tableName+"'", null);
	    if(cursor!=null) {
	        if(cursor.getCount()>0) {
	            return true;
	        }
	    }
	    return false;
	}
    
    public void initialize(){
    	this.users = new ArrayList<User>();
    	this.userAdapter = new DbUserAdapter(this);
    	this.lvUserList = (ListView) findViewById(R.id.lv_user_list);
    	this.lvUserList.setOnItemClickListener(this);
    	registerForContextMenu(this.lvUserList);
    	this.views = new Views(this);
    	this.selectedUser = 0;
    	this.userPaymentAdapter = new DbUserPaymentAdapter(this);
    	this.spFilter = (Spinner) findViewById(R.id.sp_filter);
    	this.spFilter.setOnItemSelectedListener(this);
    	this.allUsers = this.userAdapter.get();
    	this.spSort = (Spinner) findViewById(R.id.sp_sort);
    	this.spSort.setOnItemSelectedListener(this);
    	this.sort = 0;
    	this.contact = new Contact();
    	this.spEmailList = (Spinner) findViewById(R.id.sp_email_list);
    	this.spEmailList.setOnItemSelectedListener(this);
    	this.spPhoneNumberList = (Spinner) findViewById(R.id.sp_phone_number_list);
    	this.spPhoneNumberList.setOnItemSelectedListener(this);
    	this.prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    	this.llFilterWrapper = (LinearLayout) findViewById(R.id.ll_filter_wrapper);
    	
    }
    
    public void populate_view(boolean getAll){
    	if(getAll){
    		this.users.clear();
    		for(int i = 0; i < this.allUsers.size(); i++){
    			this.users.add(this.allUsers.get(i));
    		}
    	}
    	
    	this.views.user_list(this.users, this.lvUserList);
    }
    
    private void populate_view(){
    	this.populate_view(true);
    }
    
    private void populate_sp_email_list(long id){
    	Contacts cont = new Contacts(this);
    	this.contact = cont.getDetails(id);
    	
    	ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		adapter.add("Cancel");
		
		for(int i = 0; i < this.contact.getEmails().size(); i++){
			adapter.add(this.contact.getEmail(i).getType() + " (" + this.contact.getEmail(i).getAddress() + ")");
		}
		
		this.spEmailList.setAdapter(adapter);
		this.spEmailList.setSelection(-1);
		
    }
    
    private void populate_sp_phone_number_list(long id){
    	Contacts cont = new Contacts(this);
    	this.contact = cont.getDetails(id);
    	
    	ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		adapter.add("Cancel");
		
		for(int i = 0; i < this.contact.getNumbers().size(); i++){
			adapter.add(this.contact.getNumber(i).getType() + " (" + this.contact.getNumber(i).getNumber() + ")");
		}
		
		this.spPhoneNumberList.setAdapter(adapter);
		this.spPhoneNumberList.setSelection(-1);
    }
    
    private void sort_users(int sort){
    	/*
    	 * 0 - Alphabetical
    	 * 1 - desc
    	 * 2 - asc
    	 */
    	
    	Gen.sort_user(this.users, sort);
    }
    
	@Override
	public void onItemSelected(AdapterView<?> view, View arg1, int position, long arg3) {
		switch(view.getId()){
			case R.id.sp_filter:
				this.filter_users(position);
				this.sort_users(this.sort);
				break;
			case R.id.sp_sort:
				this.sort = position;
				this.sort_users(position);
				break;
			case R.id.sp_email_list:
				if(position > 0){
					position = position - 1;
					String subject = this.prefs.getString("default_email_reminder_subject", this.getString(R.string.default_email_reminder_subject));
					
					Send.emailDialog(this, this.contact.getEmail(position).getAddress(), subject, Gen.create_email_body(this, this.users.get(this.selectedUser)));
				}
				break;
			case R.id.sp_phone_number_list:
				if(position > 0){
					position = position - 1;
					
					String message = this.prefs.getString("default_sms_reminder_body", this.getString(R.string.default_sms_reminder_body));
					String username = this.users.get(this.selectedUser).get_name();
					String balance = this.prefs.getString("default_currency", this.getString(R.string.default_currency)) + this.users.get(this.selectedUser).get_balance_text();
					String number = this.contact.getNumber(position).getNumber();
					
					message = message.replace("%USERNAME", username);
					message = message.replace("%BALANCE", balance);
					
					Send.smsDialog(this, number, message);
				}
				
				break;
		}
		
		this.populate_view(false);
		
	}
	
	private void filter_users(int filter){
		/*
		 * 0 - all
		 * 1 - owe me
		 * 2 - i owe
		 * 3 - neutral
		 */
		
		this.users.clear();
		for(int i = 0; i < this.allUsers.size(); i++){
			switch(filter){
				case 0:
					this.users.add(this.allUsers.get(i));
					break;
				case 1:
					if(this.allUsers.get(i).get_balance() > 0){
						this.users.add(this.allUsers.get(i));
					}
					break;
				case 2:
					if(this.allUsers.get(i).get_balance() < 0){
						this.users.add(this.allUsers.get(i));
					}
					break;
				case 3:
					if(this.allUsers.get(i).get_balance() == 0){
						this.users.add(this.allUsers.get(i));
					}
					break;
			}
		}
	}
    
	@Override
	public void onItemClick(AdapterView<?> view, View arg1, int position, long arg3) {
		switch(view.getId()){
			case R.id.lv_user_list:
				startActivityForResult(Intents.user_view(this, this.users.get(position).get_id()), Constants.ACTIVITY_USER_VIEW);
				break;
		}
		
	}
	
	@Override
	public void onClick(View view) {
		
		switch(view.getId()){
		}
	}
	
	private void get_all_users(){
		this.allUsers = this.userAdapter.get();
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
			case Constants.ACTIVITY_NEW_USER:
				this.get_all_users();
				this.populate_view();
				break;
			case Constants.ACTIVITY_USER_VIEW:
				this.get_all_users();
				this.populate_view();
				break;	
			case Constants.ACTIVITY_SETTINGS:
				Gen.changeTheme(this);
				break;
			case Constants.ACTIVITY_POPUP_DELETE:
				if(resultCode == Activity.RESULT_OK){
					if(data.getBooleanExtra("result", false)){
						this.userAdapter.delete(this.users.get(this.selectedUser));
						this.get_all_users();
						this.populate_view();
					}
				}
				break;
			case Constants.ACTIVITY_REPAY_PAYMENT_USER:
				this.get_all_users();
				this.populate_view();
				break;
			case Constants.ACTIVITY_EDIT_USER:
				this.get_all_users();
				this.populate_view();
				break;
			case Constants.ACTIVITY_GROUP_NEW:
				this.populate_view();
				break;
			case Constants.ACTIVITY_GROUP_EDIT:
				this.populate_view();
				break;
			case Constants.ACTIVITY_GROUP_VIEW:
				this.populate_view();
				break;
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		switch(v.getId()){
			case R.id.lv_user_list:
				this.selectedUser = info.position;
				menu.setHeaderTitle(this.getString(R.string.context_heading));
				
				menu.add(Menu.NONE, Constants.CONTEXT_EDIT, Constants.CONTEXT_EDIT, this.getString(R.string.context_edit));
				menu.add(Menu.NONE, Constants.CONTEXT_DELETE, Constants.CONTEXT_DELETE, this.getString(R.string.context_delete));
				
				if(this.users.get(this.selectedUser).get_balance() != 0){
					menu.add(Menu.NONE, Constants.CONTEXT_REPAY_ALL, Constants.CONTEXT_REPAY_ALL, this.getString(R.string.context_user_repay_all));
					menu.add(Menu.NONE, Constants.CONTEXT_REPAY_SOME, Constants.CONTEXT_REPAY_SOME, this.getString(R.string.context_user_repay_some));
				}
				
				if(this.users.get(this.selectedUser).is_in_contact_dir()){
					menu.add(Menu.NONE, Constants.CONTEXT_VIEW_CONTACT_CARD, Constants.CONTEXT_VIEW_CONTACT_CARD, this.getString(R.string.context_user_open_contact_card));
					
					if(this.users.get(this.selectedUser).get_balance() > 0){
						if(this.prefs.getBoolean("allow_email_reminders", true)){
							menu.add(Menu.NONE, Constants.CONTEXT_REMINDER_EMAIL, Constants.CONTEXT_REMINDER_EMAIL, this.getString(R.string.context_user_reminder_email));
						}
						if(this.prefs.getBoolean("allow_sms_reminders", true)){
							menu.add(Menu.NONE, Constants.CONTEXT_REMINDER_SMS, Constants.CONTEXT_REMINDER_SMS, this.getString(R.string.context_user_reminder_sms));
						}
					}
					
				}
				break;
		}
	}
	
	public boolean onContextItemSelected(android.view.MenuItem item) {
		int menuItemIndex = item.getItemId();
		switch(menuItemIndex){
			case Constants.CONTEXT_REPAY_ALL:
				// repay all //
				Payment payment = new Payment();
				
				payment.set_description("Repayment: All");
				
				if(this.users.get(this.selectedUser).get_balance() > 0){
					payment.set_type_db(3);
				} else {
					payment.set_type_db(2);
				}
				
				
				payment.set_amount(Math.abs(this.users.get(this.selectedUser).get_balance()));
				
				
				payment.set_date(Gen.get_pdt());
				
				payment.set_user_id(this.users.get(this.selectedUser).get_id());
				payment.set_id(0);
				
				this.userPaymentAdapter.save(payment);
				
				Gen.display_minimalistic_text(this, this.users.get(this.selectedUser), payment);
				
				this.populate_view();
				
				break;
			case Constants.CONTEXT_REPAY_SOME:
				startActivityForResult(Intents.repay_payment_user(this, this.users.get(this.selectedUser).get_id()), Constants.ACTIVITY_REPAY_PAYMENT_USER);
				break;
			case Constants.CONTEXT_EDIT:
				startActivityForResult(Intents.edit_user(this, this.users.get(this.selectedUser).get_id()), Constants.ACTIVITY_EDIT_USER);
				break;
			case Constants.CONTEXT_DELETE:
				startActivityForResult(Intents.popup_delete(this, Constants.GROUP), Constants.ACTIVITY_POPUP_DELETE);
				break;
			case Constants.CONTEXT_REMINDER_EMAIL:
				// email reminder //
				this.populate_sp_email_list(this.users.get(this.selectedUser).get_contact_id());
				this.spEmailList.performClick();
				break;
			case Constants.CONTEXT_REMINDER_SMS:
				// sms reminder //
				this.populate_sp_phone_number_list(this.users.get(this.selectedUser).get_contact_id());
				this.spPhoneNumberList.performClick();
				break;
			case Constants.CONTEXT_VIEW_CONTACT_CARD:
				Contacts.openContact(this, this.users.get(this.selectedUser).get_contact_id());
				break;
		}
		return true;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}

}