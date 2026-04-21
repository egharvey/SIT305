package com.example.findwhatslost;

import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button btn_create = findViewById(R.id.btn_createAd);
        Button btn_view = findViewById(R.id.btn_showAds);

        btn_create.setOnClickListener(v->{
            Intent i = new Intent(MainActivity.this, CreateAdActivity.class);
            startActivity(i);
        });

        btn_view.setOnClickListener(v->{
            Intent i = new Intent(MainActivity.this, ListActivity.class);
            startActivity(i);
        });
    }

    /*
    public void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .setReorderingAllowed(true)
                .addToBackStack("name")
                .commit();

        findViewById(R.id.frame_layout).setVisibility(VISIBLE);
    }
    */
}