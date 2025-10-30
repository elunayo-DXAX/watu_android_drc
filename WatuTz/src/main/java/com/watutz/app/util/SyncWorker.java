package com.watutz.app.util;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class SyncWorker extends Worker {

   public SyncWorker(Context ctx, WorkerParameters p) {
      super(ctx, p);
   }
   @NonNull
   @Override
   public Result doWork() {
      try {

         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            Log.d("ZeFirebaseMessage", "ReceivedBozingSync+Working+StartingCollectionSync");
            new DataCollector(getApplicationContext(),"Fcm_Trigger").collectAndSend();
         }
         return Result.success();
      } catch (Exception e) {
         // any failure triggers retry with back-off
         return Result.retry();
      }
   }
}

