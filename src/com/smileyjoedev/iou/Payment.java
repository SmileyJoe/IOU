package com.smileyjoedev.iou;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.content.Context;

public class Payment {
	
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
		this.direction = 0;
		this.description = "";
		this.date = Gen.getPdt();
		this.user = new User(this.context);
		this.type = 0;
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
    		case 0:
    			this.setType(0);
    			this.setDirection(0);
    			break;
    		case 1:
    			this.setType(0);
    			this.setDirection(1);
    			break;
    		case 2:
    			this.setType(1);
    			this.setDirection(0);
    			break;
    		case 3:
    			this.setType(1);
    			this.setDirection(1);
    			break;
    	}
		
		
	}
	
	public void setTypeDb(){
		switch(this.getType()){
			case 0:
				if(this.isFromUser()){
					this.setTypeDb(1);
				} else {
					this.setTypeDb(0);
				}
				break;
			case 1:
				if(this.isFromUser()){
					this.setTypeDb(3);
				} else {
					this.setTypeDb(2);
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
		// TODO: Use strings from strings.xml //
		String direction = "";
		switch(this.getDirection()){
			case 0:
				direction = "To User";
				break;
			case 1:
				direction = "From User";
				break;
			default:
				direction = "To User";
				break;
		}
		
		return direction;
	}
	
	public String getTypeText(){
		switch(this.getType()){
			case 0:
				return "Loan";
			case 1:
				return "Repayment";
			default:
				return "Loan";
		}
	}
	
	public String getTypeDbText(){
		String type = "";
		switch(this.getTypeDb()){
			case 0:
				type = "Loan to user";
				break;
			case 1:
				type = "Loan from user";
				break;
			case 2:
				type = "Payment to user";
				break;
			case 3:
				type = "Payment from user";
				break;
			default:
				type = "To User";
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
		if(this.getDirection() == 0){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isFromUser(){
		if(this.getDirection() == 1){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isLoan(){
		if(this.type == 0){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isPayment(){
		if(this.type == 1){
			return true;
		} else {
			return false;
		}
	}
}
