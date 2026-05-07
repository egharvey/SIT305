package com.example.learningbyloosing;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<History> history;

    public HistoryAdapter(List<History> history){
        this.history = history;
    }

    @NonNull
    @Override
    public HistoryAdapter.HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int index){
        History current = history.get(index);
        int green = Color.parseColor("#27850B");
        int red = Color.parseColor("#850B0B");
        if(current.getC1() != current.getS1()) {
            switch (current.getS1()) {
                case "a":
                    holder.tv_a11.setTextColor(red);
                    break;
                case "b":
                    holder.tv_a12.setTextColor(red);
                    break;
                case "c":
                    holder.tv_a13.setTextColor(red);
                    break;
            }
        }

        if(current.getC2() != current.getS2()) {
            switch (current.getS2()) {
                case "a":
                    holder.tv_a21.setTextColor(red);
                    break;
                case "b":
                    holder.tv_a22.setTextColor(red);
                    break;
                case "c":
                    holder.tv_a23.setTextColor(red);
                    break;
            }
        }

        if(current.getC3() != current.getS3()) {
            switch (current.getS3()) {
                case "a":
                    holder.tv_a31.setTextColor(red);
                    break;
                case "b":
                    holder.tv_a32.setTextColor(red);
                    break;
                case "c":
                    holder.tv_a33.setTextColor(red);
                    break;
            }
        }

        switch (current.getC1()){
            case "a":
                holder.tv_a11.setTextColor(green);
                break;
            case "b":
                holder.tv_a12.setTextColor(green);
                break;
            case "c":
                holder.tv_a13.setTextColor(green);
                break;
        }

        switch (current.getC2()){
            case "a":
                holder.tv_a21.setTextColor(green);
                break;
            case "b":
                holder.tv_a22.setTextColor(green);
                break;
            case "c":
                holder.tv_a23.setTextColor(green);
                break;
        }

        switch (current.getC3()){
            case "a":
                holder.tv_a31.setTextColor(green);
                break;
            case "b":
                holder.tv_a32.setTextColor(green);
                break;
            case "c":
                holder.tv_a33.setTextColor(green);
                break;
        }



        holder.tv_q1.setText(current.getQ1());
        holder.tv_q2.setText(current.getQ2());
        holder.tv_q3.setText(current.getQ3());
        holder.tv_a11.setText(current.getA11());
        holder.tv_a12.setText(current.getA12());
        holder.tv_a13.setText(current.getA13());
        holder.tv_a21.setText(current.getA21());
        holder.tv_a22.setText(current.getA22());
        holder.tv_a23.setText(current.getA23());
        holder.tv_a31.setText(current.getA31());
        holder.tv_a32.setText(current.getA32());
        holder.tv_a33.setText(current.getA33());
    }

    @Override
    public int getItemCount() { return history.size(); }

    public static class HistoryViewHolder extends RecyclerView.ViewHolder{
        TextView tv_q1, tv_q2, tv_q3, tv_a11, tv_a12, tv_a13, tv_a21, tv_a22, tv_a23, tv_a31, tv_a32, tv_a33;

        public HistoryViewHolder(@NonNull View itemView){
            super(itemView);
            tv_q1 = itemView.findViewById(R.id.tv_q1);
            tv_q2 = itemView.findViewById(R.id.tv_q2);
            tv_q3 = itemView.findViewById(R.id.tv_q3);
            tv_a11 = itemView.findViewById(R.id.a11);
            tv_a12 = itemView.findViewById(R.id.a12);
            tv_a13 = itemView.findViewById(R.id.a13);
            tv_a21 = itemView.findViewById(R.id.a21);
            tv_a22 = itemView.findViewById(R.id.a22);
            tv_a23 = itemView.findViewById(R.id.a23);
            tv_a31 = itemView.findViewById(R.id.a31);
            tv_a32 = itemView.findViewById(R.id.a32);
            tv_a33 = itemView.findViewById(R.id.a33);
        }
    }
}
