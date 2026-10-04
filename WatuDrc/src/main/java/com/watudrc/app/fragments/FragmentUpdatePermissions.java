package com.watudrc.app.fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.remoteconfig.ConfigUpdate;
import com.google.firebase.remoteconfig.ConfigUpdateListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.watudrc.app.GeneralCommands;
import com.watudrc.app.MainActivity;
import com.watudrc.app.R;
import com.watudrc.app.UpdatePermissionsActivity;
import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.font.FirmaTextView;
import com.watudrc.app.mobileservices.GetData;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;
import com.watudrc.app.util.FirebaseConfig;

import java.util.concurrent.Executor;

public class FragmentUpdatePermissions extends Fragment implements IWhereMyLocationConstants {

    private View parentView;

    private static StoreAndGetFromPrefs storageObject;
    public static UpdatePermissionsActivity parentActivity;
    static Context mContext;
    private boolean isReceiverRegistered = false;
    private static final int REQUEST_ENABLE_BT = 1;

    //Location Stuff
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private static final int BLUETOOTH_PERMISSION_REQUEST_CODE = 1003;
    private static final int REQUEST_CHECK_SETTINGS = 1002;

    private static final int CALL_PERMISSION_REQUEST_CODE = 100;


    private BroadcastReceiver locationProviderReceiver;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private boolean isLocationEnabled = false;
    //private BluetoothStateReceiver bluetoothStateReceiver;

    //BlueTooth
    BluetoothAdapter bluetoothAdapter;

    RelativeLayout loadingView;

    RelativeLayout btnDone;
    SwitchCompat btnBluetooth, btnNotifications, btnFingerPrint, btnBluetoothPermission, btnLocation, btnLocationPermission, btnInternet, btnCallPermission;

    //Firebase
    FirebaseConfig config;
    boolean should_force_all_permissions = false;
    FirebaseConfig remoteConfig;
    FirebaseRemoteConfig mFirebaseConfig;


    NotificationManagerCompat notificationManager;
    boolean areNotificationsEnabled = false;

    RelativeLayout loadView, completeView;
    LinearLayout contentView;
    RelativeLayout view1, view2, view3;
    ImageView progressWheel;
    RelativeLayout btnComplete;

    FirmaTextView header, titleText;
    ImageView div1, div2;

