package com.smileyjoedev.iou;

import java.util.ArrayList;

import com.smileyjoedev.genLibrary.Contacts;
import com.smileyjoedev.iou.R;

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

public class GroupPaymentListAdapter extends BaseAdapter {

	private ArrayList<GroupPayment> payments;
	private Context context;
	private Views views;
	
	public GroupPaymentListAdapter(Context context, ArrayList<GroupPayment> payments){
		this.payments = payments;
		this.context = context;
		this.views = new Views(this.context);
	}
	
	public void setPayments(ArrayList<GroupPayment> payments){
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
		GroupPayment payment = this.payments.get(position);
		Contacts cont = new Contacts(this.context);
		Views views = new Views(this.context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.xml.group_payment_row, null);
		
		TextView tvPaymentTitle = (TextView) convertView.findViewById(R.id.tv_payment_title);
		TextView tvPaymentDate = (TextView) convertView.findViewById(R.id.tv_payment_date);
		TextView tvPaymentDescription = (TextView) convertView.findViewById(R.id.tv_payment_description);
		TextView tvUsersPaid = (TextView) convertView.findViewById(R.id.tv_users_paid);
		TextView tvUsersPaidFor = (TextView) convertView.findViewById(R.id.tv_users_paid_for);
		
		if(!payment.getTitle().equals("")){
			tvPaymentTitle.setText(payment.getTitle());
		} else {
			tvPaymentTitle.setVisibility(View.GONE);
		}
		
		tvPaymentDate.setText(Gen.convertPdt(payment.getPdt(), true));
		
		if(!payment.getDescription().equals("")){
			tvPaymentDescription.setText(payment.getDescription());
		} else {
			tvPaymentDescription.setVisibility(View.GONE);
		}
		
		tvUsersPaid.setText(payment.getPayingCsv());
		tvUsersPaidFor.setText(payment.getPaidForCsv());
		
		return convertView;
	}
	
}
