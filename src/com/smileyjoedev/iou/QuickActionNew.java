package com.smileyjoedev.iou;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.smileyjoedev.genLibrary.Debug;
import com.smileyjoedev.genLibrary.R;

public class QuickActionNew extends SherlockActivity implements OnClickListener {

	private QuickAction quickAction;
	private RadioButton rbTypeUser;
	private RadioButton rbTypeGroup;
	private RadioButton rbActionViewAll;
	private RadioButton rbActionViewSpecific;
	private RadioButton rbActionPaymentNew;
	private Button btSave;
	private Button btCancel;
	private Button btChooseTarget;
	private DbUserAdapter userAdapter;
	private DbGroupAdapter groupAdapter;
	private TextView tvTargetTitle;
	private DbQuickActionAdapter quickActionAdapter;
	private boolean isEdit;
	private boolean returnResult;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gen.setTheme(this);
        setContentView(R.layout.quick_action_new);
        
        this.initialize();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        try{
			Bundle extras = getIntent().getExtras();
			this.quickAction = this.quickActionAdapter.getDetails(extras.getLong("quick_action_id"));
			getSupportActionBar().setTitle(R.string.bar_title_quick_action_edit);
			this.isEdit = true;
		} catch(NullPointerException e){
			this.quickAction = new QuickAction(this);
			this.isEdit = false;
		}
        
        try{
			Bundle extras = getIntent().getExtras();
			this.returnResult = extras.getBoolean("return_result", false);
		} catch(NullPointerException e){
			this.returnResult = false;
		}
        
