package com.example.android.myanylist.persistence;


import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.android.myanylist.models.MediaEntry;

import java.util.List;

public class EntryRepository {

    private MediaDatabase mMediaDatabase;

    public EntryRepository(Context context) {
        mMediaDatabase = MediaDatabase.getInstance(context);
    }

    public void insertEntryTask(MediaEntry entry) {

    }

    public void updateEntry(MediaEntry entry) {

    }

    public LiveData<List<MediaEntry>> retrieveEntryTask() {
        return mMediaDatabase.getEntryDao().getEntries();
    }

    public void deleteEntry(MediaEntry entry){

    }
}
