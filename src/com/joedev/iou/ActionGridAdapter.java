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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ActionGridAdapter extends BaseAdapter {

	private ArrayList<User> users;
	private Context context;
	
	public ActionGridAdapter(Context context, ArrayList<User> users){
		this.users = users;
		this.context = context;
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
		convertView = inflater.inflate(R.xml.action_grid_item, parent, false);
		
		TextView tvUserName = (TextView) convertView.findViewById(R.id.tv_user_name);
		TextView tvAction = (TextView) convertView.findViewById(R.id.tv_action);
		ImageView ivUserImage = (ImageView) convertView.findViewById(R.id.iv_user_image);
		
		tvUserName.setText(user.get_name());
		tvAction.setText("View");
		
		Gen.set_action_image(this.context, ivUserImage, user);
		
		return convertView;
	}
	
}
