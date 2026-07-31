package com.watudrc.app.fragments;

import static com.watudrc.app.MainActivity.goToChatPage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.watudrc.app.GeneralCommands;
import com.watudrc.app.MainActivity;
import com.watudrc.app.R;
import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.font.FirmaTextView;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;
import com.watudrc.app.util.FirebaseConfig;
import com.watudrc.app.util.FragmentUtils;

public class FragmentPrepaymentProcess extends Fragment implements IWhereMyLocationConstants {

    private View parentView;

    private static StoreAndGetFromPrefs storageObject;
    public static Activity parentActivity;
    static Context mContext;

    RelativeLayout loadingView;
    FirebaseConfig remoteConfig;
    FirebaseRemoteConfig mFirebaseConfig;

    RelativeLayout btnChatWithUs;
    RelativeLayout btnReplacementProcess;
    RelativeLayout btnServiceCenter;
    RelativeLayout btnBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_prepayment_process, container, false);
        parentActivity = getActivity();
        mContext= parentActivity;
        storageObject = new StoreAndGetFromPrefs(mContext);
        loadingView= parentView.findViewById(R.id.loadingItems);

        // Set colored/bold text for each step
        FirmaTextView step1Text = parentView.findViewById(R.id.step1Text);
        FirmaTextView step2Text = parentView.findViewById(R.id.step2Text);
        FirmaTextView step3Text = parentView.findViewById(R.id.step3Text);
        FirmaTextView step4Text = parentView.findViewById(R.id.step4Text);
        FirmaTextView step5Text = parentView.findViewById(R.id.step5Text);
        FirmaTextView step6Text = parentView.findViewById(R.id.step6Text);
        FirmaTextView step7Text = parentView.findViewById(R.id.step7Text);
        FirmaTextView step8Text = parentView.findViewById(R.id.step8Text);
        FirmaTextView step9Text = parentView.findViewById(R.id.step9Text);
        FirmaTextView step10Text = parentView.findViewById(R.id.step10Text);

        if (step1Text != null) step1Text.setText(Html.fromHtml(getString(R.string.loan_prepayment_description)));
        if (step2Text != null) step2Text.setText(Html.fromHtml(getString(R.string.prepayment_amount_note)));
        if (step3Text != null) step3Text.setText(Html.fromHtml(getString(R.string.remaining_loan_balance)));
        if (step4Text != null) step4Text.setText(Html.fromHtml(getString(R.string.any_unpaid_interest)));
        if (step5Text != null) step5Text.setText(Html.fromHtml(getString(R.string.any_overdue_charges_if_applicable)));
        if (step6Text != null) step6Text.setText(Html.fromHtml(getString(R.string.account_status_notice)));
        if (step7Text != null) step7Text.setText(Html.fromHtml(getString(R.string.exact_amount_info)));
        if (step8Text != null) step8Text.setText(Html.fromHtml(getString(R.string.watu_contact_info)));
        if (step9Text != null) step9Text.setText(Html.fromHtml(getString(R.string.visit_watu_branch)));
        if (step10Text != null) step10Text.setText(Html.fromHtml(getString(R.string.next_steps)));


        btnChatWithUs = parentView.findViewById(R.id.btnChatWithUs);
        btnReplacementProcess = parentView.findViewById(R.id.btnReplacementProcess);
        btnServiceCenter = parentView.findViewById(R.id.btnServiceCenter);

        btnBack= parentView.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentUtils.safePopBackStack(FragmentPrepaymentProcess.this);
            }
        });


        btnChatWithUs.setOnClickListener(v -> {
            // To DO: Open chat/support screen
            if (FragmentUtils.getSafeContext(FragmentPrepaymentProcess.this) != null) {
                GeneralCommands.logEvent(FragmentUtils.getSafeContext(FragmentPrepaymentProcess.this),"support_hub_opened_resource_prepayment_process","support_hub_opened_resource_prepayment_process");
                goToChatPage("resources_phone_unlock");
            }
        });
        btnReplacementProcess.setOnClickListener(v -> {
            // Open replacement process screen
            FragmentUtils.safeFragmentTransaction(FragmentPrepaymentProcess.this, fragmentManager -> {
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, new FragmentReplacementProcess())
                        .addToBackStack(null)
                        .commit();
            });
        });
        btnServiceCenter.setOnClickListener(v -> {
            // Open service center screen
            FragmentUtils.safeFragmentTransaction(FragmentPrepaymentProcess.this, fragmentManager -> {
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment, new FragmentServiceCenter())
                        .addToBackStack(null)
                        .commit();
            });
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
                GeneralCommands.logEvent(parentActivity,"view_prepayment_process_page","view_prepayment_process_page");
                // TODO stop audio playback
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        GeneralCommands.logEvent(parentActivity,"view_prepayment_process_page","view_prepayment_process_page");

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


}