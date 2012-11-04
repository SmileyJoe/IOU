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
	
	public User get_details(int userId){
		this.set_cursor("WHERE _id = '" + userId + "' ");
		return this.sort_cursor();
	}
	
	public ArrayList<User> get(){
		this.set_cursor("");
		return this.sort_cursor_array_list();
	}
	
	public ArrayList<User> get_by_group(int groupId){
		this.set_cursor_rel_group(" WHERE group_id = '" + groupId + "'");
		return this.sort_cursor_array_list();
	}

	
	/******************************************
	 * SAVE
	 *****************************************/
	
	public long save(User user) {
		long dbId = 0;
		
		if(user.get_id() > 0){
			this.update(user);
		} else {
			if(this.check_user_exists(user.get_name())){
				Notify.toast(this.context, R.string.toast_user_exists, user.get_name());
			} else {
				ContentValues values = create_content_values(user);
				dbId = db.insert("user", null, values);
				if(dbId > 0){
					Notify.toast(this.context, R.string.toast_user_saved, user.get_name());
				} else {
					Notify.toast(this.context, R.string.toast_user_saved_error, user.get_name());
				}
				
			}
		}
		
		return dbId;
	}
	
	/******************************************
	 * UPDATE
	 *****************************************/
	
	public void update(User user) {
		ContentValues values = create_content_values(user);
		db.update("user", values, " _id = '" + user.get_id() + "' ", null);
		Notify.toast(this.context, R.string.toast_user_updated, user.get_name());
	}
	
	/******************************************
	 * DELETE
	 *****************************************/
	
	public void delete(User user){
		db.delete("user", " _id='" + user.get_id() + "' ", null);
		Notify.toast(this.context, R.string.toast_user_deleted, user.get_name());
	}
	
	/******************************************
	 * GENERAL
	 *****************************************/
	
	private void set_cursor(String where){
		this.cursor = this.db.rawQuery(
				"SELECT _id, user_name, user_id_online, user_status, user_minimalistic_text_flag, user_variable_name, user_contact_id "
				+ "FROM user " 
				+ " " + where + " "
				+ "ORDER BY user_name ASC", null);
	}
	
	private void set_cursor_rel_group(String where){
		this.cursor = this.db.rawQuery(
				"SELECT user._id, user.user_name, user.user_id_online, user.user_status, user.user_minimalistic_text_flag, user.user_variable_name, user.user_contact_id "
				+ " FROM user user "
				+ " JOIN user_rel_group relgroup ON user._id = relgroup.user_id "
				+ " " + where + " "
				+ " ORDER BY user_name ASC ", null);
	}
	
	private void set_coloumns(){
		this.idCol = this.cursor.getColumnIndex("_id");
		this.nameCol = this.cursor.getColumnIndex("user_name");
		this.onlineIdCol = this.cursor.getColumnIndex("user_id_online");
		this.statusCol = this.cursor.getColumnIndex("user_status");
		this.minimalisticTextFlagCol = this.cursor.getColumnIndex("user_minimalistic_text_flag");
		this.variableNameCol = this.cursor.getColumnIndex("user_variable_name");
		this.contactIdCol = this.cursor.getColumnIndex("user_contact_id");
	}
	
	private User get_user_data(){
		User user = new User();
		
		user.set_id(this.cursor.getInt(this.idCol));
		user.set_name(this.cursor.getString(this.nameCol));
		user.set_online_id(this.cursor.getInt(this.onlineIdCol));
		user.set_status(this.cursor.getInt(this.statusCol));
		user.set_minimalistic_text_flag(this.cursor.getInt(this.minimalisticTextFlagCol));
		user.set_variable_name(this.cursor.getString(this.variableNameCol));
		user.set_contact_id(this.cursor.getLong(this.contactIdCol));
		
		if(this.withPayments){
			user.set_payments(this.userPaymentAdapter.get_by_user(user.get_id()));
		}
		
		
		return user;
	}
	
	private ArrayList<User> sort_cursor_array_list(){
		ArrayList<User> users = new ArrayList<User>();
		this.set_coloumns();
		
		if(this.cursor != null){
			this.cursor.moveToFirst();
			if(this.cursor.getCount() > 0){
				
				int i = 0;
				do{
					users.add(this.get_user_data());
				}while(this.cursor.moveToNext());
			}
		}
		this.cursor.close();
		return users;
	}
	
	private User sort_cursor(){
		User user = new User();
		
		this.set_coloumns();
		
		if(this.cursor != null){
			this.cursor.moveToFirst();
			if(this.cursor.getCount() > 0){
				user = this.get_user_data();
			}
		}
		this.cursor.close();
		return user;
	}
	
	private ContentValues create_content_values(User user) {
		ContentValues values = new ContentValues();
		
		values.put("user_name", user.get_name());
		values.put("user_id_online", user.get_online_id());
		values.put("user_status", user.get_status());
		values.put("user_minimalistic_text_flag", user.get_minimalistic_text_flag());
		values.put("user_variable_name", user.get_variable_name());
		values.put("user_contact_id", user.get_contact_id());
		
		return values;
	}
	
	public void close(){
		this.db.close();
	}
	
	/******************************************
	 * CHECK
	 *****************************************/
	
	public boolean check_user_exists(String userName) {
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
