package com.watudrc.app.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.ConfigUpdate;
import com.google.firebase.remoteconfig.ConfigUpdateListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException;
import com.hbb20.CountryCodePicker;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.watudrc.app.GeneralCommands;
import com.watudrc.app.LoanCompleteActivity;
import com.watudrc.app.MainActivity;
import com.watudrc.app.PaymentProcessesActivity;
import com.watudrc.app.R;
import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.font.FirmaTextView;
import com.watudrc.app.mobileservices.GetData;
import com.watudrc.app.objects.AssetObject;
import com.watudrc.app.objects.LoanCompleteIconObject;
import com.watudrc.app.objects.ProvidersItem;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;
import com.watudrc.app.util.FirebaseConfig;
import com.watudrc.app.util.ImageUtil;
import com.watudrc.app.util.PhoneNumberFormatter;
import com.watudrc.app.view.MaterialRippleLayout;
import com.watudrc.app.view.NumberThousandsTextWatcher;
import com.ypyproductions.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static com.bumptech.glide.load.resource.gif.GifDrawable.LOOP_FOREVER;

public class FragmentPayment extends Fragment implements IWhereMyLocationConstants {

    private View parentView;

    private static StoreAndGetFromPrefs storageObject;
    public static MainActivity parentActivity;
    static Context mContext;
    RelativeLayout loadingView;

    RelativeLayout btnBack;

    //    RelativeLayout btnContinue;
    AssetObject currentAsset;

    String payingFor="";
    EditText amountEditText;
//    private SwipeRefreshLayout swipeRefreshLayout;

    RelativeLayout btnDaily,btnWeekly,btnMonthly;
    ImageView dailyCheck,weeklyCheck,monthlyCheck;
    FirmaTextView dailyH,weeklyH,monthlyH;
    FirmaTextView dailyAmount,weeklyAmount,monthlyAmount;
    FirmaTextView dailyPaid,weeklyPaid,monthlyPaid;

    boolean isPhoneNumberValid;
    CountryCodePicker ccp;
    EditText phoneNumberEditText;
    double payAmountValue=0.0;
    String fromNumberValue="";
    String payOption="daily";

    double dailyFee,weeklyFee,monthlyFee;

    static LinearLayout switchView;

    RelativeLayout btnMyNumber,btnOtherNumber, otherNumberView;
    ImageView myNumberDiv,otherNumberDiv;

    FirmaTextView otherNumberText,myNumberText;

    FirmaTextView dailyAmountCurrency,weeklyAmountCurrency,monthlyAmountCurrency;
    boolean isAnotherNumber=false;

    Spinner listAssets;
    static ArrayAdapter<String> mAdapterAssets;
    ArrayList<AssetObject> listData;
    String itemPositionName="";
    String loanNumber="";
    String paymentProvider="safaricom";
    String paymentProviderID="";
    String paymentProviderName="";
    String currencyCode="USD";

    RelativeLayout assetRips;
    static FirmaTextView headerTitle;

    boolean isProgrammaticChange = false;

    //    //Slider
    LinearLayout sliderView, buttonRipView;
    SeekBar.OnSeekBarChangeListener seekbarListener;

    boolean show_slider=false;
    int slider_default=1;
    //Remote COnfigs
    String has_slider="";
    double slider_max_value =7;
    FirebaseConfig remoteConfig;
    FirebaseRemoteConfig mFirebaseConfig;
    SeekBar seekBar;
    RelativeLayout btnPay30Days;
    double payDays=1.1;

    double numberOfDays;
    FirmaTextView choosePlan;
    private RelativeLayout rlMarker;

    boolean isPayMonthly=false;

    int extraMargin = 0;

//    //End SLider

    //Temporary Unlock

    int temporary_unlock_max_attempts=3;

    int pay_for_another_attempts=5;

    //Maintenance
    RelativeLayout mainContent,btnAccept;
    ScrollView maintenanceView;
    FirmaTextView aboutContent;
    String maintenance_message="";
    String maintenance_mode= "false";

    //    //Change Number View
    RelativeLayout changeNumberView;
    static boolean show_change_number_view=false;
    RelativeLayout defaultNumberView,defaultNumberEditView;
    FirmaTextView defaultNumber;
    RelativeLayout btnChangeNumber;
    EditText defaultPhoneNumberEditText;
    CountryCodePicker ccd;

    //LoadingGiF WIn Moments
    ImageView loadingGif;
    boolean win_moment_has_win_animation=false;
    boolean win_moment_has_animation_confetti=false;
    boolean win_moment_has_animation_confetti_and_scribbles=false;

    String user, method, device, pagaNumber, amount;


    //Loan Complete View
    RelativeLayout loanCompleteView;
    LinearLayout activeLoanView;
    FirmaTextView loanCongratsText;
    ImageView loanCongratsImage;


    //    //Other Payment
    RelativeLayout btnMpesa,btnAirtel,btnAfrimoney;
    String ussdCode="";
    LinearLayout paymentButtons,paymentWeight;
    RelativeLayout btnPayMpesa,btnPayAfrimoney,btnPayAirtel;

    RelativeLayout directPayView,paybillView;
    boolean is_direct_payment_enabled,is_mpesa_payment_enabled,is_airtel_payment_enabled,is_afrimoney_payment_enabled, is_tigo_payment_enabled;

    boolean is_ussd_mpesa_payment_enabled, is_ussd_airtel_payment_enabled, is_ussd_tigo_payment_enabled;

    MaterialRippleLayout ussdAirtelRips, ussdTigoRips, ussdMpesaRips;

    MaterialRippleLayout mpesaRips,airtelRips,afrimoneyRips;
    FirmaTextView phoneNumberText;
    long payment_confirmation_waiting_time_in_seconds= 10;

    FirmaTextView  defaultModel;

    String userNumber="";

    RelativeLayout btnChangeNumberClose;

    private ImageView iconDevice;
    private FirmaTextView ftvDevice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        parentView = inflater.inflate(R.layout.fragment_payment, container, false);
        parentActivity = (MainActivity)getActivity();
        mContext= parentActivity.getApplicationContext();
        storageObject = new StoreAndGetFromPrefs(mContext);

        loadingView= parentView.findViewById(R.id.loadingItems);

        btnBack = parentView.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.viewPager.setCurrentItem(1);
            }
        });


        setTranslatedButtonIcons();

        btnDaily= parentView.findViewById(R.id.btnDaily);
        btnWeekly= parentView.findViewById(R.id.btnWeekly);
        btnMonthly= parentView.findViewById(R.id.btnMonthly);

        dailyCheck= parentView.findViewById(R.id.dailyCheck);
        weeklyCheck= parentView.findViewById(R.id.weeklyCheck);
        monthlyCheck= parentView.findViewById(R.id.monthlyCheck);

        loadingGif= parentView.findViewById(R.id.loadingGif);

        btnChangeNumberClose= parentView.findViewById(R.id.btnChangeNumberClose);

        defaultModel = parentView.findViewById(R.id.defaultModel);

        ftvDevice = parentView.findViewById(R.id.ftvDevice);
        iconDevice = parentView.findViewById(R.id.iconDevice);

        loanCompleteView= parentView.findViewById(R.id.loanCompleteView);
        activeLoanView= parentView.findViewById(R.id.activeLoanView);
        loanCongratsText= parentView.findViewById(R.id.loanCongratsText);
        loanCongratsImage= parentView.findViewById(R.id.loanCongratsImage);
        loanCongratsImage.setImageResource(R.drawable.ics_congrats);

        paymentButtons= parentView.findViewById(R.id.paymentButtons);
        paymentWeight= parentView.findViewById(R.id.paymentWeight);

        paybillView= parentView.findViewById(R.id.paybillView);
        directPayView= parentView.findViewById(R.id.directPayView);

        mpesaRips= parentView.findViewById(R.id.mpesaRips);
        airtelRips= parentView.findViewById(R.id.airtelRips);
        afrimoneyRips= parentView.findViewById(R.id.afrimoneyRips);
        //        tigoRips = parentView.findViewById(R.id.tigoRips);

        refreshAfriMoneyPayments();


        ussdMpesaRips = parentView.findViewById(R.id.ussdMpesaRips);
        ussdAirtelRips = parentView.findViewById(R.id.ussdAirtelRips);
        ussdTigoRips = parentView.findViewById(R.id.ussdTigoRips);


        payingFor="";
        phoneNumberText= parentView.findViewById(R.id.phoneNumberText);
        phoneNumberText.setText("+"+storageObject.retrieveUserPhoneNumberFromPrefs());
        PhoneNumberFormatter.applyFormattingToTextView(phoneNumberText, storageObject.retrieveUserPhoneNumberFromPrefs());
        userNumber=phoneNumberText.getText().toString();


        btnMpesa= parentView.findViewById(R.id.btnMpesa);
        btnAirtel= parentView.findViewById(R.id.btnAirtel);
        //btnAfrimoney=(RelativeLayout) parentView.findViewById(R.id.btnAfrimoney);

        btnPayMpesa= parentView.findViewById(R.id.btnPayMpesa);
        btnPayAirtel= parentView.findViewById(R.id.btnPayAirtel);
        btnPayAfrimoney= parentView.findViewById(R.id.btnPayAfrimoney);

//        Toast.makeText(mContext,"Oze"+setProvider("vodacom"),Toast.LENGTH_SHORT).show();

        btnMpesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogUSSD("mpesa","*150*00#","650880",loanNumber,"WATU CREDIT DRC.");
            }
        });

        btnAirtel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogUSSD("airtel","*501#","650880",loanNumber,"WATU CREDIT DRC.");
            }
        });

//        btnAfrimoney.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openDialogUSSD("afrimoney","*1020#","650880",loanNumber,"WATU CREDIT LTD.");
//            }
//        });

        dailyH= parentView.findViewById(R.id.dailyH);
        weeklyH= parentView.findViewById(R.id.weeklyH);
        monthlyH= parentView.findViewById(R.id.monthlyH);

        dailyAmountCurrency= parentView.findViewById(R.id.dailyAmountCurrency);
        weeklyAmountCurrency= parentView.findViewById(R.id.weeklyAmountCurrency);
        monthlyAmountCurrency= parentView.findViewById(R.id.monthlyAmountCurrency);


        myNumberText= parentView.findViewById(R.id.myNumberText);
        otherNumberText= parentView.findViewById(R.id.otherNumberText);

        assetRips= parentView.findViewById(R.id.assetRips);
        switchView= parentView.findViewById(R.id.switchView);


        //SeekBar
        seekBar= parentView.findViewById(R.id.seekBar);
        rlMarker = parentView.findViewById(R.id.rlMarker);
        btnPay30Days= parentView.findViewById(R.id.btnPay30Days);
        choosePlan= parentView.findViewById(R.id.choosePlan);

        sliderView= parentView.findViewById(R.id.sliderView);
        buttonRipView= parentView.findViewById(R.id.buttonRipView);



        ccp = parentView.findViewById(R.id.ccp);
        phoneNumberEditText = parentView.findViewById(R.id.phoneNumberEditText);
        headerTitle= parentView.findViewById(R.id.headerTitle);
        ccp.registerCarrierNumberEditText(phoneNumberEditText);
        ccp.setDefaultCountryUsingNameCode("CD");
        ccp.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @Override
            public void onValidityChanged(boolean isValidNumber) {
                isPhoneNumberValid=isValidNumber;
            }
        });

        fromNumberValue=storageObject.retrieveUserPhoneNumberFromPrefs();


        currentAsset = storageObject.retrieveCurrentAssetObjectFromPrefs();
        if(currentAsset== null && storageObject.retrieveAssetObjectObjectsFromPrefs().size()>0)
        {

            storageObject.saveCurrentAssetObjectToPrefs(storageObject.retrieveAssetObjectObjectsFromPrefs().get(0));
            currentAsset = storageObject.retrieveCurrentAssetObjectFromPrefs();

        }


      //

