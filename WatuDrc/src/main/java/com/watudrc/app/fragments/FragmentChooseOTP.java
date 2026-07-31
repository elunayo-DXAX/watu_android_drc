package com.watudrc.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.ConfigUpdate;
import com.google.firebase.remoteconfig.ConfigUpdateListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException;
import com.watudrc.app.ChooseOTPActivity;
import com.watudrc.app.GeneralCommands;
import com.watudrc.app.MainActivity;
import com.watudrc.app.OTPConfirmActivity;
import com.watudrc.app.R;
import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.font.FirmaTextView;
import com.watudrc.app.mobileservices.GetData;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;
import com.watudrc.app.util.FirebaseConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class FragmentChooseOTP extends Fragment implements IWhereMyLocationConstants {

    private View parentView;

    private static StoreAndGetFromPrefs storageObject;
    public static ChooseOTPActivity parentActivity;
    static Context mContext;

    RelativeLayout loadingView;

    RelativeLayout btnContinue;
    RelativeLayout smsContent,knoxContent, whatsAppContent;

    RelativeLayout knoxContainer,smsContainer,whatsAppContainer;

    ImageView iconWhatsApp,iconKnox,iconSMS;
    FirmaTextView whatsAppDesc,knoxDesc,smsDesc, knoxExtra, smsExtra;
    ImageView chkWhatsApp,chkKnox,chkSMS;


    //Knox OTp or SMS
    FirebaseConfig remoteConfig;
    FirebaseRemoteConfig mFirebaseConfig;
    boolean is_otp_knox_enabled;
    boolean is_otp_sms_enabled=true;

    boolean is_otp_whatsapp_enabled=true;
    String otpOtionSelected = "whatsapp";
    FirmaTextView whatsAppExtra;

    String otpChannel;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        parentView = inflater.inflate(R.layout.fragment_choose_otp, container, false);
        parentActivity = (ChooseOTPActivity) getActivity();
        mContext= parentActivity.getApplicationContext();
        storageObject = new StoreAndGetFromPrefs(mContext);
        loadingView= parentView.findViewById(R.id.loadingItems);

        //otpOtionSelected="whatsapp";

        //  String startFrom = parentActivity.getIntent().getStringExtra(KEY_START_FROM);

        iconWhatsApp= parentView.findViewById(R.id.iconWhatsApp);
        iconKnox= parentView.findViewById(R.id.iconKnox);
        iconSMS= parentView.findViewById(R.id.iconSMS);

        chkKnox= parentView.findViewById(R.id.chkKnox);
        chkSMS= parentView.findViewById(R.id.chkSMS);
        chkWhatsApp= parentView.findViewById(R.id.chkWhatsApp);

        whatsAppDesc= parentView.findViewById(R.id.whatsAppDesc);
        knoxDesc= parentView.findViewById(R.id.knoxDesc);
        smsDesc= parentView.findViewById(R.id.smsDesc);

        knoxExtra=parentView.findViewById(R.id.knoxExtra);
        smsExtra=parentView.findViewById(R.id.smsExtra);

        btnContinue= parentView.findViewById(R.id.btnContinue);

        smsContent= parentView.findViewById(R.id.smsContent);
        knoxContent= parentView.findViewById(R.id.knoxContent);
        whatsAppContent= parentView.findViewById(R.id.whatsAppContent);

        whatsAppContainer= parentView.findViewById(R.id.whatsAppContainer);
        smsContainer= parentView.findViewById(R.id.smsContainer);
        knoxContainer= parentView.findViewById(R.id.knoxContainer);

        whatsAppExtra= parentView.findViewById(R.id.whatsAppExtra);

        String contact=storageObject.retrieveUserPhoneNumberFromPrefs();
        String maskedContact=maskContactNumber(contact);

        whatsAppExtra.setText(Html.fromHtml(mContext.getResources().getString(R.string.whatsappinstructions,maskedContact)));
        knoxExtra.setText(Html.fromHtml(mContext.getResources().getString(R.string.knoxinstructions)));
        smsExtra.setText(Html.fromHtml(mContext.getResources().getString(R.string.smsinstructions)));




        whatsAppContent.setOnClickListener(v -> {

            whatsAppContainer.setBackgroundResource(R.xml.rounded_list_transparent_theme_stroke);
            iconWhatsApp.setColorFilter(ContextCompat.getColor(parentActivity, R.color.theme_color), PorterDuff.Mode.SRC_IN);
            whatsAppDesc.setTextColor(ContextCompat.getColor(parentActivity, R.color.theme_color));
            chkWhatsApp.setImageResource(R.drawable.ics_icon_opt_selected);

            whatsAppExtra.setVisibility(View.VISIBLE);
            knoxExtra.setVisibility(View.GONE);
            smsExtra.setVisibility(View.GONE);


            knoxContainer.setBackgroundResource(R.xml.rounded_list_transparent_white_stroke);
            iconKnox.setColorFilter(ContextCompat.getColor(parentActivity, R.color.white), PorterDuff.Mode.SRC_IN);
            knoxDesc.setTextColor(ContextCompat.getColor(parentActivity, R.color.white));
            chkKnox.setImageResource(R.drawable.ics_icon_opt_empty);
            chkKnox.clearColorFilter();



            smsContainer.setBackgroundResource(R.xml.rounded_list_transparent_white_stroke);
            iconSMS.setColorFilter(ContextCompat.getColor(parentActivity, R.color.white), PorterDuff.Mode.SRC_IN);
            smsDesc.setTextColor(ContextCompat.getColor(parentActivity, R.color.white));
            chkSMS.setImageResource(R.drawable.ics_icon_opt_empty);
            chkSMS.clearColorFilter();

            otpOtionSelected="whatsapp";



        });


        knoxContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                knoxContainer.setBackgroundResource(R.xml.rounded_list_orange_stroke);
                iconKnox.setColorFilter(ContextCompat.getColor(parentActivity, R.color.theme_orange_light), PorterDuff.Mode.SRC_IN);
                knoxDesc.setTextColor(ContextCompat.getColor(parentActivity, R.color.theme_orange_light));
                chkKnox.setImageResource(R.drawable.ics_icon_opt_selected);
                chkKnox.setColorFilter(ContextCompat.getColor(mContext, R.color.theme_orange_light));

                knoxExtra.setVisibility(View.VISIBLE);
                whatsAppExtra.setVisibility(View.GONE);
                smsExtra.setVisibility(View.GONE);

                whatsAppContainer.setBackgroundResource(R.xml.rounded_list_transparent_white_stroke);
                iconWhatsApp.setColorFilter(ContextCompat.getColor(parentActivity, R.color.white), PorterDuff.Mode.SRC_IN);
                whatsAppDesc.setTextColor(ContextCompat.getColor(parentActivity, R.color.white));
                chkWhatsApp.setImageResource(R.drawable.ics_icon_opt_empty);
                chkWhatsApp.clearColorFilter();


                smsContainer.setBackgroundResource(R.xml.rounded_list_transparent_white_stroke);
                iconSMS.setColorFilter(ContextCompat.getColor(parentActivity, R.color.white), PorterDuff.Mode.SRC_IN);
                smsDesc.setTextColor(ContextCompat.getColor(parentActivity, R.color.white));
                chkSMS.setImageResource(R.drawable.ics_icon_opt_empty);
                chkSMS.clearColorFilter();


                otpOtionSelected="device";



            }
        });

        smsContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                smsContainer.setBackgroundResource(R.xml.rounded_list_green_stroke);
                iconSMS.setColorFilter(ContextCompat.getColor(parentActivity, R.color.theme_green), PorterDuff.Mode.SRC_IN);
                smsDesc.setTextColor(ContextCompat.getColor(parentActivity, R.color.theme_green));
                chkSMS.setImageResource(R.drawable.ics_icon_opt_selected);
                chkSMS.setColorFilter(ContextCompat.getColor(mContext, R.color.theme_green));

                smsExtra.setVisibility(View.VISIBLE);
                whatsAppExtra.setVisibility(View.GONE);
                knoxExtra.setVisibility(View.GONE);

                whatsAppContainer.setBackgroundResource(R.xml.rounded_list_transparent_white_stroke);
                iconWhatsApp.setColorFilter(ContextCompat.getColor(parentActivity, R.color.white), PorterDuff.Mode.SRC_IN);
                whatsAppDesc.setTextColor(ContextCompat.getColor(parentActivity, R.color.white));
                chkWhatsApp.setImageResource(R.drawable.ics_icon_opt_empty);
                chkWhatsApp.clearColorFilter();


                knoxContainer.setBackgroundResource(R.xml.rounded_list_transparent_white_stroke);
                iconKnox.setColorFilter(ContextCompat.getColor(parentActivity, R.color.white), PorterDuff.Mode.SRC_IN);
                knoxDesc.setTextColor(ContextCompat.getColor(parentActivity, R.color.white));
                chkKnox.setImageResource(R.drawable.ics_icon_opt_empty);
                chkKnox.clearColorFilter();

                otpOtionSelected="sms";



            }
        });


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(otpOtionSelected.equals(""))
                {
                    String err= mContext.getResources().getString(R.string.choosehowtoreceive);
                    Toast.makeText(mContext,err,Toast.LENGTH_LONG).show();


                }
                else
                {

                    //Reset Knox Prioritization
                    parentActivity.isComingFromActivatePage = false;
//                refreshOTPViews();

                    GeneralCommands.logEvent(parentActivity, "click_auth_otp_" + otpOtionSelected, "click_auth_otp_" + otpOtionSelected);
//                GeneralCommands.logEvent(parentActivity,"click_request_knox_otp_send_confirmation","click_request_knox_otp_send_confirmation");


                    GetData ob1 = new GetData();
                    ob1.initialize(parentActivity, loadingView);

                    try {

                        ob1.userRegister(storageObject.retrieveUserPhoneNumberFromPrefs(), false);
                        ob1.setUserRegisterListener(new GetData.UserRegisterListener() {
                            @Override
                            public void onObjectReady(String title) {

                            }

                            @Override
                            public void onDataLoaded(String deviceId) {

                                if (!deviceId.equals("")) {

                                    ob1.userOTPRequestKnox(storageObject.retrieveUserPhoneNumberFromPrefs(), otpOtionSelected, false, false);
                                    ob1.setUserOTPRequestKnoxListener(new GetData.OTPRequestKnoxListener() {
                                        @Override
                                        public void onObjectReady(String title) {

                                        }

                                        @Override
                                        public void onDataLoaded(int waitingTime, String message) {


                                            if (waitingTime > 0) {
                                                //Success
                                                goToOTPConfirmPage("" + waitingTime, otpOtionSelected);

                                            } else {

                                                String err = message;
                                                Toast.makeText(mContext, err, Toast.LENGTH_LONG).show();

                                                RelativeLayout resultError = loadingView.findViewById(R.id.resultError);
                                                resultError.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        resultError.setVisibility(View.GONE);
                                                        loadingView.setVisibility(View.GONE);
                                                    }
                                                });

                                                ///Error
//                                            requestRipsKnox.setVisibility(View.VISIBLE);
//                                            progressWheelKnox.setVisibility(View.GONE);
//                                            smsContent.setVisibility(View.VISIBLE);

                                            }

                                        }
                                    });


                                } else {
                                    String err = mContext.getResources().getString(R.string.registererror);
                                    Toast.makeText(mContext, err, Toast.LENGTH_LONG).show();

//                                ob1.problemLoading(err,101);
                                    ///Error
                                    RelativeLayout resultError = loadingView.findViewById(R.id.resultError);
                                    resultError.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            resultError.setVisibility(View.GONE);
                                            loadingView.setVisibility(View.GONE);
                                        }
                                    });


                                }
                            }
                        });
                    } catch (Exception e) {


                        String err = mContext.getResources().getString(R.string.registererror);
                        Toast.makeText(mContext, err, Toast.LENGTH_LONG).show();
//                    ob1.problemLoading(err,102);
                        ///Error

                        RelativeLayout resultError = loadingView.findViewById(R.id.resultError);
                        resultError.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                resultError.setVisibility(View.GONE);
                                loadingView.setVisibility(View.GONE);
                            }
                        });


                        e.printStackTrace();
                    }


                }


            }
        });


