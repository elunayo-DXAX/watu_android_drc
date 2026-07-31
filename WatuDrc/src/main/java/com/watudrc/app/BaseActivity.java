package com.watudrc.app;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import androidx.core.view.WindowCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;


import com.watudrc.app.R;
import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;
import com.watudrc.app.util.FontScaleUtils;
import com.watudrc.app.util.LocaleUtils;

import java.util.Locale;

public class BaseActivity extends AppCompatActivity implements IWhereMyLocationConstants {

	StoreAndGetFromPrefs storageObject;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		allowLock();
		// Ensure system insets are applied as before
		WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
		super.onCreate(savedInstanceState);


	}

	@Override
	protected void attachBaseContext(Context newBase) {
		storageObject= new StoreAndGetFromPrefs(newBase);
		String savedLanguage = storageObject.retrieveLanguageFromPrefs();
		Context context = LocaleUtils.setLocale(newBase, savedLanguage);
		super.attachBaseContext(FontScaleUtils.wrap(newBase));

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

	private void changeFragment(Fragment targetFragment){
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.main_fragment, targetFragment, "fragment")
				.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.commit();
	}

	public void hideKeyboard()
	{
		try
		{
			InputMethodManager inputManager = (InputMethodManager)
					getSystemService(Context.INPUT_METHOD_SERVICE);

			inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);


		} catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void allowLock()
	{
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
			setShowWhenLocked(true);
		}

//
//		final Window win = getWindow();
//		win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
//		win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
//				| WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
//
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
//			setShowWhenLocked(true);
//			setTurnScreenOn(true);
//			KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
//			keyguardManager.requestDismissKeyguard(this, null);
//
//		} getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
//			| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//			| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
	}





}
