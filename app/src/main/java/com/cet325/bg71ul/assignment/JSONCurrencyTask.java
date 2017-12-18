package com.cet325.bg71ul.assignment;

import android.os.AsyncTask;
import android.util.Log;

import com.cet325.bg71ul.assignment.models.CurrencyRate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Anthony on 13/12/2017.
 */

public class JSONCurrencyTask extends AsyncTask<String, Void, CurrencyRate> {
    @Override
    protected CurrencyRate doInBackground(String... strings) {

        Log.d("PARAM 1 and 2 " , strings[0] + " " + strings[1] );
        CurrencyRate currencyRate = new CurrencyRate();
        String data = ((new CurrencyHttpClient()).getCurrencyData(strings[1]));

        if(data != null){
            Log.d("JSON currency",data);
            try {
                currencyRate = JSONCurrencyParser.getCurrencyRate(data, strings[0], strings[1]);
            } catch (Exception e){
                e.printStackTrace();
            }
            return currencyRate;
        }

        // If the data is null, set rate to 0.0
        currencyRate.setCurrencyType(strings[0]);
        currencyRate.setCurrencyRate(0);
        return currencyRate;
    }

    @Override
    protected void onPostExecute(CurrencyRate currencyRate) {
        rate(currencyRate);
    }

    private CurrencyRate rate(CurrencyRate myValue) {
        return myValue;
    }

    public List<CurrencyRate> fetchCurrencyRates(String[] rates, String currency){
        //create list object for the currency rates
        List<CurrencyRate> currencyRates = new ArrayList<>();

        for(String rate : rates){
            //add each currency to the list and get the currency data
            JSONCurrencyTask task = new JSONCurrencyTask();
            task.execute(new String[]{rate, currency});
            try {
                CurrencyRate currencyRate = task.get();
                currencyRates.add(currencyRate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return currencyRates;
    }
}
