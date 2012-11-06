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
		convertView = inflater.inflate(R.xml.group_row, null);
		
		TextView tvGroupTitle = (TextView) convertView.findViewById(R.id.tv_group_title);
		TextView tvGroupDescription = (TextView) convertView.findViewById(R.id.tv_group_description);
		LinearLayout llGroupDetails = (LinearLayout) convertView.findViewById(R.id.ll_group_details);
		
		tvGroupTitle.setText(group.getTitle());
		tvGroupDescription.setText(group.getDescription());
		llGroupDetails.addView(views.addField(R.string.tv_user_list_title, Gen.getUserCsv(group.getUsers())));
		
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
