package com.smileyjoedev.iou;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.smileyjoedev.genLibrary.Debug;
import com.smileyjoedev.genLibrary.Notify;
import com.smileyjoedev.iou.R;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
	private ImageButton ibtAddPayment;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gen.setTheme(this);
        setContentView(R.layout.user_view);
        this.initialize();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extras = getIntent().getExtras();
		
		this.userId = extras.getInt("user_id");
		Notify.cancelNotification(this, this.userId);
		
		this.user = new User();
		this.user = this.userAdapter.getDetails(userId);
        
		this.populateSpFilter();
        this.populateView();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.user_view, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
			case R.id.menu_add_payment:
				startActivityForResult(Intents.newPayment(this, this.user.getId()), Constants.ACTIVITY_NEW_PAYMENT);
				return true;
			case R.id.menu_filter:
				this.spFilter.performClick();
				return true;
			case android.R.id.home:
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
    	this.ibtAddPayment = (ImageButton) findViewById(R.id.ibt_add_payment);
    	this.ibtAddPayment.setOnClickListener(this);
    }
    
    private void populateView(){
    	this.populateView(true);
    }
    
    private void populateView(boolean getAll){
    	TextView tvUserName = (TextView) findViewById(R.id.tv_user_name);
    	
    	if(getAll){
    		this.payments = this.userPaymentAdapter.getByUser(this.user.getId());
    	}
    	
    	Gen.setUserImage(this, this.ivUserImage, this.user);
    	tvUserName.setText(this.user.getName());
    	
    	this.populateHeader();
    	this.paymentListAdapter = this.views.paymentList(this.payments, (LinearLayout) findViewById(R.id.ll_user_payment_list_wrapper));
    }
    
    private void populateHeader(){
    	TextView tvTotalAmount = (TextView) findViewById(R.id.tv_total_amount);
    	TextView tvTotalStateText = (TextView) findViewById(R.id.tv_total_state_text);
    	TextView tvLastPaymentDate = (TextView) findViewById(R.id.tv_last_payment_date);
    	
    	tvTotalAmount.setText(this.user.getBalanceText());
    	tvTotalStateText.setText(this.user.getStateText());
    	
    	//TODO: Use String for this //
    	if(!this.user.getPayments().isEmpty()){
    		tvLastPaymentDate.setText("Last: " + this.user.getPayments().get(0).getDateText(false));
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
    }
    
    private void updatePaymentList(){
    	this.user = this.userAdapter.getDetails(userId);
    	this.payments = this.user.getPayments();
    	this.paymentListAdapter.setPayments(this.payments);
    	this.paymentListAdapter.notifyDataSetChanged();
		this.lvPaymentList.refreshDrawableState();
		this.populateHeader();
    }
    
    private void populateSpFilter(){
    	ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		//TODO: Use Strings for this //
		adapter.add("All");
		adapter.add("Loans to " + this.user.getName());
		adapter.add("Loans from " + this.user.getName());
		adapter.add("Repayments to " + this.user.getName());
		adapter.add("Repayments from " + this.user.getName());
		
		this.spFilter.setAdapter(adapter);
    }

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.ibt_add_payment:
				startActivityForResult(Intents.newPayment(this, this.user.getId()), Constants.ACTIVITY_NEW_PAYMENT);
				break;
		}
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
				Payment payment = new Payment();
				payment = this.payments.get(this.selectedPayment);
				payment.setDescription("Repayment: " + payment.getDescription());
				
				if(payment.isToUser()){
					payment.setTypeDb(3);
				} else {
					payment.setTypeDb(2);
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
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
		switch(position){
			case 0:
				this.payments = this.userPaymentAdapter.getByUser(this.user.getId());
				break;
			case 1:
				this.payments = this.userPaymentAdapter.getByTypeDb(0, this.user.getId());
				break;
			case 2:
				this.payments = this.userPaymentAdapter.getByTypeDb(1, this.user.getId());
				break;
			case 3:
				this.payments = this.userPaymentAdapter.getByTypeDb(2, this.user.getId());
				break;
			case 4:
				this.payments = this.userPaymentAdapter.getByTypeDb(3, this.user.getId());
				break;
		}
		this.populateView(false);
		
	}
	

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
}
