package com.joedev.iou;


import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.bugsense.trace.BugSenseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends SherlockActivity implements OnClickListener {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gen.setTheme(this);
        setContentView(R.layout.main);
//        BugSenseHandler.setup(this, "04b74a70");
        
        this.initialize();
        this.populate_view();
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
			case R.id.menu_settings:
				startActivityForResult(Intents.settings(this), Constants.ACTIVITY_SETTINGS);
				return true;
	        default:
	            return super.onOptionsItemSelected(item);
        }

    }
    
    public void initialize(){
    	Button btUserList = (Button) findViewById(R.id.bt_user_list);
    	btUserList.setOnClickListener(this);
    	
    	Button btGroupList = (Button) findViewById(R.id.bt_group_list);
    	btGroupList.setOnClickListener(this);
    }
    
    private void populate_view(){
    	
    }
    
	@Override
	public void onClick(View view) {
		switch(view.getId()){
			case R.id.bt_user_list:
				startActivityForResult(Intents.user_list(this), Constants.ACTIVITY_USER_LIST);
				break;
			case R.id.bt_group_list:
				startActivityForResult(Intents.group_list(this), Constants.ACTIVITY_GROUP_LIST);
				break;
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
			case Constants.ACTIVITY_USER_LIST:
//				this.get_all_users();
				this.populate_view();
				break;
			case Constants.ACTIVITY_GROUP_LIST:
//				this.get_all_users();
				this.populate_view();
				break;
		}
	}
	
}