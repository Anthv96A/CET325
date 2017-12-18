package com.cet325.bg71ul.assignment;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Anthony Vest on 13/12/2017.
 */

public class CurrencyHttpClient {
    //Fixer IO URL
    private final String BASE_URL = "http://api.fixer.io/latest?base=";
    private String urlString;

    public String getCurrencyData(String rate){
        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            // Pass in the rate, which will be Euros
            urlString = BASE_URL + URLEncoder.encode(rate, "utf-8");
            Log.d("URL String", urlString);
        } catch (Exception e){
            e.printStackTrace();
        }
        try {
            // Attempt to start a new connection
            connection = (HttpURLConnection) (new URL(urlString)).openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int response = connection.getResponseCode();

            Log.d("Response code", String.valueOf(response));

            // If we have a response code of 200, proceed
            if(response == HttpURLConnection.HTTP_OK){
                StringBuilder buffer = new StringBuilder();
                inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    buffer.append(line + "\r\n");
                }

                inputStream.close();
                connection.disconnect();
                Log.d("JSON",buffer.toString());
                // Return rates
                return buffer.toString();
            } else{
                Log.d("HttpURLConnection","Unable to connect");
                return null;
            }

        } catch (Exception e){
            Log.d("HttpURLConnection","Unable to connect");
            e.printStackTrace();
        } finally {
            // Finally close try to close input stream and connection
            try {
                inputStream.close();
            } catch (Exception e){
                e.printStackTrace();
            }
            try {
                connection.disconnect();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }
}