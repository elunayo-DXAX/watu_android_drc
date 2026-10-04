package com.watudrc.app;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.ConfigUpdate;
import com.google.firebase.remoteconfig.ConfigUpdateListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException;
import com.google.gson.Gson;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.font.FirmaTextView;
import com.watudrc.app.fragments.FragmentHomeRevamp;
import com.watudrc.app.fragments.FragmentNotifications;
import com.watudrc.app.fragments.FragmentPayment;
import com.watudrc.app.fragments.FragmentResources;
import com.watudrc.app.fragments.FragmentTransactions;
import com.watudrc.app.mobileservices.GetData;
import com.watudrc.app.objects.AssetObject;
import com.watudrc.app.objects.ProfileObject;
import com.watudrc.app.objects.ServiceDownObject;
import com.watudrc.app.prefs.RateAppPrefs;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;
import com.watudrc.app.util.DataCollector;
import com.watudrc.app.util.FirebaseConfig;
import com.watudrc.app.util.NotificationDeepLinkResolver;
import com.watudrc.app.util.PhoneNumberFormatter;
import com.watudrc.app.view.BadgeDrawerArrowDrawable;
import com.watudrc.app.view.MaterialRippleLayout;
import com.watudrc.app.view.ViewPagerNoScroll;

import org.infobip.mobile.messaging.MobileMessaging;
import org.infobip.mobile.messaging.chat.InAppChat;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.concurrent.Executor;

//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;

public class MainActivity extends BaseActivity implements IWhereMyLocationConstants {

    public DialogPlus currentDialogPlus;

    static Context mContext;
    static Activity parentActivity;
    private static StoreAndGetFromPrefs storageObject;

    //Permissions
    final int ACTION_REQUEST_MULTIPLE_PERMISSION = 1;
    String[] thePermissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //Profile
    static FirmaTextView userName;
    static FirmaTextView userNumber;
    RelativeLayout parentView;


    String mStartFrom = "";

    //Tab Stuff
    public static ViewPagerNoScroll viewPager;
    TabIconMain tabIcon;
    ViewGroup tab;
    public SmartTabLayout viewPagerTab;
    FragmentPagerItems pages;
    FragmentPagerItemAdapter adapter;

    public static boolean is_from_home_page = false;
    RelativeLayout loadingView;


    //BottomTab
//	public static MeowBottomNavigation bottomNavigation;


    //Drawers
    public Toolbar mToolbar;
    public TextView titleBarText;
    public DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private BadgeDrawerArrowDrawable badgeDrawable;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    FirebaseConfig config;
    String theme_color = "";
    FirebaseConfig remoteConfig;
    FirebaseRemoteConfig mFirebaseConfig;

    int temporary_unlock_max_attempts = 3;

    int pay_for_another_attempts = 5;
    boolean upgrade_feature_enabled = true;

    public static String is_source = "daily";


    //Data Collector
    private static final int REQ_CODE_PERMS = 1001;
    private static final String[] REQ_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };


    //Service Down

    MaterialRippleLayout serviceDownRips, documentRips;

    RelativeLayout btnServiceDown;
    //Service Down
    String service_down = "";
    // constants (put these in your class)
    private static final String PREFS_NAME = "app_prefs";
    private static final String KEY_SERVICE_DOWN_LAST_SHOWN = "service_down_last_shown";
    private static final long FOUR_HOURS_MS = 1L * 60L * 60L * 1000L; // 1 hours in ms
//	private static final long FOUR_HOURS_MS = 2L * 60L * 1000L; // 2 minutes (TEST ONLY)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RateAppPrefs.onAppLaunch(this);
        allowLock();


        parentActivity = this;
        mContext = parentActivity.getApplicationContext();
        storageObject = new StoreAndGetFromPrefs(mContext);

//		addWelcomeNotification();//Welcome Notifcation

        loadingView = findViewById(R.id.loadingItems);
        parentView = findViewById(R.id.mainContent);

        userName = findViewById(R.id.userName);
        userNumber = findViewById(R.id.userNumber);

        //Service Down
        serviceDownRips = parentView.findViewById(R.id.serviceDownRips);
        ;
        btnServiceDown = findViewById(R.id.btnServiceDown);

        if (storageObject.retrieveServiceDownObjectFromPrefs() != null && storageObject.retrieveServiceDownObjectFromPrefs().getServiceDown()) {
            //Service is down
            serviceDownRips.setVisibility(View.VISIBLE);
        } else {
            serviceDownRips.setVisibility(View.GONE);

        }
        btnServiceDown.setOnClickListener(v -> openDialogServiceDown());

        Intent mIntent = getIntent();
        if (mIntent != null) {

            mStartFrom = mIntent.getStringExtra(KEY_START_FROM);
            if (mStartFrom != null) {
                mStartFrom = mIntent.getStringExtra(KEY_START_FROM);

                assert mStartFrom != null;
                if (mStartFrom.equals("payments_process")) {
                    changeFragment(new FragmentTransactions());
                }

            }
        }

        requestPermissions();
