package com.smileyjoedev.iou;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.smileyjoedev.genLibrary.Notify;
import com.smileyjoedev.iou.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

public class PaymentNew extends SherlockActivity implements OnClickListener, OnItemSelectedListener {
	
	private User user;
	private DbUserAdapter userAdapter;
	private DbUserPaymentAdapter userPaymentAdapter;
	private Payment payment;
	private Button btSave;
	private Button btCancel;
	private EditText etTitle;
	private EditText etDescription;
	private EditText etAmount;
	private RadioButton rbPaymentFrom;
	private RadioButton rbPaymentTo;
	private RadioButton rbLoan;
	private RadioButton rbRepayment;
	private boolean isEdit;
	private boolean isRepayment;
	private boolean isUser;
	private boolean isQuickAdd;
	private ArrayList<User> users;
	private LinearLayout llUserSpinner;
	private Spinner spUserSpinner;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gen.setTheme(this);
        setContentView(R.layout.payment_new);
        this.initialize();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try{
        	Bundle extras = getIntent().getExtras();
    		
            if(extras.containsKey("user_id")){
            	int userId = extras.getInt("user_id");
        		this.user = this.userAdapter.get_details(userId);
            }
            
            if(extras.containsKey("payment_id")){
            	getSupportActionBar().setTitle(R.string.bar_title_payment_edit);
            	this.isEdit = extras.getBoolean("is_edit");
            	this.isRepayment = extras.getBoolean("is_repayment");
            	this.isUser = extras.getBoolean("is_user");
            	this.payment = this.userPaymentAdapter.get_details(extras.getInt("payment_id"));
            	this.user = this.userAdapter.get_details(this.payment.get_user_id());
            }
        } catch(NullPointerException e){
        	this.isQuickAdd = true;
        	this.users = this.userAdapter.get();
        	this.populate_sp_user();
        	this.llUserSpinner.setVisibility(View.VISIBLE);
        }
		
        
        this.populate_view();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.payment_new, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
			case android.R.id.home:
				this.hide_keyboard();
				finish();
				return true;
			default:
	            return super.onOptionsItemSelected(item);
        }

    }
    
    private void populate_sp_user(){
    	ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		for(int i = 0; i < this.users.size(); i++){
			adapter.add(this.users.get(i).get_name());
		}
		
		this.spUserSpinner.setAdapter(adapter);
    }
    
    private void initialize(){
    	this.user = new User();
    	this.userAdapter = new DbUserAdapter(this);
    	this.userPaymentAdapter = new DbUserPaymentAdapter(this);
    	this.payment = new Payment();
    	this.btSave = (Button) findViewById(R.id.bt_save);
    	this.btSave.setOnClickListener(this);
    	this.btCancel = (Button) findViewById(R.id.bt_cancel);
    	this.btCancel.setOnClickListener(this);
    	this.etTitle = (EditText) findViewById(R.id.et_payment_title);
    	this.etDescription = (EditText) findViewById(R.id.et_payment_description);
    	this.etAmount = (EditText) findViewById(R.id.et_payment_amount);
    	this.rbPaymentFrom = (RadioButton) findViewById(R.id.rb_payment_from);
    	this.rbPaymentFrom.setOnClickListener(this);
    	this.rbPaymentTo = (RadioButton) findViewById(R.id.rb_payment_to);
    	this.rbPaymentTo.setOnClickListener(this);
    	this.rbLoan = (RadioButton) findViewById(R.id.rb_loan);
    	this.rbLoan.setOnClickListener(this);
    	this.rbRepayment = (RadioButton) findViewById(R.id.rb_repayment);
    	this.rbRepayment.setOnClickListener(this);
    	this.isEdit = false;
    	this.isRepayment = false;
    	this.isUser = false;
    	this.isQuickAdd = false;
    	this.users = new ArrayList<User>();
    	this.llUserSpinner = (LinearLayout) findViewById(R.id.ll_user_spinner);
    	this.spUserSpinner = (Spinner) findViewById(R.id.sp_user_spinner);
    	this.spUserSpinner.setOnItemSelectedListener(this);
    }
    
    private void populate_view(){
    	this.rbPaymentFrom.setText("From " + this.user.get_first_name());
    	this.rbPaymentFrom.setTextColor(Color.RED);
    	this.rbPaymentTo.setText("To " + this.user.get_first_name());
    	this.rbPaymentTo.setTextColor(Color.GREEN);
    	
//    	switch(this.payment.get_type()){
//    		case 0:
//    			this.rbLoan.setChecked(true);
//    			this.rbPaymentTo.setChecked(true);
//    			break;
//    		case 1:
//    			this.rbLoan.setChecked(true);
//    			this.rbPaymentFrom.setChecked(true);
//    			break;
//    		case 2:
//    			this.rbRepayment.setChecked(true);
//    			this.rbPaymentTo.setChecked(true);
//    			break;
//    		case 3:
//    			this.rbRepayment.setChecked(true);
//    			this.rbPaymentFrom.setChecked(true);
//    			break;
//    	}
    	
    	switch(this.payment.get_direction()){
    		case 0:
    			this.rbPaymentTo.setChecked(true);
    			break;
    		case 1:
    			this.rbPaymentFrom.setChecked(true);
    			break;
    		default:
    			this.rbPaymentTo.setChecked(true);
    			break;
    	}
    	
    	switch(this.payment.get_type()){
    		case 0:
    			this.rbLoan.setChecked(true);
    			break;
    		case 1:
    			this.rbRepayment.setChecked(true);
    			break;
    		default:
    			this.rbLoan.setChecked(true);
    			break;
    	}
    	
    	if(this.isEdit){
    		this.etAmount.setText(this.payment.get_amount_text(false));
    		this.etDescription.setText(this.payment.get_description());
    		this.etTitle.setText(this.payment.get_title());
    	}
    	
    	if(this.isRepayment){
    		if(this.isUser){
    			this.etAmount.setHint(Float.toString(Math.abs(this.user.get_balance())));
    			this.etDescription.setText("Part repayment: All");
    			this.etTitle.setText("Part repayment");
    			if(this.user.get_balance() > 0){
    				this.payment.set_direction(1);
        			this.rbPaymentFrom.setChecked(true);
        		} else {
        			this.payment.set_direction(0);
        			this.rbPaymentTo.setChecked(true);
        		}
    		} else {
    			this.etAmount.setHint(Float.toString(this.payment.get_amount()));
    			this.etDescription.setText("Part repayment: " + this.payment.get_description());
    			this.etTitle.setText("Part repayment");
    			if(this.payment.is_to_user()){
        			this.rbPaymentFrom.setChecked(true);
        			this.payment.set_direction(1);
        		} else {
        			this.rbPaymentTo.setChecked(true);
        			this.payment.set_direction(0);
        		}
    		}
    		this.payment.set_type(1);
    		this.rbRepayment.setChecked(true);
    	}
    	
    }

    private void hide_keyboard(){
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(this.etDescription.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(this.etAmount.getWindowToken(), 0);
    }
    
	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.bt_save:
				if(!this.etAmount.getText().toString().trim().equals("")){
					this.payment.set_user_id(this.user.get_id());
					this.payment.set_description(this.etDescription.getText().toString().trim());
					this.payment.set_title(this.etTitle.getText().toString().trim());
					this.payment.set_amount(Float.parseFloat(this.etAmount.getText().toString().trim()));
					this.payment.set_date(Gen.get_pdt());
					this.payment.set_type_db();
					
					if(this.isEdit){
						this.userPaymentAdapter.update(this.payment);
					} else {
						this.userPaymentAdapter.save(this.payment);
					}
					
					Gen.display_minimalistic_text(this, this.user, this.payment);
					

					this.hide_keyboard();
					finish();
				} else {
					Notify.toast(this, R.string.toast_no_amount);
				}
				
				break;
			case R.id.bt_cancel:
				this.hide_keyboard();
				finish();
				break;
			case R.id.rb_payment_from:
				this.payment.set_direction(1);
				break;
			case R.id.rb_payment_to:
				this.payment.set_direction(0);
				break;
			case R.id.rb_loan:
				this.payment.set_type(0);
				break;
			case R.id.rb_repayment:
				this.payment.set_type(1);
				break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
		this.user = this.users.get(position);
    	this.rbPaymentFrom.setText("From " + this.user.get_name());
    	this.rbPaymentTo.setText("To " + this.user.get_name());
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
    
    
}
