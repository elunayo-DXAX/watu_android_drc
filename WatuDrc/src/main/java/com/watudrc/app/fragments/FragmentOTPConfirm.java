package com.watudrc.app.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.ConfigUpdate;
import com.google.firebase.remoteconfig.ConfigUpdateListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException;
import com.watudrc.app.ChangePasswordActivity;
import com.watudrc.app.ChooseOTPActivity;
import com.watudrc.app.GeneralCommands;
import com.watudrc.app.MainActivity;
import com.watudrc.app.OTPConfirmActivity;
import com.watudrc.app.R;
import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.font.FirmaTextView;
import com.watudrc.app.mobileservices.GetData;
import com.watudrc.app.objects.User;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;
import com.watudrc.app.util.FirebaseConfig;
import com.watudrc.app.view.CountdownBar;
import com.watudrc.app.view.MaterialRippleLayout;

import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FragmentOTPConfirm extends Fragment implements IWhereMyLocationConstants {

    private View parentView;

    private static StoreAndGetFromPrefs storageObject;
    public static OTPConfirmActivity parentActivity;
    static Context mContext;

    RelativeLayout btnResend;
    FirmaTextView btnPhoneText;
    RelativeLayout btnNext;

    LinearLayout countView;
    CountdownBar countDownView;



    //LoadingView
    ImageView progressWheelImage;
    RelativeLayout loadingView,wrongView;
    RelativeLayout loadingActive;
    FirmaTextView progressText;

    EditText codeEditText1,codeEditText2,codeEditText3,codeEditText4,codeEditText5;
    MaterialRippleLayout resendRips;


    //Knox OTP
    FirebaseConfig remoteConfig;
    FirebaseRemoteConfig mFirebaseConfig;
    Boolean has_knox_otp_enabled=true;

    Integer show_knox_otp_request_delay_in_seconds=10;
    RelativeLayout btnKnox;
    RelativeLayout knoxH, smsH, whatsAppH;
    RelativeLayout resendKnoxView;
    FirmaTextView  resendKnoxText;


    ImageView icShield,div;
    FirmaTextView icShieldText;


    //SMS
    private boolean isSmsReceiverRegistered = false;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        parentView = inflater.inflate(R.layout.fragment_otp_confirm, container, false);
        parentActivity = (OTPConfirmActivity)getActivity();
        mContext= parentActivity.getApplicationContext();
        storageObject = new StoreAndGetFromPrefs(mContext);

        codeEditText1 = parentView.findViewById(R.id.codeEditText1);
        codeEditText2 = parentView.findViewById(R.id.codeEditText2);
        codeEditText3 = parentView.findViewById(R.id.codeEditText3);
        codeEditText4 = parentView.findViewById(R.id.codeEditText4);
        codeEditText5 = parentView.findViewById(R.id.codeEditText5);

        wrongView = parentView.findViewById(R.id.wrongView);

        icShieldText = parentView.findViewById(R.id.icShieldText);
        div= parentView.findViewById(R.id.div);


        setCodeEditTexts();

        resendRips = parentView.findViewById(R.id.resendRips);
        btnResend = parentView.findViewById(R.id.btnResend);
        btnPhoneText = parentView.findViewById(R.id.btnPhoneText);

        btnNext = parentView.findViewById(R.id.btnNext);



        countView = parentView.findViewById(R.id.countView);


        loadingView = parentActivity.findViewById(R.id.loadingItems);
        loadingActive= parentActivity.findViewById(R.id.loadingActive);
        progressText = parentActivity.findViewById(R.id.progressText);

        btnPhoneText.setText(storageObject.retrieveUserPhoneNumberFromPrefs());

        int waitingSeconds=Integer.parseInt(parentActivity.waitingTime);

        //Countdown
        countDownView = new CountdownBar.Builder(parentActivity)
                .setMaxProgress(waitingSeconds)
                .setProgressColor(mContext.getResources().getColor(R.color.theme_color))
                .setTextColor(mContext.getResources().getColor(R.color.theme_orange))
                .build();

        countView.addView(countDownView);
        //Countdown 30 Seconds
        new CountDownTimer(waitingSeconds* 1000L, 1000) {

            public void onTick(long millisUntilFinished) {
                long secondsRemaining=  (millisUntilFinished / 1000);
                countDownView.setProgress((int)secondsRemaining);

            }

            public void onFinish() {
                countView.setVisibility(View.GONE);
                resendRips.setVisibility(View.VISIBLE);
            }

        }.start();

        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(codeEditText1.length()<1)
                {

                    String errorMsg= mContext.getResources().getString(R.string.enterthecodereceived);
                    wrongView.setVisibility(View.VISIBLE);
                    Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
                }
                else if(codeEditText2.length()<1)
                {
                    codeEditText1.setError(null);
                    String errorMsg= mContext.getResources().getString(R.string.enterthecodereceived);
                    wrongView.setVisibility(View.VISIBLE);
                    Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
                }

                else if(codeEditText3.length()<1)
                {
                    codeEditText2.setError(null);
                    String errorMsg= mContext.getResources().getString(R.string.enterthecodereceived);
                    wrongView.setVisibility(View.VISIBLE);
                    Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
                }

                else if(codeEditText4.length()<1)
                {
                    codeEditText3.setError(null);
                    String errorMsg= mContext.getResources().getString(R.string.enterthecodereceived);
                    wrongView.setVisibility(View.VISIBLE);
                    Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
                }

                else if(codeEditText5.length()<1)
                {
                    codeEditText4.setError(null);
                    String errorMsg= mContext.getResources().getString(R.string.enterthecodereceived);
                    wrongView.setVisibility(View.VISIBLE);
                    Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
                }



                else
                {
                    wrongView.setVisibility(View.GONE);
                    parentActivity.hideKeyboard();

                    String code=codeEditText1.getText().toString()+codeEditText2.getText().toString()+codeEditText3.getText().toString()+codeEditText4.getText().toString()+codeEditText5.getText().toString();
                    code=code.toUpperCase();

                    GetData ob1 = new GetData();
                    ob1.initialize(parentActivity,loadingView);
                    ob1.userOTPConfirm(storageObject.retrieveUserPhoneNumberFromPrefs(),code,false);
                    ob1.setUserOTPConfirmListener(new GetData.OTPConfirmListener() {
                        @Override
                        public void onObjectReady(String title) {

                        }

                        @Override
                        public void onDataLoaded(boolean isSuccess) {
                            if(isSuccess)
                            {
                                GeneralCommands.logEvent(parentActivity,"click_otp_confirmed","click_otp_confirmed");
                                goToChangePasswordPage(storageObject.retrieveUserPhoneNumberFromPrefs());
                            }
                            else {
                                wrongView.setVisibility(View.VISIBLE);
                            }

                        }
                    });




                }
            }
        });

        btnResend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
