package com.example.android.myanylist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.myanylist.models.ContentItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ContentActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ContentActivity";
    private static final int EDIT_MODE_ENABLED = 1;
    private static final int EDIT_MODE_DISABLED = 0;

    // ui components
    private ImageView mImageView;
    private ImageButton mBackView, mCheckView;
    private TextView mTitleHeader, mDescriptionHeader;
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

        initialiseViews(); // links view variables to their link id
        setListeners(); // set onclicklisteners to appropriate views

        if (getIncomingIntent()) {
            // new entry, edit mode
            initialiseContent();
            enableEditMode();

            //TODO
            // on edit:
            // allow modification of image
            // swap status with a spinner for statuses
        } else {
            // existing note, view mode
            readContent();
            disableContentInteraction();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.content_list_toolbar);
        setSupportActionBar(toolbar);
    }

    private void initialiseViews(){
        mBackView = findViewById(R.id.view_back_button);
        mCheckView = findViewById(R.id.view_check_button);

        mTitleHeader = findViewById(R.id.view_title_header);
        mDescriptionHeader = findViewById(R.id.view_description_header);

        mViewTitle = findViewById(R.id.view_title);
        mViewDescription = findViewById(R.id.view_description);
        mViewStatus = findViewById(R.id.view_status);
        mViewCreator = findViewById(R.id.view_creator);
        mViewDateCreated = findViewById(R.id.view_date_created);
        mFab = findViewById(R.id.content_fab);

        mImageView = findViewById(R.id.view_image);
    }

    private void setListeners(){
        mFab.setOnClickListener(this);
        mCheckView.setOnClickListener(this);
        mBackView.setOnClickListener(this);
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
        mTitleHeader.setVisibility(View.VISIBLE);
        mDescriptionHeader.setVisibility(View.VISIBLE);

        mMode = EDIT_MODE_ENABLED;

        mFab.hide();

        enableContentInteraction();

        mViewTitle.requestFocus();
    }

    private void disableEditMode(){
        hideEditTextUnderline();
        mBackView.setVisibility(View.VISIBLE);
        mCheckView.setVisibility(View.GONE);
        mTitleHeader.setVisibility(View.GONE);
        mDescriptionHeader.setVisibility(View.GONE);

        mMode = EDIT_MODE_DISABLED;

        mFab.show();

        disableContentInteraction(); // probably unnecessary
    }

    private void disableContentInteraction(){
        disableEditText(mViewTitle);
        disableEditText(mViewDescription);
        disableEditText(mViewStatus);
        disableEditText(mViewCreator);
        disableEditText(mViewDateCreated);
    }

    private void disableEditText(EditText v){
        v.setKeyListener(null);
        v.setFocusable(false);
        v.setFocusableInTouchMode(false);
        v.setCursorVisible(false);
        v.clearFocus();
    }

    private void enableContentInteraction(){
        enableEditText(mViewTitle);
        enableEditText(mViewDescription);
        enableEditText(mViewStatus);
        enableEditText(mViewCreator);
        enableEditText(mViewDateCreated);
    }

    private void enableEditText (EditText v){
        v.setKeyListener(new EditText(this).getKeyListener());
        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
        v.setCursorVisible(true);
    }

    private void hideSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
                hideSoftKeyboard();
                break;
            case R.id.view_back_button:
                finish();
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("mode", mMode);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMode = savedInstanceState.getInt("mode");
        if(mMode == EDIT_MODE_ENABLED){
            enableEditMode();
        }
    }
}
