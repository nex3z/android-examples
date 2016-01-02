package com.nex3z.examples.contentprovider.util;


import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.nex3z.examples.contentprovider.model.Movie;
import com.nex3z.examples.contentprovider.provider.MovieContract;

import java.util.List;
import java.util.Vector;

public class MovieDbUtility {
    private static final String LOG_TAG = MovieDbUtility.class.getSimpleName();

    public static void addToDatabase(Context context, List<Movie> movies) {
        Vector<ContentValues> cVVector = new Vector<ContentValues>(movies.size());

        for (int i = 0; i < movies.size(); i++) {
            ContentValues movieValues = buildContentValues(movies.get(i));
            cVVector.add(movieValues);
        }
        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            context.getContentResolver()
                    .bulkInsert(MovieContract.MovieEntry.CONTENT_URI, cvArray);
        }
        Log.d(LOG_TAG, "addToDatabase(): Write database Complete, "
                + cVVector.size() + " Inserted.");
    }

    public static ContentValues buildContentValues(Movie movie) {
        ContentValues movieValues = new ContentValues();

        movieValues.put(MovieContract.MovieEntry.COLUMN_ID, movie.getId());
        movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        movieValues.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, movie.getVoteCount());
        movieValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        movieValues.put(MovieContract.MovieEntry.COLUMN_ADULT, movie.isAdult());
        movieValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdropPath());
        movieValues.put(MovieContract.MovieEntry.COLUMN_GENRE_IDS, movie.getGenreIds().toString());
        movieValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_LANGUAGE, movie.getOriginalLanguage());
        movieValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        movieValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        movieValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, movie.getPopularity());
        movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        movieValues.put(MovieContract.MovieEntry.COLUMN_VIDEO, movie.isVideo());

        return movieValues;
    }

}
