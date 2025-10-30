package com.watutz.app.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.watutz.app.GeneralCommands;
import com.watutz.app.LevelUpActivity;
import com.watutz.app.MainActivity;
import com.watutz.app.R;
import com.watutz.app.UpgradeActivity;
import com.watutz.app.constant.IWhereMyLocationConstants;
import com.watutz.app.font.FirmaTextView;
import com.watutz.app.objects.AssetObject;
import com.watutz.app.objects.CurrentDevice;
import com.watutz.app.objects.DeviceUpgradeObject;
import com.watutz.app.objects.UpgradeDevicesItem;
import com.watutz.app.prefs.StoreAndGetFromPrefs;
import com.watutz.app.util.ImageUtil;
import com.watutz.app.view.MaterialRippleLayout;


import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class FragmentUpgrade extends Fragment implements IWhereMyLocationConstants {

    private View parentView;
   
    private static StoreAndGetFromPrefs storageObject;
    public static UpgradeActivity parentActivity;
	static Context mContext;

	RelativeLayout loadingView;
    RelativeLayout optionsContent,devicesContent;


    FirmaTextView progressAmount,progressText;
    SeekBar seekBar;

    //Current Device
    FirmaTextView deviceName;
    ImageView deviceImage;
//    FirmaTextView deviceTitle,deviceRam;
    FirmaTextView deviceDisplay,deviceRom,deviceCamera,deviceBattery;
    public boolean isChoosingDevice=false;

    //New Device
    FirmaTextView newDeviceName;
    LinearLayout optionsView;
    ImageView newDeviceImage;
//    FirmaTextView newDeviceTitle,newDeviceRam;
    FirmaTextView newDeviceDisplay,newDeviceRom,newDeviceCamera,newDeviceBattery;


    FirmaTextView deviceUpgradeAmount;

    MaterialRippleLayout upgradeWhatsappRips,upgradeRips;
    RelativeLayout btnUpgrade,btnUpgradeWhatsApp;
    
    AssetObject currentAssetObject;
    DeviceUpgradeObject upgradeObject;
    CurrentDevice device;
    ArrayList<UpgradeDevicesItem> listDataUpgrades;
    UpgradeDevicesItem upgradeDeviceItem;




    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        parentView = inflater.inflate(R.layout.fragment_upgrade, container, false);
        parentActivity = (UpgradeActivity)getActivity();
        mContext= parentActivity.getApplicationContext();
        storageObject = new StoreAndGetFromPrefs(mContext);
        loadingView= (RelativeLayout) parentView.findViewById(R.id.loadingItems);

        optionsContent= (RelativeLayout) parentView.findViewById(R.id.optionsContent);
        devicesContent= (RelativeLayout) parentView.findViewById(R.id.devicesContent);

        progressAmount = parentView.findViewById(R.id.progressAmount);
        seekBar = parentView.findViewById(R.id.seekBar);
        progressText = parentView.findViewById(R.id.progressText);

        // Current Device
        deviceName = parentView.findViewById(R.id.deviceName);
        deviceImage = parentView.findViewById(R.id.deviceImage);
        deviceDisplay = parentView.findViewById(R.id.deviceDisplay);
        deviceRom = parentView.findViewById(R.id.deviceRom);
        deviceCamera = parentView.findViewById(R.id.deviceCamera);
        deviceBattery = parentView.findViewById(R.id.deviceBattery);

        // New Device
        newDeviceName = parentView.findViewById(R.id.newDeviceName);
        optionsView = parentView.findViewById(R.id.optionsView);
        newDeviceImage = parentView.findViewById(R.id.newDeviceImage);
        newDeviceDisplay = parentView.findViewById(R.id.newDeviceDisplay);
        newDeviceRom = parentView.findViewById(R.id.newDeviceRom);
        newDeviceCamera = parentView.findViewById(R.id.newDeviceCamera);
        newDeviceBattery = parentView.findViewById(R.id.newDeviceBattery);

        deviceUpgradeAmount = parentView.findViewById(R.id.deviceUpgradeAmount);
        btnUpgrade = parentView.findViewById(R.id.btnUpgrade);
        btnUpgradeWhatsApp = parentView.findViewById(R.id.btnUpgradeWhatsApp);

        upgradeRips = parentView.findViewById(R.id.upgradeRips);
        upgradeWhatsappRips = parentView.findViewById(R.id.upgradeWhatsappRips);
        

        // Set the current device details
         setDetails();

        parentActivity.btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(!isChoosingDevice){
                    parentActivity.goBack();

                }
                else {

                    //Show Details
                    optionsContent.setVisibility(View.VISIBLE);
                    devicesContent.setVisibility(View.GONE);
                    isChoosingDevice=false;
                    parentActivity.isChoosingDevice=false;
                }
            }
        });

        // Listen for the result; the empty Bundle is fine if you have no payload.
        getParentFragmentManager().setFragmentResultListener(
                "REFRESH_KEY",
                this,
                new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String isChoosing,
                                                 @NonNull Bundle bundle) {
                        // Called whenever setFragmentResult("REFRESH_KEY", ...) is invoked
                        boolean choice= bundle.getBoolean("isChoosing", false);
                        if(choice)
                        {
                            //Show Details
                            optionsContent.setVisibility(View.VISIBLE);
                            devicesContent.setVisibility(View.GONE);
                            isChoosingDevice=false;
                            parentActivity.isChoosingDevice=false;

                        }
                    }
                }
        );

		
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


    private void setDetails() {
        
        currentAssetObject= storageObject.retrieveCurrentAssetObjectFromPrefs();
        upgradeObject= currentAssetObject.getDeviceUpgradeObject();
        
        if(currentAssetObject !=null && upgradeObject !=null)
        {
            //Calculate Progress
            int num1 = Integer.parseInt(currentAssetObject.getRemainingBalance());
            int num2 = Integer.parseInt(currentAssetObject.getTotalRepayment());
            int totalPaid=num2 -num1;
            String outstandingBalance=GeneralCommands.fixCommas(""+(num2-totalPaid));


            float progress = num2 > 0 ? ((float)(num2 - num1) / num2) * 100 : 0;


            //Set Seekbar
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    // Update the text
                    progressText.setText(progress + "%");

                    // Calculate position
                    int thumbOffset = seekBar.getThumbOffset();
                    int max = seekBar.getMax();
                    int seekBarWidth = seekBar.getWidth() - 2 * thumbOffset;
                    float ratio = progress / (float) max;

                    // Calculate the X position where text should be
                    int newX = (int) (ratio * seekBarWidth + thumbOffset - (progressText.getWidth() / 2f));
                    progressText.setX(newX);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });

            progressText.post(() -> {
                // Trigger initial positioning after layout
                int thumbOffset = seekBar.getThumbOffset();
                int max = seekBar.getMax();
                int seekBarWidth = seekBar.getWidth() - 2 * thumbOffset;
                float ratio = progress / (float) max;

                int newX = (int) (ratio * seekBarWidth + thumbOffset - (progressText.getWidth() / 2f));
                progressText.setX(newX);
            });


            seekBar.setProgress((int)progress);
            seekBar.setEnabled(false);


            if(progress<100)
            {
                //not completed
                progressAmount.setText(Html.fromHtml(mContext.getResources().getString(R.string.youarejustfromupgrade, currentAssetObject.getCurrencyCode()+" "+outstandingBalance)));
                upgradeRips.setVisibility(View.VISIBLE);
                upgradeWhatsappRips.setVisibility(View.GONE);
            }
            else
            {
                progressAmount.setText(Html.fromHtml(mContext.getResources().getString(R.string.youcanunlock)));
                upgradeRips.setVisibility(View.GONE);
                upgradeWhatsappRips.setVisibility(View.VISIBLE);
            }

            device = upgradeObject.getCurrentDevice();
            listDataUpgrades= (ArrayList<UpgradeDevicesItem>) upgradeObject.getUpgradeDevices();





            String urlData= device.getParams().getImageURL();
            ImageUtil.displayImage(deviceImage, urlData, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });

            if(device !=null)
            {
                deviceName.setText(device.getName()+" "+device.getModelCode());
//                deviceTitle.setText(device.getModelCode());
                deviceDisplay.setText(mContext.getResources().getString(R.string.ram)+":"+device.getParams().getRAM());
//                deviceRam.setText(mContext.getResources().getString(R.string.ram)+":"+device.getParams().getRAM());
                deviceRom.setText(mContext.getResources().getString(R.string.rom)+":"+device.getParams().getStorage());
                deviceCamera.setText(mContext.getResources().getString(R.string.camera)+":"+device.getAdditionalParams().getCamera().getFront()+"\n"+device.getAdditionalParams().getCamera().getMain());
                deviceBattery.setText(mContext.getResources().getString(R.string.battery)+":"+device.getParams().getBattery());

                if(upgradeObject !=null && listDataUpgrades!=null && listDataUpgrades.size()>0)
                {
                    //There are some upgrade devices
                    upgradeDeviceItem= listDataUpgrades.get(0);
                    refreshUpgradeDeviceDetails(upgradeDeviceItem);

                    // Populate and add device options
                    populateDeviceOptions(optionsView, listDataUpgrades);


                }
                else {
                    //There are no upgrade devices
                }
            }


            btnUpgrade.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   isChoosingDevice=false;
                    parentActivity.isChoosingDevice=false;
                    //Go to upgrade choose page
                    parentActivity.goBack();
                    try{ if(MainActivity.viewPager !=null)MainActivity.viewPager.setCurrentItem(2);}catch(Exception e){};

                    GeneralCommands.logEvent(parentActivity,"click_upgrade_page_button","click_upgrade_page_button");

                }
            });

            btnUpgradeWhatsApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isChoosingDevice=false;
                    parentActivity.isChoosingDevice=false;

                    GeneralCommands.logEvent(parentActivity,"click_upgrade_whatsapp_button","click_upgrade_whatsapp_button");
                    openWhatsAppWithMessage(currentAssetObject.getLoanNumber(), storageObject.retrieveCurrentUpgradeDevicesItemFromPrefs().getModelCode());
                }
            });



        }
        else {
            //Invalid Eligibility
            parentActivity.finish();
            goToLevelUpPage();
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


    public void refreshUpgradeDeviceDetails(UpgradeDevicesItem item)
    {
        //Here are some new devices

        if(item!=null)
        {
            String urlData= item.getParams().getImageURL();
            ImageUtil.displayImage(newDeviceImage, urlData, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                }
            });


            newDeviceName.setText(item.getName()+" "+item.getModelCode());
//            newDeviceTitle.setText(item.getModelCode());
            newDeviceDisplay.setText(mContext.getResources().getString(R.string.ram)+":"+item.getParams().getRAM());
//            newDeviceRam.setText(mContext.getResources().getString(R.string.ram)+":"+item.getParams().getRAM());
            newDeviceRom.setText(mContext.getResources().getString(R.string.rom)+":"+item.getParams().getStorage());
            newDeviceCamera.setText(mContext.getResources().getString(R.string.camera)+":"+item.getAdditionalParams().getCamera().getFront()+"\n"+item.getAdditionalParams().getCamera().getMain());
            newDeviceBattery.setText(mContext.getResources().getString(R.string.battery)+":"+item.getParams().getBattery());

        }
        else {

            //There are no upgrade devices

            Toast.makeText(parentActivity, mContext.getResources().getString(R.string.cannotupgrade), Toast.LENGTH_SHORT).show();
            newDeviceName.setText("");
//            newDeviceTitle.setText("");
            newDeviceDisplay.setText("");
//            newDeviceRam.setText("");
            newDeviceRom.setText("");
            newDeviceCamera.setText("");
            newDeviceBattery.setText("");
        }

        

        deviceUpgradeAmount.setText("NGN 20,000");
    }



    private void populateDeviceOptions(LinearLayout optionsView, List<UpgradeDevicesItem> upgradeDevices) {
        // Create String array from the list of upgrade devices
        String[] deviceOptions = new String[upgradeDevices.size()];

        for (int i = 0; i < upgradeDevices.size(); i++) {
            // Assuming getModelCode() returns the model code as a string
            deviceOptions[i] = upgradeDevices.get(i).getName()+" "+upgradeDevices.get(i).getModelCode();
        }

        // Pass the populated array to the method that adds views
        addDeviceOptionsToLayout(optionsView, deviceOptions);
    }


    private void addDeviceOptionsToLayout(LinearLayout optionsView, String[] deviceOptions) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        optionsView.removeAllViews();

        for (int i = 0; i < deviceOptions.length; i++) {
            final String deviceName = deviceOptions[i];
            final int position = i;
            String urlData = listDataUpgrades.get(i).getParams().getImageURL();

            // Inflate the new layout
            View deviceView = inflater.inflate(R.layout.list_item_upgrade_device, optionsView, false);

            // Get references to inner views
            RelativeLayout clickableContainer = deviceView.findViewById(R.id.newDeviceUpgradeImageView);
            FirmaTextView nameText = deviceView.findViewById(R.id.newDeviceUpgradeName);
            ImageView imageView = deviceView.findViewById(R.id.newDeviceUpgradeImage);

            // Set data
            nameText.setText(deviceName);
            ImageUtil.displayLessRoundImage(imageView, urlData,new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {}

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {}

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {}

                @Override
                public void onLoadingCancelled(String s, View view) {}
            });

            // Optional: Set different images dynamically if needed
            // imageView.setImageResource(...)

            // Handle selection styling if needed
