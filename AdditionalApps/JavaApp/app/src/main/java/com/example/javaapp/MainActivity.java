package com.example.javaapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

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


        //Handle Button
        Button helloWorldButton = findViewById(R.id.helloWorldButton);
        ImageView goku = findViewById(R.id.goku);
        helloWorldButton.setOnClickListener(v ->{

            //If the image is gone, make it visible and make a toast
            if (goku.getVisibility() > 1){
                /* Not yet fully sure how getVisibility works, it returns a number based on
                visibility, but I cannot find the full list of numbers. What I do know is it:
                - Returns 0 if visibility == GONE
                - Returns 8 if visibility == VISIBLE
                This is fine for now, but note to self, find the proper documentation! */
                goku.setVisibility(View.VISIBLE);
                Toast.makeText(this, "A wild Goku appeared!", Toast.LENGTH_SHORT).show();
            } else { //If image visible, make it disappear with a custom toast
                goku.setVisibility(View.GONE);
                Toast.makeText(this, "Where did he go?!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}