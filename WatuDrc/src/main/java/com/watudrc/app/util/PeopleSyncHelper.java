package com.watudrc.app.util;

import android.content.Context;
import android.util.Log;

import org.infobip.mobile.messaging.MobileMessaging;
import org.infobip.mobile.messaging.SuccessPending;
import org.infobip.mobile.messaging.User;
import org.infobip.mobile.messaging.UserAttributes;
import org.infobip.mobile.messaging.UserIdentity;
import org.infobip.mobile.messaging.mobileapi.MobileMessagingError;
import org.infobip.mobile.messaging.mobileapi.Result;

public class PeopleSyncHelper {

    public interface DepersonalizeListener {
        void onDepersonalizeSuccess();
        void onDepersonalizeFailed(String error);
    }

    private static final String TAG = "ZeChatPeople";

    /**
     * Sync user with Infobip People API:
     *  - Personalizes with new user
     *  - If PERSON_DELETED, forces re-registration and retries
     */
    public static void syncUser(Context context,
                                UserIdentity identity,
                                UserAttributes attributes,
                                DepersonalizeListener listener) {
        Log.d(TAG, "➡️ Attempting personalize for user: " + identity.getExternalUserId());
        personalizeUser(context, identity, attributes, listener);
    }

    public static void personalizeUser(Context context,
                                       UserIdentity identity,
                                       UserAttributes attributes,
                                       DepersonalizeListener listener) {
        MobileMessaging.getInstance(context).personalize(
                identity,
                attributes,
                true, // forceDepersonalize: allow re-personalizing an already-personalized installation
                new MobileMessaging.ResultListener<org.infobip.mobile.messaging.User>() {
                    @Override
                    public void onResult(Result<User, MobileMessagingError> persResult) {
                        if (persResult.getError() == null) {
                            Log.i(TAG, "✅ User personalized: " + persResult.getData());
                            resetWidgetCache(context);
                            if (listener != null) listener.onDepersonalizeSuccess();

                        } else {
                            String errorMsg = persResult.getError().getMessage();
                            Log.e(TAG, "❌ Personalization failed: " + errorMsg);

                            if (errorMsg != null && errorMsg.toLowerCase().contains("deleted")) {
                                // cleanup() wipes local SDK data. We then rebuild to trigger fresh
                                // registration, but must wait for REGISTRATION_CREATED broadcast
                                // before retrying personalize — otherwise it races with the new reg ID.
                                Log.w(TAG, "⚠️ Person deleted — running cleanup and waiting for re-registration");
                                try {
                                    MobileMessaging.getInstance(context).cleanup();

                                    // Register a one-shot BroadcastReceiver to wait for the new push reg ID
                                    android.content.BroadcastReceiver registrationReceiver = new android.content.BroadcastReceiver() {
                                        @Override
                                        public void onReceive(android.content.Context ctx, android.content.Intent intent) {
                                            ctx.unregisterReceiver(this);
                                            Log.i(TAG, "✅ New registration received — retrying personalize");
                                            personalizeUser(context, identity, attributes, listener);
                                        }
                                    };

                                    android.content.IntentFilter filter = new android.content.IntentFilter(
                                            org.infobip.mobile.messaging.Event.REGISTRATION_CREATED.getKey()
                                    );
                                    context.registerReceiver(registrationReceiver, filter);

                                    // Now rebuild the SDK — this triggers async re-registration
                                    new MobileMessaging.Builder(((android.app.Application) context.getApplicationContext())).build();
                                    Log.i(TAG, "✅ SDK rebuilt — waiting for REGISTRATION_CREATED...");

                                } catch (Exception e) {
                                    Log.e(TAG, "❌ Cleanup/rebuild failed: " + e.getMessage());
                                    if (listener != null)
                                        listener.onDepersonalizeFailed(e.getMessage());
                                }
                            } else {
                                // Non-deleted error — surface it immediately
                                if (listener != null) listener.onDepersonalizeFailed(errorMsg);
                            }
                        }
                    }
                }
        );
    }

    public static void depersonalizeUser(Context context,
                                         UserIdentity identity,
                                         UserAttributes attributes,
                                         DepersonalizeListener listener) {
        MobileMessaging.getInstance(context).depersonalize(
                new MobileMessaging.ResultListener<SuccessPending>() {
                    @Override
                    public void onResult(Result<SuccessPending, MobileMessagingError> depResult) {
                        if (depResult.getError() == null) {
                            Log.i(TAG, "✅ Depersonalization successful");
                            Log.d(TAG, "➡️ Proceeding with personalization for user: " + identity.getExternalUserId());
                            if (listener != null) listener.onDepersonalizeSuccess();

                        } else {
                            Log.e(TAG, "❌ Depersonalization failed: " + depResult.getError().getMessage());
                            if (listener != null) listener.onDepersonalizeFailed(depResult.getError().getMessage());
                        }
                    }
                }
        );
    }

    private static void resetWidgetCache(Context context) {
        // Reserved for widget cache reset logic
    }
}