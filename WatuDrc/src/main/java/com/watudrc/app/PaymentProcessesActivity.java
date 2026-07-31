package com.watudrc.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.fragments.FragmentPaymentProcess;
import com.watudrc.app.fragments.OpenFragment;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;

public class PaymentProcessesActivity extends BaseActivity implements IWhereMyLocationConstants {
   
	Context mContext;
    Activity parentActivity;
    private static StoreAndGetFromPrefs storageObject;
    
    Toolbar  mToolbar;
    TextView titleBarText;

    RelativeLayout btnBack;

    String mStartFrom="";
	String method;
	String amount;
	String numberOfDays;
	String userName;
	String deviceName;
	String pagaNumber;
    
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plain);
		
		parentActivity= this;
		mContext = parentActivity.getApplicationContext();
		storageObject= new StoreAndGetFromPrefs(mContext);
		allowLock();

		Bundle bundle = getIntent().getExtras();

		if (bundle != null) {
			method = bundle.getString("method");
			amount = bundle.getString("amount");
			numberOfDays = bundle.getString("numberOfDays");
			userName = bundle.getString("user");
			deviceName = bundle.getString("device");
			pagaNumber = bundle.getString("pagaNumber");

			// Use the values as needed
			Log.d("PaymentProcessActivity", "pagaNumber: " + pagaNumber);
		}

//		 mToolbar = findViewById(R.id.toolbar);
//
//		 titleBarText= mToolbar.findViewById(R.id.titleBarText);
//		 titleBarText.setTypeface(Common.getFirmaBold());
////		 titleBarText.setTextSize(getResources().getDimension(R.dimen.textsize_medium));
////		 titleBarText.setText(mContext.getResources().getString(R.string.makepayment));
//		 titleBarText.setVisibility(View.VISIBLE);
//
//         btnBack= mToolbar.findViewById(R.id.btnBack);

		Intent mIntent = getIntent();
		if (mIntent != null)
		{

			mStartFrom = mIntent.getStringExtra(KEY_START_FROM);
			if(mStartFrom != null)
			{
				mStartFrom = mIntent.getStringExtra(KEY_START_FROM);

			}
		}






		 
		
		 changeFragment(new FragmentPaymentProcess());



		
	}

	  private void changeFragment(Fragment targetFragment){

		  Bundle data = new Bundle();
		  data.putString("method", method);
		  data.putString("amount", amount);
		  data.putString("numberOfDays", numberOfDays);
		  data.putString("user", userName);
		  data.putString("device", deviceName);
		  data.putString("pagaNumber", pagaNumber);

		  targetFragment.setArguments(data);

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
		  //Log Event
		  GeneralCommands.logEvent(this,"view_page","view_activity_page");


	  }

	@Override
	protected void onResume(){
		super.onResume();
		//Log Event
		GeneralCommands.logEvent(this,"view_page","view_activity_page");


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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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
