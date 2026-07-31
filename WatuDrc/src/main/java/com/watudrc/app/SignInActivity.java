package com.watudrc.app;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;


import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.fragments.FragmentSignIn;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;


public class SignInActivity extends BaseActivity implements IWhereMyLocationConstants {
   
	Context mContext;
    Activity parentActivity;
    private static StoreAndGetFromPrefs storageObject;
   

    
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
		setContentView(R.layout.activity_login);
		
		parentActivity= this;
		mContext = parentActivity.getApplicationContext();
		storageObject= new StoreAndGetFromPrefs(mContext);

		allowLock();
		
		changeFragment(new FragmentSignIn());

    
		
		
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

		  GeneralCommands.logEvent(this,"view_signin_page","view_signin_page");

	  }
	  
	  @Override
		protected void onDestroy() {
			
			super.onDestroy();
			
		}   
	  
	  public void getFacebookKeyHash()
      {
    	  PackageInfo info;
    	  try {
    		    info = getPackageManager().getPackageInfo("com.watudrc.app", PackageManager.GET_SIGNATURES);
    		    for (Signature signature : info.signatures) {
    		        MessageDigest md;
    		        md = MessageDigest.getInstance("SHA");
    		        md.update(signature.toByteArray());
    		        String something = new String(Base64.encode(md.digest(), 0));
    		        //String something = new String(Base64.encodeBytes(md.digest()));
    		        Log.e("Facebook Hash Key", something);
    		    }
    		} catch (NameNotFoundException e1) {
    		    Log.e("name not found", e1.toString());
    		} catch (NoSuchAlgorithmException e) {
    		    Log.e("no such an algorithm", e.toString());
    		} catch (Exception e) {
    		    Log.e("exception", e.toString());
    		}
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
