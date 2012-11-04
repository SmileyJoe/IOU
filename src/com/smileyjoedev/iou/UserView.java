package com.smileyjoedev.iou;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class UserView extends SherlockActivity implements OnClickListener, OnItemSelectedListener {
	
	private User user;
	private DbUserAdapter userAdapter;
	private DbUserPaymentAdapter userPaymentAdapter;
//	private TextView tvHeaderText;
	private ImageView ivUserImage;
//	private Button btAddPayment;
	private ListView lvPaymentList;
	private Views views;
	private ArrayList<Payment> payments;
	private int selectedPayment;
//	private Button btFilter;
	private Spinner spFilter;
	private PaymentListAdapter paymentlistAdapter;
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
		this.user = new User();
		this.user = this.userAdapter.get_details(userId);
        
		this.populate_sp_filter();
        this.populate_view();
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
				startActivityForResult(Intents.new_payment(this, this.user.get_id()), Constants.ACTIVITY_NEW_PAYMENT);
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
//    	this.tvHeaderText = (TextView) findViewById(R.id.tv_header_text);
    	this.ivUserImage = (ImageView) findViewById(R.id.iv_user_image);
//    	this.btAddPayment = (Button) findViewById(R.id.bt_add_payment);
//    	this.btAddPayment.setOnClickListener(this);
    	this.userPaymentAdapter = new DbUserPaymentAdapter(this);
    	this.lvPaymentList = (ListView) findViewById(R.id.lv_payment_list);
    	registerForContextMenu(this.lvPaymentList);
    	this.views = new Views(this);
    	this.payments = new ArrayList<Payment>();
    	this.selectedPayment = 0;
//    	this.btFilter = (Button) findViewById(R.id.bt_filter);
//    	this.btFilter.setOnClickListener(this);
    	this.spFilter = (Spinner) findViewById(R.id.sp_filter);
    	this.spFilter.setOnItemSelectedListener(this);
    	this.ibtAddPayment = (ImageButton) findViewById(R.id.ibt_add_payment);
    	this.ibtAddPayment.setOnClickListener(this);
    }
    
    private void populate_view(){
    	this.populate_view(true);
    }
    
    private void populate_view(boolean getAll){
    	TextView tvUserName = (TextView) findViewById(R.id.tv_user_name);
    	
    	if(getAll){
    		this.payments = this.userPaymentAdapter.get_by_user(this.user.get_id());
    	}
    	
//    	this.tvHeaderText.setText(this.user.get_name());
    	Gen.set_user_image(this, this.ivUserImage, this.user);
    	tvUserName.setText(this.user.get_name());
    	
    	this.populateHeader();
    	this.paymentlistAdapter = this.views.payment_list(this.payments, this.lvPaymentList);
    }
    
    private void populateHeader(){
    	TextView tvTotalAmount = (TextView) findViewById(R.id.tv_total_amount);
    	TextView tvTotalStateText = (TextView) findViewById(R.id.tv_total_state_text);
    	TextView tvLastPaymentDate = (TextView) findViewById(R.id.tv_last_payment_date);
    	
    	tvTotalAmount.setText(this.user.get_balance_text());
    	tvTotalStateText.setText(this.user.get_state_text());
    	
    	//TODO: Use String for this //
    	if(!this.user.get_payments().isEmpty()){
    		tvLastPaymentDate.setText("Last: " + this.user.get_payments().get(0).get_date_text(false));
    	} else {
    		tvLastPaymentDate.setVisibility(View.GONE);
    	}
    	
    	
    	if(this.user.get_balance() > 0){
    		tvTotalAmount.setTextColor(Color.GREEN);
    		tvTotalStateText.setTextColor(Color.GREEN);
		} else {
			if(this.user.get_balance() < 0){
				tvTotalAmount.setTextColor(Color.RED);
				tvTotalStateText.setTextColor(Color.RED);
			}
		}
    }
    
    private void updatePaymentList(){
    	this.user = this.userAdapter.get_details(userId);
    	this.payments = this.user.get_payments();
    	this.paymentlistAdapter.setPayments(this.payments);
    	this.paymentlistAdapter.notifyDataSetChanged();
		this.lvPaymentList.refreshDrawableState();
		this.populateHeader();
    }
    
    private void populate_sp_filter(){
    	ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		//TODO: Use Strings for this //
		adapter.add("All");
		adapter.add("Loans to " + this.user.get_name());
		adapter.add("Loans from " + this.user.get_name());
		adapter.add("Repayments to " + this.user.get_name());
		adapter.add("Repayments from " + this.user.get_name());
		
		this.spFilter.setAdapter(adapter);
    }

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.ibt_add_payment:
				startActivityForResult(Intents.new_payment(this, this.user.get_id()), Constants.ACTIVITY_NEW_PAYMENT);
				break;
