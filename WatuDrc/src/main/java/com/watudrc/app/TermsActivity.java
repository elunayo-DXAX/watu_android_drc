package com.watudrc.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.ConfigUpdate;
import com.google.firebase.remoteconfig.ConfigUpdateListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException;
import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.fragments.FragmentTerms;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;
import com.watudrc.app.util.FirebaseConfig;

public class TermsActivity extends BaseActivity implements IWhereMyLocationConstants {

    Context mContext;
    Activity parentActivity;
    private static StoreAndGetFromPrefs storageObject;

    Toolbar mToolbar;
    TextView titleBarText;

    RelativeLayout btnBack;

    String mStartFrom = "";

    FirebaseConfig remoteConfig;
    FirebaseRemoteConfig mFirebaseConfig;
    public String terms = "";
    public String terms_sw = "";

    public String terms_fr = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back);

        parentActivity = this;
        mContext = parentActivity.getApplicationContext();
        storageObject = new StoreAndGetFromPrefs(mContext);

        allowLock();

        mToolbar = findViewById(R.id.toolbar);

        titleBarText = mToolbar.findViewById(R.id.titleBarText);
        titleBarText.setTypeface(Common.getFirmaBold());

        titleBarText.setText(mContext.getResources().getString(R.string.back));

        btnBack = mToolbar.findViewById(R.id.btnBack);

        Intent mIntent = getIntent();
        if (mIntent != null) {

            mStartFrom = mIntent.getStringExtra(KEY_START_FROM);
            if (mStartFrom != null) {
                mStartFrom = mIntent.getStringExtra(KEY_START_FROM);

            }
        }

        btnBack.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            goBack();
        });


        //Remote Configs
        remoteConfig = new FirebaseConfig();
        mFirebaseConfig = remoteConfig.getConfigs();
        mFirebaseConfig.fetchAndActivate().addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    boolean updated = task.getResult();
//							Toast.makeText(mContext, "Fetch and activate succeeded",
//									Toast.LENGTH_SHORT).show();

                    terms = mFirebaseConfig.getString("terms_and_conditions");
                    terms_sw = mFirebaseConfig.getString("terms_and_conditions_sw");
                    terms_fr = mFirebaseConfig.getString("terms_and_conditions_fr");
                    Log.d("ZeTerms", "ZeTerms Eng" + terms);
                    Log.d("ZeTerms", "ZeTerms French" + terms_fr);

                    changeFragment(new FragmentTerms());


                } else {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.fetchfailed),
                            Toast.LENGTH_SHORT).show();
                    goBack();
                }

            }
        });

        mFirebaseConfig.addOnConfigUpdateListener(new ConfigUpdateListener() {
            @Override
            public void onUpdate(ConfigUpdate configUpdate) {
                //Log.d("ZeConfigs", "Updated keys: " + configUpdate.getUpdatedKeys());

                mFirebaseConfig.activate().addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        terms = mFirebaseConfig.getString("terms_and_conditions");
                        terms_sw = mFirebaseConfig.getString("terms_and_conditions_sw");
                        terms_fr = mFirebaseConfig.getString("terms_and_conditions_fr");

                        Log.d("ZeTerms", "ZeTerms" + terms_fr);
                        changeFragment(new FragmentTerms());
                    }
                });
            }

            @Override
            public void onError(FirebaseRemoteConfigException error) {
                Log.w("ZeConfigs", "Config update error with code: " + error.getCode(), error);
            }
        });


    }


    private void changeFragment(Fragment targetFragment) {

        try {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment, targetFragment, "fragment")
                    .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        } catch (Exception e) {

        }
    }

    public void hideKeyboard() {
        try {
            InputMethodManager inputManager = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);


        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {

        goBack();

    }


    public void goBack() {
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Log Event
        GeneralCommands.logEvent(this, "view_terms_page", "view_terms_page");


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

    public void goToPage() {
        Intent mIntent = new Intent(parentActivity, MainActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
        GeneralCommands.goToPage(parentActivity, mIntent, false);

    }
}
