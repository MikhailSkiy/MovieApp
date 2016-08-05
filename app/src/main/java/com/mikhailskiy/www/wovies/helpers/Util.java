package com.mikhailskiy.www.wovies.helpers;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;

import com.mikhailskiy.www.wovies.R;
import com.mikhailskiy.www.wovies.activities.MainActivity;
import com.mikhailskiy.www.wovies.models.Genre;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Mikhail Valuyskiy on 02.09.2015.
 */
public class Util {

    public static String getUserFriendlyRuntime(String runtime, Context context) {
        String userFriendlyRuntime = runtime + " " + context.getResources().getString(R.string.runtime_tag);
        return userFriendlyRuntime;
    }

    public static String getUserFriendlyOrigLanguage(String original, Context context) {
        String originalLanguage = "English";
        if (original == "en") {
            originalLanguage = context.getResources().getString(R.string.original_en_language);
        }
        return originalLanguage;
    }

    public static String getLanguage(){
        String local="";
        String code = Locale.getDefault().getISO3Language();
        if (code.equals("rus")){
            local = "ru";
        } else {
            local = "en";
        }

        return local;
    }

    public static String getUIFriendlyData(String data) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(data);
        } catch (ParseException e) {

        }
        dateFormat.applyPattern("dd MMM yyyy");
        String result = dateFormat.format(date);
        return result;
    }

    public static long getUnixDate(String data) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(data);
        } catch (ParseException e) {

        }
        return date.getTime();
    }

    public static String getDateFromUnix(long date) {
        Calendar myDate = Calendar.getInstance();
        myDate.setTimeInMillis(date * 1000);
        String convertedDate = myDate.get(Calendar.DAY_OF_MONTH) + "." + myDate.get(Calendar.MONTH) + "." + myDate.get(Calendar.YEAR);
        return convertedDate;
    }

    public static String getGenres(List<Genre> genres) {
        String allGenres = "";
        int genresCount = 0;
        if (genres.size() > 2) {
            genresCount = 2;
        } else {
            genresCount = genres.size();
        }

        for (int i = 0; i < genresCount; i++) {
            String genre = genres.get(i).getGenreName();
            String separator = "";
            if (genresCount - 1 == i) {
                allGenres += genre + separator;
            } else {
                separator = ",";
                allGenres += genre + separator;
            }
        }
        return allGenres;
    }

    public static Bitmap getRoundedCroppedBitmap(Bitmap bitmap, int radius) {
        Bitmap finalBitmap;
        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius)
            finalBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius, false);
        else
            finalBitmap = bitmap;
        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(), finalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, finalBitmap.getWidth(), finalBitmap.getHeight());
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(finalBitmap.getWidth() / 2 + 0.7f,
                finalBitmap.getHeight() / 2 + 0.7f,
                finalBitmap.getWidth() / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(finalBitmap, rect, rect, paint);

        return output;
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

    public static int getIconId(int itemId){
        int id=0;
        switch (itemId){
            case R.id.rate_app_title_id :
            id = R.drawable.rate;
            break;

            case R.id.share_ideas_title_id:
                // Todo change icon for feedback
                id = R.drawable.rate;
                break;

            case R.id.invite_friends_title_id:
                // Todo change icon for invate friends
                id = R.drawable.rate;
            break;

            case R.id.about_title_id:
                id = R.drawable.ic_information_outline_grey;
                break;

            default:break;
        }
        return id;
    }

    public static Drawable getDrawable(byte[] image) {
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

    public static String getStringResource(int stringId) {
        Context context = MainActivity.getContextOfApplication();
        return context.getResources().getString(stringId);
    }

    public static int getConnectionStatus(Context context) {
        if (isNetworkAvailable(context)) {
            return Constants.CONNECTION_STATUS_OK;
        } else {
            return Constants.NO_CONNECTION;
        }
    }

    public static boolean isUserLogedIn(){
        String userId = SharedPrefUtil.getAccountIdFromSharedPrefs();
        if (userId!=null){
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns true if the network is available or about to become available
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