//			case R.id.bt_add_payment:
//				startActivityForResult(Intents.new_payment(this, this.user.get_id()), Constants.ACTIVITY_NEW_PAYMENT);
//				break;
//			case R.id.bt_filter:
//				this.spFilter.performClick();
//				break;
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
			
			if(this.payments.get(this.selectedPayment).is_loan()){
				menu.add(Menu.NONE, Constants.CONTEXT_REPAY_ALL, Constants.CONTEXT_REPAY_ALL, this.getString(R.string.context_payment_repay_all));
				menu.add(Menu.NONE, Constants.CONTEXT_REPAY_SOME, Constants.CONTEXT_REPAY_SOME, this.getString(R.string.context_payment_repay_some));
			}
			menu.add(Menu.NONE, Constants.CONTEXT_EDIT, Constants.CONTEXT_EDIT, this.getString(R.string.context_payment_edit));
			menu.add(Menu.NONE, Constants.CONTEXT_DELETE, Constants.CONTEXT_DELETE, this.getString(R.string.context_payment_delete));
		}
	}
	
	public boolean onContextItemSelected(android.view.MenuItem item) {
		//AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
			int menuItemIndex = item.getItemId();
		switch(menuItemIndex){
			case Constants.CONTEXT_REPAY_ALL:
				// repay all //
				Payment payment = new Payment();
				payment = this.payments.get(this.selectedPayment);
				payment.set_description("Repayment: " + payment.get_description());
				
				if(payment.is_to_user()){
					payment.set_type_db(3);
				} else {
					payment.set_type_db(2);
				}
				
				payment.set_date(Gen.get_pdt());
				
				payment.set_id(0);
				
				
				this.userPaymentAdapter.save(payment);
				
				Gen.display_minimalistic_text(this, this.user, payment);
				
				this.updatePaymentList();
				
				break;
			case Constants.CONTEXT_REPAY_SOME:
				// repay some //
				startActivityForResult(Intents.repay_payment(this, this.payments.get(this.selectedPayment).get_id()), Constants.ACTIVITY_REPAY_PAYMENT);
				break;
			case Constants.CONTEXT_EDIT:
				// edit //
				startActivityForResult(Intents.edit_payment(this, this.payments.get(this.selectedPayment).get_id()), Constants.ACTIVITY_EDIT_PAYMENT);
				break;
			case Constants.CONTEXT_DELETE:
				// delete //
				startActivityForResult(Intents.popup_delete(this, Constants.PAYMENT), Constants.ACTIVITY_POPUP_DELETE);
				break;
		}
		return true;
	}

	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
		switch(position){
			case 0:
				this.payments = this.userPaymentAdapter.get_by_user(this.user.get_id());
				break;
			case 1:
				this.payments = this.userPaymentAdapter.get_by_type_db(0, this.user.get_id());
				break;
			case 2:
				this.payments = this.userPaymentAdapter.get_by_type_db(1, this.user.get_id());
				break;
			case 3:
				this.payments = this.userPaymentAdapter.get_by_type_db(2, this.user.get_id());
				break;
			case 4:
				this.payments = this.userPaymentAdapter.get_by_type_db(3, this.user.get_id());
				break;
		}
		this.populate_view(false);
		
	}
	

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
}
