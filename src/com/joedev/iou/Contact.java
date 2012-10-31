package com.joedev.iou;

import java.util.ArrayList;

public class Contact {
	
	private String name;
	private long id;
	private ArrayList<PhoneNumber> numbers;
	private ArrayList<EmailAddress> emails;
	
	public Contact(){
		this.name = "";
		this.id = 0;
		this.numbers = new ArrayList<PhoneNumber>();
		this.emails = new ArrayList<EmailAddress>();
	}
	
	public void set_name(String name){
		this.name = name;
	}
	
	public void set_id(long id){
		this.id = id;
	}
	
	public void set_numbers(ArrayList<PhoneNumber> numbers){
		this.numbers = numbers;
	}
	
	public void add_number(PhoneNumber number){
		this.numbers.add(number);
	}
	
	public void set_emails(ArrayList<EmailAddress> emails){
		this.emails = emails;
	}
	
	public void add_email(EmailAddress email){
		this.emails.add(email);
	}
	
	public String get_name(){
		return this.name;
	}
	
	public long get_id(){
		return this.id;
	}
	
	public ArrayList<PhoneNumber> get_numbers(){
		return this.numbers;
	}
	
	public PhoneNumber get_number(int id){
		return this.numbers.get(id);
	}
	
	public ArrayList<EmailAddress> get_emails(){
		return this.emails;
	}
	
	public EmailAddress get_email(int id){
		return this.emails.get(id);
	}
	
}
