package com.smileyjoedev.iou;

import java.util.Comparator;


public class SortUser implements Comparator<User> {
	
	public static final int SORT_ALPHABETICAL = 0;
	public static final int SORT_DESC = 1;
	public static final int SORT_ASC = 2;
	
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
			case SortUser.SORT_ALPHABETICAL:
				returnValue = user1.getName().compareToIgnoreCase(user2.getName());
				break;
			case SortUser.SORT_DESC:
				if(user1.getBalance() > user2.getBalance()){
					returnValue = -1;
				} else {
					returnValue = 1;
				}
				break;
			case SortUser.SORT_ASC:
				if(user1.getBalance() < user2.getBalance()){
					returnValue = -1;
				} else {
					returnValue = 1;
				}
				break;
		}
		
		return returnValue;
	}

}
