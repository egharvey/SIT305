package com.example.sportsnews;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class DetailsFragment extends Fragment {
    private static final String ARG_TITLE = "title";
    private static final String ARG_IMAGE = "image";
    private static final String ARG_DESCRIPTION = "description";
    private static final String ARG_BOOKMARK = "bookmark";

    private String title;
    private String image;
    private String description;
    private Boolean bookmark;

    public DetailsFragment() {
        // Required empty public constructor
    }

    public static DetailsFragment newInstance(String title, String image, String description, boolean bookmark) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_IMAGE, image);
        args.putString(ARG_DESCRIPTION, description);
        args.putBoolean(ARG_BOOKMARK, bookmark);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        // Views
        ImageView bookmarkButton = view.findViewById(R.id.btn_saveBookmark);
        ImageView backButton = view.findViewById(R.id.btn_back);
        ImageView headerImage = view.findViewById(R.id.headerImage);
        TextView tv_title = view.findViewById(R.id.storyTitle);
        TextView tv_description = view.findViewById(R.id.storyDescription);
        RecyclerView rv_related = view.findViewById(R.id.rv_related);

        //Here would be where you get the related stories but I cannot find a way to pass a list as an argument
        List<Story> relatedStories = new ArrayList<>();
        relatedStories.add(new Story("https://www.shutterstock.com/image-photo/orange-cat-wearing-glasses-red-600w-2479784503.jpg", "Related Story 1", "This is the first related story", false, new ArrayList<>()));
        relatedStories.add(new Story("https://www.shutterstock.com/image-photo/cute-tabby-silver-grey-young-600w-2253899743.jpg", "Related Story 2", "This is the second related story", false, new ArrayList<>()));
        relatedStories.add(new Story("https://www.shutterstock.com/image-photo/lazy-calico-cat-lying-on-600w-2642935825.jpg", "Related Story 3", "This is the third related story", false, new ArrayList<>()));

        rv_related.setLayoutManager(new LinearLayoutManager(getContext()));
        com.example.sportsnews.NewsAdapter newsAdapter = new NewsAdapter(relatedStories, story -> {
            DetailsFragment detailsFragment = DetailsFragment.newInstance(story.getTitle(), story.getImage(), story.getDescription(), story.getBookmark());
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, detailsFragment)
                    .addToBackStack(null)
                    .commit();
        });
        rv_related.setAdapter(newsAdapter);

        try{
            if (getArguments() != null) {
                title = getArguments().getString(ARG_TITLE);
                image = getArguments().getString(ARG_IMAGE);
                description = getArguments().getString(ARG_DESCRIPTION);
                bookmark = getArguments().getBoolean(ARG_BOOKMARK);
            }

            //Fill in text
            if(bookmark) {
                bookmarkButton.setImageResource(R.drawable.outline_bookmark_added_24);
            }

            tv_title.setText(title);
            tv_description.setText(description);
            Picasso.get().load(image).into(headerImage);

        } catch (Exception e) {
            Toast.makeText(getActivity(), "Failed to load story", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        }

        backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        bookmarkButton.setOnClickListener(v -> {
            //This will not save between instances as persistence has not been implemented
            if(!bookmark){
                bookmarkButton.setImageResource(R.drawable.outline_bookmark_added_24);
                Toast.makeText(getContext(), "Bookmark added", Toast.LENGTH_SHORT).show();
            } else {
                bookmarkButton.setImageResource(R.drawable.outline_bookmark_add_24);
                Toast.makeText(getContext(), "Bookmark removed", Toast.LENGTH_SHORT).show();
            }
            bookmark = !bookmark;
        });
    }
}