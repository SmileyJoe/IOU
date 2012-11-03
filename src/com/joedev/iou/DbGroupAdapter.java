package com.joedev.iou;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbGroupAdapter {
	
	/*****************************************
	 * VARIABLES
	 ****************************************/
	
	private Context context;
	private SQLiteDatabase db;
	private DbHelper dbHelper;
	private Cursor cursor;
	private DbUserAdapter userAdapter;
	private int idCol;
	private int titleCol;
	private int descriptionCol;
	private DbGroupPaymentAdapter groupPaymentAdapter;
	
	/*****************************************
	 * CONSTRUCTOR
	 ****************************************/
	
	public DbGroupAdapter(Context context) {
		this.context = context;
		this.dbHelper = new DbHelper(context);
		this.db = dbHelper.getWritableDatabase();
		this.userAdapter = new DbUserAdapter(this.context, false);
		this.groupPaymentAdapter = new DbGroupPaymentAdapter(this.context);
		
	}
	
	/******************************************
	 * GET
	 *****************************************/
	
	public Group get_details(int groupId){
		this.set_cursor("WHERE _id = '" + groupId + "' ");
		return this.sort_cursor();
	}
	
	public ArrayList<Group> get(){
		this.set_cursor("");
		return this.sort_cursor_array_list();
	}
	
	/******************************************
	 * SAVE
	 *****************************************/

	public long save(Group group) {
		long dbId = 0;
		if(this.check_group_exists(group.get_title())){
			Notify.toast(this.context, R.string.toast_group_exists, group.get_title());
		} else {
			ContentValues values = create_content_values(group);
			dbId = db.insert("group_detail", null, values);
			if(dbId > 0){
				boolean usersSaved= false;
				usersSaved = this.save_users_rel(group.get_users(), (int) dbId);
				if(usersSaved){
					Notify.toast(this.context, R.string.toast_group_saved, group.get_title());
				} else {
					group.set_id((int) dbId);
					this.delete(group, false);
					Notify.toast(this.context, R.string.toast_group_saved_error, group.get_title());
				}
			} else {
				Notify.toast(this.context, R.string.toast_group_saved_error, group.get_title());
			}
		}
		
		return dbId;
	}
	
	public boolean save_users_rel(ArrayList<User> users, int groupId){
		boolean success = true;
		
		for(int i = 0; i < users.size(); i++){
			if(users.get(i).is_selected()){
				this.save_user_rel(users.get(i).get_id(), groupId);
			}
		}
		
		return success;
	}
	
	public long save_user_rel(int userId, int groupId){
		long dbId = 0;
		
		ContentValues values = create_content_values_user_rel(userId, groupId);
		dbId = db.insert("user_rel_group", null, values);
		
		return dbId;
	}
	
	/******************************************
	 * UPDATE
	 *****************************************/
	
	public int update(Group group) {
		int numRows = 0;
		if(this.check_group_exists(group.get_title(), group.get_id())){
			Notify.toast(this.context, R.string.toast_group_exists, group.get_title());
		} else {
			ContentValues values = create_content_values(group);
			numRows = this.db.update("group_detail", values, " _id = '" + group.get_id() + "' ", null);
			this.delete_users_rel(group.get_id());
			this.save_users_rel(group.get_users(), group.get_id());
			Notify.toast(this.context, R.string.toast_group_updated, group.get_title());
		}
		return numRows;
	}
	
	/******************************************
	 * DELETE
	 *****************************************/
	
	public void delete(Group group){
		this.delete(group, true);
	}
	
	public void delete(Group group, boolean showToast){
		db.delete("group_detail", " _id='" + group.get_id() + "' ", null);
		this.delete_users_rel(group.get_id());
		this.groupPaymentAdapter.delete_by_group(group.get_id());
//		this.delete_rel(group);
		if(showToast){
			Notify.toast(this.context, R.string.toast_group_deleted, group.get_title());
		}
	}
	
	public void delete_user_rel(int userId, int groupId){
		db.delete("group_rel_user", " user_id = '" + userId + "' AND group_id = '" + groupId + "' ", null);
	}
	
	public void delete_users_rel(int groupId){
		db.delete("user_rel_group", " group_id = '" + groupId + "' ", null);
	}
	
	/******************************************
	 * GENERAL
	 *****************************************/
	
	private void set_cursor(String where){
		this.cursor = this.db.rawQuery(
				"SELECT _id, group_title, group_description "
				+ "FROM group_detail "
				+ " " + where + " "
				+ "ORDER BY group_title DESC", null);
	}
	
	private void set_coloumns(){
		this.idCol = this.cursor.getColumnIndex("_id");
		this.titleCol = this.cursor.getColumnIndex("group_title");
		this.descriptionCol = this.cursor.getColumnIndex("group_description");
	}
	
	private Group get_group_data(){
		Group group = new Group();
		
		group.set_id(this.cursor.getInt(this.idCol));
		group.set_title(this.cursor.getString(this.titleCol));
		group.set_description(this.cursor.getString(this.descriptionCol));
		group.set_users(this.userAdapter.get_by_group(group.get_id()));
		
		return group;
	}
	
	private ArrayList<Group> sort_cursor_array_list(){
		ArrayList<Group> groups = new ArrayList<Group>();
		
		this.set_coloumns();
		
		if(this.cursor != null){
			this.cursor.moveToFirst();
			if(this.cursor.getCount() > 0){
				do{
					groups.add(this.get_group_data());
				}while(this.cursor.moveToNext());
			}
		}
		this.cursor.close();
		return groups;
	}
	
	private Group sort_cursor(){
		Group group = new Group();
		
		this.set_coloumns();
		
		if(this.cursor != null){
			this.cursor.moveToFirst();
			if(this.cursor.getCount() > 0){
				group = this.get_group_data();
			}
		}
		this.cursor.close();
		return group;
	}
	
	private ContentValues create_content_values(Group group) {
		ContentValues values = new ContentValues();
		
		values.put("group_title", group.get_title());
		values.put("group_description", group.get_description());
		
		return values;
	}
	
	private ContentValues create_content_values_user_rel(int userId, int groupId) {
		ContentValues values = new ContentValues();
		
		values.put("user_id", userId);
		values.put("group_id", groupId);
		
		return values;
	}

	public void close(){
		this.db.close();
	}
	
	/***********************************************
	 * CHECK
	 **********************************************/
	
	public boolean check_group_exists(String groupTitle, int groupId) {
		boolean groupExists = false;
		
		Cursor cursor = this.db.rawQuery("SELECT _id FROM group_detail WHERE group_title = '" + groupTitle + "' AND _id != '" + groupId + "' ", null);
		
		if(cursor != null){
			if(cursor.getCount() > 0){
				groupExists = true;
			}
			
		}
		
		return groupExists;
	}
	
	public boolean check_group_exists(String groupTitle) {
		boolean groupExists = false;
		
		Cursor cursor = this.db.rawQuery("SELECT _id FROM group_detail WHERE group_title = '" + groupTitle + "' ", null);
		
		if(cursor != null){
			if(cursor.getCount() > 0){
				groupExists = true;
			}
			
		}
		
		return groupExists;
	}
}
