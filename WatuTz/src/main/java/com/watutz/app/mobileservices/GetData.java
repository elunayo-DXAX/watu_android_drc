
package com.watutz.app.mobileservices;

import static androidx.core.util.Preconditions.checkArgument;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.orhanobut.dialogplus.DialogPlus;
import com.watutz.app.ActivateAccountActivity;
import com.watutz.app.BuildConfig;
import com.watutz.app.ChangePasswordActivity;
import com.watutz.app.FilePath;
import com.watutz.app.GeneralCommands;
import com.watutz.app.LoginActivity;
import com.watutz.app.MainActivity;
import com.watutz.app.OTPConfirmActivity;
import com.watutz.app.R;
import com.watutz.app.SharedObjects;
import com.watutz.app.ViewPDFActivity;
import com.watutz.app.constant.IWhereMyLocationConstants;
import com.watutz.app.font.FirmaTextView;
import com.watutz.app.font.FontView;
import com.watutz.app.objects.AssetObject;
import com.watutz.app.objects.DeviceUpgradeObject;
import com.watutz.app.objects.ErrorResponseObject;
import com.watutz.app.objects.InitiatePaymentObject;
import com.watutz.app.objects.LoanAccountsObject;
import com.watutz.app.objects.LoanListItemObject;
import com.watutz.app.objects.Payment;
import com.watutz.app.objects.PaymentProviderObject;
import com.watutz.app.objects.PaymentsListObject;
import com.watutz.app.objects.ProfileObject;
import com.watutz.app.objects.ProvidersItem;
import com.watutz.app.objects.User;
import com.watutz.app.objects.UserLoginObject;
import com.watutz.app.prefs.StoreAndGetFromPrefs;
import com.watutz.app.util.AndroidMultiPartEntity;
import com.watutz.app.util.AndroidMultiPartEntity.ProgressListener;
import com.watutz.app.util.ApiInterface;
import com.watutz.app.util.FileUtils;
import com.watutz.app.util.ImageUtil;
import com.watutz.app.util.Logger;
import com.watutz.app.view.MaterialRippleLayout;
import com.ypyproductions.utils.DirectionUtils;
import com.ypyproductions.utils.StringUtils;

import org.apache.http.entity.mime.content.ByteArrayBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.ECGenParameterSpec;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GetData implements IWhereMyLocationConstants {


    public static StoreAndGetFromPrefs storageObject;

    //Firebase Stuff
    private DatabaseReference mFirebaseRefUsers;
    FirebaseAuth mAuth;


    //Loading View
    public View loadingView;
    DialogPlus dialogPlus;
    int rating = 0;


    static Activity parentActivity;
    static Context mContext;

    RelativeLayout loadingActive;
    RelativeLayout resultSuccess, btnSuccess, btnFailed, btnDone, btnSuccessTemp, btnRetry;
    RelativeLayout resultError;

    //	GeometricProgressView progressView;
    ImageView progressWheel;

    FirmaTextView progressText;
    FirmaTextView successText, successTitle, btnSuccessText, btnDoneText, btnRetryText;
    FontView btnFailedText;
    FirmaTextView errorText, errorTitle, supportNumbers;

    MaterialRippleLayout doneRips;

    ImageView errorIcon;

    Animation animFadeIn;
    Animation animFadeOut;

    private CountDownTimer timerUnlock;

    RelativeLayout resendView, btnResend;

    LinearLayout callOptionsView;


    ViewGroup tab;
    RelativeLayout orderSuccessView;


    ImageView userImage;

    //Listeners
    private PaymentsListener listenerPayments;
    private UserRegisterListener listenerUserRegister;
    private OTPRequestListener listenerOTPRequest;
    private UserChannelsListener listenerUserChannels;

    private OTPRequestKnoxListener listenerOTPRequestKnox;

    private LoanDetailsListener listenerLoanDetails;

    private TemporaryUnlockListener listenerTemporaryUnlock;

    private CustomerDetailsListener listenerCustomerDetails;

    private LoanUpgradeListener listenerLoanUpgrade;
    private OTPConfirmListener listenerOTPConfirm;

    private GetLoanAccountsListener listenerLoanAccounts;

    private UpcomingPaymentsListener listenerUpcomingPayments;
    private PaymentSTKListener listenerPaymentStk;
    private ConfirmPaymentSTKListener listenerConfirmPaymentStk;

    private SubmitFeedbackListener listenerSubmitFeedback;

    public void initialize(Activity mTheContext, View loadingview) {
        parentActivity = mTheContext;
        mContext = mTheContext;

        storageObject = new StoreAndGetFromPrefs(parentActivity);

        //Initialize Firebase URLS
        mFirebaseRefUsers = FirebaseDatabase.getInstance().getReference().child("users");
        FirebaseApp.initializeApp(parentActivity);
        mAuth = FirebaseAuth.getInstance();


        loadingView = loadingview;
        loadingActive = loadingView.findViewById(R.id.loadingActive);
        resultSuccess = loadingView.findViewById(R.id.resultSuccess);

        resultError = loadingView.findViewById(R.id.resultError);

        btnSuccess = loadingView.findViewById(R.id.btnSuccess);
        btnFailed = loadingView.findViewById(R.id.btnFailed);
        btnDone = loadingView.findViewById(R.id.btnDone);
        btnSuccessTemp = loadingView.findViewById(R.id.btnSuccessTemp);
        btnRetry = loadingView.findViewById(R.id.btnRetry);

        doneRips = loadingView.findViewById(R.id.doneRips);

        resendView = loadingView.findViewById(R.id.resendView);
        btnResend = loadingView.findViewById(R.id.btnResend);

        callOptionsView = loadingView.findViewById(R.id.callOptionsView);
        supportNumbers = loadingView.findViewById(R.id.supportNumbers);

        progressText = loadingView.findViewById(R.id.progressText);
        successText = loadingView.findViewById(R.id.successText);
        errorText = loadingView.findViewById(R.id.errorText);
        btnDoneText = loadingView.findViewById(R.id.btnDoneText);

        btnSuccessText = loadingView.findViewById(R.id.btnSuccessText);
        btnFailedText = loadingView.findViewById(R.id.btnFailedText);
        btnRetryText = loadingView.findViewById(R.id.btnRetryText);

        successTitle = loadingView.findViewById(R.id.successTextHeader);
        errorTitle = loadingView.findViewById(R.id.errorTextHeader);

        errorIcon= loadingView.findViewById(R.id.errorIcon);

//		progressView= (GeometricProgressView) loadingView.findViewById(R.id.progressView);;
        progressWheel = loadingView.findViewById(R.id.progressWheel);

        try {
            Glide.with(parentActivity)
                    .asGif()
                    .load(R.drawable.loader) // Replace with your local GIF resource
                    .into(progressWheel);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        animFadeIn = AnimationUtils.loadAnimation(parentActivity,
                R.anim.fadein);

        animFadeOut = AnimationUtils.loadAnimation(parentActivity,
                R.anim.fadeout);

        loadingview.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (progressText.getText().toString().contains("...")) {
                } else {
                    //finishLoading();

                }

            }
        });

        loadingActive.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (progressText.getText().toString().contains("...")) {
                } else {
                    //finishLoading();

                }

            }
        });

        resultSuccess.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (progressText.getText().toString().contains("...")) {
                } else {
                    //finishLoading();

                }

            }
        });

        btnFailed.setOnClickListener(view -> {
            finishLoading();
            if (dialogPlus != null) dialogPlus.dismiss();
        });

        btnSuccess.setOnClickListener(view -> finishLoading());


        if (btnSuccessTemp != null) {
            btnSuccessTemp.setOnClickListener(view -> {
                finishLoading();
                if (dialogPlus != null) dialogPlus.dismiss();
            });
        }


        if (btnRetry != null) {
            btnRetry.setOnClickListener(view -> {
                finishLoading();
                if (dialogPlus != null) dialogPlus.dismiss();
            });

        }
    }

    public void initializeSilent(Activity theActivity) {

        parentActivity = theActivity;
        mContext = theActivity;
        storageObject = new StoreAndGetFromPrefs(theActivity);
        //Initialize Firebase URLS
        mFirebaseRefUsers = FirebaseDatabase.getInstance().getReference().child("users");
    }

    public void initializeSilentContext(Context context) {

        mContext = context;
        storageObject = new StoreAndGetFromPrefs(context);
        //Initialize Firebase URLS
        mFirebaseRefUsers = FirebaseDatabase.getInstance().getReference().child("users");
    }

    public void startLoading(String info) {

        parentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Your UI update code here
                // This code will run on the main/UI thread
                loadingView.setVisibility(View.VISIBLE);
                loadingActive.setVisibility(View.VISIBLE);
                resultSuccess.setVisibility(View.GONE);
                resultError.setVisibility(View.GONE);
                progressText.setText(info);
            }
        });
        //progressView.start;
    }

