package com.watutz.app.fragments;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.watutz.app.GeneralCommands;
import com.watutz.app.LanguageActivity;
import com.watutz.app.MainActivity;
import com.watutz.app.R;
import com.watutz.app.SettingsActivity;
import com.watutz.app.constant.IWhereMyLocationConstants;
import com.watutz.app.font.FirmaTextView;
import com.watutz.app.prefs.StoreAndGetFromPrefs;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;

public class FragmentSettings extends Fragment implements IWhereMyLocationConstants {

    private View parentView;

    private static StoreAndGetFromPrefs storageObject;
    public static SettingsActivity parentActivity;
    static Context mContext;
    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1;



    RelativeLayout btnLanguage;


    RelativeLayout loadingView;

    RelativeLayout btnNotifications;
    FirmaTextView notificationText;
    NotificationManagerCompat notificationManager ;
    boolean areNotificationsEnabled=false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_settings, container, false);
        parentActivity = (SettingsActivity) getActivity();
        mContext = parentActivity.getApplicationContext();
        storageObject = new StoreAndGetFromPrefs(mContext);
        loadingView = (RelativeLayout) parentView.findViewById(R.id.loadingItems);

        notificationManager= NotificationManagerCompat.from(parentActivity);
        boolean areNotificationsEnabled = notificationManager.areNotificationsEnabled();

        btnNotifications = parentView.findViewById(R.id.btnNotifications);
        notificationText = parentView.findViewById(R.id.notificationText);

        btnLanguage= parentView.findViewById(R.id.btnLanguage);


        btnLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLanguagePage();
            }
        });

        if (!areNotificationsEnabled)
        {

            btnNotifications.setBackgroundResource(R.xml.rounded_list_button_orange);
            notificationText.setText(mContext.getResources().getString(R.string.turnon));
        } else {

//            Toast.makeText(mContext, "Already on", Toast.LENGTH_SHORT).show();

            btnNotifications.setBackgroundResource(R.xml.rounded_list_button_green);
            notificationText.setText(mContext.getResources().getString(R.string.turnedon));
        }


        btnNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check if all Bluetooth permissions are granted
                if (!areNotificationsEnabled)
                {
//                        Toast.makeText(mContext, "good", Toast.LENGTH_SHORT).show();
                    GeneralCommands.logEvent(parentActivity,"click_turn_on_notifications","click_turn_on_notifications");

                    // Notifications are ON, show user a message or toggle off as needed
                    Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                            .putExtra(Settings.EXTRA_APP_PACKAGE, parentActivity.getPackageName());
                    parentActivity.startActivity(intent);

                    // All permissions are granted, proceed with your app logic
                } else {
                   Toast.makeText(mContext, mContext.getResources().getString(R.string.turnedon),Toast.LENGTH_SHORT).show();
                }

            }
        });



        return parentView;
    }

    private boolean checkBluetoothPermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return ContextCompat.checkSelfPermission(parentActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(parentActivity, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(parentActivity, Manifest.permission.BLUETOOTH_ADVERTISE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(parentActivity, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(parentActivity, Manifest.permission.FOREGROUND_SERVICE) == PackageManager.PERMISSION_GRANTED &&

                    ContextCompat.checkSelfPermission(parentActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(parentActivity, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(parentActivity, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(parentActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(parentActivity, Manifest.permission.FOREGROUND_SERVICE) == PackageManager.PERMISSION_GRANTED &&

                    ContextCompat.checkSelfPermission(parentActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
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
    public void onPause() {
        super.onPause();
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }


    public void goToLanguagePage()
    {
        Intent mIntent = new Intent(parentActivity, LanguageActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
        GeneralCommands.goToPage( parentActivity,mIntent,true);

    }

}
