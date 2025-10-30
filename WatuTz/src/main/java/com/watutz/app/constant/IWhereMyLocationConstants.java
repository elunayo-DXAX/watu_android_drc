package com.watutz.app.constant;


public interface IWhereMyLocationConstants {
	
	
	public static final String ANALYTICS_ID = "273766161";
	public static final String KEY_START_FROM = "start_from";

	public static final String PDF_STRING = "pdfString";
	public static final String PDF_FILE_PATH = "pdfFilePath";
	public static final String WAITING_TIME = "60";
	public static final String ITEM_ID = "0";

	public static final String URL_TERMS_AND_CONDITIONS = "https://watuafrica.com/data-protection-and-privacy-notice/";
	public static final String URL_SHARE_APP = "https://play.google.com/store/apps/details?id=com.watutz.app"; // for examples :https://play.google.com/store/search?q=ypyproductions
    public static final String URL_SHARE_APP_FACEBOOK = "https://fb.me/183811895609192";
	public static final String URL_WHATSAPP = "https://wa.me/";

    public static final String SHARE_TITLE= "share_title";
    public static final String SHARE_DESCRIPTION = "share_description";
    public static final String SHARE_IMAGE_URL = "share_image_url";
    public static final String SHARE_LINK = "share_link";
    public static final String SHARE_LINK_FB = "share_link_fb";
    public static final String SHARE_IMAGE_URI_PATH = "share_image_path";

	public static final String INTENT_RESTARTER = "com.watutz.app.restarter";

	public static final String CP = "9e0f478f89140ca3fec7a369399c5dc879d94d3cbb37c71a6d2c9393e99366a2";
    
    public static final String MAP_KEY = "AIzaSyBBAMIdM1a5YhZHFA8_qIUF-chA4wUSXE0";
	public static final String API_KEY = "AIzaSyC6zGsFwIPTC4ix6CTIDmoHvBQVDFIiDJQ";
	static final String LOG_TAG = "Google Places Autocomplete";

	public static final String PHONE_NUMBER = "phone_number";


	
    public static final String POS = "pos";
    
    public static final String SKE = "648d6f1989bdd151";
	public static final String IVSPEC = "503e3c5296d6c669";
   
    public static final String MESSAGE = "message";
	public static final String MESSAGE_TYPE = "message_type";


	String gifUrlCons = "https://watuafrica.com/wp-content/uploads/2024/06/Confetti-and-scribbles.gif";
	String gifUrlConf = "https://watuafrica.com/wp-content/uploads/2024/06/Confetti-1.gif";


	public static final String IMAGE_CAPTION = "image_caption";
	public static final String IMAGE_URL = "image_url";
	
	public static final String USER_KEY = "user_key";
	public static final String USER_NAME = "user_name";
	public static final String USER_EMAIL = "user_email";
	public static final String USER_PHONE = "user_phone";
	public static final String USER_PROFILE_PIC = "user_profile_pic";
	public static final String USER_ID = "user_id";
	
	public static final String GROUP_ID = "group_id";
	public static final String GROUP_NAME = "group_name";
	public static final String GROUP_ADMIN_EMAIL = "group_admin_email";
	public static final String GROUP_ADMIN_NAME = "group_adin_name";
	public static final String GROUP_NUMBER_OF_MEMBERS = "group_number_of_members";
	public static final String GROUP_PHONE = "group_phone";
	public static final String GROUP_IS_GROUP = "group_is_group";
	public static final String GROUP_ADMINS = "group_admins";
	public static final String GROUP_PRIVATE = "group_private";
	
    
    public static final String ADMIN_SUPPORT_EMAIL = "admin@watu.com";
   	public static final String ADMIN_SUPPORT_NAME = "Watu Support";
	public static final String ADMIN_SUPPORT_KEY = "1";
	public static final int    ADMIN_SUPPORT_ID = 1;
	public static final String ADMIN_SUPPORT_PHONE = "0800750228";
	
    
    public static final String GROUP_GENERAL_ID = "watu_all";
	public static final String GROUP_GENERAL_NAME = "General Forum";
	
