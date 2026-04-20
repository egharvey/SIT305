package com.example.learningbyloosing;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {
    @POST("getQuiz?topic=")
    Call<ApiResponse> generateContent(
            @Body ApiRequest request
    );
}
