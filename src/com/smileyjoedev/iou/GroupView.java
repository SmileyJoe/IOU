package com.smileyjoedev.iou;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.smileyjoedev.genLibrary.Notify;
import com.smileyjoedev.iou.R;

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
	private GroupPaymentListAdapter groupPaymentListAdapter;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gen.setTheme(this);
        setContentView(R.layout.group_view);
        this.initialize();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extras = getIntent().getExtras();
		
		int groupId = extras.getInt("group_id");
		this.group = this.groupAdapter.getDetails(groupId);
		
		if(this.group.getId() == 0){
			Notify.toast(this, R.string.toast_group_does_not_exist);
			finish();
		}
		
        this.populateView();
        
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
				startActivityForResult(Intents.groupPaymentNew(this, group.getId()), Constants.ACTIVITY_GROUP_PAYMENT_NEW);
				return true;
			case R.id.menu_repayment:
				startActivityForResult(Intents.groupRepayment(this, this.group.getId()), Constants.ACTIVITY_GROUP_REPAYMENT);
				return true;
			case R.id.menu_user_details:
				startActivityForResult(Intents.groupUserPaymentDetails(this, this.group.getId()), Constants.ACTIVITY_GROUP_USER_PAYMENT_DETAILS);
				return true;
			case android.R.id.home:
				Intent intent = new Intent(this, GroupList.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
	            startActivity(intent);
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
    
    private void populateView(){
    	this.payments = this.groupPaymentAdapter.getByGroup(this.group.getId());
    	this.groupPaymentListAdapter = this.views.groupPaymentList(this.payments, (LinearLayout) findViewById(R.id.ll_group_payment_list_wrapper));
    }
    
    private void updatePaymentList(){
    	this.payments = this.groupPaymentAdapter.getByGroup(this.group.getId());
    	this.groupPaymentListAdapter.setPayments(this.payments);
    	this.groupPaymentListAdapter.notifyDataSetChanged();
		this.lvPaymentList.refreshDrawableState();
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
				this.updatePaymentList();
				break;
			case Constants.ACTIVITY_GROUP_REPAYMENT:
				this.updatePaymentList();
				break;
			case Constants.ACTIVITY_POPUP_DELETE:
				if(resultCode == Activity.RESULT_OK){
					if(data.getBooleanExtra("result", false)){
						this.groupPaymentAdapter.delete(this.payments.get(this.selectedPayment));
						this.updatePaymentList();
					}
				}
				break;
			case Constants.ACTIVITY_GROUP_PAYMENT_EDIT:
				this.updatePaymentList();
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
		int menuItemIndex = item.getItemId();
		switch(menuItemIndex){
			case Constants.CONTEXT_EDIT:
				startActivityForResult(Intents.groupPaymentEdit(this,this.group.getId(), this.payments.get(this.selectedPayment).getId()), Constants.ACTIVITY_GROUP_PAYMENT_EDIT);
				break;
			case Constants.CONTEXT_DELETE:
				startActivityForResult(Intents.popupDelete(this, Constants.PAYMENT), Constants.ACTIVITY_POPUP_DELETE);
				break;
			default:
				break;
		}
		return true;
	}


}
