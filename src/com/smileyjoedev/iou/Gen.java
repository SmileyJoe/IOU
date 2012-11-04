package com.smileyjoedev.iou;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import com.smileyjoedev.genLibrary.Contacts;
import com.smileyjoedev.genLibrary.MinimalisticText;
import com.smileyjoedev.iou.R;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Gen {
	
	//TODO: instead of include time send a constant through of the format //
	public static String convert_pdt(long pdt, boolean includeTime){
		String format = "dd MMM yyyy HH:mm";
		
		if(includeTime){
			format = "dd MMM yyyy HH:mm";
		} else {
			format = "dd MMM yyyy";
		}
		Date date = new Date(pdt);
		SimpleDateFormat df = new SimpleDateFormat(format);
		String text = df.format(date);
		
		return text;
	}
	
	public static long get_pdt(){
		Calendar c = Calendar.getInstance();
		Date pdt = c.getTime();
		
		return pdt.getTime();
	}

    public static void fill_window(Window window){
    	window.setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
    }
	
	public static void hide_empty_view(ArrayList<?> array, View view){
		if(array.isEmpty()){
			view.setVisibility(View.GONE);
		}
	}


	public static void changeTheme(Activity activity)
	{
		activity.finish();
		
		activity.startActivity(new Intent(activity, activity.getClass()));
	}

	public static void setTheme(Activity activity)
	{
//		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity.getBaseContext());
//		int theme = Integer.parseInt(prefs.getString("theme", "0"));
//		switch (theme)
//		{
//		default:
//		case Constants.THEME_DEFAULT:
//			activity.setTheme(R.style.global_dark);
//			break;
//		case Constants.THEME_DARK:
//			activity.setTheme(R.style.global_dark);
//			break;
//		case Constants.THEME_LIGHT:
//			activity.setTheme(R.style.global_light);
//			break;
//		}
	}

	public static boolean set_user_image(Context context, ImageView ivUserImage, User user){
		boolean imageFound = false;
		String userName = user.get_name();
//		java.io.File file = new java.io.File(Constants.IMAGE_PATH + userName + Constants.IMAGE_EXTENSION);
//		if(file.exists()){
//			Bitmap bm = BitmapFactory.decodeFile(Constants.IMAGE_PATH + userName + Constants.IMAGE_EXTENSION);
//			ivUserImage.setImageBitmap(bm);
//			imageFound = true;
//		}else{
			if(user.is_in_contact_dir()){
				Contacts cont = new Contacts(context);
				Bitmap image = cont.getPhoto(user.get_contact_id());
				
				try{
					image.equals(null);
					ivUserImage.setImageBitmap(image);
					imageFound = true;
				} catch (NullPointerException e){
					ivUserImage.setImageResource(R.drawable.default_user_large);
					ivUserImage.setBackgroundColor(context.getResources().getColor(R.color.medium_grey));
				}
				
			} else {
				ivUserImage.setImageResource(R.drawable.default_user_large);
				ivUserImage.setBackgroundColor(context.getResources().getColor(R.color.medium_grey));
			}
//		}
		
		return imageFound;
	}
	
	public static boolean set_action_image(Context context, ImageView ivUserImage, User user){
		boolean imageFound = false;
		String userName = user.get_name();
		if(user.is_in_contact_dir()){
			Contacts cont = new Contacts(context);
			Bitmap image = cont.getPhoto(user.get_contact_id());
			
			try{
				image.equals(null);
				ivUserImage.setImageBitmap(image);
				imageFound = true;
			} catch (NullPointerException e){
				ivUserImage.setImageResource(R.drawable.default_user_large);
//				ivUserImage.setBackgroundColor(context.getResources().getColor(R.color.medium_grey));
			}
			
		} else {
			ivUserImage.setImageResource(R.drawable.default_user_large);
//			ivUserImage.setBackgroundColor(context.getResources().getColor(R.color.medium_grey));
		}
		
		return imageFound;
	}
	
	public static String get_amount_text(float amount){
		boolean positive;
		
		if(amount < 0){
			positive = false;
		} else {
			positive = true;
		}
		
		String number = Gen.get_formatted_amount(Math.abs(amount));
		
		Locale locale=Locale.getDefault();
        Currency currency=Currency.getInstance(locale);
        String symbol = currency.getSymbol();
        
        number = symbol + number;
        
        if(!positive){
        	number = "-" + number;	        	
        }
        
		return number;
	}
	
	public static String get_formatted_amount(float amount){
		String number = Float.toString(amount);
		int decPos = number.indexOf(".");
		String dec = number.substring(decPos + 1);
		
		if(dec.length() == 0){
			number = number + "00";
		} else{
			if(dec.length() == 1){
				number = number + "0";
			} else {
				number = number.replace("." + dec, "");
				dec = dec.substring(0, 2);
				number = number + "." + dec;
			}
		}
		
		return number;
	}
	
	public static void display_minimalistic_text(Context context, User user, Payment payment){
		
		if(user.is_using_minimalistic_text()){
			float newBalance = user.get_balance();
			if(payment.is_to_user()){
				newBalance = newBalance + payment.get_amount();
			} else {
				newBalance = newBalance - payment.get_amount();
			}
			
			Gen.display_minimalistic_text(context, user, newBalance);
		}
	}
	
	public static void display_minimalistic_text(Context context, User user, float amount){
		Gen.display_minimalistic_text(context, user.get_variable_name(), amount);
	}
	
	public static void display_minimalistic_text(Context context, String variableName, float amount){
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		
		if(prefs.getBoolean("allow_minimalistic_text", true)){
			MinimalisticText.SendTo(context, variableName, Gen.get_amount_text(amount));
			if(amount == 0){
				MinimalisticText.SendTo(context, variableName + "SIGN", "0");
			} else {
				if(amount > 0){
					MinimalisticText.SendTo(context, variableName + "SIGN", "0");
				} else {
					MinimalisticText.SendTo(context, variableName + "SIGN", "1");
				}
			}
		}
		
	}

    public static void sort_user(ArrayList<User> tempArray, int sort){
    	Collections.sort(tempArray, new SortUser(sort));
    }
    
    public static String create_email_body(Context context, User user){
    	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
    	String msg = prefs.getString("default_email_reminder_body", context.getString(R.string.default_email_reminder_body));
		String username = user.get_name();
		String balance = prefs.getString("default_currency", context.getString(R.string.default_currency)) + user.get_balance_text();
		
		msg = msg.replace("%USERNAME", username);
		msg = msg.replace("%BALANCE", balance);
    	
    	return msg;
    }

    public static String get_user_csv(ArrayList<User> users){
    	String csv = "";
    	boolean first = true;
    	
    	for(int i = 0; i < users.size(); i++){
    		if(first){
    			csv += users.get(i).get_name();
    			first = false;
    		} else {
    			csv += ", " + users.get(i).get_name();
    		}
    	}
    	
    	return csv;
    }
    
    public static ArrayList<Payment> get_user_payments(Group group, ArrayList<GroupPayment> payments){
    	 ArrayList<Payment> tempPayments = new ArrayList<Payment>();
         
         for(int i = 0; i < group.get_users().size(); i++){
         	Payment payment = new Payment();
         	
         	payment.set_user(group.get_user(i));
         	payment.set_user_id(group.get_user(i).get_id());
         	
         	tempPayments.add(payment);
         }
         
         for(int i = 0; i < payments.size(); i++){
         	GroupPayment payment = payments.get(i);
         	
         	for(int j = 0; j < payment.get_splits().size(); j++){
         		for(int k = 0; k < tempPayments.size(); k++){
 	        		if(payment.get_split(j).get_user_id() == tempPayments.get(k).get_user_id()){
 	        			if(payment.get_split(j).is_paying()){
 	        				tempPayments.get(k).set_amount(tempPayments.get(k).get_amount() +  payment.get_split(j).get_amount());
 	        			} else {
 	        				tempPayments.get(k).set_amount(tempPayments.get(k).get_amount() -  payment.get_split(j).get_amount());
 	        			}
 	        		}
         		}
         	}
         }
         
         return tempPayments;
    }
    
    public static ArrayList<GroupRepayment> sort_group_repayments(ArrayList<Payment> userPayments){
    	ArrayList<GroupRepayment> repayments = new ArrayList<GroupRepayment>();
    	
    	ArrayList<Payment> owedPayments = new ArrayList<Payment>();
        ArrayList<Payment> owingPayments = new ArrayList<Payment>();
        
        for(int i = 0; i < userPayments.size(); i++){
        	if(userPayments.get(i).get_amount() != 0){
        		if(userPayments.get(i).get_amount() > 0){
            		owedPayments.add(userPayments.get(i));
            	} else {
            		userPayments.get(i).set_amount(Math.abs(userPayments.get(i).get_amount()));
            		owingPayments.add(userPayments.get(i));
            	}           		
        	}
        }
        
        for(int i = 0; i < owingPayments.size(); i++){
        	while(owingPayments.get(i).get_amount() > 0){
        		if(owingPayments.get(i).get_amount() >= owedPayments.get(0).get_amount()){
        			GroupRepayment repayment = new GroupRepayment();
        			
        			repayment.set_amount(owedPayments.get(0).get_amount());
        			repayment.set_owed_user(owedPayments.get(0).get_user());
        			repayment.set_owing_user(owingPayments.get(i).get_user());
        			
        			repayments.add(repayment);
        			
        			owingPayments.get(i).set_amount(owingPayments.get(i).get_amount() - owedPayments.get(0).get_amount());
        			owedPayments.remove(0);
        		} else {
        			
        			GroupRepayment repayment = new GroupRepayment();
        			
        			repayment.set_amount(owingPayments.get(i).get_amount());
        			repayment.set_owed_user(owedPayments.get(0).get_user());
        			repayment.set_owing_user(owingPayments.get(i).get_user());
        			
        			repayments.add(repayment);
        			
        			owedPayments.get(0).set_amount(owedPayments.get(0).get_amount() - owingPayments.get(i).get_amount());
        			owingPayments.get(i).set_amount(0);
        		}
        	}
        }
    	
    	return repayments;
    }
    
    public static float format_number(float number){
    	DecimalFormat df = new DecimalFormat("###.##");
    	
    	try{
			number = Float.parseFloat(df.format(number));
		} catch(NumberFormatException e){
		}
		
    	return number;
    }
}
