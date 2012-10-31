package com.joedev.iou;

import java.util.ArrayList;

public class User {
	
	private int id;
	private String name;
	private int onlineId;
	private int status;
	private ArrayList<Payment> payments;
	private float balance;
	private int minimalisticTextFlag;
	private String variableName;
	private long contactId;
	private boolean selected;
	
	/*****************************************************
	 * CONSTRUCTOR
	 ****************************************************/
	
	public User(){
		this.id = 0;
		this.name = "";
		this.onlineId = 0;
		this.status = 1;
		this.payments = new ArrayList<Payment>();
		this.balance = 0;
		this.minimalisticTextFlag = 0;
		this.variableName = "";
		this.contactId = 0;
		this.selected = false;
	}
	
	/*****************************************************
	 * SETTERS
	 ****************************************************/
	
	public void set_id(int id){
		this.id = id;
	}
	
	public void set_name(String name){
		this.name = name;
	}
	
	public void set_online_id(int id){
		this.onlineId = id;
	}
	
	public void set_status(int status){
		this.status = status;
	}
	
	public void set_payments(ArrayList<Payment> payments){
		this.payments = payments;
		this.set_balance();
	}
	
	public void set_balance(){
		for(int i = 0; i < this.get_payments().size(); i++){
			if(this.get_payments().get(i).is_from_user()){
				this.balance = this.balance - this.get_payments().get(i).get_amount();
			} else {
				this.balance = this.balance + this.get_payments().get(i).get_amount();
			}
		}
		
		this.balance = (float) (Math.round(this.balance*100.0)/100.0);
	}
	
	public void set_variable_name(String name){
		this.variableName = name;
	}
	
	public void set_minimalistic_text_flag(int flag){
		this.minimalisticTextFlag = flag;
	}
	
	public void set_contact_id(long id){
		this.contactId = id;
	}
	
	public void set_selected(boolean selected){
		this.selected = selected;
	}
	
	/******************************************************
	 * GETTERS
	 *****************************************************/
	
	public int get_id(){
		return this.id;
	}
	
	public String get_name(){
		return this.name;
	}
	
	public int get_online_id(){
		return this.onlineId;
	}
	
	public int get_status(){
		return this.status;
	}
	
	public ArrayList<Payment> get_payments(){
		return this.payments;
	}
	
	public float get_balance(){
		return this.balance;
	}
	
	public String get_status_text(){
		String status = "";
		switch(this.status){
			case 0:
				status = "Inactive";
				break;
			case 1:
				status = "Active";
				break;
			case 2:
				status = "Current";
				break;
			case 3:
				status = "Favourite";
				break;
			default:
				status = "Active";
				break;
		}
		
		return status;
	}
	
	public String get_balance_text(){
		return Gen.get_amount_text(Math.abs(this.balance));
	}
	
	public String get_variable_name(){
		return this.variableName;
	}
	
	public int get_minimalistic_text_flag(){
		return this.minimalisticTextFlag;
	}
	
	public long get_contact_id(){
		return this.contactId;
	}
	
	public boolean get_selected(){
		return this.selected;
	}
	
	public String get_state_text(){
		String state = "";
		
		if(this.get_balance() > 0){
			state = "Owes You";
		} else {
			if(this.get_balance() < 0){
				state = "You Owe";
			} else {
				state = "All Square";
			}
		}
		
		return state;
	}
	
	public String get_first_name(){
		String first = "";
		
		if(this.get_name().contains(" ")){
			first = this.get_name().substring(0, this.get_name().indexOf(" "));
		} else {
			first = this.get_name();
		}
		
		return first;
	}
	
	/*******************************************************
	 * CHECKS
	 ******************************************************/

	public boolean is_inactive(){
		if(this.get_status() == 0){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean is_active(){
		if(this.get_status() == 1){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean is_current(){
		if(this.get_status() == 2){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean is_favourite(){
		if(this.get_status() == 3){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean is_using_minimalistic_text(){
		if(this.minimalisticTextFlag == 1){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean is_not_using_minimalistic_text(){
		if(this.minimalisticTextFlag == 0){
			return true;
		} else {
			return false;
		}
	}

	public boolean is_in_contact_dir(){
		if(this.contactId != 0){
			return true;
		} else {
			return false;
		}
	}

	public boolean is_selected(){
		return this.selected;
	}
	
}
