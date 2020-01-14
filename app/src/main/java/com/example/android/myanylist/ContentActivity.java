package com.example.android.myanylist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.myanylist.models.ContentItem;

public class ContentActivity extends AppCompatActivity {

    private static final String TAG = "ContentActivity";

    // ui components
    private ImageView mImageView;
    private EditText mViewTitle, mViewDescription, mStatus, mCreator, mDateCreated;

    // vars
    private static ContentItem initialContent;
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

        mImageView = findViewById(R.id.view_image);

        if(getIncomingIntent()){
            // new entry, edit mode
            //TODO

            // on edit:
            // allow access to edittexts
            // set edittext background to a colour
            // allow modification of image
            // swap status with a spinner for statuses
            // make title and description header visible
        } else {
            // existing note, view mode
            updateViews();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.content_list_toolbar);
        setSupportActionBar(toolbar);
        setTitle(initialContent.getTitle());
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }

    private boolean getIncomingIntent(){
        if(getIntent().hasExtra("selected_content")) {
            initialContent = getIntent().getParcelableExtra("selected_content");
            Log.d(TAG, "onCreate: content imported: " + initialContent.toString());

            mIsNewEntry = false;
            return false;
        }
        mIsNewEntry = true;
        return true;
    }

    private void updateViews(){
        String title = initialContent.getTitle();
        String description = initialContent.getDescription();
        String status = ContentItem.getStringStatus(initialContent.getStatus());
        String creator = initialContent.getCreator();
        String dateCreated = initialContent.getDateCreated();
        int imageRes = initialContent.getImage();

        mViewTitle.setText(title);
        mViewDescription.setText(description);
        mStatus.setText(status);
        mCreator.setText(creator);
        mDateCreated.setText(dateCreated);
        mImageView.setImageResource(imageRes);
    }
}
