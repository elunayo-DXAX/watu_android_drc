package com.watudrc.app.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkUtils {

    public interface InternetCheckCallback {
        void onResult(boolean isConnected);
    }

    public static void checkInternetConnection(Context context, InternetCheckCallback callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            boolean isConnected = false;

            if (isNetworkConnected(context)) {
                try {
                    // Fast and reliable Google endpoint
                    HttpURLConnection urlConnection = (HttpURLConnection)
                            (new URL("https://clients3.google.com/generate_204").openConnection());
                    urlConnection.setRequestProperty("User-Agent", "Android");
                    urlConnection.setRequestProperty("Connection", "close");
                    urlConnection.setConnectTimeout(1500);
                    urlConnection.connect();
                    isConnected = (urlConnection.getResponseCode() == 204);
                    urlConnection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            boolean finalIsConnected = isConnected;
            // Switch back to main thread if needed (optional)
            new android.os.Handler(android.os.Looper.getMainLooper()).post(() ->
                    callback.onResult(finalIsConnected)
            );
        });
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        Network network = cm.getActiveNetwork();
        if (network == null) return false;

        NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
    }
}
