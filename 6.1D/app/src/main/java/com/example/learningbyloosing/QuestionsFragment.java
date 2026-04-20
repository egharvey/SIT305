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
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class QuestionsFragment extends Fragment {
    private static final String ARG_USERNAME = "username";
    private static final String ARG_TOPIC = "topic";
    private String username;
    private String topic;
    private ApiService apiService;
    private GeminiApiService geminiApiService;

    private static final String API_KEY = "";

    private static final String BASE_URL = "https://generativelanguage.googleapis.com/";

    public QuestionsFragment() {
        // Required empty public constructor
    }

    public static QuestionsFragment newInstance(String username, String topic) {
        QuestionsFragment fragment = new QuestionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_TOPIC, topic);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME);
            topic = getArguments().getString(ARG_TOPIC);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_questions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView tv_question = view.findViewById(R.id.question);
        TextView tv_qNumber = view.findViewById(R.id.tv_qNumber);
        TextView tv_hint = view.findViewById(R.id.btn_hint);
        Button btn_answer1 = view.findViewById(R.id.btn_answer1);
        Button btn_answer2 = view.findViewById(R.id.btn_answer2);
        Button btn_answer3 = view.findViewById(R.id.btn_answer3);
        Button btn_hint = view.findViewById(R.id.btn_hint);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().readTimeout(10, java.util.concurrent.TimeUnit.MINUTES).build()) // this will set the read timeout for 10mins (IMPORTANT: If not your request will exceed the default read timeout)
                .build();

        apiService = retrofit.create(ApiService.class);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient geminiClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        Retrofit geminiRetro = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(geminiClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        geminiApiService = geminiRetro.create(GeminiApiService.class);

        btn_hint.setOnClickListener(v -> {
            btn_hint.setEnabled(false);
            String prompt  = tv_question.getText().toString();

            GeminiRequest request = new GeminiRequest(prompt);

            geminiApiService.generateContent(API_KEY, request).enqueue(new Callback<GeminiResponse>() {
                @Override
                public void onResponse(@NonNull Call<GeminiResponse> call,
                                       @NonNull Response<GeminiResponse> response) {

                    if (response.isSuccessful() && response.body() != null) {
                        tv_hint.setText("Gemini says:\n\n" + response.body().getFirstText());
                        tv_hint.setVisibility(VISIBLE);
                    } else if (response.code() == 429) {
                        String errorBody = "";
                        try {
                            errorBody = response.errorBody() != null ? response.errorBody().string() : "";
                        } catch (Exception e) { /* ignore */ }

                        if (errorBody.contains("PerDay")) {
                            tv_hint.setText("Daily quota exhausted.\n\nPlease use a new API key from a different Google account.");
                        } else {
                            tv_hint.setText("Failed to get analysis.");
                        }
                    } else {
                        tv_hint.setText("Error " + response.code() + ": " + response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GeminiResponse> call, @NonNull Throwable t) {
                    tv_hint.setText("Network error:\n" + t.getMessage());
                }
            });
        });

        ApiRequest request = new ApiRequest(topic);

        apiService.generateContent(request).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<String> list = response.body().getAllText();
                    List<String> answers = new ArrayList<>();
                    tv_qNumber.setText("Q1");
                    tv_question.setText(list.get(4));
                    btn_answer1.setText(list.get(1));
                    btn_answer2.setText(list.get(2));
                    btn_answer3.setText(list.get(3));

                    btn_answer1.setOnClickListener(v-> {
                        answers.add("a");
                        switch(answers.size()) {
                            case 1:
                                tv_qNumber.setText("Q2");
                                btn_answer1.setText(list.get(6));
                                btn_answer2.setText(list.get(7));
                                btn_answer3.setText(list.get(8));
                                tv_question.setText(list.get(9));
                                break;
                            case 2:
                                tv_qNumber.setText("Q2");
                                btn_answer1.setText(list.get(11));
                                btn_answer2.setText(list.get(12));
                                btn_answer3.setText(list.get(13));
                                tv_question.setText(list.get(14));
                                break;
                            case 3:
                                answers.add("a");
                                String answer = String.join(",", answers);
                                String question = String.join(",", list);
                                ((MainActivity) getActivity()).setCurrentFragment(ResultsFragment.newInstance(username, answer, question));
                                break;
                        }
                    });

                    btn_answer2.setOnClickListener(v-> {
                        answers.add("b");
                        switch(answers.size()) {
                            case 1:
                                tv_qNumber.setText("Q2");
                                btn_answer1.setText(list.get(5));
                                btn_answer2.setText(list.get(6));
                                btn_answer3.setText(list.get(7));
                                tv_question.setText(list.get(8));
                                break;
                            case 2:
                                tv_qNumber.setText("Q2");
                                btn_answer1.setText(list.get(10));
                                btn_answer2.setText(list.get(11));
                                btn_answer3.setText(list.get(12));
                                tv_question.setText(list.get(13));
                                break;
                            case 3:
                                String answer = String.join(",", answers);
                                String question = String.join(",", list);
                                ((MainActivity) getActivity()).setCurrentFragment(ResultsFragment.newInstance(username, answer, question));
                                break;
                        }
                    });

                    btn_answer1.setOnClickListener(v-> {
                        answers.add("c");
                        switch(answers.size()) {
                            case 1:
                                tv_qNumber.setText("Q2");
                                btn_answer1.setText(list.get(5));
                                btn_answer2.setText(list.get(6));
                                btn_answer3.setText(list.get(7));
                                tv_question.setText(list.get(8));
                                break;
                            case 2:
                                tv_qNumber.setText("Q2");
                                btn_answer1.setText(list.get(10));
                                btn_answer2.setText(list.get(11));
                                btn_answer3.setText(list.get(12));
                                tv_question.setText(list.get(13));
                                break;
                            case 3:
                                answers.add("a");
                                String answer = String.join(",", answers);
                                String question = String.join(",", list);
                                ((MainActivity) getActivity()).setCurrentFragment(ResultsFragment.newInstance(username, answer, question));
                                break;
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable throwable) {
                Toast.makeText(getContext(), "Failed to load questions...", Toast.LENGTH_SHORT).show();
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }
}