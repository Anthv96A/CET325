package com.cet325.bg71ul.assignment.activities;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Spinner;

import com.cet325.bg71ul.assignment.R;

import org.junit.Before;

/**
 * Created by Anthony Vest on 18/12/2017.
 */
public class CurrencyActivityInstrumentedTest extends ActivityInstrumentationTestCase2<CurrencyActivity> {


    private Activity currencyActivity;
    private Spinner spinner;
    final String[] rates = new String[]{"GBP","EUR","USD"};

    public CurrencyActivityInstrumentedTest() {
       super(CurrencyActivity.class);
    }
    @Before
    public void beforeEach(){
        currencyActivity = null;
        spinner = null;
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        currencyActivity = getActivity();
        spinner = (Spinner) currencyActivity.findViewById(R.id.currency_selection);

        currencyActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Initialise the spinner to the first index
                spinner.setSelection(0);
            }
        });
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSelectedIsEuro(){
        // Make sure the activity has returned
        assertNotNull(currencyActivity);
        spinner = (Spinner) currencyActivity.findViewById(R.id.currency_selection);

        // Expected is Euros
        final String expected = rates[1];

        currencyActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // In the Activity, the 2nd selection should be euros
                spinner.setSelection(1);
                final String actual = spinner.getSelectedItem().toString();
                assertEquals(expected,actual);
            }
        });
        getInstrumentation().waitForIdleSync();

    }

    public void testSelectedIsBritishPound(){
        // Make sure the activity has returned
        assertNotNull(currencyActivity);
        spinner = (Spinner) currencyActivity.findViewById(R.id.currency_selection);

        // Expected is British Pounds
        final String expected = rates[0];

        currencyActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // In the Activity, the 1st selection should be British pounds
                spinner.setSelection(0);
                final String actual = spinner.getSelectedItem().toString();
                assertEquals(expected,actual);
            }
        });
        getInstrumentation().waitForIdleSync();
    }

    public void testSelectedIsUSDollar(){
        // Make sure the activity has returned
        assertNotNull(currencyActivity);
        spinner = (Spinner) currencyActivity.findViewById(R.id.currency_selection);

        // Expected value should be US Dollars
        final String expected = rates[2];

        currencyActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // In the Activity, the last selection should be US Dollars
                spinner.setSelection(2);
                final String actual = spinner.getSelectedItem().toString();
                assertEquals(expected,actual);
            }
        });
        getInstrumentation().waitForIdleSync();

    }


}