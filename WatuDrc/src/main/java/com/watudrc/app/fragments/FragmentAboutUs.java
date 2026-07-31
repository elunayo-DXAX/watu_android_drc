package com.watudrc.app.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.ConfigUpdate;
import com.google.firebase.remoteconfig.ConfigUpdateListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.watudrc.app.AboutUsActivity;
import com.watudrc.app.GeneralCommands;
import com.watudrc.app.MainActivity;
import com.watudrc.app.R;
import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.font.FirmaTextView;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;
import com.watudrc.app.util.FirebaseConfig;
import com.watudrc.app.util.ImageUtil;
import com.ypyproductions.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class FragmentAboutUs extends Fragment implements IWhereMyLocationConstants {

    private View parentView;
   
    private static StoreAndGetFromPrefs storageObject;
    public static AboutUsActivity parentActivity;
	static Context mContext;

	RelativeLayout loadingView;

    RelativeLayout btnFacebook,btnTwitter,btnInstagram,btnCall,btnEmail,btnAbout;

    FirmaTextView aboutText,aboutContent;
    ImageView aboutImage;

    FirebaseConfig remoteConfig;
    FirebaseRemoteConfig mFirebaseConfig;
    String about_us="";
    String about_us_sw="";
    String about_us_fr="";
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        parentView = inflater.inflate(R.layout.fragment_about_us, container, false);
        parentActivity = (AboutUsActivity)getActivity();
        mContext= parentActivity.getApplicationContext();
        storageObject = new StoreAndGetFromPrefs(mContext);
        loadingView= parentView.findViewById(R.id.loadingItems);

        btnFacebook= parentView.findViewById(R.id.btnFacebook);
        btnTwitter= parentView.findViewById(R.id.btnTwitter);
        btnInstagram= parentView.findViewById(R.id.btnInstagram);
        btnCall= parentView.findViewById(R.id.btnCall);
        btnEmail= parentView.findViewById(R.id.btnEmail);

        aboutText= parentView.findViewById(R.id.aboutText);
        aboutImage= parentView.findViewById(R.id.aboutImage);

        aboutContent= parentView.findViewById(R.id.aboutContent);


        btnAbout= parentView.findViewById(R.id.btnAbout);

        btnAbout.setOnClickListener(v -> {

            GeneralCommands.logEvent(parentActivity,"click_about_website","click_about_website");


            String url = URL_SITE; // Replace with your URL
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });

        btnFacebook.setOnClickListener(v -> {

            GeneralCommands.logEvent(parentActivity,"click_about_facebook","click_about_facebook");

            String url = URL_FACEBOOK; // Replace with your URL
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });

        btnTwitter.setOnClickListener(v -> {

            GeneralCommands.logEvent(parentActivity,"click_about_twitter","click_about_twitter");

            String url = URL_TWITTER; // Replace with your URL
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });

        btnInstagram.setOnClickListener(v -> {

            GeneralCommands.logEvent(parentActivity,"click_about_instagram","click_about_instagram");

            String url = URL_INSTAGRAM; // Replace with your URL
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });

        btnCall.setOnClickListener(v -> openCall());


        btnEmail.setOnClickListener(v -> {

            GeneralCommands.logEvent(parentActivity,"click_about_email","click_about_email");

            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822"); // Specify the MIME type for email
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{URL_EMAIL});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, mContext.getResources().getString(R.string.hellowatu));
            emailIntent.putExtra(Intent.EXTRA_TEXT, mContext.getResources().getString(R.string.supportinquiry));
            startActivity(Intent.createChooser(emailIntent, "Send email"));
        });

        setConfigs();
       

		
        return parentView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that we are currently visible
        if (this.isVisible()) {
            // If we are becoming invisible, then...
            if (isVisibleToUser) {
                GeneralCommands.logEvent(parentActivity,"view_event","view_about_us");

                // TODO stop audio playback
            }
        }
    }

    public void goToPage()
    {
        Intent mIntent = new Intent(parentActivity, MainActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
        GeneralCommands.goToPage(parentActivity, mIntent,false);

    }

    public  void setConfigs()
    {
        //Remote Configs
        remoteConfig= new FirebaseConfig();
        mFirebaseConfig=remoteConfig.getConfigs();
        mFirebaseConfig.fetchAndActivate().addOnCompleteListener(parentActivity, new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    boolean updated = task.getResult();
//							Toast.makeText(mContext, "Fetch and activate succeeded",
//									Toast.LENGTH_SHORT).show();

                    about_us = mFirebaseConfig.getString("about_us");
                    about_us_sw = mFirebaseConfig.getString("about_us_sw");
                    about_us_fr = mFirebaseConfig.getString("about_us_fr");
                    if(storageObject.retrieveLanguageFromPrefs().equals("fr"))
                    {
                        about_us=about_us_fr;
                    }

                    if(!StringUtils.isStringEmpty(about_us) ) {
                        try {
                            JSONObject obj = new JSONObject(about_us);

                            Log.d("ZeABout", "ZeABout" + obj);
                            refreshDetails(obj);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }



                } else {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.fetchfailed),
                            Toast.LENGTH_SHORT).show();
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
                        about_us = mFirebaseConfig.getString("about_us");
                        about_us_sw = mFirebaseConfig.getString("about_sw");
                        about_us_fr = mFirebaseConfig.getString("about_fr");
                        if(storageObject.retrieveLanguageFromPrefs().equals("fr"))
                        {
                            about_us=about_us_fr;
                        }


                        try {
                            JSONObject obj = new JSONObject(about_us);

                            Log.d("ZeABout","ZeABout"+ obj);
                            refreshDetails(obj);

                        } catch (JSONException e) {
                            e.printStackTrace();
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

    public void refreshDetails(JSONObject jsonObject) throws JSONException {
        JSONObject drcArray =new JSONObject();
        JSONObject dataArray =new JSONObject();



        // Check if the object contains "drc" data
        if (jsonObject.has("drc")) {
            // Get the "drc" object
            drcArray = jsonObject.getJSONObject("drc");
        }




        dataArray=drcArray;

        if(dataArray.length() > 0)
        {
            aboutText.setText(dataArray.getString("title"));
            aboutContent.setText(dataArray.getString("message"));

            String urlData=dataArray.getString("image");

            ImageUtil.displayImage(aboutImage, urlData, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                        ImageView vm = (ImageView) view;
                        vm.setImageResource(R.drawable.ic_about);
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });

        }

    }

    public void openCall() {
        int permissionCheck = ContextCompat.checkSelfPermission(parentActivity, Manifest.permission.CALL_PHONE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    parentActivity,
                    new String[]{Manifest.permission.CALL_PHONE},
                    123);
        } else {

            //All Good Make Call
            GeneralCommands.logEvent(parentActivity,"click_about_call","click_about_call");

            String phoneNumber = mContext.getResources().getString(R.string.supportnumbers); // Replace with the phone number you want to dial
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 123:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    openCall();
                } else {
//                    Log.d("TAG", "Call Permission Not Granted");
                    Toast.makeText(parentActivity, mContext.getResources().getString(R.string.permallow), Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }
       
}
