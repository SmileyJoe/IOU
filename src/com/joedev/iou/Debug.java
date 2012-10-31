package com.joedev.iou;

import android.util.Log;

public class Debug {
	
	public static void v(int msg){
		v(Integer.toString(msg));
	}
	
	public static void v(String msg, float number){
		v(msg + ": " + Float.toString(number));
	}
	
	public static void v(String msg, int number){
		v(msg + ": " + Integer.toString(number));
	}
	
	public static void v(String msg, long number){
		v(msg + ": " + Long.toString(number));
	}
	
	public static void v(long msg){
		v(Long.toString(msg));
	}
	
	public static void v(String msg, String var){
		v(msg + ": " + var);
	}
	
	public static void v(String msg){
		Log.v(Constants.TAG, msg);
	}
	
	public static void v(){
		v("******************");
	}
}
