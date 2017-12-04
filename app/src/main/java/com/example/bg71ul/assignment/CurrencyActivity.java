package com.example.bg71ul.assignment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class CurrencyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private Spinner spinner = null;
    private ArrayAdapter currencyAdapter = null;
    private SharedPreferences currencyPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);

        this.currencyPreferences = getPreferences(0);

        this.spinner = (Spinner) findViewById(R.id.currency_selection);
        this.spinner.setOnItemSelectedListener(this);


        this.currencyAdapter = ArrayAdapter.createFromResource(this, R.array.currencies, R.layout.spinner_item);
        this.currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.spinner.setAdapter(this.currencyAdapter);
        this.spinner.setSelection(this.currencyPreferences.getInt("currencySelection",0));
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        SharedPreferences.Editor editor = getPreferences(0).edit();
        int selectedItem = i;

        try{

            switch (selectedItem){
                case 1:
                    editor.putInt("currencySelection", selectedItem);
                    editor.commit();
                    break;
                case 2:
                    editor.putInt("currencySelection", selectedItem);
                    editor.commit();
                    break;
                case 3:
                    editor.putInt("currencySelection", selectedItem);
                    editor.commit();
                    break;
            }

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