    private CompoundButton.OnCheckedChangeListener fingerprintListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_update_permissions, container, false);
        parentActivity = (UpdatePermissionsActivity) getActivity();
        mContext = parentActivity.getApplicationContext();
        storageObject = new StoreAndGetFromPrefs(mContext);
        loadingView = parentView.findViewById(R.id.loadingItems);

        final Handler handler = new Handler();

        // Remote Configs
        remoteConfig = new FirebaseConfig();
        mFirebaseConfig = remoteConfig.getConfigs();

        btnBluetooth = parentView.findViewById(R.id.btnBluetooth);
        btnBluetoothPermission = parentView.findViewById(R.id.btnBluetoothPermission);
        btnLocation = parentView.findViewById(R.id.btnLocation);
        btnLocationPermission = parentView.findViewById(R.id.btnLocationPermission);
        btnInternet = parentView.findViewById(R.id.btnInternet);
        btnFingerPrint = parentView.findViewById(R.id.btnFingerPrint);
        btnNotifications = parentView.findViewById(R.id.btnNotifications);

        btnCallPermission = parentView.findViewById(R.id.btnCallPermission);

        btnCallPermission.setChecked(hasCallPermission());


        btnComplete = parentView.findViewById(R.id.btnComplete);

        contentView = parentView.findViewById(R.id.contentView);
        loadView = parentView.findViewById(R.id.loadView);
        completeView = parentView.findViewById(R.id.completeView);


        progressWheel = parentView.findViewById(R.id.progressWheel);


        notificationManager = NotificationManagerCompat.from(parentActivity);
        boolean areNotificationsEnabled = notificationManager.areNotificationsEnabled();


        if (!areNotificationsEnabled) {
            btnNotifications.setChecked(false);
        } else {

            btnNotifications.setChecked(true);

        }

        btnFingerPrint.setChecked(storageObject.isUserFingerprintEnabled());

        btnNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                    GeneralCommands.logEvent(parentActivity, "click_notifications_enable", "click_notifications_enable");
                    Intent notifSettingsIntent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                    notifSettingsIntent.putExtra(Settings.EXTRA_APP_PACKAGE, parentActivity.getPackageName());
                    startActivity(notifSettingsIntent);
                }
                else {
                    GeneralCommands.logEvent(parentActivity, "click_notifications_disable", "click_notifications_disable");
                    // Android does not allow apps to disable their own notifications programmatically.
                    // Redirect the user to the system notification settings for this app.
                    Intent notifSettingsIntent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                    notifSettingsIntent.putExtra(Settings.EXTRA_APP_PACKAGE, parentActivity.getPackageName());
                    startActivity(notifSettingsIntent);
                }

            }
        } );




        btnComplete.setOnClickListener(v -> {
            GeneralCommands.logEvent(parentActivity, "click_complete_button", "click_complete_button");
            goToDashboard();

        });

        btnDone = parentView.findViewById(R.id.btnDone);
        // Register the receiver to listen for changes in location provider status
        registerLocationProviderReceiver();
        locationManager = (LocationManager) parentActivity.getSystemService(LOCATION_SERVICE);


        btnLocation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    GeneralCommands.logEvent(parentActivity, "click_turn_on_location", "click_turn_on_location");

                    if (isLocationPermisionGranted()) {
                        turnOnLocation();
                    }
                } else {
                    GeneralCommands.logEvent(parentActivity, "click_turn_off_location", "click_turn_off_location");
                    // Apps cannot programmatically turn off system-level location.
                    // Redirect the user to Location Settings so they can disable it themselves.
                    Intent locationSettingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(locationSettingsIntent);
                }
            }
        });


        btnLocationPermission.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    GeneralCommands.logEvent(parentActivity, "click_turn_on_location_permission", "click_turn_on_location_permission");

                    isLocationPermisionGranted();
                } else {
                    GeneralCommands.logEvent(parentActivity, "click_turn_off_location_permission", "click_turn_off_location_permission");
                    // Runtime permissions cannot be revoked programmatically.
                    // Redirect the user to App Settings so they can revoke location permission manually.
                    Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    appSettingsIntent.setData(Uri.fromParts("package", parentActivity.getPackageName(), null));
                    startActivity(appSettingsIntent);
                }
            }
        });


        btnBluetooth.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    GeneralCommands.logEvent(parentActivity, "click_turn_on_bluetooth", "click_turn_on_bluetooth");

                    // Check if all Bluetooth permissions are granted
                    if (checkBluetoothPermissions()) {
//                        Toast.makeText(mContext, "good", Toast.LENGTH_SHORT).show();

                        turnOnBluetooth();

                        // All permissions are granted, proceed with your app logic
                    } else {
                        // Request permissions
//                        Toast.makeText(mContext, "Req", Toast.LENGTH_SHORT).show();
                        requestBluetoothPermissions();
                    }
                } else {
                    GeneralCommands.logEvent(parentActivity, "click_turn_off_bluetooth", "click_turn_off_bluetooth");
                    // Disable Bluetooth
                    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            // BluetoothAdapter.disable() is restricted on Android 13+.
                            // Redirect user to Bluetooth settings to disable it manually.
                            Intent btSettingsIntent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
                            startActivity(btSettingsIntent);
                        } else {
                            //noinspection deprecation
                            bluetoothAdapter.disable();
                        }
                    }
                }
            }
        });


        btnBluetoothPermission.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Check if all Bluetooth permissions are granted
                    GeneralCommands.logEvent(parentActivity, "click_turn_on_bluetooth_permission", "click_turn_on_bluetooth_permission");


                    requestBluetoothPermissions();

                } else {
                    GeneralCommands.logEvent(parentActivity, "click_turn_off_bluetooth_permission", "click_turn_off_bluetooth_permission");
                    // Runtime permissions cannot be revoked programmatically.
                    // Redirect the user to App Settings so they can revoke Bluetooth permission manually.
                    Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    appSettingsIntent.setData(Uri.fromParts("package", parentActivity.getPackageName(), null));
                    startActivity(appSettingsIntent);
                }
            }
        });


        fingerprintListener = (buttonView, isChecked) -> {
            if (isChecked) {
                GeneralCommands.logEvent(parentActivity, "click_drawer_fingerprint_enable", "click_drawer_fingerprint_enable");

                if (checkBiometricSupportAndProceed()) {
                    showBiometricPrompt(btnFingerPrint);
                } else {
                    Toast.makeText(parentActivity, mContext.getResources().getString(R.string.fingeprintnotsupported), Toast.LENGTH_SHORT).show();

                    // If biometric isn't supported, roll back to false silently
                    btnFingerPrint.setOnCheckedChangeListener(null);
                    btnFingerPrint.setChecked(false);
                    btnFingerPrint.setOnCheckedChangeListener(fingerprintListener);
                }
            } else {
                GeneralCommands.logEvent(parentActivity, "click_drawer_fingerprint_disable", "click_drawer_fingerprint_disable");
                storageObject.saveUSerFingeprintEnabled(false);
                // Note: Removed btnFingerPrint.setChecked(false) from here
                // since the user already toggled it off to reach this branch.
            }
        };

        btnFingerPrint.setOnCheckedChangeListener(fingerprintListener);

        btnCallPermission.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Request the permission if not already granted (hasCallPermission() also requests it)
                    hasCallPermission();
                } else {
                    GeneralCommands.logEvent(parentActivity, "click_call_permission_disable", "click_call_permission_disable");
                    // Runtime permissions cannot be revoked programmatically.
                    // Redirect the user to App Settings so they can revoke Call permission manually.
                    Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    appSettingsIntent.setData(Uri.fromParts("package", parentActivity.getPackageName(), null));
                    startActivity(appSettingsIntent);
                }
            }
        });


        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(mContext, ""+should_force_all_permissions, Toast.LENGTH_SHORT).show();

                if (should_force_all_permissions) {
                    if (btnLocation.isChecked() && btnNotifications.isChecked() && btnFingerPrint.isChecked()) {

                        //Alll Good
                        showProgress();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // showCompleteView();
                                goToDashboard();
                            }
                        }, 4000);


                    } else {

//                        Toast.makeText(mContext, "Not all permissions granted"+btnLocation.isChecked()+":"+btnNotifications.isChecked()+":"+btnFingerPrint.isChecked(), Toast.LENGTH_SHORT).show();

                        //Enforce Permisission CHeck
                        String err = mContext.getResources().getString(R.string.turnonallpermissions);
                        Toast.makeText(mContext, err, Toast.LENGTH_SHORT).show();

                    }


                } else {
                    //Proceed Good
                    showProgress();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //showCompleteView();
                            goToDashboard();
                        }
                    }, 4000);
                }


            }
        });

        // Create the BluetoothStateReceiver
