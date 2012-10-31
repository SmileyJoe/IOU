package com.joedev.iou;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;

public class Contacts {
	
	private Context context;
	
	private Uri emailURI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
	private Uri numberURI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
	private Uri nameURI = ContactsContract.Contacts.CONTENT_URI;
	
	private String nameIdCol = ContactsContract.Contacts._ID;
	private String nameCol = ContactsContract.Contacts.DISPLAY_NAME;
	private String emailIdCol = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
	private String emailCol = ContactsContract.CommonDataKinds.Email.DATA;
	private String emailTypeIdCol = ContactsContract.CommonDataKinds.Email.TYPE;
	private String emailLabelCol = ContactsContract.CommonDataKinds.Email.LABEL;
	private String numberIdCol = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
	private String numberCol = ContactsContract.CommonDataKinds.Phone.NUMBER;
	private String numberTypeIdCol = ContactsContract.CommonDataKinds.Phone.TYPE;
	private String numberLabelCol = ContactsContract.CommonDataKinds.Phone.LABEL;
	
	
	public Contacts(Context context) {
		this.context = context;
	}
	
	public ArrayList<Contact> get_list(boolean visible) {
		ArrayList<Contact> contacts = new ArrayList<Contact>();
		int visibleFlag = 0;
		
		if(visible){
			visibleFlag = 1;
		}
		
		String[] projection = new String[]{this.nameIdCol, this.nameCol};
		String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '" + visibleFlag + "'";
		String sortOrder = this.nameCol + " COLLATE LOCALIZED ASC";
		
		Cursor cursor = this.context.getContentResolver().query(this.nameURI, projection, selection, null, sortOrder);
		
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				Contact contact = new Contact();
				contact.set_name(cursor.getString(cursor.getColumnIndex(this.nameCol)));
				contact.set_id(cursor.getLong(cursor.getColumnIndex(this.nameIdCol)));
				contacts.add(contact);
			}while(cursor.moveToNext());
		}
		
		
		cursor.close();
		
		return contacts;
	}
	
	public Contact get_details(long id){
		Contact contact = new Contact();
		
		String[] projection = new String[]{this.nameIdCol, this.nameCol};
		String selection = this.nameIdCol + " = '" + id + "'";
		String sortOrder = this.nameCol + " COLLATE LOCALIZED ASC";
		
		Cursor cursor = this.context.getContentResolver().query(this.nameURI, projection, selection, null, sortOrder);
		
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				contact.set_name(cursor.getString(cursor.getColumnIndex(this.nameCol)));
				contact.set_id(cursor.getLong(cursor.getColumnIndex(this.nameIdCol)));
				contact.set_numbers(this.get_numbers(id));
				contact.set_emails(this.get_emails(id));
			}while(cursor.moveToNext());
		}
		
		cursor.close();
		
		return contact;
	}
	
	public ArrayList<PhoneNumber> get_numbers(long id){
		ArrayList<PhoneNumber> numbers = new ArrayList<PhoneNumber>();
		
		String[] projection = new String[]{this.numberIdCol, this.numberCol, this.numberTypeIdCol, this.numberLabelCol};
		String selection = this.numberIdCol + " = '" + id + "'";
		String sortOrder = this.numberCol + " COLLATE LOCALIZED ASC";
		
		Cursor cursor = this.context.getContentResolver().query(this.numberURI, projection, selection, null, sortOrder);
		
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				PhoneNumber number = new PhoneNumber();
				
				number.set_number(cursor.getString(cursor.getColumnIndex(this.numberCol)));
				number.set_type_id(cursor.getInt(cursor.getColumnIndex(this.numberTypeIdCol)));
				number.set_type((String) ContactsContract.CommonDataKinds.Phone.getTypeLabel(this.context.getResources(), number.get_type_id(), cursor.getString(cursor.getColumnIndex(this.numberLabelCol))));
				
				numbers.add(number);			
			}while(cursor.moveToNext());
		}
		cursor.close();
		
		return numbers;
	}
	
	public ArrayList<EmailAddress> get_emails(long id){
		ArrayList<EmailAddress> emails = new ArrayList<EmailAddress>();
		
		String[] projection = new String[]{this.emailIdCol, this.emailCol, this.emailTypeIdCol, this.emailLabelCol};
		String selection = this.emailIdCol + " = '" + id + "'";
		String sortOrder = this.emailCol + " COLLATE LOCALIZED ASC";
		
		Cursor cursor = this.context.getContentResolver().query(this.emailURI, projection, selection, null, sortOrder);
		
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			do{
				EmailAddress email = new EmailAddress();
				
				email.set_address(cursor.getString(cursor.getColumnIndex(this.emailCol)));
				email.set_type_id(cursor.getInt(cursor.getColumnIndex(this.emailTypeIdCol)));
				email.set_type((String) ContactsContract.CommonDataKinds.Email.getTypeLabel(this.context.getResources(), email.get_type_id(), cursor.getString(cursor.getColumnIndex(this.emailLabelCol))));
				
				emails.add(email);
			}while(cursor.moveToNext());
		}
		
		cursor.close();
		
		return emails;
	}
	
	public Bitmap get_photo(long id) {
	    Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
	    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(this.context.getContentResolver(), uri);
	    if (input == null) {
	        return null;
	    }
	    return BitmapFactory.decodeStream(input);
	}
	
	public ArrayList<String> get_name_list(ArrayList<Contact> contacts){
		
		ArrayList<String> names = new ArrayList<String>();
		
		for(int i = 0; i < contacts.size(); i++){
			names.add(contacts.get(i).get_name());
		}
		
		return names;
		
	}
	
	public HashMap<String, Long> get_name_hash(ArrayList<Contact> contacts){
		HashMap<String, Long> map = new HashMap<String, Long>();
		
		for(int i = 0; i < contacts.size(); i++){
			map.put(contacts.get(i).get_name(), contacts.get(i).get_id());
		}
		
		return map;
	}

	public static void open_contact(Context context, long id){
		Intent intent = new Intent(Intent.ACTION_VIEW);
	    Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(id));
	    intent.setData(uri);
	    context.startActivity(intent);
	}
}
