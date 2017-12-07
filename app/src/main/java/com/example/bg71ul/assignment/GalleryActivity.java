package com.example.bg71ul.assignment;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class GalleryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {
    private CursorAdapter cursorAdapter = null;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        list = (ListView)findViewById(R.id.listView_gallery);
        list.setEmptyView(findViewById(R.id.empty));


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
        ContentValues values = new ContentValues();

        values.put(MuseumDBOpenHelper.DB_KEY_ARTIST, newGallery.getArtist());
        values.put(MuseumDBOpenHelper.DB_KEY_TITLE, newGallery.getTitle());
        values.put(MuseumDBOpenHelper.DB_KEY_ROOM, newGallery.getTitle());
        values.put(MuseumDBOpenHelper.DB_KEY_DESCRIPTION, newGallery.getDescription());
        values.put(MuseumDBOpenHelper.DB_KEY_YEAR, newGallery.getYear());

        getContentResolver().insert(MuseumProvider.CONTENT_URI, values);
        Snackbar snackbar = Snackbar.make(view,"New Gallery created",Snackbar.LENGTH_SHORT);
        snackbar.setAction("Action", null).show();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem all = menu.findItem(R.id.allGallery);
        onNavigationItemSelected(all);
    }


//    public void listViewClickListener(){
//        list = (ListView)findViewById(R.id.listView_gallery);
//        //set onclick listener for list
//        list.setClickable(true);
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //get the title of the item selected
//                //create a view containing the get track details activity
////                final Cursor cursor = (Cursor)list.getItemAtPosition(position);
////                selectedItem = cursor.getString(cursor.getColumnIndexOrThrow
////                        (PlaceDBOpenHelper.KEY_ID));
////                Intent intent = new Intent(view.getContext(), PlacesDetailView.class);
////                intent.putExtra("index", selectedItem);
////                intent.putExtra("localCurrency", localCurrencyRates);
////                intent.putExtra("favCurrency", favCurrencyRates);
////                startActivity(intent);
//            }
//        });
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.gallery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                QueryDB(0);
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

    public void QueryDB(int index){
        this.list = (ListView) findViewById(R.id.listView_gallery);

        MuseumDBOpenHelper museumDBOpenHelper = new MuseumDBOpenHelper(this);
        SQLiteDatabase database = museumDBOpenHelper.getReadableDatabase();

        // Make sure table is immutable
        final String Database_table = MuseumDBOpenHelper.MUSEUM_TABLE_NAME;
        String[] columns = {"*"};
        String groupBy = null;
        String having = null;
        String orderBy = null;
        String limit = null;
        String selection = null;
        String[] selectionArgs = null;

        switch (index){
            case 0:
                // Show all and ordering by rank default
               // orderBy = MuseumDBOpenHelper.DB_KEY_RANK + " desc";
                break;
            case 1:
                // Order paintings by title alphabetically
                orderBy = MuseumDBOpenHelper.DB_KEY_TITLE + " asc";
                break;
            case 2:
                orderBy = MuseumDBOpenHelper.DB_KEY_ARTIST + " asc";
                break;
            case 3:

                break;
            default:
                selection = null;
                selectionArgs = null;
                break;
        }

        Cursor cursor = database.query(
                Database_table,
                columns,
                selection,
                selectionArgs,
                groupBy,
                having,
                orderBy);

        int result = cursor.getCount();

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

        LayoutInflater li = LayoutInflater.from(GalleryActivity.this);
        View getGalleryIdView = li.inflate(R.layout.dialog_add_new_gallery,null);

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
                Snackbar snackbar = Snackbar.make(view, "Action Cancelled", Snackbar.LENGTH_SHORT);

                snackbar.setAction("Action",null).show();
            }
        }).setPositiveButton("Create", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialogInterface, int i){

                if(artistInput.getText().toString().trim().isEmpty() || artistInput.getText().length() == 0
                        || titleInput.getText().toString().trim().isEmpty() || titleInput.getText().length() == 0
                        || yearInput.getText().toString().trim().isEmpty() || yearInput.getText().length() == 0
                        ) {
                    Toast.makeText(GalleryActivity.this, "Please insert into Artist, Title and Year.", Toast.LENGTH_LONG).show();
                    createDialog(view);
                } else {
                    Gallery gallery = new Gallery();
                    gallery.setArtist(artistInput.getText().toString());
                    gallery.setTitle(titleInput.getText().toString());
                    gallery.setRoom(roomInput.getText().toString());
                    gallery.setDescription(descriptionInput.getText().toString());
                    gallery.setYear(yearInput.getText().toString());

                    createNewGallery(gallery, view);
                }





            }
        }).create().show();

    }




}