	public static final String JOB_LOCATION = "job_location";
	public static final String JOB_LOCATION_NAME = "job_location_name";
	public static final String JOB_TYPE = "job_type";
	public static final String JOB_COST = "job_cost";

    
    //Location Stuff
	public static  String SEARCH_RADIUS_IN_KM = "10000";
	public static  String LOCATION_LAT_LONG = "location_lat_long";
	public static  String LOCATION_NAME = "location_name";
	public static final float ONE_MILE = 1.60934f;
	
    public static final String FORMAT_URL_PHOTO_REF = "https://maps.googleapis.com/maps/api/place/photo?photoreference=%1$s&sensor=false&maxwidth=%2$s&maxheight=%3$s&key=%4$s";
	public static final String FORMAT_DETAIL_LOCATION_REF = "https://maps.googleapis.com/maps/api/place/details/json?reference=%1$s&sensor=%2$s&key=%3$s";
	public static final String FORMAT_URL_TYPE_SEARCH = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
			+ "location=%1$s,%2$s&radius=%3$s&types=%4$s&sensor=%5$s&key=%6$s";
	public static final String FORMAT_URL_TEXT_SEARCH = "https://maps.googleapis.com/maps/api/place/textsearch/json?"
			+ "location=%1$s,%2$s&radius=%3$s&query=%4$s&sensor=%5$s&key=%6$s";

	
	public static final String FORMAT_URL_LOCATION_SEARCH = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
			+ "location=%1$s,%2$s&radius=%3$s&query=%4$s&sensor=%5$s&key=%6$s";
	public static final String UNIT_KILOMETTER = "Kilometers";
	public static final String UNIT_MILE = "Miles";
	public static final double INVALID_VALUE = -1000;
	public static final int TIME_OUT = 5;
	public static final String PIORITY_DISTANCE = "distance";
	public static final String PIORITY_PHONE = "prominence";

	public static final String TRAVEL_MODE_DRIVING = "driving";
	public static final String DRIVING = "Driving";

	public static final String TRAVEL_MODE_WALKING = "walking";
	public static final String WALKING = "Walking ";

	public static final String SERVICE = "service_type";
	public static final String SERVICE_DISPLAY = "service_display";
	public static final String PARAMS = "params ";

	
	public static final String FORMAT_URL_PHOTO = "https://maps.googleapis.com/maps/api/place/photo?photoreference=%1$s&maxheight=320&maxwidth=320&sensor=false&key=%2$s";

	public static final String FORMAT_DIRECTION_URL = "https://maps.googleapis.com/maps/api/directions/json?origin=%1$s&destination=%2$s&sensor=%3$s&key=%4$s&mode=%5$s";
	public static final String FORMAT_NEXTPAGE_TYPE_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%1$s,%2$s&radius=%3$s&types=%4$s&sensor=%5$s&key=%6$s&pagetoken=%7$s";
	public static final String FORMAT_NEXTPAGE_TEXT_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?location=%1$s,%2$s&radius=%3$s&types=%4$s&sensor=%5$s&key=%6$s&pagetoken=%7$s";

	public static final int TYPE_SEARCH_BY_TYPES = 1;
	public static final int TYPE_SEARCH_BY_TEXT = 2;
	public static final int TYPE_SEARCH_BY_LOCATION = 3;
    
  //Facebook Stuff
    public static final String PERMISSION = "publish_actions";
    public final String PENDING_ACTION_BUNDLE_KEY = "com.watutz.app:PendingAction";


    public static enum PendingAction {
        NONE,
        POST_PHOTO,
        POST_STATUS_UPDATE
        
    }


	//Firebase URL
	public static final String FIREBASE_URL = "https://watu-13cb8-default-rtdb.europe-west1.firebasedatabase.app";

    //Permissions Result Codes
    public int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1000;

	
	//API Responses
	public static final String RESPONSE_RESULT_CODE = "result_code";
	public static final String RESPONSE_RESULT_MESSAGE = "message";
	public static final String RESPONSE_RESULT_DATA = "data";
	public static final String RESPONSE_RESULT_SUCCESS = "0";
	public static final String RESPONSE_RESULT_FAILED = "1";
	public static final String RESPONSE_RESULT_MESSAGE_NOT_REGISTERED = "Email not found. Check and try again.";

	public static final String OTP_MODE = "otp_mode";


