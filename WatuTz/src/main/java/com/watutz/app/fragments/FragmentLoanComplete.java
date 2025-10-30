package com.watutz.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.watutz.app.GeneralCommands;
import com.watutz.app.LoanCompleteActivity;
import com.watutz.app.MainActivity;
import com.watutz.app.R;
import com.watutz.app.constant.IWhereMyLocationConstants;
import com.watutz.app.prefs.StoreAndGetFromPrefs;

import static com.bumptech.glide.load.resource.gif.GifDrawable.LOOP_FOREVER;

public class FragmentLoanComplete extends Fragment implements IWhereMyLocationConstants {

    private View parentView;
   
    private static StoreAndGetFromPrefs storageObject;
    public static LoanCompleteActivity parentActivity;
	static Context mContext;

	RelativeLayout loadingView;

    RelativeLayout btnHome;

    ImageView adCard;

	
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        parentView = inflater.inflate(R.layout.fragment_loan_complete, container, false);
        parentActivity = (LoanCompleteActivity)getActivity();
        mContext= parentActivity.getApplicationContext();
        storageObject = new StoreAndGetFromPrefs(mContext);
        loadingView= (RelativeLayout) parentView.findViewById(R.id.loadingItems);

        btnHome= parentView.findViewById(R.id.btnHome);
        adCard= parentView.findViewById(R.id.adCard);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainPage();
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

    public void goToMainPage()
    {
        Intent mIntent = new Intent(parentActivity, MainActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
        GeneralCommands.goToPage(parentActivity, mIntent,true);

    }
   
       
}
