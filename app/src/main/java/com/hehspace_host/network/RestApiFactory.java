package com.hehspace_host.network;


import android.util.Log;


import com.hehspace_host.BuildConfig;
import com.hehspace_host.util.App;
import com.hehspace_host.util.SessionManager;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public final class RestApiFactory {
    public static String BASE_URL = "https://hehspace.com/";
    public static RestApi create() {
        SessionManager sessionManager = SessionManager.getInstance(App.singleton);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        HttpLoggingInterceptor.Level logLevel = HttpLoggingInterceptor.Level.BODY;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(logLevel);
        httpClient.addInterceptor((Interceptor) (new Interceptor() {
            public final Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer "+sessionManager.getAuthToken()).build();
                if (BuildConfig.DEBUG)
                    Log.e("authToken", sessionManager.getAuthToken());
                return chain.proceed(request);
            }
        })).addInterceptor(interceptor);
        httpClient.connectTimeout(2, TimeUnit.MINUTES);
        httpClient.readTimeout(2, TimeUnit.MINUTES);
        httpClient.writeTimeout(2, TimeUnit.MINUTES);

        Retrofit retrofit = (new Retrofit.Builder()).baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient.build())
                .build();
        return retrofit.create(RestApi.class);
    }

}
