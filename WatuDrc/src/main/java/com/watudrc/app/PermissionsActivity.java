package com.watudrc.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.fragments.FragmentPermissions;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;


public class PermissionsActivity extends BaseActivity implements IWhereMyLocationConstants {

    Context mContext;
    Activity parentActivity;
    private static StoreAndGetFromPrefs storageObject;

    Toolbar  mToolbar;
    TextView titleBarText;

    RelativeLayout btnBack;

    String mStartFrom="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back);

        parentActivity= this;
        mContext = parentActivity.getApplicationContext();
        storageObject= new StoreAndGetFromPrefs(mContext);
        allowLock();

        mToolbar = findViewById(R.id.toolbar);

        titleBarText= mToolbar.findViewById(R.id.titleBarText);
        titleBarText.setTypeface(Common.getFirmaBold());

        titleBarText.setText(mContext.getResources().getString(R.string.back));

        btnBack= mToolbar.findViewById(R.id.btnBack);

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






        changeFragment(new FragmentPermissions());




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
