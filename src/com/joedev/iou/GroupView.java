package com.joedev.iou;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
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

public class GroupView extends SherlockActivity implements OnItemClickListener, OnClickListener {
	
	private ArrayList<GroupPayment> payments;
	private DbGroupAdapter groupAdapter;
	private Group group;
	private ListView lvPaymentList;
	private Views views;
	private DbGroupPaymentAdapter groupPaymentAdapter;
	private int selectedPayment;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gen.setTheme(this);
        setContentView(R.layout.group_view);
        this.initialize();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extras = getIntent().getExtras();
		
		int groupId = extras.getInt("group_id");
		this.group = this.groupAdapter.get_details(groupId);
		
        this.populate_view();
        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.group_view, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case R.id.menu_add_payment:
				startActivityForResult(Intents.group_payment_new(this, group.get_id()), Constants.ACTIVITY_GROUP_PAYMENT_NEW);
				return true;
			case R.id.menu_repayment:
				startActivityForResult(Intents.group_repayment(this, this.group.get_id()), Constants.ACTIVITY_GROUP_REPAYMENT);
				return true;
			case R.id.menu_user_details:
				startActivityForResult(Intents.group_user_payment_details(this, this.group.get_id()), Constants.ACTIVITY_GROUP_USER_PAYMENT_DETAILS);
				return true;
			case android.R.id.home:
				finish();
				return true;
	        default:
	            return super.onOptionsItemSelected(item);
        }

    }
    
    private void initialize(){
    	this.payments = new ArrayList<GroupPayment>();
    	this.groupAdapter = new DbGroupAdapter(this);
    	this.group = new Group();
    	this.lvPaymentList = (ListView) findViewById(R.id.lv_payment_list);
    	this.lvPaymentList.setOnItemClickListener(this);
    	registerForContextMenu(this.lvPaymentList);
    	this.views = new Views(this);
    	this.groupPaymentAdapter = new DbGroupPaymentAdapter(this);
    	this.selectedPayment = 0;
    }
    
    private void populate_view(){
    	this.payments = this.groupPaymentAdapter.get_by_group(this.group.get_id());
    	this.views.group_payment_list(this.payments, this.lvPaymentList);
    }

	@Override
	public void onClick(View v) {
		
		
	}

	@Override
	public void onItemClick(AdapterView<?> view, View arg1, int position, long arg3) {
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
	
	public boolean onContextItemSelected(android.view.MenuItem item) {
		//AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		Debug.v("Context item selected");
		int menuItemIndex = item.getItemId();
		switch(menuItemIndex){
			case Constants.CONTEXT_EDIT:
				startActivityForResult(Intents.group_payment_edit(this,this.group.get_id(), this.payments.get(this.selectedPayment).get_id()), Constants.ACTIVITY_GROUP_PAYMENT_EDIT);
				break;
			case Constants.CONTEXT_DELETE:
				startActivityForResult(Intents.popup_delete(this, Constants.PAYMENT), Constants.ACTIVITY_POPUP_DELETE);
				break;
			default:
				Debug.v("In default" + Integer.toString(menuItemIndex));
				break;
		}
		return true;
	}


}
