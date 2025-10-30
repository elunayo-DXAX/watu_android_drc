package com.watutz.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.watutz.app.GeneralCommands;
import com.watutz.app.MainActivity;
import com.watutz.app.OpenActivity;
import com.watutz.app.R;
import com.watutz.app.constant.IWhereMyLocationConstants;
import com.watutz.app.prefs.StoreAndGetFromPrefs;

public class OpenFragment extends Fragment implements IWhereMyLocationConstants {

    private View parentView;
   
    private static StoreAndGetFromPrefs storageObject;
    public static OpenActivity parentActivity;
	static Context mContext;

	
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        parentView = inflater.inflate(R.layout.fragment_open, container, false);
        parentActivity = (OpenActivity)getActivity();
        mContext= parentActivity.getApplicationContext();
        storageObject = new StoreAndGetFromPrefs(mContext);
        
       

		
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

    public void goToMainPage()
    {
        Intent mIntent = new Intent(parentActivity, MainActivity.class);
        mIntent.putExtra(KEY_START_FROM, "splash");
        GeneralCommands.goToPage(parentActivity, mIntent,false);

    }
    
   
       
}
