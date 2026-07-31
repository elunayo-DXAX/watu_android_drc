package com.watudrc.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.watudrc.app.ChangePasswordActivity;
import com.watudrc.app.GeneralCommands;
import com.watudrc.app.LoginActivity;
import com.watudrc.app.MainActivity;
import com.watudrc.app.PreviewActivity;
import com.watudrc.app.R;
import com.watudrc.app.ResetPasswordActivity;
import com.watudrc.app.SetFingerPrintActivity;
import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.mobileservices.GetData;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;
import com.watudrc.app.view.FloatLabeledEditText;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.DialogPlus.Gravity;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.Locale;


public class FragmentChangePassword extends Fragment implements IWhereMyLocationConstants {

    private View parentView;


    private static StoreAndGetFromPrefs storageObject;
    public static ChangePasswordActivity parentActivity;
    static Context mContext;


    private EditText[] pinFields;
    private EditText[] confirmFields;
    private String enteredPin;

    RelativeLayout btnRegister;
    RelativeLayout loadingView;




    RelativeLayout wrongView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        parentView = inflater.inflate(R.layout.fragment_change_password, container, false);
        parentActivity = (ChangePasswordActivity)getActivity();
        mContext= parentActivity.getApplicationContext();
        storageObject = new StoreAndGetFromPrefs(mContext);



        // bind your 8 EditTexts into two arrays
        pinFields = new EditText[] {
                parentView.findViewById(R.id.codeEditText1),
                parentView.findViewById(R.id.codeEditText2),
                parentView.findViewById(R.id.codeEditText3),
                parentView.findViewById(R.id.codeEditText4)
        };
        confirmFields = new EditText[] {
                parentView.findViewById(R.id.codeConfirmEditText1),
                parentView.findViewById(R.id.codeConfirmEditText2),
                parentView.findViewById(R.id.codeConfirmEditText3),
                parentView.findViewById(R.id.codeConfirmEditText4)
        };

        // initialize behavior on both sets
        initPinBehavior(pinFields, false);
        initPinBehavior(confirmFields, true);

        btnRegister= (RelativeLayout) parentView.findViewById(R.id.btnRegister);
        wrongView= (RelativeLayout) parentView.findViewById(R.id.wrongView);



        loadingView= (RelativeLayout) parentActivity.findViewById(R.id.loadingItems);





        btnRegister.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub



                handleSubmit();


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


    private void openDialogForgotPassword()
    {
        // TODO Auto-generated method stub
        View contentView = parentActivity.getLayoutInflater().inflate(R.layout.dialog_content_reset_password, null);

        final DialogPlus dialogPlus = new DialogPlus.Builder(parentActivity)
                .setContentHolder(new ViewHolder(contentView))
                .setCancelable(true)
                .setBackgroundColorResourceId(R.color.transparent)
                .setGravity(Gravity.CENTER)
                .create();




        //Set Content View

        View content = dialogPlus.getHolderView();



        ImageView gifImage= (ImageView) content.findViewById(R.id.gifImage);

        try
        {


            Glide.with(parentActivity)
                    .asGif()
                    .load(R.drawable.loader) // Replace with your local GIF resource
                    .into(gifImage);

        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        final RelativeLayout loadingView = (RelativeLayout) content.findViewById(R.id.loadingItems);

        final FloatLabeledEditText emailEditText= (FloatLabeledEditText) content.findViewById(R.id.emailEditText);

        RelativeLayout btnCancel = (RelativeLayout) contentView.findViewById(R.id.btnCancel) ;
        btnCancel.setOnClickListener(new OnClickListener() {



            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                dialogPlus.dismiss();
                parentActivity.hideKeyboard();

            }


        });

        RelativeLayout btnConfirm = (RelativeLayout) content.findViewById(R.id.btnConfirm) ;
        btnConfirm.setOnClickListener(new OnClickListener() {



            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                parentActivity.hideKeyboard();
                if(emailEditText.getTextString().length()<1)
                {
                    String errorMsg= mContext.getResources().getString(R.string.enteremail);
                    emailEditText.setError(errorMsg);
                    Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();


                }

                else
                {


                    GetData ob1 = new GetData();
                    ob1.initialize(parentActivity, loadingView);
                    ob1.userForgotPassword(emailEditText.getText().toString(),dialogPlus);

                }
            }


        });

        dialogPlus.show();

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


    public void goToResetPasswordPage()
    {
        Intent mIntent = new Intent(parentActivity, ResetPasswordActivity.class);
        mIntent.putExtra(KEY_START_FROM, "splash");
        GeneralCommands.goToPage( parentActivity,mIntent,true);

    }

