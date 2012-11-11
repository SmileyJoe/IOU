package com.smileyjoedev.iou;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.smileyjoedev.genLibrary.R;

public class QuickActionTargetPicker extends SherlockActivity implements OnItemClickListener, OnClickListener {

	private ListView lvTargetPicker;
	private Button btCancel;
	private DbUserAdapter userAdapter;
	private DbGroupAdapter groupAdapter;
	private ArrayList<Group> groups;
	private ArrayList<User> users;
	private int type;
	private Views view;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gen.setTheme(this);
        setContentView(R.layout.quick_action_target_picker);
        
        try{
        	Bundle extras = getIntent().getExtras();
    		
            if(extras.containsKey("action_type")){
            	this.type = extras.getInt("action_type");
            }
            
        } catch(NullPointerException e){
        }
        
        this.initialize();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.populateView();
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.quick_action_target_picker, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
	        case android.R.id.home:
				finish();
				return true;
	        default:
	            return super.onOptionsItemSelected(item);
        }

    }
    
    private void initialize(){
    	this.lvTargetPicker = (ListView) findViewById(R.id.lv_target_list);
    	this.lvTargetPicker.setOnItemClickListener(this);
    	
    	this.btCancel = (Button) findViewById(R.id.bt_cancel);
    	this.btCancel.setOnClickListener(this);
    	
    	this.lvTargetPicker = (ListView) findViewById(R.id.lv_target_list);
    	this.lvTargetPicker.setOnItemClickListener(this);
    	
    	this.view = new Views(this);
    	
    	switch(this.type){
	    	case QuickAction.TYPE_USER:
	    		this.userAdapter = new DbUserAdapter(this);
	    		this.users = this.userAdapter.get();
	    		break;
	    	case QuickAction.TYPE_GROUP:
	    		this.groupAdapter = new DbGroupAdapter(this);
	    		this.groups = this.groupAdapter.get();
	    		break;
    	}
    }
    
    private void populateView(){
    	
    	switch(this.type){
	    	case QuickAction.TYPE_USER:
	    		this.view.userList(this.users, this.lvTargetPicker);
	    		break;
	    	case QuickAction.TYPE_GROUP:
	    		this.view.groupList(this.groups, this.lvTargetPicker);
	    		break;
		}
    	
    }

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.bt_cancel:
				finish();
				break;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> v, View arg1, int position, long arg3) {
		switch(v.getId()){
			case R.id.lv_target_list:
				Intent resultIntent = new Intent();
				
				switch(this.type){
					case QuickAction.TYPE_USER:
						resultIntent.putExtra("target_id", this.users.get(position).getId());
						break;
					case QuickAction.TYPE_GROUP:
						resultIntent.putExtra("target_id", this.groups.get(position).getId());
						break;
				}
				
				setResult(Activity.RESULT_OK, resultIntent);
				finish();
				break;
		}
		
	}
	
}
