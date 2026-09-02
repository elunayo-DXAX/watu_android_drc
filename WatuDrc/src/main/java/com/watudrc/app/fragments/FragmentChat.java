package com.watudrc.app.fragments;

import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.telecom.TelecomManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.ConfigUpdate;
import com.google.firebase.remoteconfig.ConfigUpdateListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException;
import com.google.gson.Gson;
import com.watudrc.app.ChatActivity;
import com.watudrc.app.GeneralCommands;
import com.watudrc.app.MainActivity;
import com.watudrc.app.PermissionsActivity;
import com.watudrc.app.R;
import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.font.FirmaTextView;
import com.watudrc.app.mobileservices.GetData;
import com.watudrc.app.objects.AssetObject;
import com.watudrc.app.objects.DeviceUpgradeObject;
import com.watudrc.app.objects.Payment;
import com.watudrc.app.objects.UpgradeDevicesItem;
import com.watudrc.app.objects.User;
import com.watudrc.app.prefs.CallTracker;
import com.watudrc.app.prefs.PermissionTracker;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;
import com.watudrc.app.util.FirebaseConfig;
import com.watudrc.app.util.NetworkUtils;
import com.watudrc.app.util.PeopleSyncHelper;
import com.watudrc.app.view.MaterialRippleLayout;
import com.ypyproductions.utils.StringUtils;

