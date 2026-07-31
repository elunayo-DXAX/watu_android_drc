package com.watudrc.app;

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




import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.fragments.FragmentChangePassword;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;

public class ChangePasswordActivity extends BaseActivity implements IWhereMyLocationConstants {
   
	Context mContext;
    Activity parentActivity;
    private static StoreAndGetFromPrefs storageObject;
    String mStartFrom;
    public String phoneNumber;

	Toolbar  mToolbar;
	TextView titleBarText;

	RelativeLayout btnBack;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
		setContentView(R.layout.activity_back);
		
		parentActivity= this;
		mContext = parentActivity.getApplicationContext();
		storageObject= new StoreAndGetFromPrefs(mContext);

		allowLock();

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

			}
		}

		btnBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				goBack();
			}
		});

		changeFragment(new FragmentChangePassword());


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

		  GeneralCommands.logEvent(this,"view_change_pin_page","view_change_pin_page");


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
