package com.watutz.app.util;

import android.content.Context;
import android.util.Log;

import org.infobip.mobile.messaging.MobileMessaging;
import org.infobip.mobile.messaging.SuccessPending;
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
    *  - Depersonalizes old installation
    *  - Cleans widget + cache
    *  - Personalizes with new user
    */
   public static void syncUser(Context context,
                               UserIdentity identity,
                               UserAttributes attributes,DepersonalizeListener listener) {
      Log.d(TAG, "➡️ Attempting personalize for user: " + identity.getExternalUserId());

//       personalizeUser(context, identity, attributes);


       // Step 3: Personalize with new identity
       personalizeUser(context, identity, attributes,listener);



   }

   private static void personalizeUser(Context context,
                                       UserIdentity identity,
                                       UserAttributes attributes,DepersonalizeListener listener) {
      MobileMessaging.getInstance(context).personalize(
              identity,
              attributes,
              new MobileMessaging.ResultListener<org.infobip.mobile.messaging.User>() {
                 @Override
                 public void onResult(Result<org.infobip.mobile.messaging.User, MobileMessagingError> persResult) {
                    if (persResult.getError() == null) {
                       Log.i(TAG, "✅ User personalized: " + persResult.getData());
                       resetWidgetCache(context);
                        if (listener != null) {
                            listener.onDepersonalizeSuccess();
                        }

                    } else {
                       Log.e(TAG, "❌ Personalization failed: "
                               + persResult.getError().getMessage());
                        if (listener != null) {
                            listener.onDepersonalizeFailed(persResult.getError().getMessage());
                        }
                    }
                 }
              }
      );
   }

   public static void depersonalizeUser(Context context,
                                 UserIdentity identity,
                                 UserAttributes attributes,DepersonalizeListener listener)
   {
        //Step 1: Depersonalize
      MobileMessaging.getInstance(context).depersonalize(
              new MobileMessaging.ResultListener<SuccessPending>() {
                 @Override
                 public void onResult(Result<SuccessPending, MobileMessagingError> depResult) {
                    if (depResult.getError() == null) {
                       Log.i(TAG, "✅ Depersonalization successful");

                       Log.d(TAG, "➡️ Proceeding with personalization for user: " + identity.getExternalUserId());
                        if (listener != null) {
                            listener.onDepersonalizeSuccess();
                        }

                    } else {
                       Log.e(TAG, "❌ Depersonalization failed: "
                               + depResult.getError().getMessage());
                        if (listener != null) {
                            listener.onDepersonalizeFailed(depResult.getError().getMessage());
                        }
                    }
                 }
              }
      );
   }

   private static void resetWidgetCache(Context context) {
//      Log.w(TAG, "⚠️ Chat widget cache and history reset");

//       InAppChat inAppChat = InAppChat.getInstance(context);
//      inAppChat.activate();
     // Reactivate the widget
//      inAppChat.activate();
   }



}
