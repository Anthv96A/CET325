package com.example.bg71ul.assignment;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SplashScreen extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {


    private static final int DURATION = 5000;

    private static final int WRITE_TO_EXTERNAL_STORAGE = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        // Delayed self excuted method, to check for permissions
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                try{
                    if(Build.VERSION.SDK_INT >= 23 ){
                        checkPermissions();
                    }

                    if(ContextCompat.checkSelfPermission(getApplicationContext(),
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

                        Intent mainMenuIntent = new Intent(getApplicationContext(), MainMenu.class);
                        startActivity(mainMenuIntent);
                        finish();
                    }



                } catch (Exception e){
                    e.printStackTrace();

                }


            }
        }, DURATION );

    }


    public void checkPermissions(){

        String[] permissions = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

        int result;
        List<String> permissionsNeeded = new ArrayList<>();

        for(String permission: permissions){
            result = ContextCompat.checkSelfPermission(this, permission);
            if(result != PackageManager.PERMISSION_GRANTED){
                permissionsNeeded.add(permission);
            }
        }

        String[] permissionsArray = permissionsNeeded.toArray( new String[permissionsNeeded.size()]);

        if(!permissionsNeeded.isEmpty()){
            ActivityCompat.requestPermissions(this,permissionsArray,WRITE_TO_EXTERNAL_STORAGE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case WRITE_TO_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if(getContentResolver().query(MuseumProvider.CONTENT_URI,null,null,null,null).getCount() == 0){
                        initialiseDatabase();
                    }

                    Intent mainMenuIntent = new Intent(getApplicationContext(), MainMenu.class);
                    startActivity(mainMenuIntent);
                    finish();

                } else {
                    Toast.makeText(this, "Permissions Denied, must accept to continue", Toast.LENGTH_LONG).show();
                    // Ensure permissions are accepted before we can continue any further.
                    checkPermissions();
                }
                return;
            }

        }
    }

    private void initialiseDatabase() {
       createGallery(
               "Leonardo Da Vinci",
               "Mona Lisa",
               "The Mona Lisa is argubly one of the most famous paintings of all time, it took Leonardo 14 years to create!",
               "Room 134",
               5,
               1665);
    }

    private void createGallery(String artist,String title,String description, String room, int rank, int year) {
        ContentValues values = new ContentValues();
        values.put(MuseumDBOpenHelper.DB_KEY_ARTIST, artist);
        values.put(MuseumDBOpenHelper.DB_KEY_TITLE, title);
        values.put(MuseumDBOpenHelper.DB_KEY_ROOM, room);
        values.put(MuseumDBOpenHelper.DB_KEY_DESCRIPTION, description);
        values.put(MuseumDBOpenHelper.DB_KEY_RANK, rank);
        values.put(MuseumDBOpenHelper.DB_KEY_YEAR, year);

        getContentResolver().insert(MuseumProvider.CONTENT_URI, values);
    }


}