import org.infobip.mobile.messaging.CustomAttributeValue;
import org.infobip.mobile.messaging.MobileMessaging;
import org.infobip.mobile.messaging.UserAttributes;
import org.infobip.mobile.messaging.UserIdentity;
import org.infobip.mobile.messaging.chat.InAppChat;
import org.infobip.mobile.messaging.chat.view.InAppChatFragment;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class FragmentChat extends Fragment implements IWhereMyLocationConstants {

    private View parentView;
    private static StoreAndGetFromPrefs storageObject;
    public static ChatActivity parentActivity;
    static Context mContext;
    static Context chatContext;

    RelativeLayout loadingView;

    RelativeLayout btnUserIcon;
    public static FirmaTextView userName;


    public static FirmaTextView userInitials;
    RelativeLayout btnBack, btnCallCC, btnNotifications;

    private FrameLayout chatContainer;

    private TelecomManager telecomManager;
    FirmaTextView tvThrottle;
    int support_throttle_max_initial_attempts = 2;
    int support_throttle_max_attempts_per_day = 5;
    int support_throttle_rapid_tap_cooldown_minutes = 10;
    private static final int CALL_PERMISSION_REQUEST_CODE = 100;
    boolean support_calling_enabled = true;
    private String support_care_number = ADMIN_SUPPORT_PHONE;
    FirebaseConfig remoteConfig;
    FirebaseRemoteConfig mFirebaseConfig;

    private CountDownTimer throttleTimer;

    String isConnectedStatus;

    CallTracker callTracker;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_chat, container, false);
        parentActivity = (ChatActivity) getActivity();
        if (parentActivity != null) {
            mContext = parentActivity.getApplicationContext();
            storageObject = new StoreAndGetFromPrefs(mContext);
        }

        callTracker = new CallTracker(mContext);
        //callTracker.resetCallCount();

        telecomManager = (TelecomManager) mContext.getSystemService(Context.TELECOM_SERVICE);

        loadingView = parentView.findViewById(R.id.loadingItems);
        chatContext = requireContext();

        btnBack = parentView.findViewById(R.id.btnBack);
        btnCallCC = parentView.findViewById(R.id.btnCallCC);
        btnNotifications = parentView.findViewById(R.id.btnNotifications);

        setDefaults();
        refreshCallButtonView();

        try {
            setConfigs();
        } catch (Exception e) {
        }

        btnBack.setOnClickListener(v -> {
            GeneralCommands.logEvent(parentActivity, "click_button_back_fragment_chat", "click_button_back_fragment_chat");
            parentActivity.goBack();

        });


        btnCallCC.setOnClickListener(v -> {
            GeneralCommands.logEvent(parentActivity, "support_click_button_call_fragment_chat", "support_click_button_call_fragment_chat");
            NetworkUtils.checkInternetConnection(parentActivity, isConnected -> {
                if (isConnected) {
                    if (callTracker.canMakeCall() == CallTracker.CallStatus.ALLOWED) {
                        enableDirectCall();
                    } else {
                        showThrottleCallsCoolDown();
                    }
                } else {
                    if (callTracker.canMakeCall() == CallTracker.CallStatus.ALLOWED) {
                        showChatUnAvailableDialog();
                    } else {
                        showThrottleCallsCoolDown();
                    }
                }
            });
        });

        // showCallingDisabledDialog();

        btnNotifications.setOnClickListener(v -> {

            GeneralCommands.logEvent(parentActivity, "click_button_notifications_fragment_chat", "click_button_notifications_fragment_chat");
            try {
                MainActivity.viewPager.setCurrentItem(3, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            parentActivity.goBack();

        });



//        btnNotifications.setOnClickListener(v -> {
//            GeneralCommands.logEvent(requireContext(), "click_button_notifications_fragment_chat", "click_button_notifications_fragment_chat");
//
//            Activity currentActivity = getActivity();
//            if (currentActivity == null) return;
//
//            Intent intent = new Intent(currentActivity, MainActivity.class);
//            intent.putExtra("OPEN_TAB", 3);
//            intent.putExtra(KEY_START_FROM, "notifications");
//
//            // Instantly bring MainActivity to top without transition animation
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                    | Intent.FLAG_ACTIVITY_SINGLE_TOP
//                    | Intent.FLAG_ACTIVITY_NO_ANIMATION);
//
//            currentActivity.startActivity(intent);
//            currentActivity.finish();
//
//            // Disable window animation on exit
//            currentActivity.overridePendingTransition(0, 0);
//        });


        userName = parentView.findViewById(R.id.userName);
        userInitials = parentView.findViewById(R.id.userInitials);

        userName.setText(storageObject.retrieveUserNameFromPrefs());
        try {
            userInitials.setText(GeneralCommands.getUserInitials(storageObject.retrieveUserNameFromPrefs()));
            userName.setText(storageObject.retrieveUserFirstNameFromPrefs());

        } catch (Exception e) {
            e.printStackTrace();
        }

        NetworkUtils.checkInternetConnection(parentActivity, isConnected -> {
            if (!isConnected) {
                showChatUnAvailableDialog();
                //Toast.makeText(parentActivity, "Arrest Network Unaavailability", Toast.LENGTH_SHORT).show();
            }

        });

        GetData ob1 = new GetData();
        ob1.initialize(parentActivity, loadingView);
        ob1.startLoading(mContext.getResources().getString(R.string.pleasewait));
        parentView.post(() -> {
            // Attach Infobip ChatFragment into the container
            if (savedInstanceState == null && getChildFragmentManager() != null) {

                // Use InAppChat API to show chat fragment inside your container


                try {


                    //Add Context Data
                    // Inside your method
                    Gson gson = new Gson();

                    User customer = storageObject.retrieveUserObjectFromPrefs();

                    customer.setName(storageObject.retrieveUserLastNameFromPrefs());
                    customer.setEmail(storageObject.retrieveUserPhoneNumberFromPrefs());
                    customer.setFirstName(storageObject.retrieveUserFirstNameFromPrefs());
                    customer.setLastName(storageObject.retrieveUserLastNameFromPrefs());
                    customer.setDateOfBirth(storageObject.retrieveUserDOBFromPrefs());
                    customer.setGender(storageObject.retrieveUserGenderFromPrefs());
                    customer.setCountryCode("cd");

                    AssetObject currentAsset = storageObject.retrieveCurrentAssetObjectFromPrefs();

                    ArrayList<Payment> transactions = storageObject.retrievePaymentObjectsFromPrefs();

                    if (transactions != null && transactions.size() > 5) {
                        // subList(fromIndex, toIndex) - toIndex is exclusive
                        transactions = new ArrayList<>(transactions.subList(0, 5));
                    }


                    UpgradeDevicesItem upgradeItem = storageObject.retrieveCurrentUpgradeDevicesItemFromPrefs();

                    String currentData = "";
                    DeviceUpgradeObject upgrade = (currentAsset != null) ? currentAsset.getDeviceUpgradeObject() : null;

                    if (upgrade != null && upgrade.getCurrentDevice() != null) {
                        currentData = gson.toJson(upgrade.getCurrentDevice());
                    }


                    JSONObject oauthObj = new JSONObject();
                    try {
                        oauthObj.put("token", storageObject.retrieveTokenFromPrefs());
                        oauthObj.put("apiSecret", storageObject.retrieveApiSecretFromPrefs());
                        oauthObj.put("deviceId", storageObject.retrieveDeviceIDFromPrefs()); // since loanData is a list

                        // Log.d("ZeChat", "ZeOauthData: " + oauthData);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String customerData = gson.toJson(customer);
                    String assetData = gson.toJson(currentAsset);
                    String transactionsData = gson.toJson(transactions);
                    String upgradeData = gson.toJson(upgradeItem);
                    String oauthData = gson.toJson(oauthObj);

                    sendDataForPeople(chatContext, assetData, customerData, transactionsData, oauthData, currentData, upgradeData, ob1);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                createWidget(ob1);


            }

        });

        return parentView;
    }


    private void setDefaults() {
        if (!StringUtils.isStringEmpty(CallTracker.getSupportNumber()))
            support_care_number = CallTracker.getSupportNumber();
        if (CallTracker.getSoftCoolDownLimit() != 0)
            support_throttle_max_initial_attempts = CallTracker.getSoftCoolDownLimit();
        if (CallTracker.getHardCoolDownLimit() != 0)
            support_throttle_max_attempts_per_day = CallTracker.getHardCoolDownLimit();
        if (CallTracker.getSoftCoolDownTimeInMins() != 0)
            support_throttle_rapid_tap_cooldown_minutes = CallTracker.getSoftCoolDownTimeInMins();
    }

    private void refreshCallButtonView() {
        if (CallTracker.getIsCallEnabled() && btnCallCC != null) {
            btnCallCC.setVisibility(View.VISIBLE);
            btnCallCC.setEnabled(true);

        } else {
            assert btnCallCC != null;
            btnCallCC.setVisibility(View.GONE);
            btnCallCC.setEnabled(false);
        }
    }

    private void showThrottleCallsCoolDown() {
        Dialog dialog = new Dialog(parentActivity);
        dialog.setContentView(R.layout.dialog_throttled_calls_cool_down);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RelativeLayout chatWithUs = dialog.findViewById(R.id.chat_with_cc);
        FirmaTextView tvCopyNumber = dialog.findViewById(R.id.tv_copy_number);
        RelativeLayout btnCopyNumber = dialog.findViewById(R.id.btnCopyNumber);
        ImageView ivClose = dialog.findViewById(R.id.iv_close);
        FirmaTextView textCopyNumber = dialog.findViewById(R.id.tvCopyNumber);
        FirmaTextView tvNotifyState = dialog.findViewById(R.id.tv_notify_state);
        MaterialRippleLayout copyRips = dialog.findViewById(R.id.copyRips);
        MaterialRippleLayout chatRips = dialog.findViewById(R.id.chatRips);

        tvCopyNumber.setText(support_care_number);
        textCopyNumber.setText(support_care_number);

        textCopyNumber.setOnClickListener(v -> copyToClipboard(support_care_number));

        NetworkUtils.checkInternetConnection(parentActivity, isConnected -> {
            if (isConnected) {
                GeneralCommands.logEvent(parentActivity, "support_chat_cta_viewed", "support_chat_cta_viewed");

                chatRips.setVisibility(View.VISIBLE);
            } else {

                chatRips.setVisibility(View.GONE);
                copyRips.setVisibility(View.VISIBLE);
                tvNotifyState.setText(R.string.copy_the_number_or_use_another_phone_to_call_customer_care);
                textCopyNumber.setVisibility(View.GONE);
                GeneralCommands.logEvent(parentActivity, "support_chat_unavailable_shown", "support_chat_unavailable_shown");
            }
        });

        btnCopyNumber.setOnClickListener(v -> {
            copyToClipboard(support_care_number);
        });

        tvThrottle = dialog.findViewById(R.id.tv_throttle);

        dialog.show(); // always show dialog first

        ivClose.setOnClickListener(v -> {
            GeneralCommands.logEvent(parentActivity, "support_click_close_dialog_throttled_calls_cool_down", "support_click_close_dialog_throttled_calls_cool_down");

            dialog.dismiss();
            goToPage();
        });

        chatWithUs.setOnClickListener(v -> NetworkUtils.checkInternetConnection(parentActivity, isConnected -> {
            GeneralCommands.logEvent(parentActivity, "support_call_fallback_to_chat", "support_call_fallback_to_chat");

            dialog.dismiss();
            if (!isConnected) {
                Toast.makeText(mContext, R.string.please_turn_on_your_internet, Toast.LENGTH_SHORT).show();
                showChatUnAvailableDialog();
            }
        }));

        // --- Use CallTracker as source of truth ---
        CallTracker.CallStatus status = callTracker.canMakeCall();
        long remainingSeconds;

        if (status == CallTracker.CallStatus.DAILY_LIMIT) {
            remainingSeconds = (callTracker.getMidnightMillis() - System.currentTimeMillis()) / 1000;
        } else {
            remainingSeconds = callTracker.getRemainingCooldownSeconds();
        }

        // If no remaining time, show message
        if (remainingSeconds <= 0) {
            // tvThrottle.setText(R.string.you_can_now_call_again);
            tvThrottle.setVisibility(View.GONE);
            return;
        }

        // Start countdown timer

        if (remainingSeconds <= 0) {
            tvThrottle.setVisibility(View.GONE);
            return;
        }

        // Start and store countdown timer reference
        CountDownTimer timer = new CountDownTimer(remainingSeconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Prevent IllegalStateException if Fragment is detached
                if (!isAdded() || parentActivity == null || parentActivity.isFinishing()) {
                    return;
                }

                long totalSeconds = millisUntilFinished / 1000;
                long hours = totalSeconds / 3600;
                long minutes = (totalSeconds % 3600) / 60;
                long seconds = totalSeconds % 60;

                String timeLeft = String.format("%02d:%02d:%02d", hours, minutes, seconds);
                // Use parentActivity.getString() for extra safety
                String html = String.format(parentActivity.getString(R.string.try_again_in_font_color_04afab_s_font), timeLeft);
                tvThrottle.setText(HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY));
            }

            @Override
            public void onFinish() {
                if (!isAdded() || parentActivity == null || parentActivity.isFinishing()) {
                    return;
                }
                tvThrottle.setText(R.string.you_can_now_call_again);
            }
        };

        timer.start();

        // Cancel timer when dialog is dismissed to prevent memory leaks and background crashes
        dialog.setOnDismissListener(d -> {
            if (timer != null) {
                timer.cancel();
            }
        });

//        new CountDownTimer(remainingSeconds * 1000, 1000) {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                long totalSeconds = millisUntilFinished / 1000;
//                long hours = totalSeconds / 3600;
//                long minutes = (totalSeconds % 3600) / 60;
//                long seconds = totalSeconds % 60;
//
//                String timeLeft = String.format("%02d:%02d:%02d", hours, minutes, seconds);
//                String html = String.format(getString(R.string.try_again_in_font_color_04afab_s_font), timeLeft);
//                tvThrottle.setText(HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY));
//            }
//
//            @Override
//            public void onFinish() {
//                tvThrottle.setText(R.string.you_can_now_call_again);
//            }
//        }.start();
    }

    private void showEnableCallingSoftNudge() {

        GeneralCommands.logEvent(parentActivity, "support_call_permission_explainer_shown", "support_call_permission_explainer_shown");

        Dialog dialog = new Dialog(parentActivity);
        dialog.setContentView(R.layout.dialog_enable_calling_soft);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView ivClose = dialog.findViewById(R.id.iv_close);

        ivClose.setOnClickListener(v -> {
            GeneralCommands.logEvent(parentActivity, "support_lick_close_dialog_enable_calling_soft_nudge", "support_click_close_dialog_enable_calling_soft_nudge");

            dialog.dismiss();
            goToPage();
        });

        RelativeLayout chatWithUs = dialog.findViewById(R.id.chat_with_cc);
        RelativeLayout enableCallPermissions = dialog.findViewById(R.id.enable_call_permissions);
        FirmaTextView tvLiveChat = dialog.findViewById(R.id.tvLiveChat);
        MaterialRippleLayout chatRips = dialog.findViewById(R.id.chatRips);


        NetworkUtils.checkInternetConnection(parentActivity, isConnected -> {
            if (isConnected) {
                GeneralCommands.logEvent(parentActivity, "support_chat_cta_viewed", "support_chat_cta_viewed");

                tvLiveChat.setVisibility(View.VISIBLE);
                chatRips.setVisibility(View.VISIBLE);

            } else {
                tvLiveChat.setVisibility(View.GONE);
                chatRips.setVisibility(View.GONE);
            }
        });

        chatWithUs.setOnClickListener(v -> NetworkUtils.checkInternetConnection(parentActivity, isConnected -> {
            GeneralCommands.logEvent(parentActivity, "support_click_chat_with_customer_care_dialog_enable_calling_soft_nudge", "support_click_chat_with_customer_care_dialog_enable_calling_soft_nudge");
            PermissionTracker.incrementCallPermissionRequest(parentActivity);
            if (isConnected) {
                dialog.dismiss();

            } else {
                Toast.makeText(mContext, R.string.please_turn_on_your_internet, Toast.LENGTH_SHORT).show();
                showChatUnAvailableDialog();
            }
        }));

        enableCallPermissions.setOnClickListener(v -> {
            GeneralCommands.logEvent(parentActivity, "support_permission_soft_nudge_accepted", "support_permission_soft_nudge_accepted");

            PermissionTracker.incrementCallPermissionRequest(parentActivity);
            dialog.dismiss();
            if (PermissionTracker.getCallPermissionRequestCount(parentActivity) == 1) {
                showSettingsDialog();
            } else {
                requestCallPermission();
            }


        });
        dialog.show();
    }


    private void showEnableCallingHardNudge() {
        Dialog dialog = new Dialog(parentActivity);
        dialog.setContentView(R.layout.dialog_enable_calling_hard);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView ivClose = dialog.findViewById(R.id.iv_close);

        ivClose.setOnClickListener(v -> {
            GeneralCommands.logEvent(parentActivity, "support_click_close_dialog_enable_calling_hard_nudge", "support_click_close_dialog_enable_calling_hard_nudge");

            dialog.dismiss();
            goToPage();
        });

        MaterialRippleLayout chatRips = dialog.findViewById(R.id.chatRips);
        MaterialRippleLayout copyRips = dialog.findViewById(R.id.copyRips);
        MaterialRippleLayout enableCallRips = dialog.findViewById(R.id.enableCallRips);
        RelativeLayout btnChatWithUs = dialog.findViewById(R.id.btnChatWithUs);
        RelativeLayout enableCallPermissions = dialog.findViewById(R.id.btnEnableCallPermission);
        RelativeLayout btnCopyNumber = dialog.findViewById(R.id.btnCopyNumber);
        FirmaTextView tvCopyNumber = dialog.findViewById(R.id.tvCopyNumber);
        FirmaTextView tvCopyNumber2 = dialog.findViewById(R.id.tvCopyNumber2);
        tvCopyNumber.setText(support_care_number);
        tvCopyNumber2.setText(support_care_number);

        NetworkUtils.checkInternetConnection(parentActivity, isConnected -> {
            if (isConnected) {
                GeneralCommands.logEvent(parentActivity, "support_chat_cta_viewed", "support_chat_cta_viewed");
                tvCopyNumber.setVisibility(View.GONE);
                enableCallRips.setVisibility(View.VISIBLE);
                chatRips.setVisibility(View.VISIBLE);
                copyRips.setVisibility(View.GONE);

            } else {
                tvCopyNumber.setVisibility(View.VISIBLE);
                enableCallRips.setVisibility(View.VISIBLE);
                chatRips.setVisibility(View.GONE);
                copyRips.setVisibility(View.GONE);
            }
        });

        enableCallPermissions.setOnClickListener(v -> {
            GeneralCommands.logEvent(parentActivity, "support_permission_hard_nudge_accepted", "support_permission_hard_nudge_accepted");
            PermissionTracker.incrementCallPermissionRequest(parentActivity);
            dialog.dismiss();
            if (PermissionTracker.getCallPermissionRequestCount(parentActivity) >= 2) {
                showSettingsDialog();
            } else {
                requestCallPermission();
            }
        });

        btnCopyNumber.setOnClickListener(v -> {
            PermissionTracker.incrementCallPermissionRequest(parentActivity);
            copyToClipboard(support_care_number);
        });


        btnChatWithUs.setOnClickListener(v -> {

            GeneralCommands.logEvent(parentActivity, "support_click_chat_with_customer_care_dialog_enable_calling_hard_nudge", "support_click_chat_with_customer_care_dialog_enable_calling_hard_nudge");

            PermissionTracker.incrementCallPermissionRequest(parentActivity);
            NetworkUtils.checkInternetConnection(parentActivity, isConnected -> {
                if (isConnected) {
                    dialog.dismiss();

                } else {
                    showChatUnAvailableDialog();
                }
            });

        });

        tvCopyNumber.setOnClickListener(v -> {
            String textToCopy = support_care_number;
            copyToClipboard(textToCopy);
        });
        dialog.show();
    }

    private void showChatUnAvailableDialog() {
        if (parentActivity == null || parentActivity.isFinishing() || parentActivity.isDestroyed()) {
            return;
        }

        Dialog dialog = new Dialog(parentActivity);
        dialog.setContentView(R.layout.dialog_chat_un_available);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView ivClose = dialog.findViewById(R.id.iv_close);
        RelativeLayout callCustomerCare = dialog.findViewById(R.id.call_cc);
        FirmaTextView copyNumber = dialog.findViewById(R.id.tv_copy_number);

        ivClose.setOnClickListener(v -> {
            GeneralCommands.logEvent(parentActivity, "support_click_close_dialog_chat_un_available", "support_click_close_dialog_chat_un_available");
            dialog.dismiss();
            goToPage();
        });

        callCustomerCare.setOnClickListener(v -> {
            //Log CTA Tapped HERE
            GeneralCommands.logEvent(parentActivity, "support_call_cta_tapped", "support_call_cta_tapped");

            dialog.dismiss();
            if (hasCallPermission()) {
                CallTracker.CallStatus status = callTracker.canMakeCall();
                if (status == CallTracker.CallStatus.ALLOWED) {
                    callTracker.recordCall();
                    makeSelfManagedCall("tel:" + support_care_number);
                } else {
                    showThrottleCallsCoolDown();
                }
            } else {
                if (PermissionTracker.getCallPermissionRequestCount(parentActivity) >= 2) {
                    showDialogOpenSetUpWizard();
                } else if (PermissionTracker.getCallPermissionRequestCount(parentActivity) == 1) {
                    PermissionTracker.incrementCallPermissionRequest(parentActivity);
                    showEnableCallingHardNudge();
                } else {
                    PermissionTracker.incrementCallPermissionRequest(parentActivity);
                    showEnableCallingSoftNudge();
                }
            }
        });

        copyNumber.setText(support_care_number);
        copyNumber.setOnClickListener(v -> copyToClipboard(copyNumber.getText().toString()));

        dialog.show();

        //Log CTA Viewed HERE (when dialog is shown)
        GeneralCommands.logEvent(parentActivity, "support_call_cta_viewed", "support_call_cta_viewed");
    }


    private void copyToClipboard(String textToCopy) {
        GeneralCommands.logEvent(parentActivity, "support_copy_number_tapped", "support_copy_number_tapped");

        ClipboardManager clipboard = getSystemService(mContext, ClipboardManager.class);
        ClipData clip = ClipData.newPlainText(getString(R.string.copied_text), textToCopy);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
        }
    }

    private void enableDirectCall() {
        Dialog dialog = new Dialog(parentActivity);
        dialog.setContentView(R.layout.dialog_no_internet);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView ivClose = dialog.findViewById(R.id.iv_close);
        RelativeLayout chatWithUs = dialog.findViewById(R.id.chat_with_cc);
        RelativeLayout callCustomerCare = dialog.findViewById(R.id.call_cc);
        FirmaTextView copyNumber = dialog.findViewById(R.id.tv_copy_number);

        ivClose.setOnClickListener(v -> {
            dialog.dismiss();
            goToPage();
        });

        chatWithUs.setOnClickListener(v -> NetworkUtils.checkInternetConnection(parentActivity, isConnected -> {
            GeneralCommands.logEvent(parentActivity, "support_chat_cta_clicked", "support_chat_cta_clicked");

            dialog.dismiss();
            if (!isConnected) {
                Toast.makeText(mContext, R.string.please_turn_on_your_internet, Toast.LENGTH_SHORT).show();
                showChatUnAvailableDialog();
            }
        }));

        NetworkUtils.checkInternetConnection(parentActivity, isConnected -> {
            if (isConnected) {
                GeneralCommands.logEvent(parentActivity, "support_chat_cta_viewed", "support_chat_cta_viewed");
                chatWithUs.setVisibility(View.VISIBLE);
            } else {
                chatWithUs.setVisibility(View.GONE);
            }
        });

        callCustomerCare.setOnClickListener(v -> {
            //Log CTA Tapped HERE
            GeneralCommands.logEvent(getContext(), "support_call_cta_tapped", "support_call_cta_tapped");

            if (hasCallPermission()) {
                CallTracker.CallStatus status = callTracker.canMakeCall();
                if (status == CallTracker.CallStatus.ALLOWED) {
                    callTracker.recordCall();
                    makeSelfManagedCall("tel:" + support_care_number);
                } else {
                    showThrottleCallsCoolDown();
                }
                GeneralCommands.logEvent(getContext(), "support_call_throttle_checked", "support_call_throttle_checked");
            } else {
                dialog.dismiss();
                if (PermissionTracker.getCallPermissionRequestCount(parentActivity) == 1) {
                    showEnableCallingHardNudge();
                } else if (PermissionTracker.getCallPermissionRequestCount(parentActivity) >= 2 && !hasCallPermission()) {
                    showDialogOpenSetUpWizard();
                } else {
                    showEnableCallingSoftNudge();
                }
            }
            dialog.dismiss();
        });

        copyNumber.setOnClickListener(v -> copyToClipboard(copyNumber.getText().toString()));

        dialog.show();

        //Log CTA Viewed HERE (when dialog is shown)
        GeneralCommands.logEvent(parentActivity, "support_call_cta_viewed", "support_call_cta_viewed");
    }

    private void showDialogOpenSetUpWizard() {
        Dialog dialog = new Dialog(parentActivity);
        dialog.setContentView(R.layout.dialog_open_set_up_wizard);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RelativeLayout chatWithUs = dialog.findViewById(R.id.chat_with_cc);
        RelativeLayout openSettings = dialog.findViewById(R.id.open_settings);
        ImageView ivClose = dialog.findViewById(R.id.iv_close);
        FirmaTextView tvCopyNumber = dialog.findViewById(R.id.tvCopyNumber);
        MaterialRippleLayout chatRips = dialog.findViewById(R.id.chatRips);
        tvCopyNumber.setText(support_care_number);

        ivClose.setOnClickListener(v -> {
            GeneralCommands.logEvent(parentActivity, "click_close_dialog_open_set_up_wizard", "click_close_dialog_open_set_up_wizard");
            dialog.dismiss();
            goToPage();
        });

        NetworkUtils.checkInternetConnection(parentActivity, isConnected -> {
            if (isConnected) {
                tvCopyNumber.setVisibility(View.GONE);
            } else {
                chatRips.setVisibility(View.GONE);
                tvCopyNumber.setVisibility(View.VISIBLE);
            }

        });

        chatWithUs.setOnClickListener(v -> {
            NetworkUtils.checkInternetConnection(parentActivity, isConnected -> {
                if (isConnected) {
                    GeneralCommands.logEvent(parentActivity, "click_chat_btn_internet_dialog_open_set_up_wizard", "click_chat_btn_internet_dialog_open_set_up_wizard");
                    dialog.dismiss();
                } else {
                    GeneralCommands.logEvent(parentActivity, "click_chat_btn_no_internet_dialog_open_set_up_wizard", "click_chat_btn_no_internet_dialog_open_set_up_wizard");

                    dialog.dismiss();
                    showChatUnAvailableDialog();
                }
            });
        });

        tvCopyNumber.setOnClickListener(v -> {

            copyToClipboard(support_care_number);
        });

        openSettings.setOnClickListener(v -> {
            GeneralCommands.logEvent(parentActivity, "click_open_settings_dialog_open_set_up_wizard", "click_open_settings_dialog_open_set_up_wizard");
            // goToPermissionsPage();
            openAppSettings();
        });


        dialog.show();
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", parentActivity.getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    private void createWidget(GetData ob1) {

        FragmentManager frag = getChildFragmentManager();

        InAppChat inAppChat = InAppChat.getInstance(chatContext);
        chatContainer = parentActivity.findViewById(R.id.main_fragment);

        // --- 1. CONFIGURATION ---
        // Configure the singleton before showing the fragment
        inAppChat.activate(); // Important: Syncs config with server

        openChat();

        // --- 2. BACK BUTTON HANDLING ---
        // Register the callback to intercept the back button
        parentActivity.getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Check if the chat container is currently visible
                if (chatContainer.getVisibility() == View.VISIBLE) {

                    // >> YOUR CUSTOM LOGIC HERE <<
                    // e.g. parentActivity.goBack();
                    closeChat();
                } else {
                    // If chat is not open, perform standard back action
                    setEnabled(false);
                    parentActivity.getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });

        ob1.finishLoading();
    }

    private void sendDataForPeople(Context chatContext, String assetData, String customerData, String transactionData, String oauthData, String currentDeviceData, String upgradeData, GetData ob1) {

        String installationId = MobileMessaging.getInstance(chatContext).getInstallation().getApplicationUserId();

        User obj = storageObject.retrieveUserObjectFromPrefs();

        try {
            // --- Build identity ---
            UserIdentity identity = new UserIdentity();
            identity.setExternalUserId(obj.getPhone_number()); // use phone as external ID
//            identity.setExternalUserId(obj.getPhone_number()+"_"+ UUID.randomUUID()); // use phone as external ID
            identity.setField("name", obj.getFirstName() + " " + obj.getLastName());
            identity.setField("phone_number", obj.getPhone_number());
            identity.setField("entry_point", parentActivity.mStartFrom);

            // --- Build attributes ---
            UserAttributes attributes = new UserAttributes();
            attributes.setFirstName(obj.getFirstName());
            attributes.setLastName(obj.getLastName());
            attributes.setGender(UserAttributes.Gender.Male);


            Log.d("ZeAsse", "ZeAsset" + assetData);
            Log.d("ZeAsseTran", "ZeAsseTran" + transactionData);


            // --- Custom attributes ---
            Map<String, CustomAttributeValue> custom = new HashMap<>();
            custom.put("customerData", new CustomAttributeValue(customerData));
            custom.put("assetData", new CustomAttributeValue(assetData));
            custom.put("transactionData", new CustomAttributeValue(transactionData));
            custom.put("oauthData", new CustomAttributeValue(oauthData));
            custom.put("language", new CustomAttributeValue(storageObject.retrieveLanguageFromPrefs()));
            custom.put("entry_point", new CustomAttributeValue(parentActivity.mStartFrom));
            if (parentActivity.mStartFrom.equalsIgnoreCase("jijenge")) {
                custom.put("current_device", new CustomAttributeValue(currentDeviceData));
                custom.put("upgrade_device", new CustomAttributeValue(upgradeData));

            }

            attributes.setCustomAttributes(custom);

            Log.d("ZeChat", "Attempting personalize for user: " + obj.getPhone_number());


            PeopleSyncHelper.syncUser(chatContext, identity, attributes, new PeopleSyncHelper.DepersonalizeListener() {
                @Override
                public void onDepersonalizeSuccess() {
                    Log.d("ZeChat", "Personalization successful for user: " + obj.getPhone_number());
                }

                @Override
                public void onDepersonalizeFailed(String error) {
                    Log.e("ZeChat", "Personalization failed for user: " + obj.getPhone_number() + " error: " + error);


                    String errorMsg = error.toLowerCase();

                    if (errorMsg != null && errorMsg.contains("deleted")) {
                        Log.w("ZeChat", "⚠️ Person deleted on backend — depersonalizing and retrying...");

                    }


                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (this.isVisible() && isVisibleToUser) {
            GeneralCommands.logEvent(parentActivity, "view_page", "view_page");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        GeneralCommands.logEvent(parentActivity, "view_live_chat_page", "view_live_chat_page");

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void goToPage() {
        Intent mIntent = new Intent(parentActivity, MainActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
        GeneralCommands.goToPage(parentActivity, mIntent, false);
    }

    public void goToPermissionsPage() {
        Intent mIntent = new Intent(parentActivity, PermissionsActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
        GeneralCommands.goToPage(parentActivity, mIntent, false);
    }

    private ActionBar getActivityActionBar() {
        Activity activity = requireActivity();
        if (activity instanceof AppCompatActivity) {
            return ((AppCompatActivity) activity).getSupportActionBar();
        }
        return null;
    }

    private void hideActionBar() {
        ActionBar activityActionBar = getActivityActionBar();
        if (activityActionBar != null) {
            activityActionBar.hide();
        }
    }

    private void showActionBar() {
        ActionBar activityActionBar = getActivityActionBar();
        if (activityActionBar != null) {
            activityActionBar.show();
        }
    }

    private void logAllViews(View root) {
        if (root == null) return;

        int id = root.getId();
        String idName = "";
        if (id != View.NO_ID) {
            try {
                idName = root.getResources().getResourceEntryName(id);
            } catch (Resources.NotFoundException e) {
                idName = "unknown";
            }
        } else {
            idName = "no-id";
        }

        Log.d("ZeChatView", "View: " + root.getClass().getSimpleName() + " id=" + id + " name=" + idName);

        if (root instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) root;
            for (int i = 0; i < group.getChildCount(); i++) {
                logAllViews(group.getChildAt(i));
            }
        }
    }

    public void openChat() {
        // Create the fragment instance directly
        // Note: Use 'new InAppChatFragment()' for SDK 14.x
        Fragment chatFragment = new InAppChatFragment();
        GeneralCommands.logEvent(getContext(), "support_chat_session_started", "support_chat_session_started");

        FragmentTransaction transaction = parentActivity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.chat_container, chatFragment, "CHAT_TAG");
        transaction.commit();

        chatContainer.setVisibility(View.VISIBLE);

        // Register a callback to listen for when the Chat Fragment is ready to hide the toolbar
        parentActivity.getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
            @Override
            public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
                // Only act if this is the InfoBip Chat Fragment
                if (f instanceof InAppChatFragment) {
                    hideToolbarInView(v);
                }
            }
        }, true);
    }

    public void closeChat() {
        Fragment chatFragment = parentActivity.getSupportFragmentManager().findFragmentByTag("CHAT_TAG");
        if (chatFragment != null) {
            parentActivity.getSupportFragmentManager().beginTransaction().remove(chatFragment).commit();
        }
        chatContainer.setVisibility(View.GONE);
        parentActivity.goBack();

        // Re-enable the back button callback for next time
        // (Not strictly necessary as logic handles visibility check, but good practice)
    }

    // Helper method to find and hide the Toolbar recursively
    private void hideToolbarInView(View view) {
        // 1. If this view is a Toolbar, hide it
        if (view instanceof Toolbar) {
            view.setVisibility(View.GONE);
            return;
        }

        // 2. If it's a container (Layout), check its children
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                hideToolbarInView(group.getChildAt(i));
            }
        }
    }

    private boolean hasCallPermission() {
        return ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        // Call Permission Listener
//        if (requestCode == CALL_PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                GeneralCommands.logEvent(parentActivity, "support_call_permission_granted", "support_call_permission_granted");
//
//            } else {
//                GeneralCommands.logEvent(parentActivity, "support_call_permission_denied", "support_call_permission_denied");
//
//            }
//        }
//
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CALL_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GeneralCommands.logEvent(parentActivity, "support_call_permission_granted", "support_call_permission_granted");

                // Log tapped event and auto-initiate call after permission granted:
                GeneralCommands.logEvent(parentActivity, "support_call_cta_tapped", "support_call_cta_tapped");
                makeSelfManagedCall("tel:" + support_care_number);
            } else {
                GeneralCommands.logEvent(parentActivity, "support_call_permission_denied", "support_call_permission_denied");
            }
        }
    }


    private void requestCallPermission() {
        GeneralCommands.logEvent(parentActivity, "support_call_permission_requested", "support_call_permission_requested");

        ActivityCompat.requestPermissions(
                parentActivity,
                new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION_REQUEST_CODE

        );
    }

    private void makeSelfManagedCall(String number) {
        // Defensively check permission first (Safety net)
        if (!hasCallPermission()) {
            Toast.makeText(parentActivity, R.string.call_permission_is_required_please_enable_it_in_app_settings, Toast.LENGTH_SHORT).show();
            return;
        }

        // Safe UI Loading State Update
        if (loadingView != null) {
            RelativeLayout loadingActive = loadingView.findViewById(R.id.loadingActive);
            if (loadingActive != null) {
                loadingActive.setVisibility(View.VISIBLE);
            }
        }

        Uri uri = Uri.parse(number);
        Bundle extras = new Bundle();
        extras.putBoolean(TelecomManager.EXTRA_START_CALL_WITH_SPEAKERPHONE, false);

        try {
            //Log Intent Start right before the OS takes over
//            GeneralCommands.logEvent(parentActivity, "support_call_intent_started_experiment", "support_call_intent_started_experiment");
            GeneralCommands.logEvent(parentActivity, "support_call_intent_started", "support_call_intent_started");
            telecomManager.placeCall(uri, extras);

        } catch (Exception e) {
            // 4. Log Intent Failure if the OS blocks or drops the call execution
            GeneralCommands.logEvent(parentActivity, "support_call_intent_failed", "support_call_intent_failed");
            Toast.makeText(mContext, getString(R.string.call_failed) + e.getMessage(), Toast.LENGTH_SHORT).show();

            // Hide loading indicator if the call immediately fails to launch
            if (loadingView != null) {
                RelativeLayout loadingActive = loadingView.findViewById(R.id.loadingActive);
                if (loadingActive != null) {
                    loadingActive.setVisibility(View.GONE);
                }
            }
        }
    }

    private void showSettingsDialog() {
        Toast.makeText(parentActivity, R.string.call_permission_is_required_please_enable_it_in_app_settings, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                parentActivity.getPackageName(),
                null);

        intent.setData(uri);
        parentActivity.startActivity(intent);
    }

    public void setConfigs() {
        //Remote Configs
        remoteConfig = new FirebaseConfig();
        mFirebaseConfig = remoteConfig.getConfigs();
        mFirebaseConfig.fetchAndActivate().addOnCompleteListener(parentActivity, new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    Log.d("ZeFetchSuccess", "ZeFetchSuccess");
                    boolean updated = task.getResult();

                    support_throttle_max_initial_attempts = (int) mFirebaseConfig.getLong("support_throttle_max_initial_attempts");
                    CallTracker.saveSoftCoolDownLimit(support_throttle_max_initial_attempts);
                    support_throttle_max_attempts_per_day = (int) mFirebaseConfig.getLong("support_throttle_max_attempts_per_day");
                    CallTracker.saveHardCoolDownLimit(support_throttle_max_attempts_per_day);
                    support_throttle_rapid_tap_cooldown_minutes = (int) mFirebaseConfig.getLong("support_throttle_rapid_tap_cooldown_minutess");
                    CallTracker.saveSoftCoolDownTimeInMins(support_throttle_rapid_tap_cooldown_minutes);
                    support_calling_enabled = mFirebaseConfig.getBoolean("support_calling_enabled");
                    CallTracker.saveIsCallEnabled(support_calling_enabled);
                    support_care_number = mFirebaseConfig.getString("support_care_number");
                    CallTracker.saveSupportNumber(support_care_number);


                    // Toast.makeText(mContext, "Remote Configs Data : Soft Cool Down Limit: " +support_throttle_max_initial_attempts+"  Hard Cool Down Limit: "+support_throttle_max_attempts_per_day+"  Soft Cool Down Time Limit: "+support_throttle_rapid_tap_cooldown_minutes+" Call Number "+support_care_number, Toast.LENGTH_SHORT).show();
                    Log.d("Remote Configs Data: ", "Remote Configs Data : Soft Cool Down Limit: " + support_throttle_max_initial_attempts + "  Hard Cool Down Limit: " + support_throttle_max_attempts_per_day + "  Soft Cool Down Time Limit: " + support_throttle_rapid_tap_cooldown_minutes + " Call Number " + support_care_number);


                    refreshCallButtonView();


                } else {
                    Log.d("ZeFetchFailed", "ZeFetchFailed");
                }

            }
        });

        mFirebaseConfig.addOnConfigUpdateListener(new ConfigUpdateListener() {
            @Override
            public void onUpdate(ConfigUpdate configUpdate) {
                Log.d("ZeConfigs", "Updated keys: " + configUpdate.getUpdatedKeys());


                mFirebaseConfig.activate().addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                        support_throttle_max_initial_attempts = (int) mFirebaseConfig.getLong("support_throttle_max_initial_attempts");
                        CallTracker.saveSoftCoolDownLimit(support_throttle_max_initial_attempts);
                        support_throttle_max_attempts_per_day = (int) mFirebaseConfig.getLong("support_throttle_max_attempts_per_day");
                        CallTracker.saveHardCoolDownLimit(support_throttle_max_attempts_per_day);
                        support_throttle_rapid_tap_cooldown_minutes = (int) mFirebaseConfig.getLong("support_throttle_rapid_tap_cooldown_minutess");
                        CallTracker.saveSoftCoolDownTimeInMins(support_throttle_rapid_tap_cooldown_minutes);
                        support_calling_enabled = mFirebaseConfig.getBoolean("support_calling_enabled");
                        CallTracker.saveIsCallEnabled(support_calling_enabled);
                        support_care_number = mFirebaseConfig.getString("support_care_number");
                        CallTracker.saveSupportNumber(support_care_number);

                        refreshCallButtonView();

                    }
                });
            }

            @Override
            public void onError(FirebaseRemoteConfigException error) {
                Log.w("ZeConfigs", "Config update error with code: " + error.getCode(), error);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (throttleTimer != null) {
            throttleTimer.cancel();
            throttleTimer = null;
        }
    }

}

