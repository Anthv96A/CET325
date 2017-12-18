package com.cet325.bg71ul.assignment;


import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.io.File;

/**
 * Created by Anthony Vest on 07/12/2017.
 */

public class MuseumCursorAdapter extends CursorAdapter{

    public MuseumCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Use layout museum_list_item.xml to bind data
        return LayoutInflater.from(context).inflate(
                R.layout.museum_list_item, parent, false
        );
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Fetch title, artist, room, ranking, year from database
        String title = cursor.getString(cursor.getColumnIndex(MuseumDBOpenHelper.DB_KEY_TITLE));
        String artist = cursor.getString(cursor.getColumnIndex(MuseumDBOpenHelper.DB_KEY_ARTIST));
        String room = cursor.getString(cursor.getColumnIndex(MuseumDBOpenHelper.DB_KEY_ROOM));
        String ranking = cursor.getString(cursor.getColumnIndex(MuseumDBOpenHelper.DB_KEY_RANK));
        String year = cursor.getString(cursor.getColumnIndex(MuseumDBOpenHelper.DB_KEY_YEAR));

        float rank;

        // If the rank has come back as null, it has not been ranked yet
        if(ranking == null){
            rank = 0;
        } else{
            // else parse the rank from string
            rank = Float.parseFloat(ranking);
        }

        // As the room field is not mandatory, we need to check it is not null before we bind
        if(room == null || room.isEmpty()){
            room = "No room set";
        }


        TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        TextView artistTextView = (TextView) view.findViewById(R.id.nameArtistView);
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


        String fullTitle = "Painting: " + title;
        String fullArtist = "Artist: " + artist;
        String fullRoom = "Room: " + room;
        String fullYear = "The year of completion: " + year;


        // Bind all: title, artist, room, year and rank all to list item
        titleTextView.setText(fullTitle);
        artistTextView.setText(fullArtist);
        roomTextView.setText(fullRoom);
        yearView.setText(fullYear);
        ratingBar.setRating(rank);
    }
}
