package com.watudrc.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.watudrc.app.GeneralCommands;
import com.watudrc.app.MainActivity;
import com.watudrc.app.PermissionsActivity;
import com.watudrc.app.R;
import com.watudrc.app.SetFingerPrintActivity;
import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.mobileservices.GetData;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;

import java.util.concurrent.Executor;

public class FragmentSetFingeprint extends Fragment implements IWhereMyLocationConstants {

    private View parentView;
   
    private static StoreAndGetFromPrefs storageObject;
    public static SetFingerPrintActivity parentActivity;
	static Context mContext;

	RelativeLayout loadingView;

    private ImageView fingerprintIcon, fingerprintIconFilled,fingerprintIconError;
    private RelativeLayout btnConfirm, btnSkip;

	
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        parentView = inflater.inflate(R.layout.fragment_set_fingerprint, container, false);
        parentActivity = (SetFingerPrintActivity)getActivity();
        mContext= parentActivity.getApplicationContext();
        storageObject = new StoreAndGetFromPrefs(mContext);
        loadingView= parentView.findViewById(R.id.loadingItems);

        btnConfirm = parentView.findViewById(R.id.btnConfirm);
        btnSkip = parentView.findViewById(R.id.btnSkip);

        fingerprintIcon = parentView.findViewById(R.id.fingerprintIcon);
        fingerprintIconFilled = parentView.findViewById(R.id.fingerprintIconFilled);
        fingerprintIconError= parentView.findViewById(R.id.fingerprintIconError);



        btnConfirm.setOnClickListener(v -> {
            GeneralCommands.logEvent(parentActivity,"click_fingerprint_confirm","click_fingerprint_confirm");
            if(checkBiometricSupportAndProceed()) {
                showBiometricPrompt();
            } else {
                Toast.makeText(parentActivity, mContext.getResources().getString(R.string.fingeprintnotsupported), Toast.LENGTH_SHORT).show();
            }
        });

        fingerprintIcon.setOnClickListener(v -> {
            GeneralCommands.logEvent(parentActivity,"click_fingerprint_confirm","click_fingerprint_confirm");
            if(checkBiometricSupportAndProceed()) {
                showBiometricPrompt();
            } else {
                Toast.makeText(parentActivity, mContext.getResources().getString(R.string.fingeprintnotsupported), Toast.LENGTH_SHORT).show();
            }
        });

        fingerprintIconFilled.setOnClickListener(v -> {
            GeneralCommands.logEvent(parentActivity,"click_fingerprint_confirm","click_fingerprint_confirm");
            if(checkBiometricSupportAndProceed()) {
                showBiometricPrompt();
            } else {
                Toast.makeText(parentActivity, mContext.getResources().getString(R.string.fingeprintnotsupported), Toast.LENGTH_SHORT).show();
            }
        });

        fingerprintIconError.setOnClickListener(v -> {
            GeneralCommands.logEvent(parentActivity,"click_fingerprint_confirm","click_fingerprint_confirm");
            if(checkBiometricSupportAndProceed()) {
                showBiometricPrompt();
            } else {
                Toast.makeText(parentActivity, mContext.getResources().getString(R.string.fingeprintnotsupported), Toast.LENGTH_SHORT).show();
            }
        });

        btnSkip.setOnClickListener(v -> {
            GeneralCommands.logEvent(getContext(),"click_fingerprint_skip","click_fingerprint_skip");
//
//            GetData ob1 = new GetData();
//            ob1.initialize(parentActivity, loadingView);
//            ob1.userLogin(storageObject.retrieveUserPhoneNumberFromPrefs(),storageObject.retrieveUserPasswordFromPrefs());
            goToPermissionsPage();
        });



       

		
        return parentView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that we are currently visible
        if (this.isVisible()) {
            // If we are becoming invisible, then...
            if (isVisibleToUser) {
                GeneralCommands.logEvent(parentActivity,"view_page","view_page");
                // TODO stop audio playback
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onStart() {
        super.onStart();
    }
    public void goToPage()
    {
        Intent mIntent = new Intent(parentActivity, MainActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
        GeneralCommands.goToPage(parentActivity, mIntent,false);

    }

    private boolean checkBiometricSupportAndProceed() {
        GeneralCommands.logEvent(getContext(),"click_fingerprint_activate","click_fingerprint_activate");


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
//                        Toast.makeText(parentActivity, "Auth error: " + errString, Toast.LENGTH_SHORT).show();

                        fingerprintIcon.setVisibility(View.GONE);
                        fingerprintIconFilled.setVisibility(View.GONE);
                        fingerprintIconError.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
//                        Toast.makeText(parentActivity, "Auth succeeded!", Toast.LENGTH_SHORT).show();

                        fingerprintIcon.setVisibility(View.GONE);
                        fingerprintIconError.setVisibility(View.GONE);
                        fingerprintIconFilled.setVisibility(View.VISIBLE);
                        storageObject.saveUSerFingeprintEnabled(true);

                        goToPermissionsPage();


                        // TODO: Proceed to the main activity or perform login
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();

                        Toast.makeText(parentActivity, "Auth failed", Toast.LENGTH_SHORT).show();
                        fingerprintIcon.setVisibility(View.GONE);
                        fingerprintIconFilled.setVisibility(View.GONE);
                        fingerprintIconError.setVisibility(View.VISIBLE);

                    }
                });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(mContext.getResources().getString(R.string.loginwithfingerprint))
                .setSubtitle(mContext.getResources().getString(R.string.touchfingerprinttoauthenticate))
                .setNegativeButtonText(mContext.getResources().getString(R.string.cancel))
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    public void goToPermissionsPage() {

        Intent mIntent = new Intent(parentActivity, PermissionsActivity.class);
        mIntent.putExtra(KEY_START_FROM, "splash");
        GeneralCommands.goToPage(parentActivity, mIntent, true);
    }


}
