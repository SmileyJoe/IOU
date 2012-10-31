package com.joedev.iou;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbUserPaymentAdapter {
	
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
	private int titleCol;
	private int dateCol;
	
	/*****************************************
	 * CONSTRUCTOR
	 ****************************************/
	
	public DbUserPaymentAdapter(Context context) {
		this.context = context;
		this.dbHelper = new DbHelper(context);
		this.db = dbHelper.getWritableDatabase();
		//this.userAdapter = new DbUserAdapter(context);
		
	}
	
	/******************************************
	 * GET
	 *****************************************/
	
	public Payment get_details(int paymentId){
		this.set_cursor("WHERE pay._id = '" + paymentId + "' ");
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
			long relDbId = 0;
			relDbId = this.save_rel(payment.get_user_id(), (int) dbId);
			if(relDbId > 0){
				Notify.toast(this.context, R.string.toast_payment_saved, payment.get_description());
			} else {
				payment.set_id((int) dbId);
				this.delete(payment, false);
				Notify.toast(this.context, R.string.toast_payment_saved_error, payment.get_description());
			}
		} else {
			Notify.toast(this.context, R.string.toast_payment_saved_error, payment.get_description());
		}
		
		return dbId;
	}
	
	public long save_rel(int userId, int paymentId){
		long dbId = 0;
		ContentValues values = create_content_values_rel(userId, paymentId);
		dbId = db.insert("user_rel_payment", null, values);
		
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
		this.delete(payment, true);
	}
	
	public void delete(Payment payment, boolean showToast){
		db.delete("payment", " _id='" + payment.get_id() + "' ", null);
		this.delete_rel(payment);
		if(showToast){
			Notify.toast(this.context, R.string.toast_payment_deleted, payment.get_description());
		}
	}
	
	public void delete_rel(Payment payment){
		db.delete("user_rel_payment", " payment_id='" + payment.get_id() + "' ", null);
	}
	
	/******************************************
	 * GENERAL
	 *****************************************/
	
	private void set_cursor(String where){
		this.cursor = this.db.rawQuery(
				"SELECT pay._id, userrel.user_id, pay.payment_amount, pay.payment_type, pay.payment_description, pay.payment_date, pay.payment_title "
				+ "FROM payment pay "
				+ "JOIN user_rel_payment userrel ON userrel.payment_id = pay._id"
				+ " " + where + " "
				+ "ORDER BY payment_date DESC", null);
	}
	
	private void set_coloumns(){
		this.idCol = this.cursor.getColumnIndex("_id");
		this.userIdCol = this.cursor.getColumnIndex("user_id");
		this.amountCol = this.cursor.getColumnIndex("payment_amount");
		this.typeCol = this.cursor.getColumnIndex("payment_type");
		this.descriptionCol = this.cursor.getColumnIndex("payment_description");
		this.titleCol = this.cursor.getColumnIndex("payment_title");
		this.dateCol = this.cursor.getColumnIndex("payment_date");
	}
	
	private Payment get_payment_data(){
		Payment payment = new Payment();
		
		payment.set_id(this.cursor.getInt(this.idCol));
		payment.set_user_id(this.cursor.getInt(this.userIdCol));
		payment.set_amount(this.cursor.getFloat(this.amountCol));
		payment.set_type_db(this.cursor.getInt(this.typeCol));
		payment.set_description(this.cursor.getString(this.descriptionCol));
		payment.set_title(this.cursor.getString(this.titleCol));
		payment.set_date(this.cursor.getLong(this.dateCol));
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
		this.cursor.close();
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
		this.cursor.close();
		return payment;
	}
	
	private ContentValues create_content_values(Payment payment) {
		ContentValues values = new ContentValues();
		
		values.put("payment_amount", payment.get_amount());
		values.put("payment_type", payment.get_type_db());
		values.put("payment_description", payment.get_description());
		values.put("payment_title", payment.get_title());
		values.put("payment_date", payment.get_date());
		
		return values;
	}
	
	private ContentValues create_content_values_rel(int userId, int paymentId) {
		ContentValues values = new ContentValues();
		
		values.put("user_id", userId);
		values.put("payment_id", paymentId);
		
		return values;
	}
	
	public void close(){
		this.db.close();
	}
	
	/******************************************
	 * CHECK
	 *****************************************/
	

}
