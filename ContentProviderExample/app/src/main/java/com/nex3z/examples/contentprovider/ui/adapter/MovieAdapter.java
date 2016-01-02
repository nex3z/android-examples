package com.nex3z.examples.contentprovider.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nex3z.examples.contentprovider.R;
import com.nex3z.examples.contentprovider.ui.fragment.MainActivityFragment;
import com.nex3z.examples.contentprovider.util.ImageUtility;
import com.squareup.picasso.Picasso;

public class MovieAdapter extends  RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private Cursor mCursor;

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
        mCursor.moveToPosition(position);

        String title = mCursor.getString(MainActivityFragment.COL_MOVIE_TITLE);
        holder.mTitle.setText(title);

        String key = mCursor.getString(MainActivityFragment.COL_MOVIE_POSTER_PATH);
        String url = ImageUtility.getImageUrl(key);
        Picasso.with(holder.itemView.getContext())
                .load(url)
                .error(R.drawable.placeholder_poster_white)
                .placeholder(R.drawable.placeholder_poster_white)
                .into(holder.mPoster);
    }

    @Override
    public int getItemCount() {
        if (mCursor != null) {
            return mCursor.getCount();
        } else {
            return 0;
        }
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mPoster;
        public TextView mTitle;

        public ViewHolder(View itemView) {
            super(itemView);

            mPoster = (ImageView) itemView.findViewById(R.id.movie_poster);
            mTitle = (TextView) itemView.findViewById(R.id.movie_title);
        }
    }
}
