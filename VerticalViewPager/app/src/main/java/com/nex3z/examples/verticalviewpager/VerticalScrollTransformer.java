package com.nex3z.examples.verticalviewpager;

import android.support.v4.view.ViewPager;
import android.view.View;

public class VerticalScrollTransformer implements ViewPager.PageTransformer {

    @Override
    public void transformPage(View view, float position) {
        view.setTranslationX(-position * view.getWidth());
        view.setTranslationY(position * view.getHeight());
    }

}