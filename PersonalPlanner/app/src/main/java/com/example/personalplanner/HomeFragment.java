package com.example.personalplanner;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventAdapter adapter;
    private List<Event> eventList;
    private AppDatabase database;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstance){
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstance){
        super.onViewCreated(view, savedInstance);

        recyclerView = view.findViewById(R.id.recycler);
        TextView textView = view.findViewById(R.id.textView);
        textView.setVisibility(GONE);

        database = AppDatabase.getInstance(getContext());

        //Real data
        try {
            loadEvents();
            if(eventList.isEmpty()){
                throw new Exception();
            }
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            adapter = new EventAdapter(eventList, event -> {
                DetailsFragment detailsFragment = DetailsFragment.newInstance(
                        event.getTitle(), event.getLocation(), event.getCategory(), event.getTime(), event.getId());
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frame_layout, detailsFragment)
                        .addToBackStack(null)
                        .commit();
            });
            recyclerView.setAdapter(adapter);

            recyclerView.setVisibility(VISIBLE);
            textView.setVisibility(GONE);
        } catch (Exception e) {
            recyclerView.setVisibility(GONE);
            textView.setVisibility(VISIBLE);
        }

        //Create recycler view
    }

    private void loadEvents() {
        executorService.execute(() ->{
            List<Event> events = database.eventDao().getAllEvents();

            getActivity().runOnUiThread(() -> eventList = events);
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        loadEvents();
    }
}