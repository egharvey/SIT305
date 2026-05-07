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
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {
    private static final String ARG_PARAM1 = "username";

    private AppDatabase database;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private String username;
    private List<History> history = new ArrayList<>();
    private HistoryAdapter adapter;

    public HistoryFragment() {}

    public static HistoryFragment newInstance(String username) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = AppDatabase.getInstance(getContext());

        if (getArguments() != null) {
            username = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loadHistory();
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btn_back = view.findViewById(R.id.btn_back);
        ImageView btn_reload = view.findViewById(R.id.btn_reload);
        RecyclerView rv_history = view.findViewById(R.id.rv_history);

        try{
            rv_history.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new HistoryAdapter(history);
            rv_history.setAdapter(adapter);
        } catch (Exception e){
            Toast.makeText(getContext(), "Failed to load history", Toast.LENGTH_SHORT).show();
        }

        btn_back.setOnClickListener(v->{
            ((MainActivity) getActivity()).setCurrentFragment(ProfileFragment.newInstance(username));
        });

        btn_reload.setOnClickListener(v->{
            try{
                rv_history.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter = new HistoryAdapter(history);
                rv_history.setAdapter(adapter);
            } catch (Exception e){
                Toast.makeText(getContext(), "Failed to load history", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadHistory() {
        executorService.execute(() ->{
            List<History> found = database.historyDao().getUserHistory(username);
            getActivity().runOnUiThread(() -> history = found);
            });

    }
}