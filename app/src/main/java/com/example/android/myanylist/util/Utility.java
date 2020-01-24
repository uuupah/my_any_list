package com.example.android.myanylist.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {

    public static String getCurrentTimestamp(){
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
            String currentDateTime = dateFormat.format(new Date());

            return currentDateTime;
        }catch(Exception e){
            return null;
        }
    }

    // copied
    public static boolean saveImageToInternalStorage(final Context context, final Bitmap image, String fileName) {

        try {
            final FileOutputStream fos = context.openFileOutput(fileName+".png", Context.MODE_PRIVATE);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

            return true;
        } catch (Exception e) {

            return false;
        }
    }

    // copied
    public static Bitmap loadImageBitmap(final Context context,String name) {

        final FileInputStream fileInputStream;
        Bitmap bitmap = null;
        try {
            fileInputStream = context.openFileInput(name+".png");

            bitmap = BitmapFactory.decodeStream(fileInputStream);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