//    public void problemLoading(String info, int code) {
//
//        String title = mContext.getResources().getString(R.string.sorry);
//        if(code ==429)
//        {
//            //Throttled Error
//            info = mContext.getResources().getString(R.string.throttlederror);
//            title=mContext.getResources().getString(R.string.toomanyrequests);
//
//        }
//
//
//        String finalInfo = info;
//        String finalTitle = title;
//        parentActivity.runOnUiThread(() -> {
//            JSONObject uParams = new JSONObject();
//            try {
//                uParams.put("errorMessage", finalInfo);
//                uParams.put("errorCode", code);
//
//            } catch (JSONException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            GeneralCommands.logEventObject(SharedObjects.context, "api_error_call", "api_error_call", uParams);
//
//            String title1 = mContext.getResources().getString(R.string.sorry);
//
//            if(code ==429)
//            {
//                //Throttled Error
//                errorIcon.setImageResource(R.drawable.ics_icon_many_requests);
//
//            }
//
//            loadingView.setVisibility(View.VISIBLE);
//            loadingActive.setVisibility(View.GONE);
//            resultSuccess.setVisibility(View.GONE);
//            resultError.setVisibility(View.VISIBLE);
//            errorText.setText(finalInfo);
//            errorTitle.setText(finalTitle);
//            progressText.setText("");
//
//            try {
//
//                if (storageObject.retrieveAssetObjectObjectsFromPrefs().size() > 1) {
//                    if (callOptionsView != null) callOptionsView.setVisibility(View.GONE);
//                } else if (storageObject.retrieveAssetObjectObjectsFromPrefs().size() == 1) {
//                    //Just One Asset Check whihc it is
//                    if (callOptionsView != null) callOptionsView.setVisibility(View.GONE);
//                    AssetObject item = storageObject.retrieveAssetObjectObjectsFromPrefs().get(0);
//                    if (item.getAssetTypeId() == 4) {
//                        //Smartphone so show support phone number
//                        supportNumbers.setText(mContext.getResources().getString(R.string.supportnumbersimu));
//
//                    } else if (item.getAssetTypeId() == 1 || item.getAssetTypeId() == 2) {
//                        //Boda or TukTuk
//                        supportNumbers.setText(mContext.getResources().getString(R.string.supportnumberboda));
//
//                    } else {
//
//                    }
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//    }

    public void problemLoading(String info, int code) {

        String title=mContext.getResources().getString(R.string.sorry);
        if(code ==429)
        {
            //Throttled Error
            info = mContext.getResources().getString(R.string.throttlederror);
            title=mContext.getResources().getString(R.string.toomanyrequests);

        }


        String finalInfo = info;
        String finalTitle = title;
        parentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {


                JSONObject uParams = new JSONObject();
                try
                {
                    uParams.put("errorMessage", finalInfo);
                    uParams.put("errorCode", code);

                } catch (JSONException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                GeneralCommands.logEventObject(SharedObjects.context, "api_error_call","api_error_call",uParams);;

                if(code ==429)
                {
                    //Throttled Error
                    errorIcon.setImageResource(R.drawable.ics_icon_many_requests);

                }

                loadingView.setVisibility(View.VISIBLE);
                loadingActive.setVisibility(View.GONE);
                resultSuccess.setVisibility(View.GONE);
                resultError.setVisibility(View.VISIBLE);
                errorText.setText(finalInfo);
                errorTitle.setText(finalTitle);
                progressText.setText("");

                try{

                    if(storageObject.retrieveAssetObjectObjectsFromPrefs().size()>1)
                    {
                        if(callOptionsView!=null) callOptionsView.setVisibility(View.GONE);
                    }
                    else if(storageObject.retrieveAssetObjectObjectsFromPrefs().size()==1) {
                        //Just One Asset Check whihc it is
                        if(callOptionsView !=null)callOptionsView.setVisibility(View.GONE);
                        AssetObject item= storageObject.retrieveAssetObjectObjectsFromPrefs().get(0);
                        if(item.getAssetTypeId() ==4)
                        {
                            //Smartphone so show support phone number
                            supportNumbers.setText(mContext.getResources().getString(R.string.supportnumbersimu));

                        }
                        else if(item.getAssetTypeId() ==1 || item.getAssetTypeId() ==2)
                        {
                            //Boda or TukTuk
                            supportNumbers.setText(mContext.getResources().getString(R.string.supportnumberboda));

                        }
                        else {

                        }
                    }

                }catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


    }

    public void successLoading(String info, String title) {
        //String tapToDismiss= mContext.getResources().getString(R.string.taptodismiss);
        //info= info +tapToDismiss;

        parentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Your UI update code here
                loadingView.setVisibility(View.VISIBLE);
                loadingActive.setVisibility(View.GONE);
                resultSuccess.setVisibility(View.VISIBLE);
                resultError.setVisibility(View.GONE);
                successText.setText(info);
                successTitle.setText(title);

                progressText.setText("");
            }
        });


    }

    public void finishLoading() {

        parentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Your UI update code here
                loadingView.setVisibility(View.GONE);
                loadingView.setVisibility(View.GONE);
                loadingActive.setVisibility(View.GONE);
                resultSuccess.setVisibility(View.GONE);
            }
        });
    }

    public void userLogin(final String phoneNumber, final String userPin) {

        String serverUrl = USER_LOGIN_URL;

        String theLoadingMsg = mContext.getResources().getString(R.string.loggingin);
        String theOp = "user_login";

        startLoading(theLoadingMsg);

        List<String> params = new ArrayList<String>();
        params.add(phoneNumber);
        params.add(userPin);

        JSONObject uParams = new JSONObject();

        try {
            uParams.put("phoneNumber", phoneNumber);
            uParams.put("pin", userPin);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ApiInterface service = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), uParams.toString());

        String token = "Bearer " + storageObject.retrieveTokenFromPrefs();
        String apiSecret = storageObject.retrieveApiSecretFromPrefs();

        //Prepare API Call
        Call<ResponseBody> call = null;
        call = service.userGetLoanAccounts(apiSecret, token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 401) {
                    logOutUser();
                } else if (response.code() == 200) {
                    //Got Successful response
                    try {

                        ServiceObject theObject = new ServiceObject();
                        theObject.setParams(params);
                        theObject.setResult(response.body().string());
                        JSONObject sampleObject = new JSONObject(theObject.getResult());
                        GeneralCommands.logEventObject(SharedObjects.context, "api_call", "api_request_body", uParams);

                        Bundle bundle = new Bundle();
                        bundle.putString(FirebaseAnalytics.Param.METHOD, "login");
                        FirebaseAnalytics.getInstance(mContext).logEvent(FirebaseAnalytics.Event.LOGIN, bundle);

                        performUserLogin(theObject);
                    } catch (Exception e) {

                        if (listenerLoanAccounts != null) listenerLoanAccounts.onDataLoaded(null);

                        String info = mContext.getResources().getString(R.string.getloanserror);
                        problemLoading(info, 101);
                        e.printStackTrace();
                    }

                } else {

                    if (listenerLoanAccounts != null) listenerLoanAccounts.onDataLoaded(null);

                    String message = mContext.getResources().getString(R.string.getloanserror);
                    // Handle the error response here
                    try {
                        ResponseBody errorBody = response.errorBody();
                        if (errorBody != null) {

                            // Parse the error response body as JSON
                            Gson gson = new Gson();
                            ErrorResponseObject errorResponse = gson.fromJson(errorBody.string(), ErrorResponseObject.class);

                            int errorCode = errorResponse.getError().getCode();

//                            message = errorResponse.getError().getMessage();

                            // Handle the error information

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    problemLoading(message, response.code());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if (listenerLoanAccounts != null) listenerLoanAccounts.onDataLoaded(null);

                Logger.d("API error:", t.getMessage());
                String info = mContext.getResources().getString(R.string.getloanserror);
                problemLoading(info, 101);

            }
        });
    }

    public void userRegister(final String phoneNumber, boolean isSilent) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {

        String serverUrl = USER_REGISTER_URL;

        String theLoadingMsg = mContext.getResources().getString(R.string.registering);
        String theOp = "user_register";

        if (!isSilent) startLoading(theLoadingMsg);

        final KeyPair keyPair = generateECKeyPair();
        String publicKey = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            publicKey = java.util.Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        }

        String apiSecret = GeneralCommands.getSha256Hash(phoneNumber + publicKey);
        storageObject.saveApiSecretToPrefs(apiSecret);


        List<String> params = new ArrayList<String>();
        params.add(phoneNumber);
        params.add(publicKey);

        JSONObject uParams = new JSONObject();

        try {
            uParams.put("phoneNumber", phoneNumber);
            uParams.put("publicKey", publicKey);


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        ApiInterface service = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), uParams.toString());

        //Prepare API Call
        Call<ResponseBody> call = null;
        call = service.userRegister(apiSecret, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 401) {
                    logOutUser();
                } else if (response.code() == 200) {
                    //Got Successfull response
                    try {
                        ServiceObject theObject = new ServiceObject();
                        theObject.setParams(params);
                        theObject.setResult(response.body().string());
                        JSONObject sampleObject = new JSONObject(theObject.getResult());

                        String deviceId = "";
                        String token = "";
                        try {
                            deviceId = sampleObject.getString("deviceId");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        long ts = System.currentTimeMillis();
                        final Instant instant;  // Gets the current UTC timestamp
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            instant = Instant.now();
                            ts = instant.getEpochSecond();

                        }

                        if (!deviceId.equals("")) {

                            token = Jwts.builder()
                                    .claim("did", deviceId)
                                    .claim("ts", ts)
                                    .signWith(SignatureAlgorithm.ES256, keyPair.getPrivate())
                                    .compact();
                            theObject.getParams().add(token);
                            storageObject.saveTokenToPrefs(token);
                            storageObject.saveDeviceIDToPrefs(deviceId);

                        }

                        GeneralCommands.logEventObject(SharedObjects.context, "api_call", "api_request_body", uParams);

                        GeneralCommands.logEvent(SharedObjects.context, FirebaseAnalytics.Event.SIGN_UP, FirebaseAnalytics.Event.SIGN_UP);

                        Bundle bundle = new Bundle();
                        bundle.putString(FirebaseAnalytics.Param.METHOD, "sign_up");
                        FirebaseAnalytics.getInstance(mContext).logEvent(FirebaseAnalytics.Event.SIGN_UP, bundle);

                        performUserRegister(theObject);

                    } catch (Exception e) {

                        if (listenerUserRegister != null) listenerUserRegister.onDataLoaded("");

                        String info = mContext.getResources().getString(R.string.registererror);
                        problemLoading(info, response.code());
                        e.printStackTrace();
                    }

                } else {

                    if (listenerUserRegister != null) listenerUserRegister.onDataLoaded("");

                    String info = mContext.getResources().getString(R.string.registererror);
                    problemLoading(info, response.code());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Logger.d("API error:", t.getMessage());

                if (listenerUserRegister != null) listenerUserRegister.onDataLoaded("");

                String info = mContext.getResources().getString(R.string.loginerror);
                if (!isSilent) problemLoading(info, 101);

            }
        });


    }

    public void userOTPRequest(final String userPhoneNumber, boolean isSilent) {

        String serverUrl = USER_OTP_REQUEST_URL;

        String theLoadingMsg = mContext.getResources().getString(R.string.requestingotp);
        String theOp = "user_otp_request";

        if (!isSilent) startLoading(theLoadingMsg);

        List<String> params = new ArrayList<String>();
        params.add(userPhoneNumber);

        JSONObject uParams = new JSONObject();

        try {
            uParams.put("lang", storageObject.retrieveLanguageFromPrefs());
            uParams.put("deliveryType", "sms");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //Get OTP Channels
        userGetOTPChannels(true);

        ApiInterface service = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), uParams.toString());

        String token = "Bearer " + storageObject.retrieveTokenFromPrefs();
        String apiSecret = storageObject.retrieveApiSecretFromPrefs();

        //Prepare API Call
        Call<ResponseBody> call = null;
        call = service.userOTPRequest(apiSecret, token, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 401) {
                    logOutUser();
                } else if (response.code() == 200) {
                    //Got Successfull response
                    try {
                        ServiceObject theObject = new ServiceObject();
                        theObject.setParams(params);
                        theObject.setResult(response.body().string());
                        JSONObject sampleObject = new JSONObject(theObject.getResult());
                        GeneralCommands.logEventObject(SharedObjects.context, "api_call", "api_request_body", uParams);
                        ;

                        performUserOTPRequest(theObject, true, isSilent);


                    } catch (Exception e) {

                        if (listenerOTPRequest != null) listenerOTPRequest.onDataLoaded(0);


                        String info = mContext.getResources().getString(R.string.requestingotperror);
                        if (!isSilent) problemLoading(info, response.code());
                        e.printStackTrace();
                    }

                } else {

                    if (listenerOTPRequest != null) listenerOTPRequest.onDataLoaded(0);

                    String message = mContext.getResources().getString(R.string.requestingotperror);
                    // Handle the error response here
                    try {
                        ResponseBody errorBody = response.errorBody();
                        if (errorBody != null) {
                            String errorJson = errorBody.string();

                            if (errorJson.trim().startsWith("{")) {

                                // Parse the error response body as JSON
                                Gson gson = new Gson();
                                ErrorResponseObject errorResponse = gson.fromJson(errorJson, ErrorResponseObject.class);

                                int errorCode = errorResponse.getError().getCode();
                                ;
//                                message = errorResponse.getError().getMessage();

                                // Handle the error information
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!isSilent) problemLoading(message, response.code());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if (listenerOTPRequest != null) listenerOTPRequest.onDataLoaded(0);


                Logger.d("API error:", t.getMessage());
                String info = mContext.getResources().getString(R.string.requestingotperror);
                if (!isSilent) problemLoading(info, 400);

            }
        });

    }


    public void userOTPResend(final String userPhoneNumber, boolean isSilent) {

        String serverUrl = USER_OTP_REQUEST_URL;

        String theLoadingMsg = mContext.getResources().getString(R.string.requestingotp);
        String theOp = "user_otp_request";

        if (!isSilent) startLoading(theLoadingMsg);

        List<String> params = new ArrayList<String>();
        params.add(userPhoneNumber);

        JSONObject uParams = new JSONObject();

        try {
            uParams.put("lang", storageObject.retrieveLanguageFromPrefs());
            uParams.put("deliveryType", "sms");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //Get OTP Channels
        userGetOTPChannels(true);

        ApiInterface service = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), uParams.toString());

        String token = "Bearer " + storageObject.retrieveTokenFromPrefs();
        String apiSecret = storageObject.retrieveApiSecretFromPrefs();

        //Prepare API Call
        Call<ResponseBody> call = null;
        call = service.userOTPRequest(apiSecret, token, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 401) {
                    logOutUser();
                } else if (response.code() == 200) {
                    //Got Successfull response
                    try {
                        ServiceObject theObject = new ServiceObject();
                        theObject.setParams(params);
                        theObject.setResult(response.body().string());
                        JSONObject sampleObject = new JSONObject(theObject.getResult());
                        GeneralCommands.logEventObject(SharedObjects.context, "api_call", "api_request_body", uParams);
                        ;

                        performUserOTPRequest(theObject, false, isSilent);


                    } catch (Exception e) {

                        if (listenerOTPRequest != null) listenerOTPRequest.onDataLoaded(0);


                        String info = mContext.getResources().getString(R.string.requestingotperror);
                        problemLoading(info, response.code());
                        e.printStackTrace();
                    }

                } else {

                    if (listenerOTPRequest != null) listenerOTPRequest.onDataLoaded(0);

                    String message = mContext.getResources().getString(R.string.requestingotperror);

                    // Handle the error response here
                    try {
                        ResponseBody errorBody = response.errorBody();
                        if (errorBody != null) {
                            String errorJson = errorBody.string();

                            if (errorJson.trim().startsWith("{")) {
                                Gson gson = new Gson();
                                ErrorResponseObject errorResponse = gson.fromJson(errorJson, ErrorResponseObject.class);

                                if (errorResponse != null && errorResponse.getError() != null) {
                                    int errorCode = errorResponse.getError().getCode();
//                                    message = errorResponse.getError().getMessage();
                                    // Do something with errorCode and message
                                } else {
                                    // Handle null object safely
                                    Log.e("API", "Error object was null");
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    problemLoading(message, response.code());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if (listenerOTPRequest != null) listenerOTPRequest.onDataLoaded(0);


                Logger.d("API error:", t.getMessage());
                String info = mContext.getResources().getString(R.string.requestingotperror);
                if (!isSilent) problemLoading(info, 400);

            }
        });

    }
    public void userOTPRequestKnox(final String userPhoneNumber, boolean shouldGoToConfirmPage, boolean isSilent) {

        String serverUrl = USER_OTP_REQUEST_URL;

        String theLoadingMsg = mContext.getResources().getString(R.string.requestingotp);
        String theOp = "user_otp_request";


        if (!isSilent) startLoading(theLoadingMsg);


        List<String> params = new ArrayList<String>();
        params.add(userPhoneNumber);


        JSONObject uParams = new JSONObject();

        try {
            uParams.put("lang", storageObject.retrieveLanguageFromPrefs());
            uParams.put("deliveryType", "device");


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        //Get OTP Channels
        userGetOTPChannels(true);

        ApiInterface service = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), uParams.toString());

        String token = "Bearer " + storageObject.retrieveTokenFromPrefs();
        String apiSecret = storageObject.retrieveApiSecretFromPrefs();

        //Prepare API Call
        Call<ResponseBody> call = null;
        call = service.userOTPRequestKnox(apiSecret, token, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 401) {
                    logOutUser();
                } else if (response.code() == 200) {
                    //Got Successfull response
                    try {
                        ServiceObject theObject = new ServiceObject();
                        theObject.setParams(params);
                        theObject.setResult(response.body().string());
                        JSONObject sampleObject = new JSONObject(theObject.getResult());
                        GeneralCommands.logEventObject(SharedObjects.context, "api_call", "api_request_body", uParams);
                        ;

                        performUserOTPRequestKnox(theObject, shouldGoToConfirmPage, isSilent);


                    } catch (Exception e) {

                        String info = mContext.getResources().getString(R.string.requestingotperror);

                        if (listenerOTPRequestKnox != null)
                            listenerOTPRequestKnox.onDataLoaded(0, info);


                        if (!isSilent) problemLoading(info, response.code());
                        e.printStackTrace();
                    }

                } else {

                    String message = mContext.getResources().getString(R.string.requestingotperror);

                    // Handle the error response here
                    try {
                        ResponseBody errorBody = response.errorBody();
                        if (errorBody != null) {
                            String errorJson = errorBody.string();

                            if (errorJson.trim().startsWith("{")) {
                                Gson gson = new Gson();
                                ErrorResponseObject errorResponse = gson.fromJson(errorJson, ErrorResponseObject.class);

                                if (errorResponse != null && errorResponse.getError() != null) {
                                    int errorCode = errorResponse.getError().getCode();
//                                    message = errorResponse.getError().getMessage();
                                    // Do something with errorCode and message
                                } else {
                                    // Handle null object safely
                                    Log.e("API", "Error object was null");
                                }
                            }
                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                    if (listenerOTPRequestKnox != null)
                        listenerOTPRequestKnox.onDataLoaded(0, message);

                    if (!isSilent) problemLoading(message, response.code());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                String info = mContext.getResources().getString(R.string.requestingotperror);

                if (listenerOTPRequestKnox != null) listenerOTPRequestKnox.onDataLoaded(0, info);


                Logger.d("API error:", t.getMessage());
                if (!isSilent) problemLoading(info, 400);

            }
        });

    }


    public void userOTPConfirm(final String userPhoneNumber, final String userOTP, boolean isSilent) {

        String serverUrl = USER_OTP_CONFIRM_URL;

        String theLoadingMsg = mContext.getResources().getString(R.string.confirmingotp);
        String theOp = "user_otp_confirm";

        if (!isSilent) startLoading(theLoadingMsg);


        List<String> params = new ArrayList<String>();
        params.add(userPhoneNumber);
        params.add(userOTP);


        JSONObject uParams = new JSONObject();

        try {
            uParams.put("otp", userOTP);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ApiInterface service = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), uParams.toString());

        String token = "Bearer " + storageObject.retrieveTokenFromPrefs();
        String apiSecret = storageObject.retrieveApiSecretFromPrefs();

        //Prepare API Call
        Call<ResponseBody> call = null;
        call = service.userOTPConfirm(apiSecret, token, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 401) {
                    logOutUser();
                } else if (response.code() == 200) {
                    //Got Successfull response
                    try {
                        ServiceObject theObject = new ServiceObject();
                        theObject.setParams(params);
                        theObject.setResult(response.body().string());
                        JSONObject sampleObject = new JSONObject(theObject.getResult());
                        GeneralCommands.logEventObject(SharedObjects.context, "api_call", "api_request_body", uParams);

                        performUserOTPConfirm(theObject);

                    } catch (Exception e) {

                        if (listenerOTPConfirm != null) listenerOTPConfirm.onDataLoaded(false);

                        String info = mContext.getResources().getString(R.string.confirmingotperror);
                        problemLoading(info, response.code());
                        e.printStackTrace();
                    }

                } else {

                    if (listenerOTPConfirm != null) listenerOTPConfirm.onDataLoaded(false);
                    String message = mContext.getResources().getString(R.string.confirmingotperror);

                    // Handle the error response here
                    try {
                        ResponseBody errorBody = response.errorBody();
                        if (errorBody != null) {

                            // Parse the error response body as JSON
                            Gson gson = new Gson();
                            ErrorResponseObject errorResponse = gson.fromJson(errorBody.string(), ErrorResponseObject.class);

                            int errorCode = errorResponse.getError().getCode();
//                            message = errorResponse.getError().getMessage();

                            // Handle the error information

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    problemLoading(message, response.code());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if (listenerOTPConfirm != null) listenerOTPConfirm.onDataLoaded(false);

                Logger.d("API error:", t.getMessage());
                String info = mContext.getResources().getString(R.string.confirmingotperror);
                if (!isSilent) problemLoading(info, 101);

            }
        });

    }

    public void userGetLoanUpgradeEligibility(String loanNumber, boolean isSilent) {

        String serverUrl = USER_GET_LOAN_DETAILS_URL;

        String theLoadingMsg = mContext.getResources().getString(R.string.gettingloandetails);
        String theOp = "user_get_loan_details";

        if (!isSilent) startLoading(theLoadingMsg);

        List<String> params = new ArrayList<String>();
        params.add(loanNumber);

        JSONObject uParams = new JSONObject();

        try {
            uParams.put("loanNumber", loanNumber);


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ApiInterface service = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), uParams.toString());

        String token = "Bearer " + storageObject.retrieveTokenFromPrefs();
        String apiSecret = storageObject.retrieveApiSecretFromPrefs();

        //Prepare API Call
        Call<ResponseBody> call = null;
        call = service.userGetLoanUpgradeEligibility(apiSecret, token, loanNumber);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 401) {
                    logOutUser();
                } else if (response.code() == 200) {
                    //Got Successfull response
                    try {
                        ServiceObject theObject = new ServiceObject();
                        theObject.setParams(params);
                        theObject.setResult(response.body().string());
                        JSONObject sampleObject = new JSONObject(theObject.getResult());
                        GeneralCommands.logEventObject(SharedObjects.context, "api_call", "api_request_body", uParams);
                        ;

                        performUserGetLoanUpgradeEligibility(theObject, isSilent);


                    } catch (Exception e) {

                        if (listenerLoanDetails != null) listenerLoanDetails.onDataLoaded(null);


                        String info = mContext.getResources().getString(R.string.getloandetailsserror);
                        if (!isSilent) problemLoading(info, response.code());
                        e.printStackTrace();
                    }

                } else {

                    if (listenerLoanDetails != null) listenerLoanDetails.onDataLoaded(null);

                    String message = mContext.getResources().getString(R.string.getloandetailsserror);
                    // Handle the error response here
                    try {
                        ResponseBody errorBody = response.errorBody();
                        if (errorBody != null) {
                            String errorJson = errorBody.string();

                            if (errorJson.trim().startsWith("{")) {
                                Gson gson = new Gson();
                                ErrorResponseObject errorResponse = gson.fromJson(errorJson, ErrorResponseObject.class);

                                if (errorResponse != null && errorResponse.getError() != null) {
                                    int errorCode = errorResponse.getError().getCode();
//                                    message = errorResponse.getError().getMessage();
                                    // Do something with errorCode and message
                                } else {
                                    // Handle null object safely
                                    Log.e("API", "Error object was null");
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!isSilent) problemLoading(message, response.code());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if (listenerLoanDetails != null) listenerLoanDetails.onDataLoaded(null);

                Logger.d("API error:", t.getMessage());
                String info = mContext.getResources().getString(R.string.getloandetailsserror);
                if (!isSilent) problemLoading(info, 400);

            }
        });

    }


    public void userGetCustomerDetails(boolean isSilent) {

        String serverUrl = USER_GET_MY_PROFILE_URL;

        String theLoadingMsg = mContext.getResources().getString(R.string.gettingloandetails);
        String theOp = "user_get_loan_details";

        if (!isSilent) startLoading(theLoadingMsg);

        List<String> params = new ArrayList<String>();

        JSONObject uParams = new JSONObject();


        ApiInterface service = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), uParams.toString());

        String token = "Bearer " + storageObject.retrieveTokenFromPrefs();
        String apiSecret = storageObject.retrieveApiSecretFromPrefs();

        //Prepare API Call
        Call<ResponseBody> call = null;
        call = service.userGetCustomerDetails(apiSecret, token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 401) {
                    logOutUser();
                } else if (response.code() == 200) {
                    //Got Successfull response
                    try {

                        String responseBodyString = response.body().string();

                        ServiceObject theObject = new ServiceObject();
                        theObject.setParams(params);
                        theObject.setResult(responseBodyString);
                        JSONObject sampleObject = new JSONObject(theObject.getResult());
                        GeneralCommands.logEventObject(SharedObjects.context, "api_call", "api_request_body", uParams);


                        performUserGetMyProfile(theObject, isSilent);


                    } catch (Exception e) {

                        if (listenerCustomerDetails != null)
                            listenerCustomerDetails.onDataLoaded(null);


                        String info = mContext.getResources().getString(R.string.getloandetailsserror);
                        if (!isSilent) problemLoading(info, response.code());
                        e.printStackTrace();
                    }

                } else {

                    if (listenerCustomerDetails != null) listenerCustomerDetails.onDataLoaded(null);

                    String message = mContext.getResources().getString(R.string.getloandetailsserror);
                    // Handle the error response here
                    try {
                        ResponseBody errorBody = response.errorBody();
                        if (errorBody != null) {

                            // Parse the error response body as JSON
                            Gson gson = new Gson();
                            ErrorResponseObject errorResponse = gson.fromJson(errorBody.string(), ErrorResponseObject.class);

                            int errorCode = errorResponse.getError().getCode();
                            ;
//                            message = errorResponse.getError().getMessage();

                            // Handle the error information

                        }
                    } catch (Exception e) {
                        if (listenerCustomerDetails != null)
                            listenerCustomerDetails.onDataLoaded(null);

                        e.printStackTrace();
                    }
                    if (!isSilent) problemLoading(message, response.code());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if (listenerLoanDetails != null) listenerLoanDetails.onDataLoaded(null);

                Logger.d("API error:", t.getMessage());
                String info = mContext.getResources().getString(R.string.getloandetailsserror);
                if (!isSilent) problemLoading(info, 101);

            }
        });

    }

    public void addUserAction(String action, boolean isSilent) {

        String serverUrl = USER_GET_MY_PROFILE_URL;

        String theLoadingMsg = mContext.getResources().getString(R.string.addinguserction);

        if (!isSilent) startLoading(theLoadingMsg);

        List<String> params = new ArrayList<String>();

        JSONObject uParams = new JSONObject();
        try {
            uParams.put("type", action);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        ApiInterface service = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), uParams.toString());

        String token = "Bearer " + storageObject.retrieveTokenFromPrefs();
        String apiSecret = storageObject.retrieveApiSecretFromPrefs();

        //Prepare API Call
        Call<ResponseBody> call = null;
        call = service.addUserAction(apiSecret, token, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 401) {
                    logOutUser();
                } else if (response.code() == 200) {
                    //Got Successfull response
                    try {

                        String responseBodyString = response.body().string();

                        ServiceObject theObject = new ServiceObject();
                        theObject.setParams(params);
                        theObject.setResult(responseBodyString);
                        JSONObject sampleObject = new JSONObject(theObject.getResult());
                        GeneralCommands.logEventObject(SharedObjects.context, "api_call", "api_request_body", uParams);


                        performUserAddAction(theObject, isSilent);


                    } catch (Exception e) {

//						if(listenerAddUserAction!=null) listenerAddUserAction.onDataLoaded(null);


                        String info = mContext.getResources().getString(R.string.adduseractionerror);
                        if (!isSilent) problemLoading(info, response.code());
                        e.printStackTrace();
                    }

                } else {

//					if(listenerAddUserAction!=null) listenerAddUserAction.onDataLoaded(null);

                    String message = mContext.getResources().getString(R.string.adduseractionerror);
                    // Handle the error response here
                    try {
                        ResponseBody errorBody = response.errorBody();
                        if (errorBody != null) {

                            // Parse the error response body as JSON
//							Gson gson = new Gson();
//							ErrorResponseObject errorResponse = gson.fromJson(errorBody.string(), ErrorResponseObject.class);
//
//							int errorCode = errorResponse.getError().getCode();;
//							message = errorResponse.getError().getMessage();

                            // Handle the error information

                        }
                    } catch (Exception e) {
//						if(listenerAddUserAction!=null) listenerAddUserAction.onDataLoaded(null);

                        e.printStackTrace();
                    }
                    if (!isSilent) problemLoading(message, response.code());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

//				if(listenerAddUserAction!=null) listenerAddUserAction.onDataLoaded(null);

                Logger.d("API error:", t.getMessage());
                String info = mContext.getResources().getString(R.string.adduseractionerror);
                if (!isSilent) problemLoading(info, 101);

            }
        });

    }


    public void userEditProfile(boolean isSilent) {

        String serverUrl = USER_GET_MY_PROFILE_URL;

        String theLoadingMsg = mContext.getResources().getString(R.string.editingmyprofile);

        if (!isSilent) startLoading(theLoadingMsg);

        List<String> params = new ArrayList<String>();

        JSONObject uParams = new JSONObject();
        try {
            uParams.put("lang", storageObject.retrieveLanguageFromPrefs());

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        ApiInterface service = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), uParams.toString());

        String token = "Bearer " + storageObject.retrieveTokenFromPrefs();
        String apiSecret = storageObject.retrieveApiSecretFromPrefs();

        //Prepare API Call
        Call<ResponseBody> call = null;
        call = service.userUpdateProfile(apiSecret, token, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 401) {
                    logOutUser();
                } else if (response.code() == 200) {
                    //Got Successfull response
                    try {

                        String responseBodyString = response.body().string();

                        ServiceObject theObject = new ServiceObject();
                        theObject.setParams(params);
                        theObject.setResult(responseBodyString);
//						JSONObject sampleObject = new JSONObject(theObject.getResult());
//						GeneralCommands.logEventObject(SharedObjects.context, "api_call","api_request_body",uParams);
//
//
//						performUserUpdateProfile(theObject,isSilent);


                    } catch (Exception e) {

//						if(listenerAddUserAction!=null) listenerAddUserAction.onDataLoaded(null);


                        String info = mContext.getResources().getString(R.string.editmyprofileerror);
                        if (!isSilent) problemLoading(info, response.code());
                        e.printStackTrace();
                    }

                } else {

//					if(listenerAddUserAction!=null) listenerAddUserAction.onDataLoaded(null);

                    String message = mContext.getResources().getString(R.string.editmyprofileerror);
                    // Handle the error response here
                    try {
                        ResponseBody errorBody = response.errorBody();
                        if (errorBody != null) {

                            // Parse the error response body as JSON
//							Gson gson = new Gson();
//							ErrorResponseObject errorResponse = gson.fromJson(errorBody.string(), ErrorResponseObject.class);
//
//							int errorCode = errorResponse.getError().getCode();;
//							message = errorResponse.getError().getMessage();

                            // Handle the error information

                        }
                    } catch (Exception e) {
//						if(listenerAddUserAction!=null) listenerAddUserAction.onDataLoaded(null);

                        e.printStackTrace();
                    }
                    if (!isSilent) problemLoading(message, response.code());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

//				if(listenerAddUserAction!=null) listenerAddUserAction.onDataLoaded(null);

                Logger.d("API error:", t.getMessage());
                String info = mContext.getResources().getString(R.string.editmyprofileerror);
                if (!isSilent) problemLoading(info, 101);

            }
        });

    }

    public void userUpdateTokenFCM(String newToken, boolean isSilent) {

        String serverUrl = USER_GET_MY_PROFILE_URL;

        String theLoadingMsg = mContext.getResources().getString(R.string.updatingtoken);

        if (!isSilent) startLoading(theLoadingMsg);

        List<String> params = new ArrayList<String>();

        JSONObject uParams = new JSONObject();
        try {
            uParams.put("token", newToken);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        ApiInterface service = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), uParams.toString());

        String token = "Bearer " + storageObject.retrieveTokenFromPrefs();
        String apiSecret = storageObject.retrieveApiSecretFromPrefs();

        //Prepare API Call
        Call<ResponseBody> call = null;
        call = service.userUpdateTokenFCM(apiSecret, token, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 401) {
                    logOutUser();
                } else if (response.code() == 200) {
                    //Got Successfull response
                    try {

                        String responseBodyString = response.body().string();

                        ServiceObject theObject = new ServiceObject();
                        theObject.setParams(params);
                        theObject.setResult(responseBodyString);
                        JSONObject sampleObject = new JSONObject(theObject.getResult());
                        GeneralCommands.logEventObject(SharedObjects.context, "api_call", "api_request_body", uParams);


                        performUserUpdateTokenFCM(theObject, isSilent);


                    } catch (Exception e) {

//						if(listenerAddUserAction!=null) listenerAddUserAction.onDataLoaded(null);


                        String info = mContext.getResources().getString(R.string.updatetokenerror);
                        if (!isSilent) problemLoading(info, response.code());
                        e.printStackTrace();
                    }

                } else {

//					if(listenerAddUserAction!=null) listenerAddUserAction.onDataLoaded(null);

                    String message = mContext.getResources().getString(R.string.updatetokenerror);
                    // Handle the error response here
                    try {
                        ResponseBody errorBody = response.errorBody();
                        if (errorBody != null) {

                            // Parse the error response body as JSON
//							Gson gson = new Gson();
//							ErrorResponseObject errorResponse = gson.fromJson(errorBody.string(), ErrorResponseObject.class);
//
//							int errorCode = errorResponse.getError().getCode();;
//							message = errorResponse.getError().getMessage();

                            // Handle the error information

                        }
                    } catch (Exception e) {
//						if(listenerAddUserAction!=null) listenerAddUserAction.onDataLoaded(null);

                        e.printStackTrace();
                    }
                    if (!isSilent) problemLoading(message, response.code());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

//				if(listenerAddUserAction!=null) listenerAddUserAction.onDataLoaded(null);

                Logger.d("API error:", t.getMessage());
                String info = mContext.getResources().getString(R.string.updatetokenerror);
                if (!isSilent) problemLoading(info, 101);

            }
        });

    }

    public void userPushData(JSONObject uParams, boolean isSilent)
    {

        String serverUrl = USER_OTP_REQUEST_URL;


        String theLoadingMsg= mContext.getResources().getString(R.string.updatingtoken);
        String theOp="user_push_data";


        if(!isSilent)startLoading(theLoadingMsg);


        List<String> params = new ArrayList<String>();
        params.add(uParams.toString());


        ApiInterface service = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),uParams.toString());

        String token= "Bearer "+storageObject.retrieveTokenFromPrefs();
        String apiSecret=storageObject.retrieveApiSecretFromPrefs();

        //Prepare API Call
        Call<ResponseBody> call = null;
        call = service.userPushData(apiSecret,token,body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 401) {logOutUser();}
                else if (response.code() == 200) {
                    //Got Successfull response
                    try {
                        ServiceObject theObject = new ServiceObject();
                        theObject.setParams(params);
                        theObject.setResult(response.body().string());
//						theObject.setResult("{\n" +
//								"   \"success\":TRUE,\n" +
//								"   \"tempDeviceActiveTill\":\"1705925705166\"\n" +
//								"}");
                        JSONObject sampleObject = new JSONObject(theObject.getResult());
                        GeneralCommands.logEventObject(SharedObjects.context, "api_call","api_request_body",uParams);;

                        performUserPushData(theObject, isSilent);


                    } catch (Exception e) {

//						if(listenerPushData!=null) listenerPushData.onDataLoaded(0);

                        String info = mContext.getResources().getString(R.string.updateinformationerror);
                        if(!isSilent) problemLoading(info,101);
                        e.printStackTrace();
                    }

                } else {



                    String message = mContext.getResources().getString(R.string.updateinformationerror);

                    // Handle the error response here
                    try
                    {
                        ResponseBody errorBody = response.errorBody();
                        if (errorBody != null) {

                            // Parse the error response body as JSON
                            Gson gson = new Gson();
                            ErrorResponseObject errorResponse = gson.fromJson(errorBody.string(), ErrorResponseObject.class);

                            int errorCode = errorResponse.getError().getCode();;
                           // message = errorResponse.getError().getMessage();


                            // Handle the error information

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(!isSilent) problemLoading(message,response.code());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

//				if(listenerPushData!=null) listenerPushData.onDataLoaded(0);


                Log.d("API error:", t.getMessage());
                String info = mContext.getResources().getString(R.string.updateinformationerror);
                if(!isSilent) problemLoading(info,101);

            }
        });

    }

    public void userGetOTPChannels(boolean isSilent) {

        String serverUrl = USER_GET_MY_PROFILE_URL;

        String theLoadingMsg = mContext.getResources().getString(R.string.addinguserction);

        if (!isSilent) startLoading(theLoadingMsg);

        List<String> params = new ArrayList<String>();

        JSONObject uParams = new JSONObject();


        ApiInterface service = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), uParams.toString());

        String token = "Bearer " + storageObject.retrieveTokenFromPrefs();
        String apiSecret = storageObject.retrieveApiSecretFromPrefs();

        //Prepare API Call
        Call<ResponseBody> call = null;
        call = service.userGetOTPChannels(apiSecret, token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 401) {
                    logOutUser();
                } else if (response.code() == 200) {
                    //Got Successfull response
                    try {

                        String responseBodyString = response.body().string();

                        ServiceObject theObject = new ServiceObject();
                        theObject.setParams(params);
                        theObject.setResult(responseBodyString);
                        JSONObject sampleObject = new JSONObject(theObject.getResult());
                        GeneralCommands.logEventObject(SharedObjects.context, "api_call", "api_request_body", uParams);


                        performUserOTPChannels(theObject, isSilent);


                    } catch (Exception e) {

                        if (listenerUserChannels != null) listenerUserChannels.onDataLoaded(null);


                        String info = mContext.getResources().getString(R.string.adduseractionerror);
                        if (!isSilent) problemLoading(info, response.code());
                        e.printStackTrace();
                    }

                } else {

//					if(listenerAddUserAction!=null) listenerAddUserAction.onDataLoaded(null);
                    if (listenerUserChannels != null) listenerUserChannels.onDataLoaded(null);

                    String message = mContext.getResources().getString(R.string.adduseractionerror);
                    // Handle the error response here
                    try {
                        ResponseBody errorBody = response.errorBody();
                        if (errorBody != null) {

                            // Parse the error response body as JSON
//							Gson gson = new Gson();
//							ErrorResponseObject errorResponse = gson.fromJson(errorBody.string(), ErrorResponseObject.class);
//
//							int errorCode = errorResponse.getError().getCode();;
//							message = errorResponse.getError().getMessage();

                            // Handle the error information

                        }
                    } catch (Exception e) {
                        if (listenerUserChannels != null) listenerUserChannels.onDataLoaded(null);

                        e.printStackTrace();
                    }
                    if (!isSilent) problemLoading(message, response.code());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if (listenerUserChannels != null) listenerUserChannels.onDataLoaded(null);

                Logger.d("API error:", t.getMessage());
                String info = mContext.getResources().getString(R.string.adduseractionerror);
                if (!isSilent) problemLoading(info, 101);

            }
        });

    }

    public void userDownloadLoanAgreement(String loanNumber, boolean isSilent) {

        String serverUrl = USER_GET_MY_PROFILE_URL;

        String theLoadingMsg = mContext.getResources().getString(R.string.downloadingloanagreement);

        if (!isSilent) startLoading(theLoadingMsg);

        List<String> params = new ArrayList<String>();

        JSONObject uParams = new JSONObject();


        ApiInterface service = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), uParams.toString());

        String token = "Bearer " + storageObject.retrieveTokenFromPrefs();
        String apiSecret = storageObject.retrieveApiSecretFromPrefs();

        //Prepare API Call
        Call<ResponseBody> call = null;
        call = service.userDownloadLoanAgreement(apiSecret, token, loanNumber);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 401) {
                    logOutUser();
                } else if (response.code() == 200) {
                    //Got Successfull response
                    try {

                        String responseBodyString = response.body().string();

                        ServiceObject theObject = new ServiceObject();
                        theObject.setParams(params);
                        theObject.setResult(responseBodyString);
                        JSONObject sampleObject = new JSONObject(theObject.getResult());
                        GeneralCommands.logEventObject(SharedObjects.context, "api_call", "api_request_body", uParams);


                        performUserDownloadLoanAgreement(theObject, isSilent);


                    } catch (Exception e) {

//						if(listenerAddUserAction!=null) listenerAddUserAction.onDataLoaded(null);
                        storageObject.setHasLoanAgreement(false);


                        String info = mContext.getResources().getString(R.string.downloadloanagreementerror);
                        if (!isSilent) {
                            //There was a problem
                            String message = mContext.getResources().getString(R.string.downloadloanagreementerror);
                            problemLoading(message, 400);
                            errorTitle.setText(mContext.getResources().getString(R.string.unabletodownloadloanagreement));
                            btnRetryText.setText(mContext.getResources().getString(R.string.chatwithus));
                            btnRetry.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String errorMsg = mContext.getResources().getString(R.string.unabletodownloadloanagreement);
                                    openWhatsAppWithMessage(errorMsg);
                                }
                            });
                        }
                        e.printStackTrace();
                    }

                } else {

//					if(listenerAddUserAction!=null) listenerAddUserAction.onDataLoaded(null);
                    storageObject.setHasLoanAgreement(false);

                    String message = mContext.getResources().getString(R.string.downloadloanagreementerror);
                    // Handle the error response here
                    try {
                        ResponseBody errorBody = response.errorBody();
                        if (errorBody != null) {

                            // Parse the error response body as JSON
                            Gson gson = new Gson();
                            ErrorResponseObject errorResponse = gson.fromJson(errorBody.string(), ErrorResponseObject.class);

                            int errorCode = errorResponse.getError().getCode();
                            ;
//							message = errorResponse.getError().getMessage();

                            // Handle the error information

                        }
                    } catch (Exception e) {
//						if(listenerAddUserAction!=null) listenerAddUserAction.onDataLoaded(null);

                        e.printStackTrace();
                    }
                    if (!isSilent) {
                        //There was a problem
                        problemLoading(message, 1010);
                        errorTitle.setText(mContext.getResources().getString(R.string.unabletodownloadloanagreement));
                        btnRetryText.setText(mContext.getResources().getString(R.string.chatwithus));
                        btnRetry.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String errorMsg = mContext.getResources().getString(R.string.unabletodownloadloanagreement);
                                openWhatsAppWithMessage(errorMsg);
                            }
                        });
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

