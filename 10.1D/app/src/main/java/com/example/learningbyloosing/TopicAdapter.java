package com.example.learningbyloosing;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.TopicViewHolder> {
    private List<String> topics;
    private final OnBookmarkClickListener listener;

    public interface OnBookmarkClickListener {
        void onViewClick(String bookmark);
    }

    public TopicAdapter(List<String> topics, OnBookmarkClickListener listener){
        this.topics = topics;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_topic, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int index){
        String topic = topics.get(index);
        holder.tv_topic.setText(topic);
        holder.tv_topic.setOnClickListener(v -> listener.onViewClick(topic));
    }

    @Override
    public int getItemCount() { return topics.size(); }

    public static class TopicViewHolder extends RecyclerView.ViewHolder{
        TextView tv_topic;

        public TopicViewHolder(@NonNull View itemView){
            super(itemView);
            tv_topic = itemView.findViewById(R.id.tv_topic);
        }
    }
}
