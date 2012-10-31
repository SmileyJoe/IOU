package com.joedev.iou;

public class EmailAddress {
	private int typeId;
	private String address;
	private String type;
	
	public EmailAddress(){
		this.typeId = 0;
		this.type = "";
		this.address = "";
	}
	
	public void set_type_id(int id){
		this.typeId = id;
	}
	
	public void set_type(String type){
		this.type = type;
	}
	
	public void set_address(String address){
		this.address = address;
	}
	
	public int get_type_id(){
		return this.typeId;
	}
	
	public String get_type(){
		return this.type;
	}
	
	public String get_address(){
		return this.address;
	}
		
}
