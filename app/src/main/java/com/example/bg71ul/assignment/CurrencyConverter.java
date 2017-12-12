package com.example.bg71ul.assignment;

/**
 * Created by antho on 12/12/2017.
 */

public class CurrencyConverter {


    public double calculatePrices(String localCurrency, String yourCurrency){

        final double admissionPrice = 10;

        final double EuroToPoundRate = 0.88;
        final double EuroToDollarRate = 0.88;

        double total = 0;


        if(yourCurrency.equals(localCurrency)){
            return admissionPrice;
        }

        if(yourCurrency.equals("GBP")){
            return EuroToPoundRate * admissionPrice;
        }

        if(yourCurrency.equals("USD")){
            return EuroToDollarRate + admissionPrice;
        }

        return admissionPrice;

    }
}
