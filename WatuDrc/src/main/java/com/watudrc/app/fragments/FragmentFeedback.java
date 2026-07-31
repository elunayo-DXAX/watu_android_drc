package com.watudrc.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.watudrc.app.FeedbackActivity;
import com.watudrc.app.GeneralCommands;
import com.watudrc.app.MainActivity;
import com.watudrc.app.R;
import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.mobileservices.GetData;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;

import org.json.JSONException;
import org.json.JSONObject;

public class FragmentFeedback extends Fragment implements IWhereMyLocationConstants {

    private View parentView;
   
    private static StoreAndGetFromPrefs storageObject;
    public static FeedbackActivity parentActivity;
	static Context mContext;

	RelativeLayout loadingView;
    String selectedOption="payment";
    EditText commentEditText;
    RelativeLayout btnSubmit;

    CheckBox rdPayment,rdAccountDetails,rdOther,rdTransactions,rdUserExperience;


	
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        parentView = inflater.inflate(R.layout.fragment_feedback, container, false);
        parentActivity = (FeedbackActivity)getActivity();
        mContext= parentActivity.getApplicationContext();
        storageObject = new StoreAndGetFromPrefs(mContext);
        loadingView= (RelativeLayout) parentView.findViewById(R.id.loadingItems);

        rdPayment = (CheckBox) parentView.findViewById(R.id.rdPayment);
        rdAccountDetails = (CheckBox) parentView.findViewById(R.id.rdAccountDetails);
        rdOther = (CheckBox) parentView.findViewById(R.id.rdOther);
        rdTransactions = (CheckBox) parentView.findViewById(R.id.rdTransactions);
        rdUserExperience = (CheckBox) parentView.findViewById(R.id.rdUserExperience);

        btnSubmit = (RelativeLayout) parentView.findViewById(R.id.btnSubmit);
        commentEditText = (EditText) parentView.findViewById(R.id.commentEditText);


        rdPayment.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    selectedOption="Payment Issues";
                    rdAccountDetails.setChecked(false);
                    rdOther.setChecked(false);
                    rdTransactions.setChecked(false);
                    rdUserExperience.setChecked(false);
                }
            }
        });



        rdAccountDetails.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    selectedOption="Account Details";
                    rdPayment.setChecked(false);
                    rdOther.setChecked(false);
                    rdTransactions.setChecked(false);
                    rdUserExperience.setChecked(false);
                }
            }
        });


        rdOther.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    selectedOption="General";
                    rdPayment.setChecked(false);
                    rdAccountDetails.setChecked(false);
                    rdTransactions.setChecked(false);
                    rdUserExperience.setChecked(false);
                }
            }
        });



        rdTransactions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    selectedOption="Account Details";
                    rdPayment.setChecked(false);
                    rdOther.setChecked(false);
                    rdAccountDetails.setChecked(false);
                    rdUserExperience.setChecked(false);
                }
            }
        });

        rdUserExperience.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    selectedOption="User Experience";
                    rdPayment.setChecked(false);
                    rdOther.setChecked(false);
                    rdTransactions.setChecked(false);
                    rdAccountDetails.setChecked(false);
                }
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(commentEditText.getText().length()<1)
                {
                    String err=mContext.getResources().getString(R.string.pleasetype);

                    Toast.makeText(mContext, err, Toast.LENGTH_SHORT).show();
                    commentEditText.setError(err);
                }
                else {
                    //All GOod

                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("selectedOption",selectedOption);
                        obj.put("comment",commentEditText.getText().toString());


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    GeneralCommands.logEventObject(parentActivity,"click_submit_feedback","click_submit_feedback",obj);



                    commentEditText.setError(null);
                    GetData ob1 = new GetData();
                    ob1.initialize(parentActivity,loadingView);
                    ob1.userSubmitFeedback(selectedOption, commentEditText.getText().toString(),false);
                    ob1.setSubmitFeedbackListener(new GetData.SubmitFeedbackListener() {
                        @Override
                        public void onObjectReady(String title) {

                        }

                        @Override
                        public void onDataLoaded(boolean isSuccess) {

                            if(isSuccess)
                            {
                                parentActivity.goBack();

                            }
                        }
                    });



                }
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
