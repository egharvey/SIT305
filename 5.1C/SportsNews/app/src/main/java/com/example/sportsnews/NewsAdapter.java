package com.example.sportsnews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private List<Story> stories;
    private final OnNewsClickListener listener;

    public interface OnNewsClickListener {
        void onViewClick(Story story);
    }

    public NewsAdapter(List<Story> stories, OnNewsClickListener listener){
        this.stories = stories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int index){
        Story story = stories.get(index);

        //FILL OUT DETAILS FRAGMENT
        Picasso.get().load(story.getImage()).into(holder.imageView);
        holder.tv_title.setText(story.getTitle());
        holder.tv_description.setText(story.getDescription());

        holder.tv_title.setOnClickListener(v -> listener.onViewClick(story));
        holder.tv_description.setOnClickListener(v -> listener.onViewClick(story));
        holder.imageView.setOnClickListener(v -> listener.onViewClick(story));
    }

    @Override
    public int getItemCount() { return stories.size(); }

    public static class NewsViewHolder extends RecyclerView.ViewHolder{
        TextView tv_title, tv_description;
        ImageView imageView;

        public NewsViewHolder(@NonNull View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_description = itemView.findViewById(R.id.tv_description);
        }
    }
}
