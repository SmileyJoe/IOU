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
        		this.user = this.userAdapter.getDetails(userId);
            }
            
            if(extras.containsKey("payment_id")){
            	getSupportActionBar().setTitle(R.string.bar_title_payment_edit);
            	this.isEdit = extras.getBoolean("is_edit");
            	this.isRepayment = extras.getBoolean("is_repayment");
            	this.isUser = extras.getBoolean("is_user");
            	this.payment = this.userPaymentAdapter.getDetails(extras.getInt("payment_id"));
            	this.user = this.userAdapter.getDetails(this.payment.getUserId());
            }
        } catch(NullPointerException e){
        	this.isQuickAdd = true;
        	this.users = this.userAdapter.get();
        	this.populateSpUser();
        	this.llUserSpinner.setVisibility(View.VISIBLE);
        }
		
        
        this.populateView();
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
				this.hideKeyboard();
				finish();
				return true;
			default:
	            return super.onOptionsItemSelected(item);
        }

    }
    
    private void populateSpUser(){
    	ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		for(int i = 0; i < this.users.size(); i++){
			adapter.add(this.users.get(i).getName());
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
    
    private void populateView(){
    	this.rbPaymentFrom.setText("From " + this.user.getFirstName());
    	this.rbPaymentFrom.setTextColor(Color.RED);
    	this.rbPaymentTo.setText("To " + this.user.getFirstName());
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
    	
    	switch(this.payment.getDirection()){
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
    	
    	switch(this.payment.getType()){
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
    		this.etAmount.setText(this.payment.getAmountText(false));
    		this.etDescription.setText(this.payment.getDescription());
    		this.etTitle.setText(this.payment.getTitle());
    	}
    	
    	if(this.isRepayment){
    		if(this.isUser){
    			this.etAmount.setHint(Float.toString(Math.abs(this.user.getBalance())));
    			this.etDescription.setText("Part repayment: All");
    			this.etTitle.setText("Part repayment");
    			if(this.user.getBalance() > 0){
    				this.payment.setDirection(1);
        			this.rbPaymentFrom.setChecked(true);
        		} else {
        			this.payment.setDirection(0);
        			this.rbPaymentTo.setChecked(true);
        		}
    		} else {
    			this.etAmount.setHint(Float.toString(this.payment.getAmount()));
    			this.etDescription.setText("Part repayment: " + this.payment.getDescription());
    			this.etTitle.setText("Part repayment");
    			if(this.payment.isToUser()){
        			this.rbPaymentFrom.setChecked(true);
        			this.payment.setDirection(1);
        		} else {
        			this.rbPaymentTo.setChecked(true);
        			this.payment.setDirection(0);
        		}
    		}
    		this.payment.setType(1);
    		this.rbRepayment.setChecked(true);
    	}
    	
    }

    private void hideKeyboard(){
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(this.etDescription.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(this.etAmount.getWindowToken(), 0);
    }
    
	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.bt_save:
				if(!this.etAmount.getText().toString().trim().equals("")){
					this.payment.setUserId(this.user.getId());
					this.payment.setDescription(this.etDescription.getText().toString().trim());
					this.payment.setTitle(this.etTitle.getText().toString().trim());
					this.payment.setAmount(Float.parseFloat(this.etAmount.getText().toString().trim()));
					this.payment.setDate(Gen.getPdt());
					this.payment.setTypeDb();
					
					if(this.isEdit){
						this.userPaymentAdapter.update(this.payment);
					} else {
						this.userPaymentAdapter.save(this.payment);
					}
					
					Gen.displayMinimalisticText(this, this.user, this.payment);
					

					this.hideKeyboard();
					finish();
				} else {
					Notify.toast(this, R.string.toast_no_amount);
				}
				
				break;
			case R.id.bt_cancel:
				this.hideKeyboard();
				finish();
				break;
			case R.id.rb_payment_from:
				this.payment.setDirection(1);
				break;
			case R.id.rb_payment_to:
				this.payment.setDirection(0);
				break;
			case R.id.rb_loan:
				this.payment.setType(0);
				break;
			case R.id.rb_repayment:
				this.payment.setType(1);
				break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
		this.user = this.users.get(position);
    	this.rbPaymentFrom.setText("From " + this.user.getName());
    	this.rbPaymentTo.setText("To " + this.user.getName());
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
    
    
}
