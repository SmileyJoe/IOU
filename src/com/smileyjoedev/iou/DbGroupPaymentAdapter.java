package com.smileyjoedev.iou;

import java.util.ArrayList;

import com.smileyjoedev.genLibrary.Notify;
import com.smileyjoedev.iou.R;

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

	public GroupPayment getDetails(int paymentId){
		this.setCursor("WHERE pay._id = '" + paymentId + "' ");
		return this.sortCursor();
	}
	
	public ArrayList<GroupPayment> getByGroup(int groupId){
		this.setCursor(" WHERE grouprel.group_id = '" + groupId + "'");
		return this.sortCursorArrayList();
	}
	
	/*********************************************************
	 * SAVE
	 ********************************************************/
	
	public long save(GroupPayment payment) {
		long dbId = 0;
		ContentValues values = createContentValues(payment);
		dbId = db.insert("payment", null, values);
		if(dbId > 0){
			long relDbId = 0;
			relDbId = this.saveRel(payment.getGroupId(), (int) dbId);
			if(relDbId > 0){
				this.saveSplits(payment.getSplits(), (int) dbId);
				Notify.toast(this.context, R.string.toast_payment_saved, payment.getTitle());
			} else {
				payment.setId((int) dbId);
				this.delete(payment, false);
				Notify.toast(this.context, R.string.toast_payment_saved_error, payment.getTitle());
			}
		} else {
			Notify.toast(this.context, R.string.toast_payment_saved_error, payment.getTitle());
		}
		
		return dbId;
	}
	
	public long saveRel(int groupId, int paymentId){
		long dbId = 0;
		ContentValues values = createContentValuesRel(groupId, paymentId);
		dbId = db.insert("group_rel_payment", null, values);
		
		return dbId;
	}
	
	public void saveSplits(ArrayList<PaymentSplit> splits, int paymentId){
		
		for(int i = 0; i < splits.size(); i++){
			splits.get(i).setPaymentId(paymentId);
			this.saveSplit(splits.get(i));
		}
	}
	
	public long saveSplit(PaymentSplit split){
		long dbId = 0;
		ContentValues values = createContentValuesSplit(split);
		dbId = db.insert("payment_split", null, values);
		
		return dbId;
	}
	
	/*********************************************************
	 * UPDATE
	 ********************************************************/
	
	public void update(GroupPayment payment) {
		ContentValues values = createContentValues(payment);
		db.update("payment", values, " _id = '" + payment.getId() + "' ", null);
		this.updateSplits(payment.getSplits(), payment.getId());
		Notify.toast(this.context, R.string.toast_payment_updated, payment.getTitle());
	}
	
	public void updateSplits(ArrayList<PaymentSplit> splits, int paymentId){
		this.deleteSplits(paymentId);
		this.saveSplits(splits, paymentId);
	}
	
	/*********************************************************
	 * DELETE
	 ********************************************************/
	
	public void delete(GroupPayment payment){
		this.delete(payment, true);
	}
	
	public void delete(GroupPayment payment, boolean showToast){
		db.delete("payment", " _id='" + payment.getId() + "' ", null);
		this.deleteRel(payment.getId());
		this.deleteSplits(payment.getId());
		if(showToast){
			Notify.toast(this.context, R.string.toast_payment_deleted, payment.getTitle());
		}
	}
	
	public void deleteByGroup(int groupId){
		ArrayList<Integer> paymentIds = new ArrayList<Integer>();
		
		Cursor cursor = this.db.rawQuery("SELECT payment_id FROM group_rel_payment WHERE group_id = '" + groupId + "'", null);
		
		
		
		if(cursor != null){
			if(cursor.getCount() > 0){
				cursor.moveToFirst();
				do{
					paymentIds.add(cursor.getInt(cursor.getColumnIndex("payment_id")));
				}while(cursor.moveToNext());
			}
		}
		
		
		for(int i = 0; i < paymentIds.size(); i++){
			db.delete("payment", " _id='" + paymentIds.get(i) + "' ", null);
			this.deleteRel(paymentIds.get(i));
			this.deleteSplits(paymentIds.get(i));
		}
	}
	
	public void deleteRel(int paymentId){
		db.delete("group_rel_payment", " payment_id='" + paymentId + "' ", null);
	}
	
	public void deleteSplits(int paymentId){
		db.delete("payment_split", " payment_id='" + paymentId + "' ", null);
	}
	
	/*********************************************************
	 * GENERAL
	 ********************************************************/
	
	private void setCursor(String where){
		this.cursor = this.db.rawQuery(
				"SELECT pay._id, grouprel.group_id, pay.payment_amount, pay.payment_title, pay.payment_description, pay.payment_date "
				+ "FROM payment pay "
				+ "JOIN group_rel_payment grouprel ON grouprel.payment_id = pay._id"
				+ " " + where + " "
				+ "ORDER BY payment_date DESC", null);
	}
	
	private void setColoumns(){
		this.idCol = this.cursor.getColumnIndex("_id");
		this.groupIdCol = this.cursor.getColumnIndex("group_id");
		this.amountCol = this.cursor.getColumnIndex("payment_amount");
		this.titleCol = this.cursor.getColumnIndex("payment_title");
		this.descriptionCol = this.cursor.getColumnIndex("payment_description");
		this.dateCol = this.cursor.getColumnIndex("payment_date");
	}
	
	private ArrayList<PaymentSplit> getSplit(int id){
		ArrayList<PaymentSplit> splits = new ArrayList<PaymentSplit>();
		PaymentSplit split = new PaymentSplit(this.context);
		Cursor cursor = this.db.rawQuery(
				"SELECT _id, payment_id, user_id, payment_amount, split_type"
				+ " FROM payment_split "
				+ " WHERE payment_id = '" + id + "'", null);
		
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				split = new PaymentSplit(this.context);
				split.setId(cursor.getInt(cursor.getColumnIndex("_id")));
				split.setAmount(cursor.getFloat(cursor.getColumnIndex("payment_amount")));
				split.setType(cursor.getInt(cursor.getColumnIndex("split_type")));
				split.setUserId(cursor.getInt(cursor.getColumnIndex("user_id")));
				split.setPaymentId(cursor.getInt(cursor.getColumnIndex("payment_id")));
				split.setUser(this.userAdapter.getDetails(split.getUserId()));
				splits.add(split);
			}while(cursor.moveToNext());
		}
		
		return splits;
	}
	
	private GroupPayment getPaymentData(){
		GroupPayment payment = new GroupPayment(this.context);
		
		payment.setId(this.cursor.getInt(this.idCol));
		payment.setAmount(this.cursor.getFloat(this.amountCol));
		payment.setTitle(this.cursor.getString(this.titleCol));
		payment.setDescription(this.cursor.getString(this.descriptionCol));
		payment.setPdt(this.cursor.getLong(this.dateCol));
		payment.setSplits(this.getSplit(payment.getId()));
		
		return payment;
	}
	
	private ArrayList<GroupPayment> sortCursorArrayList(){
		ArrayList<GroupPayment> payments = new ArrayList<GroupPayment>();
		
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
	
	private GroupPayment sortCursor(){
		GroupPayment payment = new GroupPayment(this.context);
		
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
	
	private ContentValues createContentValues(GroupPayment payment) {
		ContentValues values = new ContentValues();
		
		values.put("payment_amount", payment.getAmount());
		values.put("payment_type", payment.getType());
		values.put("payment_title", payment.getTitle());
		values.put("payment_description", payment.getDescription());
		values.put("payment_date", payment.getPdt());
		
		return values;
	}
	
	private ContentValues createContentValuesSplit(PaymentSplit paymentSplit) {
		ContentValues values = new ContentValues();
		
		values.put("payment_id", paymentSplit.getPaymentId());
		values.put("user_id", paymentSplit.getUserId());
		values.put("payment_amount", paymentSplit.getAmount());
		values.put("split_type", paymentSplit.getType());
		
		return values;
	}
	
	private ContentValues createContentValuesRel(int groupId, int paymentId) {
		ContentValues values = new ContentValues();
		
		values.put("group_id", groupId);
		values.put("payment_id", paymentId);
		
		return values;
	}
	
	/*********************************************************
	 * CHECK
	 ********************************************************/
	
}
