package com.example.admin.moviesapp.helpers;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.example.admin.moviesapp.R;
import com.example.admin.moviesapp.models.Genre;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * Created by Mikhail Valuyskiy on 02.09.2015.
 */
public class Util {

    public static String getUserFriendlyRuntime(String runtime,Context context){
        String userFriendlyRuntime = runtime + " " + context.getResources().getString(R.string.runtime_label);
        return userFriendlyRuntime;
    }

    public static String getUserFriendlyOrLanguage(String original,Context context){
        String originalLanguage = "English";
        if (original == "en"){
           originalLanguage = context.getResources().getString(R.string.original_language);
        }
        return originalLanguage;
    }

    public static String getGenres(List<Genre> genres){
        String allGenres = "";
        for (int i=0;i<genres.size();i++){
           String genre =  genres.get(i).getGenreName();
            allGenres +=genre + ",";
        }
        return allGenres;
    }

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

    public static String getApiKey(Context context) {
        String placesApiKey = "";
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = info.metaData;
            placesApiKey = bundle.getString("com.google.android.maps.v2.API_KEY");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(context.getApplicationInfo().className, "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e(context.getApplicationInfo().className, "Failed to load meta-data, NullPointer: " + e.getMessage());
        }
        return placesApiKey;
    }
}
