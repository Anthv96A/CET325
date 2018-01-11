package com.cet325.bg71ul.assignment.activities;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cet325.bg71ul.assignment.JSONGetNearbyPlacesTask;
import com.cet325.bg71ul.assignment.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class LouvreMapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks,
         LocationListener,AdapterView.OnItemSelectedListener{

    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Spinner googleMapsSpinner = null;
    private ArrayAdapter adapter = null;
    public static final int REQUEST_LOCATION_CODE = 99;
    double latitude,longitude;
    public boolean active = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_louvre_maps);

        this.googleMapsSpinner = (Spinner) findViewById(R.id.google_maps_spinner);
        this.googleMapsSpinner.setOnItemSelectedListener(this);

        this.adapter = ArrayAdapter.createFromResource(this, R.array.places_selection, R.layout.spinner_item);
        this.adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);

        this.googleMapsSpinner.setAdapter(this.adapter);

        if (Build.VERSION.SDK_INT >= 23)
        {
            // Check permissions again when entering this activity
            checkLocationPermission();

        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode)
        {
            case REQUEST_LOCATION_CODE:
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) !=  PackageManager.PERMISSION_GRANTED)
                    {
                        if(client == null)
                        {
                            bulidGoogleApiClient();
                        }

                        onMapReady(mMap);
                    }
                }
                else
                {
                }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // When the map loads, load it on the Louvre Musuem
        Location location = new Location("");
        location.setLatitude(48.86);
        location.setLongitude(2.33);

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        double louvrelatitude = location.getLatitude();
        double louvrelongitude = location.getLongitude();

        MarkerOptions louvreMarkerOptions = new MarkerOptions();
        LatLng louvreLatLng = new LatLng(louvrelatitude, louvrelongitude);
        louvreMarkerOptions.position(louvreLatLng);
        louvreMarkerOptions.title("We are here");
        louvreMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            bulidGoogleApiClient();
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            mMap.addMarker(louvreMarkerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(louvreLatLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        } else{
            checkLocationPermission();
            if(!checkLocationPermission()){
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                mMap.addMarker(louvreMarkerOptions);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(louvreLatLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                active = true;
            }
        }
    }


    protected synchronized void bulidGoogleApiClient() {
        client = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        client.connect();

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    private String[] extractValues(double latitude, double longitude, String nearbyPlace){
        // Extract the latitude and longitude and the searched criteria to do
        // a Google Places API call
        String values[] = new String[3];
        values[0] = Double.toString(latitude);
        values[1] = Double.toString(longitude);
        values[2] = nearbyPlace;

        return values;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }
    }


    public boolean checkLocationPermission()
    {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED )
        {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION },REQUEST_LOCATION_CODE);
            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION },REQUEST_LOCATION_CODE);
            }
            return false;

        }
        else {
            return true;
        }
    }


    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {




        // Programmatically change the text colour of the selected spinner item
        ((TextView) this.googleMapsSpinner.getSelectedView()).setTextColor(getResources().getColor(R.color.blackText));
        ((TextView) this.googleMapsSpinner.getSelectedView()).setGravity(Gravity.CENTER);

        // This just stops the maps immediately start searching when the map loads
        if(!active){
            active = true;
            return;
        }

        // The selected is whatever the spinner value maybe.
        String selected = this.googleMapsSpinner.getSelectedItem().toString();

        if(selected.equals("Shops")){
            selected = "magasins";
        } else if(selected.equals("Hotels")){
            selected = "hôtels";
        }else if(selected.equals("Cafe")){
            selected = "café";
        } else if(selected.equals("Restaurants")){
            selected = "Restaurants";
        }

        // Initialise Object Array
        Object dataTransfer[] = new Object[2];
        // Initialise JSONGetNearbyPlacesTask
        JSONGetNearbyPlacesTask jsonGetNearbyPlacesTask = new JSONGetNearbyPlacesTask();

        // Clear the map ready for the next search
        mMap.clear();
        // Extract the values to be added in a String Array
        Log.d("SELECTED", selected);
        String[] values = extractValues(latitude,longitude, selected);
        // Add Google Maps
        dataTransfer[0] = mMap;
        // Add String array values
        dataTransfer[1] = values;
        // Execute the task for nearby places.
        jsonGetNearbyPlacesTask.execute(dataTransfer);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {


    }
}
