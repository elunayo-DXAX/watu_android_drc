package com.watutz.app.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.ConfigUpdate;
import com.google.firebase.remoteconfig.ConfigUpdateListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException;
import com.watutz.app.ChooseOTPActivity;
import com.watutz.app.GeneralCommands;
import com.watutz.app.MainActivity;
import com.watutz.app.OTPConfirmActivity;
import com.watutz.app.R;
import com.watutz.app.constant.IWhereMyLocationConstants;
import com.watutz.app.mobileservices.GetData;
import com.watutz.app.prefs.StoreAndGetFromPrefs;
import com.watutz.app.util.FirebaseConfig;
import com.watutz.app.view.MaterialRippleLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Arrays;


public class FragmentChooseOTP extends Fragment implements IWhereMyLocationConstants {

    private View parentView;

    private static StoreAndGetFromPrefs storageObject;
    public static ChooseOTPActivity parentActivity;
    static Context mContext;

    RelativeLayout loadingView;

    ImageView progressWheelKnox,progressWheelSMS;
    RelativeLayout btnRequestKnox,btnRequestSMS;
    MaterialRippleLayout requestRipsKnox,requestRipsSMS;
    RelativeLayout smsContent,knoxContent;

    //Knox OTp or SMS
    FirebaseConfig remoteConfig;
    FirebaseRemoteConfig mFirebaseConfig;
    boolean is_otp_knox_enabled;
    boolean is_otp_sms_enabled=true;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        parentView = inflater.inflate(R.layout.fragment_choose_otp, container, false);
        parentActivity = (ChooseOTPActivity) getActivity();
        mContext= parentActivity.getApplicationContext();
        storageObject = new StoreAndGetFromPrefs(mContext);
        loadingView= (RelativeLayout) parentView.findViewById(R.id.loadingItems);

        progressWheelKnox= (ImageView) parentView.findViewById(R.id.progressWheelKnox);
        progressWheelSMS= (ImageView) parentView.findViewById(R.id.progressWheelSMS);

        btnRequestKnox= (RelativeLayout) parentView.findViewById(R.id.btnRequestKnox);
        btnRequestSMS= (RelativeLayout) parentView.findViewById(R.id.btnRequestSMS);

        smsContent= (RelativeLayout) parentView.findViewById(R.id.smsContent);
        knoxContent= (RelativeLayout) parentView.findViewById(R.id.knoxContent);

        requestRipsKnox= (MaterialRippleLayout) parentView.findViewById(R.id.requestRipsKnox);
        requestRipsSMS= (MaterialRippleLayout) parentView.findViewById(R.id.requestRipsSMS);


        btnRequestKnox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestRipsKnox.setVisibility(View.GONE);
                smsContent.setVisibility(View.GONE);

                //Reset Knox Prioritization
                parentActivity.isComingFromActivatePage=false;
                refreshOTPViews();

                GeneralCommands.logEvent(parentActivity,"click_auth_otp_knox","click_auth_otp_knox");
//                GeneralCommands.logEvent(parentActivity,"click_request_knox_otp_send_confirmation","click_request_knox_otp_send_confirmation");


                try
                {

                    progressWheelKnox.setVisibility(View.VISIBLE);
                    Glide.with(parentActivity)
                            .asGif()
                            .load(R.drawable.loader_knox) // Replace with your local GIF resource
                            .into(progressWheelKnox);

                } catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                GetData ob1 = new GetData();
                ob1.initialize(parentActivity,loadingView);

