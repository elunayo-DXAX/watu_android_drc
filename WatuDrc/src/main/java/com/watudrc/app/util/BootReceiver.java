package com.watudrc.app.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
   @Override
   public void onReceive(Context ctx, Intent intent) {
      ctx.getSharedPreferences("events", Context.MODE_PRIVATE)
              .edit()
              .putLong("boot_successful", System.currentTimeMillis())
              .apply();
   }
}
