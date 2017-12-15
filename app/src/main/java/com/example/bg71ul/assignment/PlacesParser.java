package com.example.bg71ul.assignment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Anthony Vest on 15/12/2017.
 */

public class PlacesParser {

    private HashMap<String,String> getPlace(JSONObject googlePlaceJSON){
        HashMap<String,String> googlePlacesMap = new HashMap<>();
        String placeName = "-NA-";
        String vicinity = "-NA-";
        String latitude = "";
        String longitude = "";
        String reference = "";

        try{
            if(!googlePlaceJSON.isNull("name")){
                placeName = googlePlaceJSON.getString("name");
            }

            if(!googlePlaceJSON.isNull("vicinity")){
                vicinity = googlePlaceJSON.getString("vicinity");
            }

            latitude = googlePlaceJSON.getJSONObject("geometry")
                    .getJSONObject("location")
                    .getString("lat");


            longitude = googlePlaceJSON.getJSONObject("geometry")
                    .getJSONObject("location")
                    .getString("lng");

            reference = googlePlaceJSON.getString("reference");

            // Extracting the values from the user input string into hash map
            googlePlacesMap.put("placeName", placeName);
            googlePlacesMap.put("vicinity", vicinity);
            googlePlacesMap.put("lat", latitude);
            googlePlacesMap.put("lng", longitude);
            googlePlacesMap.put("reference", reference);


        } catch (Exception e){
            e.printStackTrace();
        }

        return googlePlacesMap;
    }

    private List<HashMap<String,String>> getPlaces(JSONArray jsonArray){

        int count = jsonArray.length();
        List<HashMap<String,String>> places = new ArrayList<>();
        HashMap<String,String> place = null;

        for(int i =0; i < count; i++){
            try {
                place = this.getPlace((JSONObject) jsonArray.get(i));
                places.add(place);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        return places;
    }

    public List<HashMap<String,String>> parse(String jsonData){

        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try{
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (Exception e){
            e.printStackTrace();
        }
        return getPlaces(jsonArray);

    }
}
