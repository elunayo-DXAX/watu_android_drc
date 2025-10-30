package com.watutz.app.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class ClickReceiver extends BroadcastReceiver {
   @Override
   public void onReceive(Context ctx, Intent intent) {
      SharedPreferences prefs = ctx.getSharedPreferences("events", Context.MODE_PRIVATE);
      int clicks = prefs.getInt("notification_clicked", 0);
      prefs.edit()
              .putInt("notification_clicked", clicks + 1)
              .apply();
   }
}
