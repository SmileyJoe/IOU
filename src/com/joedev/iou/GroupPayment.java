package com.joedev.iou;

import java.util.ArrayList;

public class GroupPayment {
	
	private int id;
	private float amount;
	private int type;
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
		this.description = "";
		this.pdt = Gen.get_pdt();
		this.splits = new ArrayList<PaymentSplit>();
		this.groupId = 0;
	}
	
	/**************************************************
	 * SETTERS
	 *************************************************/

	public void set_id(int id) {
		this.id = id;
	}
	
	public void set_amount(float amount) {
		this.amount = amount;
	}
	
	public void set_type(int type) {
		this.type = type;
	}
	
	public void set_description(String description) {
		this.description = description;
	}
	
	public void set_pdt(long pdt) {
		this.pdt = pdt;
	}
	
	public void set_splits(ArrayList<PaymentSplit> splits) {
		this.splits = splits;
		
		for(int i = 0; i < splits.size(); i++){
			if(this.splits.get(i).is_paying()){
				this.amount += splits.get(i).get_amount();
			}
		}
	}
	
	public void set_group_id(int id){
		this.groupId = id;
	}
	
	public void add_split(PaymentSplit split){
		this.splits.add(split);
		if(split.is_paying()){
			this.amount += split.get_amount();
		}
		
	}
	
	/**************************************************
	 * GETTERS
	 *************************************************/

	public int get_id() {
		return id;
	}

	public float get_amount() {
		return Gen.format_number(amount);
	}
	
	public int get_type() {
		return type;
	}
	
	public String get_description() {
		return description;
	}
	
	public long get_pdt() {
		return pdt;
	}
	
	public ArrayList<PaymentSplit> get_splits() {
		return splits;
	}
	
	public int get_group_id(){
		return this.groupId;
	}
	
	public PaymentSplit get_split(int id){
		return this.splits.get(id);
	}
	
	public String get_amount_text(){
		return Gen.get_amount_text(this.amount);
	}
	
	public String get_paying_csv(){
		String result = "";
		boolean first = true;
		
		for(int i = 0; i < this.get_splits().size(); i++){
			if(this.get_split(i).is_paying()){
				String temp = this.get_split(i).get_user().get_name() + "(" + this.get_split(i).get_amount_text(true) + ")";
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
	
	public String get_paid_for_csv(){
		String result = "";
		boolean first = true;
		
		for(int i = 0; i < this.get_splits().size(); i++){
			if(this.get_split(i).is_paid_for()){
				String temp = this.get_split(i).get_user().get_name() + "(" + this.get_split(i).get_amount_text(true) + ")";
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
