package com.example.talktoawall;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_USER = 1;
    private static final int VIEW_TYPE_BOT = 2;
    private static final String CURRENT_DATE = new SimpleDateFormat("MM/dd/yyyy",Locale.getDefault()).format(new Date());

    private Context context;
    private List<Message> messages;

    public MessageAdapter(Context context, List<Message> messages){
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position){
        return messages.get(position).getIsBot() ? VIEW_TYPE_BOT : VIEW_TYPE_USER;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        if (viewType == VIEW_TYPE_USER){
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.item_user_msg, parent, false);
            return new UserViewHolder(view);
        } else {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.item_bot_msg, parent, false);
            return new BotViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position){
        Message msg = messages.get(position);

        //Show shortened timestamp for messages sent today
        String timestamp = msg.getTimestamp();
        String[] split = timestamp.split(" ");
        if(split[0].equals(CURRENT_DATE)) timestamp = split[1];

        if (holder instanceof UserViewHolder){
            ((UserViewHolder) holder).tv_message.setText(msg.getMessage());
            ((UserViewHolder) holder).tv_timestamp.setText(timestamp);
        } else {
            ((BotViewHolder) holder).tv_message.setText(msg.getMessage());
            ((BotViewHolder) holder).tv_timestamp.setText(timestamp);
        }
    }

    @Override
    public int getItemCount() { return messages.size(); }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tv_message;
        TextView tv_timestamp;
        public UserViewHolder(@NonNull View itemView){
            super(itemView);
            tv_message = itemView.findViewById(R.id.tv_message);
            tv_timestamp = itemView.findViewById(R.id.tv_timestamp);
        }
    }

    public static class BotViewHolder extends RecyclerView.ViewHolder {
        TextView tv_message;
        TextView tv_timestamp;
        public BotViewHolder(@NonNull View itemView){
            super(itemView);
            tv_message = itemView.findViewById(R.id.tv_message);
            tv_timestamp = itemView.findViewById(R.id.tv_timestamp);
        }
    }
}
