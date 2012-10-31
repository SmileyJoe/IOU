package com.joedev.iou;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GroupView extends Activity implements OnItemClickListener, OnClickListener {
	
	private ArrayList<GroupPayment> payments;
	private DbGroupAdapter groupAdapter;
	private Group group;
	private TextView tvHeaderText;
	private ListView lvPaymentList;
	private Button btAddPayment;
	private Views views;
	private DbGroupPaymentAdapter groupPaymentAdapter;
	private Button btOverflow;
	private LinearLayout llOverflowWrapper;
	private LinearLayout llRepayment;
	private RelativeLayout rlMainWrapper;
	private int selectedPayment;
	private LinearLayout llUserDetails;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gen.setTheme(this);
        setContentView(R.layout.group_view);
        this.initialize();
        
        Bundle extras = getIntent().getExtras();
		
		int groupId = extras.getInt("group_id");
		this.group = this.groupAdapter.get_details(groupId);
		
        this.populate_view();
        
    }
    
    private void initialize(){
    	this.payments = new ArrayList<GroupPayment>();
    	this.groupAdapter = new DbGroupAdapter(this);
    	this.group = new Group();
    	this.tvHeaderText = (TextView) findViewById(R.id.tv_header_text);
    	this.lvPaymentList = (ListView) findViewById(R.id.lv_payment_list);
    	this.lvPaymentList.setOnItemClickListener(this);
    	registerForContextMenu(this.lvPaymentList);
    	this.btAddPayment = (Button) findViewById(R.id.bt_add_payment);
    	this.btAddPayment.setOnClickListener(this);
    	this.views = new Views(this);
    	this.groupPaymentAdapter = new DbGroupPaymentAdapter(this);
    	this.btOverflow = (Button) findViewById(R.id.bt_overflow);
    	this.btOverflow.setOnClickListener(this);
    	this.llOverflowWrapper = (LinearLayout) findViewById(R.id.ll_overflow_wrapper);
    	this.llRepayment = (LinearLayout) findViewById(R.id.ll_repayment);
    	this.llRepayment.setOnClickListener(this);
    	this.rlMainWrapper = (RelativeLayout) findViewById(R.id.rl_main_wrapper);
    	this.rlMainWrapper.setOnClickListener(this);
    	this.selectedPayment = 0;
    	this.llUserDetails = (LinearLayout) findViewById(R.id.ll_user_details);
    	this.llUserDetails.setOnClickListener(this);
    }
    
    private void populate_view(){
    	this.payments = this.groupPaymentAdapter.get_by_group(this.group.get_id());
    	this.tvHeaderText.setText(this.group.get_title());
    	this.views.group_payment_list(this.payments, this.lvPaymentList);
    }

	@Override
	public void onClick(View v) {
		
		if(v.getId() != R.id.bt_overflow){
			this.close_drop_downs();
		}
		
		switch(v.getId()){
			case R.id.bt_add_payment:
				startActivityForResult(Intents.group_payment_new(this, group.get_id()), Constants.ACTIVITY_GROUP_PAYMENT_NEW);
				break;
			case R.id.bt_overflow:
				Debug.v("overflow clicked");
				if(this.llOverflowWrapper.isShown()){
					this.llOverflowWrapper.setVisibility(View.GONE);
				} else {
					this.llOverflowWrapper.setVisibility(View.VISIBLE);
					this.llOverflowWrapper.bringToFront();
				}
				break;
			case R.id.ll_repayment:
				startActivityForResult(Intents.group_repayment(this, this.group.get_id()), Constants.ACTIVITY_GROUP_REPAYMENT);
				break;
			case R.id.ll_user_details:
				startActivityForResult(Intents.group_user_payment_details(this, this.group.get_id()), Constants.ACTIVITY_GROUP_USER_PAYMENT_DETAILS);
				break;
		}
		
	}

	@Override
	public void onItemClick(AdapterView<?> view, View arg1, int position, long arg3) {
		this.close_drop_downs();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode){
			case Constants.ACTIVITY_GROUP_PAYMENT_NEW:
				this.populate_view();
				break;
			case Constants.ACTIVITY_GROUP_REPAYMENT:
				this.populate_view();
				break;
			case Constants.ACTIVITY_POPUP_DELETE:
				if(resultCode == Activity.RESULT_OK){
					if(data.getBooleanExtra("result", false)){
						this.groupPaymentAdapter.delete(this.payments.get(this.selectedPayment));
						this.populate_view();
					}
				}
				break;
			case Constants.ACTIVITY_GROUP_PAYMENT_EDIT:
				this.populate_view();
				break;
		}
	}

	private boolean close_drop_downs(){
		boolean open = false;
		if(this.llOverflowWrapper.isShown()){
			this.llOverflowWrapper.setVisibility(View.GONE);
			open = true;
		}
		
		return open;
	}
	
	public void onBackPressed() {
		boolean open = this.close_drop_downs();
		
		if(!open){
			finish();
		}
	}
	
	public boolean onKeyDown(int keycode, KeyEvent event ) {
		if(keycode == KeyEvent.KEYCODE_MENU){
			this.btOverflow.performClick();
		}
		return super.onKeyDown(keycode,event);  
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		switch(v.getId()){
			case R.id.lv_payment_list:
				this.selectedPayment = info.position;
				menu.setHeaderTitle(this.getString(R.string.context_heading));
				
				menu.add(Menu.NONE, Constants.CONTEXT_EDIT, Constants.CONTEXT_EDIT, this.getString(R.string.context_edit));
				menu.add(Menu.NONE, Constants.CONTEXT_DELETE, Constants.CONTEXT_DELETE, this.getString(R.string.context_delete));
				break;
		}
	}
	
	public boolean onContextItemSelected(MenuItem item) {
		//AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
			int menuItemIndex = item.getItemId();
		switch(menuItemIndex){
			case Constants.CONTEXT_EDIT:
				startActivityForResult(Intents.group_payment_edit(this,this.group.get_id(), this.payments.get(this.selectedPayment).get_id()), Constants.ACTIVITY_GROUP_PAYMENT_EDIT);
				break;
			case Constants.CONTEXT_DELETE:
				startActivityForResult(Intents.popup_delete(this, Constants.PAYMENT), Constants.ACTIVITY_POPUP_DELETE);
				break;
		}
		return true;
	}


}
