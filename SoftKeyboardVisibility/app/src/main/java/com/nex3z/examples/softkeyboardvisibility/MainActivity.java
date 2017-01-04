package com.nex3z.examples.softkeyboardvisibility;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int KEYBOARD_MIN_HEIGHT_DP = 100;

    private View mContainer;
    private ViewTreeObserver.OnGlobalLayoutListener mListener;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContainer = getWindow().getDecorView().findViewById(android.R.id.content);
        mListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int rootHeight = mContainer.getRootView().getHeight();
                Rect r = new Rect();
                mContainer.getWindowVisibleDisplayFrame(r);
                Log.v(LOG_TAG, "onGlobalLayout(): r.top = " + r.top
                        + ", r.bottom = " +  r.bottom + ", rootHeight = " + rootHeight);

                if (rootHeight - r.bottom > dpToPx(KEYBOARD_MIN_HEIGHT_DP)) {
                    Log.v(LOG_TAG, "onGlobalLayout(): Keyboard is visible.");
                    showToast(R.string.keyboard_visible);
                } else {
                    Log.v(LOG_TAG, "onGlobalLayout(): Keyboard is gone.");
                    showToast(R.string.keyboard_gone);
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        mContainer.getViewTreeObserver().addOnGlobalLayoutListener(mListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            mContainer.getViewTreeObserver().removeGlobalOnLayoutListener(mListener);
        } else {
            mContainer.getViewTreeObserver().removeOnGlobalLayoutListener(mListener);
        }
    }

    private void showToast(int resId) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, resId, Toast.LENGTH_SHORT);
        mToast.show();
    }

    private float dpToPx(float dp){
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
