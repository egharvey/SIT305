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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private AppDatabase database;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private List<Account> accountList;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = AppDatabase.getInstance(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        Button btn_signUp = view.findViewById(R.id.btn_signUp);
        Button btn_logIn = view.findViewById(R.id.btn_logIn);
        EditText et_name = view.findViewById(R.id.et_username);
        EditText et_password = view.findViewById(R.id.et_password);

        //Try
        try{
            loadAccounts();
            if(accountList == null || accountList.isEmpty()) throw new Exception();
        } catch (Exception e){
            System.out.println("error");
        }

        btn_signUp.setOnClickListener(v-> {
            ((MainActivity) getActivity()).setCurrentFragment(new SignUpFragment());
        });

        btn_logIn.setOnClickListener(v -> {
            //Make sure accounts are loaded properly
            loadAccounts();

            String username = et_name.getText().toString();
            String password = et_password.getText().toString();

            for(int i = 0; i < accountList.size(); i++){
                if (accountList.get(i).getUsername().equals(username)){
                    if(accountList.get(i).getPassword().equals(password)){
                        ((MainActivity) getActivity()).setCurrentFragment(YourTopicsFragment.newInstance(username));
                    }
                }
            }

        });
    }

    private void loadAccounts() {
        executorService.execute(() ->{
            List<Account> accounts = database.accountDao().getAllAccounts();

            getActivity().runOnUiThread(() -> accountList = accounts);
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        loadAccounts();
    }
}