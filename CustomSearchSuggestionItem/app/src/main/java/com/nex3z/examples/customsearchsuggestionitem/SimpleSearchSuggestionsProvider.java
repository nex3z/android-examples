package com.nex3z.examples.customsearchsuggestionitem;


import android.content.SearchRecentSuggestionsProvider;


public class SimpleSearchSuggestionsProvider extends SearchRecentSuggestionsProvider {
    private static final String LOG_TAG = SimpleSearchSuggestionsProvider.class.getSimpleName();

    public final static String AUTHORITY = "com.nex3z.examples.customsearchsuggestionitem.SimpleSearchSuggestionsProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public SimpleSearchSuggestionsProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

}
