package com.watudrc.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.watudrc.app.DocumentsActivity;
import com.watudrc.app.GeneralCommands;
import com.watudrc.app.LevelUpActivity;
import com.watudrc.app.MainActivity;
import com.watudrc.app.R;
import com.watudrc.app.UpgradeActivity;
import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.font.FirmaTextView;
import com.watudrc.app.objects.AssetObject;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;
import com.watudrc.app.view.MaterialRippleLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class FragmentLevelUp extends Fragment implements IWhereMyLocationConstants {

    public static LevelUpActivity parentActivity;
    static Context mContext;
    private static StoreAndGetFromPrefs storageObject;
    RelativeLayout loadingView;

    RelativeLayout btnUserIcon;
    public static FirmaTextView userName;
    public static FirmaTextView userInitials;
    RelativeLayout btnNotifications;
    RelativeLayout btnCheckProgress, btnUpgradeNow, btnUnlock;
    RelativeLayout upgradeLockedView, upgradeUnlockedView;
    AssetObject currentAssetObject;
    MaterialRippleLayout ownRips;
    FirmaTextView ownText;
    RelativeLayout seekView;
    SeekBar seekBar;
    ImageView ownIcon;
    String assetName = "";
    FirmaTextView upgradeDeviceText;
    private View parentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_level_up, container, false);
        parentActivity = (LevelUpActivity) getActivity();
        mContext = parentActivity.getApplicationContext();
        storageObject = new StoreAndGetFromPrefs(mContext);
        loadingView = parentView.findViewById(R.id.loadingItems);

        btnCheckProgress = parentView.findViewById(R.id.btnCheckProgress);
        btnUpgradeNow = parentView.findViewById(R.id.btnUpgradeNow);

        btnUnlock = parentView.findViewById(R.id.btnUnlock);

        upgradeLockedView = parentView.findViewById(R.id.upgradeLockedView);
        upgradeUnlockedView = parentView.findViewById(R.id.upgradeUnlockedView);

        upgradeDeviceText = parentView.findViewById(R.id.upgradeDeviceText);


        userName = parentView.findViewById(R.id.userName);
        userInitials = parentView.findViewById(R.id.userInitials);

        userName.setText(storageObject.retrieveUserNameFromPrefs());
        try{
            userInitials.setText(GeneralCommands.getUserInitials(storageObject.retrieveUserNameFromPrefs()));
            userName.setText(storageObject.retrieveUserFirstNameFromPrefs());

        }catch(Exception e)
        {
            e.printStackTrace();
        }
        btnNotifications = parentView.findViewById(R.id.btnNotifications);

        btnNotifications.setOnClickListener(v -> {

            GeneralCommands.logEvent(parentActivity,"click_go_to_notifications","click_go_to_notifications");
            MainActivity.viewPager.setCurrentItem(3);
        });


        if (storageObject.isUpgradeTestTwoEnabled()) {
            upgradeDeviceText.setText(mContext.getResources().getText(R.string.levelupnow));
        }


        ownRips = parentView.findViewById(R.id.ownRips);
        ownText = parentView.findViewById(R.id.ownText);
        seekView = parentView.findViewById(R.id.seekView);
        seekBar = parentView.findViewById(R.id.seekBar);
        ownIcon = parentView.findViewById(R.id.ownIcon);
        seekBar.setEnabled(false);


        if (noUpgradeOptions()) {

            //Cannot upgrade either because not eligle or device not supported
            upgradeLockedView.setVisibility(View.VISIBLE);
            upgradeUnlockedView.setVisibility(View.GONE);
        } else {


            //Can Upgrade
            upgradeLockedView.setVisibility(View.GONE);
            upgradeUnlockedView.setVisibility(View.VISIBLE);
        }

        btnNotifications.setOnClickListener(v -> {

            GeneralCommands.logEvent(parentActivity,"click_go_to_notifications","click_go_to_notifications");
            MainActivity.viewPager.setCurrentItem(3);
        });


        btnCheckProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (noUpgradeOptions()) {
                    //Cannot upgrade either because not eligle or device not supported
                    openDialogCannotUpgrade();

                } else {
                    //Can Upgrade
                    goToUpgradePage();
                }
            }
        });

        btnUpgradeNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (noUpgradeOptions()) {
                    //Cannot upgrade either because not eligle or device not supported
                    openDialogCannotUpgrade();

                } else {
                    //Can Upgrade
                    goToUpgradePage();
                }
            }
        });


        btnUnlock.setOnClickListener(v -> {


            if (noUpgradeOptions()) {
                //Cannot upgrade either because not eligle or device not supported
                openDialogCannotUpgrade();

            } else {
                //Can Upgrade
                goToUpgradePage();
            }

        });


        currentAssetObject = storageObject.retrieveCurrentAssetObjectFromPrefs();

