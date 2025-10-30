package com.watutz.app.fragments;

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

import com.watutz.app.GeneralCommands;
import com.watutz.app.MainActivity;
import com.watutz.app.R;
import com.watutz.app.TermsActivity;
import com.watutz.app.constant.IWhereMyLocationConstants;
import com.watutz.app.font.FirmaTextView;
import com.watutz.app.prefs.StoreAndGetFromPrefs;
import com.watutz.app.view.UITabs;

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
        loadingView= (RelativeLayout) parentView.findViewById(R.id.loadingItems);

        termsContent=(FirmaTextView) parentView.findViewById(R.id.termsContent);
        termsTitle=(FirmaTextView) parentView.findViewById(R.id.termsTitle);

        declineText=(FirmaTextView) parentView.findViewById(R.id.declineText);
        acceptText=(FirmaTextView) parentView.findViewById(R.id.acceptText);


        btnAccept=(RelativeLayout)parentView.findViewById(R.id.btnAccept);
        btnDecline=(RelativeLayout)parentView.findViewById(R.id.btnDecline);


        tabs=(UITabs) parentView.findViewById(R.id.chooseTabs);

        spanned = HtmlCompat.fromHtml(parentActivity.terms, HtmlCompat.FROM_HTML_MODE_COMPACT);
        termsContent.setText(spanned);


        tabs.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId)
                {
                    case R.id.tabSwahili:
                        spanned = HtmlCompat.fromHtml(parentActivity.terms_sw, HtmlCompat.FROM_HTML_MODE_COMPACT);
                        termsContent.setText(spanned);
                        termsTitle.setText("Ilani ya Faragha");
                        acceptText.setText("Kubali");
                        declineText.setText("Kataa");

                        return;


                    case R.id.tabEnglish:
                        spanned = HtmlCompat.fromHtml(parentActivity.terms, HtmlCompat.FROM_HTML_MODE_COMPACT);
                        termsContent.setText(spanned);
                        termsTitle.setText(mContext.getResources().getString(R.string.privacynotice));

                        acceptText.setText(mContext.getResources().getString(R.string.accept));
                        declineText.setText(mContext.getResources().getString(R.string.decline));

                        return;

                    default:
                        spanned = HtmlCompat.fromHtml(parentActivity.terms, HtmlCompat.FROM_HTML_MODE_COMPACT);
                        termsContent.setText(spanned);
                        termsTitle.setText(mContext.getResources().getString(R.string.privacynotice));

                        return;
                }
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
