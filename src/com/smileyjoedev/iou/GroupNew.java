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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GroupNew extends SherlockActivity implements OnClickListener, OnItemClickListener, TextWatcher {
	
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
	private UserListAdapter userListAdapter;
	private boolean isTitle;
	private int numSelectedUser;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gen.setTheme(this);
        setContentView(R.layout.group_new);
        
        this.initialize();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
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
			
			this.isTitle = true;
			this.numSelectedUser = this.group.getUsers().size();
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
    
    private void updateUserList(){
    	this.users = this.userAdapter.get();
    	this.userListAdapter.setUsers(this.users);
    	this.userListAdapter.notifyDataSetChanged();
		this.lvUserList.refreshDrawableState();
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
			case R.id.menu_add_user:
				startActivityForResult(Intents.userNew(this), Constants.ACTIVITY_NEW_USER);
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
    	this.etGroupTitle.addTextChangedListener(this);
    	this.etGroupDescription = (EditText) findViewById(R.id.et_group_description);
    	this.lvUserList = (ListView) findViewById(R.id.lv_user_list);
    	this.lvUserList.setOnItemClickListener(this);
    	this.users = new ArrayList<User>();
    	this.views = new Views(this);
    	this.userAdapter = new DbUserAdapter(this, false);
    	this.groupAdapter = new DbGroupAdapter(this);
    	this.group = new Group();
    	this.edit = false;
    	this.isTitle = false;
    	this.numSelectedUser = 0;
    }
    
    private void populateView(){
    	if(!this.edit){
    		this.users = this.userAdapter.get();
    	} else {
    		this.users = this.group.getUsers();
    		this.etGroupTitle.setText(this.group.getTitle());
    		this.etGroupDescription.setText(this.group.getDescription());
    	}
    	
    	this.userListAdapter = this.views.userList(this.users, (LinearLayout) findViewById(R.id.ll_user_list_wrapper), true);
    	this.enableSave();
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
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> view, View row, int position, long arg3) {
		switch(view.getId()){
			case R.id.lv_user_list:
				int elementPos = position - this.lvUserList.getFirstVisiblePosition();
				CheckBox clickedBox = (CheckBox) row.findViewById(R.id.cb_user_selected);
				if(clickedBox.isChecked()){
					this.numSelectedUser--;
					clickedBox.setChecked(false);
					this.users.get(position).setSelected(false);
				}else{
					this.numSelectedUser++;
					clickedBox.setChecked(true);
					this.users.get(position).setSelected(true);
				}
				this.enableSave();
				break;
		}
		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
			case Constants.ACTIVITY_NEW_USER:
				this.updateUserList();
				break;
		}
	}
	
	private void enableSave(){
		if((this.isTitle) && (this.numSelectedUser > 0)){
			this.btSave.setEnabled(true);
		} else {
			this.btSave.setEnabled(false);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		if(s.toString().equals("")){
			this.isTitle = false;
		} else {
			this.isTitle = true;
		}
		
		this.enableSave();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
	}
	
}
