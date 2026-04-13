package com.example.istream;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignUpFragment extends Fragment {
    private AppDatabase database;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private List<Account> accountList;

    public SignUpFragment() {
        // Required empty public constructor
    }

    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = AppDatabase.getInstance(getContext());

        EditText et_name = view.findViewById(R.id.et_name);
        EditText et_username = view.findViewById(R.id.et_username);
        EditText et_password = view.findViewById(R.id.et_password);
        EditText et_confirmPassword = view.findViewById(R.id.et_confirmPassword);
        Button btn_signUp = view.findViewById(R.id.btn_signUp);

        btn_signUp.setOnClickListener(v -> {
            String name = et_name.getText().toString();
            String username = et_username.getText().toString();
            String password = et_password.getText().toString();
            String confirmPassword = et_confirmPassword.getText().toString();
            Boolean valid = true; //Just used for the for loop

            if(!name.isEmpty()){
                if(!username.isEmpty()){
                    loadAccounts();
                    try{
                    for(int i = 0; i < accountList.size(); i++){
                        if(accountList.get(i).getUsername().equals(username)){
                            valid = false;
                            Toast.makeText(getContext(), "Invalid username", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }} catch (Exception e) { //If no accounts exist
                        if(password.equals(confirmPassword) && !password.isEmpty()){
                            addAccount(new Account(name, username, password, ""));
                            Toast.makeText(getContext(), "Account created", Toast.LENGTH_SHORT).show();
                            requireActivity().getSupportFragmentManager().popBackStack();
                        }
                    }
                    if(valid){
                        if(password.equals(confirmPassword) && !password.isEmpty()){
                            addAccount(new Account(name, username, password, ""));
                            Toast.makeText(getContext(), "Account created", Toast.LENGTH_SHORT).show();
                            requireActivity().getSupportFragmentManager().popBackStack();
                        }
                    } else{
                        Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Invalid username", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Invalid name", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAccounts() {
        executorService.execute(() ->{
            List<Account> accounts = database.accountDao().getAllAccounts();

            getActivity().runOnUiThread(() -> accountList = accounts);
        });
    }

    private void addAccount(Account account){
        executorService.execute(() -> {
            database.accountDao().insert(account);
        });
    }
}