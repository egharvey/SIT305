package com.example.week5workshop;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private EditText editTextNote;
    private Button buttonSave, buttonRefresh, buttonDeleteAll;
    private TextView textViewNotes;
    private AppDatabase database;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        editTextNote = findViewById(R.id.editTextNote);
        buttonSave = findViewById(R.id.buttonSave);
        buttonRefresh = findViewById(R.id.buttonRefresh);
        buttonDeleteAll = findViewById(R.id.buttonDeleteAll);
        textViewNotes = findViewById(R.id.textViewNotes);

        database = AppDatabase.getInstance(getApplicationContext());

        buttonSave.setOnClickListener(v-> saveNote());
        buttonRefresh.setOnClickListener(v-> loadNotes());
        buttonDeleteAll.setOnClickListener(v-> deleteAllNotes());

        loadNotes();
    }

    private void saveNote() {
        String noteText = editTextNote.getText().toString().trim();

        if(noteText.isEmpty()){
            Toast.makeText(this, "Please enter a note", Toast.LENGTH_SHORT).show();
            return;
        }

        executorService.execute(() -> {
            database.noteDao().insert(new Note(noteText)); // Update database
            List<Note> notes = database.noteDao().getAllNotes(); // Get update from database

            // Display changes
            runOnUiThread(() -> {
                editTextNote.setText("");
                displayNotes(notes);
                Toast.makeText(this, "Note Saved!", Toast.LENGTH_SHORT).show();
            });
        });
    }

    private void loadNotes(){
        executorService.execute(() ->{
            List<Note> notes = database.noteDao().getAllNotes();

            runOnUiThread(() -> displayNotes(notes));
        });
    }

    private void deleteAllNotes(){
        executorService.execute(() ->{
            database.noteDao().deleteAllNotes();

            runOnUiThread(() -> {
                textViewNotes.setText("Notes will be saved here...");
                Toast.makeText(this, "All Notes Deleted!", Toast.LENGTH_SHORT).show();
            });
        });
    }

    private void displayNotes(List<Note> notes){
        if (notes == null || notes.isEmpty()){
            textViewNotes.setText("Notes will be saved here...");
            return;
        }

        StringBuilder builder = new StringBuilder();
        for (Note note : notes) {
            builder.append(note.id)
                    .append(". ")
                    .append(note.title)
                    .append("\n");
        }
        textViewNotes.setText(builder.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}