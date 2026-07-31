package com.watudrc.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.ConfigUpdate;
import com.google.firebase.remoteconfig.ConfigUpdateListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException;
import com.hbb20.CountryCodePicker;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;
import com.watudrc.app.ActivateAccountActivity;
import com.watudrc.app.ChooseOTPActivity;
import com.watudrc.app.GeneralCommands;
import com.watudrc.app.LoginActivity;
import com.watudrc.app.R;
import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.font.FirmaTextView;
import com.watudrc.app.mobileservices.GetData;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;
import com.watudrc.app.util.FirebaseConfig;
import com.watudrc.app.util.ImageUtil;
import com.watudrc.app.util.PhoneNumberFormatter;
import com.watudrc.app.view.MaterialRippleLayout;
import com.ypyproductions.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;

public class FragmentActivateAccount extends Fragment implements IWhereMyLocationConstants {

    public static ActivateAccountActivity parentActivity;
    static Context mContext;
    private static StoreAndGetFromPrefs storageObject;
    RelativeLayout loadingView;
    RelativeLayout btnSubmit;
    boolean isPhoneNumberValid;
    CountryCodePicker ccp;
    EditText phoneNumberEditText, idNumberEditText;
    //Back
    RelativeLayout btnLogin;
    //Preview Images
    RelativeLayout guidanceView;
    int default_timeout = 8;
    CarouselView previewImagesView;
    int[] previewImages = {R.drawable.hp_1, R.drawable.hp_2, R.drawable.hp_3, R.drawable.hp_4, R.drawable.hp_5, R.drawable.hp_5};
    RelativeLayout btnChallenge;
    FirebaseConfig remoteConfig;
    FirebaseRemoteConfig mFirebaseConfig;
    String carousel_view = "";
    String has_sign_up_help_carousel = "false";
    ViewListener viewListener;
    MaterialRippleLayout challengeRips;
    private View parentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_activate_account, container, false);
        parentActivity = (ActivateAccountActivity) getActivity();
        mContext = parentActivity.getApplicationContext();
        storageObject = new StoreAndGetFromPrefs(mContext);
        loadingView = parentView.findViewById(R.id.loadingItems);

        btnLogin = parentView.findViewById(R.id.btnLogin);

        previewImagesView = parentView.findViewById(R.id.previewImagesView);
        btnChallenge = parentView.findViewById(R.id.btnChallenge);
        guidanceView = parentView.findViewById(R.id.guidanceView);
        challengeRips = parentView.findViewById(R.id.challengeRips);

        btnLogin.setOnClickListener(view -> goToLoginPage());

        idNumberEditText = parentView.findViewById(R.id.idNumberEditText);

        ccp = parentView.findViewById(R.id.ccp);
        phoneNumberEditText = parentView.findViewById(R.id.phoneNumberEditText);

        ccp.registerCarrierNumberEditText(phoneNumberEditText);
        ccp.setPhoneNumberValidityChangeListener(isValidNumber -> isPhoneNumberValid = isValidNumber);

        if (storageObject.retrieveUserPhoneNumberFromPrefs() != "")
            ccp.setFullNumber(storageObject.retrieveUserPhoneNumberFromPrefs());
        if (!phoneNumberEditText.getText().toString().isEmpty() && !phoneNumberEditText.getText().toString().startsWith("0")) {
            phoneNumberEditText.setText("0" + phoneNumberEditText.getText().toString());
        }
        PhoneNumberFormatter.applyFormatting(phoneNumberEditText);

        btnSubmit = parentView.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(view -> {

            if(phoneNumberEditText.getText().toString().length()<1)
            {
                String errorMsg= mContext.getResources().getString(R.string.entervalidphonenumber);
                phoneNumberEditText.setError(errorMsg);
                Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();


            }
            else {


                if(GeneralCommands.isDemo(phoneNumberEditText.getText().toString().replaceAll("\\s+", ""),"1234"))isPhoneNumberValid=true;



                String userNumber= ccp.getFullNumber();
                idNumberEditText.setError(null);
                storageObject.saveUserPhoneNumberToPrefs(userNumber);
                storageObject.saveUserIDNumberToPrefs(idNumberEditText.getText().toString());
                parentActivity.hideKeyboard();

                GetData ob1 = new GetData();
                ob1.initialize(parentActivity,loadingView);
                try {
                    ob1.userRegister(userNumber,false);
                    ob1.setUserRegisterListener(new GetData.UserRegisterListener() {
                        @Override
                        public void onObjectReady(String title) {

                        }

                        @Override
                        public void onDataLoaded(String deviceId) {
                            if(!StringUtils.isStringEmpty(deviceId))
                            {
                                ob1.userGetOTPChannels(false);
                                ob1.setUserChannelsListener(new GetData.UserChannelsListener() {
                                    @Override
                                    public void onObjectReady(String title) {

                                    }

                                    @Override
                                    public void onDataLoaded(String[] channels) {

                                        if(channels!=null && channels.length>0)
                                        {
                                            //Go To OTP Page Once Channels Have Been Saved
                                            goToChooseOTPPage();
                                        }


                                    }
                                });

                            }

                        }
                    });
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }


        });


        btnChallenge.setOnClickListener(v -> {

            //All Good Make Call
            GeneralCommands.logEvent(parentActivity, "click_sign_up_page_issues_signing_up", "click_sign_up_page_issues_signing_up");

            guidanceView.setVisibility(View.VISIBLE);
            previewImagesView.setVisibility(View.VISIBLE);
            btnChallenge.setVisibility(View.GONE);
        });


        try {
            setCarouselDetails();
            setConfigs();

        } catch (Exception e) {
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

                // TODO stop audio playback
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //Log Event
        if (loadingView != null) loadingView.setVisibility(View.GONE);

    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void goToLoginPage() {
        Intent mIntent = new Intent(parentActivity, LoginActivity.class);
        mIntent.putExtra(KEY_START_FROM, "splash");
        GeneralCommands.goToPage(parentActivity, mIntent, true);

    }


    public void setConfigs() {
        //Remote Configs
        remoteConfig = new FirebaseConfig();
        mFirebaseConfig = remoteConfig.getConfigs();
        mFirebaseConfig.fetchAndActivate().addOnCompleteListener(parentActivity, new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    boolean updated = task.getResult();
//							Toast.makeText(mContext, "Fetch and activate succeeded",
//									Toast.LENGTH_SHORT).show();

                    carousel_view = mFirebaseConfig.getString("translated_signup_help_carousel_v2");
                    has_sign_up_help_carousel = mFirebaseConfig.getString("has_sign_up_help_carousel");

                    if (has_sign_up_help_carousel != null && !has_sign_up_help_carousel.equals("") && has_sign_up_help_carousel.equals("true")) {
                        challengeRips.setVisibility(View.VISIBLE);
                    } else {
                        challengeRips.setVisibility(View.GONE);

                    }

                    if (carousel_view != null && !carousel_view.equals("")) {
                        try {
                            JSONObject obj = new JSONObject(carousel_view);

                            Log.d("ZeCarousel", "ZeCarousel" + obj);
                            refreshCarouselDetails(obj);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                } else {
                    Log.d("ZeFetchFailed", "ZeFetchFailed");
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
                        carousel_view = mFirebaseConfig.getString("translated_signup_help_carousel_v2");

                        has_sign_up_help_carousel = mFirebaseConfig.getString("has_sign_up_help_carousel");

                        if (has_sign_up_help_carousel != null && !has_sign_up_help_carousel.equals("") && has_sign_up_help_carousel.equals("true")) {
                            challengeRips.setVisibility(View.VISIBLE);
                        } else {
                            challengeRips.setVisibility(View.GONE);

                        }

                        if (carousel_view != null && !carousel_view.equals("")) {

                            try {
                                JSONObject obj = new JSONObject(carousel_view);
                                Log.d("ZeCarousel", "ZeCarousel" + obj);
                                refreshCarouselDetails(obj);

                            } catch (JSONException e) {
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

    public void setCarouselDetails() {

        viewListener = position -> {
            View customView = getLayoutInflater().inflate(R.layout.carousel_view_custom, null);
            //set view attributes here

            ImageView imageView = customView.findViewById(R.id.imageView);
            if (previewImages != null && previewImages.length >= position)
                imageView.setImageResource(previewImages[position]);


            FirmaTextView mainMsg = customView.findViewById(R.id.mainMsg);
            FirmaTextView subMsg = customView.findViewById(R.id.subMsg);

            mainMsg.setVisibility(View.GONE);
            subMsg.setVisibility(View.GONE);

            return customView;
        };

        previewImagesView.setViewListener(viewListener);
        previewImagesView.setPageCount(previewImages.length);
    }

    public void refreshCarouselDetails(JSONObject jsonObject) throws JSONException {
        JSONArray drcArray = new JSONArray();
        JSONArray dataArray = new JSONArray();
        // Check if the object contains "drc" data

        if (jsonObject.has("drc")) {
            JSONObject newJsonObject = jsonObject.getJSONObject("drc");
            if (newJsonObject.has("fr") && storageObject.retrieveLanguageFromPrefs().equals("fr")) {
				drcArray = newJsonObject.getJSONArray("fr");
            } else {
				drcArray = newJsonObject.getJSONArray("en");
            }
        }

        dataArray = drcArray;

        Log.d("ZeArrayzFin", "ZeArrayzFIn" + storageObject.retrieveLanguageFromPrefs() + ":" + dataArray);

        if (dataArray.length() > 0) {
            previewImagesView.setSlideInterval(default_timeout * 1000);//Set Slide Interval
            previewImagesView.setPageCount(dataArray.length());

            JSONArray finalDataArray = dataArray;
            viewListener = position -> {
                View customView = getLayoutInflater().inflate(R.layout.carousel_view_custom, null);
                //set view attributes here

                ImageView imageView = customView.findViewById(R.id.imageView);

                FirmaTextView mainMsg = customView.findViewById(R.id.mainMsg);
                FirmaTextView subMsg = customView.findViewById(R.id.subMsg);


                try {
                    JSONObject kenyaObject = finalDataArray.getJSONObject(position);

                    // Extract individual items from the "" object
                    String title = kenyaObject.getString("title");
                    String message = kenyaObject.getString("message");
                    String image = kenyaObject.getString("image");
                    String timeout_seconds = kenyaObject.getString("timeout_seconds");

                    mainMsg.setVisibility(View.GONE);
                    subMsg.setVisibility(View.GONE);

                    if (timeout_seconds != null && !timeout_seconds.equals("")) {

                        try {
                            int seconds = Integer.parseInt(timeout_seconds);
                            if (seconds > 0) {
                                default_timeout = seconds;
                                previewImagesView.setSlideInterval(default_timeout * 1000);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    // Do something with the extracted title and message
                    ImageUtil.displayLessRoundImage(imageView, image, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {
                            if (previewImages != null && previewImages.length >= position && imageView != null)
                                imageView.setImageResource(previewImages[position]);

                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {
                            if (previewImages != null && previewImages.length >= position && imageView != null)
                                imageView.setImageResource(previewImages[position]);
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return customView;
            };

            previewImagesView.setPageCount(dataArray.length());
            previewImagesView.setViewListener(viewListener);

        }
    }

    public void goToChooseOTPPage() {
        Intent mIntent = new Intent(parentActivity, ChooseOTPActivity.class);
        mIntent.putExtra(KEY_START_FROM, "splash");
        GeneralCommands.goToPage(parentActivity, mIntent, true);

    }

}
