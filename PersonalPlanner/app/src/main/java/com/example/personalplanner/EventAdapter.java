package com.example.personalplanner;

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


public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private List<Event> eventList;
    private final OnEventClickListener listener;

    public interface OnEventClickListener {
        void onViewClick(Event event);
    }

    public EventAdapter(List<Event> eventList, OnEventClickListener listener){
        this.eventList = eventList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int index){
        Event event = eventList.get(index);

        //FILL OUT DETAILS FRAGMENT
        holder.tv_title.setText(event.getTitle());
        holder.tv_location.setText(event.getLocation());
        holder.tv_category.setText(event.getCategory());
        Date time = new Date(event.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        holder.tv_time.setText(dateFormat.format(time));
    }

    @Override
    public int getItemCount() { return eventList.size(); }

    public static class EventViewHolder extends RecyclerView.ViewHolder{
        TextView tv_title, tv_location, tv_category, tv_time;

        public EventViewHolder(@NonNull View itemView){
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_location = itemView.findViewById(R.id.tv_location);
            tv_category = itemView.findViewById(R.id.tv_category);
            tv_time = itemView.findViewById(R.id.tv_time);
        }
    }
}
