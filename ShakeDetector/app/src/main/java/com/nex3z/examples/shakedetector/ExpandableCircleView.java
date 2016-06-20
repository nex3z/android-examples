package com.nex3z.examples.shakedetector;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ExpandableCircleView extends View {

    private static final int DEFAULT_OUTER_COLOR = Color.BLACK;
    private static final int DEFAULT_INNER_COLOR = Color.BLUE;
    private static final int DEFAULT_HEIGHT = 200;
    private static final int DEFAULT_WIDTH = 200;
    private static final int DEFAULT_EXPAND_ANIMATION_DURATION = 100;
    private static final float DEFAULT_PROGRESS = 0.5f;

    private int mOuterColor = DEFAULT_OUTER_COLOR;
    private int mInnerColor = DEFAULT_INNER_COLOR;
    private float mProgress = 0;
    private Paint mOuterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mInnerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private ObjectAnimator mExpandAnimator;

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
            mOuterColor = a.getColor(R.styleable.ExpandableCircleView_outerColor,
                    DEFAULT_OUTER_COLOR);
            mInnerColor = a.getColor(R.styleable.ExpandableCircleView_innerColor,
                    DEFAULT_INNER_COLOR);
            mProgress = a.getFloat(R.styleable.ExpandableCircleView_progress, DEFAULT_PROGRESS);
        } finally {
            a.recycle();
        }

        init();
    }

    private void init() {
        mOuterPaint.setColor(mOuterColor);
        mOuterPaint.setStyle(Paint.Style.STROKE);
        mInnerPaint.setColor(mInnerColor);

        mExpandAnimator = ObjectAnimator.ofFloat(this, "Progress", 0);
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

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;

        float borderRadius = (width < height ? width : height) / 2;
        float cx = paddingLeft + borderRadius;
        float cy = paddingTop + borderRadius;

        canvas.drawCircle(cx, cy, borderRadius, mOuterPaint);
        canvas.drawCircle(cx, cy, borderRadius * mProgress, mInnerPaint);
    }

    public void expand(float progress) {
        if (mExpandAnimator.isRunning()) {
            mExpandAnimator.cancel();
        }
        mExpandAnimator.setFloatValues(progress);
        mExpandAnimator.setDuration(DEFAULT_EXPAND_ANIMATION_DURATION).start();
    }

    public float getProgress() {
        return mProgress;
    }

    public void setProgress(float progress) {
        mProgress = progress;
        invalidate();
    }

    public int getOuterColor() {
        return mOuterColor;
    }

    public void setOuterColor(int color) {
        mOuterColor = color;
        mOuterPaint.setColor(mOuterColor);
    }

    public int getInnerColor() {
        return mOuterColor;
    }

    public void setInnerColor(int color) {
        mInnerColor = color;
        mInnerPaint.setColor(mInnerColor);
    }

}
