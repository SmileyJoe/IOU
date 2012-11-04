package com.smileyjoedev.iou;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.smileyjoedev.iou.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GroupNew extends SherlockActivity implements OnClickListener, OnItemClickListener {
	
	private Button btSave;
	private Button btCancel;
	private EditText etGroupTitle;
	private EditText etGroupDescription;
	private ListView lvUserList;
	private ArrayList<User> users;
	private Views views;
	private DbUserAdapter userAdapter;
	private DbGroupAdapter groupAdapter;
	private Group group;
	private boolean edit;
//	private Button btAddUser;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gen.setTheme(this);
        setContentView(R.layout.group_new);
        
        this.initialize();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		TextView tvDialogHeading = (TextView) findViewById(R.id.tv_dialog_heading);
//		tvDialogHeading.setText(R.string.dialog_heading_group_new);
        
        try{
			Bundle extras = getIntent().getExtras();
			
			int groupId = extras.getInt("group_id");
			int j = 0;
			boolean found = false;
			this.group = this.groupAdapter.getDetails(groupId);
			this.users = this.userAdapter.get();
			
			for(int i = 0; i < this.group.getUsers().size(); i++){
				j = 0;
				found = false;
				while (j < this.users.size() && !found){
					if(this.group.getUser(i).getId() == this.users.get(j).getId()){
						this.users.get(j).setSelected(true);
						found = true;
					}
					j++;
				}
			}
			
			this.group.setUsers(this.users);
			
			this.edit = true;
		} catch(NullPointerException e){
			this.edit = false;
		}
        
        this.populateView();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.group_new, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
			case R.id.menu_add_user:
				startActivityForResult(Intents.newUser(this), Constants.ACTIVITY_NEW_USER);
				return true;
			case android.R.id.home:
				finish();
				return true;
	        default:
	            return super.onOptionsItemSelected(item);
        }

    }
    
    private void initialize(){
    	this.btSave = (Button) findViewById(R.id.bt_save);
    	this.btSave.setOnClickListener(this);
    	this.btCancel = (Button) findViewById(R.id.bt_cancel);
    	this.btCancel.setOnClickListener(this);
    	this.etGroupTitle = (EditText) findViewById(R.id.et_group_title);
    	this.etGroupDescription = (EditText) findViewById(R.id.et_group_description);
    	this.lvUserList = (ListView) findViewById(R.id.lv_user_list);
    	this.lvUserList.setOnItemClickListener(this);
    	this.users = new ArrayList<User>();
    	this.views = new Views(this);
    	this.userAdapter = new DbUserAdapter(this, false);
    	this.groupAdapter = new DbGroupAdapter(this);
    	this.group = new Group();
    	this.edit = false;
//    	this.btAddUser = (Button) findViewById(R.id.bt_add_user);
//    	this.btAddUser.setOnClickListener(this);
    }
    
    private void populateView(){
    	if(!this.edit){
    		this.users = this.userAdapter.get();
    	} else {
    		this.users = this.group.getUsers();
    		this.etGroupTitle.setText(this.group.getTitle());
    		this.etGroupDescription.setText(this.group.getDescription());
    	}
    	
    	this.views.userList(this.users, this.lvUserList, true);
    }

	@Override
	public void onClick(View view) {
		switch(view.getId()){
			case R.id.bt_save:
				String groupTitle = this.etGroupTitle.getText().toString().trim();
				
				this.group.setTitle(groupTitle);
				this.group.setDescription(this.etGroupDescription.getText().toString().trim());
				this.group.setUsers(this.users);
				
				if(!this.edit){
					long dbId = this.groupAdapter.save(this.group);
					
					if(dbId > 0){
						finish();
					}
				} else {
					int numRows = this.groupAdapter.update(this.group);
					
					if(numRows > 0){
						finish();
					}
				}
				
				break;
			case R.id.bt_cancel:
				finish();
				break;
//			case R.id.bt_add_user:
//				startActivityForResult(Intents.new_user(this), Constants.ACTIVITY_NEW_USER);
//				break;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> view, View row, int position, long arg3) {
		switch(view.getId()){
			case R.id.lv_user_list:
				int elementPos = position - this.lvUserList.getFirstVisiblePosition();
				CheckBox clickedBox = (CheckBox) row.findViewById(R.id.cb_user_selected);
				if(clickedBox.isChecked()){
					clickedBox.setChecked(false);
					this.users.get(position).setSelected(false);
				}else{
					clickedBox.setChecked(true);
					this.users.get(position).setSelected(true);
				}
				
				break;
		}
		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
			case Constants.ACTIVITY_NEW_USER:
				this.populateView();
				break;
		}
	}
	
}
