package com.watudrc.app.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.ConfigUpdate;
import com.google.firebase.remoteconfig.ConfigUpdateListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;
import com.watudrc.app.GeneralCommands;
import com.watudrc.app.MainActivity;
import com.watudrc.app.R;
import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.font.FirmaTextView;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;
import com.watudrc.app.util.FirebaseConfig;
import com.watudrc.app.util.ImageUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FragmentFAQsResources extends Fragment implements IWhereMyLocationConstants {

    private View parentView;
   
    private static StoreAndGetFromPrefs storageObject;
    public static Activity parentActivity;
	static Context mContext;

	RelativeLayout loadingView;

    RelativeLayout btnMakePayment,btnSignup,btnTemporaryUnlock;

    FirebaseConfig remoteConfig;
    FirebaseRemoteConfig mFirebaseConfig;
    int default_timeout=8;

    //Sign Up Carousel
    CarouselView previewImagesViewSignUp;
    int[] previewImagesSignUp = {R.drawable.hpl_1,R.drawable.hpl_2,R.drawable.hpl_3,R.drawable.hpl_4 ,R.drawable.hpl_5};
    String carousel_view_sign_up="";
    String has_sign_up_help_carousel="false";
    ViewListener viewListenerSignUp;


    //How to Pay ToolTip
    CarouselView previewImagesViewPay;
    int[] previewImagesPay = {R.drawable.p1,R.drawable.p2,R.drawable.p3,R.drawable.p4,R.drawable.p5, R.drawable.p6};
    String carousel_view_pay="";
    ViewListener viewListenerPay;
    boolean has_how_to_pay_tooltip=true;

    //How To Temporary Unlock
    CarouselView previewImagesViewUnlock;
    int[] previewImagesUnlock = {R.drawable.t1,R.drawable.t2,R.drawable.t3,R.drawable.t4,R.drawable.t5};
    String carousel_view_unlock="";
    ViewListener viewListenerUnlock;
    boolean has_how_to_temporary_unlock_tooltip=true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        parentView = inflater.inflate(R.layout.fragment_faqs, container, false);
        parentActivity = getActivity();
        mContext= parentActivity;

        storageObject = new StoreAndGetFromPrefs(mContext);
        loadingView= (RelativeLayout) parentView.findViewById(R.id.loadingItems);


        btnSignup= parentView.findViewById(R.id.btnSignup);
        btnTemporaryUnlock= parentView.findViewById(R.id.btnTemporaryUnlock);
        btnMakePayment= parentView.findViewById(R.id.btnMakePayment);


        View btnBack= parentView.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity()
                        .getSupportFragmentManager()
                        .popBackStack();
            }
        });


        previewImagesViewSignUp = (CarouselView) parentView.findViewById(R.id.previewImagesView);


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialogSignUpCarousel();
            }
        });

        btnTemporaryUnlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialogUnlockCarousel();
            }
        });

        btnMakePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialogPayCarousel();
            }
        });




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


    public void
    setConfigsSignUp()
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
//							Toast.makeText(mContext, "Fetch and activate succeeded",
//									Toast.LENGTH_SHORT).show();

                    //Sign UP Carousel
                    carousel_view_sign_up = mFirebaseConfig.getString("how_to_signup_tooltip_carousel_v2");
                    has_sign_up_help_carousel = mFirebaseConfig.getString("has_sign_up_help_carousel");
                    if(carousel_view_sign_up!=null && !carousel_view_sign_up.equals("") )
                    {
                        try
                        {
                            JSONObject obj = new JSONObject(carousel_view_sign_up);

                            Log.d("ZeCarousel","ZeCarousel"+obj.toString());
                            refreshCarouselDetailsSignUp(obj);

                        } catch (JSONException e) {
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

                       //Sign Up Help
                        carousel_view_sign_up = mFirebaseConfig.getString("how_to_signup_tooltip_carousel_v2");
                        has_sign_up_help_carousel = mFirebaseConfig.getString("has_sign_up_help_carousel");
                        if (carousel_view_sign_up != null && !carousel_view_sign_up.equals(""))
                        {

                            try {
                                JSONObject obj = new JSONObject(carousel_view_sign_up);
                                Log.d("ZeCarousel", "ZeCarousel" + obj.toString());
                                refreshCarouselDetailsSignUp(obj);

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

    public void setConfigsPay()
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
//							Toast.makeText(mContext, "Fetch and activate succeeded",
//									Toast.LENGTH_SHORT).show();


                    //Pay Carousel

                    carousel_view_pay = mFirebaseConfig.getString("how_to_pay_tooltip_carousel");
                    has_how_to_pay_tooltip = mFirebaseConfig.getBoolean("has_how_to_pay_tooltip");
                    storageObject.setPayTooltip(has_how_to_pay_tooltip);


                    if(carousel_view_pay!=null && !carousel_view_pay.equals("") ) {
                        try {
                            JSONObject obj = new JSONObject(carousel_view_pay);

                            Log.d("ZeCarousel", "ZeCarousel" + obj.toString());
                            refreshCarouselDetailsPay(obj);

                        } catch (JSONException e) {
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




                        //Pay Carousel
                        carousel_view_pay = mFirebaseConfig.getString("how_to_pay_tooltip_carousel");
                        has_how_to_pay_tooltip = mFirebaseConfig.getBoolean("has_how_to_pay_tooltip");
                        storageObject.setPayTooltip(has_how_to_pay_tooltip);


                        if(carousel_view_pay!=null && !carousel_view_pay.equals("") )
                        {
                            try {
                                JSONObject obj = new JSONObject(carousel_view_pay);

                                Log.d("ZeCarousel", "ZeCarousel" + obj.toString());
                                refreshCarouselDetailsPay(obj);

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


    public void setConfigsUnlock()
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
//							Toast.makeText(mContext, "Fetch and activate succeeded",
//									Toast.LENGTH_SHORT).show();


                    //Temporary UNlock
                    carousel_view_unlock = mFirebaseConfig.getString("how_to_temporary_unlock_tooltip_carousel");
                    has_how_to_temporary_unlock_tooltip = mFirebaseConfig.getBoolean("has_how_to_temporary_unlock_tooltip");
                    storageObject.setTemporaryUnlockTooltip(has_how_to_temporary_unlock_tooltip);

                    if(carousel_view_unlock!=null && !carousel_view_unlock.equals("") ) {
                        try {
                            JSONObject obj = new JSONObject(carousel_view_unlock);

                            Log.d("ZeCarousel", "ZeCarousel" + obj.toString());
                            refreshCarouselDetailsUnlock(obj);

                        } catch (JSONException e) {
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


                        //Temporary UNlock
                        carousel_view_unlock = mFirebaseConfig.getString("how_to_temporary_unlock_tooltip_carousel");
                        has_how_to_temporary_unlock_tooltip = mFirebaseConfig.getBoolean("has_how_to_temporary_unlock_tooltip");
                        storageObject.setTemporaryUnlockTooltip(has_how_to_temporary_unlock_tooltip);

                        if(carousel_view_unlock!=null && !carousel_view_unlock.equals("") ) {
                            try {
                                JSONObject obj = new JSONObject(carousel_view_unlock);

                                Log.d("ZeCarousel", "ZeCarousel" + obj.toString());
                                refreshCarouselDetailsUnlock(obj);

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

    //Sign UP Carousel
    public void openDialogSignUpCarousel()
    {
        View contentView = parentActivity.getLayoutInflater().inflate(R.layout.dialog_content_pay_carousel, null);


        final DialogPlus dialogPlus = new DialogPlus.Builder(parentActivity)
                .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(contentView))
                .setGravity(DialogPlus.Gravity.CENTER)
                .setBackgroundColorResourceId(R.color.black)
                .setCancelable(true)
                .create();


        //Content View
        View content = dialogPlus.getHolderView();

        RelativeLayout loadingView= (RelativeLayout) content.findViewById(R.id.loadingItems);

        RelativeLayout btnBack= (RelativeLayout) content.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPlus.dismiss();
            }
        });




        previewImagesViewSignUp = (CarouselView) contentView.findViewById(R.id.previewImagesView);


        try
        {
            setConfigsSignUp();
            setCarouselDetailsSignUp();

        }catch(Exception e){}




        dialogPlus.show();
    }


    public void setCarouselDetailsSignUp()
    {

        viewListenerSignUp = new ViewListener() {
            @Override
            public View setViewForPosition(int position) {
                View customView = getLayoutInflater().inflate(R.layout.carousel_view_custom, null);
                //set view attributes here

                ImageView imageView = customView.findViewById(R.id.imageView);
                if(previewImagesSignUp!=null && previewImagesSignUp.length >= position) imageView.setImageResource(previewImagesSignUp[position]);

                FirmaTextView mainMsg= (FirmaTextView) customView.findViewById(R.id.mainMsg);
                FirmaTextView subMsg= (FirmaTextView) customView.findViewById(R.id.subMsg);

                mainMsg.setVisibility(View.GONE);
                subMsg.setVisibility(View.GONE);


                return customView;
            }
        };

        previewImagesViewSignUp.setPageCount(previewImagesSignUp.length);
        previewImagesViewSignUp.setViewListener(viewListenerSignUp);



    }

    public void refreshCarouselDetailsSignUp(JSONObject jsonObject) throws JSONException {
        JSONArray drcArray =new JSONArray();
        JSONArray dataArray =new JSONArray();



        // Check if the object contains "drc" data
        if (jsonObject.has("drc")) {
            // Get the "drc" array

            JSONObject newJsonObject= jsonObject.getJSONObject("drc");
            if(newJsonObject.has("fr") && storageObject.retrieveLanguageFromPrefs().equals("fr"))
            {
                drcArray= newJsonObject.getJSONArray("fr");


            }

            else
            {
                drcArray= newJsonObject.getJSONArray("en");
            }

        }


        dataArray=drcArray;

        if(dataArray.length() > 0)
        {
            previewImagesViewSignUp.setSlideInterval(default_timeout*1000);//Set Slide Interval
            previewImagesViewSignUp.setPageCount(dataArray.length());

            JSONArray finalDataArray = dataArray;
            viewListenerSignUp = new ViewListener() {
                @Override
                public View setViewForPosition(int position) {
                    View customView = getLayoutInflater().inflate(R.layout.carousel_view_custom, null);
                    //set view attributes here

                    ImageView imageView = customView.findViewById(R.id.imageView);

                    FirmaTextView mainMsg= (FirmaTextView) customView.findViewById(R.id.mainMsg);
                    FirmaTextView subMsg= (FirmaTextView) customView.findViewById(R.id.subMsg);


                    try {
                        JSONObject drcObject = finalDataArray.getJSONObject(position);

                        // Extract individual items from the "" object
                        String title = drcObject.getString("title");
                        String message = drcObject.getString("message");
                        String image = drcObject.getString("image");
                        String timeout_seconds = drcObject.getString("timeout_seconds");

                        mainMsg.setVisibility(View.GONE);
                        subMsg.setVisibility(View.GONE);

                        if(timeout_seconds!=null && !timeout_seconds.equals("") )
                        {

                            try
                            {
                                int seconds= Integer.parseInt(timeout_seconds);
                                if(seconds > 0)
                                {
                                    default_timeout=seconds;
                                    previewImagesViewSignUp.setSlideInterval(default_timeout*1000);
                                }

                            }catch(Exception e)
                            {
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

                                if(previewImagesSignUp !=null && previewImagesSignUp.length >= position && imageView!=null )imageView.setImageResource(previewImagesSignUp[position]);



                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {
                                if(previewImagesSignUp !=null && previewImagesSignUp.length >= position && imageView!=null )imageView.setImageResource(previewImagesSignUp[position]);

                            }
                        });



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    return customView;
                }
            };

            previewImagesViewSignUp.setPageCount(dataArray.length());
            previewImagesViewSignUp.setViewListener(viewListenerSignUp);

        }


    }


    //Pay Carousel
    public void openDialogPayCarousel()
    {
        View contentView = parentActivity.getLayoutInflater().inflate(R.layout.dialog_content_pay_carousel, null);


        final DialogPlus dialogPlus = new DialogPlus.Builder(parentActivity)
                .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(contentView))
                .setGravity(DialogPlus.Gravity.CENTER)
                .setBackgroundColorResourceId(R.color.black)
                .setCancelable(true)
                .create();


        //Content View
        View content = dialogPlus.getHolderView();

        RelativeLayout loadingView= (RelativeLayout) content.findViewById(R.id.loadingItems);

        RelativeLayout btnBack= (RelativeLayout) content.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPlus.dismiss();
            }
        });




        previewImagesViewPay= (CarouselView) contentView.findViewById(R.id.previewImagesView);


        try
        {
            setConfigsPay();
            setCarouselDetailsPay();

        }catch(Exception e){}




        dialogPlus.show();
    }


    public void setCarouselDetailsPay()
    {

        viewListenerPay = new ViewListener() {
            @Override
            public View setViewForPosition(int position) {
                View customView = getLayoutInflater().inflate(R.layout.carousel_view_custom, null);
                //set view attributes here

                ImageView imageView = customView.findViewById(R.id.imageView);
                if(previewImagesPay!=null && previewImagesPay.length >= position) imageView.setImageResource(previewImagesPay[position]);

                FirmaTextView mainMsg= (FirmaTextView) customView.findViewById(R.id.mainMsg);
                FirmaTextView subMsg= (FirmaTextView) customView.findViewById(R.id.subMsg);

                mainMsg.setVisibility(View.GONE);
                subMsg.setVisibility(View.GONE);


                return customView;
            }
        };

        previewImagesViewPay.setPageCount(previewImagesPay.length);
        previewImagesViewPay.setViewListener(viewListenerPay);



    }

    public void refreshCarouselDetailsPay(JSONObject jsonObject) throws JSONException {
        JSONArray drcArray =new JSONArray();
        JSONArray dataArray =new JSONArray();

        // Check if the object contains "drc" data
        if (jsonObject.has("drc")) {
            // Get the "drc" array

            JSONObject newJsonObject= jsonObject.getJSONObject("drc");
            if(newJsonObject.has("fr") && storageObject.retrieveLanguageFromPrefs().equals("fr"))
            {
                drcArray= newJsonObject.getJSONArray("fr");

            }

            else
            {
                drcArray= newJsonObject.getJSONArray("en");
            }

        }




        dataArray=drcArray;

//		Log.d("ZeArrayzFin","ZeArrayzFIn"+storageObject.retrieveLanguageFromPrefs()+":"+dataArray.toString());

        if(dataArray.length() > 0 && previewImagesViewPay!=null )
        {
            previewImagesViewPay.setSlideInterval(default_timeout*1000);//Set Slide Interval
            previewImagesViewPay.setPageCount(dataArray.length());

            JSONArray finalDataArray = dataArray;
            viewListenerPay = new ViewListener() {
                @Override
                public View setViewForPosition(int position) {
                    View customView = getLayoutInflater().inflate(R.layout.carousel_view_custom, null);
                    //set view attributes here

                    ImageView imageView = customView.findViewById(R.id.imageView);

                    FirmaTextView mainMsg= (FirmaTextView) customView.findViewById(R.id.mainMsg);
                    FirmaTextView subMsg= (FirmaTextView) customView.findViewById(R.id.subMsg);


                    try {
                        JSONObject drcObject = finalDataArray.getJSONObject(position);

                        // Extract individual items from the "" object
                        String title = drcObject.getString("title");
                        String message = drcObject.getString("message");
                        String image = drcObject.getString("image");
                        String timeout_seconds = drcObject.getString("timeout_seconds");

                        mainMsg.setVisibility(View.GONE);
                        subMsg.setVisibility(View.GONE);

                        if(timeout_seconds!=null && !timeout_seconds.equals("") )
                        {

                            try
                            {
                                int seconds= Integer.parseInt(timeout_seconds);
                                if(seconds > 0)
                                {
                                    default_timeout=seconds;
                                    previewImagesViewPay.setSlideInterval(default_timeout*1000);
                                }

                            }catch(Exception e)
                            {
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
                                if(previewImagesPay !=null && previewImagesPay.length >= position && imageView!=null )imageView.setImageResource(previewImagesPay[position]);

                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {
                                if(previewImagesPay !=null && previewImagesPay.length >= position && imageView!=null )imageView.setImageResource(previewImagesPay[position]);
                            }
                        });



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    return customView;
                }
            };

            previewImagesViewPay.setPageCount(dataArray.length());
            previewImagesViewPay.setViewListener(viewListenerPay);

        }




    }

    //Temporary Unlock
    public void openDialogUnlockCarousel()
    {
        View contentView = parentActivity.getLayoutInflater().inflate(R.layout.dialog_content_pay_carousel, null);


        final DialogPlus dialogPlus = new DialogPlus.Builder(parentActivity)
                .setContentHolder(new com.orhanobut.dialogplus.ViewHolder(contentView))
                .setGravity(DialogPlus.Gravity.CENTER)
                .setBackgroundColorResourceId(R.color.black)
                .setCancelable(true)
                .create();


        //Content View
        View content = dialogPlus.getHolderView();

        RelativeLayout loadingView= (RelativeLayout) content.findViewById(R.id.loadingItems);

        RelativeLayout btnBack= (RelativeLayout) content.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPlus.dismiss();
            }
        });




        previewImagesViewUnlock= (CarouselView) contentView.findViewById(R.id.previewImagesView);


        try
        {
            setConfigsUnlock();
            setCarouselDetailsUnlock();

        }catch(Exception e){}




        dialogPlus.show();
    }


    public void setCarouselDetailsUnlock()
    {

        viewListenerUnlock = new ViewListener() {
            @Override
            public View setViewForPosition(int position) {
                View customView = getLayoutInflater().inflate(R.layout.carousel_view_custom, null);
                //set view attributes here

                ImageView imageView = customView.findViewById(R.id.imageView);
                if(previewImagesUnlock!=null && previewImagesUnlock.length >= position) imageView.setImageResource(previewImagesUnlock[position]);

                FirmaTextView mainMsg= (FirmaTextView) customView.findViewById(R.id.mainMsg);
                FirmaTextView subMsg= (FirmaTextView) customView.findViewById(R.id.subMsg);

                mainMsg.setVisibility(View.GONE);
                subMsg.setVisibility(View.GONE);


                return customView;
            }
        };

        previewImagesViewUnlock.setPageCount(previewImagesUnlock.length);
        previewImagesViewUnlock.setViewListener(viewListenerUnlock);



    }

    public void refreshCarouselDetailsUnlock(JSONObject jsonObject) throws JSONException {
        JSONArray drcArray =new JSONArray();
        JSONArray dataArray =new JSONArray();

        // Check if the object contains "drc" data
        if (jsonObject.has("drc")) {
            // Get the "drc" array


            JSONObject newJsonObject= jsonObject.getJSONObject("drc");
            if(newJsonObject.has("fr") && storageObject.retrieveLanguageFromPrefs().equals("fr"))
            {
                drcArray= newJsonObject.getJSONArray("fr");


            }

            else
            {
                drcArray= newJsonObject.getJSONArray("en");
            }

        }




        dataArray=drcArray;

//		Log.d("ZeArrayzFin","ZeArrayzFIn"+storageObject.retrieveLanguageFromPrefs()+":"+dataArray.toString());

        if(dataArray.length() > 0 && previewImagesViewUnlock!=null )
        {
            previewImagesViewUnlock.setSlideInterval(default_timeout*1000);//Set Slide Interval
            previewImagesViewUnlock.setPageCount(dataArray.length());

            JSONArray finalDataArray = dataArray;
            viewListenerUnlock = new ViewListener() {
                @Override
                public View setViewForPosition(int position) {
                    View customView = getLayoutInflater().inflate(R.layout.carousel_view_custom, null);
                    //set view attributes here

                    ImageView imageView = customView.findViewById(R.id.imageView);

                    FirmaTextView mainMsg= (FirmaTextView) customView.findViewById(R.id.mainMsg);
                    FirmaTextView subMsg= (FirmaTextView) customView.findViewById(R.id.subMsg);


                    try {
                        JSONObject drcObject = finalDataArray.getJSONObject(position);

                        // Extract individual items from the "" object
                        String title = drcObject.getString("title");
                        String message = drcObject.getString("message");
                        String image = drcObject.getString("image");
                        String timeout_seconds = drcObject.getString("timeout_seconds");

                        mainMsg.setVisibility(View.GONE);
                        subMsg.setVisibility(View.GONE);

                        if(timeout_seconds!=null && !timeout_seconds.equals("") )
                        {

                            try
                            {
                                int seconds= Integer.parseInt(timeout_seconds);
                                if(seconds > 0)
                                {
                                    default_timeout=seconds;
                                    previewImagesViewUnlock.setSlideInterval(default_timeout*1000);
                                }

                            }catch(Exception e)
                            {
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
                                if(previewImagesUnlock !=null && previewImagesUnlock.length >= position && imageView!=null )imageView.setImageResource(previewImagesUnlock[position]);

                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {
                                if(previewImagesUnlock !=null && previewImagesUnlock.length >= position && imageView!=null )imageView.setImageResource(previewImagesUnlock[position]);
                            }
                        });



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    return customView;
                }
            };

            previewImagesViewUnlock.setPageCount(dataArray.length());
            previewImagesViewUnlock.setViewListener(viewListenerUnlock);

        }




    }


}
