package com.example.learningbyloosing;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class YourTopicsFragment extends Fragment {

    private static final String ARG_USERNAME = "username";

    private AppDatabase database;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Account account;
    private List<String> topicsList;
    private String username, topics;
    private TopicAdapter adapter;

    public YourTopicsFragment() {
        // Required empty public constructor
    }

    public static YourTopicsFragment newInstance(String username) {
        YourTopicsFragment fragment = new YourTopicsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = AppDatabase.getInstance(getContext());
        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        loadAccount();
        return inflater.inflate(R.layout.fragment_your_topics, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btn_add = view.findViewById(R.id.btn_add);
        Button btn_profile = view.findViewById(R.id.btn_profile);
        EditText et_newTopic =  view.findViewById(R.id.et_newTopic);
        RecyclerView rv_topics = view.findViewById(R.id.rv_topics);
        ImageView btn_reload = view.findViewById(R.id.btn_reload);

        try{
            topics = account.getTopics();
            String[] bookmarkArray =  topics.split(" ");
            topicsList = new ArrayList<>();
            topicsList.addAll(Arrays.asList(bookmarkArray));

            rv_topics.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new TopicAdapter(topicsList, topic -> {
                QuestionsFragment questionsFragment = QuestionsFragment.newInstance(
                        topic, username);
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, questionsFragment)
                        .addToBackStack(null)
                        .commit();
            });
            rv_topics.setAdapter(adapter);
        } catch (Exception e) {
            System.out.println("Error loading topics");
        }

        btn_reload.setOnClickListener(v -> {
            topics = account.getTopics();
            String[] bookmarkArray =  topics.split(" ");
            topicsList = new ArrayList<>();
            topicsList.addAll(Arrays.asList(bookmarkArray));

            rv_topics.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new TopicAdapter(topicsList, topic -> {
                QuestionsFragment questionsFragment = QuestionsFragment.newInstance(
                        topic, username);
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, questionsFragment)
                        .addToBackStack(null)
                        .commit();
            });
            rv_topics.setAdapter(adapter);
        });

        btn_add.setOnClickListener(v-> {
            String topic = et_newTopic.getText().toString();
            if(!topic.isEmpty()) {
                topics = account.getTopics() + " " + topic;
                updateTopics();
                Toast.makeText(getContext(), "Topic Added", Toast.LENGTH_SHORT).show();
            }
            loadAccount();
        });

        btn_profile.setOnClickListener(v->{
            ((MainActivity) getActivity()).setCurrentFragment(ProfileFragment.newInstance(username));
        });
    }

    private void loadAccount() {
        executorService.execute(() ->{
            Account found = database.accountDao().getAccountByUsername(username);
            System.out.println("Loading...");
            getActivity().runOnUiThread(() -> account = found);
        });
    }

    private void updateTopics() {
        executorService.execute(() ->{
            database.accountDao().updateTopics(username, topics);
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        loadAccount();
    }
}