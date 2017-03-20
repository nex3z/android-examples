package com.nex3z.examples.retrofit2.rest.typeadapter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nex3z.examples.retrofit2.model.Movie;

public class MovieTypeAdapterFactory extends CustomTypeAdapterFactory<Movie> {
    private static final String LOG_TAG= MovieTypeAdapterFactory.class.getSimpleName();

    public MovieTypeAdapterFactory() {
        super(Movie.class);
    }

    @Override
    protected void afterRead(JsonElement tree) {
        JsonObject custom = tree.getAsJsonObject();
        float vote = custom.get("vote_average").getAsFloat();
        int score = (int)(vote * 10);
        custom.addProperty("score", score);
    }
}
