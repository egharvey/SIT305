package com.example.findwhatslost;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ConcatAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder>{
    private Context context;
    private List<Post> posts;
    private final OnPostClickListener listener;

    public interface OnPostClickListener {
        void onViewClick(Post post);
    }

    public PostAdapter(Context context, List<Post> posts, OnPostClickListener listener){
        this.context = context;
        this.posts = posts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int index){
        Post post = posts.get(index);
        holder.tv_name.setText(post.getName());
        holder.tv_description.setText(post.getDescription());
        holder.card.setOnClickListener(v -> listener.onViewClick(post));
    }

    @Override
    public int getItemCount() { return posts.size(); }

    public static class PostViewHolder extends RecyclerView.ViewHolder{
        TextView tv_name;
        TextView tv_description;
        CardView card;

        public PostViewHolder(@NonNull View itemView){
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_description = itemView.findViewById(R.id.tv_description);
            card = itemView.findViewById(R.id.card);
        }
    }
}
