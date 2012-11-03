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

public class GroupListAdapter extends BaseAdapter {

	private ArrayList<Group> groups;
	private Context context;
	
	public GroupListAdapter(Context context, ArrayList<Group> groups){
		this.groups = groups;
		this.context = context;
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
		Group group = this.groups.get(position);
		Contacts cont = new Contacts(this.context);
		Views views = new Views(this.context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.xml_group_row, null);
		
		TextView tvGroupTitle = (TextView) convertView.findViewById(R.id.tv_group_title);
		TextView tvGroupDescription = (TextView) convertView.findViewById(R.id.tv_group_description);
		LinearLayout llGroupDetails = (LinearLayout) convertView.findViewById(R.id.ll_group_details);
		
		tvGroupTitle.setText(group.get_title());
		tvGroupDescription.setText(group.get_description());
		llGroupDetails.addView(views.add_field(R.string.tv_user_list_title, Gen.get_user_csv(group.get_users())));
		
//		Gen.set_group_image(this.context, ivUserImage, user);
		
//		if(user.get_balance() > 0){
//			tvUserBalance.setTextColor(Color.GREEN);
//		} else {
//			if(user.get_balance() < 0){
//				tvUserBalance.setTextColor(Color.RED);
//			}
//		}
		
//		tvUserBalance.setText(user.get_balance_text());
		return convertView;
	}
	
}
