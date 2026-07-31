package com.watudrc.app.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.watudrc.app.GeneralCommands;
import com.watudrc.app.MainActivity;
import com.watudrc.app.R;
import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.mobileservices.GetData;
import com.watudrc.app.objects.AssetObject;
import com.watudrc.app.objects.LoanListItemObject;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;

import java.util.ArrayList;

public class FragmentHomeNew extends Fragment implements IWhereMyLocationConstants {

    private View parentView;
   
    private static StoreAndGetFromPrefs storageObject;
    public static MainActivity parentActivity;
	static Context mContext;
	RelativeLayout loadingView;
    private SwipeRefreshLayout swipeRefreshLayout;

    SnapHelper snapHelper = new PagerSnapHelper();



    public static RecyclerView listView;

    ArrayList<AssetObject> listData= new ArrayList<>();

    int position=0;
    static String loanItemName="";

    //Network Check
    BroadcastReceiver networkChangeReceiver;
    ImageView progressWheelNetwork;
    RelativeLayout networkView;
    RelativeLayout btnNetwork;
    private boolean isReceiverRegistered = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        parentActivity = (MainActivity)getActivity();
        mContext= parentActivity.getApplicationContext();
        storageObject = new StoreAndGetFromPrefs(mContext);
        parentView = inflater.inflate(R.layout.fragment_home_new, container, false);
        loadingView= parentView.findViewById(R.id.loadingItems);

        //Network
        networkView= loadingView.findViewById(R.id.networkView);
        btnNetwork= loadingView.findViewById(R.id.btnNetwork);
        progressWheelNetwork= loadingView.findViewById(R.id.progressWheelNetwork);


        listView= parentView.findViewById(R.id.listView);

        if(storageObject.retrieveAssetObjectObjectsFromPrefs().size()>0)
        {

            listData=storageObject.retrieveAssetObjectObjectsFromPrefs();
            storageObject.saveCurrentAssetObjectToPrefs(listData.get(0));

            LinearLayoutManager layoutManager = new LinearLayoutManager(parentActivity, LinearLayoutManager.HORIZONTAL, false);
            listView.setLayoutManager(layoutManager);


        }



        new Thread(new Runnable() {
            public void run() {
                // a potentially time consuming task
                parentView.post(new Runnable() {
                    public void run() {
                        setAdapter();
                    }
                });
            }
        }).start();


