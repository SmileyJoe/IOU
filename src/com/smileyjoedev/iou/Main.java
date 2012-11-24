package com.smileyjoedev.iou;


import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.bugsense.trace.BugSenseHandler;
import com.smileyjoedev.genLibrary.Contacts;
import com.smileyjoedev.iou.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

// TODO: Add in a click background to the all users/groups LinearLayouts //

public class Main extends SherlockActivity implements OnClickListener, OnItemClickListener {
	
	private Views views;
	private ActionGridAdapter actionGridApater;
	private GridView gvActions;
	private ArrayList<QuickAction> quickActions;
	private int selectedQuickAction;
	private DbQuickActionAdapter quickActionAdapter;
	private LinearLayout llUserListWrapper;
	private LinearLayout llGroupListWrapper;
	private SharedPreferences prefs;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gen.setTheme(this);
        setContentView(R.layout.main);
//        BugSenseHandler.setup(this, "04b74a70");
        
        this.initialize();
        
        switch(Integer.parseInt(this.prefs.getString("default_start_page", "0"))){
        	case Settings.START_INDIVIDUAL_VIEW_ALL:
        		startActivityForResult(Intents.userList(this), Constants.ACTIVITY_START_PAGE);
        		break;
        	case Settings.START_GROUP_VIEW_ALL:
        		startActivityForResult(Intents.groupList(this), Constants.ACTIVITY_START_PAGE);
        		break;
        	default:
        		break;
        }
        
        this.getQuickActions();
        this.actionGridApater = this.views.actionGrid(this.quickActions, this.gvActions);
        