//        try{
//            loanNumber = currentAsset.getLoanNumber();
//            currencyCode = currentAsset.getCurrencyCode();
//            payingFor = "";
//
//            if (currentAsset != null) {
//                setDeviceIcon(currentAsset.getAssetTypeId());
//                payingFor = currentAsset.getAssetTypeName();
//                itemPositionName = payingFor;
//                FragmentHomeNew.loanItemName = payingFor;
//            }
//        }catch(Exception e){}

        try {
            loanNumber = currentAsset.getLoanNumber();
            currencyCode = currentAsset.getCurrencyCode();
            payingFor = "";

            if (currentAsset != null) {
                setDeviceIcon(currentAsset.getAssetTypeId());

                payingFor = currentAsset.getAssetTypeName();
                itemPositionName = payingFor;
            }
        } catch (Exception e) {
        }


        dailyAmount= parentView.findViewById(R.id.dailyAmount);
        weeklyAmount= parentView.findViewById(R.id.weeklyAmount);
        monthlyAmount= parentView.findViewById(R.id.monthlyAmount);

        dailyPaid= parentView.findViewById(R.id.dailyPaid);
        weeklyPaid= parentView.findViewById(R.id.weeklyPaid);
        monthlyPaid= parentView.findViewById(R.id.monthlyPaid);



//        btnContinue = (RelativeLayout) parentView.findViewById(R.id.btnContinue);

        amountEditText = parentView.findViewById(R.id.amountEditText);
