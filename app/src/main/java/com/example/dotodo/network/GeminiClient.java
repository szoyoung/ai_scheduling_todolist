package com.example.dotodo.network;

import android.content.Context;
import com.example.dotodo.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeminiClient {
    private static final String BASE_URL = "https://generativelanguage.googleapis.com/";
    private static GeminiClient instance;
    private final GeminiApiService apiService;
    private final String apiKey;

    private GeminiClient(Context context) {
        this.apiKey = BuildConfig.GEMINI_API_KEY;

        // 로깅 인터셉터 설정
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)  // 연결 타임아웃
                .writeTimeout(30, TimeUnit.SECONDS)    // 쓰기 타임아웃
                .readTimeout(30, TimeUnit.SECONDS)     // 읽기 타임아웃
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(GeminiApiService.class);
    }

    public static synchronized GeminiClient getInstance(Context context) {
        if (instance == null) {
            instance = new GeminiClient(context);
        }
        return instance;
    }

    public GeminiApiService getApiService() {
        return apiService;
    }

    public String getApiKey() {
        return apiKey;
    }
}