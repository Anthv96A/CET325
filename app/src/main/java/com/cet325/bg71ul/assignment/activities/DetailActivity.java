package com.cet325.bg71ul.assignment.activities;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cet325.bg71ul.assignment.models.Gallery;
import com.cet325.bg71ul.assignment.MuseumDBOpenHelper;
import com.cet325.bg71ul.assignment.MuseumProvider;
import com.cet325.bg71ul.assignment.R;

import java.io.File;
import java.util.Calendar;

public class DetailActivity extends AppCompatActivity {

    private String ID;
    private boolean canEditFully;
    private float rank;
    private String title;
    TextView artistTextView = null;
    TextView yearTextView = null;
    TextView descriptionTextView = null;
    TextView roomTextView = null;
    RatingBar rankRatingBar = null;
    ImageView galleryImage = null;
    final ViewGroup nullParent = null;
    Gallery gallery = new Gallery();

    public String getID(){
        return this.ID;
    }

    public void setID(String ID){
        this.ID = ID;
    }

    public float getRank(){
        return this.rank;
    }

    public void setRank(float rank){
        this.rank = rank;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


            switch (item.getItemId()) {
                case R.id.gallery_options: {
                    editFields();
                    return true;
                }
            }


        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        setID(intent.getStringExtra("id"));


        artistTextView = (TextView) findViewById(R.id.txtArtist);
        yearTextView = (TextView) findViewById(R.id.txtYear);
        descriptionTextView = (TextView) findViewById(R.id.txtDescription);
        roomTextView = (TextView) findViewById(R.id.txtRoom);
        rankRatingBar = (RatingBar) findViewById(R.id.ratingBarGallery);
        galleryImage = (ImageView) findViewById(R.id.imageViewGallery);

        Cursor cursor = QueryDatabase();

        if(cursor != null && cursor.moveToFirst()){
            title = cursor.getString(cursor.getColumnIndex(MuseumDBOpenHelper.DB_KEY_TITLE));
            setTitle(title);
            populateFields(cursor);
        }


    }

    private void populateFields(Cursor cursor){

        try{
            String artist = cursor.getString(cursor.getColumnIndex(MuseumDBOpenHelper.DB_KEY_ARTIST));
            String year = cursor.getString(cursor.getColumnIndex(MuseumDBOpenHelper.DB_KEY_YEAR));
            String description = cursor.getString(cursor.getColumnIndex(MuseumDBOpenHelper.DB_KEY_DESCRIPTION));
            String room = cursor.getString(cursor.getColumnIndex(MuseumDBOpenHelper.DB_KEY_ROOM));
            String ranking = cursor.getString(cursor.getColumnIndex(MuseumDBOpenHelper.DB_KEY_RANK));
            String image = cursor.getString(cursor.getColumnIndex(MuseumDBOpenHelper.DB_KEY_IMAGE));
            canEditFully = (cursor.getInt(cursor.getColumnIndex(MuseumDBOpenHelper.DB_KEY_EDITABLE)) == 1);


            if(ranking == null){
                setRank(0);
            } else{
                setRank(Float.parseFloat(ranking));
            }

            if(room == null || room.isEmpty()){
                room = "No room yet set";
            }

            // If we don't have an image already, set the default
            if(image.isEmpty() || image == null){
                galleryImage.setImageResource(R.drawable.profile_pic);
            } else{
                File file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), image);
                try{
                    if(file.exists()){
                        Bitmap bitmapImage = BitmapFactory.decodeFile(file.getAbsolutePath());
                        galleryImage.setImageBitmap(bitmapImage);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

            }

            artistTextView.setText(artist);
            yearTextView.setText(year);
            descriptionTextView.setText(description);
            roomTextView.setText(room);
            rankRatingBar.setRating(getRank());

            gallery.setArtist(artist);
            gallery.setTitle(title);
            gallery.setYear(year);
            gallery.setDescription(description);
            gallery.setRoom(room);


        } catch (Exception e){
            e.printStackTrace();
        }


    }


    private Cursor QueryDatabase(){
        MuseumDBOpenHelper museumDBOpenHelper = new MuseumDBOpenHelper(this);
        SQLiteDatabase sqLiteDatabase = museumDBOpenHelper.getReadableDatabase();

        // Fetch record that the user has clicked
        Cursor cursor = sqLiteDatabase.query(
                MuseumDBOpenHelper.MUSEUM_TABLE_NAME,
                museumDBOpenHelper.ALL_COLUMNS,
                museumDBOpenHelper.DB_KEY_ID + " = ?",
                new String[]{getID()},
                null,
                null,
                null
        );

        return cursor;
    }


