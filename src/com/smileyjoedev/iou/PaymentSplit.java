package com.smileyjoedev.iou;

public class PaymentSplit {
	
	private int id;
	private int type;
	private int userId;
	private User user;
	private float amount;
	private int paymentId;
	
	/*************************************************
	 * CONSTRUCTOR
	 ************************************************/
	
	public PaymentSplit() {
		this.id = 0;
		this.type = 0;
		this.userId = 0;
		this.user = new User();
		this.amount = 0;
		this.paymentId = 0;
	}

	/*************************************************
	 * SETTERS
	 ************************************************/
	
	public void set_id(int id) {
		this.id = id;
	}

	public void set_type(int type) {
		this.type = type;
	}

	public void set_user_Id(int userId) {
		this.userId = userId;
	}

	public void set_user(User user) {
		this.user = user;
	}

	public void set_amount(float amount) {
		this.amount = amount;
	}
	
	public void set_payment_id(int id){
		this.paymentId = id;
	}

	/*************************************************
	 * GETTERS
	 ************************************************/
	
	public int get_id() {
		return id;
	}

	public int get_type() {
		return type;
	}

	public int get_user_id() {
		return userId;
	}

	public User get_user() {
		return user;
	}

	public float get_amount() {
		return Gen.format_number(amount);
	}
	
	public int get_payment_id(){
		return this.paymentId;
	}
	
	public String get_amount_text(boolean includeSymbol){
		if(includeSymbol){
			return Gen.get_amount_text(this.get_amount());
		} else {
			return Gen.get_formatted_amount(this.get_amount());
		}
		
	}
	
	/*************************************************
	 * CHECKS
	 ************************************************/

	public boolean is_paying(){
		if(this.type == 0){
			return true;
		} else {
			return false;
		}
	}

	public boolean is_paid_for(){
		if(this.type == 1){
			return true;
		} else {
			return false;
		}
	}
	
}