//            if (i == 0) {
//                clickableContainer.setBackgroundResource(R.xml.rounded_list_theme_orange); // Selected
//            } else {
//                clickableContainer.setBackgroundResource(R.xml.rounded_list_transparent_white_faint_stroke);
//            }

            // Add click listener
            clickableContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   isChoosingDevice=true;
                    parentActivity.isChoosingDevice=true;

                    updateSelection(optionsView, v.getId()); // Optional if you want to update selection UI
                    onDeviceSelected(deviceName, position);  // Callback
                }
            });

            optionsView.addView(deviceView);
        }
    }


    // Helper method to update selection UI
    private void updateSelection(LinearLayout parentLayout, int selectedId) {
        for (int i = 0; i < parentLayout.getChildCount(); i++) {
            View child = parentLayout.getChildAt(i);
            if (child instanceof com.watutz.app.view.MaterialRippleLayout) {
                View innerView = ((com.watutz.app.view.MaterialRippleLayout) child).getChildAt(0);
                if (innerView instanceof RelativeLayout) {
                    RelativeLayout rel = (RelativeLayout) innerView;
                    if (rel.getId() == selectedId) {
                        rel.setBackgroundResource(R.xml.rounded_list_theme_orange);
                    } else {
                        rel.setBackgroundResource(R.xml.rounded_list_transparent_white_stroke);
                    }
                }
            }
        }
    }

    // Updated method to handle device selection with position
    private void onDeviceSelected(String deviceName, int position) {
        // Implement your device selection logic here
        // Now you have both the device name and its position in the array
        upgradeDeviceItem=listDataUpgrades.get(position);
        refreshUpgradeDeviceDetails(upgradeDeviceItem);
        storageObject.saveCurrentUpgradeDevicesItemToPrefs(upgradeDeviceItem);
        optionsContent.setVisibility(View.GONE);
        devicesContent.setVisibility(View.VISIBLE);

        Toast.makeText(parentActivity, deviceName, Toast.LENGTH_SHORT).show();
    }


    // Helper method to convert dp to pixels
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }



    private void openWhatsAppWithMessage(String loanNumber, String deviceModel) {
        try {
            // Prepare the text message with the required format
            String message = getString(R.string.whatsapp_upgrade_message, loanNumber, deviceModel);

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
                    startActivity(whatsappIntent);
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
                startActivity(browserIntent);
                return; // Success! Exit the method
            } catch (ActivityNotFoundException e) {
                // Browser approach failed, continue to next attempt
                e.printStackTrace();
            }

            // Third try: Use wa.me shortlink format (mobile friendly)
            try {
                String waUrl = "https://wa.me/" + phoneNumber + "?text=" + encodedMessage;
                Intent waIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(waUrl));
                startActivity(waIntent);
                return; // Success! Exit the method
            } catch (ActivityNotFoundException e) {
                // All attempts failed
                e.printStackTrace();
                Toast.makeText(getContext(), mContext.getResources().getString(R.string.whatsappnotinstalled), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), mContext.getResources().getString(R.string.problemopeningwhatsapp), Toast.LENGTH_SHORT).show();
        }
    }

    // Helper method to check if an app is installed
    private boolean isAppInstalled(String packageName) {
        PackageManager pm = getContext().getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void goToLevelUpPage()
    {
        Intent mIntent = new Intent(parentActivity, LevelUpActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
        GeneralCommands.goToPage(parentActivity, mIntent,false);

    }

}
