package com.joedev.iou;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class Db {
	private static final String CREATE_PAYMENT = "CREATE TABLE payment (_id integer primary key autoincrement, payment_amount float not null, payment_type int not null, payment_title text not null, payment_description text not null, payment_date long not null);";
	private static final String CREATE_USER = "CREATE TABLE user (_id integer primary key autoincrement, user_name text not null, user_id_online int not null, user_status int not null, user_variable_name text, user_minimalistic_text_flag int not null, user_contact_id long not null);";
	private static final String CREATE_USER_REL_PAYMENT = "CREATE TABLE user_rel_payment (_id integer primary key autoincrement, user_id int not null, payment_id int not null);";
	private static final String CREATE_GROUP_REL_PAYMENT = "CREATE TABLE group_rel_payment (_id integer primary key autoincrement, group_id int not null, payment_id int not null);";
	private static final String CREATE_USER_REL_GROUP = "CREATE TABLE user_rel_group (_id integer primary key autoincrement, group_id int not null, user_id int not null);";
	private static final String CREATE_GROUP_DETAIL = "CREATE TABLE group_detail (_id integer primary key autoincrement, group_title text, group_description text);";
	private static final String CREATE_PAYMENT_SPLIT = "CREATE TABLE payment_split (_id integer primary key autoincrement, payment_id int not null, user_id int not null, payment_amount float not null, split_type int not null);";
	
	
 	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_PAYMENT);
		database.execSQL(CREATE_USER);
		database.execSQL(CREATE_USER_REL_PAYMENT);
		database.execSQL(CREATE_GROUP_REL_PAYMENT);
		database.execSQL(CREATE_USER_REL_GROUP);
		database.execSQL(CREATE_GROUP_DETAIL);
		database.execSQL(CREATE_PAYMENT_SPLIT);
		
	}
	
	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		if(!is_column_exists(database, "user", "user_variable_name")){
			database.execSQL("ALTER TABLE user ADD user_variable_name text");
			database.execSQL("ALTER TABLE user ADD user_minimalistic_text_flag int DEFAULT 0 not null");
		}
		
		if(!is_column_exists(database, "user", "user_contact_id")){
			database.execSQL("ALTER TABLE user ADD user_contact_id long DEFAULT 0 not null");
		}
		
		if(!is_table_exists(database, "user_rel_payment")){
			database.execSQL(CREATE_USER_REL_PAYMENT);
			database.execSQL("INSERT INTO user_rel_payment SELECT _id, user_id, _id FROM payment;");
			database.execSQL("CREATE TEMPORARY TABLE payment_backup(_id integer primary key autoincrement, payment_amount float not null, payment_type int not null, payment_description text not null, payment_date long not null);");
			database.execSQL("INSERT INTO payment_backup SELECT _id, payment_amount, payment_type, payment_description, payment_date FROM payment;");
			database.execSQL("DROP TABLE payment;");
			database.execSQL("CREATE TABLE payment(_id integer primary key autoincrement, payment_amount float not null, payment_type int not null, payment_description text not null, payment_date long not null);");
			database.execSQL("INSERT INTO payment SELECT _id, payment_amount, payment_type, payment_description, payment_date FROM payment_backup;");
			database.execSQL("DROP TABLE payment_backup;");
		}
		
		if(!is_table_exists(database, "group_detail")){
			database.execSQL(CREATE_GROUP_REL_PAYMENT);
			database.execSQL(CREATE_USER_REL_GROUP);
			database.execSQL(CREATE_GROUP_DETAIL);
			database.execSQL(CREATE_PAYMENT_SPLIT);
		}
		
		if(is_column_exists(database, "user_rel_group", "int")){
			database.execSQL("DROP TABLE user_rel_group;");
			database.execSQL(CREATE_USER_REL_GROUP);
		}

		if(!is_column_exists(database, "payment_split", "split_type")){
			database.execSQL("ALTER TABLE payment_split ADD split_type int DEFAULT 0 not null");
		}
		
		if(is_column_exists(database, "group_rel_payment", "user_id")){
			database.execSQL("CREATE TEMPORARY TABLE temp(_id integer primary key autoincrement, group_id int not null, payment_id int not null);");
			database.execSQL("INSERT INTO temp SELECT _id, group_id, payment_id FROM group_rel_payment;");
			database.execSQL("DROP TABLE group_rel_payment;");
			database.execSQL(CREATE_GROUP_REL_PAYMENT);
			database.execSQL("INSERT INTO group_rel_payment SELECT _id, group_id, payment_id FROM temp;");
			database.execSQL("DROP TABLE temp;");
		}
		
		if(!is_column_exists(database, "payment", "payment_title")){
			database.execSQL("ALTER TABLE payment ADD payment_title text DEFAULT '' not null");
		}
		
		if(!is_column_exists(database, "group_detail", "group_description")){
			database.execSQL("ALTER TABLE group_detail ADD group_description text DEFAULT '' not null");
		}
		
	}
	
	public static boolean is_table_exists(SQLiteDatabase database, String tableName) {
	    Cursor cursor = database.rawQuery("SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = '" + tableName + "'", null);
	    if(cursor!=null) {
	        if(cursor.getCount()>0) {
	            return true;
	        }
	    }
	    cursor.close();
	    return false;
	}
	
	public static void output_table_exists(SQLiteDatabase database, String tableName){
		if(Db.is_table_exists(database, tableName)){
			Debug.v("table " + tableName + " exists");
		} else {
			Debug.v("table " + tableName + " does not exist");
		}
	}
	
	public static boolean is_column_exists(SQLiteDatabase database, String tableName, String colName){
		try{
			database.rawQuery("SELECT " + colName + " FROM " + tableName, null);
			return true;
		} catch (SQLiteException e){
			return false;
		}
	}
	
	public static void output_column_exists(SQLiteDatabase database, String tableName, String colName){
		if(Db.is_column_exists(database, tableName, colName)){
			Debug.v("coloumn " + colName + " exists in table " + tableName);
		} else {
			Debug.v("coloumn " + colName + " does not exist in table " + tableName);
		}
	}
	
	public static void output_table_details(SQLiteDatabase database, String tableName){
		Cursor c = null;
		c = database.rawQuery("pragma table_info (" + tableName + ")",null);
		
		if(c.getCount() > 0){
			c.moveToFirst();
			do{
				Debug.v("table name", tableName);
				Debug.v();
				Debug.v("cid", c.getString(c.getColumnIndex("cid")));
				Debug.v("name", c.getString(c.getColumnIndex("name")));
				Debug.v("type", c.getString(c.getColumnIndex("type")));
				Debug.v("notnull", c.getString(c.getColumnIndex("notnull")));
				Debug.v("dflt_value", c.getString(c.getColumnIndex("dflt_value")));
				Debug.v("pk", c.getString(c.getColumnIndex("pk")));
				Debug.v();				
			}while(c.moveToNext());
		}
	}
	
}
