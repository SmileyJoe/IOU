package com.joedev.iou;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class GroupPaymentNew extends SherlockActivity implements OnClickListener {
	
	private DbGroupAdapter groupAdapter;
	private DbGroupPaymentAdapter groupPaymentAdapter;
	private GroupPayment payment;
	private static Group group;
	private boolean isEdit;
	private LinearLayout llPayingWrapper;
	private LinearLayout llPaidForWrapper;
	private ArrayList<EditText> payingFields;
	private ArrayList<EditText> paidForFields;
	private Button btSave;
	private Button btCancel;
	private EditText etTitle;
	private EditText etDescription;
	private float payingAmount;
	private float paidAmount;
	
	private static float totalPaying;
	private static float totalPaidFor;
	private static float lastEnteredNumber;
	
	private static TextView tvPayingTotal;
	private static TextView tvPaidForTotal;
	
	private ToggleButton tbEqualPayment;
	private static boolean equalPayment;
	private LinearLayout llPaidForSectionWrapper;
	private TextView tvPaidForTotalTitle;
	
	private static TextView tvDifferenceAmount;
	private static TextView tvDifferenceAmountTitle;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Gen.setTheme(this);
		setContentView(R.layout.group_payment_new);
		this.initialize();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		try{
			Bundle extras = getIntent().getExtras();
			
			if(extras.containsKey("group_id")){
				int groupId = extras.getInt("group_id");
				this.group = this.groupAdapter.get_details(groupId);
			}
			
			if(extras.containsKey("payment_id")){
				this.isEdit = true;
				this.payment = this.groupPaymentAdapter.get_details(extras.getInt("payment_id"));
				// this.group =
				// this.groupAdapter.get_details(this.payment.get_group_id());
			}
		}catch(NullPointerException e){
		}
		
		this.populate_view();
	}
	
   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.group_payment_new, menu);
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
	
	private void initialize() {
		this.groupAdapter = new DbGroupAdapter(this);
		this.groupPaymentAdapter = new DbGroupPaymentAdapter(this);
		this.payment = new GroupPayment();
		this.group = new Group();
		this.isEdit = false;
		this.llPayingWrapper = (LinearLayout) findViewById(R.id.ll_paying_wrapper);
		this.llPaidForWrapper = (LinearLayout) findViewById(R.id.ll_paid_for_wrapper);
		this.payingFields = new ArrayList<EditText>();
		this.paidForFields = new ArrayList<EditText>();
		this.btSave = (Button) findViewById(R.id.bt_save);
		this.btSave.setOnClickListener(this);
		this.btCancel = (Button) findViewById(R.id.bt_cancel);
		this.btCancel.setOnClickListener(this);
		this.etTitle = (EditText) findViewById(R.id.et_payment_title);
		this.etDescription = (EditText) findViewById(R.id.et_payment_description);
		this.payingAmount = 0;
		this.paidAmount = 0;
		this.totalPaying = 0;
		this.totalPaidFor = 0;
		this.lastEnteredNumber = 0;
		this.tvPayingTotal = (TextView) findViewById(R.id.tv_paying_total);
		this.tvPaidForTotal = (TextView) findViewById(R.id.tv_paid_for_total);
		this.tbEqualPayment = (ToggleButton) findViewById(R.id.tb_equal_payment);
		this.tbEqualPayment.setOnClickListener(this);
		this.equalPayment = false;
		this.llPaidForSectionWrapper = (LinearLayout) findViewById(R.id.ll_paid_for_section_wrapper);
		this.tvPaidForTotalTitle = (TextView) findViewById(R.id.tv_paid_for_total_title);
		this.tvDifferenceAmount = (TextView) findViewById(R.id.tv_difference_amount);
		this.tvDifferenceAmountTitle = (TextView) findViewById(R.id.tv_difference_amount_title);
	}
	
	private void populate_view() {
		for(int i = 0; i < this.group.get_users().size(); i++){
			this.populate_user_row(this.group.get_user(i), true);
			this.populate_user_row(this.group.get_user(i), false);
		}
		
		if(this.isEdit){
			this.etTitle.setText(this.payment.get_title());
			this.etDescription.setText(this.payment.get_description());
		}
		
		this.tvPayingTotal.setText(Gen.get_amount_text(this.totalPaying));
		this.tvPaidForTotal.setText(Gen.get_amount_text(this.totalPaidFor));
		
	}
	
	public EditText populate_user_row(User user, boolean paying) {
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.xml.group_payment_user, null);
		
		TextView name = (TextView) view.findViewById(R.id.tv_user_name);
		EditText amount = (EditText) view.findViewById(R.id.et_amount);
		ImageView userImage = (ImageView) view.findViewById(R.id.iv_user_image);
		
		name.setText(user.get_name());
		
		amount.setTag(user.get_id());
		
		amount.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			public void onFocusChange(View v, boolean hasFocus) {
				EditText input = (EditText)v;
				String number = input.getText().toString().trim();
				if(!hasFocus){
					GroupPaymentNew.lastEnteredNumber = 0;
					if(!number.equals("")){
						//input.setText(Gen.get_amount_text(Float.parseFloat(number)));
					}
				} else {
					if(!number.equals("")){
						GroupPaymentNew.lastEnteredNumber = Float.parseFloat(number);
					}
					
				}
			}
		});
		
		Gen.set_user_image(this, userImage, user);
		
		if(paying){
			if(this.isEdit){
				for(int i = 0; i < this.payment.get_splits().size(); i++){
					if(this.payment.get_split(i).get_user_id() == user.get_id() && this.payment.get_split(i).is_paying()){
						amount.setText(this.payment.get_split(i).get_amount_text(false));
						GroupPaymentNew.totalPaying += this.payment.get_split(i).get_amount();
					}
				}
			}
			
			amount.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					GroupPaymentNew.totalPaying -= GroupPaymentNew.lastEnteredNumber;
					GroupPaymentNew.lastEnteredNumber = 0;
				}
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if(s != null && !s.toString().equals("")){
						GroupPaymentNew.lastEnteredNumber = Float.parseFloat(s.toString());
						GroupPaymentNew.totalPaying += GroupPaymentNew.lastEnteredNumber;
						if(GroupPaymentNew.equalPayment){
							GroupPaymentNew.tvPaidForTotal.setText(Gen.get_amount_text(GroupPaymentNew.totalPaying / GroupPaymentNew.group.get_users().size()));
						}
						GroupPaymentNew.tvPayingTotal.setText(Gen.get_amount_text(GroupPaymentNew.totalPaying));
					} else {
						if(GroupPaymentNew.equalPayment){
							GroupPaymentNew.tvPaidForTotal.setText(Gen.get_amount_text(GroupPaymentNew.totalPaying / GroupPaymentNew.group.get_users().size()));
						}
						GroupPaymentNew.tvPayingTotal.setText(Gen.get_amount_text(GroupPaymentNew.totalPaying));
					}
					
					GroupPaymentNew.set_difference_total(getApplicationContext());
				}
				
				@Override
				public void afterTextChanged(Editable arg0) {
				}
			});
			
			this.llPayingWrapper.addView(view);
			this.payingFields.add(amount);
		}else{
			if(this.isEdit){
				for(int i = 0; i < this.payment.get_splits().size(); i++){
					if(this.payment.get_split(i).get_user_id() == user.get_id() && this.payment.get_split(i).is_paid_for()){
						amount.setText(this.payment.get_split(i).get_amount_text(false));
						GroupPaymentNew.totalPaidFor += this.payment.get_split(i).get_amount();
					}
				}
			}
			
			amount.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					GroupPaymentNew.totalPaidFor -= GroupPaymentNew.lastEnteredNumber;
					GroupPaymentNew.lastEnteredNumber = 0;
				}
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if(s != null && !s.toString().equals("")){
						GroupPaymentNew.lastEnteredNumber = Float.parseFloat(s.toString());
						GroupPaymentNew.totalPaidFor += GroupPaymentNew.lastEnteredNumber;
						GroupPaymentNew.tvPaidForTotal.setText(Gen.get_amount_text(GroupPaymentNew.totalPaidFor));
					} else {
						GroupPaymentNew.tvPaidForTotal.setText(Gen.get_amount_text(GroupPaymentNew.totalPaidFor));
					}
					
					GroupPaymentNew.set_difference_total(getApplicationContext());
				}
				
				@Override
				public void afterTextChanged(Editable arg0) {
				}
			});
			
			this.llPaidForWrapper.addView(view);
			this.paidForFields.add(amount);
		}
		
		
		return amount;
	}
	
	private static void set_difference_total(Context context){
		if(!GroupPaymentNew.equalPayment){
			float diff = GroupPaymentNew.totalPaidFor - GroupPaymentNew.totalPaying; 
			
			if(diff > 0){
				GroupPaymentNew.tvDifferenceAmount.setText(Gen.get_amount_text(Math.abs(diff)));
				GroupPaymentNew.tvDifferenceAmountTitle.setText(context.getString(R.string.tv_need_more_paid_title));
			} else {
				if(diff < 0){
					GroupPaymentNew.tvDifferenceAmount.setText(Gen.get_amount_text(Math.abs(diff)));
					GroupPaymentNew.tvDifferenceAmountTitle.setText(context.getString(R.string.tv_need_more_paid_for_title));
				} else {
					GroupPaymentNew.tvDifferenceAmount.setText("");
					GroupPaymentNew.tvDifferenceAmountTitle.setText("");
				}
			}
		}
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
			case R.id.bt_save:
				this.payment.set_splits(new ArrayList<PaymentSplit>());
				this.set_split(this.payingFields, 0);
				this.set_split(this.paidForFields, 1);
				
				for(int i = 0; i < this.payment.get_splits().size(); i++){
					Debug.v("ID", this.payment.get_split(i).get_user_id());
					Debug.v("Amount", this.payment.get_split(i).get_amount());
				}
				
				this.payment.set_title(this.etTitle.getText().toString().trim());
				this.payment.set_description(this.etDescription.getText().toString().trim());
				this.payment.set_group_id(this.group.get_id());
				
				if(this.paidAmount == this.payingAmount || this.equalPayment){
					if(this.isEdit){
						this.groupPaymentAdapter.update(this.payment);
					}else{
						this.groupPaymentAdapter.save(this.payment);
					}
					
					finish();
				}else{
					Notify.toast(this, "Paying for and paid amounts are not equal");
					this.payment.set_amount(0);
					this.paidAmount = 0;
					this.payingAmount = 0;
				}
				
				Debug.v("total", this.payment.get_amount());
				break;
			case R.id.bt_cancel:
				finish();
				break;
			case R.id.tb_equal_payment:
				if(this.equalPayment){
//					this.tbEqualPayment.setChecked(false);
					this.equalPayment = false;
					this.llPaidForSectionWrapper.setVisibility(View.VISIBLE);
					this.tvPaidForTotalTitle.setText(this.getText(R.string.tv_total_title));
//					this.totalPaidFor = 0;
					this.tvPaidForTotal.setText(Gen.get_amount_text(this.totalPaidFor));
					this.set_difference_total(this);
				} else {
//					this.tbEqualPayment.setChecked(true);
					this.equalPayment = true;
					this.llPaidForSectionWrapper.setVisibility(View.GONE);
					this.tvPaidForTotalTitle.setText(this.getText(R.string.tv_total_title_per_user));
					this.tvPaidForTotal.setText(Gen.get_amount_text(this.totalPaying / this.group.get_users().size()));
					this.tvDifferenceAmount.setText("");
					this.tvDifferenceAmountTitle.setText("");
				}
				break;
		}
		
	}
	
	private void set_split(ArrayList<EditText> array, int type) {
		EditText etAmount;
		PaymentSplit split;
		int userId;
		float amount;
		
		for(int i = 0; i < array.size(); i++){
			split = new PaymentSplit();
			etAmount = array.get(i);
			userId = Integer.parseInt(etAmount.getTag().toString().trim());
			
			
			if(type == 1){
				if(this.equalPayment){
					amount = Float.parseFloat(Gen.get_formatted_amount((this.totalPaying/this.group.get_users().size())));
				} else {
					if(etAmount.getText().toString().trim().equals("")){
						amount = 0;
					}else{
						amount = Float.parseFloat(etAmount.getText().toString().trim());
					}
				}
			} else {
				if(etAmount.getText().toString().trim().equals("")){
					amount = 0;
				}else{
					amount = Float.parseFloat(etAmount.getText().toString().trim());
				}
			}
			
			
			if(amount > 0){
				split.set_amount(amount);
				split.set_user_Id(userId);
				split.set_type(type);
				this.payment.add_split(split);
				
				switch(type){
					case 0:
						this.payingAmount += amount;
						break;
					case 1:
						this.paidAmount += amount;
						break;
				}
				
			}
		}
	}
	
}
