package com.smileyjoedev.iou;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.smileyjoedev.genLibrary.Contact;
import com.smileyjoedev.genLibrary.Contacts;
import com.smileyjoedev.genLibrary.Debug;
import com.smileyjoedev.genLibrary.Notify;
import com.smileyjoedev.iou.R;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class UserNew extends SherlockActivity implements OnClickListener, TextWatcher, OnItemClickListener {
	
	private Button btCancel;
	private Button btSave;
	private AutoCompleteTextView etName;
	private DbUserAdapter userAdapter;
	private int userId = 0;
	private User user;
	private boolean edit;
	private String userName;
	private String oldUserName;
	private ImageView ivUserImage; 
	private ToggleButton tbMinimalisticTextFlag;
	private EditText etVariableName;
	private LinearLayout llVariableName;
	private TextView tvVariableNameInfo;
	private SharedPreferences prefs;
	private RelativeLayout rlMinimalisticTextWrapper;
	private Contacts cont;
	private ArrayList<Contact> contacts;
	private Long contactId;
	private Long oldContactId;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_new);
		this.initialize();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		try{
			Bundle extras = getIntent().getExtras();
			this.userId = extras.getInt("user_id");
			getSupportActionBar().setTitle(R.string.bar_title_user_edit);
			this.user = new User(this);
			this.user = this.userAdapter.getDetails(this.userId);
			this.edit = true;
			this.oldUserName = this.user.getName();
			this.oldContactId = this.user.getContactId();
			this.btSave.setEnabled(true);
		} catch(NullPointerException e){
			this.user = new User(this);
			this.edit = false;
			this.btSave.setEnabled(false);
		}
		
		
		this.populateAutoNames();
		this.populateUserImage();
		this.populateView();
		
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.user_new, menu);
        return super.onCreateOptionsMenu(menu);
    }
	
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
			case android.R.id.home:
				this.hideKeyboard();
				finish();
				return true;
			default:
	            return super.onOptionsItemSelected(item);
        }

    }
    
	public void initialize() {
		this.btSave = (Button) findViewById(R.id.bt_save);
		this.btSave.setOnClickListener(this);
		this.btCancel = (Button) findViewById(R.id.bt_cancel);
		this.btCancel.setOnClickListener(this);
		this.etName = (AutoCompleteTextView) findViewById(R.id.et_user_name);
		this.etName.addTextChangedListener(this);
		this.etName.setOnItemClickListener(this);
		this.userAdapter = new DbUserAdapter(this);
		this.userName = "";
		this.ivUserImage = (ImageView) findViewById(R.id.iv_user_image);
		this.ivUserImage.setOnClickListener(this);
		this.tbMinimalisticTextFlag = (ToggleButton) findViewById(R.id.tb_minimalistic_text_flag);
		this.tbMinimalisticTextFlag.setOnClickListener(this);
		this.llVariableName = (LinearLayout) findViewById(R.id.ll_variable_name);
		this.etVariableName = (EditText) findViewById(R.id.et_variable_name);
		this.tvVariableNameInfo = (TextView) findViewById(R.id.tv_variable_name_info);
		this.prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		this.rlMinimalisticTextWrapper = (RelativeLayout) findViewById(R.id.rl_minimalistic_text_wrapper);
		this.cont = new Contacts(this);
		this.contacts = new ArrayList<Contact>();
		this.contactId = (long) 0;
		this.oldContactId = (long) 0;
	}
	
	public void populateView(){
		if(this.edit){
			this.etName.setText(this.user.getName());
			
			
			this.populateUserImage();

			if(this.user.isUsingMinimalisticText()){
				this.tbMinimalisticTextFlag.setChecked(true);
				this.llVariableName.setVisibility(View.VISIBLE);
				this.etVariableName.setText(this.user.getVariableName());
				this.populateVariableNameInfo(this.user.getName());
			}
		}
		
		if(!this.prefs.getBoolean("allow_minimalistic_text", true)){
			this.llVariableName.setVisibility(View.GONE);
			this.rlMinimalisticTextWrapper.setVisibility(View.GONE);
		}
	}
	
	private void populateUserImage(){
		Gen.setUserImage(this, this.ivUserImage, this.user);
	}
	
	private void populateVariableNameInfo(String name){
		this.tvVariableNameInfo.setText("A variable with the name " + name + "SIGN will be created for you. It will contain '0' for a positive balance or '1' for a negative balance");
	}
	
	private void populateAutoNames(){
		Debug.d("populate auto names");
		this.contacts = cont.getList(true);
		Debug.d("Contact size", this.contacts.size());
		for(int i = 0; i < this.contacts.size(); i++){
			Debug.d(this.contacts.get(i).getName());
		}
		
		ArrayList<String> names = cont.getNameList(contacts);
		
		Debug.d("names size", names.size());
		
		for(int i = 0; i < names.size(); i++){
			Debug.d(names.get(i).getBytes().toString());
		}
		
		this.etName.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, cont.getNameList(contacts)));
	}
	
	private void updateUser(){
		this.userAdapter.update(this.user);
	}
	
	private void saveUser(){
		this.userAdapter.save(this.user);
	}
	
	private void hideKeyboard(){
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(this.etName.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(this.etVariableName.getWindowToken(), 0);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.bt_save:
				String userName = this.etName.getText().toString().trim();
				this.userName = userName;
				String oldName = this.user.getName();
				
				if(userName.equals("")){
					Notify.toast(this, R.string.toast_no_user_name);
					break;
				} else {
				}
				
				if(this.contactId == 0){
					if(this.user.getName().equals(userName)){
						this.contactId = this.oldContactId;
					}
				}
				
				this.user.setName(userName);
				this.user.setVariableName(this.etVariableName.getText().toString().trim().replace(" ", ""));
				
				
				this.user.setContactId(this.contactId);
				
				if(this.edit){
					if(oldName.equals(userName)){
						this.updateUser();
						finish();
					} else {
						if(!this.userAdapter.checkUserExists(user.getName())){
							this.updateUser();
							finish();
						} else {
							Notify.toast(this, R.string.toast_user_exists, user.getName());
						}
					}
				} else {
					if(!this.userAdapter.checkUserExists(user.getName())){
						this.saveUser();
						finish();
					} else {
						Notify.toast(this, R.string.toast_user_exists, user.getName());
					}
				}
				
				Gen.displayMinimalisticText(this, this.user, 0);
				this.hideKeyboard();
				break;
			case R.id.bt_cancel:
				this.hideKeyboard();
				finish();
				break;
			case R.id.tb_minimalistic_text_flag:
				if(this.user.isUsingMinimalisticText()){
					this.user.setMinimalisticTextFlag(0);
					this.llVariableName.setVisibility(View.GONE);
				} else {
					this.user.setMinimalisticTextFlag(1);
					this.llVariableName.setVisibility(View.VISIBLE);
					String name = this.etName.getText().toString().trim().toUpperCase().replace(" ", "");
					if(name.equals("")){
						this.etVariableName.setHint("eg. %IOUUSERNAME");
						this.populateVariableNameInfo(" eg. %IOUUSERNAME");
					} else {
						this.etVariableName.setText("%IOU" + name);
						this.etVariableName.setHint("eg. %IOU" + name);
						this.populateVariableNameInfo("%IOU" + name);
					}
					
				}
				break;
		}
		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
			
		}
	}
	
	@Override
	public void afterTextChanged(Editable arg0) {
		if(arg0.toString().equals("")){
			this.btSave.setEnabled(false);
		} else {
			this.btSave.setEnabled(true);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
		HashMap<String, Long> map = this.cont.getNameHash(this.contacts);
		this.contactId = map.get((String)parent.getItemAtPosition(position));
		this.user.setContactId(this.contactId);
		this.populateUserImage();
	}
	
}
