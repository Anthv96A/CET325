package com.example.bg71ul.assignment;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Anthony Vest on 15/12/2017.
 */

public class NearbyPlacesHttpClient {


    public String getPlaces(String inputUrl){
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;

        try {
            urlConnection = (HttpURLConnection) (new URL(inputUrl)).openConnection();
            urlConnection.connect();

            int response = urlConnection.getResponseCode();

            Log.d("RESPONSE", String.valueOf(response));

            if(response == HttpURLConnection.HTTP_OK){

                StringBuffer buffer = new StringBuffer();
                inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line = "";

                while ((line = bufferedReader.readLine()) != null){
                        buffer.append(line);
                }

                inputStream.close();
                urlConnection.disconnect();
                Log.d("Places",buffer.toString());
                return buffer.toString();
            } else {
                Log.d("Bad response ", String.valueOf(response));
            }

        } catch (Exception e) {
            Log.d("HttpURLConnection","Unable to connect");
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (Exception e){
                e.printStackTrace();
            }
            try {
                urlConnection.disconnect();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;

    }
}
