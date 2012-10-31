package com.joedev.iou;

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
//	private Button btChoosePhoto;
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
//	private ImageView ivDeleteUserImage;
//	private RelativeLayout rlUserImageWrapper;
//	private boolean allowImageDelete;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_new);
//		Gen.fill_window(getWindow());
		this.initialize();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		TextView tvDialogHeading = (TextView) findViewById(R.id.tv_dialog_heading);
//		tvDialogHeading.setText(R.string.dialog_heading_user_new);
		
		try{
			Bundle extras = getIntent().getExtras();
			this.userId = extras.getInt("user_id");
			getSupportActionBar().setTitle(R.string.bar_title_user_edit);
			this.user = new User();
			this.user = this.userAdapter.get_details(this.userId);
			this.edit = true;
			this.oldUserName = this.user.get_name();
			this.oldContactId = this.user.get_contact_id();
		} catch(NullPointerException e){
			this.user = new User();
			this.edit = false;
		}
		
		
		this.populate_auto_names();
		this.populate_user_image();
		this.populate_view();
		
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.new_user, menu);
        return super.onCreateOptionsMenu(menu);
    }
	
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
			case android.R.id.home:
				this.hide_keyboard();
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
//		this.btChoosePhoto = (Button) findViewById(R.id.bt_choose_photo);
//		this.btChoosePhoto.setOnClickListener(this);
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
//		this.ivDeleteUserImage = (ImageView) findViewById(R.id.iv_delete_user_image);
//		this.ivDeleteUserImage.setOnClickListener(this);
//		this.rlUserImageWrapper = (RelativeLayout) findViewById(R.id.rl_user_image_wrapper);
//		this.allowImageDelete = false;
	}
	
	public void populate_view(){
		if(this.edit){
			this.etName.setText(this.user.get_name());
			
			
			this.populate_user_image();

//			java.io.File file = new java.io.File(Constants.IMAGE_PATH + fileName + Constants.IMAGE_EXTENSION);
//			if(file.exists()){
//				Bitmap bm = BitmapFactory.decodeFile(Constants.IMAGE_PATH + fileName + Constants.IMAGE_EXTENSION);
//				
//				this.ivUserImage.setImageBitmap(bm);
//				this.ivUserImage.setVisibility(View.VISIBLE);
//			}else{
//				this.ivUserImage.setVisibility(View.GONE);
//			}
			
			if(this.user.is_using_minimalistic_text()){
				this.tbMinimalisticTextFlag.setChecked(true);
				this.llVariableName.setVisibility(View.VISIBLE);
				this.etVariableName.setText(this.user.get_variable_name());
				this.populate_variable_name_info(this.user.get_name());
			}
		}
		
		if(!this.prefs.getBoolean("allow_minimalistic_text", true)){
			this.llVariableName.setVisibility(View.GONE);
			this.rlMinimalisticTextWrapper.setVisibility(View.GONE);
		}
	}
	
	private void populate_user_image(){
		Gen.set_user_image(this, this.ivUserImage, this.user);
//		if(Gen.set_user_image(this, this.ivUserImage, this.user)){
//			this.ivUserImage.setVisibility(View.VISIBLE);
//		} else {
//			this.ivUserImage.setVisibility(View.GONE);
//		}
		
//		java.io.File file = new java.io.File(Constants.IMAGE_PATH + this.user.get_name() + Constants.IMAGE_EXTENSION);
//		if(file.exists()){
//			this.allowImageDelete = true;
//		}
	}
	
	private void populate_variable_name_info(String name){
		this.tvVariableNameInfo.setText("A variable with the name " + name + "SIGN will be created for you. It will contain '0' for a positive balance or '1' for a negative balance");
	}
	
	private void populate_auto_names(){
		this.contacts = cont.get_list(true);
		   
		this.etName.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, cont.get_name_list(contacts)));
	}
	
	private void update_user(){
		this.userAdapter.update(this.user);
	}
	
	private void save_user(){
		this.userAdapter.save(this.user);
	}
	
	private void hide_keyboard(){
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
				String oldName = this.user.get_name();
				
				if(userName.equals("")){
					Notify.toast(this, R.string.toast_no_user_name);
					break;
				} else {
				}
				
				if(this.contactId == 0){
					if(this.user.get_name().equals(userName)){
						this.contactId = this.oldContactId;
					}
				}
				
				this.user.set_name(userName);
				this.user.set_variable_name(this.etVariableName.getText().toString().trim().replace(" ", ""));
				
				
				this.user.set_contact_id(this.contactId);
				
				if(this.edit){
					if(oldName.equals(userName)){
						this.update_user();
//						this.rename_image();
						finish();
					} else {
						if(!this.userAdapter.check_user_exists(user.get_name())){
							this.update_user();
//							this.rename_image();
							finish();
						} else {
							Notify.toast(this, R.string.toast_user_exists, user.get_name());
						}
					}
				} else {
					if(!this.userAdapter.check_user_exists(user.get_name())){
						this.save_user();
//						this.rename_image();
//						this.delete_image();
						finish();
					} else {
						Notify.toast(this, R.string.toast_user_exists, user.get_name());
					}
				}
				
				Gen.display_minimalistic_text(this, this.user, 0);
				this.hide_keyboard();
				break;
			case R.id.bt_cancel:
				this.hide_keyboard();
				finish();
				break;
//			case R.id.bt_choose_photo:
//
//					this.userName = this.etName.getText().toString().trim();
//					int width = get_screen_width();
//					int height = get_screen_height();
//					
//					try{
//						startActivityForResult(Intents.take_picture(width, height), Constants.ACTIVITY_TAKE_PHOTO);
//					}catch(ActivityNotFoundException e){
//						
//					}
//				
//				break;
			case R.id.tb_minimalistic_text_flag:
				if(this.user.is_using_minimalistic_text()){
					this.user.set_minimalistic_text_flag(0);
					this.llVariableName.setVisibility(View.GONE);
				} else {
					this.user.set_minimalistic_text_flag(1);
					this.llVariableName.setVisibility(View.VISIBLE);
					String name = this.etName.getText().toString().trim().toUpperCase().replace(" ", "");
					if(name.equals("")){
						this.etVariableName.setHint("eg. %IOUUSERNAME");
						this.populate_variable_name_info(" eg. %IOUUSERNAME");
					} else {
						this.etVariableName.setText("%IOU" + name);
						this.etVariableName.setHint("eg. %IOU" + name);
						this.populate_variable_name_info("%IOU" + name);
					}
					
				}
				break;
//			case R.id.iv_user_image:
//				if(this.allowImageDelete){
//					if(this.ivDeleteUserImage.isShown()){
//						this.ivDeleteUserImage.setVisibility(View.GONE);
//					} else {
//						this.ivDeleteUserImage.setVisibility(View.VISIBLE);
//					}
//				}
//				
//				
//				break;
//			case R.id.iv_delete_user_image:
//				this.delete_image();
//				this.populate_user_image();
//				this.allowImageDelete = false;
//				this.ivDeleteUserImage.setVisibility(View.GONE);
////				this.ivUserImage.setVisibility(View.GONE);
////				this.ivDeleteUserImage.setVisibility(View.GONE);
////				this.rlUserImageWrapper.setVisibility(View.GONE);
//				break;
		}
		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		//super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
