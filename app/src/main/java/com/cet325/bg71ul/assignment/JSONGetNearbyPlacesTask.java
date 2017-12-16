package com.cet325.bg71ul.assignment;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Anthony Vest on 15/12/2017.
 */

public class JSONGetNearbyPlacesTask extends AsyncTask<Object,String,String> {


    private String googlePlacesData;
    private GoogleMap googleMap;
    private String url;
    private String[] values;

    @Override
    protected String doInBackground(Object... objects) {
        this.googleMap = (GoogleMap) objects[0];
        this.values = (String[]) objects[1];

        NearbyPlacesHttpClient nearbyPlacesHttpClient = new NearbyPlacesHttpClient();
        try {
            googlePlacesData = nearbyPlacesHttpClient.getPlaces(values);
        } catch (Exception e){
            e.printStackTrace();
        }

        Log.d("googlePlacesData", googlePlacesData);

        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String,String>> nearbyPlaces;
        PlacesParser placesParser = new PlacesParser();
        nearbyPlaces = placesParser.parse(s);
        showNearByPlaces(nearbyPlaces);
    }

    private void showNearByPlaces(List<HashMap<String,String>> nearByPlaces){

        for(int i = 0; i < nearByPlaces.size(); i++){
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String,String> googlePlace = nearByPlaces.get(i);

            String placeName = googlePlace.get("placeName");
            String vicinity = googlePlace.get("vicinity");
            double latitude = Double.parseDouble( googlePlace.get("lat"));
            double longitude = Double.parseDouble( googlePlace.get("lng"));

            LatLng latLng = new LatLng(latitude,longitude);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " +vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

            this.googleMap.addMarker(markerOptions);
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            this.googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));

        }

    }
}