//        bluetoothStateReceiver = new BluetoothStateReceiver(new BluetoothStateReceiver.BluetoothStateListener() {
//            @Override
//            public void onBluetoothStateChanged(boolean isEnabled) {
//                if (!isEnabled) {
//                    // Bluetooth is not enabled, prompt the user to enable it
//                    if (btnBluetooth != null) btnBluetooth.setChecked(false);
//                    if (btnBluetoothPermission != null) btnBluetoothPermission.setChecked(false);
//                } else {
//                    // Bluetooth is already enabled, proceed with your app logic
//                    if (btnBluetooth != null) btnBluetooth.setChecked(true);
//                    if (btnBluetoothPermission != null) btnBluetoothPermission.setChecked(true);
//                }
//            }
//        });

        new Thread(() -> {
            // a potentially time consuming task
            setDefaultConfigs();
//				setFirebaseDetails();
            parentView.post(new Runnable() {
                public void run() {
                    //Update UI
                }
            });
        }).start();

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

    private void requestBluetoothPermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions(parentActivity, new String[]{
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_ADVERTISE,
                    Manifest.permission.FOREGROUND_SERVICE,
                    Manifest.permission.BLUETOOTH_CONNECT,
                    Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                    Manifest.permission.SCHEDULE_EXACT_ALARM,
            }, BLUETOOTH_PERMISSION_REQUEST_CODE);

        } else {
            ActivityCompat.requestPermissions(parentActivity, new String[]{
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.FOREGROUND_SERVICE,
                    Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                    Manifest.permission.SCHEDULE_EXACT_ALARM,
            }, BLUETOOTH_PERMISSION_REQUEST_CODE);

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

        if (mFirebaseConfig != null) {
            // Re-fetch configs every time app comes to foreground
            mFirebaseConfig.fetchAndActivate().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    applyConfigs();
                }
            });
        }

        if (btnCallPermission != null) {
            // We use ContextCompat directly so it doesn't accidentally prompt them again
            boolean hasCallPerm = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;

            // Temporarily clear listener to prevent loop/accidental triggering, update UI, then re-attach
            btnCallPermission.setOnCheckedChangeListener(null);
            btnCallPermission.setChecked(hasCallPerm);
            btnCallPermission.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        hasCallPermission();
                    } else {
                        GeneralCommands.logEvent(parentActivity, "click_call_permission_disable", "click_call_permission_disable");
                        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        appSettingsIntent.setData(Uri.fromParts("package", parentActivity.getPackageName(), null));
                        startActivity(appSettingsIntent);
                    }
                }
            });
        }

        // Check if location provider is enabled when the activity resumes
        isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isLocationEnabled) {
            if (btnLocation != null) btnLocation.setChecked(false);
            if (btnLocationPermission != null) btnLocationPermission.setChecked(false);
        } else {
            if (btnLocation != null) btnLocation.setChecked(true);
            if (btnLocationPermission != null) btnLocationPermission.setChecked(true);
        }

        // Check if bluetooth is enabled
        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled()) {
                if (btnBluetooth != null) btnBluetooth.setChecked(false);
                if (btnBluetoothPermission != null) btnBluetoothPermission.setChecked(false);
            } else {
                if (btnBluetooth != null) btnBluetooth.setChecked(true);
                if (btnBluetoothPermission != null) btnBluetoothPermission.setChecked(true);
            }
        }

        // Check if location provider is enabled when the activity resumes
        isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!isLocationEnabled) {
            // Location provider is disabled, handle accordingly
            // Maybe prompt the user to enable location services
            if (btnLocation != null) btnLocation.setChecked(false);
            if (btnLocationPermission != null) btnLocationPermission.setChecked(false);
        } else {
            if (btnLocation != null) btnLocation.setChecked(true);
            if (btnLocationPermission != null) btnLocationPermission.setChecked(true);


        }


        // Check if bluetooth is enabled
        if (bluetoothAdapter != null) {
            if (!bluetoothAdapter.isEnabled()) {
                // Bluetooth is not enabled, prompt the user to enable it
                if (btnBluetooth != null) btnBluetooth.setChecked(false);
                if (btnBluetoothPermission != null) btnBluetoothPermission.setChecked(false);




            } else {
                // Bluetooth is already enabled, proceed with your app logic
                if (btnBluetooth != null) btnBluetooth.setChecked(true);
                if (btnBluetoothPermission != null) btnBluetoothPermission.setChecked(true);


            }
        }

        // Register the receiver to listen for Bluetooth state changes
