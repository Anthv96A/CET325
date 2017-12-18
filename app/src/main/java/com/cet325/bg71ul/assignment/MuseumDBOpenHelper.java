package com.cet325.bg71ul.assignment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Anthony Vest on 07/12/2017.
 */

public class MuseumDBOpenHelper extends SQLiteOpenHelper{

    // Database version and name
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "museum.db";
    public static final String MUSEUM_TABLE_NAME = "museum";

    // Database column properties
    public static final String DB_KEY_ID = "_id";
    public static final String DB_KEY_ARTIST = "artist";
    public static final String DB_KEY_TITLE = "title";
    public static final String DB_KEY_ROOM = "room";
    public static final String DB_KEY_DESCRIPTION = "description";
    public static final String DB_KEY_IMAGE = "image";
    public static final String DB_KEY_YEAR = "year";
    public static final String DB_KEY_RANK = "rank";
    public static final String DB_KEY_EDITABLE = "editable";

    // Get all columns
    public static final String[] ALL_COLUMNS = {
            DB_KEY_ID,
            DB_KEY_ARTIST,
            DB_KEY_TITLE,
            DB_KEY_ROOM,
            DB_KEY_DESCRIPTION,
            DB_KEY_IMAGE,
            DB_KEY_YEAR,
            DB_KEY_RANK,
            DB_KEY_EDITABLE
    };

    // Create table name
    private static final String CREATE_TABLE =
            "CREATE TABLE " + MUSEUM_TABLE_NAME + "( " +
                    DB_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DB_KEY_ARTIST + " TEXT, " +
                    DB_KEY_TITLE + " TEXT, " +
                    DB_KEY_ROOM + " TEXT, " +
                    DB_KEY_DESCRIPTION + " TEXT, " +
                    DB_KEY_IMAGE + " TEXT, " +
                    DB_KEY_YEAR + " INTEGER, " +
                    DB_KEY_EDITABLE + " INTEGER, " +
                    DB_KEY_RANK + " INTEGER);";


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Creating table
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drops old museum database if it already exists
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ CREATE_TABLE);
        this.onCreate(sqLiteDatabase);
    }

    public MuseumDBOpenHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
