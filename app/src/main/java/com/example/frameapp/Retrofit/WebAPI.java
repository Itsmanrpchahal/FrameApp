package com.example.frameapp.Retrofit;

import com.example.americloud.Retrofit.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WebAPI {
    public static String BASE_URL = "https://survey.gpcoders.dev/api/v1/";
    public static WebAPI mInstance;
    Retrofit retrofit;
    public static ApiInterface apiInterface;
    public WebAPI() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(180, TimeUnit.SECONDS)
                .writeTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();

        apiInterface = retrofit.create(ApiInterface.class);
    }


    public static synchronized WebAPI getInstance() {
        if (mInstance == null) {
            mInstance = new WebAPI();
        }
        return mInstance;
    }

    public ApiInterface getApi() {
        return retrofit.create(ApiInterface.class);
    }
}