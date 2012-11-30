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
import com.smileyjoedev.genLibrary.Contact;
import com.smileyjoedev.genLibrary.Contacts;
import com.smileyjoedev.genLibrary.Debug;
import com.smileyjoedev.genLibrary.Notify;
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

	public static final int FILTER_ALL = 0;
	public static final int FILTER_OWE_ME = 1;
	public static final int FILTER_I_OWE = 2;
	public static final int FILTER_NEUTRAL = 3;
	
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
	private UserListAdapter userListAdapter;
	private boolean isStartPage;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gen.setTheme(this);
        setContentView(R.layout.user_list);
        
        this.initialize();
        if(!this.isStartPage){
        	getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        this.populateView();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        
        if(this.isStartPage){
        	inflater.inflate(R.menu.user_list_start_page, menu);
        } else {
        	inflater.inflate(R.menu.user_list, menu);
        }
        
        return super.onCreateOptionsMenu(menu);
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
			case R.id.menu_add_user:
				startActivityForResult(Intents.userNew(this), Constants.ACTIVITY_NEW_USER);
				return true;
			case R.id.menu_filter:
				if(this.llFilterWrapper.getVisibility() == View.VISIBLE){
					this.llFilterWrapper.setVisibility(View.GONE);
				} else {
					this.llFilterWrapper.setVisibility(View.VISIBLE);
				}
				return true;
			case R.id.menu_settings:
				startActivityForResult(Intents.settings(this), Constants.ACTIVITY_SETTINGS);
				return true;
			case android.R.id.home:
				if(!this.isStartPage){
					finish();
				}
				return true;
	        default:
	            return super.onOptionsItemSelected(item);
        }

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
    	this.sort = SortUser.SORT_ALPHABETICAL;
    	this.contact = new Contact();
    	this.spEmailList = (Spinner) findViewById(R.id.sp_email_list);
    	this.spEmailList.setOnItemSelectedListener(this);
    	this.spPhoneNumberList = (Spinner) findViewById(R.id.sp_phone_number_list);
    	this.spPhoneNumberList.setOnItemSelectedListener(this);
    	this.prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    	this.llFilterWrapper = (LinearLayout) findViewById(R.id.ll_filter_wrapper);
    	
    	if(Integer.parseInt(this.prefs.getString("default_start_page", "0")) == Settings.START_INDIVIDUAL_VIEW_ALL){
        	this.isStartPage = true;
        } else {
        	this.isStartPage = false;
        }
    	
    }
    
    public void populateView(){
    	this.users = this.userAdapter.get();
    	
    	this.userListAdapter = this.views.userList(this.users, (LinearLayout) findViewById(R.id.ll_user_list_wrapper));
    }
    
    private void updateUserList(){
    	this.updateUserList(true);
    }
    
    private void updateUserList(boolean getAll){
    	if(getAll){
    		this.allUsers = this.userAdapter.get();
    		this.users.clear();
    		for(int i = 0; i < this.allUsers.size(); i++){
    			this.users.add(this.allUsers.get(i));
    		}
    	}
    	
    	this.userListAdapter.setUsers(this.users);
    	this.userListAdapter.notifyDataSetChanged();
		this.lvUserList.refreshDrawableState();
    }
    
    private void populateSpEmailList(long id){
    	Contacts cont = new Contacts(this);
    	this.contact = cont.getDetails(id);
    	
    	ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		adapter.add(this.getString(R.string.spinner_cancel));
		
		for(int i = 0; i < this.contact.getEmails().size(); i++){
			adapter.add(this.contact.getEmail(i).getType() + " (" + this.contact.getEmail(i).getAddress() + ")");
		}
		
		this.spEmailList.setAdapter(adapter);
		this.spEmailList.setSelection(-1);
		
    }
    
    private void populateSpPhoneNumberList(long id){
    	Contacts cont = new Contacts(this);
    	this.contact = cont.getDetails(id);
    	
    	ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		adapter.add(this.getString(R.string.spinner_cancel));
		
		for(int i = 0; i < this.contact.getNumbers().size(); i++){
			adapter.add(this.contact.getNumber(i).getType() + " (" + this.contact.getNumber(i).getNumber() + ")");
		}
		
		this.spPhoneNumberList.setAdapter(adapter);
		this.spPhoneNumberList.setSelection(-1);
    }
    
    private void sortUsers(int sort){
    	Gen.sortUser(this.users, sort);
    }
    
	@Override
	public void onItemSelected(AdapterView<?> view, View arg1, int position, long arg3) {
		switch(view.getId()){
			case R.id.sp_filter:
				this.filterUsers(position);
				this.sortUsers(this.sort);
				this.updateUserList(false);
				break;
			case R.id.sp_sort:
				this.sort = position;
				this.sortUsers(position);
				this.updateUserList(false);
				break;
			case R.id.sp_email_list:
				if(position > 0){
					position = position - 1;
					String subject = this.prefs.getString("default_email_reminder_subject", this.getString(R.string.default_email_reminder_subject));
					
					Send.emailDialog(this, this.contact.getEmail(position).getAddress(), subject, Gen.createEmailBody(this, this.users.get(this.selectedUser)));
				}
				break;
			case R.id.sp_phone_number_list:
				if(position > 0){
					position = position - 1;
					
					String number = this.contact.getNumber(position).getNumber();
					String message = Gen.createSms(this, this.users.get(this.selectedUser));
					
					Send.smsDialog(this, number, message);
				}
				
				break;
		}
		
	}
	
	private void filterUsers(int filter){
		this.users.clear();
		for(int i = 0; i < this.allUsers.size(); i++){
			switch(filter){
				case UserList.FILTER_ALL:
					this.users.add(this.allUsers.get(i));
					break;
				case UserList.FILTER_OWE_ME:
					if(this.allUsers.get(i).getBalance() > 0){
						this.users.add(this.allUsers.get(i));
					}
					break;
				case UserList.FILTER_I_OWE:
					if(this.allUsers.get(i).getBalance() < 0){
						this.users.add(this.allUsers.get(i));
					}
					break;
				case UserList.FILTER_NEUTRAL:
					if(this.allUsers.get(i).getBalance() == 0){
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
				startActivityForResult(Intents.userView(this, this.users.get(position).getId()), Constants.ACTIVITY_USER_VIEW);
				break;
		}
		
	}
	
	@Override
	public void onClick(View view) {
		
		switch(view.getId()){
		}
	}
	
	private void getAllUsers(){
		this.allUsers = this.userAdapter.get();
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
			case Constants.ACTIVITY_NEW_USER:
				this.updateUserList();
				break;
			case Constants.ACTIVITY_USER_VIEW:
				this.updateUserList();
				break;	
			case Constants.ACTIVITY_POPUP_DELETE:
				if(resultCode == Activity.RESULT_OK){
					if(data.getBooleanExtra("result", false)){
						this.userAdapter.delete(this.users.get(this.selectedUser));
						this.updateUserList();
					}
				}
				break;
			case Constants.ACTIVITY_REPAY_PAYMENT_USER:
				this.updateUserList();
				break;
			case Constants.ACTIVITY_EDIT_USER:
				this.updateUserList();
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
				
				if(this.users.get(this.selectedUser).getBalance() != 0){
					if(this.prefs.getBoolean("allow_notification_reminders", true)){
						menu.add(Menu.NONE, Constants.CONTEXT_PERSISTENT_NOTIFICATION, Constants.CONTEXT_PERSISTENT_NOTIFICATION, this.getString(R.string.context_notification_reminder));
					}
					menu.add(Menu.NONE, Constants.CONTEXT_REPAY_ALL, Constants.CONTEXT_REPAY_ALL, this.getString(R.string.context_user_repay_all));
					menu.add(Menu.NONE, Constants.CONTEXT_REPAY_SOME, Constants.CONTEXT_REPAY_SOME, this.getString(R.string.context_user_repay_some));
				}
				
				if(this.users.get(this.selectedUser).isInContactDir()){
					menu.add(Menu.NONE, Constants.CONTEXT_VIEW_CONTACT_CARD, Constants.CONTEXT_VIEW_CONTACT_CARD, this.getString(R.string.context_user_open_contact_card));
					
					if(this.users.get(this.selectedUser).getBalance() > 0){
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
				Payment payment = new Payment(this);
				payment.setTitle(this.getString(R.string.payment_title_repayment));
				payment.setDescription(this.getString(R.string.payment_description_repayment_all));
				
				if(this.users.get(this.selectedUser).getBalance() > 0){
					payment.setTypeDb(Payment.TYPE_DB_PAYMENT_FROM_USER);
				} else {
					payment.setTypeDb(Payment.TYPE_DB_PAYMENT_TO_USER);
				}
				
				
				payment.setAmount(Math.abs(this.users.get(this.selectedUser).getBalance()));
				
				
				payment.setDate(Gen.getPdt());
				
				payment.setUserId(this.users.get(this.selectedUser).getId());
				payment.setId(0);
				
				this.userPaymentAdapter.save(payment);
				
				Gen.displayMinimalisticText(this, this.users.get(this.selectedUser), payment);
				
				this.updateUserList();
				
				break;
			case Constants.CONTEXT_REPAY_SOME:
				startActivityForResult(Intents.repayPaymentUser(this, this.users.get(this.selectedUser).getId()), Constants.ACTIVITY_REPAY_PAYMENT_USER);
				break;
			case Constants.CONTEXT_EDIT:
				startActivityForResult(Intents.editUser(this, this.users.get(this.selectedUser).getId()), Constants.ACTIVITY_EDIT_USER);
				break;
			case Constants.CONTEXT_DELETE:
				startActivityForResult(Intents.popupDelete(this, Constants.USER), Constants.ACTIVITY_POPUP_DELETE);
				break;
			case Constants.CONTEXT_REMINDER_EMAIL:
				this.populateSpEmailList(this.users.get(this.selectedUser).getContactId());
				this.spEmailList.performClick();
				break;
			case Constants.CONTEXT_REMINDER_SMS:
				this.populateSpPhoneNumberList(this.users.get(this.selectedUser).getContactId());
				this.spPhoneNumberList.performClick();
				break;
			case Constants.CONTEXT_VIEW_CONTACT_CARD:
				Contacts.openContact(this, this.users.get(this.selectedUser).getContactId());
				break;
			case Constants.CONTEXT_PERSISTENT_NOTIFICATION:
				String message = new String();
				User user = this.users.get(this.selectedUser);
				
				if(user.getBalance() > 0){
					message = user.getName() + " " + this.getString(R.string.notification_owed_user) + " " + user.getBalanceText();
				} else {
					message = this.getString(R.string.notification_user_owed) + " " + user.getName() + " " + user.getBalanceText();
				}
				
		        Notify.notification(this, Intents.userView(this, user.getId(), user.getId()), this.getString(R.string.notification_title), message, this.prefs.getBoolean("notification_reminder_persistent", false));
				
				break;
		}
		return true;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}

}