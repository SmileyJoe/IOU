package com.joedev.iou;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;

public class Intents {
	
	public static Intent take_picture(int width, int height){
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());
//		intent.putExtra("crop", "true");
//		intent.putExtra("outputX", width);
//		intent.putExtra("outputY", height);
//		intent.putExtra("aspectX", width);
//		intent.putExtra("aspectY", height);
//		intent.putExtra("scale", true);
//		intent.putExtra("return-data", true);
		
		return intent;
	}
	
	public static Intent popup_delete(Context context, int sectionId){
		Intent intent = new Intent(context, PopupDelete.class);
		
		Bundle extras = new Bundle();
		extras.putInt("section_id", sectionId);
		intent.putExtras(extras);
		
		return intent;
	}
	
	public static Intent new_user(Context context){
		Intent intent = new Intent(context, UserNew.class);
		return intent;
	}
	
	public static Intent user_list(Context context){
		Intent intent = new Intent(context, UserList.class);
		return intent;
	}
	
	public static Intent group_list(Context context){
		Intent intent = new Intent(context, GroupList.class);
		return intent;
	}
	
	public static Intent edit_user(Context context, int userId){
		Intent intent = new Intent(context, UserNew.class);
		
		Bundle extras = new Bundle();
		extras.putInt("user_id", userId);
		intent.putExtras(extras);
		
		return intent;
	}
	
	public static Intent user_view(Context context, int userId){
		Intent intent = new Intent(context, UserView.class);
		
		Bundle extras = new Bundle();
		extras.putInt("user_id", userId);
		intent.putExtras(extras);
		
		return intent;
	}
	
	public static Intent new_payment(Context context, int userId){
		Intent intent = new Intent(context, PaymentNew.class);
		
		Bundle extras = new Bundle();
		extras.putInt("user_id", userId);
		intent.putExtras(extras);
		
		return intent;
	}

	public static Intent settings(Context context){
		Intent intent = new Intent(context, Settings.class);
		return intent;
	}

	public static Intent repay_payment(Context context, int paymentId){
		Intent intent = new Intent(context, PaymentNew.class);
		
		Bundle extras = new Bundle();
		extras.putInt("payment_id", paymentId);
		extras.putBoolean("is_repayment", true);
		extras.putBoolean("is_edit", false);
		extras.putBoolean("is_user", false);
		intent.putExtras(extras);
		
		return intent;
	}
	
	public static Intent repay_payment_user(Context context, int userId){
		Intent intent = new Intent(context, PaymentNew.class);
		
		Bundle extras = new Bundle();
		extras.putInt("payment_id", userId);
		extras.putBoolean("is_repayment", true);
		extras.putBoolean("is_edit", false);
		extras.putBoolean("is_user", true);
		intent.putExtras(extras);
		
		return intent;
	}
	
	public static Intent edit_payment(Context context, int paymentId){
		Intent intent = new Intent(context, PaymentNew.class);
		
		Bundle extras = new Bundle();
		extras.putInt("payment_id", paymentId);
		extras.putBoolean("is_repayment", false);
		extras.putBoolean("is_edit", true);
		extras.putBoolean("is_user", false);
		intent.putExtras(extras);
		
		return intent;
	}
	
	public static Intent widget_quick_add(Context context){
		Intent intent = new Intent(context, PaymentNew.class);
		intent.setAction(Constants.ACTION_WIDGET_QUICK_ADD);
		
		return intent;
	}

	public static Intent group_new(Context context){
		Intent intent = new Intent(context, GroupNew.class);
		return intent;
	}

	public static Intent group_edit(Context context, int groupId){
		Intent intent = new Intent(context, GroupNew.class);
		
		Bundle extras = new Bundle();
		extras.putInt("group_id", groupId);
		intent.putExtras(extras);
		
		return intent;
	}

	public static Intent group_view(Context context, int groupId){
		Intent intent = new Intent(context, GroupView.class);
		
		Bundle extras = new Bundle();
		extras.putInt("group_id", groupId);
		intent.putExtras(extras);
		
		return intent;
	}

	public static Intent group_payment_new(Context context, int groupId){
		Intent intent = new Intent(context, GroupPaymentNew.class);
		
		Bundle extras = new Bundle();
		extras.putInt("group_id", groupId);
		intent.putExtras(extras);
		
		return intent;
	}

	public static Intent group_payment_edit(Context context, int groupId, int paymentId){
		Intent intent = new Intent(context, GroupPaymentNew.class);
		
		Bundle extras = new Bundle();
		extras.putInt("group_id", groupId);
		extras.putInt("payment_id", paymentId);
		intent.putExtras(extras);
		
		return intent;
	}
	
	public static Intent group_repayment(Context context, int groupId){
		Intent intent = new Intent(context, GroupPayback.class);
		
		Bundle extras = new Bundle();
		extras.putInt("group_id", groupId);
		intent.putExtras(extras);
		
		return intent;
	}

	public static Intent group_user_payment_details(Context context, int groupId){
		Intent intent = new Intent(context, GroupUserPaymentDetails.class);
		
		Bundle extras = new Bundle();
		extras.putInt("group_id", groupId);
		intent.putExtras(extras);
		
		return intent;
	}
	
	
}
