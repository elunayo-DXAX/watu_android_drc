package com.watutz.app.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;
import com.watutz.app.GeneralCommands;
import com.watutz.app.R;
import com.watutz.app.ResetPasswordActivity;
import com.watutz.app.SharedObjects;
import com.watutz.app.constant.IWhereMyLocationConstants;
import com.watutz.app.mobileservices.GetData;
import com.watutz.app.prefs.StoreAndGetFromPrefs;

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
        loadingView= (RelativeLayout) parentView.findViewById(R.id.loadingItems);



        idNumberEditText = (EditText) parentView.findViewById(R.id.idNumberEditText);


        ccp = (CountryCodePicker) parentView.findViewById(R.id.ccp);
        phoneNumberEditText = (EditText) parentView.findViewById(R.id.phoneNumberEditText);

        ccp.registerCarrierNumberEditText(phoneNumberEditText);
        ccp.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @Override
            public void onValidityChanged(boolean isValidNumber) {
                isPhoneNumberValid=isValidNumber;
            }
        });
        ccp.setNumberAutoFormattingEnabled(false);

        btnResetPassword= (RelativeLayout) parentView.findViewById(R.id.btnResetPassword);
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {


               if(phoneNumberEditText.getText().toString().length()<1)
               {

                   phoneNumberEditText.setError(null);

                   String errorMsg= mContext.getResources().getString(R.string.entervalidphonenumber);
                   phoneNumberEditText.setError(errorMsg);
                   Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();


               }
               else
               {

                   GeneralCommands.logEvent(SharedObjects.context, "click_reset_password","click_reset_password");;


                   storageObject.saveUserPhoneNumberToPrefs(ccp.getFullNumber());
                   storageObject.saveUserIDNumberToPrefs(idNumberEditText.getText().toString());

                   parentActivity.hideKeyboard();
                   GetData ob1 = new GetData();
                   ob1.initialize(parentActivity, loadingView);
                   try {
                       ob1.userRegister(ccp.getFullNumber(),false);
                       ob1.setUserRegisterListener(new GetData.UserRegisterListener() {
                           @Override
                           public void onObjectReady(String title) {

                           }

                           @Override
                           public void onDataLoaded(String deviceId) {
                               if(!deviceId.equals(""))
                               {
                                   ob1.userOTPRequest(ccp.getFullNumber(),false);

                               }
                               else
                               {
                                   ob1.problemLoading(mContext.getResources().getString(R.string.resetpassworderror),400);
                               }
                           }
                       });
                   } catch (InvalidAlgorithmParameterException e) {
                       ob1.problemLoading(mContext.getResources().getString(R.string.resetpassworderror),101);

                       e.printStackTrace();
                   } catch (NoSuchAlgorithmException e) {
                       ob1.problemLoading(mContext.getResources().getString(R.string.resetpassworderror),102);

                       e.printStackTrace();
                   }


               }
           }
       });

		
        return parentView;
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
