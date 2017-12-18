package com.cet325.bg71ul.assignment.activities;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.cet325.bg71ul.assignment.models.Gallery;
import com.cet325.bg71ul.assignment.MuseumCursorAdapter;
import com.cet325.bg71ul.assignment.MuseumDBOpenHelper;
import com.cet325.bg71ul.assignment.MuseumProvider;
import com.cet325.bg71ul.assignment.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Calendar;

public class GalleryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener{
    private CursorAdapter cursorAdapter = null;
    private ListView list;
    public static int checker = 0;
    private static int outOutBoundsYear = 0;
    private int selected;
    final ViewGroup nullParent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        restartLoader();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = (ListView)findViewById(R.id.listView_gallery);
        list.setEmptyView(findViewById(R.id.empty));
        list.setClickable(true);

        list.setOnItemClickListener(this.clicked);

        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, final long id){

                LayoutInflater li = LayoutInflater.from(GalleryActivity.this);
                View getDeleteDialog = li.inflate(R.layout.delete_gallery,nullParent);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GalleryActivity.this);
                alertDialogBuilder.setView(getDeleteDialog);

                alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("Delete", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int whichButton) {
                        deleteGallery(id, position);
                    }
                }).create().show();
                return true;
            }

        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //set up so it shows all when opening activity
        Menu menu = navigationView.getMenu();
        MenuItem all = menu.findItem(R.id.allGallery);
        onNavigationItemSelected(all);
    }


    private void createNewGallery(Gallery newGallery, View view){
        final int editable = 1;
        final int initialRank = 0;

        ContentValues values = new ContentValues();
        values.put(MuseumDBOpenHelper.DB_KEY_ARTIST, newGallery.getArtist());
        values.put(MuseumDBOpenHelper.DB_KEY_TITLE, newGallery.getTitle());
        values.put(MuseumDBOpenHelper.DB_KEY_ROOM, newGallery.getRoom());
        values.put(MuseumDBOpenHelper.DB_KEY_DESCRIPTION, newGallery.getDescription());
        values.put(MuseumDBOpenHelper.DB_KEY_YEAR, newGallery.getYear());
        values.put(MuseumDBOpenHelper.DB_KEY_IMAGE, setUpDefaultImage());
        values.put(MuseumDBOpenHelper.DB_KEY_EDITABLE, editable);
        values.put(MuseumDBOpenHelper.DB_KEY_RANK, initialRank);

        getContentResolver().insert(MuseumProvider.CONTENT_URI, values);
        Snackbar snackbar = Snackbar.make(view,"New Gallery created",Snackbar.LENGTH_SHORT);
        snackbar.setAction("Action", null).show();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem all = menu.findItem(R.id.allGallery);
        onNavigationItemSelected(all);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private void deleteGallery(long id, int position){
        String whereClause = MuseumDBOpenHelper.DB_KEY_ID + "= '" + id + "'";
        Cursor cursor = (Cursor) list.getItemAtPosition(position);
        boolean isEditable = (cursor.getInt(cursor.getColumnIndex(MuseumDBOpenHelper.DB_KEY_EDITABLE)) == 1);

        if(!isEditable){

            Toast.makeText(this, "You cannot delete this record", Toast.LENGTH_SHORT).show();
            return;

        }

        getContentResolver().delete(MuseumProvider.CONTENT_URI,whereClause, null);
        Toast.makeText(this, "Record deleted", Toast.LENGTH_SHORT).show();
        restartLoader();

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        switch (id){
            case R.id.goBack:
                Intent mainMenu = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(mainMenu);
                break;
            case R.id.allGallery:
                makeQuery(0);
                break;
            case R.id.allRanked:
                makeQuery(1);
                break;
            case R.id.allNotRanked:
                makeQuery(2);
                break;
            case R.id.sortArtistAndTitle:
                makeQuery(3);
                break;
            case R.id.sortTitle:
                makeQuery(4);
                break;
            case R.id.sortRank:
                makeQuery(5);
                break;
        }

        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, MuseumProvider.CONTENT_URI,null,null,null,null);
    }

    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    // This method is responsible for all the different query criteria
    public void makeQuery(int index){
        this.list = (ListView) findViewById(R.id.listView_gallery);

        MuseumDBOpenHelper museumDBOpenHelper = new MuseumDBOpenHelper(this);
        SQLiteDatabase database = museumDBOpenHelper.getReadableDatabase();

        // Make sure table is immutable
        final String Database_table = MuseumDBOpenHelper.MUSEUM_TABLE_NAME;
        String[] columns = {"*"};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String where = null;
        String[] selectionArgs = null;

        switch (index){
            case 0:
                // Show all nothing is required here as this is only a select all statement
                this.selected = 1;
                break;
            case 1:
                // Select all from database who have been ranked
                where =  MuseumDBOpenHelper.DB_KEY_RANK + " != ?";
                selectionArgs = new String[]{"0"};
                this.selected = 2;
                break;
            case 2:
                // Select all from database who have NOT been ranked
                where =  MuseumDBOpenHelper.DB_KEY_RANK + " = ?";
                selectionArgs = new String[]{"0"};
                this.selected = 3;
                break;
            case 3:
                if(this.selected == 1){
                    // If all is selected, then order by artist and title
                    orderBy = MuseumDBOpenHelper.DB_KEY_ARTIST + " asc, " + MuseumDBOpenHelper.DB_KEY_TITLE + " asc";
                } else if(this.selected ==2){
                    // If ranked is selected, then order by artist and title
                    where =  MuseumDBOpenHelper.DB_KEY_RANK + " != ?";
                    selectionArgs = new String[]{"0"};
                    orderBy = MuseumDBOpenHelper.DB_KEY_ARTIST + " asc";
                } else if(this.selected ==3){
                    // If NOT rank is selected, then order by artist and title
                    where =  MuseumDBOpenHelper.DB_KEY_RANK + " = ?";
                    selectionArgs = new String[]{"0"};
                    orderBy = MuseumDBOpenHelper.DB_KEY_ARTIST + " asc";
                }

                break;
            case 4:
                if(this.selected == 1){
                    // If all is selected, then order by title
                    orderBy = MuseumDBOpenHelper.DB_KEY_TITLE + " asc";
                } else if(this.selected ==2){
                    // If ranked is selected, then order by title
                    where =  MuseumDBOpenHelper.DB_KEY_RANK + " != ?";
                    selectionArgs = new String[]{"0"};
                    orderBy = MuseumDBOpenHelper.DB_KEY_TITLE + " asc";
                } else if(this.selected ==3){
                    // If NOT ranked is selected, then order by title
                    where =  MuseumDBOpenHelper.DB_KEY_RANK + " = ?";
                    selectionArgs = new String[]{"0"};
                    orderBy = MuseumDBOpenHelper.DB_KEY_TITLE + " asc";
                }
                break;
            case 5:
                if(this.selected == 1){
                    // If all is selected, then order by rank descending
                    orderBy = MuseumDBOpenHelper.DB_KEY_RANK + " desc";
                } else if(this.selected ==2){
                    // If ranked is selected, then order by rank descending
                    where =  MuseumDBOpenHelper.DB_KEY_RANK + " != ?";
                    selectionArgs = new String[]{"0"};
                    orderBy = MuseumDBOpenHelper.DB_KEY_RANK + " desc";
                } else if(this.selected ==3){
                    // If NOT ranked is selected, then order by rank descending
                    where =  MuseumDBOpenHelper.DB_KEY_RANK + " = ?";
                    selectionArgs = new String[]{"0"};
                    orderBy =MuseumDBOpenHelper.DB_KEY_RANK + " desc";
                    Toast.makeText(this, "No rank to order", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                // default is select all from database
                where = null;
                selectionArgs = null;
                break;
        }

        Cursor cursor = database.query(
                Database_table,
                columns,
                where,
                selectionArgs,
                groupBy,
                having,
                orderBy);

        int result = cursor.getCount();

        // make sure database is not empty, otherwise set the adapter to null
        if(result == 0 || result < 1){
            list.setAdapter(null);
        } else{
            this.cursorAdapter = new MuseumCursorAdapter(this,cursor,0);
            list.setAdapter(cursorAdapter);
        }

    }

    @Override
    public void onClick(final View view) {
        int id = view.getId();

        if(id == R.id.fab){
            createDialog(view);
        }


    }

    private void createDialog(final View view){

        final int currentYear = Calendar.getInstance().get(Calendar.YEAR);


        // If there are bad inputs from user, it will recursively call createDialog and will increment by one
        if(checker > 0){
            // Reset value because we know no input has been added
            outOutBoundsYear = 0;
            Toast.makeText(GalleryActivity.this, "Please insert into Artist, Title and Year.", Toast.LENGTH_LONG).show();
        }

        if(outOutBoundsYear > 0){
            Toast.makeText(this,"You are trying to add a year greater than " + currentYear ,Toast.LENGTH_SHORT).show();
        }

        LayoutInflater li = LayoutInflater.from(GalleryActivity.this);
        View getGalleryIdView = li.inflate(R.layout.dialog_add_new_gallery,nullParent);

        final AlertDialog.Builder createNewGallery = new AlertDialog.Builder(GalleryActivity.this);
        createNewGallery.setView(getGalleryIdView);


        final EditText artistInput = (EditText) getGalleryIdView.findViewById(R.id.editTextDialogArtistInput);
        final EditText titleInput = (EditText) getGalleryIdView.findViewById(R.id.editTextDialogTitleInput);
        final EditText roomInput = (EditText) getGalleryIdView.findViewById(R.id.editTextDialogRoomInput);
        final EditText descriptionInput = (EditText) getGalleryIdView.findViewById(R.id.editTextDialogDescriptionInput);
        final EditText yearInput = (EditText) getGalleryIdView.findViewById(R.id.editTextDialogYearInput);

        createNewGallery.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checker = 0;
                outOutBoundsYear = 0;
                Snackbar snackbar = Snackbar.make(view, "Action Cancelled", Snackbar.LENGTH_SHORT);

                snackbar.setAction("Action",null).show();
            }
        }).setPositiveButton("Create", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialogInterface, int i){

                if(artistInput.getText().toString().trim().isEmpty() || artistInput.getText().length() == 0
                        || titleInput.getText().toString().trim().isEmpty() || titleInput.getText().length() == 0
                        || yearInput.getText().toString().trim().isEmpty() || yearInput.getText().length() == 0
                        ) {
                    // When we first call the dialog, we don't want to tell the user about
                    // mandatory fields. If they haven't added them, the recursive method
                    // check whether the fields were not answered.
                    checker++;
                    // Recursive method call to dialog if mandatory fields are not met.
                    createDialog(view);
                    return;
                }

                
                if(Double.parseDouble(yearInput.getText().toString()) > currentYear){
                    outOutBoundsYear++;
                    createDialog(view);
                    return;
                }


                    Gallery gallery = new Gallery();
                    gallery.setArtist(artistInput.getText().toString());
                    gallery.setTitle(titleInput.getText().toString());
                    gallery.setRoom(roomInput.getText().toString());
                    gallery.setDescription(descriptionInput.getText().toString());
                    gallery.setYear(yearInput.getText().toString());
                    createNewGallery(gallery, view);

            }
        }).create().show();

    }


    // Every time we create a new record, we will use this record to create default image
    public String setUpDefaultImage(){

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.profile_pic);
        bitmap = Bitmap.createScaledBitmap(bitmap,600,400,false);
        OutputStream output;
        String filename = "default.jpg";

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

        return filename;
    }


    // Adapter listener
    private AdapterView.OnItemClickListener clicked = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            Cursor cursor = (Cursor) list.getItemAtPosition(i);
            String selected = cursor.getString(cursor.getColumnIndex(MuseumDBOpenHelper.DB_KEY_ID));

            Intent detailsIntent = new Intent(view.getContext(), DetailActivity.class);
            detailsIntent.putExtra("id", selected);
            startActivity(detailsIntent);

        }
    };


    private void restartLoader() {
        getLoaderManager().restartLoader(0,null,this);
    }




}