    public void goToMainPage()
    {
        Intent mIntent = new Intent(parentActivity, MainActivity.class);
        mIntent.putExtra(KEY_START_FROM, "splash");
        GeneralCommands.goToPage( parentActivity,mIntent,true);

    }


    public void goToSetFingerPrintPage()
    {
        Intent mIntent = new Intent(parentActivity, SetFingerPrintActivity.class);
        mIntent.putExtra(KEY_START_FROM, "splash");
        GeneralCommands.goToPage( parentActivity,mIntent,true);

    }

    public boolean isInteger(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            // It's not an integer
            return false;
        }
    }


    private void initPinBehavior(final EditText[] fields, final boolean isConfirm) {
        for (int i = 0; i < fields.length; i++) {
            final EditText current = fields[i];
            final EditText next    = (i < fields.length - 1 ? fields[i + 1] : null);
            final EditText prev    = (i > 0 ? fields[i - 1] : null);

            // only the first field in each group is enabled initially
            current.setEnabled(i == 0);

            // when text changes...
            current.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
                @Override public void onTextChanged(CharSequence s, int st, int b, int cnt) {}

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 1) {
                        // move forward
                        if (next != null) {
                            next.setEnabled(true);
                            next.requestFocus();
                        } else {
                            // last digit entered
                            if (!isConfirm) {
                                enteredPin = collectPin(fields);
                            } else {
                                String confirmPin = collectPin(fields);
                                checkPinsMatch(confirmPin);
                            }
                        }
                    }
                }
            });

            // handle back-delete navigation
            current.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN
                            && keyCode == KeyEvent.KEYCODE_DEL
                            && current.getText().length() == 0
                            && prev != null) {
                        prev.requestFocus();
                        prev.setText("");
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    /** Concatenate the 4 digits into a single string. */
    private String collectPin(EditText[] fields) {
        StringBuilder sb = new StringBuilder(4);
        for (EditText e : fields) {
            sb.append(e.getText().toString());
        }
        return sb.toString();
    }

    /** Called once confirm-pin is fully entered. */
    private void checkPinsMatch(String confirmPin) {
        if (!confirmPin.equals(enteredPin)) {
            // mismatch: show error, clear confirm fields, reset focus
//            Toast.makeText(parentActivity, "PINs do not match", Toast.LENGTH_SHORT).show();
            wrongView.setVisibility(View.VISIBLE);
            for (EditText e : confirmFields) {
                e.setText("");
                e.setEnabled(false);
            }
            confirmFields[0].setEnabled(true);
            confirmFields[0].requestFocus();
        } else {
            // success!
            wrongView.setVisibility(View.GONE);
            hideKeyboard();
//            Toast.makeText(parentActivity, "PIN set successfully", Toast.LENGTH_SHORT).show();
            // ...proceed to next step...
        }
    }

    /** Called when the user taps “Submit PIN” */
    private void handleSubmit() {
        String pin        = collectPin(pinFields);
        String confirmPin = collectPin(confirmFields);

        if (pin.length() < 4 || confirmPin.length() < 4) {
            Toast.makeText(parentActivity, mContext.getResources().getString(R.string.enterpindigits), Toast.LENGTH_SHORT).show();
            return;
        }

        if (pin.equals(confirmPin)) {
            // SUCCESS path
//            Toast.makeText(parentActivity, "PIN set successfully!", Toast.LENGTH_SHORT).show();
            wrongView.setVisibility(View.GONE);

            String message = mContext.getResources().getString(R.string.resetpasswordsuccess, storageObject.retrieveUserPhoneNumberFromPrefs());
            String title = mContext.getResources().getString(R.string.resetpassword);
            storageObject.saveUserPasswordToPrefs(pin);
            GeneralCommands.logEvent(parentActivity,"click_set_pin","click_set_pin");

            Toast.makeText(mContext, title, Toast.LENGTH_SHORT).show();;

            hideKeyboard();
//            GetData ob1 = new GetData();
//            ob1.initialize(parentActivity, loadingView);
//            ob1.userLogin(storageObject.retrieveUserPhoneNumberFromPrefs(),pin);
            goToSetFingerPrintPage();

            // … e.g. finish(), launch next Activity, etc.
        } else {
            // MISMATCH path
//            Toast.makeText(parentActivity, "PINs do not match", Toast.LENGTH_SHORT).show();
             wrongView.setVisibility(View.VISIBLE);
            // reset only the confirm fields so they retry
            for (EditText e : confirmFields) {
                e.setText("");
                e.setEnabled(false);
            }
            confirmFields[0].setEnabled(true);
            confirmFields[0].requestFocus();
        }
    }

    private void hideKeyboard() {
        View view = parentActivity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) parentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}
