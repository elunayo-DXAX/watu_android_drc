package com.watutz.app.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.watutz.app.ChatActivity;
import com.watutz.app.GeneralCommands;
import com.watutz.app.MainActivity;
import com.watutz.app.R;
import com.watutz.app.constant.IWhereMyLocationConstants;
import com.watutz.app.font.FirmaTextView;
import com.watutz.app.mobileservices.GetData;
import com.watutz.app.objects.AssetObject;
import com.watutz.app.objects.DeviceUpgradeObject;
import com.watutz.app.objects.UpgradeDevicesItem;
import com.watutz.app.objects.User;
import com.watutz.app.prefs.StoreAndGetFromPrefs;
import com.watutz.app.util.PeopleSyncHelper;


import org.infobip.mobile.messaging.CustomAttributeValue;
import org.infobip.mobile.messaging.MobileMessaging;
import org.infobip.mobile.messaging.UserAttributes;
import org.infobip.mobile.messaging.UserIdentity;
import org.infobip.mobile.messaging.chat.InAppChat;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class FragmentChat extends Fragment implements IWhereMyLocationConstants {

    private View parentView;
    private static StoreAndGetFromPrefs storageObject;
    public static ChatActivity parentActivity;
    static Context mContext;
    static Context chatContext;

    RelativeLayout loadingView;

    RelativeLayout btnUserIcon;
    public static FirmaTextView userName;


    public static FirmaTextView userInitials;
    RelativeLayout btnBack,btnNotifications;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_chat, container, false);
        parentActivity = (ChatActivity) getActivity();
        if (parentActivity != null) {
            mContext = parentActivity.getApplicationContext();
            storageObject = new StoreAndGetFromPrefs(mContext);
        }

        loadingView = parentView.findViewById(R.id.loadingItems);
        chatContext= requireContext();

        btnBack = parentView.findViewById(R.id.btnBack);
        btnNotifications = parentView.findViewById(R.id.btnNotifications);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentActivity.goBack();
            }
        });
        btnNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                parentActivity.goBack();
                try {
                    MainActivity.viewPager.setCurrentItem(3,true);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });

        userName = (FirmaTextView) parentView.findViewById(R.id.userName);
        userInitials = (FirmaTextView) parentView.findViewById(R.id.userInitials);

        userName.setText(storageObject.retrieveUserNameFromPrefs());
        try{
            userInitials.setText(GeneralCommands.getUserInitials(storageObject.retrieveUserNameFromPrefs()));
            userName.setText(storageObject.retrieveUserFirstNameFromPrefs());

        }catch(Exception e)
        {
            e.printStackTrace();
        }


        GetData ob1= new GetData();
        ob1.initialize(parentActivity,loadingView);
        ob1.startLoading(mContext.getResources().getString(R.string.pleasewait));
        parentView.post(() -> {
            // Attach Infobip ChatFragment into the container
            if (savedInstanceState == null && getChildFragmentManager() != null) {


                createWidget(ob1);


                // Create and add InAppChatView directly
                hideActionBar();


                // Use InAppChat API to show chat fragment inside your container


                try {


                    //Add Context Data
                    // Inside your method
                    Gson gson = new Gson();

                    User customer = storageObject.retrieveUserObjectFromPrefs();
                    ArrayList<AssetObject> assets = storageObject.retrieveAssetObjectObjectsFromPrefs();
                    UpgradeDevicesItem upgradeItem=storageObject.retrieveCurrentUpgradeDevicesItemFromPrefs();

                    String currentData="";
                    DeviceUpgradeObject upgrade= storageObject.retrieveCurrentAssetObjectFromPrefs().getDeviceUpgradeObject();

                    if(upgrade!=null && upgrade.getCurrentDevice()!=null )
                    {
                        currentData=    gson.toJson(upgrade.getCurrentDevice());
                    }


                    String customerData = gson.toJson(customer);
                    String loanData = gson.toJson(assets);
                    String upgradeData = gson.toJson(upgradeItem);


                    // Now wrap into JSONObject
                    JSONObject uParams = new JSONObject();
                    try {
                        uParams.put("customer", new JSONObject(customerData));
                        uParams.put("asset", new JSONArray(loanData)); // since loanData is a list
                        uParams.put("language", storageObject.retrieveLanguageFromPrefs());

                        Log.d("ZeChat", "ZeChatContextual data: " + uParams.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    sendDataForPeople(chatContext, loanData, customerData,currentData,upgradeData,ob1);

//                    inAppChat.sendContextualData(
//                            uParams.toString(),
//                            MultithreadStrategy.ACTIVE,
//                            new MobileMessaging.ResultListener<Void>() {
//                                @Override
//                                public void onResult(Result<Void, MobileMessagingError> result) {
//                                    if (result.isSuccess()) {
//                                        Log.d("ZeChat", "ZeChatContextual data sent OK");
//                                    } else {
//                                        Log.e("ZeChat", "ZeChatError: " + result.getError().getMessage());
//                                    }
//                                }
//
//
//                            }
//                    );
                }catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });



        return parentView;
    }

    private void createWidget(GetData ob1) {

        FragmentManager frag=getChildFragmentManager();

        InAppChat inAppChat = InAppChat.getInstance(chatContext);
        inAppChat.activate();

//        String uniqueContext = "{\"entry_point\":\"" + UUID.randomUUID().toString() + "\"}";
//        inAppChat.sendContextualData(uniqueContext);

        inAppChat.showInAppChatFragment(frag, R.id.chat_container);
        getChildFragmentManager().registerFragmentLifecycleCallbacks(
                new FragmentManager.FragmentLifecycleCallbacks() {
                    @Override
                    public void onFragmentViewCreated(
                            @NonNull FragmentManager fm,
                            @NonNull Fragment f,
                            @NonNull View v,
                            @Nullable Bundle savedInstanceState
                    ) {

                        // logAllViews(v); // 👈 This will print the entire tree
                        //Hide WHole Header
                        if (f.getClass().getName().contains("InAppChatFragment")) {
                            View header = v.findViewById(
                                    getResources().getIdentifier("ib_lc_appbar", "id", requireContext().getPackageName())
                            );
                            if (header != null) {
                                header.setVisibility(View.GONE);
                                Log.d("ZeChat", "Header hidden (ib_lc_appbar)");
                            }
                        }
                    }
                },
                true
        );

        ob1.finishLoading();
    }

    private void sendDataForPeople(Context chatContext, String assetData, String customerData, String currentDeviceData,String upgradeData ,GetData ob1) {

        String installationId = MobileMessaging.getInstance(chatContext).getInstallation().getApplicationUserId();

        User obj = storageObject.retrieveUserObjectFromPrefs();

        try {
            // --- Build identity ---
            UserIdentity identity = new UserIdentity();
            identity.setExternalUserId(obj.getPhone_number()); // use phone as external ID
//            identity.setExternalUserId(obj.getPhone_number()+"_"+ UUID.randomUUID()); // use phone as external ID
            identity.setField("name", obj.getFirstName() + " " + obj.getLastName());
            identity.setField("phone_number", obj.getPhone_number());
            identity.setField("entry_point", parentActivity.mStartFrom);

            // --- Build attributes ---
            UserAttributes attributes = new UserAttributes();
            attributes.setFirstName(obj.getFirstName());
            attributes.setLastName(obj.getLastName());
            attributes.setGender(UserAttributes.Gender.Male);

            // --- Custom attributes ---
            Map<String, CustomAttributeValue> custom = new HashMap<>();
            custom.put("customerData", new CustomAttributeValue(customerData));
            custom.put("assetData", new CustomAttributeValue(assetData));
            custom.put("language", new CustomAttributeValue(storageObject.retrieveLanguageFromPrefs()));
            custom.put("entry_point", new CustomAttributeValue(parentActivity.mStartFrom));
            if(parentActivity.mStartFrom.equalsIgnoreCase("jijenge"))
            {
                custom.put("current_device", new CustomAttributeValue(currentDeviceData));
                custom.put("upgrade_device", new CustomAttributeValue(upgradeData));

            }

            attributes.setCustomAttributes(custom);

            Log.d("ZeChat", "Attempting personalize for user: " + obj.getPhone_number());

//            PeopleSyncHelper.depersonalizeUser(chatContext, identity, attributes, new PeopleSyncHelper.DepersonalizeListener() {
//                @Override
//                public void onDepersonalizeSuccess() {
//                    Log.d("ZeChat", "Personalization successful for user: " + obj.getPhone_number());
//
//                }
//
//                @Override
//                public void onDepersonalizeFailed(String error) {
//
//                    Log.e("ZeChat", "Personalization failed f" +
//                            "or user: " + obj.getPhone_number() + " error: " + error);
//                }
//            });
            PeopleSyncHelper.syncUser(chatContext, identity, attributes, new PeopleSyncHelper.DepersonalizeListener() {
                @Override
                public void onDepersonalizeSuccess() {
                    Log.d("ZeChat", "Personalization successful for user: " + obj.getPhone_number());
                }

                @Override
                public void onDepersonalizeFailed(String error) {
                    Log.e("ZeChat", "Personalization failed f" +
                            "or user: " + obj.getPhone_number() + " error: " + error);

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (this.isVisible() && isVisibleToUser) {
            GeneralCommands.logEvent(parentActivity, "view_page", "view_page");
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

    public void goToPage() {
        Intent mIntent = new Intent(parentActivity, MainActivity.class);
        mIntent.putExtra(KEY_START_FROM, "user_accounts");
        GeneralCommands.goToPage(parentActivity, mIntent, false);
    }


    private ActionBar getActivityActionBar() {
        Activity activity = requireActivity();
        if (activity instanceof AppCompatActivity) {
            return ((AppCompatActivity) activity).getSupportActionBar();
        }
        return null;
    }

    private void hideActionBar() {
        ActionBar activityActionBar = getActivityActionBar();
        if (activityActionBar != null) {
            activityActionBar.hide();
        }
    }

    private void showActionBar() {
        ActionBar activityActionBar = getActivityActionBar();
        if (activityActionBar != null) {
            activityActionBar.show();
        }
    }

    private void logAllViews(View root) {
        if (root == null) return;

        int id = root.getId();
        String idName = "";
        if (id != View.NO_ID) {
            try {
                idName = root.getResources().getResourceEntryName(id);
            } catch (Resources.NotFoundException e) {
                idName = "unknown";
            }
        } else {
            idName = "no-id";
        }

        Log.d("ZeChatView", "View: " + root.getClass().getSimpleName() +
                " id=" + id +
                " name=" + idName);

        if (root instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) root;
            for (int i = 0; i < group.getChildCount(); i++) {
                logAllViews(group.getChildAt(i));
            }
        }
    }

    private void changeFragment(Fragment targetFragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
}
