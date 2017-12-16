package com.example.bg71ul.assignment;

import com.example.bg71ul.assignment.models.CurrencyRate;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Anthony Vest on 16/12/2017.
 */
public class CurrencyConverterTest {

    CurrencyConverter currencyConverter = null;
    List<CurrencyRate> currencyRates = null;

    // Create a new instance of currency converter for each test
    @Before
    public void beforeEach(){
        currencyConverter = new CurrencyConverter();
        currencyRates = new ArrayList<>();
    }


    @Test
    public void calculatePricesSelectedCurrencyEUR() throws Exception {

        CurrencyRate currencyRateEUR = new CurrencyRate();
        currencyRateEUR.setCurrencyType("EUR");
        currencyRateEUR.setCurrencyRate(1f);

        currencyRates.add(currencyRateEUR);

        double expectedCost = 20;
        double actualCost = currencyConverter.calculatePrices("EUR","EUR", currencyRates,20);

        assertEquals(expectedCost,actualCost,0.1);

    }

    @Test
    public void calculatePricesSelectedCurrencyGBP() throws Exception{

        CurrencyRate currencyRateGBP = new CurrencyRate();
        currencyRateGBP.setCurrencyType("GBP");
        currencyRateGBP.setCurrencyRate(0.88f);

        currencyRates.add(currencyRateGBP);

        double expectedCost = 17.6;
        double actualCost = currencyConverter.calculatePrices("EUR","GBP", currencyRates,20);
        assertEquals(expectedCost,actualCost,0.1);
    }

    @Test
    public void calculatePricesSelectedCurrencyUSD() throws Exception{

        CurrencyRate currencyRateUSD = new CurrencyRate();
        currencyRateUSD.setCurrencyType("USD");
        currencyRateUSD.setCurrencyRate(1.18f);

        currencyRates.add(currencyRateUSD);

        double expectedCost = 23.6;
        double actualCost = currencyConverter.calculatePrices("EUR","USD", currencyRates,20);
        assertEquals(expectedCost,actualCost,0.1);
    }

    @Test
    public void calculateStudentPricesSelectedCurrencyEUR() throws Exception {
        CurrencyRate currencyRateEUR = new CurrencyRate();
        currencyRateEUR.setCurrencyType("EUR");
        currencyRateEUR.setCurrencyRate(1f);

        currencyRates.add(currencyRateEUR);

        double expectedCost = 14;
        double actualCost = currencyConverter.calculateStudentPrices("EUR","EUR", currencyRates,20);

        assertEquals(expectedCost,actualCost,0.1);
    }

    @Test
    public void calculateStudentPricesSelectedCurrencyGBP() throws Exception {

        CurrencyRate currencyRateGBP = new CurrencyRate();
        currencyRateGBP.setCurrencyType("GBP");
        currencyRateGBP.setCurrencyRate(0.88f);

        currencyRates.add(currencyRateGBP);

        double expectedCost = 12.4;
        double actualCost = currencyConverter.calculateStudentPrices("EUR","GBP", currencyRates,20);
        assertEquals(expectedCost,actualCost,0.1);
    }

    @Test
    public void calculateStudentPricesSelectedCurrencyUSD() throws Exception {

        CurrencyRate currencyRateUSD = new CurrencyRate();
        currencyRateUSD.setCurrencyType("USD");
        currencyRateUSD.setCurrencyRate(1.18f);

        currencyRates.add(currencyRateUSD);

        double expectedCost = 16.6;
        double actualCost = currencyConverter.calculateStudentPrices("EUR","USD", currencyRates,20);
        assertEquals(expectedCost,actualCost,0.1);

    }

}