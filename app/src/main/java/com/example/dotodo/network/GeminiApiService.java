package com.example.dotodo.network;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface GeminiApiService {
    @POST("v1/models/gemini-pro:generateContent")
    Call<GenerateContentResponse> generateContent(
            @Header("x-goog-api-key") String apiKey,
            @Body GenerateContentRequest request
    );
}