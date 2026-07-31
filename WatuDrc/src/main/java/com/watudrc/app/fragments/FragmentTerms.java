package com.watudrc.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import com.watudrc.app.GeneralCommands;
import com.watudrc.app.MainActivity;
import com.watudrc.app.R;
import com.watudrc.app.TermsActivity;
import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.font.FirmaTextView;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;
import com.watudrc.app.view.UITabs;

public class FragmentTerms extends Fragment implements IWhereMyLocationConstants {

    private View parentView;

    private static StoreAndGetFromPrefs storageObject;
    public static TermsActivity parentActivity;
    static Context mContext;

    RelativeLayout loadingView;
    FirmaTextView termsContent,termsTitle;

    FirmaTextView acceptText,declineText;

    RelativeLayout btnDecline,btnAccept;

    UITabs tabs;
    Spanned spanned;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        parentView = inflater.inflate(R.layout.fragment_terms, container, false);
        parentActivity = (TermsActivity)getActivity();
        mContext= parentActivity.getApplicationContext();
        storageObject = new StoreAndGetFromPrefs(mContext);
        loadingView= parentView.findViewById(R.id.loadingItems);

        termsContent= parentView.findViewById(R.id.termsContent);
        termsTitle= parentView.findViewById(R.id.termsTitle);

        declineText= parentView.findViewById(R.id.declineText);
        acceptText= parentView.findViewById(R.id.acceptText);


        btnAccept= parentView.findViewById(R.id.btnAccept);
        btnDecline= parentView.findViewById(R.id.btnDecline);


        tabs= parentView.findViewById(R.id.chooseTabs);

        spanned = HtmlCompat.fromHtml(parentActivity.terms, HtmlCompat.FROM_HTML_MODE_COMPACT);
        termsContent.setText(spanned);


        tabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.tabFrench) {
                    spanned = HtmlCompat.fromHtml(parentActivity.terms_fr, HtmlCompat.FROM_HTML_MODE_COMPACT);
                    termsContent.setText(spanned);
                    termsTitle.setText("Avis de Confidentialité");
                    acceptText.setText("Accepter");
                    declineText.setText("Refuser");
                    return;
                }

                if (checkedId == R.id.tabEnglish) {
                    spanned = HtmlCompat.fromHtml(parentActivity.terms, HtmlCompat.FROM_HTML_MODE_COMPACT);
                    termsContent.setText(spanned);
                    termsTitle.setText("Privacy Notice");
                    acceptText.setText("Accept");
                    declineText.setText("Decline");
                    return;
                }

                spanned = HtmlCompat.fromHtml(parentActivity.terms, HtmlCompat.FROM_HTML_MODE_COMPACT);
                termsContent.setText(spanned);
                termsTitle.setText(mContext.getResources().getString(R.string.privacynotice));
            }
        });


        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storageObject.setIsAgreedToTerms(true);

                //Log Event
                GeneralCommands.logEvent(parentActivity,"click_accept_terms","click_accept_terms");

                Toast.makeText(mContext,mContext.getResources().getString(R.string.accepted),Toast.LENGTH_SHORT).show();
                parentActivity.goBack();
            }
        });

        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Log Event
                GeneralCommands.logEvent(parentActivity,"click_decline_terms","click_decline_terms");

                storageObject.setIsAgreedToTerms(false);
                Toast.makeText(mContext,mContext.getResources().getString(R.string.declined),Toast.LENGTH_SHORT).show();
                parentActivity.goBack();
            }
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

                // TODO stop audio playback
            }
        }
    }

    public void goToPage()
    {
        Intent mIntent = new Intent(parentActivity, MainActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
        GeneralCommands.goToPage(parentActivity, mIntent,false);

    }


}