//                countView.setVisibility(View.VISIBLE);
                resendRips.setVisibility(View.GONE);
                wrongView.setVisibility(View.GONE);

                Toast.makeText(mContext,mContext.getResources().getString(R.string.requestingotp),Toast.LENGTH_SHORT).show();
                parentActivity.hideKeyboard();

                showKnowView(waitingSeconds);


                codeEditText1.setText("");
                codeEditText2.setText("");
                codeEditText3.setText("");
                codeEditText4.setText("");
                codeEditText5.setText("");


                GetData ob1 = new GetData();
                ob1.initialize(parentActivity,loadingView);
                ob1.userOTPResend(storageObject.retrieveUserPhoneNumberFromPrefs(),true);
                ob1.setUserOTPRequestListener(new GetData.OTPRequestListener() {
                    @Override
                    public void onObjectReady(String title) {

                    }

                    @Override
                    public void onDataLoaded(int waitingTime) {

                        if(waitingTime>0)
                        {
                            new CountDownTimer(waitingTime* 1000L, 1000) {

                                public void onTick(long millisUntilFinished) {
                                    long secondsRemaining=  (millisUntilFinished / 1000);
                                    int waitingSeconds=(int)secondsRemaining;
                                    countDownView.setProgress(waitingSeconds);

                                }

                                public void onFinish() {
                                    countView.setVisibility(View.GONE);
                                    resendRips.setVisibility(View.VISIBLE);
                                }

                            }.start();

                        }

                    }
                });
            }
        });





        //KNox
        btnKnox = parentView.findViewById(R.id.btnKnox);
        knoxH = parentView.findViewById(R.id.knoxH);
        resendKnoxView = parentView.findViewById(R.id.resendKnoxView);
        icShield = parentView.findViewById(R.id.icShield);

        resendKnoxText = parentView.findViewById(R.id.resendKnoxText);

        try
        {
            setConfigs();

        }catch(Exception e){}


        String mode = parentActivity.otpMode;


        btnKnox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                GeneralCommands.logEvent(parentActivity,"click_request_knox_otp_dialog","click_request_knox_otp_dialog");
                // goToChooseOTPPage();
                Intent mIntent = new Intent(parentActivity, ChooseOTPActivity.class);

                String mode = parentActivity.otpMode;
                String startFlag = "";

                // logic: Knox failed -> WhatsApp | WhatsApp failed -> Knox | SMS -> WhatsApp
                if ("device".equalsIgnoreCase(mode)) {
                    startFlag = "from_whatsapp_alt";
                    GeneralCommands.logEvent(parentActivity,"click_pop_up_fallback","click_pop_up_fallback");
                } else if ("whatsapp".equalsIgnoreCase(mode)) {
                    startFlag = "from_knox_alt";
                    GeneralCommands.logEvent(parentActivity,"click_whatsapp_fallback","click_whatsapp_fallback");

                } else if ("sms".equalsIgnoreCase(mode)) {
                    startFlag = "from_whatsapp_alt";
                    GeneralCommands.logEvent(parentActivity,"click_sms_fallback","click_sms_fallback");

                }

                mIntent.putExtra(KEY_START_FROM, startFlag);
                GeneralCommands.goToPage(parentActivity, mIntent, true);

            }
        });

