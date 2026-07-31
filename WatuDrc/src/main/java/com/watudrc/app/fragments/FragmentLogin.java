package com.watudrc.app.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.ConfigUpdate;
import com.google.firebase.remoteconfig.ConfigUpdateListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;
import com.watudrc.app.ActivateAccountActivity;
import com.watudrc.app.GeneralCommands;
import com.watudrc.app.LoginActivity;
import com.watudrc.app.MainActivity;
import com.watudrc.app.PreviewActivity;
import com.watudrc.app.R;
import com.watudrc.app.ResetPasswordActivity;
import com.watudrc.app.SharedObjects;
import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.font.FirmaTextView;
import com.watudrc.app.font.FontView;
import com.watudrc.app.mobileservices.GetData;
import com.watudrc.app.objects.AssetObject;
import com.watudrc.app.objects.ServiceDownObject;
import com.watudrc.app.objects.User;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;
import com.watudrc.app.util.FirebaseConfig;
import com.watudrc.app.util.PhoneNumberFormatter;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.ypyproductions.utils.DirectionUtils;
import com.ypyproductions.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.concurrent.Executor;

public class FragmentLogin extends Fragment implements IWhereMyLocationConstants {

    private View parentView;
   
    private static StoreAndGetFromPrefs storageObject;
    public static LoginActivity parentActivity;
	static Context mContext;

//	EditText passwordEditText;
    private EditText[] pinFields;
	FirmaTextView userInitials, userName;
	RelativeLayout inView;

	RelativeLayout btnForgotPassword, btnLogin,btnRegister;
	RelativeLayout loadingView;

	boolean isPhoneNumberValid;
	CountryCodePicker ccp;
	EditText phoneNumberEditText;

	EditText emailEditText;

	RelativeLayout showPass;
	FontView passShowIcon;
	RelativeLayout wrongView;

	//Network Check
	BroadcastReceiver networkChangeReceiver;
	ImageView progressWheelNetwork;
	RelativeLayout networkView;
	RelativeLayout btnNetwork;
	private boolean isReceiverRegistered = false;

	//Support Number
	String support_number="";

	String whatsapp_number="";
	FirebaseConfig remoteConfig;
	FirebaseRemoteConfig mFirebaseConfig;

	boolean upgrade_feature_enabled=false;

	//Whatsapp Error
	boolean show_whatsapp_button_on_error_page;
	boolean show_call_support_options_on_error_page;
	String support_page_configs="";

	boolean has_rating_enabled;

	int minimum_rating_days=30;

	boolean should_log_data_usage_every_launch;


	//Service Down
	String service_down="";
	// constants (put these in your class)
	private static final String PREFS_NAME = "app_prefs";
	private static final String KEY_SERVICE_DOWN_LAST_SHOWN = "service_down_last_shown";
	private static final long FOUR_HOURS_MS = 60L * 60L * 1000L; // 1 hours in ms
//    private static final long FOUR_HOURS_MS = 2L * 60L * 1000L; // 2 minutes (TEST ONLY)

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        parentView = inflater.inflate(R.layout.fragment_login, container, false);
        parentActivity = (LoginActivity)getActivity();
        mContext= parentActivity.getApplicationContext();
        storageObject = new StoreAndGetFromPrefs(mContext);
		loadingView= parentActivity.findViewById(R.id.loadingItems);

		userInitials= parentView.findViewById(R.id.userInitials);
		userName= parentView.findViewById(R.id.userName);
		inView= parentView.findViewById(R.id.inView);

		emailEditText= parentView.findViewById(R.id.emailEditText);

//        passwordEditText= (EditText) parentView.findViewById(R.id.passwordEditText);
//        passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        btnForgotPassword= parentView.findViewById(R.id.btnForgotPassword);
        btnLogin= parentView.findViewById(R.id.btnLogin);

		btnRegister= parentView.findViewById(R.id.btnRegister);
		wrongView= parentView.findViewById(R.id.wrongView);


		// 1) bind the 4 pin EditTexts
		pinFields = new EditText[] {
				parentView.findViewById(R.id.codeEditText1),
				parentView.findViewById(R.id.codeEditText2),
				parentView.findViewById(R.id.codeEditText3),
				parentView.findViewById(R.id.codeEditText4)
		};

		// 2) set up auto-advance & back-delete
		initPinFields(pinFields);

		if(!StringUtils.isStringEmpty(storageObject.retrieveUserNameFromPrefs()))
		{
			inView.setVisibility(View.VISIBLE);
			userInitials.setText(GeneralCommands.getUserInitials(storageObject.retrieveUserNameFromPrefs()));
			userName.setText(mContext.getResources().getString(R.string.hiname,storageObject.retrieveUserFirstNameFromPrefs()));

		}
		else
		{
			inView.setVisibility(View.GONE);

		}


