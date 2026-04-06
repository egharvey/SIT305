package com.example.personalplanner;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DetailsFragment extends Fragment {
    private AppDatabase database;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private static final String ARG_TITLE = "title";
    private static final String ARG_LOCATION = "location";
    private static final String ARG_CATEGORY = "category";
    //ADD TIME
    private static final String ARG_DATETIME = "time";
    private static final String ARG_ID = "id";

    public static DetailsFragment newInstance(String title, String location, String category, Long time, int id) {
        DetailsFragment fragment = new DetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_LOCATION, location);
        args.putString(ARG_CATEGORY, category);
        args.putLong(ARG_DATETIME, time);
        args.putInt(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        TextView et_title = view.findViewById(R.id.et_title);
        TextView et_location = view.findViewById(R.id.et_location);
        TextView et_date = view.findViewById(R.id.et_date);
        TextView et_time = view.findViewById(R.id.et_time);
        Spinner sp_category = view.findViewById(R.id.sp_category);
        Button btn_back = view.findViewById(R.id.btn_back);
        Button btn_delete = view.findViewById(R.id.btn_delete);
        Button btn_submit = view.findViewById(R.id.btn_submit);

        try {
            if (getArguments() != null) {
                String title = getArguments().getString(ARG_TITLE);
                String location = getArguments().getString(ARG_LOCATION);
                String category = getArguments().getString(ARG_CATEGORY);
                Long dateTime = getArguments().getLong(ARG_DATETIME);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                String formattedDateTime = dateFormat.format(dateTime);
                String[] splitDateTime = formattedDateTime.split(" ");

                et_title.setText(title);
                et_location.setText(location);
                et_date.setText(splitDateTime[0]);
                et_time.setText(splitDateTime[1]);

                switch (category) {
                    case "Work":
                        sp_category.setSelection(0);
                        break;
                    case "Personal":
                        sp_category.setSelection(1);
                        break;
                    case "Travel":
                        sp_category.setSelection(2);
                        break;
                    default: //Otherwise assume Misc
                        sp_category.setSelection(3);
                        break;
                }
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Failed to load event", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        }

        btn_back.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        btn_delete.setOnClickListener(v -> {
            deleteEvent(getArguments().getInt(ARG_ID));
        });

        btn_submit.setOnClickListener(v-> {
            String title = et_title.getText().toString();
            String location = et_location.getText().toString();
            String category = sp_category.getSelectedItem().toString();
            String date = et_date.getText().toString();
            String[] dayMonthYear = date.split("/");
            String time = et_time.getText().toString();
            String[] hourMinute = time.split(":");

            boolean valid = true;
            long ldt = 0l;

            //Validation
            if (title == null || title.isEmpty())
            {
                Toast.makeText(getActivity(), "Enter event title", Toast.LENGTH_SHORT).show();
                valid = false;
            }

            if (location == null || location.isEmpty()) {
                Toast.makeText(getActivity(), "Enter event location", Toast.LENGTH_SHORT).show();
                valid = false;
            }

            if (date == null || date.isEmpty() || dayMonthYear.length != 3) {
                Toast.makeText(getActivity(), "Enter a valid date", Toast.LENGTH_SHORT).show();
                valid = false;
            } else {
                if (time == null || time.isEmpty() || hourMinute.length != 2) {
                    Toast.makeText(getActivity(), "Enter a valid time: " + time + " " + hourMinute.length, Toast.LENGTH_SHORT).show();
                    valid = false;
                } else {
                    if (Integer.parseInt(dayMonthYear[0]) > 31 || Integer.parseInt(dayMonthYear[1]) > 12 || Integer.parseInt(hourMinute[0]) > 24 || Integer.parseInt(hourMinute[1]) > 59) {
                        Toast.makeText(getActivity(), "Enter a valid date and time", Toast.LENGTH_SHORT).show();
                        valid = false;
                    } else {
                        try {
                            Calendar edt = Calendar.getInstance();
                            edt.set(Calendar.YEAR, Integer.parseInt(dayMonthYear[2]));
                            edt.set(Calendar.MONTH, Integer.parseInt(dayMonthYear[1]));
                            edt.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayMonthYear[0]));
                            edt.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourMinute[0]));
                            edt.set(Calendar.MINUTE, Integer.parseInt(hourMinute[1]));
                            Date finalDate = edt.getTime();
                            if (finalDate.before(new Date())) throw new Exception();
                            ldt = finalDate.getTime();
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Enter a valid date and time", Toast.LENGTH_SHORT).show();
                            valid = false;
                        }
                    }
                }
            }
            if (valid){
                try {
                    updateEvent(new Event(title, location, category, ldt));
                    Toast.makeText(getActivity(), "Added", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    // Create the object of AlertDialog Builder class
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    // Set the message show for the Alert time
                    builder.setMessage(e.getMessage());

                    // Set Alert Title
                    builder.setTitle("Error !");

                    // Set Cancelable false for when the user clicks
                    // on the outside the Dialog Box then it will remain show
                    builder.setCancelable(false);

                    // Set the positive button with yes name Lambda
                    // OnClickListener method is use of DialogInterface interface.
                    builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {

                    });

                    // Set the Negative button with No name Lambda
                    // OnClickListener method is use of DialogInterface interface.
                    builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {

                        // If user click no then dialog box is canceled.
                        dialog.cancel();
                    });

                    // Create the Alert dialog
                    AlertDialog alertDialog = builder.create();

                    // Show the Alert Dialog box
                    alertDialog.show();
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateEvent(Event event){
        executorService.execute(() -> {
            database.eventDao().insert(event);
        });
    }

    private void deleteEvent(int id){
        executorService.execute(() -> {
            database.eventDao().deleteEvent(id);
        });
    }
}