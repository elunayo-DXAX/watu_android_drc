package com.watutz.app.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.watutz.app.FAQsActivity;
import com.watutz.app.GeneralCommands;
import com.watutz.app.MainActivity;
import com.watutz.app.NotificationsActivity;
import com.watutz.app.R;
import com.watutz.app.adapters.NotificationsListAdapter;
import com.watutz.app.constant.IWhereMyLocationConstants;
import com.watutz.app.font.FirmaTextView;
import com.watutz.app.objects.NotificationObject;
import com.watutz.app.prefs.StoreAndGetFromPrefs;
import com.watutz.app.view.EndlessListView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class FragmentNotifications extends Fragment implements IWhereMyLocationConstants {

    private View parentView;

    private static StoreAndGetFromPrefs storageObject;
    public static MainActivity parentActivity;
    static Context mContext;

    RelativeLayout loadingView;

    static NotificationsListAdapter mAdapterNotifications;
    static EndlessListView listViewNotifications;
    private boolean isLoading = false;

    RelativeLayout btnBack;
    RelativeLayout btnFilter;
    RelativeLayout btnMarkAllAsRead;
    static FirmaTextView notificationCount;

    static ArrayList<NotificationObject> listData;
    public static int offset = 0;
    private Set<Integer> loadedOffsets = new HashSet<>();

    private int previousOffset = 0;
    // Handler to delay the action
    private Handler handler = new Handler();
    private SwipeRefreshLayout swipeRefreshLayout;
    //Welcome Notifiction
    static boolean alreadyAdded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        parentView = inflater.inflate(R.layout.fragment_notifications, container, false);
        parentActivity = (MainActivity) getActivity();
        mContext= parentActivity.getApplicationContext();
        storageObject = new StoreAndGetFromPrefs(mContext);
        loadingView= (RelativeLayout) parentView.findViewById(R.id.loadingItems);

        notificationCount=(FirmaTextView)parentView.findViewById(R.id.notificationCount);



        btnBack= (RelativeLayout) parentView.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                parentActivity.goBack();
            }
        });


        btnFilter= (RelativeLayout) parentView.findViewById(R.id.btnFilter);
        btnFilter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                GeneralCommands.logEvent(parentActivity,"click_filter_notifications","click_filter_notifications");
                openDialogFilter();
            }
        });

        btnMarkAllAsRead= (RelativeLayout) parentView.findViewById(R.id.btnMarkAllAsRead);
        btnMarkAllAsRead.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                GeneralCommands.logEvent(parentActivity,"click_mark_all_as_read_notification","click_mark_all_as_read_notification");

                resetNotifications();
            }
        });



        listViewNotifications= parentView.findViewById(R.id.listViewNotifications);
        listViewNotifications.setLoadingView(R.layout.list_item_loading);

        listViewNotifications.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                GeneralCommands.logEvent(parentActivity,"click_read_notification","click_read_notification");


                if(mAdapterNotifications!=null && mAdapterNotifications.getCount()>=position)
                {
                    NotificationObject item =(NotificationObject) mAdapterNotifications.getItem(position);
                    if(item!=null) {
                        item.setRead(true);
                        mAdapterNotifications.notifyDataSetChanged();
                        storageObject.saveNotificationObjectObjectsToPrefs(listData);
                        resetUnreadNotificationsCount();
                        if(item.getType().equals("default_welcome") || item.getType().equals("welcome"))
                        {
                            openDialogReadWelcomeNotification(item, position);

                        }
                        else
                        {
                            openDialogReadNotification(item, position);

                        }
                    }

                }


            }
        });



        listViewNotifications.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if (mAdapterNotifications == null)
                    return ;

                if (mAdapterNotifications.getCount() == 0)
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

                    listData=getListDataNotifications("all");
                    mAdapterNotifications.notifyDataSetChanged();
                    isLoading=false;

                }
            }
        });


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
                if (listViewNotifications != null) {
                    return listViewNotifications.canScrollVertically(-1);
                }
                return false;
            }
        });



