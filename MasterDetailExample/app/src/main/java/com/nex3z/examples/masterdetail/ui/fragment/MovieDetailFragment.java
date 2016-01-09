package com.nex3z.examples.masterdetail.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nex3z.examples.masterdetail.R;
import com.nex3z.examples.masterdetail.model.Movie;
import com.nex3z.examples.masterdetail.util.ImageUtility;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class MovieDetailFragment extends Fragment {
    private static final String LOG_TAG = MovieDetailFragment.class.getSimpleName();

    public static final String ARG_MOVIE_INFO = "MOVIE_INFO";

    private ImageView mPoster;
    private TextView mTitle;
    private TextView mReleaseDate;
    private TextView mOverview;
    private Movie mMovie;


    public MovieDetailFragment() { }

    public static MovieDetailFragment newInstance(Serializable serializable) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(MovieDetailFragment.ARG_MOVIE_INFO, serializable);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_MOVIE_INFO)) {
            mMovie = (Movie) getArguments().getSerializable(ARG_MOVIE_INFO);
            Log.v(LOG_TAG, "onCreate(): mMovie = " + mMovie);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);

        mPoster = (ImageView) rootView.findViewById(R.id.detail_poster);
        mTitle = (TextView) rootView.findViewById(R.id.detail_title);
        mReleaseDate = (TextView) rootView.findViewById(R.id.detail_release_date);
        mOverview = (TextView) rootView.findViewById(R.id.detail_overview);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        String url = ImageUtility.getImageUrl(mMovie.getPosterPath());
        Picasso.with(getActivity()).load(url).into(mPoster);

        mTitle.setText(mMovie.getTitle());
        mReleaseDate.setText(mMovie.getReleaseDate());
        mOverview.setText(mMovie.getOverview());

        getActivity().supportStartPostponedEnterTransition();
    }
}
