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

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFragment extends Fragment {

    private AppDatabase database;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public AddFragment() {
        // Required empty public constructor
    }

    public static AddFragment newInstance() {
        AddFragment fragment = new AddFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        database = AppDatabase.getInstance(getContext());

        TextView et_title = view.findViewById(R.id.et_title);
        TextView et_location = view.findViewById(R.id.et_location);
        TextView et_date = view.findViewById(R.id.et_date);
        TextView et_time = view.findViewById(R.id.et_time);
        Spinner sp_category = view.findViewById(R.id.sp_category);
        Button btn_submit = view.findViewById(R.id.btn_submit);

        btn_submit.setOnClickListener(v -> {
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

            //Check validation passed
            if (valid){
                try {
                    addEvent(new Event(title, location, category, ldt));
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

    private void addEvent(Event event){
        executorService.execute(() -> {
            database.eventDao().insert(event);
        });
    }
}