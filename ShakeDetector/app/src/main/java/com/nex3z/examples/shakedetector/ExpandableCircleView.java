package com.nex3z.examples.shakedetector;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class ExpandableCircleView extends View {
    private static final String LOG_TAG = ExpandableCircleView.class.getSimpleName();

    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;
    private static final int DEFAULT_COLOR = Color.BLUE;
    private static final int DEFAULT_HEIGHT = 200;
    private static final int DEFAULT_WIDTH = 200;
    private static final float DEFAULT_FILL_PROPORTION = 0.5f;

    private int mBorderColor = DEFAULT_BORDER_COLOR;
    private int mFillColor = DEFAULT_COLOR;
    private float mFillProportion = 0;
    private Paint mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public ExpandableCircleView(Context context) {
        super(context);
        init();
    }

    public ExpandableCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public ExpandableCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ExpandableCircleView,
                0, 0);

        try {
            mBorderColor = a.getColor(
                    R.styleable.ExpandableCircleView_borderColor, DEFAULT_BORDER_COLOR);
            mFillColor = a.getColor(R.styleable.ExpandableCircleView_fillColor, DEFAULT_COLOR);
            mFillProportion = a.getFloat(R.styleable.ExpandableCircleView_fillProportion, DEFAULT_FILL_PROPORTION);
            Log.v(LOG_TAG, "ExpandableCircleView(): mBorderColor = " + mFillColor +
                    ", mFillColor = " + mFillColor + ", mFillProportion = " + mFillProportion);
        } finally {
            a.recycle();
        }

        init();
    }

    private void init() {
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mFillPaint.setColor(mFillColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(DEFAULT_WIDTH, heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSize, DEFAULT_HEIGHT);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();

        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;

        float borderRadius = (width < height ? width : height) / 2;

        float cx = paddingLeft + borderRadius;
        float cy = paddingTop + borderRadius;
        canvas.drawCircle(cx, cy, borderRadius, mBorderPaint);
        canvas.drawCircle(cx, cy, borderRadius * mFillProportion, mFillPaint);
    }

    public float getFillProportion() {
        return mFillProportion;
    }

    public void setFillProportion(float proportion) {
        mFillProportion = proportion;
        invalidate();
    }

}
