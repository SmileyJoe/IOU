package com.smileyjoedev.iou;

import java.util.Comparator;


public class SortUser implements Comparator<User> {
	int sort;
	
	public SortUser(int sort){
		this.sort = sort;
	}
	
	public SortUser(){
		this.sort = 0;
	}
	
	@Override
	public int compare(User user1, User user2) {
		int returnValue = 0;
		
		switch(this.sort){
			case 0:
				returnValue = user1.get_name().compareToIgnoreCase(user2.get_name());
				break;
			case 1:
				if(user1.get_balance() > user2.get_balance()){
					returnValue = -1;
				} else {
					returnValue = 1;
				}
				break;
			case 2:
				if(user1.get_balance() < user2.get_balance()){
					returnValue = -1;
				} else {
					returnValue = 1;
				}
				break;
		}
		
		return returnValue;
	}

}
