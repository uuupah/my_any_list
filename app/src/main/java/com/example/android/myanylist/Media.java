package com.example.android.myanylist;

// class to bundle name and icon together for media menu

public class Media {

    private static final int NO_IMAGE_PROVIDED = -1;

    private String mName;                                           // name of the media type
    private int mImageResourceId = NO_IMAGE_PROVIDED;               // material icon for media type

    public Media(String Name, int ImageResourceId) {                // constructor
        mName = Name;
        mImageResourceId = ImageResourceId;
    }

    public String getName() {
        return mName;
    }

    public int getImageResourceId() {
        return mImageResourceId;
    }

    public boolean hasImage() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }
}
