package com.cet325.bg71ul.assignment;

import com.cet325.bg71ul.assignment.models.CurrencyRate;

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

    private final double ticketPrice = 20;
    private final double studentDiscount = 30;

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
        double actualCost = currencyConverter.calculatePrices("EUR","EUR", currencyRates,ticketPrice);

        assertEquals("Local currency is Euros, if selected currency is also Euros it should equal" ,expectedCost,actualCost,0.1);

    }

    @Test
    public void calculatePricesSelectedCurrencyGBP() throws Exception{

        CurrencyRate currencyRateGBP = new CurrencyRate();
        currencyRateGBP.setCurrencyType("GBP");
        currencyRateGBP.setCurrencyRate(0.88f);

        currencyRates.add(currencyRateGBP);

        double expectedCost = 17.6;
        double actualCost = currencyConverter.calculatePrices("EUR","GBP", currencyRates,ticketPrice);
        assertEquals("Local currency is Euros, the current rate is 0.88 against the British Pound",expectedCost,actualCost,0.1);
    }

    @Test
    public void calculatePricesSelectedCurrencyUSD() throws Exception{

        CurrencyRate currencyRateUSD = new CurrencyRate();
        currencyRateUSD.setCurrencyType("USD");
        currencyRateUSD.setCurrencyRate(1.18f);

        currencyRates.add(currencyRateUSD);

        double expectedCost = 23.6;
        double actualCost = currencyConverter.calculatePrices("EUR","USD", currencyRates,ticketPrice);
        assertEquals("Local currency is Euros, the current rate is 1.18 against the US Dollar",expectedCost,actualCost,0.1);
    }

    @Test
    public void calculateStudentPricesSelectedCurrencyEUR() throws Exception {
        CurrencyRate currencyRateEUR = new CurrencyRate();
        currencyRateEUR.setCurrencyType("EUR");
        currencyRateEUR.setCurrencyRate(1f);

        currencyRates.add(currencyRateEUR);

        double expectedCost = 14;
        double actualCost = currencyConverter.calculateStudentPrices("EUR","EUR", currencyRates,ticketPrice,studentDiscount);

        assertEquals("Local currency is Euro, with additional 30% off for student for selected currency of Euros",expectedCost,actualCost,0.1);
    }

    @Test
    public void calculateStudentPricesSelectedCurrencyGBP() throws Exception {

        CurrencyRate currencyRateGBP = new CurrencyRate();
        currencyRateGBP.setCurrencyType("GBP");
        currencyRateGBP.setCurrencyRate(0.88f);

        currencyRates.add(currencyRateGBP);

        double expectedCost = 12.4;
        double actualCost = currencyConverter.calculateStudentPrices("EUR","GBP", currencyRates,ticketPrice,studentDiscount);
        assertEquals("Local currency is Euro, with additional 30% off for student for selected currency of British Pounds",expectedCost,actualCost,0.1);
    }

    @Test
    public void calculateStudentPricesSelectedCurrencyUSD() throws Exception {

        CurrencyRate currencyRateUSD = new CurrencyRate();
        currencyRateUSD.setCurrencyType("USD");
        currencyRateUSD.setCurrencyRate(1.18f);

        currencyRates.add(currencyRateUSD);

        double expectedCost = 16.6;
        double actualCost = currencyConverter.calculateStudentPrices("EUR","USD", currencyRates,ticketPrice,studentDiscount);
        assertEquals("Local currency is Euro, with additional 30% off for student for selected currency of US Dollars",expectedCost,actualCost,0.1);

    }

}