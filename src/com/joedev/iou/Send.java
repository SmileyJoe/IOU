package com.joedev.iou;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.widget.Toast;

public class Send {
	
	public static void sms(Context context, String number, String message){
		PendingIntent pi = PendingIntent.getActivity(context, 0, new Intent(), 0);                
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(number, null, message, pi, null);
	}
	
	public static void sms_dialog(Context context, String number, String message){
		Uri uri = Uri.parse("smsto:" + number);   
		Intent it = new Intent(Intent.ACTION_SENDTO, uri);   
		it.putExtra("sms_body", message);   
		context.startActivity(it); 
	}
	
	public static void email_dialog(Context context, String address, String subject, String body){
		
		Intent it=new Intent(Intent.ACTION_SEND);     
		it.putExtra(Intent.EXTRA_EMAIL, new String[]{address});     
		it.putExtra(Intent.EXTRA_TEXT, body);     
		it.putExtra(Intent.EXTRA_SUBJECT, subject);     
		it.setType("message/rfc822");     
		context.startActivity(Intent.createChooser(it, "Choose Email Client")); 

	}
	
}
