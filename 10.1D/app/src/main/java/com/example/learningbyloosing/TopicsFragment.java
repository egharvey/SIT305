package com.example.learningbyloosing;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TopicsFragment extends Fragment {

    private static final String ARG_USERNAME = "username";

    private AppDatabase database;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Account account;
    private List<String> topicsList;
    private String username, topics;

    public TopicsFragment() {
        // Required empty public constructor
    }

    public static TopicsFragment newInstance(String username) {
        TopicsFragment fragment = new TopicsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_topics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = AppDatabase.getInstance(getContext());
        loadAccount();

        Button btn_add = view.findViewById(R.id.btn_add);
        Button btn_next = view.findViewById(R.id.btn_next);
        EditText et_topic =  view.findViewById(R.id.et_topic);

        btn_add.setOnClickListener(v-> {
            String topic = et_topic.getText().toString();
            try{
                if(!topic.isEmpty()) {
                    account.setTopics(account.getTopics() + " " + topic);
                    Toast.makeText(getContext(), "Topic Added!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                if(!topic.isEmpty()) account.setTopics(topic);
                Toast.makeText(getContext(), "Topic Added!", Toast.LENGTH_SHORT).show();
            }
        });

        btn_next.setOnClickListener(v -> {
            try{
                if(account.getTopics().isEmpty()) Toast.makeText(getContext(), "You need a topic!", Toast.LENGTH_SHORT).show();
                else ((MainActivity) getActivity()).setCurrentFragment(YourTopicsFragment.newInstance(username));
            } catch (Exception e){
                Toast.makeText(getContext(), "You need a topic!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAccount() {
        executorService.execute(() ->{
            Account found = database.accountDao().getAccountByUsername(username);

            getActivity().runOnUiThread(() -> account = found);
        });
    }
}