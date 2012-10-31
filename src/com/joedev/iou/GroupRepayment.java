package com.joedev.iou;

public class GroupRepayment {
	
	private User owedUser;
	private User owingUser;
	private float amount;
	private boolean selected;
	
	/*****************************************************
	 * CONSTRUCTOR
	 ****************************************************/
	
	public GroupRepayment(){
		this.amount = 0;
		this.owedUser = new User();
		this.owingUser = new User();
		this.selected = false;
	}
	
	/*****************************************************
	 * SETTERS
	 ****************************************************/
	
	public void set_owed_user(User owedUser) {
		this.owedUser = owedUser;
	}
	
	public void set_owing_user(User owingUser) {
		this.owingUser = owingUser;
	}
	
	public void set_amount(float amount) {
		this.amount = amount;
	}

	public void set_selected(boolean selected){
		this.selected = selected;
	}
	
	/*****************************************************
	 * GETTERS
	 ****************************************************/
	
	public User get_owed_user() {
		return owedUser;
	}

	public User get_owing_user() {
		return owingUser;
	}

	public float get_amount() {
		return amount;
	}
	
	public boolean get_selected(){
		return this.selected;
	}
	
	public String get_amount_text(){
		return Gen.get_amount_text(this.amount);
	}

	@Override
	public String toString() {
		return this.get_owing_user().get_name() + " owes " + this.get_owed_user().get_name() + " " + this.get_amount_text();
	}
	
	
}
