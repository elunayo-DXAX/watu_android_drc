package com.watudrc.app.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.ConfigUpdate;
import com.google.firebase.remoteconfig.ConfigUpdateListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ViewListener;
import com.watudrc.app.ActivateAccountActivity;
import com.watudrc.app.GeneralCommands;
import com.watudrc.app.LanguageActivity;
import com.watudrc.app.PreviewActivity;
import com.watudrc.app.R;
import com.watudrc.app.TermsActivity;
import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.font.FirmaTextView;
import com.watudrc.app.objects.AssetObject;
import com.watudrc.app.objects.ServiceDownObject;
import com.watudrc.app.prefs.StoreAndGetFromPrefs;
import com.watudrc.app.util.FirebaseConfig;
import com.watudrc.app.util.ImageUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FragmentPreview extends Fragment implements IWhereMyLocationConstants {

    private View parentView;

    private static StoreAndGetFromPrefs storageObject;
    public static PreviewActivity parentActivity;
    static Context mContext;

    //Preview Images
    CarouselView previewImagesView;
    int[] previewImages = { R.drawable.ic_preview_1, R.drawable.ic_preview_2, R.drawable.ic_preview_3};


    RelativeLayout btnGetStarted, botView, btnTerms;

    CheckBox chkTerms;

    FirebaseConfig remoteConfig;
    FirebaseRemoteConfig mFirebaseConfig;
    String carousel_view = "";

    ViewListener viewListener;
    FirmaTextView termsText, acceptText;

    String support_number = "";
    String whatsapp_number = "";

    String support_page_configs = "";

    boolean upgrade_feature_enabled = false;
    boolean show_whatsapp_button_on_error_page;
    boolean show_call_support_options_on_error_page;
    boolean has_rating_enabled;

    int minimum_rating_days = 30;

    //Service Down
    String service_down = "";
    // constants (put these in your class)
    private static final String PREFS_NAME = "app_prefs";
    private static final String KEY_SERVICE_DOWN_LAST_SHOWN = "service_down_last_shown";
    private static final long FOUR_HOURS_MS = 60L * 60L * 1000L; // 1 hours in ms
//	private static final long FOUR_HOURS_MS = 2L * 60L * 1000L; // 2 minutes (TEST ONLY)

    boolean upgrade_test_two_enabled;

    boolean should_log_data_usage_every_launch;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_preview, container, false);
        parentActivity = (PreviewActivity) getActivity();
        mContext = parentActivity.getApplicationContext();
        storageObject = new StoreAndGetFromPrefs(mContext);

        previewImagesView = parentView.findViewById(R.id.previewImagesView);
        btnGetStarted = parentView.findViewById(R.id.btnGetStarted);
        btnTerms = parentView.findViewById(R.id.btnTerms);
        botView = parentView.findViewById(R.id.botView);

        acceptText = parentView.findViewById(R.id.acceptText);

        termsText = parentView.findViewById(R.id.termsText);
        termsText.setPaintFlags(termsText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        chkTerms = parentView.findViewById(R.id.chkTerms);

        try {
            setCarouselDetails();
            setConfigs();

        } catch (Exception e) {
        }


        if (storageObject.checkIfUserIsRegistered() && !storageObject.retrieveUserPasswordFromPrefs().equals("")) {
            //USer Already registered
            chkTerms.setChecked(true);
        }


        btnTerms.setOnClickListener(v -> goToTermsPage());

        btnGetStarted.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            if (chkTerms.isChecked()) {
                storageObject.setRegisterFirst(true);
                goToSignUpPage();
            } else {
                String err = mContext.getResources().getString(R.string.acceptterms);
                Toast.makeText(mContext, err, Toast.LENGTH_SHORT).show();
            }

        });

        chkTerms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btnGetStarted.setBackgroundResource(R.xml.rounded_list_button_white);
                    acceptText.setTextColor(mContext.getResources().getColor(R.color.theme_color));
                } else {
                    btnGetStarted.setBackgroundResource(R.xml.rounded_list_transparent_white_stroke);
                    acceptText.setTextColor(mContext.getResources().getColor(R.color.white));
                }
            }
        });


        return parentView;
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

    @Override
    public void onResume() {
        super.onResume();
        if (chkTerms != null) chkTerms.setChecked(storageObject.isAgreedToTerms());

    }


    private void goToLanguagePage() {
        GeneralCommands.logEvent(parentActivity, "click_view_language", "click_view_language");

        // TODO Auto-generated method stub
        Intent mIntent = new Intent(parentActivity, LanguageActivity.class);
        mIntent.putExtra(KEY_START_FROM, "preview");
        GeneralCommands.goToPage(parentActivity, mIntent, false);

    }

    private void goToSignUpPage() {
        GeneralCommands.logEvent(parentActivity, "click_view_sign_up_page", "click_view_sign_up_page");

        // TODO Auto-generated method stub
        Intent mIntent = new Intent(parentActivity, ActivateAccountActivity.class);
        mIntent.putExtra(KEY_START_FROM, "preview");
        GeneralCommands.goToPage(parentActivity, mIntent, false);

    }

    private void goToTermsPage() {
        //Log Event
        GeneralCommands.logEvent(parentActivity, "click_view_terms", "click_view_terms");

        // TODO Auto-generated method stub
        Intent mIntent = new Intent(parentActivity, TermsActivity.class);
        mIntent.putExtra(KEY_START_FROM, "preview");
        GeneralCommands.goToPage(parentActivity, mIntent, false);

    }

    public void setConfigs() {
        //Remote Configs
        remoteConfig = new FirebaseConfig();
        mFirebaseConfig = remoteConfig.getConfigs();
        mFirebaseConfig.fetchAndActivate().addOnCompleteListener(parentActivity, new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful()) {
                    boolean updated = task.getResult();
//							Toast.makeText(mContext, "Fetch and activate succeeded",
//									Toast.LENGTH_SHORT).show();


                    //Support Number
                    support_number = mFirebaseConfig.getString("SUPPORT_NUMBER");
                    storageObject.saveSupportNumberToPrefs(support_number);


                    //Log Data Usage Every Launch
                    should_log_data_usage_every_launch = mFirebaseConfig.getBoolean("should_log_data_usage_every_launch");
                    storageObject.saveLogDataUsageEveryLaunchToPrefs(should_log_data_usage_every_launch);


                    //Whatsapp Number
                    whatsapp_number = mFirebaseConfig.getString("WHATSAPP_NUMBER");
                    storageObject.saveWhatsAppNumberToPrefs(whatsapp_number);

                    //Upgrade Feature
                    upgrade_feature_enabled = mFirebaseConfig.getBoolean("upgrade_feature_enabled");
                    storageObject.saveUpgradeFeatureEnabledToPrefs(upgrade_feature_enabled);

                    //WhatsApp Error Button
                    show_whatsapp_button_on_error_page = mFirebaseConfig.getBoolean("show_whatsapp_button_on_error_page");
                    storageObject.saveShowWhatsAppOnError(show_whatsapp_button_on_error_page);

                    //Call Support Error Button
                    show_call_support_options_on_error_page = mFirebaseConfig.getBoolean("show_call_support_options_on_error_page");
                    storageObject.saveShowCallSupportOptionsOnError(show_call_support_options_on_error_page);

                    //Rating Enabled
                    has_rating_enabled = mFirebaseConfig.getBoolean("has_rating_enabled");
                    storageObject.setRatingEnabled(has_rating_enabled);

                    //Minimum Rating Days
                    minimum_rating_days = (int) mFirebaseConfig.getLong("minimum_rating_days");
                    storageObject.saveMinimumRatingDaysToPrefs(minimum_rating_days);


                    //Call Support Error Button
                    support_page_configs = mFirebaseConfig.getString("support_page_configs");
                    try {
                        JSONObject supportObj = new JSONObject(support_page_configs);
                        Log.d("ZeSupport", "ZeSupportConfigs" + supportObj);
                        storageObject.saveSupportPageConfigToPrefs(supportObj);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    carousel_view = mFirebaseConfig.getString("carousel_view_v2");
                    try {
                        JSONObject obj = new JSONObject(carousel_view);

                        Log.d("ZeCarousel", "ZeCarousel" + obj);
                        refreshCarouselDetails(obj);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //Service Down Object Check
                    service_down = mFirebaseConfig.getString("service_down");
                    if (service_down != null && !service_down.trim().isEmpty()) {
                        try {
                            Log.d("ZeDown", "ZeDown" + service_down);

                            // Save to Prefs
                            storageObject.saveServiceDownObjectToPrefs(service_down);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    Log.d("ZeFetchFailed", "ZeFetchFailed");
                }

            }
        });

        mFirebaseConfig.addOnConfigUpdateListener(new ConfigUpdateListener() {
            @Override
            public void onUpdate(ConfigUpdate configUpdate) {
                //Log.d("ZeConfigs", "Updated keys: " + configUpdate.getUpdatedKeys());

                mFirebaseConfig.activate().addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                        //Support Number
                        support_number = mFirebaseConfig.getString("SUPPORT_NUMBER");
                        storageObject.saveSupportNumberToPrefs(support_number);


                        //Log Data Usage Every Launch
                        should_log_data_usage_every_launch = mFirebaseConfig.getBoolean("should_log_data_usage_every_launch");
                        storageObject.saveLogDataUsageEveryLaunchToPrefs(should_log_data_usage_every_launch);


                        //Whatsapp Number
                        whatsapp_number = mFirebaseConfig.getString("WHATSAPP_NUMBER");
                        storageObject.saveWhatsAppNumberToPrefs(whatsapp_number);

                        //Upgrade Feature
                        upgrade_feature_enabled = mFirebaseConfig.getBoolean("upgrade_feature_enabled");
                        storageObject.saveUpgradeFeatureEnabledToPrefs(upgrade_feature_enabled);

                        //WhatsApp Error Button
                        show_whatsapp_button_on_error_page = mFirebaseConfig.getBoolean("show_whatsapp_button_on_error_page");
                        storageObject.saveShowWhatsAppOnError(show_whatsapp_button_on_error_page);


                        //Rating Enabled
                        has_rating_enabled = mFirebaseConfig.getBoolean("has_rating_enabled");
                        storageObject.setRatingEnabled(has_rating_enabled);

                        //Minimum Rating Days
                        minimum_rating_days = (int) mFirebaseConfig.getLong("minimum_rating_days");
                        storageObject.saveMinimumRatingDaysToPrefs(minimum_rating_days);

                        //Call Support Error Button
                        show_call_support_options_on_error_page = mFirebaseConfig.getBoolean("show_call_support_options_on_error_page");
                        storageObject.saveShowCallSupportOptionsOnError(show_call_support_options_on_error_page);

                        //Call Support Error Button
                        support_page_configs = mFirebaseConfig.getString("support_page_configs");
                        try {
                            JSONObject supportObj = new JSONObject(support_page_configs);
                            Log.d("ZeSupport", "ZeSupportConfigs" + supportObj);
                            storageObject.saveSupportPageConfigToPrefs(supportObj);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        //Refresh Carousel
                        carousel_view = mFirebaseConfig.getString("carousel_view_v2");
                        try {
                            JSONObject obj = new JSONObject(carousel_view);
                            Log.d("ZeCarousel", "ZeCarousel" + obj);
                            refreshCarouselDetails(obj);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        //Service Down Object Check
                        service_down = mFirebaseConfig.getString("service_down");
                        if (service_down != null && !service_down.trim().isEmpty()) {
                            try {
                                Log.d("ZeDown", "ZeDown" + service_down);

                                // Save to Prefs
                                storageObject.saveServiceDownObjectToPrefs(service_down);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }

            @Override
            public void onError(FirebaseRemoteConfigException error) {
                Log.w("ZeConfigs", "Config update error with code: " + error.getCode(), error);
            }
        });
    }

    public void setCarouselDetails() {

        viewListener = position -> {
            View customView = getLayoutInflater().inflate(R.layout.carousel_view_custom, null);
            //set view attributes here

            ImageView imageView = customView.findViewById(R.id.imageView);
            if (previewImages != null && previewImages.length >= position)
                imageView.setImageResource(previewImages[position]);

            FirmaTextView mainMsg = customView.findViewById(R.id.mainMsg);
            FirmaTextView subMsg = customView.findViewById(R.id.subMsg);
            LinearLayout indicatorView = customView.findViewById(R.id.indicatorView);


            if (position == 0) {
                //botView.setVisibility(View.GONE);


                mainMsg.setText(mContext.getResources().getString(R.string.mainmsg2));
                subMsg.setText(mContext.getResources().getString(R.string.submsg2));
            } else if (position == 1) {
                //botView.setVisibility(View.GONE);
                mainMsg.setText(mContext.getResources().getString(R.string.mainmsg3));
                subMsg.setText(mContext.getResources().getString(R.string.submsg3));
            } else if (position == 2) {
                //botView.setVisibility(View.GONE);
                mainMsg.setText(mContext.getResources().getString(R.string.mainmsg1));
                subMsg.setText(mContext.getResources().getString(R.string.submsg1));


            }

            addIndicatorViews(indicatorView, position);

            return customView;
        };

        previewImagesView.setPageCount(previewImages.length);
        previewImagesView.setViewListener(viewListener);


    }

    public void refreshCarouselDetails(JSONObject jsonObject) throws JSONException {
        JSONArray drcArray = new JSONArray();
        JSONArray dataArray = new JSONArray();


        JSONObject newJsonObject = jsonObject.getJSONObject("drc");
        if (newJsonObject.has("fr") && storageObject.retrieveLanguageFromPrefs().equals("fr")) {
            drcArray = newJsonObject.getJSONArray("fr");


        } else {
            drcArray = newJsonObject.getJSONArray("en");
        }


        dataArray = drcArray;

        if (dataArray.length() > 0) {
            previewImagesView.setSlideInterval(4000);
            previewImagesView.setPageCount(dataArray.length());

            JSONArray finalDataArray = dataArray;
            viewListener = new ViewListener() {
                @Override
                public View setViewForPosition(int position) {
                    View customView = getLayoutInflater().inflate(R.layout.carousel_view_custom, null);
                    //set view attributes here

                    ImageView imageView = customView.findViewById(R.id.imageView);

                    FirmaTextView mainMsg = customView.findViewById(R.id.mainMsg);
                    FirmaTextView subMsg = customView.findViewById(R.id.subMsg);
                    LinearLayout indicatorView = customView.findViewById(R.id.indicatorView);


                    try {
                        JSONObject kenyaObject = finalDataArray.getJSONObject(position);

                        // Extract individual items from the "" object
                        String title = kenyaObject.getString("title");
                        String message = kenyaObject.getString("message");
                        String image = kenyaObject.getString("image");

                        mainMsg.setText(title);
                        subMsg.setText(message);

                        // Do something with the extracted title and message
                        ImageUtil.displayImage(imageView, image, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String s, View view) {


                            }

                            @Override
                            public void onLoadingFailed(String s, View view, FailReason failReason) {

                                if (previewImages != null && previewImages.length >= position && imageView != null)
                                    imageView.setImageResource(previewImages[position]);

                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {

                                if (previewImages != null && previewImages.length >= position && imageView != null)
                                    imageView.setImageResource(previewImages[position]);
                            }
                        });


                    } catch (Exception e) {

                        e.printStackTrace();
                    }


                    addIndicatorViews(indicatorView, position);
                    return customView;
                }
            };

            previewImagesView.setPageCount(dataArray.length());
            previewImagesView.setViewListener(viewListener);

        }


    }


    public void addIndicatorViews(LinearLayout indicatorView, int position) {
        // Set up indicators
        indicatorView.removeAllViews();
        for (int i = 0; i < previewImages.length; i++) {
            View indicator = getLayoutInflater().inflate(R.layout.list_item_indicator, null);
            RelativeLayout iconCircle = indicator.findViewById(R.id.iconCircle);

            if (i == position) {
                iconCircle.setBackgroundResource(R.drawable.rounded_rectangle_indicator);
            } else {
                iconCircle.setBackgroundResource(R.drawable.rounded_rectangle_indicator_inactive);
            }

            indicatorView.addView(indicator);
        }
    }

    //Service Down
    public void openDialogServiceDown()
    {
        View contentView = parentActivity.getLayoutInflater().inflate(R.layout.dialog_content_service_down, null);


        final DialogPlus dialogPlus = new DialogPlus.Builder(parentActivity)
                .setContentHolder(new ViewHolder(contentView))
                .setGravity(DialogPlus.Gravity.CENTER)
                .setBackgroundColorResourceId(R.color.transparent)
                .setCancelable(true)
                .create();


        //Content View
        View content = dialogPlus.getHolderView();



        RelativeLayout loadingView= content.findViewById(R.id.loadingItems);
        RelativeLayout btnClose =content.findViewById(R.id.btnClose);
        RelativeLayout btnBack= content.findViewById(R.id.btnBack);

        FirmaTextView titleText=content.findViewById(R.id.titleText);
        FirmaTextView titleDescription=content.findViewById(R.id.titleDescription);

        String tText=mContext.getResources().getString(R.string.serviceinterruption);
        String tDescription=mContext.getResources().getString(R.string.servicedowndescription);

        ImageView errorIcon = content.findViewById(R.id.titleImage);


        if (storageObject.retrieveServiceDownObjectFromPrefs() != null) {
            ServiceDownObject service = storageObject.retrieveServiceDownObjectFromPrefs();

            if(service.getCategory() !=null && service.getTitle_description() !=null)
            {
                if (service.getCategory().toLowerCase().contains("server_down")) {
                    errorIcon.setImageResource(R.drawable.ics_icon_service_down);
                } else if (service.getCategory().toLowerCase().contains("intermittent")) {

                    errorIcon.setImageResource(R.drawable.ics_icon_service_intermittent);

                } else if (service.getCategory().toLowerCase().contains("payment")) {
                    errorIcon.setImageResource(R.drawable.ics_icon_service_payment);

                } else if (service.getCategory().toLowerCase().contains("timestamp")) {
                    errorIcon.setImageResource(R.drawable.ics_icon_service_intermittent);

                }
            }

            if (storageObject.retrieveLanguageFromPrefs().equalsIgnoreCase("fr")) {

                if(service.getTitle_fr()!=null && service.getTitle_description_fr()!=null)
                {
                    tText = service.getTitle_fr();
                    tDescription = service.getTitle_description_fr();
                }

            } else
            {
                tText = service.getTitle();
                tDescription = service.getTitle_description();

//				Toast.makeText(mContext,"GootenTitle"+tDescription,Toast.LENGTH_SHORT).show();

            }

            setHtml(titleDescription, tDescription);
            setHtml(titleText, tText);

        } else {
            titleText.setText(tText);
            titleDescription.setText(Html.fromHtml(tDescription));

        }




        //Current Device OBJect
        JSONObject obj = new JSONObject();
        AssetObject currentAsset= storageObject.retrieveCurrentAssetObjectFromPrefs();
        try {

            Gson gson= new Gson();

            obj.put("assetLoanNumber",currentAsset.getLoanNumber());
            obj.put("assetName",currentAsset.getAssetTypeName());
            obj.put("assetTypeId",currentAsset.getAssetTypeId());
            if(currentAsset.getDeviceUpgradeObject()!=null && currentAsset.getDeviceUpgradeObject().getUpgradeDevices()!=null)
            {
                obj.put("currentDevice",gson.toJson(currentAsset.getDeviceUpgradeObject().getCurrentDevice()));
                obj.put("upgradeOptions",gson.toJson(currentAsset.getDeviceUpgradeObject().getUpgradeDevices()));

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }



        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GeneralCommands.logEventObject(parentActivity,"click_service_down_back","click_service_down_back", obj);
                dialogPlus.dismiss();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GeneralCommands.logEventObject(parentActivity,"click_service_down_back","click_service_down_back", obj);
                dialogPlus.dismiss();
            }
        });






        dialogPlus.show();
    }

    public void tryOpenServiceDownDialogWithBackoff(final Context context, boolean forceShow) {
        if (context == null) return;

        long now = System.currentTimeMillis();
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        long lastShown = prefs.getLong(KEY_SERVICE_DOWN_LAST_SHOWN, 0L);

        if (forceShow || (now - lastShown) >= FOUR_HOURS_MS || lastShown == 0L) {
            // Ensure UI thread when showing dialog
            if (context instanceof Activity) {
                ((Activity) context).runOnUiThread(() -> {
                    openDialogServiceDown();
                    // update timestamp
                    prefs.edit().putLong(KEY_SERVICE_DOWN_LAST_SHOWN, System.currentTimeMillis()).apply();
                });
            } else {
                // If no activity context available, still update timestamp and try to show
                openDialogServiceDown();
                prefs.edit().putLong(KEY_SERVICE_DOWN_LAST_SHOWN, System.currentTimeMillis()).apply();
            }
        } else {
            // Backoff not expired — no-op (optional: log or debug)
            long remaining = FOUR_HOURS_MS - (now - lastShown);
            Log.d("ZeServiceDown", "Dialog backoff active, remaining ms: " + remaining);
        }
    }

    /**
     * Force-clear the backoff so dialog may show immediately next time.
     */
    public void resetServiceDownDialogBackoff(Context context) {
        if (context == null) return;
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(KEY_SERVICE_DOWN_LAST_SHOWN).apply();
    }

    /**
     * Remove the <Data><![CDATA[ ... ]]></Data> wrapper if present and return the inner HTML.
     */
    private String unwrapCData(String maybeCdata) {
        if (maybeCdata == null) return null;
        String s = maybeCdata.trim();

        final String start = "<Data><![CDATA[";
        final String end = "]]></Data>";

        if (s.startsWith(start) && s.endsWith(end)) {
            s = s.substring(start.length(), s.length() - end.length());
        }

        // If the JSON contained escaped newlines (like "\\n"), convert them to actual newlines
        s = s.replace("\\n", "\n");

        // Convert literal newlines to <br/> so HTML renderer preserves line breaks
        s = s.replace("\n\n", "<br/><br/>").replace("\n", "<br/>");

        // If the content was double-escaped (e.g. contains "&lt;font&gt;"), unescape basic HTML entities:
        // Simple common replacements (avoid heavy deps). Add more if you need them.
        s = s.replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&");

        return s;
    }

    /**
     * Render HTML into a TextView safely.
     */
    private void setHtml(TextView tv, String rawHtml) {
        if (tv == null) return;

        String unwrapped = unwrapCData(rawHtml);
        if (unwrapped == null) {
            tv.setText("");
            return;
        }

        // Use HtmlCompat for consistent behavior across API levels
        Spanned spanned = HtmlCompat.fromHtml(unwrapped, HtmlCompat.FROM_HTML_MODE_LEGACY);

        tv.setText(spanned);
        // Make links clickable (if you have <a href="..."> links)
        tv.setMovementMethod(LinkMovementMethod.getInstance());
    }

    //End Service Down


}