//

        //Tab Stuff
        tabIcon = TabIconMain.getTab();

        tab = findViewById(R.id.theTab);
        tab.addView(LayoutInflater.from(this).inflate(R.layout.tab_layout_main, tab, false));

        viewPager = findViewById(R.id.theViewPager);
        viewPagerTab = findViewById(R.id.viewpagertab);

        tabIcon.setup(viewPagerTab);

        pages = new FragmentPagerItems(this);
        pages.add(FragmentPagerItem.of(mContext.getResources().getString(R.string.tab_1), FragmentHomeRevamp.class));
        pages.add(FragmentPagerItem.of(mContext.getResources().getString(R.string.tab_2), FragmentTransactions.class));
        pages.add(FragmentPagerItem.of(mContext.getResources().getString(R.string.tab_3), FragmentPayment.class));
        pages.add(FragmentPagerItem.of(mContext.getResources().getString(R.string.notifications), FragmentNotifications.class));
        pages.add(FragmentPagerItem.of(mContext.getResources().getString(R.string.support), FragmentResources.class));


        adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), pages);

        viewPager.setAdapter(adapter);
        viewPagerTab.setViewPager(viewPager);

        //Remove Ripple
        for (int i = 0; i < tabIcon.tabs().length; i++) {
            View tabs = viewPagerTab.getTabAt(i);
            tabs.setBackground(null);
        }

        // Set initial tab selection
        updateTabSelection(viewPager.getCurrentItem());

        // Listen for tab clicks
        viewPagerTab.setOnTabClickListener(new SmartTabLayout.OnTabClickListener() {
            @Override
            public void onTabClicked(int position) {
                viewPager.setCurrentItem(position);
                updateTabSelection(position);
            }
        });

        // Listen for page changes (swipes)
        viewPager.addOnPageChangeListener(new ViewPagerNoScroll.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                updateTabSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //Drawers
        mToolbar = findViewById(R.id.toolbar);
//		setSupportActionBar(mToolbar);


        titleBarText = mToolbar.findViewById(R.id.titleBarText);
        titleBarText.setVisibility(View.GONE);
        titleBarText.setText(mContext.getResources().getString(R.string.tab_1));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = findViewById(R.id.drawer_layout);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                //mToolbar.setTitle(mTitle);
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                //mToolbar.setTitle(mDrawerTitle);
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                refreshShowDocuments();
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);


        badgeDrawable = new BadgeDrawerArrowDrawable(mToolbar.getContext());
        mDrawerToggle.setToolbarNavigationClickListener(v -> {
            // TODO Auto-generated method stub
            if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            } else {
                mDrawerLayout.openDrawer(Gravity.LEFT);
                refreshShowDocuments();
            }
        });

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerToggle.setHomeAsUpIndicator(badgeDrawable);


        setDrawerDetails(mDrawerLayout);


        setDrawerDetails(mDrawerLayout);
        setDrawerProfile();

        new Thread(new Runnable() {
            public void run() {
                // a potentially time consuming task
                setDefaultConfigs();
                setFirebaseDetails();
                parentView.post(new Runnable() {
                    public void run() {
                        //Update UI
                    }
                });
            }
        }).start();

        GetData ob1 = new GetData();
        ob1.initialize(parentActivity, loadingView);
        ob1.userGetCustomerDetails(true);
        ob1.userGetPaymentProviders(true);
        ob1.userUpdateTokenFCM(storageObject.retrieveUserFCMTokenFromPrefs(), true);
        ob1.userGetPaymentInfo(true);
        ob1.setCustomerDetailsListener(new GetData.CustomerDetailsListener() {
            @Override
            public void onObjectReady(String title) {

            }

            @Override
            public void onDataLoaded(ProfileObject data) {

                if (data != null) {
                    setDrawerProfile();
                }
            }
        });

        setDefaultConfigs();
//		addDummyNotifications();

        //Rate App
