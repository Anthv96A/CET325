package com.cet325.bg71ul.assignment.activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cet325.bg71ul.assignment.R;

public class CurrencyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private Spinner spinner = null;
    private ArrayAdapter currencyAdapter = null;
    private SharedPreferences currencyPreferences = null;
    private int selectedPosition;
    private String yourCurrency;
    private int initialLoading = 0;
    private boolean outOfBoundsError = false;
    private boolean emptyError = false;
    final ViewGroup nullParent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_currency);

        this.currencyPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Selected currency
        this.yourCurrency = currencyPreferences.getString("yourCurrency", "DEFAULT");

        // get currencies from string array resources
        String[] currencies = getResources().getStringArray(R.array.currencies);

        for (int i = 0; i < currencies.length; i++) {

            // If out of 'GBP', 'EUR' and 'USD' from string array
            // equals the currency you have selected on the spinner.
            // Then we set the position of the spinner.
            if (currencies[i].equals(this.yourCurrency)) {
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
        if (initialLoading > 0) {
            Snackbar.make(view, "New currency selected", Snackbar.LENGTH_SHORT).show();
        }

        initialLoading++;


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_currency, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.setTicketPrice: {
                editTicketPrice();
                return true;
            }
            case R.id.setStudentDiscount:{
                editStudentDiscount();
                return true;

            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void editStudentDiscount() {

        // This error is checking to make sure user doesn't put a ridiculous percentage value
        if(outOfBoundsError){
            Toast.makeText(this, "Your value is out of bounds", Toast.LENGTH_SHORT).show();
        }

        // This error is making sure we can't
        if(emptyError){
            Toast.makeText(this, "You can't submit an empty value", Toast.LENGTH_SHORT).show();
        }

        LayoutInflater layoutInflater = LayoutInflater.from(CurrencyActivity.this);
        View getEditDialog = layoutInflater.inflate(R.layout.edit_student_discount, nullParent);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CurrencyActivity.this);

        String currentStudentDiscount = this.currencyPreferences.getString("studentDiscount","DEFAULT");

        final TextView txtNewStudentDiscount = (TextView) getEditDialog.findViewById(R.id.editTextDialogStudentInput);
        txtNewStudentDiscount.setText(currentStudentDiscount);
        alertDialogBuilder.setView(getEditDialog);

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                outOfBoundsError = false;
                emptyError = false;
                Toast.makeText(CurrencyActivity.this, "Dismissed", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }).setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String newPrice = txtNewStudentDiscount.getText().toString();
                if(newPrice == null || newPrice.isEmpty()){
                    emptyError = true;
                    outOfBoundsError = false;
                    editStudentDiscount();
                    return;
                }

                double check = Double.parseDouble(txtNewStudentDiscount.getText().toString());
                if(check > 100 || check <= 0 ){
                    outOfBoundsError = true;
                    emptyError = false;
                    editStudentDiscount();
                    return;
                }


                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                        .putString("studentDiscount", newPrice).apply();
                outOfBoundsError = false;
                emptyError = false;
                Toast.makeText(CurrencyActivity.this, "Updated", Toast.LENGTH_SHORT).show();
            }
        }).create().show();
    }


    private void editTicketPrice(){

        if(emptyError){
            Toast.makeText(this, "You can't submit an empty value", Toast.LENGTH_SHORT).show();
        }

        LayoutInflater layoutInflater = LayoutInflater.from(CurrencyActivity.this);
        View getEditDialog = layoutInflater.inflate(R.layout.edit_ticket_price, nullParent);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CurrencyActivity.this);

        String currentTicketPrice = this.currencyPreferences.getString("ticketPrice","DEFAULT");

        final TextView txtNewPrice = (TextView) getEditDialog.findViewById(R.id.editTextDialogTicketInput);
        txtNewPrice.setText(currentTicketPrice);
        alertDialogBuilder.setView(getEditDialog);

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                outOfBoundsError = false;
                emptyError = false;
                Toast.makeText(CurrencyActivity.this, "Dismissed", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }).setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {


                String newPrice = txtNewPrice.getText().toString();

                if(newPrice == null || newPrice.isEmpty()){
                    emptyError = true;
                    editTicketPrice();
                    return;
                }
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit()
                        .putString("ticketPrice", newPrice).apply();
                emptyError = false;
                Toast.makeText(CurrencyActivity.this, "Updated", Toast.LENGTH_SHORT).show();
            }
        }).create().show();
    }

}
