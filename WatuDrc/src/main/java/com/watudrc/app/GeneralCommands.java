package com.watudrc.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CalendarContract;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.watudrc.app.R;

import com.watudrc.app.constant.IWhereMyLocationConstants;
import com.watudrc.app.font.FirmaTextView;

import com.watudrc.app.prefs.StoreAndGetFromPrefs;
import com.watudrc.app.util.ImageUtil;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.DialogPlus.Gravity;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.ypyproductions.utils.DirectionUtils;


public class GeneralCommands extends BaseActivity implements IWhereMyLocationConstants
{
	public static final String TAG = GeneralCommands.class.getSimpleName();

	static StoreAndGetFromPrefs storageObject;
	static Activity mContext;
	static Activity parentActivity;

	//User

	String userKey = "";
	String userName = "";
	String userEmail = "";
	String userPhone= "";
	String userProfilePic="";

	long theAddedEventId = 0;

	public static int PERMISSIONS_REQUEST_CODE = 0001;

	// Stuff

	String status= "pending";

	// WebViewStuff
	public boolean IS_FB = true;
	public String LINK_BACK = "#null";
	boolean openPage = false;

	// Loading View
	public View loadingView;

	RelativeLayout loadingActive;
	RelativeLayout resultSuccess;
	RelativeLayout resultError;

	FirmaTextView progressText;
	FirmaTextView successText;
	FirmaTextView errorText;

	ImageView progressWheelImage;
	ImageView progressWheel;
	ImageView successImage;

	Animation animFadeIn;
	Animation animFadeOut;
    private static DatabaseReference mFirebaseRefUsers;

	public GeneralCommands(Activity context)
	{
		mContext = context;
		parentActivity= mContext;
		storageObject = new StoreAndGetFromPrefs(mContext);

		//Init Firebase
		//Init Firebase
		mFirebaseRefUsers = FirebaseDatabase.getInstance().getReference().child("users");


	}

