package com.smileyjoedev.iou;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.content.Context;

public class Payment {
	
	public static final int DIRECTION_TO_USER = 0;
	public static final int DIRECTION_FROM_USER = 1;
	
	public static final int TYPE_LOAN = 0;
	public static final int TYPE_REPAYMENT = 1;
	
	public static final int TYPE_DB_LOAN_TO_USER = 0;
	public static final int TYPE_DB_LOAN_FROM_USER = 1;
	public static final int TYPE_DB_PAYMENT_TO_USER = 2;
	public static final int TYPE_DB_PAYMENT_FROM_USER = 3;
	
	private int id;
	private int userId;
	private float amount;
	private int direction;
	private String description;
	private String title;
	private long date;
	private User user;
	private int type;
	private int typeDb;
	private Context context;
	
	/*********************************************************
	 * CONSTRUCTOR
	 ********************************************************/
	
	public Payment(Context context){
		this.context = context;
		this.id = 0;
		this.userId = 0;
		this.amount = 0;
		this.direction = Payment.DIRECTION_TO_USER;
		this.description = "";
		this.date = Gen.getPdt();
		this.user = new User(this.context);
		this.type = Payment.TYPE_LOAN;
		this.title = "";
		
	}
	
	/**********************************************************
	 * SETTERS
	 *********************************************************/
	
	public void setId(int id){
		this.id = id;
	}
	
	public void setUserId(int id){
		this.userId = id;
	}
	
	public void setAmount(float amount){
		amount = (float) (Math.round(amount*100.0)/100.0);
		this.amount = amount;
	}
	
	public void setDirection(int direction){
		this.direction = direction;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setDate(long date){
		this.date = date;
	}
	
	public void setUser(User user){
		this.user = user;
	}
	
	public void setType(int type){
		this.type = type;
	}
	
	public void setTypeDb(int type){
		this.typeDb = type;
    	switch(this.getTypeDb()){
    		case Payment.TYPE_DB_LOAN_TO_USER:
    			this.setType(Payment.TYPE_LOAN);
    			this.setDirection(Payment.DIRECTION_TO_USER);
    			break;
    		case Payment.TYPE_DB_LOAN_FROM_USER:
    			this.setType(Payment.TYPE_LOAN);
    			this.setDirection(Payment.DIRECTION_FROM_USER);
    			break;
    		case Payment.TYPE_DB_PAYMENT_TO_USER:
    			this.setType(Payment.TYPE_REPAYMENT);
    			this.setDirection(Payment.DIRECTION_TO_USER);
    			break;
    		case Payment.TYPE_DB_PAYMENT_FROM_USER:
    			this.setType(Payment.TYPE_REPAYMENT);
    			this.setDirection(Payment.DIRECTION_FROM_USER);
    			break;
    	}
		
		
	}
	
	public void setTypeDb(){
		switch(this.getType()){
			case Payment.TYPE_LOAN:
				if(this.isFromUser()){
					this.setTypeDb(Payment.TYPE_DB_LOAN_FROM_USER);
				} else {
					this.setTypeDb(Payment.TYPE_DB_LOAN_TO_USER);
				}
				break;
			case Payment.TYPE_REPAYMENT:
				if(this.isFromUser()){
					this.setTypeDb(Payment.TYPE_DB_PAYMENT_FROM_USER);
				} else {
					this.setTypeDb(Payment.TYPE_DB_PAYMENT_TO_USER);
				}
				break;
		}
	}
	
	
	/*********************************************************
	 * GETTERS
	 ********************************************************/
	
	public int getId(){
		return this.id;
	}
	
	public int getUserId(){
		return this.userId;
	}
	
	public float getAmount(){
		return Gen.formatNumber(this.amount);
	}
	
	public int getDirection(){
		return this.direction;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public long getDate(){
		return this.date;
	}
	
	public int getType(){
		return this.type;
	}
	
	public int getTypeDb(){
		return this.typeDb;
	}
	
	public User getUser(){
		return this.user;
	}
	
	public String getDateText(boolean includeTime){
		return Gen.convertPdt(this.getDate(), includeTime);
	}
	
	public String getDirectionText(){
		String direction = "";
		switch(this.getDirection()){
			case Payment.DIRECTION_TO_USER:
				direction = this.context.getString(R.string.payment_direction_to_user);
				break;
			case Payment.DIRECTION_FROM_USER:
				direction = this.context.getString(R.string.payment_direction_from_user);;
				break;
			default:
				direction = this.context.getString(R.string.payment_direction_to_user);;
				break;
		}
		
		return direction;
	}
	
	public String getTypeText(){
		switch(this.getType()){
			case Payment.TYPE_LOAN:
				return this.context.getString(R.string.payment_type_loan);
			case Payment.TYPE_REPAYMENT:
				return this.context.getString(R.string.payment_type_repayment);
			default:
				return this.context.getString(R.string.payment_type_loan);
		}
	}
	
	public String getTypeDbText(){
		String type = "";
		switch(this.getTypeDb()){
			case Payment.TYPE_DB_LOAN_TO_USER:
				type = this.context.getString(R.string.payment_type_db_loan_to_user);
				break;
			case Payment.TYPE_DB_LOAN_FROM_USER:
				type = this.context.getString(R.string.payment_type_db_loan_from_user);
				break;
			case Payment.TYPE_DB_PAYMENT_TO_USER:
				type = this.context.getString(R.string.payment_type_db_payment_to_user);
				break;
			case Payment.TYPE_DB_PAYMENT_FROM_USER:
				type = this.context.getString(R.string.payment_type_db_payment_from_user);
				break;
			default:
				type = this.context.getString(R.string.payment_type_db_payment_to_user);
				break;
		}
		
		return type;
	}
	
	public String getAmountText(boolean includeSymbol){
		if(includeSymbol){
			return Gen.getAmountText(this.context, this.getAmount());
		} else {
			return Gen.getFormattedAmount(this.getAmount());
		}
		
	}
	
	/**********************************************************
	 * CHECKS
	 *********************************************************/
	
	public boolean isToUser(){
		if(this.getDirection() == Payment.DIRECTION_TO_USER){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isFromUser(){
		if(this.getDirection() == Payment.DIRECTION_FROM_USER){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isLoan(){
		if(this.type == Payment.TYPE_LOAN){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isPayment(){
		if(this.type == Payment.TYPE_REPAYMENT){
			return true;
		} else {
			return false;
		}
	}
}
