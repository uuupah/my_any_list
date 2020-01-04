package com.example.android.myanylist.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.myanylist.data.MediaListContract.MediaEntry;

public class LibraryProvider extends ContentProvider {

    public static final String LOG_TAG = LibraryProvider.class.getSimpleName();
    private LibraryDbHelper mDbHelper; // database helper object

    @Override
    public boolean onCreate() {
        mDbHelper = new LibraryDbHelper(getContext());
        return true;
    }

    // uri setup
    private static final int MEDIA = 100;  // action on entire media list table
    private static final int MEDIA_ID = 101;    // action on specific media id

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(MediaListContract.CONTENT_AUTHORITY, MediaListContract.PATH_MEDIA, MEDIA); // 100
        sUriMatcher.addURI(MediaListContract.CONTENT_AUTHORITY, MediaListContract.PATH_MEDIA + "/#", MEDIA_ID); // 101
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case MEDIA:
                cursor = db.query(MediaEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case MEDIA_ID:
                selection = MediaEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(MediaEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI" + uri);
        }

        // set the notification uri on the cursor, therefore if the data changes the cursor changes
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MEDIA:
                return MediaEntry.MEDIA_LIST_TYPE;
            case MEDIA_ID:
                return MediaEntry.MEDIA_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MEDIA:
                return insertMedia(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertMedia(Uri uri, ContentValues values) {
        // sanity checks
        String mediaName = values.getAsString(MediaEntry.COLUMN_MEDIA_NAME);
        if (mediaName == null) {
            throw new IllegalArgumentException("Media type requires a name");
        }

        //TODO fix the if statement so that it doesnt break if more creator types are added
        Integer creatorType = values.getAsInteger(MediaEntry.COLUMN_CREATOR_TYPE);
        if (creatorType < 0 || creatorType > 2) {
            throw new IllegalArgumentException("Invalid creator type");
        }
        // end sanity checks

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // perform insert
        long id = db.insert(MediaEntry.TABLE_NAME, null, values);

        // check for success
        if (id < 0) {
            Log.e(LOG_TAG, "failed to insert row for " + uri);
            return null;
        }

        // notify listeners that the data has changed for the media uri
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MEDIA:
                rowsDeleted = db.delete(MediaEntry.TABLE_NAME, selection, selectionArgs);
                // this case should probably create a new instance of the default database
                break;
            case MEDIA_ID:
                selection = MediaEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(MediaEntry.TABLE_NAME, selection, selectionArgs);
                // this case should include a check for protected media
                break;
            default:
                throw new IllegalArgumentException("deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MEDIA:
                return updateMedia(uri, values, selection, selectionArgs);
            case MEDIA_ID:
                selection = MediaEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateMedia(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("update is not supported for " + uri);
        }
    }

    private int updateMedia(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // sanity checks
        String mediaName = values.getAsString(MediaEntry.COLUMN_MEDIA_NAME);
        if (mediaName == null) {
            throw new IllegalArgumentException("Media type requires a name");
        }

        Integer creatorType = values.getAsInteger(MediaEntry.COLUMN_CREATOR_TYPE);
        if (creatorType < 0 || creatorType > 2) {
            throw new IllegalArgumentException("Invalid creator type");
        }
        // end sanity checks

        if (values.size() < 1) { return 0; }

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int rowsUpdated = db.update(MediaEntry.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }
}
    