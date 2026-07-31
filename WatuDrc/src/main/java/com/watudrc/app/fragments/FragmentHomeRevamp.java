package com.watudrc.app.fragments;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.ConfigUpdate;
import com.google.firebase.remoteconfig.ConfigUpdateListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;
import com.watudrc.app.DocumentsActivity;
import com.watudrc.app.GeneralCommands;
import com.watudrc.app.LevelUpActivity;
import com.watudrc.app.MainActivity;
import com.watudrc.app.NotificationsActivity;
import com.watudrc.app.R;
import com.watudrc.app.UpgradeActivity;
import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.font.FirmaTextView;
import com.watudrc.app.mobileservices.GetData;
import com.watudrc.app.objects.AdCard;
import com.watudrc.app.objects.AssetObject;
import com.watudrc.app.objects.LoanListItemObject;
import com.watudrc.app.objects.ServiceDownObject;
import com.watudrc.app.objects.UnlockInfoResponse;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;
import com.watudrc.app.util.FirebaseConfig;
import com.watudrc.app.util.ImageUtil;
import com.watudrc.app.view.MaterialRippleLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class FragmentHomeRevamp extends Fragment implements IWhereMyLocationConstants {

    public static MainActivity parentActivity;
    public static FirmaTextView userName;
    public static FirmaTextView userInitials;
    static Context mContext;
    private static StoreAndGetFromPrefs storageObject;
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); // You can customize the format
    SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm"); // You can customize the format
    //Network Check
    BroadcastReceiver networkChangeReceiver;
    ImageView progressWheelNetwork;
    RelativeLayout networkView;
    RelativeLayout btnNetwork;
    LayoutInflater vi;
    AssetObject currentAsset;
    ArrayList<AssetObject> listData = new ArrayList<>();

    ArrayList<AssetObject> listDataSimu = new ArrayList<>();
    ArrayList<AssetObject> listDataBoda = new ArrayList<>();
    ArrayList<AssetObject> listDataTv = new ArrayList<>();
    ArrayList<AssetObject> listDataTukTuk = new ArrayList<>();
    ArrayList<AssetObject> listDataGari = new ArrayList<>();

    RelativeLayout loadingView;
    ScrollView scrollView;
    RelativeLayout btnUserIcon;
    RelativeLayout btnNotifications;
    RelativeLayout btnRefreshAccount;
    LinearLayout assetsView;
    int assetsAvailable = 0;
    MaterialRippleLayout simuRips, bodaRips, tvRips, tukTukRips, gariRips;
    MaterialRippleLayout mainRips;
    ImageView iconMain;
    RelativeLayout btnSimu, btnBoda, btnTv, btnTukTuk, btnGari;
    ImageView iconSimu, iconBoda, iconTv, iconTukTuk, iconGari;
    //CounterView
    RelativeLayout activeUntilView, tempUnlockView, countView;
    MaterialRippleLayout unlockToolTipRips, payToolTipRips;
    MaterialRippleLayout unlockRips;
    RelativeLayout btnPay, btnUnlock;
    RelativeLayout btnDaily, btnWeekly, btnMonthly;
    RelativeLayout btnUnlockTip, btnPayTip;
    ImageView iconLock;
    FirmaTextView deviceActiveH;
    FirmaTextView deviceActiveDate;
    FirmaTextView tvDaysValue, tvHoursValue, tvMinutesValue, tvSecondsValue;
    //Unlock Limit View
    RelativeLayout unlockLimitView;
    ImageView unlockIcon;
    FirmaTextView unlockText;
    //Amount Card
    FirmaTextView amountValue, amountToUnlockLabel;
    FirmaTextView loanTotalPayment, loanBalance;
    //How To Pay Unlock
    CarouselView previewImagesUnlockView;
    ViewListener viewListenerUnlock;
    String carousel_view_unlock = "";
    int[] previewImagesUnlock = {R.drawable.t1, R.drawable.t2, R.drawable.t3, R.drawable.t4, R.drawable.t5};
    boolean has_how_to_pay_tooltip = true;
    //How to Pay ToolTIp
    ViewListener viewListenerPay;
    CarouselView previewImagesPayView;
    String carousel_view_pay = "";
    int[] previewImagesPay = {R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4, R.drawable.p5, R.drawable.p6};
    boolean has_how_to_temporary_unlock_tooltip = true;
    FirebaseConfig remoteConfig;
    FirebaseRemoteConfig mFirebaseConfig;
    String current_ad_card = "";
    int default_timeout = 8;
    boolean accessibility_has_pay_icon = true;
    //Accesibility
    ImageView payIcon;
    //Lower Section
    FirmaTextView progressText;
    SeekBar seekBar;
    RelativeLayout btnDownloadStatement;
    FirmaTextView asOf;
    FirmaTextView issueDateH;
    FirmaTextView loanDailyAmount, loanWeeklyAmount, loanMonthlyAmount;
    RelativeLayout btnCheckProgress;
    //Loan Complete View
    RelativeLayout loanCompleteView;
    LinearLayout activeLoanView;
    MaterialRippleLayout statementRips, statementCompleteRips;
    RelativeLayout btnDownloadStatementComplete;
    FirmaTextView totalPaymentText, outstandingBalanceText, installmentAmountText, dailyPaymentText;
    //Jijenge Rips
    RelativeLayout jijengeContainer;
    RelativeLayout upgradeUnlockedView;
    ImageView upgradeLockedView;
    RelativeLayout btnUpgradeNow;
    private View parentView;
    private boolean isReceiverRegistered = false;
    //Top View
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout secondaryDevicesContainer;
    private View selectedDeviceView = null;
    private CountDownTimer timerUnlock, timerPayment;

    private ImageView adCard;

    public static void goToDocumentsPage() {
        Intent mIntent = new Intent(parentActivity, DocumentsActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
//        mIntent.putExtra(ITEM_ID, position);
        GeneralCommands.goToPage(parentActivity, mIntent, false);

    }

    public static void setWaitingTime(CountDownTimer timerUnlock, RelativeLayout unlockLimitView, FirmaTextView unlockText, ImageView unlockIcon, RelativeLayout btnUnlock, long waitingTime) {
        if (waitingTime < 0) {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.maxunlockattempts), Toast.LENGTH_SHORT).show();
            //Max Attempts Reached
            unlockLimitView.setVisibility(View.VISIBLE);

            unlockText.setText(mContext.getResources().getString(R.string.temporaryunlock));
            unlockIcon.setImageResource(R.drawable.ics_unlock_locked);
            btnUnlock.setBackgroundResource(R.xml.rounded_list_button_orange);

            //Reset Active Till
            storageObject.saveTempActiveTillToPrefs(0);

            btnUnlock.setEnabled(true);


        } else if (waitingTime > 0) {
            unlockLimitView.setVisibility(View.GONE);
            btnUnlock.setEnabled(false);


            long currentTimePayment = System.currentTimeMillis();
            long timeRemainingPayment = waitingTime - currentTimePayment;

            if (timeRemainingPayment < 0) {
                //Already Unlcoked
                unlockText.setText(mContext.getResources().getString(R.string.devicealreadyunlocked));
                unlockIcon.setImageResource(R.drawable.ics_unlock_timer);
                btnUnlock.setBackgroundResource(R.xml.rounded_list_transparent_orange_light_stroke);

                storageObject.saveTempActiveTillToPrefs(0);


            } else {


                if (timerUnlock != null) timerUnlock.cancel();
                //There is time
                timerUnlock = new CountDownTimer(timeRemainingPayment, 1000) {
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

                        unlockText.setText(mContext.getResources().getString(R.string.temporarilyunlockedfor) + "\n" + days + ":" + hrs + ":" + mns + ":" + scs + " " + mContext.getResources().getString(R.string.minutesremaining));


                    }

                    public void onFinish() {
                        // Handle countdown completion, if needed
                        unlockText.setText(mContext.getResources().getString(R.string.temporaryunlock));
                        unlockIcon.setImageResource(R.drawable.ics_unlock);
                        btnUnlock.setBackgroundResource(R.xml.rounded_list_button_orange_light);

                        storageObject.saveTempActiveTillToPrefs(0);
                        btnUnlock.setEnabled(true);


                    }
                }.start();


                unlockIcon.setImageResource(R.drawable.ics_unlock_timer);
                btnUnlock.setBackgroundResource(R.xml.rounded_list_transparent_orange_light_stroke);


            }


        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_home_revamp, container, false);
        parentActivity = (MainActivity) getActivity();
        mContext = parentActivity.getApplicationContext();
        storageObject = new StoreAndGetFromPrefs(mContext);
        loadingView = parentView.findViewById(R.id.loadingItems);
        scrollView = parentView.findViewById(R.id.scrollView);
        swipeRefreshLayout = parentView.findViewById(R.id.swipeRefreshLayout);
        vi = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //        Ad Card
        adCard = parentView.findViewById(R.id.ad_card);
        refreshAdCard();


        //Network
        networkView = loadingView.findViewById(R.id.networkView);
        btnNetwork = loadingView.findViewById(R.id.btnNetwork);
        progressWheelNetwork = loadingView.findViewById(R.id.progressWheelNetwork);


        jijengeContainer = parentView.findViewById(R.id.jijengeContainer);
        upgradeLockedView = parentView.findViewById(R.id.upgradeLockedView);
        upgradeUnlockedView = parentView.findViewById(R.id.upgradeUnlockedView);
        btnUpgradeNow = parentView.findViewById(R.id.btnUpgradeNow);

        btnUserIcon = parentView.findViewById(R.id.btnUserIcon);


        userName = parentView.findViewById(R.id.userName);
        userInitials = parentView.findViewById(R.id.userInitials);


        userName = parentView.findViewById(R.id.userName);
        userInitials = parentView.findViewById(R.id.userInitials);

        userName.setText(storageObject.retrieveUserNameFromPrefs());
        try {
            userInitials.setText(GeneralCommands.getUserInitials(storageObject.retrieveUserNameFromPrefs()));
            userName.setText(storageObject.retrieveUserFirstNameFromPrefs());

        } catch (Exception e) {
            e.printStackTrace();
        }
        btnNotifications = parentView.findViewById(R.id.btnNotifications);
        btnRefreshAccount = parentView.findViewById(R.id.btnRefreshAccount);

        assetsView = parentView.findViewById(R.id.assetsView);
        mainRips = parentView.findViewById(R.id.mainRips);
        iconMain = parentView.findViewById(R.id.iconMain);


        simuRips = parentView.findViewById(R.id.simuRips);
        bodaRips = parentView.findViewById(R.id.bodaRips);
        tvRips = parentView.findViewById(R.id.tvRips);

        tukTukRips = parentView.findViewById(R.id.tukTukRips);
        gariRips = parentView.findViewById(R.id.gariRips);

        btnSimu = parentView.findViewById(R.id.btnSimu);
        btnBoda = parentView.findViewById(R.id.btnBoda);
        btnTv = parentView.findViewById(R.id.btnTv);

        btnTukTuk = parentView.findViewById(R.id.btnTukTuk);
        btnGari = parentView.findViewById(R.id.btnGari);

        iconBoda = parentView.findViewById(R.id.iconBoda);
        iconSimu = parentView.findViewById(R.id.iconSimu);
        iconTv = parentView.findViewById(R.id.iconTv);

        iconTukTuk = parentView.findViewById(R.id.iconTukTuk);
        iconGari = parentView.findViewById(R.id.iconGari);

        secondaryDevicesContainer = parentView.findViewById(R.id.secondaryDevicesContainer);

        //CounterView
        activeUntilView = parentView.findViewById(R.id.activeUntilView);
        tempUnlockView = parentView.findViewById(R.id.tempUnlockView);
        countView = parentView.findViewById(R.id.countView);
        unlockToolTipRips = parentView.findViewById(R.id.unlockToolTipRips);
        btnUnlockTip = parentView.findViewById(R.id.btnUnlockTip);

        payToolTipRips = parentView.findViewById(R.id.payToolTipRips);
        btnPayTip = parentView.findViewById(R.id.btnPayTip);

        btnDaily = parentView.findViewById(R.id.btnDaily);
        btnWeekly = parentView.findViewById(R.id.btnWeekly);
        btnMonthly = parentView.findViewById(R.id.btnMonthly);


        unlockRips = parentView.findViewById(R.id.unlockRips);
        btnUnlock = parentView.findViewById(R.id.btnUnlock);

        deviceActiveDate = parentView.findViewById(R.id.deviceActiveDate);


        iconLock = parentView.findViewById(R.id.iconLock);
        deviceActiveH = parentView.findViewById(R.id.deviceActiveH);
        tvDaysValue = parentView.findViewById(R.id.tvDaysValue);
        tvHoursValue = parentView.findViewById(R.id.tvHoursValue);
        tvMinutesValue = parentView.findViewById(R.id.tvMinutesValue);
        tvSecondsValue = parentView.findViewById(R.id.tvSecondsValue);

        btnUnlock = parentView.findViewById(R.id.btnUnlock);
        unlockLimitView = parentView.findViewById(R.id.unlockLimitView);


        unlockIcon = parentView.findViewById(R.id.unlockIcon);
        unlockText = parentView.findViewById(R.id.unlockText);

