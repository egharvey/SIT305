package com.example.findwhatslost;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView tv_name = findViewById(R.id.tv_name);
        TextView tv_description = findViewById(R.id.tv_description);
        TextView tv_phone = findViewById(R.id.tv_phone);
        TextView tv_location = findViewById(R.id.tv_location);
        TextView tv_date = findViewById(R.id.tv_date);
        Button btn_back = findViewById(R.id.btn_back);
        Button btn_delete = findViewById(R.id.btn_delete);
        ImageView img_preview = findViewById(R.id.img_preview);

        tv_name.setText(getIntent().getStringExtra("name"));
        tv_description.setText(getIntent().getStringExtra("description"));
        tv_location.setText(getIntent().getStringExtra("location"));
        tv_phone.setText("Phone: " + getIntent().getStringExtra("phone"));
        if(getIntent().getStringExtra("name").charAt(0) == 'f') {
            tv_date.setText("Found: " + getIntent().getStringExtra("found") + "   Posted: " + getIntent().getStringExtra("posted"));
        } else {
            tv_date.setText("Lost: " + getIntent().getStringExtra("found") + "   Posted: " + getIntent().getStringExtra("posted"));
        }

        String image = getIntent().getStringExtra("image");
        if(image != null && !image.isEmpty()){
            Glide.with(this).load(Uri.parse(image)).into(img_preview);
        } else {
            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
        }

        btn_back.setOnClickListener(v->{
            Intent i = new Intent(DetailsActivity.this, ListActivity.class);
            startActivity(i);
        });

        btn_delete.setOnClickListener(v -> {
            DatabaseHelper db = new DatabaseHelper(this);
            db.deletePost(Integer.parseInt(getIntent().getStringExtra("id")));
            Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(DetailsActivity.this, ListActivity.class);
            startActivity(i);
        });
    }
}