package com.watutz.app;

import java.util.Locale;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;


import androidx.annotation.NonNull;

import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.watutz.app.constant.IWhereMyLocationConstants;
import com.watutz.app.prefs.StoreAndGetFromPrefs;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.infobip.mobile.messaging.MobileMessaging;
import org.infobip.mobile.messaging.chat.InAppChat;


public class Common extends Application implements IWhereMyLocationConstants{
	

	
	public static Typeface mFirmaFont, mFirmaFontBold;

	
	static StoreAndGetFromPrefs storageObject;
	
	private static SharedPreferences prefs;
	
	//Crash Report

	Context mContext;

	String androidId="";

	
	@Override
	public void onCreate() {
		super.onCreate();
		SharedObjects.context = this;
		mContext=SharedObjects.context;

		//Infobip
		new MobileMessaging
				.Builder(this)
				.withApplicationCode(getString(R.string.infobip_application_code))
				.build();

		InAppChat.getInstance(mContext).activate();
		
        storageObject = new StoreAndGetFromPrefs(getApplicationContext());
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
		mFirmaFont = Typeface.createFromAsset(getAssets(), "fonts/poppins-light.ttf");
		mFirmaFontBold = Typeface.createFromAsset(getAssets(), "fonts/poppins-bold.ttf");

//Data COllector Reccomended Privacy-Safe way to get Android ID
		FirebaseInstallations.getInstance()
				.getId()
				.addOnCompleteListener(new OnCompleteListener<String>() {
					@Override
					public void onComplete(@NonNull Task<String> task) {
						if (task.isSuccessful()) {
							androidId = task.getResult();
							storageObject.saveAndroidIDObjectToPrefs(androidId);

							Log.d("ZeFireMyApp", "ZeFirebase Installation ID = " + androidId);
							// TODO: send fid to your backend or persist it as needed
						} else {
							Log.e("ZeFireMyApp", "ZeFireFailed to get FID", task.getException());
						}
					}
				});
		FirebaseApp.initializeApp(this);
		FirebaseCrashlytics crash =  FirebaseCrashlytics.getInstance();
		//ACRA.init(this);
		// Create a trace (optional)
		Trace myTrace = FirebasePerformance.getInstance().newTrace("watu_trace");
		myTrace.start();

		ImageLoader.getInstance()
				.init(ImageLoaderConfiguration.createDefault(this));
        
        int REPEAT_TIME_IN_MINUTES = 10; //repeat every 10 minutess
        Runnable mRunnable;
        Handler mHandler = new Handler();
        mRunnable = new Runnable() {
            @SuppressWarnings("deprecation")
            @Override
            public void run() {
                if ( ImageLoader.getInstance()!=null) {
                	 ImageLoader.getInstance().getDiscCache().clear();
                	 ImageLoader.getInstance().getMemoryCache().clear();
                }
            }
        };
        mHandler.postDelayed(mRunnable, REPEAT_TIME_IN_MINUTES * 1000 * 60);

        SharedObjects.context= getApplicationContext();

		//Remote Config



        
	}
	
	
	public static Typeface getFirma()
	{
		return mFirmaFont;
	}
	
	public static Typeface getFirmaBold()
	{
		return mFirmaFontBold;
	}



	public void setLocale(String lang) {
		// Step 1: Update Locale
		Locale myLocale = new Locale(lang);
		Locale.setDefault(myLocale); // Set the new default locale

		// Update the configuration for the app's resources
		Resources res = getResources();
		Configuration conf = res.getConfiguration();
		conf.setLocale(myLocale);
		conf.setLayoutDirection(myLocale); // For RTL languages

		res.updateConfiguration(conf, res.getDisplayMetrics());

	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(updateBaseContextLocale(base));
	}


	public Context updateBaseContextLocale(Context context) {

		StoreAndGetFromPrefs storageObject = new StoreAndGetFromPrefs(context);
		String language = storageObject.retrieveLanguageFromPrefs();
		Locale locale = new Locale(language);
		Locale.setDefault(locale);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			return updateResourcesLocale(context, locale);
		}

		return updateResourcesLocaleLegacy(context, locale);
	}

	@TargetApi(Build.VERSION_CODES.N)
	private Context updateResourcesLocale(Context context, Locale locale) {
		Configuration configuration = context.getResources().getConfiguration();
		configuration.setLocale(locale);
		return context.createConfigurationContext(configuration);
	}

	@SuppressWarnings("deprecation")
	private Context updateResourcesLocaleLegacy(Context context, Locale locale) {
		Resources resources = context.getResources();
		Configuration configuration = resources.getConfiguration();
		configuration.locale = locale;
		resources.updateConfiguration(configuration, resources.getDisplayMetrics());
		return context;
	}
	  
}
