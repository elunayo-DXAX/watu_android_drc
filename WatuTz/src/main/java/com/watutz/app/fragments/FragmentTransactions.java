package com.watutz.app.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.watutz.app.DocumentsActivity;
import com.watutz.app.GeneralCommands;
import com.watutz.app.MainActivity;
import com.watutz.app.R;
import com.watutz.app.adapters.TransactionsListAdapter;
import com.watutz.app.constant.IWhereMyLocationConstants;
import com.watutz.app.font.FirmaTextView;
import com.watutz.app.mobileservices.GetData;
import com.watutz.app.objects.ContractObject;
import com.watutz.app.objects.Payment;
import com.watutz.app.prefs.StoreAndGetFromPrefs;
import com.watutz.app.view.EndlessListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FragmentTransactions extends Fragment implements IWhereMyLocationConstants{

    private View parentView;

    private static StoreAndGetFromPrefs storageObject;
    public static MainActivity parentActivity;
    static Context mContext;

    View footer;

    static TransactionsListAdapter mAdapterTransactions;
    static EndlessListView listViewTransactions;
    static RelativeLayout loadingView,noItemsView;
    private boolean isLoading = false;


    static ArrayList<Payment> listData;
    public static int offset = 0;
    private Set<Integer> loadedOffsets = new HashSet<>();

    private int previousOffset = 0;
    // Handler to delay the action
    private Handler handler = new Handler();
    private SwipeRefreshLayout swipeRefreshLayout;
    RelativeLayout btnBack;
    
    RelativeLayout btnDownloadStatement;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        parentView = inflater.inflate(R.layout.fragment_transactions, container, false);
        parentActivity = (MainActivity) getActivity();
        mContext= parentActivity.getApplicationContext();
        storageObject = new StoreAndGetFromPrefs(mContext);

        loadingView= (RelativeLayout)  parentView.findViewById(R.id.loadingItems);
        noItemsView= (RelativeLayout)  parentView.findViewById(R.id.noItemsView);

        btnBack = (RelativeLayout) parentView.findViewById(R.id.btnBack);
        btnDownloadStatement = (RelativeLayout) parentView.findViewById(R.id.btnDownloadStatement);

        btnDownloadStatement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                GeneralCommands.logEvent(parentActivity,"click_download_statement_on_transactions_page","click_download_statement_on_transactions_page");
                goToDocumentsPage();

            }
        });



        offset=0;


        listViewTransactions= parentView.findViewById(R.id.listViewTransactions);
        listViewTransactions.setLoadingView(R.layout.list_item_loading);
        listViewTransactions.setListener(new EndlessListView.EndlessListener() {
            @Override
            public void onObjectReady(String title) {

            }

            @Override
            public void loadMoreData() {

            }
        });



        listViewTransactions.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if (mAdapterTransactions == null)
                    return ;

                if (mAdapterTransactions.getCount() == 0)
                    return ;

                int l = visibleItemCount + firstVisibleItem;
