package com.example.android.myanylist.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {
    private static final String TAG = "Utility";

    /** retrieve the current time as a string **/
    public static String getCurrentTimestamp(){
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
            String currentDateTime = dateFormat.format(new Date());

            return currentDateTime;
        }catch(Exception e){
            return null;
        }
    }

    /** check if the file at location loc exists and is a valid image file **/
    public static boolean isValidImageLocation(String loc){

        if(loc == null || !(new File(loc).exists())){                                               // file does not exist
            Log.d(TAG, "fillViews: file does not exist");
            return false;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(loc, options);
        if(options.outWidth != -1 && options.outHeight != -1) {                                     // file exists and is valid
            Log.d(TAG, "isValidImageLocation: valid file at location");
            return true;
        } else {                                                                                    // file is invalid
            Log.d(TAG, "isValidImageLocation: invalid file at location");
            return true;
        }
    }

//    public static boolean saveImageToInternalStorage(final Context context, final Bitmap image, String fileName) {
//
//        try {
//            final FileOutputStream fos = context.openFileOutput(fileName+".png", Context.MODE_PRIVATE);
//            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
//            fos.close();
//
//            return true;
//        } catch (Exception e) {
//
//            return false;
//        }
//    }
//
//    public static Bitmap loadImageBitmap(final Context context,String name) {
//
//        final FileInputStream fileInputStream;
//        Bitmap bitmap = null;
//        try {
//            fileInputStream = context.openFileInput(name+".png");
//
//            bitmap = BitmapFactory.decodeStream(fileInputStream);
//            fileInputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return bitmap;
//    }
}
