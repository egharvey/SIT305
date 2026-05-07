package com.example.learningbyloosing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

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

        EditText et_username = view.findViewById(R.id.et_username);
        EditText et_email = view.findViewById(R.id.et_email);
        EditText et_confirmEmail = view.findViewById(R.id.et_confirmEmail);
        EditText et_password = view.findViewById(R.id.et_password);
        EditText et_confirmPassword = view.findViewById(R.id.et_confirmPassword);
        EditText et_phone = view.findViewById(R.id.et_phone);
        Button btn_signUp = view.findViewById(R.id.btn_signUp);

        btn_signUp.setOnClickListener(v -> {
            String username = et_username.getText().toString();
            String email = et_email.getText().toString();
            String confirmEmail = et_confirmEmail.getText().toString();
            String password = et_password.getText().toString();
            String confirmPassword = et_confirmPassword.getText().toString();
            Boolean valid = true; //Just used for the for loop
            int phone = 0;

            try {
                phone = Integer.parseInt(et_phone.getText().toString());
            } catch (Exception e) {
                valid = false;
            }

            if(!username.isEmpty()){
                loadAccounts();
                try
                {
                    for(int i = 0; i < accountList.size(); i++){
                        if(accountList.get(i).getUsername().equals(username)){
                            valid = false;
                            Toast.makeText(getContext(), "Invalid username", Toast.LENGTH_SHORT).show();
                            break;
                        }
                    }} catch (Exception e) { //If no accounts exist
                        if(password.equals(confirmPassword) && !password.isEmpty()){
                            if(isEmail(email) && email.equals(confirmEmail)) {
                                if(isPhone(phone)) {
                                    addAccount(new Account(username, email, password, "", phone, false));
                                    Toast.makeText(getContext(), "Account created", Toast.LENGTH_SHORT).show();
                                    ((MainActivity) getActivity()).setCurrentFragment(TopicsFragment.newInstance(username));
                                } else {
                                    Toast.makeText(getContext(), "Invalid phone number.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(getContext(), "Invalid email.", Toast.LENGTH_SHORT).show();
                            }
                        } else{
                            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(valid){
                        if(password.equals(confirmPassword) && !password.isEmpty()){
                            addAccount(new Account(username, email, password, "", phone, false));
                            Toast.makeText(getContext(), "Account created", Toast.LENGTH_SHORT).show();
                            ((MainActivity) getActivity()).setCurrentFragment(TopicsFragment.newInstance(username));
                        }
                    } else{
                        Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                    }
            } else {
                Toast.makeText(getContext(), "Invalid username", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private static boolean isEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern p = Pattern.compile(emailRegex);
        return email != null && p.matcher(email).matches();
    }

    private static boolean isPhone(int phone) {
        String phoneRegex = "^[7-9][0-9]{9}$";
        Pattern p = Pattern.compile(phoneRegex);
        return p.matcher(Integer.toString(phone)).matches();
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