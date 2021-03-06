package com.cet325.bg71ul.assignment.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cet325.bg71ul.assignment.R;
import com.cet325.bg71ul.assignment.models.CurrencyRate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainMenu extends AppCompatActivity implements View.OnClickListener{

    private Button galleryButton = null;
    private Button googleMapsButton = null;
    private Button currencyButton = null;
    private RelativeLayout relativeLayout = null;

    private List<CurrencyRate> currencyRates = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Intent intent = getIntent();

        // Check local currency rates are not null, otherwise it will fail
        if(intent.getSerializableExtra("localCurrencyRates") != null){
            this.currencyRates = (List<CurrencyRate>) intent.getSerializableExtra("localCurrencyRates");
        }

        Log.d("Currency rates", this.currencyRates.toString());

        // Relative layout menu
        this.relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        this.relativeLayout.setOnCreateContextMenuListener(this);

        // Gallery Button to Gallery Activity
        this.galleryButton = (Button)findViewById(R.id.btn_gallery);
        this.galleryButton.setOnClickListener(this);

        // Google Maps Button to Google Maps Activity
        this.googleMapsButton = (Button) findViewById(R.id.btn_google_maps);
        this.googleMapsButton.setOnClickListener(this);

        // Currency button to currency setter activity
        this.currencyButton = (Button) findViewById(R.id.btn_currency);
        this.currencyButton.setOnClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Intent intent;

        // The buttons that will give access to the core functionality of the application
        switch (id){
            case R.id.btn_gallery:
                // Once we start activity, destroy the main menu activity on the stack
                intent = new Intent(MainMenu.this,GalleryActivity.class);
                intent.putExtra("localCurrencyRates", (Serializable) currencyRates);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
                break;
            case R.id.btn_google_maps:
                intent = new Intent(getApplicationContext(), LouvreMapsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                break;
            case R.id.btn_currency:
                // Once we start activity, destroy the main menu activity on the stack
                intent = new Intent(MainMenu.this,CurrencyActivity.class);
                intent.putExtra("localCurrencyRates", (Serializable) currencyRates);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
                break;
            default:
                Toast.makeText(this, "Unrecognised", Toast.LENGTH_SHORT).show();
        }


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // This will show the latest prices and your currency
        switch (item.getItemId()){
            case R.id.ticket_prices:{
                Intent intent = new Intent(this,PricesActivity.class);
                Log.d("Option selected", currencyRates.toString());
                intent.putExtra("localCurrencyRates", (Serializable) currencyRates);
                startActivity(intent);
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }
}