//		initializeRateApp();

        if (!hasAllPermissions()) {
            ActivityCompat.requestPermissions(this, REQ_PERMS, REQ_CODE_PERMS);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                startCollectors();
            }
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                goBack();
            }
        });

        new MobileMessaging.Builder(this.getApplication())
                .withApplicationCode(getString(R.string.infobip_application_code))
                .build();

        if (getIntent() != null) {
            handleTabSelection(getIntent());
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);          // keep getIntent() in sync

        // 1. Pop all nested fragment transactions inside MainActivity back to root tabs
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        // 2. Select the destination tab from extras
        handleTabSelection(intent);
    }

    private void handleTabSelection(Intent intent) {
        if (intent == null) return;

        int targetTab = -1;
        String category = null;

        if (intent.hasExtra("OPEN_TAB")) {
            targetTab = intent.getIntExtra("OPEN_TAB", -1);
        } else if (intent.hasExtra("category")) {
            category = intent.getStringExtra("category");
        } else if (intent.hasExtra(KEY_START_FROM)) {
            String startFrom = intent.getStringExtra(KEY_START_FROM);
            if ("notifications".equalsIgnoreCase(startFrom)) {
                targetTab = NotificationDeepLinkResolver.TAB_NOTIFICATIONS;
            }
        }

        if (targetTab == -1 && category == null) {
            // The push tap may have landed on LoginActivity/SplashScreenActivity first
            // (they have no viewPager), which stashed the category for us to consume here.
            String pendingCategory = storageObject.retrievePendingNotificationCategoryFromPrefs();
            if (pendingCategory != null && !pendingCategory.isEmpty()) {
                category = pendingCategory;
                storageObject.clearPendingNotificationCategoryFromPrefs();
            }
        }

        if (NotificationDeepLinkResolver.isLiveChatCategory(category)) {
            goToChatPage("push_notification");
            return;
        }

        if (targetTab == -1 && category != null) {
            targetTab = NotificationDeepLinkResolver.resolveTabForCategory(category);
        }

        if (targetTab != -1 && viewPager != null) {
            final int finalTab = targetTab;
            viewPager.post(() -> {
                // Smooth scroll set to false prevents page sliding flickers
                viewPager.setCurrentItem(finalTab, false);
                updateTabSelection(finalTab);
            });
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        checkAppRating();//Rate App
//    }

    @Override
    public void onResume() {
        super.onResume();
//		checkAppRating();//Rate App
        // 1) Runtime permissions
        if (!hasAllPermissions()) {
            //ActivityCompat.requestPermissions(this, REQ_PERMS, REQ_CODE_PERMS);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                startCollectors();

            }
        }
    }

    public static void setDrawerProfile() {

        String name = storageObject.retrieveUserNameFromPrefs().toLowerCase().contains("null") ? "" : storageObject.retrieveUserNameFromPrefs();

        userName.setText(name);
        userNumber.setText(storageObject.retrieveUserPhoneNumberFromPrefs());
        try {
            FragmentHomeRevamp.userInitials.setText(GeneralCommands.getUserInitials(storageObject.retrieveUserNameFromPrefs()));
            FragmentHomeRevamp.userName.setText(storageObject.retrieveUserFirstNameFromPrefs());

        } catch (Exception e) {
            e.printStackTrace();
        }


        PhoneNumberFormatter.applyFormattingToTextView(userNumber, storageObject.retrieveUserPhoneNumberFromPrefs());


    }

    private void changeFragment(Fragment targetFragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
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

    public void toggleDrawer() {
        try {
            if (mDrawerLayout != null) {
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void closeDrawer() {
        try {
            if (mDrawerLayout != null) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    public void goBack() {
//        try {
//
//
//
//            if (mDrawerLayout != null) {
//                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
//                    mDrawerLayout.closeDrawer(Gravity.LEFT);
//                } else {
//                    //Drawer is closed
//                    //CheckTabview
//                    if (viewPager != null) {
//                        if (viewPager.getCurrentItem() == 0) {
//                            //Do nothing
//                            GeneralCommands ob1 = new GeneralCommands(parentActivity);
//                            openDialogConfirmLogOutUser(mContext.getResources().getString(R.string.confirmlogout));
//                        } else {
//                            viewPager.setCurrentItem(0);
//                        }
//                    } else {
//                        //Do nothing
//                        finish();
//                    }
//
//                }
//
//            }
//        } catch (Exception e) {
//            finish();
//
//        }
//    }

    public void goBack() {
        try {

            // 1. Check DialogPlus first
            if (currentDialogPlus != null) {
                currentDialogPlus.dismiss();
                currentDialogPlus=null;
                return; // Exit so we don't trigger the Drawer or ViewPager logic
            }

            if (mDrawerLayout != null)
            {
                if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    //Drawer is closed
                    //CheckTabview
                    if (viewPager != null) {
                        if (viewPager.getCurrentItem() == 0) {
                            //Do nothing
                            GeneralCommands ob1 = new GeneralCommands(parentActivity);
                            openDialogConfirmLogOutUser(mContext.getResources().getString(R.string.confirmlogout));
                        } else {
                            viewPager.setCurrentItem(0);
                        }
                    } else {
                        //Do nothing
                        //finish();

                        logOutUser();
                    }

                }

            }
        } catch (Exception e) {
            logOutUser();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        GeneralCommands.logEvent(this, "view_home_page", "view_home_page");
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

    public void requestPermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean readDenied = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED;
            boolean writeDenied = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED;

            if (readDenied || writeDenied) {
                boolean shouldShowRationale =
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (shouldShowRationale) {
                    // Show dialog because user has denied before
                    new AlertDialog.Builder(this)
                            .setTitle(getString(R.string.permgivepermissions))
                            .setMessage(Html.fromHtml("<p>" + getString(R.string.permallow) + "</p>"))
                            .setNeutralButton(getString(R.string.okay), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions(
                                            MainActivity.this,  // or `parentActivity` if that's your context
                                            thePermissions,
                                            ACTION_REQUEST_MULTIPLE_PERMISSION
                                    );
                                }
                            })
                            .show();
                } else {
                    // First time or "Don't ask again" not selected — just request
                    ActivityCompat.requestPermissions(this, thePermissions, ACTION_REQUEST_MULTIPLE_PERMISSION);
                }
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    //Drawer Stuff

    private void setDrawerDetails(View drawerView) {


        FirmaTextView versionText= drawerView.findViewById(R.id.versionText);
        try {
            PackageInfo pInfo = parentActivity.getPackageManager().getPackageInfo(parentActivity.getPackageName(), 0);
            String versionName = pInfo.versionName;

            // Use longVersionCode for modern Android (API 28+)
            long versionCode = androidx.core.content.pm.PackageInfoCompat.getLongVersionCode(pInfo);

            versionText.setText(parentActivity.getString(R.string.app_version, versionName + "." + versionCode));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        RelativeLayout btnLogout= drawerView.findViewById(R.id.btnLogout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralCommands ob1 = new GeneralCommands(parentActivity);
                ob1.openDialogConfirmLogOutUser(mContext.getResources().getString(R.string.confirmlogout));
            }
        });

        RelativeLayout btnLanguage = drawerView.findViewById(R.id.btnLanguage);
        RelativeLayout btnPermissions = drawerView.findViewById(R.id.btnPermissions);
        RelativeLayout btnAboutUs= drawerView.findViewById(R.id.btnAboutUs);
        RelativeLayout btnNotifications= drawerView.findViewById(R.id.btnNotifications);
        RelativeLayout btnBack= drawerView.findViewById(R.id.btnBack);
        RelativeLayout btnFingerprint = drawerView.findViewById(R.id.btnFingeprint);
        MaterialRippleLayout upgradeRips= drawerView.findViewById(R.id.upgradeRips);
        documentRips= drawerView.findViewById(R.id.documentRips);

        RelativeLayout btnChat = drawerView.findViewById(R.id.btnChat);

        SwitchCompat  switchFingerprint= drawerView.findViewById(R.id.switchFingerprint);
        switchFingerprint.setChecked(storageObject.isUserFingerprintEnabled());



        RelativeLayout btnDocuments= drawerView.findViewById(R.id.btnDocuments);
        RelativeLayout btnUpgrade= drawerView.findViewById(R.id.btnUpgrade);


        RelativeLayout btnResources= drawerView.findViewById(R.id.btnResources);
        RelativeLayout btnHelpAndSupport= drawerView.findViewById(R.id.btnHelpAndSupport);

        FirmaTextView upgradeText= drawerView.findViewById(R.id.upgradeText);

        if(storageObject.isUpgradeTestTwoEnabled())
        {
            upgradeText.setText(mContext.getResources().getText(R.string.levelup));
        }


        //Jijenge
        if(storageObject.isUpgradeFeatureEnabled())
        {
            upgradeRips.setVisibility(View.VISIBLE);
        }
        else
        {
            upgradeRips.setVisibility(View.GONE);

        }


        refreshShowDocuments();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GeneralCommands.logEvent(parentActivity,"click_drawer_back","click_drawer_back");
                toggleDrawer();
            }
        });
        btnLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GeneralCommands.logEvent(parentActivity,"click_drawer_language","click_drawer_language");

                goToLanguagePage();
                toggleDrawer();
            }
        });

        btnPermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GeneralCommands.logEvent(parentActivity,"click_drawer_permissions","click_drawer_permissions");
                toggleDrawer();
                goToUpdatePermissionsPage();
            }
        });

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GeneralCommands.logEvent(parentActivity,"support_hub_opened_drawer_contact_us","support_hub_opened_drawer_contact_us");
                toggleDrawer();
                goToChatPage("navigation_drawer");
            }
        });

        btnDocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GeneralCommands.logEvent(parentActivity,"click_drawer_documents","click_drawer_documents");
                toggleDrawer();
                goToDocumentsPage();
            }
        });

        btnFingerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!switchFingerprint.isChecked())
                {
                    GeneralCommands.logEvent(parentActivity,"click_drawer_fingerprint_enable","click_drawer_fingerprint_enable");


                    //Enable
                    if(checkBiometricSupportAndProceed()) {
                        showBiometricPrompt(switchFingerprint);
                    } else
                    {
                        Toast.makeText(parentActivity, mContext.getResources().getString(R.string.fingeprintnotsupported), Toast.LENGTH_SHORT).show();
                    }

                }
                else {

                    GeneralCommands.logEvent(parentActivity,"click_drawer_fingerprint_disable","click_drawer_fingerprint_disable");

                    //Disable
                    storageObject.saveUSerFingeprintEnabled(false);
                    switchFingerprint.setChecked(false);

                }

            }
        });

