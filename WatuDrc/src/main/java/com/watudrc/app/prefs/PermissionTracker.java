package com.watudrc.app.prefs;

import android.content.Context;
import android.content.SharedPreferences;

public class PermissionTracker {

    private static final String PREF_NAME = "permission_tracker_prefs";
    private static final String KEY_CALL_PERMISSION_COUNT = "call_permission_count";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    // Increment count when permission request is shown
    public static void incrementCallPermissionRequest(Context context) {
        SharedPreferences prefs = getPrefs(context);
        int count = prefs.getInt(KEY_CALL_PERMISSION_COUNT, 0) + 1;
        prefs.edit().putInt(KEY_CALL_PERMISSION_COUNT, count).apply();
    }

    // Get current count
    public static int getCallPermissionRequestCount(Context context) {
        return getPrefs(context).getInt(KEY_CALL_PERMISSION_COUNT, 0);
    }
}

