package com.smileyjoedev.iou;

import java.text.DecimalFormat;
import java.text.NumberFormat;

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
	
	/*********************************************************
	 * CONSTRUCTOR
	 ********************************************************/
	
	public Payment(){
		this.id = 0;
		this.userId = 0;
		this.amount = 0;
		this.direction = 0;
		this.description = "";
		this.date = Gen.get_pdt();
		this.user = new User();
		this.type = 0;
		this.title = "";
	}
	
	/**********************************************************
	 * SETTERS
	 *********************************************************/
	
	public void set_id(int id){
		this.id = id;
	}
	
	public void set_user_id(int id){
		this.userId = id;
	}
	
	public void set_amount(float amount){
		amount = (float) (Math.round(amount*100.0)/100.0);
		this.amount = amount;
	}
	
	public void set_direction(int direction){
		this.direction = direction;
	}
	
	public void set_description(String description){
		this.description = description;
	}
	
	public void set_title(String title){
		this.title = title;
	}
	
	public void set_date(long date){
		this.date = date;
	}
	
	public void set_user(User user){
		this.user = user;
	}
	
	public void set_type(int type){
		this.type = type;
	}
	
	public void set_type_db(int type){
		this.typeDb = type;
    	switch(this.get_type_db()){
    		case 0:
    			this.set_type(0);
    			this.set_direction(0);
    			break;
    		case 1:
    			this.set_type(0);
    			this.set_direction(1);
    			break;
    		case 2:
    			this.set_type(1);
    			this.set_direction(0);
    			break;
    		case 3:
    			this.set_type(1);
    			this.set_direction(1);
    			break;
    	}
		
		
	}
	
	public void set_type_db(){
		switch(this.get_type()){
			case 0:
				if(this.is_from_user()){
					this.set_type_db(1);
				} else {
					this.set_type_db(0);
				}
				break;
			case 1:
				if(this.is_from_user()){
					this.set_type_db(3);
				} else {
					this.set_type_db(2);
				}
				break;
		}
	}
	
	
	/*********************************************************
	 * GETTERS
	 ********************************************************/
	
	public int get_id(){
		return this.id;
	}
	
	public int get_user_id(){
		return this.userId;
	}
	
	public float get_amount(){
		return Gen.format_number(this.amount);
	}
	
	public int get_direction(){
		return this.direction;
	}
	
	public String get_description(){
		return this.description;
	}
	
	public String get_title(){
		return this.title;
	}
	
	public long get_date(){
		return this.date;
	}
	
	public int get_type(){
		return this.type;
	}
	
	public int get_type_db(){
		return this.typeDb;
	}
	
	public User get_user(){
		return this.user;
	}
	
	public String get_date_text(boolean includeTime){
		return Gen.convert_pdt(this.get_date(), includeTime);
	}
	
	public String get_direction_text(){
		String direction = "";
		switch(this.get_direction()){
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
	
	public String get_type_text(){
		switch(this.get_type()){
			case 0:
				return "Loan";
			case 1:
				return "Repayment";
			default:
				return "Loan";
		}
	}
	
	public String get_type_db_text(){
		String type = "";
		switch(this.get_type_db()){
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
	
	public String get_amount_text(boolean includeSymbol){
		if(includeSymbol){
			return Gen.get_amount_text(this.get_amount());
		} else {
			return Gen.get_formatted_amount(this.get_amount());
		}
		
	}
	
	/**********************************************************
	 * CHECKS
	 *********************************************************/
	
	public boolean is_to_user(){
		if(this.get_direction() == 0){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean is_from_user(){
		if(this.get_direction() == 1){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean is_loan(){
		if(this.type == 0){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean is_payment(){
		if(this.type == 1){
			return true;
		} else {
			return false;
		}
	}
}
