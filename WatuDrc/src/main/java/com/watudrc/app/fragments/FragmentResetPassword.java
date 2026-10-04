package com.watudrc.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.watudrc.app.ChooseOTPActivity;
import com.watudrc.app.GeneralCommands;
import com.watudrc.app.R;
import com.watudrc.app.ResetPasswordActivity;
import com.watudrc.app.SharedObjects;
import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.mobileservices.GetData;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;
import com.ypyproductions.utils.StringUtils;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;

public class FragmentResetPassword extends Fragment implements IWhereMyLocationConstants {

    private View parentView;
    RelativeLayout loadingView;
   
    private static StoreAndGetFromPrefs storageObject;
    public static ResetPasswordActivity parentActivity;
	static Context mContext;

	RelativeLayout btnResetPassword;

	boolean isPhoneNumberValid;
    CountryCodePicker ccp;
    EditText phoneNumberEditText,idNumberEditText;

    //Back

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        parentView = inflater.inflate(R.layout.fragment_reset_password, container, false);
        parentActivity = (ResetPasswordActivity)getActivity();
        mContext= parentActivity.getApplicationContext();
        storageObject = new StoreAndGetFromPrefs(mContext);
        loadingView= parentView.findViewById(R.id.loadingItems);



        idNumberEditText = parentView.findViewById(R.id.idNumberEditText);


        ccp = parentView.findViewById(R.id.ccp);
        phoneNumberEditText = parentView.findViewById(R.id.phoneNumberEditText);

        ccp.registerCarrierNumberEditText(phoneNumberEditText);
        ccp.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @Override
            public void onValidityChanged(boolean isValidNumber) {
                isPhoneNumberValid=isValidNumber;
            }
        });
        ccp.setNumberAutoFormattingEnabled(false);

        btnResetPassword= parentView.findViewById(R.id.btnResetPassword);
        btnResetPassword.setOnClickListener(view -> {

            if(phoneNumberEditText.getText().toString().length()<1)
            {
                String errorMsg= mContext.getResources().getString(R.string.entervalidphonenumber);
                phoneNumberEditText.setError(errorMsg);
                Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();


            }
            else {

                GeneralCommands.logEvent(parentActivity,"click_reset_password","click_reset_password");
                if(GeneralCommands.isDemo(phoneNumberEditText.getText().toString().replaceAll("\\s+", ""),"1234"))isPhoneNumberValid=true;

                String userNumber= ccp.getFullNumber();
                idNumberEditText.setError(null);
                storageObject.saveUserPhoneNumberToPrefs(userNumber);
                storageObject.saveUserIDNumberToPrefs(idNumberEditText.getText().toString());
                parentActivity.hideKeyboard();

                GetData ob1 = new GetData();
                ob1.initialize(parentActivity,loadingView);
                try {
                    ob1.userRegister(userNumber,false);
                    ob1.setUserRegisterListener(new GetData.UserRegisterListener() {
                        @Override
                        public void onObjectReady(String title) {

                        }

                        @Override
                        public void onDataLoaded(String deviceId) {
                            if(!StringUtils.isStringEmpty(deviceId))
                            {
                                ob1.userGetOTPChannels(false);
                                ob1.setUserChannelsListener(new GetData.UserChannelsListener() {
                                    @Override
                                    public void onObjectReady(String title) {

                                    }

                                    @Override
                                    public void onDataLoaded(String[] channels) {

                                        if(channels!=null && channels.length>0)
                                        {
                                            //Go To OTP Page Once Channels Have Been Saved
                                            goToChooseOTPPage();
                                        }


                                    }
                                });

                            }

                        }
                    });
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }


        });

		
        return parentView;
    }

    public void goToChooseOTPPage()
    {
        Intent mIntent = new Intent(parentActivity, ChooseOTPActivity.class);
        mIntent.putExtra(KEY_START_FROM, "splash");
        GeneralCommands.goToPage( parentActivity,mIntent,true);

    }


    @Override
    public void onResume(){
        super.onResume();
        //Log Event
        if(loadingView !=null)loadingView.setVisibility(View.GONE);

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
   
       
}
