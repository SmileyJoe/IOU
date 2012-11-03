package com.joedev.iou;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DbGroupPaymentAdapter {
	
	/*****************************************
	 * VARIABLES
	 ****************************************/
	
	private Context context;
	private SQLiteDatabase db;
	private DbHelper dbHelper;
	private DbUserAdapter userAdapter;
	private Cursor cursor;
	private int idCol;
	private int groupIdCol;
	private int amountCol;
	private int typeCol;
	private int titleCol;
	private int descriptionCol;
	private int dateCol;
	private int splitAmountCol;
	
	/*****************************************
	 * CONSTRUCTOR
	 ****************************************/
	
	public DbGroupPaymentAdapter(Context context) {
		this.context = context;
		this.dbHelper = new DbHelper(context);
		this.db = dbHelper.getWritableDatabase();
		this.userAdapter = new DbUserAdapter(context, false);
		
	}
	
	/*********************************************************
	 * GET
	 ********************************************************/

	public GroupPayment get_details(int paymentId){
		this.set_cursor("WHERE pay._id = '" + paymentId + "' ");
		return this.sort_cursor();
	}
	
	public ArrayList<GroupPayment> get_by_group(int groupId){
		Debug.v("Group id", groupId);
		this.set_cursor(" WHERE grouprel.group_id = '" + groupId + "'");
		return this.sort_cursor_array_list();
	}
	
	/*********************************************************
	 * SAVE
	 ********************************************************/
	
	public long save(GroupPayment payment) {
		long dbId = 0;
		ContentValues values = create_content_values(payment);
		dbId = db.insert("payment", null, values);
		if(dbId > 0){
			long relDbId = 0;
			relDbId = this.save_rel(payment.get_group_id(), (int) dbId);
			if(relDbId > 0){
				this.save_splits(payment.get_splits(), (int) dbId);
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
	
	public long save_rel(int groupId, int paymentId){
		long dbId = 0;
		ContentValues values = create_content_values_rel(groupId, paymentId);
		dbId = db.insert("group_rel_payment", null, values);
		
		return dbId;
	}
	
	public void save_splits(ArrayList<PaymentSplit> splits, int paymentId){
		
		for(int i = 0; i < splits.size(); i++){
			splits.get(i).set_payment_id(paymentId);
			this.save_split(splits.get(i));
		}
	}
	
	public long save_split(PaymentSplit split){
		long dbId = 0;
		ContentValues values = create_content_values_split(split);
		dbId = db.insert("payment_split", null, values);
		
		return dbId;
	}
	
	/*********************************************************
	 * UPDATE
	 ********************************************************/
	
	public void update(GroupPayment payment) {
		ContentValues values = create_content_values(payment);
		db.update("payment", values, " _id = '" + payment.get_id() + "' ", null);
		this.update_splits(payment.get_splits(), payment.get_id());
		Notify.toast(this.context, R.string.toast_payment_updated, payment.get_description());
	}
	
	public void update_splits(ArrayList<PaymentSplit> splits, int paymentId){
		this.delete_splits(paymentId);
		this.save_splits(splits, paymentId);
	}
	
	/*********************************************************
	 * DELETE
	 ********************************************************/
	
	public void delete(GroupPayment payment){
		this.delete(payment, true);
	}
	
	public void delete(GroupPayment payment, boolean showToast){
		db.delete("payment", " _id='" + payment.get_id() + "' ", null);
		this.delete_rel(payment.get_id());
		this.delete_splits(payment.get_id());
		if(showToast){
			Notify.toast(this.context, R.string.toast_payment_deleted, payment.get_description());
		}
	}
	
	public void delete_by_group(int groupId){
		Debug.v("Delete payment start");
		ArrayList<Integer> paymentIds = new ArrayList<Integer>();
		
		Cursor cursor = this.db.rawQuery("SELECT payment_id FROM group_rel_payment WHERE group_id = '" + groupId + "'", null);
		
		
		
		if(cursor != null){
			Debug.v("Cursor size", cursor.getCount());
			if(cursor.getCount() > 0){
				cursor.moveToFirst();
				do{
					Debug.v("add payment", cursor.getInt(cursor.getColumnIndex("payment_id")));
					paymentIds.add(cursor.getInt(cursor.getColumnIndex("payment_id")));
				}while(cursor.moveToNext());
			}
		}
		
		Debug.v("Payment count", paymentIds.size());
		
		for(int i = 0; i < paymentIds.size(); i++){
			Debug.v("payment being deleted", paymentIds.get(i));
			db.delete("payment", " _id='" + paymentIds.get(i) + "' ", null);
			this.delete_rel(paymentIds.get(i));
			this.delete_splits(paymentIds.get(i));
		}
	}
	
	public void delete_rel(int paymentId){
		db.delete("group_rel_payment", " payment_id='" + paymentId + "' ", null);
	}
	
	public void delete_splits(int paymentId){
		db.delete("payment_split", " payment_id='" + paymentId + "' ", null);
	}
	
	/*********************************************************
	 * GENERAL
	 ********************************************************/
	
	private void set_cursor(String where){
		this.cursor = this.db.rawQuery(
				"SELECT pay._id, grouprel.group_id, pay.payment_amount, pay.payment_title, pay.payment_description, pay.payment_date "
				+ "FROM payment pay "
				+ "JOIN group_rel_payment grouprel ON grouprel.payment_id = pay._id"
				+ " " + where + " "
				+ "ORDER BY payment_date DESC", null);
	}
	
	private void set_coloumns(){
		this.idCol = this.cursor.getColumnIndex("_id");
		this.groupIdCol = this.cursor.getColumnIndex("group_id");
		this.amountCol = this.cursor.getColumnIndex("payment_amount");
		this.titleCol = this.cursor.getColumnIndex("payment_title");
		this.descriptionCol = this.cursor.getColumnIndex("payment_description");
		this.dateCol = this.cursor.getColumnIndex("payment_date");
	}
	
	private ArrayList<PaymentSplit> get_split(int id){
		ArrayList<PaymentSplit> splits = new ArrayList<PaymentSplit>();
		PaymentSplit split = new PaymentSplit();
		Cursor cursor = this.db.rawQuery(
				"SELECT _id, payment_id, user_id, payment_amount, split_type"
				+ " FROM payment_split "
				+ " WHERE payment_id = '" + id + "'", null);
		
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				split = new PaymentSplit();
				split.set_id(cursor.getInt(cursor.getColumnIndex("_id")));
				split.set_amount(cursor.getFloat(cursor.getColumnIndex("payment_amount")));
				split.set_type(cursor.getInt(cursor.getColumnIndex("split_type")));
				split.set_user_Id(cursor.getInt(cursor.getColumnIndex("user_id")));
				split.set_payment_id(cursor.getInt(cursor.getColumnIndex("payment_id")));
				split.set_user(this.userAdapter.get_details(split.get_user_id()));
				splits.add(split);
			}while(cursor.moveToNext());
		}
		
		return splits;
	}
	
	private GroupPayment get_payment_data(){
		GroupPayment payment = new GroupPayment();
		
		payment.set_id(this.cursor.getInt(this.idCol));
		payment.set_amount(this.cursor.getFloat(this.amountCol));
		payment.set_title(this.cursor.getString(this.titleCol));
		payment.set_description(this.cursor.getString(this.descriptionCol));
		payment.set_pdt(this.cursor.getLong(this.dateCol));
		payment.set_splits(this.get_split(payment.get_id()));
		
		return payment;
	}
	
	private ArrayList<GroupPayment> sort_cursor_array_list(){
		ArrayList<GroupPayment> payments = new ArrayList<GroupPayment>();
		
		this.set_coloumns();
		
		if(this.cursor != null){
			Debug.v("Cursor not null");
			this.cursor.moveToFirst();
			if(this.cursor.getCount() > 0){
				Debug.v("Cursor count", this.cursor.getCount());
				do{
					payments.add(this.get_payment_data());
				}while(this.cursor.moveToNext());
			}
		}
		this.cursor.close();
		return payments;
	}
	
	private GroupPayment sort_cursor(){
		GroupPayment payment = new GroupPayment();
		
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
	
	private ContentValues create_content_values(GroupPayment payment) {
		ContentValues values = new ContentValues();
		
		values.put("payment_amount", payment.get_amount());
		values.put("payment_type", payment.get_type());
		values.put("payment_title", payment.get_title());
		values.put("payment_description", payment.get_description());
		values.put("payment_date", payment.get_pdt());
		
		return values;
	}
	
	private ContentValues create_content_values_split(PaymentSplit paymentSplit) {
		ContentValues values = new ContentValues();
		
		values.put("payment_id", paymentSplit.get_payment_id());
		values.put("user_id", paymentSplit.get_user_id());
		values.put("payment_amount", paymentSplit.get_amount());
		values.put("split_type", paymentSplit.get_type());
		
		return values;
	}
	
	private ContentValues create_content_values_rel(int groupId, int paymentId) {
		ContentValues values = new ContentValues();
		
		values.put("group_id", groupId);
		values.put("payment_id", paymentId);
		
		return values;
	}
	
	/*********************************************************
	 * CHECK
	 ********************************************************/
	
}
