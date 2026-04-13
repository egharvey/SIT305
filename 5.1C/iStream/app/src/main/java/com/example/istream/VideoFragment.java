package com.example.istream;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VideoFragment extends Fragment {
    private static final String ARG_URL= "url";
    private static final String ARG_USERNAME= "username";

    private String url;
    private String username;

    private AppDatabase database;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private List<Account> accountList;

    public VideoFragment() {
        // Required empty public constructor
    }

    public static VideoFragment newInstance(String url, String username) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        args.putString(ARG_USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString(ARG_URL);
            username = getArguments().getString(ARG_USERNAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        WebView wv_video = view.findViewById(R.id.wv_video);
        ImageView btn_bookmark = view.findViewById(R.id.btn_bookmark);
        ImageView btn_back = view.findViewById(R.id.btn_back);

        database = AppDatabase.getInstance(getContext());

        String completeURL = "<iframe width=\"100%\" height=\"100%\" src=\"" + url + "\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
        wv_video.loadUrl(completeURL);
        wv_video.getSettings().setJavaScriptEnabled(true);
        wv_video.setWebChromeClient(new WebChromeClient());

        btn_back.setOnClickListener(v->{
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        btn_bookmark.setOnClickListener(v -> {
            loadAccounts();
            for(int i = 0; i < accountList.size(); i++){
                if(accountList.get(i).getUsername().equals(username)){
                    // 5 spaces are used as a seperator between bookmarks, used in combination with .split()
                    accountList.get(i).setBookmarks(accountList.get(i).getBookmarks() + "     " + url);
                    btn_bookmark.setImageResource(R.drawable.outline_bookmark_added_24);
                    Toast.makeText(getContext(), "Saved!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadAccounts() {
        executorService.execute(() ->{
            List<Account> accounts = database.accountDao().getAllAccounts();

            getActivity().runOnUiThread(() -> accountList = accounts);
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        loadAccounts();
    }
}