/********************************************
 * Adapter to build the ActionGrid as used
 * on the main dash
 *******************************************/

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

public class ActionGridAdapter extends BaseAdapter {

	private ArrayList<QuickAction> quickActions;
	private Context context;
	
	public ActionGridAdapter(Context context, ArrayList<QuickAction> quickActions){
		this.quickActions = quickActions;
		this.context = context;
	}
	
	public void setQuickActions(ArrayList<QuickAction> quickActions){
		this.quickActions = quickActions;
	}
	
	@Override
	public int getCount() {
		return this.quickActions.size();
	}

	@Override
	public Object getItem(int position) {
		return this.quickActions.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		QuickAction quickAction = this.quickActions.get(position);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.xml.action_grid_item, parent, false);
		
		TextView tvUserName = (TextView) convertView.findViewById(R.id.tv_user_name);
		TextView tvAction = (TextView) convertView.findViewById(R.id.tv_action);
		ImageView ivUserImage = (ImageView) convertView.findViewById(R.id.iv_user_image);
		
		tvUserName.setText(quickAction.getTitle());
		tvAction.setText(quickAction.getActionText());
		switch(quickAction.getType()){
			case QuickAction.TYPE_USER:
				Gen.setActionImage(this.context, ivUserImage, (User) quickAction.getTargetData());
				break;
			case QuickAction.TYPE_GROUP:
				ivUserImage.setImageResource(R.drawable.default_group_large);
				break;
		}
		
		
		return convertView;
	}
	
}
