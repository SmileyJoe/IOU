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
		this.group = this.groupAdapter.get_details(groupId);
		
        this.populate_view();
        
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
    
    private void populate_view(){
    	this.payments = this.groupPaymentAdapter.get_by_group(this.group.get_id());
    	this.get_repayments();
    	
    	if(this.repayments.size() > 0){
        	for(int i = 0; i < this.repayments.size(); i++){
        		this.populate_details(this.repayments.get(i), i);
        	}    		
    	} else {
    		this.populate_empty_details();
    		this.disable_button(this.btRepaySelected);
    	}
    	
    	if(this.selectedCount > 0){
			this.enable_button(this.btRepaySelected);
		} else {
			this.disable_button(this.btRepaySelected);
		}
    }
    
    private void disable_button(Button button){
    	// TODO: Change button appearence to show disabled
//    	Debug.v("Disable");
    	button.setClickable(false);
//    	button.setTextColor(R.color.button_disabled_text);
    }
    
    private void enable_button(Button button){
//    	Debug.v("Enable");
    	button.setClickable(true);
//    	button.setTextColor(R.color.button_text);
//    	button.invalidate();
//    	button.refreshDrawableState();
    }
    
    private void populate_empty_details(){
    	TextView tv = new TextView(this);
    	tv.setText(this.getString(R.string.tv_no_repayments));
    	this.llRepaymentDetails.addView(tv);
    }
    
    private void populate_details(GroupRepayment repayment, int position){
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
    
    private void get_repayments(){
    	ArrayList<Payment> userPayments = Gen.get_user_payments(this.group, this.payments);
        
    	if(userPayments.size() > 0){
    		this.repayments = Gen.sort_group_repayments(userPayments);
    	}
        
    }

    private void repay_user(GroupRepayment repayment){
    	GroupPayment payment = new GroupPayment();
    	ArrayList<PaymentSplit> splits = new ArrayList<PaymentSplit>();
    	PaymentSplit split = new PaymentSplit();
    	
    	payment.set_amount(repayment.get_amount());
    	payment.set_description(this.getString(R.string.repayment_description));
    	payment.set_group_id(this.group.get_id());
    	payment.set_pdt(Gen.get_pdt());
    	
    	split.set_amount(repayment.get_amount());
    	split.set_type(0);
    	split.set_user(repayment.get_owing_user());
    	split.set_user_Id(repayment.get_owing_user().get_id());
    	
    	splits.add(split);
    	
    	split = new PaymentSplit();
    	
    	split.set_amount(repayment.get_amount());
    	split.set_type(1);
    	split.set_user(repayment.get_owed_user());
    	split.set_user_Id(repayment.get_owed_user().get_id());
    	
    	splits.add(split);
    	
		payment.set_splits(splits);
		
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
					if(this.repayments.get(i).get_selected()){
						this.repay_user(this.repayments.get(i));
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
					this.repayments.get(Integer.parseInt(buttonView.getTag().toString())).set_selected(true);
					this.selectedCount++;
				} else {
					this.repayments.get(Integer.parseInt(buttonView.getTag().toString())).set_selected(false);
					this.selectedCount--;
				}
				
				
				if(this.selectedCount > 0){
					this.enable_button(this.btRepaySelected);
				} else {
					this.disable_button(this.btRepaySelected);
				}
				break;
		}
	}
	
}
