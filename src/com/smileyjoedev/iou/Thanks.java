package com.smileyjoedev.iou;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

public class Thanks extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gen.setTheme(this);
        setContentView(R.layout.thanks);
        
        this.initialize();
        this.populateView();
    }
    
    private void initialize(){
    	TextView tvRichArmstrong = (TextView) findViewById(R.id.tv_rich_armstrong);
    	tvRichArmstrong.setMovementMethod(LinkMovementMethod.getInstance());
    	
    	TextView tvActionBarSherlock = (TextView) findViewById(R.id.tv_actionbar_sherlock);
    	tvActionBarSherlock.setMovementMethod(LinkMovementMethod.getInstance());
    	
    	TextView tvHoloEverywhere = (TextView) findViewById(R.id.tv_holo_everywhere);
    	tvHoloEverywhere.setMovementMethod(LinkMovementMethod.getInstance());
    	
    	TextView tvBetaTesters = (TextView) findViewById(R.id.tv_beta_testers);
    	tvBetaTesters.setMovementMethod(LinkMovementMethod.getInstance());
    }
    
    private void populateView(){
    	
    }
	
}
