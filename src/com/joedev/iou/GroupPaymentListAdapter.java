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

public class GroupPaymentListAdapter extends BaseAdapter {

	private ArrayList<GroupPayment> payments;
	private Context context;
	private Views views;
	
	public GroupPaymentListAdapter(Context context, ArrayList<GroupPayment> payments){
		this.payments = payments;
		this.context = context;
		this.views = new Views(this.context);
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
		convertView = inflater.inflate(R.layout.xml_group_payment_row, null);
		
		TextView tvPaymentTitle = (TextView) convertView.findViewById(R.id.tv_payment_title);
		TextView tvPaymentDescription = (TextView) convertView.findViewById(R.id.tv_payment_description);
		LinearLayout llGroupDetails = (LinearLayout) convertView.findViewById(R.id.ll_payment_details);
		
		tvPaymentTitle.setText(payment.get_title());
		tvPaymentDescription.setText(payment.get_description());
		
		llGroupDetails.addView(this.views.add_field(R.string.date_title, Gen.convert_pdt(payment.get_pdt(), false)));
		llGroupDetails.addView(this.views.add_field(R.string.tv_payment_description, payment.get_description()));
		
		llGroupDetails.addView(this.views.add_field(R.string.tv_paying_title));
		llGroupDetails.addView(this.views.add_field(payment.get_paying_csv()));
		
		llGroupDetails.addView(this.views.add_field(R.string.tv_payed_for_title));
		llGroupDetails.addView(this.views.add_field(payment.get_paid_for_csv()));
		
		return convertView;
	}
	
}
