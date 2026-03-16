package com.example.quiztime;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class QuestionsActivity extends AppCompatActivity {

    private int questionNumber = 1;
    private int selectedQuestion = -1;
    private int selectedAnswer = -1;
    private int score = 0;

    private String[][] questions = {
            {"Which of the following foods is the only one that cannot go bad?", "Dark Chocolate", "Honey", "Peanut Butter", "2"},
            {"What company produces the Xperia series of phones?", "Sony", "Samsung", "OnePlus", "1"},
            {"Entomophobia is the fear of what?", "Marsupials", "Insects", "Birds", "2"},
            {"What language is generally considered to have the longest alphabet?", "Khmer", "Hindi", "Russian", "1"},
            {"Who was the first emperor of Rome", "Caligula", "Ceaser", "Augustus", "3"},
            {"What is the longest river that only passes through a single country?", "Amazon River", "Nile", "Yangtze", "3"},
            {"The Maldives is in which continent?", "Europe", "Africa", "Asia", "3"},
            {"Which planet rotates clockwise?", "Saturn", "Venus", "Jupiter", "1"},
            {"How many colours are there in a rainbow?", "6", "7", "8", "2"},
            {"Which of the following fictional detectives was NOT created by Agetha Christie?", "Jessica Fletcher", "Hercule Poirot", "Jane Marple", "1"}
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_questions);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Establish Items
        TextView qNum = findViewById(R.id.questionNumber);
        TextView qBox = findViewById(R.id.questionBox);
        Button a1 = findViewById(R.id.answer1);
        Button a2 = findViewById(R.id.answer2);
        Button a3 = findViewById(R.id.answer3);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        Button submitAnswer = findViewById(R.id.submitAnswer);
        Button nextBtn = findViewById(R.id.nextBtn);

        //Get intent (it will just be passed on
        String username = getIntent().getStringExtra("username");

        //Set the question number
        qNum.setText("Q" + questionNumber);
        //Get question
        selectedQuestion = new Random().nextInt(10);
        qBox.setText(questions[selectedQuestion][0]);
        a1.setText(questions[selectedQuestion][1]);
        a2.setText(questions[selectedQuestion][2]);
        a3.setText(questions[selectedQuestion][3]);

        a1.setOnClickListener(v -> {
            selectedAnswer = 1;
            submitAnswer.setEnabled(true);
        });

        a2.setOnClickListener(v -> {
            selectedAnswer = 2;
            submitAnswer.setEnabled(true);
        });

        a3.setOnClickListener(v -> {
            selectedAnswer = 3;
            submitAnswer.setEnabled(true);
        });

        submitAnswer.setOnClickListener(v -> {
            // Disable answer buttons
            a1.setEnabled(false);
            a2.setEnabled(false);
            a3.setEnabled(false);

            // Answer is right
            if (selectedAnswer == Integer.parseInt(questions[selectedQuestion][4])){
                score++;
                switch(selectedAnswer){
                    case 1:
                        a1.setBackgroundColor(getResources().getColor(R.color.success));
                        break;
                    case 2:
                        a2.setBackgroundColor(getResources().getColor(R.color.success));
                        break;
                    case 3:
                        a3.setBackgroundColor(getResources().getColor(R.color.success));
                        break;
                    default:
                        Toast.makeText(this, "Internal error!", Toast.LENGTH_SHORT).show();
                        break;
                }
            } else { //Answer is wrong
                //Set answer button red
                switch(selectedAnswer){
                    case 1:
                        a1.setBackgroundColor(getResources().getColor(R.color.fail));
                        break;
                    case 2:
                        a2.setBackgroundColor(getResources().getColor(R.color.fail));
                        break;
                    case 3:
                        a3.setBackgroundColor(getResources().getColor(R.color.fail));
                        break;
                    default:
                        Toast.makeText(this, "Internal error!", Toast.LENGTH_SHORT).show();
                        break;
                }

                switch(questions[selectedQuestion][4]){
                    case "1":
                        a1.setBackgroundColor(getResources().getColor(R.color.success));
                        break;
                    case "2":
                        a2.setBackgroundColor(getResources().getColor(R.color.success));
                        break;
                    case "3":
                        a3.setBackgroundColor(getResources().getColor(R.color.success));
                        break;
                    default:
                        Toast.makeText(this, "Internal error!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
            progressBar.setProgress(questionNumber * 20);
            submitAnswer.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.VISIBLE);
        });

        nextBtn.setOnClickListener(v -> {
            //Reset buttons
            a1.setBackgroundColor(getResources().getColor(R.color.navy));
            a2.setBackgroundColor(getResources().getColor(R.color.navy));
            a3.setBackgroundColor(getResources().getColor(R.color.navy));
            a1.setEnabled(true);
            a2.setEnabled(true);
            a3.setEnabled(true);
            submitAnswer.setEnabled(false);
            nextBtn.setVisibility(View.GONE);
            questionNumber++;
            if (questionNumber < 6) // Quiz continues
            {
                submitAnswer.setVisibility(View.VISIBLE);
                qNum.setText("Q"+questionNumber);
                selectedQuestion = new Random().nextInt(10);
                qBox.setText(questions[selectedQuestion][0]);
                a1.setText(questions[selectedQuestion][1]);
                a2.setText(questions[selectedQuestion][2]);
                a3.setText(questions[selectedQuestion][3]);
            } else { // Quiz has ended
                Intent endQuiz = new Intent(QuestionsActivity.this, ResultsActivity.class);
                endQuiz.putExtra("username", username);
                endQuiz.putExtra("score", score);
                startActivity(endQuiz);
            }
        });
    }
}