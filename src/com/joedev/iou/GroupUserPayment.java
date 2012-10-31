package com.joedev.iou;

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
	
	public void set_user(User user) {
		this.user = user;
	}
	
	public void set_paid(float paid) {
		this.paid = paid;
	}
	
	public void set_spent(float spent) {
		this.spent = spent;
	}
	
	/**********************************************
	 * GETTERS
	 *********************************************/
	

	public User get_user() {
		return user;
	}
	
	public float get_paid() {
		return Gen.format_number(paid);
	}
	
	public float get_spent() {
		return Gen.format_number(spent);
	}
	
	public float get_balance(){
		float balance = this.get_paid() - this.get_spent();
		
		return Gen.format_number(balance);
	}
	
	public String get_paid_text(){
		return Gen.get_amount_text(this.get_paid());
	}
	
	public String get_spent_text(){
		return Gen.get_amount_text(this.get_spent());
	}
	
	public String get_balance_text(){
		return this.get_balance_text(true);
	}
	
	public String get_balance_text(boolean showSign){
		if(showSign){
			return Gen.get_amount_text(this.get_balance());
		} else {
			return Gen.get_amount_text(Math.abs(this.get_balance()));
		}
		
	}
	
	
}
