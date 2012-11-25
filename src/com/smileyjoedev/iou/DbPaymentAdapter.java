package com.smileyjoedev.iou;

import java.util.ArrayList;

import com.smileyjoedev.genLibrary.Notify;
import com.smileyjoedev.iou.R;

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
	
	public Payment getDetails(int paymentId){
		this.setCursor("WHERE _id = '" + paymentId + "' ");
		return this.sortCursor();
	}
	
	public ArrayList<Payment> get(){
		this.setCursor("");
		return this.sortCursorArrayList();
	}
	
	public ArrayList<Payment> getByUser(int userId){
		this.setCursor("WHERE user_id = '" + userId + "' ");
		return this.sortCursorArrayList();
	}	
	
	public ArrayList<Payment> getByType_db(int type, int userId){
		this.setCursor("WHERE payment_type = '" + type + "' AND user_id = '" + userId + "' ");
		return this.sortCursorArrayList();
	}	
	
	/******************************************
	 * SAVE
	 *****************************************/
	
	public long save(Payment payment) {
		long dbId = 0;
		ContentValues values = createContentValues(payment);
		dbId = db.insert("payment", null, values);
		if(dbId > 0){
			Notify.toast(this.context, R.string.toast_payment_saved, payment.getDescription());
		} else {
			Notify.toast(this.context, R.string.toast_payment_saved_error, payment.getDescription());
		}
		
		return dbId;
	}
	
	/******************************************
	 * UPDATE
	 *****************************************/
	
	public void update(Payment payment) {
		ContentValues values = createContentValues(payment);
		db.update("payment", values, " _id = '" + payment.getId() + "' ", null);
		Notify.toast(this.context, R.string.toast_payment_updated, payment.getDescription());
	}
	
	/******************************************
	 * DELETE
	 *****************************************/
	
	public void delete(Payment payment){
		db.delete("payment", " _id='" + payment.getId() + "' ", null);
		Notify.toast(this.context, R.string.toast_payment_deleted, payment.getDescription());
	}
	
	/******************************************
	 * GENERAL
	 *****************************************/
	
	private void setCursor(String where){
		this.cursor = this.db.rawQuery(
				"SELECT _id, user_id, payment_amount, payment_type, payment_description, payment_date, payment_title "
				+ "FROM payment " 
				+ " " + where + " "
				+ "ORDER BY payment_date DESC", null);
	}
	
	private void setColoumns(){
		this.idCol = this.cursor.getColumnIndex("_id");
		this.userIdCol = this.cursor.getColumnIndex("user_id");
		this.amountCol = this.cursor.getColumnIndex("payment_amount");
		this.typeCol = this.cursor.getColumnIndex("payment_type");
		this.descriptionCol = this.cursor.getColumnIndex("payment_description");
		this.dateCol = this.cursor.getColumnIndex("payment_date");
		this.titleCol = this.cursor.getColumnIndex("payment_title");
	}
	
	private Payment getPaymentData(){
		Payment payment = new Payment(this.context);
		
		payment.setId(this.cursor.getInt(this.idCol));
		payment.setUserId(this.cursor.getInt(this.userIdCol));
		payment.setAmount(this.cursor.getFloat(this.amountCol));
		payment.setTypeDb(this.cursor.getInt(this.typeCol));
		payment.setDescription(this.cursor.getString(this.descriptionCol));
		payment.setDate(this.cursor.getLong(this.dateCol));
		payment.setTitle(this.cursor.getString(this.titleCol));
		//payment.set_user(this.userAdapter.get_details(payment.get_user_id()));
		
		return payment;
	}
	
	private ArrayList<Payment> sortCursorArrayList(){
		ArrayList<Payment> payments = new ArrayList<Payment>();
		
		this.setColoumns();
		
		if(this.cursor != null){
			this.cursor.moveToFirst();
			if(this.cursor.getCount() > 0){
				do{
					payments.add(this.getPaymentData());
				}while(this.cursor.moveToNext());
			}
		}
		
		return payments;
	}
	
	private Payment sortCursor(){
		Payment payment = new Payment(this.context);
		
		this.setColoumns();
		
		if(this.cursor != null){
			this.cursor.moveToFirst();
			if(this.cursor.getCount() > 0){
				payment = this.getPaymentData();
			}
		}
		
		return payment;
	}
	
	private ContentValues createContentValues(Payment payment) {
		ContentValues values = new ContentValues();
		
		values.put("user_id", payment.getUserId());
		values.put("payment_amount", payment.getAmount());
		values.put("payment_type", payment.getTypeDb());
		values.put("payment_description", payment.getDescription());
		values.put("payment_date", payment.getDate());
		values.put("payment_title", payment.getTitle());
		
		return values;
	}
	
	/******************************************
	 * CHECK
	 *****************************************/
	

}
