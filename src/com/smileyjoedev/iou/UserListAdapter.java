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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class UserListAdapter extends BaseAdapter {

	private ArrayList<User> users;
	private Context context;
	private boolean showCheck;
	
	public UserListAdapter(Context context, ArrayList<User> users, boolean showCheck){
		this.users = users;
		this.context = context;
		this.showCheck = showCheck;
	}
	
	public void setUsers(ArrayList<User> users){
		this.users = users;
	}
	
	@Override
	public int getCount() {
		return this.users.size();
	}

	@Override
	public Object getItem(int position) {
		return this.users.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		User user = this.users.get(position);
		Contacts cont = new Contacts(this.context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.xml.user_row, null);
		
		TextView tvUserName = (TextView) convertView.findViewById(R.id.tv_user_name);
		ImageView ivUserImage = (ImageView) convertView.findViewById(R.id.iv_user_image);
		TextView tvUserBalance = (TextView) convertView.findViewById(R.id.tv_user_balance);
		CheckBox cbUserSelected = (CheckBox) convertView.findViewById(R.id.cb_user_selected);
		View vStateIndicator = (View) convertView.findViewById(R.id.v_state_indicator);
		
		tvUserName.setText(user.getName());
		
		Gen.setUserImage(this.context, ivUserImage, user);
		
		if(this.showCheck){
			if(user.isSelected()){
				cbUserSelected.setChecked(true);
			} else {
				cbUserSelected.setChecked(false);
			}
			tvUserBalance.setVisibility(View.GONE);
		} else {
			cbUserSelected.setVisibility(View.GONE);
			if(user.getBalance() > 0){
				tvUserBalance.setTextColor(Color.GREEN);
				vStateIndicator.setBackgroundColor(Color.GREEN);
			} else {
				if(user.getBalance() < 0){
					tvUserBalance.setTextColor(Color.RED);
					vStateIndicator.setBackgroundColor(Color.RED);
				} else {
					vStateIndicator.setBackgroundColor(this.context.getResources().getColor(R.color.medium_grey));
				}
			}
			
			tvUserBalance.setText(user.getBalanceText());
		}
		
		return convertView;
	}
	
}
