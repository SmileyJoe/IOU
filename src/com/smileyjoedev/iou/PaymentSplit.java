package com.smileyjoedev.iou;

import android.content.Context;

public class PaymentSplit {
	
	public static final int TYPE_PAYING = 0;
	public static final int TYPE_PAID_FOR = 1;
	
	private int id;
	private int type;
	private int userId;
	private User user;
	private float amount;
	private int paymentId;
	private Context context;
	
	/*************************************************
	 * CONSTRUCTOR
	 ************************************************/
	
	public PaymentSplit(Context context) {
		this.context = context;
		this.id = 0;
		this.type = PaymentSplit.TYPE_PAYING;
		this.userId = 0;
		this.user = new User(this.context);
		this.amount = 0;
		this.paymentId = 0;
		
	}

	/*************************************************
	 * SETTERS
	 ************************************************/
	
	public void setId(int id) {
		this.id = id;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}
	
	public void setPaymentId(int id){
		this.paymentId = id;
	}

	/*************************************************
	 * GETTERS
	 ************************************************/
	
	public int getId() {
		return id;
	}

	public int getType() {
		return type;
	}

	public int getUserId() {
		return userId;
	}

	public User getUser() {
		return user;
	}

	public float getAmount() {
		return Gen.formatNumber(amount);
	}
	
	public int getPaymentId(){
		return this.paymentId;
	}
	
	public String getAmountText(boolean includeSymbol){
		if(includeSymbol){
			return Gen.getAmountText(this.context, this.getAmount());
		} else {
			return Gen.getFormattedAmount(this.getAmount());
		}
		
	}
	
	/*************************************************
	 * CHECKS
	 ************************************************/

	public boolean isPaying(){
		if(this.type == PaymentSplit.TYPE_PAYING){
			return true;
		} else {
			return false;
		}
	}

	public boolean isPaidFor(){
		if(this.type == PaymentSplit.TYPE_PAID_FOR){
			return true;
		} else {
			return false;
		}
	}
	
}
