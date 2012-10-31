package com.joedev.iou;

import java.util.ArrayList;

public class Group {
	
	/************************************
	 * VARIABLES
	 ***********************************/
	
	private int id;
	private String title;
	private ArrayList<User> users;
	
	/************************************
	 * VARIABLES
	 ***********************************/
	
	public Group(){
		this.id = 0;
		this.title = "";
		this.users = new ArrayList<User>();
	}
	
	/************************************
	 * VARIABLES
	 ***********************************/
	
	public void set_id(int id){
		this.id = id;
	}
	
	public void set_title(String title){
		this.title = title;
	}
	
	public void set_users(ArrayList<User> users){
		this.users = users;
	}
	
	public void add_user(User user){
		this.users.add(user);
	}
	
	/************************************
	 * GETTERS
	 ***********************************/
	
	public int get_id(){
		return this.id;
	}
	
	public String get_title(){
		return this.title;
	}
	
	public ArrayList<User> get_users(){
		return this.users;
	}
	
	public User get_user(int id){
		return this.users.get(id);
	}
	
	
	/************************************
	 * CHECKS
	 ***********************************/
	
}
