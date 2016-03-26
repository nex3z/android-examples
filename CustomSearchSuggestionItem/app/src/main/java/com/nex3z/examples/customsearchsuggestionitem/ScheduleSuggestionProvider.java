package com.nex3z.examples.customsearchsuggestionitem;


import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.net.Uri;


public class ScheduleSuggestionProvider extends SearchRecentSuggestionsProvider {
    private static final String LOG_TAG = ScheduleSuggestionProvider.class.getSimpleName();

    public final static String AUTHORITY = "com.nex3z.examples.customsearchsuggestionitem.ScheduleSuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public ScheduleSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

}
