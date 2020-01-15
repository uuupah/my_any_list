package com.example.android.myanylist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.android.myanylist.models.ContentItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ContentActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ContentActivity";
    private static final int EDIT_MODE_ENABLED = 1;
    private static final int EDIT_MODE_DISABLED = 0;

    // ui components
    private ImageView mImageView;
    private ImageButton mBackView, mCheckView;
    private EditText mViewTitle, mViewDescription, mViewStatus, mViewCreator, mViewDateCreated;
    private FloatingActionButton mFab;

    // vars
    private static ContentItem mInitialContent;
    private boolean mIsNewEntry;
    private String mTitle, mDescription, mStatus, mCreator, mDateCreated;
    private int mImageRes, mStatusInt, mMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        mBackView = findViewById(R.id.view_back_button);
        mCheckView = findViewById(R.id.view_check_button);

        mViewTitle = findViewById(R.id.view_title);
        mViewDescription = findViewById(R.id.view_description);
        mViewStatus = findViewById(R.id.view_status);
        mViewCreator = findViewById(R.id.view_creator);
        mViewDateCreated = findViewById(R.id.view_date_created);
        mFab = findViewById(R.id.content_fab);

        mImageView = findViewById(R.id.view_image);

        findViewById(R.id.content_fab).setOnClickListener(this);
        mCheckView.setOnClickListener(this);

        if (getIncomingIntent()) {
            // new entry, edit mode
            initialiseContent();
            enableEditMode();

            //TODO

            // on edit:
            // allow access to edittexts
            // set edittext background to a colour

            // allow modification of image
            // swap status with a spinner for statuses
            // make title and description header visible
        } else {
            // existing note, view mode
            readContent();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.content_list_toolbar);
        setSupportActionBar(toolbar);
    }

    private boolean getIncomingIntent() {
        if (getIntent().hasExtra("selected_content")) {
            mInitialContent = getIntent().getParcelableExtra("selected_content");
            Log.d(TAG, "onCreate: content imported: " + mInitialContent.toString());

            mMode = EDIT_MODE_DISABLED;
            mIsNewEntry = false;
            return false;
        }
        mMode = EDIT_MODE_ENABLED;
        mIsNewEntry = true;
        return true;
    }

    private void enableEditMode(){
        displayEditTextUnderline();
        mBackView.setVisibility(View.GONE);
        mCheckView.setVisibility(View.VISIBLE);

        mMode = EDIT_MODE_ENABLED;

        mFab.hide();
    }

    private void disableEditMode(){
        hideEditTextUnderline();
        mBackView.setVisibility(View.VISIBLE);
        mCheckView.setVisibility(View.GONE);

        mMode = EDIT_MODE_DISABLED;

        mFab.show();
    }

    private void readContent() {
        mTitle = mInitialContent.getTitle();
        mDescription = mInitialContent.getDescription();
        mStatus = ContentItem.getStringStatus(mInitialContent.getStatus());
        mCreator = mInitialContent.getCreator();
        mDateCreated = mInitialContent.getDateCreated();
        mImageRes = mInitialContent.getImage();

        fillViews();
    }

    private void initialiseContent() {
        mTitle = "New Entry";
        mDescription = "Describe the entry here.";
        mStatusInt = 0;
        mStatus = ContentItem.getStringStatus(mStatusInt);
//        creator = "Creator";
//        dateCreated = "01 Jan 1970";
        mImageRes = R.mipmap.ic_launcher; // temporary image

        fillViews();
    }

    private void fillViews() {
        mViewTitle.setText(mTitle);
        mViewDescription.setText(mDescription);
        mViewStatus.setText(mStatus);
        mViewCreator.setText(mCreator);
        mViewDateCreated.setText(mDateCreated);
        mImageView.setImageResource(mImageRes);
    }

    private void displayEditTextUnderline() {
        mViewTitle.setBackgroundTintList(getColorStateList(R.color.grey_underline));
        mViewDescription.setBackgroundTintList(getColorStateList(R.color.grey_underline));
        mViewStatus.setBackgroundTintList(getColorStateList(R.color.grey_underline));
        mViewCreator.setBackgroundTintList(getColorStateList(R.color.grey_underline));
        mViewDateCreated.setBackgroundTintList(getColorStateList(R.color.grey_underline));
    }

    private void hideEditTextUnderline() {
        mViewTitle.setBackgroundTintList(getColorStateList(R.color.transparent_underline));
        mViewDescription.setBackgroundTintList(getColorStateList(R.color.transparent_underline));
        mViewStatus.setBackgroundTintList(getColorStateList(R.color.transparent_underline));
        mViewCreator.setBackgroundTintList(getColorStateList(R.color.transparent_underline));
        mViewDateCreated.setBackgroundTintList(getColorStateList(R.color.transparent_underline));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.content_fab:
                enableEditMode();
                break;
            case R.id.view_check_button:
                disableEditMode();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if(mMode == EDIT_MODE_ENABLED) {
            onClick(mCheckView);
        } else {
            super.onBackPressed();
        }
    }
}
