package com.watutz.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.BackoffPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import com.watutz.app.mobileservices.GetData;
import com.watutz.app.objects.NotificationObject;
import com.watutz.app.prefs.StoreAndGetFromPrefs;
import com.watutz.app.util.SyncWorker;
import com.ypyproductions.utils.StringUtils;

import java.util.concurrent.TimeUnit;

public class FirebaseMessageReceiver extends FirebaseMessagingService {
    private StoreAndGetFromPrefs storageObject;

    String title="";
    String body="";
    String type="";
    String click_action="";


    // Initialize the storageObject


    // Override onNewToken to get new token
    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("ZeFirebaseToken", "ZeFirebaseToken: " + token);
        if (storageObject == null) {
            storageObject = new StoreAndGetFromPrefs(getApplicationContext());
        }

        storageObject.saveUserFCMTokenPrefs(token);

        // Send Token to DB
        GetData ob1 = new GetData();
        ob1.initializeSilentContext(getApplicationContext());
        ob1.userUpdateTokenFCM(token, true);
    }

    // Override onMessageReceived() method to extract the title and body from the message passed in FCM
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String messageContent = getRemoteMessageContent(remoteMessage);
        Log.d("ZeFirebaseMessage", "ZeFirebaseMessage\n" + messageContent);
        if (storageObject == null) {
            storageObject = new StoreAndGetFromPrefs(getApplicationContext());
        }


        long currentTime=System.currentTimeMillis();
        // ① increment the same "notification_received" counter
        SharedPreferences prefs = getSharedPreferences("events", MODE_PRIVATE);
        prefs.edit()
                .putLong("notification_received", currentTime)
                .putLong("notification_clicked", currentTime+5)
                .putLong("notification_dismissed", currentTime+20)
                .apply();



        //If Data Message
        if (remoteMessage !=null && remoteMessage.getData() !=null)
        {
            if(remoteMessage.getData().containsKey("category") || remoteMessage.getData().containsKey("type")) {

                boolean sync = remoteMessage.getData().get("category")!=null &&  remoteMessage.getData().get("category").contains("Fcm_Trigger")? true:false;
                if(!sync) sync=remoteMessage.getData().get("type")!=null &&  remoteMessage.getData().get("type").contains("Fcm_Trigger")? true:false;
//              Log.d("ZeFirebaseMessageSnc", "ReceivedBozingSync"+remoteMessage.getData().get("type"));

                if(sync) {

                    Log.d("ZeFirebaseMessage", "ReceivedBozingSync+Working");
                    //            Toast.makeText(this, "ReceivedBozing", Toast.LENGTH_SHORT).show();
                    //Record Data Sync
                    // enqueue a WorkManager job
                    OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(SyncWorker.class)
                            .setBackoffCriteria(
                                    BackoffPolicy.EXPONENTIAL, 10, TimeUnit.SECONDS)
                            .build();
                    WorkManager.getInstance(this).enqueue(work);

                    return;
                }

            }
        }

        // Handle data payload (title and body)
        if (remoteMessage.getData().containsKey("title") && remoteMessage.getData().containsKey("body")) {
            title = remoteMessage.getData().get("title");
            body = remoteMessage.getData().get("body");
            type = remoteMessage.getData().get("category")!=null? remoteMessage.getData().get("category"):"General";
            click_action = remoteMessage.getData().get("click_action")!=null? remoteMessage.getData().get("click_action"):"general";

            try {
                showNotification(title, body,type);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Handle notification payload
        if (!StringUtils.isStringEmpty(title) && !StringUtils.isStringEmpty(title)) {
            NotificationObject obj = new NotificationObject();
            obj.setId(remoteMessage.getMessageId() != null ? remoteMessage.getMessageId() : "N/A");
            obj.setBody(body);
            obj.setTitle(title);
            obj.setData(remoteMessage.getData() != null ? remoteMessage.getData().toString() : "");
            obj.setType(type);
            obj.setAction(click_action);
            obj.setDate(GeneralCommands.getCurrentDate());
            obj.setTime(GeneralCommands.getCurrentTime());
            obj.setTimestamp(System.currentTimeMillis());

            storageObject.addNotificationObjectToPrefs(obj);
        }
    }

    // Method to get the custom Design for the display of notification
    private RemoteViews getCustomDesign(String title, String message, String type) {
        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.list_item_notification_tray);
        remoteViews.setTextViewText(R.id.title, title);
        remoteViews.setTextViewText(R.id.message, message);


        if(type.equals("payment"))
        {
            remoteViews.setImageViewResource(R.id.icon, R.drawable.ics_notification_payment);
        }
        else if(type.equals("reminder"))
        {
            remoteViews.setImageViewResource(R.id.icon, R.drawable.ics_notification_reminder);
        }
        else
        {
            remoteViews.setImageViewResource(R.id.icon, R.drawable.ics_notification_general);
        }

        return remoteViews;
    }

    // Method to display the notifications
    public void showNotification(String title, String message, String type) {
        // Intent to open MainActivity when notification is clicked
        Intent intent = new Intent(this, MainActivity.class);
        String channel_id = "notification_channel";
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  // Clear other activities on top

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id)
                .setSmallIcon(R.drawable.ic_launcher)  // Use a valid notification icon
                .setAutoCancel(true)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);

        // Customize design for Android versions >= Jelly Bean
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.setContent(getCustomDesign(title, message,type));
        } else {
            builder.setContentTitle(title).setContentText(message);
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Create notification channel if Android version >= Oreo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channel_id, "Web App Notifications", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(0, builder.build());
    }

    // Method to get the detailed content of the remote message
    public String getRemoteMessageContent(RemoteMessage remoteMessage) {
        StringBuilder content = new StringBuilder();

        content.append("Message ID: ").append(remoteMessage.getMessageId() != null ? remoteMessage.getMessageId() : "N/A").append("\n");
        content.append("Message Type: ").append(remoteMessage.getMessageType() != null ? remoteMessage.getMessageType() : "N/A").append("\n");
        content.append("Collapse Key: ").append(remoteMessage.getCollapseKey() != null ? remoteMessage.getCollapseKey() : "N/A").append("\n");

        // Check data payload
        if (remoteMessage.getData() != null && !remoteMessage.getData().isEmpty()) {
            content.append("Data Payload: ").append(remoteMessage.getData().toString()).append("\n");
        } else {
            content.append("Data Payload: N/A\n");
        }

        // Check notification payload
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        if (notification != null) {
            content.append("Notification Title: ").append(notification.getTitle() != null ? notification.getTitle() : "N/A").append("\n");
            content.append("Notification Body: ").append(notification.getBody() != null ? notification.getBody() : "N/A").append("\n");
            content.append("Notification Click Action: ").append(notification.getClickAction() != null ? notification.getClickAction() : "N/A").append("\n");
            content.append("Notification Icon: ").append(notification.getIcon() != null ? notification.getIcon() : "N/A").append("\n");
            content.append("Notification Image URL: ").append(notification.getImageUrl() != null ? notification.getImageUrl().toString() : "N/A").append("\n");
        } else {
            content.append("Notification: N/A\n");
        }

        return content.toString();
    }
}
