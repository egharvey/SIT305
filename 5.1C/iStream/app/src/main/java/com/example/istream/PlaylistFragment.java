package com.example.istream;

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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlaylistFragment extends Fragment {
    private static final String ARG_USERNAME= "username";
    private String username, bookmarks;
    private List<String> bookmarksList;
    private AppDatabase database;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Account account;
    private BookmarkAdapter adapter;

    public PlaylistFragment() {
        // Required empty public constructor
    }

    public static PlaylistFragment newInstance(String username) {
        PlaylistFragment fragment = new PlaylistFragment();
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
        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView btn_back = view.findViewById(R.id.btn_back);
        RecyclerView rv_bookmarks = view.findViewById(R.id.rv_bookmarks);

        database = AppDatabase.getInstance(getContext());
        loadAccount();
        
        bookmarks = account.getBookmarks();
        String[] bookmarkArray =  bookmarks.split(" ");
        bookmarksList = new ArrayList<>();
        bookmarksList.addAll(Arrays.asList(bookmarkArray));

        rv_bookmarks.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BookmarkAdapter(bookmarksList, bookmark -> {
            VideoFragment videoFragment = VideoFragment.newInstance(
                    bookmark, username);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, videoFragment)
                    .addToBackStack(null)
                    .commit();
        });
        rv_bookmarks.setAdapter(adapter);

        btn_back.setOnClickListener(v-> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    private void loadAccount() {
        executorService.execute(() ->{
            Account found = database.accountDao().getAccountbyUsername(username);

            getActivity().runOnUiThread(() -> account = found);
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        loadAccount();
    }
}