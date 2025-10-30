package com.watutz.app.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class UnlockReceiver extends BroadcastReceiver {
   @Override
   public void onReceive(Context ctx, Intent intent) {
      ctx.getSharedPreferences("events", Context.MODE_PRIVATE)
              .edit()
              .putLong("phone_unlocked", System.currentTimeMillis())
              .apply();

      Log.d("ZeSecondaryUnloc", "ZeSecondary got USER_PRESENT");
   }
}
