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

    private static final String GOOGLE_PLACES_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private static String location;
    private static final String distanceRadius = "&radius=10000";
    private static String nearbyPlace;
    private static final String sensor = "&sensor=true";
    private static final String API_KEY = "&key=AIzaSyBU58QUYxRfxD3zYgIy5asnxV-96mYVjYk";
    private static String finalURL;


    public String getPlaces(String[] values){
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;

        location = "location="+ values[0]+ ","+ values[1];
        nearbyPlace = "&type="+ values[2];

        finalURL = GOOGLE_PLACES_URL + location + distanceRadius + nearbyPlace + sensor + API_KEY;
        Log.d("Passed url", finalURL);

        try {
            urlConnection = (HttpURLConnection) (new URL(finalURL)).openConnection();
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