//        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
//        parentActivity.registerReceiver(bluetoothStateReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the receiver when the activity is paused
//        parentActivity.unregisterReceiver(bluetoothStateReceiver);
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

    public boolean isLocationPermisionGranted() {
        boolean isPermisionGranted = false;
        final int ACTION_REQUEST_MULTIPLE_PERMISSION = 1;
        if (Build.VERSION.SDK_INT >= 23) {  // Only ask for these permissions on runtime when running Android 6.0 or higher
            switch (ContextCompat.checkSelfPermission(parentActivity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                case PackageManager.PERMISSION_DENIED:
                    int pCheck = ContextCompat.checkSelfPermission(parentActivity, Manifest.permission.ACCESS_FINE_LOCATION);
                    pCheck += ContextCompat.checkSelfPermission(parentActivity, Manifest.permission.ACCESS_COARSE_LOCATION);
                    pCheck += ContextCompat.checkSelfPermission(parentActivity, Manifest.permission.FOREGROUND_SERVICE);
                    if (pCheck != 0) {
                        ActivityCompat.requestPermissions(parentActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.FOREGROUND_SERVICE,
                        }, LOCATION_PERMISSION_REQUEST_CODE);
                    }
                    // Make the link clickable. Needs to be called after show(), in order to generate hyperlinks
                    isPermisionGranted = false;
                    break;
                case PackageManager.PERMISSION_GRANTED:
                    isPermisionGranted = true;
//                    turnOnLocation();
                    break;
            }

        } else {
            //Aready Granted on Installtion
            isPermisionGranted = true;
        }
        return isPermisionGranted;
    }

    public boolean isBlueToothPermisionGranted() {
        boolean isPermisionGranted = false;
        final int ACTION_REQUEST_MULTIPLE_PERMISSION = 1;
        if (Build.VERSION.SDK_INT >= 23) {  // Only ask for these permissions on runtime when running Android 6.0 or higher

            int pCheck = ContextCompat.checkSelfPermission(parentActivity, Manifest.permission.BLUETOOTH);
            pCheck += ContextCompat.checkSelfPermission(parentActivity, Manifest.permission.BLUETOOTH_SCAN);
            pCheck += ContextCompat.checkSelfPermission(parentActivity, Manifest.permission.BLUETOOTH_ADVERTISE);
            pCheck += ContextCompat.checkSelfPermission(parentActivity, Manifest.permission.BLUETOOTH_CONNECT);
            pCheck += ContextCompat.checkSelfPermission(parentActivity, Manifest.permission.FOREGROUND_SERVICE);
            pCheck += ContextCompat.checkSelfPermission(parentActivity, Manifest.permission.BLUETOOTH_ADMIN);

            boolean isBluetoothGranted = ContextCompat.checkSelfPermission(parentActivity, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(parentActivity, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(parentActivity, Manifest.permission.BLUETOOTH_ADVERTISE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(parentActivity, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(parentActivity, Manifest.permission.FOREGROUND_SERVICE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(parentActivity, Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED;


            if (!isBluetoothGranted) {
//                Toast.makeText(parentActivity, "asking"+pCheck, Toast.LENGTH_SHORT).show();

                ActivityCompat.requestPermissions(parentActivity, new String[]{
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_ADVERTISE,
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.FOREGROUND_SERVICE,
                        Manifest.permission.BLUETOOTH_ADMIN
                }, BLUETOOTH_PERMISSION_REQUEST_CODE);

                // Make the link clickable. Needs to be called after show(), in order to generate hyperlinks
                isPermisionGranted = false;
            } else {
                isPermisionGranted = true;
                Toast.makeText(parentActivity, "granted" + pCheck, Toast.LENGTH_SHORT).show();

//                    turnOnLocation();
            }

        } else {
            //Aready Granted on Installtion
            isPermisionGranted = true;
        }
        return isPermisionGranted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, turn on location
                turnOnLocation();
            } else {
                // Permission denied
                // Handle accordingly, maybe show a message to the user
            }
        }

        // ADD THIS: Call Permission Listener
        if (requestCode == CALL_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted! Toggle the switch ON
                if (btnCallPermission != null) {
                    btnCallPermission.setChecked(true);
                    //  btnCallPermission.setEnabled(false);
                    //   Toast.makeText(mContext, "Call Permission Granted", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Permission denied! Toggle the switch OFF
                if (btnCallPermission != null) {
                    btnCallPermission.setChecked(false);
                    //  Toast.makeText(mContext, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }

        if (requestCode == BLUETOOTH_PERMISSION_REQUEST_CODE) {
            // Check if all permissions are granted
            boolean allPermissionsGranted = true;
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    // Permission not granted, handle accordingly
                    allPermissionsGranted = false;
                    handlePermissionDenied(permissions[i]);
                }
            }

            if (allPermissionsGranted) {
                // All permissions granted, proceed with your app logic
            }
        }
    }

    private void turnOnLocation() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        LocationServices.getSettingsClient(parentActivity)
                .checkLocationSettings(builder.build())
                .addOnSuccessListener(parentActivity, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        // Location settings satisfied, location is turned on
                        // Proceed with your app logic
                        if (btnLocation != null) btnLocation.setChecked(true);
                        if (btnLocationPermission != null) btnLocationPermission.setChecked(true);


                    }
                })
                .addOnFailureListener(parentActivity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof ResolvableApiException) {
                            // Location settings are not satisfied, but this can be fixed by showing the user a dialog.
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                ResolvableApiException resolvable = (ResolvableApiException) e;
                                resolvable.startResolutionForResult(parentActivity, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sendEx) {
                                // Ignore the error.
                            }
                        }
                    }
                });
    }

    private void turnOnBluetooth() {
        // Check if Bluetooth is supported on the device

        //  Initialize BluetoothAdapter
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Check if Bluetooth is supported on the device
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            // Handle accordingly, maybe show a message to the user
        } else {
            // Bluetooth is supported, check if it's enabled
            if (!bluetoothAdapter.isEnabled()) {
                // Bluetooth is not enabled, prompt the user to enable it
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                // Bluetooth is already enabled, proceed with your app logic
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Remove location updates to prevent memory leaks
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates((android.location.LocationListener) locationListener);
        }

        if (isReceiverRegistered) {
            try {
                // Unregister the receiver when the activity is destroyed
                parentActivity.unregisterReceiver(locationProviderReceiver);
                isReceiverRegistered = false;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void registerLocationProviderReceiver() {
        locationProviderReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() != null && intent.getAction().equals(LocationManager.PROVIDERS_CHANGED_ACTION)) {
                    // Location provider status changed


                    if (!isLocationProviderEnabled()) {
                        // Location provider is disabled, handle accordingly
                        // Maybe prompt the user to enable location services
                        if (btnLocation != null) btnLocation.setChecked(false);
                        if (btnLocationPermission != null) btnLocationPermission.setChecked(false);

                    } else {
                        if (btnLocation != null) btnLocation.setChecked(true);
                        if (btnLocationPermission != null) btnLocationPermission.setChecked(true);


                    }
                }
            }
        };
        if (!isReceiverRegistered) {

            // Register the receiver to listen for changes in location provider status
            IntentFilter intentFilter = new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION);
            parentActivity.registerReceiver(locationProviderReceiver, intentFilter);
            isReceiverRegistered = true;
        }
    }

    private boolean hasCallPermission() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(parentActivity,
                    new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION_REQUEST_CODE);
            return false; // Permission not granted yet
        }
        return true; // Permission already granted
    }

    private boolean isLocationProviderEnabled() {
        LocationManager locationManager = (LocationManager) parentActivity.getSystemService(LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == RESULT_OK) {
                // User enabled location, proceed with your app logic
                if (btnLocation != null) btnLocation.setChecked(true);
                if (btnLocationPermission != null) btnLocationPermission.setChecked(true);



            } else {
                // User cancelled or did not enable location, handle accordingly
                if (btnLocation != null) btnLocation.setChecked(false);
                if (btnLocationPermission != null) btnLocationPermission.setChecked(false);
            }
        }
    }

    private void handlePermissionDenied(String permission) {
        // Handle the denied permission here
        // You can show a message to the user or take appropriate action
        switch (permission) {
            case Manifest.permission.BLUETOOTH:
                // Handle BLUETOOTH permission denied
//                Toast.makeText(mContext,"Not 1",Toast.LENGTH_SHORT).show();
                break;
            case Manifest.permission.BLUETOOTH_SCAN:
                // Handle BLUETOOTH_SCAN permission denied
//                Toast.makeText(mContext,"Not 2",Toast.LENGTH_SHORT).show();
                break;
            case Manifest.permission.BLUETOOTH_ADVERTISE:
                // Handle BLUETOOTH_ADVERTISE permission denied
//                Toast.makeText(mContext,"Not 3",Toast.LENGTH_SHORT).show();
                break;
            case Manifest.permission.BLUETOOTH_CONNECT:
                // Handle BLUETOOTH_CONNECT permission denied
//                Toast.makeText(mContext,"Not 4",Toast.LENGTH_SHORT).show();
                break;
            case Manifest.permission.BLUETOOTH_ADMIN:
                // Handle BLUETOOTH_ADMIN permission denied
//                Toast.makeText(mContext,"Not 5",Toast.LENGTH_SHORT).show();
                break;
        }
    }


    public void setDefaultConfigs()
    {

// Set fetch interval (important!)
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600) // use 0 during debug only
                .build();
        mFirebaseConfig.setConfigSettingsAsync(configSettings);