//				if(listenerAddUserAction!=null) listenerAddUserAction.onDataLoaded(null);
                storageObject.setHasLoanAgreement(false);

                Logger.d("API error:", t.getMessage());
                if (!isSilent) {
                    //There was a problem
                    String message = mContext.getResources().getString(R.string.downloadloanagreementerror);
                    problemLoading(message, 101);
                    errorTitle.setText(mContext.getResources().getString(R.string.unabletodownloadloanagreement));
                    btnRetryText.setText(mContext.getResources().getString(R.string.chatwithus));
                    btnRetry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String errorMsg = mContext.getResources().getString(R.string.unabletodownloadloanagreement);
                            openWhatsAppWithMessage(errorMsg);
                        }
                    });
                }

            }
        });

    }

    public void userGetPaymentProviders(boolean isSilent) {

        String serverUrl = USER_GET_MY_PROFILE_URL;

        String theLoadingMsg = mContext.getResources().getString(R.string.gettingpayments);

        if (!isSilent) startLoading(theLoadingMsg);

        List<String> params = new ArrayList<String>();

        JSONObject uParams = new JSONObject();


        ApiInterface service = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), uParams.toString());

        String token = "Bearer " + storageObject.retrieveTokenFromPrefs();
        String apiSecret = storageObject.retrieveApiSecretFromPrefs();

        //Prepare API Call
        Call<ResponseBody> call = null;
        call = service.userGetPaymentProviders(apiSecret, token);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 401) {
                    logOutUser();
                } else if (response.code() == 200) {
                    //Got Successfull response
                    try {

                        String responseBodyString = response.body().string();

                        ServiceObject theObject = new ServiceObject();
                        theObject.setParams(params);
                        theObject.setResult(responseBodyString);
                        JSONObject sampleObject = new JSONObject(theObject.getResult());
                        GeneralCommands.logEventObject(SharedObjects.context, "api_call", "api_request_body", uParams);


                        performUserPaymentProviders(theObject, isSilent);


                    } catch (Exception e) {

//						if(listenerAddUserAction!=null) listenerAddUserAction.onDataLoaded(null);


                        String info = mContext.getResources().getString(R.string.getpaymentserror);
                        if (!isSilent) problemLoading(info, response.code());
                        e.printStackTrace();
                    }

                } else {

//					if(listenerAddUserAction!=null) listenerAddUserAction.onDataLoaded(null);

                    String message = mContext.getResources().getString(R.string.getpaymentserror);
                    // Handle the error response here
                    try {
                        ResponseBody errorBody = response.errorBody();
                        if (errorBody != null) {

                            // Parse the error response body as JSON
//							Gson gson = new Gson();
//							ErrorResponseObject errorResponse = gson.fromJson(errorBody.string(), ErrorResponseObject.class);
//
//							int errorCode = errorResponse.getError().getCode();;
//							message = errorResponse.getError().getMessage();

                            // Handle the error information

                        }
                    } catch (Exception e) {
//						if(listenerAddUserAction!=null) listenerAddUserAction.onDataLoaded(null);

                        e.printStackTrace();
                    }
                    if (!isSilent) problemLoading(message, response.code());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

//				if(listenerAddUserAction!=null) listenerAddUserAction.onDataLoaded(null);

                Logger.d("API error:", t.getMessage());
                String info = mContext.getResources().getString(R.string.getpaymentserror);
                if (!isSilent) problemLoading(info, 101);

            }
        });

    }

    public void performUserPushData(ServiceObject theObject, boolean isSilent) {
        try
        {
            JSONObject sampleObject = new JSONObject(theObject.getResult());
            String status = sampleObject.getString("success");

            if(status.equals("success"))
            {
                //Handle Success
                String message = mContext.getResources().getString(R.string.updateinformationtitle);
                String title = mContext.getResources().getString(R.string.updateinformationsuccess);
                if(!isSilent) successLoading(message,title);

//				if(listenerPushData!=null) listenerPushData.onDataLoaded(true);
//				Toast.makeText(parentActivity, "OzeSuk", Toast.LENGTH_SHORT).show();

            }

            else
            {
                String error=sampleObject.getString("error");
                //There was a problem
                String message = error;

                if(!isSilent)  problemLoading(message,400);

//				if(listenerPushData!=null) listenerPushData.onDataLoaded(0);
//				Toast.makeText(parentActivity, "OzeFe", Toast.LENGTH_SHORT).show();


            }



        }
        catch (Exception e)
        {
//			if(listenerPushData!=null) listenerPushData.onDataLoaded(0);

            String info = mContext.getResources().getString(R.string.updateinformationerror);
            if(!isSilent)  problemLoading(info,101);
            e.printStackTrace();


        }
    }

    public void userTemporaryUnlock(String loanNumber, boolean isSilent) {

        String serverUrl = USER_OTP_REQUEST_URL;

//		loanNumber="AF000001717";

        String theLoadingMsg = mContext.getResources().getString(R.string.requestingunlock);
        String theOp = "user_temporary _unlock";


        if (!isSilent) startLoading(theLoadingMsg);


        List<String> params = new ArrayList<String>();
        params.add(loanNumber);


        JSONObject uParams = new JSONObject();

        try {
            uParams.put("loanNumber", loanNumber);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ApiInterface service = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), uParams.toString());

        String token = "Bearer " + storageObject.retrieveTokenFromPrefs();
        String apiSecret = storageObject.retrieveApiSecretFromPrefs();

        //Prepare API Call
        Call<ResponseBody> call = null;
        call = service.userTemporaryUnlock(apiSecret, token, loanNumber);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 401) {
                    logOutUser();
                } else if (response.code() == 200) {
                    //Got Successfull response
                    try {
                        ServiceObject theObject = new ServiceObject();
                        theObject.setParams(params);
                        theObject.setResult(response.body().string());
//						theObject.setResult("{\n" +
//								"   \"success\":TRUE,\n" +
//								"   \"tempDeviceActiveTill\":\"1705925705166\"\n" +
//								"}");

                        JSONObject sampleObject = new JSONObject(theObject.getResult());
                        GeneralCommands.logEventObject(SharedObjects.context, "api_call", "api_request_body", uParams);
                        ;

                        performUserTemporaryUnlock(theObject, false);


                    } catch (Exception e) {


                        if (listenerTemporaryUnlock != null)
                            listenerTemporaryUnlock.onDataLoaded(0);


                        String info = mContext.getResources().getString(R.string.requestingunlockerror);
                        problemLoading(info, response.code());
                        e.printStackTrace();
                    }

                } else {


                    int rem = storageObject.retrieveTempUnlockConfigFromPrefs() - storageObject.retrieveTemporaryUnlockCountFromPrefs();


                    String message = mContext.getResources().getString(R.string.requestingunlockerror);

                    // Handle the error response here
                    try {
                        ResponseBody errorBody = response.errorBody();
                        if (errorBody != null) {

                            // Parse the error response body as JSON
                            Gson gson = new Gson();
                            ErrorResponseObject errorResponse = gson.fromJson(errorBody.string(), ErrorResponseObject.class);

                            int errorCode = errorResponse.getError().getCode();
                            ;
//                            message = errorResponse.getError().getMessage();


                            if (message.toLowerCase().contains("maximum")) {
                                message = mContext.getResources().getString(R.string.maxunlockattempts);
                                //Max Attempts Allowed Reached
                                if (listenerTemporaryUnlock != null)
                                    listenerTemporaryUnlock.onDataLoaded(-1);


                            } else {

                                if (listenerTemporaryUnlock != null)
                                    listenerTemporaryUnlock.onDataLoaded(0);

                            }

                            // Handle the error information

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    problemLoading(message, response.code());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if (listenerTemporaryUnlock != null) listenerTemporaryUnlock.onDataLoaded(0);


                Logger.d("API error:", t.getMessage());
                String info = mContext.getResources().getString(R.string.requestingunlockerror);
                if (!isSilent) problemLoading(info, 101);

            }
        });

    }

    public void userGetLoanDetails(String loanNumber, boolean isSilent) {

        String serverUrl = USER_GET_LOAN_DETAILS_URL;

        String theLoadingMsg = mContext.getResources().getString(R.string.gettingloandetails);
        String theOp = "user_get_loan_details";

        if (!isSilent) startLoading(theLoadingMsg);

        List<String> params = new ArrayList<String>();
        params.add(loanNumber);

        JSONObject uParams = new JSONObject();

        try {
            uParams.put("loanNumber", loanNumber);


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ApiInterface service = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), uParams.toString());

        String token = "Bearer " + storageObject.retrieveTokenFromPrefs();
        String apiSecret = storageObject.retrieveApiSecretFromPrefs();

        //Prepare API Call
        Call<ResponseBody> call = null;
        call = service.userGetLoanDetailsSingle(apiSecret, token, loanNumber);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 401) {
                    logOutUser();
                } else if (response.code() == 200) {
                    //Got Successfull response
                    try {
                        ServiceObject theObject = new ServiceObject();
                        theObject.setParams(params);
                        theObject.setResult(response.body().string());
                        JSONObject sampleObject = new JSONObject(theObject.getResult());
                        GeneralCommands.logEventObject(SharedObjects.context, "api_call", "api_request_body", uParams);
                        ;

                        performUserGetLoanDetails(theObject, isSilent);


                    } catch (Exception e) {

                        if (listenerLoanDetails != null) listenerLoanDetails.onDataLoaded(null);


                        String info = mContext.getResources().getString(R.string.getloandetailsserror);
                        if (!isSilent) problemLoading(info, response.code());
                        e.printStackTrace();
                    }

                } else {

                    if (listenerLoanDetails != null) listenerLoanDetails.onDataLoaded(null);

                    String message = mContext.getResources().getString(R.string.getloandetailsserror);
                    // Handle the error response here
                    try {
                        ResponseBody errorBody = response.errorBody();
                        if (errorBody != null) {

                            // Parse the error response body as JSON
                            Gson gson = new Gson();
                            ErrorResponseObject errorResponse = gson.fromJson(errorBody.string(), ErrorResponseObject.class);

                            int errorCode = errorResponse.getError().getCode();
                            ;
//                            message = errorResponse.getError().getMessage();

                            // Handle the error information

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!isSilent) problemLoading(message, response.code());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if (listenerLoanDetails != null) listenerLoanDetails.onDataLoaded(null);

                Logger.d("API error:", t.getMessage());
                String info = mContext.getResources().getString(R.string.getloandetailsserror);
                if (!isSilent) problemLoading(info, 101);

            }
        });

    }

    public void userGetPayments(String from, String to, String offset, boolean isSilent) {

        String serverUrl = USER_GET_PAYMENTS_URL;

        String theLoadingMsg = mContext.getResources().getString(R.string.gettingpayments);
        String theOp = "user_get_payments";

        if (!isSilent) startLoading(theLoadingMsg);

        List<String> params = new ArrayList<String>();
        params.add(from);
        params.add(to);

        JSONObject uParams = new JSONObject();

        try {
            if (!from.equals("")) uParams.put("from", from);
            if (!to.equals("")) uParams.put("to", to);


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ApiInterface service = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), uParams.toString());

        String token = "Bearer " + storageObject.retrieveTokenFromPrefs();
        String apiSecret = storageObject.retrieveApiSecretFromPrefs();

        //Prepare API Call
        Call<ResponseBody> call = null;
        call = service.userGetPayments(apiSecret, token, offset);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 401) {
                    logOutUser();
                } else if (response.code() == 200) {
                    //Got Successfull response
                    try {
                        ServiceObject theObject = new ServiceObject();
                        theObject.setParams(params);
                        theObject.setResult(response.body().string());
                        JSONObject sampleObject = new JSONObject(theObject.getResult());
                        GeneralCommands.logEventObject(SharedObjects.context, "api_call", "api_request_body", uParams);
                        ;

                        performUserGetPayments(theObject, isSilent);


                    } catch (Exception e) {

                        if (listenerPayments != null) listenerPayments.onDataLoaded(null);


                        String info = mContext.getResources().getString(R.string.getpaymentserror);
                        if (!isSilent) problemLoading(info, response.code());
                        e.printStackTrace();
                    }

                } else {

                    if (listenerLoanDetails != null) listenerLoanDetails.onDataLoaded(null);

                    String message = mContext.getResources().getString(R.string.getloandetailsserror);
                    // Handle the error response here
                    try {
                        ResponseBody errorBody = response.errorBody();
                        if (errorBody != null) {

                            // Parse the error response body as JSON
                            Gson gson = new Gson();
                            ErrorResponseObject errorResponse = gson.fromJson(errorBody.string(), ErrorResponseObject.class);

                            int errorCode = errorResponse.getError().getCode();
                            ;
//                            message = errorResponse.getError().getMessage();

                            // Handle the error information

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!isSilent) problemLoading(message, response.code());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if (listenerLoanDetails != null) listenerLoanDetails.onDataLoaded(null);

                Logger.d("API error:", t.getMessage());
                String info = mContext.getResources().getString(R.string.getloandetailsserror);
                if (!isSilent) problemLoading(info, 101);

            }
        });

    }

    public void initiateSTKPayment(String loanNumber, String currencyCode, int totalAmount, String provider, String phoneNumber, DialogPlus dialog, boolean isSilent) {

        dialogPlus = dialog;

        String serverUrl = USER_INITIATE_STK_PAYMENT_URL;

        String theLoadingMsg = mContext.getResources().getString(R.string.initiatingpayment);
        String theOp = "user_initiate_payment_stk";

        if (!isSilent) startLoading(theLoadingMsg);

        List<String> params = new ArrayList<String>();
        params.add(phoneNumber);
        params.add(loanNumber);
        params.add("" + totalAmount);

        JSONObject uParams = new JSONObject();


        try {
            uParams.put("loanNumber", loanNumber);
            uParams.put("currency", currencyCode);
            uParams.put("amount", totalAmount);
            uParams.put("providerId", provider);
            uParams.put("targetPhone", phoneNumber);

            if (!phoneNumber.equals(storageObject.retrieveUserPhoneNumberFromPrefs())) {

                uParams.put("targetPhone", phoneNumber);

            }


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ApiInterface service = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), uParams.toString());

        String token = "Bearer " + storageObject.retrieveTokenFromPrefs();
        String apiSecret = storageObject.retrieveApiSecretFromPrefs();

        //Prepare API Call
        Call<ResponseBody> call = null;
        call = service.userInitiatePayment(apiSecret, token, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 401) {
                    logOutUser();
                } else if (response.code() == 200) {
                    //Got Successfull response
                    try {
                        ServiceObject theObject = new ServiceObject();
                        theObject.setParams(params);
                        theObject.setResult(response.body().string());
                        JSONObject sampleObject = new JSONObject(theObject.getResult());
                        GeneralCommands.logEventObject(SharedObjects.context, "api_call", "api_request_body", uParams);


                        performUserInitiatePaymentStk(theObject, false);


                    } catch (Exception e) {

                        if (listenerPaymentStk != null) listenerPaymentStk.onDataLoaded(false);


                        String info = mContext.getResources().getString(R.string.initiatepaymenterror);
                        problemLoading(info, response.code());
                        e.printStackTrace();
                    }

                } else {

                    if (listenerPaymentStk != null) listenerPaymentStk.onDataLoaded(false);

                    String message = mContext.getResources().getString(R.string.initiatepaymenterror);


                    // Handle the error response here
                    try {
                        ResponseBody errorBody = response.errorBody();
                        if (errorBody != null) {

                            // Parse the error response body as JSON
                            Gson gson = new Gson();
                            ErrorResponseObject errorResponse = gson.fromJson(errorBody.string(), ErrorResponseObject.class);

                            int errorCode = errorResponse.getError().getCode();
                            ;
//                            message = errorResponse.getError().getMessage();

                            if (!StringUtils.isStringEmpty(message) && message.toLowerCase().contains("server error")) {
                                message = mContext.getResources().getString(R.string.initiatepaymenterror);
                            }

                            if (message.contains("Maximum")) {
                                message = mContext.getResources().getString(R.string.maxpayforanother);
                            }

                            // Handle the error information

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    problemLoading(message, response.code());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if (listenerPaymentStk != null) listenerPaymentStk.onDataLoaded(false);


                Logger.d("API error:", t.getMessage());
                String info = mContext.getResources().getString(R.string.initiatepaymenterror);
                problemLoading(info, 101);

            }
        });


    }

    public void confirmSTKPayment(String transactionNumber, boolean isSilent) {

        String serverUrl = USER_POLL_STK_PAYMENT_URL;

        String theLoadingMsg = mContext.getResources().getString(R.string.confirmingpayment);
        String theOp = "user_confirm_payment_stk";


        if (!isSilent) startLoading(theLoadingMsg);

        List<String> params = new ArrayList<String>();
        params.add(transactionNumber);
        //params.add(amount);


        JSONObject uParams = new JSONObject();

        try {
            uParams.put("transactionNumber", transactionNumber);


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ApiInterface service = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), uParams.toString());

        String token = "Bearer " + storageObject.retrieveTokenFromPrefs();
        String apiSecret = storageObject.retrieveApiSecretFromPrefs();

        //Prepare API Call
        Call<ResponseBody> call = null;
        call = service.userConfirmPayment(apiSecret, token, transactionNumber);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 401) {
                    logOutUser();
                } else if (response.code() == 200) {
                    //Got Successfull response
                    try {
                        ServiceObject theObject = new ServiceObject();
                        theObject.setParams(params);
                        theObject.setResult(response.body().string());
                        JSONObject sampleObject = new JSONObject(theObject.getResult());
                        GeneralCommands.logEventObject(SharedObjects.context, "api_call", "api_request_body", uParams);
                        ;

                        performUserConfirmPaymentStk(theObject, isSilent);


                    } catch (Exception e) {

                        if (listenerConfirmPaymentStk != null)
                            listenerConfirmPaymentStk.onDataLoaded(null, null);


                        String info = mContext.getResources().getString(R.string.confirmpaymenterror);
                        if (!isSilent) problemLoading(info, response.code());
                        e.printStackTrace();
                    }

                } else {

                    if (listenerConfirmPaymentStk != null)
                        listenerConfirmPaymentStk.onDataLoaded(null, null);

                    String message = mContext.getResources().getString(R.string.confirmpaymenterror);
                    // Handle the error response here
                    try {
                        ResponseBody errorBody = response.errorBody();
                        if (errorBody != null) {

                            // Parse the error response body as JSON
                            Gson gson = new Gson();
                            ErrorResponseObject errorResponse = gson.fromJson(errorBody.string(), ErrorResponseObject.class);

                            int errorCode = errorResponse.getError().getCode();
                            ;
//                            message = errorResponse.getError().getMessage();

                            // Handle the error information

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!isSilent) problemLoading(message, response.code());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if (listenerConfirmPaymentStk != null)
                    listenerConfirmPaymentStk.onDataLoaded(null, null);

                Logger.d("API error:", t.getMessage());
                String info = mContext.getResources().getString(R.string.confirmpaymenterror);
                if (!isSilent) problemLoading(info, 101);

            }
        });


    }


    public void userSubmitFeedback(String category, String comment, boolean isSilent) {

        String serverUrl = USER_GET_LOAN_DETAILS_URL;

        String theLoadingMsg = mContext.getResources().getString(R.string.submittingfeedback);
        String theOp = "user_get_loan_details";

        if (!isSilent) startLoading(theLoadingMsg);

        List<String> params = new ArrayList<String>();
        params.add(category);

        JSONObject uParams = new JSONObject();

        try {
            uParams.put("category", category);
            uParams.put("feedback", comment);


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ApiInterface service = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), uParams.toString());

        String token = "Bearer " + storageObject.retrieveTokenFromPrefs();
        String apiSecret = storageObject.retrieveApiSecretFromPrefs();

        //Prepare API Call
        Call<ResponseBody> call = null;
        call = service.userSubmitFeedback(apiSecret, token, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 401) {
                    logOutUser();
                } else if (response.code() == 201) {
                    //Got Successfull response
                    try {
                        ServiceObject theObject = new ServiceObject();
                        theObject.setParams(params);
                        theObject.setResult("{}");
                        JSONObject sampleObject = new JSONObject(theObject.getResult());
                        GeneralCommands.logEventObject(SharedObjects.context, "api_call", "api_request_body", uParams);
                        ;

                        performUserSubmitFeedback(theObject, isSilent);


                    } catch (Exception e) {

                        if (listenerSubmitFeedback != null)
                            listenerSubmitFeedback.onDataLoaded(false);


                        String info = mContext.getResources().getString(R.string.submitfeedbackserror);
                        if (!isSilent) problemLoading(info, response.code());
                        e.printStackTrace();
                    }

                } else {

                    if (listenerSubmitFeedback != null) listenerSubmitFeedback.onDataLoaded(false);

                    String message = mContext.getResources().getString(R.string.submitfeedbackserror);
                    // Handle the error response here
                    try {
                        ResponseBody errorBody = response.errorBody();
                        if (errorBody != null) {

                            // Parse the error response body as JSON
                            Gson gson = new Gson();
                            ErrorResponseObject errorResponse = gson.fromJson(errorBody.string(), ErrorResponseObject.class);

                            int errorCode = errorResponse.getError().getCode();
                            ;
//                            message = errorResponse.getError().getMessage();

                            // Handle the error information

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!isSilent) problemLoading(message, response.code());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                if (listenerSubmitFeedback != null) listenerSubmitFeedback.onDataLoaded(false);

                Logger.d("API error:", t.getMessage());
                String info = mContext.getResources().getString(R.string.submitfeedbackserror);
                if (!isSilent) problemLoading(info, 101);

            }
        });

    }


    public void userSubmitPaymentRating(String paymentRequestId, String rating, boolean isSilent) {

        String serverUrl = USER_GET_LOAN_DETAILS_URL;

        String theLoadingMsg = mContext.getResources().getString(R.string.submittingfeedback);
        String theOp = "user_get_loan_details";

        if (!isSilent) startLoading(theLoadingMsg);

        List<String> params = new ArrayList<String>();
        params.add(rating);

        JSONObject uParams = new JSONObject();

        try {
            uParams.put("rating", rating);
            uParams.put("paymentRequest", paymentRequestId);


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        ApiInterface service = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), uParams.toString());

        String token = "Bearer " + storageObject.retrieveTokenFromPrefs();
        String apiSecret = storageObject.retrieveApiSecretFromPrefs();

        //Prepare API Call
        Call<ResponseBody> call = null;
        call = service.userSubmitPaymentRating(apiSecret, token, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 401) {
                    logOutUser();
                } else if (response.code() == 200) {
                    //Got Successfull response
                    try {
                        ServiceObject theObject = new ServiceObject();
                        theObject.setParams(params);
                        theObject.setResult("{}");

                        JSONObject sampleObject = new JSONObject(theObject.getResult());
                        GeneralCommands.logEventObject(SharedObjects.context, "api_call", "api_request_body", uParams);
                        ;

                        performUserSubmitPaymentRating(theObject, isSilent);


                    } catch (Exception e) {


                        String info = mContext.getResources().getString(R.string.submitfeedbackserror);
                        if (!isSilent) problemLoading(info, response.code());
                        e.printStackTrace();
                    }

                } else {


                    String message = mContext.getResources().getString(R.string.submitfeedbackserror);
                    // Handle the error response here
                    try {
                        ResponseBody errorBody = response.errorBody();
                        if (errorBody != null) {

                            // Parse the error response body as JSON
                            Gson gson = new Gson();
                            ErrorResponseObject errorResponse = gson.fromJson(errorBody.string(), ErrorResponseObject.class);

                            int errorCode = errorResponse.getError().getCode();
                            ;
//                            message = errorResponse.getError().getMessage();

                            // Handle the error information

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!isSilent) problemLoading(message, response.code());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                Logger.d("API error:", t.getMessage());
                String info = mContext.getResources().getString(R.string.submitfeedbackserror);
                if (!isSilent) problemLoading(info, 101);

            }
        });

    }

    public void userForgotPassword(final String userEmail, DialogPlus dialog) {

        dialogPlus = dialog;
        String serverUrl = USER_FORGOT_PASSWORD_URL;

        String theLoadingMsg = mContext.getResources().getString(R.string.resettingpassword);
        String theOp = "user_forgot_password";


        List<String> params = new ArrayList<String>();
        params.add(userEmail);


        JSONObject uParams = new JSONObject();

        try {

            uParams.put("email", userEmail);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

//		(new LongOperation(serverUrl,params,uParams,theLoadingMsg, theOp, ServiceHandler.POST,false)).execute();


    }

    public void userConfirmationEmailResend(final String userEmail, boolean isSilent) {

        String serverUrl = USER_EMAIL_CONFIRMATION_RESEND_URL;

        String theLoadingMsg = mContext.getResources().getString(R.string.requestingotp);
        String theOp = "user_confirmation_email_resend";


        List<String> params = new ArrayList<String>();
        params.add(userEmail);


        JSONObject uParams = new JSONObject();

        try {
            uParams.put("email", userEmail);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

//		(new LongOperation(serverUrl,params,uParams,theLoadingMsg, theOp, ServiceHandler.POST,isSilent)).execute();

    }


    public void submitDocuments(String client_id, String type, Uri mDocumentUriOne, Uri mDocumentUriTwo, Uri mDocumentUriThree, boolean isSilent) {

        String serverUrl = USER_UPLOAD_DOCUMENT_URL;

        String theLoadingMsg = mContext.getResources().getString(R.string.uploadingdocument);
        String theOp = "upload_document";


        final List<String> params = new ArrayList<String>();
        params.add(client_id);
        params.add(type);

        startLoading(theLoadingMsg);

        ApiInterface service = RetrofitInstance.getRetrofitInstance().create(ApiInterface.class);
        String cookie = storageObject.retrieveTokenCookieStringFromPrefs();

        RequestBody customer_id = RequestBody.create(MediaType.parse("text/plain"), client_id);
        RequestBody request_type = RequestBody.create(MediaType.parse("text/plain"), type);

        List<File> files = new ArrayList<>();

        if (mDocumentUriOne != null) {

            String selectedFilePath = getFileName(mDocumentUriOne);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                selectedFilePath = FilePath.getPath(mContext, mDocumentUriOne);
            }
            File file = new File(selectedFilePath);
            files.add(file);
        }

        if (mDocumentUriTwo != null) {
            String selectedFilePath = getFileName(mDocumentUriTwo);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                selectedFilePath = FilePath.getPath(mContext, mDocumentUriTwo);
            }
            File file = new File(selectedFilePath);
            files.add(file);
        }

        if (mDocumentUriThree != null) {
            String selectedFilePath = getFileName(mDocumentUriThree);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                selectedFilePath = FilePath.getPath(mContext, mDocumentUriThree);
            }
            File file = new File(selectedFilePath);
            files.add(file);
        }

        //Prepare API Call
        Call<ResponseBody> call = null;
        if (files.size() == 1) {
            MultipartBody.Part body = prepareFilePart("files[]", mDocumentUriOne);
            call = service.uploadFile(cookie, body, customer_id, request_type);

        } else if (files.size() == 2) {
            MultipartBody.Part body = prepareFilePart("files[]", mDocumentUriOne);
            MultipartBody.Part body2 = prepareFilePart("files[]", mDocumentUriTwo);
            call = service.uploadTwoFiles(cookie, body, body2, customer_id, request_type);

        } else if (files.size() == 3) {
            MultipartBody.Part body = prepareFilePart("files[]", mDocumentUriOne);
            MultipartBody.Part body2 = prepareFilePart("files[]", mDocumentUriTwo);
            MultipartBody.Part body3 = prepareFilePart("files[]", mDocumentUriThree);

            call = service.uploadThreeFiles(cookie, body, body2, body3, customer_id, request_type);

        }


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    //Got successfull response

                    try {
                        ServiceObject theObject = new ServiceObject();
                        theObject.setParams(params);
                        theObject.setResult(response.body().string());
                        JSONObject sampleObject = new JSONObject(theObject.getResult());
                        GeneralCommands.logEvent(SharedObjects.context, "api_call", serverUrl + ":-Status is " + response.code() + ":body");


                        boolean isSuccess = sampleObject.getBoolean("success");
                        if (isSuccess) {
                            finishLoading();

                            String message = mContext.getResources().getString(R.string.uploaddocumentsuccess);
                            String title = mContext.getResources().getString(R.string.successful);

                            successLoading(message, title);

                            btnSuccessText.setText(mContext.getResources().getString(R.string.awesome));
                            resultSuccess.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    goToMainPage();
                                }
                            });
                            btnSuccess.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    goToMainPage();
                                }
                            });


                        } else {
                            String error = sampleObject.getString("details");
                            //There was a problem
                            String message = error;
                            problemLoading(message, response.code());

                        }


                    } catch (Exception e) {

                        String info = mContext.getResources().getString(R.string.uploaddocumenterror);
                        problemLoading(info, response.code());
                        e.printStackTrace();
                    }

                } else {

                    String info = mContext.getResources().getString(R.string.uploaddocumenterror);
                    problemLoading(info, response.code());

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Upload error:", t.getMessage());
                String info = mContext.getResources().getString(R.string.uploaddocumenterror);
                if (!isSilent) problemLoading(info, 101);

            }
        });

    }


    public void userChangePassword(final String userPhoneNumber, final String userPassword, boolean isSilent) {

        String serverUrl = USER_PASSWORD_CHANGE_URL;

        String theLoadingMsg = mContext.getResources().getString(R.string.requestingotp);
        String theOp = "user_change_password";


        List<String> params = new ArrayList<String>();
        params.add(userPhoneNumber);
        params.add(userPassword);


        JSONObject uParams = new JSONObject();

        try {
            uParams.put("phone_number", userPhoneNumber);
            uParams.put("password", userPassword);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

//		(new LongOperation(serverUrl,params,uParams,theLoadingMsg, theOp, ServiceHandler.POST,isSilent)).execute();

    }


    public void uploadProfilePic(Uri imageUri, ImageView theUserImage, boolean isSilent) {
        userImage = theUserImage;
        final long totalSize = 0;


        String serverUrl = USER_UPLOAD_PROFILE_PIC_URL;

        String theLoadingMsg = mContext.getResources().getString(R.string.uploadingprofilepicimage);
        String theOp = "upload_profile_pic";


        List<String> params = new ArrayList<String>();


        AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                new ProgressListener() {

                    @Override
                    public void transferred(long num) {
                        int progress = (int) ((num / (float) totalSize) * 100);

                    }
                });


        try {

            Bitmap b = BitmapFactory.decodeFile(imageUri.getPath());
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            b.compress(CompressFormat.JPEG, 100, bao);

            ByteArrayBody body = new ByteArrayBody(bao.toByteArray(), "image/jpeg", "photo.jpg");
            // Adding file data to http body
            entity.addPart("img[0]", body);
            entity.addPart("img[1]", body);
            entity.addPart("img[2]", body);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


    public void getMyProfile(boolean isSilent) {

        String serverUrl = USER_GET_MY_PROFILE_URL;

        String theLoadingMsg = mContext.getResources().getString(R.string.gettingmyprofile);
        String theOp = "get_my_profile";


        List<String> params = new ArrayList<String>();


        JSONObject uParams = new JSONObject();

        try {

            uParams.put("", "");


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

//		   (new LongOperation(serverUrl,params,uParams,theLoadingMsg, theOp, ServiceHandler.POST,isSilent)).execute();


    }

    public void editMyProfile(String name, String email, String phone, boolean isSilent) {

        String serverUrl = USER_EDIT_PROFILE_URL;

        String theLoadingMsg = mContext.getResources().getString(R.string.editingmyprofile);
        String theOp = "edit_my_profile";


        List<String> params = new ArrayList<String>();
        params.add(name);
        params.add(phone);


        JSONObject uParams = new JSONObject();

        try {

            uParams.put("name", name);
            uParams.put("email", email);
            uParams.put("phone", phone);


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

//		   (new LongOperation(serverUrl,params,uParams,theLoadingMsg, theOp, ServiceHandler.POST,isSilent)).execute();


    }


    public void performUserVerifyToken(ServiceObject theObject) {
        try {
            JSONObject sampleObject = new JSONObject(theObject.getResult());

            Gson gson = new Gson();
            String result = sampleObject.toString();
            String status = sampleObject.getString("access_token");


            if (!status.equals("")) {
                //Token is there
                final String token = sampleObject.getString("access_token");
                storageObject.saveUserVerifyTokenToPrefs(token);


            } else {
                JSONArray errors = sampleObject.getJSONArray("errors");

                //There was a problem
                if (errors.length() > 0) {

                    JSONObject errorObj = (JSONObject) errors.get(0);
                    Iterator<String> keys = errorObj.keys();
                    String error = "";
                    while (keys.hasNext()) {
                        String key = keys.next();
//									if (errorObj.get(key) instanceof JSONObject) {
//										// do something with jsonObject here
//									}

                        JSONArray arr = (JSONArray) errorObj.getJSONArray(key);
                        if (arr.length() > 0) {
                            error = error + key + " " + arr.get(0) + "\n";
                        }

                    }

                    String message = error;
                    problemLoading(message, 101);
                } else {
                    String info = mContext.getResources().getString(R.string.verifyidentityerror);
                    problemLoading(info, 101);

                }

            }


        } catch (Exception e) {
            String info = mContext.getResources().getString(R.string.verifyidentityerror);
            problemLoading(info, 101);
            e.printStackTrace();
        }
    }


    public void performUserVerifyIdentity(ServiceObject theObject) {
        try {
            JSONObject sampleObject = new JSONObject(theObject.getResult());

            Gson gson = new Gson();
            String result = sampleObject.toString();
            boolean status = sampleObject.getBoolean("success");

            if (status) {
                String message = mContext.getResources().getString(R.string.verifyidentitysuccess);
                String title = mContext.getResources().getString(R.string.verifyidentitysuccesstitle);
                successLoading(message, title);

                resultSuccess.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });

                //goToRegisterPageTwo();


            } else {
                JSONArray errors = sampleObject.getJSONArray("errors");

                //There was a problem
                if (errors.length() > 0) {

                    String error = "";

                    for (int i = 0; i < errors.length(); i++) {
                        error = error + errors.get(i) + "\n";

                    }


                    String message = error;
                    problemLoading(message, 400);
                } else {
                    String info = mContext.getResources().getString(R.string.verifyidentityerror);
                    problemLoading(info, 101);

                }

            }


        } catch (Exception e) {
            String info = mContext.getResources().getString(R.string.verifyidentityerror);
            problemLoading(info, 101);
            e.printStackTrace();
        }
    }

    public void performUserLogin(ServiceObject theObject) {
        final String email = theObject.getParams().get(0);
        try {
            JSONObject sampleObject = new JSONObject(theObject.getResult());

            Gson gson = new Gson();
            String result = sampleObject.toString();

            LoanAccountsObject accounts = gson.fromJson(result, LoanAccountsObject.class);
            if (accounts != null) {
                ArrayList<LoanListItemObject> loans = (ArrayList<LoanListItemObject>) accounts.getLoanList();


                //successLoading(message,title);

                String deviceId = storageObject.retrieveDeviceIDFromPrefs();
                String token = storageObject.retrieveTokenFromPrefs();
                String phoneNumber = theObject.getParams().get(0);

                //Handle Success
                User user = new User();
                user.setId(deviceId);
                user.setName(deviceId);
                user.setEmail(phoneNumber);
                user.setPhone_number(phoneNumber);
                user.setPhoneNumber(phoneNumber);

                String message = mContext.getResources().getString(R.string.loginsuccess);
                String title = mContext.getResources().getString(R.string.loginsuccesstitle);
                //successLoading(message,title);
                Toast.makeText(parentActivity, title, Toast.LENGTH_SHORT).show();


                storageObject.saveUserKeyToPrefs("" + user.getId());
                storageObject.saveUserObjectToPrefs(user);
                storageObject.saveTokenToPrefs(token);
                storageObject.saveUserFirstNameToPrefs(user.getFirstName());
                storageObject.saveUserLastNameToPrefs(user.getLastName());


                storageObject.saveUserNameToPrefs(user.getName());


                storageObject.saveUserEmailToPrefs(user.getEmail());
                storageObject.saveUserPhoneNumberToPrefs(user.getPhone_number());
                storageObject.setUserRegistered(true);

                storageObject.saveLoanListItemObjectObjectsToPrefs(loans);

                User fireUser = storageObject.retrieveUserObjectFromPrefs();

                mFirebaseRefUsers = FirebaseDatabase.getInstance().getReference().child("users");
                mFirebaseRefUsers.child("" + fireUser.getId()).setValue(fireUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        String userKey = "" + fireUser.getId();
                        storageObject.saveUserKeyToPrefs(userKey);
                        mFirebaseRefUsers.child(userKey).child("id").setValue(fireUser.getId());

                    }
                });


                mAuth.signInAnonymously().addOnCompleteListener(parentActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Logger.d("ZeFirebaseUser", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Logger.d("ZeFireUser", "createUserWithEmail:failure", task.getException());

                        }

                        // ...
                    }
                });

                if (listenerLoanAccounts != null) listenerLoanAccounts.onDataLoaded(loans);

                finishLoading();
                goToMainPage();

                //Log User Action and Update Background Prefs
                addUserAction("screenUnlock", true);
                userUpdateTokenFCM(storageObject.retrieveUserFCMTokenFromPrefs(), true);
                userEditProfile(true);

            } else {
                String err = sampleObject.getString("errors");

                //There was a problem
                if (!StringUtils.isStringEmpty(err)) {
                    String message = err;
                    problemLoading(message, 400);
                } else {
                    String info = mContext.getResources().getString(R.string.loginerror);
                    problemLoading(info, 101);

                }


            }


        } catch (Exception e) {
            String info = mContext.getResources().getString(R.string.loginerror);
            problemLoading(info, 101);
            e.printStackTrace();
        }
    }

    public void performUserRegister(ServiceObject theObject) {
        try {
            JSONObject sampleObject = new JSONObject(theObject.getResult());
            String phoneNumber = theObject.getParams().get(0);

            Gson gson = new Gson();
            String result = sampleObject.toString();
            String deviceId = sampleObject.getString("deviceId");

            if (!deviceId.equals("")) {
                //Handle Success
                User user = new User();
                user.setId(deviceId);
                user.setName(deviceId);
                user.setEmail(phoneNumber);
                user.setPhone_number(phoneNumber);
                user.setPhoneNumber(phoneNumber);

                String message = mContext.getResources().getString(R.string.loginsuccess);
                String title = mContext.getResources().getString(R.string.loginsuccesstitle);
                successLoading(message, title);

                String token = theObject.getParams().get(2);//Save Token


                storageObject.saveUserKeyToPrefs("" + user.getId());
                storageObject.saveUserObjectToPrefs(user);
                storageObject.saveTokenToPrefs(token);
                storageObject.saveUserNameToPrefs(user.getName());
                storageObject.saveUserEmailToPrefs(user.getEmail());
                storageObject.saveUserPhoneNumberToPrefs(user.getPhone_number());

                storageObject.setUserRegistered(true);

                User fireUser = storageObject.retrieveUserObjectFromPrefs();

                mFirebaseRefUsers = FirebaseDatabase.getInstance().getReference().child("users");
                mFirebaseRefUsers.child("" + fireUser.getId()).setValue(fireUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        String userKey = "" + fireUser.getId();
                        storageObject.saveUserKeyToPrefs(userKey);
                        mFirebaseRefUsers.child(userKey).child("id").setValue(fireUser.getId());

                    }
                });


                mAuth.signInAnonymously().addOnCompleteListener(parentActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Logger.d("ZeFirebaseUser", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Logger.d("ZeFireUser", "createUserWithEmail:failure", task.getException());

                        }

                        // ...
                    }
                });

                if (listenerUserRegister != null) listenerUserRegister.onDataLoaded(deviceId);


            } else {

                if (listenerUserRegister != null) listenerUserRegister.onDataLoaded("");

                JSONArray errors = sampleObject.getJSONArray("errors");
                String error = "";

                //There was a problem
                if (errors.length() > 0) {


                    for (int i = 0; i < errors.length(); i++) {
                        String key = (String) errors.get(i);
//									if (errorObj.get(key) instanceof JSONObject) {
//										// do something with jsonObject here
//									}

                        error = error + key + "\n";

                    }


                    String message = mContext.getResources().getString(R.string.registererror);
                    problemLoading(message, 400);
                } else {
                    String info = mContext.getResources().getString(R.string.registererror);
                    problemLoading(info, 101);

                }

            }


        } catch (Exception e) {
            if (listenerUserRegister != null) listenerUserRegister.onDataLoaded("");

            String info = mContext.getResources().getString(R.string.registererror);
            problemLoading(info, 101);
            e.printStackTrace();
        }
    }


    public void performUserForgotPassword(ServiceObject theObject) {
        try {
            final String userEmail = theObject.getParams().get(0);
            JSONObject sampleObject = new JSONObject(theObject.getResult());

            Gson gson = new Gson();
            String result = sampleObject.toString();
            UserLoginObject response = gson.fromJson(result, UserLoginObject.class);

            if (response.getSuccess()) {
                finishLoading();
                RelativeLayout resendView = loadingView.findViewById(R.id.resendView);
                resendView.setVisibility(View.VISIBLE);

                RelativeLayout btnResend = loadingView.findViewById(R.id.resendView);
                btnResend.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        userForgotPassword(userEmail, dialogPlus);
                    }
                });


                btnSuccessText.setText(mContext.getResources().getString(R.string.openemailapp));
                resultSuccess.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogPlus.dismiss();
                    }
                });

                btnSuccess.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Open Email APp
                        parentActivity.finish();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                        Uri data = Uri.parse("mailto:");
                        intent.setData(data);
                        mContext.startActivity(intent);
                    }
                });

                String message = mContext.getResources().getString(R.string.resetpasswordsuccess, userEmail);
                String title = mContext.getResources().getString(R.string.resetpasswordsuccesstitle);
                successLoading(message, title);


            } else {
                //There was a problem
                if (response.getErrors().size() > 0) {
                    String message = response.getErrors().get(0);
                    problemLoading(message, 400);
                } else {
                    String info = mContext.getResources().getString(R.string.resetpassworderror);
                    problemLoading(info, 101);

                }

            }


        } catch (Exception e) {

            String info = mContext.getResources().getString(R.string.resetpassworderror);
            problemLoading(info, 101);
            e.printStackTrace();
        }
    }

    public void performUserOTPRequest(ServiceObject theObject, boolean shouldGoToConfirmPage, boolean isSilent) {
        try {
            JSONObject sampleObject = new JSONObject(theObject.getResult());
            int waitingTime = sampleObject.getInt("resendDelta");


            if (waitingTime > 0) {

                String message = mContext.getResources().getString(R.string.requestingotpsuccess);
                String title = mContext.getResources().getString(R.string.requestingotpsuccesstitle);

//				if(!isSilent) successLoading(message,title);
                Toast.makeText(mContext, title, Toast.LENGTH_SHORT).show();
                ;

                storageObject.setRegisterSecond(true);
                if (listenerOTPRequest != null) listenerOTPRequest.onDataLoaded(waitingTime);

                if (shouldGoToConfirmPage) {
                    goToOTPConfirmPage("" + waitingTime);

                }


            } else {
                String error = sampleObject.getString("error");
                //There was a problem
                String message = error;
                if (!isSilent) problemLoading(message, 400);

                if (listenerOTPRequest != null) listenerOTPRequest.onDataLoaded(0);


            }


        } catch (Exception e) {
            if (listenerOTPRequest != null) listenerOTPRequest.onDataLoaded(0);

            String info = mContext.getResources().getString(R.string.requestingotperror);
            if (!isSilent) problemLoading(info, 101);
            e.printStackTrace();


        }
    }

    public void performUserOTPRequestKnox(ServiceObject theObject, boolean shouldGoToConfirmPage, boolean isSilent) {
        try {
            JSONObject sampleObject = new JSONObject(theObject.getResult());
            int waitingTime = sampleObject.getInt("resendDelta");


            if (waitingTime > 0) {

                String message = mContext.getResources().getString(R.string.requestingotpsuccess);
                String title = mContext.getResources().getString(R.string.requestingotpsuccesstitle);

                //successLoading(message,title);
                Toast.makeText(mContext, title, Toast.LENGTH_SHORT).show();
                ;

                storageObject.setRegisterSecond(true);
                if (listenerOTPRequestKnox != null)
                    listenerOTPRequestKnox.onDataLoaded(waitingTime, message);

                if (shouldGoToConfirmPage) {
                    goToOTPConfirmPage("" + waitingTime);

                }

            } else {
                String error = sampleObject.getString("error");
                //There was a problem
                String message = error;
                if (!isSilent) problemLoading(message, 400);

                if (listenerOTPRequestKnox != null) listenerOTPRequestKnox.onDataLoaded(0, message);


            }


        } catch (Exception e) {
            String info = mContext.getResources().getString(R.string.requestingotperror);

            if (listenerOTPRequestKnox != null) listenerOTPRequestKnox.onDataLoaded(0, info);
            if (!isSilent) problemLoading(info, 400);

            e.printStackTrace();


        }
    }

    public void performUserTemporaryUnlock(ServiceObject theObject, boolean shouldGoToConfirmPage) {
        try {
            JSONObject sampleObject = new JSONObject(theObject.getResult());
            long waitingTime = sampleObject.getLong("tempDeviceActiveTill");
//			long waitingTime=TimeUnit.MILLISECONDS.toMinutes(1692962640000);
            String status = sampleObject.getString("success");

//
//			// Add 5 minutes
//			Calendar calendar = Calendar.getInstance();
//			calendar.setTime(new Date());
//			calendar.add(Calendar.MINUTE, 5);
//			waitingTime=calendar.getTimeInMillis();

            String loanNumber = theObject.getParams().get(0);
            storageObject.saveTempActiveTillToPrefs(waitingTime);
            storageObject.saveTempLoanNumberToPrefs(loanNumber);

            if (waitingTime > 0) {

                long currentTimePayment = System.currentTimeMillis();
                long timeDifference = waitingTime - currentTimePayment;

                String minutes = "" + (convertTimestampToMinutes(timeDifference) + 1);
                int rem = storageObject.retrieveTempUnlockConfigFromPrefs() - storageObject.retrieveTemporaryUnlockCountFromPrefs();


                String title = Html.fromHtml(mContext.getResources().getString(R.string.requestingunlocksuccess, minutes)).toString();
                if (status.equals("false")) {
                    //Already unlocked don't reduce count
                    title = mContext.getResources().getString(R.string.alreadyunlocked);
                } else {
                    //Not unnlocked so reduce count.
                    storageObject.increaseTemporaryUnlockCountToPrefs();
                    if (rem < 1) rem = 0;
                }

                String message = mContext.getResources().getString(R.string.requestingunlocksuccessmessage);


                RelativeLayout remainingView = loadingView.findViewById(R.id.remainingView);
                FirmaTextView tvMinutes = loadingView.findViewById(R.id.tvMinutes);
                FirmaTextView tvSeconds = loadingView.findViewById(R.id.tvSeconds);
                RelativeLayout btnSuccessTemp = loadingView.findViewById(R.id.btnSuccessTemp);

                btnSuccessTemp.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            MainActivity.viewPager.setCurrentItem(2);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        finishLoading();
                    }
                });


                remainingView.setVisibility(View.VISIBLE);

                storageObject.setIsPhoneLocked(false);


                if (listenerTemporaryUnlock != null)
                    listenerTemporaryUnlock.onDataLoaded(waitingTime);

                if (status.equals("true"))//Unlocked SHow COuntdown
                {
                    long timeRemaining = timeDifference;
                    if (timerUnlock != null) timerUnlock.cancel();
                    timerUnlock = new CountDownTimer(timeRemaining, 1000) {
                        public void onTick(long millisUntilFinished) {
                            // Calculate hours, minutes, and seconds
                            long days = millisUntilFinished / 3600000 / 24;
                            long hours = millisUntilFinished / 3600000;
                            long minutes = (millisUntilFinished % 3600000) / 60000;
                            long seconds = (millisUntilFinished % 60000) / 1000;

                            // Update the TextViews
                            String hrs = String.format("%02d", hours);
                            String mns = String.format("%02d", minutes);
                            String scs = String.format("%02d", seconds);
                            if (hours > 24) {
                                hours = hours % 24;
                                hrs = String.format("%02d", hours);


                            }


                            tvMinutes.setText(mns);
                            tvSeconds.setText(scs);

                            String min = mns + "m " + scs + "s ";
                            String title = mContext.getResources().getString(R.string.requestingunlocksuccess, min);

//							if(successTitle!=null) successTitle.setText(Html.fromHtml(title));


                        }

                        public void onFinish() {
                            // Handle countdown completion, if needed
                        }
                    }.start();

                }


            } else {
                String error = sampleObject.getString("error");
                //There was a problem
                String message = error;
                problemLoading(message, 400);

                if (listenerTemporaryUnlock != null) listenerTemporaryUnlock.onDataLoaded(0);


            }


        } catch (Exception e) {
            if (listenerTemporaryUnlock != null) listenerTemporaryUnlock.onDataLoaded(0);

            String info = mContext.getResources().getString(R.string.requestingunlockerror);
            problemLoading(info, 101);
            e.printStackTrace();


        }
    }

    public void performUserConfirmationEmailResend(ServiceObject theObject) {
        final String email = theObject.getParams().get(0);

        try {
            JSONObject sampleObject = new JSONObject(theObject.getResult());

            boolean success = sampleObject.getBoolean("success");
            if (success) {

                finishLoading();
                String message = mContext.getResources().getString(R.string.registersuccess, email);
                String title = mContext.getResources().getString(R.string.registersuccesstitle);
                successLoading(message, title);

                resendView.setVisibility(View.VISIBLE);
                btnResend.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        userConfirmationEmailResend(email, false);
                    }
                });

                btnSuccessText.setText(mContext.getResources().getString(R.string.openemailapp));
                resultSuccess.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });

                btnSuccess.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Open Email APp
                        try {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            parentActivity.startActivity(intent);
                        } catch (android.content.ActivityNotFoundException e) {
                            Toast.makeText(parentActivity, mContext.getResources().getString(R.string.openemailapp), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                doneRips.setVisibility(View.VISIBLE);
                btnDone.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goToLoginPage();
                    }
                });

            } else {
                String message = mContext.getResources().getString(R.string.requestingotperror);
                JSONArray error = sampleObject.getJSONArray("errors");
                //There was a problem
                if (error.length() > 0) {
                    message = error.get(0).toString();
                }
                problemLoading(message, 400);

            }


        } catch (Exception e) {

            String info = mContext.getResources().getString(R.string.requestingotperror);
            problemLoading(info, 101);
            e.printStackTrace();
        }
    }

    public void performUserOTPConfirm(ServiceObject theObject) {
        try {
            String userPhoneNumber = theObject.getParams().get(0);
            JSONObject sampleObject = new JSONObject(theObject.getResult());

            String hash = sampleObject.getString("hash");
            if (!hash.equals("")) {

                storageObject.saveHashToPrefs(hash);
                String message = mContext.getResources().getString(R.string.confirmingotpsuccess);
                String title = mContext.getResources().getString(R.string.confirmingotpsuccesstitle);
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();

//				finishLoading();
                goToChangePasswordPage(userPhoneNumber);

            } else {
                //There was a problem
                String message = mContext.getResources().getString(R.string.confirmingotperror);
                problemLoading(message, 400);

            }


        } catch (Exception e) {

            String info = mContext.getResources().getString(R.string.confirmingotperror);
            problemLoading(info, 101);
            e.printStackTrace();
        }
    }

    public void performUserGetLoanDetails(ServiceObject theObject, boolean isSilent) {
        try {
            JSONObject sampleObject = new JSONObject(theObject.getResult());
            String result = sampleObject.toString();
            String loanNumber = theObject.getParams().get(0);


            Gson gson = new Gson();
            AssetObject asset = gson.fromJson(result, AssetObject.class);

            String jsonString = "";

            if (sampleObject.has("upgradeEligibility")) {
                try {


                    jsonString = sampleObject.getString("upgradeEligibility");
                    JSONObject ubj = new JSONObject(jsonString);
                    String resUbj = ubj.toString();

                    DeviceUpgradeObject devices = gson.fromJson(resUbj, DeviceUpgradeObject.class);
                    asset.setDeviceUpgradeObject(devices);


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }


            if (asset != null) {

                if (!isSilent) {
                    finishLoading();

                }

                asset.setLoanNumber(loanNumber);
                if (listenerLoanDetails != null) listenerLoanDetails.onDataLoaded(asset);


            } else {


                String error = mContext.getResources().getString(R.string.getloandetailsserror);

                //There was a problem
                if (!isSilent) {
                    String message = error;
                    problemLoading(message, 400);
                }

                if (listenerLoanDetails != null) listenerLoanDetails.onDataLoaded(null);

            }


        } catch (Exception e) {
            if (listenerLoanDetails != null) listenerLoanDetails.onDataLoaded(null);

            if (!isSilent) {
                String info = mContext.getResources().getString(R.string.getloandetailsserror);
                problemLoading(info, 101);
            }
            e.printStackTrace();
        }
    }

    public void performUserGetLoanUpgradeEligibility(ServiceObject theObject, boolean isSilent) {
        try {
            JSONObject sampleObject = new JSONObject(theObject.getResult());
            String result = sampleObject.toString();
            String loanNumber = theObject.getParams().get(0);


            Gson gson = new Gson();
            DeviceUpgradeObject devices = gson.fromJson(result, DeviceUpgradeObject.class);

            if (devices != null) {

                if (!isSilent) {
                    finishLoading();

                }

//				devices.setLoanNumber(loanNumber);
                if (listenerLoanUpgrade != null) listenerLoanUpgrade.onDataLoaded(devices);


            } else {


                String error = mContext.getResources().getString(R.string.getloandetailsserror);

                //There was a problem
                if (!isSilent) {
                    String message = error;
                    problemLoading(message, 400);
                }

                if (listenerLoanUpgrade != null) listenerLoanUpgrade.onDataLoaded(null);

            }


        } catch (Exception e) {
            if (listenerLoanUpgrade != null) listenerLoanUpgrade.onDataLoaded(null);

            if (!isSilent) {
                String info = mContext.getResources().getString(R.string.getloandetailsserror);
                problemLoading(info, 101);
            }
            e.printStackTrace();
        }
    }

    public void performUserSubmitFeedback(ServiceObject theObject, boolean isSilent) {
        try {
            JSONObject sampleObject = new JSONObject(theObject.getResult());

            String message = mContext.getResources().getString(R.string.thankyouforyourfeedback);
            String title = mContext.getResources().getString(R.string.feedbacksubmitted);

            successLoading(message, title);
            Toast.makeText(mContext, title, Toast.LENGTH_SHORT).show();
            ;

            btnSuccess.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listenerSubmitFeedback != null) listenerSubmitFeedback.onDataLoaded(true);

                }
            });


        } catch (Exception e) {
            if (listenerSubmitFeedback != null) listenerSubmitFeedback.onDataLoaded(false);

            String info = mContext.getResources().getString(R.string.submitfeedbackserror);
            problemLoading(info, 101);
            e.printStackTrace();


        }
    }

    public void performUserSubmitPaymentRating(ServiceObject theObject, boolean isSilent) {
        try {

            String message = mContext.getResources().getString(R.string.thankyouforyourfeedback);
            String title = mContext.getResources().getString(R.string.feedbacksubmitted);

//				successLoading(message,title);
            Toast.makeText(mContext, title, Toast.LENGTH_SHORT).show();
            ;


        } catch (Exception e) {

            String info = mContext.getResources().getString(R.string.submitfeedbackserror);
            problemLoading(info, 101);
            e.printStackTrace();


        }
    }

    public void performUserGetPayments(ServiceObject theObject, boolean isSilent) {
        try {
            JSONObject sampleObject = new JSONObject(theObject.getResult());
            String result = sampleObject.toString();

            Gson gson = new Gson();
            PaymentsListObject response = gson.fromJson(result, PaymentsListObject.class);


            if (response.getTransactionList() != null) {

                ArrayList<Payment> payments = (ArrayList<Payment>) response.getTransactionList();

                for (int i = 0; i < payments.size(); i++) {

                    storageObject.addPaymentObjectToPrefs(payments.get(i));

                }
//				storageObject.savePaymentObjectsToPrefs(payments);


                if (!isSilent) {
                    finishLoading();

                }

                if (listenerPayments != null) listenerPayments.onDataLoaded(payments);


            } else {
                ArrayList<Payment> payments = new ArrayList<Payment>();
                storageObject.savePaymentObjectsToPrefs(payments);

                String error = sampleObject.getString("details");

                //There was a problem
                if (!isSilent) {
                    String message = mContext.getResources().getString(R.string.getpaymentserror);
                    problemLoading(message, 400);
                }

                if (listenerPayments != null) listenerPayments.onDataLoaded(payments);

            }


        } catch (Exception e) {
            if (!isSilent) {
                String info = mContext.getResources().getString(R.string.getpaymentserror);
                problemLoading(info, 101);
            }
            e.printStackTrace();
        }
    }

    public void performUserGetUpcomingPayments(ServiceObject theObject, boolean isSilent) {
        try {
            String userPhoneNumber = theObject.getParams().get(0);
            JSONObject sampleObject = new JSONObject(theObject.getResult());


            String status = sampleObject.getString("minimum_monthly_installment");

            if (!StringUtils.isStringEmpty(status)) {


                double upcoming = sampleObject.getDouble("minimum_monthly_installment");
                double outstanding = sampleObject.getDouble("outstanding_amount");
                String date = sampleObject.getString("due_date");

                int upcomingValue = (int) Math.round(upcoming);
                int outstandingValue = (int) Math.round(outstanding);
                storageObject.saveUserUpcomingAmountToPrefs(upcomingValue);
                storageObject.saveUserOutStandingAmountToPrefs(outstandingValue);
                storageObject.saveUserDueDateToPrefs(date);


                if (!isSilent) {
                    finishLoading();

                }

                if (listenerUpcomingPayments != null)
                    listenerUpcomingPayments.onDataLoaded(date, outstanding, upcoming);

            } else {
                String error = sampleObject.getString("details");

                //There was a problem
                if (!isSilent) {
                    String message = error;
                    problemLoading(message, 400);
                }

                if (listenerUpcomingPayments != null)
                    listenerUpcomingPayments.onDataLoaded("", 0, 0);


            }


        } catch (Exception e) {
            if (!isSilent) {
                String info = mContext.getResources().getString(R.string.getupcomingpaymentserror);
                problemLoading(info, 101);
            }
            e.printStackTrace();
        }
    }

    public void performUserInitiatePaymentStk(ServiceObject theObject, boolean isSilent) {
        final String phoneNumber = theObject.getParams().get(0);

        try {
            final JSONObject sampleObject = new JSONObject(theObject.getResult());
            String result = sampleObject.toString();

            Gson gson = new Gson();
            InitiatePaymentObject transaction = gson.fromJson(result, InitiatePaymentObject.class);

            if (transaction != null && transaction.isSuccess()) {

                if (!isSilent) {
                    finishLoading();

                }

                String sText = mContext.getResources().getString(R.string.paymentsuccessmessage);


                if (!phoneNumber.equals(storageObject.retrieveUserPhoneNumberFromPrefs())) {

                    //Pay for ANother Number
//					RelativeLayout remainingView=loadingView.findViewById(R.id.remainingView);
//					FirmaTextView remainingText= loadingView.findViewById(R.id.remainingText);
//					ImageView successIcon= loadingView.findViewById(R.id.successIcon);
//
//
//					sText=sText+" "+ mContext.getResources().getString(R.string.attemptsleft);
//
//					int rem = storageObject.retrievePayOtherConfigFromPrefs()-storageObject.retrieveOtherNumberCountFromPrefs();
//					if(rem<1) rem=0;
//
//					successIcon.setVisibility(View.GONE);
//					remainingView.setVisibility(View.VISIBLE);
//					remainingText.setText(""+rem);


                }

                successLoading(sText, mContext.getResources().getString(R.string.processingpayment));
                showRateView(loadingView, "" + transaction.getPaymentRequestId());


                btnSuccessText.setText(mContext.getResources().getString(R.string.confirmpayment));
                btnSuccess.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        userGetPayments("", "", "" + "0", true);

                        confirmSTKPayment("" + transaction.getPaymentRequestId(), false);


                    }
                });

            } else {


                //There was aproblem
                if (!isSilent) {
                    btnFailedText.setText(mContext.getResources().getString(R.string.retry));
                    String info = mContext.getResources().getString(R.string.initiatepaymenterror);
                    problemLoading(info, 101);
                }

                btnFailed.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogPlus.dismiss();
                    }
                });

                if (listenerPaymentStk != null) listenerPaymentStk.onDataLoaded(false);

            }


        } catch (Exception e) {
            if (listenerPaymentStk != null) listenerPaymentStk.onDataLoaded(false);

            if (!isSilent) {
                btnFailedText.setText(mContext.getResources().getString(R.string.retry));
                String info = mContext.getResources().getString(R.string.initiatepaymenterror);
                problemLoading(info, 101);
            }
            e.printStackTrace();
        }
    }

    public void performUserConfirmPaymentStk(ServiceObject theObject, boolean isSilent) {
        String transactionId = theObject.getParams().get(0);


        try {
            JSONObject sampleObject = new JSONObject(theObject.getResult());
            String status = sampleObject.getString("status");


            if (!StringUtils.isStringEmpty(status) && status.equals("SUCCESSFUL")) {

                if (!isSilent) {
                    finishLoading();
                    String message = mContext.getResources().getString(R.string.paymentcompletedmessage);
                    String title = mContext.getResources().getString(R.string.paymentsuccesstitle);
                    successLoading(message, title);

                }

                GeneralCommands.logEvent(parentActivity, "click_pay_done", "click_pay_done");
                if (listenerPaymentStk != null) listenerPaymentStk.onDataLoaded(true);

                if (listenerConfirmPaymentStk != null)
                    listenerConfirmPaymentStk.onDataLoaded(status, transactionId);


            } else {


                String error = mContext.getResources().getString(R.string.confirmpaymentfailed, status);
                //There was a problem
                if (!isSilent) {
                    String message = error;
                    problemLoading(message, 400);
                }

                if (listenerConfirmPaymentStk != null)
                    listenerConfirmPaymentStk.onDataLoaded(status, transactionId);

            }


        } catch (Exception e) {
            if (listenerConfirmPaymentStk != null)
                listenerConfirmPaymentStk.onDataLoaded(null, transactionId);
            if (!isSilent) {
                String info = mContext.getResources().getString(R.string.confirmpaymenterror);
                problemLoading(info, 101);
            }
            e.printStackTrace();
        }
    }


    public void performUserChangePassword(ServiceObject theObject) {
        try {
            String userPhoneNumber = theObject.getParams().get(0);
            JSONObject sampleObject = new JSONObject(theObject.getResult());


            boolean isSuccess = sampleObject.getBoolean("success");
            if (isSuccess) {
                finishLoading();
                String feedback = sampleObject.getString("message");

                String message = feedback;
                String title = mContext.getResources().getString(R.string.resetpasswordsuccesstitle);

                successLoading(message, title);

                resultSuccess.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goToLoginPage();
                    }
                });

                btnSuccess.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goToLoginPage();
                    }
                });


            } else {
                String error = sampleObject.getString("details");
                //There was a problem
                String message = error;
                problemLoading(message, 400);

            }


        } catch (Exception e) {

            String info = mContext.getResources().getString(R.string.resetpassworderror);
            problemLoading(info, 101);
            e.printStackTrace();
        }
    }

    public void performUserUploadDocument(ServiceObject theObject, boolean isSilent) {
        try {
            JSONObject sampleObject = new JSONObject(theObject.getResult());


            boolean isSuccess = sampleObject.getBoolean("success");
            if (isSuccess) {
                finishLoading();

                String message = mContext.getResources().getString(R.string.uploaddocumentsuccess);
                String title = mContext.getResources().getString(R.string.successful);

                successLoading(message, title);

                btnSuccessText.setText(mContext.getResources().getString(R.string.awesome));
                resultSuccess.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goToMainPage();
                    }
                });
                btnSuccess.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goToMainPage();
                    }
                });


            } else {
                String error = sampleObject.getString("details");
                //There was a problem
                String message = error;
                problemLoading(message, 400);

            }


        } catch (Exception e) {

            String info = mContext.getResources().getString(R.string.uploaddocumenterror);
            problemLoading(info, 101);
            e.printStackTrace();
        }
    }


    public void performUploadProfilePic(ServiceObject theObject, final boolean isSilent, final Uri imageUri) {
        try {
            JSONObject sampleObject = new JSONObject(theObject.getResult());


            int result_code = sampleObject.getInt(RESPONSE_RESULT_CODE);

            String reply = "" + result_code;

            if (reply.equals(RESPONSE_RESULT_SUCCESS)) {
                String newPic = sampleObject.getString(RESPONSE_RESULT_DATA);

                User user = storageObject.retrieveUserObjectFromPrefs();

                ImageUtil.displayRoundImage(userImage, newPic, null);

                storageObject.saveUserObjectToPrefs(user);

                if (!isSilent) {
                    finishLoading();

                }

                String resMsg = mContext.getResources().getString(R.string.uploadprofilepicsuccess);
                Toast.makeText(mContext, resMsg, Toast.LENGTH_SHORT).show();
            } else {

                if (!isSilent) {
                    //There was a problem uploading the image
                    String info = mContext.getResources().getString(R.string.uploadprofilepicerror);
                    problemLoading(info, 400);

                }


            }


        } catch (Exception e) {
            if (!isSilent) {
                String info = mContext.getResources().getString(R.string.uploadprofilepicerror);
                problemLoading(info, 101);
            }

            e.printStackTrace();
        }
    }


    public void performUserGetMyProfile(ServiceObject theObject, final boolean isSilent) {
        try {


            JSONObject sampleObject = new JSONObject(theObject.getResult());

            Gson gson = new Gson();
            String result = sampleObject.toString();

            ProfileObject user = gson.fromJson(result, ProfileObject.class);

            if (user != null && user.getFirstname() != null) {


                storageObject.saveUserFirstNameToPrefs(user.getFirstname());
                storageObject.saveUserLastNameToPrefs(user.getLastname());
                storageObject.saveUserNameToPrefs(user.getFirstname() + " " + user.getLastname());

                storageObject.saveUserPhoneNumberToPrefs(user.getPhoneNumber());
                storageObject.saveUserDOBToPrefs(user.getDateOfBirth());
                storageObject.saveUserGenderToPrefs(user.getGender());

                User currentUser = storageObject.retrieveUserObjectFromPrefs();
                currentUser.setFirstName(user.getFirstname());
                currentUser.setLastName(user.getLastname());
                currentUser.setName(user.getFirstname() + " " + user.getLastname());
                currentUser.setDateOfBirth(user.getDateOfBirth());
                currentUser.setPhone_number(user.getPhoneNumber());
                currentUser.setPhoneNumber(user.getPhoneNumber());
                storageObject.saveUserObjectToPrefs(currentUser);


                if (listenerCustomerDetails != null) listenerCustomerDetails.onDataLoaded(user);


                if (!isSilent) {
                    finishLoading();

                }


            } else {

                if (!isSilent) {

                    if (listenerCustomerDetails != null) listenerCustomerDetails.onDataLoaded(null);

                    //There was a problem
                    String message = mContext.getResources().getString(R.string.getmyprofileerror);
                    problemLoading(message, 400);

                }

            }


        } catch (Exception e) {
            if (listenerCustomerDetails != null) listenerCustomerDetails.onDataLoaded(null);

            if (!isSilent) {
                String info = mContext.getResources().getString(R.string.getmyprofileerror);
                problemLoading(info, 101);
            }
            e.printStackTrace();
        }
    }

    public void performUserEditProfile(ServiceObject theObject, final boolean isSilent) {
        try {
            JSONObject sampleObject = new JSONObject(theObject.getResult());
            int result_code = sampleObject.getInt(RESPONSE_RESULT_CODE);


            String reply = "" + result_code;


            if (reply.equals(RESPONSE_RESULT_SUCCESS)) {

                Gson gson = new Gson();
                String result = sampleObject.toString();

                ProfileObject user = gson.fromJson(result, ProfileObject.class);
//					        storageObject.saveUserObjectToPrefs(user);

                if (!isSilent) {
                    finishLoading();
                }

                String resMsg = mContext.getResources().getString(R.string.editmyprofilessuccess);
                Toast.makeText(mContext, resMsg, Toast.LENGTH_SHORT).show();


            } else {

                if (!isSilent) {
                    //There was a problem
                    String message = mContext.getResources().getString(R.string.editmyprofileerror);
                    problemLoading(message, 400);

                }

            }

        } catch (Exception e) {
            if (!isSilent) {
                String info = mContext.getResources().getString(R.string.editmyprofileerror);
                problemLoading(info, 101);
            }
            e.printStackTrace();
        }
    }

    public void performUserAddAction(ServiceObject theObject, final boolean isSilent) {
        try {
            JSONObject sampleObject = new JSONObject(theObject.getResult());
            int result_code = sampleObject.getInt(RESPONSE_RESULT_CODE);


            String reply = "" + result_code;


            if (reply.equals(RESPONSE_RESULT_SUCCESS)) {

                Gson gson = new Gson();
                String result = sampleObject.toString();

                if (!isSilent) {
                    String resMsg = mContext.getResources().getString(R.string.adduseractionsuccess);
                    Toast.makeText(mContext, resMsg, Toast.LENGTH_SHORT).show();

                    finishLoading();
                }


            } else {

                if (!isSilent) {
                    //There was a problem
                    String message = mContext.getResources().getString(R.string.adduseractionerror);
                    problemLoading(message, 400);

                }

            }

        } catch (Exception e) {
            if (!isSilent) {
                String info = mContext.getResources().getString(R.string.adduseractionerror);
                problemLoading(info, 101);
            }
            e.printStackTrace();
        }
    }


    public void performUserUpdateProfile(ServiceObject theObject, final boolean isSilent) {
        try {
            JSONObject sampleObject = new JSONObject(theObject.getResult());
            int result_code = sampleObject.getInt(RESPONSE_RESULT_CODE);


            String reply = "" + result_code;


            if (reply.equals(RESPONSE_RESULT_SUCCESS)) {

                Gson gson = new Gson();
                String result = sampleObject.toString();

                if (!isSilent) {
                    String resMsg = mContext.getResources().getString(R.string.editmyprofilessuccess);
                    Toast.makeText(mContext, resMsg, Toast.LENGTH_SHORT).show();

                    finishLoading();
                }


            } else {

                if (!isSilent) {
                    //There was a problem
                    String message = mContext.getResources().getString(R.string.editmyprofileerror);
                    problemLoading(message, 400);

                }

            }

        } catch (Exception e) {
            if (!isSilent) {
                String info = mContext.getResources().getString(R.string.editmyprofileerror);
                problemLoading(info, 101);
            }
            e.printStackTrace();
        }
    }


    public void performUserUpdateTokenFCM(ServiceObject theObject, final boolean isSilent) {
        try {
            JSONObject sampleObject = new JSONObject(theObject.getResult());
            int result_code = sampleObject.getInt(RESPONSE_RESULT_CODE);


            String reply = "" + result_code;


            if (reply.equals(RESPONSE_RESULT_SUCCESS)) {

                Gson gson = new Gson();
                String result = sampleObject.toString();

                if (!isSilent) {
                    String resMsg = mContext.getResources().getString(R.string.updatetokensuccess);
                    Toast.makeText(mContext, resMsg, Toast.LENGTH_SHORT).show();

                    finishLoading();
                }


            } else {

                if (!isSilent) {
                    //There was a problem
                    String message = mContext.getResources().getString(R.string.updatetokenerror);
                    problemLoading(message, 400);

                }

            }

        } catch (Exception e) {
            if (!isSilent) {
                String info = mContext.getResources().getString(R.string.updatetokenerror);
                problemLoading(info, 101);
            }
            e.printStackTrace();
        }
    }

    public void performUserOTPChannels(ServiceObject theObject, final boolean isSilent) {
        try {
            JSONObject sampleObject = new JSONObject(theObject.getResult());
            String res = theObject.getResult();

            if (res.contains("channels")) {
                boolean isKnoxActive = res.toLowerCase().contains("device") ? true : false;
                storageObject.setKnoxFeatureStatus(isKnoxActive);

                JSONObject channelsObj = new JSONObject(res);
                JSONArray channels = channelsObj.getJSONArray("channels");


                String[] channelArray = new String[channels.length()];

                for (int i = 0; i < channels.length(); i++) {
                    // if it's a JSON string value:
                    channelArray[i] = channels.getString(i);
                    // if it's a JSON object with a "name" field, for example:
                    // channelArray[i] = channels.getJSONObject(i).getString("name");
                }

                if (listenerUserChannels != null) listenerUserChannels.onDataLoaded(channelArray);
                storageObject.saveUserChannelsToPrefs(channelArray);


                Gson gson = new Gson();
                String result = sampleObject.toString();

                if (!isSilent) {
                    String resMsg = mContext.getResources().getString(R.string.adduseractionsuccess);
                    Toast.makeText(mContext, resMsg, Toast.LENGTH_SHORT).show();

                    finishLoading();
                }


            } else {
                if (listenerUserChannels != null) listenerUserChannels.onDataLoaded(null);


                if (!isSilent) {
                    //There was a problem
                    String message = mContext.getResources().getString(R.string.adduseractionerror);
                    problemLoading(message, 400);

                }

            }

        } catch (Exception e) {
            if (listenerUserChannels != null) listenerUserChannels.onDataLoaded(null);

            if (!isSilent) {
                String info = mContext.getResources().getString(R.string.adduseractionerror);
                problemLoading(info, 101);
            }
            e.printStackTrace();
        }
    }

    public void performUserDownloadLoanAgreement(ServiceObject theObject, final boolean isSilent) {
        try {
            JSONObject sampleObject = new JSONObject(theObject.getResult());
            String res = theObject.getResult();

            if (res.contains("content")) {
                storageObject.setHasLoanAgreement(true);


                String jsonString = sampleObject.getString("content");
                String extension = sampleObject.getString("extension");

                handleBase64File(parentActivity, jsonString, extension);


                Gson gson = new Gson();

                if (!isSilent) {
                    String resMsg = mContext.getResources().getString(R.string.downloadsuccess);
                    Toast.makeText(mContext, resMsg, Toast.LENGTH_SHORT).show();

                    finishLoading();
                }


            } else {
                storageObject.setHasLoanAgreement(false);


                if (!isSilent) {
                    //There was a problem
                    String message = mContext.getResources().getString(R.string.downloadloanagreementerror);
                    problemLoading(message, 400);
                    errorTitle.setText(mContext.getResources().getString(R.string.unabletodownloadloanagreement));
                    btnRetryText.setText(mContext.getResources().getString(R.string.chatwithus));
                    btnRetry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String errorMsg = mContext.getResources().getString(R.string.unabletodownloadloanagreement);
                            openWhatsAppWithMessage(errorMsg);
                        }
                    });

                }

            }

        } catch (Exception e) {
            storageObject.setHasLoanAgreement(false);

            if (!isSilent) {
                //There was a problem
                String message = mContext.getResources().getString(R.string.downloadloanagreementerror);
                problemLoading(message, 101);
                errorTitle.setText(mContext.getResources().getString(R.string.unabletodownloadloanagreement));
                btnRetryText.setText(mContext.getResources().getString(R.string.chatwithus));
                btnRetry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String errorMsg = mContext.getResources().getString(R.string.unabletodownloadloanagreement);
                        openWhatsAppWithMessage(errorMsg);
                    }
                });
            }
            e.printStackTrace();
        }
    }

    public void performUserPaymentProviders(ServiceObject theObject, final boolean isSilent) {
        try {
            JSONObject sampleObject = new JSONObject(theObject.getResult());
            String res = theObject.getResult();


            if (res.contains("providers")) {


                Gson gson = new Gson();
                String result = sampleObject.toString();
                PaymentProviderObject response = gson.fromJson(result, PaymentProviderObject.class);

                if (response.getProviders() != null) {
                    ArrayList<ProvidersItem> providers = (ArrayList<ProvidersItem>) response.getProviders();
                    storageObject.savePaymentProvidersToPrefs(providers);

                }
                if (!isSilent) {
                    String resMsg = mContext.getResources().getString(R.string.adduseractionsuccess);
                    Toast.makeText(mContext, resMsg, Toast.LENGTH_SHORT).show();

                    finishLoading();
                }


            } else {

                if (!isSilent) {
                    //There was a problem
                    String message = mContext.getResources().getString(R.string.adduseractionerror);
                    problemLoading(message, 400);

                }

            }

        } catch (Exception e) {
            if (!isSilent) {
                String info = mContext.getResources().getString(R.string.adduseractionerror);
                problemLoading(info, 101);
            }
            e.printStackTrace();
        }
    }

    public void goToMainPage() {
        Intent intent = new Intent(parentActivity, MainActivity.class);
//		  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        GeneralCommands.goToPage(parentActivity, intent, true);

    }


    public void goToLoginPage() {
        Intent mIntent = new Intent(parentActivity, LoginActivity.class);
        mIntent.putExtra(KEY_START_FROM, "splash");
        DirectionUtils.changeActivity(parentActivity, R.anim.slide_in_from_right, R.anim.slide_out_to_left, true, mIntent);

    }

    public void goToChangePasswordPage(final String userPhoneNumber) {
        Intent mIntent = new Intent(parentActivity, ChangePasswordActivity.class);
        mIntent.putExtra(KEY_START_FROM, "splash");
        mIntent.putExtra(PHONE_NUMBER, userPhoneNumber);
        GeneralCommands.goToPage(parentActivity, mIntent, true);

    }


    private String getCurrentDateTime() {
        String delegateTime = "hh:mm aaa";// 2: 30 pm
        String time = (String) DateFormat.format(delegateTime, Calendar.getInstance().getTime());


        SimpleDateFormat df = new SimpleDateFormat("dd MMMM, yyyy");//14 August, 2014
        String date = df.format(Calendar.getInstance().getTime());

        return date + " at " + time;


    }


    private String getCurrentTime() {
        String delegate = "hh:mm aaa";
        return (String) DateFormat.format(delegate, Calendar.getInstance().getTime());
    }


    public void goToOTPConfirmPage(String waitingTime) {
        Intent mIntent = new Intent(parentActivity, OTPConfirmActivity.class);
        mIntent.putExtra(KEY_START_FROM, "signin");
        mIntent.putExtra(WAITING_TIME, waitingTime);
        GeneralCommands.goToPage(parentActivity, mIntent, true);

    }

    // Assign the listener implementing events interface that will receive the events


    // Assign the listener implementing events interface that will receive the events
    public void setPaymentsListener(PaymentsListener listener) {
        this.listenerPayments = listener;
    }

    public interface PaymentsListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        public void onObjectReady(String title);

        // or when data has been loaded
        public void onDataLoaded(ArrayList<Payment> data);
    }

    public void setLoanAccountsListener(GetLoanAccountsListener listener) {
        this.listenerLoanAccounts = listener;
    }

    public interface GetLoanAccountsListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        public void onObjectReady(String title);

        // or when data has been loaded
        public void onDataLoaded(ArrayList<LoanListItemObject> data);
    }

    public void setLoanDetailsListener(LoanDetailsListener listener) {
        this.listenerLoanDetails = listener;
    }

    public interface LoanDetailsListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        public void onObjectReady(String title);

        // or when data has been loaded
        public void onDataLoaded(AssetObject data);
    }


    public void setCustomerDetailsListener(CustomerDetailsListener listener) {
        this.listenerCustomerDetails = listener;
    }

    public interface CustomerDetailsListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        public void onObjectReady(String title);

        // or when data has been loaded
        public void onDataLoaded(ProfileObject data);
    }

    public interface UserRegisterListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        public void onObjectReady(String title);

        // or when data has been loaded
        public void onDataLoaded(String deviceId);
    }

    public void setUserRegisterListener(UserRegisterListener listener) {
        this.listenerUserRegister = listener;
    }


    public interface OTPRequestListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        public void onObjectReady(String title);

        // or when data has been loaded
        public void onDataLoaded(int waitingTime);
    }

    public void setUserOTPRequestListener(OTPRequestListener listener) {
        this.listenerOTPRequest = listener;
    }


    public interface UserChannelsListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        public void onObjectReady(String title);

        // or when data has been loaded
        public void onDataLoaded(String[] channels);
    }

    public void setUserChannelsListener(UserChannelsListener listener) {
        this.listenerUserChannels = listener;
    }


    public interface OTPRequestKnoxListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        public void onObjectReady(String title);

        // or when data has been loaded
        public void onDataLoaded(int waitingTime, String message);
    }

    public void setUserOTPRequestKnoxListener(OTPRequestKnoxListener listener) {
        this.listenerOTPRequestKnox = listener;
    }


    public interface TemporaryUnlockListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        public void onObjectReady(String title);

        // or when data has been loaded
        public void onDataLoaded(long waitingTime);
    }

    public void setTemporaryUnlockListener(TemporaryUnlockListener listener) {
        this.listenerTemporaryUnlock = listener;
    }

    public interface OTPConfirmListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        public void onObjectReady(String title);

        // or when data has been loaded
        public void onDataLoaded(boolean isSuccess);
    }

    public void setUserOTPConfirmListener(OTPConfirmListener listener) {
        this.listenerOTPConfirm = listener;
    }

    public void setLoanUpgradeListener(LoanUpgradeListener listener) {
        this.listenerLoanUpgrade = listener;
    }

    public interface LoanUpgradeListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        public void onObjectReady(String title);

        // or when data has been loaded
        public void onDataLoaded(DeviceUpgradeObject data);
    }


    // Assign the listener implementing events interface that will receive the events


    public interface UpcomingPaymentsListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        public void onObjectReady(String title);

        // or when data has been loaded
        public void onDataLoaded(String date, double outstanding, double monthly);
    }

    // Assign the listener implementing events interface that will receive the events
    public void setPaymentSTKListener(PaymentSTKListener listener) {
        this.listenerPaymentStk = listener;
    }

    public interface PaymentSTKListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        public void onObjectReady(String title);

        // or when data has been loaded
        public void onDataLoaded(boolean isSuccess);
    }


    // Assign the listener implementing events interface that will receive the events
    public void confirmPaymentSTKListener(ConfirmPaymentSTKListener listener) {
        this.listenerConfirmPaymentStk = listener;
    }

    public interface ConfirmPaymentSTKListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        public void onObjectReady(String title);

        // or when data has been loaded
        public void onDataLoaded(String status, String paymentId);
    }

    public void setSubmitFeedbackListener(SubmitFeedbackListener listener) {
        this.listenerSubmitFeedback = listener;
    }

    public interface SubmitFeedbackListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        public void onObjectReady(String title);

        // or when data has been loaded
        public void onDataLoaded(boolean status);
    }

    public String fixNumber(String number) {

        if (number.length() > 5 && number.regionMatches(0, "07", 0, 1)) {
            StringBuffer buff = new StringBuffer(number.length() - 1);
            buff.append(number.substring(0, 0)).append(number.substring(0 + 1));
            String fixed = "255" + buff.toString();

            return fixed;
        } else {
            return number;
        }

    }

    public String unFixNumber(String number) {

        if (number.length() > 5 && number.regionMatches(0, "07", 0, 1)) {

            StringBuffer buff = new StringBuffer(number);
            buff.setCharAt(0, '~');
            buff.setCharAt(1, '~');
            buff.setCharAt(2, '~');

            String fixed = "0" + buff.toString().replace("~", "");

            return fixed;
        } else {
            return number;

        }
    }

    // Assign the listener implementing events interface that will receive the events

    public static String getFileName(Uri uri) {
        String result;

        //if uri is content
        if (uri.getScheme() != null && uri.getScheme().equals("content")) {
            Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    //local filesystem
                    int index = cursor.getColumnIndex("_data");
                    if (index == -1)
                        //google drive
                        index = cursor.getColumnIndex("_display_name");
                    result = cursor.getString(index);
                    if (result != null)
                        uri = Uri.parse(result);
                    else
                        return null;
                }
            } finally {
                cursor.close();
            }
        }

        result = uri.getPath();

        //get filename + ext of path
        int cut = result.lastIndexOf('/');
        if (cut != -1)
            result = result.substring(cut + 1);
        return result;
    }

    public String getName(Uri uri) {
        String filename;
        Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);

        if (cursor == null) filename = uri.getPath();
        else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME);
            filename = cursor.getString(idx);
            cursor.close();
        }

        String name = filename.substring(0, filename.lastIndexOf("."));
        String extension = filename.substring(filename.lastIndexOf(".") + 1);

        return name + "." + extension;
    }

    private String getFileType(String fileName) {
        // TODO Auto-generated method stub
        return fileName.substring(fileName.lastIndexOf("."), fileName.length());
    }

    @SuppressLint("RestrictedApi")
    String getDayOfMonthSuffix(final int n) {
        checkArgument(n >= 1 && n <= 31, "illegal day of month: " + n);
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(mContext, fileUri);

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),
                file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    public void logOutUser() {

        String message = mContext.getResources().getString(R.string.pleaseverifyagain);
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT);


        if (parentActivity != null) {
            Intent intent = new Intent(parentActivity, ActivateAccountActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            GeneralCommands.goToPage(parentActivity, intent, true);
        }

    }

    public ByteArrayBody getByteArrayPdf(Uri filepath, String file_name) {
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            inputStream = mContext.getContentResolver().openInputStream(filepath);

            byte[] buffer = new byte[1024];
            byteArrayOutputStream = new ByteArrayOutputStream();

            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        byte[] pdfByteArray = byteArrayOutputStream.toByteArray();

        ByteArrayBody body = new ByteArrayBody(pdfByteArray, file_name);
        return body;
    }


    public static KeyPair generateECKeyPair() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        // Generate an EC key pair using the P-256 curve
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        ECGenParameterSpec ecGenSpec = new ECGenParameterSpec("secp256r1");
        keyPairGenerator.initialize(ecGenSpec);

        // Generate the key pair
        return keyPairGenerator.generateKeyPair();
    }

    public static long convertTimestampToMinutes(long timestampInMillis) {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timestampInMillis);
        return minutes;
    }

    // Example usage in an Android activity or class

    public void showRateView(View v, String paymentRequestId) {

        RelativeLayout rateView = v.findViewById(R.id.rateView);
        RelativeLayout btnRate1 = v.findViewById(R.id.btnRate1);
        RelativeLayout btnRate2 = v.findViewById(R.id.btnRate2);
        RelativeLayout btnRate3 = v.findViewById(R.id.btnRate3);
        RelativeLayout btnRate4 = v.findViewById(R.id.btnRate4);
        RelativeLayout btnRate5 = v.findViewById(R.id.btnRate5);

        ImageView rate1 = (ImageView) v.findViewById(R.id.rate1);
        ImageView rate2 = (ImageView) v.findViewById(R.id.rate2);
        ImageView rate3 = (ImageView) v.findViewById(R.id.rate3);
        ImageView rate4 = (ImageView) v.findViewById(R.id.rate4);
        ImageView rate5 = (ImageView) v.findViewById(R.id.rate5);

        rateView.setVisibility(View.GONE);


        btnRate1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                rating = 1;
                rate1.setColorFilter(mContext.getResources().getColor(R.color.theme_orange_light));
                rate2.setColorFilter(mContext.getResources().getColor(R.color.white));
                rate3.setColorFilter(mContext.getResources().getColor(R.color.white));
                rate4.setColorFilter(mContext.getResources().getColor(R.color.white));
                rate5.setColorFilter(mContext.getResources().getColor(R.color.white));


            }
        });

        btnRate2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                rating = 2;
                rate1.setColorFilter(mContext.getResources().getColor(R.color.white));
                rate2.setColorFilter(mContext.getResources().getColor(R.color.theme_orange_light));
                rate3.setColorFilter(mContext.getResources().getColor(R.color.white));
                rate4.setColorFilter(mContext.getResources().getColor(R.color.white));
                rate5.setColorFilter(mContext.getResources().getColor(R.color.white));


            }
        });

        btnRate3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                rating = 3;
                rate1.setColorFilter(mContext.getResources().getColor(R.color.white));
                rate2.setColorFilter(mContext.getResources().getColor(R.color.white));
                rate3.setColorFilter(mContext.getResources().getColor(R.color.theme_orange_light));
                rate4.setColorFilter(mContext.getResources().getColor(R.color.white));
                rate5.setColorFilter(mContext.getResources().getColor(R.color.white));


            }
        });

        btnRate4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                rating = 4;
                rate1.setColorFilter(mContext.getResources().getColor(R.color.white));
                rate2.setColorFilter(mContext.getResources().getColor(R.color.white));
                rate3.setColorFilter(mContext.getResources().getColor(R.color.white));
                rate4.setColorFilter(mContext.getResources().getColor(R.color.theme_orange_light));
                rate5.setColorFilter(mContext.getResources().getColor(R.color.white));


            }
        });

        btnRate5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                rating = 5;
                rate1.setColorFilter(mContext.getResources().getColor(R.color.white));
                rate2.setColorFilter(mContext.getResources().getColor(R.color.white));
                rate3.setColorFilter(mContext.getResources().getColor(R.color.white));
                rate4.setColorFilter(mContext.getResources().getColor(R.color.white));
                rate5.setColorFilter(mContext.getResources().getColor(R.color.theme_orange_light));


            }
        });