//		switchFingerprint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				if(isChecked)
//				{
//					if(checkBiometricSupportAndProceed()) {
//						showBiometricPrompt();
//
//					} else
//					{
//						Toast.makeText(parentActivity, mContext.getResources().getString(R.string.fingeprintnotsupported), Toast.LENGTH_SHORT).show();
//					}
//
//				}
//				else {
//					storageObject.saveUSerFingeprintEnabled(false);
//
//				}
//			}
//		});

        btnUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GeneralCommands.logEvent(parentActivity,"click_drawer_upgrade","click_drawer_upgrade");
                toggleDrawer();
                goToUpgradePage();
            }
        });


        btnAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GeneralCommands.logEvent(parentActivity,"click_drawer_about_us","click_drawer_about_us");
                toggleDrawer();
                goToAboutUsPage();
            }
        });


        btnResources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GeneralCommands.logEvent(parentActivity,"click_drawer_resources","click_drawer_resources");
                toggleDrawer();
                viewPager.setCurrentItem(4);
            }
        });

        btnNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GeneralCommands.logEvent(parentActivity,"click_drawer_notifications","click_drawer_notifications");
                toggleDrawer();
                viewPager.setCurrentItem(3);
            }
        });


        btnHelpAndSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GeneralCommands.logEvent(parentActivity,"click_drawer_help_and_support","click_drawer_help_and_support");
                toggleDrawer();
                goToFAQsPage();
            }
        });

    }
    private void refreshShowDocuments() {

        if(storageObject!=null && documentRips !=null)
        {
            //Should show statement
            if(storageObject.retrieveCurrentAssetObjectFromPrefs()!=null && storageObject.retrieveCurrentAssetObjectFromPrefs().hasLoanAgreement())
            {
                documentRips.setVisibility(View.VISIBLE);
            }
            else {
                documentRips.setVisibility(View.GONE);
            }

        }

    }
    private void goToChatsPage() {
        Toast.makeText(mContext, "Chat With Us", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void setTitle(int titleId) {
        setTitle(getString(titleId));
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    //End Drawer Stuff

    public void goToLanguagePage() {
        Intent mIntent = new Intent(parentActivity, LanguageActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
        GeneralCommands.goToPage(parentActivity, mIntent, true);

    }

    public void goToSettingsPage() {
        Intent mIntent = new Intent(parentActivity, SettingsActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
        GeneralCommands.goToPage(parentActivity, mIntent, false);

    }

    public void goToAboutUsPage() {
        Intent mIntent = new Intent(parentActivity, AboutUsActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
        GeneralCommands.goToPage(parentActivity, mIntent, false);

    }

    public void goToDocumentsPage() {
        Intent mIntent = new Intent(parentActivity, DocumentsActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
//        mIntent.putExtra(ITEM_ID, position);
        GeneralCommands.goToPage(parentActivity, mIntent, false);

    }

    public static void goToChatPage(String entryPoint) {
        if (parentActivity == null || parentActivity.isFinishing() || parentActivity.isDestroyed()) {
            return;
        }

        Intent mIntent = new Intent(parentActivity, ChatActivity.class);
        mIntent.putExtra(KEY_START_FROM, entryPoint);
        GeneralCommands.goToPage(parentActivity, mIntent, false);

    }


    public void goToUpgradePage() {
        Intent mIntent = new Intent(parentActivity, LevelUpActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
//        mIntent.putExtra(ITEM_ID, position);
        GeneralCommands.goToPage(parentActivity, mIntent, false);

    }

    public void goToFeedbackPage() {
        Intent mIntent = new Intent(parentActivity, FeedbackActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
        GeneralCommands.goToPage(parentActivity, mIntent, false);

    }

    public void goToFAQsPage() {

        Intent mIntent = new Intent(parentActivity, FAQsActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
        GeneralCommands.goToPage(parentActivity, mIntent, false);

    }

    public void goToResourcesPage() {

        Intent mIntent = new Intent(parentActivity, ResourcesActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
        GeneralCommands.goToPage(parentActivity, mIntent, false);

    }

    public void goToNotificationsPage() {

        Intent mIntent = new Intent(parentActivity, NotificationsActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
        GeneralCommands.goToPage(parentActivity, mIntent, false);

    }

    public void setDefaultConfigs() {
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


                    //Unlock Attempts
                    temporary_unlock_max_attempts = (int) mFirebaseConfig.getLong("MOB_APP_UNLOCK_MAX_COUNT");
                    storageObject.saveTempUnlockConfigToPrefs(temporary_unlock_max_attempts);

                    //Other Config
                    pay_for_another_attempts = (int) mFirebaseConfig.getLong("PAY_FOR_ANOTHER_NUMBER_MAX_ATTEMPTS");
                    storageObject.savePayOtherConfigToPrefs(pay_for_another_attempts);


                    //Upgrade Feature
                    upgrade_feature_enabled = mFirebaseConfig.getBoolean("upgrade_feature_enabled");
                    storageObject.saveUpgradeFeatureEnabledToPrefs(upgrade_feature_enabled);

                    //Service Down Object Check
                    service_down = mFirebaseConfig.getString("service_down");
                    if (service_down != null && !service_down.trim().isEmpty()) {
                        try {
                            Log.d("ZeDown", "ZeDown" + service_down);

                            // Save to Prefs
                            storageObject.saveServiceDownObjectToPrefs(service_down);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


//					Toast.makeText(mContext, "Ozi"+temporary_unlock_max_attempts, Toast.LENGTH_SHORT).show();


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

                        //Unlock Attempts
                        temporary_unlock_max_attempts = (int) mFirebaseConfig.getLong("MOB_APP_UNLOCK_MAX_COUNT");
                        storageObject.saveTempUnlockConfigToPrefs(temporary_unlock_max_attempts);


                        //Update Unlock Button View

                        //Other COnfig
                        pay_for_another_attempts = (int) mFirebaseConfig.getLong("PAY_FOR_ANOTHER_NUMBER_MAX_ATTEMPTS");
                        storageObject.savePayOtherConfigToPrefs(pay_for_another_attempts);


                        //Upgrade Feature
                        upgrade_feature_enabled = mFirebaseConfig.getBoolean("upgrade_feature_enabled");
                        storageObject.saveUpgradeFeatureEnabledToPrefs(upgrade_feature_enabled);


                        //Update Other Number Views
                        try {
                            FragmentPayment.setSwitch();
                        } catch (Exception e) {
                        }
//
//						Toast.makeText(mContext, "Ozing"+temporary_unlock_max_attempts,
//								Toast.LENGTH_SHORT).show();

                        //Service Down Object Check
                        service_down = mFirebaseConfig.getString("service_down");
                        if (service_down != null && !service_down.trim().isEmpty()) {
                            try {
                                Log.d("ZeDown", "ZeDown" + service_down);

                                // Save to Prefs
                                storageObject.saveServiceDownObjectToPrefs(service_down);
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

    public void initializeRateApp() {
        try {
            ReviewManager reviewManager = ReviewManagerFactory.create(this);
            Task<ReviewInfo> request = reviewManager.requestReviewFlow();
            request.addOnCompleteListener(task -> {
                try {
                    if (task.isSuccessful()) {
                        ReviewInfo reviewInfo = task.getResult();
                        Task<Void> flow = reviewManager.launchReviewFlow(MainActivity.this, reviewInfo);
                        flow.addOnCompleteListener(flowTask -> {
                            try {
                                if (flowTask.isSuccessful()) {
                                    // Treat as success even if UI wasn’t shown (Play decides)
                                    RateAppPrefs.markRated(parentActivity);

                                    JSONObject obj = new JSONObject();
                                    obj.put("rating_success", "rating_success");
                                    GeneralCommands.logEventObject(parentActivity, "click_rating_success", "click_rating_success", obj);
                                } else {
                                    // User dismissed or Play didn’t show UI: back off
                                    RateAppPrefs.backoff(parentActivity, 90);

                                    JSONObject obj = new JSONObject();
                                    obj.put("rating_cancelled", "rating_cancelled");
                                    GeneralCommands.logEventObject(parentActivity, "click_rating_cancelled", "click_rating_cancelled", obj);
                                }
                            } catch (Exception ignored) {
                                RateAppPrefs.backoff(parentActivity, 90);
                            }
                        });
                    } else {
                        // Couldn’t fetch ReviewInfo: back off
                        RateAppPrefs.backoff(parentActivity, 30);
                    }
                } catch (Exception ignored) {
                    RateAppPrefs.backoff(parentActivity, 30);
                }
            });
        } catch (Exception ignored) {
            RateAppPrefs.backoff(parentActivity, 30);
        }
    }

    public void setFirebaseDetails() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.d("ZeFirebaseToken", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        Log.d("ZeFirebaseToken", token);
//						Toast.makeText(MainActivity.this, "Token is "+token, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //update tab
    private void updateTabSelection(int selectedTab) {
        //5 tabs: 0=Home, 1=Transactions, 2=Payments, 3=Notifications, 4=Support
        for (int i = 0; i < 5; i++) {
            View tab = viewPagerTab.getTabAt(i);
            if (tab == null) continue;

            RelativeLayout iconBackground = tab.findViewById(R.id.iconBackground);
            ImageView icon = tab.findViewById(R.id.icon);
            TextView label = tab.findViewById(R.id.iconText);
            View dot = tab.findViewById(R.id.tabDot);

            if (i == selectedTab) {

                if (i == 2) { // Payments tab
                    iconBackground.setBackgroundResource(R.xml.rounded_circle_green_small);
                    icon.setColorFilter(ContextCompat.getColor(parentActivity, R.color.black), PorterDuff.Mode.SRC_IN);
                    label.setTextColor(getResources().getColor(R.color.theme_green));
                    if (dot != null) dot.setVisibility(View.GONE);
                } else {
                    iconBackground.setBackgroundResource(R.drawable.custom_tab); // or your theme background
                    icon.setColorFilter(ContextCompat.getColor(parentActivity, R.color.theme_color), PorterDuff.Mode.SRC_IN);
                    label.setTextColor(getResources().getColor(R.color.theme_color));
                    if (dot != null) dot.setVisibility(View.VISIBLE);
                }
            } else {

                if (i == 2) { // Payments tab
                    iconBackground.setBackgroundResource(R.xml.rounded_circle_green_small);
                    icon.setColorFilter(ContextCompat.getColor(parentActivity, R.color.black), PorterDuff.Mode.SRC_IN);
                    label.setTextColor(getResources().getColor(R.color.theme_green));
                    if (dot != null) dot.setVisibility(View.GONE);
                } else {
                    iconBackground.setBackgroundResource(android.R.color.transparent);
                    icon.setColorFilter(ContextCompat.getColor(parentActivity, R.color.white), PorterDuff.Mode.SRC_IN);
                    label.setTextColor(getResources().getColor(R.color.white));
                    if (dot != null) dot.setVisibility(View.GONE);
                }

            }
        }
    }

    //Finger Print
    private boolean checkBiometricSupportAndProceed() {
        BiometricManager biometricManager = BiometricManager.from(parentActivity);

        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                // Everything is good
                return true;

            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // Device supports biometrics, but none enrolled
                Toast.makeText(parentActivity, mContext.getResources().getString(R.string.fingeprintnotsetup), Toast.LENGTH_LONG).show();
                Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BiometricManager.Authenticators.BIOMETRIC_STRONG);
                startActivity(enrollIntent);
                break;

            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
            default:
                // Device does not support biometric
                Toast.makeText(parentActivity, mContext.getResources().getString(R.string.fingeprintnotsupported), Toast.LENGTH_SHORT).show();
                return false;
        }

        return false;
    }

    private void showBiometricPrompt(SwitchCompat switchFingerprint) {
        Executor executor = ContextCompat.getMainExecutor(parentActivity);
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor,
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
//						Toast.makeText(parentActivity, "Auth error: " + errString, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
//						Toast.makeText(parentActivity, "Auth succeeded!", Toast.LENGTH_SHORT).show();
                        storageObject.saveUSerFingeprintEnabled(true);
                        switchFingerprint.setChecked(true);

                        // TODO: Proceed to the main activity or perform login
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
//						Toast.makeText(parentActivity, "Auth failed", Toast.LENGTH_SHORT).show();
                    }
                });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(mContext.getResources().getString(R.string.loginwithfingerprint))
                .setSubtitle(mContext.getResources().getString(R.string.touchfingerprinttoauthenticate))
                .setNegativeButtonText(mContext.getResources().getString(R.string.cancel))
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    public void checkAppRating() {
        if (storageObject.isRatingEnabled()) {


            if (!RateAppPrefs.isEligibleForEnjoyPrompt(this)) return;

            // Mark as shown immediately so it never re-appears if user dismisses dialog
            RateAppPrefs.markEnjoyPromptShown(this);

            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.enjoyingtheapp))
                    .setMessage(getString(R.string.enjoyingtheappsofar))
                    .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                        GeneralCommands.logEvent(this, "click_rate_dialog_enjoying_app_ok", "click_rate_dialog_enjoying_app_ok");
                        initializeRateApp(); // launches in-app review
                    })
                    .setNegativeButton(getString(R.string.notreally), (dialog, which) -> {
                        GeneralCommands.logEvent(this, "click_rate_dialog_enjoying_app_not_really", "click_rate_dialog_enjoying_app_not_really");
                        RateAppPrefs.disableRatingPrompt(this); // never ask again
                    })
                    .setCancelable(true)
                    .show();
        }
    }

    public static void openWhatsAppWithMessage(String message, String jijenge) {
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

    public void openDialogConfirmLogOutUser(String message) {
        View contentView = parentActivity.getLayoutInflater().inflate(R.layout.dialog_content_confirm, null);


        final DialogPlus dialogPlus = new DialogPlus.Builder(parentActivity)
                .setContentHolder(new ViewHolder(contentView))
                .setGravity(DialogPlus.Gravity.CENTER)
                .setBackgroundColorResourceId(R.color.transparent)
                .setCancelable(true)
                .create();


        //Content View
        View content = dialogPlus.getHolderView();

        FirmaTextView confirmMessage = (FirmaTextView) content.findViewById(R.id.confirmText);

        confirmMessage.setText(message);

        final RelativeLayout loadingView = (RelativeLayout) content.findViewById(R.id.loadingItems);


        RelativeLayout btnYes = (RelativeLayout) content.findViewById(R.id.btnYes);

        btnYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                GeneralCommands.logEvent(parentActivity, "click_logout_user", "click_logout_user");
                dialogPlus.dismiss();
                logOutUser();
            }
        });

        RelativeLayout btnNo = (RelativeLayout) content.findViewById(R.id.btnNo);
        btnNo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialogPlus.dismiss();
            }
        });


        dialogPlus.show();
    }


    //Service Down
    public void openDialogServiceDown()
    {
        View contentView = parentActivity.getLayoutInflater().inflate(R.layout.dialog_content_service_down, null);


        final DialogPlus dialogPlus = new DialogPlus.Builder(parentActivity)
                .setContentHolder(new ViewHolder(contentView))
                .setGravity(DialogPlus.Gravity.CENTER)
                .setBackgroundColorResourceId(R.color.transparent)
                .setCancelable(true)
                .create();


        //Content View
        View content = dialogPlus.getHolderView();



        RelativeLayout loadingView= content.findViewById(R.id.loadingItems);
        RelativeLayout btnClose =content.findViewById(R.id.btnClose);
        RelativeLayout btnBack= content.findViewById(R.id.btnBack);

        FirmaTextView titleText=content.findViewById(R.id.titleText);
        FirmaTextView titleDescription=content.findViewById(R.id.titleDescription);

        String tText=mContext.getResources().getString(R.string.serviceinterruption);
        String tDescription=mContext.getResources().getString(R.string.servicedowndescription);

        ImageView errorIcon = content.findViewById(R.id.titleImage);


        if (storageObject.retrieveServiceDownObjectFromPrefs() != null) {
            ServiceDownObject service = storageObject.retrieveServiceDownObjectFromPrefs();

            if(service.getCategory() !=null && service.getTitle_description() !=null)
            {
                if (service.getCategory().toLowerCase().contains("server_down")) {
                    errorIcon.setImageResource(R.drawable.ics_icon_service_down);
                } else if (service.getCategory().toLowerCase().contains("intermittent")) {

                    errorIcon.setImageResource(R.drawable.ics_icon_service_intermittent);

                } else if (service.getCategory().toLowerCase().contains("payment")) {
                    errorIcon.setImageResource(R.drawable.ics_icon_service_payment);

                } else if (service.getCategory().toLowerCase().contains("timestamp")) {
                    errorIcon.setImageResource(R.drawable.ics_icon_service_intermittent);

                }
            }

            if (storageObject.retrieveLanguageFromPrefs().equalsIgnoreCase("fr")) {

                if(service.getTitle_fr()!=null && service.getTitle_description_fr()!=null)
                {
                    tText = service.getTitle_fr();
                    tDescription = service.getTitle_description_fr();
                }

            } else
            {
                tText = service.getTitle();
                tDescription = service.getTitle_description();

//				Toast.makeText(mContext,"GootenTitle"+tDescription,Toast.LENGTH_SHORT).show();

            }

            setHtml(titleDescription, tDescription);
            setHtml(titleText, tText);

        } else {
            titleText.setText(tText);
            titleDescription.setText(Html.fromHtml(tDescription));

        }




        //Current Device OBJect
        JSONObject obj = new JSONObject();
        AssetObject currentAsset= storageObject.retrieveCurrentAssetObjectFromPrefs();
        try {

            Gson gson= new Gson();

            obj.put("assetLoanNumber",currentAsset.getLoanNumber());
            obj.put("assetName",currentAsset.getAssetTypeName());
            obj.put("assetTypeId",currentAsset.getAssetTypeId());
            if(currentAsset.getDeviceUpgradeObject()!=null && currentAsset.getDeviceUpgradeObject().getUpgradeDevices()!=null)
            {
                obj.put("currentDevice",gson.toJson(currentAsset.getDeviceUpgradeObject().getCurrentDevice()));
                obj.put("upgradeOptions",gson.toJson(currentAsset.getDeviceUpgradeObject().getUpgradeDevices()));

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }



        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GeneralCommands.logEventObject(parentActivity,"click_service_down_back","click_service_down_back", obj);
                dialogPlus.dismiss();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GeneralCommands.logEventObject(parentActivity,"click_service_down_back","click_service_down_back", obj);
                dialogPlus.dismiss();
            }
        });






        dialogPlus.show();
    }

    public void tryOpenServiceDownDialogWithBackoff(final Context context, boolean forceShow) {
        if (context == null) return;

        long now = System.currentTimeMillis();
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        long lastShown = prefs.getLong(KEY_SERVICE_DOWN_LAST_SHOWN, 0L);

        if (forceShow || (now - lastShown) >= FOUR_HOURS_MS || lastShown == 0L) {
            // Ensure UI thread when showing dialog
            if (context instanceof Activity) {
                ((Activity) context).runOnUiThread(() -> {
                    openDialogServiceDown();
                    // update timestamp
                    prefs.edit().putLong(KEY_SERVICE_DOWN_LAST_SHOWN, System.currentTimeMillis()).apply();
                });
            } else {
                // If no activity context available, still update timestamp and try to show
                openDialogServiceDown();
                prefs.edit().putLong(KEY_SERVICE_DOWN_LAST_SHOWN, System.currentTimeMillis()).apply();
            }
        } else {
            // Backoff not expired — no-op (optional: log or debug)
            long remaining = FOUR_HOURS_MS - (now - lastShown);
            Log.d("ZeServiceDown", "Dialog backoff active, remaining ms: " + remaining);
        }
    }

    /**
     * Force-clear the backoff so dialog may show immediately next time.
     */
    public void resetServiceDownDialogBackoff(Context context) {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(KEY_SERVICE_DOWN_LAST_SHOWN).apply();
    }

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


    //Data Collector
    private boolean hasAllPermissions() {
        for (String p : REQ_PERMS) {
            if (ContextCompat.checkSelfPermission(this, p)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    //DataCollector

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startCollectors() {
        // you could kick off periodic uploads here, e.g.:
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                new DataCollector(this, "Auth_Success").collectAndSend();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void logOutUser() {
        storageObject.logOutUser();
        //LoginManager.getInstance().logOut();

        Intent mIntent = new Intent(parentActivity, LoginActivity.class);
        mIntent.putExtra(KEY_START_FROM, "main");
        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        GeneralCommands.goToPage(parentActivity,mIntent,true);

    }

    public void goToUpdatePermissionsPage()
    {
        Intent mIntent = new Intent(parentActivity, UpdatePermissionsActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
        GeneralCommands.goToPage( parentActivity,mIntent,false);

    }
}