//        Toast.makeText(parentActivity, "ZePut"+currentAssetObject.getDeviceUpgradeObject().getUpgradeDevices(), Toast.LENGTH_SHORT).show();


        if (currentAssetObject != null) {

            assetName = getAssetName(currentAssetObject);

            //Calculate Progress
            Double num1 = Double.parseDouble(currentAssetObject.getRemainingBalance());
            Double num2 = Double.parseDouble(currentAssetObject.getTotalRepayment());
            Double totalPaid = num2 - num1;
            String outstandingBalance = GeneralCommands.fixCommas("" + (num2 - totalPaid));


            Double progress = num2 > 0 ? ((float) (num2 - num1) / num2) * 100 : 0;

            if (progress >= 100) {
                //Loan Completed
                seekView.setVisibility(View.VISIBLE);
                ownRips.setVisibility(View.GONE);
                ownIcon.setImageResource(R.drawable.ics_own_it_complete);
                ownText.setText(Html.fromHtml(mContext.getResources().getString(R.string.congratulations_you_own_it, assetName)));


            }

        }


        return parentView;
    }

    private boolean noUpgradeOptions() {

        boolean cannotUpgrade = storageObject.retrieveCurrentAssetObjectFromPrefs().getDeviceUpgradeObject() == null || storageObject.retrieveCurrentAssetObjectFromPrefs().getDeviceUpgradeObject().getUpgradeDevices() == null || storageObject.retrieveCurrentAssetObjectFromPrefs().getDeviceUpgradeObject().getUpgradeDevices().size() == 0;
        if (!cannotUpgrade) {
            //Can Upgrade
            storageObject.setEligibleForUpgrade(true);
        }
        return cannotUpgrade;
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

    public void goToDocumentPage() {
        Intent mIntent = new Intent(parentActivity, DocumentsActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
        GeneralCommands.goToPage(parentActivity, mIntent, false);

    }

    public void goToUpgradePage() {
        Intent mIntent = new Intent(parentActivity, UpgradeActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
        GeneralCommands.goToPage(parentActivity, mIntent, false);

    }

    public void openDialogCannotUpgrade() {
        View contentView = parentActivity.getLayoutInflater().inflate(R.layout.dialog_content_cannot_upgrade, null);


        final DialogPlus dialogPlus = new DialogPlus.Builder(parentActivity)
                .setContentHolder(new ViewHolder(contentView))
                .setGravity(DialogPlus.Gravity.CENTER)
                .setBackgroundColorResourceId(R.color.transparent)
                .setCancelable(true)
                .create();


        //Content View
        View content = dialogPlus.getHolderView();


        RelativeLayout loadingView = content.findViewById(R.id.loadingItems);
        RelativeLayout btnChatWithUs = content.findViewById(R.id.btnChatWithUs);
        RelativeLayout btnBack = content.findViewById(R.id.btnBack);


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

                GeneralCommands.logEventObject(parentActivity, "click_upgrade_no_device_eligible_dialog_close", "click_upgrade_no_device_eligible_dialog_close", obj);
                dialogPlus.dismiss();
            }
        });

        parentActivity.hideKeyboard();


        btnChatWithUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogPlus.dismiss();


                GeneralCommands.logEventObject(parentActivity, "click_upgrade_no_device_eligible_dialog_done", "click_upgrade_no_device_eligible_dialog_done", obj);
                parentActivity.goBack();
//                try {MainActivity.openWhatsAppWithMessage(mContext.getResources().getString(R.string.howcaniupgrade));}catch (Exception e){e.printStackTrace();}

            }
        });


        dialogPlus.show();
    }

    public String getAssetName(AssetObject asset) {
        if (asset == null) return storageObject.retrieveUserPhoneNumberFromPrefs();

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


}
