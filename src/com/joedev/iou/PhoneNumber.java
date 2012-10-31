package com.joedev.iou;

public class PhoneNumber {
	
	private int typeId;
	private String number;
	private String type;
	
	public PhoneNumber(){
		this.typeId = 0;
		this.type = "";
		this.number = "";
	}
	
	public void set_type_id(int id){
		this.typeId = id;
	}
	
	public void set_type(String type){
		this.type = type;
	}
	
	public void set_number(String number){
		this.number = number;
	}
	
	public int get_type_id(){
		return this.typeId;
	}
	
	public String get_type(){
		return this.type;
	}
	
	public String get_number(){
		return this.number;
	}
	
}