//        addDummyNotifications();


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


    public void setData()
    {
//        storageObject.addNotificationObjectObjectToPrefs(data.get(i));
        setAdapter("all");

        swipeRefreshLayout.setRefreshing(false);
    }

    public static void setAdapter(String filter)
    {
        mAdapterNotifications= new NotificationsListAdapter(parentActivity,getListDataNotifications(filter));
        listViewNotifications.setAdapter(mAdapterNotifications);
        resetUnreadNotificationsCount();

    }


    public static ArrayList<NotificationObject> getListDataNotifications(String filter)
    {
        listData =storageObject.retrieveNotificationObjectObjectsFromPrefs();

        // Filter based on the specified filter parameter
        switch (filter.toLowerCase()) {
            case "all":
                // No filtering needed; show all notifications
                break;
            case "unread":
                listData = filterByUnread(listData);
                break;
            case "general":
                listData = filterByType(listData, "general");
                break;
            case "payment":
                listData = filterByType(listData, "payment");
                break;
            case "reminders":
                listData = filterByType(listData, "reminder");
                break;
            default:
                // If no matching filter, return an empty list or original list
                listData = new ArrayList<>();
                break;
        }

        //Reset Default Notification
        Iterator<NotificationObject> it = listData.iterator();
        while (it.hasNext()) {
            NotificationObject n = it.next();
            if ("default_welcome".equals(n.getType())) {
                it.remove();    // safe removal via the Iterator
            }
        }

        // Sort the list by date and time in descending order
        Collections.sort(listData, new Comparator<NotificationObject>() {
            @Override
            public int compare(NotificationObject n1, NotificationObject n2) {
                long dateTime1 = n1.getTimestamp();
                long dateTime2 = n2.getTimestamp();
                return Long.compare(dateTime2, dateTime1);

            }
        });

        addWelcomeNotification();

        return listData;
    }



    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onStart() {
        super.onStart();
    }
    public void goToPaymentPage()
    {
        Intent mIntent = new Intent(parentActivity, MainActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
        try {MainActivity.viewPager.setCurrentItem(2); }catch(Exception e){e.printStackTrace();}

        parentActivity.goBack();

//        GeneralCommands.goToPage(parentActivity, mIntent,false);



    }


    public void goToTransactionsPage()
    {
        Intent mIntent = new Intent(parentActivity, MainActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
        try {MainActivity.viewPager.setCurrentItem(1); }catch(Exception e){e.printStackTrace();}

        parentActivity.goBack();



    }


    public void resetNotifications()
    {

        for(NotificationObject object: listData)
        {
            object.setRead(true);
        }

        storageObject.saveNotificationObjectObjectsToPrefs(listData);
        mAdapterNotifications.notifyDataSetChanged();
        notificationCount.setText(""+mAdapterNotifications.getCount());
        notificationCount.setVisibility(View.GONE);

    }

    public static void resetUnreadNotificationsCount()
    {

        int count =0;
        for(NotificationObject object: listData)
        {
            if(!object.isRead())
            {
                count++;
            }
        }


        notificationCount.setText(""+count);
//        if(count>0)
//        {
//            notificationCount.setVisibility(View.VISIBLE);
//
//        }
//        else {
//            notificationCount.setVisibility(View.GONE);
//
//        }

    }



    public void openDialogFilter() {
        // Define filter options
        String[] filterOptions = {
                getString(R.string.filter_all),
                getString(R.string.filter_unread),
                getString(R.string.filter_general_notifications),
                getString(R.string.filter_payment_notifications),
                getString(R.string.filter_reminders)
        };

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(parentActivity);
        builder.setTitle(mContext.getResources().getString(R.string.filternotifications));

        builder.setItems(filterOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        // Code to show All notifications
                        setAdapter("all");
                        break;
                    case 1:
                        // Code to show Unread notifications
                        setAdapter("unread");
                        break;
                    case 2:
                        // Code to show General Notifications
                        setAdapter("general");
                        break;
                    case 3:
                        // Code to show Payment Notifications
                        setAdapter("payment");
                        break;
                    case 4:
                        // Code to show Reminders
                        setAdapter("reminders");
                        break;
                }
            }
        });

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    // Helper method to filter unread notifications
    private static ArrayList<NotificationObject> filterByUnread(ArrayList<NotificationObject> list) {
        ArrayList<NotificationObject> filteredList = new ArrayList<>();
        for (NotificationObject notification : list) {
            if (!notification.isRead()) {
                filteredList.add(notification);
            }
        }
        return filteredList;
    }

    // Helper method to filter notifications by type
    private static ArrayList<NotificationObject> filterByType(ArrayList<NotificationObject> list, String type) {
        ArrayList<NotificationObject> filteredList = new ArrayList<>();
        for (NotificationObject notification : list) {
            if (notification.getType() != null && notification.getType().equalsIgnoreCase(type)) {
                filteredList.add(notification);
            }
        }
        return filteredList;
    }

    public void openDialogReadNotification(NotificationObject notification, int notificationPosition)
    {
        View contentView = parentActivity.getLayoutInflater().inflate(R.layout.dialog_content_read_notification, null);


        final DialogPlus dialogPlus = new DialogPlus.Builder(parentActivity)
                .setContentHolder(new ViewHolder(contentView))
                .setGravity(DialogPlus.Gravity.TOP)
                .setBackgroundColorResourceId(R.color.transparent)
                .setCancelable(true)
                .create();


        //Content View
        View content = dialogPlus.getHolderView();

        FirmaTextView type =content.findViewById(R.id.type);
        ImageView icon =contentView.findViewById(R.id.icon);

        FirmaTextView date =content.findViewById(R.id.date);
        FirmaTextView time =content.findViewById(R.id.time);
        FirmaTextView message =content.findViewById(R.id.message);

        FirmaTextView buttonText =content.findViewById(R.id.buttonText);
        RelativeLayout btnNotification =contentView.findViewById(R.id.btnNotification);
        RelativeLayout btnDeleteNotification =contentView.findViewById(R.id.btnDeleteNotification);

        btnDeleteNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GeneralCommands.logEvent(parentActivity,"click_clear_notification","click_clear_notification");


                listData.remove(notificationPosition);
                storageObject.saveNotificationObjectObjectsToPrefs(listData);
                mAdapterNotifications.notifyDataSetChanged();
                setAdapter("all");
                dialogPlus.dismiss();


            }
        });

        String typeTranslated=mContext.getResources().getString(R.string.general);


        if(notification.getType().toLowerCase().contains("reminder"))
        {
            typeTranslated=mContext.getResources().getString(R.string.reminder);
        }

        else if(notification.getType().toLowerCase().contains("payment"))
        {
            typeTranslated=mContext.getResources().getString(R.string.payment);
        }

        type.setText(GeneralCommands.capitalizeWords(typeTranslated));
        message.setText(notification.getBody());
        date.setText(notification.getDate());
        time.setText(notification.getTime());

        if(notification.getType().equals("payment"))
        {
            icon.setImageResource(R.drawable.ics_notification_payment);
            buttonText.setText(mContext.getResources().getString(R.string.viewtransactionsh));
            btnNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    GeneralCommands.logEvent(parentActivity,"click_notification_done_go_to_transactions","click_notification_done_go_to_transactions");

                    goToTransactionsPage();
                    dialogPlus.dismiss();
                }
            });
        }
        else if(notification.getType().equals("reminder"))
        {

            icon.setImageResource(R.drawable.ics_notification_reminder);
            buttonText.setText(mContext.getResources().getString(R.string.paynow));

            btnNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    GeneralCommands.logEvent(parentActivity,"click_notification_done_go_to_payments","click_notification_done_go_to_payments");

                    goToPaymentPage();
                    dialogPlus.dismiss();
                }
            });
        }
        else
        {


            buttonText.setText(mContext.getResources().getString(R.string.done));
            icon.setImageResource(R.drawable.ics_notification_general);
            btnNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GeneralCommands.logEvent(parentActivity,"click_notification_done","click_notification_done");

                    dialogPlus.dismiss();
                }
            });
        }





        dialogPlus.show();
    }

    public void addDummyNotifications()
    {
        ArrayList<NotificationObject> notifications = new ArrayList<>();

        // Adding notifications with provided message samples and randomizing order
        // Create a list for notifications

         // Add notifications with current system time (System.currentTimeMillis())
        notifications.add(new NotificationObject("1", "Reminder", "Your device is about to be locked.",
                "reminder", "reminderData1", "2024-11-03", "15:20",
                "ACT_NOW", System.currentTimeMillis()));
        notifications.add(new NotificationObject("2", "Payment", "Dear customer, your phone will be locked soon. Please, make a payment to continue using it.",
                "payment", "paymentData2", "2024-10-28", "09:45",
                "PAY_NOW", System.currentTimeMillis()));
        notifications.add(new NotificationObject("3", "General", "Dear customer, pay weekly amounts so you can enjoy using your phone device longer.",
                "general", "generalData3", "2024-10-25", "10:00",
                "READ_MORE", System.currentTimeMillis()));
        notifications.add(new NotificationObject("4", "Payment", "Immediate payment required to avoid service interruption.",
                "payment", "paymentData4", "2024-10-29", "13:20",
                "SETTLE_NOW", System.currentTimeMillis()));
        notifications.add(new NotificationObject("5", "General", "Keep your account active by topping up today.",
                "general", "generalData5", "2024-10-26", "11:30",
                "TOP_UP", System.currentTimeMillis()));
        notifications.add(new NotificationObject("6", "General", "Explore the latest features added to your account.",
                "general", "generalData6", "2024-10-27", "12:15",
                "DISCOVER", System.currentTimeMillis()));
        notifications.add(new NotificationObject("7", "Payment", "Your account is overdue. Please settle your bill to continue.",
                "payment", "paymentData7", "2024-10-30", "14:05",
                "PAY_NOW", System.currentTimeMillis()));
        notifications.add(new NotificationObject("8", "Reminder", "Your subscription is due to renew soon. Please review details.",
                "reminder", "reminderData8", "2024-11-02", "11:45",
                "CHECK_DETAILS", System.currentTimeMillis()));
        notifications.add(new NotificationObject("9", "General", "New promotional rates are available for a limited time!",
                "general", "generalData9", "2024-10-28", "08:30",
                "VIEW_PROMO", System.currentTimeMillis()));
        notifications.add(new NotificationObject("10", "Reminder", "Your device is about to be locked. Please take action.",
                "reminder", "reminderData10", "2024-11-01", "10:15",
                "ACT_NOW", System.currentTimeMillis()));
        notifications.add(new NotificationObject("11", "Payment", "Service will be temporarily suspended until payment is made.",
                "payment", "paymentData11", "2024-10-31", "16:30",
                "RESUME_SERVICE", System.currentTimeMillis()));
        notifications.add(new NotificationObject("12", "Reminder", "Remember to review your security settings regularly.",
                "reminder", "reminderData12", "2024-11-04", "14:00",
                "REVIEW_SETTINGS", System.currentTimeMillis()));
        notifications.add(new NotificationObject("13", "General", "Did you know? Regular usage helps improve your experience!",
                "general", "generalData13", "2024-10-29", "09:00",
                "LEARN_MORE", System.currentTimeMillis()));
        notifications.add(new NotificationObject("14", "Payment", "Thank you for your payment. Your account is now active.",
                "payment", "paymentData14", "2024-11-01", "11:45",
                "CONFIRM", System.currentTimeMillis()));
        notifications.add(new NotificationObject("15", "Reminder", "A new software update is available. Update now to stay secure.",
                "reminder", "reminderData15", "2024-11-05", "13:00",
                "UPDATE_NOW", System.currentTimeMillis()));

        storageObject.saveNotificationObjectObjectsToPrefs(notifications);
    }

    public void openDialogReadWelcomeNotification(NotificationObject notification, int notificationPosition)
    {

            View contentView = parentActivity.getLayoutInflater().inflate(R.layout.dialog_content_read_notification_welcome, null);
            final DialogPlus dialogPlus = new DialogPlus.Builder(parentActivity)
                    .setContentHolder(new ViewHolder(contentView))
                    .setGravity(DialogPlus.Gravity.CENTER)
                    .setBackgroundColorResourceId(R.color.transparent)
                    .setCancelable(true)
                    .create();

            RelativeLayout helpCenterButton = contentView.findViewById(R.id.helpCenterButton);
            helpCenterButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Opens Resource Center activity
                    try {MainActivity.viewPager.setCurrentItem(4);}catch(Exception e ){e.printStackTrace();}
                    dialogPlus.dismiss();

//                    deleteNotification(notification, notificationPosition);
//                    storageObject.setDefaultWelcomeNotificationAdded(false);
                }
            });

            FirmaTextView welcomeBody = contentView.findViewById(R.id.welcomeBody);
            if (welcomeBody != null) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    welcomeBody.setText(android.text.Html.fromHtml(parentActivity.getString(R.string.did_you_know), android.text.Html.FROM_HTML_MODE_LEGACY));
                } else {
                    welcomeBody.setText(android.text.Html.fromHtml(parentActivity.getString(R.string.did_you_know)));
                }
            }


           ImageView welcomeImage = contentView.findViewById(R.id.welcomeImage);
            if(storageObject.retrieveLanguageFromPrefs().equals("sw"))
            {
                welcomeImage.setImageResource(R.drawable.welcome_sw);
            }else {
                welcomeImage.setImageResource(R.drawable.welcome_en);
            }




            dialogPlus.show();



    }

    public void deleteNotification(NotificationObject notification, int notificationPosition)
    {
        GeneralCommands.logEvent(parentActivity,"click_clear_notification","click_clear_notification");

        listData.remove(notificationPosition);
        storageObject.saveNotificationObjectObjectsToPrefs(listData);
        mAdapterNotifications.notifyDataSetChanged();
        setAdapter("all");
    }


    public static void addWelcomeNotification() {



            String id = "default_welcome";
            String title = mContext.getResources().getString(R.string.welcomeh);
            String body = mContext.getResources().getString(R.string.didyouknowthewatuapp)+ " " + mContext.getResources().getString(R.string.watuwelcomebody);
            String type = "default_welcome";
            String data = "";
            String action = "";
            java.util.Date now = new java.util.Date();
            String date = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(now);
            String time = new java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(now);
            long timestamp = now.getTime();
            NotificationObject welcomeNotification = new NotificationObject(
                    id, title, body, type, data, date, time, action, timestamp
            );
            welcomeNotification.setRead(false);
            if(listData!=null)listData.add(welcomeNotification);

    }
}