	//About Us
	public static final String URL_SITE="https://www.watuafrica.co.tz";
	public static final String URL_FACEBOOK="https://www.facebook.com/profile.php?id=100094292111495";
	public static final String URL_TWITTER="https://twitter.com/Watu_Africa";

	public static final String URL_INSTAGRAM="https://www.instagram.com/watu_tanzania/";
	public static final String URL_EMAIL="info@watuafrica.co.tz";

	String gifUrlWallet = "https://watuafrica.com/wp-content/uploads/2024/12/payment-icon-compressed.gif";

	public static final String LIVE_URL="https://tz-mob.watucredit.com/";

	public static final String TEST_URL="https://tz-demomx-mob.watucredit.com/";


//	public static final String DEV_URL="https://dev.fintelum.com:8345/";


	public static final String WATU_URL=TEST_URL;

	public static final String IMAGE_DEFAULT_URL=WATU_URL+"/";
	public static final String DEFAULT_AVATAR = WATU_URL+"/img/avatar.png";


	public static final String USER_LOGIN_URL = WATU_URL+"/agent_sessions/login";

	public static final String USER_ADD_DEVICE_URL = WATU_URL+"/pilot/api/v1/device/add/";
	public static final String USER_GET_TOKEN_URL = WATU_URL+"/pilot/api/oauth/token";
	public static final String USER_GET_CLIENT_LOANS_URL = WATU_URL+"/pilot/api/v1/loans";
	public static final String USER_GET_LOAN_DETAILS_URL = WATU_URL+"/pilot/api/v1/loan/details";


	public static final String USER_GET_INFO_URL = WATU_URL+"/agents";
	public static final String USER_UPLOAD_DOCUMENT_URL = WATU_URL+"/documents";
	public static final String USER_GET_CREDIT_LIMIT_URL = WATU_URL+"/credit_limit/";
	public static final String USER_GET_UPCOMING_PAYMENTS_URL = WATU_URL+"/payments/upcoming/";

	public static final String USER_LOGIN_FACEBOOK_URL = WATU_URL+"/api/facebook/login?";
	public static final String USER_REGISTER_URL = WATU_URL+"/agent_auth";
	public static final String USER_REGISTER_SELF_EMPLOYED_URL = WATU_URL+"/auth";
	public static final String USER_FORGOT_PASSWORD_URL = WATU_URL+"/auth/password?";
	public static final String USER_OTP_REQUEST_URL = WATU_URL+"/auth/password/generate";
	public static final String USER_OTP_RESEND_URL = WATU_URL+"/auth/password/resend";
	public static final String USER_OTP_CONFIRM_URL = WATU_URL+"/auth/password/verify";
	public static final String USER_EMAIL_CONFIRMATION_RESEND_URL = WATU_URL+"/auth/confirmation";
	public static final String USER_PASSWORD_CHANGE_URL = WATU_URL+"/auth/password/update_password";
	public static final String USER_VERIFY_TOKEN_URL = WATU_URL+"/api/v2/oauth/token";
	public static final String USER_VERIFY_IDENTITY_URL = WATU_URL+"/v1/verify_customer_info";
	public static final String USER_GET_PURCHASES_URL = WATU_URL+"/facilities/all/";
	public static final String USER_GET_PAYMENTS_URL = WATU_URL+"/payments/";
	public static final String USER_INITIATE_STK_PAYMENT_URL = WATU_URL+"/payments/generate_stk_push/";
	public static final String USER_POLL_STK_PAYMENT_URL = WATU_URL+"/payments/";

	public static final String USER_CUSTOMER_VIEW_URL = WATU_URL+"/get_current_customer";

	public static final String USER_UPLOAD_IMAGE_URL = WATU_URL+"/api/image?";
	public static final String USER_GET_MY_PROFILE_URL = WATU_URL+"/api/user?";
	public static final String USER_EDIT_PROFILE_URL = WATU_URL+"/agents";
	public static final String USER_UPLOAD_PROFILE_PIC_URL = WATU_URL+"/api/image-upload?";
	
	public static final String USER_GET_SINGLE_JOB_URL = WATU_URL+"/api/job?";
	public static final String USER_GET_JOBS_URL = WATU_URL+"/api/jobs?";

	
	
}
