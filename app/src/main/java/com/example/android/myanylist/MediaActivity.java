package com.example.android.myanylist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MediaActivity extends AppCompatActivity {

    private static final String TAG = "MediaActivity";

    //vars
    private ArrayList<Media> mMediaTypes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: started.");

        initImageBitmaps();
        initMediaRecyclerView();
    }

    private void initImageBitmaps(){
        Log.d(TAG, "initImageBitmaps: preparing bitmaps");

        mMediaTypes.add(new Media("Film", R.drawable.baseline_movie_black_48));
        mMediaTypes.add(new Media("Books", R.drawable.baseline_book_black_48));
        mMediaTypes.add(new Media("Video Games", R.drawable.baseline_videogame_asset_black_48));

    }

    private void initMediaRecyclerView(){
        Log.d(TAG, "initMediaRecyclerView: init media recycler view.");
        RecyclerView mediaRecyclerView = findViewById(R.id.media_recycler_view);
        MediaRecyclerViewAdapter adapter = new MediaRecyclerViewAdapter(this, mMediaTypes); // update this when adapter supports Media objects

        mediaRecyclerView.setAdapter(adapter);
        mediaRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
