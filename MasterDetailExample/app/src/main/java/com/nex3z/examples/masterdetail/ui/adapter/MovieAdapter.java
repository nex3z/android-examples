package com.nex3z.examples.masterdetail.ui.adapter;

import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nex3z.examples.masterdetail.R;
import com.nex3z.examples.masterdetail.model.Movie;
import com.nex3z.examples.masterdetail.util.ImageUtility;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends  RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private List<Movie> mMovies;
    private static OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position, MovieAdapter.ViewHolder vh);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public MovieAdapter(List<Movie> movies) {
        mMovies = movies;
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_movie, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(final MovieAdapter.ViewHolder holder, int position) {
        Movie movie = mMovies.get(position);

        holder.mTitle.setText(movie.getTitle());

        String key = movie.getPosterPath();
        String url = ImageUtility.getImageUrl(key);
        Picasso.with(holder.itemView.getContext())
                .load(url)
                .error(R.drawable.placeholder_poster_white)
                .placeholder(R.drawable.placeholder_poster_white)
                .into(holder.mPoster, new PicassoCallBack(holder.mPoster));
    }

    private class PicassoCallBack implements com.squareup.picasso.Callback {
        ImageView mImageView;
        public PicassoCallBack(ImageView imageView) {
            mImageView = imageView;
        }

        @Override
        public void onSuccess() {
            mImageView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right,
                                           int bottom, int oldLeft, int oldTop,
                                           int oldRight, int oldBottom) {
                    v.removeOnLayoutChangeListener(this);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        int cx = (mImageView.getLeft() + mImageView.getRight()) / 2;
                        int cy = (mImageView.getTop() + mImageView.getBottom()) / 2;

                        int finalRadius = Math.max(mImageView.getWidth(), mImageView.getHeight());

                        Animator anim = ViewAnimationUtils.createCircularReveal(
                                mImageView, cx, cy, 0, finalRadius);

                        mImageView.setVisibility(View.VISIBLE);
                        anim.start();
                    } else {
                        mImageView.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        @Override
        public void onError() {

        }
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mPoster;
        public TextView mTitle;

        public ViewHolder(View itemView) {
            super(itemView);

            mPoster = (ImageView) itemView.findViewById(R.id.movie_poster);
            mTitle = (TextView) itemView.findViewById(R.id.movie_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onItemClick(getLayoutPosition(), ViewHolder.this);
                    }
                }
            });
        }
    }
}
