package com.example.week4workshop;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    //Items
    private EditText etName;
    private EditText etDraft;
    private EditText etFileNote;
    private CheckBox cbDarkMode;
    private TextView tvStatus;
    private Button btnSavePrefs;
    private Button btnLoadPrefs;
    private Button btnSaveFile;
    private Button btnLoadFile;

    //Constants
    private static final String PREF_FILE_NAME = "workshop_prefs";
    private static final String KEY_NAME = "key_name";
    private static final String KEY_DARK_MODE = "key_dark_mode";

    //More constants
    private static final String STATE_DRAFT = "state_draft";
    private static final String STATE_STATUS = "state_status";

    //Last constant
    private static final String NOTE_FILE_NAME = "my_note.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //CONNECT JAVA TO XML
        etName = findViewById(R.id.etName);
        etDraft = findViewById(R.id.etDraft);
        etFileNote = findViewById(R.id.etFileNote);
        cbDarkMode = findViewById(R.id.cbDarkMode);
        tvStatus = findViewById(R.id.tvStatus);
        btnSavePrefs = findViewById(R.id.btnSavePrefs);
        btnLoadPrefs = findViewById(R.id.btnLoadPrefs);
        btnSaveFile = findViewById(R.id.btnSaveFile);
        btnLoadFile = findViewById(R.id.btnLoadFile);

        //Restore if activity was recreates (see onSaveInstanceState at line 152)
        if (savedInstanceState != null){
            String restoredDraft = savedInstanceState.getString(STATE_DRAFT, "");
            String restoredStatus = savedInstanceState.getString(STATE_STATUS, "");
            etDraft.setText(restoredDraft);
            tvStatus.setText(restoredStatus);
        }

        //Checkbox listener
        cbDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        //SET BUTTON LISTENERS
        btnSavePrefs.setOnClickListener(v-> saveUsingSharedPreferences());
        btnLoadPrefs.setOnClickListener(v -> loadUsingSharedPreferences());
        btnSaveFile.setOnClickListener(v-> saveNoteToInternalFile());
        btnLoadFile.setOnClickListener(v-> loadNoteFromInternalFile());
    }

    //SAVE TO PREFERENCES
    private void saveUsingSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_NAME, etName.getText().toString());
        editor.putBoolean(KEY_DARK_MODE, cbDarkMode.isChecked());
        editor.apply();

        tvStatus.setText("Saved SharedPreferences!");
        Toast.makeText(this, "Preferences Saved!", Toast.LENGTH_SHORT).show();
    }

    private void loadUsingSharedPreferences() {
        SharedPreferences preferences = getSharedPreferences(PREF_FILE_NAME, MODE_PRIVATE);
        String savedName = preferences.getString(KEY_NAME, "");
        boolean savedDarkMode = preferences.getBoolean(KEY_DARK_MODE, false);

        etName.setText(savedName);

        cbDarkMode.setChecked(savedDarkMode);

        tvStatus.setText("Loaded SharedPreferences!");
        Toast.makeText(this, "Preferences Loaded!", Toast.LENGTH_SHORT).show();
    }

    //SAVE TO FILE
    private void saveNoteToInternalFile() {
        String noteText = etFileNote.getText().toString();
        try {
            OutputStreamWriter writer = new OutputStreamWriter(openFileOutput(NOTE_FILE_NAME, MODE_PRIVATE));
            writer.write(noteText);
            writer.close();
            tvStatus.setText("Saved file!");
            Toast.makeText(this, "File Saved!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            tvStatus.setText("Error saving file: " + e.getMessage());
            Toast.makeText(this, "File save failed :(", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadNoteFromInternalFile(){
        StringBuilder fileContent = new StringBuilder();
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(openFileInput(NOTE_FILE_NAME));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
            reader.close();
            etFileNote.setText(fileContent.toString());
            tvStatus.setText("Loaded file!");
            Toast.makeText(this, "File Loaded!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            tvStatus.setText("Error loading file: " + e.getMessage());
            Toast.makeText(this, "File load failed :(", Toast.LENGTH_SHORT).show();
        }
    }

    // Saving and Restoring Activity Instance
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_DRAFT, etDraft.getText().toString());
        outState.putString(STATE_STATUS, tvStatus.getText().toString());
    }
}