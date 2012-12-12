package com.smileyjoedev.iou;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.smileyjoedev.genLibrary.Contact;
import com.smileyjoedev.genLibrary.Contacts;
import com.smileyjoedev.genLibrary.Debug;
import com.smileyjoedev.genLibrary.Notify;
import com.smileyjoedev.genLibrary.Send;
import com.smileyjoedev.iou.R;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class UserView extends SherlockActivity implements OnClickListener, OnItemSelectedListener {
	
	private User user;
	private DbUserAdapter userAdapter;
	private DbUserPaymentAdapter userPaymentAdapter;
	private ImageView ivUserImage;
	private ListView lvPaymentList;
	private Views views;
	private ArrayList<Payment> payments;
	private int selectedPayment;
	private Spinner spFilter;
	private UserPaymentListAdapter paymentListAdapter;
	private int userId;
//	private ImageButton ibtAddPayment;
	private LinearLayout llFilterWrapper;
	private SharedPreferences prefs;
	
	private Menu menu;
	private Spinner spEmailList;
	private Spinner spPhoneNumberList;
	private Contact contact;
	
	private static final int MENU_ADD_PAYMENT = 0;
	private static final int MENU_EDIT = 1;
	private static final int MENU_DELETE = 2;
	private static final int MENU_SEND_EMAIL = 3;
	private static final int MENU_SEND_SMS = 4;
	private static final int MENU_NOTIFICATION_REMINDER = 5;
	private static final int MENU_FILTER = 6;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gen.setTheme(this);
        setContentView(R.layout.user_view);
        this.initialize();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extras = getIntent().getExtras();
		
		this.userId = extras.getInt("user_id");
		Notify.cancelNotification(this, this.userId);
		
		this.user = new User(this);
		this.user = this.userAdapter.getDetails(userId);
		
		if(this.user.getId() == 0){
			Notify.toast(this, R.string.toast_user_does_not_exist);
			finish();
		}
        
		this.populateSpFilter();
        this.populateView();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.user_view, menu);
        
        this.menu = menu;
        this.handleMenu();
        return super.onCreateOptionsMenu(menu);
    }
    
    public void handleMenu(){
    	
    	if(this.user.getBalance() != 0){
			if(this.prefs.getBoolean("allow_notification_reminders", true)){
				this.menu.getItem(UserView.MENU_NOTIFICATION_REMINDER).setVisible(true);
			}
		} else {
			this.menu.getItem(UserView.MENU_NOTIFICATION_REMINDER).setVisible(false);
		}
		
		if(this.user.isInContactDir()){
			if(this.user.getBalance() > 0){
				if(this.prefs.getBoolean("allow_email_reminders", true)){
					this.menu.getItem(UserView.MENU_SEND_EMAIL).setVisible(true);
				}
				if(this.prefs.getBoolean("allow_sms_reminders", true)){
					this.menu.getItem(UserView.MENU_SEND_SMS).setVisible(true);
				}
			} else {
				this.menu.getItem(UserView.MENU_SEND_EMAIL).setVisible(false);
				this.menu.getItem(UserView.MENU_SEND_SMS).setVisible(false);
			}
		} else {
			this.menu.getItem(UserView.MENU_SEND_EMAIL).setVisible(false);
			this.menu.getItem(UserView.MENU_SEND_SMS).setVisible(false);
		}
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
			case R.id.menu_add_payment:
				startActivityForResult(Intents.newPayment(this, this.user.getId()), Constants.ACTIVITY_NEW_PAYMENT);
				return true;
			case R.id.menu_filter:
				if(this.llFilterWrapper.getVisibility() == View.VISIBLE){
					this.llFilterWrapper.setVisibility(View.GONE);
				} else {
					this.llFilterWrapper.setVisibility(View.VISIBLE);
				}
				return true;
			case R.id.menu_notification_reminder:
				String message = new String();
				
				if(this.user.getBalance() > 0){
					message = this.user.getName() + " " + this.getString(R.string.notification_owed_user) + " " + this.user.getBalanceText();
				} else {
					message = this.getString(R.string.notification_user_owed) + " " + this.user.getName() + " " + this.user.getBalanceText();
				}
				
		        Notify.notification(this, Intents.userView(this, this.user.getId(), this.user.getId()), this.getString(R.string.notification_title), message, this.prefs.getBoolean("notification_reminder_persistent", false));
				return true;
			case R.id.menu_edit_user:
				startActivityForResult(Intents.editUser(this, this.user.getId()), Constants.ACTIVITY_EDIT_USER);
				return true;
			case R.id.menu_delete_user:
				startActivityForResult(Intents.popupDelete(this, Constants.USER), Constants.ACTIVITY_POPUP_DELETE_USER);
				return true;
			case R.id.menu_send_email_reminder:
				this.populateSpEmailList(this.user.getContactId());
				this.spEmailList.performClick();
				return true;
			case R.id.menu_send_sms_reminder:
				this.populateSpPhoneNumberList(this.user.getContactId());
				this.spPhoneNumberList.performClick();
				return true;
			case android.R.id.home:
				Intent intent = new Intent(this, UserList.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
	            startActivity(intent);
	            finish();
				return true;
			default:
	            return super.onOptionsItemSelected(item);
        }

    }
    
    private void initialize(){
    	this.userAdapter = new DbUserAdapter(this);
    	this.ivUserImage = (ImageView) findViewById(R.id.iv_user_image);
    	this.userPaymentAdapter = new DbUserPaymentAdapter(this);
    	this.lvPaymentList = (ListView) findViewById(R.id.lv_payment_list);
    	registerForContextMenu(this.lvPaymentList);
    	this.views = new Views(this);
    	this.payments = new ArrayList<Payment>();
    	this.selectedPayment = 0;
    	this.spFilter = (Spinner) findViewById(R.id.sp_filter);
    	this.spFilter.setOnItemSelectedListener(this);
//    	this.ibtAddPayment = (ImageButton) findViewById(R.id.ibt_add_payment);
//    	this.ibtAddPayment.setOnClickListener(this);
    	this.llFilterWrapper = (LinearLayout) findViewById(R.id.ll_filter_wrapper);
    	this.prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    	this.spEmailList = (Spinner) findViewById(R.id.sp_email_list);
    	this.spEmailList.setOnItemSelectedListener(this);
    	this.spPhoneNumberList = (Spinner) findViewById(R.id.sp_phone_number_list);
    	this.spPhoneNumberList.setOnItemSelectedListener(this);
    	this.contact = new Contact();
    }
    
    private void populateView(){
    	this.populateView(true);
    }
    
    private void populateView(boolean getAll){
    	
    	
    	if(getAll){
    		this.payments = this.userPaymentAdapter.getByUser(this.user.getId());
    	}
    	
    	this.populateHeader();
    	this.paymentListAdapter = this.views.paymentList(this.payments, (LinearLayout) findViewById(R.id.ll_user_payment_list_wrapper));
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
    
    private void populateHeader(){
    	TextView tvTotalAmount = (TextView) findViewById(R.id.tv_total_amount);
    	TextView tvTotalStateText = (TextView) findViewById(R.id.tv_total_state_text);
    	TextView tvLastPaymentDate = (TextView) findViewById(R.id.tv_last_payment_date);
    	TextView tvUserName = (TextView) findViewById(R.id.tv_user_name);
    	
    	tvTotalAmount.setText(this.user.getBalanceText());
    	tvTotalStateText.setText(this.user.getStateText());
    	tvUserName.setText(this.user.getName());
    	
    	if(!this.user.getPayments().isEmpty()){
    		tvLastPaymentDate.setText(this.getString(R.string.tv_last_payment_title) + ": " + this.user.getPayments().get(0).getDateText(false));
    	} else {
    		tvLastPaymentDate.setVisibility(View.GONE);
    	}
    	
    	
    	if(this.user.getBalance() > 0){
    		tvTotalAmount.setTextColor(Color.GREEN);
    		tvTotalStateText.setTextColor(Color.GREEN);
		} else {
			if(this.user.getBalance() < 0){
				tvTotalAmount.setTextColor(Color.RED);
				tvTotalStateText.setTextColor(Color.RED);
			}
		}
    	
    	Gen.setUserImage(this, this.ivUserImage, this.user);
    }
    
    private void updatePaymentList(){
    	this.updatePaymentList(true);
    }
    
    private void updatePaymentList(boolean getPayments){
    	this.user = this.userAdapter.getDetails(userId);
    	if(getPayments){
    		this.payments = this.user.getPayments();
    	}
    	
    	this.paymentListAdapter.setPayments(this.payments);
    	this.paymentListAdapter.notifyDataSetChanged();
		this.lvPaymentList.refreshDrawableState();
		this.populateHeader();
		this.handleMenu();
    }
    
    private void populateSpFilter(){
    	ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		adapter.add(this.getString(R.string.spinner_all));
		adapter.add(this.getString(R.string.spinner_loans_to) + " " + this.user.getName());
		adapter.add(this.getString(R.string.spinner_loans_from) + " " + this.user.getName());
		adapter.add(this.getString(R.string.spinner_repayments_to) + " " + this.user.getName());
		adapter.add(this.getString(R.string.spinner_repayments_from) + " " + this.user.getName());
		
		this.spFilter.setAdapter(adapter);
    }

	@Override
	public void onClick(View v) {
//		switch(v.getId()){
//			case R.id.ibt_add_payment:
//				startActivityForResult(Intents.newPayment(this, this.user.getId()), Constants.ACTIVITY_NEW_PAYMENT);
//				break;
//		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
			case Constants.ACTIVITY_NEW_PAYMENT:
				this.updatePaymentList();
				break;
			case Constants.ACTIVITY_REPAY_PAYMENT:
				this.updatePaymentList();
				break;
			case Constants.ACTIVITY_EDIT_PAYMENT:
				this.updatePaymentList();
				break;
			case Constants.ACTIVITY_POPUP_DELETE:
				if(resultCode == Activity.RESULT_OK){
					if(data.getBooleanExtra("result", false)){
						this.userPaymentAdapter.delete(this.payments.get(this.selectedPayment));
						this.updatePaymentList();
					}
				}
				
				break;
			case Constants.ACTIVITY_POPUP_DELETE_USER:
				if(resultCode == Activity.RESULT_OK){
					if(data.getBooleanExtra("result", false)){
						this.userAdapter.delete(this.user);
						finish();
					}
				}
				
				break;
			case Constants.ACTIVITY_EDIT_USER:
				this.user = this.userAdapter.getDetails(this.userId);
				this.populateHeader();
				break;
		}
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if(v.getId() == R.id.lv_payment_list){
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			this.selectedPayment = info.position;
			menu.setHeaderTitle(this.getString(R.string.context_payment_heading));
			
			if(this.payments.get(this.selectedPayment).isLoan()){
				menu.add(Menu.NONE, Constants.CONTEXT_REPAY_ALL, Constants.CONTEXT_REPAY_ALL, this.getString(R.string.context_payment_repay_all));
				menu.add(Menu.NONE, Constants.CONTEXT_REPAY_SOME, Constants.CONTEXT_REPAY_SOME, this.getString(R.string.context_payment_repay_some));
			}
			menu.add(Menu.NONE, Constants.CONTEXT_EDIT, Constants.CONTEXT_EDIT, this.getString(R.string.context_payment_edit));
			menu.add(Menu.NONE, Constants.CONTEXT_DELETE, Constants.CONTEXT_DELETE, this.getString(R.string.context_payment_delete));
		}
	}
	
	public boolean onContextItemSelected(android.view.MenuItem item) {
		int menuItemIndex = item.getItemId();
		switch(menuItemIndex){
			case Constants.CONTEXT_REPAY_ALL:
				Payment payment = new Payment(this);
				payment = this.payments.get(this.selectedPayment);
				payment.setDescription(payment.getTitle());
				payment.setTitle(this.getString(R.string.payment_title_repayment));
				
				
				if(payment.isToUser()){
					payment.setTypeDb(Payment.TYPE_DB_PAYMENT_FROM_USER);
				} else {
					payment.setTypeDb(Payment.TYPE_DB_PAYMENT_TO_USER);
				}
				
				payment.setDate(Gen.getPdt());
				
				payment.setId(0);
				
				
				this.userPaymentAdapter.save(payment);
				
				Gen.displayMinimalisticText(this, this.user, payment);
				
				this.updatePaymentList();
				
				break;
			case Constants.CONTEXT_REPAY_SOME:
				startActivityForResult(Intents.repayPayment(this, this.payments.get(this.selectedPayment).getId()), Constants.ACTIVITY_REPAY_PAYMENT);
				break;
			case Constants.CONTEXT_EDIT:
				startActivityForResult(Intents.editPayment(this, this.payments.get(this.selectedPayment).getId()), Constants.ACTIVITY_EDIT_PAYMENT);
				break;
			case Constants.CONTEXT_DELETE:
				startActivityForResult(Intents.popupDelete(this, Constants.PAYMENT), Constants.ACTIVITY_POPUP_DELETE);
				break;
		}
		return true;
	}

	
	@Override
	public void onItemSelected(AdapterView<?> view, View arg1, int position, long arg3) {
		switch(view.getId()){
			case R.id.sp_filter:
				switch(position){
					case 0:
						this.payments = this.userPaymentAdapter.getByUser(this.user.getId());
						break;
					case 1:
						this.payments = this.userPaymentAdapter.getByTypeDb(Payment.TYPE_DB_LOAN_TO_USER, this.user.getId());
						break;
					case 2:
						this.payments = this.userPaymentAdapter.getByTypeDb(Payment.TYPE_DB_LOAN_FROM_USER, this.user.getId());
						break;
					case 3:
						this.payments = this.userPaymentAdapter.getByTypeDb(Payment.TYPE_DB_PAYMENT_TO_USER, this.user.getId());
						break;
					case 4:
						this.payments = this.userPaymentAdapter.getByTypeDb(Payment.TYPE_DB_PAYMENT_FROM_USER, this.user.getId());
						break;
				}
				this.updatePaymentList(false);
			break;
			case R.id.sp_email_list:
				if(position > 0){
					position = position - 1;
					String subject = this.prefs.getString("default_email_reminder_subject", this.getString(R.string.default_email_reminder_subject));
					
					Send.emailDialog(this, this.contact.getEmail(position).getAddress(), subject, Gen.createEmailBody(this, this.user));
				}
				break;
			case R.id.sp_phone_number_list:
				if(position > 0){
					position = position - 1;
					
					String number = this.contact.getNumber(position).getNumber();
					String message = Gen.createSms(this, this.user);
					
					Send.smsDialog(this, number, message);
				}
				
				break;
		}
		
	}
	

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	
	public void onBackPressed() {
		if(this.llFilterWrapper.getVisibility() == View.VISIBLE){
			this.llFilterWrapper.setVisibility(View.GONE);
		} else {
			finish();
		}
	}
}
