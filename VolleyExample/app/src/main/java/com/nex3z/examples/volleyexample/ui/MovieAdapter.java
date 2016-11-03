package com.nex3z.examples.volleyexample.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.nex3z.examples.volleyexample.R;
import com.nex3z.examples.volleyexample.model.Movie;
import com.nex3z.examples.volleyexample.net.VolleySingleton;
import com.nex3z.examples.volleyexample.util.ImageUtility;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private List<Movie> mMovies;
    private ImageLoader mImageLoader;

    public MovieAdapter(List<Movie> movies) {
        mMovies = movies;
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        mImageLoader = VolleySingleton.getInstance(context).getImageLoader();


        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.item_movie, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.ViewHolder holder, int position) {
        Movie movie = mMovies.get(position);

        holder.mTitle.setText(movie.getTitle());

        String key = movie.getPosterPath();
        String url = ImageUtility.getImageUrl(key);

        holder.mPoster.setImageUrl(url, mImageLoader);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public NetworkImageView mPoster;
        public TextView mTitle;

        public ViewHolder(View itemView) {
            super(itemView);

            mPoster = (NetworkImageView) itemView.findViewById(R.id.movie_poster);
            mTitle = (TextView) itemView.findViewById(R.id.movie_title);
        }
    }
}
