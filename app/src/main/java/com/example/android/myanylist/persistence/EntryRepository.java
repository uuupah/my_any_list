package com.example.android.myanylist.persistence;


import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.android.myanylist.async.DeleteAsyncTask;
import com.example.android.myanylist.async.InsertAsyncTask;
import com.example.android.myanylist.async.UpdateAsyncTask;
import com.example.android.myanylist.models.MediaEntry;

import java.util.List;

public class EntryRepository {

    private MediaDatabase mMediaDatabase;

    public EntryRepository(Context context) {
        mMediaDatabase = MediaDatabase.getInstance(context);
    }

    public void insertEntryTask(MediaEntry entry) {
        new InsertAsyncTask(mMediaDatabase.getEntryDao()).execute(entry);
    }

    public void updateEntry(MediaEntry entry) {
        new UpdateAsyncTask(mMediaDatabase.getEntryDao()).execute(entry);
    }

    public LiveData<List<MediaEntry>> retrieveEntryTask() {
        return mMediaDatabase.getEntryDao().getEntries();
    }

    public void deleteEntry(MediaEntry entry){
        new DeleteAsyncTask(mMediaDatabase.getEntryDao()).execute(entry);
    }
}
