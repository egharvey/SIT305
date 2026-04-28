package com.example.talktoawall;

import static android.app.PendingIntent.getActivity;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    List<Message> messageList = new ArrayList<>();
    String username;
    RecyclerView rv_messages;
    MessageAdapter adapter;
    DatabaseHelper db;

    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build();

    private static final String OLLAMA_URL = "http://10.0.2.2:11434/api/chat";
    private static final String MODEL_NAME = "llama3.1:8b";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(MainActivity.this);

        TextView tv_title = findViewById(R.id.textView);
        TextView tv_subHead = findViewById(R.id.textView2);
        Button btn_logIn = findViewById(R.id.btn_login);
        EditText et_name = findViewById(R.id.et_username);
        rv_messages = findViewById(R.id.rv_messages);
        rv_messages.setLayoutManager(new LinearLayoutManager(this));
        EditText et_newMessage = findViewById(R.id.et_newMessage);
        ImageView btn_send = findViewById(R.id.btn_send);

        btn_logIn.setOnClickListener(v-> {
            username = et_name.getText().toString();
            if(username.isEmpty()){
                Toast.makeText(this, "Enter a name first!", Toast.LENGTH_SHORT).show();
            } else {
                loadMessages();
                tv_title.setVisibility(GONE);
                tv_subHead.setVisibility(GONE);
                btn_logIn.setVisibility(GONE);
                et_name.setVisibility(GONE);

                et_newMessage.setVisibility(VISIBLE);
                btn_send.setVisibility(VISIBLE);
                rv_messages.setVisibility(VISIBLE);
            }
        });

        btn_send.setOnClickListener(v->{
            String message = et_newMessage.getText().toString();

            if(!message.isEmpty()){
                String timestamp = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault()).format(new Date());
                Message msg = new Message(0, message, timestamp, username,false);

                //Add to UI
                messageList.add(msg);
                adapter.notifyItemInserted(messageList.size() - 1);
                scrollToBottom();
                et_newMessage.setText("");

                //Save to db
                db.insertMessage(msg);

                //Call for a response
                getResponse(message);
            }
        });
    }

    private void loadMessages() {
        try{
            messageList = db.getUserMessageHistory(username);
        } catch (Exception e) {
            messageList = new ArrayList<>();
        }

        adapter = new MessageAdapter(this, messageList);
        rv_messages.setAdapter(adapter);
        scrollToBottom();
    }

    private void scrollToBottom() {
        if (messageList.size() > 0){
            rv_messages.smoothScrollToPosition(messageList.size() - 1);
        }
    }

    private void getResponse(String usrMsg){
        try{
            JSONObject messageObj = new JSONObject();
            messageObj.put("role", "user");
            messageObj.put("content", "Talk to this person. They go by " + username + " and have sent the message: " + usrMsg);

            JSONArray messagesArray = new JSONArray();
            messagesArray.put(messageObj);

            JSONObject body = new JSONObject();
            body.put("model", MODEL_NAME);
            body.put("messages", messagesArray);
            body.put("stream", false);

            RequestBody requestBody = RequestBody.create(
                    body.toString().toString(),
                    MediaType.parse("application/json"));

            Request request = new Request.Builder()
                    .url(OLLAMA_URL)
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e){
                    String botTime = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault()).format(new Date());
                    Message botMsg = new Message(0, "Bot was to sleepy to respond, sorry!", botTime, username, true);
                    runOnUiThread(() -> {
                        messageList.add(botMsg);
                        adapter.notifyItemInserted(messageList.size() - 1);
                        db.insertMessage(botMsg);
                        scrollToBottom();
                    });
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String responseBody = response.body().string();

                    if (response.isSuccessful())
                    {
                        try {
                            JSONObject json = new JSONObject(responseBody);
                            String botResponse = json.getJSONObject("message").getString("content");

                            String botTime = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault()).format(new Date());

                            Message botMsg = new Message(0, botResponse, botTime, username, true);
                            runOnUiThread(() -> {
                                messageList.add(botMsg);
                                adapter.notifyItemInserted(messageList.size() - 1);
                                db.insertMessage(botMsg);
                                scrollToBottom();
                            });
                        } catch (Exception e) {
                            String botTime = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault()).format(new Date());
                            Message botMsg = new Message(0, "Bot was to sleepy to respond, sorry!", botTime, username, true);
                            runOnUiThread(() -> {
                                messageList.add(botMsg);
                                adapter.notifyItemInserted(messageList.size() - 1);
                                db.insertMessage(botMsg);
                                scrollToBottom();
                            });
                        }
                    } else {
                        String botTime = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault()).format(new Date());
                        Message botMsg = new Message(0, "Bot was to dumb to respond, sorry!", botTime, username, true);
                        runOnUiThread(() -> {
                            messageList.add(botMsg);
                            adapter.notifyItemInserted(messageList.size() - 1);
                            db.insertMessage(botMsg);
                            scrollToBottom();
                        });
                    }
                }
            });

        } catch (Exception e) {
            String botTime = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault()).format(new Date());
            Message botMsg = new Message(0, "Bot was to sleepy to respond, sorry!", botTime, username, true);
            runOnUiThread(() -> {
                messageList.add(botMsg);
                adapter.notifyItemInserted(messageList.size() - 1);
                db.insertMessage(botMsg);
                scrollToBottom();
            });
        }
    }
}