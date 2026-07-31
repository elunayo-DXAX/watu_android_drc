package com.watudrc.app.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LocaleUtils {

   public static Context setLocale(Context context, String language) {
      Locale locale = new Locale(language);
      Locale.setDefault(locale);

      Resources resources = context.getResources();
      Configuration configuration = resources.getConfiguration();
      configuration.setLocale(locale);
      configuration.setLayoutDirection(locale);

      context.getApplicationContext().createConfigurationContext(configuration);
      resources.updateConfiguration(configuration, resources.getDisplayMetrics());
      return context;
   }
}

