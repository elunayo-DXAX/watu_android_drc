package com.watudrc.app.util;

import android.util.Log;

public class Logger {
   public static void d(String tag, String msg) {
      if (msg != null) Log.d(tag, msg);
      else Log.d(tag, "null");
   }


   public static void d(String tag, String msg, Throwable tr) {
      if (msg != null) Log.d(tag, msg ,tr);
      else Log.d(tag, "null");
   }
}
