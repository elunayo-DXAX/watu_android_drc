package com.watutz.app.constant;


public interface IWhereMyLocationConstants {


    String ANALYTICS_ID = "273766161";
    String KEY_START_FROM = "start_from";

    String PDF_STRING = "pdfString";
    String PDF_FILE_PATH = "pdfFilePath";
    String WAITING_TIME = "60";
    String ITEM_ID = "0";

    String URL_TERMS_AND_CONDITIONS = "https://watuafrica.com/data-protection-and-privacy-notice/";
    String URL_SHARE_APP = "https://play.google.com/store/apps/details?id=com.watutz.app"; // for examples :https://play.google.com/store/search?q=ypyproductions
    String URL_SHARE_APP_FACEBOOK = "https://fb.me/183811895609192";
    String URL_WHATSAPP = "https://wa.me/";

    String SHARE_TITLE = "share_title";
    String SHARE_DESCRIPTION = "share_description";
    String SHARE_IMAGE_URL = "share_image_url";
    String SHARE_LINK = "share_link";
    String SHARE_LINK_FB = "share_link_fb";
    String SHARE_IMAGE_URI_PATH = "share_image_path";

    String INTENT_RESTARTER = "com.watutz.app.restarter";

    String CP = "9e0f478f89140ca3fec7a369399c5dc879d94d3cbb37c71a6d2c9393e99366a2";

    String MAP_KEY = "AIzaSyBBAMIdM1a5YhZHFA8_qIUF-chA4wUSXE0";
    String API_KEY = "AIzaSyC6zGsFwIPTC4ix6CTIDmoHvBQVDFIiDJQ";
    String LOG_TAG = "Google Places Autocomplete";

    String PHONE_NUMBER = "phone_number";


    String POS = "pos";

    String SKE = "648d6f1989bdd151";
    String IVSPEC = "503e3c5296d6c669";

    String MESSAGE = "message";
    String MESSAGE_TYPE = "message_type";


    String gifUrlCons = "https://watuafrica.com/wp-content/uploads/2024/06/Confetti-and-scribbles.gif";
    String gifUrlConf = "https://watuafrica.com/wp-content/uploads/2024/06/Confetti-1.gif";


    String IMAGE_CAPTION = "image_caption";
    String IMAGE_URL = "image_url";

    String USER_KEY = "user_key";
    String USER_NAME = "user_name";
    String USER_EMAIL = "user_email";
    String USER_PHONE = "user_phone";
    String USER_PROFILE_PIC = "user_profile_pic";
    String USER_ID = "user_id";

    String GROUP_ID = "group_id";
    String GROUP_NAME = "group_name";
    String GROUP_ADMIN_EMAIL = "group_admin_email";
    String GROUP_ADMIN_NAME = "group_adin_name";
    String GROUP_NUMBER_OF_MEMBERS = "group_number_of_members";
    String GROUP_PHONE = "group_phone";
    String GROUP_IS_GROUP = "group_is_group";
    String GROUP_ADMINS = "group_admins";
    String GROUP_PRIVATE = "group_private";


    String ADMIN_SUPPORT_EMAIL = "admin@watu.com";
    String ADMIN_SUPPORT_NAME = "Watu Support";
    String ADMIN_SUPPORT_KEY = "1";
    int ADMIN_SUPPORT_ID = 1;
    String ADMIN_SUPPORT_PHONE = "0800750228";


    String GROUP_GENERAL_ID = "watu_all";
    String GROUP_GENERAL_NAME = "General Forum";

    String JOB_LOCATION = "job_location";
    String JOB_LOCATION_NAME = "job_location_name";
    String JOB_TYPE = "job_type";
    String JOB_COST = "job_cost";


    //Location Stuff
    String SEARCH_RADIUS_IN_KM = "10000";
    String LOCATION_LAT_LONG = "location_lat_long";
    String LOCATION_NAME = "location_name";
    float ONE_MILE = 1.60934f;

    String FORMAT_URL_PHOTO_REF = "https://maps.googleapis.com/maps/api/place/photo?photoreference=%1$s&sensor=false&maxwidth=%2$s&maxheight=%3$s&key=%4$s";
    String FORMAT_DETAIL_LOCATION_REF = "https://maps.googleapis.com/maps/api/place/details/json?reference=%1$s&sensor=%2$s&key=%3$s";
    String FORMAT_URL_TYPE_SEARCH = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
            + "location=%1$s,%2$s&radius=%3$s&types=%4$s&sensor=%5$s&key=%6$s";
    String FORMAT_URL_TEXT_SEARCH = "https://maps.googleapis.com/maps/api/place/textsearch/json?"
            + "location=%1$s,%2$s&radius=%3$s&query=%4$s&sensor=%5$s&key=%6$s";


    String FORMAT_URL_LOCATION_SEARCH = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?"
            + "location=%1$s,%2$s&radius=%3$s&query=%4$s&sensor=%5$s&key=%6$s";
    String UNIT_KILOMETTER = "Kilometers";
    String UNIT_MILE = "Miles";
    double INVALID_VALUE = -1000;
    int TIME_OUT = 5;
    String PIORITY_DISTANCE = "distance";
    String PIORITY_PHONE = "prominence";

    String TRAVEL_MODE_DRIVING = "driving";
    String DRIVING = "Driving";

    String TRAVEL_MODE_WALKING = "walking";
    String WALKING = "Walking ";

    String SERVICE = "service_type";
    String SERVICE_DISPLAY = "service_display";
    String PARAMS = "params ";


    String FORMAT_URL_PHOTO = "https://maps.googleapis.com/maps/api/place/photo?photoreference=%1$s&maxheight=320&maxwidth=320&sensor=false&key=%2$s";

