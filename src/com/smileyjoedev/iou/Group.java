package com.smileyjoedev.iou;

import java.util.ArrayList;

public class Group {
	
	/************************************
	 * VARIABLES
	 ***********************************/
	
	private int id;
	private String title;
	private String description;
	private ArrayList<User> users;
	
	/************************************
	 * VARIABLES
	 ***********************************/
	
	public Group(){
		this.id = 0;
		this.title = "";
		this.description = "";
		this.users = new ArrayList<User>();
	}
	
	/************************************
	 * VARIABLES
	 ***********************************/
	
	public void setId(int id){
		this.id = id;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public void setUsers(ArrayList<User> users){
		this.users = users;
	}
	
	public void addUser(User user){
		this.users.add(user);
	}
	
	/************************************
	 * GETTERS
	 ***********************************/
	
	public int getId(){
		return this.id;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public ArrayList<User> getUsers(){
		return this.users;
	}
	
	public User getUser(int id){
		return this.users.get(id);
	}
	
	
	/************************************
	 * CHECKS
	 ***********************************/
	
}
