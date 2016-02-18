package com.nex3z.examples.widget.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageUtility {
    private static final String LOG_TAG = ImageUtility.class.getSimpleName();

    public static final String BASE_URL = "http://image.tmdb.org/t/p/";

    public static final String POSTER_SIZE_W92 = "w92";
    public static final String POSTER_SIZE_W154 = "w154";
    public static final String POSTER_SIZE_W185 = "w185";
    public static final String POSTER_SIZE_W342 = "w342";
    public static final String POSTER_SIZE_W500 = "w500";
    public static final String POSTER_SIZE_W780 = "w780";
    public static final String POSTER_SIZE_ORIGINAL = "original";

    public static String getImageUrl(String path, String size) {
        String url = BASE_URL + size + "/" + path;
        return url;
    }

    public static String getImageUrl(String path) {
        return getImageUrl(path, POSTER_SIZE_W342);
    }

    public static Bitmap downloadBitmap(String url) {
        try {
            URL imageUrl = new URL(url);
            Log.v(LOG_TAG, "Poster imageUrl: " + imageUrl);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();

            conn.connect();
            InputStream in = conn.getInputStream();

            Bitmap poster = BitmapFactory.decodeStream(in);
            in.close();

            return poster;
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

        return null;
    }
}
