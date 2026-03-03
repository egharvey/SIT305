package com.example.unitconverter;

import static java.lang.Math.round;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Initialise buttons, spinners and text inputs
        Button currencySubmit = findViewById(R.id.currencySubmit);
        Spinner currencyFromUnit = findViewById(R.id.currencyFromSpinner);
        Spinner currencyToUnit = findViewById(R.id.currencyToSpinner);
        TextView currencyFrom = findViewById(R.id.editCurrencyFrom);
        TextView currencyTo = findViewById(R.id.editCurrencyTo);

        currencySubmit.setOnClickListener(v ->{
            String start = currencyFromUnit.getSelectedItem().toString();
            String end = currencyToUnit.getSelectedItem().toString();
            Double startValue = Double.parseDouble(currencyFrom.getText().toString());
            Double value = 0.0;

            /* Conversion method: Rather than writing an equation for every single pair,
            * convert the starting value to 1 currency (in this case USD), and then convert that
            * to the wanted result. Not perfect but saves on repeated code. */
            switch (start) {
                case "USD":
                    value = startValue;
                    break;
                case "EUR":
                    value = startValue / 0.92;
                    break;
                case"JPY":
                    value = startValue / 148.5;
                    break;
                case "GBP":
                    value = startValue / 0.78;
                    break;
                case "AUD":
                    value = startValue / 1.55;
                    break;
                default:
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                    break;
            }

            switch (end) {
                case "USD":
                    break;
                case "EUR":
                    value = value * 0.92;
                    break;
                case"JPY":
                    value = value * 148.5;
                    break;
                case "GBP":
                    value = value * 0.78;
                    break;
                case "AUD":
                    value = value * 1.55;
                    break;
                default:
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                    break;
            }

            currencyTo.setText(value.toString());
        });
    }
}