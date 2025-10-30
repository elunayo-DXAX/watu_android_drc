package com.watutz.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.watutz.app.ActivateAccountActivity;
import com.watutz.app.LoginActivity;
import com.watutz.app.R;
import com.watutz.app.SignInActivity;
import com.watutz.app.constant.IWhereMyLocationConstants;
import com.watutz.app.prefs.StoreAndGetFromPrefs;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;
import com.ypyproductions.utils.DirectionUtils;

public class FragmentSignIn extends Fragment implements IWhereMyLocationConstants {

    private View parentView;
   
    private static StoreAndGetFromPrefs storageObject;
    public static SignInActivity parentActivity;
	static Context mContext;

    //Preview Images
    CarouselView previewImagesView;
    int[] previewImages = {R.drawable.ic_preview_1,R.drawable.ic_preview_2,R.drawable.ic_preview_3};

    RelativeLayout btnLogin,btnRegister,btnActivateAccount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        parentView = inflater.inflate(R.layout.fragment_sign_in, container, false);
        parentActivity = (SignInActivity)getActivity();
        mContext= parentActivity.getApplicationContext();
        storageObject = new StoreAndGetFromPrefs(mContext);

        btnLogin= (RelativeLayout) parentView.findViewById(R.id.btnLogin);
        btnRegister= (RelativeLayout) parentView.findViewById(R.id.btnRegister);
        btnActivateAccount= (RelativeLayout) parentView.findViewById(R.id.btnActivateAccount);


        previewImagesView = (CarouselView) parentView.findViewById(R.id.previewImagesView);
        previewImagesView.setPageCount(previewImages.length);
        previewImagesView.setSlideInterval(2000);
        previewImagesView.isAutoPlay();



        previewImagesView.setImageListener(new ImageListener() {

            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                // TODO Auto-generated method stub

                imageView.setImageResource(previewImages[position]);


            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLoginPage();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToRegisterPage();
            }
        });

        btnActivateAccount.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                goToLoginPage();
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

    public void goToLoginPage()
    {
        Intent mIntent = new Intent(parentActivity, LoginActivity.class);
        mIntent.putExtra(KEY_START_FROM, "splash");
        DirectionUtils.changeActivity(parentActivity, R.anim.slide_in_from_right, R.anim.slide_out_to_left, false, mIntent);

    }

    public void goToRegisterPage()
    {
        Intent mIntent = new Intent(parentActivity, ActivateAccountActivity.class);
        mIntent.putExtra(KEY_START_FROM, "splash");
        DirectionUtils.changeActivity(parentActivity, R.anim.slide_in_from_right, R.anim.slide_out_to_left, false, mIntent);

    }

    public void goToActivateAccountPage()
    {
        Intent mIntent = new Intent(parentActivity, ActivateAccountActivity.class);
        mIntent.putExtra(KEY_START_FROM, "splash");
        DirectionUtils.changeActivity(parentActivity, R.anim.slide_in_from_right, R.anim.slide_out_to_left, false, mIntent);

    }
       
}
