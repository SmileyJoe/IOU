package com.smileyjoedev.iou;

import java.util.ArrayList;

public class GroupPayment {
	
	private int id;
	private float amount;
	private int type;
	private String title;
	private String description;
	private long pdt;
	private int groupId;
	private ArrayList<PaymentSplit> splits;
	
	/**************************************************
	 * CONSTRUCTOR
	 *************************************************/
	
	public GroupPayment() {
		this.id = 0;
		this.amount = 0;
		this.type = 0;
		this.title = "";
		this.description = "";
		this.pdt = Gen.getPdt();
		this.splits = new ArrayList<PaymentSplit>();
		this.groupId = 0;
	}
	
	/**************************************************
	 * SETTERS
	 *************************************************/

	public void setId(int id) {
		this.id = id;
	}
	
	public void setAmount(float amount) {
		this.amount = amount;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setPdt(long pdt) {
		this.pdt = pdt;
	}
	
	public void setSplits(ArrayList<PaymentSplit> splits) {
		this.splits = splits;
		
		for(int i = 0; i < splits.size(); i++){
			if(this.splits.get(i).isPaying()){
				this.amount += splits.get(i).getAmount();
			}
		}
	}
	
	public void setGroupId(int id){
		this.groupId = id;
	}
	
	public void addSplit(PaymentSplit split){
		this.splits.add(split);
		if(split.isPaying()){
			this.amount += split.getAmount();
		}
		
	}
	
	/**************************************************
	 * GETTERS
	 *************************************************/

	public int getId() {
		return id;
	}

	public float getAmount() {
		return Gen.formatNumber(amount);
	}
	
	public int getType() {
		return type;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public long getPdt() {
		return pdt;
	}
	
	public ArrayList<PaymentSplit> getSplits() {
		return splits;
	}
	
	public int getGroupId(){
		return this.groupId;
	}
	
	public PaymentSplit getSplit(int id){
		return this.splits.get(id);
	}
	
	public String getAmountText(){
		return Gen.getAmountText(this.amount);
	}
	
	public String getPayingCsv(){
		String result = "";
		boolean first = true;
		
		for(int i = 0; i < this.getSplits().size(); i++){
			if(this.getSplit(i).isPaying()){
				String temp = this.getSplit(i).getUser().getName() + "(" + this.getSplit(i).getAmountText(true) + ")";
				if(first){
					result += temp;
					first = false;
				} else {
					result += ", " + temp;
				}
			}
		}
		
		return result;
	}
	
	public String getPaidForCsv(){
		String result = "";
		boolean first = true;
		
		for(int i = 0; i < this.getSplits().size(); i++){
			if(this.getSplit(i).isPaidFor()){
				String temp = this.getSplit(i).getUser().getName() + "(" + this.getSplit(i).getAmountText(true) + ")";
				if(first){
					result += temp;
					first = false;
				} else {
					result += ", " + temp;
				}
			}
		}
		
		return result;
	}
	
	/**************************************************
	 * CHECKS
	 *************************************************/
	
}
