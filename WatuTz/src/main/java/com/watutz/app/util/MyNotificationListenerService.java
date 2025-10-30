package com.watutz.app.util;

import android.content.SharedPreferences;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class MyNotificationListenerService extends NotificationListenerService {
   @Override
   public void onNotificationPosted(StatusBarNotification sbn) {
      SharedPreferences prefs = getSharedPreferences("events", MODE_PRIVATE);
      int rec = prefs.getInt("notification_received", 0);
      prefs.edit()
              .putInt("notification_received", rec + 1)
              .apply();
   }
   @Override
   public void onNotificationRemoved(StatusBarNotification sbn) {
      SharedPreferences prefs = getSharedPreferences("events", MODE_PRIVATE);
      int dis = prefs.getInt("notification_dismissed", 0);
      prefs.edit()
              .putInt("notification_dismissed", dis + 1)
              .apply();
   }
}