        this.populateView();
        
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
			case R.id.menu_quick_action_new:
				startActivityForResult(Intents.quickActionNew(this), Constants.ACTIVITY_QUICK_ACTION_NEW);
				return true;
	        default:
	            return super.onOptionsItemSelected(item);
        }

    }
    
    public void initialize(){
    	this.views = new Views(this, getWindowManager());
    	this.gvActions = (GridView) findViewById(R.id.gv_actions);
    	this.gvActions.setOnItemClickListener(this);
    	registerForContextMenu(this.gvActions);
    	
    	this.selectedQuickAction = 0;
    	
    	this.quickActionAdapter = new DbQuickActionAdapter(this);
    	
    	this.llUserListWrapper = (LinearLayout) findViewById(R.id.ll_user_list_wrapper);
    	this.llUserListWrapper.setOnClickListener(this);
    	
    	this.llGroupListWrapper = (LinearLayout) findViewById(R.id.ll_group_list_wrapper);
    	this.llGroupListWrapper.setOnClickListener(this);
    	
    	this.prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    	
    }
    
    private void populateView(){
    	DbUserAdapter userAdapter = new DbUserAdapter(this);
    	ArrayList<User> users = new ArrayList<User>();
    	users = userAdapter.get();
    	
    	TextView tvTotalOwedUser = (TextView) findViewById(R.id.tv_total_owed_user);
    	tvTotalOwedUser.setText(this.getOwedText(users));
    	
    	TextView tvTotalUserOwed = (TextView) findViewById(R.id.tv_total_user_owed);
    	tvTotalUserOwed.setText(this.getOweText(users));
    	
    	LinearLayout llBalanceWrapper = (LinearLayout) findViewById(R.id.ll_balance_wrapper);
    	
    	if(!this.prefs.getBoolean("allow_individual", true)){
    		this.llUserListWrapper.setVisibility(View.GONE);
    		llBalanceWrapper.setVisibility(View.GONE);
    	} else {
    		this.llUserListWrapper.setVisibility(View.VISIBLE);
    		llBalanceWrapper.setVisibility(View.VISIBLE);
    	}
    	
    	if(!this.prefs.getBoolean("allow_group", true)){
    		this.llGroupListWrapper.setVisibility(View.GONE);
    	} else {
    		this.llGroupListWrapper.setVisibility(View.VISIBLE);
    	}
    }
    
    private void getQuickActions(){
    	this.quickActions = this.quickActionAdapter.get();
    }
    
    private String getOwedText(ArrayList<User> users){
    	String totalText = "";
    	float total = 0;
    	
    	for(int i = 0; i < users.size(); i++){
    		if(users.get(i).getBalance() > 0){
    			total = total + users.get(i).getBalance(); 
    		}
    	}
    	
    	totalText = Gen.getAmountText(total);
    	
    	return totalText;
    }
    
    private String getOweText(ArrayList<User> users){
    	String totalText = "";
    	float total = 0;
    	
    	for(int i = 0; i < users.size(); i++){
    		if(users.get(i).getBalance() < 0){
    			total = total - users.get(i).getBalance(); 
    		}
    	}
    	
    	totalText = Gen.getAmountText(total);
    	
    	return totalText;
    }
    
	@Override
	public void onClick(View view) {
		switch(view.getId()){
			case R.id.ll_user_list_wrapper:
				startActivityForResult(Intents.userList(this), Constants.ACTIVITY_USER_LIST);
				break;
			case R.id.ll_group_list_wrapper:
				startActivityForResult(Intents.groupList(this), Constants.ACTIVITY_GROUP_LIST);
				break;
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
			case Constants.ACTIVITY_USER_LIST:
				this.populateView();
				break;
			case Constants.ACTIVITY_GROUP_LIST:
				this.populateView();
				break;
			case Constants.ACTIVITY_QUICK_ACTION_NEW:
				this.updateQuickActions();
				break;
			case Constants.ACTIVITY_QUICK_ACTION_EXCECUTE:
				this.populateView();
				break;
			case Constants.ACTIVITY_POPUP_DELETE:
				if(resultCode == Activity.RESULT_OK){
					if(data.getBooleanExtra("result", false)){
						this.quickActionAdapter.delete(this.quickActions.get(this.selectedQuickAction));
						this.updateQuickActions();
					}
				}
				break;
			case Constants.ACTIVITY_QUICK_ACTION_EDIT:
				this.updateQuickActions();
				break;
			case Constants.ACTIVITY_SETTINGS:
				this.populateView();
				this.updateQuickActions();
				break;
			case Constants.ACTIVITY_START_PAGE:
				this.populateView();
				finish();
				break;
		}
	}
	
	private void updateQuickActions(){
		this.getQuickActions();
    	this.actionGridApater.setQuickActions(this.quickActions);
    	this.actionGridApater.notifyDataSetChanged();
		this.gvActions.refreshDrawableState();
	}

	@Override
	public void onItemClick(AdapterView<?> v, View arg1, int position, long arg3) {
		switch(v.getId()){
			case R.id.gv_actions:
				startActivityForResult(this.quickActions.get(position).getIntent(), Constants.ACTIVITY_QUICK_ACTION_EXCECUTE);
				break;
		}
		
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		switch(v.getId()){
			case R.id.gv_actions:
				this.selectedQuickAction = info.position;
				
				menu.setHeaderTitle(this.getString(R.string.context_heading));
				
				menu.add(Menu.NONE, Constants.CONTEXT_EDIT, Constants.CONTEXT_EDIT, this.getString(R.string.context_edit));
				menu.add(Menu.NONE, Constants.CONTEXT_DELETE, Constants.CONTEXT_DELETE, this.getString(R.string.context_delete));
				
				break;
		}
	}
	
	public boolean onContextItemSelected(android.view.MenuItem item) {
		int menuItemIndex = item.getItemId();
		switch(menuItemIndex){
			case Constants.CONTEXT_EDIT:
				startActivityForResult(Intents.quickActionEdit(this, this.quickActions.get(this.selectedQuickAction).getId()), Constants.ACTIVITY_QUICK_ACTION_EDIT);
				break;
			case Constants.CONTEXT_DELETE:
				startActivityForResult(Intents.popupDelete(this, Constants.QUICK_ACTION), Constants.ACTIVITY_POPUP_DELETE);
				break;
		}
		return true;
	}
	
	
}