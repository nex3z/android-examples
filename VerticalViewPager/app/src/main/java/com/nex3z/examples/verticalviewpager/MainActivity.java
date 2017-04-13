package com.nex3z.examples.verticalviewpager;

import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int PAGE_COUNT = 5;

    private VerticalViewPager mPager;
    private RadioGroup mRgPages;
    private List<Fragment> mFragments = new ArrayList<>();
    private boolean mProtectFromCheckedState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPager();
        initRadioGroup();
    }

    private void initPager() {
        for (int i = 0; i < PAGE_COUNT; i++) {
            Fragment fragment = NumberFragment.newInstance(i);
            mFragments.add(fragment);
        }

        mPager = (VerticalViewPager) findViewById(R.id.vp_pager);
        mPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), mFragments));
        mPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                Log.v(LOG_TAG, "onPageSelected(): position = " + position);
                mProtectFromCheckedState = true;
                ((RadioButton) mRgPages.getChildAt(position)).setChecked(true);
                mProtectFromCheckedState = false;
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void initRadioGroup() {
        mRgPages = (RadioGroup) findViewById(R.id.rg_page);
        mRgPages.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (mProtectFromCheckedState) {
                    return;
                }
                int page = group.indexOfChild(group.findViewById(checkedId));
                Log.v(LOG_TAG, "onCheckedChanged(): checkedId = " + checkedId + ", page = " + page);
                mPager.setCurrentItem(page);
            }
        });

        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.MATCH_PARENT, (int) dpToPx(40));
        for (int i = 0; i < PAGE_COUNT; i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(String.format(getString(R.string.page), String.valueOf(i)));
            radioButton.setTextColor(ContextCompat.getColorStateList(this,
                    R.color.selector_page_button_text));
            radioButton.setBackgroundResource(R.drawable.selector_bg_page_button);
            radioButton.setLayoutParams(params);
            radioButton.setButtonDrawable(new StateListDrawable());
            radioButton.setGravity(Gravity.CENTER);
            mRgPages.addView(radioButton);
        }

        ((RadioButton) mRgPages.getChildAt(0)).setChecked(true);
    }


    private class FragmentAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragments;

        FragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

    private float dpToPx(float dp){
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

}
