package com.watudrc.app.util;

//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
//import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
        import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
        import com.watudrc.app.R;

public class FirebaseConfig {
   FirebaseRemoteConfig mFirebaseRemoteConfig;
   FirebaseRemoteConfigSettings configSettings;
   long cacheExpiration = 0;
   String theme_color = "";
   public FirebaseConfig() {
      mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
      configSettings = new FirebaseRemoteConfigSettings.Builder()
              .setMinimumFetchIntervalInSeconds(3600)
              .build();
      mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);
      mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

   }
   public FirebaseRemoteConfig getConfigs(){




     return mFirebaseRemoteConfig;
   }
   public long getCacheExpiration() {
       return cacheExpiration;
   }
}
