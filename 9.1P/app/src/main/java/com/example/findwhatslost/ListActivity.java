package com.example.findwhatslost;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findwhatslost.databinding.ActivityListBinding;

import java.util.List;
import java.util.Locale;

public class ListActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    DatabaseHelper db;
    PostAdapter adapter;
    RecyclerView recyclerView;
    LatLng currentPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list);

        db = new DatabaseHelper(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        Button btn_back = findViewById(R.id.btn_back);
        EditText et_search = findViewById(R.id.et_search);
        Spinner spinner = findViewById(R.id.spinner);
        recyclerView = findViewById(R.id.rv_posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterPosts(s.toString(), spinner.getSelectedItem().toString());
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterPosts(et_search.getText().toString(), spinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        btn_back.setOnClickListener(v->{
            Intent i = new Intent(ListActivity.this, MainActivity.class);
            startActivity(i);
        });

        if(checkPermission()) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            currentPoint = new LatLng(location.getLatitude(), location.getLongitude());
                        }
                    });
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        loadPosts();
    }

    private void loadPosts(){
        List<Post> posts = db.getAllPosts();
        adapter = new PostAdapter(this, posts, post -> {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("id", Integer.toString(post.getId()));
            intent.putExtra("name", post.getName());
            String description = post.getLatitude() + " " + post.getLongitude();
            intent.putExtra("description", description);
            intent.putExtra("phone", Integer.toString(post.getPhone()));
            intent.putExtra("location", post.getLocation());
            intent.putExtra("image", post.getImage());
            intent.putExtra("found", post.getFoundDate());
            intent.putExtra("posted", post.getPostedDate());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    private void filterPosts(String query, String filterDistance){
        //Get all posts
        List<Post> filtered = query.isEmpty() ? db.getAllPosts() : db.searchPosts(query);

        //Filter by distance
        try {
            if (filterDistance != "All" && currentPoint != null) {
                int distance = Integer.parseInt(filterDistance.substring(0, filterDistance.length() - 2));
                for (int i = 0; i < filtered.size(); ) {
                    Post post = filtered.get(i);
                    float[] result = new float[1];
                    Location.distanceBetween(
                            currentPoint.latitude, currentPoint.longitude,
                            post.getLatitude(), post.getLongitude(), result);

                    double km = result[0] / 1000.0;
                    if (km > distance) {
                        filtered.remove(i);
                    } else {
                        i++;
                    }
                }
            }
        } catch (Exception e){}

        //View on adapter
        adapter = new PostAdapter(this, filtered, post -> {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra("id", Integer.toString(post.getId()));
            intent.putExtra("name", post.getName());
            intent.putExtra("description", post.getDescription());
            intent.putExtra("phone", Integer.toString(post.getPhone()));
            intent.putExtra("location", post.getLocation());
            intent.putExtra("image", post.getImage());
            intent.putExtra("found", post.getFoundDate());
            intent.putExtra("posted", post.getPostedDate());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
    }

    private boolean checkPermission(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE
            );
            return false;
        }
        return true;
    }
}