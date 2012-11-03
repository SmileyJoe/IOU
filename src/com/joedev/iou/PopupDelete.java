package com.joedev.iou;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class PopupDelete extends SherlockActivity implements OnClickListener {
	
	private int sectionId;
	private String message;
	private String positiveText;
	private String negativeText;
	private TextView tvMessage;
	private Button btPositive;
	private Button btNegative;
//	private TextView tvDialogHeading;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Gen.setTheme(this);
		setContentView(R.layout.popup_delete);
		Bundle extras = getIntent().getExtras();
		this.sectionId = extras.getInt("section_id");
		
		this.initialize();
		
		this.populate_view();
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.popup_delete, menu);
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
		this.tvMessage = (TextView) findViewById(R.id.tv_popup_delete_message);
		this.btNegative = (Button) findViewById(R.id.bt_negative);
		this.btNegative.setOnClickListener(this);
		this.btPositive = (Button) findViewById(R.id.bt_positive);
		this.btPositive.setOnClickListener(this);
//		this.tvDialogHeading = (TextView) findViewById(R.id.tv_dialog_heading);
		
		switch(this.sectionId){
			case Constants.PAYMENT:
				this.message = this.getString(R.string.tv_popup_delete_payment);
				this.positiveText = this.getString(R.string.bt_popup_delete_positive);
				this.negativeText = this.getString(R.string.bt_popup_delete_negative);
//				this.tvDialogHeading.setText(R.string.dialog_heading_delete);
				break;
			case Constants.USER:
				this.message = this.getString(R.string.tv_popup_delete_user);
				this.positiveText = this.getString(R.string.bt_popup_delete_positive);
				this.negativeText = this.getString(R.string.bt_popup_delete_negative);
//				this.tvDialogHeading.setText(R.string.dialog_heading_delete);
				break;
			case Constants.EXIT_APP:
				this.message = this.getString(R.string.tv_popup_delete_exit);
				this.positiveText = this.getString(R.string.bt_popup_delete_positive);
				this.negativeText = this.getString(R.string.bt_popup_delete_negative);
//				this.tvDialogHeading.setText(R.string.dialog_heading_exit);
				break;
			case Constants.GROUP:
				this.message = this.getString(R.string.tv_popup_delete_group);
				this.positiveText = this.getString(R.string.bt_popup_delete_positive);
				this.negativeText = this.getString(R.string.bt_popup_delete_negative);
//				this.tvDialogHeading.setText(R.string.dialog_heading_delete);
				break;
		}
	}
	
	public void populate_view(){
		this.tvMessage.setText(this.message);
		this.btNegative.setText(this.negativeText);
		this.btPositive.setText(this.positiveText);
	}

	@Override
	public void onClick(View v) {
		Intent resultIntent = new Intent();
		switch(v.getId()){
			case R.id.bt_positive:
				resultIntent.putExtra("result", true);
				setResult(Activity.RESULT_OK, resultIntent);
				finish();
				break;
			case R.id.bt_negative:
				resultIntent.putExtra("result", false);
				setResult(Activity.RESULT_OK, resultIntent);
				finish();
				break;
		}
		
	}
	
}
