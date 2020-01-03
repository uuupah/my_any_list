// MediaActivity.java
// contains all the behaviour for the main activity that allows users to select between different media types
// should create the database if it does not exist

package com.example.android.myanylist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MediaActivity extends AppCompatActivity {

    private static final String TAG = "MediaActivity";

    //vars
    private ArrayList<Media> mMediaTypes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        Log.d(TAG, "onCreate: started.");

        //TODO replace init contents with a task that creates a template database if it does not exist
        initContents();
        initMediaRecyclerView();
    }

    private void initContents(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps");

        mMediaTypes.add(new Media("Film", "film desc"));
        mMediaTypes.add(new Media("Books", "books desc"));
        mMediaTypes.add(new Media("Video Games", "video games desc"));

    }

    //TODO modify function to read from database instead of array
    private void initMediaRecyclerView(){
        Log.d(TAG, "initMediaRecyclerView: init media recycler view.");
        RecyclerView mediaRecyclerView = findViewById(R.id.media_recycler_view);
        MediaRecyclerViewAdapter adapter = new MediaRecyclerViewAdapter(this, mMediaTypes);

        mediaRecyclerView.setAdapter(adapter);
        mediaRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
