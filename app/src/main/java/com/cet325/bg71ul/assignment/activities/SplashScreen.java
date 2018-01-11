package com.cet325.bg71ul.assignment.activities;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.cet325.bg71ul.assignment.JSONCurrencyTask;
import com.cet325.bg71ul.assignment.MuseumDBOpenHelper;
import com.cet325.bg71ul.assignment.MuseumProvider;
import com.cet325.bg71ul.assignment.R;
import com.cet325.bg71ul.assignment.models.CurrencyRate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SplashScreen extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {


    // Splash screen self executed method duration
    private static final int DURATION = 1000;
    private static final int WRITE_TO_EXTERNAL_STORAGE = 0;
    public static final int REQUEST_LOCATION_CODE = 99;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        // Delayed self executed method, to check for permissions
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                try{
                    // If API level is over 23, ask permissions
                    if(Build.VERSION.SDK_INT >= 23 ){
                        checkPermissions();
                    }

                    if(ContextCompat.checkSelfPermission(getApplicationContext(),
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

                        // If the database is not empty, continue. We don't want to keep pre-populating.
                        if(getContentResolver().query(MuseumProvider.CONTENT_URI,null,null,null,null).getCount() != 0){

                            // This is the default Shared Preferences
                            SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

                            // This is the Shared Preferences that will store different rates
                            SharedPreferences storedRates = getSharedPreferences("rates",Context.MODE_PRIVATE);
                            // Getting ready to edit different rates for each currency
                            SharedPreferences.Editor editor = storedRates.edit();

                            // Get Local currency as we need new rates every time the app loads.
                            String localCurrency = mSharedPreferences.getString("localCurrency","DEFAULT");
                            // List of currency rates
                            List<CurrencyRate> localCurrencyRates = new ArrayList<CurrencyRate>();

                            try{
                                // Initialise JSON Currency Task
                                JSONCurrencyTask jsonCurrencyTask = new JSONCurrencyTask();
                                // Ensure the currency task completes
                                Thread sleeperThread = new Thread();
                                try {
                                    // Sleep for half a second
                                    sleeperThread.sleep(500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                // Checking what is coming back from Async Event
                                for(CurrencyRate cr: jsonCurrencyTask.fetchCurrencyRates(
                                        getResources().getStringArray(R.array.currencies),
                                        localCurrency) ){
                                    Log.d("For each cr type ", cr.getCurrencyType());
                                    Log.d("For each cr ", String.valueOf(cr.getCurrencyRate()));
                                }

                                // Add all the types and rates from Async event to list
                                localCurrencyRates.addAll(jsonCurrencyTask.fetchCurrencyRates(
                                        getResources().getStringArray(R.array.currencies),
                                        localCurrency));

                                // This loops will store the current rates which comes back,
                                // However, if the rates fail, we want to keep what we previously stored
                                // so if the rates come back as 0.0, we will still have previous rates
                                for(CurrencyRate rate: localCurrencyRates){
                                    if(rate.getCurrencyType().equals("GBP")){
                                        if(rate.getCurrencyRate() != 0.0){
                                            editor.putString("previousGBP", rate.getCurrencyType());
                                            editor.putFloat("previousGBPRate", rate.getCurrencyRate());
                                            editor.apply();
                                        }
                                    }

                                    if(rate.getCurrencyType().equals("EUR")){
                                        if(rate.getCurrencyRate() != 0.0){
                                            editor.putString("previousEUR", rate.getCurrencyType());
                                            editor.putFloat("previousEURRate", rate.getCurrencyRate());
                                            editor.apply();
                                        }
                                    }

                                    if(rate.getCurrencyType().equals("USD")){
                                        if(rate.getCurrencyRate() != 0.0){
                                            editor.putString("previousUSD", rate.getCurrencyType());
                                            editor.putFloat("previousUSDRate", rate.getCurrencyRate());
                                            editor.apply();
                                        }
                                    }
                                }

                            } catch (Exception e){
                                e.printStackTrace();

                            } finally {
                                // Finally we will start the main activity with the local currency rates
                                Intent mainMenuIntent = new Intent(getApplicationContext(), MainMenu.class);
                                mainMenuIntent.putExtra("localCurrencyRates", (Serializable) localCurrencyRates);
                                // Start Activity
                                startActivity(mainMenuIntent);
                                // Finish SplashScreen Activity
                                finish();
                            }

                        }
                    }

                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, DURATION );

    }


    public void checkPermissions(){

        // Permissions needed
        String[] permissions = new String[]{
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        int result;
        // New list of permissions needed
        List<String> permissionsNeeded = new ArrayList<>();

        // Add permissions we need
        for(String permission: permissions){
            result = ContextCompat.checkSelfPermission(this, permission);
            if(result != PackageManager.PERMISSION_GRANTED){
                permissionsNeeded.add(permission);
            }
        }

        // Convert list into array
        String[] permissionsArray = permissionsNeeded.toArray( new String[permissionsNeeded.size()]);

        // If the list is not empty, we need to request user permission
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

                    if(getContentResolver().query(MuseumProvider.CONTENT_URI,null,null,null,null).getCount() == 0) {
                        initialiseDatabase();

                        SharedPreferences storedRates = getSharedPreferences("rates",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = storedRates.edit();

                        // On start up, set up the default currencies
                        PreferenceManager.getDefaultSharedPreferences(this).edit()
                                .putString("localCurrency", "EUR").apply();

                        // We need to make sure on the first run, the selected currency is in Euros too
                        PreferenceManager.getDefaultSharedPreferences(this).edit()
                                .putString("yourCurrency", "EUR").apply();

                        // Create default ticket price (In Euros)
                        PreferenceManager.getDefaultSharedPreferences(this).edit()
                                .putString("ticketPrice", "20").apply();

                        PreferenceManager.getDefaultSharedPreferences(this).edit()
                                .putString("studentDiscount", "30").apply();

                        // Fetch the local currency Euros
                        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                        String localCurrency = mSharedPreferences.getString("localCurrency","DEFAULT");
                        // List of currency rates
                        List<CurrencyRate> localCurrencyRates = new ArrayList<CurrencyRate>();

                        try{
                            JSONCurrencyTask jsonCurrencyTask = new JSONCurrencyTask();
                            // Ensure the currency task completes
                            Thread sleeperThread = new Thread();
                            try {
                                // Sleep thread for one second
                                sleeperThread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            // Add all rates and types to list
                            localCurrencyRates.addAll(jsonCurrencyTask.fetchCurrencyRates(
                                    getResources().getStringArray(R.array.currencies),
                                    localCurrency));

                            // If the app runs for the first time, we need to set default values if
                            // the API call fails.
                            for(CurrencyRate rate: localCurrencyRates){
                                    if(rate.getCurrencyType().equals("GBP")){
                                        if(rate.getCurrencyRate() == 0.0){
                                            editor.putString("previousGBP", "GBP");
                                            editor.putFloat("previousGBPRate", 0.88f);
                                            editor.apply();
                                        }
                                    }
                                    if(rate.getCurrencyType().equals("EUR")){
                                        if(rate.getCurrencyRate() == 0.0){
                                            editor.putString("previousEUR", "EUR");
                                            editor.putFloat("previousEURRate", 1f);
                                            editor.apply();
                                        }
                                    }

                                    if(rate.getCurrencyType().equals("USD")){
                                        if(rate.getCurrencyRate() == 0.0){
                                            editor.putString("previousUSD", "USD");
                                            editor.putFloat("previousUSDRate", 1.17f);
                                            editor.apply();
                                        }
                                    }
                            }

                        } catch (Exception e){
                            e.printStackTrace();

                        } finally {
                            // Start up the main menu activity and pass in the new rates
                            Intent mainMenuIntent = new Intent(getApplicationContext(), MainMenu.class);
                            mainMenuIntent.putExtra("localCurrencyRates", (Serializable) localCurrencyRates);
                            startActivity(mainMenuIntent);
                            // Finish Splash Screen Activity
                            finish();
                        }

                    }

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

        // Pre-loaded data at run time.

        // 1.
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

        // 2.
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
        // 3.
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
        // 4.
        createGallery(
                "Jan van Eyck",
                "Madonna of Chancellor Rolin",
                "The scene depicts the Virgin Mary crowned by a hovering Angel while she presents the Infant Jesus to Rolin. It is set within a spacious Italian-style loggia with a rich decoration of columns and bas-reliefs. In the background is a landscape with a city on a river, probably intended to be Autun in Burgundy, Rolin's hometown. "+"" +
                        " A wide range of well detailed palaces, churches, an island, a towered bridge, hills and fields is portrayed, subject to a uniform light. Perhaps some of the Chancellor's many landholdings around Autun are included in the vista",
                "Room 22",
                0,
                1435,
                R.drawable.chancellor_rolin
        );

        // 5.
        createGallery(
                "Eugène Delacroix",
                "Liberty Leading the People",
                "Liberty Leading the People (French: La Liberté guidant le peuple [la libɛʁte ɡidɑ̃ lə pœpl]) is a painting by Eugène Delacroix commemorating the July Revolution of 1830, which toppled King Charles X of France. A woman personifying the concept and the Goddess of Liberty leads the people forward over a barricade and the bodies of the fallen, holding the flag of the French Revolution – the tricolour flag, which remains France's national flag – in one hand and brandishing a bayonetted musket with the other. The figure of Liberty is also viewed as a symbol of France and the French Republic known as Marianne.",
                "Room 100",
                4,
                1830,
                R.drawable.liberty
        );

        // 6.
        createGallery(
                "Leonardo Da Vinci",
                "La belle ferronnièr",
                "La belle ferronnière is a portrait of a lady, usually attributed to Leonardo da Vinci, in the Louvre. It is also known as Portrait of an Unknown Woman. The painting's title, applied as early as the seventeenth century, identifying the sitter as the wife or daughter of an ironmonger (a ferronnier), was said to be discreetly alluding to a reputed mistress of Francis I of France, married to a certain Le Ferron. The tale is a romantic legend of revenge in which the aggrieved husband intentionally infects himself with syphilis, which he passes to the king through infecting his wife.",
                "Salle des Etats",
                5,
                1490,
                R.drawable.belle
        );

        // 7.
        createGallery(
                "Eugène Delacroix",
                "The Death of Sardanapalus",
                "The main focus of Death of Sardanapalus is a large bed draped in rich red fabric. On it lies a man overseeing a scene of chaos with a disinterested eye. He is dressed in flowing white fabrics and sumptuous gold around his neck and head. A woman lies dead at his feet, prone across the lower half of the large bed. She is one of five or six in the scene, all in various shades of undress, and all in assorted throes of death by the hands of the half dozen men in the scene. There are several people being stabbed with knives and one man is dying from a self-inflicted wound from a sword, and a man in the left foreground is attempting to kill an intricately adorned horse. A young man by the king’s right elbow is standing behind a side table which has an elaborate golden decanter and a cup. There are golden elephant heads at the base of the bed, as well as various valuable trinkets scattered amongst the carnage. In the background, several architectural elements are visible but difficult to discern.",
                "Room 100",
                4,
                1827,
                R.drawable.sardanapalus
        );

        // 8.
        createGallery(
                "Paolo Veronese",
                "The Wedding at Cana",
                "The Wedding Feast at Cana (1563), by Paolo Veronese, is a representational painting that depicts the Bible story of the Marriage at Cana, a wedding banquet at which Jesus converts water to wine (John 2:1–11). The work is a large-format (6.77 m × 9.94 m) oil painting executed in the Mannerist style of the High Renaissance (1490–1527); as such, The Wedding Feast at Cana is the most expansive canvas (67.29 m2) in the paintings collection of the Musée du Louvre.",
                "Room 21",
                5,
                1563,
                R.drawable.cana
        );

        // 9.
        createGallery(
                "Théodore Géricault",
                "The Raft of the Medusa",
                "The Raft of the Medusa (French: Le Radeau de la Méduse [lə ʁado d(ə) la medyz]) is an oil painting of 1818–1819 by the French Romantic painter and lithographer Théodore Géricault (1791–1824). Completed when the artist was 27, the work has become an icon of French Romanticism. At 491 cm × 716 cm , it is an over-life-size painting that depicts a moment from the aftermath of the wreck of the French naval frigate Méduse, which ran aground off the coast of today's Mauritania on 2 July 1816. On 5 July 1816, at least 147 people were set adrift on a hurriedly constructed raft; all but 15 died in the 13 days before their rescue, and those who survived endured starvation and dehydration and practised cannibalism. The event became an international scandal, in part because its cause was widely attributed to the incompetence of the French captain.",
                "Room 82",
                3,
                1819,
                R.drawable.medusa
        );

        // 10.
        createGallery(
                "Jacques-Louis David",
                "The Intervention of the Sabine Women",
                "The Intervention of the Sabine Women is a 1799 painting by the French painter Jacques-Louis David, showing a legendary episode following the abduction of the Sabine women by the founding generation of Rome. Work on the painting commenced in 1796, after his estranged wife visited him in jail. He conceived the idea of telling the story, to honour his wife, with the theme being love prevailing over conflict. The painting was also seen as a plea for the people to reunite after the bloodshed of the revolution. Its realization took him nearly four years.",
                "Room 92",
                3,
                1799,
                R.drawable.sabine
        );

    }

    private void createGallery(String artist,String title,String description, String room, double rank, int year, int pictureId) {

        // We will set zero in the database for later to say that the pre-loaded records can't be edited fully
        final int notEditable = 0;

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
        values.put(MuseumDBOpenHelper.DB_KEY_EDITABLE, notEditable);

        getContentResolver().insert(MuseumProvider.CONTENT_URI, values);
    }



}
