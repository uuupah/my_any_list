// MediaContract.java
// this file contains the columns expected to be in the media table, as well as enumerated values
// used by the table

package com.example.android.myanylist.data;

import android.net.Uri;
import android.provider.BaseColumns;

public final class MediaContract {
    private MediaContract () {}                                                                     // empty constructor to stop contract from being instantiated

    public static final String CONTENT_AUTHORITY = "com.example.android.myanylist";                 // set up all the uri stuff that sucks and i hate it
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MEDIA = "media";

    public static final class MediaEntry implements BaseColumns {                                   // define constant values for the table, each entry is a media type
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MEDIA);

        //TODO mime type definitions

        public static final String TABLE_NAME = "Media";

        // columns
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_MEDIA_NAME = "Name";
        public static final String COLUMN_CREATOR_TYPE = "Creator type";

        // creator type enum
        public static final int CREATOR_CREATOR = 0;
        public static final int CREATOR_AUTHOR = 1;
        public static final int CREATOR_DIRECTOR = 3;

        public static boolean isValidCreator(int n) {
            if (n == CREATOR_CREATOR || n == CREATOR_AUTHOR || n == CREATOR_DIRECTOR) {
                return true;
            }
            return false;
        }
    }
}