//			case Constants.ACTIVITY_TAKE_PHOTO:
//
//				if(resultCode == Activity.RESULT_OK){
//					final Bundle extras1 = data.getExtras();
//					this.userName = this.etName.getText().toString().trim();
//					
//					if(extras1 != null){
//						Bitmap photo = extras1.getParcelable("data");
//						
//						ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//						photo.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
//						
//						File fp = new File(Constants.IMAGE_PATH);
//						
//						fp.mkdirs();
//						
//						
//						File f = new File(Constants.IMAGE_PATH + Constants.TEMP_IMAGE_NAME + Constants.IMAGE_EXTENSION);
//						
//						try{
//							f.createNewFile();
//							// write the bytes in file
//							FileOutputStream fo = new FileOutputStream(f);
//							fo.write(bytes.toByteArray());
//						}catch(IOException e){
//							e.printStackTrace();
//						}
//						
////						this.populate_user_image();
//						this.ivUserImage.setVisibility(View.VISIBLE);
////						Gen.set_user_image(this, this.ivUserImage, this.user);
//						this.ivUserImage.setImageBitmap(photo);
//						this.allowImageDelete = true;
//					}
//					
//					/*InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//					mgr.showSoftInput(this.ivPlayerImage, InputMethodManager.SHOW_IMPLICIT);*/
//				}
//				
//				break;
			
		}
	}
	
//	public void rename_image() {
//		File tempFile = new File(Constants.IMAGE_PATH + Constants.TEMP_IMAGE_NAME + Constants.IMAGE_EXTENSION);
//		if(!tempFile.exists()){
//			tempFile = new File(Constants.IMAGE_PATH + this.oldUserName + Constants.IMAGE_EXTENSION);
//		}
//		File newFile = new File(Constants.IMAGE_PATH + this.userName + Constants.IMAGE_EXTENSION);
//		
//		tempFile.renameTo(newFile);
//	}
//	
//	public void delete_image() {
//		java.io.File file = new java.io.File(Constants.IMAGE_PATH + this.oldUserName + Constants.IMAGE_EXTENSION);
//		if(file.exists()){
//			file.delete();
//		}
//		
//		file = new java.io.File(Constants.IMAGE_PATH + Constants.TEMP_IMAGE_NAME + Constants.IMAGE_EXTENSION);
//		if(file.exists()){
//			file.delete();
//		}
//	}
	
//	private int get_screen_width() {
//		int width;
//		DisplayMetrics displaymetrics = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//		width = displaymetrics.widthPixels;
//		return width;
//	}
//	
//	private int get_screen_height() {
//		int height;
//		DisplayMetrics displaymetrics = new DisplayMetrics();
//		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//		height = displaymetrics.heightPixels;
//		return height;
//	}
	
	@Override
	public void afterTextChanged(Editable arg0) {
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
		HashMap<String, Long> map = this.cont.get_name_hash(this.contacts);
		this.contactId = map.get((String)parent.getItemAtPosition(position));
		this.user.set_contact_id(this.contactId);
		this.populate_user_image();
	}
	
}