//        knoxH.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });



        showKnowView(waitingSeconds);



        // new AppSignatureHelper(requireContext()).getAppSignatures();



        return parentView;
    }

    @Override
    public void onResume(){
        super.onResume();
        //Log Event
        if(loadingView !=null)loadingView.setVisibility(View.GONE);

    }


    @Override
    public void onStart() {
        super.onStart();
        //Start sms call
        smsRetrieverCall();
        registerSmsReceiver();
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (isSmsReceiverRegistered) {
            try {
                requireContext().unregisterReceiver(smsBroadcastReceiver);
            } catch (IllegalArgumentException e) {
                Log.w("ZeSMS", "Receiver was not registered or already unregistered.");
            }
            isSmsReceiverRegistered = false;
        }
    }

    public void registerSmsReceiver() {
        if (!isSmsReceiverRegistered) {
            IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                requireContext().registerReceiver(smsBroadcastReceiver, intentFilter, Context.RECEIVER_EXPORTED);
            } else {
                requireContext().registerReceiver(smsBroadcastReceiver, intentFilter);
            }
            isSmsReceiverRegistered = true;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that we are currently visible
        if (this.isVisible()) {
            // If we are becoming invisible, then...
            if (isVisibleToUser) {

                // TODO stop audio playback
            }
        }
    }

    private boolean isEmailValid(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void fakeLogin()
    {
        User user = new User();
        user.setName("James Reed");
//		user.setFirst_name("James");
//		user.setLast_name("Reed");
        user.setEmail("jamesmuindi@gmail.com");
        user.setId("1");
        user.setPhone_number(storageObject.retrieveUserPhoneNumberFromPrefs());
        user.setImage("https://zynamis.co.ke/images/team-members/3.jpg");
        storageObject.saveUserObjectToPrefs(user);
        storageObject.setUserRegistered(true);
        goToMainPage();
    }


    public void goToMainPage()
    {
        Intent mIntent = new Intent(parentActivity, MainActivity.class);
        mIntent.putExtra(KEY_START_FROM, "splash");
        GeneralCommands.goToPage( parentActivity,mIntent,true);

    }

    public void goToChooseOTPPage()
    {
        Intent mIntent = new Intent(parentActivity, ChooseOTPActivity.class);
        mIntent.putExtra(KEY_START_FROM, "otp_confirm");
        GeneralCommands.goToPage( parentActivity,mIntent,true);

    }


    public void goToChangePasswordPage(final  String userPhoneNumber)
    {
        Intent mIntent = new Intent(parentActivity, ChangePasswordActivity.class);
        mIntent.putExtra(KEY_START_FROM, "splash");
        mIntent.putExtra(PHONE_NUMBER, userPhoneNumber);
        GeneralCommands.goToPage( parentActivity,mIntent,true);

    }

    private void smsRetrieverCall() {
        // Get an instance of SmsRetrieverClient, used to start listening for a matching
        // SMS message.
        SmsRetrieverClient client = SmsRetriever.getClient(requireContext());

        // Starts SmsRetriever, which waits for ONE matching SMS message until timeout
        // (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
        // action SmsRetriever#SMS_RETRIEVED_ACTION.
        Log.d("ZeSMS", "Attempting to start SMS Retriever");

        Task<Void> task = client.startSmsRetriever();

        // Listen for success/failure of the start Task. If in a background thread, this
        // can be made blocking using Tasks.await(task, [timeout]);
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Successfully started retriever, expect broadcast intent
                Log.d("ZeSMS", "ZeSMSsmsRetrieverCall SUCCESS");

            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Failed to start retriever, inspect Exception for more details
                // ...
                Log.d("ZeSMS", "ZeSMSRetrieverCall FAIL");
                //result.setText("Retriever start Fail");
            }
        });
    }

    private final BroadcastReceiver smsBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("ZeSMS", "🔔 onReceive triggered");

            if (intent == null) {
                Log.w("ZeSMS", "❌ Received null intent");
                return;
            }

            Log.d("ZeSMS", "📨 Intent action: " + intent.getAction());

            if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
                Bundle extras = intent.getExtras();

                if (extras == null) {
                    Log.w("ZeSMS", "❌ Extras were null in the intent");
                    return;
                }

                Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
                Log.d("ZeSMS", "📦 Retrieved status: " + (status != null ? status.getStatusCode() : "null"));

