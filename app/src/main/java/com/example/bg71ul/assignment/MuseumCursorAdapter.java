package com.example.bg71ul.assignment;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Anthony Vest on 07/12/2017.
 */

public class MuseumCursorAdapter extends CursorAdapter{

    public MuseumCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(
                R.layout.museum_list_item, parent, false
        );
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String title = cursor.getString(cursor.getColumnIndex(MuseumDBOpenHelper.DB_KEY_TITLE));
        String artist = cursor.getString(cursor.getColumnIndex(MuseumDBOpenHelper.DB_KEY_ARTIST));
        String room = cursor.getString(cursor.getColumnIndex(MuseumDBOpenHelper.DB_KEY_ROOM));
        String description = cursor.getString(cursor.getColumnIndex(MuseumDBOpenHelper.DB_KEY_DESCRIPTION));
        String ranking = cursor.getString(cursor.getColumnIndex(MuseumDBOpenHelper.DB_KEY_RANK));
        String year = cursor.getString(cursor.getColumnIndex(MuseumDBOpenHelper.DB_KEY_YEAR));



        if(description == null || description.isEmpty()){
            description = "No description is set";
        } else {
            if(description.length() > 120){
                description = description.substring(0,119);
                description += "...";
            }
        }

        float rank;

        if(ranking == null){
            rank = 0;
        } else{
            rank = Float.parseFloat(ranking);
        }


        TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        TextView artistTextView = (TextView) view.findViewById(R.id.nameArtistView);
        TextView descriptionTextView = (TextView) view.findViewById(R.id.descriptionTextView);
        TextView roomTextView = (TextView) view.findViewById(R.id.roomTextView);
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar) ;
        ImageView imageView = (ImageView) view.findViewById(R.id.imageDocIcon);
        TextView yearView = (TextView) view.findViewById(R.id.yearTextView);


        if(cursor.getString(cursor.getColumnIndex(MuseumDBOpenHelper.DB_KEY_IMAGE)) == null){
            imageView.setImageResource(R.drawable.profile_pic);
        } else{
            String galleryImage = cursor.getString(cursor.getColumnIndex(MuseumDBOpenHelper.DB_KEY_IMAGE));
            File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),galleryImage);

            if(file.exists()){
                Bitmap image = BitmapFactory.decodeFile(file.getAbsolutePath());
                image = Bitmap.createScaledBitmap(image, 300, 300, false);
                imageView.setImageBitmap(image);
            } else{
                imageView.setImageResource(R.drawable.profile_pic);
            }
        }

        titleTextView.setText("Painting: "+ title);
        artistTextView.setText("Artist: " + artist);
        roomTextView.setText("Room: " + room);
        yearView.setText("The year of completion: " + year);
        descriptionTextView.setText(description);
        ratingBar.setRating(rank);

    }
}