//		setPaymentSTKListener(new PaymentSTKListener() {
//			@Override
//			public void onObjectReady(String title) {
//
//			}
//
//			@Override
//			public void onDataLoaded(boolean isSuccess) {
//
//				if(isSuccess)
//				{
//					userSubmitPaymentRating(paymentRequestId,""+rating,true);
//				}
//			}
//		});


    }

    public void handleBase64File(Context context, String base64Content, String extension) {
        try {
            if ("pdf".equalsIgnoreCase(extension)) {
                Uri uri = writeTempFileFromBase64(context, base64Content, "doc-", ".pdf");
                if (uri == null) {
                    Toast.makeText(context, context.getString(R.string.couldnotdisplaydocument) + "ERR:101", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(context, ViewPDFActivity.class)
                        .setData(uri)
                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                // If 'context' may be non-Activity (e.g., Service/Adapter), add:
                if (!(context instanceof android.app.Activity)) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }

                context.startActivity(intent);

            } else if (isImageExtension(extension)) {
                downloadBase64Image(context, base64Content, extension); // your existing path
            } else {
                Toast.makeText(context, context.getString(R.string.couldnotdisplaydocument) + "ERR:100", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, context.getString(R.string.couldnotdisplaydocument) + "ERR:200", Toast.LENGTH_SHORT).show();
        }
    }

    private Uri writeTempFileFromBase64(Context context, String base64, String prefix, String suffix) {
        try {
            // Strip data URL prefix if present
            int comma = base64.indexOf(',');
            if (comma != -1 && base64.substring(0, comma).contains("base64")) {
                base64 = base64.substring(comma + 1);
            }

            byte[] bytes = android.util.Base64.decode(base64, android.util.Base64.DEFAULT);

            // Optional: guardrail ~20MB
            if (bytes.length > 20 * 1024 * 1024) {
                return null;
            }

            File out = File.createTempFile(prefix, suffix, context.getCacheDir());
            try (java.io.FileOutputStream fos = new java.io.FileOutputStream(out)) {
                fos.write(bytes);
            }

            return androidx.core.content.FileProvider.getUriForFile(
                    context,
                    BuildConfig.APPLICATION_ID + ".fileprovider",
                    out
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean isImageExtension(String ext) {
        String[] imageExtensions = {"png", "jpg", "jpeg", "bmp", "gif", "webp"};
        for (String s : imageExtensions) {
            if (ext.equalsIgnoreCase(s)) return true;
        }
        return false;
    }

    private void downloadBase64Image(Context context, String base64Content, String extension) {
        try {
            byte[] imageBytes = Base64.decode(base64Content, Base64.DEFAULT);

            // Create file in Downloads
            String fileName = "downloaded_image_" + System.currentTimeMillis() + "." + extension;
            File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File imageFile = new File(downloadDir, fileName);

            FileOutputStream fos = new FileOutputStream(imageFile);
            fos.write(imageBytes);
            fos.close();

            // Scan media
            MediaScannerConnection.scanFile(context, new String[]{imageFile.getAbsolutePath()}, null, null);

            Toast.makeText(context, mContext.getResources().getString(R.string.downloadsuccess) + " " + fileName, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, mContext.getResources().getString(R.string.couldnotdisplaydocument) + "ERR:400", Toast.LENGTH_SHORT).show();
        }
    }


    //Opne WhatsAPp
    public static void openWhatsAppWithMessage(String message) {
        try {
            // Prepare the text message with the required format

            // URL encode the message
            String encodedMessage = URLEncoder.encode(message, "UTF-8");

            // Create the WhatsApp intent
            // Note: You might want to replace the phone number below with your support number
            String phoneNumber = storageObject.retrieveWhatsAppNumberFromPrefs(); // Replace with your support phone number
            // First try: Open with WhatsApp app directly
            boolean whatsappInstalled = isAppInstalled("com.whatsapp");
            if (whatsappInstalled) {

                Intent whatsappIntent = new Intent(Intent.ACTION_VIEW);
                whatsappIntent.setPackage("com.whatsapp");
                String url = "https://wa.me/" + phoneNumber + "?text=" + encodedMessage;
                whatsappIntent.setData(Uri.parse(url));

                try {
                    parentActivity.startActivity(whatsappIntent);
                    return; // Success! Exit the method
                } catch (ActivityNotFoundException e) {
                    // First approach failed, continue to next attempt
                    e.printStackTrace();
                }
            }


            // Second try: Open in browser (WhatsApp Web)
            try {
                String webUrl = "https://wa.me/" + phoneNumber + "?text=" + encodedMessage;


                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl));
                parentActivity.startActivity(browserIntent);
                return; // Success! Exit the method
            } catch (ActivityNotFoundException e) {
                // Browser approach failed, continue to next attempt
                e.printStackTrace();
            }

            // Third try: Use wa.me shortlink format (mobile friendly)
            try {
                String waUrl = "https://wa.me/" + phoneNumber + "?text=" + encodedMessage;
                Intent waIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(waUrl));
                parentActivity.startActivity(waIntent);
                return; // Success! Exit the method
            } catch (ActivityNotFoundException e) {
                // All attempts failed
                e.printStackTrace();
                Toast.makeText(parentActivity, mContext.getResources().getString(R.string.whatsappnotinstalled), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(parentActivity, mContext.getResources().getString(R.string.problemopeningwhatsapp), Toast.LENGTH_SHORT).show();
        }
    }

    // Helper method to check if an app is installed
    private static boolean isAppInstalled(String packageName) {
        PackageManager pm = parentActivity.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


}
