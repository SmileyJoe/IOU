package com.smileyjoedev.iou;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;

public class Intents {
	
	public static Intent takePicture(int width, int height){
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
	
	public static Intent popupDelete(Context context, int sectionId){
		Intent intent = new Intent(context, com.smileyjoedev.genLibrary.PopupDelete.class);
		String message = "";
		String positiveText = "";
		String negativeText = "";
		
		switch(sectionId){
			case Constants.PAYMENT:
				message = context.getString(R.string.tv_popup_delete_payment);
				positiveText = context.getString(R.string.bt_popup_delete_positive);
				negativeText = context.getString(R.string.bt_popup_delete_negative);
				break;
			case Constants.USER:
				message = context.getString(R.string.tv_popup_delete_user);
				positiveText = context.getString(R.string.bt_popup_delete_positive);
				negativeText = context.getString(R.string.bt_popup_delete_negative);
				break;
			case Constants.EXIT_APP:
				message = context.getString(R.string.tv_popup_delete_exit);
				positiveText = context.getString(R.string.bt_popup_delete_positive);
				negativeText = context.getString(R.string.bt_popup_delete_negative);
				break;
			case Constants.GROUP:
				message = context.getString(R.string.tv_popup_delete_group);
				positiveText = context.getString(R.string.bt_popup_delete_positive);
				negativeText = context.getString(R.string.bt_popup_delete_negative);
				break;
		}
		
		Bundle extras = new Bundle();
		extras.putString("message", message);
		extras.putString("positive_text", positiveText);
		extras.putString("negative_text", negativeText);
		intent.putExtras(extras);
		
		return intent;
	}
	
	public static Intent newUser(Context context){
		Intent intent = new Intent(context, UserNew.class);
		return intent;
	}
	
	public static Intent userList(Context context){
		Intent intent = new Intent(context, UserList.class);
		return intent;
	}
	
	public static Intent groupList(Context context){
		Intent intent = new Intent(context, GroupList.class);
		return intent;
	}
	
	public static Intent editUser(Context context, int userId){
		Intent intent = new Intent(context, UserNew.class);
		
		Bundle extras = new Bundle();
		extras.putInt("user_id", userId);
		intent.putExtras(extras);
		
		return intent;
	}
	
	public static Intent userView(Context context, int userId){
		Intent intent = new Intent(context, UserView.class);
		
		Bundle extras = new Bundle();
		extras.putInt("user_id", userId);
		intent.putExtras(extras);
		
		return intent;
	}
	
	public static Intent newPayment(Context context, int userId){
		Intent intent = new Intent(context, UserPaymentNew.class);
		
		Bundle extras = new Bundle();
		extras.putInt("user_id", userId);
		intent.putExtras(extras);
		
		return intent;
	}

	public static Intent settings(Context context){
		Intent intent = new Intent(context, Settings.class);
		return intent;
	}

	public static Intent repayPayment(Context context, int paymentId){
		Intent intent = new Intent(context, UserPaymentNew.class);
		
		Bundle extras = new Bundle();
		extras.putInt("payment_id", paymentId);
		extras.putBoolean("is_repayment", true);
		extras.putBoolean("is_edit", false);
		extras.putBoolean("is_user", false);
		intent.putExtras(extras);
		
		return intent;
	}
	
	public static Intent repayPaymentUser(Context context, int userId){
		Intent intent = new Intent(context, UserPaymentNew.class);
		
		Bundle extras = new Bundle();
		extras.putInt("payment_id", userId);
		extras.putBoolean("is_repayment", true);
		extras.putBoolean("is_edit", false);
		extras.putBoolean("is_user", true);
		intent.putExtras(extras);
		
		return intent;
	}
	
	public static Intent editPayment(Context context, int paymentId){
		Intent intent = new Intent(context, UserPaymentNew.class);
		
		Bundle extras = new Bundle();
		extras.putInt("payment_id", paymentId);
		extras.putBoolean("is_repayment", false);
		extras.putBoolean("is_edit", true);
		extras.putBoolean("is_user", false);
		intent.putExtras(extras);
		
		return intent;
	}
	
	public static Intent widgetQuickAdd(Context context){
		Intent intent = new Intent(context, UserPaymentNew.class);
		intent.setAction(Constants.ACTION_WIDGET_QUICK_ADD);
		
		return intent;
	}

	public static Intent groupNew(Context context){
		Intent intent = new Intent(context, GroupNew.class);
		return intent;
	}

	public static Intent groupEdit(Context context, int groupId){
		Intent intent = new Intent(context, GroupNew.class);
		
		Bundle extras = new Bundle();
		extras.putInt("group_id", groupId);
		intent.putExtras(extras);
		
		return intent;
	}

	public static Intent groupView(Context context, int groupId){
		Intent intent = new Intent(context, GroupView.class);
		
		Bundle extras = new Bundle();
		extras.putInt("group_id", groupId);
		intent.putExtras(extras);
		
		return intent;
	}

	public static Intent groupPaymentNew(Context context, int groupId){
		Intent intent = new Intent(context, GroupPaymentNew.class);
		
		Bundle extras = new Bundle();
		extras.putInt("group_id", groupId);
		intent.putExtras(extras);
		
		return intent;
	}

	public static Intent groupPaymentEdit(Context context, int groupId, int paymentId){
		Intent intent = new Intent(context, GroupPaymentNew.class);
		
		Bundle extras = new Bundle();
		extras.putInt("group_id", groupId);
		extras.putInt("payment_id", paymentId);
		intent.putExtras(extras);
		
		return intent;
	}
	
	public static Intent groupRepayment(Context context, int groupId){
		Intent intent = new Intent(context, GroupPayback.class);
		
		Bundle extras = new Bundle();
		extras.putInt("group_id", groupId);
		intent.putExtras(extras);
		
		return intent;
	}

	public static Intent groupUserPaymentDetails(Context context, int groupId){
		Intent intent = new Intent(context, GroupUserPaymentDetails.class);
		
		Bundle extras = new Bundle();
		extras.putInt("group_id", groupId);
		intent.putExtras(extras);
		
		return intent;
	}
	
	
}