//                if (status != null && status.getStatusCode() == CommonStatusCodes.TIMEOUT) {
//                    Log.w("ZeSMS", "⏰ SMS Retriever timed out");
//
//                    // 🔍 DEBUG: Read last SMS from inbox
//                    try {
//                        Cursor cursor = context.getContentResolver().query(
//                                Telephony.Sms.Inbox.CONTENT_URI,
//                                null, null, null,
//                                "date DESC LIMIT 1" // get the latest SMS
//                        );
//
//                        if (cursor != null && cursor.moveToFirst()) {
//                            String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));
//                            Log.d("ZeSMS", "📦 RAW SMS Body: " + body);
//                            cursor.close();
//                        } else {
//                            Log.w("ZeSMS", "No SMS found in inbox");
//                        }
//                    } catch (Exception e) {
//                        Log.e("ZeSMS", "Failed to read SMS", e);
//                    }
//                }

                if (status != null) {
                    switch (status.getStatusCode()) {
                        case CommonStatusCodes.SUCCESS:
                            String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);

                            if (message != null) {
                                Log.d("ZeSMS", "📩 SMS message: " + message);

                                // Extract the OTP
                                String otp = extractOtpFromMessage(message);
                                Log.d("ZeSMS", "🔍 Extracted OTP: " + otp);

                                if (otp != null) {
                                    setCodeToEditTexts(otp);
                                } else {
                                    Log.w("ZeSMS", "⚠️ OTP extraction returned null");
                                }
                            } else {
                                Log.w("ZeSMS", "❌ SMS message was null in extras");
                            }
                            break;

                        case CommonStatusCodes.TIMEOUT:
                            Log.w("ZeSMS", "⏰ SMS Retriever timed out");

                            break;

                        default:
                            Log.w("ZeSMS", "❓ Unhandled status code: " + status.getStatusCode());
                            break;
                    }
                } else {
                    Log.w("ZeSMS", "❌ Status object was null in extras");
                }
            }
        }
    };


    public void setCodeToEditTexts(String code)
    {
        Log.d("ZeSMSTAG", "ZeSMS CODe****"+code.charAt(0)+code.charAt(1)+code.charAt(2)+code.charAt(3)+code.charAt(4)+"********************");

        codeEditText1.setText(""+code.charAt(0));
        codeEditText2.setText(""+code.charAt(1));
        codeEditText3.setText(""+code.charAt(2));
        codeEditText4.setText(""+code.charAt(3));
        codeEditText5.setText(""+code.charAt(4));


    }

    public void setCodeEditTexts(){
        codeEditText1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(codeEditText1.getText().toString().length()==1)     //size as per your requirement
                {
                    codeEditText2.setEnabled(true);
                    codeEditText2.requestFocus();
                }
            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });

        codeEditText2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(codeEditText2.getText().toString().length()==1)     //size as per your requirement
                {
                    codeEditText3.setEnabled(true);
                    codeEditText3.requestFocus();
                }

            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

        });

        codeEditText3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(codeEditText3.getText().toString().length()==1)     //size as per your requirement
                {
                    codeEditText4.setEnabled(true);
                    codeEditText4.requestFocus();
                }

            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

        });
        codeEditText4.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                if(codeEditText4.getText().toString().length()==1)     //size as per your requirement
                {
                    codeEditText5.setEnabled(true);
                    codeEditText5.requestFocus();
                }



            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

        });

        codeEditText5.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start,int before, int count)
            {
                // TODO Auto-generated method stub
                codeEditText5.setEnabled(true);
                hideKeyboard();



            }
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

        });


        codeEditText2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        codeEditText1.requestFocus();
                    }
                }
                // Return false to allow the event to continue processing
                return false;
            }
        });


        codeEditText3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        codeEditText2.requestFocus();
                    }
                }
                // Return false to allow the event to continue processing
                return false;
            }
        });


        codeEditText4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        codeEditText3.requestFocus();
                    }
                }
                // Return false to allow the event to continue processing
                return false;
            }
        });


        codeEditText5.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        codeEditText4.requestFocus();
                    }
                }
                // Return false to allow the event to continue processing
                return false;
            }
        });
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


                    has_knox_otp_enabled = mFirebaseConfig.getBoolean("has_knox_otp_enabled");
                    show_knox_otp_request_delay_in_seconds = (int)mFirebaseConfig.getLong("show_knox_otp_request_delay_in_seconds");

