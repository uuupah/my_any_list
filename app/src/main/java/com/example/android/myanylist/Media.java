// Media.java
// this class is used to bundle the name and description of a media type together for use in the

package com.example.android.myanylist;

public class Media {

    private String mName;                                           // name of the media type
    private String mDesc;                                           // description of the media type

    public Media(String Name, String Desc) {                        // constructor
        mName = Name;
        mDesc = Desc;
    }

    public String getName() {
        return mName;
    }

    public String getDesc() { return mDesc; }
}