		//Network
		networkView= loadingView.findViewById(R.id.networkView);
		btnNetwork= loadingView.findViewById(R.id.btnNetwork);
		progressWheelNetwork= loadingView.findViewById(R.id.progressWheelNetwork);


		ccp = parentView.findViewById(R.id.ccp);
		phoneNumberEditText = parentView.findViewById(R.id.phoneNumberEditText);

		ccp.registerCarrierNumberEditText(phoneNumberEditText);
		ccp.setPhoneNumberValidityChangeListener(isValidNumber -> isPhoneNumberValid=isValidNumber);


		if(storageObject.retrieveUserPhoneNumberFromPrefs()!="")ccp.setFullNumber(storageObject.retrieveUserPhoneNumberFromPrefs());
		if (!phoneNumberEditText.getText().toString().isEmpty() && !phoneNumberEditText.getText().toString().startsWith("0")) {phoneNumberEditText.setText("0"+ phoneNumberEditText.getText().toString());}
        PhoneNumberFormatter.applyFormatting(phoneNumberEditText);


		loadingView= parentActivity.findViewById(R.id.loadingItems);

//		showPass= (RelativeLayout) parentView.findViewById(R.id.showPass);
//		passShowIcon= (FontView) parentView.findViewById(R.id.passShowIcon);
//		showPass.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				if(passwordEditText.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
//					//Show Password
//					passShowIcon.setText(mContext.getResources().getString(R.string.dw_eye_cancel));
//					passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//				}
//				else{
//
//					//Hide Password
//					passShowIcon.setText(mContext.getResources().getString(R.string.dw_eye));
//					passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
//
//			   }
//			}
//		});
//


		btnForgotPassword.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GeneralCommands.logEvent(SharedObjects.context, "click_forgot_pin","click_forgot_pin");
                goToResetPasswordPage();
			}

			
		});

		try{setConfigs();}catch(Exception e){}

		btnLogin.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						String pin = collectPin(pinFields);

						proceedWithLogin(pin);

						
					}
		
					
				});

        btnRegister.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                storageObject.logOutUser();
                goToRegisterPage();
            }


        });


		//Network Check
		networkChangeReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

				// Check if network is connected
				if (networkInfo != null && networkInfo.isConnected()) {
					// Network is connected - update the fragment views
					loadingView.setVisibility(View.GONE);
					networkView.setVisibility(View.GONE);

					storageObject.saveTimeLastOnline(System.currentTimeMillis());


//					Toast.makeText(context, "Network is ON", Toast.LENGTH_SHORT).show();
				} else {
					// Network is disconnected - update the fragment views
					loadingView.setVisibility(View.VISIBLE);
					networkView.setVisibility(View.VISIBLE);

					try
					{
						Glide.with(parentActivity)
								.asGif()
								.load(R.drawable.loader_network) // Replace with your local GIF resource
								.into(progressWheelNetwork);

					} catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}


//					Toast.makeText(context, "Network is OFF", Toast.LENGTH_SHORT).show();
				}
			}
		};

		btnNetwork.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				GeneralCommands.logEvent(parentActivity,"click_turn_on_data","click_turn_on_data");

				// Open network settings
				openDataUsageSettings(parentActivity);
			}
		});

		//End Network


//		fillPinFields(pinFields, storageObject.retrieveUserPasswordFromPrefs());

		//Fingeprint Login
		if(storageObject.isUserFingerprintEnabled())
		{
			if(checkBiometricSupportAndProceed()) {
				showBiometricPrompt();
			} else {
//				Toast.makeText(parentActivity, mContext.getResources().getString(R.string.fingeprintnotsupported), Toast.LENGTH_SHORT).show();
			}


		}

		return parentView;
    }

