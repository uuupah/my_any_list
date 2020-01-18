package com.example.android.myanylist.async;

import android.os.AsyncTask;

import com.example.android.myanylist.models.MediaEntry;
import com.example.android.myanylist.persistence.EntryDao;

public class DeleteAsyncTask extends AsyncTask<MediaEntry, Void, Void> {

    private EntryDao mEntryDao;

    public DeleteAsyncTask(EntryDao dao) {
        mEntryDao = dao;
    }

    @Override
    protected Void doInBackground(MediaEntry... mediaEntries) {
        mEntryDao.delete(mediaEntries); // insert notes into the database and close
        return null;
    }
}
