package com.smileyjoedev.iou;

import com.smileyjoedev.genLibrary.Debug;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;

public class Intents {
	
	public static Intent dateTimePicker(Context context){
		Intent intent = new Intent(context, com.smileyjoedev.genLibrary.DateTimePicker.class);
		
		return intent;
	}
	
	public static Intent dateTimePicker(Context context, long millie){
		Intent intent = new Intent(context, com.smileyjoedev.genLibrary.DateTimePicker.class);
		Bundle extras = new Bundle();
		extras.putLong("milliesecond_timestamp", millie);
		intent.putExtras(extras);	
		
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
			case Constants.QUICK_ACTION:
				message = context.getString(R.string.tv_popup_delete_quick_action);
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
	
	public static Intent quickActionNew(Context context){
		return quickActionNew(context, false);
	}
	
	public static Intent quickActionNew(Context context, boolean returnResult){
		Intent intent = new Intent(context, QuickActionNew.class);
		
		Bundle extras = new Bundle();
		extras.putBoolean("return_result", returnResult);
		intent.putExtras(extras);		
		
		return intent;
	}
	
	public static Intent quickActionEdit(Context context, long quickActionId){
		Intent intent = new Intent(context, QuickActionNew.class);
		
		Bundle extras = new Bundle();
		extras.putLong("quick_action_id", quickActionId);
		intent.putExtras(extras);
		
		return intent;
	}
	
	public static Intent quickActionTargetPicker(Context context, int actionType){
		Intent intent = new Intent(context, QuickActionTargetPicker.class);
		
		Bundle extras = new Bundle();
		extras.putInt("action_type", actionType);
		intent.putExtras(extras);
		
		return intent;
	}
	
	public static Intent userNew(Context context){
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
	
	public static Intent userView(Context context, int userId, int notificationId){
		Intent intent = new Intent(context, UserView.class);
		Bundle extras = new Bundle();
		extras.putInt("user_id", userId);
		extras.putInt("notification_id", notificationId);
		intent.setAction(Long.toString(System.currentTimeMillis()));
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
