package com.example.bg71ul.assignment.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.bg71ul.assignment.MuseumDBOpenHelper;
import com.example.bg71ul.assignment.MuseumProvider;
import com.example.bg71ul.assignment.R;
import com.example.bg71ul.assignment.activities.MainMenu;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
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

        // Mona Lisa
       createGallery(
               "Leonardo Da Vinci",
               "Mona Lisa",
               "The Mona Lisa is arguably one of the most famous paintings of all time, it took Leonardo 14 years to create! " +
                       "The painting is thought to be a portrait of Lisa Gherardini, the wife of Francesco del Giocondo, and is in oil on a white Lombardy poplar panel."
               ,
               "Salle des Etats",
               5,
               1517,
               R.drawable.mona_lisa
               );

        createGallery(
                "Titian",
                "Portrait of Francis I",
                "Son of Charles d'Angoulême and Louise de Savoie, and cousin of King Louis XII whom he succeeded to the throne in 1515, François I wears the pendant of the Order of Saint Michael," + "" +
                        " which he served as Grand Master. The king's face is identical to an existing drawing by Clouet (Musée Condé, Chantilly). The modernity of the portrait is evident in the monumental and physical character of the bust, as well as the importance given to the hands; the date of execution has been judged at 1527-30.",
                "Room 5",
                3.5,
                1539,
                R.drawable.francois_the_first
        );
        createGallery(
                "Johannes Vermeer",
                "The Astronomer",
                "Between 1881 and 1888 it was sold by the Paris art dealer Léon Gauchez to the banker and art collector Alphonse James de Rothschild, after whose death it was inherited by his son, Édouard Alphonse James de Rothschild. In 1940 it was seized from his hotel in Paris by the Nazi Einsatzstab Reichsleiter Rosenberg für die Besetzten Gebiete after the German invasion of France."+"" +
                        " A small swastika was stamped on the back in black ink.",
                "Room 27",
                4,
                1668,
                R.drawable.the_astronomer
        );
        createGallery(
                "Jan van Eyck",
                "Madonna of Chancellor Rolin",
                "The scene depicts the Virgin Mary crowned by a hovering Angel while she presents the Infant Jesus to Rolin. It is set within a spacious Italian-style loggia with a rich decoration of columns and bas-reliefs. In the background is a landscape with a city on a river, probably intended to be Autun in Burgundy, Rolin's hometown. "+"" +
                        " A wide range of well detailed palaces, churches, an island, a towered bridge, hills and fields is portrayed, subject to a uniform light. Perhaps some of the Chancellor's many landholdings around Autun are included in the vista",
                "Room 22",
                1.5,
                1435,
                R.drawable.chancellor_rolin
        );
    }

    private void createGallery(String artist,String title,String description, String room, double rank, int year, int pictureId) {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), pictureId);
        bitmap = Bitmap.createScaledBitmap(bitmap,600,400,false);
        OutputStream output;
        String filename = title + ".jpg";



        // Attempt to save file
        final File file = new File(getApplicationContext().getExternalFilesDir
                (Environment.DIRECTORY_PICTURES), filename);

        try{
            output = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, output);
            output.flush();
            output.close();
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Error: Cannot create file", Toast.LENGTH_SHORT).show();
        }



        ContentValues values = new ContentValues();
        values.put(MuseumDBOpenHelper.DB_KEY_ARTIST, artist);
        values.put(MuseumDBOpenHelper.DB_KEY_TITLE, title);
        values.put(MuseumDBOpenHelper.DB_KEY_ROOM, room);
        values.put(MuseumDBOpenHelper.DB_KEY_DESCRIPTION, description);
        values.put(MuseumDBOpenHelper.DB_KEY_RANK, rank);
        values.put(MuseumDBOpenHelper.DB_KEY_YEAR, year);
        values.put(MuseumDBOpenHelper.DB_KEY_IMAGE, filename);

        getContentResolver().insert(MuseumProvider.CONTENT_URI, values);
    }


}
