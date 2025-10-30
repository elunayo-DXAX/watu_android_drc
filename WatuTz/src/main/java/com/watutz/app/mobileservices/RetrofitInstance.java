package com.watutz.app.mobileservices;

import com.watutz.app.constant.IWhereMyLocationConstants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance implements IWhereMyLocationConstants {

    private static Retrofit retrofit;
    private static final String BASE_URL_API = WATU_URL;
    static HttpLoggingInterceptor logging = new HttpLoggingInterceptor();


    public static Retrofit getRetrofitInstance() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60,TimeUnit.SECONDS).build();

        if (retrofit == null) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);
//            httpClient.addInterceptor(new ApiResponseInterceptor());
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL_API).client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();


        }
        return retrofit;
    }
}