                try {

                    ob1.userRegister(storageObject.retrieveUserPhoneNumberFromPrefs(),false);
                    ob1.setUserRegisterListener(new GetData.UserRegisterListener() {
                        @Override
                        public void onObjectReady(String title) {

                        }

                        @Override
                        public void onDataLoaded(String deviceId) {

                            if(!deviceId.equals(""))
                            {

                                ob1.userOTPRequestKnox(storageObject.retrieveUserPhoneNumberFromPrefs(),false,false);
                                ob1.setUserOTPRequestKnoxListener(new GetData.OTPRequestKnoxListener() {
                                    @Override
                                    public void onObjectReady(String title) {

                                    }

                                    @Override
                                    public void onDataLoaded(int waitingTime, String message) {

                                        progressWheelKnox.setVisibility(View.GONE);

                                        if(waitingTime>0)
                                        {
                                            //Success
                                            goToOTPConfirmPage(""+waitingTime);

                                        }
                                        else {

                                            String err= message;
                                            Toast.makeText(mContext,err,Toast.LENGTH_LONG).show();

                                            RelativeLayout resultError= loadingView.findViewById(R.id.resultError);
                                            resultError.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    resultError.setVisibility(View.GONE);
                                                    loadingView.setVisibility(View.GONE);
                                                }
                                            });

                                            ///Error
                                            requestRipsKnox.setVisibility(View.VISIBLE);
                                            progressWheelKnox.setVisibility(View.GONE);
                                            smsContent.setVisibility(View.VISIBLE);

                                        }

                                    }
                                });


                            }
                            else
                            {
                                String err= mContext.getResources().getString(R.string.registererror);
                                Toast.makeText(mContext,err,Toast.LENGTH_LONG).show();

//                                ob1.problemLoading(err,101);
                                ///Error
                                RelativeLayout resultError= loadingView.findViewById(R.id.resultError);
                                resultError.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        resultError.setVisibility(View.GONE);
                                        loadingView.setVisibility(View.GONE);
                                    }
                                });

                                requestRipsKnox.setVisibility(View.VISIBLE);
                                progressWheelKnox.setVisibility(View.GONE);
                                smsContent.setVisibility(View.VISIBLE);

                            }
                        }
                    });
                } catch (Exception e) {


                    String err= mContext.getResources().getString(R.string.registererror);
                    Toast.makeText(mContext,err,Toast.LENGTH_LONG).show();
//                    ob1.problemLoading(err,102);
                    ///Error

                    RelativeLayout resultError= loadingView.findViewById(R.id.resultError);
                    resultError.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            resultError.setVisibility(View.GONE);
                            loadingView.setVisibility(View.GONE);
                        }
                    });

                    requestRipsKnox.setVisibility(View.VISIBLE);
                    progressWheelKnox.setVisibility(View.GONE);
                    smsContent.setVisibility(View.VISIBLE);


                    e.printStackTrace();
                }








            }
        });


        btnRequestSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GeneralCommands.logEvent(parentActivity,"click_auth_otp_sms","click_auth_otp_sms");

                requestRipsSMS.setVisibility(View.GONE);
                knoxContent.setVisibility(View.GONE);

                //Reset Knox Prioritization
                parentActivity.isComingFromActivatePage=false;
                refreshOTPViews();

                try
                {

                    progressWheelSMS.setVisibility(View.VISIBLE);
                    Glide.with(parentActivity)
                            .asGif()
                            .load(R.drawable.loader_knox) // Replace with your local GIF resource
                            .into(progressWheelSMS);

                } catch (Exception e)
                {

                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


                GetData ob1 = new GetData();
                ob1.initialize(parentActivity,loadingView);
                try {

                    ob1.userRegister(storageObject.retrieveUserPhoneNumberFromPrefs(),false);
                    ob1.setUserRegisterListener(new GetData.UserRegisterListener() {
                        @Override
                        public void onObjectReady(String title) {

                        }

                        @Override
                        public void onDataLoaded(String deviceId) {

                            if(!deviceId.equals(""))
                            {

                                ob1.userOTPRequest(storageObject.retrieveUserPhoneNumberFromPrefs(),false);
                                ob1.setUserOTPRequestListener(new GetData.OTPRequestListener() {
                                    @Override
                                    public void onObjectReady(String title) {

                                    }

                                    @Override
                                    public void onDataLoaded(int waitingTime) {

                                        if(waitingTime<=0)
                                        {
                                            String err= mContext.getResources().getString(R.string.registererror);
                                            Toast.makeText(mContext,err,Toast.LENGTH_LONG).show();

                                            RelativeLayout resultError= loadingView.findViewById(R.id.resultError);
                                            resultError.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    resultError.setVisibility(View.GONE);
                                                    loadingView.setVisibility(View.GONE);
                                                }
                                            });

                                            ///Error
                                            requestRipsSMS.setVisibility(View.VISIBLE);
                                            progressWheelSMS.setVisibility(View.GONE);
                                            knoxContent.setVisibility(View.VISIBLE);
                                        }
                                    }
                                });

                            }
                            else
                            {
                                String err= mContext.getResources().getString(R.string.registererror);
                                Toast.makeText(mContext,err,Toast.LENGTH_LONG).show();

//                                ob1.problemLoading(err,101);
                                ///Error
                                RelativeLayout resultError= loadingView.findViewById(R.id.resultError);
                                resultError.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        resultError.setVisibility(View.GONE);
                                        loadingView.setVisibility(View.GONE);
                                    }
                                });

                                requestRipsSMS.setVisibility(View.VISIBLE);
                                progressWheelSMS.setVisibility(View.GONE);
                                knoxContent.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                } catch (Exception e) {


                    String err= mContext.getResources().getString(R.string.registererror);
                    Toast.makeText(mContext,err,Toast.LENGTH_LONG).show();
//                    ob1.problemLoading(err,102);
                    ///Error

                    RelativeLayout resultError= loadingView.findViewById(R.id.resultError);
                    resultError.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            resultError.setVisibility(View.GONE);
                            loadingView.setVisibility(View.GONE);
                        }
                    });

                    requestRipsSMS.setVisibility(View.VISIBLE);
                    progressWheelSMS.setVisibility(View.GONE);
                    knoxContent.setVisibility(View.VISIBLE);

                    e.printStackTrace();
                }



            }
        });

        refreshOTPViews();

        try
        {
            setConfigs();

        }catch(Exception e){}


        return parentView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that we are currently visible
        if (this.isVisible()) {
            // If we are becoming invisible, then...
            if (isVisibleToUser) {
                GeneralCommands.logEvent(parentActivity,"view_page","view_page");
                // TODO stop audio playback
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onStart() {
        super.onStart();
    }
    public void goToPage()
    {
        Intent mIntent = new Intent(parentActivity, MainActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
        GeneralCommands.goToPage(parentActivity, mIntent,false);

    }

    public void goToOTPConfirmPage(String waitingTime)
    {


        Intent mIntent = new Intent(parentActivity, OTPConfirmActivity.class);
        mIntent.putExtra(KEY_START_FROM, "otp_choose");
        mIntent.putExtra(WAITING_TIME, waitingTime);
        mIntent.putExtra(OTP_MODE, "knox");
        GeneralCommands.goToPage(parentActivity,mIntent,true);

    }


    public void setConfigs()
    {
        //Remote Configs
        remoteConfig= new FirebaseConfig();
        mFirebaseConfig=remoteConfig.getConfigs();
        mFirebaseConfig.fetchAndActivate().addOnCompleteListener(parentActivity, new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful())
                {
                    boolean updated = task.getResult();

                    //OTP KnoX
                    is_otp_knox_enabled = mFirebaseConfig.getBoolean("is_otp_knox_enabled");
                    storageObject.saveOTPKnoxEnabledToPrefs(is_otp_knox_enabled);


                    //OTP KnoX
                    is_otp_sms_enabled = mFirebaseConfig.getBoolean("is_otp_sms_enabled");
                    storageObject.saveOTPSMSEnabledToPrefs(is_otp_sms_enabled);

                    refreshOTPViews();

                }
                else {
                    Log.d("ZeFetchFailed","ZeFetchFailed");
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

                        //OTP KnoX
                        is_otp_knox_enabled = mFirebaseConfig.getBoolean("is_otp_knox_enabled");
                        storageObject.saveOTPKnoxEnabledToPrefs(is_otp_knox_enabled);


                        //OTP KnoX
                        is_otp_sms_enabled = mFirebaseConfig.getBoolean("is_otp_sms_enabled");
                        storageObject.saveOTPSMSEnabledToPrefs(is_otp_sms_enabled);

                        refreshOTPViews();

                    }
                });
            }

            @Override
            public void onError(FirebaseRemoteConfigException error) {
                Log.w("ZeConfigs", "Config update error with code: " + error.getCode(), error);
            }
        });
    }

    public void refreshOTPViews()
    {



        if(storageObject.isOTPKnoxEnabledFromPrefs())
        {
            knoxContent.setVisibility(View.VISIBLE);
        }
        else
        {
            knoxContent.setVisibility(View.GONE);
        }


        if(storageObject.isOTPSMSEnabledFromPrefs())
        {
            smsContent.setVisibility(View.VISIBLE);
        }
        else
        {
            smsContent.setVisibility(View.GONE);
        }


        //Set Preferred OTP Based on Channels
        String[] channels = storageObject.retrieveUserChannelsFromPrefs();
        boolean hasDevice = channels != null && Arrays.asList(channels).contains("device");

        if(hasDevice && parentActivity.isComingFromActivatePage)
        {
            //Only Priotize Knox if Coming from Activate Page and is a Simu User
            //Is a Simu User Prioritize Knox
            knoxContent.setVisibility(View.VISIBLE);
            smsContent.setVisibility(View.GONE);
//            parentActivity.isComingFromActivatePage=false;

        }

        else
        {
            //Likely another User - So Show Both Knox and SMS
            knoxContent.setVisibility(View.VISIBLE);
            smsContent.setVisibility(View.VISIBLE);
        }

//        Toast.makeText(parentActivity, "Ze"+(!storageObject.isOTPKnoxEnabledFromPrefs() && !storageObject.isOTPSMSEnabledFromPrefs()), Toast.LENGTH_SHORT).show();

        //Default Incase both are Off
        if(!storageObject.isOTPKnoxEnabledFromPrefs() && !storageObject.isOTPSMSEnabledFromPrefs())
        {
            knoxContent.setVisibility(View.VISIBLE);
            smsContent.setVisibility(View.VISIBLE);
        }

    }


}
