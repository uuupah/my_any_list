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

        resetMediaList();
        initMediaRecyclerView();
    }

    private void resetMediaList(){
        Log.d(TAG, "resetMediaList: ");

        mMediaTypes.add(new Media("Film", "film desc"));
        mMediaTypes.add(new Media("Books", "books desc"));
        mMediaTypes.add(new Media("Video Games", "video games desc"));

        //TODO setup database creation
        // for testing purposes just:
        // delete existing database
        // create and fill new database
        // for later testing check if a non-empty database exists and only create/fill if it doesn't
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
