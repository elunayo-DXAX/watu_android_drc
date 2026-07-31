package com.watudrc.app;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;
import com.watudrc.app.util.AppSignatureHelper;
import com.watudrc.app.util.DataCollector;

import java.util.ArrayList;

import okhttp3.MediaType;

public class SplashScreenActivity extends BaseActivity implements IWhereMyLocationConstants {


    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static StoreAndGetFromPrefs storageObject;
    private static final String HOST = "https://dev.fintelum.com";
    private static final String PORT = "8345";
    int OVERLAY_PERMISSION_REQUEST_CODE = 3344;
    ImageView logo;
    private final Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		getWindow().requestFeature(Window.FEATURE_NO_TITLE); //Removing ActionBar
        setContentView(R.layout.activity_splash_screen);
        final Handler handler = new Handler();

        allowLock();

        storageObject = new StoreAndGetFromPrefs(this);

        logo = findViewById(R.id.logo);

        //Data Collector
        try {
            // you could kick off periodic uploads here, e.g.:
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                new DataCollector(this, "App_Launch").collectAndSend();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                proceed();
            }
        }, 2000);

//		getAppSignatures();
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
//				!Settings.canDrawOverlays(this)) {
//			Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
//					Uri.parse("package:" + getPackageName()));
//			startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE);
//		}



    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void proceed() {
        if (storageObject.checkIfUserIsRegistered() && !storageObject.retrieveUserPasswordFromPrefs().equals("")) {

//				  Intent mIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
            Intent mIntent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            mIntent.putExtra(KEY_START_FROM, "splash");
            GeneralCommands.goToPage(this, mIntent, true);

        } else {
            Intent mIntent = new Intent(SplashScreenActivity.this, LanguageActivity.class);
//            Intent mIntent = new Intent(SplashScreenActivity.this, PreviewActivity.class);
            mIntent.putExtra(KEY_START_FROM, "splash");
            GeneralCommands.goToPage(this, mIntent, true);

        }

    }

    public ArrayList<String> getAppSignatures() {
        ArrayList<String> appCodes = new ArrayList<>();

        try {
            // Get all package signatures for the current package
            String packageName = getPackageName();
            PackageManager packageManager = getPackageManager();
            Signature[] signatures = packageManager.getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES).signatures;

            // For each signature create a compatible hash
            for (Signature signature : signatures) {
                AppSignatureHelper help = new AppSignatureHelper(this);

                String hash = AppSignatureHelper.hash(packageName, signature.toCharsString());
                if (hash != null) {
                    Log.e("ZeSMSHash", "ZeSMSHash" + hash);

                    appCodes.add(String.format("%s", hash));
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("ZeSMSHash", "Unable to find package to obtain hash.", e);
        }
        return appCodes;
    }

}
