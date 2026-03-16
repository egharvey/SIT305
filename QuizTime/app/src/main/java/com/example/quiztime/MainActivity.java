package com.example.quiztime;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //Track score and username
    private String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Initialise items on screen(s)
        TextView nameInput = findViewById(R.id.nameEntry);
        Button submitNameBtn = findViewById(R.id.startBtn);

        username = getIntent().getStringExtra("username");
        nameInput.setText(username);

        submitNameBtn.setOnClickListener(v -> {
            //Set name
            try {
                //Get and check username
                username = nameInput.getText().toString();
                if (username.isEmpty()) throw new IllegalArgumentException();

                //Move to question screen
                Intent startQuiz = new Intent(MainActivity.this, QuestionsActivity.class);
                startQuiz.putExtra("username", username);
                startActivity(startQuiz);
            } catch (IllegalArgumentException e) {
                Toast.makeText(this, "Enter your name before starting!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}