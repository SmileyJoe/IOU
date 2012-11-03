package com.joedev.iou;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Notify {
	
	public static void toast(Context context, String msg, int length) {
		switch(length){
			case 1:
				length = Toast.LENGTH_SHORT;
				break;
			case 2:
				length = Toast.LENGTH_LONG;
				break;
			default:
				length = Toast.LENGTH_SHORT;
				break;
		}
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		toast.show();
	}
	
	public static void toast(Context context, String msg) {
		toast(context, msg, 2);
	}
	
	public static void toast(Context context, int resId) {
		toast(context, context.getString(resId), 2);
	}
	
	public static void toast(Context context, int resId, String extra) {
		toast(context, context.getString(resId) + ": " + extra, 2);
	}

	
}
