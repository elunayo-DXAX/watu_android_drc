package com.watutz.app;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;



import com.watutz.app.constant.IWhereMyLocationConstants;
import com.watutz.app.fragments.FragmentLogin;
import com.watutz.app.mobileservices.GetData;
import com.watutz.app.prefs.StoreAndGetFromPrefs;


public class LoginActivity extends BaseActivity implements IWhereMyLocationConstants {
   
	Context mContext;
    Activity parentActivity;
    private static StoreAndGetFromPrefs storageObject;

	Toolbar mToolbar;
	TextView titleBarText;

//	RelativeLayout btnBack;
    
    String mStartFrom="";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
		setContentView(R.layout.activity_plain);
		allowLock();
		
		parentActivity= this;
		mContext = parentActivity.getApplicationContext();
		storageObject= new StoreAndGetFromPrefs(mContext);
		//printFacebookHashKey();

		mToolbar = (Toolbar) findViewById(R.id.toolbar);

//		titleBarText= (TextView) mToolbar.findViewById(R.id.titleBarText);
//		titleBarText.setTypeface(Common.getFirmaBold());
//
//		titleBarText.setText(mContext.getResources().getString(R.string.back));

//		btnBack= (RelativeLayout) mToolbar.findViewById(R.id.btnBack);

		requestNotifications();


		Intent mIntent = getIntent();
		if (mIntent != null)
		{

			mStartFrom = mIntent.getStringExtra(KEY_START_FROM);
			if(mStartFrom != null)
			{
				mStartFrom = mIntent.getStringExtra(KEY_START_FROM);

			}
		}

//		btnBack.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				goBack();
//			}
//		});
		
		changeFragment(new FragmentLogin());




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

		  GeneralCommands.logEvent(this,"view_login_page","view_login_page");


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
	  
	  public void printFacebookHashKey() {
		   try {
		      PackageInfo info = getPackageManager().getPackageInfo("com.watutz.app",
		      PackageManager.GET_SIGNATURES);
		      for (Signature signature : info.signatures) {
		         MessageDigest md = MessageDigest.getInstance("SHA");
		         md.update(signature.toByteArray());
		         Log.d("FACEBOOK HASH KEY:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
		      }
		   } catch (PackageManager.NameNotFoundException e) {
		 } catch (NoSuchAlgorithmException e) {
		}
}


	public void requestNotifications()
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
		{
			if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
					!= PackageManager.PERMISSION_GRANTED)
			{
				// Request the permission
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
			} else
			{
				// Permission already granted, proceed with notification
			}
		} else
		{
			// For lower versions, no need to request permission
		}
	}

	  
	
}
