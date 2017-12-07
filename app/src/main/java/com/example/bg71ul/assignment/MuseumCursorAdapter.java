package com.example.bg71ul.assignment;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by antho on 07/12/2017.
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

        float rank;

        if(ranking == null){
            rank = 0;
        } else{
            rank = Float.parseFloat(ranking);
        }





        TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        TextView artistTextView = (TextView) view.findViewById(R.id.nameArtistView);
        TextView descriptionTextView = (TextView) view.findViewById(R.id.descriptionTextView);
        //TextView roomTextView = (TextView) view.findViewById(R.id.roomTextView);
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar) ;

        titleTextView.setText(title);
        artistTextView.setText(artist);
        descriptionTextView.setText(description);
        ratingBar.setRating(rank);

    }
}
