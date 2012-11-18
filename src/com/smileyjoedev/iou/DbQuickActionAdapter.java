package com.smileyjoedev.iou;

import java.util.ArrayList;

import com.smileyjoedev.genLibrary.Notify;
import com.smileyjoedev.iou.R;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

// TODO: Delete actions when a group or user is deleted //

public class DbQuickActionAdapter {
	
	/*****************************************
	 * VARIABLES
	 ****************************************/
	
	private Context context;
	private SQLiteDatabase db;
	private DbHelper dbHelper;
	private Cursor cursor;
	private int idCol;
	private int typeCol;
	private int actionCol;
	private int targetIdCol;
	
	/*****************************************
	 * CONSTRUCTOR
	 ****************************************/
	
	public DbQuickActionAdapter(Context context) {
		this.context = context;
		this.dbHelper = new DbHelper(context);
		this.db = dbHelper.getWritableDatabase();
	}
	
	/******************************************
	 * GET
	 *****************************************/
	
	public QuickAction getDetails(long quickActionId){
		this.setCursor("WHERE _id = '" + quickActionId + "' ");
		return this.sortCursor();
	}
	
	public ArrayList<QuickAction> get(){
		this.setCursor("");
		return this.sortCursorArrayList();
	}
	
	/******************************************
	 * SAVE
	 *****************************************/
	
	public long save(QuickAction quickAction) {
		long dbId = 0;
		
		if(quickAction.getId() > 0){
			this.update(quickAction);
			dbId = quickAction.getId();
		} else {
			ContentValues values = createContentValues(quickAction);
			dbId = db.insert("quick_action", null, values);
			if(dbId > 0){
				Notify.toast(this.context, R.string.toast_quick_action_saved, quickAction.getDescription());
			} else {
				Notify.toast(this.context, R.string.toast_quick_action_saved_error, quickAction.getDescription());
			}
		}
		
		return dbId;
	}
	
	/******************************************
	 * UPDATE
	 *****************************************/
	
	public void update(QuickAction quickAction) {
		ContentValues values = createContentValues(quickAction);
		db.update("quick_action", values, " _id = '" + quickAction.getId() + "' ", null);
		Notify.toast(this.context, R.string.toast_quick_action_updated, quickAction.getDescription());
	}
	
	/******************************************
	 * DELETE
	 *****************************************/
	
	public void delete(QuickAction quickAction){
		db.delete("quick_action", " _id='" + quickAction.getId() + "' ", null);
		Notify.toast(this.context, R.string.toast_quick_action_deleted, quickAction.getDescription());
	}
	
	/******************************************
	 * GENERAL
	 *****************************************/
	
	private void setCursor(String where){
		this.cursor = this.db.rawQuery(
				"SELECT _id, quick_action_type, quick_action_action, quick_action_target_id "
				+ "FROM quick_action " 
				+ " " + where + " "
				+ "ORDER BY _id ASC", null);
	}
	
	private void setColoumns(){
		this.idCol = this.cursor.getColumnIndex("_id");
		this.typeCol = this.cursor.getColumnIndex("quick_action_type");
		this.actionCol = this.cursor.getColumnIndex("quick_action_action");
		this.targetIdCol = this.cursor.getColumnIndex("quick_action_target_id");
	}
	
	private QuickAction getQuickActionData(){
		QuickAction quickAction = new QuickAction(this.context);
		
		quickAction.setId(this.cursor.getInt(this.idCol));
		quickAction.setType(this.cursor.getInt(this.typeCol));
		quickAction.setAction(this.cursor.getInt(this.actionCol));
		quickAction.setTargetId(this.cursor.getInt(this.targetIdCol));
		
		return quickAction;
	}
	
	private ArrayList<QuickAction> sortCursorArrayList(){
		ArrayList<QuickAction> quickActions = new ArrayList<QuickAction>();
		this.setColoumns();
		
		if(this.cursor != null){
			this.cursor.moveToFirst();
			if(this.cursor.getCount() > 0){
				
				int i = 0;
				do{
					quickActions.add(this.getQuickActionData());
				}while(this.cursor.moveToNext());
			}
		}
		this.cursor.close();
		return quickActions;
	}
	
	private QuickAction sortCursor(){
		QuickAction quickAction = new QuickAction(this.context);
		
		this.setColoumns();
		
		if(this.cursor != null){
			this.cursor.moveToFirst();
			if(this.cursor.getCount() > 0){
				quickAction = this.getQuickActionData();
			}
		}
		this.cursor.close();
		return quickAction;
	}
	
	private ContentValues createContentValues(QuickAction user) {
		ContentValues values = new ContentValues();
		
		values.put("quick_action_type", user.getType());
		values.put("quick_action_action", user.getAction());
		values.put("quick_action_target_id", user.getTargetId());
		
		return values;
	}
	
	public void close(){
		this.db.close();
	}
	
	/******************************************
	 * CHECK
	 *****************************************/
	
}
