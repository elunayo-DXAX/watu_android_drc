package com.watutz.app.util;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @Headers({"Accept: application/json"})
    @POST("/api/v1/signup")
    Call<ResponseBody> userRegister(@Header("X-API-SECRET") String secret, @Body RequestBody params);

    @Headers({"Accept: application/json"})
    @POST("api/v1/auth/otprequest")
    Call<ResponseBody> userOTPRequest(@Header("X-API-SECRET") String secret,@Header("Authorization") String token, @Body RequestBody params);

    @Headers({"Accept: application/json"})
    @POST("api/v2/auth/otprequest")
    Call<ResponseBody> userOTPRequestKnox(@Header("X-API-SECRET") String secret,@Header("Authorization") String token, @Body RequestBody params);

    @Headers({"Accept: application/json"})
    @PUT("/api/v1/profile")
    Call<ResponseBody> userUpdateProfile(@Header("X-API-SECRET") String secret,@Header("Authorization") String token, @Body RequestBody params);

    @Headers({"Accept: application/json"})
    @POST("/api/v1/notification/token")
    Call<ResponseBody> userUpdateTokenFCM(@Header("X-API-SECRET") String secret,@Header("Authorization") String token, @Body RequestBody params);


    @Headers({"Accept: application/json"})
    @POST("/api/v1/auth/otpverify")
    Call<ResponseBody> userOTPConfirm(@Header("X-API-SECRET") String secret,@Header("Authorization") String token, @Body RequestBody params);

    @Headers({"Accept: application/json"})
    @POST("/api/v1/payment/requestpayment")
    Call<ResponseBody> userInitiatePayment(@Header("X-API-SECRET") String secret,@Header("Authorization") String token, @Body RequestBody params);


    @Headers({"Accept: application/json"})
    @GET("/api/v1/loan/upgrade-eligibility/{loanNumber}")
    Call<ResponseBody> userGetLoanUpgradeEligibility(@Header("X-API-SECRET") String secret,@Header("Authorization") String token,@Path("loanNumber") String loanNumber);



    @Headers({"Accept: application/json"})
    @POST("/api/v1/tempunlock/confirm/{loanNumber}")
    Call<ResponseBody> userTemporaryUnlock(@Header("X-API-SECRET") String secret,@Header("Authorization") String token, @Path("loanNumber") String  loanNumber);


    @Headers({"Accept: application/json"})
    @GET("/api/v1/payment/{transactionNumber}/status")
    Call<ResponseBody> userConfirmPayment(@Header("X-API-SECRET") String secret,@Header("Authorization") String token,@Path("transactionNumber") String  transactionNumber);


    @Headers({"Accept: application/json"})
    @GET("/api/v1/customer")
    Call<ResponseBody> userGetCustomerDetails(@Header("X-API-SECRET") String secret,@Header("Authorization") String token);


    @Headers({"Accept: application/json"})
    @GET("/api/v1/loans ")
    Call<ResponseBody> userGetLoanAccounts(@Header("X-API-SECRET") String secret,@Header("Authorization") String token);

    @Headers({"Accept: application/json"})
    @GET("/api/v1/loan/details/{loanNumber}")
    Call<ResponseBody> userGetLoanDetailsSingle(@Header("X-API-SECRET") String secret,@Header("Authorization") String token,@Path("loanNumber") String loanNumber);

    @Headers({"Accept: application/json"})
    @GET("api/v1/transactions")
    Call<ResponseBody> userGetPayments(@Header("X-API-SECRET") String secret,@Header("Authorization") String token,@Query("offset") String offset);

    @Headers({"Accept: application/json"})
    @POST("/api/v1/feedback")
    Call<ResponseBody> userSubmitFeedback(@Header("X-API-SECRET") String secret,@Header("Authorization") String token, @Body RequestBody params);

    @Headers({"Accept: application/json"})
    @POST("/api/v1/paymentrating")
    Call<ResponseBody> userSubmitPaymentRating(@Header("X-API-SECRET") String secret,@Header("Authorization") String token, @Body RequestBody params);

    @Headers({"Accept: application/json"})
    @POST("/api/v1/action")
    Call<ResponseBody> addUserAction(@Header("X-API-SECRET") String secret,@Header("Authorization") String token, @Body RequestBody params);

    @Headers({"Accept: application/json"})
    @GET("api/v1/auth/otptype")
    Call<ResponseBody> userGetOTPChannels(@Header("X-API-SECRET") String secret,@Header("Authorization") String token);

    @Headers({"Accept: application/json"})
    @POST("/api/v1/user/device/data")
    Call<ResponseBody> userPushData(@Header("X-API-SECRET") String secret,@Header("Authorization") String token, @Body RequestBody params);



    @Headers({"Accept: application/json"})
    @GET("/api/v1/payment/providers")
    Call<ResponseBody> userGetPaymentProviders(@Header("X-API-SECRET") String secret,@Header("Authorization") String token);

    @Headers({"Accept: application/json"})
    @GET("/api/v1/loan/agreement/{loanNumber}")
    Call<ResponseBody> userDownloadLoanAgreement(@Header("X-API-SECRET") String secret,@Header("Authorization") String token,@Path("loanNumber") String loanNumber);


    @POST("/documents")
    @Multipart
    Call<ResponseBody> uploadFile(
            @Header("Cookie") String sessionId,
            @Part MultipartBody.Part body,
            @Part("customer_id") RequestBody customer_id,
            @Part("request_type") RequestBody request_type


    );

    @POST("/documents")
    @Multipart
    Call<ResponseBody> uploadTwoFiles(
            @Header("Cookie") String sessionId,
            @Part MultipartBody.Part body,
            @Part MultipartBody.Part body2,
            @Part("customer_id") RequestBody customer_id,
            @Part("request_type") RequestBody request_type


    );

    @POST("/documents")
    @Multipart
    Call<ResponseBody> uploadThreeFiles(
            @Header("Cookie") String sessionId,
            @Part MultipartBody.Part body,
            @Part MultipartBody.Part body2,
            @Part MultipartBody.Part body3,
            @Part("customer_id") RequestBody customer_id,
            @Part("request_type") RequestBody request_type


    );
}
