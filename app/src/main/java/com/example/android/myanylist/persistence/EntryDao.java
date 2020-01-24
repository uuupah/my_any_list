package com.example.android.myanylist.persistence;

import android.provider.MediaStore;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.android.myanylist.models.MediaEntry;

import java.util.List;

@Dao
public interface EntryDao {

    @Insert
    long[] insertEntry(MediaEntry... entries);

    @Query("SELECT * FROM entries ORDER BY status, title")
    LiveData<List<MediaEntry>> getEntries();

//    @Query("SELECT * FROM entries WHERE id = :id")
//    List<Note> getNotesWithCustomQuery(int id)

    @Delete
    int delete(MediaEntry... entries);

    @Update
    int update(MediaEntry... entries);

}
