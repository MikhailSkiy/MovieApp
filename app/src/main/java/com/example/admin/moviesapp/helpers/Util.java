package com.example.admin.moviesapp.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by Mikhail Valuyskiy on 02.09.2015.
 */
public class Util {

    /**
     * Converts byte[] to Bitmap
     */
    public static Bitmap getBitmapFromBytes(byte[] image) {
        Bitmap decodedImage = null;
        if (image != null) {
            ByteArrayInputStream imageStream = new ByteArrayInputStream(image);
            decodedImage = BitmapFactory.decodeStream(imageStream);
        }
        return decodedImage;
    }

    public static Drawable getDrawable(byte [] image){
        Drawable drawable = new BitmapDrawable(BitmapFactory.decodeByteArray(image, 0, image.length));
        return drawable;
    }

    public static byte[] getBytesFromBitmap(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte imageInByte[] = stream.toByteArray();
        return imageInByte;
    }

    public static byte[] getBytesFromDrawable(Drawable image) {
        Bitmap bitmap = ((BitmapDrawable) image).getBitmap();
        byte imageInByte[] = getBytesFromBitmap(bitmap);
        return imageInByte;
    }
}
