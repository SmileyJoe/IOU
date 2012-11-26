package com.smileyjoedev.iou;

import java.util.ArrayList;

import com.smileyjoedev.iou.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GroupUserPaymentDetails extends Activity implements OnClickListener {
	
	private DbGroupAdapter groupAdapter;
	private DbGroupPaymentAdapter groupPaymentAdapter;
	private Group group;
	private Button btOk;
	private LinearLayout llUserDetails;
//	private TextView tvGroupTotal;
//	private TextView tvGroupTotalAmount;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Gen.setTheme(this);
		setContentView(R.layout.group_user_payment_details);
		this.initialize();
		
		Bundle extras = getIntent().getExtras();
		
		int groupId = extras.getInt("group_id");
		this.group = this.groupAdapter.getDetails(groupId);
		
		this.populateView();
		
	}
	
	private void initialize() {
		this.groupAdapter = new DbGroupAdapter(this);
		this.groupPaymentAdapter = new DbGroupPaymentAdapter(this);
		this.group = new Group();
		this.btOk = (Button) findViewById(R.id.bt_ok);
		this.btOk.setOnClickListener(this);
		this.llUserDetails = (LinearLayout) findViewById(R.id.ll_user_details);
//		this.tvGroupTotal = (TextView) findViewById(R.id.tv_group_total);
//		this.tvGroupTotalAmount = (TextView) findViewById(R.id.tv_group_total_amount);
	}
	
	private void populateView() {
		ArrayList<GroupPayment> payments = this.groupPaymentAdapter.getByGroup(this.group.getId());
		
		ArrayList<GroupUserPayment> userPayments = new ArrayList<GroupUserPayment>();
		
		for(int i = 0; i < this.group.getUsers().size(); i++){
			GroupUserPayment userPayment = new GroupUserPayment(this);
			
			userPayment.setUser(this.group.getUser(i));
			
			userPayments.add(userPayment);
		}
		
		for(int i = 0; i < payments.size(); i++){
			GroupPayment payment = payments.get(i);
			
			for(int j = 0; j < payment.getSplits().size(); j++){
				for(int k = 0; k < userPayments.size(); k++){
					if(payment.getSplit(j).getUserId() == userPayments.get(k).getUser().getId()){
						if(payment.getSplit(j).isPaying()){
							userPayments.get(k).setPaid(userPayments.get(k).getPaid() + payment.getSplit(j).getAmount());
						}else{
							userPayments.get(k).setSpent(userPayments.get(k).getSpent() + payment.getSplit(j).getAmount());
						}
					}
				}
			}
		}
		
		float total = 0;
		
		for(int i = 0; i < userPayments.size(); i++){
			this.populateDetails(userPayments.get(i));
			total += userPayments.get(i).getSpent();
		}
		
//		this.tvGroupTotal.setText("Total spent by group:");
//		this.tvGroupTotalAmount.setText(Gen.getAmountText(this, total));
		
	}
	
	private void populateDetails(GroupUserPayment userPayments) {
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.xml.group_user_payment_details, null);
		
		TextView details = (TextView) view.findViewById(R.id.tv_details);
		TextView name = (TextView) view.findViewById(R.id.tv_user_name);
		TextView balance = (TextView) view.findViewById(R.id.tv_user_balance);
		
		details.setText(this.getString(R.string.tv_paid_title) + " " + userPayments.getPaidText() + ", " + this.getString(R.string.tv_spent_title) + " " + userPayments.getSpentText());
		name.setText(userPayments.getUser().getName());
		balance.setText(userPayments.getBalanceText());
		
		if(userPayments.getBalance() > 0){
			balance.setTextColor(Color.GREEN);
		} else {
			if(userPayments.getBalance() < 0){
				balance.setTextColor(Color.RED);
			}
		}
		
		this.llUserDetails.addView(view);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.bt_ok:
				finish();
				break;
		}
		
	}
	
}
