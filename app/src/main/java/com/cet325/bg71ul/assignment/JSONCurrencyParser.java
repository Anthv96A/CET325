package com.cet325.bg71ul.assignment;

import android.util.Log;

import com.cet325.bg71ul.assignment.models.CurrencyRate;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Anthony on 13/12/2017.
 */

public class JSONCurrencyParser {

    public static CurrencyRate getCurrencyRate(String data, String rate, String currency) throws JSONException{
        CurrencyRate currencyRate = new CurrencyRate();

        if(rate.equals(currency)){
            currencyRate.setCurrencyType(currency);
            currencyRate.setCurrencyRate(1);
        } else{
            JSONObject jsonObject = new JSONObject(data);
            // Abstract 'rates' from JSON Response
            JSONObject rateObj = jsonObject.getJSONObject("rates");
            // Then we get the rate of whatever the passed rate may be, from the JSON String
            String currencyR = rateObj.getString(rate);
            currencyRate.setCurrencyType(rate);
            currencyRate.setCurrencyRate(Float.parseFloat(String.valueOf(currencyR)));
        }

        Log.d("getCurrencyRate", currencyRate.toString());
        return currencyRate;
    }
}