//        amountEditText.addTextChangedListener(new NumberThousandsTextWatcher(amountEditText));


        btnMyNumber= parentView.findViewById(R.id.btnMyNumber);
        btnOtherNumber= parentView.findViewById(R.id.btnOtherNumber);

        myNumberDiv= parentView.findViewById(R.id.myNumberDiv);
        otherNumberDiv= parentView.findViewById(R.id.otherNumberDiv);
        otherNumberView= parentView.findViewById(R.id.otherNumberView);

        fromNumberValue=storageObject.retrieveUserPhoneNumberFromPrefs();



        //Default Number
        changeNumberView= parentView.findViewById(R.id.changeNumberView);
        defaultNumberView= parentView.findViewById(R.id.defaultNumberView);
        defaultNumberEditView= parentView.findViewById(R.id.defaultNumberEditView);
        btnChangeNumber= parentView.findViewById(R.id.btnChangeNumber);
        defaultNumber= parentView.findViewById(R.id.defaultNumber);


        ccd = parentView.findViewById(R.id.ccd);
        defaultPhoneNumberEditText = parentView.findViewById(R.id.defaultPhoneNumberEditText);
        ccd.registerCarrierNumberEditText(defaultPhoneNumberEditText);
        ccd.setDefaultCountryUsingNameCode("CD");
        ccd.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @Override
            public void onValidityChanged(boolean isValidNumber) {
                isPhoneNumberValid=isValidNumber;
            }
        });

        fromNumberValue=storageObject.retrieveUserPhoneNumberFromPrefs();
        ccd.setFullNumber(fromNumberValue);
        if (!defaultPhoneNumberEditText.getText().toString().isEmpty() && !defaultPhoneNumberEditText.getText().toString().startsWith("0")) {defaultPhoneNumberEditText.setText("0"+ defaultPhoneNumberEditText.getText().toString());}
        defaultNumber.setText(fromNumberValue);
        defaultNumber.setText(fromNumberValue);
        defaultModel.setText(getAssetName(currentAsset));
        PhoneNumberFormatter.applyFormatting(defaultPhoneNumberEditText);
        PhoneNumberFormatter.applyFormattingToTextView(defaultNumber, storageObject.retrieveUserPhoneNumberFromPrefs());


        //Add Listener for AMount

        //SeekbarView
        // ---------------------------------------------------------
        // 1. SLIDER LISTENER
        // ---------------------------------------------------------
        seekbarListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {

                // --- VISUAL UPDATES (Always run this so the bubble marker moves) ---
                String dayText = mContext.getResources().getString(R.string.day);
                if(i > 1) {
                    dayText = mContext.getResources().getString(R.string.days);
                }
                updateMarker(seekBar, rlMarker, (i + " " + dayText));

                // --- LOGIC GUARD ---
                // If this change was triggered by the code (TextWatcher),
                // we stop here to prevent updating the EditText again.
                if (isProgrammaticChange) {
                    return;
                }

                // --- YOUR ORIGINAL CALCULATION LOGIC ---
                if(!isPayMonthly) {
                    if(i > 0) {
                        if(i == 7) {
                            payDays = 7;
                            payAmountValue = weeklyFee;
                        }
                        else if(i == 30) {
                            payDays = 30;
                            payAmountValue = monthlyFee;
                        }
                        else {
                            payDays = i;
                            payAmountValue = (dailyFee * i);
                        }

                        // Format the double
                        payAmountValue = BigDecimal.valueOf(payAmountValue)
                                .setScale(2, RoundingMode.DOWN)
                                .doubleValue();

                        // LOCK THE TEXT WATCHER
                        isProgrammaticChange = true;
                        amountEditText.setText("" + payAmountValue);
                        isProgrammaticChange = false; // RELEASE LOCK
                    }
                    else {
                        payDays = 1;
                        payAmountValue = dailyFee;

                        payAmountValue = BigDecimal.valueOf(payAmountValue)
                                .setScale(2, RoundingMode.DOWN)
                                .doubleValue();

                        // LOCK THE TEXT WATCHER
                        isProgrammaticChange = true;
                        amountEditText.setText("" + payAmountValue);
                        isProgrammaticChange = false; // RELEASE LOCK
                    }
                }
                else {
                    // Unselect logic
                    btnPay30Days.setBackgroundResource(R.xml.rounded_list_button_stroke);
                    isPayMonthly = false;

                    payAmountValue = BigDecimal.valueOf(payAmountValue)
                            .setScale(2, RoundingMode.DOWN)
                            .doubleValue();

                    // LOCK THE TEXT WATCHER
                    isProgrammaticChange = true;
                    amountEditText.setText("" + payAmountValue);
                    isProgrammaticChange = false; // RELEASE LOCK
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Your existing visibility logic
                rlMarker.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Your existing stop tracking logic
                // rlMarker.setVisibility(View.GONE); // (Kept commented out as per your code)
                if(!isPayMonthly) {
                    btnPay30Days.setBackgroundResource(R.xml.rounded_list_button_stroke);
                }
            }
        };

        seekBar.setOnSeekBarChangeListener(seekbarListener);


        // ---------------------------------------------------------
        // 2. EDIT TEXT LISTENER (TEXT WATCHER)
        // ---------------------------------------------------------
        Handler handler = new Handler();
        amountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed
            }

            @Override
            public void afterTextChanged(Editable s) {

                // --- LOGIC GUARD ---
                // If the Slider updated this text, we exit immediately.
                // This stops the infinite loop.
                if (isProgrammaticChange) {
                    return;
                }

                // Cancel previous delayed task
                handler.removeCallbacksAndMessages(null);

                // Start a new delayed task to capture the final value
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String cleanString = amountEditText.getText().toString().replace(",", "");

                            // Handle empty string crash
                            if (cleanString.isEmpty()) return;

                            double finalValue = Double.parseDouble(cleanString);

                            // --- REVERSE CALCULATION LOGIC ---

                            // Check Special Weekly Fee
                            if (Math.abs(finalValue - weeklyFee) < 0.1) { // Using epsilon for double comparison
                                payDays = 7;
                                payAmountValue = weeklyFee;
                            }
                            // Check Special Monthly Fee
                            else if (Math.abs(finalValue - monthlyFee) < 0.1) {
                                payDays = 30;
                                payAmountValue = monthlyFee;
                            }
                            // Standard Calculation
                            else {
                                // Calculate days from amount
                                payDays = finalValue / dailyFee;
                                payAmountValue = finalValue;
                            }

                            // --- UPDATE SEEKBAR ---

                            int daysInt = (int) payDays;
                            if(daysInt < 1) daysInt = 1;

                            // 1. Adjust Max if needed (Dynamic Range)
                            if (daysInt > 30) {
                                // Temporarily remove listener to change Max without triggering jitter
                                seekBar.setOnSeekBarChangeListener(null);
                                seekBar.setMax(daysInt + 5);
                                seekBar.setOnSeekBarChangeListener(seekbarListener);
                            }
                            else if (seekBar.getMax() > 30 && daysInt <= 30) {
                                // Reset to standard max if we drop back down
                                seekBar.setOnSeekBarChangeListener(null);
                                seekBar.setMax(30);
                                seekBar.setOnSeekBarChangeListener(seekbarListener);
                            }

                            // 2. Move the Slider
                            // LOCK THE SLIDER LISTENER so it doesn't update the Text again
                            isProgrammaticChange = true;

                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                seekBar.setProgress(daysInt, true); // Animate
                            } else {
                                seekBar.setProgress(daysInt);
                            }

                            isProgrammaticChange = false; // RELEASE LOCK

                            // 3. Update Visuals
                            setSliderText(payDays);

                        } catch(Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 500); // 500ms delay
            }
        });

        //End SeekBar View

        btnChangeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultNumberView.setVisibility(View.GONE);
                defaultNumberEditView.setVisibility(View.VISIBLE);
            }
        });

        btnChangeNumberClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Reset Chaange Number
                resetChangeNumber();
            }
        });


        //End Change Number

        new Thread(new Runnable() {
            public void run() {
                // a potentially time consuming task
                parentView.post(new Runnable() {
                    public void run() {

                        setData();
                    }
                });
            }
        }).start();

        btnPayMpesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (amountEditText.getText().length() < 1) {
                    String err = mContext.getResources().getString(R.string.enteramount);
                    Toast.makeText(mContext, err, Toast.LENGTH_SHORT).show();
                    return;
                }
                method = "mpesa";
                amount = "USD. " + payAmountValue;
                numberOfDays = payDays;
                user = storageObject.retrieveUserNameFromPrefs();
                device = getAssetName(currentAsset);
                pagaNumber = storageObject.retrieveUserPagaNumberFromPrefs();

                //openDialogUSSD("mpesa", "*150*00#", "650880", loanNumber, "WATU CREDIT drc.");
                showPaymentOptions(method, amountEditText.getText().toString(), payDays, user, device, pagaNumber, "650880", "*1122#");
                GeneralCommands.logEvent(parentActivity, "click_mpesa", "click_mpesa");
            }
        });

        btnPayAirtel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Incase Number is Changed
                if (amountEditText.getText().length() < 1) {
                    String err = mContext.getResources().getString(R.string.enteramount);
                    Toast.makeText(mContext, err, Toast.LENGTH_SHORT).show();
                    return;
                }

                //Incase Number is Changed
                paymentProvider = setProvider("airtel");

                if (payingFor == null || payingFor.equals("")) {
                    String err = mContext.getResources().getString(R.string.selectasset);
                    Toast.makeText(mContext, err, Toast.LENGTH_SHORT).show();
                } else {

                    if (amountEditText.getText().toString().length() > 0) {


                        try {
                            payAmountValue = Double.parseDouble(amountEditText.getText().toString().replace(",", ""));


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    if (isAnotherNumber) {

                        //Proceed All Good
                        if (phoneNumberEditText.getText().length() < 5) {
                            phoneNumberEditText.setError(null);
                            String errorMsg = mContext.getResources().getString(R.string.entervalidphonenumber);
                            Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();

                        } else {
                            //Number Okay
                            fromNumberValue = ccp.getFullNumber();


                            //CHeck for SHow Number
                            if (show_change_number_view) {

                                if (defaultPhoneNumberEditText.getText().length() < 5) {
                                    defaultPhoneNumberEditText.setError(null);
                                    String errorMsg = mContext.getResources().getString(R.string.entervalidphonenumber);
                                    Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();

                                } else {

                                    fromNumberValue = ccd.getFullNumber();

                                    //All Good FOr Show PhoneNumber
                                    openDialogConfirmPayment();

                                }

                            } else {
                                //All Good Not Show PhoneNumber
                                openDialogConfirmPayment();

                            }

                        }

                    } else {
                        //My Number

                        //CHeck for SHow Number
                        if (show_change_number_view) {
                            if (defaultPhoneNumberEditText.getText().length() < 5) {
                                defaultPhoneNumberEditText.setError(null);
                                String errorMsg = mContext.getResources().getString(R.string.entervalidphonenumber);
                                Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();

                            } else {

                                fromNumberValue = ccd.getFullNumber();
                                //All Good FOr Show PhoneNumber
                                openDialogConfirmPayment();

                            }

                        } else {
                            //All Good Not Show PhoneNumber
                            openDialogConfirmPayment();

                        }
                    }
                }

            }
        });

        btnPayAfrimoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (amountEditText.getText().length() < 1) {
                    String err = mContext.getResources().getString(R.string.enteramount);
                    Toast.makeText(mContext, err, Toast.LENGTH_SHORT).show();
                    return;
                }

                method = "afrimoney";
                amount = "USD. " + payAmountValue;
                numberOfDays = payDays;
                user = storageObject.retrieveUserNameFromPrefs();
                device = getAssetName(currentAsset);
                pagaNumber = storageObject.retrieveUserPagaNumberFromPrefs();

                showPaymentOptions(method, amountEditText.getText().toString(), numberOfDays, user, device, pagaNumber, "650880", "*1020#");
                GeneralCommands.logEvent(parentActivity, "click_afrimoney", "click_afrimoney");
            }
        });



        btnMyNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myNumberDiv.setVisibility(View.VISIBLE);
                otherNumberDiv.setVisibility(View.GONE);
                otherNumberView.setVisibility(View.GONE);

                isAnotherNumber=false;
                fromNumberValue=storageObject.retrieveUserPhoneNumberFromPrefs();

                myNumberText.setTextColor(mContext.getResources().getColor(R.color.theme_color));
                otherNumberText.setTextColor(mContext.getResources().getColor(R.color.white));

                GeneralCommands.logEvent(parentActivity,"click_pay_my_number","click_pay_my_number");

            }
        });

        btnOtherNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myNumberDiv.setVisibility(View.GONE);
                otherNumberDiv.setVisibility(View.VISIBLE);
                otherNumberView.setVisibility(View.VISIBLE);

                isAnotherNumber=true;


                myNumberText.setTextColor(mContext.getResources().getColor(R.color.white));
                otherNumberText.setTextColor(mContext.getResources().getColor(R.color.theme_color));

                GeneralCommands.logEvent(parentActivity,"click_pay_another_number","click_pay_another_number");

            }
        });

        setSwitch();
        setPaymentOptions();

        new Thread(new Runnable() {
            public void run() {
                // a potentially time consuming task
                setDefaultConfigs();
//				setFirebaseDetails();
                parentView.post(new Runnable() {
                    public void run() {
                        //Update UI
                    }
                });
            }
        }).start();


        return parentView;
    }

    private void setTranslatedButtonIcons() {

        ImageView iconAirtel = parentView.findViewById(R.id.iv_airtel);
        ImageView iconMpesa = parentView.findViewById(R.id.iv_mpesa);
        ImageView iconAfriMoney = parentView.findViewById(R.id.iv_afri_money);

        if (storageObject.retrieveLanguageFromPrefs().contains("fr")) {
            Glide.with(this)
                    .load(R.drawable.ics_pay_via_airtel_2_fr)
                    .into(iconAirtel);

            Glide.with(this)
                    .load(R.drawable.ics_pay_via_fr)
                    .into(iconMpesa);

            Glide.with(this)
                    .load(R.drawable.ics_icon_pay_afrimoney_fr)
                    .into(iconAfriMoney);

        } else {
            Glide.with(this)
                    .load(R.drawable.ics_icon_airtel)
                    .into(iconAirtel);

            Glide.with(this)
                    .load(R.drawable.ics_pay_via)
                    .into(iconMpesa);

            Glide.with(this)
                    .load(R.drawable.ics_icon_pay_afrimoney)
                    .into(iconAfriMoney);
        }
    }

    private void refreshAfriMoneyPayments() {
        remoteConfig = new FirebaseConfig();
        mFirebaseConfig = remoteConfig.getConfigs();
        mFirebaseConfig.fetchAndActivate().addOnCompleteListener(parentActivity, task -> {
            if (task.isSuccessful()) {
                //Check if Traka payments has been enabled
                boolean is_afrimoney_payment_enabled=mFirebaseConfig.getBoolean("is_afrimoney_payment_enabled");

                try {
                    if (is_afrimoney_payment_enabled && afrimoneyRips!=null) {
                        afrimoneyRips.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(mContext, mContext.getResources().getString(R.string.fetchfailed),
                        Toast.LENGTH_SHORT).show();
            }

        });

        mFirebaseConfig.addOnConfigUpdateListener(new ConfigUpdateListener() {
            @Override
            public void onUpdate(@NonNull ConfigUpdate configUpdate) {
                boolean is_afrimoney_payment_enabled=mFirebaseConfig.getBoolean("is_afrimoney_payment_enabled");

                try {
                    if (is_afrimoney_payment_enabled && afrimoneyRips!=null) {
                        afrimoneyRips.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(@NonNull FirebaseRemoteConfigException error) {


            }
        });
    }

    private void resetChangeNumber()
    {
        try {
            fromNumberValue=storageObject.retrieveUserPhoneNumberFromPrefs();
            ccd.setFullNumber(fromNumberValue);
            ccp.setFullNumber(fromNumberValue);
            if (!defaultPhoneNumberEditText.getText().toString().isEmpty() && !defaultPhoneNumberEditText.getText().toString().startsWith("0")) {defaultPhoneNumberEditText.setText("0"+ defaultPhoneNumberEditText.getText().toString());}
            if (!phoneNumberEditText.getText().toString().isEmpty() && !phoneNumberEditText.getText().toString().startsWith("0")) {phoneNumberEditText.setText("0"+ phoneNumberEditText.getText().toString());}


            defaultNumberView.setVisibility(View.VISIBLE);
            defaultNumberEditView.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        GeneralCommands.logEvent(parentActivity,"view_payment_page","view_payment_page");
        setIsVisible();
    }

    @Override
    public void onPause() {
        super.onPause();

        // Fragment became invisible
        setIsInvisible();
        resetChangeNumber();
    }


    public void setIsVisible()
    {
        isPayMonthly=false;
        // Fragment became visible
//        Toast.makeText(mContext, "Fragment  Visible", Toast.LENGTH_SHORT).show();

        //If is From Home then Set Default to 1 Day
        if(MainActivity.is_from_home_page)
        {
            if(MainActivity.is_source.equals("daily"))
            {

                // slider_default= 1;
                payDays=1;
                payAmountValue=dailyFee;
                btnDaily.performClick();



            }
            else if(MainActivity.is_source.equals("weekly"))
            {


                payDays=7;
                payAmountValue=weeklyFee;

                btnWeekly.performClick();

            }
            else if(MainActivity.is_source.equals("monthly"))
            {
                payDays=30;
                payAmountValue=monthlyFee;

                btnMonthly.performClick();

            }

        }
        else
        {
            //Not From Home Page Accessed Directly

            payDays=slider_default;
            seekBar.setMax((int)slider_max_value);
            payAmountValue=(dailyFee*payDays);
            amountEditText.setText(""+payAmountValue);
            seekBar.setProgress((int)payDays);
        }


        showSliderView(payDays,slider_max_value);

    }

    public void setIsInvisible(){
//         Toast.makeText(mContext, "Fragment  Paused", Toast.LENGTH_SHORT).show();
        MainActivity.is_from_home_page =false;
    }

    public void setData()
    {

        if(currentAsset!=null)
        {
            //Set Default

            payingFor=currentAsset.getAssetTypeName();

            try{

                dailyFee = Double.parseDouble(currentAsset.getDailyPayment());
                weeklyFee = Double.parseDouble(currentAsset.getWeeklyPayment());
                monthlyFee = Double.parseDouble(currentAsset.getMonthlyPayment());

//                        Toast.makeText(mContext, ""+dailyFee, Toast.LENGTH_SHORT).show();
                dailyAmount.setText(GeneralCommands.fixCommas(""+dailyFee));
                weeklyAmount.setText(GeneralCommands.fixCommas(""+weeklyFee));
                monthlyAmount.setText(GeneralCommands.fixCommas(""+monthlyFee));

                dailyAmountCurrency.setText(currentAsset.getCurrencyCode().equals("USD")?"Usd. ":currentAsset.getCurrencyCode());
                weeklyAmountCurrency.setText(currentAsset.getCurrencyCode().equals("USD")?"Usd. ":currentAsset.getCurrencyCode());
                monthlyAmountCurrency.setText(currentAsset.getCurrencyCode().equals("USD")?"Usd. ":currentAsset.getCurrencyCode());


            }catch(Exception e){
                e.printStackTrace();
            }
        }


        if(storageObject.retrieveAssetObjectObjectsFromPrefs().size()>1 )
        {
            assetRips.setVisibility(View.VISIBLE);

        }
        else {
            assetRips.setVisibility(View.GONE);

        }




        //Update
        Locale locale = new Locale(storageObject.retrieveLanguageFromPrefs()); // or "fr" for Swahili
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        mContext.getResources().updateConfiguration(config, parentActivity.getBaseContext().getResources().getDisplayMetrics());



        // Asset click listener
        listAssets = parentView.findViewById(R.id.listAssets);
        //Gender
        mAdapterAssets= new ArrayAdapter<String>(parentActivity, R.layout.spinner_item, getListDataAsset());
        mAdapterAssets.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listAssets.setAdapter(mAdapterAssets);


        selectAsset();



        listAssets.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                AssetObject object= listData.get(i);



                JSONObject obj = new JSONObject();

                try {

                    obj.put("assetLoanNumber",object.getLoanNumber());
                    obj.put("assetName",object.getAssetTypeName());
                    obj.put("assetTypeId",object.getAssetTypeId());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                GeneralCommands.logEventObject(parentActivity,"selected_asset_option","selected_asset_option",obj);


                storageObject.saveCurrentAssetObjectToPrefs(object);
                currentAsset=object;
                payingFor=object.getAssetTypeName();
                itemPositionName=payingFor;

//                if(defaultModel!=null)defaultModel.setText(getAssetName(currentAsset));
                //FragmentHomeNew.loanItemName=payingFor;

//                    Toast.makeText(mContext, "Day:"+currentAsset.getDailyPayment(), Toast.LENGTH_SHORT).show();


                try{
                    payAmountValue= Double.parseDouble(currentAsset.getDailyPayment());

                    dailyFee = Double.parseDouble(currentAsset.getDailyPayment());
                    weeklyFee = Double.parseDouble(currentAsset.getWeeklyPayment());
                    monthlyFee = Double.parseDouble(currentAsset.getMonthlyPayment());

//                        Toast.makeText(mContext, ""+dailyFee, Toast.LENGTH_SHORT).show();
                    dailyAmount.setText(GeneralCommands.fixCommas(""+dailyFee));
                    weeklyAmount.setText(GeneralCommands.fixCommas(""+weeklyFee));
                    monthlyAmount.setText(GeneralCommands.fixCommas(""+monthlyFee));

                    dailyAmountCurrency.setText(currentAsset.getCurrencyCode().equals("USD")?"Usd. ":currentAsset.getCurrencyCode());
                    weeklyAmountCurrency.setText(currentAsset.getCurrencyCode().equals("USD")?"Usd. ":currentAsset.getCurrencyCode());
                    monthlyAmountCurrency.setText(currentAsset.getCurrencyCode().equals("USD")?"Usd. ":currentAsset.getCurrencyCode());


                }catch(Exception e){
                    e.printStackTrace();
                }

                int assetId= object.getAssetTypeId();

                if (object != null && (("" + object.getAssetTypeId()).equals("4")))//smartphone set has slider
                {

//                   showSliderView();
                    sliderView.setVisibility(View.VISIBLE);
                    buttonRipView.setVisibility(View.GONE);
                    setDeviceIcon(assetId);

                } else {

                    //Not a Smart Phone
                    sliderView.setVisibility(View.GONE);
                    buttonRipView.setVisibility(View.VISIBLE);
                    setDeviceIcon(assetId);
                }

                //Laon COmplete View

                if(object.getLoanStatus().toLowerCase().contains("closed"))
                {

                    String model= object.getVehicleMakeName() + " | "+object.getVehicleModelName();
                    model= model.toLowerCase().contains("null") ?"" :model;


                    //Loan COmplete
                    activeLoanView.setVisibility(View.GONE);
                    loanCompleteView.setVisibility(View.VISIBLE);
                    loanCongratsText.setVisibility(View.VISIBLE);
                    loanCongratsText.setText(mContext.getResources().getString(R.string.loancompletetext,model));
                    //Loan Complete
                    LoanCompleteIconObject icon= storageObject.retrieveCurrentLoanCompleteIconObjectFromPrefs();
                    if(icon != null && icon.getImage_url() !=null)
                    {


                        //Loan Congrats Icon Available
                        loanCongratsImage.setVisibility(View.VISIBLE);
                        String urlData= icon.getImage_url();
                        String urlData_sw= icon.getImage_url_sw();
                        if(storageObject.retrieveLanguageFromPrefs().equals("fr"))
                        {
                            urlData=urlData_sw;

                        }

                        if(icon.getType().equalsIgnoreCase("gif"))
                        {
                            String finalUrlData = urlData;
                            Glide.with(parentActivity)
                                    .asGif()
                                    .load(urlData)
                                    .listener(new RequestListener<GifDrawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {

                                            //Failed
                                            ImageUtil.displayImage(loanCongratsImage, finalUrlData, new ImageLoadingListener() {
                                                @Override
                                                public void onLoadingStarted(String s, View view) {

                                                }

                                                @Override
                                                public void onLoadingFailed(String s, View view, FailReason failReason) {

                                                    ImageView image=(ImageView) view;
                                                    image.setImageResource(R.drawable.ics_congrats);
                                                }

                                                @Override
                                                public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                                                }

                                                @Override
                                                public void onLoadingCancelled(String s, View view) {

                                                }
                                            });



                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                                            return false;
                                        }
                                    })

                                    .into(new CustomTarget<GifDrawable>() {
                                        @Override
                                        public void onResourceReady(@NonNull GifDrawable resource, @Nullable Transition<? super GifDrawable> transition) {
                                            loanCongratsImage.setImageDrawable(resource);
                                            resource.setLoopCount(LOOP_FOREVER); // Set loop count to 1 to detect the end of the first loop
                                            resource.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                                                @Override
                                                public void onAnimationEnd(Drawable drawable) {
                                                    super.onAnimationEnd(drawable);

                                                }
                                            });
                                            resource.start(); // Start the GIF animation
                                        }

                                        @Override
                                        public void onLoadCleared(@Nullable Drawable placeholder) {
                                            // Handle the cleanup of the resource if needed
                                        }
                                    });

                        }
                        else
                        {
                            //Is an Image
                            ImageUtil.displayImage(loanCongratsImage, urlData, new ImageLoadingListener() {
                                @Override
                                public void onLoadingStarted(String s, View view) {

                                }

                                @Override
                                public void onLoadingFailed(String s, View view, FailReason failReason) {

                                    ImageView image=(ImageView) view;
                                    image.setImageResource(R.drawable.ics_congrats);
                                }

                                @Override
                                public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                                }

                                @Override
                                public void onLoadingCancelled(String s, View view) {

                                }
                            });
                        }



                    }else {
                        //Image Util Local
                        loanCongratsImage.setImageResource(R.drawable.ics_congrats);

                    }
                }
                else {
                    activeLoanView.setVisibility(View.VISIBLE);
                    loanCompleteView.setVisibility(View.GONE);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


//        swipeRefreshLayout.setRefreshing(false);


        btnPay30Days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!isPayMonthly)
                {
                    isPayMonthly=true;
                    btnMonthly.performClick();
                    btnPay30Days.setBackgroundResource(R.xml.rounded_list_button_orange_light);




                }else
                {

                    isPayMonthly=false;
                    btnDaily.performClick();
                    btnPay30Days.setBackgroundResource(R.xml.rounded_list_button_stroke);


                }

            }
        });

        //return view V
    }

    public static void setSwitch()
    {
        if(storageObject!=null && switchView!=null)
        {
            if(storageObject.retrievePayOtherConfigFromPrefs() ==0)//Cannot Pay wit Another Number
            {
                switchView.setVisibility(View.GONE);
                headerTitle.setVisibility(View.GONE);
            }
            else
            {
                if(!show_change_number_view)switchView.setVisibility(View.VISIBLE);
                if(storageObject.retrieveAssetObjectObjectsFromPrefs().size() < 2)
                {
//                    headerTitle.setVisibility(View.GONE);
                }
                else {
          //          headerTitle.setVisibility(View.VISIBLE);
                }
            }
        }

    }
    public void setPaymentOptions()
    {

        btnDaily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDaily.setBackgroundResource(R.xml.rounded_list_button_orange_light);
                dailyH.setTextColor(mContext.getResources().getColor(R.color.white));
                dailyAmount.setTextColor(mContext.getResources().getColor(R.color.white));
                dailyAmountCurrency.setTextColor(mContext.getResources().getColor(R.color.white));

                dailyPaid.setTextColor(mContext.getResources().getColor(R.color.white));
                dailyCheck.setVisibility(View.VISIBLE);


                payAmountValue=dailyFee;
                amountEditText.setText(""+payAmountValue);
                payOption="daily";
                payDays=1;
                seekBar.setMax(7);
                seekBar.setProgress((int)payDays);


                btnWeekly.setBackgroundResource(R.xml.rounded_list_button_grey);
                weeklyH.setTextColor(mContext.getResources().getColor(R.color.black));
                weeklyAmount.setTextColor(mContext.getResources().getColor(R.color.black));
                weeklyAmountCurrency.setTextColor(mContext.getResources().getColor(R.color.black));

                weeklyPaid.setTextColor(mContext.getResources().getColor(R.color.black));
                weeklyCheck.setVisibility(View.GONE);


                btnMonthly.setBackgroundResource(R.xml.rounded_list_button_grey);
                monthlyH.setTextColor(mContext.getResources().getColor(R.color.black));
                monthlyAmount.setTextColor(mContext.getResources().getColor(R.color.black));
                monthlyAmountCurrency.setTextColor(mContext.getResources().getColor(R.color.black));

                monthlyPaid.setTextColor(mContext.getResources().getColor(R.color.black));
                monthlyCheck.setVisibility(View.GONE);

                JSONObject obj = new JSONObject();

                try {
                    obj.put("dailyAmount",dailyFee);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                GeneralCommands.logEventObject(parentActivity,"click_pay_daily","click_pay_daily",obj);


            }
        });


        btnWeekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDaily.setBackgroundResource(R.xml.rounded_list_button_grey);
                dailyH.setTextColor(mContext.getResources().getColor(R.color.black));
                dailyAmount.setTextColor(mContext.getResources().getColor(R.color.black));
                dailyAmountCurrency.setTextColor(mContext.getResources().getColor(R.color.black));

                dailyPaid.setTextColor(mContext.getResources().getColor(R.color.black));
                dailyCheck.setVisibility(View.GONE);

                payAmountValue=weeklyFee;
                amountEditText.setText(""+payAmountValue);
                payOption="weekly";
                payDays=7;
                seekBar.setMax(7);
                seekBar.setProgress((int)payDays);


                btnWeekly.setBackgroundResource(R.xml.rounded_list_button_orange_light);
                weeklyH.setTextColor(mContext.getResources().getColor(R.color.white));
                weeklyAmount.setTextColor(mContext.getResources().getColor(R.color.white));
                weeklyAmountCurrency.setTextColor(mContext.getResources().getColor(R.color.white));

                weeklyPaid.setTextColor(mContext.getResources().getColor(R.color.white));
                weeklyCheck.setVisibility(View.VISIBLE);


                btnMonthly.setBackgroundResource(R.xml.rounded_list_button_grey);
                monthlyH.setTextColor(mContext.getResources().getColor(R.color.black));
                monthlyAmount.setTextColor(mContext.getResources().getColor(R.color.black));
                monthlyAmountCurrency.setTextColor(mContext.getResources().getColor(R.color.black));

                monthlyPaid.setTextColor(mContext.getResources().getColor(R.color.black));
                monthlyCheck.setVisibility(View.GONE);


                JSONObject obj = new JSONObject();

                try {
                    obj.put("weeklyAmount",weeklyFee);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                GeneralCommands.logEventObject(parentActivity,"click_pay_weekly","click_pay_weekly",obj);


            }
        });


        btnMonthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDaily.setBackgroundResource(R.xml.rounded_list_button_grey);
                dailyH.setTextColor(mContext.getResources().getColor(R.color.black));
                dailyAmount.setTextColor(mContext.getResources().getColor(R.color.black));
                dailyAmountCurrency.setTextColor(mContext.getResources().getColor(R.color.black));

                dailyPaid.setTextColor(mContext.getResources().getColor(R.color.black));
                dailyCheck.setVisibility(View.GONE);

                payAmountValue=monthlyFee;
                amountEditText.setText(""+payAmountValue);
                payOption="monthly";
                payDays=30;
                seekBar.setMax(30);
                seekBar.setProgress((int)payDays);


                btnWeekly.setBackgroundResource(R.xml.rounded_list_button_grey);
                weeklyH.setTextColor(mContext.getResources().getColor(R.color.black));
                weeklyAmount.setTextColor(mContext.getResources().getColor(R.color.black));
                weeklyAmountCurrency.setTextColor(mContext.getResources().getColor(R.color.black));

                weeklyPaid.setTextColor(mContext.getResources().getColor(R.color.black));
                weeklyCheck.setVisibility(View.GONE);


                btnMonthly.setBackgroundResource(R.xml.rounded_list_button_orange_light);
                monthlyH.setTextColor(mContext.getResources().getColor(R.color.white));
                monthlyAmount.setTextColor(mContext.getResources().getColor(R.color.white));
                monthlyAmountCurrency.setTextColor(mContext.getResources().getColor(R.color.white));

                monthlyPaid.setTextColor(mContext.getResources().getColor(R.color.white));
                monthlyCheck.setVisibility(View.VISIBLE);


                JSONObject obj = new JSONObject();

                try {
                    obj.put("monthlyAmount",weeklyFee);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                GeneralCommands.logEventObject(parentActivity,"click_pay_monthly","click_pay_monthly",obj);

            }
        });
    }

    private String fixCommas (String digits)
    {
        String result = "";
        int len = digits.length();
        int nDigits = 0;

        for (int i = len - 1; i >= 0; i--)
        {
            result = digits.charAt(i) + result;
            nDigits++;
            if (((nDigits % 3) == 0) && (i > 0))
            {
                result = "," + result;
            }
        }
        return (result);
    }

    public void openDialogSelectPaymentMethod()
    {
        View contentView = parentActivity.getLayoutInflater().inflate(R.layout.dialog_content_select_payment_method, null);


        final DialogPlus dialogPlus = new DialogPlus.Builder(parentActivity)
                .setContentHolder(new ViewHolder(contentView))
                .setGravity(DialogPlus.Gravity.BOTTOM)
                .setBackgroundColorResourceId(R.color.transparent)
                .setCancelable(true)
                .create();


        //Content View
        View content = dialogPlus.getHolderView();



        RelativeLayout btnContinue= content.findViewById(R.id.btnContinue);
        RelativeLayout btnBack= content.findViewById(R.id.btnBack);
        RelativeLayout btnMpesa= content.findViewById(R.id.btnMpesa);


        btnBack.setOnClickListener(view -> {
            //dialogPlus.dismiss();
            parentActivity.onBackPressed();
        });

        btnContinue.setOnClickListener(view -> {
            dialogPlus.setDismiss();
            if(amountEditText.getText().length()>1 && isInteger(amountEditText.getText().toString()))
            {

                try{
                    payAmountValue = BigDecimal.valueOf(payAmountValue).setScale(2, RoundingMode.DOWN).doubleValue();
                   // payAmountValue= Double.parseDouble(amountEditText.getText().toString().replace(",",""));
                }catch(Exception e){
                    e.printStackTrace();
                }

            }

        });


        parentActivity.currentDialogPlus = dialogPlus;
        dialogPlus.show();
    }

    public void openDialogConfirmPayment()
    {
        View contentView = parentActivity.getLayoutInflater().inflate(R.layout.dialog_content_confirm_payment, null);


        final DialogPlus dialogPlus = new DialogPlus.Builder(parentActivity)
                .setContentHolder(new ViewHolder(contentView))
                .setGravity(DialogPlus.Gravity.BOTTOM)
                .setBackgroundColorResourceId(R.color.black)
                .setCancelable(true)
                .create();





        //Content View
        View content = dialogPlus.getHolderView();

        ImageView iconImage= content.findViewById(R.id.payIcon);

        if(storageObject.retrieveLanguageFromPrefs().equals("fr")){
            Glide.with(this)
                    .load(R.drawable.ics_pay_via_airtel_2_fr)
                    .into(iconImage);

        }else{
            Glide.with(this)
                    .load(R.drawable.ics_icon_airtel)
                    .into(iconImage);
        }

        RelativeLayout loadingView= content.findViewById(R.id.loadingItems);


        RelativeLayout btnCancel= content.findViewById(R.id.btnCancel);
        RelativeLayout btnConfirm= content.findViewById(R.id.btnConfirm);
        RelativeLayout btnDone= content.findViewById(R.id.btnSuccess);
        FirmaTextView errorText = content.findViewById(R.id.errorText);
        RelativeLayout iconView= content.findViewById(R.id.iconView);
        ImageView payIcon= content.findViewById(R.id.payIcon);


        FirmaTextView payAmount = content.findViewById(R.id.payAmount);
        FirmaTextView fromNumber = content.findViewById(R.id.fromNumber);
        FirmaTextView payCurrency = content.findViewById(R.id.payCurrency);

        RelativeLayout limitView= content.findViewById(R.id.limitView);


        if(paymentProviderName.toLowerCase().contains("safaricom"))
        {
            iconView.setVisibility(View.VISIBLE);
            //iconView.setBackgroundResource(R.xml.rounded_list_transparent_green_stroke);
            payIcon.setImageResource(R.drawable.ics_rect_mpesa);


        }

        else if(paymentProviderName.toLowerCase().contains("airtel")) {
            iconView.setVisibility(View.VISIBLE);
           // iconView.setBackgroundResource(R.xml.rounded_list_transparent_orange_stroke);
            payIcon.setImageResource(R.drawable.ics_rect_airtel);

        }

        else if(paymentProviderName.toLowerCase().contains("tigo")) {
            iconView.setVisibility(View.VISIBLE);
            //iconView.setBackgroundResource(R.xml.rounded_list_transparent_white_stroke);
            payIcon.setImageResource(R.drawable.ics_mixx_payment_trans);

        }
        else
        {
            iconView.setVisibility(View.GONE);

        }

        if(payAmountValue < dailyFee){limitView.setVisibility(View.VISIBLE);}else{limitView.setVisibility(View.GONE);}


       // payCurrency.setText(currentAsset.getCurrencyCode().equals("USD")?"Usd.":currentAsset.getCurrencyCode());

        payCurrency.setText(
                (currentAsset.getCurrencyCode().equals("USD") ? "Usd." : currentAsset.getCurrencyCode())
                        .toUpperCase(Locale.ROOT)
        );
        payAmount.setText(GeneralCommands.fixCommas(" "+payAmountValue));
        fromNumber.setText(fromNumberValue);
        PhoneNumberFormatter.applyFormattingToTextView(fromNumber, fromNumberValue);



        JSONObject obj = new JSONObject();

        try {
            obj.put("loanNumber",loanNumber);
            obj.put(FirebaseAnalytics.Param.CURRENCY,currencyCode);
            obj.put(FirebaseAnalytics.Param.VALUE,payAmountValue);
            obj.put("amount",""+payAmountValue);
            obj.put("paymentProvider",paymentProvider);
            obj.put("fromNumberValue",fromNumberValue);
            obj.put("paymentDaysOption",payOption);
            obj.put("pay_option","afrimoney");

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        GeneralCommands.logEventObject(parentActivity,"click_pay_button_"+paymentProviderName,"click_pay_button_"+paymentProviderName,obj);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPlus.setDismiss();


                openDialogProcessingPayment();


            }
        });


        if(!fromNumberValue.equals(storageObject.retrieveUserPhoneNumberFromPrefs()))
        {

            if(storageObject.retrieveOtherNumberCountFromPrefs()>=storageObject.retrievePayOtherConfigFromPrefs())
            {
                loadingView.setVisibility(View.VISIBLE);
                //Check if time has lapsed
                boolean is24HoursPassed = check24HoursPassed(storageObject.retrievePaymentTimeStampFromPrefs(), System.currentTimeMillis());
                if(!is24HoursPassed)
                {
                    GetData ob1 = new GetData();
                    ob1.initialize(parentActivity,loadingView);
                    ob1.problemLoading(mContext.getResources().getString(R.string.maxpayforanother),400);
                    errorText.setTextColor(mContext.getResources().getColor(R.color.theme_orange));


                    //Max Limit Reached
                    dialogPlus.setDialogCancelable(true);
                    RelativeLayout resultError= loadingView.findViewById(R.id.resultError);
                    RelativeLayout resultErrorSupport= loadingView.findViewById(R.id.resultErrorSupport);
                    RelativeLayout btnFailedSupport= loadingView.findViewById(R.id.btnFailedSupport);
                    FirmaTextView errorTextSupport= loadingView.findViewById(R.id.errorTextSupport);

                    resultError.setVisibility(View.GONE);
                    resultErrorSupport.setVisibility(View.VISIBLE);
                    errorTextSupport.setText(mContext.getResources().getString(R.string.maxpayforanother));

                    btnFailedSupport.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogPlus.setDismiss();
                        }
                    });


                    btnDone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogPlus.dismiss();
                        }
                    });

                    btnConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogPlus.dismiss();
                        }
                    });


                }
                else
                {
                    //Time Elasped so reset everything and proceed
                    storageObject.savePaymentTimestampToPrefsToPrefs(System.currentTimeMillis());
                    storageObject.saveOtherNumberCountToPrefs(0);

                }


            }
            else
            {
                //Increase COunter for Other Number Check
                storageObject.increaseOtherNumberCountToPrefs();
                long currentTimePayment = System.currentTimeMillis();
                storageObject.savePaymentTimestampToPrefsToPrefs(currentTimePayment);
            }


        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPlus.dismiss();
            }
        });


        parentActivity.currentDialogPlus = dialogPlus;

        dialogPlus.show();
    }

    public void openDialogProcessingPayment()
    {
        View contentView = parentActivity.getLayoutInflater().inflate(R.layout.dialog_content_process_payment, null);


        final DialogPlus dialogPlus = new DialogPlus.Builder(parentActivity)
                .setContentHolder(new ViewHolder(contentView))
                .setGravity(DialogPlus.Gravity.BOTTOM)
                .setBackgroundColorResourceId(R.color.transparent)
                .setCancelable(true)
                .create();


        //Content View
        View content = dialogPlus.getHolderView();


        ImageView  progressWheel= content.findViewById(R.id.progressWheel);

        try
        {


            Glide.with(parentActivity)
                    .asGif()
                    .load(R.drawable.loader) // Replace with your local GIF resource
                    .into(progressWheel);

        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        RelativeLayout loadingView= content.findViewById(R.id.loadingItems);
        RelativeLayout btnSuccess =loadingView.findViewById(R.id.btnSuccess);
        FirmaTextView btnSuccessText =loadingView.findViewById(R.id.btnSuccessText);



        FirmaTextView errorTitleText =loadingView.findViewById(R.id.errorTextHeader);
        FirmaTextView errorText =loadingView.findViewById(R.id.errorText);


        FirmaTextView btnErrorText =loadingView.findViewById(R.id.btnErrorText);

        RelativeLayout activeView=content.findViewById(R.id.activeView);
        RelativeLayout counterView=content.findViewById(R.id.pendingCounterView);
        FirmaTextView counterText= content.findViewById(R.id.counterText);
        ImageView progressWheelWallet=content.findViewById(R.id.progressWheelWallet);
        RelativeLayout btnConfirmPayment=content.findViewById(R.id.btnConfirmPayment);
        MaterialRippleLayout confirmRips=content.findViewById(R.id.confirmRips);



        FirmaTextView payAmount = content.findViewById(R.id.payAmount);
        FirmaTextView payCurrency = content.findViewById(R.id.payCurrency);
        FirmaTextView fromNumber = content.findViewById(R.id.fromNumber);

        payCurrency.setText(currentAsset.getCurrencyCode().equals("USD")?"Usd.":currentAsset.getCurrencyCode());
        payAmount.setText(GeneralCommands.fixCommas(""+payAmountValue));
        fromNumber.setText(fromNumberValue);
        PhoneNumberFormatter.applyFormattingToTextView(fromNumber, fromNumberValue);

        RelativeLayout iconView= content.findViewById(R.id.iconView);
        ImageView payIcon= content.findViewById(R.id.payIcon);


        parentActivity.hideKeyboard();

        if(paymentProviderName.toLowerCase().contains("safaricom"))
        {
            iconView.setVisibility(View.VISIBLE);
           // iconView.setBackgroundResource(R.xml.rounded_list_transparent_green_stroke);
            payIcon.setImageResource(R.drawable.ics_rect_mpesa);


        }

        else if(paymentProviderName.toLowerCase().contains("airtel")) {
            iconView.setVisibility(View.VISIBLE);
            //iconView.setBackgroundResource(R.xml.rounded_list_transparent_orange_stroke);
            payIcon.setImageResource(R.drawable.ics_rect_airtel);

        }

        else if(paymentProviderName.toLowerCase().contains("tigo")) {
            iconView.setVisibility(View.VISIBLE);
           // iconView.setBackgroundResource(R.xml.rounded_list_transparent_white_stroke);
            payIcon.setImageResource(R.drawable.ics_mixx_payment_trans);

        }
        else
        {
            iconView.setVisibility(View.GONE);

        }



        GetData ob1 = new GetData();
        ob1.initialize(parentActivity, loadingView);





        JSONObject obj = new JSONObject();
        try {
            obj.put("loanNumber",loanNumber);
            obj.put(FirebaseAnalytics.Param.CURRENCY,currencyCode);
            obj.put(FirebaseAnalytics.Param.VALUE,payAmountValue);
            obj.put("amount",""+payAmountValue);
            obj.put("paymentProvider",paymentProvider);
            obj.put("fromNumberValue",fromNumberValue);
            obj.put("paymentDaysOption",payOption);


        } catch (JSONException e) {
            e.printStackTrace();
        }




        GeneralCommands.logEventObject(parentActivity,"add_to_cart","add_to_cart",obj);

        //Initiate Payment
        ob1.initiateSTKPayment(loanNumber,currencyCode,payAmountValue,paymentProviderID,fromNumberValue, dialogPlus,false);
        ob1.setPaymentSTKListener(new GetData.PaymentSTKListener() {
            @Override
            public void onObjectReady(String title) {

            }

            @Override
            public void onDataLoaded(boolean isSuccess) {
                if(!isSuccess)
                {
//                    Toast.makeText(mContext,"ZeBoo-Failed"+status+":"+paymentId,Toast.LENGTH_SHORT).show();

                    dialogPlus.setDialogCancelable(true);
                }
                else {
                    //Stk SUcess

                }

            }
        });


        ob1.confirmPaymentSTKListener(new GetData.ConfirmPaymentSTKListener() {
            @Override
            public void onObjectReady(String title) {

            }

            @Override
            public void onDataLoaded(String status, String paymentId) {


                if(status !=null && status.equalsIgnoreCase("SUCCESSFUL"))
                {

                    btnSuccessText.setText(mContext.getResources().getString(R.string.done));
                    btnSuccess.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Toast.makeText(mContext, mContext.getResources().getString(R.string.thankyouforfeedback), Toast.LENGTH_SHORT).show();
                            dialogPlus.dismiss();
                            MainActivity.viewPager.setCurrentItem(1);
                        }
                    });

                    JSONObject obj = new JSONObject();

                    try {

                        obj.put("paymentTransactionId",paymentId);
                        obj.put("loanNumber",loanNumber);
                        obj.put(FirebaseAnalytics.Param.CURRENCY,currencyCode);
                        obj.put(FirebaseAnalytics.Param.VALUE,payAmountValue);
                        obj.put("amount",""+payAmountValue);
                        obj.put("paymentProvider",paymentProvider);
                        obj.put("fromNumberValue",fromNumberValue);
                        obj.put("paymentDaysOption",payOption);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    GeneralCommands.logEventObject(parentActivity,"purchase","purchase",obj);



                    showWin(dialogPlus,loadingView);



                }
                else if(status !=null && status.equalsIgnoreCase("PENDING"))
                {


                    JSONObject obj = new JSONObject();

                    try {

                        obj.put("paymentTransactionId",paymentId);
                        obj.put("loanNumber",loanNumber);
                        obj.put(FirebaseAnalytics.Param.CURRENCY,currencyCode);
                        obj.put(FirebaseAnalytics.Param.VALUE,payAmountValue);
                        obj.put("amount",""+payAmountValue);
                        obj.put("paymentProvider",paymentProvider);
                        obj.put("fromNumberValue",fromNumberValue);
                        obj.put("paymentDaysOption",payOption);




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



                    GeneralCommands.logEventObject(parentActivity,"payment_pending","payment_pending",obj);
                    dialogPlus.setDialogCancelable(true);

                    loadingView.setVisibility(View.GONE);
                    activeView.setVisibility(View.GONE);
                    counterView.setVisibility(View.VISIBLE);





                    //Set Gif Wallet
                    Glide.with(parentActivity)
                            .asGif()
                            .load(gifUrlWallet) // Replace with your local GIF resource
                            .listener(new RequestListener<GifDrawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {

                                    // Fallback to local GIF if online GIF fails to load
                                    Handler mainHandler = new Handler(Looper.getMainLooper());

                                    mainHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Glide.with(parentActivity)
                                                    .asGif()
                                                    .load(R.drawable.loader_wallet) // Replace with your local GIF resource
                                                    .into(progressWheelWallet);
                                        }
                                    });


                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            })
                            .into(progressWheelWallet) ;

                    CountDownTimer timerPayment= new CountDownTimer(payment_confirmation_waiting_time_in_seconds*1000, 1000) {
                        public void onTick(long millisUntilFinished) {

                            long secondsRemaining=  (millisUntilFinished / 1000);
                            if(counterText.getVisibility()== View.GONE)counterText.setVisibility(View.VISIBLE);
                            counterText.setText(""+((double)secondsRemaining));

                        }

                        public void onFinish() {
                            // Handle countdown completion, if needed
                            progressWheelWallet.setVisibility(View.GONE);
                            confirmRips.setVisibility(View.VISIBLE);
                            counterText.setVisibility(View.GONE);



                            btnConfirmPayment.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    confirmRips.setVisibility(View.GONE);
                                    progressWheelWallet.setVisibility(View.VISIBLE);


                                    //Reconfirming Payment
                                    ob1.confirmSTKPayment(paymentId,false);
                                }
                            });


                        }
                    }.start();



                }

                else if(status !=null && status.equalsIgnoreCase("FAILED"))
                {

                    JSONObject obj = new JSONObject();

                    try {

                        obj.put("paymentTransactionId",paymentId);
                        obj.put("loanNumber",loanNumber);
                        obj.put(FirebaseAnalytics.Param.CURRENCY,currencyCode);
                        obj.put(FirebaseAnalytics.Param.VALUE,payAmountValue);
                        obj.put("amount",""+payAmountValue);
                        obj.put("paymentProvider",paymentProvider);
                        obj.put("fromNumberValue",fromNumberValue);
                        obj.put("paymentDaysOption",payOption);




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    GeneralCommands.logEventObject(parentActivity,"payment_failed","payment_failed",obj);

                    String error=mContext.getResources().getString(R.string.paymentfailed);
                    String errorHeader=mContext.getResources().getString(R.string.failed);


                    errorTitleText.setText(mContext.getResources().getString(R.string.processingpayment));
                    errorText.setText(error);
                    btnErrorText.setText(mContext.getResources().getString(R.string.retry));


                    dialogPlus.setDialogCancelable(true);


                }

            }
        });

        parentActivity.currentDialogPlus = dialogPlus;

        dialogPlus.show();
    }

    public static boolean isInteger(String s) {
        return isInteger(s,10);
    }

    public static boolean isInteger(String s, int radix) {
        Scanner sc = new Scanner(s.trim());
        if(!sc.hasNextInt(radix)) return false;
        // we know it starts with a valid int, now make sure
        // there's nothing left!
        sc.nextInt(radix);
        return !sc.hasNext();
    }

    public void goToMainPage()
    {
        Intent mIntent = new Intent(parentActivity, MainActivity.class);
        mIntent.putExtra(KEY_START_FROM, "splash");
        GeneralCommands.goToPage( parentActivity,mIntent,true);

    }

    public List<String> getListDataAsset()
    {
        listData= storageObject.retrieveAssetObjectObjectsFromPrefs();
        List<String> assets=  new ArrayList<String>();

        for (AssetObject data:listData) {
            assets.add(data.getAssetTypeName());
        }
        return assets;
    }

    public void selectAsset()
    {
        for (int i=0;i<getListDataAsset().size();i++)
        {
            if(storageObject.retrieveCurrentAssetObjectFromPrefs() !=null && storageObject.retrieveCurrentAssetObjectFromPrefs().getAssetTypeName() !=null)
            {
                if ((getListDataAsset().get(i) != null && storageObject.retrieveCurrentAssetObjectFromPrefs() != null) && getListDataAsset().get(i).equals(storageObject.retrieveCurrentAssetObjectFromPrefs().getAssetTypeName())) {
                    listAssets.setSelection(i);
                    break;
                }
            }
        }
    }

    public boolean check24HoursPassed(long timeStamp1, long timeStamp2)
    {
        long timeDifferenceInMillis = timeStamp2 - timeStamp1;
        long hoursDifference = TimeUnit.MILLISECONDS.toHours(timeDifferenceInMillis);

        return hoursDifference >= 24;
    }

    //Slider

