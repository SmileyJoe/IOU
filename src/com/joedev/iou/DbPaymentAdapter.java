package com.joedev.iou;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbPaymentAdapter {
	
	/*****************************************
	 * VARIABLES
	 ****************************************/
	
	private Context context;
	private SQLiteDatabase db;
	private DbHelper dbHelper;
	private DbUserAdapter userAdapter;
	private Cursor cursor;
	private int idCol;
	private int userIdCol;
	private int amountCol;
	private int typeCol;
	private int descriptionCol;
	private int dateCol;
	private int titleCol;
	
	/*****************************************
	 * CONSTRUCTOR
	 ****************************************/
	
	public DbPaymentAdapter(Context context) {
		this.context = context;
		this.dbHelper = new DbHelper(context);
		this.db = dbHelper.getWritableDatabase();
		//this.userAdapter = new DbUserAdapter(context);
	}
	
	/******************************************
	 * GET
	 *****************************************/
	
	public Payment get_details(int paymentId){
		this.set_cursor("WHERE _id = '" + paymentId + "' ");
		return this.sort_cursor();
	}
	
	public ArrayList<Payment> get(){
		this.set_cursor("");
		return this.sort_cursor_array_list();
	}
	
	public ArrayList<Payment> get_by_user(int userId){
		this.set_cursor("WHERE user_id = '" + userId + "' ");
		return this.sort_cursor_array_list();
	}	
	
	public ArrayList<Payment> get_by_type_db(int type, int userId){
		this.set_cursor("WHERE payment_type = '" + type + "' AND user_id = '" + userId + "' ");
		return this.sort_cursor_array_list();
	}	
	
	/******************************************
	 * SAVE
	 *****************************************/
	
	public long save(Payment payment) {
		long dbId = 0;
		ContentValues values = create_content_values(payment);
		dbId = db.insert("payment", null, values);
		if(dbId > 0){
			Notify.toast(this.context, R.string.toast_payment_saved, payment.get_description());
		} else {
			Notify.toast(this.context, R.string.toast_payment_saved_error, payment.get_description());
		}
		
		return dbId;
	}
	
	/******************************************
	 * UPDATE
	 *****************************************/
	
	public void update(Payment payment) {
		ContentValues values = create_content_values(payment);
		db.update("payment", values, " _id = '" + payment.get_id() + "' ", null);
		Notify.toast(this.context, R.string.toast_payment_updated, payment.get_description());
	}
	
	/******************************************
	 * DELETE
	 *****************************************/
	
	public void delete(Payment payment){
		db.delete("payment", " _id='" + payment.get_id() + "' ", null);
		Notify.toast(this.context, R.string.toast_payment_deleted, payment.get_description());
	}
	
	/******************************************
	 * GENERAL
	 *****************************************/
	
	private void set_cursor(String where){
		Debug.v("WHERE ", where);
		this.cursor = this.db.rawQuery(
				"SELECT _id, user_id, payment_amount, payment_type, payment_description, payment_date, payment_title "
				+ "FROM payment " 
				+ " " + where + " "
				+ "ORDER BY payment_date DESC", null);
	}
	
	private void set_coloumns(){
		this.idCol = this.cursor.getColumnIndex("_id");
		this.userIdCol = this.cursor.getColumnIndex("user_id");
		this.amountCol = this.cursor.getColumnIndex("payment_amount");
		this.typeCol = this.cursor.getColumnIndex("payment_type");
		this.descriptionCol = this.cursor.getColumnIndex("payment_description");
		this.dateCol = this.cursor.getColumnIndex("payment_date");
		this.titleCol = this.cursor.getColumnIndex("payment_title");
	}
	
	private Payment get_payment_data(){
		Payment payment = new Payment();
		
		payment.set_id(this.cursor.getInt(this.idCol));
		payment.set_user_id(this.cursor.getInt(this.userIdCol));
		payment.set_amount(this.cursor.getFloat(this.amountCol));
		payment.set_type_db(this.cursor.getInt(this.typeCol));
		payment.set_description(this.cursor.getString(this.descriptionCol));
		payment.set_date(this.cursor.getLong(this.dateCol));
		payment.set_title(this.cursor.getString(this.titleCol));
		//payment.set_user(this.userAdapter.get_details(payment.get_user_id()));
		
		return payment;
	}
	
	private ArrayList<Payment> sort_cursor_array_list(){
		ArrayList<Payment> payments = new ArrayList<Payment>();
		
		this.set_coloumns();
		
		if(this.cursor != null){
			this.cursor.moveToFirst();
			if(this.cursor.getCount() > 0){
				do{
					payments.add(this.get_payment_data());
				}while(this.cursor.moveToNext());
			}
		}
		
		return payments;
	}
	
	private Payment sort_cursor(){
		Payment payment = new Payment();
		
		this.set_coloumns();
		
		if(this.cursor != null){
			this.cursor.moveToFirst();
			if(this.cursor.getCount() > 0){
				payment = this.get_payment_data();
			}
		}
		
		return payment;
	}
	
	private ContentValues create_content_values(Payment payment) {
		ContentValues values = new ContentValues();
		
		values.put("user_id", payment.get_user_id());
		values.put("payment_amount", payment.get_amount());
		values.put("payment_type", payment.get_type_db());
		values.put("payment_description", payment.get_description());
		values.put("payment_date", payment.get_date());
		values.put("payment_title", payment.get_title());
		
		return values;
	}
	
	/******************************************
	 * CHECK
	 *****************************************/
	

}
