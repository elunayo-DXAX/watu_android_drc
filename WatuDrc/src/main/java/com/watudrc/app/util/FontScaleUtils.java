package com.watudrc.app.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;

public class FontScaleUtils {
   public static ContextWrapper wrap(Context context) {
      Configuration configuration = context.getResources().getConfiguration();
      if (configuration.fontScale != 0.8f) {
         configuration.fontScale = 0.8f;
         context = context.createConfigurationContext(configuration);
      }
      return new ContextWrapper(context);
   }
}
