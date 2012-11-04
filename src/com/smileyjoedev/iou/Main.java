package com.smileyjoedev.iou;


import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.bugsense.trace.BugSenseHandler;
import com.smileyjoedev.iou.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

// TODO: Add in a click background to the all users/groups LinearLayouts //

public class Main extends SherlockActivity implements OnClickListener {
	
	private Views views;
	
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
//    	Button btUserList = (Button) findViewById(R.id.bt_user_list);
//    	btUserList.setOnClickListener(this);
//    	
//    	Button btGroupList = (Button) findViewById(R.id.bt_group_list);
//    	btGroupList.setOnClickListener(this);
    	
    	this.views = new Views(this, getWindowManager());
    	
    	LinearLayout llUserListWrapper = (LinearLayout) findViewById(R.id.ll_user_list_wrapper);
    	llUserListWrapper.setOnClickListener(this);
    	
    	LinearLayout llGroupListWrapper = (LinearLayout) findViewById(R.id.ll_group_list_wrapper);
    	llGroupListWrapper.setOnClickListener(this);
    	
    }
    
    private void populate_view(){
    	GridView gvActions = (GridView) findViewById(R.id.gv_actions);
    	DbUserAdapter userAdapter = new DbUserAdapter(this);
    	ArrayList<User> users = new ArrayList<User>();
    	users = userAdapter.get();
    	
    	this.views.action_grid(users, gvActions);
    	
    	TextView tvTotalOwedUser = (TextView) findViewById(R.id.tv_total_owed_user);
    	tvTotalOwedUser.setText(this.get_owed_text(users));
    	
    	TextView tvTotalUserOwed = (TextView) findViewById(R.id.tv_total_user_owed);
    	tvTotalUserOwed.setText(this.get_owe_text(users));
    }
    
    private String get_owed_text(ArrayList<User> users){
    	String totalText = "";
    	float total = 0;
    	
    	for(int i = 0; i < users.size(); i++){
    		if(users.get(i).get_balance() > 0){
    			total = total + users.get(i).get_balance(); 
    		}
    	}
    	
    	totalText = Gen.get_amount_text(total);
    	
    	return totalText;
    }
    
    private String get_owe_text(ArrayList<User> users){
    	String totalText = "";
    	float total = 0;
    	
    	for(int i = 0; i < users.size(); i++){
    		if(users.get(i).get_balance() < 0){
    			total = total - users.get(i).get_balance(); 
    		}
    	}
    	
    	totalText = Gen.get_amount_text(total);
    	
    	return totalText;
    }
    
	@Override
	public void onClick(View view) {
		switch(view.getId()){
			case R.id.ll_user_list_wrapper:
//			case R.id.bt_user_list:
				startActivityForResult(Intents.user_list(this), Constants.ACTIVITY_USER_LIST);
				break;
			case R.id.ll_group_list_wrapper:
//			case R.id.bt_group_list:
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