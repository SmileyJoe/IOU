package com.smileyjoedev.iou;

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
	
	public void setId(int id){
		this.id = id;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public void setOnlineId(int id){
		this.onlineId = id;
	}
	
	public void setStatus(int status){
		this.status = status;
	}
	
	public void setPayments(ArrayList<Payment> payments){
		this.payments = payments;
		this.setBalance();
	}
	
	public void setBalance(){
		for(int i = 0; i < this.getPayments().size(); i++){
			if(this.getPayments().get(i).isFromUser()){
				this.balance = this.balance - this.getPayments().get(i).getAmount();
			} else {
				this.balance = this.balance + this.getPayments().get(i).getAmount();
			}
		}
		
		this.balance = (float) (Math.round(this.balance*100.0)/100.0);
	}
	
	public void setVariableName(String name){
		this.variableName = name;
	}
	
	public void setMinimalisticTextFlag(int flag){
		this.minimalisticTextFlag = flag;
	}
	
	public void setContactId(long id){
		this.contactId = id;
	}
	
	public void setSelected(boolean selected){
		this.selected = selected;
	}
	
	/******************************************************
	 * GETTERS
	 *****************************************************/
	
	public int getId(){
		return this.id;
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getOnlineId(){
		return this.onlineId;
	}
	
	public int getStatus(){
		return this.status;
	}
	
	public ArrayList<Payment> getPayments(){
		return this.payments;
	}
	
	public float getBalance(){
		return this.balance;
	}
	
	public String getStatusText(){
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
	
	public String getBalanceText(){
		return Gen.getAmountText(Math.abs(this.balance));
	}
	
	public String getVariableName(){
		return this.variableName;
	}
	
	public int getMinimalisticTextFlag(){
		return this.minimalisticTextFlag;
	}
	
	public long getContactId(){
		return this.contactId;
	}
	
	public boolean getSelected(){
		return this.selected;
	}
	
	public String getStateText(){
		String state = "";
		// TODO: Use Strings from strings.xml //
		if(this.getBalance() > 0){
			state = "Owes You";
		} else {
			if(this.getBalance() < 0){
				state = "You Owe";
			} else {
				state = "All Square";
			}
		}
		
		return state;
	}
	
	public String getFirstName(){
		String first = "";
		
		if(this.getName().contains(" ")){
			first = this.getName().substring(0, this.getName().indexOf(" "));
		} else {
			first = this.getName();
		}
		
		return first;
	}
	
	/*******************************************************
	 * CHECKS
	 ******************************************************/

	public boolean isInactive(){
		if(this.getStatus() == 0){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isActive(){
		if(this.getStatus() == 1){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isCurrent(){
		if(this.getStatus() == 2){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isFavourite(){
		if(this.getStatus() == 3){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isUsingMinimalisticText(){
		if(this.minimalisticTextFlag == 1){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isNotUsingMinimalisticText(){
		if(this.minimalisticTextFlag == 0){
			return true;
		} else {
			return false;
		}
	}

	public boolean isInContactDir(){
		if(this.contactId != 0){
			return true;
		} else {
			return false;
		}
	}

	public boolean isSelected(){
		return this.selected;
	}
	
	@Override
	public String toString() {
		return "User [getId()=" + getId() + ", getName()=" + getName()
				+ ", getOnlineId()=" + getOnlineId() + ", getStatus()="
				+ getStatus() + ", getPayments()=" + getPayments()
				+ ", getBalance()=" + getBalance() + ", getStatusText()="
				+ getStatusText() + ", getBalanceText()=" + getBalanceText()
				+ ", getVariableName()=" + getVariableName()
				+ ", getMinimalisticTextFlag()=" + getMinimalisticTextFlag()
				+ ", getContactId()=" + getContactId() + ", getSelected()="
				+ getSelected() + ", getStateText()=" + getStateText()
				+ ", getFirstName()=" + getFirstName() + ", isInactive()="
				+ isInactive() + ", isActive()=" + isActive()
				+ ", isCurrent()=" + isCurrent() + ", isFavourite()="
				+ isFavourite() + ", isUsingMinimalisticText()="
				+ isUsingMinimalisticText() + ", isNotUsingMinimalisticText()="
				+ isNotUsingMinimalisticText() + ", isInContactDir()="
				+ isInContactDir() + ", isSelected()=" + isSelected() + "]";
	}
	
}
