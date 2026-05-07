package com.example.learningbyloosing;

import static android.view.View.VISIBLE;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultsFragment extends Fragment {
    private static final String ARG_USERNAME = "username";
    private static final String ARG_ANSWERS = "answers";
    private static final String ARG_QUESTIONS = "questions";
    private String username;
    private List<String> answers;
    private List<String> questions;

    private GeminiApiService apiService;
    private static final String API_KEY = "";

    private static final String BASE_URL = "https://generativelanguage.googleapis.com/";

    public ResultsFragment() {
        // Required empty public constructor
    }

    public static ResultsFragment newInstance(String username, String answers, String questions) {
        ResultsFragment fragment = new ResultsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_ANSWERS, answers);
        args.putString(ARG_QUESTIONS, questions);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME);
            answers = Arrays.asList(getArguments().getString(ARG_ANSWERS).split(","));
            questions = Arrays.asList(getArguments().getString(ARG_QUESTIONS).split(","));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_results, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tv_q1 = view.findViewById(R.id.tv_q1);
        TextView tv_q2 = view.findViewById(R.id.tv_q2);
        TextView tv_q3 = view.findViewById(R.id.tv_q3);
        TextView tv_response = view.findViewById(R.id.tv_response);
        Button btn_analyse = view.findViewById(R.id.btn_analyse);
        Button btn_again = view.findViewById(R.id.btn_again);
        boolean q1;
        boolean q2;
        boolean q3;

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(GeminiApiService.class);

        if(answers.get(0).equals(questions.get(0))){
            q1 = true;
            tv_q1.setText("Q1: " + questions.get(0) + "✔");
        } else {
            tv_q1.setText("Q1: " + questions.get(0) + "❌");
            q1 = false;
        }

        if(answers.get(1).equals(questions.get(5))){
            q2 = true;
            tv_q2.setText("Q1: " + questions.get(5) + "✔");
        } else {
            tv_q2.setText("Q1: " + questions.get(5) + "❌");
            q2 = false;
        }

        if(answers.get(2).equals(questions.get(10))){
            q3 = true;
            tv_q3.setText("Q1: " + questions.get(10) + "✔");
        } else {
            tv_q3.setText("Q1: " + questions.get(10) + "❌");
            q3 = false;
        }

        btn_again.setOnClickListener(v -> {
            ((MainActivity) getActivity()).setCurrentFragment(YourTopicsFragment.newInstance(username));
        });

        btn_analyse.setOnClickListener(v -> {
            btn_analyse.setEnabled(false);
            String prompt = "Analyse my results on these questions: ";
            if(q1) prompt += " " + questions.get(4) + " I got this correct.";
            else prompt += " " + questions.get(4) + " I got this wrong.";
            if(q2) prompt += " " + questions.get(9) + " I got this correct.";
            else prompt += " " + questions.get(9) + " I got this wrong.";
            if(q3) prompt += " " + questions.get(14) + " I got this correct.";
            else prompt += " " + questions.get(4) + " I got this wrong.";

            GeminiRequest request = new GeminiRequest(prompt);

            apiService.generateContent(API_KEY, request).enqueue(new Callback<GeminiResponse>() {
                @Override
                public void onResponse(@NonNull Call<GeminiResponse> call,
                                       @NonNull Response<GeminiResponse> response) {

                    if (response.isSuccessful() && response.body() != null) {
                        tv_response.setText("Gemini says:\n\n" + response.body().getFirstText());
                        tv_response.setVisibility(VISIBLE);
                    } else if (response.code() == 429) {
                        String errorBody = "";
                        try {
                            errorBody = response.errorBody() != null ? response.errorBody().string() : "";
                        } catch (Exception e) { /* ignore */ }

                        if (errorBody.contains("PerDay")) {
                            tv_response.setText("Daily quota exhausted.\n\nPlease use a new API key from a different Google account.");
                        } else {
                            tv_response.setText("Failed to get analysis.");
                        }
                    } else {
                        tv_response.setText("Error " + response.code() + ": " + response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GeminiResponse> call, @NonNull Throwable t) {
                    tv_response.setText("Network error:\n" + t.getMessage());
                }
            });
        });
    }
}