package com.nex3z.examples.masterdetail.ui.misc;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    public static String LOG_TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    private final int VISIBLE_THRESHOLD = 10;

    private int mPreviousTotal = 0;
    private boolean mLoading = true;
    private int mCurrentPage = 1;
    private LinearLayoutManager mLinearLayoutManager;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = recyclerView.getChildCount();
        int totalItemCount = mLinearLayoutManager.getItemCount();
        int firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        if (mLoading) {
            if (totalItemCount > mPreviousTotal) {
                mLoading = false;
                mPreviousTotal = totalItemCount;
            }
        }
        if (!mLoading &&
                (totalItemCount - visibleItemCount) <= (firstVisibleItem + VISIBLE_THRESHOLD)) {

            mCurrentPage++;
            onLoadMore(mCurrentPage);

            mLoading = true;
        }
    }

    public abstract void onLoadMore(int currentPage);
}