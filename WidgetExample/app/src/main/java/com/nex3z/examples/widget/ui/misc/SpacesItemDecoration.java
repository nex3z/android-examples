package com.nex3z.examples.widget.ui.misc;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int mSpaceTop;
    private int mSpaceRight;
    private int mSpaceBottom;
    private int mSpaceLeft;

    public SpacesItemDecoration(int space) {
        this.mSpaceTop = space;
        this.mSpaceRight = space;
        this.mSpaceBottom = space;
        this.mSpaceLeft = space;
    }

    public SpacesItemDecoration(int top, int right, int bottom, int left) {
        this.mSpaceTop = top;
        this.mSpaceRight = right;
        this.mSpaceBottom = bottom;
        this.mSpaceLeft = left;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.top = mSpaceTop;
        outRect.right = mSpaceRight;
        outRect.bottom = mSpaceBottom;
        outRect.left = mSpaceLeft;
    }
}