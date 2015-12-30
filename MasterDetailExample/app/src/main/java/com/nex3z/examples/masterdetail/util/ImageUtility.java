package com.nex3z.examples.masterdetail.util;

public class ImageUtility {
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
}
