package com.smileyjoedev.iou;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;

public class QuickAction {

	/*********************************************************
	 * VARIABLES
	 ********************************************************/
	
	private long id;
	private int action;
	private int type;
	private int targetId;
	private Object targetData;
	private Context context;
	private DbUserAdapter userAdapter;
	private DbGroupAdapter groupAdapter;
	
	/*********************************************************
	 * CONSTANTS
	 ********************************************************/
	
	public static final int TYPE_USER = 1;
	public static final int TYPE_GROUP = 2;
	
	public static final int ACTION_VIEW_SPECIFIC = 1;
	public static final int ACTION_VIEW_ALL = 2;
	public static final int ACTION_PAYMENT_NEW = 3;
	
	/*********************************************************
	 * CONSTRUCTOR
	 ********************************************************/
	
	public QuickAction(Context context){
		this.context = context;
		this.id = 0;
		this.action = 0;
		this.type = 0;
		this.targetId = 0;
		this.userAdapter = new DbUserAdapter(this.context);
		this.groupAdapter = new DbGroupAdapter(this.context);
	}
	
	/*********************************************************
	 * SETTERS
	 ********************************************************/
	
	public void setId(long id){
		this.id = id;
	}
	
	public void setAction(int action) {
		this.action = action;
		
		switch(this.action){
			case ACTION_VIEW_ALL:
				this.setTargetId(0);
				break;
			default:
				break;
		}
	}
	
	public void setType(int type) {
		this.type = type;
		this.setTargetId(0);
	}
	
	public void setTargetId(int targetId) {
		this.targetId = targetId;
		switch(this.getType()){
			case TYPE_USER:
				this.setTargetData(this.userAdapter.getDetails(this.targetId));
				break;
			case TYPE_GROUP:
				this.setTargetData(this.groupAdapter.getDetails(this.targetId));
				break;
			default:
				this.setTargetData(new Object());
				break;
		}
		
	}
	
	public void setTargetData(Object data){
		this.targetData = data;
	}
	
	/**********************************************************
	 * GETTERS
	 *********************************************************/
	
	public long getId(){
		return this.id;
	}
	
	public int getAction() {
		return action;
	}
	
	public int getType() {
		return type;
	}
	
	public int getTargetId() {
		return targetId;
	}
	
	public Object getTargetData(){
		return this.targetData;
	}
	
	public Intent getIntent(){
		Intent intent = new Intent();
		
		switch(this.getAction()){
			case ACTION_VIEW_SPECIFIC:
				switch(this.getType()){
					case TYPE_USER:
						intent = Intents.userView(this.context, this.getTargetId());
						break;
					case TYPE_GROUP:
						intent = Intents.groupView(this.context, this.getTargetId());
						break;
				}
				break;
			case ACTION_VIEW_ALL:
				switch(this.getType()){
					case TYPE_USER:
						intent = Intents.userList(this.context);
						break;
					case TYPE_GROUP:
						intent = Intents.groupList(this.context);
						break;
				}
				break;
			case ACTION_PAYMENT_NEW:
				switch(this.getType()){
					case TYPE_USER:
						intent = Intents.newPayment(this.context, this.getTargetId());
						break;
					case TYPE_GROUP:
						intent = Intents.groupPaymentNew(this.context, this.getTargetId());
						break;
				}
				break;
		}
		
		return intent;
	}
	
	public String getTypeText(){
		String text = "";
		
		switch(this.getType()){
			case TYPE_USER:
				text = this.context.getString(R.string.quick_action_type_user_title);
				break;
			case TYPE_GROUP:
				text = this.context.getString(R.string.quick_action_type_group_title);
				break;
		}
		
		return text;
	}
	
	public String getActionText(){
		String text = "";
		
		switch(this.getAction()){
			case ACTION_VIEW_SPECIFIC:
				text = this.context.getString(R.string.quick_action_action_view_specific_title);
				break;
			case ACTION_VIEW_ALL:
				text = this.context.getString(R.string.quick_action_action_view_all_title);
				break;
			case ACTION_PAYMENT_NEW:
				text = this.context.getString(R.string.quick_action_action_payment_add_title);
				break;
		}
		
		return text;
	}
	
	public String getTitle(){
		String title = "";
		
		if(this.getTargetId() > 0){
			switch(this.getType()){
				case TYPE_USER:
					title = ((User) this.getTargetData()).getName();
					break;
				case TYPE_GROUP:
					title = ((Group) this.getTargetData()).getTitle();
					break;
				default:
					title = this.context.getString(R.string.tv_no_target).toString();
					break;
			}
		} else {
			switch(this.getAction()){
				case ACTION_VIEW_ALL:
					title = this.getTypeText();
					break;
				default:
					title = this.context.getString(R.string.tv_no_target).toString();
					break;
			}
			
		}
		
		
		return title;
	}
	
	public String getDescription(){
		return this.getActionText() + ":" + this.getTitle();
	}

	/**********************************************************
	 * GENERAL
	 *********************************************************/
	
	@Override
	public String toString() {
		return "QuickAction [getAction()=" + getAction() + ", getType()="
				+ getType() + ", getTargetId()=" + getTargetId()
				+ ", getTargetData()=" + getTargetData().toString() + "]";
	}
	
	
	
}
