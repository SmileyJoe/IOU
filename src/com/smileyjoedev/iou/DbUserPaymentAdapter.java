package com.smileyjoedev.iou;

import java.util.ArrayList;

import com.smileyjoedev.genLibrary.Notify;
import com.smileyjoedev.iou.R;

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
	
	public Payment getDetails(int paymentId){
		this.setCursor("WHERE pay._id = '" + paymentId + "' ");
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
	
	public ArrayList<Payment> getByTypeDb(int type, int userId){
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
			long relDbId = 0;
			relDbId = this.saveRel(payment.getUserId(), (int) dbId);
			if(relDbId > 0){
				Notify.toast(this.context, R.string.toast_payment_saved, payment.getDescription());
			} else {
				payment.setId((int) dbId);
				this.delete(payment, false);
				Notify.toast(this.context, R.string.toast_payment_saved_error, payment.getDescription());
			}
		} else {
			Notify.toast(this.context, R.string.toast_payment_saved_error, payment.getDescription());
		}
		
		return dbId;
	}
	
	public long saveRel(int userId, int paymentId){
		long dbId = 0;
		ContentValues values = createContentValuesRel(userId, paymentId);
		dbId = db.insert("user_rel_payment", null, values);
		
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
		this.delete(payment, true);
	}
	
	public void delete(Payment payment, boolean showToast){
		db.delete("payment", " _id='" + payment.getId() + "' ", null);
		this.deleteRel(payment);
		if(showToast){
			Notify.toast(this.context, R.string.toast_payment_deleted, payment.getDescription());
		}
	}
	
	public void deleteRel(Payment payment){
		db.delete("user_rel_payment", " payment_id='" + payment.getId() + "' ", null);
	}
	
	/******************************************
	 * GENERAL
	 *****************************************/
	
	private void setCursor(String where){
		this.cursor = this.db.rawQuery(
				"SELECT pay._id, userrel.user_id, pay.payment_amount, pay.payment_type, pay.payment_description, pay.payment_date, pay.payment_title "
				+ "FROM payment pay "
				+ "JOIN user_rel_payment userrel ON userrel.payment_id = pay._id"
				+ " " + where + " "
				+ "ORDER BY payment_date DESC", null);
	}
	
	private void setColoumns(){
		this.idCol = this.cursor.getColumnIndex("_id");
		this.userIdCol = this.cursor.getColumnIndex("user_id");
		this.amountCol = this.cursor.getColumnIndex("payment_amount");
		this.typeCol = this.cursor.getColumnIndex("payment_type");
		this.descriptionCol = this.cursor.getColumnIndex("payment_description");
		this.titleCol = this.cursor.getColumnIndex("payment_title");
		this.dateCol = this.cursor.getColumnIndex("payment_date");
	}
	
	private Payment getPaymentData(){
		Payment payment = new Payment(this.context);
		
		payment.setId(this.cursor.getInt(this.idCol));
		payment.setUserId(this.cursor.getInt(this.userIdCol));
		payment.setAmount(this.cursor.getFloat(this.amountCol));
		payment.setTypeDb(this.cursor.getInt(this.typeCol));
		payment.setDescription(this.cursor.getString(this.descriptionCol));
		payment.setTitle(this.cursor.getString(this.titleCol));
		payment.setDate(this.cursor.getLong(this.dateCol));
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
		this.cursor.close();
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
		this.cursor.close();
		return payment;
	}
	
	private ContentValues createContentValues(Payment payment) {
		ContentValues values = new ContentValues();
		
		values.put("payment_amount", payment.getAmount());
		values.put("payment_type", payment.getTypeDb());
		values.put("payment_description", payment.getDescription());
		values.put("payment_title", payment.getTitle());
		values.put("payment_date", payment.getDate());
		
		return values;
	}
	
	private ContentValues createContentValuesRel(int userId, int paymentId) {
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
