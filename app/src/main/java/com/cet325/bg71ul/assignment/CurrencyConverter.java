package com.cet325.bg71ul.assignment;

import com.cet325.bg71ul.assignment.models.CurrencyRate;

import java.util.List;

/**
 * Created by Anthony Vest on 12/12/2017.
 */

public class CurrencyConverter {


    public double calculatePrices(String localCurrency, String yourCurrency, List<CurrencyRate> rates, double ticketPrice){

        final double admissionPrice = ticketPrice;

        float exchangeRate = 0;

        for(CurrencyRate cr: rates){
            if(yourCurrency.equals(cr.getCurrencyType())){
                exchangeRate = cr.getCurrencyRate();
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

    public double calculateStudentPrices(String localCurrency, String yourCurrency, List<CurrencyRate> rates, double ticketPrice, double studentDiscount){

        final double admissionPrice = ticketPrice;
        final double admissionStudentDiscount = (studentDiscount / 100);

        float exchangeRate = 0;

        for(CurrencyRate cr: rates){
            if(yourCurrency.equals(cr.getCurrencyType())){
                exchangeRate = cr.getCurrencyRate();
            }
        }

        if(yourCurrency.equals(localCurrency)){
            return (admissionPrice) - (admissionPrice * admissionStudentDiscount);
        }

        if(yourCurrency.equals("GBP")){
            return (exchangeRate * admissionPrice ) - (exchangeRate * admissionPrice * admissionStudentDiscount);
        }

        if(yourCurrency.equals("USD")){
            return (exchangeRate * admissionPrice ) - (exchangeRate * admissionPrice * admissionStudentDiscount);
        }

        return admissionPrice * admissionStudentDiscount;

    }
}
