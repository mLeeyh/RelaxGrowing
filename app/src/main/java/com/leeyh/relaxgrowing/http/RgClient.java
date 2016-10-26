package com.leeyh.relaxgrowing.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * Created by leeyh on 16.10.22.
 */

public class RgClient {
    public static final String HOST = "http://gank.io/api/";
    private static RgRetrofit rgRetrofit;
    private static Retrofit retrofit;

    private RgClient() {
    }

    static {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public static RgRetrofit getGankRetrofitInstance() {
        if (rgRetrofit == null) {
            synchronized (RgClient.class) {
                if (rgRetrofit == null) {
                    rgRetrofit = retrofit.create(RgRetrofit.class);
                }
            }
        }
        return rgRetrofit;
    }
}
