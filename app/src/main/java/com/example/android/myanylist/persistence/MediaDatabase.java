package com.example.android.myanylist.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.android.myanylist.models.MediaEntry;

@Database(entities = {MediaEntry.class}, version = 2)
public abstract class MediaDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "media_db";

    private static MediaDatabase instance;

    // singleton pattern that ensures there is only one instance of the database in memory
    static MediaDatabase getInstance (final Context context) {
        if (instance == null){
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    MediaDatabase.class,
                    DATABASE_NAME
            ).build();
        }
        return instance;
    }

    public abstract EntryDao getEntryDao();
}
