package com.example.bg71ul.assignment;

import android.util.Log;
import android.widget.Toast;

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
    private final String BASE_URL = "http://api.fixer.io/latest?base=";
    private String urlString;

    public String getCurrencyData(String rate){
        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            urlString = BASE_URL + URLEncoder.encode(rate, "utf-8");
            Log.d("URL String", urlString);
        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            connection = (HttpURLConnection) (new URL(urlString)).openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int response = connection.getResponseCode();

            Log.d("Response code", String.valueOf(response));

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
                return buffer.toString();
            } else{
                Log.d("HttpURLConnection","Unable to connect");
                return null;
            }

        } catch (Exception e){
            Log.d("HttpURLConnection","Unable to connect");
            e.printStackTrace();
        } finally {
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