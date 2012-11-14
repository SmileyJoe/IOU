package com.smileyjoedev.iou;

import com.smileyjoedev.genLibrary.Contacts;
import com.smileyjoedev.genLibrary.Debug;

import android.app.Activity;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.graphics.Bitmap;
import android.os.Bundle;

public class Shortcut extends Activity{
	
	private QuickAction quickAction;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		
		this.quickAction = new QuickAction(this);
		
		startActivityForResult(Intents.quickActionNew(this, true), Constants.ACTIVITY_QUICK_ACTION_NEW);
		
		
	}
	
	private void setShortcut(){
		DbUserAdapter userAdapter = new DbUserAdapter(this);
		
		Contacts cont = new Contacts(this);
		Intent intent = new Intent();
		Debug.d(this.quickAction.toString());
		switch(this.quickAction.getType()){
			case QuickAction.TYPE_USER:
				User user = (User) this.quickAction.getTargetData();
				Bitmap image = cont.getPhoto(user.getContactId());
				
				try{
					if(!image.equals(null)){
						intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, image);
					} else {
						ShortcutIconResource icon = Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher);
						intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
					}
				}catch(NullPointerException e){
					ShortcutIconResource icon = Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher);
					intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
				}
				
				break;
			case QuickAction.TYPE_GROUP:
				ShortcutIconResource icon = Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher);
				intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
				break;
		}
		
		Intent shortcutIntent = this.quickAction.getIntent();
		shortcutIntent.setAction(Intent.ACTION_MAIN);
		
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, this.quickAction.getTitle() + ": " + this.quickAction.getActionText());
		setResult(RESULT_OK, intent);
		finish();
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
			case Constants.ACTIVITY_QUICK_ACTION_NEW:
				if(resultCode == Activity.RESULT_OK){
					this.quickAction.setAction(data.getIntExtra("quick_action_action", 0));
					this.quickAction.setType(data.getIntExtra("quick_action_type", 0));
					this.quickAction.setTargetId(data.getIntExtra("quick_action_target_id", 0));
					this.setShortcut();
					
				}
				break;
		}
	}
	
}
