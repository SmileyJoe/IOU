package com.smileyjoedev.iou;

import android.content.Context;

public class GroupRepayment {
	
	private User owedUser;
	private User owingUser;
	private float amount;
	private boolean selected;
	private Context context;
	
	/*****************************************************
	 * CONSTRUCTOR
	 ****************************************************/
	
	public GroupRepayment(Context context){
		this.context = context;
		this.amount = 0;
		this.owedUser = new User(this.context);
		this.owingUser = new User(this.context);
		this.selected = false;
	}
	
	/*****************************************************
	 * SETTERS
	 ****************************************************/
	
	public void setOwedUser(User owedUser) {
		this.owedUser = owedUser;
	}
	
	public void setOwingUser(User owingUser) {
		this.owingUser = owingUser;
	}
	
	public void setAmount(float amount) {
		this.amount = amount;
	}

	public void setSelected(boolean selected){
		this.selected = selected;
	}
	
	/*****************************************************
	 * GETTERS
	 ****************************************************/
	
	public User getOwedUser() {
		return owedUser;
	}

	public User getOwingUser() {
		return owingUser;
	}

	public float getAmount() {
		return amount;
	}
	
	public boolean getSelected(){
		return this.selected;
	}
	
	public String getAmountText(){
		return Gen.getAmountText(this.context, this.amount);
	}

	@Override
	public String toString() {
		return this.getOwingUser().getName() + " owes " + this.getOwedUser().getName() + " " + this.getAmountText();
	}
	
	
}
