package com.watudrc.app;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;


import com.google.firebase.analytics.FirebaseAnalytics;
import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.fragments.FragmentPreview;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;


public class PreviewActivity extends BaseActivity implements IWhereMyLocationConstants {
   
	Context mContext;
    Activity parentActivity;
    private static StoreAndGetFromPrefs storageObject;
	FirebaseAnalytics mFirebaseAnalytics;

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plain);
		
		parentActivity= this;
		mContext = parentActivity.getApplicationContext();
		storageObject= new StoreAndGetFromPrefs(mContext);

		requestNotifications();
		allowLock();
		changeFragment(new FragmentPreview());
		
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
			 //Log Event
		    GeneralCommands.logEvent(this,"view_onboarding_page","view_onboarding_page");

	  }
	  
	  @Override
		protected void onDestroy() {
			
			super.onDestroy();
			
		}   
	  
	 //Location Stuff
	 public boolean requestLoc()
	 {
		 boolean isPermisionGranted =false;
		 final int ACTION_REQUEST_MULTIPLE_PERMISSION = 1;
		 if (Build.VERSION.SDK_INT >= 23)
		 {  // Only ask for these permissions on runtime when running Android 6.0 or higher
			 switch (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
				 case PackageManager.PERMISSION_DENIED:
					 ((TextView) new AlertDialog.Builder(this)
							 .setTitle(mContext.getResources().getString(R.string.permgivepermissions))
							 .setMessage(Html.fromHtml("<p>"+mContext.getResources().getString(R.string.permallow)+"</p>"))
							 .setNeutralButton("Okay", new DialogInterface.OnClickListener() {
								 @Override
								 public void onClick(DialogInterface dialog, int which) {
									 GeneralCommands.logEvent(parentActivity,"click_allow_notifications","click_allow_notifications");

									 int pCheck = ContextCompat.checkSelfPermission(parentActivity,Manifest.permission.ACCESS_FINE_LOCATION);
									 pCheck += ContextCompat.checkSelfPermission(parentActivity,Manifest.permission.ACCESS_COARSE_LOCATION);
									 if (pCheck != 0) {
										 ActivityCompat.requestPermissions(parentActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
										 }, ACTION_REQUEST_MULTIPLE_PERMISSION);
									 }
								 }
							 })
							 .show()
							 .findViewById(android.R.id.message))
							 .setMovementMethod(LinkMovementMethod.getInstance());
					 // Make the link clickable. Needs to be called after show(), in order to generate hyperlinks
					 isPermisionGranted=false;
					 break;
				 case PackageManager.PERMISSION_GRANTED:
					 isPermisionGranted=true;
					 break;
			 }


		 }else
		 {
		 	//Aready Granted on Installtion
			 isPermisionGranted=true;
		 }
		 return isPermisionGranted;
	 }

	 public void requestNotifications()
	 {
		 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
		 {
			 if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
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
