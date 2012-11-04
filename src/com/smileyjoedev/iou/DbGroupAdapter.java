package com.smileyjoedev.iou;

import java.util.ArrayList;

import com.smileyjoedev.genLibrary.Notify;
import com.smileyjoedev.iou.R;

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
	
	public Group getDetails(int groupId){
		this.setCursor("WHERE _id = '" + groupId + "' ");
		return this.sortCursor();
	}
	
	public ArrayList<Group> get(){
		this.setCursor("");
		return this.sortCursorArrayList();
	}
	
	/******************************************
	 * SAVE
	 *****************************************/

	public long save(Group group) {
		long dbId = 0;
		if(this.checkGroupExists(group.getTitle())){
			Notify.toast(this.context, R.string.toast_group_exists, group.getTitle());
		} else {
			ContentValues values = createContentValues(group);
			dbId = db.insert("group_detail", null, values);
			if(dbId > 0){
				boolean usersSaved= false;
				usersSaved = this.saveUsersRel(group.getUsers(), (int) dbId);
				if(usersSaved){
					Notify.toast(this.context, R.string.toast_group_saved, group.getTitle());
				} else {
					group.setId((int) dbId);
					this.delete(group, false);
					Notify.toast(this.context, R.string.toast_group_saved_error, group.getTitle());
				}
			} else {
				Notify.toast(this.context, R.string.toast_group_saved_error, group.getTitle());
			}
		}
		
		return dbId;
	}
	
	public boolean saveUsersRel(ArrayList<User> users, int groupId){
		boolean success = true;
		
		for(int i = 0; i < users.size(); i++){
			if(users.get(i).isSelected()){
				this.saveUserRel(users.get(i).getId(), groupId);
			}
		}
		
		return success;
	}
	
	public long saveUserRel(int userId, int groupId){
		long dbId = 0;
		
		ContentValues values = createContentValuesUserRel(userId, groupId);
		dbId = db.insert("user_rel_group", null, values);
		
		return dbId;
	}
	
	/******************************************
	 * UPDATE
	 *****************************************/
	
	public int update(Group group) {
		int numRows = 0;
		if(this.checkGroupExists(group.getTitle(), group.getId())){
			Notify.toast(this.context, R.string.toast_group_exists, group.getTitle());
		} else {
			ContentValues values = createContentValues(group);
			numRows = this.db.update("group_detail", values, " _id = '" + group.getId() + "' ", null);
			this.deleteUsersRel(group.getId());
			this.saveUsersRel(group.getUsers(), group.getId());
			Notify.toast(this.context, R.string.toast_group_updated, group.getTitle());
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
		db.delete("group_detail", " _id='" + group.getId() + "' ", null);
		this.deleteUsersRel(group.getId());
		this.groupPaymentAdapter.deleteByGroup(group.getId());
//		this.delete_rel(group);
		if(showToast){
			Notify.toast(this.context, R.string.toast_group_deleted, group.getTitle());
		}
	}
	
	public void deleteUserRel(int userId, int groupId){
		db.delete("group_rel_user", " user_id = '" + userId + "' AND group_id = '" + groupId + "' ", null);
	}
	
	public void deleteUsersRel(int groupId){
		db.delete("user_rel_group", " group_id = '" + groupId + "' ", null);
	}
	
	/******************************************
	 * GENERAL
	 *****************************************/
	
	private void setCursor(String where){
		this.cursor = this.db.rawQuery(
				"SELECT _id, group_title, group_description "
				+ "FROM group_detail "
				+ " " + where + " "
				+ "ORDER BY group_title DESC", null);
	}
	
	private void setColoumns(){
		this.idCol = this.cursor.getColumnIndex("_id");
		this.titleCol = this.cursor.getColumnIndex("group_title");
		this.descriptionCol = this.cursor.getColumnIndex("group_description");
	}
	
	private Group getGroupData(){
		Group group = new Group();
		
		group.setId(this.cursor.getInt(this.idCol));
		group.setTitle(this.cursor.getString(this.titleCol));
		group.setDescription(this.cursor.getString(this.descriptionCol));
		group.setUsers(this.userAdapter.getByGroup(group.getId()));
		
		return group;
	}
	
	private ArrayList<Group> sortCursorArrayList(){
		ArrayList<Group> groups = new ArrayList<Group>();
		
		this.setColoumns();
		
		if(this.cursor != null){
			this.cursor.moveToFirst();
			if(this.cursor.getCount() > 0){
				do{
					groups.add(this.getGroupData());
				}while(this.cursor.moveToNext());
			}
		}
		this.cursor.close();
		return groups;
	}
	
	private Group sortCursor(){
		Group group = new Group();
		
		this.setColoumns();
		
		if(this.cursor != null){
			this.cursor.moveToFirst();
			if(this.cursor.getCount() > 0){
				group = this.getGroupData();
			}
		}
		this.cursor.close();
		return group;
	}
	
	private ContentValues createContentValues(Group group) {
		ContentValues values = new ContentValues();
		
		values.put("group_title", group.getTitle());
		values.put("group_description", group.getDescription());
		
		return values;
	}
	
	private ContentValues createContentValuesUserRel(int userId, int groupId) {
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
	
	public boolean checkGroupExists(String groupTitle, int groupId) {
		boolean groupExists = false;
		
		Cursor cursor = this.db.rawQuery("SELECT _id FROM group_detail WHERE group_title = '" + groupTitle + "' AND _id != '" + groupId + "' ", null);
		
		if(cursor != null){
			if(cursor.getCount() > 0){
				groupExists = true;
			}
			
		}
		
		return groupExists;
	}
	
	public boolean checkGroupExists(String groupTitle) {
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
