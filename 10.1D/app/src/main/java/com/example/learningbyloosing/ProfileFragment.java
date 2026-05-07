package com.example.learningbyloosing;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.button.ButtonOptions;
import com.google.android.gms.wallet.button.PayButton;
import com.google.android.gms.wallet.contract.TaskResultContracts;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfileFragment extends Fragment {
    private static final String ARG_USERNAME = "username";

    private AppDatabase database;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Account account;
    private String username;
    private int correct, incorrect, total = 0;

    private PaymentViewModel model;

    public ProfileFragment() { }

    public static ProfileFragment newInstance(String username) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(this).get(PaymentViewModel.class);
        database = AppDatabase.getInstance(getContext());
        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        loadAccount();
        loadHistory();
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CardView card_ad = view.findViewById(R.id.card_ad);
        LinearLayout layout_overview = view.findViewById(R.id.collection_overview);
        Button btn_upgrade = view.findViewById(R.id.btn_upgrade);
        Button btn_back = view.findViewById(R.id.btn_back);
        Button btn_share = view.findViewById(R.id.btn_share);
        Button btn_history = view.findViewById(R.id.btn_history);
        Button btn_check = view.findViewById(R.id.btn_premiumCheck);
        TextView tv_username = view.findViewById(R.id.tv_username);
        TextView tv_total = view.findViewById(R.id.tv_total);
        TextView tv_correct = view.findViewById(R.id.tv_correct);
        TextView tv_incorrect = view.findViewById(R.id.tv_incorrect);
        PayButton pay_button = view.findViewById(R.id.googlePayButton);

        tv_username.setText(username);

        try {
            pay_button.initialize(
                    ButtonOptions.newBuilder()
                            .setAllowedPaymentMethods(PaymentsUtil.getAllowedPaymentMethods().toString()).build()
            );
            pay_button.setOnClickListener(v-> requestPayment(view));
        } catch (JSONException e) {}

        try{
            if(account.getIsPremium()){
                if(total != 0){
                    tv_total.setText(total);
                    tv_correct.setText(correct);
                    tv_incorrect.setText(incorrect);
                }
            }
        } catch (Exception e){}

        btn_back.setOnClickListener(v -> {
            ((MainActivity) getActivity()).setCurrentFragment(YourTopicsFragment.newInstance(username));
        });

        btn_history.setOnClickListener(v -> {
            ((MainActivity) getActivity()).setCurrentFragment(HistoryFragment.newInstance(username));
        });

        btn_share.setOnClickListener(v->{
            loadHistory();
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            String body = "My Learning By Loosing Record: \nTotal Questions: " + total + "\nTotal Correct: " + correct + "!";
            share.putExtra(Intent.EXTRA_TEXT, body);
            startActivity(Intent.createChooser(share, "Share using "));
        });

        btn_upgrade.setOnClickListener(v->{
            loadHistory();
            if(account.getIsPremium())
            {
                tv_total.setText(Integer.toString(total));
                tv_correct.setText(Integer.toString(correct));
                tv_incorrect.setText(Integer.toString(incorrect));
                card_ad.setVisibility(GONE);
                layout_overview.setVisibility(VISIBLE);
            } else {
                pay_button.setVisibility(VISIBLE);
                btn_upgrade.setVisibility(GONE);
            }
        });

        btn_check.setOnClickListener(v->{
            loadHistory();
            if(account.getIsPremium())
            {
                tv_total.setText(Integer.toString(total));
                tv_correct.setText(Integer.toString(correct));
                tv_incorrect.setText(Integer.toString(incorrect));
                card_ad.setVisibility(GONE);
                layout_overview.setVisibility(VISIBLE);
            } else {
                Toast.makeText(getContext(), "Sorry, your account has not been upgraded.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAccount() {
        executorService.execute(() ->{
            Account found = database.accountDao().getAccountByUsername(username);
            System.out.println("Loading...");
            getActivity().runOnUiThread(() -> account = found);
        });
    }

    private void loadHistory() {
        try{
            executorService.execute(() ->{
                List<History> history = database.historyDao().getUserHistory(username);

                int right = 0;
                int wrong = 0;

                for(int i = 0; i < history.size(); i++){
                    QuizData item = history.get(i).getQuizData();
                    if(item.getQuestion1().correct_answer.equals(item.getAnswer1())){
                        right++;
                    } else wrong++;
                    if(item.getQuestion3().correct_answer.equals(item.getAnswer2())){
                        right++;
                    } else wrong++;
                    if(item.getQuestion3().correct_answer.equals(item.getAnswer3())){
                        right++;
                    } else wrong++;
                }

                int t = right + wrong;

                int finalRight = right;
                int finalWrong = wrong;
                getActivity().runOnUiThread(() -> {
                    correct = finalRight;
                    incorrect = finalWrong;
                    total = t;
                });
            });
        } catch(Exception e){
            correct = 0;
            incorrect = 0;
            total = 0;
        }
    }


    @Override
    public void onResume(){
        super.onResume();
        loadAccount();
    }

    private final ActivityResultLauncher<Task<PaymentData>> paymentDataLauncher =
            registerForActivityResult(new TaskResultContracts.GetPaymentDataResult(), result -> {
                int statusCode = result.getStatus().getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.SUCCESS:
                        handlePaymentSuccess(result.getResult());
                        break;
                    case CommonStatusCodes.DEVELOPER_ERROR:
                        handleError(statusCode, result.getStatus().getStatusMessage());
                        break;
                    default:
                        handleError(statusCode, "Unexpected non API" +
                                " exception when trying to deliver the task result to an activity!");
                        break;
                }
            });

    private void handlePaymentSuccess(PaymentData paymentData) {
        final String paymentInfo = paymentData.toJson();
        try {
            JSONObject paymentMethodData = new JSONObject(paymentInfo).getJSONObject("paymentMethodData");

            final JSONObject info = paymentMethodData.getJSONObject("info");
            final String billingName = info.getJSONObject("billingAddress").getString("name");
            Toast.makeText(getContext(), "Payment Successful " + billingName + "!", Toast.LENGTH_LONG).show();

            Log.d("Google Pay token", paymentMethodData
                    .getJSONObject("tokenizationData")
                    .getString("token"));
        } catch (JSONException e) {
            Log.e("handlePaymentSuccess", "Error: " + e);
        }
    }

    private void handleError(int statusCode, @Nullable String message) {
        Log.e("loadPaymentData failed",
                String.format(Locale.getDefault(), "Error code: %d, Message: %s", statusCode, message));
    }

    public void requestPayment(View view) {
        final Task<PaymentData> task = model.getLoadPaymentDataTask("9.99");
        task.addOnCompleteListener(paymentDataLauncher::launch);
    }
}