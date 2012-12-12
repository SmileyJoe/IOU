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

public class GroupListAdapter extends BaseAdapter {

	private ArrayList<Group> groups;
	private Context context;
	
	public GroupListAdapter(Context context, ArrayList<Group> groups){
		this.groups = groups;
		this.context = context;
	}
	
	public void setGroups(ArrayList<Group> groups){
		this.groups = groups;
	}
	
	@Override
	public int getCount() {
		return this.groups.size();
	}

	@Override
	public Object getItem(int position) {
		return this.groups.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = GroupListAdapter.populateView(this.context, this.groups.get(position));
		
		return convertView;
	}
	
	public static View populateView(Context context, Group group){
		DbGroupPaymentAdapter groupPaymentAdapter = new DbGroupPaymentAdapter(context);
		ArrayList<GroupPayment> payments = groupPaymentAdapter.getByGroup(group.getId());
		ArrayList<Payment> userPayments = Gen.getUserPayments(context, group, payments);
        
		Contacts cont = new Contacts(context);
		Views views = new Views(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View convertView = inflater.inflate(R.xml.group_row, null);
		
		TextView tvGroupTitle = (TextView) convertView.findViewById(R.id.tv_group_title);
		TextView tvGroupDescription = (TextView) convertView.findViewById(R.id.tv_group_description);
		TextView tvGroupUsers = (TextView) convertView.findViewById(R.id.tv_group_users);
		LinearLayout llGroupImageWrapper = (LinearLayout) convertView.findViewById(R.id.ll_group_image_wrapper);
		View vStateIndicator = (View) convertView.findViewById(R.id.v_state_indicator);
		
		if(userPayments.size() > 0){
    		ArrayList<GroupRepayment> repayments = Gen.sortGroupRepayments(context, userPayments);
    		
    		if(repayments.size() > 0){
    			tvGroupTitle.setTextColor(context.getResources().getColor(R.color.red));
    			vStateIndicator.setBackgroundColor(context.getResources().getColor(R.color.red));
    		}
    	}
		
		Gen.setGroupImage(context, group.getUsers(), llGroupImageWrapper);
		
		tvGroupTitle.setText(group.getTitle());
		
		if(!group.getDescription().equals("")){
			tvGroupDescription.setText(group.getDescription());
		} else {
			tvGroupDescription.setVisibility(View.GONE);
		}
		
		tvGroupUsers.setText(Gen.getUserCsv(group.getUsers()));
		
		return convertView;
	}
	
}
