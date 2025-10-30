package com.watutz.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;



import com.watutz.app.constant.IWhereMyLocationConstants;
import com.watutz.app.prefs.StoreAndGetFromPrefs;
import com.ypyproductions.utils.StringUtils;

public class OTPConfirmActivity extends BaseActivity implements IWhereMyLocationConstants {
   
	Context mContext;
    Activity parentActivity;
    private static StoreAndGetFromPrefs storageObject;

	Toolbar mToolbar;
	TextView titleBarText;

	RelativeLayout btnBack;

	String mStartFrom="";
	public String waitingTime="60";
	public String otpMode="sms";
   

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
		setContentView(R.layout.activity_back);
		allowLock();
		
		parentActivity= this;
		mContext = parentActivity.getApplicationContext();
		storageObject= new StoreAndGetFromPrefs(mContext);

		mToolbar = (Toolbar) findViewById(R.id.toolbar);

		titleBarText= (TextView) mToolbar.findViewById(R.id.titleBarText);
		titleBarText.setTypeface(Common.getFirmaBold());

		titleBarText.setText(mContext.getResources().getString(R.string.back));

		btnBack= (RelativeLayout) mToolbar.findViewById(R.id.btnBack);


		Intent mIntent = getIntent();
		if (mIntent != null)
		{

			mStartFrom = mIntent.getStringExtra(KEY_START_FROM);
			if(mStartFrom != null)
			{
				mStartFrom = mIntent.getStringExtra(KEY_START_FROM);
				waitingTime = mIntent.getStringExtra(WAITING_TIME);
				otpMode = mIntent.getStringExtra(OTP_MODE);

				if(StringUtils.isStringEmpty(waitingTime))waitingTime="60";
				if(StringUtils.isStringEmpty(otpMode))otpMode="sms";


			}
		}

		btnBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goBack();
			}
		});
		
		changeFragment(new com.watumx.app.fragments.FragmentOTPConfirm());


	}
	
	
	
	
	
	  private void changeFragment(Fragment targetFragment){
	        getSupportFragmentManager()
	                .beginTransaction()
	                .replace(R.id.main_fragment, targetFragment, "fragment")
	                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
	                .commit();
	    }
	  
	  public void hideKeyboard()
		{
		  try 
			{
					InputMethodManager inputManager = (InputMethodManager)
			                getSystemService(Context.INPUT_METHOD_SERVICE); 
	
			        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
			                   InputMethodManager.HIDE_NOT_ALWAYS);
					
				
			} catch (Exception e) 
			{
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	  
	  @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				
				goBack();
				
				return true;
			}
			return super.onKeyDown(keyCode, event);
		}
	  
	  
	  public void goBack()
	  {
		  finish();
	  }
	  
	  @Override
	     protected void onStart(){
	    	 super.onStart();

		  GeneralCommands.logEvent(this,"view_otp_confirm_page","view_otp_confirm_page");

	  }
	  
	  @Override
		protected void onDestroy() {
			
			super.onDestroy();
			
		}   
	  
	  @Override
      protected void onActivityResult(int requestCode, int resultCode, Intent data) {
          super.onActivityResult(requestCode, resultCode, data);
          for (Fragment fragment : getSupportFragmentManager().getFragments()) {
              //System.out.println("@#@");
              fragment.onActivityResult(requestCode, resultCode, data);
          }
      }
	  
	
}
