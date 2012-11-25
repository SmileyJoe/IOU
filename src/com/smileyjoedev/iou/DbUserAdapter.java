package com.smileyjoedev.iou;

import java.util.ArrayList;

import com.smileyjoedev.genLibrary.Notify;
import com.smileyjoedev.iou.R;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbUserAdapter {
	
	/*****************************************
	 * VARIABLES
	 ****************************************/
	
	private Context context;
	private SQLiteDatabase db;
	private DbHelper dbHelper;
	private DbUserPaymentAdapter userPaymentAdapter;
	private Cursor cursor;
	private int idCol;
	private int nameCol;
	private int onlineIdCol;
	private int statusCol;
	private int variableNameCol;
	private int minimalisticTextFlagCol;
	private int contactIdCol;
	private boolean withPayments;
	
	/*****************************************
	 * CONSTRUCTOR
	 ****************************************/
	
	public DbUserAdapter(Context context, boolean withPayments) {
		this.withPayments = withPayments;
		this.initialize(context);
	}
	
	public DbUserAdapter(Context context) {
		this.initialize(context);
		this.withPayments = true;
	}
	
	public void initialize(Context context){
		this.context = context;
		this.dbHelper = new DbHelper(context);
		this.db = dbHelper.getWritableDatabase();
		this.userPaymentAdapter = new DbUserPaymentAdapter(context);
	}
	
	/******************************************
	 * GET
	 *****************************************/
	
	public User getDetails(int userId){
		this.setCursor("WHERE _id = '" + userId + "' ");
		return this.sortCursor();
	}
	
	public ArrayList<User> get(){
		this.setCursor("");
		return this.sortCursorArrayList();
	}
	
	public ArrayList<User> getByGroup(int groupId){
		this.setCursorRelGroup(" WHERE group_id = '" + groupId + "'");
		return this.sortCursorArrayList();
	}

	
	/******************************************
	 * SAVE
	 *****************************************/
	
	public long save(User user) {
		long dbId = 0;
		
		if(user.getId() > 0){
			this.update(user);
		} else {
			if(this.checkUserExists(user.getName())){
				Notify.toast(this.context, R.string.toast_user_exists, user.getName());
			} else {
				ContentValues values = createContentValues(user);
				dbId = db.insert("user", null, values);
				if(dbId > 0){
					Notify.toast(this.context, R.string.toast_user_saved, user.getName());
				} else {
					Notify.toast(this.context, R.string.toast_user_saved_error, user.getName());
				}
				
			}
		}
		
		return dbId;
	}
	
	/******************************************
	 * UPDATE
	 *****************************************/
	
	public void update(User user) {
		ContentValues values = createContentValues(user);
		db.update("user", values, " _id = '" + user.getId() + "' ", null);
		Notify.toast(this.context, R.string.toast_user_updated, user.getName());
	}
	
	/******************************************
	 * DELETE
	 *****************************************/
	
	public void delete(User user){
		db.delete("user", " _id='" + user.getId() + "' ", null);
		Notify.toast(this.context, R.string.toast_user_deleted, user.getName());
	}
	
	/******************************************
	 * GENERAL
	 *****************************************/
	
	private void setCursor(String where){
		this.cursor = this.db.rawQuery(
				"SELECT _id, user_name, user_id_online, user_status, user_minimalistic_text_flag, user_variable_name, user_contact_id "
				+ "FROM user " 
				+ " " + where + " "
				+ "ORDER BY user_name ASC", null);
	}
	
	private void setCursorRelGroup(String where){
		this.cursor = this.db.rawQuery(
				"SELECT user._id, user.user_name, user.user_id_online, user.user_status, user.user_minimalistic_text_flag, user.user_variable_name, user.user_contact_id "
				+ " FROM user user "
				+ " JOIN user_rel_group relgroup ON user._id = relgroup.user_id "
				+ " " + where + " "
				+ " ORDER BY user_name ASC ", null);
	}
	
	private void setColoumns(){
		this.idCol = this.cursor.getColumnIndex("_id");
		this.nameCol = this.cursor.getColumnIndex("user_name");
		this.onlineIdCol = this.cursor.getColumnIndex("user_id_online");
		this.statusCol = this.cursor.getColumnIndex("user_status");
		this.minimalisticTextFlagCol = this.cursor.getColumnIndex("user_minimalistic_text_flag");
		this.variableNameCol = this.cursor.getColumnIndex("user_variable_name");
		this.contactIdCol = this.cursor.getColumnIndex("user_contact_id");
	}
	
	private User getUserData(){
		User user = new User(this.context);
		
		user.setId(this.cursor.getInt(this.idCol));
		user.setName(this.cursor.getString(this.nameCol));
		user.setOnlineId(this.cursor.getInt(this.onlineIdCol));
		user.setStatus(this.cursor.getInt(this.statusCol));
		user.setMinimalisticTextFlag(this.cursor.getInt(this.minimalisticTextFlagCol));
		user.setVariableName(this.cursor.getString(this.variableNameCol));
		user.setContactId(this.cursor.getLong(this.contactIdCol));
		
		if(this.withPayments){
			user.setPayments(this.userPaymentAdapter.getByUser(user.getId()));
		}
		
		
		return user;
	}
	
	private ArrayList<User> sortCursorArrayList(){
		ArrayList<User> users = new ArrayList<User>();
		this.setColoumns();
		
		if(this.cursor != null){
			this.cursor.moveToFirst();
			if(this.cursor.getCount() > 0){
				
				int i = 0;
				do{
					users.add(this.getUserData());
				}while(this.cursor.moveToNext());
			}
		}
		this.cursor.close();
		return users;
	}
	
	private User sortCursor(){
		User user = new User(this.context);
		
		this.setColoumns();
		
		if(this.cursor != null){
			this.cursor.moveToFirst();
			if(this.cursor.getCount() > 0){
				user = this.getUserData();
			}
		}
		this.cursor.close();
		return user;
	}
	
	private ContentValues createContentValues(User user) {
		ContentValues values = new ContentValues();
		
		values.put("user_name", user.getName());
		values.put("user_id_online", user.getOnlineId());
		values.put("user_status", user.getStatus());
		values.put("user_minimalistic_text_flag", user.getMinimalisticTextFlag());
		values.put("user_variable_name", user.getVariableName());
		values.put("user_contact_id", user.getContactId());
		
		return values;
	}
	
	public void close(){
		this.db.close();
	}
	
	/******************************************
	 * CHECK
	 *****************************************/
	
	public boolean checkUserExists(String userName) {
		boolean playerExists = false;
		
		Cursor cursor = this.db.rawQuery("SELECT _id FROM user WHERE user_name = '" + userName + "'", null);
		
		if(cursor != null){
			if(cursor.getCount() > 0){
				playerExists = true;
			}
			
		}
		
		return playerExists;
	}

}
