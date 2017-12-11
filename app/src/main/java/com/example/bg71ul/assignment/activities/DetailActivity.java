package com.example.bg71ul.assignment.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.bg71ul.assignment.MuseumDBOpenHelper;
import com.example.bg71ul.assignment.R;

public class DetailActivity extends AppCompatActivity {

    String getID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();

        getID = intent.getStringExtra("id");

        int selected = Integer.parseInt(getID);
        TextView textView = (TextView) findViewById(R.id.textView3);

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



//            String name = cursor.getString(cursor.getColumnIndex(MuseumDBOpenHelper.DB_KEY_ARTIST));
//            textView.setText(name);



    }
}
