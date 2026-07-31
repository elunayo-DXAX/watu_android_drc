package com.watudrc.app.util;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.os.StatFs;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.mobileservices.GetData;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DataCollector implements IWhereMyLocationConstants {
    private final Context ctx;
    private final FusedLocationProviderClient fusedLoc;
    private final SharedPreferences sp;
    private final StoreAndGetFromPrefs storageObject;
    String eventType = "";

    public DataCollector(Context context, String evType) {
        this.ctx = context.getApplicationContext();
        this.fusedLoc = LocationServices.getFusedLocationProviderClient(ctx);
        this.sp = ctx.getSharedPreferences("events", Context.MODE_PRIVATE);
        this.storageObject = new StoreAndGetFromPrefs(ctx);
        this.eventType = evType;


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void collectAndSend() {


        final JSONObject json = new JSONObject();
//      Toast.makeText(ctx, "ZeCollectingStarting", Toast.LENGTH_SHORT).show();
        Log.d("ZeCollectingStarting", "ZeCollectingStarting");


        if (!storageObject.shouldLogDataUsageEveryLaunchFromPrefs()) {
            //Check if 24 Hours have Passed.
            long lastLogSent = storageObject.retrieveLastLogSendDate();
            // ➋ Read the last timestamp (0 if never set)

            // ➌ Convert to Calendar and extract Y/M/D
            Calendar lastCal = Calendar.getInstance();
            if (lastLogSent > 0) {
                lastCal.setTimeInMillis(lastLogSent);
            }
            int lastYear = lastCal.get(Calendar.YEAR);
            int lastMonth = lastCal.get(Calendar.MONTH);
            int lastDay = lastCal.get(Calendar.DAY_OF_MONTH);

            // ➍ Get today’s Y/M/D
            Calendar todayCal = Calendar.getInstance();
            int todayYear = todayCal.get(Calendar.YEAR);
            int todayMonth = todayCal.get(Calendar.MONTH);
            int todayDay = todayCal.get(Calendar.DAY_OF_MONTH);

//          Toast.makeText(ctx, "Already collected today"+lastLogSent+":"+lastDay+"/"+lastMonth+"/"+lastYear, Toast.LENGTH_SHORT).show();


            // ➎ If we have a valid lastTs and it’s the same Y/M/D as today, skip
            if (lastLogSent > 0 && lastYear == todayYear && lastMonth == todayMonth && lastDay == todayDay) {
                //Last Log Already Sent Today
                // Toast.makeText(ctx, "Already collected today"+lastDay, Toast.LENGTH_SHORT).show();

//                     Toast.makeText(ctx, "Already collected today skip"+lastLogSent+":"+lastDay+"/"+lastMonth+"/"+lastYear, Toast.LENGTH_SHORT).show();

                Log.d("ZeSecondary", "ZeCollectSecondarySkipping collectAndSend (already sent today)");
                return;
            }


            // … otherwise proceed with data collection …

        }

        // ── PRIMARY DATA ───────────────────────────────────────────────────────
        try {
            json.put("eventType", eventType);
            json.put("timestamp", System.currentTimeMillis());


            JSONObject user = new JSONObject();


            user.put("androidId", storageObject.retrieveAndroidIDObjectFromPrefs());
            user.put("deviceId", storageObject.retrieveDeviceIDFromPrefs());
            user.put("clientID", storageObject.retrieveUserPhoneNumberFromPrefs());

            json.put("userIdentifiers", user);


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        // ── SECONDARY DATA ─────────────────────────────────────────────────────
        collectSecondary(json);

        // ── LOCATION (async, permission-guarded) ───────────────────────────────


        fetchLocationAndSend(json);
//        sendPayload(json);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void collectNetwork(JSONObject json) {
        try {
            ConnectivityManager cm = ctx.getSystemService(ConnectivityManager.class);
            boolean deviceOnline = cm.getActiveNetworkInfo() != null
                    && cm.getActiveNetworkInfo().isConnected();
            boolean wifiConnected = cm.getActiveNetworkInfo() != null
                    && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

            String ipAddr = getLocalIpAddress();
            if (ipAddr.isEmpty()) ipAddr = "unknown";

            json.put("deviceOnlineTimestamp", deviceOnline)
                    .put("wifiConnectedTimestamp", wifiConnected)
                    .put("ipAddress", ipAddr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getLocalIpAddress() {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                for (InetAddress addr : Collections.list(intf.getInetAddresses())) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        if (sAddr.indexOf(':') < 0) // IPv4
                            return sAddr;
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return "";
    }

    private void collectAppEvents(JSONObject json) {
        try {
            json.put("bootSuccessfulTimestamp", sp.getLong("boot_successful", -1))
                    .put("phoneUnlockedTimestamp", sp.getLong("phone_unlocked", -1))
                    .put("appStartedTimestamp", sp.getLong("app_started", -1))
                    .put("appResumeTimestamp", sp.getLong("app_resume", -1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void collectNotifications(JSONObject json) {
        try {
            json.put("notificationReceivedTimestamp", sp.getLong("notification_received", 0))
                    .put("notificationClickedTimestamp", sp.getLong("notification_clicked", 0))
                    .put("notificationDismissedTimestamp", sp.getLong("notification_dismissed", 0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void collectSecondary(JSONObject json) {
//      Toast.makeText(ctx, "ZeCollectSecondary", Toast.LENGTH_SHORT).show();
        Log.d("ZeCollectSecondary", "ZeCollectSecondary" + json.toString());
        JSONObject sec = new JSONObject();
        try {


            // Device & OS
            sec.put("deviceModel", Build.MODEL)
                    .put("deviceBrand", Build.BRAND)
                    .put("deviceManufacturer", Build.MANUFACTURER)
                    .put("deviceCodename", Build.DEVICE)
                    .put("deviceProduct", Build.PRODUCT)
                    .put("deviceHardware", Build.HARDWARE)
                    .put("osVersion", Build.VERSION.RELEASE)
                    .put("sdkInt", Build.VERSION.SDK_INT);
//                 .put("security_patch", Build.VERSION.SECURITY_PATCH)
//                 .put("build_fingerprint", Build.FINGERPRINT)
//                 .put("supported_abis", new JSONArray(Arrays.asList(Build.SUPPORTED_ABIS)));

            // RAM
            ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
            am.getMemoryInfo(mi);
            sec.put("internalRAM", mi.totalMem)
                    .put("internalRAMAvailable", mi.availMem);

            // Screen
            DisplayMetrics dm = new DisplayMetrics();
            ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay().getMetrics(dm);
//         sec.put("screen_width_px", dm.widthPixels)
//                 .put("screen_height_px", dm.heightPixels)
//                 .put("screen_dpi", dm.densityDpi);

            // Storage
            StatFs stat = new StatFs(ctx.getFilesDir().getAbsolutePath());
            long total = stat.getBlockCountLong() * stat.getBlockSizeLong();
            long avail = stat.getAvailableBlocksLong() * stat.getBlockSizeLong();
            sec.put("internalStorage", total)
                    .put("internalStorageAvailable", avail)
                    .put("externalStorage", Environment.getExternalStorageState());

            // Battery
            IntentFilter ifilt = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batt = ctx.registerReceiver(null, ifilt);
            int lvl = batt.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int sc = batt.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int st = batt.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            int tmp = batt.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
            int volt = batt.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
            int plug = batt.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);

            float pct = (lvl >= 0 && sc > 0) ? (lvl / (float) sc) * 100f : -1f;
            String chg = (st == BatteryManager.BATTERY_STATUS_CHARGING) ? "charging"
                    : (st == BatteryManager.BATTERY_STATUS_FULL) ? "full"
                    : (st == BatteryManager.BATTERY_STATUS_DISCHARGING) ? "discharging"
                    : "unknown";
            String src = (plug == BatteryManager.BATTERY_PLUGGED_USB) ? "usb"
                    : (plug == BatteryManager.BATTERY_PLUGGED_AC) ? "ac"
                    : (plug == BatteryManager.BATTERY_PLUGGED_WIRELESS) ? "wireless"
                    : "none";

            sec.put("batteryLevel", pct);
//                 .put("charging_status", chg)
//                 .put("battery_temp_c", tmp / 10f)
//                 .put("battery_voltage_mv", volt)
//                 .put("power_source", src);

            // Camera info
            CameraManager camMgr = ctx.getSystemService(CameraManager.class);
            String[] camIds = camMgr.getCameraIdList();
//         sec.put("camera_count", camIds.length);
            JSONArray cams = new JSONArray();
            for (String id : camIds) {
                CameraCharacteristics c = camMgr.getCameraCharacteristics(id);
                JSONObject cj = new JSONObject();
//            cj.put("facing", c.get(CameraCharacteristics.LENS_FACING));
//            cj.put("flash_available", c.get(CameraCharacteristics.FLASH_INFO_AVAILABLE));
//            cj.put("hw_level", c.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL));

                StreamConfigurationMap map = c.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
//            cj.put("formats", new JSONArray(Arrays.asList(map.getOutputFormats())));
                JSONArray res = new JSONArray();
                for (android.util.Size s : map.getOutputSizes(android.graphics.ImageFormat.JPEG)) {
                    res.put(s.getWidth() + "x" + s.getHeight());
                }
//            cj.put("resolutions", res);
                cams.put(cj);
            }
//         sec.put("cameras", cams);

            // App Context
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), 0);
            sec.put("appVersion", pi.versionName);
            String installer = pm.getInstallerPackageName(ctx.getPackageName());
            if (installer == null) {
                installer = "unknown";
            }
            sec.put("installationSource", installer);
            sec.put("appUptimeMs", SystemClock.elapsedRealtime());

            // Locale / Language
            Locale loc = Locale.getDefault();
            sec.put("language", loc.getLanguage());
            sec.put("locale", loc.toLanguageTag());

            // Security / Integrity
            sec.put("isRooted", isDeviceRooted());
            sec.put("isEmulator", isEmulator());
            int devMode = Settings.Secure.getInt(
                    ctx.getContentResolver(),
                    Settings.Secure.DEVELOPMENT_SETTINGS_ENABLED,
                    0
            );
            sec.put("developerModeEnabled", devMode == 1);

            // Network & Environment
            TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            sec.put("carrierName", tm.getNetworkOperatorName());
//         if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            sec.put("networkType", tm.getNetworkType());
//
//            return;
//         }

            ConnectivityManager cm = ctx.getSystemService(ConnectivityManager.class);
            NetworkCapabilities caps = cm.getNetworkCapabilities(cm.getActiveNetwork());
            boolean onCellular = caps != null &&
                    caps.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
            sec.put("networkType", !onCellular ? "wifi" : "cellular");

            ConnectivityManager conn = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkCapabilities nc = conn.getNetworkCapabilities(conn.getActiveNetwork());
            boolean vpn = nc != null && nc.hasTransport(NetworkCapabilities.TRANSPORT_VPN);
            sec.put("vpnActive", vpn);
            String tz = TimeZone.getDefault().getID();
            sec.put("timeZone", tz);
            sec.put("timezoneLocationMismatch", false);
            sec.put("timeLastOnline", storageObject.retrieveTimeLastOnline());


            collectNetwork(sec);
            collectAppEvents(sec);
            collectNotifications(sec);


            Log.d("ZeCollectLocationSTart", "ZeCollectSecondary+ZeCollectLocationSTart" + json.toString());

            //Location Info
            SharedPreferences prefs = ctx.getSharedPreferences("events", Context.MODE_PRIVATE);
            String lastLat = prefs.getString("current_location_latitude", "");
            String lastLon = prefs.getString("current_location_longitude", "");
            boolean hasLastKnown = !lastLat.isEmpty() && !lastLon.isEmpty();

            if (hasLastKnown) {

                try {
                    sec.put("currentLocationLatitude", Double.parseDouble(lastLat));
                    sec.put("currentLocationLongitude", Double.parseDouble(lastLon));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            // Attach and send
            json.put("deviceSignals", sec);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendPayload(JSONObject json) {

        Log.d("ZeCollectSecondaryFinal", "ZeSecondaryDataCollectionFinal " + json.toString());

        GetData ob1 = new GetData();
        ob1.initializeSilentContext(ctx);
        ob1.userPushData(json, true);
        storageObject.saveLastLogSendDate(System.currentTimeMillis());//Update Last Log Sent Date

    }

    // ── Helpers ──────────────────────────────────────────────────────────────
    private boolean isDeviceRooted() {
        String tags = Build.TAGS;
        if (tags != null && tags.contains("test-keys")) return true;
        return new File("/system/app/Superuser.apk").exists()
                || new File("/system/xbin/su").exists();
    }

    private boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.MODEL.contains("Emulator")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void fetchLocationAndSend(final JSONObject json) {
        // Ensure deviceSignals exists so we don't crash
        JSONObject deviceSignals = json.optJSONObject("deviceSignals");
        if (deviceSignals == null) {
            deviceSignals = new JSONObject();
            safePut(json, "deviceSignals", deviceSignals);
        }

        // 1. Check Permissions: If missing, put NULL and send payload immediately
        if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            safePut(deviceSignals, "currentLocationLatitude", JSONObject.NULL);
            safePut(deviceSignals, "currentLocationLongitude", JSONObject.NULL);
            sendPayload(json);
            return;
        }

        // 2. Try to get cached location
        final JSONObject finalDeviceSignals = deviceSignals;
        fusedLoc.getLastLocation()
                .addOnSuccessListener(loc -> {
                    if (loc != null) {
                        safePut(finalDeviceSignals, "currentLocationLatitude", loc.getLatitude());
                        safePut(finalDeviceSignals, "currentLocationLongitude", loc.getLongitude());
                        sendPayload(json);
                    } else {
                        fallbackLocation(json, finalDeviceSignals);
                    }
                })
                .addOnFailureListener(e -> fallbackLocation(json, finalDeviceSignals));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void fallbackLocation(final JSONObject json, final JSONObject deviceSignals) {
        LocationManager lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        boolean gpsOn = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean netOn = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        // 3. Hardware check: If location tracking is turned off globally on the device,
        // safely pass NULLs. NO settings redirection.
        if (!gpsOn && !netOn) {
            safePut(deviceSignals, "currentLocationLatitude", JSONObject.NULL);
            safePut(deviceSignals, "currentLocationLongitude", JSONObject.NULL);
            sendPayload(json);
            return;
        }

        // 4. Double check permission wrapper before requesting fresh updates
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            safePut(deviceSignals, "currentLocationLatitude", JSONObject.NULL);
            safePut(deviceSignals, "currentLocationLongitude", JSONObject.NULL);
            sendPayload(json);
            return;
        }

        // 5. Check if we already have location stored to prevent redundant heavy network calls
        if (storageObject.retrieveCurrentLocationLatFromPrefs() == null && storageObject.retrieveCurrentLocationLongFromPrefs() == null) {

            LocationRequest req = LocationRequest.create()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setNumUpdates(1)
                    .setExpirationDuration(5000); // 5-second timeout safeguard

            fusedLoc.requestLocationUpdates(req, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult res) {
                    Location fresh = (res != null) ? res.getLastLocation() : null;
                    if (fresh != null) {
                        safePut(deviceSignals, "currentLocationLatitude", fresh.getLatitude());
                        safePut(deviceSignals, "currentLocationLongitude", fresh.getLongitude());

                        storageObject.saveCurrentLocationLatToPrefs(fresh.getLatitude());
                        storageObject.saveCurrentLocationLongToPrefs(fresh.getLongitude());
                    } else {
                        safePut(deviceSignals, "currentLocationLatitude", JSONObject.NULL);
                        safePut(deviceSignals, "currentLocationLongitude", JSONObject.NULL);
                    }
                    sendPayload(json);
                    fusedLoc.removeLocationUpdates(this);
                }
            }, Looper.getMainLooper());
        } else {
            // If they are already cached in storageObject, pull them or fall back to payload send
            sendPayload(json);
        }
    }

    private void safePut(JSONObject obj, String key, Object val) {
        try {
            obj.put(key, val);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
