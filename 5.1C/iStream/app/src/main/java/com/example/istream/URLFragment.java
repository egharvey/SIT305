package com.example.istream;

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

public class URLFragment extends Fragment {
    private static final String ARG_USERNAME = "username";
    private String username;
    private AppDatabase database;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private List<Account> accountList;

    public URLFragment() {
        // Required empty public constructor
    }

    public static URLFragment newInstance(String username) {
        URLFragment fragment = new URLFragment();
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
        return inflater.inflate(R.layout.fragment_u_r_l, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText et_url = view.findViewById(R.id.et_url);
        Button btn_play = view.findViewById(R.id.btn_play);
        Button btn_save = view.findViewById(R.id.btn_save);
        Button btn_playlist = view.findViewById(R.id.btn_playlist);

        database = AppDatabase.getInstance(getContext());
        loadAccounts();

        btn_play.setOnClickListener(v-> {
            String url = et_url.getText().toString();
            if(!url.isEmpty()){
                ((MainActivity) getActivity()).setCurrentFragment(VideoFragment.newInstance(url, username));
            } else {
                Toast.makeText(getContext(), "Please enter a url", Toast.LENGTH_SHORT).show();
            }
        });

        btn_save.setOnClickListener(v -> {
            loadAccounts();
            String url = et_url.getText().toString();
            if(!url.isEmpty()) {
                for(int i = 0; i < accountList.size(); i++){
                    if(accountList.get(i).getUsername().equals(username)){
                        // 5 spaces are used as a seperator between bookmarks, used in combination with .split()
                        accountList.get(i).setBookmarks(accountList.get(i).getBookmarks() + "     " + url);
                        Toast.makeText(getContext(), "Saved!", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(getContext(), "Please enter a url", Toast.LENGTH_SHORT).show();
            }
        });

        btn_playlist.setOnClickListener(v-> {
            ((MainActivity) getActivity()).setCurrentFragment(PlaylistFragment.newInstance(username));
        });
    }

    private void loadAccounts() {
        executorService.execute(() ->{
            List<Account> accounts = database.accountDao().getAllAccounts();

            getActivity().runOnUiThread(() -> accountList = accounts);
        });
    }
}