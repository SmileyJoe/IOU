package com.joedev.iou;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PaymentListAdapter extends BaseAdapter {

	private ArrayList<Payment> payments;
	private Context context;
	
	public PaymentListAdapter(Context context, ArrayList<Payment> payments){
		this.payments = payments;
		this.context = context;
	}
	
	public void setPayments(ArrayList<Payment> payments){
		this.payments = payments;
	}
	
	@Override
	public int getCount() {
		return this.payments.size();
	}

	@Override
	public Object getItem(int position) {
		return this.payments.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Views views = new Views(this.context);
		Payment payment = this.payments.get(position);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.xml_payment_row, null);
		
		TextView tvPaymentTitle = (TextView) convertView.findViewById(R.id.tv_payment_title);
		TextView tvPaymentDate = (TextView) convertView.findViewById(R.id.tv_payment_date);
		TextView tvPaymentDescription = (TextView) convertView.findViewById(R.id.tv_payment_description);
		TextView tvPaymentAmount = (TextView) convertView.findViewById(R.id.tv_payment_amount);
		LinearLayout llPaymentDetails = (LinearLayout) convertView.findViewById(R.id.ll_payment_details);
		View vStateIndicator = (View) convertView.findViewById(R.id.v_state_indicator);
		
		if(payment.get_title().equals("")){
			Debug.v("No Title");
			tvPaymentTitle.setVisibility(View.GONE);
		} else {
			Debug.v("Title");
			tvPaymentTitle.setText(payment.get_title());
		}
		
		if(payment.get_description().equals("")){
			tvPaymentDescription.setVisibility(View.GONE);
		} else {
			tvPaymentDescription.setText(payment.get_description());
		}
		
		if(payment.is_to_user()){
			tvPaymentAmount.setTextColor(Color.GREEN);
			vStateIndicator.setBackgroundColor(Color.GREEN);
		} else {
			tvPaymentAmount.setTextColor(Color.RED);
			vStateIndicator.setBackgroundColor(Color.RED);
		}
		
		tvPaymentDate.setText(payment.get_date_text(true));
		tvPaymentAmount.setText(payment.get_amount_text(true));
		
		return convertView;
	}
	
}
