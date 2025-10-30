package com.watutz.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.watutz.app.R;
import com.watutz.app.SelectCountryActivity;
import com.watutz.app.SignInActivity;
import com.watutz.app.adapters.CountriesListAdapter;
import com.watutz.app.constant.IWhereMyLocationConstants;
import com.watutz.app.objects.Country;
import com.watutz.app.prefs.StoreAndGetFromPrefs;
import com.ypyproductions.utils.DirectionUtils;

import java.util.ArrayList;

public class FragmentSelectCountry extends Fragment implements IWhereMyLocationConstants {

    private View parentView;
   
    private static StoreAndGetFromPrefs storageObject;
    public static SelectCountryActivity parentActivity;
	static Context mContext;

	CountriesListAdapter mAdapter;
	ListView listView;
	boolean isCountrySelected=true;

	RelativeLayout btnContinue;

	
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        parentView = inflater.inflate(R.layout.fragment_select_country, container, false);
        parentActivity = (SelectCountryActivity)getActivity();
        mContext= parentActivity.getApplicationContext();
        storageObject = new StoreAndGetFromPrefs(mContext);

        btnContinue= (RelativeLayout) parentView.findViewById(R.id.btnContinue);

        listView= (ListView) parentView.findViewById(R.id.listView);
        mAdapter= new CountriesListAdapter(parentActivity,getListData());
        listView.setAdapter(mAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Country country = (Country) mAdapter.getItem(i);
                storageObject.saveUserCountryToPrefs(country.getName());
                storageObject.saveUserCountryCodeToPrefs(country.getCode());
                deselectAllItems();
                country.setSelected(true);
                isCountrySelected= true;
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isCountrySelected)
                {
                    String err= mContext.getResources().getString(R.string.selectcountry);
                    Toast.makeText(mContext, err, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    goToSignInPage();
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
              
                // TODO stop audio playback
            }
        }
    }
    
    
   public ArrayList<Country> getListData()
   {
       ArrayList<Country> listData= new ArrayList<>();

       Country c1= new Country();
       c1.setName("Kenya");
       c1.setCode("KE");
       c1.setRes(R.drawable.ics_country_kenya);
       c1.setSelected(true);
       storageObject.saveUserCountryToPrefs("Kenya");
       storageObject.saveUserCountryCodeToPrefs("KE");

       Country c2= new Country();
       c2.setName("Uganda");
       c2.setCode("UG");
       c2.setRes(R.drawable.ics_country_uganda);


       Country c3= new Country();
       c3.setName("Rwanda");
       c3.setCode("RW");
       c3.setRes(R.drawable.ics_country_rwanda);
//
//
//       Country c4= new Country();
//       c4.setName("Nigeria");
//       c4.setCode("NG");
//       c4.setRes(R.drawable.ics_country_nigeria);

       listData.add(c1);
       listData.add(c2);
       listData.add(c3);
      // listData.add(c4);


       return listData;
   }


    public void goToSignInPage()
    {
        Intent mIntent = new Intent(parentActivity, SignInActivity.class);
        mIntent.putExtra(KEY_START_FROM, "signin");
        DirectionUtils.changeActivity(parentActivity, R.anim.slide_in_from_right, R.anim.slide_out_to_left, false, mIntent);

    }

    public void deselectAllItems()
    {
        for(int i=0;i<mAdapter.getCount();i++)
        {
            Country country= (Country) mAdapter.getItem(i);
            country.setSelected(false);
        }

        mAdapter.notifyDataSetChanged();
    }
       
}
