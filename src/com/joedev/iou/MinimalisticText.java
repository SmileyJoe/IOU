package com.joedev.iou;

import android.content.Context;
import android.content.Intent;

public class MinimalisticText {
	
	public static void SendTo(Context context, String varName, String varContent){
	    Intent sendintent = new Intent("com.twofortyfouram.locale.intent.action.FIRE_SETTING");
	    // important that we only target minimalistic text widget, and not all Locale plugins
	    sendintent.setClassName("de.devmil.minimaltext", "de.devmil.minimaltext.locale.LocaleFireReceiver");
	    sendintent.putExtra("de.devmil.minimaltext.locale.extras.VAR_NAME", varName);
	    sendintent.putExtra("de.devmil.minimaltext.locale.extras.VAR_TEXT", varContent);
	    
	    context.sendBroadcast(sendintent);
	}
}