	public static String getUserInitials(String fullName) {

			if (fullName == null) return "";
			String trimmed = fullName.trim();
			if (trimmed.isEmpty()) return "";

			String[] parts = trimmed.split("\\s+");
			// always take the first letter of the first part
			char first = parts[0].charAt(0);

			if (parts.length == 1) {
				// only one name → single initial
				return String.valueOf(Character.toUpperCase(first));
			}

			// two or more parts → also take the first letter of the last part
			char last = parts[parts.length - 1].charAt(0);
			return ("" + Character.toUpperCase(first) + Character.toUpperCase(last));

	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plain);

	}

	//Image
	Bitmap bmp = null;

	public void hideKeyboard()
	{
		InputMethodManager inputManager = (InputMethodManager)
				mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

		inputManager.hideSoftInputFromWindow(mContext.getCurrentFocus().getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);

	}

	public void openDialogChangeLanguage(final String currentLanguage)
	{
		View contentView = parentActivity.getLayoutInflater().inflate(R.layout.dialog_content_change_language, null);


		final DialogPlus dialogPlus = new DialogPlus.Builder(parentActivity)
				.setContentHolder(new ViewHolder(contentView))
				.setGravity(Gravity.CENTER)
				.setBackgroundColorResourceId(R.color.transparent)
				.setCancelable(true)

				.create();



		//Content View
		View content = dialogPlus.getHolderView();


		final RelativeLayout loadingView= content.findViewById(R.id.loadingItems);

		final FirmaTextView statusHolder1;
		final FirmaTextView statusHolder2;

		final RelativeLayout sIcon1;
		final RelativeLayout sIcon2;

		final FirmaTextView statusText1;
		final FirmaTextView statusText2;

		RelativeLayout btnLanguageEnglish,btnLanguageSwahili,btnLanguageFinnish;

		statusHolder1= content.findViewById(R.id.statusHolder1);
		statusHolder2= content.findViewById(R.id.statusHolder2);

		sIcon1= content.findViewById(R.id.sIcon1);
		sIcon2= content.findViewById(R.id.sIcon2);

		statusText1= content.findViewById(R.id.statusText1);
		statusText2= content.findViewById(R.id.statusText2);

		btnLanguageEnglish= content.findViewById(R.id.btnLanguageEnglish);
		btnLanguageSwahili= content.findViewById(R.id.btnLanguageSwahili);
		status=currentLanguage;

		if(currentLanguage.equals("en"))
		{
			statusHolder1.setTextColor(mContext.getResources().getColor(R.color.theme_pink));
			statusHolder2.setTextColor(mContext.getResources().getColor(R.color.theme_dark_light));

			sIcon1.setBackgroundResource(R.xml.rounded_circle_pink_small);
			sIcon2.setBackgroundResource(R.xml.rounded_circle_dark_light_small);

			statusText1.setTextColor(mContext.getResources().getColor(R.color.theme_pink));
			statusText2.setTextColor(mContext.getResources().getColor(R.color.theme_dark_light));

		}

		else if(currentLanguage.equals("fr"))
		{
			statusHolder1.setTextColor(mContext.getResources().getColor(R.color.theme_dark_light));
			statusHolder2.setTextColor(mContext.getResources().getColor(R.color.theme_pink));

			sIcon1.setBackgroundResource(R.xml.rounded_circle_dark_light_small);
			sIcon2.setBackgroundResource(R.xml.rounded_circle_pink_small);

			statusText1.setTextColor(mContext.getResources().getColor(R.color.theme_dark_light));
			statusText2.setTextColor(mContext.getResources().getColor(R.color.theme_pink));

		}


		btnLanguageEnglish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				status ="en";

				statusHolder1.setTextColor(mContext.getResources().getColor(R.color.theme_pink));
				statusHolder2.setTextColor(mContext.getResources().getColor(R.color.theme_dark_light));

				sIcon1.setBackgroundResource(R.xml.rounded_circle_pink_small);
				sIcon2.setBackgroundResource(R.xml.rounded_circle_dark_light_small);

				statusText1.setTextColor(mContext.getResources().getColor(R.color.theme_pink));
				statusText2.setTextColor(mContext.getResources().getColor(R.color.theme_dark_light));



			}
		});


		btnLanguageSwahili.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				status ="fr";

				statusHolder1.setTextColor(mContext.getResources().getColor(R.color.theme_dark_light));
				statusHolder2.setTextColor(mContext.getResources().getColor(R.color.theme_pink));

				sIcon1.setBackgroundResource(R.xml.rounded_circle_dark_light_small);
				sIcon2.setBackgroundResource(R.xml.rounded_circle_pink_small);

				statusText1.setTextColor(mContext.getResources().getColor(R.color.theme_dark_light));
				statusText2.setTextColor(mContext.getResources().getColor(R.color.theme_pink));



			}
		});



		RelativeLayout btnYes= content.findViewById(R.id.btnYes);

		btnYes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				storageObject.saveLanguageToPrefs(status);
				setLocale(status);
			}
		});

		RelativeLayout btnNo= content.findViewById(R.id.btnNo);

		btnNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogPlus.dismiss();
			}
		});


		dialogPlus.show();
	}

	public void openDialogConfirmLogOutUser(String message)
	{
		View contentView = parentActivity.getLayoutInflater().inflate(R.layout.dialog_content_confirm, null);


		final DialogPlus dialogPlus = new DialogPlus.Builder(parentActivity)
				.setContentHolder(new ViewHolder(contentView))
				.setGravity(Gravity.CENTER)
				.setBackgroundColorResourceId(R.color.transparent)
				.setCancelable(true)
				.create();


		//Content View
		View content = dialogPlus.getHolderView();

		FirmaTextView confirmMessage = content.findViewById(R.id.confirmText);

		confirmMessage.setText(message);

		final RelativeLayout loadingView= content.findViewById(R.id.loadingItems);


		RelativeLayout btnYes= content.findViewById(R.id.btnYes);

		btnYes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				GeneralCommands.logEvent(parentActivity,"click_logout_user","click_logout_user");

				dialogPlus.dismiss();
				logOutUser();
			}
		});

		RelativeLayout btnNo= content.findViewById(R.id.btnNo);

		btnNo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogPlus.dismiss();
			}
		});


		dialogPlus.show();
	}

	public void shareToWhatsApp(String title, String description, String imageUrl, String link, Uri imageBitmapUri)
	{
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		//Target whatsapp:
		shareIntent.setPackage("com.whatsapp");
		//Add text and then Image URI
		shareIntent.putExtra(Intent.EXTRA_TEXT, description);
		shareIntent.putExtra(Intent.EXTRA_STREAM, imageBitmapUri);
		shareIntent.setType("image/*");
		shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

		try {
			parentActivity.startActivity(shareIntent);
		} catch (Exception ex)
		{
			ex.printStackTrace();
			shareNormalWithImage(title,description,imageUrl,link, imageBitmapUri);
		}
	}

	public void shareToWhatsAppText(String title, String description, String imageUrl, String link)
	{
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		//Target whatsapp:
		shareIntent.setPackage("com.whatsapp");
		//Add text and then Image URI
		shareIntent.putExtra(Intent.EXTRA_TEXT, description);
		shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

		try {
			parentActivity.startActivity(shareIntent);
		} catch (Exception ex)
		{
			ex.printStackTrace();
			shareNormalText(title,description,imageUrl,link);
		}
	}

	public void shareToTwitter(String title, String description, String imageUrl, String link, Uri imageBitmapUri)
	{

		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		//Target whatsapp:
		shareIntent.setPackage("com.twitter.android");
		//Add text and then Image URI
		shareIntent.putExtra(Intent.EXTRA_TEXT, description);
		shareIntent.putExtra(Intent.EXTRA_STREAM, imageBitmapUri);
		shareIntent.setType("image/*");
		shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

		try {
			parentActivity.startActivity(shareIntent);
		} catch (Exception ex)
		{
			ex.printStackTrace();
			shareNormalWithImage(title,description,imageUrl,link, imageBitmapUri);

		}
	}

	public void shareToTwitterText(String title, String description, String imageUrl, String link)
	{

		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		//Target whatsapp:
		shareIntent.setPackage("com.twitter.android");
		//Add text and then Image URI
		shareIntent.putExtra(Intent.EXTRA_TEXT, description);
		shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

		try {
			parentActivity.startActivity(shareIntent);
		} catch (Exception ex)
		{
			ex.printStackTrace();
			shareNormalText(title,description,imageUrl,link);

		}
	}

	public void shareNormalText(String title, String description, String imageUrl, String link)
	{
		Intent shareintent = new Intent(Intent.ACTION_SEND);
		shareintent.setType("text/plain");

		shareintent.putExtra(Intent.EXTRA_TEXT, description);
		parentActivity.startActivity(Intent.createChooser(shareintent, "Share via"));

	}

	public void shareNormalWithImage(String title, String description, String imageUrl, String link, Uri imageBitmapUri)
	{
		Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		//Target any app:
		//Add text and then Image URI
		shareIntent.putExtra(Intent.EXTRA_TEXT, description);
		shareIntent.putExtra(Intent.EXTRA_STREAM, imageBitmapUri);
		shareIntent.setType("image/*");
		shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

		try {
			parentActivity.startActivity(shareIntent);
		} catch (Exception ex)
		{
			ex.printStackTrace();
			shareNormalText(title,description,imageUrl,link);
		}
	}


	public File getImageFileFromUrl(String img_url) {

		File casted_image = null;

		try {
			File rootSdDirectory = Environment.getExternalStorageDirectory();

			casted_image = new File(rootSdDirectory, "image.jpg");
			if (casted_image.exists()) {
				casted_image.delete();
			}
			casted_image.createNewFile();

			FileOutputStream fos = new FileOutputStream(casted_image);

			URL url = new URL(img_url);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.connect();
			InputStream in = connection.getInputStream();

			byte[] buffer = new byte[1024];
			int size = 0;
			while ((size = in.read(buffer)) > 0) {
				fos.write(buffer, 0, size);
			}
			fos.close();
			return casted_image;

		} catch (Exception e) {

			System.out.print(e);
			// e.printStackTrace();

		}
		return casted_image;
	}

	public static Uri getImageBitmapUriFromImageView(Activity parentActivity,ImageView imageView) {
		// Extract Bitmap from ImageView drawable
		Drawable drawable = imageView.getDrawable();
		Bitmap bmp = null;
		if (drawable instanceof BitmapDrawable){
			bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
		} else
		{
			return null;
		}
		// Store image to default external storage directory
		Uri bmpUri = null;
		try {
			// Use methods on Context to access package-specific directories on external storage.
			// This way, you don't need to request external read/write permission.
			// See https://youtu.be/5xVh-7ywKpE?t=25m25s
			File file =  new File(parentActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
			FileOutputStream out = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.close();
			// **Warning:** This will fail for API >= 24, use a FileProvider as shown below instead.
			bmpUri = Uri.fromFile(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bmpUri;
	}

	public Uri getImageBitmapUriFromImageUrl(ImageView imageView, String urlData) {
		// Extract Bitmap from ImageView drawable
		Drawable drawable = imageView.getDrawable();

		if (drawable instanceof BitmapDrawable){
			bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
		} else
		{

			ImageUtil.loadImage(urlData, new ImageLoadingListener() {

				@Override
				public void onLoadingStarted(String imageUri, View view) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onLoadingFailed(String imageUri, View view,
											FailReason failReason) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
					// TODO Auto-generated method stub
					bmp= loadedImage;
				}

				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					// TODO Auto-generated method stub

				}
			});

		}
		// Store image to default external storage directory
		Uri bmpUri = null;
		try {
			// Use methods on Context to access package-specific directories on external storage.
			// This way, you don't need to request external read/write permission.
			// See https://youtu.be/5xVh-7ywKpE?t=25m25s
			File file =  new File(parentActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
			FileOutputStream out = new FileOutputStream(file);
			bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.close();
			// **Warning:** This will fail for API >= 24, use a FileProvider as shown below instead.
			bmpUri = Uri.fromFile(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bmpUri;
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

	}

    public void logOutUser()
    {
        storageObject.logOutUser();
        //LoginManager.getInstance().logOut();

        Intent mIntent = new Intent(parentActivity, LoginActivity.class);
        mIntent.putExtra(KEY_START_FROM, "main");
        mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        GeneralCommands.goToPage(parentActivity,mIntent,true);

    }


	public void viewImageDetail(String imageCaption, String imageUrl)
	{
		Intent mIntent = new Intent(mContext, ImageDetailActivity.class);
		mIntent.putExtra(KEY_START_FROM, "dialog_info");
		mIntent.putExtra(IMAGE_CAPTION, imageCaption);
		mIntent.putExtra(IMAGE_URL, imageUrl);

		DirectionUtils.changeActivity(mContext, R.anim.fadein, R.anim.fadeout, false , mIntent);
	}

	public void setLocale(String lang) {
		// Step 1: Update Locale
		Locale myLocale = new Locale(lang);
		Locale.setDefault(myLocale);

		Resources res = parentActivity.getResources();
		Configuration conf = res.getConfiguration();

		conf.setLocale(myLocale); // Set the new locale
		conf.setLayoutDirection(myLocale); // Update layout direction for RTL languages

		Context context = parentActivity.createConfigurationContext(conf);
		res.updateConfiguration(conf, res.getDisplayMetrics());

		// Step 2: Save Language to Preferences
		storageObject.saveLanguageToPrefs(lang);

		// Step 3: Restart the Target Activity
		Intent mIntent;
		if (storageObject.checkIfUserIsRegistered()) {
			mIntent = new Intent(parentActivity, LoginActivity.class);
		} else {
			mIntent = new Intent(parentActivity, PreviewActivity.class);
		}

		mIntent.putExtra("KEY_START_FROM", "login_page");

		// Clear existing tasks and start fresh
		mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		parentActivity.startActivity(mIntent);

		// Finish current activity to avoid stacking
		parentActivity.finish();
	}



	public static boolean isNumeric(String str)
	{
		return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}


	public String[] getEventDayAndMonth(String date) {
		String[] separated = date.split("/");
		String day = separated[0];
		int month = Integer.valueOf(separated[1]);
		String dispMonth = getMonthName(month);
		String theDnM = day + "," + dispMonth;
		String[] dNm = theDnM.split(",");
		return dNm;

	}

	public static String getMonthName(int month) {
		switch (month) {
			case 1:
				return "January";

			case 2:
				return "February";

			case 3:
				return "March";

			case 4:
				return "April";

			case 5:
				return "May";

			case 6:
				return "June";

			case 7:
				return "July";

			case 8:
				return "August";

			case 9:
				return "September";

			case 10:
				return "October";

			case 11:
				return "November";

			case 12:
				return "December";
		}

		return "";
	}

	public long addToCalendar(String eventId, String eventTitle, String eventDesc, String eventVenue, String eventActualStart) {
		try {
			theAddedEventId = pushAppointmentsToCalender(mContext, eventTitle, eventDesc, eventVenue, 1, eventActualStart, true, true);
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return theAddedEventId;
	}

	public static long pushAppointmentsToCalender(Activity curActivity, String title, String addInfo, String place, int status, String eventStartDate, boolean needReminder, boolean needMailService) throws java.text.ParseException {
		/***************** Event: note(without alert) *******************/

		long theStartDate = 0;
		long theEndDate = 0;

		try {

			String dateString = eventStartDate;
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
			Date date = sdf.parse(dateString);
			theStartDate = date.getTime();
			theEndDate = theStartDate + 1000 * 60 * 60;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		Log.d("Ze Start Date", "Ze Start Date" + eventStartDate + ":" + theStartDate);

		//
		// eventValues.put("eventTimezone", "UTC/GMT +3:00");

		ContentValues event = new ContentValues();
		event.put(CalendarContract.Events.CALENDAR_ID, 1);
		TimeZone timeZone = TimeZone.getDefault();
		event.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
		event.put(CalendarContract.Events.TITLE, title);
		event.put(CalendarContract.Events.DESCRIPTION, addInfo);
		event.put(CalendarContract.Events.EVENT_LOCATION, place);
		event.put(CalendarContract.Events.HAS_ALARM, 1);
		event.put(CalendarContract.Events.DTSTART, (theStartDate));
		event.put(CalendarContract.Events.DTEND, (theEndDate));
		event.put(CalendarContract.Events.ALL_DAY, 0);
		event.put(CalendarContract.Events.HAS_ALARM, 1);
		Uri baseUri;
		if (Build.VERSION.SDK_INT >= 8) {
			baseUri = Uri.parse("content://com.android.calendar/events");
		} else {
			baseUri = Uri.parse("content://calendar/events");
		}
		Uri eventUri = mContext.getContentResolver().insert(baseUri, event);
		long eventID = Long.parseLong(eventUri.getLastPathSegment());

		// Reminder
		/***************** Event: Reminder(with alert) Adding reminder to event *******************/

		String reminderUriString = "content://com.android.calendar/reminders";

		ContentValues reminderValues = new ContentValues();

		reminderValues.put("event_id", eventID);
		reminderValues.put("minutes", 10); // Default value of the system.
		// Minutes is a
		reminderValues.put("method", 1);// Alert Methods: Default(0),//
		// Alert(1), Email(2),// SMS(3)

		ContentValues reminderValues2 = new ContentValues();

		reminderValues2.put("event_id", eventID);
		reminderValues2.put("minutes", 10); // Default value of the system.
		// Minutes is a
		reminderValues2.put("method", 2);// Alert Methods: Default(0),//
		// Alert(1), Email(2),// SMS(3)

		curActivity.getApplicationContext().getContentResolver().insert(Uri.parse(reminderUriString), reminderValues);
		curActivity.getApplicationContext().getContentResolver().insert(Uri.parse(reminderUriString), reminderValues2);

		// Add Attendee
		String attendeesUriString = "content://com.android.calendar/attendees";
		ContentValues attendeesValues = new ContentValues();

		attendeesValues.put("event_id", eventID);
		attendeesValues.put("attendeeName", storageObject.retrieveUserNameFromPrefs()); // Attendees
		// name
		attendeesValues.put("attendeeEmail", storageObject.retrieveUserEmailFromPrefs());// Attendee
		attendeesValues.put("attendeeRelationship", 0); // Relationship_Attendee(1),Relationship_None(0),Organizer(2),Performer(3),Speaker(4)
		attendeesValues.put("attendeeType", 0); // None(0),
		// Optional(1),Required(2),
		// Resource(3)
		attendeesValues.put("attendeeStatus", 1); // NOne(0),
		// Accepted(1),Decline(2),Invited(3),Tentative(4)

		curActivity.getApplicationContext().getContentResolver().insert(Uri.parse(attendeesUriString), attendeesValues);

		return eventID;

	}

	public int DeleteCalendarEntry(long entryID) {
		int iNumRowsDeleted = 0;

		Uri eventUri = ContentUris.withAppendedId(getCalendarUriBase(), entryID);
		iNumRowsDeleted = mContext.getContentResolver().delete(eventUri, null, null);

		return iNumRowsDeleted;
	}

	private Uri getCalendarUriBase() {
		Uri eventUri;
		if (Build.VERSION.SDK_INT <= 7) {
			// the old way

			eventUri = Uri.parse("content://calendar/events");
		} else {
			// the new way

			eventUri = Uri.parse("content://com.android.calendar/events");
		}

		return eventUri;
	}

	//Permissions
	public static boolean isPermissionRequired(@NonNull Context context, @NonNull String permission) {
		return Build.VERSION.SDK_INT >= 23 &&
				ContextCompat.checkSelfPermission(context,permission) != PackageManager.PERMISSION_GRANTED;
	}

	public static final Uri getUriToResource(@NonNull Context context,
											 @AnyRes int resourceId)
			throws Resources.NotFoundException {
		/** Return a Resources instance for your application's package. */
		Resources res = context.getResources();
		Resources resources = context.getResources();
		Uri uri = new Uri.Builder()
				.scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
				.authority(resources.getResourcePackageName(resourceId))
				.appendPath(resources.getResourceTypeName(resourceId))
				.appendPath(resources.getResourceEntryName(resourceId))
				.build();
		return uri;
	}

	public static String fixCommas(String digits) {
		if (digits ==null || digits.equals("0") || digits.equals("0.0")) {
			return "0";
		}


		// Split the input into whole and decimal parts
		String[] parts = digits.split("\\.");
		String wholePart = parts[0];
		String decimalPart = parts.length > 1 ? parts[1] : "";

		// Process the whole part for commas
		StringBuilder result = new StringBuilder();
		int len = wholePart.length();
		int nDigits = 0;

		for (int i = len - 1; i >= 0; i--) {
			result.insert(0, wholePart.charAt(i));
			nDigits++;
			if (((nDigits % 3) == 0) && (i > 0)) {
				result.insert(0, ',');
			}
		}

		// Append the decimal part if it exists
		if (!decimalPart.isEmpty()) {
			result.append('.').append(decimalPart);
		}

		return result.toString();
	}

	// WebView Stuff
	public void openDialogWebView(Activity parentActivity, final String url) {
		View contentView = parentActivity.getLayoutInflater().inflate(R.layout.dialog_content_web_view, null);

		final DialogPlus dialogPlus = new DialogPlus.Builder(parentActivity).setContentHolder(new ViewHolder(contentView)).setHeader(R.layout.dialog_header_web_view).setGravity(Gravity.BOTTOM)

				.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss(DialogPlus dialog) {
						// TODO Auto-generated method stub

					}
				})

				.create();

		// Content View
		View view = dialogPlus.getHolderView();

		final WebView mWebView = view.findViewById(R.id.webView);


		final RelativeLayout loadingView = view.findViewById(R.id.loadingItems);
		initializeLoader(loadingView);
		ConnectivityManager cm = (ConnectivityManager) parentActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected()) {
			SharedPreferences pref = mContext.getSharedPreferences("MyPref", 0); // 0
			// -
			// for
			// private
			// mode
			final String lastLink = pref.getString("lastlink", "#null");

			SQUWebViewSettings(mWebView);
			mWebView.setWebViewClient(new EVWebViewClient(mWebView));

			startLoading(mContext.getResources().getString(R.string.loading));

			mWebView.loadUrl(url);

		} else {
			// No Connection

			problemLoading(mContext.getResources().getString(R.string.error));

		}

		// Header View
		View headerView = dialogPlus.getHeaderView();

		RelativeLayout btnClose = headerView.findViewById(R.id.btnCancel);
		btnClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				stopEveryThing(mWebView);
				dialogPlus.dismiss();

			}
		});

		RelativeLayout btnWebBack = headerView.findViewById(R.id.btnWebBack);
		btnWebBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (null != LINK_BACK) {
					if (LINK_BACK.equals(url)) {
						stopEveryThing(mWebView);
						dialogPlus.dismiss();
					} else if (!mWebView.canGoBack() && !LINK_BACK.contains(url)) {
						stopEveryThing(mWebView);
						startLoading(mContext.getResources().getString(R.string.loading));

						mWebView.loadUrl(url);
					} else if (mWebView.canGoBack() && !LINK_BACK.equals(url)) {
						stopEveryThing(mWebView);
						startLoading(mContext.getResources().getString(R.string.loading));

						mWebView.goBack();
					}

				}

				else {

					stopEveryThing(mWebView);
					dialogPlus.dismiss();
				}

			}
		});

		dialogPlus.show();
	}


	// ===mWebView Options and Settings===//
	@SuppressWarnings("deprecation")
	public void SQUWebViewSettings(WebView mWebView) {
		mWebView.setHorizontalScrollBarEnabled(false);
		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.setScrollbarFadingEnabled(true);
		mWebView.getSettings().setAllowFileAccess(true);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		mWebView.getSettings().getPluginState();
		mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
		mWebView.getSettings().setGeolocationEnabled(true);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setDomStorageEnabled(true);
		mWebView.getSettings().setDatabaseEnabled(true);
//		mWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 15);
//		mWebView.getSettings().setAppCachePath(mContext.getCacheDir().getAbsolutePath());
		mWebView.getSettings().setAllowFileAccess(true);
//		mWebView.getSettings().setAppCacheEnabled(true);
		mWebView.getSettings().setGeolocationDatabasePath(mContext.getFilesDir().getPath());
	}

	public class EVWebViewClient extends WebViewClient {
		WebView theMWebView;

		public EVWebViewClient(WebView mWebView) {
			// TODO Auto-generated constructor stub
			theMWebView = mWebView;

		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (url.startsWith("http:") || url.startsWith("https:")) {
				startLoading(mContext.getResources().getString(R.string.loading));

				theMWebView.loadUrl(url);
			} else {
				startLoading(mContext.getResources().getString(R.string.loading));

				theMWebView.loadUrl(url);
			}
			return true;
		}

		// On Error
		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			problemLoading(mContext.getResources().getString(R.string.error));

		}

		// On Page Finished
		@Override
		public void onPageFinished(WebView view, String url) {
			finishLoading();
			saveCurrentLink(theMWebView);

		}

	}

	public void stopEveryThing(WebView mWebView) {
		if (mWebView != null) {
			mWebView.clearAnimation();
			mWebView.clearDisappearingChildren();
			mWebView.onPause();
			mWebView.stopLoading();
		}

	}

	public void refresh(WebView mWebView) {
		mWebView.stopLoading();
		mWebView.reload();
	}

	public void saveCurrentLink(WebView mWebView) {

		LINK_BACK = mWebView.getUrl();
		SharedPreferences pref = mContext.getSharedPreferences("MyPref", 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString("lastlink", LINK_BACK);
		editor.commit();
	}

	public void startLoading(String info) {
		loadingView.setVisibility(View.VISIBLE);
		loadingActive.setVisibility(View.VISIBLE);
		resultSuccess.setVisibility(View.GONE);
		resultError.setVisibility(View.GONE);
		progressText.setText(info);

	}

	public void problemLoading(String info) {
		String tapToDismiss = mContext.getResources().getString(R.string.taptodismiss);
		info = info + tapToDismiss;

		loadingView.setVisibility(View.VISIBLE);
		loadingActive.setVisibility(View.GONE);
		resultSuccess.setVisibility(View.GONE);
		resultError.startAnimation(animFadeIn);
		resultError.setVisibility(View.VISIBLE);
		errorText.setText(info);
		progressText.setText("");

	}

	public void successLoading(String info) {
		String tapToDismiss = mContext.getResources().getString(R.string.taptodismiss);
		info = info + tapToDismiss;

		loadingView.setVisibility(View.VISIBLE);
		loadingActive.setVisibility(View.GONE);
		resultSuccess.startAnimation(animFadeIn);
		resultSuccess.setVisibility(View.VISIBLE);
		resultError.setVisibility(View.GONE);
		successText.setText(info);
		progressText.setText("");

	}

	public void finishLoading() {
		loadingView.setVisibility(View.GONE);
		loadingView.setVisibility(View.GONE);
		loadingActive.setVisibility(View.GONE);
		resultSuccess.setVisibility(View.GONE);

	}


	public void initializeLoader(RelativeLayout lView) {
		loadingView = lView;
		loadingActive = loadingView.findViewById(R.id.loadingActive);
		resultSuccess = loadingView.findViewById(R.id.resultSuccess);

		resultError = loadingView.findViewById(R.id.resultError);
		progressText = loadingView.findViewById(R.id.progressText);
		successText = loadingView.findViewById(R.id.successText);
		errorText = loadingView.findViewById(R.id.errorText);

//		progressWheelImage = (ImageView) loadingView.findViewById(R.id.progressWheelImage);
//		progressWheel = (ImageView) loadingView.findViewById(R.id.progressWheel);
//		successImage = (ImageView) loadingView.findViewById(R.id.successImage);

		animFadeIn = AnimationUtils.loadAnimation(parentActivity, R.anim.fadein);
		animFadeOut = AnimationUtils.loadAnimation(parentActivity, R.anim.fadeout);

//		try {
//
//			GifDrawable gifDrawableWheelImage = new GifDrawable(parentActivity.getResources(), R.drawable.loadwheel);
//			GifDrawable gifDrawableWheel = new GifDrawable(parentActivity.getResources(), R.drawable.loader);
//			GifDrawable gifDrawableSuccess = new GifDrawable(parentActivity.getResources(), R.drawable.loadsuccess);
//
//			progressWheelImage.setImageDrawable(gifDrawableWheelImage);
//			progressWheel.setImageDrawable(gifDrawableWheel);
//			successImage.setImageDrawable(gifDrawableSuccess);
//
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		resultError.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Send Broadcast RSVPD.
				finishLoading();

			}
		});
	}

	public static void goToPage(Activity fromActivity, Intent intent, boolean hasFinish)
	{
//		intent.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
//		intent.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
//				| WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);

		// 1) Clean up only Intent flags related to activity behavior:
		if (hasFinish) {
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		}

		// 2) Launch:
		fromActivity.startActivity(intent);
		fromActivity.overridePendingTransition(R.anim.enter, R.anim.exit);

		// 3) Only finish *this* Activity:
		if (hasFinish) {
			fromActivity.finish();
		}


	}


	public static void logEvent(Context context, String eventName, String eventValue)
	{
		String versionName = "";
		String versionCode= "";
		String version= "";

		try {
			PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			versionName = pInfo.versionName;
			// Use longVersionCode for modern Android (API 28+)
			versionCode = ""+androidx.core.content.pm.PackageInfoCompat.getLongVersionCode(pInfo);

		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		version= versionName+"."+versionCode;

		if(storageObject==null)storageObject= new StoreAndGetFromPrefs(context);
		Bundle bundle = new Bundle();
		bundle.putString(eventName, eventValue);
		bundle.putString("hash", storageObject.retrieveHashFromPrefs());
		bundle.putString("phone_number", storageObject.retrieveUserPhoneNumberFromPrefs());
		bundle.putString("app_version", version);
		bundle.putString("event_date", getCurrentDate());
		bundle.putString("event_time", getCurrentTime());
		bundle.putBoolean("is_boda_user", storageObject.isBodaUser());
		bundle.putBoolean("is_simu_user", storageObject.isSimuUser());
		bundle.putLong("event_timestamp", System.currentTimeMillis());
		FirebaseAnalytics.getInstance(context).logEvent(eventValue, bundle);
		Log.d("ZeEvent","ZeEvent"+eventName+":"+eventValue);

	}

	public static void logEventObject(Context context, String eventName, String eventValue, JSONObject obj)
	{
		String versionName = "";
		String versionCode= "";
		String version= "";

		try {
			PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			versionName = pInfo.versionName;
			// Use longVersionCode for modern Android (API 28+)
			versionCode = ""+androidx.core.content.pm.PackageInfoCompat.getLongVersionCode(pInfo);

		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		version= versionName+"."+versionCode;

		if(storageObject==null)storageObject= new StoreAndGetFromPrefs(context);
		Bundle bundle = new Bundle();
		bundle.putString(eventName, eventValue);
		bundle.putString("hash", storageObject.retrieveHashFromPrefs());
		bundle.putString("phone_number", storageObject.retrieveUserPhoneNumberFromPrefs());
		bundle.putString("app_version", version);
		bundle.putString("event_date", getCurrentDate());

		bundle.putBoolean("is_boda_user", storageObject.isBodaUser());
		bundle.putBoolean("is_simu_user", storageObject.isSimuUser());

		bundle.putString("event_time", getCurrentTime());
		bundle.putLong("event_timestamp", System.currentTimeMillis());

		// Loop through the keys
		Iterator<String> keys = obj.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			// Access the value using obj.getString(key) or other appropriate methods
			String value = "";
			try {
				value = obj.getString(key);
				if(key.equals("value"))
				{
					bundle.putDouble(key, obj.getDouble(key));

				}
				else
				{
					bundle.putString(key, value);

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			// Your logic here, for example, printing key-value pairs
		}

		FirebaseAnalytics.getInstance(context).logEvent(eventValue, bundle);
		Log.d("ZeEvent","ZeEvent"+eventName+":"+eventValue+":"+ bundle);

	}




	public static String getSha256Hash(String password) {
		try {
			MessageDigest digest = null;
			try {
				digest = MessageDigest.getInstance("SHA-256");
			} catch (NoSuchAlgorithmException e1) {
				e1.printStackTrace();
			}
			digest.reset();
			return bin2hex(digest.digest(password.getBytes()));
		} catch (Exception ignored) {
			return null;
		}
	}

	private static String bin2hex(byte[] data) {
		StringBuilder hex = new StringBuilder(data.length * 2);
		for (byte b : data)
			hex.append(String.format("%02x", b & 0xFF));
		return hex.toString();
	}

	public static String getCurrentDateTime() {
		String delegateTime = "hh:mm aaa";// 2: 30 pm
		String time = (String) DateFormat.format(delegateTime,Calendar.getInstance().getTime());


		SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");//14 August, 2014
		String date = df.format(Calendar.getInstance().getTime());

		return date+" "+time;


	}

	public static String getCurrentDate() {
		String delegateTime = "hh:mm aaa";// 2: 30 pm
		String time = (String) DateFormat.format(delegateTime,Calendar.getInstance().getTime());


		SimpleDateFormat df = new SimpleDateFormat("dd MMM, yyyy");//14 August, 2014
		String date = df.format(Calendar.getInstance().getTime());

		return date;


	}


	public static String getCurrentTime() {
		String delegateTime = "hh:mm aaa";// 2: 30 pm
		String time = (String) DateFormat.format(delegateTime,Calendar.getInstance().getTime());


		SimpleDateFormat df = new SimpleDateFormat("dd MMMM, yyyy");//14 August, 2014
		String date = df.format(Calendar.getInstance().getTime());

		return time;


	}


	public static boolean isDemo(String number, String pin)
	{
		boolean isDemo=false;
		if((number.equals("271123456789") || number.equals("0123456789") || number.equals("123456789")) && pin.equals("1234"))
		{
			return true;
		}


		return isDemo;
	}


	public static String capitalizeWords(String input) {

		try {
			if (input == null || input.isEmpty()) {
				return input;
			}

			StringBuilder titleCase = new StringBuilder();
			boolean nextTitleCase = true;

			for (char c : input.toCharArray()) {
				if (Character.isSpaceChar(c)) {
					nextTitleCase = true;
				} else if (nextTitleCase) {
					c = Character.toTitleCase(c);
					nextTitleCase = false;
				}
				titleCase.append(c);
			}

			return titleCase.toString();


		} catch (Exception e) {
			e.printStackTrace();

			return input;
		}
	}

}
