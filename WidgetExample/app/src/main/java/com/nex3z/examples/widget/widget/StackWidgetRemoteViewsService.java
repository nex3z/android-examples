package com.nex3z.examples.widget.widget;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.nex3z.examples.widget.R;
import com.nex3z.examples.widget.app.App;
import com.nex3z.examples.widget.model.Movie;
import com.nex3z.examples.widget.rest.model.MovieResponse;
import com.nex3z.examples.widget.rest.service.MovieService;
import com.nex3z.examples.widget.util.ImageUtility;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import retrofit.Call;

public class StackWidgetRemoteViewsService extends RemoteViewsService {
    private static final String LOG_TAG = StackWidgetRemoteViewsService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private List<Movie> mMovies;

            @Override
            public void onCreate() { }

            @Override
            public void onDataSetChanged() {
                Log.v(LOG_TAG, "onDataSetChanged()");
                MovieService movieService = App.getRestClient().getMovieService();
                Call<MovieResponse> call = movieService.getMovies(MovieService.SORT_BY_POPULARITY_DESC, 1);

                try {
                    MovieResponse response = call.execute().body();
                    List<Movie> movies = response.getMovies();
                    mMovies = movies;
                    Log.v(LOG_TAG, "onDataSetChanged(): mMovies.size() = " + mMovies.size());
                } catch (IOException e) {
                    Log.e(LOG_TAG, "ERROR: ", e);
                }
            }

            @Override
            public void onDestroy() { }

            @Override
            public int getCount() {
                return mMovies == null ? 0 : mMovies.size();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                Log.v(LOG_TAG, "getViewAt(): position = " + position);

                RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_stack_item);

                String posterPath = ImageUtility
                        .getImageUrl(mMovies.get(position).getPosterPath());
                try {
                    Bitmap poster = Picasso.with(StackWidgetRemoteViewsService.this)
                            .load(ImageUtility.getImageUrl(posterPath))
                            .get();
                    views.setImageViewBitmap(R.id.widget_movie_poster, poster);
                }catch (IOException e) {
                    Log.e(LOG_TAG, "ERROR: ", e);
                }

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_stack_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (position < mMovies.size()) {
                    return mMovies.get(position).getId();
                }
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
}
