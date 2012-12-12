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
	 * USER
	 *****************************************************/

	public UserListAdapter userList(ArrayList<User> users, LinearLayout llUserList) {
		return this.userList(users, llUserList, false);
	}
	
	public UserListAdapter userList(ArrayList<User> users, LinearLayout wrapper, boolean showCheck) {
		ListView list = (ListView) wrapper.findViewById(R.id.lv_user_list);
		TextView emptyView = (TextView) wrapper.findViewById(R.id.tv_user_list_empty);
		
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
		list.setEmptyView(emptyView);
		
		
		return adapter;
	}
	
	/*******************************************************
	 * GROUP
	 ******************************************************/
	
	public GroupListAdapter groupList(ArrayList<Group> groups, LinearLayout wrapper) {
		ListView list = (ListView) wrapper.findViewById(R.id.lv_group_list);
		TextView emptyView = (TextView) wrapper.findViewById(R.id.tv_group_list_empty);
		
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
		list.setEmptyView(emptyView);
		
		return adapter;
	}
	
	/*******************************************************
	 * PAYMENTS
	 ******************************************************/
	
	public UserPaymentListAdapter paymentList(ArrayList<Payment> payments, LinearLayout wrapper) {
		
		ListView list = (ListView) wrapper.findViewById(R.id.lv_payment_list);
		TextView emptyView = (TextView) wrapper.findViewById(R.id.tv_user_payment_list_empty);
		
		int first = list.getFirstVisiblePosition();
		View top_child = list.getChildAt(0);
		int top;
		
		if(top_child == null){
			top = 0;
		}else{
			top = top_child.getTop();
		}
		
		UserPaymentListAdapter adapter = new UserPaymentListAdapter(this.context, payments);
		list.setAdapter(adapter);
		list.setSelectionFromTop(first, top);
		list.setEmptyView(emptyView);
		
		return adapter;
	}
	
	public GroupPaymentListAdapter groupPaymentList(ArrayList<GroupPayment> payments, LinearLayout wrapper) {
		
		ListView list = (ListView) wrapper.findViewById(R.id.lv_payment_list);
		TextView emptyView = (TextView) wrapper.findViewById(R.id.tv_payment_list_empty);
		
		int first = list.getFirstVisiblePosition();
		View top_child = list.getChildAt(0);
		int top;
		
		if(top_child == null){
			top = 0;
		}else{
			top = top_child.getTop();
		}
		
		GroupPaymentListAdapter adapter = new GroupPaymentListAdapter(this.context, payments);
		list.setAdapter(adapter);
		list.setSelectionFromTop(first, top);
		list.setEmptyView(emptyView);
		
		return adapter;
	}
	
	/******************************************************
	 * GENERAL
	 *****************************************************/
	
	public ActionGridAdapter actionGrid(ArrayList<QuickAction> quickActions, GridView gvActionGrid) {
		ActionGridAdapter adapter = new ActionGridAdapter(this.context, quickActions);
		gvActionGrid.setAdapter(adapter);
		return adapter;
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
	
}