    String FORMAT_DIRECTION_URL = "https://maps.googleapis.com/maps/api/directions/json?origin=%1$s&destination=%2$s&sensor=%3$s&key=%4$s&mode=%5$s";
    String FORMAT_NEXTPAGE_TYPE_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=%1$s,%2$s&radius=%3$s&types=%4$s&sensor=%5$s&key=%6$s&pagetoken=%7$s";
    String FORMAT_NEXTPAGE_TEXT_SEARCH_URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?location=%1$s,%2$s&radius=%3$s&types=%4$s&sensor=%5$s&key=%6$s&pagetoken=%7$s";

    int TYPE_SEARCH_BY_TYPES = 1;
    int TYPE_SEARCH_BY_TEXT = 2;
    int TYPE_SEARCH_BY_LOCATION = 3;

    //Facebook Stuff
    String PERMISSION = "publish_actions";
    String PENDING_ACTION_BUNDLE_KEY = "com.watutz.app:PendingAction";


    enum PendingAction {
        NONE,
        POST_PHOTO,
        POST_STATUS_UPDATE

    }


    //Firebase URL
    String FIREBASE_URL = "https://watu-13cb8-default-rtdb.europe-west1.firebasedatabase.app";

    //Permissions Result Codes
    int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1000;


    //API Responses
    String RESPONSE_RESULT_CODE = "result_code";
    String RESPONSE_RESULT_MESSAGE = "message";
    String RESPONSE_RESULT_DATA = "data";
    String RESPONSE_RESULT_SUCCESS = "0";
    String RESPONSE_RESULT_FAILED = "1";
    String RESPONSE_RESULT_MESSAGE_NOT_REGISTERED = "Email not found. Check and try again.";

    String OTP_MODE = "otp_mode";


    //About Us
    String URL_SITE = "https://www.watuafrica.co.tz";
    String URL_FACEBOOK = "https://www.facebook.com/profile.php?id=100094292111495";
    String URL_TWITTER = "https://twitter.com/Watu_Africa";

    String URL_INSTAGRAM = "https://www.instagram.com/watu_tanzania/";
    String URL_EMAIL = "info@watuafrica.co.tz";

    String gifUrlWallet = "https://watuafrica.com/wp-content/uploads/2024/12/payment-icon-compressed.gif";

    String LIVE_URL = "https://tz-mob.watucredit.com/";

    String TEST_URL = "https://tz-demomx-mob.watucredit.com/";


//	public static final String DEV_URL="https://dev.fintelum.com:8345/";


    String WATU_URL = LIVE_URL;

    String IMAGE_DEFAULT_URL = WATU_URL + "/";
    String DEFAULT_AVATAR = WATU_URL + "/img/avatar.png";


    String USER_LOGIN_URL = WATU_URL + "/agent_sessions/login";

    String USER_ADD_DEVICE_URL = WATU_URL + "/pilot/api/v1/device/add/";
    String USER_GET_TOKEN_URL = WATU_URL + "/pilot/api/oauth/token";
    String USER_GET_CLIENT_LOANS_URL = WATU_URL + "/pilot/api/v1/loans";
    String USER_GET_LOAN_DETAILS_URL = WATU_URL + "/pilot/api/v1/loan/details";


    String USER_GET_INFO_URL = WATU_URL + "/agents";
    String USER_UPLOAD_DOCUMENT_URL = WATU_URL + "/documents";
    String USER_GET_CREDIT_LIMIT_URL = WATU_URL + "/credit_limit/";
    String USER_GET_UPCOMING_PAYMENTS_URL = WATU_URL + "/payments/upcoming/";

    String USER_LOGIN_FACEBOOK_URL = WATU_URL + "/api/facebook/login?";
    String USER_REGISTER_URL = WATU_URL + "/agent_auth";
    String USER_REGISTER_SELF_EMPLOYED_URL = WATU_URL + "/auth";
    String USER_FORGOT_PASSWORD_URL = WATU_URL + "/auth/password?";
    String USER_OTP_REQUEST_URL = WATU_URL + "/auth/password/generate";
    String USER_OTP_RESEND_URL = WATU_URL + "/auth/password/resend";
    String USER_OTP_CONFIRM_URL = WATU_URL + "/auth/password/verify";
    String USER_EMAIL_CONFIRMATION_RESEND_URL = WATU_URL + "/auth/confirmation";
    String USER_PASSWORD_CHANGE_URL = WATU_URL + "/auth/password/update_password";
    String USER_VERIFY_TOKEN_URL = WATU_URL + "/api/v2/oauth/token";
    String USER_VERIFY_IDENTITY_URL = WATU_URL + "/v1/verify_customer_info";
    String USER_GET_PURCHASES_URL = WATU_URL + "/facilities/all/";
    String USER_GET_PAYMENTS_URL = WATU_URL + "/payments/";
    String USER_INITIATE_STK_PAYMENT_URL = WATU_URL + "/payments/generate_stk_push/";
    String USER_POLL_STK_PAYMENT_URL = WATU_URL + "/payments/";

    String USER_CUSTOMER_VIEW_URL = WATU_URL + "/get_current_customer";

    String USER_UPLOAD_IMAGE_URL = WATU_URL + "/api/image?";
    String USER_GET_MY_PROFILE_URL = WATU_URL + "/api/user?";
    String USER_EDIT_PROFILE_URL = WATU_URL + "/agents";
    String USER_UPLOAD_PROFILE_PIC_URL = WATU_URL + "/api/image-upload?";

    String USER_GET_SINGLE_JOB_URL = WATU_URL + "/api/job?";
    String USER_GET_JOBS_URL = WATU_URL + "/api/jobs?";


}
