package com.watutz.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.watutz.app.constant.IWhereMyLocationConstants;
import com.watutz.app.fragments.FragmentTransactions;
import com.watutz.app.prefs.StoreAndGetFromPrefs;

public class TransactionsActivity extends BaseActivity implements IWhereMyLocationConstants {
   
	Context mContext;
    Activity parentActivity;
    private static StoreAndGetFromPrefs storageObject;
    


    String mStartFrom="";
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plain);
		
		parentActivity= this;
		mContext = parentActivity.getApplicationContext();
		storageObject= new StoreAndGetFromPrefs(mContext);

		allowLock();


		Intent mIntent = getIntent();
		if (mIntent != null)
		{

			mStartFrom = mIntent.getStringExtra(KEY_START_FROM);
			if(mStartFrom != null)
			{
				mStartFrom = mIntent.getStringExtra(KEY_START_FROM);

			}
		}


		 changeFragment(new FragmentTransactions());



		
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
		public void onBackPressed() {
			
			goBack();
			
		}
	  
	  
	  
	  public void goBack()
	  {
		  finish();
	  }
	  
	  @Override
	     protected void onStart(){
	    	 super.onStart();

		  GeneralCommands.logEvent(this,"view_transactions_page","view_transactions_page");

	  }
	  
	  @Override
		protected void onDestroy() {
			
			super.onDestroy();
			
		}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		for (Fragment fragment : getSupportFragmentManager().getFragments()) {
			fragment.onActivityResult(requestCode, resultCode, data);
		}
	}

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void goToPage()
    {
        Intent mIntent = new Intent(parentActivity, MainActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
        GeneralCommands.goToPage(parentActivity, mIntent,false);

    }


	
}