//		countdownRunnable = new CountdownRunnable(handler,unlockText, storageObject.retrieveUserTempActiveTillFromPrefs());

        statementRips = parentView.findViewById(R.id.statementRips);
        statementCompleteRips = parentView.findViewById(R.id.statementCompleteRips);


        //Amount Card View
        payIcon = parentView.findViewById(R.id.payIcon);
        amountValue = parentView.findViewById(R.id.amountValue);
        amountToUnlockLabel = parentView.findViewById(R.id.amountToUnlockLabel);

        btnPay = parentView.findViewById(R.id.btnPay);

        //Progress View
        loanBalance = parentView.findViewById(R.id.loanBalance);
        loanTotalPayment = parentView.findViewById(R.id.loanTotalPayment);

        //Lower View

        btnDownloadStatement = parentView.findViewById(R.id.btnDownloadStatement);
        asOf = parentView.findViewById(R.id.asOf);
        issueDateH = parentView.findViewById(R.id.issueDateText);

        loanDailyAmount = parentView.findViewById(R.id.loanDailyAmount);
        loanWeeklyAmount = parentView.findViewById(R.id.loanWeeklyAmount);
        loanMonthlyAmount = parentView.findViewById(R.id.loanMonthlyAmount);

        btnCheckProgress = parentView.findViewById(R.id.btnCheckProgress);

        //Loan COmplete Views
        loanCompleteView = parentView.findViewById(R.id.loanCompleteView);
        activeLoanView = parentView.findViewById(R.id.activeLoanView);

        btnDownloadStatementComplete = parentView.findViewById(R.id.btnDownloadStatementComplete);

        totalPaymentText = parentView.findViewById(R.id.totalPaymentText);
        outstandingBalanceText = parentView.findViewById(R.id.outstandingBalanceText);
        installmentAmountText = parentView.findViewById(R.id.installmentAmountText);
        dailyPaymentText = parentView.findViewById(R.id.dailyPaymentText);


        btnSimu.setOnClickListener(v -> {

            btnSimu.setBackgroundResource(R.xml.rounded_circle_transparent_black_small_green_stroke);
            iconSimu.setColorFilter(ContextCompat.getColor(parentActivity, R.color.theme_green), PorterDuff.Mode.SRC_IN);

            btnBoda.setBackgroundResource(R.xml.rounded_circle_transparent_black_small);
            iconBoda.setColorFilter(ContextCompat.getColor(parentActivity, R.color.white), PorterDuff.Mode.SRC_IN);

            btnTv.setBackgroundResource(R.xml.rounded_circle_transparent_black_small);
            iconTv.setColorFilter(ContextCompat.getColor(parentActivity, R.color.white), PorterDuff.Mode.SRC_IN);

            btnTukTuk.setBackgroundResource(R.xml.rounded_circle_transparent_black_small);
            iconTukTuk.setColorFilter(ContextCompat.getColor(parentActivity, R.color.white), PorterDuff.Mode.SRC_IN);

            btnGari.setBackgroundResource(R.xml.rounded_circle_transparent_black_small);
            iconGari.setColorFilter(ContextCompat.getColor(parentActivity, R.color.white), PorterDuff.Mode.SRC_IN);


            GeneralCommands.logEvent(parentActivity, "click_button_simu", "click_button_simu");
            setAssets("simu", listDataSimu);

        });

        btnBoda.setOnClickListener(v -> {

            btnBoda.setBackgroundResource(R.xml.rounded_circle_transparent_black_small_green_stroke);
            iconBoda.setColorFilter(ContextCompat.getColor(parentActivity, R.color.theme_green), PorterDuff.Mode.SRC_IN);

            btnSimu.setBackgroundResource(R.xml.rounded_circle_transparent_black_small);
            iconSimu.setColorFilter(ContextCompat.getColor(parentActivity, R.color.white), PorterDuff.Mode.SRC_IN);

            btnTv.setBackgroundResource(R.xml.rounded_circle_transparent_black_small);
            iconTv.setColorFilter(ContextCompat.getColor(parentActivity, R.color.white), PorterDuff.Mode.SRC_IN);

            btnTukTuk.setBackgroundResource(R.xml.rounded_circle_transparent_black_small);
            iconTukTuk.setColorFilter(ContextCompat.getColor(parentActivity, R.color.white), PorterDuff.Mode.SRC_IN);

            btnGari.setBackgroundResource(R.xml.rounded_circle_transparent_black_small);
            iconGari.setColorFilter(ContextCompat.getColor(parentActivity, R.color.white), PorterDuff.Mode.SRC_IN);


            GeneralCommands.logEvent(parentActivity, "click_button_boda", "click_button_boda");

            setAssets("boda", listDataBoda);

        });

        btnTv.setOnClickListener(v -> {

            btnTv.setBackgroundResource(R.xml.rounded_circle_transparent_black_small_green_stroke);
            iconTv.setColorFilter(ContextCompat.getColor(parentActivity, R.color.theme_green), PorterDuff.Mode.SRC_IN);

            btnSimu.setBackgroundResource(R.xml.rounded_circle_transparent_black_small);
            iconSimu.setColorFilter(ContextCompat.getColor(parentActivity, R.color.white), PorterDuff.Mode.SRC_IN);

            btnBoda.setBackgroundResource(R.xml.rounded_circle_transparent_black_small);
            iconBoda.setColorFilter(ContextCompat.getColor(parentActivity, R.color.white), PorterDuff.Mode.SRC_IN);

            btnTukTuk.setBackgroundResource(R.xml.rounded_circle_transparent_black_small);
            iconTukTuk.setColorFilter(ContextCompat.getColor(parentActivity, R.color.white), PorterDuff.Mode.SRC_IN);

            btnGari.setBackgroundResource(R.xml.rounded_circle_transparent_black_small);
            iconGari.setColorFilter(ContextCompat.getColor(parentActivity, R.color.white), PorterDuff.Mode.SRC_IN);


            GeneralCommands.logEvent(parentActivity, "click_button_tv", "click_button_tv");

            setAssets("tv", listDataTv);

        });

        btnTukTuk.setOnClickListener(v -> {


            btnTukTuk.setBackgroundResource(R.xml.rounded_circle_transparent_black_small_green_stroke);
            iconTukTuk.setColorFilter(ContextCompat.getColor(parentActivity, R.color.theme_green), PorterDuff.Mode.SRC_IN);

            btnGari.setBackgroundResource(R.xml.rounded_circle_transparent_black_small);
            iconGari.setColorFilter(ContextCompat.getColor(parentActivity, R.color.white), PorterDuff.Mode.SRC_IN);


            btnTv.setBackgroundResource(R.xml.rounded_circle_transparent_black_small);
            iconTv.setColorFilter(ContextCompat.getColor(parentActivity, R.color.white), PorterDuff.Mode.SRC_IN);

            btnSimu.setBackgroundResource(R.xml.rounded_circle_transparent_black_small);
            iconSimu.setColorFilter(ContextCompat.getColor(parentActivity, R.color.white), PorterDuff.Mode.SRC_IN);

            btnBoda.setBackgroundResource(R.xml.rounded_circle_transparent_black_small);
            iconBoda.setColorFilter(ContextCompat.getColor(parentActivity, R.color.white), PorterDuff.Mode.SRC_IN);

            GeneralCommands.logEvent(parentActivity, "click_button_tuktuk", "click_button_tuktuk");

            setAssets("tuktuk", listDataTukTuk);

        });

        btnGari.setOnClickListener(v -> {


            btnGari.setBackgroundResource(R.xml.rounded_circle_transparent_black_small_green_stroke);
            iconGari.setColorFilter(ContextCompat.getColor(parentActivity, R.color.theme_green), PorterDuff.Mode.SRC_IN);

            btnTukTuk.setBackgroundResource(R.xml.rounded_circle_transparent_black_small);
            iconTukTuk.setColorFilter(ContextCompat.getColor(parentActivity, R.color.white), PorterDuff.Mode.SRC_IN);


            btnTv.setBackgroundResource(R.xml.rounded_circle_transparent_black_small);
            iconTv.setColorFilter(ContextCompat.getColor(parentActivity, R.color.white), PorterDuff.Mode.SRC_IN);

            btnSimu.setBackgroundResource(R.xml.rounded_circle_transparent_black_small);
            iconSimu.setColorFilter(ContextCompat.getColor(parentActivity, R.color.white), PorterDuff.Mode.SRC_IN);

            btnBoda.setBackgroundResource(R.xml.rounded_circle_transparent_black_small);
            iconBoda.setColorFilter(ContextCompat.getColor(parentActivity, R.color.white), PorterDuff.Mode.SRC_IN);

            GeneralCommands.logEvent(parentActivity, "click_button_gari", "click_button_gari");

            setAssets("gari", listDataTv);

        });


        seekBar = parentView.findViewById(R.id.seekBar);
        seekBar.setOnTouchListener((v, event) -> true);
        progressText = parentView.findViewById(R.id.progressText);


