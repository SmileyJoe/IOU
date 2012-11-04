package com.smileyjoedev.iou;

import com.smileyjoedev.genLibrary.GeneralDb;

import android.database.sqlite.SQLiteDatabase;

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
		if(!GeneralDb.isColumnExists(database, "user", "user_variable_name")){
			database.execSQL("ALTER TABLE user ADD user_variable_name text");
			database.execSQL("ALTER TABLE user ADD user_minimalistic_text_flag int DEFAULT 0 not null");
		}
		
		if(!GeneralDb.isColumnExists(database, "user", "user_contact_id")){
			database.execSQL("ALTER TABLE user ADD user_contact_id long DEFAULT 0 not null");
		}
		
		if(!GeneralDb.isTableExists(database, "user_rel_payment")){
			database.execSQL(CREATE_USER_REL_PAYMENT);
			database.execSQL("INSERT INTO user_rel_payment SELECT _id, user_id, _id FROM payment;");
			database.execSQL("CREATE TEMPORARY TABLE payment_backup(_id integer primary key autoincrement, payment_amount float not null, payment_type int not null, payment_description text not null, payment_date long not null);");
			database.execSQL("INSERT INTO payment_backup SELECT _id, payment_amount, payment_type, payment_description, payment_date FROM payment;");
			database.execSQL("DROP TABLE payment;");
			database.execSQL("CREATE TABLE payment(_id integer primary key autoincrement, payment_amount float not null, payment_type int not null, payment_description text not null, payment_date long not null);");
			database.execSQL("INSERT INTO payment SELECT _id, payment_amount, payment_type, payment_description, payment_date FROM payment_backup;");
			database.execSQL("DROP TABLE payment_backup;");
		}
		
		if(!GeneralDb.isTableExists(database, "group_detail")){
			database.execSQL(CREATE_GROUP_REL_PAYMENT);
			database.execSQL(CREATE_USER_REL_GROUP);
			database.execSQL(CREATE_GROUP_DETAIL);
			database.execSQL(CREATE_PAYMENT_SPLIT);
		}
		
		if(GeneralDb.isColumnExists(database, "user_rel_group", "int")){
			database.execSQL("DROP TABLE user_rel_group;");
			database.execSQL(CREATE_USER_REL_GROUP);
		}

		if(!GeneralDb.isColumnExists(database, "payment_split", "split_type")){
			database.execSQL("ALTER TABLE payment_split ADD split_type int DEFAULT 0 not null");
		}
		
		if(GeneralDb.isColumnExists(database, "group_rel_payment", "user_id")){
			database.execSQL("CREATE TEMPORARY TABLE temp(_id integer primary key autoincrement, group_id int not null, payment_id int not null);");
			database.execSQL("INSERT INTO temp SELECT _id, group_id, payment_id FROM group_rel_payment;");
			database.execSQL("DROP TABLE group_rel_payment;");
			database.execSQL(CREATE_GROUP_REL_PAYMENT);
			database.execSQL("INSERT INTO group_rel_payment SELECT _id, group_id, payment_id FROM temp;");
			database.execSQL("DROP TABLE temp;");
		}
		
		if(!GeneralDb.isColumnExists(database, "payment", "payment_title")){
			database.execSQL("ALTER TABLE payment ADD payment_title text DEFAULT '' not null");
		}
		
		if(!GeneralDb.isColumnExists(database, "group_detail", "group_description")){
			database.execSQL("ALTER TABLE group_detail ADD group_description text DEFAULT '' not null");
		}
		
	}
	
}