    private void editFields(){

        if(canEditFully){
            // The can edit fully is a boolean value that checks from the database where we can either
            // edit pre-loaded rank, or fully edit a manually added record.
            editAllFieldsDialog();
        } else {
            Toast.makeText(this, "You can only edit rank for pre-existing records. However, you can fully edit manually added records.", Toast.LENGTH_LONG).show();
        }
    }



    private void editAllFieldsDialog(){
        final int currentYear = Calendar.getInstance().get(Calendar.YEAR);


        LayoutInflater layoutInflater = LayoutInflater.from(DetailActivity.this);
        View getEditDialog = layoutInflater.inflate(R.layout.edit_anyfield,nullParent);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailActivity.this);

        final TextView artistDialogTextview = (TextView) getEditDialog.findViewById(R.id.editTextDialogArtistInput);
        final TextView titleDialogTextview = (TextView) getEditDialog.findViewById(R.id.editTextDialogTitleInput);
        final TextView yearDialogTextview = (TextView) getEditDialog.findViewById(R.id.editTextDialogYearInput);
        final TextView descriptionDialogTextview = (TextView) getEditDialog.findViewById(R.id.editTextDialogDescriptionInput);
        final TextView roomDialogTextview = (TextView) getEditDialog.findViewById(R.id.editTextDialogRoomInput);
        setRank(rankRatingBar.getRating());


        artistDialogTextview.setText(gallery.getArtist());
        titleDialogTextview.setText(gallery.getTitle());
        yearDialogTextview.setText(gallery.getYear());
        descriptionDialogTextview.setText(gallery.getDescription());
        roomDialogTextview.setText(gallery.getRoom());



        alertDialogBuilder.setView(getEditDialog);

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton) {
                Toast.makeText(DetailActivity.this, "Dismissed", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }).setPositiveButton("Edit", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton) {


                    gallery.setArtist(artistDialogTextview.getText().toString());
                    gallery.setTitle(titleDialogTextview.getText().toString());
                    gallery.setYear(yearDialogTextview.getText().toString());
                    gallery.setDescription(descriptionDialogTextview.getText().toString());
                    gallery.setRoom(roomDialogTextview.getText().toString());


                if(artistDialogTextview.getText().toString() == " " || artistDialogTextview.getText().toString().isEmpty()
                        || titleDialogTextview.getText().toString() == " " || titleDialogTextview.getText().toString().isEmpty()
                        || yearDialogTextview.getText().toString().isEmpty() || yearDialogTextview.getText().toString() == ""){
                    // Need to run on the current UI thread for toast to warn about missing fields
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DetailActivity.this, "Please insert into Artist, Title and Year.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    editAllFieldsDialog();
                    return;
                }

                if(Double.parseDouble(yearDialogTextview.getText().toString()) > currentYear){
                    // Need to run on the current UI thread for toast to warn about missing fields
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DetailActivity.this, "You are trying to add a year greater than " + currentYear , Toast.LENGTH_SHORT).show();
                        }
                    });
                    editAllFieldsDialog();
                    return;
                }

                String where = "_id = " + getID();
                ContentValues values = new ContentValues();
                values.put(MuseumDBOpenHelper.DB_KEY_ARTIST, artistDialogTextview.getText().toString());
                values.put(MuseumDBOpenHelper.DB_KEY_TITLE, titleDialogTextview.getText().toString());
                values.put(MuseumDBOpenHelper.DB_KEY_ROOM, roomDialogTextview.getText().toString());
                values.put(MuseumDBOpenHelper.DB_KEY_YEAR, yearDialogTextview.getText().toString());
                values.put(MuseumDBOpenHelper.DB_KEY_DESCRIPTION, descriptionDialogTextview.getText().toString());
                values.put(MuseumDBOpenHelper.DB_KEY_RANK, getRank());

                getContentResolver().update(MuseumProvider.CONTENT_URI,values,where,null);

                artistTextView.setText(artistDialogTextview.getText().toString());
                yearTextView.setText(yearDialogTextview.getText().toString());
                descriptionTextView.setText(descriptionDialogTextview.getText().toString());
                roomTextView.setText(roomDialogTextview.getText().toString());
                title = titleDialogTextview.getText().toString();
                setTitle(titleDialogTextview.getText().toString());

            }
        }).create().show();

    }


    @Override
    public void onStop(){
        super.onStop();

        // When exiting the activity, the data that is ranked will persist
            String where = "_id = " + getID();
            setRank(rankRatingBar.getRating());

            ContentValues values = new ContentValues();
            values.put(MuseumDBOpenHelper.DB_KEY_RANK, getRank());
            getContentResolver().update(MuseumProvider.CONTENT_URI, values, where, null);
    }

    @Override
    protected void onPause(){
        super.onPause();

    }


}