//                    Toast.makeText(mContext, "Fetch and activate succeeded"+has_knox_otp_enabled+":"+show_knox_otp_request_delay_in_seconds,Toast.LENGTH_SHORT).show();

                    storageObject.setKnoxOTPEnabled(has_knox_otp_enabled);
                    storageObject.setKnoxOTPDelay(show_knox_otp_request_delay_in_seconds);

                    if(has_knox_otp_enabled!=null && !has_knox_otp_enabled.equals("") ) {
                        try {

                            refreshKnoxOTPDetails();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }







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


                        has_knox_otp_enabled = mFirebaseConfig.getBoolean("has_knox_otp_enabled");
                        show_knox_otp_request_delay_in_seconds = (int)mFirebaseConfig.getLong("show_knox_otp_request_delay_in_seconds");

//                        Toast.makeText(mContext, "Fetch and activate succeeded"+ has_knox_otp_enabled+show_knox_otp_request_delay_in_seconds,Toast.LENGTH_SHORT).show();

                        storageObject.setKnoxOTPEnabled(has_knox_otp_enabled);
                        storageObject.setKnoxOTPDelay(show_knox_otp_request_delay_in_seconds);

                        if(has_knox_otp_enabled!=null && !has_knox_otp_enabled.equals("") ) {
                            try {

                                refreshKnoxOTPDetails();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }


                    }
                });
            }

            @Override
            public void onError(FirebaseRemoteConfigException error) {
                Log.w("ZeConfigs", "Config update error with code: " + error.getCode(), error);
            }
        });
    }


    public void refreshKnoxOTPDetails()
    {

//        Toast.makeText(mContext,"KNox"+storageObject.retrieveKnoxOTPDelay()+":"+storageObject.isKnoxOTPEnabled(),Toast.LENGTH_SHORT).show();

        if(storageObject!=null && storageObject.isKnoxOTPEnabled())
        {

            show_knox_otp_request_delay_in_seconds=storageObject.retrieveKnoxOTPDelay() > 0 ?storageObject.retrieveKnoxOTPDelay():show_knox_otp_request_delay_in_seconds;
            //Countdown  Seconds
            new CountDownTimer(show_knox_otp_request_delay_in_seconds*1000, 1000) {

                public void onTick(long millisUntilFinished) {
                    long secondsRemaining=  (millisUntilFinished / 1000);

                }

                public void onFinish() {
                    knoxH.setVisibility(View.VISIBLE);

                    if (parentActivity.otpMode.equals("device")) {
                        knoxH.setBackgroundResource(R.xml.rounded_list_theme);
                        icShield.setImageResource(R.drawable.ic_whatsapp_icon);
                        icShieldText.setTextColor(mContext.getResources().getColor(R.color.white));
                        icShieldText.setText(Html.fromHtml(mContext.getResources().getString(R.string.pop_up_issue_help))); // Example text

                    } else if (parentActivity.otpMode.equals("whatsapp")) {

                        knoxH.setBackgroundResource(R.xml.rounded_list_button_orange_light);
                        icShield.setImageResource(R.drawable.ic_pop_up_icon);
                        icShieldText.setTextColor(mContext.getResources().getColor(R.color.white));
                        icShieldText.setText(Html.fromHtml(mContext.getResources().getString(R.string.whatsapp_issue_help))); // Example text

                    } else if (parentActivity.otpMode.equals("sms")) {
                        knoxH.setBackgroundResource(R.xml.rounded_list_theme);
                        icShield.setImageResource(R.drawable.ic_whatsapp_icon);
                        icShieldText.setTextColor(mContext.getResources().getColor(R.color.white));
                        icShieldText.setText(Html.fromHtml(mContext.getResources().getString(R.string.sms_issue_help))); // Example text
                    }
                }

            }.start();
        }
        else {

//            Toast.makeText(mContext,"Knox OTP is not enabled",Toast.LENGTH_SHORT).show();
            knoxH.setVisibility(View.GONE);
        }
    }

    public void showKnowView(int waitingSeconds) {
        if (waitingSeconds > 0) {
            resendKnoxView.setVisibility(View.VISIBLE);

            new CountDownTimer(waitingSeconds * 1000L, 1000) {
                public void onTick(long millisUntilFinished) {
                    long secondsRemaining = (millisUntilFinished / 1000);

                    // Update the timer text based on mode
                    String resentSecs = mContext.getResources().getString(R.string.resendknox, "" + secondsRemaining);
                    if (parentActivity.otpMode.equals("sms")) {
                        resentSecs = mContext.getResources().getString(R.string.resendsms, "" + secondsRemaining);
                    }
                    resendKnoxText.setText(resentSecs);
                }

                public void onFinish() {
                    // 1. Hide the timer container
                    resendKnoxView.setVisibility(View.GONE);

                    // 2. Enable the main button
                    btnKnox.setEnabled(true);

                }
            }.start();
        } else {
            btnKnox.setEnabled(true);
        }
    }


