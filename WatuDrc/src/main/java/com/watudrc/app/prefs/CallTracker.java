package com.watudrc.app.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.watudrc.app.TinyDB;

import java.util.Calendar;

public class CallTracker {

    private static final String PREFS_NAME = "CallThrottlePrefs";
    private static final String KEY_CALL_COUNT = "callCount";
    private static final String KEY_COOLDOWN_END = "cooldownEnd";
    private static final String KEY_LAST_CALL_DAY = "lastCallDay";

    private static final String KEY_SOFT_CALL_LIMIT = "softCallLimit";
    private static final String KEY_HARD_CALL_LIMIT = "hardCallLimit";
    private static final String KEY_SOFT_COOL_DOWN_TIME_MINS = "softCoolDownTimeMins";
    private static final String KEY_CALL_FEATURE_ENABLED = "callFeatureEnabled";
    private static final String KEY_support_care_number = "callNumber";

    private final SharedPreferences prefs;

    private  Context mContext;

    static TinyDB tinyDb;

    public CallTracker(Context context) {

        this.mContext = context;
        tinyDb = new TinyDB(this.mContext);

        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }




    public enum CallStatus {
        ALLOWED,
        COOLDOWN,
        DAILY_LIMIT
    }

    // Check if user can make a call
    public CallStatus canMakeCall() {
        resetIfNewDay();

        long now = System.currentTimeMillis();
        long cooldownEnd = prefs.getLong(KEY_COOLDOWN_END, 0);
        int callCount = prefs.getInt(KEY_CALL_COUNT, 0);

        if (cooldownEnd > now) {
            return CallStatus.COOLDOWN;
        } else if (callCount >= 5) {
            return CallStatus.DAILY_LIMIT;
        } else {
            return CallStatus.ALLOWED;
        }
    }

    public void resetCallCount(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();

    }

    // Record a call event
    public void recordCall() {
        resetIfNewDay();

        int callCount = prefs.getInt(KEY_CALL_COUNT, 0) + 1;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_CALL_COUNT, callCount);
        editor.putInt(KEY_LAST_CALL_DAY, getTodayDayOfYear());

        // Rule 1: After 2 consecutive calls, enforce 10 min cooldown
        if (callCount == 2) {
            long cooldownEnd = System.currentTimeMillis() + (10 * 60 * 1000);
            editor.putLong(KEY_COOLDOWN_END, cooldownEnd);
        }
        // Rule 2: After total 5 calls, block until midnight
        else if (callCount >= 5) {
            editor.putLong(KEY_COOLDOWN_END, getMidnightMillis());
        } else {
            editor.putLong(KEY_COOLDOWN_END, 0);
        }

        editor.apply();
    }

    // Helper: Reset daily limits
    private void resetIfNewDay() {
        int lastDay = prefs.getInt(KEY_LAST_CALL_DAY, -1);
        int today = getTodayDayOfYear();

        if (lastDay != today) {
            prefs.edit()
                    .putInt(KEY_CALL_COUNT, 0)
                    .putLong(KEY_COOLDOWN_END, 0)
                    .putInt(KEY_LAST_CALL_DAY, today)
                    .apply();
        }
    }

    // Helper: Get today’s day of year
    private int getTodayDayOfYear() {
        return Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
    }

    // Helper: Get midnight timestamp
    public long getMidnightMillis() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    // Optional: Get remaining cooldown seconds
    public long getRemainingCooldownSeconds() {
        long diff = prefs.getLong(KEY_COOLDOWN_END, 0) - System.currentTimeMillis();
        return Math.max(diff / 1000, 0);
    }

    public static void saveSoftCoolDownLimit(int softCoolDownLimit) {
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putInt(KEY_SOFT_CALL_LIMIT, softCoolDownLimit);
//        editor.apply();
        tinyDb.putInt(KEY_SOFT_CALL_LIMIT, softCoolDownLimit);
    }

    public static int getSoftCoolDownLimit() {

//        return prefs.getInt(KEY_SOFT_CALL_LIMIT, 0);

       return tinyDb.getInt(KEY_SOFT_CALL_LIMIT);
    }

    public static void saveHardCoolDownLimit(int hardCoolDownLimit) {
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putInt(KEY_HARD_CALL_LIMIT, hardCoolDownLimit);
//        editor.apply();
        tinyDb.putInt(KEY_HARD_CALL_LIMIT, hardCoolDownLimit);
    }

    public static int getHardCoolDownLimit() {
//        return prefs.getInt(KEY_HARD_CALL_LIMIT, 0);
        return tinyDb.getInt(KEY_HARD_CALL_LIMIT);
    }

    public static void saveSoftCoolDownTimeInMins(int softCoolDownTimeInMins) {
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putInt(KEY_SOFT_COOL_DOWN_TIME_MINS, softCoolDownTimeInMins);
//        editor.apply();

        tinyDb.putInt(KEY_SOFT_COOL_DOWN_TIME_MINS, softCoolDownTimeInMins);
    }

    public static int getSoftCoolDownTimeInMins() {
//        return prefs.getInt(KEY_SOFT_COOL_DOWN_TIME_MINS, 0);
        return tinyDb.getInt(KEY_SOFT_COOL_DOWN_TIME_MINS);
    }

    public static void saveSupportNumber(String supportNumber) {
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putString(KEY_support_care_number, supportNumber);
//        editor.apply();
        tinyDb.putString(KEY_support_care_number, supportNumber);

    }

    public static String getSupportNumber() {
//        return prefs.getInt(KEY_support_care_number, 0);
        return tinyDb.getString(KEY_support_care_number);
    }


    public static void saveIsCallEnabled(boolean isCallEnabled) {
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.putBoolean(KEY_CALL_FEATURE_ENABLED, isCallEnabled);
//        editor.apply();
        tinyDb.putBoolean(KEY_CALL_FEATURE_ENABLED,isCallEnabled);
    }

    public static boolean getIsCallEnabled() {
//        return prefs.getInt(KEY_CALL_FEATURE_ENABLED, 0);
        return tinyDb.getBoolean(KEY_CALL_FEATURE_ENABLED);
    }
}

