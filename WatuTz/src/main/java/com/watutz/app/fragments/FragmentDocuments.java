package com.watutz.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.watutz.app.DocumentsActivity;
import com.watutz.app.GeneralCommands;
import com.watutz.app.MainActivity;
import com.watutz.app.R;
import com.watutz.app.constant.IWhereMyLocationConstants;
import com.watutz.app.mobileservices.GetData;
import com.watutz.app.prefs.StoreAndGetFromPrefs;

public class FragmentDocuments extends Fragment implements IWhereMyLocationConstants {

    private View parentView;
   
    private static StoreAndGetFromPrefs storageObject;
    public static DocumentsActivity parentActivity;
	static Context mContext;

	RelativeLayout loadingView;

    RelativeLayout btnLoanAgreement;
    RelativeLayout btnStatement;

	
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        parentView = inflater.inflate(R.layout.fragment_documents, container, false);
        parentActivity = (DocumentsActivity)getActivity();
        mContext= parentActivity.getApplicationContext();
        storageObject = new StoreAndGetFromPrefs(mContext);
        loadingView= (RelativeLayout) parentView.findViewById(R.id.loadingItems);

        btnLoanAgreement=(RelativeLayout) parentView.findViewById(R.id.btnLoanAgreement);
        btnStatement=(RelativeLayout) parentView.findViewById(R.id.btnStatement);

        btnLoanAgreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                String loanNumber = storageObject.retrieveCurrentAssetObjectFromPrefs().getLoanNumber();
//                loanNumber="AF000000296";
//                loanNumber="AF000000313";

                GetData ob1 = new GetData();
                ob1.initialize(parentActivity,loadingView);
                ob1.userDownloadLoanAgreement(loanNumber,false);

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
   
       
}
