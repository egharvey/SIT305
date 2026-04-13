package com.example.sportsnews;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.squareup.picasso.Picasso;

public class TopStoryAdapter extends RecyclerView.Adapter<TopStoryAdapter.TopStoryViewHolder> {
    private List<Story> stories;
    private final OnTopStoryClickListener listener;

    public interface OnTopStoryClickListener {
        void onViewClick(Story story);
    }

    public TopStoryAdapter(List<Story> stories, OnTopStoryClickListener listener){
        this.stories = stories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TopStoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_top_story, parent, false);
        return new TopStoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopStoryViewHolder holder, int index){
        Story story = stories.get(index);

        //FILL OUT DETAILS FRAGMENT
        Picasso.get().load(story.getImage()).into(holder.imageView);

        holder.imageView.setOnClickListener(v -> listener.onViewClick(story));
    }

    @Override
    public int getItemCount() { return stories.size(); }

    public static class TopStoryViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;

        public TopStoryViewHolder(@NonNull View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