//	public void proceedWithLogin(String pin) {
//		if(GeneralCommands.isDemo(ccp.getFullNumber(),pin))isPhoneNumberValid=true;
//
//		if (pin.length() < 4) {
//			Toast.makeText(parentActivity, mContext.getResources().getString(R.string.enterpin), Toast.LENGTH_SHORT).show();
//			return;
//		}
//
//		hideKeyboard();
//
//		//All Good-Proceed to check
//
//		GetData ob1 = new GetData();
//		parentActivity.hideKeyboard();
//		ob1.initialize(parentActivity, loadingView);
//
//		if(GeneralCommands.isDemo(ccp.getFullNumber(),pin))
//		{
////								Toast.makeText(mContext,"IsDemo",Toast.LENGTH_SHORT).show();
//			//Is Demo
//			GeneralCommands.logEvent(parentActivity,"click_login","click_login");
//			wrongView.setVisibility(View.GONE);
//			ob1.userLogin(ccp.getFullNumber(),pin);
//
//		}
//		else
//		{
//
//
//			//Check is Password Set or Not
//			if(storageObject.retrieveUserPasswordFromPrefs().equals(""))
//			{
//				Toast.makeText(mContext,mContext.getResources().getString(R.string.registrationneeded),Toast.LENGTH_SHORT).show();
//				goToRegisterPage();
//				return;
//			}
//
//			//Is Not Demo Account
//			storageObject.saveUserPhoneNumberToPrefs(ccp.getFullNumber());
//
//
//			if(pin.equals(storageObject.retrieveUserPasswordFromPrefs()))
//			{
//				GeneralCommands.logEvent(parentActivity,"click_login","click_login");
//				wrongView.setVisibility(View.GONE);
//				ob1.userLogin(ccp.getFullNumber(),pin);
//
//			}
//			else
//			{
//				wrongView.setVisibility(View.VISIBLE);
//
//
//			}
//		}
//
//	}

