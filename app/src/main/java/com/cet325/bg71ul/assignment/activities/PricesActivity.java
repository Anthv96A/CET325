package com.cet325.bg71ul.assignment.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.cet325.bg71ul.assignment.CurrencyConverter;
import com.cet325.bg71ul.assignment.R;
import com.cet325.bg71ul.assignment.models.CurrencyRate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class PricesActivity extends AppCompatActivity {

    public static String localCurrency;
    public static String yourCurrency;
    public static List<CurrencyRate> currencyRates = new ArrayList<>();
    public static double ticketPrice = 0;
    public static double studentDiscount = 0;
    private boolean usingDefaultRates = false;
    private SharedPreferences backUpRates = null;
    private SharedPreferences currencyPreferences = null;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prices);

        backUpRates = getSharedPreferences("rates", Context.MODE_PRIVATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        currencyRates = (List<CurrencyRate>) intent.getSerializableExtra("localCurrencyRates");

        // Checking to see if the currency rates are zero,
        // if they are zero, the REST API failed to fetch the latest online rates
        // So we will default back to the previous rates.
        for(CurrencyRate cr: currencyRates){
            if(cr.getCurrencyRate() == 0.0){
                if(cr.getCurrencyType().equals("GBP")){
                    cr.setCurrencyRate(backUpRates.getFloat("previousGBPRate",0.0f));
                }
                if(cr.getCurrencyType().equals("EUR")){
                    cr.setCurrencyRate(backUpRates.getFloat("previousGBPRate",0.0f));
                }
                if(cr.getCurrencyType().equals("USD")){
                    cr.setCurrencyRate(backUpRates.getFloat("previousGBPRate",0.0f));
                }
                usingDefaultRates = true;
            }
        }
        // If we have failed to fetch latest rates, so we will
        // notify user that we will use previous rates
        if(usingDefaultRates){
            Snackbar.make(findViewById(android.R.id.content),
                    "Unable to fetch latest currency rates, defaulting to previous rates",
                    Snackbar.LENGTH_LONG).show();
        }

        Log.d("Prices Activity", currencyRates.toString());

        currencyPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        yourCurrency = this.currencyPreferences.getString("yourCurrency","DEFAULT");
        localCurrency = this.currencyPreferences.getString("localCurrency","DEFAULT");

        // Extract ticket price from shared preference
        String ticketPriceString = this.currencyPreferences.getString("ticketPrice", "DEFAULT");
        ticketPrice = Double.parseDouble(ticketPriceString);

        // Extract student discount from shared preference
        String studentDiscountString = this.currencyPreferences.getString("studentDiscount","DEFAULT");
        studentDiscount = Double.parseDouble(studentDiscountString);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            // Create a new instance of Decimal Format
            DecimalFormat df = new DecimalFormat();
            // Create a new instance of currency converter
            CurrencyConverter currencyConverter = new CurrencyConverter();
            // Get the fragment that will display princes
            View rootView = inflater.inflate(R.layout.fragment_prices, container, false);
            //Show local currency
            TextView localCurrencyTextView = (TextView) rootView.findViewById(R.id.localRates);
            localCurrencyTextView.setText(localCurrency);
            //Show chosen currency
            TextView yourCurrencyTextView = (TextView) rootView.findViewById(R.id.yourCurrency);
            yourCurrencyTextView.setText(yourCurrency);

            // If the chosen currency is GBP, create new instance of decimal format in Pounds
            if(yourCurrency.equals("GBP")){
                df = new DecimalFormat("'£'0.00");
            }
            // If the chosen currency is EUR, create new instance of decimal format in Euros
            if(yourCurrency.equals("EUR")){
                df = new DecimalFormat("'€'0.00");
            }
            // If the chosen currency is USD, create new instance of decimal format in US Dollars
            if(yourCurrency.equals("USD")){
                df = new DecimalFormat("'$'0.00");
            }

            // This handles which tab we are currently on
            if(getArguments().getInt(ARG_SECTION_NUMBER) == 1){
                // Student Tab
                double totalprice = currencyConverter.calculateStudentPrices(localCurrency, yourCurrency, currencyRates, ticketPrice,studentDiscount);
                TextView pricesTextView = (TextView) rootView.findViewById(R.id.priceCost);
                pricesTextView.setText(String.valueOf(df.format(totalprice)));
            }

            if(getArguments().getInt(ARG_SECTION_NUMBER) == 2){
                // Adult Tab
                double totalprice = currencyConverter.calculatePrices(localCurrency, yourCurrency,currencyRates, ticketPrice);
                TextView pricesTextView = (TextView) rootView.findViewById(R.id.priceCost);
                pricesTextView.setText(String.valueOf(df.format(totalprice)));
            }

            if(getArguments().getInt(ARG_SECTION_NUMBER) == 3){
                // Under 18's tab
                TextView pricesTextView = (TextView) rootView.findViewById(R.id.priceCost);
                // Under 18's is free
                pricesTextView.setText(String.valueOf(df.format(0)));
            }
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Displays Page titles
            switch (position) {
                case 0:
                    return "Student";
                case 1:
                    return "Adult";
                case 2:
                    return "Under 18s";
            }
            return null;
        }
    }



}
