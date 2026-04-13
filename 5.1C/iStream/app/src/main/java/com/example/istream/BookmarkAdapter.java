package com.example.istream;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder> {
    private List<String> bookmarks;
    private final OnBookmarkClickListener listener;

    public interface OnBookmarkClickListener {
        void onViewClick(String bookmark);
    }

    public BookmarkAdapter(List<String> bookmarks, OnBookmarkClickListener listener){
        this.bookmarks = bookmarks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_url, parent, false);
        return new BookmarkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkViewHolder holder, int index){
        String url = bookmarks.get(index);
        holder.tv_url.setText(url);
        holder.tv_url.setOnClickListener(v -> listener.onViewClick(url));
    }

    @Override
    public int getItemCount() { return bookmarks.size(); }

    public static class BookmarkViewHolder extends RecyclerView.ViewHolder{
        TextView tv_url;

        public BookmarkViewHolder(@NonNull View itemView){
            super(itemView);
            tv_url = itemView.findViewById(R.id.tv_url);
        }
    }
}