public void proceedWithLogin(String pin) {
    if(GeneralCommands.isDemo(ccp.getFullNumber(),pin))isPhoneNumberValid=true;

    if (pin.length() < 4) {
        Toast.makeText(parentActivity, mContext.getResources().getString(R.string.enterpin), Toast.LENGTH_SHORT).show();
        return;
    }

    hideKeyboard();

    //All Good-Proceed to check

    GetData ob1 = new GetData();
    parentActivity.hideKeyboard();
    ob1.initialize(parentActivity, loadingView);

    if(GeneralCommands.isDemo(ccp.getFullNumber(),pin))
    {
//								Toast.makeText(mContext,"IsDemo",Toast.LENGTH_SHORT).show();
        //Is Demo
        GeneralCommands.logEvent(parentActivity,"click_login","click_login");
        wrongView.setVisibility(View.GONE);
        ob1.userLogin(ccp.getFullNumber(),pin);

    }
    else
    {


        //Check is Password Set or Not
        if(storageObject.retrieveUserPasswordFromPrefs().equals(""))
        {
            Toast.makeText(mContext,mContext.getResources().getString(R.string.registrationneeded),Toast.LENGTH_SHORT).show();
            goToRegisterPage();
            return;
        }

        //Is Not Demo Account
        storageObject.saveUserPhoneNumberToPrefs(ccp.getFullNumber());


        if(pin.equals(storageObject.retrieveUserPasswordFromPrefs()))
        {
            GeneralCommands.logEvent(parentActivity,"click_login","click_login");
            wrongView.setVisibility(View.GONE);
            ob1.userLogin(ccp.getFullNumber(),pin);

        }
        else
        {
            wrongView.setVisibility(View.VISIBLE);


        }
    }

}

    //Network
	@Override
	public void onStart() {
		super.onStart();
		// Register the receiver if it's not registered
		if (!isReceiverRegistered && getContext() != null) {
			try {
				IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
				getContext().registerReceiver(networkChangeReceiver, filter);
				isReceiverRegistered = true;
			} catch (Exception e) {
				// Handle any exceptions during registration
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		// Unregister the receiver if it's registered
		if (isReceiverRegistered && getContext() != null) {
			try {
				getContext().unregisterReceiver(networkChangeReceiver);
				isReceiverRegistered = false;
			} catch (Exception e) {
				// Handle the case where the receiver was not registered
				e.printStackTrace();
			}

		}
	}

	@Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that we are currently visible
        if (this.isVisible()) {
            // If we are becoming invisible, then...
            if (isVisibleToUser) {
              
                // TODO stop audio playback
            }
        }
    }

    private boolean isEmailValid(CharSequence email) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

	public void setLocale(String lang) {
		// Step 1: Update Locale
		Locale myLocale = new Locale(lang);
		Locale.setDefault(myLocale);

		Resources res = parentActivity.getResources();
		Configuration conf = res.getConfiguration();

		conf.setLocale(myLocale); // Set the new locale
		conf.setLayoutDirection(myLocale); // Update layout direction for RTL languages

		Context context = parentActivity.createConfigurationContext(conf);
		res.updateConfiguration(conf, res.getDisplayMetrics());

		// Step 2: Save Language to Preferences
		storageObject.saveLanguageToPrefs(lang);

		// Step 3: Restart the Target Activity
		Intent mIntent;
		if (storageObject.checkIfUserIsRegistered()) {
			mIntent = new Intent(parentActivity, LoginActivity.class);
		} else {
			mIntent = new Intent(parentActivity, PreviewActivity.class);
		}

		mIntent.putExtra("KEY_START_FROM", "login_page");

		// Clear existing tasks and start fresh
		mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		parentActivity.startActivity(mIntent);

		// Finish current activity to avoid stacking
		parentActivity.finish();

	}

	public void goToRegisterPage()
	{
		Intent mIntent = new Intent(parentActivity, ActivateAccountActivity.class);
		mIntent.putExtra(KEY_START_FROM, "splash");
		GeneralCommands.goToPage( parentActivity,mIntent,true);

	}



	public void goToResetPasswordPage()
	{
		Intent mIntent = new Intent(parentActivity, ResetPasswordActivity.class);
		mIntent.putExtra(KEY_START_FROM, "splash");
		DirectionUtils.changeActivity(parentActivity, R.anim.slide_in_from_right, R.anim.slide_out_to_left, false, mIntent);

	}

	public void goToMainPage()
	{
		Bundle params = new Bundle();
		params.putString("user_logged_unique_id", "W2232323=");
		params.putString("user_logged_in", "James Logged In");
		params.putString("user_logged_in_number", "254713576480");
		params.putString("user_logged_in_time", "19-08-2023 12:34");
//		mFirebaseAnalytics.setDefaultEventParameters(parameters);
//

		Intent mIntent = new Intent(parentActivity, MainActivity.class);
		mIntent.putExtra(KEY_START_FROM, "splash");
		GeneralCommands.goToPage(parentActivity, mIntent,true);

	}

	public void fakeLogin()
	{
		User user = new User();
		user.setName("James Reed");
//		user.setFirst_name("James");
//		user.setLast_name("Reed");
		user.setEmail("jamesmuindi@gmail.com");
		user.setId("1");
		user.setPhone_number("254713576480");
		user.setImage("https://zynamis.co.ke/images/team-members/3.jpg");
		storageObject.saveUserObjectToPrefs(user);
		storageObject.setUserRegistered(true);
		goToMainPage();
	}



	public void skipLogin()
	{
//		User user = new User();
//		user.setName("Not Set");
//		user.setEmail("Not Set");
//		user.setId(0);
//		user.setPhone("Not Set");
//		user.setProfile_pic("https://zynamis.co.ke/images/avatar.png");
//		storageObject.saveUserObjectToPrefs(user);
//		storageObject.setUserRegistered(true);
//		goToMainPage();
	}



	public void openDataUsageSettings(Context context) {
		Intent intent = new Intent();

		// Try to open Data Usage settings
		intent.setAction(Settings.ACTION_DATA_USAGE_SETTINGS);

		if (canHandleIntent(context, intent)) {
			try {
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Add this flag
				context.startActivity(intent);
			} catch (Exception e) {
				Log.e("Error", "Failed to open Data Usage settings: " + e.getMessage());
				openAlternativeSettings(context);
			}
		} else {
			Log.e("Error", "No Activity found to handle Data Usage settings intent");
			openAlternativeSettings(context);
		}
	}

	// Check if an intent can be handled by the system
	private boolean canHandleIntent(Context context, Intent intent) {
		return intent.resolveActivity(context.getPackageManager()) != null;
	}

	// Open alternative settings if Data Usage is unavailable
	private void openAlternativeSettings(Context context) {
		// First, try to open Network Operator settings as a fallback
		Intent intent = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Add this flag

		if (canHandleIntent(context, intent)) {
			try {
				context.startActivity(intent);
				return;
			} catch (Exception e) {
				Log.e("Error", "Failed to open Network Operator settings: " + e.getMessage());
			}
		}

		// Next, try to open Wireless Settings
		intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Add this flag

		if (canHandleIntent(context, intent)) {
			try {
				context.startActivity(intent);
				return;
			} catch (Exception e) {
				Log.e("Error", "Failed to open Wireless settings: " + e.getMessage());
			}
		}

		// Fallback to General settings if nothing else works
		intent = new Intent(Settings.ACTION_SETTINGS);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Add this flag

		if (canHandleIntent(context, intent)) {
			try {
				context.startActivity(intent);
			} catch (Exception e) {
				Log.e("Error", "Failed to open General settings: " + e.getMessage());
			}
		} else {
			Log.e("Error", "No Activity found to handle General settings intent");
		}
	}


	private void initPinFields(final EditText[] fields) {
		for (int i = 0; i < fields.length; i++) {
			final EditText current = fields[i];
			final EditText next    = (i < fields.length - 1 ? fields[i + 1] : null);
			final EditText prev    = (i > 0 ? fields[i - 1] : null);

			// only the first field is enabled at start
			current.setEnabled(i == 0);

			// when the user types...
			current.addTextChangedListener(new TextWatcher() {
				@Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
				@Override public void onTextChanged(CharSequence s, int st, int b, int c) {}

				@Override
				public void afterTextChanged(Editable s) {
					if (s.length() == 1) {
						if (next != null) {
							next.setEnabled(true);
							next.requestFocus();
						} else {
							// optionally hide keyboard once all 4 digits are in
							hideKeyboard();
						}
					}
				}
			});

			// handle backspace on empty field
			current.setOnKeyListener((v, keyCode, event) -> {
				if (event.getAction() == KeyEvent.ACTION_DOWN
						&& keyCode == KeyEvent.KEYCODE_DEL
						&& current.getText().length() == 0
						&& prev != null) {
					prev.requestFocus();
					prev.setText("");
					return true;
				}
				return false;
			});
		}
	}

	/** Called when the user taps the Listen/Login button. */

	/** Concatenates the 4 EditText values into one String. */
	private String collectPin(EditText[] fields) {
		StringBuilder sb = new StringBuilder(4);
		for (EditText e : fields) {
			sb.append(e.getText().toString());
		}
		return sb.toString();
	}

	private void fillPinFields(EditText[] fields, String pin) {
		if (pin == null) pin = "";
		int length = pin.length();

		for (int i = 0; i < fields.length; i++) {
			EditText field = fields[i];
			if (i < length) {
				// set each digit
				field.setText(String.valueOf(pin.charAt(i)));
				field.setEnabled(true);
			} else {
				// clear & disable the rest
				field.setText("");
				field.setEnabled(false);
			}
		}

		// move focus to the next empty field (or last one if all filled)
		int focusIndex = Math.min(length, fields.length - 1);
		fields[focusIndex].requestFocus();
	}

	/** Hides the soft keyboard if it’s showing. */
	private void hideKeyboard() {
		View focused = parentActivity.getCurrentFocus();
		if (focused != null) {
			InputMethodManager imm = (InputMethodManager)
					parentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(focused.getWindowToken(), 0);
		}
	}

	/** Stub—swap in your real login logic here. */
	private void authenticateWithPin(String pin) {
		// e.g. call your API, check locally, etc.
		Toast.makeText(parentActivity, "Logging in with PIN: " + pin, Toast.LENGTH_SHORT).show();
	}


	//Finger Print
	private boolean checkBiometricSupportAndProceed() {
		BiometricManager biometricManager = BiometricManager.from(parentActivity);

		switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
			case BiometricManager.BIOMETRIC_SUCCESS:
				// Everything is good
				return true;

			case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
				// Device supports biometrics, but none enrolled
				Toast.makeText(parentActivity, mContext.getResources().getString(R.string.fingeprintnotsetup), Toast.LENGTH_LONG).show();
				Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
				enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
						BiometricManager.Authenticators.BIOMETRIC_STRONG);
				startActivity(enrollIntent);
				break;

			case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
			case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
			default:
				// Device does not support biometric
				Toast.makeText(parentActivity, mContext.getResources().getString(R.string.fingeprintnotsupported), Toast.LENGTH_SHORT).show();
				return false;
		}

		return false;
	}



	private void showBiometricPrompt() {
		Executor executor = ContextCompat.getMainExecutor(parentActivity);
		BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor,
				new BiometricPrompt.AuthenticationCallback() {
					@Override
					public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
						super.onAuthenticationError(errorCode, errString);
//						Toast.makeText(parentActivity, "Auth error: " + errString, Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
						super.onAuthenticationSucceeded(result);
//						Toast.makeText(parentActivity, "Auth succeeded!", Toast.LENGTH_SHORT).show();
//						fillPinFields(pinFields, storageObject.retrieveUserPasswordFromPrefs());

						GeneralCommands.logEvent(SharedObjects.context, "click_fingerprint_login","click_fingerprint_login");


                        storageObject.saveUSerFingeprintEnabled(true);
						proceedWithLogin(storageObject.retrieveUserPasswordFromPrefs());

						// TODO: Proceed to the main activity or perform login
					}

					@Override
					public void onAuthenticationFailed() {
						super.onAuthenticationFailed();
//						Toast.makeText(parentActivity, "Auth failed", Toast.LENGTH_SHORT).show();
					}
				});

		BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
				.setTitle(mContext.getResources().getString(R.string.loginwithfingerprint))
				.setSubtitle(mContext.getResources().getString(R.string.touchfingerprinttoauthenticate))
				.setNegativeButtonText(mContext.getResources().getString(R.string.cancel))
				.build();

		biometricPrompt.authenticate(promptInfo);
	}

	public void setConfigs()
	{
		//Remote Configs
		remoteConfig= new FirebaseConfig();
		mFirebaseConfig=remoteConfig.getConfigs();
		mFirebaseConfig.fetchAndActivate().addOnCompleteListener(parentActivity, new OnCompleteListener<Boolean>() {
			@Override
			public void onComplete(@NonNull Task<Boolean> task) {
				if (task.isSuccessful())
				{
					boolean updated = task.getResult();
//							Toast.makeText(mContext, "Fetch and activate succeeded",
//									Toast.LENGTH_SHORT).show();
					//Support Number
					support_number = mFirebaseConfig.getString("SUPPORT_NUMBER");
					storageObject.saveSupportNumberToPrefs(support_number);


					//Whatsapp Number
					whatsapp_number = mFirebaseConfig.getString("WHATSAPP_NUMBER");
					storageObject.saveWhatsAppNumberToPrefs(whatsapp_number);

					//Log Data Usage Every Launch
					should_log_data_usage_every_launch = mFirebaseConfig.getBoolean("should_log_data_usage_every_launch");
					storageObject.saveLogDataUsageEveryLaunchToPrefs(should_log_data_usage_every_launch);


					//Upgrade Feature
					upgrade_feature_enabled = mFirebaseConfig.getBoolean("upgrade_feature_enabled");
					storageObject.saveUpgradeFeatureEnabledToPrefs(upgrade_feature_enabled);


					//Rating Enabled
					has_rating_enabled = mFirebaseConfig.getBoolean("has_rating_enabled");
					storageObject.setRatingEnabled(has_rating_enabled);

					//Minimum Rating Days
					minimum_rating_days = (int)mFirebaseConfig.getLong("minimum_rating_days");
					storageObject.saveMinimumRatingDaysToPrefs(minimum_rating_days);


					//WhatsApp Error Button
					show_whatsapp_button_on_error_page = mFirebaseConfig.getBoolean("show_whatsapp_button_on_error_page");
					storageObject.saveShowWhatsAppOnError(show_whatsapp_button_on_error_page);

					//Call Support Error Button
					show_call_support_options_on_error_page = mFirebaseConfig.getBoolean("show_call_support_options_on_error_page");
					storageObject.saveShowCallSupportOptionsOnError(show_call_support_options_on_error_page);

					//Call Support Error Button
					support_page_configs = mFirebaseConfig.getString("support_page_configs");
					try {
						JSONObject supportObj = new JSONObject(support_page_configs);
						Log.d("ZeSupport","ZeSupportConfigs"+ supportObj);
						storageObject.saveSupportPageConfigToPrefs(supportObj);


					} catch (JSONException e) {
						e.printStackTrace();
					}

					//Service Down Object Check
					service_down = mFirebaseConfig.getString("service_down");
					if (service_down != null && !service_down.trim().isEmpty()) {
						try {
							Log.d("ZeDown", "ZeDown" + service_down);

							// Save to Prefs
							storageObject.saveServiceDownObjectToPrefs(service_down);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}


//					Toast.makeText(mContext,"ZeSup"+show_call_support_options_on_error_page+":"+show_whatsapp_button_on_error_page,Toast.LENGTH_SHORT).show();



				}
				else {
					Log.d("ZeFetchFailed","ZeFetchFailed");
				}

			}
		});

		mFirebaseConfig.addOnConfigUpdateListener(new ConfigUpdateListener() {
			@Override
			public void onUpdate(ConfigUpdate configUpdate) {
				//Log.d("ZeConfigs", "Updated keys: " + configUpdate.getUpdatedKeys());

				mFirebaseConfig.activate().addOnCompleteListener(new OnCompleteListener() {
					@Override
					public void onComplete(@NonNull Task task) {


						//Support Number
						support_number = mFirebaseConfig.getString("SUPPORT_NUMBER");
						storageObject.saveSupportNumberToPrefs(support_number);

						//Log Data Usage Every Launch
						should_log_data_usage_every_launch = mFirebaseConfig.getBoolean("should_log_data_usage_every_launch");
						storageObject.saveLogDataUsageEveryLaunchToPrefs(should_log_data_usage_every_launch);


						//Whatsapp Number
						whatsapp_number = mFirebaseConfig.getString("WHATSAPP_NUMBER");
						storageObject.saveWhatsAppNumberToPrefs(whatsapp_number);

						//Upgrade Feature
						upgrade_feature_enabled = mFirebaseConfig.getBoolean("upgrade_feature_enabled");
						storageObject.saveUpgradeFeatureEnabledToPrefs(upgrade_feature_enabled);


						//Rating Enabled
						has_rating_enabled = mFirebaseConfig.getBoolean("has_rating_enabled");
						storageObject.setRatingEnabled(has_rating_enabled);

						//Minimum Rating Days
						minimum_rating_days = (int)mFirebaseConfig.getLong("minimum_rating_days");
						storageObject.saveMinimumRatingDaysToPrefs(minimum_rating_days);


						//WhatsApp Error Button
						show_whatsapp_button_on_error_page = mFirebaseConfig.getBoolean("show_whatsapp_button_on_error_page");
						storageObject.saveShowWhatsAppOnError(show_whatsapp_button_on_error_page);

						//Call Support Error Button
						show_call_support_options_on_error_page = mFirebaseConfig.getBoolean("show_call_support_options_on_error_page");
						storageObject.saveShowCallSupportOptionsOnError(show_call_support_options_on_error_page);

						//Call Support Error Button
						support_page_configs = mFirebaseConfig.getString("support_page_configs");
						try {
							JSONObject supportObj = new JSONObject(support_page_configs);
							Log.d("ZeSupport","ZeSupportConfigs"+ supportObj);
							storageObject.saveSupportPageConfigToPrefs(supportObj);


						} catch (JSONException e) {
							e.printStackTrace();
						}

						//Service Down Object Check
						service_down = mFirebaseConfig.getString("service_down");
						if (service_down != null && !service_down.trim().isEmpty()) {
							try {
								Log.d("ZeDown", "ZeDown" + service_down);

								// Save to Prefs
								storageObject.saveServiceDownObjectToPrefs(service_down);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}


//						Toast.makeText(mContext,"ZeSup"+support_number,Toast.LENGTH_SHORT).show();



					}
				});
			}

			@Override
			public void onError(FirebaseRemoteConfigException error) {
				Log.w("ZeConfigs", "Config update error with code: " + error.getCode(), error);
			}
		});
	}


	//Service Down
	public void openDialogServiceDown()
	{
		View contentView = parentActivity.getLayoutInflater().inflate(R.layout.dialog_content_service_down, null);


		final DialogPlus dialogPlus = new DialogPlus.Builder(parentActivity)
				.setContentHolder(new ViewHolder(contentView))
				.setGravity(DialogPlus.Gravity.CENTER)
				.setBackgroundColorResourceId(R.color.transparent)
				.setCancelable(true)
				.create();


		//Content View
		View content = dialogPlus.getHolderView();



		RelativeLayout loadingView= content.findViewById(R.id.loadingItems);
		RelativeLayout btnClose =content.findViewById(R.id.btnClose);
		RelativeLayout btnBack= content.findViewById(R.id.btnBack);

		FirmaTextView titleText=content.findViewById(R.id.titleText);
		FirmaTextView titleDescription=content.findViewById(R.id.titleDescription);

		String tText=mContext.getResources().getString(R.string.serviceinterruption);
		String tDescription=mContext.getResources().getString(R.string.servicedowndescription);

		ImageView errorIcon = content.findViewById(R.id.titleImage);


		if (storageObject.retrieveServiceDownObjectFromPrefs() != null) {
			ServiceDownObject service = storageObject.retrieveServiceDownObjectFromPrefs();

			if(service.getCategory() !=null && service.getTitle_description() !=null)
			{
				if (service.getCategory().toLowerCase().contains("server_down")) {
					errorIcon.setImageResource(R.drawable.ics_icon_service_down);
				} else if (service.getCategory().toLowerCase().contains("intermittent")) {

					errorIcon.setImageResource(R.drawable.ics_icon_service_intermittent);

				} else if (service.getCategory().toLowerCase().contains("payment")) {
					errorIcon.setImageResource(R.drawable.ics_icon_service_payment);

				} else if (service.getCategory().toLowerCase().contains("timestamp")) {
					errorIcon.setImageResource(R.drawable.ics_icon_service_intermittent);

				}
			}

			if (storageObject.retrieveLanguageFromPrefs().equalsIgnoreCase("fr")) {

				if(service.getTitle_fr()!=null && service.getTitle_description_fr()!=null)
				{
					tText = service.getTitle_fr();
					tDescription = service.getTitle_description_fr();
				}

			} else
			{
				tText = service.getTitle();
				tDescription = service.getTitle_description();

//				Toast.makeText(mContext,"GootenTitle"+tDescription,Toast.LENGTH_SHORT).show();

			}

			setHtml(titleDescription, tDescription);
			setHtml(titleText, tText);

		} else {
			titleText.setText(tText);
			titleDescription.setText(Html.fromHtml(tDescription));

		}




		//Current Device OBJect
		JSONObject obj = new JSONObject();
		AssetObject currentAsset= storageObject.retrieveCurrentAssetObjectFromPrefs();
		try {

			Gson gson= new Gson();

			obj.put("assetLoanNumber",currentAsset.getLoanNumber());
			obj.put("assetName",currentAsset.getAssetTypeName());
			obj.put("assetTypeId",currentAsset.getAssetTypeId());
			if(currentAsset.getDeviceUpgradeObject()!=null && currentAsset.getDeviceUpgradeObject().getUpgradeDevices()!=null)
			{
				obj.put("currentDevice",gson.toJson(currentAsset.getDeviceUpgradeObject().getCurrentDevice()));
				obj.put("upgradeOptions",gson.toJson(currentAsset.getDeviceUpgradeObject().getUpgradeDevices()));

			}


		} catch (JSONException e) {
			e.printStackTrace();
		}



		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				GeneralCommands.logEventObject(parentActivity,"click_service_down_back","click_service_down_back", obj);
				dialogPlus.dismiss();
			}
		});

		btnClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				GeneralCommands.logEventObject(parentActivity,"click_service_down_back","click_service_down_back", obj);
				dialogPlus.dismiss();
			}
		});






		dialogPlus.show();
	}


	public void tryOpenServiceDownDialogWithBackoff(final Context context, boolean forceShow) {
		if (context == null) return;

		long now = System.currentTimeMillis();
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		long lastShown = prefs.getLong(KEY_SERVICE_DOWN_LAST_SHOWN, 0L);

		if (forceShow || (now - lastShown) >= FOUR_HOURS_MS || lastShown == 0L) {
			// Ensure UI thread when showing dialog
			if (context instanceof Activity) {
				((Activity) context).runOnUiThread(() -> {
					openDialogServiceDown();
					// update timestamp
					prefs.edit().putLong(KEY_SERVICE_DOWN_LAST_SHOWN, System.currentTimeMillis()).apply();
				});
			} else {
				// If no activity context available, still update timestamp and try to show
				openDialogServiceDown();
				prefs.edit().putLong(KEY_SERVICE_DOWN_LAST_SHOWN, System.currentTimeMillis()).apply();
			}
		} else {
			// Backoff not expired — no-op (optional: log or debug)
			long remaining = FOUR_HOURS_MS - (now - lastShown);
			Log.d("ZeServiceDown", "Dialog backoff active, remaining ms: " + remaining);
		}
	}

	/** Force-clear the backoff so dialog may show immediately next time. */
	public void resetServiceDownDialogBackoff(Context context) {
		if (context == null) return;
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		prefs.edit().remove(KEY_SERVICE_DOWN_LAST_SHOWN).apply();
	}

	/**
	 * Remove the <Data><![CDATA[ ... ]]></Data> wrapper if present and return the inner HTML.
	 */
	private String unwrapCData(String maybeCdata) {
		if (maybeCdata == null) return null;
		String s = maybeCdata.trim();

		final String start = "<Data><![CDATA[";
		final String end = "]]></Data>";

		if (s.startsWith(start) && s.endsWith(end)) {
			s = s.substring(start.length(), s.length() - end.length());
		}

		// If the JSON contained escaped newlines (like "\\n"), convert them to actual newlines
		s = s.replace("\\n", "\n");

		// Convert literal newlines to <br/> so HTML renderer preserves line breaks
		s = s.replace("\n\n", "<br/><br/>").replace("\n", "<br/>");

		// If the content was double-escaped (e.g. contains "&lt;font&gt;"), unescape basic HTML entities:
		// Simple common replacements (avoid heavy deps). Add more if you need them.
		s = s.replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&");

		return s;
	}

	/**
	 * Render HTML into a TextView safely.
	 */
	private void setHtml(TextView tv, String rawHtml) {
		if (tv == null) return;

		String unwrapped = unwrapCData(rawHtml);
		if (unwrapped == null) {
			tv.setText("");
			return;
		}

		// Use HtmlCompat for consistent behavior across API levels
		Spanned spanned = HtmlCompat.fromHtml(unwrapped, HtmlCompat.FROM_HTML_MODE_LEGACY);

		tv.setText(spanned);
		// Make links clickable (if you have <a href="..."> links)
		tv.setMovementMethod(LinkMovementMethod.getInstance());
	}

	//End Service Down
}
