package com.cet325.bg71ul.assignment;

import android.location.Location;
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
    private String[] values;

    @Override
    protected String doInBackground(Object... objects) {

        // Extract and cast values
        this.googleMap = (GoogleMap) objects[0];
        this.values = (String[]) objects[1];

        // Initialise HTTP Client to get nearby places
        NearbyPlacesHttpClient nearbyPlacesHttpClient = new NearbyPlacesHttpClient();
        try {
            // Try and fetch the places based on the values passed
            googlePlacesData = nearbyPlacesHttpClient.getPlaces(values);
        } catch (Exception e){
            e.printStackTrace();
        }


        if(googlePlacesData != null){
            Log.d("googlePlacesData", googlePlacesData);

            return googlePlacesData;
        }

        return "Could not fetch data";

    }

    @Override
    protected void onPostExecute(String s) {
        List<HashMap<String,String>> nearbyPlaces;
        PlacesParser placesParser = new PlacesParser();
        // Extract values from JSON Object
        nearbyPlaces = placesParser.parse(s);
        // Loop through the extracted values and put them on the map
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
            this.googleMap.animateCamera(CameraUpdateFactory.zoomTo(14));

        }

        Location location = new Location("");
        location.setLatitude(48.86);
        location.setLongitude(2.33);

        double louvrelatitude = location.getLatitude();
        double louvrelongitude = location.getLongitude();

        MarkerOptions louvreMarkerOptions = new MarkerOptions();
        LatLng louvreLatLng = new LatLng(louvrelatitude, louvrelongitude);
        louvreMarkerOptions.position(louvreLatLng);
        louvreMarkerOptions.title("We are here");
        louvreMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        this.googleMap.addMarker(louvreMarkerOptions);
    }
}
