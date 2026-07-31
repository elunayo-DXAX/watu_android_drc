package com.watudrc.app.util;

import android.content.SharedPreferences;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

public class MyNotificationListenerService extends NotificationListenerService {
   @Override
   public void onNotificationPosted(StatusBarNotification sbn) {
      SharedPreferences prefs = getSharedPreferences("events", MODE_PRIVATE);
       try {
           long rec = prefs.getLong("notification_received", 0);
           prefs.edit()
                   .putLong("notification_received", rec + 1)
                   .apply();
       } catch (Exception e) {
          e.printStackTrace();
//           throw new RuntimeException(e);
       }
   }
   @Override
   public void onNotificationRemoved(StatusBarNotification sbn) {
      SharedPreferences prefs = getSharedPreferences("events", MODE_PRIVATE);
       try {
           long dis = prefs.getLong("notification_dismissed", 0);
           prefs.edit()
                   .putLong("notification_dismissed", dis + 1)
                   .apply();
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
}
