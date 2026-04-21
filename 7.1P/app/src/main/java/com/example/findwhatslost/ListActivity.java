package com.example.findwhatslost;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findwhatslost.databinding.ActivityListBinding;

import java.util.List;

public class ListActivity extends AppCompatActivity {

    DatabaseHelper db;
    PostAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list);

        db = new DatabaseHelper(this);

        Button btn_back = findViewById(R.id.btn_back);
        EditText et_search = findViewById(R.id.et_search);
        recyclerView = findViewById(R.id.rv_posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterPosts(s.toString());
            }
        });

        btn_back.setOnClickListener(v->{
            Intent i = new Intent(ListActivity.this, MainActivity.class);
            startActivity(i);
        });
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

    private void filterPosts(String query){
        List<Post> filtered = query.isEmpty() ? db.getAllPosts() : db.searchPosts(query);
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
}