        // Initialize SwipeRefreshLayout
        swipeRefreshLayout = parentView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Implement your refresh logic here
                // Typically, you would initiate data loading or perform any other refresh action
                // Once the refresh action is complete, call setRefreshing(false) to stop the loading animation
                new Thread(new Runnable() {
                    public void run() {
                        // a potentially time consuming task
                        parentView.post(new Runnable() {
                            public void run() {

                                setAdapter();
                            }
                        });
                    }
                }).start();
            }
        });


        snapHelper.attachToRecyclerView(listView);


        //Network Check
        networkChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                // Check if network is connected
                if (networkInfo != null && networkInfo.isConnected()) {
                    // Network is connected - update the fragment views
                    loadingView.setVisibility(View.GONE);
                    networkView.setVisibility(View.GONE);

                    storageObject.saveTimeLastOnline(System.currentTimeMillis());


//					Toast.makeText(context, "Network is ON", Toast.LENGTH_SHORT).show();
                } else {
                    // Network is disconnected - update the fragment views
                    loadingView.setVisibility(View.VISIBLE);
                    networkView.setVisibility(View.VISIBLE);

                    try
                    {
                        Glide.with(parentActivity)
                                .asGif()
                                .load(R.drawable.loader_network) // Replace with your local GIF resource
                                .into(progressWheelNetwork);

                    } catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


//					Toast.makeText(context, "Network is OFF", Toast.LENGTH_SHORT).show();
                }
            }
        };

        btnNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeneralCommands.logEvent(parentActivity,"click_turn_on_data","click_turn_on_data");

                // Open network settings
                openDataUsageSettings(parentActivity);
            }
        });



        //End Network



        return parentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Register the receiver if it's not registered
        if (!isReceiverRegistered && getContext() != null) {
            try {
                IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
                getContext().registerReceiver(networkChangeReceiver, filter);
                isReceiverRegistered = true;
            } catch (Exception e) {
                // Handle any exceptions during registration
                e.printStackTrace();
            }
        }
    }

    //Network
    @Override
    public void onStop() {
        super.onStop();
        // Unregister the receiver if it's registered
        if (isReceiverRegistered && getContext() != null) {
            try {
                getContext().unregisterReceiver(networkChangeReceiver);
                isReceiverRegistered = false;
            } catch (Exception e) {
                // Handle the case where the receiver was not registered
                e.printStackTrace();
            }

        }
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

    public void goToTransactionsPage()
    {
//        Intent mIntent = new Intent(parentActivity, TransactionsActivity.class);
//        mIntent.putExtra(KEY_START_FROM, "user_accounts");
//        GeneralCommands.goToPage(parentActivity, mIntent,false);

    }




    public void setAdapter()
    {
        //Add Images
        getListData();




        swipeRefreshLayout.setRefreshing(false);


    }
    public ArrayList<AssetObject> getListData()
    {
        ArrayList<LoanListItemObject> accounts= storageObject.retrieveLoanListItemObjectObjectsFromPrefs();
//        storageObject.saveAssetObjectObjectsToPrefs(new ArrayList<>());
        ArrayList<AssetObject> assets= new ArrayList<>();
//        listData=storageObject.retrieveAssetObjectObjectsFromPrefs();

        for (int i=0;i<accounts.size();i++) {
            LoanListItemObject item= accounts.get(i);

            GetData ob1 = new GetData();
            ob1.initialize(parentActivity,loadingView);
            ob1.userGetLoanDetails(item.getLoanNumber(),true);
            int finalI = i;
            ob1.setLoanDetailsListener(new GetData.LoanDetailsListener() {
                @Override
                public void onObjectReady(String title) {

                }

                @Override
                public void onDataLoaded(AssetObject data) {

                    if(data !=null)
                    {
                        data.setLoanNumber(item.getLoanNumber());
                        assets.add(data);

                        String type=""+data.getAssetTypeId();

                        if(type.equals("4"))//Smartphone User
                        {
                            storageObject.setIsSimuUser(true);
                        }
                        else {
                            storageObject.setIsBodaUser(true);
                        }

//                        Toast.makeText(mContext, "Count"+assets.size()+":"+accounts.size(), Toast.LENGTH_SHORT).show();

                        if((""+data.getAssetTypeId()).equals("4"))//SmartPhone
                        {
                            //Ia s a phone so put it as the default asset
                            storageObject.saveCurrentAssetObjectToPrefs(data);
                        }

                        if(assets.size() == (accounts.size()))
                        {

                            //FInished Adding so replace now
                            listData=assets;
                            //mAdapter= new LoansListAdapter(parentActivity,listData,loadingView);

                            LinearLayoutManager layoutManager = new LinearLayoutManager(parentActivity, LinearLayoutManager.HORIZONTAL, false);
                            listView.setLayoutManager(layoutManager);
                            //listView.setAdapter(mAdapter);

                           // mAdapter.notifyDataSetChanged();
                            storageObject.saveAssetObjectObjectsToPrefs(assets);


                            if(data !=null && storageObject.retrieveCurrentAssetObjectFromPrefs()==null)storageObject.saveCurrentAssetObjectToPrefs(data);

                        }

//                        storageObject.addAssetObjectObjectToPrefs(data);
//                        mAdapter.notifyDataSetChanged();

                    }
                }
            });

        }

            return listData;
    }


    public void goToMakePayment()
    {
//        Intent mIntent = new Intent(parentActivity, MakePaymentActivity.class);
//        mIntent.putExtra(KEY_START_FROM, "user_accounts");
//        GeneralCommands.goToPage(parentActivity, mIntent,false);
    }


    public void openDataUsageSettings(Context context) {
        Intent intent = new Intent();

        // Try to open Data Usage settings
        intent.setAction(Settings.ACTION_DATA_USAGE_SETTINGS);

        if (canHandleIntent(context, intent)) {
            try {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Add this flag
                context.startActivity(intent);
            } catch (Exception e) {
                Log.e("Error", "Failed to open Data Usage settings: " + e.getMessage());
                openAlternativeSettings(context);
            }
        } else {
            Log.e("Error", "No Activity found to handle Data Usage settings intent");
            openAlternativeSettings(context);
        }
    }

    // Check if an intent can be handled by the system
    private boolean canHandleIntent(Context context, Intent intent) {
        return intent.resolveActivity(context.getPackageManager()) != null;
    }

    // Open alternative settings if Data Usage is unavailable
    private void openAlternativeSettings(Context context) {
        // First, try to open Network Operator settings as a fallback
        Intent intent = new Intent(Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Add this flag

        if (canHandleIntent(context, intent)) {
            try {
                context.startActivity(intent);
                return;
            } catch (Exception e) {
                Log.e("Error", "Failed to open Network Operator settings: " + e.getMessage());
            }
        }

        // Next, try to open Wireless Settings
        intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Add this flag

        if (canHandleIntent(context, intent)) {
            try {
                context.startActivity(intent);
                return;
            } catch (Exception e) {
                Log.e("Error", "Failed to open Wireless settings: " + e.getMessage());
            }
        }

        // Fallback to General settings if nothing else works
        intent = new Intent(Settings.ACTION_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Add this flag

        if (canHandleIntent(context, intent)) {
            try {
                context.startActivity(intent);
            } catch (Exception e) {
                Log.e("Error", "Failed to open General settings: " + e.getMessage());
            }
        } else {
            Log.e("Error", "No Activity found to handle General settings intent");
        }
    }




       
}
