package com.smileyjoedev.iou;

import java.text.DecimalFormat;

public class GroupUserPayment {
	
	private User user;
	private float paid;
	private float spent;
	
	/**********************************************
	 * CONSTRUCTOR
	 *********************************************/
	
	/**********************************************
	 * SETTERS
	 *********************************************/
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public void setPaid(float paid) {
		this.paid = paid;
	}
	
	public void setSpent(float spent) {
		this.spent = spent;
	}
	
	/**********************************************
	 * GETTERS
	 *********************************************/
	

	public User getUser() {
		return user;
	}
	
	public float getPaid() {
		return Gen.formatNumber(paid);
	}
	
	public float getSpent() {
		return Gen.formatNumber(spent);
	}
	
	public float getBalance(){
		float balance = this.getPaid() - this.getSpent();
		
		return Gen.formatNumber(balance);
	}
	
	public String getPaidText(){
		return Gen.getAmountText(this.getPaid());
	}
	
	public String getSpentText(){
		return Gen.getAmountText(this.getSpent());
	}
	
	public String getBalanceText(){
		return this.getBalanceText(true);
	}
	
	public String getBalanceText(boolean showSign){
		if(showSign){
			return Gen.getAmountText(this.getBalance());
		} else {
			return Gen.getAmountText(Math.abs(this.getBalance()));
		}
		
	}
	
	
}
