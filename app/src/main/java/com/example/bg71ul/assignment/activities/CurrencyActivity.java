package com.example.bg71ul.assignment.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bg71ul.assignment.R;

import java.util.ArrayList;

public class CurrencyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private Spinner spinner = null;
    private ArrayAdapter currencyAdapter = null;
    private SharedPreferences currencyPreferences = null;
    private int selectedPosition;
    private String yourCurrency;
    private int initialLoading = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);


        this.currencyPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.yourCurrency = currencyPreferences.getString("yourCurrency", "DEFAULT");

        // get currencies from string array resources
        String[] currencies = getResources().getStringArray(R.array.currencies);

        for(int i =0; i < currencies.length; i++){

            // If out of 'GBP', 'EUR' and 'USD' from string array
            // equals the currency you have selected on the spinner.
            // Then we set the position of the spinner.
            if(currencies[i].equals(this.yourCurrency)){
                this.selectedPosition = i;
            }
        }

        this.spinner = (Spinner) findViewById(R.id.currency_selection);
        this.spinner.setOnItemSelectedListener(this);


        this.currencyAdapter = ArrayAdapter.createFromResource(this, R.array.currencies, R.layout.spinner_item);
        this.currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.spinner.setAdapter(this.currencyAdapter);
        this.spinner.setSelection(this.selectedPosition);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        // Apply the newly selected currency
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                .putString("yourCurrency", this.spinner.getSelectedItem().toString()).apply();

        // if we load the page first time, we don't need to display this message
        if(initialLoading > 0){
            Snackbar.make(view,"New currency selected", Snackbar.LENGTH_SHORT).show();
        }

        initialLoading++;



    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
