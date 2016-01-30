package com.nex3z.examlpes.mvp.presentation.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nex3z.examlpes.mvp.R;
import com.nex3z.examlpes.mvp.data.entity.MovieEntity;
import com.nex3z.examlpes.mvp.presentation.util.ImageUtility;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieAdapter extends  RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private List<MovieEntity> mMovies;

    public MovieAdapter(List<MovieEntity> movies) {
        mMovies = movies;
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_movie, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieAdapter.ViewHolder holder, int position) {
        MovieEntity movieEntity = mMovies.get(position);

        holder.mTitle.setText(movieEntity.getTitle());

        String key = movieEntity.getPosterPath();
        String url = ImageUtility.getImageUrl(key);
        Picasso.with(holder.itemView.getContext())
                .load(url)
                .error(R.drawable.placeholder_poster_white)
                .placeholder(R.drawable.placeholder_poster_white)
                .into(holder.mPoster);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.movie_poster) ImageView mPoster;
        @Bind(R.id.movie_title) TextView mTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
