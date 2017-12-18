package com.cet325.bg71ul.assignment;

import com.cet325.bg71ul.assignment.models.CurrencyRate;

import java.util.List;

/**
 * Created by Anthony Vest on 12/12/2017.
 */

public class CurrencyConverter {


    // Calculates prices for over 18's
    public double calculatePrices(String localCurrency, String yourCurrency, List<CurrencyRate> rates, double ticketPrice){

        float exchangeRate = 0;

        for(CurrencyRate cr: rates){
            if(yourCurrency.equals(cr.getCurrencyType())){
                // Extract the rate from your selected currency
                exchangeRate = cr.getCurrencyRate();
            }
        }

        // Local currency being Euros
        if(yourCurrency.equals(localCurrency)){
            return ticketPrice;
        }

        if(yourCurrency.equals("GBP")){
            return exchangeRate * ticketPrice;
        }

        if(yourCurrency.equals("USD")){
            return exchangeRate * ticketPrice;
        }

        return ticketPrice;

    }

    // Calculates student prices
    public double calculateStudentPrices(String localCurrency, String yourCurrency, List<CurrencyRate> rates, double ticketPrice, double studentDiscount){

        final double admissionStudentDiscount = (studentDiscount / 100);

        float exchangeRate = 0;

        for(CurrencyRate cr: rates){
            if(yourCurrency.equals(cr.getCurrencyType())){
                exchangeRate = cr.getCurrencyRate();
            }
        }

        // Local currency being Euros
        if(yourCurrency.equals(localCurrency)){
            return (ticketPrice) - (ticketPrice * admissionStudentDiscount);
        }

        if(yourCurrency.equals("GBP")){
            return (exchangeRate * ticketPrice ) - (exchangeRate * ticketPrice * admissionStudentDiscount);
        }

        if(yourCurrency.equals("USD")){
            return (exchangeRate * ticketPrice ) - (exchangeRate * ticketPrice * admissionStudentDiscount);
        }

        return ticketPrice * admissionStudentDiscount;

    }
}