//                Toast.makeText(SharedObjects.context, l+":"+totalItemCount+":"+isLoading, Toast.LENGTH_SHORT).show();
//                Log.d("ZeOffset","ZeOffset"+loadedOffsets.toString());

                if (l >= totalItemCount && !isLoading && !loadedOffsets.contains(offset)) {
                    // It is time to add new data. We call the listener
                    offset=totalItemCount;
                    loadedOffsets.add(offset); // Mark the current offset as loaded
//                    Log.d("ZeOffset","ZeOffset"+loadedOffsets.toString());
//                    Toast.makeText(SharedObjects.context, SharedObjects.context.getResources().getString(R.string.fetchingmoretransactions)+":"+offset, Toast.LENGTH_SHORT).show();

                    isLoading = true;

                    GetData ob1 = new GetData();
                    ob1.initializeSilent(parentActivity);
                    ob1.userGetPayments("","",""+totalItemCount,true);
                    ob1.setPaymentsListener(new GetData.PaymentsListener() {
                        @Override
                        public void onObjectReady(String title) {

                        }

                        @Override
                        public void onDataLoaded(ArrayList<Payment> data) {

                            listData=getListDataTransactions("all");
                            mAdapterTransactions.notifyDataSetChanged();
                            isLoading=false;
                        }
                    });

                }
            }
        });


        new Thread(new Runnable() {
            public void run() {
                // a potentially time consuming task
                setData();
                parentView.post(new Runnable() {
                    public void run() {

                        setAdapter("all");
                        swipeRefreshLayout.setRefreshing(false);
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
                               setData();
                            }
                        });
                    }
                }).start();
            }
        });

        swipeRefreshLayout.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(SwipeRefreshLayout parent, View child) {
                if (listViewTransactions != null) {
                    return listViewTransactions.canScrollVertically(-1);
                }
                return false;
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentActivity.viewPager.setCurrentItem(0);
            }
        });

        return parentView;
    }

    public void setData()
    {

        GetData ob1 = new GetData();
        ob1.initialize(parentActivity,loadingView);
        ob1.userGetPayments("","",""+offset,false);

        ob1.setPaymentsListener(new GetData.PaymentsListener() {
            @Override
            public void onObjectReady(String title) {
            }
            @Override
            public void onDataLoaded(ArrayList<Payment> data) {
                if(data !=null)
                {
                    for(int i=0;i<data.size();i++)
                    {
                        storageObject.addPaymentObjectToPrefs(data.get(i));
                    }

                    setAdapter("all");

                }
            }
        });

        swipeRefreshLayout.setRefreshing(false);
    }

    public static void setAdapter(String filter)
    {
        mAdapterTransactions= new TransactionsListAdapter(parentActivity,getListDataTransactions(filter));
        listViewTransactions.setAdapter(mAdapterTransactions);

        if(mAdapterTransactions.getCount()==0)
        {
//            noItemsView.setVisibility(View.VISIBLE);
        }
        else
        {
//            noItemsView.setVisibility(View.GONE);
        }


    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // Make sure that we are currently visible
        if (this.isVisible()) {
            // If we are becoming invisible, then...
            if (isVisibleToUser) {

                GeneralCommands.logEvent(parentActivity,"view_event","view_transactions");

                //Fetch Data
                new Thread(new Runnable() {
                    public void run() {
                        // a potentially time consuming task
                        parentView.post(new Runnable() {
                            public void run() {
                                setData();
                            }
                        });
                    }
                }).start();
                // TODO stop audio playback
            }
        }
    }



    public void goToTransactionDetailsPage()
    {
//        Intent mIntent = new Intent(parentActivity, TransactionDetailsActivity.class);
//        mIntent.putExtra(KEY_START_FROM, "user_accounts");
//        GeneralCommands.goToPage(parentActivity, mIntent,false);

    }


    public static ArrayList<Payment> getListDataTransactions(String filter)
    {
         listData =storageObject.retrievePaymentObjectsFromPrefs();

         if(listData.size()>0)
         {

             SimpleDateFormat sdfDate = new SimpleDateFormat("dd MMM, yyyy"); // You can customize the format
             SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss"); // You can customize the format

             String time=sdfTime.format(listData.get(0).getPaymentDate());;
             String date= sdfDate.format(listData.get(0).getPaymentDate());;

             String dateTime=date + " " + time;


             storageObject.saveLastPaymentDate(dateTime);


         }



        return listData;
    }

    public static ArrayList<ContractObject> getListDataYesterday(String filter)
    {
        ArrayList<ContractObject> listData= new ArrayList<>();
        return listData;
    }

    private void loadMoreData() {
        // Simulate loading more data (e.g., fetch from API)
        isLoading = true;
//        Toast.makeText(parentActivity, mContext.getResources().getString(R.string.fetchingmoretransactions), Toast.LENGTH_SHORT).show();
//        Log.d("OkHttp","OkHttp-LoadingMore"+offset);

        // Add more items to the list
        GetData ob1 = new GetData();
        ob1.initialize(parentActivity,loadingView);
//        ob1.userGetPayments("","",""+offset,false);

//        mAdapterTransactions.notifyDataSetChanged();
        isLoading = false;

    }


    public static void goToDocumentsPage()
    {
        Intent mIntent = new Intent(parentActivity, DocumentsActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
//        mIntent.putExtra(ITEM_ID, position);
        GeneralCommands.goToPage(parentActivity, mIntent,false);

    }

}
