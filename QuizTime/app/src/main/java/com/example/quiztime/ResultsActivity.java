package com.example.quiztime;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_results);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //Get intents
        String username = getIntent().getStringExtra("username");
        int score = getIntent().getIntExtra("score", -1);


        //Establish views
        TextView congratsView = findViewById(R.id.congratulationsTextView);
        TextView scoreView = findViewById(R.id.scoreText);
        Button againBtn = findViewById(R.id.newQuizBtn);
        Button finishBtn = findViewById(R.id.finishBtn);

        congratsView.setText("Congratulations " + username);
        scoreView.setText(score + "/5");

        againBtn.setOnClickListener(v-> {
            Intent goAgain = new Intent(ResultsActivity.this, MainActivity.class);
            goAgain.putExtra("username", username);
            startActivity(goAgain);
        });

        finishBtn.setOnClickListener(v -> {
            finishAffinity();
        });
    }
}