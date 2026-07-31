package com.watudrc.app.fragments;

import static com.watudrc.app.MainActivity.goToChatPage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.watudrc.app.GeneralCommands;
import com.watudrc.app.MainActivity;
import com.watudrc.app.R;
import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;
import com.watudrc.app.view.MaterialRippleLayout;

import org.json.JSONObject;

public class FragmentResources extends Fragment implements IWhereMyLocationConstants {

    private View parentView;

    private static StoreAndGetFromPrefs storageObject;
    public static MainActivity parentActivity;
    static Context mContext;

    RelativeLayout loadingView;

    RelativeLayout btnPhoneUnlock, btnDeviceStolen, btnReplacement, btnReturns, btnRecovery, btnServiceCenter, btnCommonIssues, btnChatWithUs, btnPrepaymentProcess;

    MaterialRippleLayout phoneUnlockRips,
            deviceStolenRips,
            replacementProcessRips,
            returnsRips,
            recoveryProcessRips,
            serviceCenterRips,
            commonAppIssuesRips;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_resources, container, false);
        parentActivity = (MainActivity) getActivity();
        mContext = parentActivity.getApplicationContext();
        storageObject = new StoreAndGetFromPrefs(mContext);
        loadingView = parentView.findViewById(R.id.loadingItems);

        // Find clickable elements (buttons inside ripples)
        btnPhoneUnlock = parentView.findViewById(R.id.btnPhoneUnlock);
        btnDeviceStolen = parentView.findViewById(R.id.btnDeviceStolen);
        btnReplacement = parentView.findViewById(R.id.btnReplacement);
        btnReturns = parentView.findViewById(R.id.btnReturns);
        btnRecovery = parentView.findViewById(R.id.btnRecovery);
        btnServiceCenter = parentView.findViewById(R.id.btnServiceCenter);
        btnCommonIssues = parentView.findViewById(R.id.btnCommonIssues);
        btnChatWithUs = parentView.findViewById(R.id.btnChatWithUs);


        // Find ripple layouts
        phoneUnlockRips = parentView.findViewById(R.id.phoneUnlockRips);
        deviceStolenRips = parentView.findViewById(R.id.deviceStolenRips);
        replacementProcessRips = parentView.findViewById(R.id.replacementProcessRips);
        returnsRips = parentView.findViewById(R.id.returnsRips);
        recoveryProcessRips = parentView.findViewById(R.id.recoveryProcessRips);
        serviceCenterRips = parentView.findViewById(R.id.serviceCenterRips);
        commonAppIssuesRips = parentView.findViewById(R.id.commonAppIssuesRips);

        View btnBack = parentView.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> requireActivity().onBackPressed());


//        toggleButtonsVisibility();

        btnChatWithUs.setOnClickListener(v -> {
            GeneralCommands.logEvent(getContext(), "support_hub_opened_resource_center", "support_hub_opened_resource_center");
            goToChatPage("fragment_resources");
        });


        // Set click listeners for buttons
        btnPhoneUnlock.setOnClickListener(v -> {
            GeneralCommands.logEvent(parentActivity, "click_resources_phone_unlock", "click_resources_phone_unlock");
            switchFragment(new FragmentPhoneUnlock());
        });
        btnDeviceStolen.setOnClickListener(v -> {
            GeneralCommands.logEvent(parentActivity, "click_resources_device_stolen", "click_resources_device_stolen");
            switchFragment(new FragmentDeviceStolen());
        });
        btnReplacement.setOnClickListener(v -> {
            GeneralCommands.logEvent(parentActivity, "click_resources_replacement", "click_resources_replacement");
            switchFragment(new FragmentReplacementProcess());
        });

        btnRecovery.setOnClickListener(v -> {
            GeneralCommands.logEvent(parentActivity, "click_resources_recovery", "click_resources_recovery");
            switchFragment(new FragmentRecoveryProcess());
        });
        btnReturns.setOnClickListener(v -> {
            GeneralCommands.logEvent(parentActivity, "click_resources_returns", "click_resources_returns");
            switchFragment(new FragmentReturns());
        });
        btnServiceCenter.setOnClickListener(v -> {
            GeneralCommands.logEvent(parentActivity, "click_resources_service_center", "click_resources_service_center");
            switchFragment(new FragmentServiceCenter());
        });

//        btnPrepaymentProcess.setOnClickListener(v -> {
//            GeneralCommands.logEvent(parentActivity, "click_resources_prepayment_process", "click_resources_prepayment_process");
//            switchFragment(new FragmentPrepaymentProcess());
//
//        });
        btnCommonIssues.setOnClickListener(v -> {
            GeneralCommands.logEvent(parentActivity, "click_resources_common_issues", "click_resources_common_issues");
            switchFragment(new FragmentFAQsResources());
        });

        return parentView;
    }

    private void toggleButtonsVisibility() {
        try {
            JSONObject supportConfig = storageObject.retrieveSupportPageConfigFromPrefs();

            phoneUnlockRips.setVisibility(supportConfig.optBoolean("phone_unlock", false) ? View.VISIBLE : View.GONE);
            deviceStolenRips.setVisibility(supportConfig.optBoolean("device_stolen", false) ? View.VISIBLE : View.GONE);
            replacementProcessRips.setVisibility(supportConfig.optBoolean("replacement", false) ? View.VISIBLE : View.GONE);
            returnsRips.setVisibility(supportConfig.optBoolean("returns", false) ? View.VISIBLE : View.GONE);
            recoveryProcessRips.setVisibility(supportConfig.optBoolean("recovery", false) ? View.VISIBLE : View.GONE);
            serviceCenterRips.setVisibility(supportConfig.optBoolean("service_center", false) ? View.VISIBLE : View.GONE);
            commonAppIssuesRips.setVisibility(supportConfig.optBoolean("common_app_issues", false) ? View.VISIBLE : View.GONE);

        } catch (Exception e) {
            Log.e("ZeSupport", "Invalid JSON config: " + e.getMessage(), e);
        }
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

    @Override
    public void onStart() {
        super.onStart();
    }

    public void goToPage() {
        Intent mIntent = new Intent(parentActivity, MainActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
        GeneralCommands.goToPage(parentActivity, mIntent, false);
    }

    private void switchFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }
}
