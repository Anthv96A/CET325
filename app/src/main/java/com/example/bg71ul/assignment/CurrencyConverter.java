package com.example.bg71ul.assignment;

import android.util.Log;

import com.example.bg71ul.assignment.models.CurrencyRate;

import java.util.List;

/**
 * Created by Anthony Vest on 12/12/2017.
 */

public class CurrencyConverter {


    public double calculatePrices(String localCurrency, String yourCurrency, double rate, List<CurrencyRate> rates){

        final double admissionPrice = 10;

        float exchangeRate = 0;

        for(CurrencyRate cr: rates){
            if(yourCurrency.equals(cr.getCurrencyType())){
                exchangeRate = cr.getCurrencyRate();
                Log.d("Exchange rate " , String.valueOf(exchangeRate));
            }
        }

        if(yourCurrency.equals(localCurrency)){
            return admissionPrice;
        }

        if(yourCurrency.equals("GBP")){
            return exchangeRate * admissionPrice;
        }

        if(yourCurrency.equals("USD")){
            return exchangeRate * admissionPrice;
        }

        return admissionPrice;

    }

    public double calculateStudentPrices(String localCurrency, String yourCurrency, double rate, List<CurrencyRate> rates){

        final double admissionPrice = 10;
        final double studentDiscount = 0.30;

        float exchangeRate = 0;

        for(CurrencyRate cr: rates){
            if(yourCurrency.equals(cr.getCurrencyType())){
                exchangeRate = cr.getCurrencyRate();
                Log.d("Exchange rate " , String.valueOf(exchangeRate));
            }
        }

        if(yourCurrency.equals(localCurrency)){
            return (admissionPrice) - (admissionPrice * studentDiscount);
        }

        if(yourCurrency.equals("GBP")){
            return (exchangeRate * admissionPrice ) - (exchangeRate * admissionPrice * studentDiscount);
        }

        if(yourCurrency.equals("USD")){
            return (exchangeRate * admissionPrice ) - (exchangeRate * admissionPrice * studentDiscount);
        }

        return admissionPrice * studentDiscount;

    }
}
