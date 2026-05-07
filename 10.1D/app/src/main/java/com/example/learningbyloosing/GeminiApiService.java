package com.example.learningbyloosing;

import retrofit2.Call;
import retrofit2.http.*;

public interface GeminiApiService {

    //    @POST("v1beta/models/gemini-2.0-flash:generateContent")
    @POST("v1beta/models/gemini-2.0-flash-lite:generateContent")
    Call<GeminiResponse> generateContent(
            @Query("key") String apiKey,
            @Body GeminiRequest request
    );
}