        this.populateView();
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.quick_action_new, menu);
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
    	
    	this.rbTypeUser = (RadioButton) findViewById(R.id.rb_type_user);
    	this.rbTypeUser.setOnClickListener(this);
    	this.rbTypeGroup = (RadioButton) findViewById(R.id.rb_type_group);
    	this.rbTypeGroup.setOnClickListener(this);
    	
    	this.rbActionViewAll = (RadioButton) findViewById(R.id.rb_action_view_all);
    	this.rbActionViewAll.setOnClickListener(this);
    	this.rbActionViewSpecific = (RadioButton) findViewById(R.id.rb_action_view_specific);
    	this.rbActionViewSpecific.setOnClickListener(this);
    	this.rbActionPaymentNew = (RadioButton) findViewById(R.id.rb_action_payment_new);
    	this.rbActionPaymentNew.setOnClickListener(this);
    	
    	this.btCancel = (Button) findViewById(R.id.bt_cancel);
    	this.btCancel.setOnClickListener(this);
    	this.btSave = (Button) findViewById(R.id.bt_save);
    	this.btSave.setOnClickListener(this);
    	this.btChooseTarget = (Button) findViewById(R.id.bt_choose_target);
    	this.btChooseTarget.setOnClickListener(this);
    	
    	this.userAdapter = new DbUserAdapter(this);
    	this.groupAdapter = new DbGroupAdapter(this);
    	
    	this.tvTargetTitle = (TextView) findViewById(R.id.tv_target_title);
    	this.quickActionAdapter = new DbQuickActionAdapter(this);
    	
    	this.returnResult = false;
    }
    
    private void populateView(){
    	if(this.isEdit){
    		switch(this.quickAction.getType()){
	    		case QuickAction.TYPE_USER:
	    			this.rbTypeUser.setChecked(true);
	    			break;
	    		case QuickAction.TYPE_GROUP:
	    			this.rbTypeGroup.setChecked(true);
	    			break;
    		}
    		
    		switch(this.quickAction.getAction()){
	    		case QuickAction.ACTION_PAYMENT_NEW:
	    			this.rbActionPaymentNew.setChecked(true);
	    			break;
	    		case QuickAction.ACTION_VIEW_SPECIFIC:
	    			this.rbActionViewSpecific.setChecked(true);
	    			break;
	    		case QuickAction.ACTION_VIEW_ALL:
	    			this.rbActionViewAll.setChecked(true);
	    			break;
    		}
    		
    		this.tvTargetTitle.setText(this.quickAction.getTitle());
    		
    		this.handleChooseButton();
    		this.handleSaveButton();
    	}
    }
    
    private void handleSaveButton(){
    	boolean actionEnabled = false;
    	boolean altActionEnabled = false;
    	boolean typeEnabled = false;
    	boolean targetEnabled = false;
    	
    	if(this.quickAction.getType() > 0){
    		typeEnabled = true;
    	}
    	
    	if(this.quickAction.getTargetId() > 0){
    		targetEnabled = true;
    	}
    	
    	switch(this.quickAction.getAction()){
    		case QuickAction.ACTION_VIEW_ALL:
    			actionEnabled = true;
    			altActionEnabled = true;
    			break;
    		default:
    			if(targetEnabled){
    				actionEnabled = true;
    			} else {
    				actionEnabled = false;
    			}
    			break;
    	}
    	
    	if(actionEnabled && typeEnabled && (targetEnabled || altActionEnabled)){
    		this.btSave.setEnabled(true);
    	} else {
    		this.btSave.setEnabled(false);
    	}
    }
    
    private void handleChooseButton(){
    	boolean actionEnabled = false;
    	boolean typeEnabled = false;
    	
    	switch(this.quickAction.getType()){
	    	case QuickAction.TYPE_USER:
	    		typeEnabled = true;
	    		break;
	    	case QuickAction.TYPE_GROUP:
	    		typeEnabled = true;
	    		break;
    	}
    	
    	switch(this.quickAction.getAction()){
	    	case QuickAction.ACTION_VIEW_ALL:
	    		actionEnabled = false;
	    		break;
	    	case QuickAction.ACTION_VIEW_SPECIFIC:
	    		actionEnabled = true;
	    		break;
	    	case QuickAction.ACTION_PAYMENT_NEW:
	    		actionEnabled = true;
	    		break;
    	}
    	
    	if(typeEnabled && actionEnabled){
    		this.btChooseTarget.setEnabled(true);
    	} else {
    		this.btChooseTarget.setEnabled(false);
    	}
    }

	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.rb_type_user:
				this.quickAction.setType(QuickAction.TYPE_USER);
				this.rbActionViewSpecific.setText(this.getString(R.string.rb_action_view_specific_user_title));
				this.btChooseTarget.setText(this.getString(R.string.bt_choose_target_user));
				this.tvTargetTitle.setText(this.quickAction.getTitle());
				this.handleChooseButton();
				this.handleSaveButton();
				break;
			case R.id.rb_type_group:
				this.quickAction.setType(QuickAction.TYPE_GROUP);
				this.rbActionViewSpecific.setText(this.getString(R.string.rb_action_view_specific_group_title));
				this.btChooseTarget.setText(this.getString(R.string.bt_choose_target_group));
				this.tvTargetTitle.setText(this.quickAction.getTitle());
				this.handleChooseButton();
				this.handleSaveButton();
				break;
			case R.id.rb_action_view_all:
				this.quickAction.setAction(QuickAction.ACTION_VIEW_ALL);
				this.handleChooseButton();
				this.handleSaveButton();
				break;
			case R.id.rb_action_view_specific:
				this.quickAction.setAction(QuickAction.ACTION_VIEW_SPECIFIC);
				this.handleChooseButton();
				this.handleSaveButton();
				break;
			case R.id.rb_action_payment_new:
				this.quickAction.setAction(QuickAction.ACTION_PAYMENT_NEW);
				this.handleChooseButton();
				this.handleSaveButton();
				break;
			case R.id.bt_cancel:
				finish();
				break;
			case R.id.bt_save:
				if(!this.returnResult){
					long dbId = this.quickActionAdapter.save(this.quickAction);
					if(dbId > 0){
						finish();
					}
				} else {
					Debug.d(this.quickAction.toString());
					Intent resultIntent = new Intent();
					resultIntent.putExtra("quick_action_type", this.quickAction.getType());
					resultIntent.putExtra("quick_action_action", this.quickAction.getAction());
					resultIntent.putExtra("quick_action_target_id", this.quickAction.getTargetId());
					setResult(Activity.RESULT_OK, resultIntent);
					finish();
				}
				break;
			case R.id.bt_choose_target:
				startActivityForResult(Intents.quickActionTargetPicker(this, this.quickAction.getType()), Constants.ACTIVITY_QUICK_ACTION_TARGET_PICKER);
				break;
		}
		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
			case Constants.ACTIVITY_QUICK_ACTION_TARGET_PICKER:
				if(resultCode == Activity.RESULT_OK){
					this.quickAction.setTargetId(data.getIntExtra("target_id", 0));
					this.tvTargetTitle.setText(this.quickAction.getTitle());
					this.handleSaveButton();
				}
				break;
		}
	}
	
}
