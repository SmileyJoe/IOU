/*************************************
 * Constants for the application
 ************************************/

package com.smileyjoedev.iou;

import java.io.File;

import android.os.Environment;

public class Constants {
	
	public static final String DB_NAME = "iou";
	public static final String APP_FILEPATH = Environment.getExternalStorageDirectory() + File.separator + "com.smileyjoedev" + File.separator + "iou" + File.separator;
	public static final String DB_FILEPATH = "/data/data/com.smileyjoedev.iou/databases/" + Constants.DB_NAME;
	public static final String DB_BACKUP_FILEPATH = Constants.APP_FILEPATH + Constants.DB_NAME;
	public static final int DB_VERSION = 22;
	
	public static final String PREFERENCE_NAME = "iou";
	
	/*********************************************************
	 * Give unique ids to the Activities
	 * 
	 * This is used for switch statements in functions like
	 * onActivityResult()
	 ********************************************************/
	
	public static final int ACTIVITY_NEW_USER = 1;
	public static final int ACTIVITY_NEW_PAYMENT = 2;
	public static final int ACTIVITY_USER_VIEW = 3;
	public static final int ACTIVITY_POPUP_DELETE = 4;
	public static final int ACTIVITY_TAKE_PHOTO = 5;
	public static final int ACTIVITY_SETTINGS = 6;
	public static final int ACTIVITY_REPAY_PAYMENT = 7;
	public static final int ACTIVITY_EDIT_PAYMENT = 8;
	public static final int ACTIVITY_REPAY_PAYMENT_USER = 9;
	public static final int ACTIVITY_EDIT_USER = 10;
	public static final int ACTIVITY_GROUP_NEW = 11;
	public static final int ACTIVITY_GROUP_EDIT = 12;
	public static final int ACTIVITY_GROUP_VIEW = 13;
	public static final int ACTIVITY_GROUP_PAYMENT_NEW = 14;
	public static final int ACTIVITY_GROUP_REPAYMENT = 15;
	public static final int ACTIVITY_GROUP_PAYMENT_EDIT = 16;
	public static final int ACTIVITY_GROUP_USER_PAYMENT_DETAILS = 17;
	public static final int ACTIVITY_USER_LIST = 18;
	public static final int ACTIVITY_GROUP_LIST = 19;
	public static final int ACTIVITY_QUICK_ACTION_NEW = 20;
	public static final int ACTIVITY_QUICK_ACTION_TARGET_PICKER = 21;
	public static final int ACTIVITY_QUICK_ACTION_EXCECUTE = 22;
	public static final int ACTIVITY_QUICK_ACTION_EDIT = 23;
	public static final int ACTIVITY_START_PAGE = 24;
	public static final int ACTIVITY_DATE_PICKER = 25;
	public static final int ACTIVITY_POPUP_DELETE_USER = 26;
	
	public static final int PAYMENT = 101;
	public static final int USER = 102;
	public static final int EXIT_APP = 103;
	public static final int GROUP = 104;
	public static final int QUICK_ACTION = 105;
	
	public static final int THEME_DEFAULT = 0;
	public static final int THEME_DARK = 1;
	public static final int THEME_LIGHT = 2;
	
	public static final int CONTEXT_EDIT = 0;
	public static final int CONTEXT_DELETE = 1;
	public static final int CONTEXT_REPAY_ALL = 2;
	public static final int CONTEXT_REPAY_SOME = 3;
	public static final int CONTEXT_REMINDER_EMAIL = 4;
	public static final int CONTEXT_REMINDER_SMS = 5;
	public static final int CONTEXT_VIEW_CONTACT_CARD = 6;
	public static final int CONTEXT_PERSISTENT_NOTIFICATION = 7;
	
	public static final String ACTION_WIDGET_QUICK_ADD = "com.smileyjoedev.iou.quickAdd";
}