//

    //Slider

    private void updateMarker(final SeekBar sb,
                              View rlMarker,
                              String message) {

        FirmaTextView  tvProgress = (FirmaTextView) rlMarker.findViewById(R.id.tvProgress);
        final View vArrow = (View) rlMarker.findViewById(R.id.vArrow);



        /**
         * one can find the SeekBar thumb location in pixels using:
         */
        int width = sb.getWidth() - sb.getPaddingLeft() - sb.getPaddingRight();

        final int thumbPos = sb.getPaddingLeft() + width  * sb.getProgress() / sb.getMax() +  //take into consideration the margin added (in this case it is 10dp)
                Math.round(convertDpToPixel(10, parentActivity));

        // Calculate extra margin when progress is at maximum

        if (sb.getProgress() == sb.getMax()) {
            extraMargin = Math.round(convertDpToPixel(20, parentActivity)); // Adjust this value as needed
        }


        tvProgress.setText(message);

        tvProgress.post(new Runnable() {
            @Override
            public void run() {

                final Display display = ((WindowManager)parentActivity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                final Point deviceDisplay = new Point();
                display.getSize(deviceDisplay);

                //vArrow always follow seekBar thumb location
                // Set arrow position to follow thumb
                vArrow.setX(thumbPos - vArrow.getWidth() / 2);

                // Adjust text view position relative to thumb
                if ((thumbPos - tvProgress.getWidth() / 2 - sb.getPaddingLeft()) < 0) {
                    // TextView is partially to the left of the screen
                    tvProgress.setX(vArrow.getX() - 20);

                } else if ((thumbPos + tvProgress.getWidth() / 2 + sb.getPaddingRight() + extraMargin) > deviceDisplay.x) {
                    // TextView is partially to the right of the screen

                    tvProgress.setX(vArrow.getX() - tvProgress.getWidth() + 20 + vArrow.getWidth() + extraMargin);
                } else {
                    // TextView is within the screen bounds
                    tvProgress.setX(thumbPos - tvProgress.getWidth() / 2);

                }

                //Make it not Cut on the Right
                if (sb.getProgress() == sb.getMax()) {
                    tvProgress.setX((vArrow.getX() - tvProgress.getWidth() + 20 + vArrow.getWidth() + extraMargin)-(extraMargin+ extraMargin));
                    vArrow.setX((thumbPos - vArrow.getWidth() / 2)-extraMargin);

                }
            }
        });
    }

    /**
     * According to this question:
     * https://stackoverflow.com/questions/4605527/converting-pixels-to-dp
     * one can convert dp to pixels using the following method:
     */
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    //End Slider

    public void  setDefaultConfigs()
    {
        //Remote Configs
        remoteConfig= new FirebaseConfig();
        mFirebaseConfig=remoteConfig.getConfigs();
        mFirebaseConfig.fetchAndActivate().addOnCompleteListener(parentActivity, new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    boolean updated = task.getResult();


                    //Has Slider for Payment Months or 3 Tabs
                    has_slider = mFirebaseConfig.getString("has_slider");
                    slider_max_value = (double) mFirebaseConfig.getLong("slider_max_value");
                    payment_confirmation_waiting_time_in_seconds = mFirebaseConfig.getLong("payment_confirmation_waiting_time_in_seconds");

                    try {

                        if(has_slider!=null && !has_slider.equals("")) {
                            JSONObject obj = new JSONObject(has_slider);

                            Log.d("ZeFireConfig", "ZeFireConfig" + obj);
                            refreshDetails(obj);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //Has Change Number View of Pay Other Number Tab View
                    show_change_number_view = mFirebaseConfig.getBoolean("show_change_number_view");
                    showChangeNumberView();


                    //Win Moment
                    win_moment_has_win_animation = mFirebaseConfig.getBoolean("win_moment_has_win_animation");
                    win_moment_has_animation_confetti = mFirebaseConfig.getBoolean("win_moment_has_animation_confetti");
                    win_moment_has_animation_confetti_and_scribbles = mFirebaseConfig.getBoolean("win_moment_has_animation_confetti_and_scribbles");

                    //Payment Options
                    is_direct_payment_enabled = mFirebaseConfig.getBoolean("is_direct_payment_enabled");
                    is_mpesa_payment_enabled = mFirebaseConfig.getBoolean("is_mpesa_payment_enabled");
                    is_afrimoney_payment_enabled = mFirebaseConfig.getBoolean("is_afrimoney_payment_enabled");
                    is_airtel_payment_enabled = mFirebaseConfig.getBoolean("is_airtel_payment_enabled");

                    //Payment USSD
                    is_ussd_mpesa_payment_enabled = mFirebaseConfig.getBoolean("is_ussd_mpesa_payment_enabled");
                    is_ussd_tigo_payment_enabled = mFirebaseConfig.getBoolean("is_ussd_tigo_payment_enabled");
                    is_ussd_airtel_payment_enabled = mFirebaseConfig.getBoolean("is_ussd_airtel_payment_enabled");

                    refreshPaymentOptions();

//					Toast.makeText(mContext, "Ozi"+maintenance_mode, Toast.LENGTH_SHORT).show();


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

                        //Has Change Number View of Pay Other Number Tab View
                        has_slider = mFirebaseConfig.getString("has_slider");
                        slider_max_value = (double) mFirebaseConfig.getLong("slider_max_value");
                        payment_confirmation_waiting_time_in_seconds =  mFirebaseConfig.getLong("payment_confirmation_waiting_time_in_seconds");

                        try {

                            if(has_slider!=null && !has_slider.equals("")) {
                                JSONObject obj = new JSONObject(has_slider);

                                Log.d("ZeFireConfig", "ZeFireConfig" + obj);
                                refreshDetails(obj);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Has Change Number View of Pay Other Number Tab View
                        show_change_number_view = mFirebaseConfig.getBoolean("show_change_number_view");
                        showChangeNumberView();


                        //Win Moment
                        win_moment_has_win_animation = mFirebaseConfig.getBoolean("win_moment_has_win_animation");
                        win_moment_has_animation_confetti = mFirebaseConfig.getBoolean("win_moment_has_animation_confetti");
                        win_moment_has_animation_confetti_and_scribbles = mFirebaseConfig.getBoolean("win_moment_has_animation_confetti_and_scribbles");


                        //Payment Options
                        is_direct_payment_enabled = mFirebaseConfig.getBoolean("is_direct_payment_enabled");
                        is_mpesa_payment_enabled = mFirebaseConfig.getBoolean("is_mpesa_payment_enabled");
                        is_afrimoney_payment_enabled = mFirebaseConfig.getBoolean("is_afrimoney_payment_enabled");
                        is_airtel_payment_enabled = mFirebaseConfig.getBoolean("is_airtel_payment_enabled");


                        //Payment USSD
                        is_ussd_mpesa_payment_enabled = mFirebaseConfig.getBoolean("is_ussd_mpesa_payment_enabled");
                        is_ussd_tigo_payment_enabled = mFirebaseConfig.getBoolean("is_ussd_tigo_payment_enabled");
                        is_ussd_airtel_payment_enabled = mFirebaseConfig.getBoolean("is_ussd_airtel_payment_enabled");

                        refreshPaymentOptions();

                    }
                });
            }

            @Override
            public void onError(FirebaseRemoteConfigException error) {
                Log.w("ZeConfigs", "Config update error with code: " + error.getCode(), error);
            }
        });

    }

    public void showSliderView(double slider_progress, double slider_max){

//        Toast.makeText(mContext,"Oya"+slider_default,Toast.LENGTH_SHORT).show();

        if(show_slider && currentAsset !=null && ((""+currentAsset.getAssetTypeId()).equals("4")))
        {
            sliderView.setVisibility(ViewGroup.VISIBLE);
            //buttonRipView.setVisibility(View.GONE);

            if(choosePlan !=null)
            {

                choosePlan.setText(mContext.getResources().getString(R.string.slidetoselect));

            }

        }
        else {
            //Dont Show SLider
           // sliderView.setVisibility(ViewGroup.GONE);

            choosePlan.setText(mContext.getResources().getString(R.string.choosepaymentplan));
            //buttonRipView.setVisibility(View.VISIBLE);
        }



    }

    //End Slider

    public void showChangeNumberView()
    {

        if(show_change_number_view)
        {
            //SHow Change Number view Instead of Tabs
            switchView.setVisibility(View.GONE);
            changeNumberView.setVisibility(View.VISIBLE);

        }
        else {

            //Continue with Regular Tab View
            changeNumberView.setVisibility(View.GONE);
            setSwitch();
        }



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

            show_slider= dataArray.getBoolean("show_slider");
            slider_default= dataArray.getInt("slider_default");

            setIsVisible();

        }

    }

    public void showWin(DialogPlus dialogPlus, RelativeLayout loading2View) {

//        Toast.makeText(mContext,"Wiin",Toast.LENGTH_SHORT).show();
        ImageView loadingGif = loading2View.findViewById(R.id.loadingGif);

        loading2View.setVisibility(View.VISIBLE);
        loadingGif.setVisibility(View.VISIBLE);


        if (win_moment_has_win_animation) {

            String winUrl = gifUrlConf;
            if(win_moment_has_animation_confetti_and_scribbles && win_moment_has_animation_confetti)
            {
                // Step 1: Create an array to hold the strings
                String[] stringArray = new String[2];

                // Step 2: Add the strings to the array
                stringArray[0] = gifUrlCons;
                stringArray[1] = gifUrlConf;

                // Step 3: Use a random number generator to select one of the strings
                Random random = new Random();
                int randomIndex = random.nextInt(stringArray.length);

                // Print the randomly selected string
                winUrl = stringArray[randomIndex];
            }

            else if(win_moment_has_animation_confetti_and_scribbles && !win_moment_has_animation_confetti)
            {
                winUrl=gifUrlCons;
            }

            else
            {
                winUrl=gifUrlCons;
            }

            //Confetti and Scribbles
            Glide.with(parentActivity)
                    .asGif()
                    .load(winUrl)
                    .error(Glide.with(parentActivity)
                            .asGif()
                            .load(R.drawable.loading_cons) // Replace with your local GIF resource
                            .listener(new RequestListener<GifDrawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                                    Toast.makeText(parentActivity, "Failed to load local GIF", Toast.LENGTH_SHORT).show();
                                    return false; // Allow Glide to handle the error and display placeholder if available
                                }

                                @Override
                                public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                                    resource.setLoopCount(LOOP_FOREVER); // Set loop count to 1 to detect the end of the first loop
                                    resource.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                                        @Override
                                        public void onAnimationEnd(Drawable drawable) {
                                            super.onAnimationEnd(drawable);
//                                                Toast.makeText(parentActivity, "GIF animation ended", Toast.LENGTH_SHORT).show();


                                        }
                                    });
                                    resource.start(); // Start the GIF animation
                                    return false; // Return false to allow Glide to handle the resource setting
                                }
                            })
                            .into(loadingGif))

                    .into(new CustomTarget<GifDrawable>() {
                        @Override
                        public void onResourceReady(@NonNull GifDrawable resource, @Nullable Transition<? super GifDrawable> transition) {
                            loadingGif.setImageDrawable(resource);
                            resource.setLoopCount(LOOP_FOREVER); // Set loop count to 1 to detect the end of the first loop
                            resource.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                                @Override
                                public void onAnimationEnd(Drawable drawable) {
                                    super.onAnimationEnd(drawable);
                                    // Handle the end of the GIF animation
                                    // For example, you could start a new activity or show a message
//                                 Toast.makeText(parentActivity, "GIF animation ended", Toast.LENGTH_SHORT).show();

                                    Toast.makeText(mContext, mContext.getResources().getString(R.string.thankyouforfeedback), Toast.LENGTH_SHORT).show();
//                                dialogPlus.dismiss();
//                                parentActivity.viewPager.setCurrentItem(1);

                                }
                            });
                            resource.start(); // Start the GIF animation
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            // Handle the cleanup of the resource if needed
                        }
                    });

        }

    }

    public static void goToLoanCompletePage()

    {
        Intent mIntent = new Intent(parentActivity, LoanCompleteActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
        GeneralCommands.goToPage(parentActivity, mIntent,false);

    }

    public void openDialogUSSD(String type,String usCode, String paybill, String loanNumber,String company)
    {
        View contentView = parentActivity.getLayoutInflater().inflate(R.layout.dialog_content_payment_ussd, null);
        ussdCode=usCode;


        final DialogPlus dialogPlus = new DialogPlus.Builder(parentActivity)
                .setContentHolder(new ViewHolder(contentView))
                .setGravity(DialogPlus.Gravity.BOTTOM)
                .setBackgroundColorResourceId(R.color.black)
                .setCancelable(true)
                .create();


        //Content View
        View content = dialogPlus.getHolderView();

        RelativeLayout loadingView= content.findViewById(R.id.loadingItems);



        RelativeLayout btnSubmit= content.findViewById(R.id.btnSubmit);
        RelativeLayout iconView= content.findViewById(R.id.iconView);

        ImageView payIcon= content.findViewById(R.id.payIcon);

        FirmaTextView paymentTitle = content.findViewById(R.id.paymentTitle);
        FirmaTextView paymentText = content.findViewById(R.id.paymentText);

        RelativeLayout btnBack= content.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPlus.dismiss();
            }
        });


        if(currentAsset!=null)
        {
            loanNumber=currentAsset.getLoanNumber();
            if(currentAsset.getAssetTypeId()==4)//SmartPhone so Loan Number is Phone
            {
                loanNumber=storageObject.retrieveUserPhoneNumberFromPrefs();

            }
        }

        if(type.equals("afrimoney"))
        {
            iconView.setBackgroundResource(R.xml.rounded_list_transparent_white_stroke);
            payIcon.setImageResource(R.drawable.ics_rect_afrimoney);
            paymentTitle.setText(mContext.getResources().getString(R.string.howtopayviaafrimoney));
            paymentTitle.setTextColor(mContext.getResources().getColor(R.color.theme_afrimoney_orange));
            //paymentText.setText(Html.fromHtml(mContext.getResources().getString(R.string.payafrimoney,ussdCode,userNumber,loanNumber,company)));

            JSONObject obj = new JSONObject();

            try {
                obj.put("loanNumber",loanNumber);
                obj.put(FirebaseAnalytics.Param.CURRENCY,currencyCode);
                obj.put(FirebaseAnalytics.Param.VALUE,payAmountValue);
                obj.put("amount",""+payAmountValue);
                obj.put("paymentProvider",paymentProvider);
                obj.put("fromNumberValue",fromNumberValue);
                obj.put("paymentDaysOption",payOption);
                obj.put("pay_option","afrimoney");

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            GeneralCommands.logEventObject(parentActivity,"click_pay_afrimoney","click_pay_afrimoney",obj);



        }

        else if(type.equals("airtel"))
        {
            iconView.setBackgroundResource(R.xml.rounded_list_transparent_orange_stroke);
            payIcon.setImageResource(R.drawable.ics_rect_airtel);
            paymentTitle.setText(mContext.getResources().getString(R.string.howtopayviaairtel));
            paymentTitle.setTextColor(mContext.getResources().getColor(R.color.theme_orange));
          //  paymentText.setText(Html.fromHtml(mContext.getResources().getString(R.string.payairtel,ussdCode,userNumber,loanNumber,company)));


            JSONObject obj = new JSONObject();

            try {
                obj.put("loanNumber",loanNumber);
                obj.put(FirebaseAnalytics.Param.CURRENCY,currencyCode);
                obj.put(FirebaseAnalytics.Param.VALUE,payAmountValue);
                obj.put("amount",""+payAmountValue);
                obj.put("paymentProvider",paymentProvider);
                obj.put("fromNumberValue",fromNumberValue);
                obj.put("paymentDaysOption",payOption);
                obj.put("pay_option","airtel");

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            GeneralCommands.logEventObject(parentActivity,"click_pay_airtel","click_pay_airtel",obj);

        }

        else
        {
            iconView.setBackgroundResource(R.xml.rounded_list_transparent_green_stroke);
            payIcon.setImageResource(R.drawable.ics_rect_mpesa);
            paymentTitle.setText(mContext.getResources().getString(R.string.howtopayviampesa));
            paymentTitle.setTextColor(mContext.getResources().getColor(R.color.theme_green));
            paymentText.setText(Html.fromHtml(mContext.getResources().getString(R.string.paympesa,ussdCode,userNumber,loanNumber,company)));



            JSONObject obj = new JSONObject();

            try {
                obj.put("loanNumber",currentAsset.getLoanNumber());
                obj.put(FirebaseAnalytics.Param.CURRENCY,currencyCode);
                obj.put(FirebaseAnalytics.Param.VALUE,payAmountValue);
                obj.put("amount",""+payAmountValue);
                obj.put("paymentProvider",paymentProvider);
                obj.put("fromNumberValue",fromNumberValue);
                obj.put("paymentDaysOption",payOption);
                obj.put("pay_option","mpesa");

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            GeneralCommands.logEventObject(parentActivity,"click_pay_mpesa","click_pay_mpesa",obj);


        }


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                dialogPlus.setDismiss();


                JSONObject obj = new JSONObject();

                try {
                    obj.put("loanNumber",currentAsset.getLoanNumber());
                    obj.put(FirebaseAnalytics.Param.CURRENCY,currencyCode);
                    obj.put(FirebaseAnalytics.Param.VALUE,payAmountValue);
                    obj.put("amount",""+payAmountValue);
                    obj.put("paymentProvider",paymentProvider);
                    obj.put("fromNumberValue",fromNumberValue);
                    obj.put("paymentDaysOption",payOption);




                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                GeneralCommands.logEventObject(parentActivity,"click_pay_done","click_pay_done",obj);

                ussdCode=usCode;
                openDialer();

            }
        });

        parentActivity.currentDialogPlus = dialogPlus;
        dialogPlus.show();
    }

    private Uri ussdToCallableUri(String ussd) {

        String uriString = "";

        if(!ussd.startsWith("tel:"))
            uriString += "tel:";

        for(char c : ussd.toCharArray()) {

            if(c == '#')
                uriString += Uri.encode("#");
            else
                uriString += c;
        }

        return Uri.parse(uriString);
    }

    public void openDialer() {
        int permissionCheck = ContextCompat.checkSelfPermission(parentActivity, Manifest.permission.CALL_PHONE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    parentActivity,
                    new String[]{Manifest.permission.CALL_PHONE},
                    123);
        } else {

            //All Good OpenDialer
            try{
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + Uri.encode(ussdCode)));
                startActivity(intent);
            } catch (SecurityException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case 123:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    openDialer();
                } else {
//                    Log.d("TAG", "Call Permission Not Granted");
                    Toast.makeText(parentActivity, mContext.getResources().getString(R.string.permallow), Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }

    public void refreshPaymentOptions() {
        is_direct_payment_enabled = true;
//        is_airtel_payment_enabled=true;
//        Log.d("ZeOptions","ZeOptions"+is_mpesa_payment_enabled+""+is_airtel_payment_enabled+""+is_tigo_payment_enabled);

        int weight = 0;
        if (is_direct_payment_enabled) {
            //Show Direct Payment Options
            paybillView.setVisibility(View.GONE);
            directPayView.setVisibility(View.VISIBLE);
            if (is_mpesa_payment_enabled && !StringUtils.isStringEmpty(setProvider("vodacom"))) {
                mpesaRips.setVisibility(View.VISIBLE);
                weight++;

            }

            if (is_airtel_payment_enabled && !StringUtils.isStringEmpty(setProvider("airtel"))) {
                airtelRips.setVisibility(View.VISIBLE);
                weight++;

            }


            if (is_tigo_payment_enabled && !StringUtils.isStringEmpty(setProvider("tigo"))) {
               // tigoRips.setVisibility(View.VISIBLE);
                weight++;

            }

//            paymentButtons.setWeightSum(weight);

            //Payment Ussd
            if (is_ussd_mpesa_payment_enabled) {
                ussdMpesaRips.setVisibility(View.VISIBLE);

            } else {
                ussdMpesaRips.setVisibility(View.GONE);
            }

            if (is_ussd_airtel_payment_enabled) {
                ussdAirtelRips.setVisibility(View.VISIBLE);

            } else {
                ussdAirtelRips.setVisibility(View.GONE);
            }


            if (is_ussd_tigo_payment_enabled) {
                ussdTigoRips.setVisibility(View.VISIBLE);

            } else {
                ussdTigoRips.setVisibility(View.GONE);
            }


            if (!is_ussd_tigo_payment_enabled && !is_ussd_airtel_payment_enabled && !is_ussd_mpesa_payment_enabled) {
                //paymentOptionH.setVisibility(View.GONE);
            } else {
               // paymentOptionH.setVisibility(View.VISIBLE);
            }


        } else {
            //Hide Direct Payments
            paybillView.setVisibility(View.VISIBLE);
            directPayView.setVisibility(View.GONE);
        }
    }

    public String setProvider(String inputProviderName)
    {
        paymentProviderID="";
        paymentProviderName="";

        ArrayList<ProvidersItem> providers = storageObject.retrieveProvidersFromPrefs();

        // Find the providerId
        for (ProvidersItem provider : providers) {

            if (provider.getProviderName().toLowerCase().contains(inputProviderName.toLowerCase())) {
                paymentProviderID = provider.getProviderId();
                paymentProviderName = provider.getProviderName();
                break; // Exit loop once match is found
            }

        }


//        if(providers.size()>0)
//        {
//            paymentProvidersH.setVisibility(View.VISIBLE);
//        }
//        else {
//            paymentProvidersH.setVisibility(View.GONE);
//        }


//        Log.d("ZeProvider","ZeProvider"+paymentProviderName+":"+paymentProviderID);

        return paymentProviderID;
    }

//    public void setSliderText(double days)
//    {
//
//        try {
//            days=Math.round(days);
//            days=Integer.parseInt(String.valueOf(days));
//
//            String dayText= mContext.getResources().getString(R.string.day);
//            if(days>1)
//            {
//                dayText= mContext.getResources().getString(R.string.days);
//            }
//
//            if(rlMarker!=null && seekBar!=null)updateMarker(seekBar, rlMarker, days+" "+dayText);
//        } catch (NumberFormatException e) {
//            e.printStackTrace();
//        }
//
//
//    }

    public void setSliderText(double days)
    {
        days=Math.round(days);
        try {
            days=Integer.parseInt(String.valueOf(days));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        String dayText= mContext.getResources().getString(R.string.day);
        if(days>1)
        {
            dayText= mContext.getResources().getString(R.string.days);
        }

        if(rlMarker!=null && seekBar!=null)updateMarker(seekBar, rlMarker, (int)days+" "+dayText);

    }
    public String getAssetName(AssetObject asset) {
        if (asset == null) return storageObject.retrieveUserPhoneNumberFromPrefs();

        if (StringUtils.isStringEmpty(asset.getVehicleModelName())) {
            return storageObject.retrieveUserPhoneNumberFromPrefs();
        }

        String name = asset.getVehicleMakeName() + " " + asset.getVehicleModelName();
        String[] shortName = asset.getVehicleModelName().split(" ");

        //SHorten the Name Logic
        if (shortName.length > 0 && shortName[0].length() > 0) {
            name = asset.getVehicleMakeName() + " " + shortName[0];
            if (shortName.length > 1) {
                if (shortName[1].contains("GB")) {
                    name = name + " " + shortName[1];

                }

            }
            if (shortName.length > 2) {
                if (shortName[2].contains("GB")) {
                    name = name + " " + shortName[2];

                }

            }
        }

        return name;
    }

    private void showPaymentOptions(String method, String amount, double numberOfDays, String user, String device, String pagaNumber, String paybill, String ussdCode) {
        Intent intent = new Intent(requireActivity(), PaymentProcessesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("method", method);
        bundle.putString("amount", amount);
        bundle.putDouble("numberOfDays", numberOfDays);
        bundle.putString("user", user);
        bundle.putString("device", device);
        bundle.putString("pagaNumber", pagaNumber);
        bundle.putString("ussdCode", ussdCode);
        bundle.putString("paybill", paybill);
        bundle.putString("company", "WATU CREDIT LTD");
        bundle.putString("loanNumber", loanNumber);


        intent.putExtras(bundle);

        intent.putExtra("KEY_START_FROM", "payments_process");
        GeneralCommands.goToPage(requireActivity(), intent, false);


    }


    private void setDeviceIcon(int assetTypeId) {

        if (iconDevice == null || ftvDevice == null || mContext == null) {
            return;
        }

        if (assetTypeId == 4) {
            iconDevice.setImageResource(R.drawable.ics_simu);
            ftvDevice.setText(mContext.getResources().getString(R.string.device));
        } else ftvDevice.setText(mContext.getResources().getString(R.string.asset));
        if (assetTypeId == 1 || assetTypeId == 5) iconDevice.setImageResource(R.drawable.ics_boda);
        if (assetTypeId == 10) iconDevice.setImageResource(R.drawable.ics_icon_tv);
        if (assetTypeId == 2 || assetTypeId == 7 || assetTypeId == 9)
            iconDevice.setImageResource(R.drawable.ics_tuk_tuk);
    }

}
