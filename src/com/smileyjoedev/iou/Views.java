package com.smileyjoedev.iou;

import java.util.ArrayList;

import com.smileyjoedev.genLibrary.Screen;
import com.smileyjoedev.iou.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Views {
	
	private Context context;
	private Screen screen;
	private WindowManager windowManager;
	
	/******************************************************
	 * CONSTRUCTORS
	 *****************************************************/
	
	public Views(Context context) {
		this.context = context;
		this.initialize();
	}
	
	public Views(Context context, WindowManager windowManager) {
		this.context = context;
		this.windowManager = windowManager;
		this.initialize();
	}
	
	private void initialize() {
		this.screen = new Screen(this.windowManager);
	}
	


	/******************************************************
	 * PLAYER
	 *****************************************************/

	public void userList(ArrayList<User> users, ListView lvUserList) {
		this.userList(users, lvUserList, false);
	}
	
	public void userList(ArrayList<User> users, ListView lvUserList, boolean showCheck) {
		if(this.checkListContents(users, lvUserList, Constants.USER)){
			int first = lvUserList.getFirstVisiblePosition();
			View top_child = lvUserList.getChildAt(0);
			int top;
			
			if(top_child == null){
				top = 0;
			}else{
				top = top_child.getTop();
			}
			
			UserListAdapter adapter = new UserListAdapter(this.context, users, showCheck);
			lvUserList.setAdapter(adapter);
			lvUserList.setSelectionFromTop(first, top);
		}
	}
	
	public void userList(ArrayList<User> users, LinearLayout llUserList) {
		this.userList(users, llUserList, false);
	}
	
	public void userList(ArrayList<User> users, LinearLayout llUserList, boolean showCheck) {
//		TextView tv = (TextView) llUserList.findViewById(R.id.tv_no_users);
		ListView list = (ListView) llUserList.findViewById(R.id.lv_user_list);
		if(users.size() == 0){
//			tv.setText(this.context.getText(R.string.tv_no_users).toString());
//			tv.setVisibility(View.VISIBLE);
//			list.setVisibility(View.GONE);
		} else {
			int first = list.getFirstVisiblePosition();
			View topChild = list.getChildAt(0);
			int top;
			
			if(topChild == null){
				top = 0;
			}else{
				top = topChild.getTop();
			}
			
			UserListAdapter adapter = new UserListAdapter(this.context, users, showCheck);
			list.setAdapter(adapter);
			list.setSelectionFromTop(first, top);
//			tv.setVisibility(View.GONE);
			list.setVisibility(View.VISIBLE);
		}
	}
	
	/*******************************************************
	 * GROUP
	 ******************************************************/
	
	public void groupList(ArrayList<Group> groups, ListView lvGroupList) {
		if(this.checkListContents(groups, lvGroupList, Constants.GROUP)){
			int first = lvGroupList.getFirstVisiblePosition();
			View top_child = lvGroupList.getChildAt(0);
			int top;
			
			if(top_child == null){
				top = 0;
			}else{
				top = top_child.getTop();
			}
			
			GroupListAdapter adapter = new GroupListAdapter(this.context, groups);
			lvGroupList.setAdapter(adapter);
			lvGroupList.setSelectionFromTop(first, top);
		}
	}
	
	public void groupList(ArrayList<Group> groups, LinearLayout llGroupList) {
//		TextView tv = (TextView) llGroupList.findViewById(R.id.tv_no_groups);
		ListView list = (ListView) llGroupList.findViewById(R.id.lv_group_list);
		if(groups.size() == 0){
//			tv.setText(this.context.getText(R.string.tv_no_groups).toString());
//			tv.setVisibility(View.VISIBLE);
//			list.setVisibility(View.GONE);
		} else {
			int first = list.getFirstVisiblePosition();
			View topChild = list.getChildAt(0);
			int top;
			
			if(topChild == null){
				top = 0;
			}else{
				top = topChild.getTop();
			}
			
			GroupListAdapter adapter = new GroupListAdapter(this.context, groups);
			list.setAdapter(adapter);
			list.setSelectionFromTop(first, top);
//			tv.setVisibility(View.GONE);
			list.setVisibility(View.VISIBLE);
		}
	}
	
	/*******************************************************
	 * PAYMENTS
	 ******************************************************/
	
	public UserPaymentListAdapter paymentList(ArrayList<Payment> payments, ListView lvPaymentList) {
//		if(this.check_list_contents(payments, lvPaymentList, Constants.PAYMENT)){
			int first = lvPaymentList.getFirstVisiblePosition();
			View top_child = lvPaymentList.getChildAt(0);
			int top;
			
			if(top_child == null){
				top = 0;
			}else{
				top = top_child.getTop();
			}
			
			UserPaymentListAdapter adapter = new UserPaymentListAdapter(this.context, payments);
			lvPaymentList.setAdapter(adapter);
			lvPaymentList.setSelectionFromTop(first, top);
			
//		}
			
		return adapter;
	}
	
	public void groupPaymentList(ArrayList<GroupPayment> payments, ListView lvPaymentList) {
		if(this.checkListContents(payments, lvPaymentList, Constants.PAYMENT)){
			int first = lvPaymentList.getFirstVisiblePosition();
			View top_child = lvPaymentList.getChildAt(0);
			int top;
			
			if(top_child == null){
				top = 0;
			}else{
				top = top_child.getTop();
			}
			
			GroupPaymentListAdapter adapter = new GroupPaymentListAdapter(this.context, payments);
			lvPaymentList.setAdapter(adapter);
			lvPaymentList.setSelectionFromTop(first, top);
		}
	}
	
	/******************************************************
	 * GENERAL
	 *****************************************************/
	
	public void actionGrid(ArrayList<User> users, GridView gvActionGrid) {
		ActionGridAdapter adapter = new ActionGridAdapter(this.context, users);
		gvActionGrid.setAdapter(adapter);
	}
	
	public TextView addTd(int content, int width) {
		return this.addTd(Integer.toString(content), width);
	}
	
	public TextView addTd(String content, int width) {
		TextView td = new TextView(this.context);
		td.setText(content);
		td.setWidth(width);
		td.setPadding(0, 0, 10, 0);
		td.setSingleLine(true);
		td.setEllipsize(TruncateAt.END);
		
		return td;
	}
	
	public TextView addField(int resId, int content) {
		return this.addField(this.context.getText(resId) + " " + Integer.toString(content));
	}
	
	public TextView addField(String title, int content) {
		return this.addField(title + " " + Integer.toString(content));
	}
	
	public TextView addField(int resId, String content) {
		return this.addField(Html.fromHtml("<u>" + this.context.getText(resId) + "</u> " + content));
	}
	
	public TextView addField(int resId) {
		return this.addField(Html.fromHtml("<u>" + this.context.getText(resId) + "</u>"));
	}
	
	public TextView addField(String title, String content) {
		return this.addField(title + " " + content);
	}
	
	public TextView addField(Spanned content) {
		TextView tv = new TextView(this.context);
		
		tv.setText(content);
		
		return tv;
	}
	
	public TextView addField(String content) {
		TextView tv = new TextView(this.context);
		
		tv.setText(content);
		
		return tv;
	}
	
	public TextView addTitle(String content) {
		TextView tv = this.addField(content);
		
		tv.setTextColor(Color.parseColor("#FFFFFF"));
		tv.setTextSize(20);
		
		return tv;
	}

	/***********************************************************
	 * PRIVATE
	 **********************************************************/
	
	private void emptyList(View List, int sectionId) {
		
		String message = new String();
		
		switch(sectionId){
			case Constants.USER:
				message = this.context.getText(R.string.tv_no_users).toString();
				break;
			case Constants.PAYMENT:
				message = this.context.getText(R.string.tv_no_payments).toString();
				break;
			case Constants.GROUP:
				message = this.context.getText(R.string.tv_no_groups).toString();
				break;
		}
		
		TextView tv = new TextView(this.context);
		tv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		tv.setText(message);
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(15);
		tv.setPadding(10, 0, 10, 0);
		tv.setTypeface(null, Typeface.BOLD);
		tv.setId(sectionId);
		ViewGroup parent = (ViewGroup) List.getParent();
		parent.addView(tv);
		List.setVisibility(View.GONE);
	}
	
	private void notEmptyList(View List, int sectionId) {
		try{
			ViewGroup parent = (ViewGroup) List.getParent();
			TextView tv = (TextView) parent.findViewById(sectionId);
			tv.setVisibility(View.GONE);
			List.setVisibility(View.VISIBLE);
		}catch(Exception e){
			
		}
	}
	
	private boolean checkListContents(ArrayList<?> array, View list, int sectionId) {
		boolean isNotEmpty;
		
		if(array.size() == 0){
			this.emptyList(list, sectionId);
			isNotEmpty = false;
		}else{
			this.notEmptyList(list, sectionId);
			isNotEmpty = true;
		}
		
		return isNotEmpty;
	}
	
}
