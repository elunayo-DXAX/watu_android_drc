package com.watudrc.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.watudrc.app.ActivateAccountActivity;
import com.watudrc.app.GeneralCommands;
import com.watudrc.app.LanguageActivity;
import com.watudrc.app.LoginActivity;
import com.watudrc.app.MainActivity;
import com.watudrc.app.PreviewActivity;
import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;
import com.watudrc.app.util.LocaleUtils;
import com.watudrc.app.R;

import java.util.Locale;

public class FragmentLanguage extends Fragment implements IWhereMyLocationConstants {

    private View parentView;

    private static StoreAndGetFromPrefs storageObject;
    public static LanguageActivity parentActivity;
    static Context mContext;

    RelativeLayout loadingView;

    RelativeLayout btnEnglish,btnFrench;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        parentView = inflater.inflate(R.layout.fragment_language, container, false);
        parentActivity = (LanguageActivity)getActivity();
        mContext= parentActivity.getApplicationContext();
        storageObject = new StoreAndGetFromPrefs(mContext);
        loadingView= parentView.findViewById(R.id.loadingItems);

        btnEnglish= parentView.findViewById(R.id.btnEnglish);
        btnFrench= parentView.findViewById(R.id.btnFrench);

        btnEnglish.setOnClickListener(v -> {

            GeneralCommands.logEvent(parentActivity,"click_language_english","click_language_english");

            setLocale("en");

        });

        btnFrench.setOnClickListener(v -> {
            GeneralCommands.logEvent(parentActivity,"click_language_kifrench","click_language_kifrench");

            setLocale("fr");
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

    public void setLocale(String lang) {
        // Step 1: Update Locale Globally
        LocaleUtils.setLocale(parentActivity, lang);

        // Step 2: Save Language to Preferences
        storageObject.saveLanguageToPrefs(lang);

        // Step 3: Restart the Entire Application
        if (!storageObject.retrieveLanguageFromPrefs().equals(lang)) {
            LocaleUtils.setLocale(parentActivity, lang);
        }


        // Fetch English string
        String englishString = getLocalizedString(mContext, R.string.languagechanged,lang);
        String btnTextEn = getLocalizedString(mContext, R.string.done,lang);


        String message= englishString;
        String language=mContext.getResources().getString(R.string.english);
        String btnText=btnTextEn;

        if(lang.equals("fr"))
        {
            language=mContext.getResources().getString(R.string.french_);
        }

        // Show a dialog to restart the app
        String finalMessage = message;
        String finalLanguage= language;
        String finalBtnText= btnText;

        new AlertDialog.Builder(parentActivity)
                .setTitle(finalMessage)
                .setMessage(finalLanguage)
                .setPositiveButton(finalBtnText, (dialog, which) -> {

                    Intent intent = new Intent(parentActivity, LoginActivity.class);
                    if(!storageObject.checkIfUserIsRegistered())
                    {
                        intent = new Intent(parentActivity, PreviewActivity.class);
                    }




                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    parentActivity.startActivity(intent);

                    Toast.makeText(parentActivity, finalMessage+" "+ finalLanguage, Toast.LENGTH_SHORT).show();

                    // Terminate the app process
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);
                })
                .setCancelable(false)
                .show();


    }

    public static String getLocalizedString(Context context, int stringResId, String languageCode) {
        // Create a new Locale for the desired language
        Locale locale = new Locale(languageCode);

        // Get resources and create a new configuration for the locale
        Resources resources = context.getResources();
        Configuration configuration = new Configuration(resources.getConfiguration());
        configuration.setLocale(locale);

        // Create a localized context
        Context localizedContext = context.createConfigurationContext(configuration);

        // Fetch the string resource for the desired locale
        return localizedContext.getResources().getString(stringResId);
    }



}
