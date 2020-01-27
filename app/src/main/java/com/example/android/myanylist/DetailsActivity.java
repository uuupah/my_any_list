package com.example.android.myanylist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.android.myanylist.models.MediaEntry;
import com.example.android.myanylist.persistence.EntryRepository;
import com.example.android.myanylist.util.Utility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    // constant variables
    private static final String TAG = "DetailsActivity";
    private static final int EDIT_MODE_ENABLED = 1;
    private static final int EDIT_MODE_DISABLED = 0;
    private static final int STORAGE_PERMISSION_CODE = 100;
    private static final int PICK_IMAGE = 101;

    // ui components
    private ImageView mImageView;
    private ImageButton mBackView, mCheckView;
    private TextView mTitleHeader, mDescriptionHeader, mViewStatus;
    private EditText mEditTitle, mEditDescription, mEditCreator, mEditDateCreated;
    private FloatingActionButton mFab;
    private Spinner mSpinnerStatus;

    // vars
    private static MediaEntry mEntry;
    private boolean mIsNewEntry;
    private String mTitle, mDescription, mStatus, mCreator, mDateCreated, mImageLocation;
    private int mStatusInt, mMode;
    private EntryRepository mEntryRepository;
    private Uri mSelectedImage;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        requestStoragePermission();                                                                 // request access to storage through android
        initialiseViews();                                                                          // links view variables to their link id
        setListeners();                                                                             // set onclicklisteners to appropriate views

        mEntryRepository = new EntryRepository(this);                                       // initialise repository

        if (getIncomingIntent()) {                                                                  // - new entry, edit mode
            initialiseContent();                                                                    // create default data for a new entry
            enableEditMode();                                                                       // put the activity in "edit mode"
        } else {                                                                                    // - existing note, view mode
            readContent();                                                                          // read in the data passed through the intent to the variables in this activity
            disableContentInteraction();                                                            // put the activity in "view mode"
        }

        Toolbar toolbar = findViewById(R.id.content_list_toolbar);                                  // initialise toolbar
        setSupportActionBar(toolbar);
    }

    /** attach each view variable to a view id, set up spinner **/
    private void initialiseViews() {
        mBackView = findViewById(R.id.view_back_button);                                            // toolbar buttons
        mCheckView = findViewById(R.id.view_check_button);

        mTitleHeader = findViewById(R.id.view_title_header);                                        // headers that are shown during edit mode
        mDescriptionHeader = findViewById(R.id.view_description_header);

        mEditTitle = findViewById(R.id.edit_title);                                                 // content
        mEditDescription = findViewById(R.id.edit_description);
        mViewStatus = findViewById(R.id.view_status);
        mEditCreator = findViewById(R.id.edit_creator);
        mEditDateCreated = findViewById(R.id.edit_date_created);
        mFab = findViewById(R.id.content_fab);
        mImageView = findViewById(R.id.view_image);

        mSpinnerStatus = findViewById(R.id.status_spinner);                                         // - set up spinner
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this,    // adapter for spinner options
                R.array.status, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);       // spinner type
        mSpinnerStatus.setAdapter(statusAdapter);                                                   // attach adapter
        mSpinnerStatus.setOnItemSelectedListener(this);                                             // set interaction listener

        mViewStatus.setTextColor(ContextCompat.getColor(this,                               // update status color
                R.color.status_planning_yellow));                                                   // assuming that planning is the default value
    }

    private void setListeners() {
        mFab.setOnClickListener(this);
        mCheckView.setOnClickListener(this);
        mBackView.setOnClickListener(this);
    }

    /** transfer contents of attached entry to local entry (if it exists) otherwise, set activity to edit more **/
    private boolean getIncomingIntent() {
        if (getIntent().hasExtra("selected_content")) {
            mEntry = getIntent().getParcelableExtra("selected_content");

            Log.d(TAG, "onCreate: content imported: " + mEntry.toString());

            mMode = EDIT_MODE_DISABLED;
            mIsNewEntry = false;
            return false;
        } else {
            mMode = EDIT_MODE_ENABLED;
            mIsNewEntry = true;
            return true;
        }
    }

    /** pass contents of local entry to database, updating if it already exists or inserting a new entry if it does not **/
    private void saveChanges() {
        Log.d(TAG, "saveChanges: saving changes");
        if (mIsNewEntry) {                                                                          // new entry
            mEntryRepository.insertEntryTask(mEntry);
            Log.d(TAG, "saveChanges: inserted entry with " + mEntry.toString());
        } else {                                                                                    // updating existing entry
            mEntryRepository.updateEntry(mEntry);
            Log.d(TAG, "saveChanges: updated entry with " + mEntry.toString());
        }
    }

    private void enableEditMode() {
        mEditTitle.setBackgroundTintList(getColorStateList(R.color.grey_underline));                // display edittext underlines
        mEditDescription.setBackgroundTintList(getColorStateList(R.color.grey_underline));
        mViewStatus.setBackgroundTintList(getColorStateList(R.color.grey_underline));
        mEditCreator.setBackgroundTintList(getColorStateList(R.color.grey_underline));
        mEditDateCreated.setBackgroundTintList(getColorStateList(R.color.grey_underline));

        mBackView.setVisibility(View.GONE);                                                         // swap visibilty of views
        mCheckView.setVisibility(View.VISIBLE);
        mTitleHeader.setVisibility(View.VISIBLE);
        mDescriptionHeader.setVisibility(View.VISIBLE);
        mSpinnerStatus.setVisibility(View.VISIBLE);
        mViewStatus.setVisibility(View.GONE);
        mFab.hide();

        mMode = EDIT_MODE_ENABLED;
        enableContentInteraction();
        mEditTitle.requestFocus();
    }

    private void disableEditMode() {
        mEditTitle.setBackgroundTintList(getColorStateList(R.color.transparent_underline));         // hide edittext underlines
        mEditDescription.setBackgroundTintList(getColorStateList(R.color.transparent_underline));
        mViewStatus.setBackgroundTintList(getColorStateList(R.color.transparent_underline));
        mEditCreator.setBackgroundTintList(getColorStateList(R.color.transparent_underline));
        mEditDateCreated.setBackgroundTintList(getColorStateList(R.color.transparent_underline));

        mBackView.setVisibility(View.VISIBLE);                                                      // swap visibility of some views
        mCheckView.setVisibility(View.GONE);
        mTitleHeader.setVisibility(View.GONE);
        mDescriptionHeader.setVisibility(View.GONE);
        mSpinnerStatus.setVisibility(View.GONE);
        mViewStatus.setVisibility(View.VISIBLE);
        mFab.show();

        mMode = EDIT_MODE_DISABLED;
        disableContentInteraction();

        mEntry.setTitle(mEditTitle.getText().toString());                                           // set variables in edittexts in local entry variable
        mEntry.setDescription(mEditDescription.getText().toString());
        mEntry.setCreator(mEditCreator.getText().toString());
        mEntry.setDateCreated(mEditDateCreated.getText().toString());
        mEntry.setImageLocation(mImageLocation);

        saveChanges();                                                                              // save local entry variable to database
    }

    private void disableContentInteraction() {
        EditText views[] = {mEditTitle, mEditDescription, mEditCreator, mEditDateCreated};
        for (int i = 0; i < views.length; i++) {                                                    // step through edittexts, enabling each one
            views[i].setKeyListener(null);
            views[i].setFocusable(false);
            views[i].setFocusableInTouchMode(false);
            views[i].setCursorVisible(false);
            views[i].clearFocus();
        }

        mImageView.setOnClickListener(this);                                                        // enable imageview interaction
    }

    private void enableContentInteraction() {
        EditText views[] = {mEditTitle, mEditDescription, mEditCreator, mEditDateCreated};          // step through edittexts, disabling each one
        for (int i = 0; i < views.length; i++) {
            views[i].setKeyListener(new EditText(this).getKeyListener());
            views[i].setFocusable(true);
            views[i].setFocusableInTouchMode(true);
            views[i].setCursorVisible(true);
        }

        mImageView.setOnClickListener(null);                                                        // disable imageview interaction
    }

    /** disable software keyboard **/
    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager)
                this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /** read existing entry in to local variables **/
    private void readContent() {
        mTitle = mEntry.getTitle();                                                                 // read content from passed entry into local variables
        mDescription = mEntry.getDescription();
        mStatus = MediaEntry.getStringStatus(mEntry.getStatus());
        mStatusInt = mEntry.getStatus();
        mCreator = mEntry.getCreator();
        mDateCreated = mEntry.getDateCreated();
        mImageLocation = mEntry.getImageLocation();

        fillViews();
    }

    /** create default values for a new entry **/
    private void initialiseContent() {
        mTitle = "New Entry";                                                                       // create default variables
        mDescription = "Describe the entry here.";
        mStatusInt = 0;
        mStatus = MediaEntry.getStringStatus(mStatusInt);
        mImageLocation = null;                                                                      // does not need to be set as fillviews can handle empty image location

        mEntry = new MediaEntry();
        mEntry.setTitle("New Entry");

        fillViews();
    }

    /** fill views according to local variables and assign image according **/
    private void fillViews() {
        mEditTitle.setText(mTitle);                                                                 // fill views with appropriate local variables
        mEditDescription.setText(mDescription);
        mViewStatus.setText(mStatus);
        mEditCreator.setText(mCreator);
        mEditDateCreated.setText(mDateCreated);
        mViewStatus.setTextColor(getResources().getColor(MediaEntry.getStatusColor(mStatusInt)));

        if(Utility.isValidImageLocation(mImageLocation)){                                           // check if image file and imagelocation is valid, if not, display default image
            Glide.with(this).load(Uri.fromFile(new File(mImageLocation))).into(mImageView);
        } else {
            mImageView.setImageResource(R.drawable.placeholder_image);
        }
    }

    /** override clicks to certain views **/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.content_fab:                                                                  // when fab is pressed, enter edit mode
                enableEditMode();
                break;
            case R.id.view_check_button:                                                            // when checkmark is pressed, leave editmode, hide keyboard
                disableEditMode();
                hideSoftKeyboard();
                break;
            case R.id.view_back_button:                                                             // when back button is pressed, return to main activity
                finish();
                break;
            case R.id.view_image:                                                                   // when image is pressed, redirect to gallery picker
                pickImage();
                break;
        }
    }

    /** override back button behaviour if the activity is in edit mode **/
    @Override
    public void onBackPressed() {
        if (mMode == EDIT_MODE_ENABLED) {                                                           // if activity is in edit mode, make back return you to view mode
            onClick(mCheckView);
        } else {
            super.onBackPressed();
        }
    }

    /* methods for handling changes in state (rotating screen, etc) */
    /** save state before change **/
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState,
                                    @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("mode", mMode);                                                             // save state to outstate bundle
    }

    /** restore state after change **/
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMode = savedInstanceState.getInt("mode");                                              // restore state from bundle
        if (mMode == EDIT_MODE_ENABLED) {
            enableEditMode();
        }
    }

    /* spinner handling overrides */
    /** update status variable when a spinner option is selected **/
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mStatusInt = position;                                                                      // update local variables
        mStatus = MediaEntry.getStringStatus(position);
        mEntry.setStatus(position);                                                                 // update local entry
        mViewStatus.setText(mStatus);                                                               // update view contents and color
        mViewStatus.setTextColor(getResources().getColor(MediaEntry.getStatusColor(mStatusInt)));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    /* image file handling functions */
    /** redirect the user to the gallery picker **/
    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    /** get permission to access device storage **/
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                    , Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, STORAGE_PERMISSION_CODE);
        }
    }

    /** handle result of gallery picker intent from pickimage() **/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(this, "no image returned", Toast.LENGTH_SHORT).show();   // do nothing if no image was selected
                return;
            }
            try {
                mSelectedImage = data.getData();
                if (mSelectedImage != null && getContentResolver() != null){
                    mBitmap = BitmapFactory.decodeStream(getContentResolver().
                            openInputStream(mSelectedImage));
                    String path = getPath(mSelectedImage);                                          // get image path
                    if (path != null){
                        Glide.with(DetailsActivity.this).asBitmap().                         // only using glide based on other code, either update the rest of the code
                                load(mBitmap).into(mImageView);                                     // to use glide, or change this to regular android commands

                        mImageLocation = path;
                        Toast.makeText(this, "image successfully imported",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (FileNotFoundException e) {
                Log.e(TAG, "onActivityResult: error updating image", e);                        // send error if file not found
            }
        }
    }

    /** get path of imported image **/
    private String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri,
                null,
                null,
                null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();
            String document_id = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
            cursor.close();
            cursor = getContentResolver().query(
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null,
                    MediaStore.Images.Media._ID + " = ? ",
                    new String[]{document_id},
                    null);
            if (cursor != null) {
                cursor.moveToFirst();
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                cursor.close();
                return path;
            }
        }
        return null;
    }

//    private void saveImage(MediaEntry entry, String count) {
//
//        if (Utility.saveImageToInternalStorage(getApplicationContext(), mBitmap, mEditTitle + count)) {
//            entry.setImageLocation(String.valueOf(getFileStreamPath(placename.getSelectedItem().toString() + count)));
//            Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show();
//            AppDatabase.getAppDatabase(getApplicationContext()).userDao().insertAll(user);
//            Toast.makeText(getApplicationContext(), "" + AppDatabase.getAppDatabase(getApplicationContext()).userDao().countusers(), Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Failed to save image", Toast.LENGTH_SHORT).show();
//        }
//    }
}
