package com.nex3z.examples.simplecustomview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class BlockView extends View {
    private static final String LOG_TAG = BlockView.class.getSimpleName();

    private static final int DEFAULT_COLOR = Color.BLUE;
    private static final int DEFAULT_HEIGHT = 200;
    private static final int DEFAULT_WIDTH = 200;

    private int mColor = DEFAULT_COLOR;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float mPreviousX;
    private float mPreviousY;

    public BlockView(Context context) {
        super(context);
        init();
    }

    public BlockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public BlockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.BlockView,
                0, 0);

        try {
            mColor = a.getColor(R.styleable.BlockView_blockColor, DEFAULT_COLOR);
            Log.v(LOG_TAG, "BlockView(): mColor = " + mColor);
        } finally {
            a.recycle();
        }

        init();
    }

    private void init() {
        mPaint.setColor(mColor);
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

        Log.v(LOG_TAG, "onDraw(): mPaint.getColor() = " + mPaint.getColor());
        canvas.drawRect(paddingLeft, paddingTop, paddingLeft + width, paddingTop + height, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int MAX_ALPHA = 255;
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE: {
                float dx = x - mPreviousX;
                float dy = y - mPreviousY;
                if (Math.abs(dx) >= Math.abs(dy)) {
                    int newAlpha = Color.alpha(mColor) + (int) (dx / getWidth() * MAX_ALPHA);
                    if (newAlpha < 0) {
                        newAlpha = 0;
                    } else if (newAlpha > MAX_ALPHA) {
                        newAlpha = MAX_ALPHA;
                    }
                    Log.v(LOG_TAG, "onTouchEvent(): ACTION_MOVE newAlpha = " + newAlpha);
                    mColor = Color.argb(
                            newAlpha,
                            Color.red(mColor), Color.green(mColor), Color.blue(mColor));
                    setBlockColor(mColor);
                }
            }
            default: {
                break;
            }
        }

        mPreviousX = x;
        mPreviousY = y;

        return true;
    }

    public int getBlockColor() {
        return mColor;
    }

    public void setBlockColor(int color) {
        Log.v(LOG_TAG, "setBlockColor(): color = " + color);
        mColor = color;
        mPaint.setColor(mColor);
        invalidate();
    }
}
