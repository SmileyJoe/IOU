package com.smileyjoedev.iou;

import java.util.ArrayList;

import com.smileyjoedev.iou.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GroupPayback extends Activity implements OnClickListener, OnCheckedChangeListener {
	
	private ArrayList<GroupPayment> payments;
	private DbGroupAdapter groupAdapter;
	private DbGroupPaymentAdapter groupPaymentAdapter;
	private Group group;
	private ArrayList<GroupRepayment> repayments;
	private LinearLayout llRepaymentDetails;
	private Views views;
	private Button btOk;
	private Button btRepaySelected;
	private int selectedCount;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gen.setTheme(this);
        setContentView(R.layout.group_payback);
        this.initialize();
        
        Bundle extras = getIntent().getExtras();
		
		int groupId = extras.getInt("group_id");
		this.group = this.groupAdapter.getDetails(groupId);
		
        this.populateView();
        
    }
    
    private void initialize(){
    	this.groupAdapter = new DbGroupAdapter(this);
    	this.groupPaymentAdapter = new DbGroupPaymentAdapter(this);
    	this.group = new Group();
    	this.payments = new ArrayList<GroupPayment>();
    	this.repayments = new ArrayList<GroupRepayment>();
    	this.llRepaymentDetails = (LinearLayout) findViewById(R.id.ll_repayment_details);
    	this.views = new Views(this);
    	this.btOk = (Button) findViewById(R.id.bt_ok);
    	this.btOk.setOnClickListener(this);
    	this.btRepaySelected = (Button) findViewById(R.id.bt_repay_selected);
    	this.btRepaySelected.setOnClickListener(this);
    	this.selectedCount = 0;
    }
    
    private void populateView(){
    	this.payments = this.groupPaymentAdapter.getByGroup(this.group.getId());
    	this.getRepayments();
    	
    	if(this.repayments.size() > 0){
        	for(int i = 0; i < this.repayments.size(); i++){
        		this.populateDetails(this.repayments.get(i), i);
        	}    		
    	} else {
    		this.populateEmptyDetails();
    		this.disableButton(this.btRepaySelected);
    	}
    	
    	if(this.selectedCount > 0){
			this.enableButton(this.btRepaySelected);
		} else {
			this.disableButton(this.btRepaySelected);
		}
    }
    
    private void disableButton(Button button){
    	button.setClickable(false);
    }
    
    private void enableButton(Button button){
    	button.setClickable(true);
    }
    
    private void populateEmptyDetails(){
    	TextView tv = new TextView(this);
    	tv.setText(this.getString(R.string.tv_no_repayments));
    	this.llRepaymentDetails.addView(tv);
    }
    
    private void populateDetails(GroupRepayment repayment, int position){
    	LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.xml.group_payback_user, null);
		
		RelativeLayout wrapper = (RelativeLayout) view.findViewById(R.id.rl_repayment_wrapper);
		CheckBox cbSelected = (CheckBox) view.findViewById(R.id.cb_repayment_selected);
		TextView details = (TextView) view.findViewById(R.id.tv_details);
		
		wrapper.setOnClickListener(this);
		cbSelected.setOnCheckedChangeListener(this);
		cbSelected.setTag(position);
		details.setText(repayment.toString());
		
		this.llRepaymentDetails.addView(view);
    }
    
    private void getRepayments(){
    	ArrayList<Payment> userPayments = Gen.getUserPayments(this.group, this.payments);
        
    	if(userPayments.size() > 0){
    		this.repayments = Gen.sortGroupRepayments(userPayments);
    	}
        
    }

    private void repayUser(GroupRepayment repayment){
    	GroupPayment payment = new GroupPayment();
    	ArrayList<PaymentSplit> splits = new ArrayList<PaymentSplit>();
    	PaymentSplit split = new PaymentSplit();
    	
    	payment.setAmount(repayment.getAmount());
    	payment.setDescription(this.getString(R.string.repayment_description));
    	payment.setGroupId(this.group.getId());
    	payment.setPdt(Gen.getPdt());
    	
    	split.setAmount(repayment.getAmount());
    	split.setType(0);
    	split.setUser(repayment.getOwingUser());
    	split.setUserId(repayment.getOwingUser().getId());
    	
    	splits.add(split);
    	
    	split = new PaymentSplit();
    	
    	split.setAmount(repayment.getAmount());
    	split.setType(1);
    	split.setUser(repayment.getOwedUser());
    	split.setUserId(repayment.getOwedUser().getId());
    	
    	splits.add(split);
    	
		payment.setSplits(splits);
		
		this.groupPaymentAdapter.save(payment);
    }
    
	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.bt_ok:
				finish();
				break;
			case R.id.bt_repay_selected:
				for(int i = 0; i < this.repayments.size(); i++){
					if(this.repayments.get(i).getSelected()){
						this.repayUser(this.repayments.get(i));
					}
				}
				finish();
				break;
			case R.id.rl_repayment_wrapper:
				CheckBox cbSelected = (CheckBox) v.findViewById(R.id.cb_repayment_selected);
				cbSelected.performClick();
				break;
		}
		
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch(buttonView.getId()){
			case R.id.cb_repayment_selected:
				if(isChecked){
					this.repayments.get(Integer.parseInt(buttonView.getTag().toString())).setSelected(true);
					this.selectedCount++;
				} else {
					this.repayments.get(Integer.parseInt(buttonView.getTag().toString())).setSelected(false);
					this.selectedCount--;
				}
				
				
				if(this.selectedCount > 0){
					this.enableButton(this.btRepaySelected);
				} else {
					this.disableButton(this.btRepaySelected);
				}
				break;
		}
	}
	
}
