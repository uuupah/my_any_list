package com.example.android.myanylist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LibraryDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "myanylist.db";

    // constructor
    public LibraryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // creates an sql creation command and executes it
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_MEDIA_TABLE = "CREATE TABLE " + MediaListContract.MediaEntry.TABLE_NAME + " ("
                + MediaListContract.MediaEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MediaListContract.MediaEntry.COLUMN_MEDIA_NAME + " TEXT NOT NULL, "
                + MediaListContract.MediaEntry.COLUMN_CREATOR_TYPE + " INTEGER NOT NULL DEFAULT 0);";

        db.execSQL(SQL_CREATE_MEDIA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
