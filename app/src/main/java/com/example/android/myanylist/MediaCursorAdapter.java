package com.example.android.myanylist;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.myanylist.data.MediaListContract;

public class MediaCursorAdapter extends CursorAdapter {
    public MediaCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.media_layout_listitem, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = /**(TextView)**/ view.findViewById(R.id.media_name);
        TextView descTextView = view.findViewById(R.id.media_desc);

        int nameColumnIndex = cursor.getColumnIndex(MediaListContract.MediaEntry.COLUMN_MEDIA_NAME);
        int descColumnIndex = cursor.getColumnIndex(MediaListContract.MediaEntry.COLUMN_MEDIA_DESC);

        String name = cursor.getString(nameColumnIndex);
        String desc = cursor.getString(descColumnIndex);

        // checks for legitimate and non null input here

        nameTextView.setText(name);
        descTextView.setText(desc);
    }
}
