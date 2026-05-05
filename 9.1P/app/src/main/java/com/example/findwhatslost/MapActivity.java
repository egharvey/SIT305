package com.example.findwhatslost;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.pm.PackageManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private DatabaseHelper db;
    private List<Post> posts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        loadPosts();
        Button btn_back = findViewById(R.id.btn_back);

        btn_back.setOnClickListener(v -> {
            Intent i = new Intent(MapActivity.this, MainActivity.class);
            startActivity(i);
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);

        if(mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap){
        mMap = googleMap;

        if(posts != null && !posts.isEmpty()) {
            for (int i = 0; i < posts.size(); i++) {
                LatLng location = new LatLng(posts.get(i).getLatitude(), posts.get(i).getLongitude());
                mMap.addMarker(new MarkerOptions()
                        .position(location)
                        .title(posts.get(i).getName()));
            }
        }
        checkPermissionAndShow();
    }

    private void checkPermissionAndShow(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE
            );
            return;
        }
        showCurrentLocation();
    }

    private void showCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if(location != null){
                        double lattitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        LatLng currentLocation = new LatLng(lattitude, longitude);

                        mMap.addMarker(new MarkerOptions()
                                .position(currentLocation)
                                .title("You Are Here!"));

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16));
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showCurrentLocation();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        loadPosts();
    }

    protected void loadPosts(){
        try {
            posts = db.getAllPosts();
        } catch (Exception e) {}
    }
}