//    public void showKnowView(int waitingSeconds)
//    {
//        if(waitingSeconds>0)
//        {
//            resendKnoxView.setVisibility(View.VISIBLE);
//
//            new CountDownTimer(waitingSeconds* 1000L, 1000) {
//
//                public void onTick(long millisUntilFinished) {
//                    long secondsRemaining=  (millisUntilFinished / 1000);
//
//                    String resentSecs=mContext.getResources().getString(R.string.resendknox,""+secondsRemaining);
//                    if(parentActivity.otpMode.equals("sms"))
//                    {
//                        resentSecs=mContext.getResources().getString(R.string.resendsms,""+secondsRemaining);
//                    }
//                    resendKnoxText.setText(resentSecs);
//
//                }
//
//                public void onFinish() {
//                    if (parentActivity.otpMode.equals("whatsapp"))
//                    {
//
//                        knoxH.setBackgroundResource(R.xml.rounded_list_button_orange_light);
    ////                        knoxH.setBackgroundResource(R.xml.rounded_list_theme);
//
//                    } else if (parentActivity.otpMode.equals("device") || parentActivity.otpMode.equals("sms")){
//                        knoxH.setBackgroundResource(R.xml.rounded_list_theme_green);
//                        Toast.makeText(parentActivity, "Show whatsapp fallback", Toast.LENGTH_SHORT).show();
//
//                    }
//
//                    resendKnoxView.setVisibility(View.GONE);
//
//                    if(parentActivity.otpMode.equals("device"))
//                    {
//                        icShield.setImageResource(R.drawable.ics_knox_active);
//                    }
//                    else if (parentActivity.otpMode.equals("whatsapp"))
//                    {
//                        icShield.setImageResource(R.drawable.ics_knox_active_sms);
//                    }else{
//                        icShield.setImageResource(R.drawable.ics_secure_sms);
//                    }
//
//                    btnKnox.setEnabled(true);
//
//
//                }
//
//            }.start();
//        }
//        else {
//            btnKnox.setEnabled(true);
//        }
//
//    }
    private void hideKeyboard() {
        View view = parentActivity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) parentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    private String extractOtpFromMessage(String message) {
        if (message == null) return null;
        String normalized = Normalizer.normalize(message, Normalizer.Form.NFKC)
                .replace('\u00A0', ' ');

        Matcher m1 = Pattern.compile("(?iu)\\bcode\\b\\W*([A-Za-z]\\d{4})(?![A-Za-z0-9])").matcher(normalized);
        if (m1.find()) return m1.group(1);

        Matcher m2 = Pattern.compile("(?u)(?<![A-Za-z0-9])[A-Za-z]\\d{4}(?![A-Za-z0-9])").matcher(normalized);
        if (m2.find()) return m2.group();

        return null;
    }


}
