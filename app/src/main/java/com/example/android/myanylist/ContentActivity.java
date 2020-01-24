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

import java.io.FileNotFoundException;

public class ContentActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "ContentActivity";
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
    private static MediaEntry mInitialEntry;
    private boolean mIsNewEntry;
    private String mTitle, mDescription, mStatus, mCreator, mDateCreated;
    private int mImageRes, mStatusInt, mMode;
    private EntryRepository mEntryRepository;
    private Uri mSelectedImage;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        requestStoragePermission(); // request access to storage through android
        initialiseViews(); // links view variables to their link id
        setListeners(); // set onclicklisteners to appropriate views

        mEntryRepository = new EntryRepository(this);

        if (getIncomingIntent()) {
            // new entry, edit mode
            initialiseContent();
            enableEditMode();

        } else {
            // existing note, view mode
            readContent();
            disableContentInteraction();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.content_list_toolbar);
        setSupportActionBar(toolbar);
    }

    private void initialiseViews() {
        // toolbar buttons
        mBackView = findViewById(R.id.view_back_button);
        mCheckView = findViewById(R.id.view_check_button);

        // headers that are shown during edit mode
        mTitleHeader = findViewById(R.id.view_title_header);
        mDescriptionHeader = findViewById(R.id.view_description_header);

        // content
        mEditTitle = findViewById(R.id.edit_title);
        mEditDescription = findViewById(R.id.edit_description);
        mViewStatus = findViewById(R.id.view_status);
        mEditCreator = findViewById(R.id.edit_creator);
        mEditDateCreated = findViewById(R.id.edit_date_created);
        mFab = findViewById(R.id.content_fab);

        mImageView = findViewById(R.id.view_image);

        //set up spinner
        mSpinnerStatus = findViewById(R.id.status_spinner);
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this, R.array.status, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerStatus.setAdapter(statusAdapter);
        mSpinnerStatus.setOnItemSelectedListener(this);

        // update status color
        mViewStatus.setTextColor(ContextCompat.getColor(this, R.color.status_planning_yellow)); // assuming that planning is the default value
    }

    private void setListeners() {
        mFab.setOnClickListener(this);
        mCheckView.setOnClickListener(this);
        mBackView.setOnClickListener(this);
    }

    private boolean getIncomingIntent() {
        if (getIntent().hasExtra("selected_content")) {
            mInitialEntry = getIntent().getParcelableExtra("selected_content");

            Log.d(TAG, "onCreate: content imported: " + mInitialEntry.toString());

            mMode = EDIT_MODE_DISABLED;
            mIsNewEntry = false;
            return false;
        }
        mMode = EDIT_MODE_ENABLED;
        mIsNewEntry = true;
        return true;
    }

    private void saveChanges() {
        Log.d(TAG, "saveChanges: saving changes");
        if (mIsNewEntry) {
            // insert
            saveNewEntry();
        } else {
            // update
            updateEntry();
        }
    }

    private void updateEntry() {
        mEntryRepository.updateEntry(mInitialEntry);
    }

    private void saveNewEntry() {
        mEntryRepository.insertEntryTask(mInitialEntry);
    }

    private void enableEditMode() {
        displayEditTextUnderline();
        mBackView.setVisibility(View.GONE);
        mCheckView.setVisibility(View.VISIBLE);
        mTitleHeader.setVisibility(View.VISIBLE);
        mDescriptionHeader.setVisibility(View.VISIBLE);
        mSpinnerStatus.setVisibility(View.VISIBLE);
        mViewStatus.setVisibility(View.GONE);

        mImageView.setOnClickListener(this);

        mMode = EDIT_MODE_ENABLED;

        mFab.hide();

        enableContentInteraction();

        mEditTitle.requestFocus();
    }

    private void disableEditMode() {
        hideEditTextUnderline();
        mBackView.setVisibility(View.VISIBLE);
        mCheckView.setVisibility(View.GONE);
        mTitleHeader.setVisibility(View.GONE);
        mDescriptionHeader.setVisibility(View.GONE);
        mSpinnerStatus.setVisibility(View.GONE);
        mViewStatus.setVisibility(View.VISIBLE);

        mImageView.setOnClickListener(null); // disable edit mode button

        mMode = EDIT_MODE_DISABLED;

        mFab.show();

        mInitialEntry.setTitle(mEditTitle.getText().toString());
        mInitialEntry.setDescription(mEditDescription.getText().toString());
        // status does not need to be set as it is set every time the spinner is changed
        mInitialEntry.setCreator(mEditCreator.getText().toString());
        mInitialEntry.setDateCreated(mEditDateCreated.getText().toString());
        // mInitialEntry.setTimeStamp(Utility.getCurrentTimestamp()); // not sure if this should be changed every time

        saveChanges();
    }

    private void disableContentInteraction() {
        disableEditText(mEditTitle);
        disableEditText(mEditDescription);
        disableEditText(mEditCreator);
        disableEditText(mEditDateCreated);
    }

    private void disableEditText(EditText v) {
        v.setKeyListener(null);
        v.setFocusable(false);
        v.setFocusableInTouchMode(false);
        v.setCursorVisible(false);
        v.clearFocus();
    }

    private void enableContentInteraction() {
        enableEditText(mEditTitle);
        enableEditText(mEditDescription);
        enableEditText(mEditCreator);
        enableEditText(mEditDateCreated);
    }

    private void enableEditText(EditText v) {
        v.setKeyListener(new EditText(this).getKeyListener());
        v.setFocusable(true);
        v.setFocusableInTouchMode(true);
        v.setCursorVisible(true);
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void readContent() {
        mTitle = mInitialEntry.getTitle();
        mDescription = mInitialEntry.getDescription();
        mStatus = MediaEntry.getStringStatus(mInitialEntry.getStatus());
        mStatusInt = mInitialEntry.getStatus();
        mCreator = mInitialEntry.getCreator();
        mDateCreated = mInitialEntry.getDateCreated();
        mImageRes = mInitialEntry.getImage();

        fillViews();
    }

    private void initialiseContent() {
        mTitle = "New Entry";
        mDescription = "Describe the entry here.";
        mStatusInt = 0;
        mStatus = MediaEntry.getStringStatus(mStatusInt);
//        creator = "Creator";
//        dateCreated = "01 Jan 1970";
        mImageRes = R.drawable.dark_souls; // temporary image

        mInitialEntry = new MediaEntry();
        mInitialEntry.setTitle("New Entry");

        fillViews();
    }

    private void fillViews() {
        mEditTitle.setText(mTitle);
        mEditDescription.setText(mDescription);
        mViewStatus.setText(mStatus);
        mEditCreator.setText(mCreator);
        mEditDateCreated.setText(mDateCreated);
        mImageView.setImageResource(mImageRes);
        mViewStatus.setTextColor(getResources().getColor(MediaEntry.getStatusColor(mStatusInt)));
    }

    private void displayEditTextUnderline() {
        mEditTitle.setBackgroundTintList(getColorStateList(R.color.grey_underline));
        mEditDescription.setBackgroundTintList(getColorStateList(R.color.grey_underline));
        mViewStatus.setBackgroundTintList(getColorStateList(R.color.grey_underline));
        mEditCreator.setBackgroundTintList(getColorStateList(R.color.grey_underline));
        mEditDateCreated.setBackgroundTintList(getColorStateList(R.color.grey_underline));
    }

    private void hideEditTextUnderline() {
        mEditTitle.setBackgroundTintList(getColorStateList(R.color.transparent_underline));
        mEditDescription.setBackgroundTintList(getColorStateList(R.color.transparent_underline));
        mViewStatus.setBackgroundTintList(getColorStateList(R.color.transparent_underline));
        mEditCreator.setBackgroundTintList(getColorStateList(R.color.transparent_underline));
        mEditDateCreated.setBackgroundTintList(getColorStateList(R.color.transparent_underline));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
            case R.id.view_image:
                pickImage();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (mMode == EDIT_MODE_ENABLED) {
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
        if (mMode == EDIT_MODE_ENABLED) {
            enableEditMode();
        }
    }

    // methods for handling spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mStatusInt = position;
        mStatus = MediaEntry.getStringStatus(position);
        mInitialEntry.setStatus(position);
        mViewStatus.setText(mStatus);
        mViewStatus.setTextColor(getResources().getColor(MediaEntry.getStatusColor(mStatusInt)));
        Log.d(TAG, "onItemSelected: changed color to " + MediaEntry.getStatusColor(mStatusInt));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // methods for handling image input
    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivity(intent);
        //        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE
                    , Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, STORAGE_PERMISSION_CODE);
        }
    }

    // onActivityResult
        // check activities of type PICK_IMAGE
        // check there is data attached
        // try/catch for filenotfoundexception
            // assign data from return to a uri
            // use bitmapfactory to get a bitmap from the uri
            // get the path for said uri
        // (for now) update image view with new image


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(this, "no image returned", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                mSelectedImage = data.getData();
                if (mSelectedImage != null && getContentResolver() != null){
                    mBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(mSelectedImage));
                    String path = getPath(mSelectedImage);
                    if (path != null){
                        Glide.with(ContentActivity.this).asBitmap().load(mBitmap).into(mImageView);     // only using glide based on other code, either update the rest of the code
                                                                                                               // to use glide, or change this to regular android commands
                    }
                }
            } catch (FileNotFoundException e) {
                Log.e(TAG, "onActivityResult: error updating image", e);
            }
        }
    }

//     copied
    private String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            String document_id = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
            cursor.close();
            cursor = getContentResolver().query(
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
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
