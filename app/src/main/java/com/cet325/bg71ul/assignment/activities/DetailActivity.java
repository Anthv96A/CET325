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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cet325.bg71ul.assignment.models.Gallery;
import com.cet325.bg71ul.assignment.MuseumDBOpenHelper;
import com.cet325.bg71ul.assignment.MuseumProvider;
import com.cet325.bg71ul.assignment.R;

import java.io.File;

public class DetailActivity extends AppCompatActivity {

    private String getID;
    private int selected;
    private boolean canEditFully;
    private float rank;
    private String title;
    private static int checker = 0;
    RatingBar edited;
    TextView artistTextView = null;
    TextView yearTextView = null;
    TextView descriptionTextView = null;
    TextView roomTextView = null;
    RatingBar rankRatingBar = null;
    ImageView galleryImage = null;

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

        switch (item.getItemId()){
            case R.id.gallery_options:{
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
        getID = intent.getStringExtra("id");
        selected = Integer.parseInt(getID);


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


        } catch (Exception e){
            e.printStackTrace();
        }


    }


    private Cursor QueryDatabase(){
        MuseumDBOpenHelper museumDBOpenHelper = new MuseumDBOpenHelper(this);
        SQLiteDatabase sqLiteDatabase = museumDBOpenHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(
                MuseumDBOpenHelper.MUSEUM_TABLE_NAME,
                museumDBOpenHelper.ALL_COLUMNS,
                museumDBOpenHelper.DB_KEY_ID + " = ?",
                new String[]{getID},
                null,
                null,
                null
        );

        return cursor;
    }




    private void editFields(){

        if(canEditFully){
            // The can edit fully is a boolean value that checks from the database where we can either
            // edit pre-loaded rank or fully edit a manually added rank.
            editAllFieldsDialog();
        } else {
            editOnlyRankDialog();
        }
    }



    private void editAllFieldsDialog(){

        if(checker > 0){
            Toast.makeText(this, "You can't leave the fields: Title, Artist and Year blank", Toast.LENGTH_SHORT).show();
        }

        LayoutInflater layoutInflater = LayoutInflater.from(DetailActivity.this);
        View getEditDialog = layoutInflater.inflate(R.layout.edit_anyfield,null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailActivity.this);

        Gallery editGallery = new Gallery();

        if(artistTextView.getText().toString() != null){
            editGallery.setArtist(artistTextView.getText().toString());
        } else {
            editGallery.setArtist(new String());
        }

        if(yearTextView.getText().toString() != null){
            editGallery.setYear(yearTextView.getText().toString());
        } else{
            editGallery.setYear("2017");
        }

        if(roomTextView.getText().toString() != null){
            editGallery.setRoom(roomTextView.getText().toString());
        } else{
            editGallery.setRoom(new String());
        }

        if(title != null){
            editGallery.setTitle(title);
        } else{
            editGallery.setTitle(new String());
        }

        if(descriptionTextView.getText().toString() !=  null){
            editGallery.setDescription(descriptionTextView.getText().toString());
        } else{
            editGallery.setDescription(new String());
        }

        final TextView artistDialogTextview = (TextView) getEditDialog.findViewById(R.id.editTextDialogArtistInput);
        final TextView titleDialogTextview = (TextView) getEditDialog.findViewById(R.id.editTextDialogTitleInput);
        final TextView yearDialogTextview = (TextView) getEditDialog.findViewById(R.id.editTextDialogYearInput);
        final TextView descriptionDialogTextview = (TextView) getEditDialog.findViewById(R.id.editTextDialogDescriptionInput);
        TextView roomDialogTextview = (TextView) getEditDialog.findViewById(R.id.editTextDialogRoomInput);

        edited = (RatingBar) getEditDialog.findViewById(R.id.ratingBarAny);
        edited.setRating(getRank());

        artistDialogTextview.setText(editGallery.getArtist());
        titleDialogTextview.setText(editGallery.getTitle());
        yearDialogTextview.setText(editGallery.getYear());
        descriptionDialogTextview.setText(editGallery.getDescription());
        roomDialogTextview.setText(editGallery.getRoom());

        alertDialogBuilder.setView(getEditDialog);

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton) {
                Toast.makeText(DetailActivity.this, "Dismissed", Toast.LENGTH_SHORT).show();
                checker = 0;
                dialog.dismiss();
            }
        }).setPositiveButton("Edit", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int whichButton) {

                if(artistTextView.getText().toString() == " " || artistDialogTextview.getText().toString().isEmpty()
                        || titleDialogTextview.getText().toString() == " " || titleDialogTextview.getText().toString().isEmpty()
                        || yearDialogTextview.getText().toString().isEmpty() || yearTextView.getText().toString() == ""){
                    checker ++;
                    editAllFieldsDialog();
                    return;
                }
                checker = 0;
                String where = "_id = " + getID;
                ContentValues values = new ContentValues();
                values.put(MuseumDBOpenHelper.DB_KEY_ARTIST, artistDialogTextview.getText().toString());
                values.put(MuseumDBOpenHelper.DB_KEY_TITLE, titleDialogTextview.getText().toString());
                values.put(MuseumDBOpenHelper.DB_KEY_ROOM, roomTextView.getText().toString());
                values.put(MuseumDBOpenHelper.DB_KEY_YEAR, yearDialogTextview.getText().toString());
                values.put(MuseumDBOpenHelper.DB_KEY_DESCRIPTION, descriptionDialogTextview.getText().toString());
                values.put(MuseumDBOpenHelper.DB_KEY_RANK, edited.getRating());

                getContentResolver().update(MuseumProvider.CONTENT_URI,values,where,null);
                // After editing, we redirect back to newly edited ranking.
                // Ensure we pass the id, otherwise it will fail
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("id", getID);
                startActivity(intent);

            }
        }).create().show();

    }


    private void editOnlyRankDialog(){

        LayoutInflater layoutInflater = LayoutInflater.from(DetailActivity.this);
        View getEditDialog = layoutInflater.inflate(R.layout.edit_preloaded_rank, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailActivity.this);

        edited = (RatingBar) getEditDialog.findViewById(R.id.editRatingPreLoaded);
        edited.setRating(getRank());

        alertDialogBuilder.setView(getEditDialog);

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Toast.makeText(DetailActivity.this, "Dismissed", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }).setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String where = "_id = " + getID;

                ContentValues values = new ContentValues();
                values.put(MuseumDBOpenHelper.DB_KEY_RANK, edited.getRating());

                getContentResolver().update(MuseumProvider.CONTENT_URI,values,where,null);
                // After editing, we redirect back to newly edited ranking.
                // Ensure we pass the id, otherwise it will fail
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("id", getID);
                startActivity(intent);
            }
        }).create().show();
    }


}