// Initial fetch on startup
        mFirebaseConfig.fetchAndActivate().addOnCompleteListener(parentActivity, task -> {
            if (task.isSuccessful()) {
                applyConfigs(); // centralized method (see below)
            } else {
                Log.w("ZeConfigs", "Initial fetch failed, using cached/default values");
                applyConfigs(); // still apply whatever is cached
            }
        });

// Realtime listener as a bonus on top of the fetch above
        mFirebaseConfig.addOnConfigUpdateListener(new ConfigUpdateListener() {
            @Override
            public void onUpdate(@NonNull ConfigUpdate configUpdate) {
                Log.d("ZeConfigs", "Realtime update received for keys: " + configUpdate.getUpdatedKeys());

                // fetch() + activate() to actually get the new values
                mFirebaseConfig.fetchAndActivate().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        applyConfigs();
                    }
                });
            }

            @Override
            public void onError(@NonNull FirebaseRemoteConfigException error) {
                Log.w("ZeConfigs", "Stream error: " + error.getCode());

                if (error.getCode() == FirebaseRemoteConfigException.Code.CONFIG_UPDATE_STREAM_ERROR) {
                    // Stream is unavailable (no network, firewall, etc.)
                    // The SDK will retry automatically — don't do anything here
                    // Your fetchAndActivate() at startup already covers you
                }
            }
        });
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

                        // Unbind listener, change state, rebind listener
                        switchFingerprint.setOnCheckedChangeListener(null);
                        switchFingerprint.setChecked(false);
                        switchFingerprint.setOnCheckedChangeListener(fingerprintListener);
                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        storageObject.saveUSerFingeprintEnabled(true);

                        switchFingerprint.setOnCheckedChangeListener(null);
                        switchFingerprint.setChecked(true);
                        switchFingerprint.setOnCheckedChangeListener(fingerprintListener);
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        storageObject.saveUSerFingeprintEnabled(false);

                        switchFingerprint.setOnCheckedChangeListener(null);
                        switchFingerprint.setChecked(false);
                        switchFingerprint.setOnCheckedChangeListener(fingerprintListener);
                    }
                });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(mContext.getResources().getString(R.string.loginwithfingerprint))
                .setSubtitle(mContext.getResources().getString(R.string.touchfingerprinttoauthenticate))
                .setNegativeButtonText(mContext.getResources().getString(R.string.cancel))
                .build();

        biometricPrompt.authenticate(promptInfo);
    }
    public void showProgress() {
        contentView.setVisibility(View.GONE);
        loadView.setVisibility(View.VISIBLE);
        completeView.setVisibility(View.GONE);

        try {


            Glide.with(parentActivity)
                    .asGif()
                    .load(R.drawable.loader) // Replace with your local GIF resource
                    .into(progressWheel);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public void showCompleteView() {
        contentView.setVisibility(View.GONE);
        loadView.setVisibility(View.GONE);
        completeView.setVisibility(View.VISIBLE);

//        view1.setBackgroundResource(R.xml.rounded_list_button);
//        view2.setBackgroundResource(R.xml.rounded_list_button);
//        view3.setBackgroundResource(R.xml.rounded_list_button);
//
//        div2.setBackgroundResource(R.xml.rounded_list_button);
//
//        header.setText(mContext.getResources().getString(R.string.setupcomplete));
//        titleText.setText(mContext.getResources().getString(R.string.deviceready));

    }


    public void goToDashboard() {

        storageObject.setPermissionsWizardComplete(true);
        GeneralCommands.logEvent(parentActivity, "click_button_done_permissions", "click_button_done_permissions");
        //Proceed
        GetData ob1 = new GetData();
        ob1.initialize(parentActivity, loadingView);
        ob1.userLogin(storageObject.retrieveUserPhoneNumberFromPrefs(), storageObject.retrieveUserPasswordFromPrefs());

    }


    // Put all your config reads in one place
    private void applyConfigs() {
        should_force_all_permissions = mFirebaseConfig.getBoolean("should_force_all_permissions");
        // add all other config keys here...
        Log.d("ZeConfigs", "Configs applied");
    }
}