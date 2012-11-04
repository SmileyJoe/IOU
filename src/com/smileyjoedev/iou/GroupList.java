package com.smileyjoedev.iou;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;


public class GroupList extends SherlockActivity implements OnItemClickListener, OnClickListener, OnItemSelectedListener {
	
	private Views views;
	private SharedPreferences prefs;
	private ListView lvGroupList;
	private ArrayList<Group> groups; 
	private DbGroupAdapter groupAdapter;
	private int selectedGroup;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gen.setTheme(this);
        setContentView(R.layout.group_list);
        
        this.initialize();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.populateView();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.group_list, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
			case R.id.menu_add_group:
				startActivityForResult(Intents.groupNew(this), Constants.ACTIVITY_GROUP_NEW);
				return true;
			case android.R.id.home:
				finish();
				return true;
	        default:
	            return super.onOptionsItemSelected(item);
        }

    }
    
    public void initialize(){
    	this.views = new Views(this);
    	this.prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
    	this.lvGroupList = (ListView) findViewById(R.id.lv_group_list);
    	this.lvGroupList.setOnItemClickListener(this);
    	registerForContextMenu(this.lvGroupList);
    	this.groups = new ArrayList<Group>();
    	this.groupAdapter = new DbGroupAdapter(this);
    	this.selectedGroup = 0;
    	
    }
    
    public void populateView(boolean getAll){
    	this.groups = this.groupAdapter.get();
    	this.views.groupList(this.groups, this.lvGroupList);
    }
    
    private void populateView(){
    	this.populateView(true);
    }
    
	@Override
	public void onItemSelected(AdapterView<?> view, View arg1, int position, long arg3) {
		switch(view.getId()){
		}
		
		this.populateView(false);
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> view, View arg1, int position, long arg3) {
		switch(view.getId()){
			case R.id.lv_group_list:
				startActivityForResult(Intents.groupView(this, this.groups.get(position).getId()), Constants.ACTIVITY_GROUP_VIEW);
				break;
		}
		
	}
	
	@Override
	public void onClick(View view) {
		switch(view.getId()){
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
			case Constants.ACTIVITY_POPUP_DELETE:
				if(resultCode == Activity.RESULT_OK){
					if(data.getBooleanExtra("result", false)){
						this.groupAdapter.delete(this.groups.get(this.selectedGroup));
						this.populateView();
					}
				}
				break;
			case Constants.ACTIVITY_GROUP_NEW:
				this.populateView();
				break;
			case Constants.ACTIVITY_GROUP_EDIT:
				this.populateView();
				break;
			case Constants.ACTIVITY_GROUP_VIEW:
				this.populateView();
				break;
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		switch(v.getId()){
			case R.id.lv_group_list:
				this.selectedGroup = info.position;
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
				startActivityForResult(Intents.groupEdit(this, this.groups.get(this.selectedGroup).getId()), Constants.ACTIVITY_GROUP_EDIT);
				break;
			case Constants.CONTEXT_DELETE:
				startActivityForResult(Intents.popupDelete(this, Constants.GROUP), Constants.ACTIVITY_POPUP_DELETE);
				break;
		}
		return true;
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}

}