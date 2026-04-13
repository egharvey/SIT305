package com.example.sportsnews;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.opengl.Visibility;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    //Views
    private RecyclerView rv_TopStories, rv_news, rv_filtered;
    private TopStoryAdapter topStoryAdapter;
    private NewsAdapter newsAdapter, bookmarkedAdapter;
    private ImageView btn_search, btn_bookmarks, btn_back;
    private EditText et_search;
    TextView tv_topStories, tv_news;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstance){
        super.onViewCreated(view, savedInstance);

        //Views
        rv_TopStories = view.findViewById(R.id.rv_topStories);
        rv_news = view.findViewById(R.id.rv_news);
        rv_filtered = view.findViewById(R.id.rv_bookmarks);
        btn_bookmarks = view.findViewById(R.id.btn_bookmarks);
        btn_search = view.findViewById(R.id.btn_search);
        btn_back = view.findViewById(R.id.btn_back);
        et_search = view.findViewById(R.id.et_search);
        tv_news = view.findViewById(R.id.tv_topStories);
        tv_topStories = view.findViewById(R.id.tv_News);

        //Dummy Data
        Story story1 = new Story("https://www.shutterstock.com/image-photo/canarie-canary-yellow-small-vibrant-600w-2630767857.jpg", "Title 0", "Description 0", false, new ArrayList<Story>());
        Story story2 = new Story("https://www.shutterstock.com/image-photo/cute-tabby-cat-sit-on-600w-2482935965.jpg", "Title 1", "Description 1", true, new ArrayList<Story>());
        Story story3 = new Story("https://images.pexels.com/photos/33539411/pexels-photo-33539411.png", "Title 2", "Description 2", false, new ArrayList<Story>());
        Story story4 = new Story("https://media.istockphoto.com/id/1979951980/photo/my-cat-is-my-best-painting-friend.webp?s=2048x2048&w=is&k=20&c=ecC9hSr5UKx1j4KvyKJy0k3XdkaiOwds4oWkwXIL4c8=", "Title 3", "Description 3", false, new ArrayList<Story>());
        Story story5 = new Story("https://www.shutterstock.com/image-photo/owner-stroking-cute-cat-600w-2586831903.jpg", "Title 4", "Description 4", true, new ArrayList<Story>());
        Story story6 = new Story("https://www.shutterstock.com/image-photo/close-mans-hands-holding-hugging-600w-2678684535.jpg", "Title 5", "Description 5", false, new ArrayList<Story>());
        Story story7 = new Story("https://www.shutterstock.com/image-photo/studio-shot-featuring-stunning-white-600w-2643537809.jpg", "Title 6", "Description 6", true, new ArrayList<Story>());
        Story story8 = new Story("https://www.shutterstock.com/image-photo/group-cute-cats-on-white-600w-2600467023.jpg", "Title 7", "Description 7", false, new ArrayList<Story>());
        Story story9 = new Story("https://www.shutterstock.com/image-photo/lazy-calico-cat-lying-on-600w-2642935825.jpg", "Title 8", "Description 8", true, new ArrayList<Story>());
        Story story10 = new Story("https://www.shutterstock.com/image-photo/orange-cat-wearing-glasses-red-600w-2479784503.jpg", "Title 9", "Description 9", true, new ArrayList<Story>());
        Story story11 = new Story("https://www.shutterstock.com/image-photo/cute-tabby-silver-grey-young-600w-2253899743.jpg", "Title 10", "Description 10", false, new ArrayList<Story>());

        List<Story> AllStories = new ArrayList<>();
        AllStories.add(story1);
        AllStories.add(story2);
        AllStories.add(story3);
        AllStories.add(story4);
        AllStories.add(story5);
        AllStories.add(story6);
        AllStories.add(story7);
        AllStories.add(story8);
        AllStories.add(story9);
        AllStories.add(story10);
        AllStories.add(story11);

        List<Story> TopStories = new ArrayList<>();
        TopStories.add(story1);
        TopStories.add(story3);
        TopStories.add(story8);
        TopStories.add(story11);

        rv_TopStories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        topStoryAdapter = new TopStoryAdapter(TopStories, story -> {
            DetailsFragment detailsFragment = DetailsFragment.newInstance(story.getTitle(), story.getImage(), story.getDescription(), story.getBookmark());
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, detailsFragment)
                    .addToBackStack(null)
                    .commit();
        });
        rv_TopStories.setAdapter(topStoryAdapter);

        rv_news.setLayoutManager(new LinearLayoutManager(getContext()));
        newsAdapter = new NewsAdapter(AllStories, story -> {
            DetailsFragment detailsFragment = DetailsFragment.newInstance(story.getTitle(), story.getImage(), story.getDescription(), story.getBookmark());
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, detailsFragment)
                    .addToBackStack(null)
                    .commit();
        });
        rv_news.setAdapter(newsAdapter);

        btn_bookmarks.setOnClickListener(v -> {
            //Get all bookmarked stories
            List<Story> bookmarked = new ArrayList<>();
            for(int i = 0; i < AllStories.size(); i++){
                if(AllStories.get(i).getBookmark()) {
                    bookmarked.add(AllStories.get(i));
                }
            }

            //Hide elements
            rv_news.setVisibility(GONE);
            rv_TopStories.setVisibility(GONE);
            btn_search.setVisibility(GONE);
            btn_bookmarks.setVisibility(GONE);
            tv_news.setVisibility(GONE);
            tv_topStories.setText("Bookmarks");

            //Show elements
            btn_back.setVisibility(VISIBLE);

            //Show bookmarks in recycler view
            rv_filtered.setLayoutManager(new LinearLayoutManager(getContext()));
            bookmarkedAdapter = new NewsAdapter(bookmarked, story -> {
                DetailsFragment detailsFragment = DetailsFragment.newInstance(story.getTitle(), story.getImage(), story.getDescription(), story.getBookmark());
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, detailsFragment)
                        .addToBackStack(null)
                        .commit();
            });
            rv_filtered.setAdapter(bookmarkedAdapter);
            rv_filtered.setVisibility(VISIBLE);
        });

        btn_search.setOnClickListener(v -> {
            //Hide elements
            rv_news.setVisibility(GONE);
            rv_TopStories.setVisibility(GONE);
            btn_search.setVisibility(GONE);
            btn_bookmarks.setVisibility(GONE);
            tv_news.setVisibility(GONE);
            tv_topStories.setVisibility(GONE);

            //Show elements
            btn_back.setVisibility(VISIBLE);
            et_search.setVisibility(VISIBLE);
            rv_filtered.setVisibility(VISIBLE);
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // this function is called before text is edited
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // this function is called when text is edited
                //Filter news by search term
                List<Story> searched = new ArrayList<>();
                for(int i = 0; i < AllStories.size(); i++){
                    //Lengthy if statement checks if search term is in the title, regardless of character case
                    if(AllStories.get(i).getTitle().toLowerCase().contains(s.toString().toLowerCase())) {
                        searched.add(AllStories.get(i));
                    }
                }

                //Show search results in recycler view
                rv_filtered.setLayoutManager(new LinearLayoutManager(getContext()));
                bookmarkedAdapter = new NewsAdapter(searched, story -> {
                    DetailsFragment detailsFragment = DetailsFragment.newInstance(story.getTitle(), story.getImage(), story.getDescription(), story.getBookmark());
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame_layout, detailsFragment)
                            .addToBackStack(null)
                            .commit();
                });
                rv_filtered.setAdapter(bookmarkedAdapter);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // this function is called after text is edited
            }
        };

        et_search.addTextChangedListener(textWatcher);

        btn_back.setOnClickListener(v -> {
            btn_back.setVisibility(GONE);
            rv_filtered.setVisibility(GONE);
            et_search.setText("");
            et_search.setVisibility(GONE);

            tv_topStories.setText("Top Stories");
            tv_topStories.setVisibility(VISIBLE);
            tv_news.setVisibility(VISIBLE);
            rv_TopStories.setVisibility(VISIBLE);
            rv_news.setVisibility(VISIBLE);
            btn_search.setVisibility(VISIBLE);
            btn_bookmarks.setVisibility(VISIBLE);
        });
    }
}