//        btnRequestSMS.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                GeneralCommands.logEvent(parentActivity,"click_auth_otp_sms","click_auth_otp_sms");
//
//                requestRipsSMS.setVisibility(View.GONE);
//                knoxContent.setVisibility(View.GONE);
//
//                //Reset Knox Prioritization
//                parentActivity.isComingFromActivatePage=false;
//                refreshOTPViews();
//
//                try
//                {
//
//                    progressWheelSMS.setVisibility(View.VISIBLE);
//                    Glide.with(parentActivity)
//                            .asGif()
//                            .load(R.drawable.loader_knox) // Replace with your local GIF resource
//                            .into(progressWheelSMS);
//
//                } catch (Exception e)
//                {
//
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//
//                GetData ob1 = new GetData();
//                ob1.initialize(parentActivity,loadingView);
//                try {
//
//                    ob1.userRegister(storageObject.retrieveUserPhoneNumberFromPrefs(),false);
//                    ob1.setUserRegisterListener(new GetData.UserRegisterListener() {
//                        @Override
//                        public void onObjectReady(String title) {
//
//                        }
//
//                        @Override
//                        public void onDataLoaded(String deviceId) {
//
//                            if(!deviceId.equals(""))
//                            {
//
//                                ob1.userOTPRequest(storageObject.retrieveUserPhoneNumberFromPrefs(),false);
//                                ob1.setUserOTPRequestListener(new GetData.OTPRequestListener() {
//                                    @Override
//                                    public void onObjectReady(String title) {
//
//                                    }
//
//                                    @Override
//                                    public void onDataLoaded(int waitingTime) {
//
//                                        if(waitingTime<=0)
//                                        {
//                                            String err= mContext.getResources().getString(R.string.registererror);
//                                            Toast.makeText(mContext,err,Toast.LENGTH_LONG).show();
//
//                                            RelativeLayout resultError= loadingView.findViewById(R.id.resultError);
//                                            resultError.setOnClickListener(new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
//                                                    resultError.setVisibility(View.GONE);
//                                                    loadingView.setVisibility(View.GONE);
//                                                }
//                                            });
//
//                                            ///Error
//                                            requestRipsSMS.setVisibility(View.VISIBLE);
//                                            progressWheelSMS.setVisibility(View.GONE);
//                                            knoxContent.setVisibility(View.VISIBLE);
//                                        }
//                                    }
//                                });
//
//                            }
//                            else
//                            {
//                                String err= mContext.getResources().getString(R.string.registererror);
//                                Toast.makeText(mContext,err,Toast.LENGTH_LONG).show();
//
////                                ob1.problemLoading(err,101);
//                                ///Error
//                                RelativeLayout resultError= loadingView.findViewById(R.id.resultError);
//                                resultError.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        resultError.setVisibility(View.GONE);
//                                        loadingView.setVisibility(View.GONE);
//                                    }
//                                });
//
//                                requestRipsSMS.setVisibility(View.VISIBLE);
//                                progressWheelSMS.setVisibility(View.GONE);
//                                knoxContent.setVisibility(View.VISIBLE);
//                            }
//                        }
//                    });
//                } catch (Exception e) {
//
//
//                    String err= mContext.getResources().getString(R.string.registererror);
//                    Toast.makeText(mContext,err,Toast.LENGTH_LONG).show();
////                    ob1.problemLoading(err,102);
//                    ///Error
//
//                    RelativeLayout resultError= loadingView.findViewById(R.id.resultError);
//                    resultError.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            resultError.setVisibility(View.GONE);
//                            loadingView.setVisibility(View.GONE);
//                        }
//                    });
//
//                    requestRipsSMS.setVisibility(View.VISIBLE);
//                    progressWheelSMS.setVisibility(View.GONE);
//                    knoxContent.setVisibility(View.VISIBLE);
//
//                    e.printStackTrace();
//                }
//
//
//
//            }
//        });

        refreshOTPViews();


        try
        {
            setConfigs();

        }catch(Exception e){}

        String startFrom = parentActivity.getIntent().getStringExtra(KEY_START_FROM);

        if (startFrom != null) {
            if (startFrom.equals("from_knox_alt")) {
                knoxContent.performClick();
            } else if(startFrom.equals("from_whatsapp_alt")){
                // This 'else' catches "from_knox_alt", "from_sms_alt", AND "activate_page"
                // If your default behavior inside performClick() is logic-heavy,
                // it might be defaulting back to Knox.
                whatsAppContent.performClick();
            }else {
                whatsAppContent.performClick();
            }
        }

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


    public String maskContactNumber(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        if (input.length() <= 6) {
            return input;
        }

        String firstThree = input.substring(0, 3);
        String lastThree = input.substring(input.length() - 3);
        return firstThree + "XXXXXX" + lastThree;
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

    public void goToOTPConfirmPage(String waitingTime, String channel)
    {


        Intent mIntent = new Intent(parentActivity, OTPConfirmActivity.class);
        mIntent.putExtra(KEY_START_FROM, "otp_choose");
        mIntent.putExtra(WAITING_TIME, waitingTime);
        mIntent.putExtra(OTP_MODE, channel);
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


                    //OTP SMS
                    is_otp_sms_enabled = mFirebaseConfig.getBoolean("is_otp_sms_enabled");
                    storageObject.saveOTPSMSEnabledToPrefs(is_otp_sms_enabled);

                    //OTP WhatsApp
                    is_otp_whatsapp_enabled = mFirebaseConfig.getBoolean("is_otp_whatsapp_enabled");
                    storageObject.saveOTPWhatsAppEnabledToPrefs(is_otp_whatsapp_enabled);


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


                        //OTP WhatsApp
                        is_otp_whatsapp_enabled = mFirebaseConfig.getBoolean("is_otp_whatsapp_enabled");
                        storageObject.saveOTPWhatsAppEnabledToPrefs(is_otp_whatsapp_enabled);

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

    public void refreshOTPViews() {

        // 1. Fetch Channels safely from SharedPreferences
        String[] rawJsonArray = storageObject.retrieveUserChannelsFromPrefs();

        java.util.List<String> channelList = new java.util.ArrayList<>();

        if (rawJsonArray != null && rawJsonArray.length > 0) {
            String rawJsonString = rawJsonArray[0]; // Extract the JSON string

            if (rawJsonString != null && !rawJsonString.isEmpty()) {
                try {
                    //Parsing directly as a JSONObject because it starts with '{'
                    JSONObject mainObject = new JSONObject(rawJsonString);

                    // Pulling the nested array out directly via its key
                    JSONArray channelsArray = mainObject.getJSONArray("channels");

                    // Extract the strings into your lowercase list
                    for (int i = 0; i < channelsArray.length(); i++) {
                        String channel = channelsArray.optString(i);
                        if (channel != null) {
                            channelList.add(channel.trim().toLowerCase());
                        }
                    }
                } catch (JSONException e) {
                }
            }
        }


        // 2. Visibility logic
        if (channelList.contains("device")) {
            knoxContent.setVisibility(View.VISIBLE);
        } else {
            knoxContent.setVisibility(View.GONE);
        }

        if (channelList.contains("whatsapp")) {
            whatsAppContent.setVisibility(View.VISIBLE);
        } else {
            whatsAppContent.setVisibility(View.GONE);
        }

        if (channelList.contains("sms")) {
            smsContent.setVisibility(View.VISIBLE);
        } else {
            smsContent.setVisibility(View.GONE);
        }
    }



}