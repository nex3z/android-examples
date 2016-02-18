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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Call;

public class StackWidgetRemoteViewsService extends RemoteViewsService {
    private static final String LOG_TAG = StackWidgetRemoteViewsService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private List<Movie> mMovies;
            private List<Bitmap> mBitmaps;

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
                    mBitmaps = new ArrayList<>();
                    while(mBitmaps.size() < mMovies.size()) mBitmaps.add(null);
                } catch (IOException e) {
                    Log.e(LOG_TAG, e.getMessage());
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
                if (position > getCount() - 1) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_stack_item);
                if (mBitmaps.get(position) == null) {
                    Log.v(LOG_TAG, "getViewAt(): bitmap is null, downloading.");
                    views.setImageViewResource(R.id.widget_movie_poster, R.drawable.placeholder_poster_white);
                    String posterUrl = ImageUtility.getImageUrl(
                            mMovies.get(position).getPosterPath());
                    Bitmap bitmap = ImageUtility.downloadBitmap(posterUrl);
                    mBitmaps.add(position, bitmap);
                }
                views.setImageViewBitmap(R.id.widget_movie_poster, mBitmaps.get(position));

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
