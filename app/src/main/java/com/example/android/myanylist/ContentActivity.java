package com.example.android.myanylist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.myanylist.models.ContentItem;

public class ContentActivity extends AppCompatActivity {

    private static final String TAG = "ContentActivity";

    // ui components
    private ImageView mImage;
    private TextView mViewTitle, mViewDescription, mStatus, mCreator, mDateCreated;

    // vars
    private static ContentItem Content;
    private boolean mIsNewEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        mViewTitle = findViewById(R.id.view_title);
        mViewDescription = findViewById(R.id.view_description);
        mStatus = findViewById(R.id.view_status);
        mCreator = findViewById(R.id.view_creator);
        mDateCreated = findViewById(R.id.view_date_created);

        if(getIncomingIntent()){
            // new entry, edit mode
            //TODO
        } else {
            // existing note, view mode
            //TODO
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.content_list_toolbar);
        setSupportActionBar(toolbar);
        setTitle(Content.getTitle());
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }

    private boolean getIncomingIntent(){
        if(getIntent().hasExtra("selected_content")) {
            Content = getIntent().getParcelableExtra("selected_content");
            Log.d(TAG, "onCreate: content imported: " + Content.toString());

            mIsNewEntry = false;
            return false;
        }
        mIsNewEntry = true;
        return true;
    }
}
