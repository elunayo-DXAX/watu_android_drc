package com.watutz.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.watutz.app.GeneralCommands;
import com.watutz.app.MainActivity;
import com.watutz.app.R;
import com.watutz.app.constant.IWhereMyLocationConstants;
import com.watutz.app.prefs.StoreAndGetFromPrefs;
import com.watutz.app.view.MaterialRippleLayout;

import org.json.JSONObject;

public class FragmentResources extends Fragment implements IWhereMyLocationConstants {

    private View parentView;

    private static StoreAndGetFromPrefs storageObject;
    public static MainActivity parentActivity;
    static Context mContext;

    RelativeLayout loadingView;

    RelativeLayout btnPhoneUnlock, btnDeviceStolen, btnReplacement, btnReturns, btnRecovery, btnServiceCenter, btnCommonIssues;


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

        // Set click listeners for buttons
        btnPhoneUnlock.setOnClickListener(v -> {
            switchFragment(new FragmentPhoneUnlock());
        });
        btnDeviceStolen.setOnClickListener(v -> {
            switchFragment(new FragmentDeviceStolen());
        });
        btnReplacement.setOnClickListener(v -> {
            switchFragment(new FragmentReplacementProcess());
        });

        btnRecovery.setOnClickListener(v -> {
            switchFragment(new FragmentRecoveryProcess());
        });
        btnReturns.setOnClickListener(v -> {
            switchFragment(new FragmentReturns());
        });
        btnServiceCenter.setOnClickListener(v -> {
            switchFragment(new FragmentServiceCenter());
        });
        btnCommonIssues.setOnClickListener(v -> {
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
