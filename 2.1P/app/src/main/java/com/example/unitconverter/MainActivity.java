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
        Button tempSubmit = findViewById(R.id.tempSubmit);
        Spinner tempFromUnit = findViewById(R.id.tempFromSpinner);
        Spinner tempToUnit = findViewById(R.id.tempToSpinner);
        TextView tempFrom = findViewById(R.id.editTempFrom);
        TextView tempTo = findViewById(R.id.editTempTo);
        Button fuelSubmit = findViewById(R.id.fuelSubmit);
        Spinner fuelFromUnit = findViewById(R.id.fuelFromSpinner);
        Spinner fuelToUnit = findViewById(R.id.fuelToSpinner);
        TextView fuelFrom = findViewById(R.id.editFuelFrom);
        TextView fuelTo = findViewById(R.id.editFuelTo);

        currencySubmit.setOnClickListener(v ->{
            String start = currencyFromUnit.getSelectedItem().toString();
            String end = currencyToUnit.getSelectedItem().toString();
            Double value = 0.0;
            try {
                value = Double.parseDouble(currencyFrom.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid value", Toast.LENGTH_SHORT).show();
                currencyFrom.setText("0.0");
            }

            /* Conversion method: Rather than writing an equation for every single pair,
            * convert the starting value to 1 currency (in this case USD), and then convert that
            * to the wanted result. Not perfect but saves on repeated code. */
            switch (start) {
                case "USD":
                    break;
                case "EUR":
                    value = value / 0.92;
                    break;
                case"JPY":
                    value = value / 148.5;
                    break;
                case "GBP":
                    value = value / 0.78;
                    break;
                case "AUD":
                    value = value / 1.55;
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

        tempSubmit.setOnClickListener(v ->{
            String start = tempFromUnit.getSelectedItem().toString();
            String end = tempToUnit.getSelectedItem().toString();
            Double value = 0.0;
            try {
                value = Double.parseDouble(tempFrom.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid value", Toast.LENGTH_SHORT).show();
                tempFrom.setText("0.0");
            }

            /* Conversion method: Rather than writing an equation for every single pair,
             * convert the starting value to 1 currency (in this case Celsius), and then convert that
             * to the wanted result. Not perfect but saves on repeated code. */
            switch (start) {
                case "Celsius":
                    break;
                case "Fahrenheit":
                    value = (value - 32) / 1.8;
                    break;
                case"Kelvin":
                    value = value - 273.15;
                    break;
                default:
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                    break;
            }

            switch (end) {
                case "Celsius":
                    break;
                case "Fahrenheit":
                    value = (value * 1.8) + 32;
                    break;
                case"Kelvin":
                    value = value + 273.15;
                    break;
                default:
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                    break;
            }

            tempTo.setText(value.toString());
        });

        // Fuel Efficiency and Distance Conversions
        fuelSubmit.setOnClickListener(v ->{
            String start = fuelFromUnit.getSelectedItem().toString();
            String end = fuelToUnit.getSelectedItem().toString();
            Double value = 0.0;

            try {
                //Causes error if user did not enter a value
                value = Double.parseDouble(fuelFrom.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid value", Toast.LENGTH_SHORT).show();
                fuelFrom.setText("0.0");
            }

            /* conversionType will store whether the starting conversion is:
            *  - 0: Distance
            *  - 1: Fuel Efficiency
            *  - 2: Volume
            *  If the types do not match, we will show a Toast telling the user the issue
             */
            int conversionType = -1;

            /* The same logic we used earlier will be applied.
             * The assumed units are kilometers, km/L and liters.
             */
            switch (start) {
                case "Kilometers":
                    conversionType = 0;
                    break;
                case "Nautical Miles":
                    conversionType = 0;
                    value = value * 1.852;
                    break;
                case"km/L":
                    conversionType = 1;
                    break;
                case "mgp":
                    conversionType = 1;
                    value = value * 0.425;
                    break;
                case "Liters":
                    conversionType = 2;
                    break;
                case"Gallons":
                    conversionType = 2;
                    value = value * 3.785;
                    break;
                default:
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                    value = 0.0;
                    break;
            }

            switch (end) {
                case "Kilometers":
                    if (conversionType != 0)
                    {
                        Toast.makeText(this, "Error: Units cannot be converted", Toast.LENGTH_SHORT).show();
                        value = 0.0;
                    }
                    break;
                case "Nautical Miles":
                    if (conversionType != 0)
                    {
                        Toast.makeText(this, "Error: Units cannot be converted", Toast.LENGTH_SHORT).show();
                        value = 0.0;
                    }
                    value = value / 1.852;
                    break;
                case"km/L":
                    if (conversionType != 1)
                    {
                        Toast.makeText(this, "Error: Units cannot be converted", Toast.LENGTH_SHORT).show();
                        value = 0.0;
                    }
                    break;
                case "mpg":
                    if (conversionType != 1)
                    {
                        Toast.makeText(this, "Error: Units cannot be converted", Toast.LENGTH_SHORT).show();
                        value = 0.0;
                    }
                    value = value / 0.425;
                    break;
                case "Liters":
                    if (conversionType != 2)
                    {
                        Toast.makeText(this, "Error: Units cannot be converted", Toast.LENGTH_SHORT).show();
                        value = 0.0;
                    }
                    break;
                case"Gallons":
                    if (conversionType != 2)
                    {
                        Toast.makeText(this, "Error: Units cannot be converted", Toast.LENGTH_SHORT).show();
                        value = 0.0;
                    }
                    value = value / 3.785;
                    break;
                default:
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                    value = 0.0;
                    break;
            }

            fuelTo.setText(value.toString());
        });
    }
}