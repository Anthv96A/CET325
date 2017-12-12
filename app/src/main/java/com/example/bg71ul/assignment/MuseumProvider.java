package com.example.bg71ul.assignment;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Anthony Vest on 07/12/2017.
 */

public class MuseumProvider extends ContentProvider {


    private SQLiteDatabase db;

    private static final String AUTHORITY = "com.example.bg71ul.assignment";
    private static final String BASE_PATH = "museum";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);


    private static final int MUSEUM = 1;
    private static final int MUSEUM_ID = 2;


    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY,BASE_PATH,MUSEUM);
        uriMatcher.addURI(AUTHORITY,BASE_PATH + "/#",MUSEUM_ID);
    }



    @Override
    public boolean onCreate() {
        MuseumDBOpenHelper museumDBOpenHelper = new MuseumDBOpenHelper(getContext());
        db = museumDBOpenHelper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        Cursor cursor = null;

        switch (uriMatcher.match(uri)){
            case MUSEUM:
                cursor = db.query(
                        MuseumDBOpenHelper.MUSEUM_TABLE_NAME,
                        MuseumDBOpenHelper.ALL_COLUMNS,
                        s,
                        null,
                        null,
                        null,
                        MuseumDBOpenHelper.DB_KEY_ID + " ASC");
                break;
            default:
                throw new IllegalArgumentException("This is an unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case MUSEUM:
                //return the mimi type of content provider
                return "vnd.android.cursor.dir/museum";
            default:
                throw new IllegalArgumentException("This is an unknown URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long id = db.insert(MuseumDBOpenHelper.MUSEUM_TABLE_NAME, null, contentValues);
        if(id > 0){
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(_uri, null);
            Log.d("CONTENT VALUES" , contentValues.toString());
            return _uri;
        }
        throw new SQLException("Insertion Failed for URI: " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        int deletedCount = 0;
        switch (uriMatcher.match(uri)){
            case MUSEUM:
                deletedCount = db.delete(MuseumDBOpenHelper.MUSEUM_TABLE_NAME, s, strings);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri,null);
        return deletedCount;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int updatedCount = 0;
        switch (uriMatcher.match(uri)){
            case MUSEUM:
                updatedCount = db.update(MuseumDBOpenHelper.MUSEUM_TABLE_NAME,contentValues,s,strings);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return updatedCount;
    }
}
