
package com.watudrc.app.prefs;


import android.content.Context;
import android.content.SharedPreferences;

public final class RateAppPrefs {
   private static final String PREF = "rate_app_prefs";

   static StoreAndGetFromPrefs storageObject;
   private static final String K_INSTALL_TS = "install_ts";
   private static final String K_LAUNCHES = "launches";
   private static final String K_ENJOY_PROMPT_SHOWN = "enjoy_prompt_shown";
   private static final String K_DO_NOT_ASK = "do_not_ask";
   private static final String K_RATED = "rated";
   private static final String K_NEXT_ELIGIBLE_AT = "next_eligible_at";

   // Tune these
   private static final int MIN_LAUNCHES = 3;      // at least 3 app opens

   private static SharedPreferences p(Context c){ return c.getSharedPreferences(PREF, Context.MODE_PRIVATE); }
   private static long now(){ return System.currentTimeMillis(); }

   public static void onAppLaunch(Context c){
      SharedPreferences sp = p(c);
      storageObject = new StoreAndGetFromPrefs(c);
      if (!sp.contains(K_INSTALL_TS)) sp.edit().putLong(K_INSTALL_TS, now()).apply();
      sp.edit().putInt(K_LAUNCHES, sp.getInt(K_LAUNCHES, 0) + 1).apply();
   }

   public static boolean isEligibleForEnjoyPrompt(Context c){
      SharedPreferences sp = p(c);
      if (sp.getBoolean(K_DO_NOT_ASK, false)) return false;
      if (sp.getBoolean(K_RATED, false)) return false;                // already rated
      if (sp.getBoolean(K_ENJOY_PROMPT_SHOWN, false)) return false;   // only show once


      long nextEligibleAt = sp.getLong(K_NEXT_ELIGIBLE_AT, 0);
      if (nextEligibleAt > 0 && now() < nextEligibleAt) return false; // still in backoff window

      // basic heuristics: launches OR days since install
      int launches = sp.getInt(K_LAUNCHES, 0);
      int MIN_DAYS_SINCE_INSTALL = storageObject.retrieveRatingMinDaysFromPrefs();
      long installTs = sp.getLong(K_INSTALL_TS, now());
      long daysSinceInstall = (now() - installTs) / (24L*60*60*1000);

      return (launches >= MIN_LAUNCHES) || (daysSinceInstall >= MIN_DAYS_SINCE_INSTALL);
   }

   public static void markEnjoyPromptShown(Context c){
      p(c).edit().putBoolean(K_ENJOY_PROMPT_SHOWN, true).apply();
   }

   public static void disableRatingPrompt(Context c){
      p(c).edit().putBoolean(K_DO_NOT_ASK, true).apply();
   }

   public static void markRated(Context c){
      p(c).edit().putBoolean(K_RATED, true).putBoolean(K_DO_NOT_ASK, true).apply();
   }

   public static void backoff(Context c, long days){
      long next = now() + days*24L*60*60*1000;
      p(c).edit().putLong(K_NEXT_ELIGIBLE_AT, next).apply();
   }

   // Optional: if you really need to give yourself a way to test again during QA
   public static void resetForTesting(Context c){
      p(c).edit().clear().apply();
   }
}
