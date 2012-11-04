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
	private TextView tvGroupTotal;
	private TextView tvGroupTotalAmount;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Gen.setTheme(this);
		setContentView(R.layout.group_user_payment_details);
		this.initialize();
		
		Bundle extras = getIntent().getExtras();
		
		int groupId = extras.getInt("group_id");
		this.group = this.groupAdapter.get_details(groupId);
		
		this.populate_view();
		
	}
	
	private void initialize() {
		this.groupAdapter = new DbGroupAdapter(this);
		this.groupPaymentAdapter = new DbGroupPaymentAdapter(this);
		this.group = new Group();
		this.btOk = (Button) findViewById(R.id.bt_ok);
		this.btOk.setOnClickListener(this);
		this.llUserDetails = (LinearLayout) findViewById(R.id.ll_user_details);
		this.tvGroupTotal = (TextView) findViewById(R.id.tv_group_total);
		this.tvGroupTotalAmount = (TextView) findViewById(R.id.tv_group_total_amount);
	}
	
	private void populate_view() {
		ArrayList<GroupPayment> payments = this.groupPaymentAdapter.get_by_group(this.group.get_id());
		
		ArrayList<GroupUserPayment> userPayments = new ArrayList<GroupUserPayment>();
		
		for(int i = 0; i < this.group.get_users().size(); i++){
			GroupUserPayment userPayment = new GroupUserPayment();
			
			userPayment.set_user(this.group.get_user(i));
			
			userPayments.add(userPayment);
		}
		
		for(int i = 0; i < payments.size(); i++){
			GroupPayment payment = payments.get(i);
			
			for(int j = 0; j < payment.get_splits().size(); j++){
				for(int k = 0; k < userPayments.size(); k++){
					if(payment.get_split(j).get_user_id() == userPayments.get(k).get_user().get_id()){
						if(payment.get_split(j).is_paying()){
							userPayments.get(k).set_paid(userPayments.get(k).get_paid() + payment.get_split(j).get_amount());
						}else{
							userPayments.get(k).set_spent(userPayments.get(k).get_spent() + payment.get_split(j).get_amount());
						}
					}
				}
			}
		}
		
		float total = 0;
		
		for(int i = 0; i < userPayments.size(); i++){
			this.populate_details(userPayments.get(i));
			total += userPayments.get(i).get_spent();
		}
		
		this.tvGroupTotal.setText("Total spent by group:");
		this.tvGroupTotalAmount.setText(Gen.get_amount_text(total));
		
	}
	
	private void populate_details(GroupUserPayment userPayments) {
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.xml.group_user_payment_details, null);
		
		TextView details = (TextView) view.findViewById(R.id.tv_details);
		TextView name = (TextView) view.findViewById(R.id.tv_user_name);
		TextView balance = (TextView) view.findViewById(R.id.tv_user_balance);
		
		details.setText("Paid " + userPayments.get_paid_text() + ", Spent " + userPayments.get_spent_text());
		name.setText(userPayments.get_user().get_name());
		balance.setText(userPayments.get_balance_text());
		
		if(userPayments.get_balance() > 0){
			balance.setTextColor(Color.GREEN);
		} else {
			if(userPayments.get_balance() < 0){
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
