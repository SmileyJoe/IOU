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
			return Gen.getAmountText(this.getAmount());
		} else {
			return Gen.getFormattedAmount(this.getAmount());
		}
		
	}
	
	/*************************************************
	 * CHECKS
	 ************************************************/

	public boolean isPaying(){
		if(this.type == 0){
			return true;
		} else {
			return false;
		}
	}

	public boolean isPaidFor(){
		if(this.type == 1){
			return true;
		} else {
			return false;
		}
	}
	
}
