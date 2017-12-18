package com.cet325.bg71ul.assignment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Anthony Vest on 15/12/2017.
 */

public class PlacesParser {

    // This class extracts values from JSON Objects

    private HashMap<String,String> getPlace(JSONObject googlePlaceJSON){
        HashMap<String,String> googlePlacesMap = new HashMap<>();
        String placeName = "-NA-";
        String vicinity = "-NA-";
        String latitude;
        String longitude;
        String reference;

        try{
            // if the name of place is not null, extract to be added to hash map
            if(!googlePlaceJSON.isNull("name")){
                placeName = googlePlaceJSON.getString("name");
            }
            // if the name of vicinity is not null, extract to be added to hash map
            if(!googlePlaceJSON.isNull("vicinity")){
                vicinity = googlePlaceJSON.getString("vicinity");
            }

            // Extract latitude
            latitude = googlePlaceJSON.getJSONObject("geometry")
                    .getJSONObject("location")
                    .getString("lat");

            // Extract longitude
            longitude = googlePlaceJSON.getJSONObject("geometry")
                    .getJSONObject("location")
                    .getString("lng");

            reference = googlePlaceJSON.getString("reference");

            // Extracting the values google based on user input string into hash map
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


    public List<HashMap<String,String>> parse(String jsonData){

        List<HashMap<String,String>> places = new ArrayList<>();
        JSONArray jsonArray = null;
        JSONObject jsonObject;
        try{
            // Transform into JSON Object
            jsonObject = new JSONObject(jsonData);
            // Transform into JSON Array
            jsonArray = jsonObject.getJSONArray("results");
        } catch (Exception e){
            e.printStackTrace();
        }

        for(int i = 0; i < jsonArray.length(); i++){
            try{
                // Pass in JSON array to extract into hash map
                places.add(this.getPlace((JSONObject) jsonArray.get(i)));
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return places;

    }
}