//        Toast.makeText(parentActivity,"OZe"+ storageObject.retrieveUserFirstNameFromPrefs()+""+(storageObject.retrieveUserNameFromPrefs()), Toast.LENGTH_SHORT).show();


        btnUserIcon.setOnClickListener(v -> parentActivity.toggleDrawer());


        currentAsset = storageObject.retrieveCurrentAssetObjectFromPrefs();
        if (currentAsset == null && storageObject.retrieveAssetObjectObjectsFromPrefs().size() > 0) {

            storageObject.saveCurrentAssetObjectToPrefs(storageObject.retrieveAssetObjectObjectsFromPrefs().get(0));
            currentAsset = storageObject.retrieveCurrentAssetObjectFromPrefs();

        }

        if (storageObject.retrieveAssetObjectObjectsFromPrefs().size() > 0) {
            //Get List data offline

            listData = storageObject.retrieveAssetObjectObjectsFromPrefs();
            if (listData.size() > 0) {
                setAssetData(listData);
            } else {
                getListData();
            }


        }


        btnNotifications.setOnClickListener(v -> {

            GeneralCommands.logEvent(parentActivity, "click_go_to_notifications", "click_go_to_notifications");
            MainActivity.viewPager.setCurrentItem(3);
        });

        btnRefreshAccount.setOnClickListener(v -> {


            JSONObject obj = new JSONObject();
            try {
                obj.put("loanNumber", currentAsset.getLoanNumber());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            GeneralCommands.logEventObject(parentActivity, "click_refresh_loan", "click_refresh_loan", obj);
            refreshFetchData();

        });

        //Should show statement
        if (storageObject.hasLoanAgreement()) {
            statementRips.setVisibility(View.VISIBLE);
            statementCompleteRips.setVisibility(View.VISIBLE);
        } else {
            statementRips.setVisibility(View.GONE);
            statementCompleteRips.setVisibility(View.GONE);

        }

        btnDownloadStatement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                JSONObject obj = new JSONObject();
                try {

                    obj.put("assetLoanNumber", currentAsset.getLoanNumber());
                    obj.put("assetName", currentAsset.getAssetTypeName());
                    obj.put("assetTypeId", currentAsset.getAssetTypeId());


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                GeneralCommands.logEventObject(parentActivity, "click_download_statement", "click_download_statement", obj);
                //  goToDocumentsPage();

                String loanNumber = storageObject.retrieveCurrentAssetObjectFromPrefs().getLoanNumber();

                GetData ob1 = new GetData();
                ob1.initialize(parentActivity, loadingView);
                ob1.userDownloadLoanAgreement(loanNumber, true,false);

            }
        });

        btnDownloadStatementComplete.setOnClickListener(v -> {


            JSONObject obj = new JSONObject();
            try {

                obj.put("assetLoanNumber", currentAsset.getLoanNumber());
                obj.put("assetName", currentAsset.getAssetTypeName());
                obj.put("assetTypeId", currentAsset.getAssetTypeId());


            } catch (JSONException e) {
                e.printStackTrace();
            }

            GeneralCommands.logEventObject(parentActivity, "click_download_statement", "click_download_statement", obj);
            goToDocumentsPage();

        });


        btnCheckProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                JSONObject obj = new JSONObject();
                try {

                    obj.put("assetLoanNumber", currentAsset.getLoanNumber());
                    obj.put("assetName", currentAsset.getAssetTypeName());
                    obj.put("assetTypeId", currentAsset.getAssetTypeId());


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                GeneralCommands.logEventObject(parentActivity, "click_check_progress", "click_check_progress", obj);
                goToUpgradePage();

            }
        });


        new Thread(new Runnable() {
            public void run() {
                // a potentially time consuming task
                parentView.post(new Runnable() {
                    public void run() {
                        //Set DAta
                        if (storageObject.retrieveAssetObjectObjectsFromPrefs().size() > 0) {
                            //Get SIlently
                            getListDataSilent();

                        } else {
                            refreshFetchData();
                        }
                    }
                });
            }
        }).start();

        // Initialize SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Implement your refresh logic here
            // Typically, you would initiate data loading or perform any other refresh action
            // Once the refresh action is complete, call setRefreshing(false) to stop the loading animation
            refreshFetchData();
        });

        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            // Enable swipe-to-refresh only if scrolled to the top
            swipeRefreshLayout.setEnabled(scrollView.getScrollY() == 0);
        });


        //Network Check
        networkChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                // Check if network is connected
                if (networkInfo != null && networkInfo.isConnected()) {
                    // Network is connected - update the fragment views
                    loadingView.setVisibility(View.GONE);
                    networkView.setVisibility(View.GONE);

                    storageObject.saveTimeLastOnline(System.currentTimeMillis());


//					Toast.makeText(context, "Network is ON", Toast.LENGTH_SHORT).show();
                } else {
                    // Network is disconnected - update the fragment views
                    loadingView.setVisibility(View.VISIBLE);
                    networkView.setVisibility(View.VISIBLE);

                    try {
                        Glide.with(parentActivity)
                                .asGif()
                                .load(R.drawable.loader_network) // Replace with your local GIF resource
                                .into(progressWheelNetwork);

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


//					Toast.makeText(context, "Network is OFF", Toast.LENGTH_SHORT).show();
                }
            }
        };

        btnNetwork.setOnClickListener(v -> {
            GeneralCommands.logEvent(parentActivity, "click_turn_on_data", "click_turn_on_data");

            // Open network settings
            openDataUsageSettings(parentActivity);
        });


        //End Network


        try {
            setConfigsUnlock();
            setCarouselDetailsUnlock();

        } catch (Exception e) {
        }


        return parentView;
    }

    private void refreshFetchData() {
        new Thread(new Runnable() {
            public void run() {
                // a potentially time consuming task
                parentView.post(new Runnable() {
                    public void run() {
                        setAdapter();
                    }
                });
            }
        }).start();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that we are currently visible
        if (this.isVisible()) {
            // If we are becoming invisible, then...
            if (isVisibleToUser) {
                GeneralCommands.logEvent(parentActivity, "view_page", "view_page");
                // TODO stop audio playback
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void onStart() {
        super.onStart();
        // Register the receiver if it's not registered
        if (!isReceiverRegistered && getContext() != null) {
            try {
                IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
                getContext().registerReceiver(networkChangeReceiver, filter);
                isReceiverRegistered = true;
            } catch (Exception e) {
                // Handle any exceptions during registration
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // Unregister the receiver if it's registered
        if (isReceiverRegistered && getContext() != null) {
            try {
                getContext().unregisterReceiver(networkChangeReceiver);
                isReceiverRegistered = false;
            } catch (Exception e) {
                // Handle the case where the receiver was not registered
                e.printStackTrace();
            }

        }
    }

    public void goToNotificationsPage() {
        Intent mIntent = new Intent(parentActivity, NotificationsActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
        GeneralCommands.goToPage(parentActivity, mIntent, false);

    }

    public void setAdapter() {
        //Add Images
        getListData();
        swipeRefreshLayout.setRefreshing(false);

    }

    public void setAssetData(ArrayList<AssetObject> assets) {

        if (assets == null || assets.size() == 0) return; // No assets to process
        clearListDataAssets();

        for (AssetObject data : assets) {
            String type = "" + data.getAssetTypeId();

            if (type.equals("4"))//Smartphone User
            {
                storageObject.setIsSimuUser(true);
            } else {
                storageObject.setIsBodaUser(true);
            }

            addLocalAssetsData(data);

        }

        //Done Adding Assets Set Default
        //Set Assets
//        setAssets("simu",listDataSimu);
        toggleDefaultAsset();
        toggleAssetsVisibility();

    }

    private void makeDailyPayment() {
        storageObject.saveCurrentAssetObjectToPrefs(currentAsset);

        JSONObject obj = new JSONObject();
        try {

            obj.put("assetLoanNumber", currentAsset.getLoanNumber());
            obj.put("assetName", currentAsset.getAssetTypeName());
            obj.put("assetTypeId", currentAsset.getAssetTypeId());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        GeneralCommands.logEventObject(parentActivity, "click_pay_loan_asset", "click_pay_loan_asset", obj);
        MainActivity.is_from_home_page = true;
        MainActivity.is_source = "daily";
        MainActivity.viewPager.setCurrentItem(2);
    }

    //    Show Subsequent Days Temp Unlocks Remaining- Technically Day 1 for 3/3 and Day 2 for 2/3 remaining attempts
    private void showTemporaryUnlockMaxAttemptsDialog(int maxAttempts, int attempts, long globalMax) {
        Dialog dialog = new Dialog(parentActivity);
        dialog.setContentView(R.layout.dialog_temp_unlock_max_attempts);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        ImageView ivClose = dialog.findViewById(R.id.iv_close);
        RelativeLayout btnPayNow = dialog.findViewById(R.id.btnPayNow);
        RelativeLayout btnTempUnlock = dialog.findViewById(R.id.btnTempUnlock);
        FirmaTextView payPrompt = dialog.findViewById(R.id.ftvPayPrompt);
        FirmaTextView ftvDescription = dialog.findViewById(R.id.ftvDescription);
        FirmaTextView ftvAttempts = dialog.findViewById(R.id.ftvAttempts);
        FirmaTextView ftvMaxAttempts = dialog.findViewById(R.id.ftvMaxAttempts);
        ImageView ivIcon = dialog.findViewById(R.id.ivIcon);

        MaterialRippleLayout tempUnlockRips = dialog.findViewById(R.id.tempUnlockRips);

        MaterialRippleLayout learnToPayRips = dialog.findViewById(R.id.learnToPayRips);
        RelativeLayout btnLearnToPay = dialog.findViewById(R.id.btnLearnToPay);
        btnLearnToPay.setOnClickListener(v -> {
            dialog.dismiss();
            openDialogPayCarousel();
        });


        int global = (int) globalMax;

        ftvAttempts.setText("" + attempts);
        ftvMaxAttempts.setText(getResources().getString(R.string.max_unlocks_left, maxAttempts));

        if (maxAttempts == 3 && attempts == 3) {
            payPrompt.setText(getResources().getString(R.string.make_a_payment_to_unlock_and_keep_using_your_device));
            ftvDescription.setText(getResources().getString(R.string.you_have_3_temporary_unlocks_remaining_today, maxAttempts));
            btnTempUnlock.setBackgroundResource(R.xml.rounded_list_button_orange_light);

        } else if (maxAttempts == 3 && attempts == 2) {
            int tintColor = ContextCompat.getColor(parentActivity, R.color.theme_orange_light);
            ivIcon.setColorFilter(tintColor);

            ftvAttempts.setTextColor(getResources().getColor(R.color.theme_orange_light));
            ftvMaxAttempts.setTextColor(getResources().getColor(R.color.theme_orange_light));
            payPrompt.setText(getResources().getString(R.string.payment_prompt_2_attempts));
            ftvDescription.setText(getResources().getString(R.string.make_payment_desc_2_attempts_daily, attempts));
            btnTempUnlock.setBackgroundResource(R.xml.rounded_list_transparent_orange_light_stroke);

        } else if (maxAttempts == 3 && attempts == 1) {
            int tintColor = ContextCompat.getColor(parentActivity, R.color.theme_red);
            ivIcon.setColorFilter(tintColor);

            ftvAttempts.setTextColor(getResources().getColor(R.color.theme_red));
            ftvMaxAttempts.setTextColor(getResources().getColor(R.color.theme_red));
            payPrompt.setText(getResources().getString(R.string.make_a_payment_to_unlock_and_keep_using_your_device));
            ftvDescription.setText(getResources().getString(R.string.make_payment_desc_1_attempts_daily));
            ftvDescription.setTextColor(getResources().getColor(R.color.theme_red));
            ftvMaxAttempts.setText(getResources().getString(R.string.max_unlocks_left_1_3, maxAttempts));
            btnTempUnlock.setBackgroundResource(R.xml.rounded_list_transparent_orange_light_stroke);
        } else if (maxAttempts == 2 && attempts == 2) {
            payPrompt.setText(getResources().getString(R.string.payment_prompt_2_2_attempts));
            ftvDescription.setText(getResources().getString(R.string.make_payment_desc_2_attempts, global));
//            btnTempUnlock.setBackgroundResource(R.xml.rounded_list_transparent_orange_light_stroke);
            btnTempUnlock.setBackgroundResource(R.xml.rounded_list_button_orange_light);
        } else if (maxAttempts == 2 && attempts == 1) {
            int tintColor = ContextCompat.getColor(parentActivity, R.color.theme_orange_light);
            ivIcon.setColorFilter(tintColor);
            ftvAttempts.setTextColor(getResources().getColor(R.color.theme_orange_light));
            ftvMaxAttempts.setTextColor(getResources().getColor(R.color.theme_orange_light));
            payPrompt.setText(getResources().getString(R.string.payment_prompt_2_2_attempts));
            ftvDescription.setText(getResources().getString(R.string.payment_desc_2_1_attempts));
            btnTempUnlock.setBackgroundResource(R.xml.rounded_list_transparent_orange_light_stroke);

        } else if (maxAttempts == 2 && attempts == 0) {
            int tintColor = ContextCompat.getColor(parentActivity, R.color.theme_red);
            ivIcon.setColorFilter(tintColor);

            ftvAttempts.setTextColor(getResources().getColor(R.color.theme_red));
            ftvMaxAttempts.setTextColor(getResources().getColor(R.color.theme_red));
            payPrompt.setText(getResources().getString(R.string.payment_prompt_2_0_attempts));
            ftvDescription.setText(getResources().getString(R.string.make_payment_desc_0_attempts, global));
            tempUnlockRips.setVisibility(View.GONE);
            learnToPayRips.setVisibility(View.VISIBLE);

        } else if (maxAttempts == 1 && attempts == 1) {
            int tintColor = ContextCompat.getColor(parentActivity, R.color.theme_orange_light);
            ivIcon.setColorFilter(tintColor);
            ftvAttempts.setTextColor(getResources().getColor(R.color.theme_orange_light));
            ftvMaxAttempts.setTextColor(getResources().getColor(R.color.theme_orange_light));
            payPrompt.setText(getResources().getString(R.string.payment_prompt_1_1_attempts));
            ftvMaxAttempts.setText(getResources().getString(R.string.max_unlocks_left_1_3, maxAttempts));
            ftvDescription.setText(getResources().getString(R.string.make_payment_desc_min_1_1_attempts, global));
            btnTempUnlock.setBackgroundResource(R.xml.rounded_list_transparent_orange_light_stroke);

        } else if (maxAttempts == 1 && attempts == 0) {
            int tintColor = ContextCompat.getColor(parentActivity, R.color.theme_red);
            ivIcon.setColorFilter(tintColor);

            ftvAttempts.setTextColor(getResources().getColor(R.color.theme_red));
            ftvMaxAttempts.setTextColor(getResources().getColor(R.color.theme_red));
            String amount = GeneralCommands.fixCommas(currentAsset.getDailyPayment());
            payPrompt.setText(getResources().getString(R.string.payment_prompt_1_0_attempts));
            ftvDescription.setText(Html.fromHtml(mContext.getResources().getString(R.string.temp_unlock_exhausted, amount, global)));
            tempUnlockRips.setVisibility(View.GONE);
            learnToPayRips.setVisibility(View.VISIBLE);

        }

        ivClose.setOnClickListener(v -> dialog.dismiss());

        btnPayNow.setOnClickListener(v -> {
            dialog.dismiss();
            makeDailyPayment();
        });

        btnTempUnlock.setOnClickListener(v -> {
            dialog.dismiss();
            initTempUnlockFlow();
        });
    }

    //    Show Maxed Out Temp Unlocks Subsequent Days without Payment-Technically Day =>3
    private void showZeroTempUnlocksDialog(long globalMax) {
        int global = (int) globalMax;
        Dialog dialog = new Dialog(parentActivity);
        dialog.setContentView(R.layout.dialog_zero_temp_unlocks_available_max);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        ImageView ivClose = dialog.findViewById(R.id.iv_close);
        RelativeLayout btnPayNow = dialog.findViewById(R.id.btnPayNow);
        RelativeLayout btnLearnToPay = dialog.findViewById(R.id.btnLearnToPay);
        FirmaTextView ftvDescription = dialog.findViewById(R.id.ftvDescription);
        String amount = GeneralCommands.fixCommas(currentAsset.getDailyPayment());
        ftvDescription.setText(Html.fromHtml(mContext.getResources().getString(R.string.temp_unlock_exhausted, amount, global)));


        ivClose.setOnClickListener(v -> dialog.dismiss());

        btnPayNow.setOnClickListener(v -> {
            dialog.dismiss();
            makeDailyPayment();
        });

        btnLearnToPay.setOnClickListener(v -> {
            dialog.dismiss();
            openDialogPayCarousel();
        });
    }


    //    Show Maxed Out Temp Unlocks Same Day without Payment-Technically Day 1
    private void showDailyZeroTempUnlocksDialog() {
        Dialog dialog = new Dialog(parentActivity);
        dialog.setContentView(R.layout.dialog_daily_zero_unlocks_available_max);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        ImageView ivClose = dialog.findViewById(R.id.iv_close);
        RelativeLayout btnPayNow = dialog.findViewById(R.id.btnPayNow);
        RelativeLayout btnLearnToPay = dialog.findViewById(R.id.btnLearnToPay);
        ivClose.setOnClickListener(v -> dialog.dismiss());

        btnPayNow.setOnClickListener(v -> {
            dialog.dismiss();
            makeDailyPayment();
        });

        btnLearnToPay.setOnClickListener(v -> {
            dialog.dismiss();
            openDialogPayCarousel();
        });
    }

    private void initTempUnlockCheck() {
        JSONObject obj = new JSONObject();

        try {
            obj.put("loanNumber", currentAsset.getLoanNumber());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        GeneralCommands.logEventObject(parentActivity, "click_temporary_unlock", "click_temporary_unlock", obj);


        GetData ob1 = new GetData();
        ob1.initialize(parentActivity, loadingView);

        ob1.userTemporaryUnlockCheck(currentAsset.getLoanNumber(), false, new GetData.UnlockInfoCallback() {
            @Override
            public void onSuccess(UnlockInfoResponse data) {

                int dailyLimit = data.getDailyLimit();
                int dailyRemaining = data.getDailyRemaining();
                long dailyGlobalMax = (data.getDailyGlobalMax() != null) ? data.getDailyGlobalMax() : 0;

//                int dailyLimit = 1;
//                int dailyRemaining = 0;
//                long dailyGlobalMax = 3L;

                if (dailyLimit == 0) {
                    showZeroTempUnlocksDialog(dailyGlobalMax);

                } else if (dailyLimit == 3 && dailyRemaining == 0) {
                    showDailyZeroTempUnlocksDialog();

                } else {
                    showTemporaryUnlockMaxAttemptsDialog(dailyLimit, dailyRemaining, dailyGlobalMax);
                }

            }

            @Override
            public void onError(String error) {
                // handle error
            }
        });


    }

    private void initTempUnlockFlow() {

        JSONObject obj = new JSONObject();

        try {
            obj.put("loanNumber", currentAsset.getLoanNumber());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        GeneralCommands.logEventObject(parentActivity, "click_temporary_unlock", "click_temporary_unlock", obj);


        GetData ob1 = new GetData();
        ob1.initialize(parentActivity, loadingView);

        ob1.userTemporaryUnlock(currentAsset.getLoanNumber(), false);

        if (storageObject.retrieveTemporaryUnlockCountFromPrefs() > storageObject.retrieveTempUnlockConfigFromPrefs()) {
            //Exceeded the unlock attempts so reste the attempts count back to 0
            storageObject.saveTemporaryUnlockCountToPrefs(0);

        }


        ob1.setTemporaryUnlockListener(new GetData.TemporaryUnlockListener() {
            @Override
            public void onObjectReady(String title) {

            }

            @Override
            public void onDataLoaded(long waitingTime) {

//						       Toast.makeText(mContext,"Time"+waitingTime,Toast.LENGTH_SHORT).show();
                setWaitingTime(timerUnlock, unlockLimitView, unlockText, unlockIcon, btnUnlock, waitingTime);

            }
        });


    }

    private void toggleDefaultAsset() {


        if (listDataBoda != null && listDataBoda.size() > 0) {
            setAssets("boda", listDataBoda);

        }


        if (listDataTv != null && listDataTv.size() > 0) {
            setAssets("tv", listDataTv);

        }

        if (listDataTukTuk != null && listDataTukTuk.size() > 0) {
            setAssets("tuktuk", listDataTukTuk);


        }

        if (listDataGari != null && listDataGari.size() > 0) {
            setAssets("gari", listDataGari);

        }


        //Set Simu as Last so It Defaults as the active item if there.

        if (listDataSimu != null && listDataSimu.size() > 0) {
            setAssets("simu", listDataSimu);

        }


    }

    private void clearListDataAssets() {

        assetsAvailable = 0;
        listDataSimu.clear();
        listDataBoda.clear();
        listDataTv.clear();
        listDataTukTuk.clear();
        listDataGari.clear();
    }

    public ArrayList<AssetObject> getListData() {
        ArrayList<LoanListItemObject> accounts = storageObject.retrieveLoanListItemObjectObjectsFromPrefs();
        ArrayList<AssetObject> assets = new ArrayList<>();
        clearListDataAssets();

//        Toast.makeText(mContext, mContext.getResources().getString(R.string.loadingyourassets), Toast.LENGTH_SHORT).show();


        for (int i = 0; i < accounts.size(); i++) {
            LoanListItemObject item = accounts.get(i);

//            Toast.makeText(parentActivity, "Ze1TV Asset Found"+item.getLoanNumber(), Toast.LENGTH_SHORT).show();

            GetData ob1 = new GetData();
            ob1.initialize(parentActivity, loadingView);
            ob1.userGetLoanDetails(item.getLoanNumber(), false);
            int finalI = i;
            ob1.setLoanDetailsListener(new GetData.LoanDetailsListener() {
                @Override
                public void onObjectReady(String title) {

                }

                @Override
                public void onDataLoaded(AssetObject data) {

                    if (data != null) {
                        data.setLoanNumber(item.getLoanNumber());
                        assets.add(data);
                        storageObject.addAssetObjectObjectToPrefs(data);


                        String type = "" + data.getAssetTypeId();

                        if (type.equals("4"))//Smartphone User
                        {
                            storageObject.setIsSimuUser(true);
                        } else {
                            storageObject.setIsBodaUser(true);
                        }


//                        Toast.makeText(mContext, "ZeCount"+assets.size()+":"+accounts.size(), Toast.LENGTH_SHORT).show();

                        addLocalAssetsData(data);
                        if (assets.size() == (accounts.size())) {
                            //Finished Adding so replace now
                            if (data != null && storageObject.retrieveCurrentAssetObjectFromPrefs() == null)
                                storageObject.saveCurrentAssetObjectToPrefs(data);

                        }

                        //Set Assets
//                        setAssets("simu",listDataSimu);
                        toggleDefaultAsset();
                        toggleAssetsVisibility();


                    }

                }
            });


        }

        return listData;
    }

    public ArrayList<AssetObject> getListDataSilent() {
        ArrayList<LoanListItemObject> accounts = storageObject.retrieveLoanListItemObjectObjectsFromPrefs();
        ArrayList<AssetObject> assets = new ArrayList<>();
        clearListDataAssets();

//        Toast.makeText(mContext, mContext.getResources().getString(R.string.loadingyourassets), Toast.LENGTH_SHORT).show();


        for (int i = 0; i < accounts.size(); i++) {
            LoanListItemObject item = accounts.get(i);

            GetData ob1 = new GetData();
            ob1.initialize(parentActivity, loadingView);
            ob1.userGetLoanDetails(item.getLoanNumber(), true);
            int finalI = i;
            ob1.setLoanDetailsListener(new GetData.LoanDetailsListener() {
                @Override
                public void onObjectReady(String title) {

                }

                @Override
                public void onDataLoaded(AssetObject data) {

                    if (data != null) {
                        data.setLoanNumber(item.getLoanNumber());
                        assets.add(data);


                        String type = "" + data.getAssetTypeId();

                        if (type.equals("4"))//Smartphone User
                        {
                            storageObject.setIsSimuUser(true);
                        } else {
                            storageObject.setIsBodaUser(true);
                        }

//                        Toast.makeText(mContext, "Ze2Count"+assets.size()+":"+accounts.size(), Toast.LENGTH_SHORT).show();

                        addLocalAssetsData(data);
                        if (assets.size() == (accounts.size())) {
                            //Finished Adding so replace now
                            if (data != null && storageObject.retrieveCurrentAssetObjectFromPrefs() == null)
                                storageObject.saveCurrentAssetObjectToPrefs(data);

                        }

                        //Set Assets
//                        setAssets("simu",listDataSimu);
                        toggleDefaultAsset();
                        toggleAssetsVisibility();


                    }

                }
            });


            //Loan Agreement
            ob1.userDownloadLoanAgreement(item.getLoanNumber(),false, true);


        }

        //Update Local Asset Data
        if(assets!=null && assets.size()>0) {
            //Update Local Asset Data
            listData = assets;
            storageObject.saveAssetObjectObjectsToPrefs(listData);
        }
        return listData;
    }

    private void addLocalAssetsData(AssetObject data) {

        if (("" + data.getAssetTypeId()).equals("4"))//SmartPhone
        {
            //Ia s a phone so put it as the default asset
            storageObject.saveCurrentAssetObjectToPrefs(data);
            listDataSimu.add(data);
            assetsAvailable++;
        } else if (("" + data.getAssetTypeId()).equals("2"))//TukTuk
        {
            listDataTukTuk.add(data);
            assetsAvailable++;

        } else if (("" + data.getAssetTypeId()).equals("1"))//Boda
        {
            listDataBoda.add(data);
            assetsAvailable++;

        } else if (("" + data.getAssetTypeId()).equals("3"))//Gari
        {
            listDataGari.add(data);
            assetsAvailable++;

        } else if (("" + data.getAssetTypeId()).equals("5"))//Tv
        {
            listDataTv.add(data);
            assetsAvailable++;

        }


    }

    public void setAssets(String assetType, List<AssetObject> assets) {
        secondaryDevicesContainer.removeAllViews(); // Clear old views
        selectedDeviceView = null; // Clear previous selection

        View firstView = null;

        for (int i = 0; i < assets.size(); i++) {
            AssetObject asset = assets.get(i);

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
            FirmaTextView deviceView = new FirmaTextView(parentActivity);
            deviceView.setText(name);

            // Layout
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    dpToPx(25)
            );
            if (i > 0) {
                params.setMargins(dpToPx(10), 0, 0, 0);
            }
            deviceView.setLayoutParams(params);

            // Style
            deviceView.setGravity(Gravity.CENTER);
            deviceView.setPadding(dpToPx(15), 0, dpToPx(15), 0);
            deviceView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.textsize_xsmall));
            deviceView.setTextColor(ContextCompat.getColor(parentActivity, R.color.white));
            deviceView.setBackgroundResource(R.drawable.rounded_pill_dark);
            deviceView.setTag(asset);

            // Click listener
            deviceView.setOnClickListener(view -> {
                if (selectedDeviceView != null) {
                    selectedDeviceView.setBackgroundResource(R.drawable.rounded_pill_dark);
                    ((FirmaTextView) selectedDeviceView).setTextColor(ContextCompat.getColor(parentActivity, R.color.white));
                }

                view.setBackgroundResource(R.drawable.rounded_pill_green);
                ((FirmaTextView) view).setTextColor(ContextCompat.getColor(parentActivity, R.color.black));
                selectedDeviceView = view;

                AssetObject selectedAsset = (AssetObject) view.getTag();
                // You can add logic for when an asset is selected
                setSelectedAssetData(selectedAsset);
            });

            secondaryDevicesContainer.addView(deviceView);

            if (i == 0) {
                firstView = deviceView;
            }
        }

        // Simulate click on first item to select it by default
        if (firstView != null) {
            firstView.performClick();
        }

        //Set Main Rips Icon Main View
        setAssetTypeMainIcon(assetType);
        toggleAssetsVisibility();
    }

    public void setAssetTypeMainIcon(String assetType) {
        if (assetType.equals("simu")) iconMain.setImageResource(R.drawable.ics_simu);
        if (assetType.equals("boda")) iconMain.setImageResource(R.drawable.ics_boda);
        if (assetType.equals("tv")) iconMain.setImageResource(R.drawable.ics_icon_tv);
        if (assetType.equals("gari")) iconMain.setImageResource(R.drawable.ics_gari);
        if (assetType.equals("tuktuk")) iconMain.setImageResource(R.drawable.ics_tuk_tuk);

    }

    private void setSelectedAssetData(AssetObject assetObject) {
        //Toast.makeText(mContext, "Selected Asset: " + assetObject.getVehicleMakeName() + " " + assetObject.getVehicleModelName(), Toast.LENGTH_SHORT).show();

        currentAsset = assetObject;
        storageObject.saveCurrentAssetObjectToPrefs(currentAsset);
        toggleAssetsVisibility();

        String category = currentAsset.getLoanProductName();
        String type = "" + currentAsset.getAssetTypeId();
        String myCurrency = currentAsset.getCurrencyCode().equals("USD") ? "Usd. " : currentAsset.getCurrencyCode().toUpperCase();
        String currency = myCurrency.toUpperCase();
        Double num1 = 0.0;
        Double num2 = 0.0;
        try {
            num1 = Double.parseDouble(currentAsset.getRemainingBalance());
            num2 = Double.parseDouble(currentAsset.getTotalRepayment());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        String activeBranch = currentAsset.getBranchName();

//				int amountToUnlock = Integer.parseInt(""+amountToUnlock);

        String amount = GeneralCommands.fixCommas("" + (num2 - num1));
        Double totalPaid = num2 - num1;
        totalPaid = Math.round(totalPaid * 100.0) / 100.0;
        String remainingBalance = GeneralCommands.fixCommas("" + (num2 - totalPaid));


        Double progress = num2 > 0 ? ((float) (num2 - num1) / num2) * 100 : 0;
//        String dateAsOf=mContext.getResources().getString(R.string.asof)+" "+ GeneralCommands.getCurrentDateTime();
        String dateAsOf = storageObject.retrieveLastPaymentDate();
        String lType = currentAsset.getAssetTypeName();
//        String activeUntilDate= ("23-02-2024");
        String activeUntilDate = sdf.format(currentAsset.getDeviceActiveTill());
        String activeUntilTime = sdfTime.format(currentAsset.getDeviceActiveTill());

        String nextPaymentDate = sdf.format(currentAsset.getNextPaymentDueDate());


        String issueDate = sdf.format(currentAsset.getDisbursementDate());
        String maturityDate = sdf.format(currentAsset.getMaturityDate());
        String status = currentAsset.getLoanStatus();
        String balance = GeneralCommands.fixCommas(currentAsset.getRemainingBalance());

        String overdueAmount = GeneralCommands.fixCommas(currentAsset.getOverdueAmount());
        String dailyAmount = GeneralCommands.fixCommas(currentAsset.getDailyPayment());
        String weeklyAmount = GeneralCommands.fixCommas(currentAsset.getWeeklyPayment());
        String monthlyAmount = GeneralCommands.fixCommas(currentAsset.getMonthlyPayment());

        long targetTimestamp = currentAsset.getDeviceActiveTill(); // The timestamp you want to count down to
        long currentTime = System.currentTimeMillis();
        long timeRemaining = targetTimestamp - currentTime;


        long targetTimestampPayment = currentAsset.getNextPaymentDueDate(); // The timestamp you want to count down to
        long currentTimePayment = System.currentTimeMillis();
        long timeRemainingPayment = targetTimestampPayment - currentTimePayment;


        String model = currentAsset.getVehicleMakeName() + " | " + currentAsset.getVehicleModelName();
        model = model.toLowerCase().contains("null") ? "" : model;


        seekBar.setProgress((int) (double) progress);
        seekBar.setEnabled(false);
        seekBar.post(() -> updateProgressText(seekBar, progressText, seekBar.getProgress()));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar sb, int progress, boolean fromUser) {
                updateProgressText(sb, progressText, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar sb) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar sb) {
            }
        });


        loanBalance.setText(currency + " " + balance);
        loanTotalPayment.setText(currency + " " + GeneralCommands.fixCommas("" + totalPaid));


        asOf.setText(dateAsOf);
        issueDateH.setText(issueDate);


        loanDailyAmount.setText(currency + " " + dailyAmount);
        loanWeeklyAmount.setText(currency + " " + weeklyAmount);
        loanMonthlyAmount.setText(currency + " " + monthlyAmount);

        //Loan Complete Data
        totalPaymentText.setText(currency + " " + totalPaid);
        outstandingBalanceText.setText(currency + " " + remainingBalance);
        installmentAmountText.setText(currency + " " + overdueAmount);
        dailyPaymentText.setText(currency + " " + dailyAmount);


        deviceActiveDate.setText(nextPaymentDate);

        if (type.equals("4"))  //SmartPhone
        {
            activeUntilView.setVisibility(View.VISIBLE);

            //Show Temporary Unlock View
            //Hide Temp Unlock if Config is Zero
            if (storageObject.retrieveTempUnlockConfigFromPrefs() == 0) {
                unlockRips.setVisibility(View.GONE);
            } else {
                unlockRips.setVisibility(View.VISIBLE);
            }


        } else {

            //Not a Smart Phone
            activeUntilView.setVisibility(View.GONE);


        }

        //Time Reamining - Set Ad Card

        if (type.equals("4"))  //SmartPhone
        {

            if (timeRemaining > 0) {
                tempUnlockView.setVisibility(View.GONE);
                countView.setVisibility(View.VISIBLE);

                //Not Yet Overdue/Is NOT Locked
                storageObject.setIsPhoneLocked(false);
                unlockRips.setVisibility(View.GONE);
                amountValue.setText(currency + " " + dailyAmount);
                amountToUnlockLabel.setText(mContext.getString(R.string.dailyamount));

                iconLock.setColorFilter(ContextCompat.getColor(parentActivity, R.color.theme_green), PorterDuff.Mode.SRC_IN);
                deviceActiveH.setText(mContext.getResources().getString(R.string.yourdevicewillbeactivefor));


                if (storageObject.showPayTooltip()) {
                    //Not Locked
                    payToolTipRips.setVisibility(View.VISIBLE);
//                    unlockToolTipRips.setVisibility(View.GONE);
                }

                btnPay.setBackgroundResource(R.xml.rounded_list_button);


                if (timerPayment != null) timerPayment.cancel();
                timerPayment = new CountDownTimer(timeRemaining, 1000) {
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

//								deviceActiveCount.setText(days+ "d "+ hrs+"h "+mns+"m "+scs+"s ");
                        tvDaysValue.setText("" + days);
                        tvHoursValue.setText(hrs);
                        tvMinutesValue.setText(mns);
                        tvSecondsValue.setText(scs);

                        deviceActiveDate.setText(activeUntilDate);


                        if (days < 0) {
//						Toast.makeText(mContext, "Less"+days, Toast.LENGTH_SHORT).show();

                            //Smart Phone
//                            activeUntilView.setBackgroundResource(R.xml.rounded_list_button_orange_stroke);
                            iconLock.setColorFilter(mContext.getResources().getColor(R.color.theme_orange));
                            deviceActiveDate.setTextColor(mContext.getResources().getColor(R.color.theme_orange));


                        } else {

//						Toast.makeText(mContext, "More"+days, Toast.LENGTH_SHORT).show();

//                            activeUntilView.setBackgroundResource(R.xml.rounded_list_button_green_stroke);
                            iconLock.setColorFilter(mContext.getResources().getColor(R.color.theme_green));
                            deviceActiveDate.setTextColor(mContext.getResources().getColor(R.color.theme_green));

                        }

                    }

                    public void onFinish() {
                        // Handle countdown completion, if needed
                    }
                }.start();


            } else {
                //Is Overdue/Locked
                countView.setVisibility(View.GONE);
                tempUnlockView.setVisibility(View.VISIBLE);

                storageObject.setIsPhoneLocked(true);
                deviceActiveDate.setText(activeUntilDate);
//                activeUntilView.setBackgroundResource(R.xml.rounded_list_button_orange_stroke);
                iconLock.setColorFilter(mContext.getResources().getColor(R.color.theme_orange));
                deviceActiveDate.setTextColor(mContext.getResources().getColor(R.color.theme_orange));

                iconLock.setColorFilter(ContextCompat.getColor(parentActivity, R.color.theme_orange), PorterDuff.Mode.SRC_IN);
                deviceActiveH.setText(mContext.getResources().getString(R.string.yourdevicewaslockedon));


                if (storageObject.showTemporaryUnlockTooltip()) {
                    //Locked
//                    payToolTipRips.setVisibility(View.GONE);
                    unlockToolTipRips.setVisibility(View.VISIBLE);
                }

                btnPay.setBackgroundResource(R.xml.rounded_list_button_orange);

                if (storageObject.retrieveTempUnlockConfigFromPrefs() == 0)//Smart Phone
                {
                    unlockRips.setVisibility(View.GONE);
                } else {
                    unlockRips.setVisibility(View.VISIBLE);
                }

                amountToUnlockLabel.setText(mContext.getString(R.string.amountduetounlock));
                amountValue.setText(currency + " " + dailyAmount);

            }


        } else {
            //Not a Smart Phone could be Boda, TukTuk, Gari or Tv

            if (timeRemaining < 0) {
                //Not a Phone and Overdue
//                activeUntilView.setBackgroundResource(R.xml.rounded_list_button_orange_stroke);
                deviceActiveDate.setTextColor(mContext.getResources().getColor(R.color.theme_orange));
                amountValue.setText(currency + " " + overdueAmount);
                amountToUnlockLabel.setText(mContext.getString(R.string.amountoverdue));

            } else {

                //Not a phone and not  Overdue
//                activeUntilView.setBackgroundResource(R.xml.rounded_list_button_green_stroke);
                deviceActiveDate.setTextColor(mContext.getResources().getColor(R.color.theme_green));


            }
            deviceActiveDate.setVisibility(View.VISIBLE);

        }


        btnPay.setOnClickListener(v -> {
            storageObject.saveCurrentAssetObjectToPrefs(currentAsset);

            JSONObject obj = new JSONObject();
            try {

                obj.put("assetLoanNumber", currentAsset.getLoanNumber());
                obj.put("assetName", currentAsset.getAssetTypeName());
                obj.put("assetTypeId", currentAsset.getAssetTypeId());


            } catch (JSONException e) {
                e.printStackTrace();
            }

            GeneralCommands.logEventObject(parentActivity, "click_pay_loan_asset", "click_pay_loan_asset", obj);
            MainActivity.is_from_home_page = true;
            MainActivity.is_source = "daily";
            MainActivity.viewPager.setCurrentItem(2);

            // goToMakePaymentPage();
        });

        btnDaily.setOnClickListener(v -> {
            storageObject.saveCurrentAssetObjectToPrefs(currentAsset);

            JSONObject obj = new JSONObject();
            try {

                obj.put("assetLoanNumber", currentAsset.getLoanNumber());
                obj.put("assetName", currentAsset.getAssetTypeName());
                obj.put("assetTypeId", currentAsset.getAssetTypeId());


            } catch (JSONException e) {
                e.printStackTrace();
            }

            GeneralCommands.logEventObject(parentActivity, "click_home_page_daily", "click_home_page_daily", obj);
            MainActivity.is_from_home_page = true;
            MainActivity.is_source = "daily";
            MainActivity.viewPager.setCurrentItem(2);

            // goToMakePaymentPage();
        });

        btnWeekly.setOnClickListener(v -> {
            storageObject.saveCurrentAssetObjectToPrefs(currentAsset);

            JSONObject obj = new JSONObject();
            try {

                obj.put("assetLoanNumber", currentAsset.getLoanNumber());
                obj.put("assetName", currentAsset.getAssetTypeName());
                obj.put("assetTypeId", currentAsset.getAssetTypeId());


            } catch (JSONException e) {
                e.printStackTrace();
            }

            GeneralCommands.logEventObject(parentActivity, "click_home_page_weekly", "click_home_page_weekly", obj);
            MainActivity.is_from_home_page = true;
            MainActivity.is_source = "weekly";
            MainActivity.viewPager.setCurrentItem(2);

            // goToMakePaymentPage();
        });

        btnMonthly.setOnClickListener(v -> {
            storageObject.saveCurrentAssetObjectToPrefs(currentAsset);

            JSONObject obj = new JSONObject();
            try {

                obj.put("assetLoanNumber", currentAsset.getLoanNumber());
                obj.put("assetName", currentAsset.getAssetTypeName());
                obj.put("assetTypeId", currentAsset.getAssetTypeId());


            } catch (JSONException e) {
                e.printStackTrace();
            }

            GeneralCommands.logEventObject(parentActivity, "click_home_page_monthly", "click_pay_monthly", obj);
            MainActivity.is_from_home_page = true;
            MainActivity.is_source = "monthly";
            MainActivity.viewPager.setCurrentItem(2);

            // goToMakePaymentPage();
        });

//        initTempUnlockCheck();
        btnUnlock.setOnClickListener(v -> {initTempUnlockCheck();});

        // Create and start the countdown timer
        setWaitingTime(timerUnlock, unlockLimitView, unlockText, unlockIcon, btnUnlock, storageObject.retrieveUserTempActiveTillFromPrefs());

        //How to Pay Tooltip

        btnPayTip.setOnClickListener(v -> openDialogPayCarousel());

        btnUnlockTip.setOnClickListener(v -> openDialogUnlockCarousel());


        try {
            setConfigsPay();
            setCarouselDetailsPay();
            setConfigsUnlock();
            setCarouselDetailsUnlock();

        } catch (Exception e) {
        }

        //EndView

        //pay_icon


        if (!storageObject.showPayIcon()) {
            payIcon.setVisibility(View.GONE);

        } else {
            payIcon.setVisibility(View.VISIBLE);

        }

        //Show Loan Complete Views
        if (currentAsset.getLoanStatus().toLowerCase().contains("closed")) {
            loanCompleteView.setVisibility(View.VISIBLE);
            activeLoanView.setVisibility(View.GONE);

        } else {
            loanCompleteView.setVisibility(View.GONE);
            activeLoanView.setVisibility(View.VISIBLE);
        }


        //Jijenge
        if (storageObject.isUpgradeFeatureEnabled()) {
            jijengeContainer.setVisibility(View.VISIBLE);
        } else {
            jijengeContainer.setVisibility(View.GONE);

        }
        //Jijenge
        btnUpgradeNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject obj = new JSONObject();
                try {

                    Gson gson = new Gson();

                    obj.put("assetLoanNumber", currentAsset.getLoanNumber());
                    obj.put("assetName", currentAsset.getAssetTypeName());
                    obj.put("assetTypeId", currentAsset.getAssetTypeId());
                    if (!noUpgradeOptions()) {
                        obj.put("currentDevice", gson.toJson(currentAsset.getDeviceUpgradeObject().getCurrentDevice()));
                        obj.put("upgradeOptions", gson.toJson(currentAsset.getDeviceUpgradeObject().getUpgradeDevices()));

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                GeneralCommands.logEventObject(parentActivity, "click_button_upgrade_now", "click_button_upgrade_now", obj);
                goToUpgradePageDirect();
            }
        });
        if (storageObject.isUpgradeFeatureEnabled()) {
            jijengeContainer.setVisibility(View.VISIBLE);
            //Check Options are Avilable
            if (!noUpgradeOptions()) {
                //Has Upgrdae Options
                upgradeUnlockedView.setVisibility(View.VISIBLE);
                upgradeLockedView.setVisibility(View.GONE);

            } else {

                upgradeUnlockedView.setVisibility(View.GONE);
                upgradeLockedView.setVisibility(View.VISIBLE);
            }
        } else {
            jijengeContainer.setVisibility(View.GONE);
        }


        //  return v


    }

    private void toggleAssetsVisibility() {

        if (assetsAvailable >= 2) {
            assetsView.setVisibility(View.VISIBLE);
            mainRips.setVisibility(View.GONE);

        } else {
            assetsView.setVisibility(View.GONE);
            mainRips.setVisibility(View.VISIBLE);
        }

        if (listDataSimu.size() > 0) {
            simuRips.setVisibility(View.VISIBLE);
        } else {
            simuRips.setVisibility(View.GONE);
        }

        if (listDataBoda.size() > 0) {
            bodaRips.setVisibility(View.VISIBLE);
        } else {
            bodaRips.setVisibility(View.GONE);
        }


        if (listDataTv.size() > 0) {
            tvRips.setVisibility(View.VISIBLE);
        } else {
            tvRips.setVisibility(View.GONE);

        }

        if (listDataTukTuk.size() > 0) {
            tukTukRips.setVisibility(View.VISIBLE);

        } else {
            tukTukRips.setVisibility(View.GONE);

        }

        if (listDataGari.size() > 0) {
            gariRips.setVisibility(View.VISIBLE);
        } else {
            gariRips.setVisibility(View.GONE);

        }

    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    //Pay FLows

    public void goToUpgradePage() {

        Intent mIntent = new Intent(parentActivity, LevelUpActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
        GeneralCommands.goToPage(parentActivity, mIntent, false);

    }


    //HOw To Pay

    public void openDialogUnlockCarousel() {
        View contentView = parentActivity.getLayoutInflater().inflate(R.layout.dialog_content_pay_carousel, null);


        final DialogPlus dialogPlus = new DialogPlus.Builder(parentActivity)
                .setContentHolder(new ViewHolder(contentView))
                .setGravity(DialogPlus.Gravity.CENTER)
                .setBackgroundColorResourceId(R.color.black)
                .setCancelable(true)
                .create();


        //Content View
        View content = dialogPlus.getHolderView();

        RelativeLayout loadingView = content.findViewById(R.id.loadingItems);

        RelativeLayout btnBack = content.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(view -> dialogPlus.dismiss());


        previewImagesUnlockView = contentView.findViewById(R.id.previewImagesView);


        try {
            setConfigsUnlock();
            setCarouselDetailsUnlock();

        } catch (Exception e) {
        }


        dialogPlus.show();
    }

    public void openDialogPayCarousel() {
        View contentView = parentActivity.getLayoutInflater().inflate(R.layout.dialog_content_pay_carousel, null);


        final DialogPlus dialogPlus = new DialogPlus.Builder(parentActivity)
                .setContentHolder(new ViewHolder(contentView))
                .setGravity(DialogPlus.Gravity.CENTER)
                .setBackgroundColorResourceId(R.color.black)
                .setCancelable(true)
                .create();


        //Content View
        View content = dialogPlus.getHolderView();

        RelativeLayout loadingView = content.findViewById(R.id.loadingItems);

        RelativeLayout btnBack = content.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(view -> dialogPlus.dismiss());


        previewImagesPayView = null;
        previewImagesPayView = contentView.findViewById(R.id.previewImagesView);


        try {
            setConfigsPay();
            setCarouselDetailsPay();

        } catch (Exception e) {
        }


        dialogPlus.show();
    }

    public void refreshAdCard() {
        remoteConfig = new FirebaseConfig();
        mFirebaseConfig = remoteConfig.getConfigs();
        mFirebaseConfig.fetchAndActivate().addOnCompleteListener(parentActivity, task -> {
            if (task.isSuccessful()) {
                setupAdVisibility();
            }
        });
        mFirebaseConfig.addOnConfigUpdateListener(new ConfigUpdateListener() {
            @Override
            public void onUpdate(@NonNull ConfigUpdate configUpdate) {
                mFirebaseConfig.fetchAndActivate().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        setupAdVisibility();
                    }
                });
            }

            @Override
            public void onError(@NonNull FirebaseRemoteConfigException error) {

            }
        });
    }

    private void setupAdVisibility() {
        // 1. Check the boolean flag
        boolean shouldShowAd = mFirebaseConfig.getBoolean("should_show_ad");

        if (shouldShowAd) {
            String adCardJson = mFirebaseConfig.getString("ad_card");

            if (!adCardJson.isEmpty()) {
                try {

                    Gson gson = new Gson();
                    AdCard card = gson.fromJson(adCardJson, AdCard.class);

                    String currentLang = Locale.getDefault().getLanguage();
                    String imageUrl = currentLang.equals("sw") ? card.getLinkSw() : card.getLinkEn();

                    adCard.setVisibility(View.VISIBLE);

                    Glide.with(this)
                            .load(imageUrl)
                            .into(adCard);

                } catch (Exception e) {
                    e.printStackTrace();
                    adCard.setVisibility(View.GONE);
                }
            } else {
                adCard.setVisibility(View.GONE);
            }
        } else {
            adCard.setVisibility(View.GONE);
        }

    }
    public void setConfigsUnlock() {
        //Remote Configs
        remoteConfig = new FirebaseConfig();
        mFirebaseConfig = remoteConfig.getConfigs();
        mFirebaseConfig.fetchAndActivate().addOnCompleteListener(parentActivity, task -> {
            if (task.isSuccessful()) {
                boolean updated = task.getResult();

//					Toast.makeText(mContext, "Fetch and activate succeeded"+ has_how_to_temporary_unlock_tooltip,Toast.LENGTH_SHORT).show();

                carousel_view_unlock = mFirebaseConfig.getString("how_to_temporary_unlock_tooltip_carousel");
                has_how_to_temporary_unlock_tooltip = mFirebaseConfig.getBoolean("has_how_to_temporary_unlock_tooltip");
                accessibility_has_pay_icon = mFirebaseConfig.getBoolean("accessibility_has_pay_icon");


                storageObject.setTemporaryUnlockTooltip(has_how_to_temporary_unlock_tooltip);
                storageObject.setPayIcon(accessibility_has_pay_icon);

                if (carousel_view_unlock != null && !carousel_view_unlock.equals("")) {
                    try {
                        JSONObject obj = new JSONObject(carousel_view_unlock);

                        Log.d("ZeCarousel", "ZeCarousel" + obj);
                        refreshCarouselDetailsUnlock(obj);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


            } else {
                Log.d("ZeFetchFailed", "ZeFetchFailed");
            }

        });

        mFirebaseConfig.addOnConfigUpdateListener(new ConfigUpdateListener() {
            @Override
            public void onUpdate(ConfigUpdate configUpdate) {
                //Log.d("ZeConfigs", "Updated keys: " + configUpdate.getUpdatedKeys());

                mFirebaseConfig.activate().addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        carousel_view_unlock = mFirebaseConfig.getString("how_to_temporary_unlock_tooltip_carousel");
                        has_how_to_temporary_unlock_tooltip = mFirebaseConfig.getBoolean("has_how_to_temporary_unlock_tooltip");
                        accessibility_has_pay_icon = mFirebaseConfig.getBoolean("accessibility_has_pay_icon");


                        storageObject.setTemporaryUnlockTooltip(has_how_to_temporary_unlock_tooltip);
                        storageObject.setPayIcon(accessibility_has_pay_icon);

                        if (carousel_view_unlock != null && !carousel_view_unlock.equals("")) {
                            try {
                                JSONObject obj = new JSONObject(carousel_view_unlock);
                                Log.d("ZeCarousel", "ZeCarousel" + obj);
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

    public void setCarouselDetailsUnlock() {

        viewListenerUnlock = position -> {
            View customView = vi.inflate(R.layout.carousel_view_custom, null);
            //set view attributes here

            ImageView imageView = customView.findViewById(R.id.imageView);
            if (previewImagesUnlock != null && previewImagesUnlock.length > position)
                imageView.setImageResource(previewImagesUnlock[position]);

            FirmaTextView mainMsg = customView.findViewById(R.id.mainMsg);
            FirmaTextView subMsg = customView.findViewById(R.id.subMsg);

            mainMsg.setVisibility(View.GONE);
            subMsg.setVisibility(View.GONE);


            return customView;
        };

        previewImagesUnlockView.setPageCount(previewImagesUnlock.length);
        previewImagesUnlockView.setViewListener(viewListenerUnlock);


    }

    public void refreshCarouselDetailsUnlock(JSONObject jsonObject) throws JSONException {
        JSONArray drcArray = new JSONArray();
        JSONArray dataArray = new JSONArray();

        // Check if the object contains "drc" data
        if (jsonObject.has("drc")) {
            // Get the "drc" array


            JSONObject newJsonObject = jsonObject.getJSONObject("drc");
            if (newJsonObject.has("fr") && storageObject.retrieveLanguageFromPrefs().equals("fr")) {
                drcArray = newJsonObject.getJSONArray("fr");


            } else {
                drcArray = newJsonObject.getJSONArray("en");
            }

        }


        dataArray = drcArray;

//		Log.d("ZeArrayzFin","ZeArrayzFIn"+storageObject.retrieveLanguageFromPrefs()+":"+dataArray.toString());

        if (dataArray.length() > 0 && previewImagesUnlockView != null) {
            previewImagesUnlockView.setSlideInterval(default_timeout * 1000); //Set Slide Interval
            previewImagesUnlockView.setPageCount(dataArray.length());

            JSONArray finalDataArray = dataArray;
            viewListenerUnlock = position -> {
                View customView = vi.inflate(R.layout.carousel_view_custom, null);
                //set view attributes here

                ImageView imageView = customView.findViewById(R.id.imageView);

                FirmaTextView mainMsg = customView.findViewById(R.id.mainMsg);
                FirmaTextView subMsg = customView.findViewById(R.id.subMsg);


                try {
                    JSONObject drcObject = finalDataArray.getJSONObject(position);

                    // Extract individual items from the "" object
                    String title = drcObject.getString("title");
                    String message = drcObject.getString("message");
                    String image = drcObject.getString("image");
                    String timeout_seconds = drcObject.getString("timeout_seconds");

                    mainMsg.setVisibility(View.GONE);
                    subMsg.setVisibility(View.GONE);

                    if (timeout_seconds != null && !timeout_seconds.equals("")) {

                        try {
                            int seconds = Integer.parseInt(timeout_seconds);
                            if (seconds > 0) {
                                default_timeout = seconds;
                                previewImagesUnlockView.setSlideInterval(default_timeout * 1000);
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
                            if (previewImagesUnlock != null && previewImagesUnlock.length >
                                    position && imageView != null)
                                imageView.setImageResource(previewImagesUnlock[position]);

                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {
                            if (previewImagesUnlock != null && previewImagesUnlock.length > position && imageView != null)
                                imageView.setImageResource(previewImagesUnlock[position]);
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return customView;
            };

            previewImagesUnlockView.setPageCount(dataArray.length());
            previewImagesUnlockView.setViewListener(viewListenerUnlock);

        }


    }


    //Pay
    public void setConfigsPay() {
        //Remote Configs
        remoteConfig = new FirebaseConfig();
        mFirebaseConfig = remoteConfig.getConfigs();
        mFirebaseConfig.fetchAndActivate().addOnCompleteListener(parentActivity, new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    boolean updated = task.getResult();

//					Toast.makeText(mContext, "Fetch and activate succeeded"+ has_how_to_temporary_unlock_tooltip,Toast.LENGTH_SHORT).show();

                    carousel_view_pay = mFirebaseConfig.getString("how_to_pay_tooltip_carousel");
                    has_how_to_pay_tooltip = mFirebaseConfig.getBoolean("has_how_to_pay_tooltip");
                    accessibility_has_pay_icon = mFirebaseConfig.getBoolean("accessibility_has_pay_icon");


                    storageObject.setPayTooltip(has_how_to_pay_tooltip);
                    storageObject.setPayIcon(accessibility_has_pay_icon);

                    if (carousel_view_pay != null && !carousel_view_pay.equals("")) {
                        try {
                            JSONObject obj = new JSONObject(carousel_view_pay);

                            Log.d("ZeCarousel", "ZeCarousel" + obj);
                            refreshCarouselDetailsPay(obj);

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
                        carousel_view_pay = mFirebaseConfig.getString("how_to_pay_tooltip_carousel");
                        has_how_to_pay_tooltip = mFirebaseConfig.getBoolean("has_how_to_pay_tooltip");
                        accessibility_has_pay_icon = mFirebaseConfig.getBoolean("accessibility_has_pay_icon");

                        storageObject.setPayTooltip(has_how_to_pay_tooltip);
                        storageObject.setPayIcon(accessibility_has_pay_icon);

                        if (carousel_view_pay != null && !carousel_view_pay.equals("")) {
                            try {
                                JSONObject obj = new JSONObject(carousel_view_pay);
                                Log.d("ZeCarousel", "ZeCarousel" + obj);
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

    public void setCarouselDetailsPay() {

        viewListenerPay = position -> {
            View customView = vi.inflate(R.layout.carousel_view_custom, null);
            //set view attributes here

            ImageView imageView = customView.findViewById(R.id.imageView);
            if (previewImagesPay != null && previewImagesPay.length > position)
                imageView.setImageResource(previewImagesPay[position]);

            FirmaTextView mainMsg = customView.findViewById(R.id.mainMsg);
            FirmaTextView subMsg = customView.findViewById(R.id.subMsg);

            mainMsg.setVisibility(View.GONE);
            subMsg.setVisibility(View.GONE);


            return customView;
        };

        previewImagesPayView.setPageCount(previewImagesPay.length);
        previewImagesPayView.setViewListener(viewListenerPay);


    }

    public void refreshCarouselDetailsPay(JSONObject jsonObject) throws JSONException {
        JSONArray drcArray = new JSONArray();
        JSONArray dataArray = new JSONArray();

        // Check if the object contains "drc" data
        if (jsonObject.has("drc")) {
            // Get the "drc" array


            JSONObject newJsonObject = jsonObject.getJSONObject("drc");
            if (newJsonObject.has("fr") && storageObject.retrieveLanguageFromPrefs().equals("fr")) {
                drcArray = newJsonObject.getJSONArray("fr");


            } else {
                drcArray = newJsonObject.getJSONArray("en");
            }

        }


        dataArray = drcArray;

//		Log.d("ZeArrayzFin","ZeArrayzFIn"+storageObject.retrieveLanguageFromPrefs()+":"+dataArray.toString());

        if (dataArray.length() > 0 && previewImagesPayView != null) {
            previewImagesPayView.setSlideInterval(default_timeout * 1000);//Set Slide Interval
            previewImagesPayView.setPageCount(dataArray.length());

            JSONArray finalDataArray = dataArray;
            viewListenerPay = new ViewListener() {
                @Override
                public View setViewForPosition(int position) {
                    View customView = vi.inflate(R.layout.carousel_view_custom, null);
                    //set view attributes here

                    ImageView imageView = customView.findViewById(R.id.imageView);

                    FirmaTextView mainMsg = customView.findViewById(R.id.mainMsg);
                    FirmaTextView subMsg = customView.findViewById(R.id.subMsg);


                    try {
                        JSONObject drcObject = finalDataArray.getJSONObject(position);

                        // Extract individual items from the "" object
                        String title = drcObject.getString("title");
                        String message = drcObject.getString("message");
                        String image = drcObject.getString("image");
                        String timeout_seconds = drcObject.getString("timeout_seconds");

                        mainMsg.setVisibility(View.GONE);
                        subMsg.setVisibility(View.GONE);

                        if (timeout_seconds != null && !timeout_seconds.equals("")) {

                            try {
                                int seconds = Integer.parseInt(timeout_seconds);
                                if (seconds > 0) {
                                    default_timeout = seconds;
                                    previewImagesPayView.setSlideInterval(default_timeout * 1000);
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
                                if (previewImagesPayView != null && previewImagesPay.length > position && imageView != null)
                                    imageView.setImageResource(previewImagesPay[position]);

                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {
                                if (previewImagesPay != null && previewImagesPay.length > position && imageView != null)
                                    imageView.setImageResource(previewImagesPay[position]);
                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    return customView;
                }
            };

            previewImagesPayView.setPageCount(dataArray.length());
            previewImagesPayView.setViewListener(viewListenerPay);

        }


    }


    //Network Check

    public void openDataUsageSettings(Context context) {
        Intent intent = new Intent();

        // Try to open Data Usage settings
        intent.setAction(Settings.ACTION_DATA_USAGE_SETTINGS);

        if (canHandleIntent(context, intent)) {
            try {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Add this flag
                context.startActivity(intent);
            } catch (Exception e) {
                Log.e("Error", "Failed to open Data Usage settings: " + e.getMessage());
                openAlternativeSettings(context);
            }
        } else {
            Log.e("Error", "No Activity found to handle Data Usage settings intent");
            openAlternativeSettings(context);
        }
    }

    // Check if an intent can be handled by the system
    private boolean canHandleIntent(Context context, Intent intent) {
        return intent.resolveActivity(context.getPackageManager()) != null;
    }

    // Open alternative settings if Data Usage is unavailable
    private void openAlternativeSettings(Context context) {
        // First, try to open Network Operator settings as a fallback
        Intent intent = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Add this flag

        if (canHandleIntent(context, intent)) {
            try {
                context.startActivity(intent);
                return;
            } catch (Exception e) {
                Log.e("Error", "Failed to open Network Operator settings: " + e.getMessage());
            }
        }

        // Next, try to open Wireless Settings
        intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Add this flag

        if (canHandleIntent(context, intent)) {
            try {
                context.startActivity(intent);
                return;
            } catch (Exception e) {
                Log.e("Error", "Failed to open Wireless settings: " + e.getMessage());
            }
        }

        // Fallback to General settings if nothing else works
        intent = new Intent(Settings.ACTION_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Add this flag

        if (canHandleIntent(context, intent)) {
            try {
                context.startActivity(intent);
            } catch (Exception e) {
                Log.e("Error", "Failed to open General settings: " + e.getMessage());
            }
        } else {
            Log.e("Error", "No Activity found to handle General settings intent");
        }


    }

    private void updateProgressText(SeekBar seekBar, FirmaTextView progressText, int progress) {
        progressText.setText(progress + "%");

        // Wait until both are laid out
        if (seekBar.getWidth() == 0 || progressText.getWidth() == 0) return;

        final int max = Math.max(1, seekBar.getMax());
        int leftPadding = seekBar.getPaddingLeft();
        int rightPadding = seekBar.getPaddingRight();
        int available = seekBar.getWidth() - leftPadding - rightPadding;

        // Progress ratio (flip for RTL)
        boolean isRtl = ViewCompat.getLayoutDirection(seekBar) == ViewCompat.LAYOUT_DIRECTION_RTL;
        float ratio = (float) progress / max;
        if (isRtl) ratio = 1f - ratio;

        // Thumb center in seekbar's local coords
        int thumbCenter = (int) (leftPadding + ratio * available);

        // Desired left edge of the label relative to seekbar start
        int desiredLeft = thumbCenter - (progressText.getWidth() / 2);

        // Clamp within seekbar width
        int minLeft = 0;
        int maxLeft = seekBar.getWidth() - progressText.getWidth();
        int clampedLeft = Math.max(minLeft, Math.min(desiredLeft, maxLeft));

        // Translate from the TextView's laid-out left (aligned to seekbar start)
        int baseLeft = progressText.getLeft(); // its laid-out position (alignedStart of seekbar)
        progressText.setTranslationX(clampedLeft - baseLeft);
    }

    //Service Down
    public void openDialogServiceDown() {
        View contentView = parentActivity.getLayoutInflater().inflate(R.layout.dialog_content_service_down, null);


        final DialogPlus dialogPlus = new DialogPlus.Builder(parentActivity)
                .setContentHolder(new ViewHolder(contentView))
                .setGravity(DialogPlus.Gravity.CENTER)
                .setBackgroundColorResourceId(R.color.transparent)
                .setCancelable(true)
                .create();


        //Content View
        View content = dialogPlus.getHolderView();


        RelativeLayout loadingView = content.findViewById(R.id.loadingItems);
        RelativeLayout btnClose = content.findViewById(R.id.btnClose);
        RelativeLayout btnBack = content.findViewById(R.id.btnBack);

        FirmaTextView titleText = content.findViewById(R.id.titleText);
        FirmaTextView titleDescription = content.findViewById(R.id.titleDescription);

        String tText = mContext.getResources().getString(R.string.serviceinterruption);
        String tDescription = mContext.getResources().getString(R.string.servicedowndescription);


        if (storageObject.retrieveServiceDownObjectFromPrefs() != null) {
            ServiceDownObject service = storageObject.retrieveServiceDownObjectFromPrefs();

            if (storageObject.retrieveLanguageFromPrefs().equalsIgnoreCase("fr")) {
                tText = service.getTitle_fr();
                tDescription = service.getTitle_description_fr();
//				Toast.makeText(mContext,"GootenBotTitle"+tText,Toast.LENGTH_SHORT).show();

            } else {
                tText = service.getTitle();
                tDescription = service.getTitle_description();

//				Toast.makeText(mContext,"GootenTitle"+tDescription,Toast.LENGTH_SHORT).show();

            }

            setHtml(titleDescription, tDescription);

        } else {
            titleText.setText(tText);
            titleDescription.setText(Html.fromHtml(tDescription));

        }


        //Current Device OBJect
        JSONObject obj = new JSONObject();
        AssetObject currentAsset = storageObject.retrieveCurrentAssetObjectFromPrefs();
        try {

            Gson gson = new Gson();

            obj.put("assetLoanNumber", currentAsset.getLoanNumber());
            obj.put("assetName", currentAsset.getAssetTypeName());
            obj.put("assetTypeId", currentAsset.getAssetTypeId());
            if (currentAsset.getDeviceUpgradeObject() != null && currentAsset.getDeviceUpgradeObject().getUpgradeDevices() != null) {
                obj.put("currentDevice", gson.toJson(currentAsset.getDeviceUpgradeObject().getCurrentDevice()));
                obj.put("upgradeOptions", gson.toJson(currentAsset.getDeviceUpgradeObject().getUpgradeDevices()));

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GeneralCommands.logEventObject(parentActivity, "click_service_down_back", "click_service_down_back", obj);
                dialogPlus.dismiss();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GeneralCommands.logEventObject(parentActivity, "click_service_down_back", "click_service_down_back", obj);
                dialogPlus.dismiss();
            }
        });


        dialogPlus.show();
    }


//    public void tryOpenServiceDownDialogWithBackoff(final Context context, boolean forceShow) {
//        if (context == null) return;
//
//        long now = System.currentTimeMillis();
//        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
//        long lastShown = prefs.getLong(KEY_SERVICE_DOWN_LAST_SHOWN, 0L);
//
//        if (forceShow || (now - lastShown) >= FOUR_HOURS_MS || lastShown == 0L) {
//            // Ensure UI thread when showing dialog
//            if (context instanceof Activity) {
//                ((Activity) context).runOnUiThread(() -> {
//                    openDialogServiceDown();
//                    // update timestamp
//                    prefs.edit().putLong(KEY_SERVICE_DOWN_LAST_SHOWN, System.currentTimeMillis()).apply();
//                });
//            } else {
//                // If no activity context available, still update timestamp and try to show
//                openDialogServiceDown();
//                prefs.edit().putLong(KEY_SERVICE_DOWN_LAST_SHOWN, System.currentTimeMillis()).apply();
//            }
//        } else {
//            // Backoff not expired — no-op (optional: log or debug)
//            long remaining = FOUR_HOURS_MS - (now - lastShown);
//            Log.d("ZeServiceDown", "Dialog backoff active, remaining ms: " + remaining);
//        }
//    }

    /** Force-clear the backoff so dialog may show immediately next time. */
//    public void resetServiceDownDialogBackoff(Context context) {
//        if (context == null) return;
//        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
//        prefs.edit().remove(KEY_SERVICE_DOWN_LAST_SHOWN).apply();
//    }

    /**
     * Remove the <Data><![CDATA[ ... ]]></Data> wrapper if present and return the inner HTML.
     */
    private String unwrapCData(String maybeCdata) {
        if (maybeCdata == null) return null;
        String s = maybeCdata.trim();

        final String start = "<Data><![CDATA[";
        final String end = "]]></Data>";

        if (s.startsWith(start) && s.endsWith(end)) {
            s = s.substring(start.length(), s.length() - end.length());
        }

        // If the JSON contained escaped newlines (like "\\n"), convert them to actual newlines
        s = s.replace("\\n", "\n");

        // Convert literal newlines to <br/> so HTML renderer preserves line breaks
        s = s.replace("\n\n", "<br/><br/>").replace("\n", "<br/>");

        // If the content was double-escaped (e.g. contains "&lt;font&gt;"), unescape basic HTML entities:
        // Simple common replacements (avoid heavy deps). Add more if you need them.
        s = s.replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&");

        return s;
    }

    /**
     * Render HTML into a TextView safely.
     */
    private void setHtml(TextView tv, String rawHtml) {
        if (tv == null) return;

        String unwrapped = unwrapCData(rawHtml);
        if (unwrapped == null) {
            tv.setText("");
            return;
        }

        // Use HtmlCompat for consistent behavior across API levels
        Spanned spanned = HtmlCompat.fromHtml(unwrapped, HtmlCompat.FROM_HTML_MODE_LEGACY);

        tv.setText(spanned);
        // Make links clickable (if you have <a href="..."> links)
        tv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    //End Service Down

    private boolean noUpgradeOptions() {
        return storageObject.retrieveCurrentAssetObjectFromPrefs().getDeviceUpgradeObject() == null || storageObject.retrieveCurrentAssetObjectFromPrefs().getDeviceUpgradeObject().getUpgradeDevices() == null || storageObject.retrieveCurrentAssetObjectFromPrefs().getDeviceUpgradeObject().getUpgradeDevices().size() == 0;
    }

    public void goToUpgradePageDirect() {

        Intent mIntent = new Intent(parentActivity, UpgradeActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
        GeneralCommands.goToPage(parentActivity, mIntent, false);

    }


}
