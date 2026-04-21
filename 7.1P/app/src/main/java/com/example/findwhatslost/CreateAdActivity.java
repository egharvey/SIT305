package com.example.findwhatslost;

import static android.view.View.VISIBLE;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class CreateAdActivity extends AppCompatActivity {

    Uri selectedImageUri = null;
    String imagePath = "";

    ActivityResultLauncher<PickVisualMediaRequest> imagePickerLauncher =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null){
                    selectedImageUri = uri;
                    imagePath = uri.toString();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_ad);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RadioButton rb_lost = findViewById(R.id.radio_lost);
        RadioButton rb_found = findViewById(R.id.radio_found);
        Button btn_post = findViewById(R.id.btn_post);
        Button btn_image = findViewById(R.id.btn_pickImage);
        ImageView img_preview = findViewById(R.id.img_preview);
        EditText et_name = findViewById(R.id.et_postName);
        EditText et_phone = findViewById(R.id.et_phone);
        EditText et_description = findViewById(R.id.et_postDescription);
        EditText et_date = findViewById(R.id.et_date);
        EditText et_location = findViewById(R.id.et_location);

        DatabaseHelper db = new DatabaseHelper(CreateAdActivity.this);

        btn_image.setOnClickListener(v -> {
            imagePickerLauncher.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
            if(!imagePath.equals("")){
                Glide.with(this).load(selectedImageUri).centerCrop().into(img_preview);
                img_preview.setVisibility(VISIBLE);
            }
        });

        btn_post.setOnClickListener(v->{
            boolean valid = true;
            String description = et_description.getText().toString();
            String location = et_location.getText().toString();
            String found = et_date.getText().toString();

            String name = "";
            if(rb_lost.isChecked()){
                name = rb_lost.getText().toString();
            } else if(rb_found.isChecked()){
                name = rb_found.getText().toString();
            } else {
                valid = false;
                Toast.makeText(this, "Select post type!", Toast.LENGTH_SHORT).show();
            }

            if(!et_name.getText().toString().isEmpty()){
                name += " " + et_name.getText().toString();
            } else {
                valid = false;
                Toast.makeText(this, "Enter a name!", Toast.LENGTH_SHORT).show();
            }

            int phone = 0;
            try {
                phone = Integer.parseInt(et_phone.getText().toString());
            } catch (Exception e){
                valid = false;
                Toast.makeText(this, "Invalid phone number!", Toast.LENGTH_SHORT).show();
            }

            if(description.isEmpty() || location.isEmpty() || found.isEmpty()) {
                valid = false;
                Toast.makeText(this, "Fill all fields!", Toast.LENGTH_SHORT).show();
            }

            try{
                LocalDate date = LocalDate.parse(found.replace("/","-"));
            } catch (Exception e){
                valid = false;
                Toast.makeText(this, "Date is format is invalid!", Toast.LENGTH_SHORT).show();
            }

            if(imagePath.equals("")){
                valid = false;
                Toast.makeText(this, "Select an image", Toast.LENGTH_SHORT).show();
            }

            if(valid){
                Toast.makeText(this, "Posted!", Toast.LENGTH_SHORT).show();
                String pattern = "yyyy/MM/dd";
                DateFormat df = new SimpleDateFormat(pattern);
                Date today = Calendar.getInstance().getTime();
                String date = df.format(today);
                db.insertPost(new Post(0, phone, name, description, location, imagePath, found, date));
                Intent i = new Intent(CreateAdActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}