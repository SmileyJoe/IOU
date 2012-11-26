package com.smileyjoedev.iou;

import java.util.ArrayList;

import android.content.Context;

public class User {
	
	public static final int STATUS_INACTIVE = 0;
	public static final int STATUS_ACTIVE = 1;
	public static final int STATUS_CURRENT = 2;
	public static final int STATUS_FAVOURITE = 3;
	
	
	private int id;
	private String name;
	private int onlineId;
	private int status;
	private ArrayList<Payment> payments;
	private float balance;
	private int minimalisticTextFlag;
	private String variableName;
	private long contactId;
	private boolean selected;
	private Context context;
	
	/*****************************************************
	 * CONSTRUCTOR
	 ****************************************************/
	
	public User(Context context){
		this.id = 0;
		this.name = "";
		this.onlineId = 0;
		this.status = User.STATUS_ACTIVE;
		this.payments = new ArrayList<Payment>();
		this.balance = 0;
		this.minimalisticTextFlag = 0;
		this.variableName = "";
		this.contactId = 0;
		this.selected = false;
		this.context = context;
	}
	
	/*****************************************************
	 * SETTERS
	 ****************************************************/
	
	public void setId(int id){
		this.id = id;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setOnlineId(int id){
		this.onlineId = id;
	}
	
	public void setStatus(int status){
		this.status = status;
	}
	
	public void setPayments(ArrayList<Payment> payments){
		this.payments = payments;
		this.setBalance();
	}
	
	public void setBalance(){
		for(int i = 0; i < this.getPayments().size(); i++){
			if(this.getPayments().get(i).isFromUser()){
				this.balance = this.balance - this.getPayments().get(i).getAmount();
			} else {
				this.balance = this.balance + this.getPayments().get(i).getAmount();
			}
		}
		
		this.balance = (float) (Math.round(this.balance*100.0)/100.0);
	}
	
	public void setVariableName(String name){
		this.variableName = name;
	}
	
	public void setMinimalisticTextFlag(int flag){
		this.minimalisticTextFlag = flag;
	}
	
	public void setContactId(long id){
		this.contactId = id;
	}
	
	public void setSelected(boolean selected){
		this.selected = selected;
	}
	
	/******************************************************
	 * GETTERS
	 *****************************************************/
	
	public int getId(){
		return this.id;
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getOnlineId(){
		return this.onlineId;
	}
	
	public int getStatus(){
		return this.status;
	}
	
	public ArrayList<Payment> getPayments(){
		return this.payments;
	}
	
	public float getBalance(){
		return this.balance;
	}
	
	public String getStatusText(){
		String status = "";
		switch(this.status){
			case User.STATUS_INACTIVE:
				status = this.context.getString(R.string.user_status_inactive);
				break;
			case User.STATUS_ACTIVE:
				status = this.context.getString(R.string.user_status_active);
				break;
			case User.STATUS_CURRENT:
				status = this.context.getString(R.string.user_status_current);
				break;
			case User.STATUS_FAVOURITE:
				status = this.context.getString(R.string.user_status_favourite);
				break;
			default:
				status = this.context.getString(R.string.user_status_active);
				break;
		}
		
		return status;
	}
	
	public String getBalanceText(){
		return Gen.getAmountText(this.context, Math.abs(this.balance));
	}
	
	public String getVariableName(){
		return this.variableName;
	}
	
	public int getMinimalisticTextFlag(){
		return this.minimalisticTextFlag;
	}
	
	public long getContactId(){
		return this.contactId;
	}
	
	public boolean getSelected(){
		return this.selected;
	}
	
	public String getStateText(){
		String state = "";
		if(this.getBalance() > 0){
			state = this.context.getString(R.string.tv_total_owed_user);
		} else {
			if(this.getBalance() < 0){
				state = this.context.getString(R.string.tv_total_user_owed);
			} else {
				state = this.context.getString(R.string.tv_total_all_square);
			}
		}
		
		return state;
	}
	
	public String getFirstName(){
		String first = "";
		
		if(this.getName().contains(" ")){
			first = this.getName().substring(0, this.getName().indexOf(" "));
		} else {
			first = this.getName();
		}
		
		return first;
	}
	
	/*******************************************************
	 * CHECKS
	 ******************************************************/

	public boolean isInactive(){
		if(this.getStatus() == User.STATUS_INACTIVE){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isActive(){
		if(this.getStatus() == User.STATUS_ACTIVE){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isCurrent(){
		if(this.getStatus() == User.STATUS_CURRENT){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isFavourite(){
		if(this.getStatus() == User.STATUS_FAVOURITE){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isUsingMinimalisticText(){
		if(this.minimalisticTextFlag == 1){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isNotUsingMinimalisticText(){
		if(this.minimalisticTextFlag == 0){
			return true;
		} else {
			return false;
		}
	}

	public boolean isInContactDir(){
		if(this.contactId != 0){
			return true;
		} else {
			return false;
		}
	}

	public boolean isSelected(){
		return this.selected;
	}
	
	@Override
	public String toString() {
		return "User [getId()=" + getId() + ", getName()=" + getName()
				+ ", getOnlineId()=" + getOnlineId() + ", getStatus()="
				+ getStatus() + ", getPayments()=" + getPayments()
				+ ", getBalance()=" + getBalance() + ", getStatusText()="
				+ getStatusText() + ", getBalanceText()=" + getBalanceText()
				+ ", getVariableName()=" + getVariableName()
				+ ", getMinimalisticTextFlag()=" + getMinimalisticTextFlag()
				+ ", getContactId()=" + getContactId() + ", getSelected()="
				+ getSelected() + ", getStateText()=" + getStateText()
				+ ", getFirstName()=" + getFirstName() + ", isInactive()="
				+ isInactive() + ", isActive()=" + isActive()
				+ ", isCurrent()=" + isCurrent() + ", isFavourite()="
				+ isFavourite() + ", isUsingMinimalisticText()="
				+ isUsingMinimalisticText() + ", isNotUsingMinimalisticText()="
				+ isNotUsingMinimalisticText() + ", isInContactDir()="
				+ isInContactDir() + ", isSelected()=" + isSelected() + "]";
	}
	
}
