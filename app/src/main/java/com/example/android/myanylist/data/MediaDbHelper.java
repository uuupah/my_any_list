package com.example.android.myanylist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MediaDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "myanylist.db";

    // constructor
    public MediaDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //TODO build sql statement from contract

//        String SQL_CREATE_PETS_TABLE =  "CREATE TABLE " + PetContract.PetEntry.TABLE_NAME + " ("
//                + PetContract.PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
//                + PetContract.PetEntry.COLUMN_PET_NAME + " TEXT NOT NULL, "
//                + PetContract.PetEntry.COLUMN_PET_BREED + " TEXT, "
//                + PetContract.PetEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL, "
//                + PetContract.PetEntry.COLUMN_PET_WEIGHT + " INTEGER NOT NULL DEFAULT 0);";


        //TODO execute sql statement
//        db.execSQL(SQL_CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
