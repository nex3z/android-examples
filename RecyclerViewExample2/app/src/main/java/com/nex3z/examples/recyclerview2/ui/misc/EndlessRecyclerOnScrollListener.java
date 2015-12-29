package com.nex3z.examples.recyclerview2.ui.misc;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {

    public static String LOG_TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    private final int VISIBLE_THRESHOLD = 10;

    private int previousTotal = 0;
    private boolean loading = true;
    private int currentPage = 1;
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

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem + VISIBLE_THRESHOLD)) {

            currentPage++;
            onLoadMore(currentPage);

            loading = true;
        }
    }

    public abstract void onLoadMore(int currentPage);
}